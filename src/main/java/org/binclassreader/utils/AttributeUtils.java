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
import org.binclassreader.enums.AttributeTypeEnum;
import org.binclassreader.parsers.PoolParser;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.structs.ConstUtf8Info;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Yannick on 11/2/2016.
 */
public class AttributeUtils extends BaseUtils {

    private static Object getAttributeInstanceByName(String name) {

        AttributeTypeEnum attributeTypeEnum = null;
        AbstractAttribute attribute = null;
        Class<?> clazz;

        if ("Code".equalsIgnoreCase(name)) {
            clazz = CodeAttr.class;
            attributeTypeEnum = AttributeTypeEnum.CODE;
        } else if ("Deprecated".equalsIgnoreCase(name)) {
            clazz = DeprecatedAttr.class;
            attributeTypeEnum = AttributeTypeEnum.DEPRECATED;
        } else if ("Exceptions".equalsIgnoreCase(name)) {
            clazz = ExceptionAttr.class;
            attributeTypeEnum = AttributeTypeEnum.EXCEPTIONS;
        } else if ("LineNumberTable".equalsIgnoreCase(name)) {
            clazz = LineNumberTableAttr.class;
            attributeTypeEnum = AttributeTypeEnum.LINE_NUMBER_TABLE;
        } else if ("LocalVariableTable".equalsIgnoreCase(name)) {
            clazz = LocalVariableTableAttr.class;
            attributeTypeEnum = AttributeTypeEnum.LOCAL_VARIABLE_TABLE;
        } else if ("LocalVariableTypeTable".equalsIgnoreCase(name)) {
            clazz = LocalVariableTypeTableAttr.class;
            attributeTypeEnum = AttributeTypeEnum.LOCAL_VARIABLE_TYPE_TABLE;
        } else if ("RuntimeInvisibleAnnotations".equalsIgnoreCase(name)) {
            clazz = InvisibleAnnotationsAttr.class;
            attributeTypeEnum = AttributeTypeEnum.RUNTIME_INVISIBLE_ANNOTATIONS;
        } else if ("RuntimeInvisibleTypeAnnotations".equalsIgnoreCase(name)) {
            clazz = InvisibleTypeAnnotationsAttr.class;
            attributeTypeEnum = AttributeTypeEnum.RUNTIME_INVISIBLE_TYPE_ANNOTATIONS;
        } else if ("RuntimeVisibleAnnotations".equalsIgnoreCase(name)) {
            clazz = VisibleAnnotationsAttr.class;
            attributeTypeEnum = AttributeTypeEnum.RUNTIME_VISIBLE_ANNOTATIONS;
        } else if ("RuntimeVisibleTypeAnnotations".equalsIgnoreCase(name)) {
            clazz = VisibleTypeAnnotationsAttr.class;
            attributeTypeEnum = AttributeTypeEnum.RUNTIME_VISIBLE_TYPE_ANNOTATIONS;
        } else if ("RuntimeInvisibleParameterAnnotations".equalsIgnoreCase(name)) {
            clazz = InvisibleParameterAnnotationsAttr.class;
            attributeTypeEnum = AttributeTypeEnum.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS;
        } else if ("RuntimeVisibleParameterAnnotations".equalsIgnoreCase(name)) {
            clazz = VisibleParameterAnnotationsAttr.class;
            attributeTypeEnum = AttributeTypeEnum.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS;
        } else {
            clazz = UnimplementedAttr.class;
            attributeTypeEnum = AttributeTypeEnum.UNKNOWN;
        }

        try {
            attribute = (AbstractAttribute) clazz.newInstance();
            attribute.setAttributeType(attributeTypeEnum);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return attribute;
    }

    public static AbstractAttribute getNextAttribute(ClassReader reader) throws IOException, IllegalAccessException, InstantiationException {
        AbstractAttribute value = null;

        short[] rawNameIndexAttribute = reader.readFromCurrentStream(2);
        int nameIndexAttribute = BaseUtils.combineBytesToInt(rawNameIndexAttribute);

        Map<Integer, Object> constPool = reader.getPoolByClass(PoolParser.class);
        Object objFromPool = constPool.get(nameIndexAttribute - 1);

        Object attributeByName = null;

        if (objFromPool != null) {
            attributeByName = getAttributeInstanceByName(((ConstUtf8Info) objFromPool).getAsNewString());
        }

        if (attributeByName != null) {
            value = (AbstractAttribute) reader.read(attributeByName, rawNameIndexAttribute);
        }

        return value;
    }

}
