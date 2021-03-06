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

import ca.watier.binclassreader.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 1/29/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6-200-A.1
public enum MethodAccessFlagsEnum {
    ACC_PUBLIC((short) 0x0001),
    ACC_PRIVATE((short) 0x0002),
    ACC_PROTECTED((short) 0x0004),
    ACC_STATIC((short) 0x0008),
    ACC_FINAL((short) 0x0010),
    ACC_SYNCHRONIZED((short) 0x0020),
    ACC_BRIDGE((short) 0x0040),
    ACC_VARARGS((short) 0x0080),
    ACC_NATIVE((short) 0x0100),
    ACC_ABSTRACT((short) 0x0400),
    ACC_STRICT((short) 0x0800),
    ACC_SYNTHETIC((short) 0x1000);

    private short value;

    MethodAccessFlagsEnum(short value) {
        this.value = value;
    }

    public static List<MethodAccessFlagsEnum> getFlagsByMask(short mask) {
        List<MethodAccessFlagsEnum> values = new ArrayList<MethodAccessFlagsEnum>();

        for (MethodAccessFlagsEnum flag : MethodAccessFlagsEnum.values()) {
            if ((mask & flag.getValue()) > 0) {
                values.add(flag);
            }
        }

        return values;
    }

    public static short getMask(List<MethodAccessFlagsEnum> list) {

        short value = 0;

        for (MethodAccessFlagsEnum fieldAccessFlagsEnum : BaseUtils.safeList(list)) {
            value += fieldAccessFlagsEnum.getValue();
        }

        return value;
    }

    public static short getMask(MethodAccessFlagsEnum... varArgs) {

        short value = 0;

        if (varArgs == null || varArgs.length == 0) {
            return value;
        }

        for (MethodAccessFlagsEnum fieldAccessFlagsEnum : varArgs) {
            value += fieldAccessFlagsEnum.getValue();
        }

        return value;
    }

    public short getValue() {
        return value;
    }
}
