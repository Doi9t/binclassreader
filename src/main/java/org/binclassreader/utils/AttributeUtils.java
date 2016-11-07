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

    private static Class<?> getAttributeWithName(String name) {

        Class<?> clazz = null;

        if ("AnnotationDefault".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("BootstrapMethods".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("ConstantValue".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("Deprecated".equalsIgnoreCase(name)) {
            clazz = DeprecatedAttr.class;
        } else if ("EnclosingMethod".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("Exceptions".equalsIgnoreCase(name)) {
            clazz = ExceptionAttr.class;
        } else if ("InnerClasses".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("LineNumberTable".equalsIgnoreCase(name)) {
            clazz = LineNumberTableAttr.class;
        } else if ("LocalVariableTable".equalsIgnoreCase(name)) {
            clazz = LocalVariableTableAttr.class;
        } else if ("LocalVariableTypeTable".equalsIgnoreCase(name)) {
            clazz = LocalVariableTypeTableAttr.class;
        } else if ("MethodParameters".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("RuntimeInvisibleAnnotations".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("RuntimeInvisibleParameterAnnotations".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("RuntimeInvisibleTypeAnnotations".equalsIgnoreCase(name)) {
            clazz = RuntimeInvisibleTypeAnnotationsAttr.class;
        } else if ("RuntimeVisibleAnnotations".equalsIgnoreCase(name)) {
            clazz = RuntimeVisibleAnnotationsAttr.class;
        } else if ("RuntimeVisibleParameterAnnotations".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("RuntimeVisibleTypeAnnotations".equalsIgnoreCase(name)) {
            clazz = RuntimeVisibleTypeAnnotationsAttr.class;
        } else if ("Signature".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("SourceDebugExtension".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("SourceFile".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("StackMapTable".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        } else if ("Synthetic".equalsIgnoreCase(name)) {
            clazz = UnimplementedAttr.class;
        }

        return clazz;
    }

    public static AbstractAttribute getNextAttribute(ClassReader reader) throws IOException, IllegalAccessException, InstantiationException {
        AbstractAttribute value = null;

        short[] rawNameIndexAttribute = reader.readFromCurrentStream(2);
        int nameIndexAttribute = BaseUtils.combineBytesToInt(rawNameIndexAttribute);

        Map<Integer, Object> constPool = reader.getPoolByClass(PoolParser.class);
        Object objFromPool = constPool.get(nameIndexAttribute - 1);

        Class<?> attributeByName = null;

        if (objFromPool != null) {
            attributeByName = getAttributeWithName(((ConstUtf8Info) objFromPool).getAsNewString());
        }

        if (attributeByName != null) {
            value = (AbstractAttribute) reader.read(attributeByName.newInstance(), rawNameIndexAttribute);
        }

        return value;
    }

}
