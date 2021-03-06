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

package ca.watier.binclassreader.enums;

/**
 * Created by Yannick on 1/25/2016.
 */
public enum ConstValuesEnum {
    UTF_8((byte) 1),
    INTEGER((byte) 3),
    FLOAT((byte) 4),
    LONG((byte) 5),
    DOUBLE((byte) 6),
    CLASS((byte) 7),
    STRING((byte) 8),
    FIELD_REF((byte) 9),
    METHOD_REF((byte) 10),
    INTERFACE_METHOD_REF((byte) 11),
    NAME_AND_TYPE((byte) 12),
    METHOD_HANDLE((byte) 15),
    METHOD_TYPE((byte) 16),
    INVOKE_DYNAMIC((byte) 18),
    UNKNOWN((byte) -1);

    private byte value;

    ConstValuesEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
