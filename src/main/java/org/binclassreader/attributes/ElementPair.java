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

package org.binclassreader.attributes;

import org.binclassreader.abstracts.Readable;
import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.enums.AnnotationEnum;
import org.binclassreader.enums.AttributeTypeEnum;
import org.binclassreader.parsers.PoolParser;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.utils.BaseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yannick on 11/12/2016.
 */

public class ElementPair extends Readable {

    @BinClassParser(byteToRead = 2)
    private short[] element_name_index;

    @BinClassParser(readOrder = 2)
    private short[] tag;

    private Object value;

    private AttributeTypeEnum attributeTypeEnum;

    public ElementPair(AttributeTypeEnum attributeTypeEnum) {
        this.attributeTypeEnum = attributeTypeEnum;
    }

    @Override
    public void afterFieldsInitialized(ClassReader reader) {

        AnnotationEnum annotationByChar = AnnotationEnum.getAnnotationByChar((char) BaseUtils.combineBytesToInt(tag));
        Map<Integer, Object> constPool = reader.getPoolByClass(PoolParser.class);

        try {
            switch (annotationByChar) {
                case BOOLEAN:
                case BYTE:
                case CHAR:
                case DOUBLE:
                case FLOAT:
                case INT:
                case LONG:
                case SHORT:
                case STRING:
                case CLASS:
                    int index = BaseUtils.combineBytesToInt(reader.readFromCurrentStream(2));
                    value = constPool.get(index - 1);
                    break;
                case ANNOTATION_TYPE:
                    VisibleAndInvisibleAnnotationsAttr attr = new VisibleAndInvisibleAnnotationsAttr();
                    attr.setAttributeType(attributeTypeEnum);
                    value = reader.read(attr);
                    break;
                case ARRAY_VALUE:
                    int nbElements = BaseUtils.combineBytesToInt(reader.readFromCurrentStream(2));
                    List<ArrayValue> list = new ArrayList<ArrayValue>(nbElements);

                    for (int i = 0; i < nbElements; i++) {
                        list.add(reader.read(new ArrayValue()));
                    }
                    value = list;
                    break;
                case ENUM_TYPE:
                    value = reader.read(new EnumValue());
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getValue() {
        return value;
    }

    private static class ArrayValue {
        @BinClassParser()
        private short[] type; //AnnotationEnum

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private short[] itemIndex;
    }

    private static class EnumValue {
        @BinClassParser(byteToRead = 2)
        private short[] type_name_index; //CONSTANT_Utf8_info

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private short[] const_name_index; //CONSTANT_Utf8_info
    }
}