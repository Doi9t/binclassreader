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

/**
 * Created by Yannick on 10/6/2016.
 */

public enum TypeEnum {

    BYTE('B'),
    CHAR('C'),
    DOUBLE('D'),
    FLOAT('F'),
    INT('I'),
    LONG('J'),
    SHORT('S'),
    BOOLEAN('Z'),
    STRING('s'),
    ENUM('e'),
    CLASS('c'),
    ANNOTATION('@'),
    REFERENCE_ARRAY('['),
    REFERENCE_CLASS('L');

    private char value;

    TypeEnum(char value) {
        this.value = value;
    }

    public static TypeEnum getFieldTypeEnumByString(String str) {
        if (str == null) {
            return null;
        }

        TypeEnum value = null;
        for (TypeEnum fieldTypeEnum : values()) {
            if (str.contains(Character.toString(fieldTypeEnum.getValue()))) {
                value = fieldTypeEnum;
                break;
            }
        }

        return value;
    }

    public char getValue() {
        return value;
    }
}
