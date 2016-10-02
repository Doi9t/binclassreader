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

package org.binclassreader.services;

import org.binclassreader.enums.ClassHelperEnum;
import org.binclassreader.enums.PoolTypeEnum;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.structs.ConstClassInfo;
import org.binclassreader.structs.ConstFieldInfo;
import org.binclassreader.structs.ConstThisClassInfo;
import org.binclassreader.structs.ConstUtf8Info;
import org.binclassreader.tree.Tree;
import org.binclassreader.tree.TreeElement;
import org.binclassreader.utils.KeyValueHolder;
import org.multiarraymap.MultiArrayMap;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yannick on 9/26/2016.
 */
public class ClassHelperService {

    private static ClassReader classReader;
    private static MultiArrayMap<PoolTypeEnum, Object> mappedPool;
    private static Map<Class<?>, Object> sections;
    private static Map<Integer, Object> constPool;

    private ClassHelperService() {
    }

    public static void loadClass(InputStream io) {

        if (io == null) {
            return;
        }

        ClassReadingService.readNewClass(io);
        List<ClassReader> readerList = ClassReadingService.getReaderList();

        if (!readerList.isEmpty()) {
            classReader = readerList.get(0);
            mappedPool = classReader.getMappedPool();
            sections = classReader.getSections();
            constPool = classReader.getConstPool();
        }
    }

    public static Map<Integer, Object> getConstPool() {
        return constPool;
    }

    public static String getSuperClassName() {
        ConstThisClassInfo thisClassInfo = (ConstThisClassInfo) sections.get(ConstThisClassInfo.class);

        ConstClassInfo ConstClassInfoSuperClass = (ConstClassInfo) constPool.get(thisClassInfo.getIndex());
        ConstUtf8Info constUtf8InfoSuperClassName = (ConstUtf8Info) constPool.get(ConstClassInfoSuperClass.getNameIndex() - 1);

        return constUtf8InfoSuperClassName.getAsNewString();
    }

    public static List<KeyValueHolder<ClassHelperEnum, Object>> getFields() {
        List<KeyValueHolder<ClassHelperEnum, Object>> values = new ArrayList<KeyValueHolder<ClassHelperEnum, Object>>();

        List<Object> fieldList = mappedPool.get(PoolTypeEnum.FIELD);
        if (fieldList != null) {
            for (Object o : fieldList) {
                if (o instanceof Tree) {
                    TreeElement element = ((Tree) o).getRoot();
                    List<TreeElement> child = element.getChild();

                    KeyValueHolder<ClassHelperEnum, Object> value = new KeyValueHolder<ClassHelperEnum, Object>();
                    value.addPair(ClassHelperEnum.ACCESS_FLAGS, ((ConstFieldInfo) element.getCurrent()).getAccessFlags());
                    value.addPair(ClassHelperEnum.NAME, child.get(0).getCurrent());
                    value.addPair(ClassHelperEnum.DESCRIPTOR, child.get(1).getCurrent());

                    values.add(value);
                }
            }
        }

        return values;
    }


    public static List<KeyValueHolder<ClassHelperEnum, ConstUtf8Info>> getMethods() {
        List<KeyValueHolder<ClassHelperEnum, ConstUtf8Info>> values = new ArrayList<KeyValueHolder<ClassHelperEnum, ConstUtf8Info>>();

        List<Object> methodList = mappedPool.get(PoolTypeEnum.METHOD);

        if (methodList != null) {
            for (Object o : methodList) {
                if (o instanceof Tree) {
                    TreeElement element = ((Tree) o).getRoot();
                    List<TreeElement> child = element.getChild();

                    KeyValueHolder<ClassHelperEnum, ConstUtf8Info> value = new KeyValueHolder<ClassHelperEnum, ConstUtf8Info>();
                    value.addPair(ClassHelperEnum.NAME, (ConstUtf8Info) child.get(0).getCurrent());
                    value.addPair(ClassHelperEnum.DESCRIPTOR, (ConstUtf8Info) child.get(1).getCurrent());
                    values.add(value);
                }
            }
        }

        return values;
    }

    public static List<String> getInterfaces() {

        List<String> values = new ArrayList<String>();

        List<Object> interfacesList = mappedPool.get(PoolTypeEnum.INTERFACE);
        if (interfacesList != null) {
            for (Object o : interfacesList) {
                if (o instanceof Tree) {
                    TreeElement element = ((Tree) o).getRoot();
                    TreeElement child = element.getChild().get(0);

                    values.add(((ConstUtf8Info) child.getCurrent()).getAsNewString());
                }
            }
        }

        return values;
    }
}
