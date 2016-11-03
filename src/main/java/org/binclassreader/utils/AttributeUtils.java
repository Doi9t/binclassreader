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

package org.binclassreader.utils;

import org.binclassreader.abstracts.AbstractAttribute;
import org.binclassreader.attributes.*;
import org.binclassreader.parsers.PoolParser;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.structs.ConstUtf8Info;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Yannick on 11/2/2016.
 */
public class AttributeUtils extends BaseUtils {

    private static Class<?> getNextAttribute(String name) {

        Class<?> clazz = null;

        if ("LineNumberTable".equalsIgnoreCase(name)) {
            clazz = LineNumberTableAttr.class;
        } else if ("LocalVariableTable".equalsIgnoreCase(name)) {
            clazz = LocalVariableTableAttr.class;
        } else if ("LocalVariableTypeTable".equalsIgnoreCase(name)) {
            clazz = LocalVariableTypeTableAttr.class;
        } else if ("RuntimeInvisibleTypeAnnotations".equalsIgnoreCase(name)) {
            clazz = RuntimeInvisibleTypeAnnotationsAttr.class;
        } else if ("RuntimeVisibleTypeAnnotations".equalsIgnoreCase(name)) {
            clazz = RuntimeVisibleTypeAnnotationsAttr.class;
        } else if ("RuntimeVisibleAnnotations".equalsIgnoreCase(name)) {
            clazz = RuntimeVisibleAnnotationsAttr.class;
        } else if ("StackMapTable".equalsIgnoreCase(name)) {
            clazz = StackMapTableAttr.class;
        } else if ("Deprecated".equalsIgnoreCase(name)) {
            clazz = DeprecatedAttr.class;
        }

        return clazz;
    }

    public static AbstractAttribute getNextAttribute(ClassReader reader) throws IOException, IllegalAccessException, InstantiationException {
        Object value = null;

        short[] rawNameIndexAttribute = reader.readFromCurrentStream(2);
        int nameIndexAttribute = BaseUtils.combineBytesToInt(rawNameIndexAttribute);

        Map<Integer, Object> constPool = reader.getPoolByClass(PoolParser.class);
        Object objFromPool = constPool.get(nameIndexAttribute - 1);
        Class<?> attributeByName = getNextAttribute(((ConstUtf8Info) objFromPool).getAsNewString());

        if (attributeByName != null) {
            value = reader.read(attributeByName.newInstance(), rawNameIndexAttribute);
        }

        return (AbstractAttribute) value;
    }

}
