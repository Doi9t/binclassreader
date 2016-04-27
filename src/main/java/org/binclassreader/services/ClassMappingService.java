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
import org.binclassreader.parsers.PoolParser;

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

    public Object getItemFromConstantPool(int idx) {
        return ((Map<Object, Object>) getPoolByClass(PoolParser.class)).get(idx);
    }


    /*
     * Order:
     * InterfacesParser
     * FieldsParser
     * MethodsParser
     * AttributesParser
     */
    public void generateTree() {

        Map<Class<?>, Object> allPools = getAllPools();

        for (Object o : allPools.values()) {
            System.out.println(o);
        }
/*
        List<Object> interfacePool = getPoolByClass(InterfacesParser.class);
        List<Object> fieldPool = getPoolByClass(FieldsParser.class);
        List<Object> methodPool = getPoolByClass(MethodsParser.class);
        List<Object> attributePool = getPoolByClass(AttributesParser.class);

        if (interfacePool != null) {
            for (Object interfaceObj : interfacePool) {
                System.out.println(interfaceObj);
            }
        }

        if (fieldPool != null) {
            for (Object fieldObj : fieldPool) {
                System.out.println(fieldObj);
            }
        }

        if (methodPool != null) {
            for (Object methodeObj : methodPool) {
                System.out.println(methodeObj);
            }
        }

        if (attributePool != null) {
            for (Object attributeObj : attributePool) {
                System.out.println(attributeObj);
            }
        }
*/
    }
}
