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

package org.binclassreader.abstracts;

import org.binclassreader.annotations.PoolDataOptions;
import org.binclassreader.enums.CollectionType;

import java.util.*;

/**
 * Created by Yannick on 4/12/2016.
 */

public abstract class AbstractPoolData {

    private static final ThreadLocal POOL;

    static {
        POOL = new ThreadLocal<Map<Class<?>, Object>>() {
            @Override
            protected Map<Class<?>, Object> initialValue() {
                return Collections.synchronizedMap(new HashMap<Class<?>, Object>());
            }
        };
    }

    protected AbstractPoolData() {
        Class<? extends AbstractPoolData> clazz = getClass();
        ((Map<Class<?>, Object>) POOL.get()).put(clazz, CollectionType.getCollectionByEnum(clazz.getAnnotation(PoolDataOptions.class).storageType()));
    }

    protected void addItemToList(Object obj) throws ClassCastException {
        if (obj == null) {
            return;
        }

        ((List<Object>) ((Map<Class<?>, Object>) POOL.get()).get(getClass())).add(obj);
    }

    protected void addItemToSet(Object obj) throws ClassCastException {
        if (obj == null) {
            return;
        }

        ((Set<Object>) ((Map<Class<?>, Object>) POOL.get()).get(getClass())).add(obj);
    }

    protected void addItemToMap(Object key, Object value) throws ClassCastException {
        if (key == null || value == null) {
            return;
        }

        ((Map<Object, Object>) ((Map<Class<?>, Object>) POOL.get()).get(getClass())).put(key, value);
    }

    protected Object getPool() {
        return ((Map<Class<?>, Object>) POOL.get()).get(getClass());
    }

    protected Object getPoolByClass(Class<?> clazz) {
        return ((Map<Class<?>, Object>) POOL.get()).get(clazz);
    }
}
