/*
 *    Copyright 2014 - 2016 Yannick Watier
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.binclassreader.reader;

import org.binclassreader.abstracts.AbstractPoolData;
import org.binclassreader.abstracts.Readable;
import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.annotations.PoolDataOptions;
import org.binclassreader.annotations.PoolItemIndex;
import org.binclassreader.enums.CollectionTypeEnum;
import org.binclassreader.enums.PoolTypeEnum;
import org.binclassreader.parsers.*;
import org.binclassreader.pojos.FieldPojo;
import org.binclassreader.structs.ConstAttributeInfo;
import org.binclassreader.structs.ConstClassInfo;
import org.binclassreader.structs.ConstFieldInfo;
import org.binclassreader.structs.ConstMethodInfo;
import org.binclassreader.tree.Tree;
import org.binclassreader.tree.TreeElement;
import org.binclassreader.utils.Assert;
import org.binclassreader.utils.BaseUtils;
import org.multiarraymap.MultiArrayMap;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Yannick on 1/25/2016.
 */

@PoolDataOptions(storageType = CollectionTypeEnum.NONE)
public class ClassReader extends AbstractPoolData {

    private Map<Short, FieldPojo> fieldSorter;
    private InputStream classData;
    private Map<Class<?>, Object> sections;
    private Map<Integer, Object> constPool;
    private MultiArrayMap<PoolTypeEnum, Object> pool;


    public ClassReader(InputStream classData, Class<?>... classSections) {
        if (!Assert.isArrayReadable(classSections) || classData == null) {
            return;
        }

        this.classData = classData;
        fieldSorter = new TreeMap<Short, FieldPojo>();
        sections = this.read(BaseUtils.createNewArrayOfObject(classSections));
        pool = generateTree();
    }

    public Map<Class<?>, Object> read(Object... type) {
        Map<Class<?>, Object> values = new HashMap<Class<?>, Object>();
        if (type != null) {
            for (Object obj : type) {
                if (obj == null) {
                    continue;
                }
                values.put(obj.getClass(), read(obj));
            }
        }

        return values;
    }

    /**
     * @param obj
     * @param values - This parameter is only used when there's a manual value (set via {@link org.binclassreader.annotations.BinClassParser#manualMode()}))
     * @return
     */
    public <T> T read(T obj, short[]... values) {
        fieldSorter.clear();

        if (obj == null) {
            return null;
        }

        Class<?> currentClass = obj.getClass();

        //Fetch all readable fields (from child to top parent)
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation instanceof BinClassParser) {
                        BinClassParser binClassParser = (BinClassParser) annotation;
                        fieldSorter.put(binClassParser.readOrder(), new FieldPojo(field, binClassParser.byteToRead(), binClassParser.manualMode()));
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        if (!fieldSorter.isEmpty()) { //Sort the fields
            short manualInx = 0;
            for (FieldPojo value : fieldSorter.values()) {
                Field fieldToWrite = value.getFieldToWrite();
                boolean isManual = value.isManualMode();

                if (isManual && values == null || isManual && values.length == 0) {
                    continue;
                }

                fieldToWrite.setAccessible(true);

                try {
                    byte nbByteToRead = value.getNbByteToRead();

                    short[] bytes = (isManual) ? values[manualInx++] : readFromCurrentStream(nbByteToRead);
                    fieldToWrite.set(obj, bytes);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (obj instanceof Readable) {
                Readable readable = (Readable) obj;

                if (readable.isEventsEnabled()) {
                    readable.afterFieldsInitialized(this);
                }
            }
        }

        return obj;
    }

    /**
     * @param nbByteToRead
     * @return An array of positives bytes (as short). The values will be -1 for the overflowing values.
     * @throws IOException
     */
    public short[] readFromCurrentStream(int nbByteToRead) throws IOException {

        if (nbByteToRead <= 0) {
            return null;
        }

        byte[] buffer = new byte[nbByteToRead];
        short[] shortBuffer = new short[nbByteToRead];

        int read = classData.read(buffer);

        for (int i = 0; i < nbByteToRead; i++) {
            shortBuffer[i] = (i < read) ? (short) ((short) buffer[i] & 0xFF) : -1;
        }

        return shortBuffer;
    }

    public MultiArrayMap<PoolTypeEnum, Object> generateTree() {

        constPool = getPoolByClass(PoolParser.class);

        if (constPool == null || constPool.isEmpty()) {
            return null;
        }

        MultiArrayMap<PoolTypeEnum, Object> values = new MultiArrayMap<PoolTypeEnum, Object>();

        iterateOnPool((List<Object>) getPoolByClass(InterfacesParser.class), ConstClassInfo.class, PoolTypeEnum.INTERFACE, values);
        iterateOnPool((List<Object>) getPoolByClass(FieldsParser.class), ConstFieldInfo.class, PoolTypeEnum.FIELD, values);
        iterateOnPool((List<Object>) getPoolByClass(MethodsParser.class), ConstMethodInfo.class, PoolTypeEnum.METHOD, values);
        iterateOnPool((List<Object>) getPoolByClass(AttributesParser.class), ConstAttributeInfo.class, PoolTypeEnum.ATTRIBUTE, values);

        return values;
    }

    private void iterateOnPool(List<Object> interfacePool, Class<?> mustBeOfType, PoolTypeEnum poolTypeEnum, MultiArrayMap<PoolTypeEnum, Object> data) {
        if (interfacePool != null) {
            for (Object interfaceObj : interfacePool) {
                if (interfaceObj != null && interfaceObj.getClass().equals(mustBeOfType)) {
                    TreeElement rootTreeElement = new TreeElement(interfaceObj);
                    Tree tree = buildTree(poolTypeEnum, new Tree(rootTreeElement), rootTreeElement);
                    data.put(poolTypeEnum, tree);

                    if (interfaceObj instanceof Readable) {
                        Readable readable = (Readable) interfaceObj;
                        if (readable.isEventsEnabled()) {
                            readable.afterTreeIsBuilt(tree);
                        }
                    }
                }
            }
        }
    }

    private Tree buildTree(PoolTypeEnum poolTypeEnum, Tree tree, TreeElement currentTreeElement) {

        if (tree == null || currentTreeElement == null) {
            return null;
        }

        Object currentObj = currentTreeElement.getCurrent();
        Method[] declaredMethods = currentObj.getClass().getDeclaredMethods();

        if (declaredMethods != null) {
            for (Method declaredMethod : declaredMethods) {
                PoolItemIndex annotation = declaredMethod.getAnnotation(PoolItemIndex.class);

                if (annotation != null) {
                    try {

                        List<? extends Class<?>> mustBeOfTypeArr = Arrays.asList(annotation.mustBeOfType());
                        Integer invoke = (Integer) declaredMethod.invoke(currentObj);

                        if (invoke == null || invoke < 1 || invoke > (constPool.size() - 1)) {
                            continue;
                        }

                        Object child = constPool.get((invoke - 1));

                        if (child instanceof ConstClassInfo && PoolTypeEnum.INTERFACE.equals(poolTypeEnum)) {
                            child = constPool.get(((ConstClassInfo) child).getNameIndex() - 1); //Get the ConstUtf8Info from The ConstClassInfo
                        }

                        if (!mustBeOfTypeArr.contains(child.getClass())) {
                            break;
                        }

                        TreeElement childTreeElement = new TreeElement(child, annotation.type());
                        currentTreeElement.addChildren(childTreeElement);
                        childTreeElement.addParent(currentTreeElement);

                        buildTree(poolTypeEnum, tree, childTreeElement);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return tree;
    }

    public long skipFromCurrentStream(int nbByteToSkip) throws IOException {
        if (nbByteToSkip <= 0) {
            return 0;
        }
        return classData.skip(nbByteToSkip);
    }

    public int readFromCurrentStream() throws IOException {
        return classData.read();
    }

    public Map<Class<?>, Object> getSections() {
        return sections;
    }

    public MultiArrayMap<PoolTypeEnum, Object> getMappedPool() {
        return pool;
    }

    public Map<Integer, Object> getConstPool() {
        return constPool;
    }
}
