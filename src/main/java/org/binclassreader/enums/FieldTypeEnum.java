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
public enum FieldTypeEnum {
    BYTE('B'),
    CHAR('C'),
    DOUBLE('D'),
    FLOAT('F'),
    INT('I'),
    LONG('J'),
    REFERENCE_CLASS('L'),
    SHORT('S'),
    BOOLEAN('Z'),
    REFERENCE_ARRAY('[');

    private char value;

    FieldTypeEnum(char value) {
        this.value = value;
    }

    public static FieldTypeEnum getFieldTypeEnumByString(String str) {
        if (str == null) {
            return null;
        }

        FieldTypeEnum value = null;
        for (FieldTypeEnum fieldTypeEnum : values()) {
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
