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
public class Utilities {

    /**
     * @param bytes - The byte to be merged
     * @return The combined byte, -1 if the byte array is empty or null
     */
    public static int combineBytesToInt(int[] bytes) {

        if (bytes == null || bytes.length == 0) {
            return -1;
        }

        int value = 0;

        int shiftBytes = (8 * bytes.length) - 8;
        for (int b : bytes) {
            value |= (b & 0xFF) << shiftBytes;
            shiftBytes = (shiftBytes - 8);
        }

        return value;
    }

    /**
     * @param bytes - The byte to be merged
     * @return The combined byte, -1 if the byte array is empty or null
     */
    public static long combineBytesToLong(int[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return -1;
        }

        long value = 0;

        long shiftBytes = (8 * bytes.length) - 8;
        for (long b : bytes) {
            value |= (b & 0xFF) << shiftBytes;
            shiftBytes = (shiftBytes - 8);
        }

        return value;
    }

    /**
     * @param x - The current byte
     * @return A ConstValuesEnum if the byte is found, ConstValuesEnum.UNKNOWN if not
     */
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


    /**
     * @param arr - An array of Object to be cloned
     * @return An array of object
     */
    public static <T> T[] safeArrayClone(T[] arr) {
        if (arr == null) {
            return null;
        }

        return arr.clone();
    }

    /**
     * @param arr - An array of byte to be cloned
     * @return An array of object
     */
    public static byte[] safeArrayClone(byte[] arr) {
        if (arr == null) {
            return null;
        }

        return arr.clone();
    }

    /**
     * @param arr - An array of int to be cloned
     * @return An array of object
     */
    public static int[] safeArrayClone(int[] arr) {
        if (arr == null) {
            return null;
        }

        return arr.clone();
    }

    /**
     * @param clazzArr - An array of class to be transformed into a array of object
     * @return An array of object
     */
    public static Object[] createNewArrayOfObject(Class<?>... clazzArr) {
        if (!Assert.isArrayReadable(clazzArr)) {
            return null;
        }

        Object[] values = new Object[clazzArr.length];

        for (int i = 0; i < clazzArr.length; i++) {
            Class<?> aClass = clazzArr[i];

            if (aClass == null) {
                continue;
            }

            try {
                values[i] = aClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return values;
    }
}
