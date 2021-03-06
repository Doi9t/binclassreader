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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 1/29/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.5-200-A.1
public enum FieldAccessFlagsEnum {
    UNKNOWN((short) 0x0000),
    ACC_PUBLIC((short) 0x0001),
    ACC_PRIVATE((short) 0x0002),
    ACC_PROTECTED((short) 0x0004),
    ACC_STATIC((short) 0x0008),
    ACC_FINAL((short) 0x0010),
    ACC_VOLATILE((short) 0x0040),
    ACC_TRANSIENT((short) 0x0080),
    ACC_SYNTHETIC((short) 0x1000),
    ACC_ENUM((short) 0x4000);

    private short value;

    FieldAccessFlagsEnum(short value) {
        this.value = value;
    }

    public static List<FieldAccessFlagsEnum> getFlagsByMask(short mask) {
        List<FieldAccessFlagsEnum> values = new ArrayList<FieldAccessFlagsEnum>();

        if (mask < 0) {
            values.add(FieldAccessFlagsEnum.UNKNOWN);
            return values;
        }

        for (FieldAccessFlagsEnum flag : FieldAccessFlagsEnum.values()) {
            if (!FieldAccessFlagsEnum.UNKNOWN.equals(flag) && ((mask & flag.getValue()) > 0)) {
                values.add(flag);
            }
        }

        if (values.isEmpty()) {
            values.add(FieldAccessFlagsEnum.UNKNOWN);
        }

        return values;
    }

    public short getValue() {
        return value;
    }
}
