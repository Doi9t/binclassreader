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
import org.binclassreader.enums.CollectionType;
import org.binclassreader.parsers.*;
import org.binclassreader.structs.ConstAttributeInfo;
import org.binclassreader.structs.ConstClassInfo;
import org.binclassreader.structs.ConstFieldInfo;
import org.binclassreader.structs.ConstMethodInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Yannick on 4/17/2016.
 */

@PoolDataOptions(storageType = CollectionType.NONE)
public class ClassMappingService extends AbstractPoolData {
    private static ClassMappingService ourInstance = new ClassMappingService();

    public static ClassMappingService getInstance() {
        return ourInstance;
    }

    private ClassMappingService() {
    }

    /*
     * Order:
     * InterfacesParser
     * FieldsParser
     * MethodsParser
     * AttributesParser
     */
    public void generateTree() {
        Map<Integer, Object> constPool = getPoolByClass(PoolParser.class);

        if (constPool == null || constPool.isEmpty()) {
            return;
        }

        List<Object> interfacePool = getPoolByClass(InterfacesParser.class);
        List<Object> fieldPool = getPoolByClass(FieldsParser.class);
        List<Object> methodPool = getPoolByClass(MethodsParser.class);
        List<Object> attributePool = getPoolByClass(AttributesParser.class);

        if (interfacePool != null) {
            for (Object interfaceObj : interfacePool) {
                if (interfaceObj != null && interfaceObj instanceof ConstClassInfo) {
                    System.out.println(interfaceObj);
                }
            }
        }

        if (fieldPool != null) {
            for (Object fieldObj : fieldPool) {
                if (fieldObj != null && fieldObj instanceof ConstFieldInfo) {
                    System.out.println(fieldObj);
                }
            }
        }

        if (methodPool != null) {
            for (Object methodeObj : methodPool) {
                if (methodeObj != null && methodeObj instanceof ConstMethodInfo) {
                    System.out.println(methodeObj);
                }
            }
        }

        if (attributePool != null) {
            for (Object attributeObj : attributePool) {
                if (attributeObj != null && attributeObj instanceof ConstAttributeInfo) {
                    System.out.println(attributeObj);
                }
            }
        }

    }
}
