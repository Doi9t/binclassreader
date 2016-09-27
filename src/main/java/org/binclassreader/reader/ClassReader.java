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

import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.enums.PoolTypeEnum;
import org.binclassreader.interfaces.SelfReader;
import org.binclassreader.pojos.FieldPojo;
import org.binclassreader.services.ClassMappingService;
import org.binclassreader.utils.Assert;
import org.binclassreader.utils.Utilities;
import org.multiarraymap.MultiArrayMap;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Yannick on 1/25/2016.
 */
public class ClassReader {

    private static final ClassMappingService classMappingServiceInstance;

    static {
        classMappingServiceInstance = ClassMappingService.getInstance();
    }

    private Map<Short, FieldPojo> fieldSorter;
    private InputStream classData;
    private Map<Class<?>, Object> sections;
    private MultiArrayMap<PoolTypeEnum, Object> pool;

    public ClassReader(InputStream classData, Class<?>... classSections) {
        if (!Assert.isArrayReadable(classSections) || classData == null) {
            return;
        }

        this.classData = classData;
        fieldSorter = new TreeMap<Short, FieldPojo>();
        sections = this.read(Utilities.createNewArrayOfObject(classSections));
        pool = classMappingServiceInstance.generateTree();
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

    public <T> T read(T obj) {
        fieldSorter.clear();

        if (obj == null) {
            return null;
        }

        Class<?> currentClass = obj.getClass();

        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation instanceof BinClassParser) {
                        BinClassParser anno = (BinClassParser) annotation;
                        fieldSorter.put(anno.readOrder(), new FieldPojo(field, anno.byteToRead()));
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        if (!fieldSorter.isEmpty()) {
            for (FieldPojo value : fieldSorter.values()) {
                Field fieldToWrite = value.getFieldToWrite();
                fieldToWrite.setAccessible(true);

                try {
                    byte nbByteToRead = value.getNbByteToRead();
                    short[] bytes = readFromCurrentStream(nbByteToRead);
                    fieldToWrite.set(obj, bytes);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (obj instanceof SelfReader) {
                ((SelfReader) obj).initReading(this);
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
        return classMappingServiceInstance.getConstPool();
    }
}
