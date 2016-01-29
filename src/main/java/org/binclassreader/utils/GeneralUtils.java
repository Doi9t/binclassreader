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

package org.binclassreader.utils;

import org.binclassreader.enums.ConstValuesEnum;

/**
 * Created by Yannick on 1/26/2016.
 */
public class GeneralUtils {

    public static int combineBytes(int[] bytes) {
        int value = 0;

        if (bytes != null && bytes.length > 1) {
            int shiftBytes = (8 * bytes.length) - 8;
            for (int b : bytes) {
                value |= (b & 0xFF) << shiftBytes;
                shiftBytes = (shiftBytes - 8);
            }
        }

        return value;
    }

    public static ConstValuesEnum getConstTypeByValue(byte x) {
        ConstValuesEnum value = ConstValuesEnum.UNKNOWN;

        if (x > 0) {
            for (ConstValuesEnum enumValue : ConstValuesEnum.values()) {
                if (enumValue.getValue() == x) {
                    value = enumValue;
                    break;
                }
            }
        }

        return value;
    }
}
