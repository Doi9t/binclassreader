/*
 *    Copyright 2014 - 2017 Yannick Watier
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

package ca.watier.binclassreader.services;

import ca.watier.binclassreader.enums.ClassHelperEnum;
import ca.watier.binclassreader.enums.PoolTypeEnum;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.structs.*;
import ca.watier.binclassreader.tree.Tree;
import ca.watier.binclassreader.tree.TreeElement;
import ca.watier.binclassreader.utils.BaseUtils;
import ca.watier.binclassreader.utils.KeyValueHolder;
import ca.watier.multiarraymap.MultiArrayMap;

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
        ConstSuperClassInfo superClassInfo = (ConstSuperClassInfo) sections.get(ConstSuperClassInfo.class);

        Object obj = constPool.get(superClassInfo.getIndex() - 1);
        ConstUtf8Info value = null;

        if (obj instanceof ConstUtf8Info) {
            value = (ConstUtf8Info) obj;
        } else if (obj instanceof ConstClassInfo) {
            ConstClassInfo constClassInfo = (ConstClassInfo) obj;
            value = (ConstUtf8Info) constPool.get(constClassInfo.getNameIndex() - 1);
        }

        return (value != null) ? value.getAsNewString() : "";
    }

    public static List<KeyValueHolder<ClassHelperEnum, Object>> getFields() {
        List<KeyValueHolder<ClassHelperEnum, Object>> values = new ArrayList<KeyValueHolder<ClassHelperEnum, Object>>();

        if (mappedPool != null) {
            for (Object o : BaseUtils.safeList(mappedPool.get(PoolTypeEnum.FIELD))) {
                if (o instanceof Tree) {

                    TreeElement element = ((Tree) o).getRoot();
                    List<TreeElement> child = element.getChild();

                    ConstFieldInfo current = (ConstFieldInfo) element.getCurrent();

                    TreeElement treeElementZero = child.get(0);
                    TreeElement treeElementOne = child.get(1);
                    Object nameValue = null, descriptorValue = null;

                    if (ClassHelperEnum.NAME.equals(treeElementZero.getMappingType())) {
                        nameValue = treeElementZero;
                        descriptorValue = treeElementOne;
                    } else {
                        nameValue = treeElementOne;
                        descriptorValue = treeElementZero;
                    }

                    KeyValueHolder<ClassHelperEnum, Object> value = new KeyValueHolder<ClassHelperEnum, Object>();

                    value.addPair(ClassHelperEnum.ACCESS_FLAGS, current.getAccessFlags());
                    value.addPair(ClassHelperEnum.NAME, nameValue);
                    value.addPair(ClassHelperEnum.DESCRIPTOR, descriptorValue);
                    value.addPair(ClassHelperEnum.OTHER_ATTR, current.getAttributesList());

                    values.add(value);
                }
            }
        }

        return values;
    }


    public static List<KeyValueHolder<ClassHelperEnum, Object>> getMethods(boolean keepSpecial) {
        List<KeyValueHolder<ClassHelperEnum, Object>> values = new ArrayList<KeyValueHolder<ClassHelperEnum, Object>>();

        if (mappedPool != null) {
            for (Object o : BaseUtils.safeList(mappedPool.get(PoolTypeEnum.METHOD))) {
                if (o instanceof Tree) {
                    TreeElement element = ((Tree) o).getRoot();
                    List<TreeElement> child = element.getChild();

                    KeyValueHolder<ClassHelperEnum, Object> value = new KeyValueHolder<ClassHelperEnum, Object>();

                    if (child.size() > 1) {

                        ConstMethodInfo currentElement = (ConstMethodInfo) element.getCurrent();

                        if (currentElement.isSpecialMethod() && !keepSpecial) {
                            continue;
                        }

                        TreeElement treeElementZero = child.get(0);
                        TreeElement treeElementOne = child.get(1);

                        TreeElement nameValue = null, descriptorValue = null;

                        if (ClassHelperEnum.NAME.equals(treeElementZero.getMappingType())) {
                            nameValue = treeElementZero;
                            descriptorValue = treeElementOne;
                        } else {
                            nameValue = treeElementOne;
                            descriptorValue = treeElementZero;
                        }

                        value.addPair(ClassHelperEnum.ACCESS_FLAGS, currentElement.getAccessFlags());
                        value.addPair(ClassHelperEnum.NAME, nameValue.getCurrent());
                        value.addPair(ClassHelperEnum.DESCRIPTOR, descriptorValue.getCurrent());
                        value.addPair(ClassHelperEnum.CODE_ATTR, currentElement.getCodeAttr());

                        values.add(value);
                    }
                }
            }
        }

        return values;
    }

    public static List<String> getInterfaces() {

        List<String> values = new ArrayList<String>();

        if (mappedPool != null) {
            for (Object o : BaseUtils.safeList(mappedPool.get(PoolTypeEnum.INTERFACE))) {
                if (o instanceof Tree) {
                    TreeElement element = ((Tree) o).getRoot();
                    TreeElement child = element.getChild().get(0);

                    values.add(((ConstUtf8Info) child.getCurrent()).getAsNewString());
                }
            }
        }

        return values;
    }

    public static String getClassName() {
        ConstUtf8Info info = null;

        ConstThisClassInfo superClassInfo = (ConstThisClassInfo) sections.get(ConstThisClassInfo.class);
        Object o = constPool.get(superClassInfo.getIndex() - 1);

        if (o instanceof ConstUtf8Info) {
            info = (ConstUtf8Info) o;
        } else if (o instanceof ConstClassInfo) {
            info = (ConstUtf8Info) constPool.get(((ConstClassInfo) o).getNameIndex() - 1);//Get the ConstUtf8Info from The ConstClassInfo
        }
        return (info != null) ? info.getAsNewString() : "";
    }
}
