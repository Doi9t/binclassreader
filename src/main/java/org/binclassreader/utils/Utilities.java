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

import org.binclassreader.enums.BytecodeExtraByteEnum;
import org.binclassreader.enums.BytecodeInstructionEnum;
import org.binclassreader.enums.ConstValuesEnum;

import java.util.*;

/**
 * Created by Yannick on 1/26/2016.
 */
public class Utilities {

    private static BytecodeExtraByteEnum INDEXBYTE_1 = BytecodeExtraByteEnum.INDEXBYTE_1, INDEXBYTE_2 = BytecodeExtraByteEnum.INDEXBYTE_2;

    /**
     * @param bytes - The byte to be merged
     * @return The combined byte, -1 if the byte array is empty or null
     */
    public static int combineBytesToInt(short[] bytes) {

        if (bytes == null || bytes.length == 0) {
            return -1;
        }

        int value = 0;

        int shiftBytes = (8 * bytes.length) - 8;
        for (int b : bytes) {
            value |= (b << shiftBytes);
            shiftBytes -= 8;
        }

        return value;
    }

    /**
     * @param bytes - The byte to be merged
     * @return The combined byte, -1 if the byte array is empty or null
     */
    public static long combineBytesToLong(short[] bytes) {
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
     * @param arr - An array of short to be cloned
     * @return An array of object
     */
    public static short[] safeArrayClone(short[] arr) {
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

    /**
     * @param varArgs - The item to be returned as a list
     * @return Aa list containing the items
     */
    public static <T> List toList(T... varArgs) {
        return (varArgs == null || varArgs.length == 0) ? Collections.EMPTY_LIST : Arrays.asList(varArgs);
    }

    /**
     * @param buffer - An array to be converted
     * @return An array of byte
     */
    public static byte[] convertIntArrayToByteArray(int[] buffer) {
        if (!Assert.isArrayReadable(buffer)) {
            return new byte[0];
        }

        byte[] values = new byte[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            values[i] = (byte) buffer[i];
        }

        return values;
    }


    public static String getBytecodeAsFormattedString(List<Short> bytecode, Map<Integer, Object> constPool) {

        if (bytecode == null || bytecode.isEmpty()) {
            return "";
        }

        int size = bytecode.size();
        StringBuffer stringBuffer = new StringBuffer();
        Map<BytecodeExtraByteEnum, Short> extraByteMapping = new HashMap<BytecodeExtraByteEnum, Short>();

        for (int i = 0; i < size; i++) {
            short code = bytecode.get(i);

            BytecodeInstructionEnum bytecodeByValue = BytecodeInstructionEnum.getBytecodeByValue(code);
            stringBuffer.append(bytecodeByValue);

            if (bytecodeByValue.haveExtraBytes()) {
                extraByteMapping.clear();

                for (BytecodeExtraByteEnum bytecodeExtraByteEnum : bytecodeByValue.getNbByteToRead()) {
                    i++;
                    extraByteMapping.put(bytecodeExtraByteEnum, bytecode.get(i));
                }

                if (extraByteMapping.containsKey(INDEXBYTE_1) && extraByteMapping.containsKey(INDEXBYTE_2)) {
                    stringBuffer.append(" #").append((extraByteMapping.get(BytecodeExtraByteEnum.INDEXBYTE_1) << 8) | extraByteMapping.get(INDEXBYTE_2));
                }

            }
            stringBuffer.append("\n");
        }

        return stringBuffer.toString();
    }
}
