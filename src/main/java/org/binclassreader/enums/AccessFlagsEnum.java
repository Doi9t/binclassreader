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
 * Created by Yannick on 1/29/2016.
 */
public enum AccessFlagsEnum {
    UNKNOWN((byte) 0x0000),
    ACC_PUBLIC((byte) 0x0001),
    ACC_PRIVATE((byte) 0x0002),
    ACC_PROTECTED((byte) 0x0004),
    ACC_STATIC((byte) 0x0008),
    ACC_FINAL((byte) 0x0010),
    ACC_VOLATILE((byte) 0x0040),
    ACC_TRANSIENT((byte) 0x0080),
    ACC_SYNTHETIC((byte) 0x1000),
    ACC_ENUM((byte) 0x4000);

    private byte value;

    AccessFlagsEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }


    public static AccessFlagsEnum getFlagById(byte id) {
        AccessFlagsEnum value = AccessFlagsEnum.UNKNOWN;

        if (id < 0) {
            return value;
        }

        for (AccessFlagsEnum flag : AccessFlagsEnum.values()) {
            if (flag.getValue() == id) {
                value = flag;
                break;
            }
        }

        return value;
    }
}
