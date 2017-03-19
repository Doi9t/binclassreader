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

package ca.watier.binclassreader.attributes;

import ca.watier.binclassreader.abstracts.Readable;
import ca.watier.binclassreader.annotations.BinClassParser;
import ca.watier.binclassreader.enums.AnnotationEnum;
import ca.watier.binclassreader.enums.AttributeTypeEnum;
import ca.watier.binclassreader.parsers.PoolParser;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.utils.BaseUtils;
import ca.watier.multiarraymap.MultiArrayMap;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Yannick on 11/12/2016.
 */

public class ElementPair extends Readable {

    @BinClassParser(byteToRead = 2)
    private byte[] element_name_index;

    @BinClassParser(readOrder = 2)
    private byte[] tag;

    private ItemContainer currentValue;

    private AttributeTypeEnum attributeTypeEnum;

    private AnnotationEnum annotationEnum;

    public ElementPair(AttributeTypeEnum attributeTypeEnum) {
        this.attributeTypeEnum = attributeTypeEnum;
    }

    @Override
    public void afterFieldsInitialized(ClassReader reader) {

        annotationEnum = AnnotationEnum.getAnnotationByChar((char) BaseUtils.combineBytesToInt(tag));
        currentValue = readElementItem(reader, annotationEnum);
    }

    private ItemContainer readElementItem(ClassReader reader, AnnotationEnum valueToRead) {
        ItemContainer value = new ItemContainer(valueToRead);
        Map<Integer, Object> constPool = reader.getPoolByClass(PoolParser.class);

        int decBaseIndex = 0;
        boolean isIndexable = false;

        try {
            switch (valueToRead) {
                case LONG:
                case DOUBLE:
                    isIndexable = true;
                    break;
                case BOOLEAN:
                case BYTE:
                case CHAR:
                case FLOAT:
                case INT:
                case SHORT:
                case STRING:
                case CLASS:
                    isIndexable = true;
                    decBaseIndex = 1;
                    break;
                case ANNOTATION_TYPE:
                    VisibleAndInvisibleAnnotationsAttr attr = new VisibleAndInvisibleAnnotationsAttr();
                    attr.setAttributeType(attributeTypeEnum);
                    value.setObjValue(reader.read(attr));
                    break;
                case ARRAY_VALUE:
                    int nbElements = BaseUtils.combineBytesToInt(reader.readFromCurrentStream(2));
                    MultiArrayMap<Integer, ItemContainer> values = new MultiArrayMap<Integer, ItemContainer>();

                    for (int i = 0; i < nbElements; i++) {
                        byte[] typeOfArray = reader.readFromCurrentStream(1);
                        AnnotationEnum annotationByChar = AnnotationEnum.getAnnotationByChar((char) BaseUtils.combineBytesToInt(typeOfArray));

                        values.put(i, new ItemContainer(annotationByChar, readElementItem(reader, annotationByChar)));
                    }

                    value.setObjValue(values);
                    break;
                case ENUM_TYPE:
                    value.setObjValue(reader.read(new EnumValue()));
                    break;
            }

            if (isIndexable) {
                int calculatedIndex = BaseUtils.combineBytesToInt(reader.readFromCurrentStream(2)) - decBaseIndex;
                value.setObjValue(constPool.get(calculatedIndex));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    public ItemContainer getValue() {
        return currentValue;
    }

    public class ItemContainer {
        private AnnotationEnum enumValue;
        private Object objValue;

        public ItemContainer(AnnotationEnum enumValue) {
            this.enumValue = enumValue;
        }

        public ItemContainer(AnnotationEnum enumValue, Object objValue) {
            this.enumValue = enumValue;
            this.objValue = objValue;
        }

        public void setObjValue(Object objValue) {
            this.objValue = objValue;
        }

        public AnnotationEnum getEnumValue() {
            return enumValue;
        }

        public Object getObjValue() {
            return objValue;
        }
    }

    public static class EnumValue {
        @BinClassParser(byteToRead = 2)
        private byte[] type_name_index; //CONSTANT_Utf8_info

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private byte[] const_name_index; //CONSTANT_Utf8_info

        public int getTypeNameIndex() {
            return BaseUtils.combineBytesToInt(type_name_index);
        }

        public int getConstNameIndex() {
            return BaseUtils.combineBytesToInt(const_name_index);
        }
    }

    /**
     * Get the leaf item of the tree
     *
     * @param item
     * @return
     */
    public static Object getLeafItem(Object item) {

        if (item == null || !(item instanceof ItemContainer)) {
            return item;
        }

        return getLeafItem(((ItemContainer) item).getObjValue());
    }

    public AnnotationEnum getAnnotationEnum() {
        return annotationEnum;
    }
}