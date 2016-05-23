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

import org.binclassreader.abstracts.AbstractPoolData;
import org.binclassreader.annotations.PoolDataOptions;
import org.binclassreader.annotations.PoolItemIndex;
import org.binclassreader.enums.CollectionTypeEnum;
import org.binclassreader.enums.PoolTypeEnum;
import org.binclassreader.parsers.*;
import org.binclassreader.structs.ConstAttributeInfo;
import org.binclassreader.structs.ConstClassInfo;
import org.binclassreader.structs.ConstFieldInfo;
import org.binclassreader.structs.ConstMethodInfo;
import org.binclassreader.tree.Tree;
import org.binclassreader.tree.TreeElement;
import org.multiarraymap.MultiArrayMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Yannick on 4/17/2016.
 */

@PoolDataOptions(storageType = CollectionTypeEnum.NONE)
public class ClassMappingService extends AbstractPoolData {
    private static ClassMappingService ourInstance = new ClassMappingService();
    private Map<Integer, Object> constPool;

    private ClassMappingService() {
    }

    public static ClassMappingService getInstance() {
        return ourInstance;
    }

    public MultiArrayMap generateTree() {

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

    private void iterateOnPool(List<Object> interfacePool, Class<?> mustBeOfType, PoolTypeEnum anInterface, MultiArrayMap data) {
        if (interfacePool != null) {
            for (Object interfaceObj : interfacePool) {
                if (interfaceObj != null && interfaceObj.getClass().equals(mustBeOfType)) {
                    TreeElement rootTreeElement = new TreeElement(interfaceObj);
                    data.put(anInterface, buildTree(new Tree(rootTreeElement), rootTreeElement));
                }
            }
        }
    }

    private Tree buildTree(Tree tree, TreeElement currentTreeElement) {

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

                        List<Class<?>> mustBeOfTypeArr = Arrays.asList(annotation.mustBeOfType());
                        Integer invoke = (Integer) declaredMethod.invoke(currentObj);

                        if (invoke == null || invoke < 1 || invoke > (constPool.size() - 1)) {
                            continue;
                        }

                        Object child = constPool.get((invoke - 1));

                        if (!mustBeOfTypeArr.contains(child.getClass())) {
                            break;
                        }

                        TreeElement childTreeElement = new TreeElement(child);
                        currentTreeElement.addChildren(childTreeElement);
                        childTreeElement.addParent(currentTreeElement);

                        buildTree(tree, childTreeElement);
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
}
