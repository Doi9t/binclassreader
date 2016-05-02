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

package org.binclassreader.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Yannick on 4/13/2016.
 */
public enum CollectionType {
    LIST, MAP, SET, NONE;

    public static <T> T getCollectionByEnum(CollectionType type) {
        T t = null;

        if (type == null) {
            return null;
        }

        switch (type) {
            case MAP:
                t = (T) new HashMap<Object, Object>();
                break;
            case SET:
                t = (T) new HashSet<Object>();
                break;
            default:
                t = (T) new ArrayList<Object>();
                break;
        }

        return t;
    }

    public boolean isCollection() {
        return CollectionType.LIST.equals(this) || CollectionType.SET.equals(this);
    }
}
