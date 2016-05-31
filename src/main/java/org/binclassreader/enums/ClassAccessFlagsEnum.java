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
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1-200-E.1
public enum ClassAccessFlagsEnum {
    UNKNOWN((short) 0x0000),
    ACC_PUBLIC((short) 0x0001),
    ACC_FINAL((short) 0x0010),
    ACC_SUPER((short) 0x0020),
    ACC_INTERFACE((short) 0x0200),
    ACC_ABSTRACT((short) 0x0400),
    ACC_SYNTHETIC((short) 0x1000),
    ACC_ANNOTATION((short) 0x2000),
    ACC_ENUM((short) 0x4000);

    private short value;

    ClassAccessFlagsEnum(short value) {
        this.value = value;
    }

    public static ClassAccessFlagsEnum getFlagById(short id) {
        ClassAccessFlagsEnum value = ClassAccessFlagsEnum.UNKNOWN;

        if (id < 0) {
            return value;
        }

        for (ClassAccessFlagsEnum flag : ClassAccessFlagsEnum.values()) {
            if (flag.getValue() == id) {
                value = flag;
                break;
            }
        }

        return value;
    }

    public short getValue() {
        return value;
    }
}
