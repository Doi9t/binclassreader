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

import org.binclassreader.structs.*;

/**
 * Created by Yannick on 11/30/2016.
 */

public enum AnnotationEnum {
    BYTE((char) 0x42, ConstIntegerInfo.class), //const_value_index
    CHAR((char) 0x43, ConstIntegerInfo.class), //const_value_index
    DOUBLE((char) 0x44, ConstDoubleInfo.class), //const_value_index
    FLOAT((char) 0x46, ConstFloatInfo.class), //const_value_index
    INT((char) 0x49, ConstIntegerInfo.class), //const_value_index
    LONG((char) 0x4A, ConstLongInfo.class), //const_value_index
    SHORT((char) 0x53, ConstIntegerInfo.class), //const_value_index
    BOOLEAN((char) 0x5A, ConstIntegerInfo.class), //const_value_index
    STRING((char) 0x73, ConstUtf8Info.class), //const_value_index
    ENUM_TYPE((char) 0x65, null), //enum_const_value
    CLASS((char) 0x63, ConstUtf8Info.class), //class_info_index
    ANNOTATION_TYPE((char) 0x40, null), //annotation_value
    ARRAY_VALUE((char) 0x5B, null); //array_value

    private char key;
    private Class<?> type;

    AnnotationEnum(char key, Class<?> type) {
        this.key = key;
        this.type = type;
    }

    public char getKey() {
        return key;
    }

    public Class<?> getType() {
        return type;
    }

    public static AnnotationEnum getAnnotationByChar(char requestChar) {
        AnnotationEnum value = null;

        for (AnnotationEnum annotationEnum : values()) {
            if (annotationEnum.getKey() == requestChar) {
                value = annotationEnum;
                break;
            }
        }
        return value;
    }
}
