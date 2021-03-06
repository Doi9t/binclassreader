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

package ca.watier.binclassreader.utils;

import ca.watier.binclassreader.enums.BytecodeExtraByteEnum;
import ca.watier.binclassreader.enums.BytecodeInstructionEnum;
import ca.watier.binclassreader.enums.ConstValuesEnum;
import ca.watier.defassert.Assert;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by Yannick on 1/26/2016.
 */
public class BaseUtils {

    private static BytecodeExtraByteEnum INDEXBYTE_1 = BytecodeExtraByteEnum.INDEXBYTE_1, INDEXBYTE_2 = BytecodeExtraByteEnum.INDEXBYTE_2;

    /**
     * @param bytes - The byte to be merged
     */
    public static int combineBytesToInt(byte[] bytes) {
        Assert.assertNotEmpty(bytes);

        return new BigInteger(bytes).intValue();
    }

    /**
     * @param bytes - The byte to be merged
     */
    public static short combineBytesToShort(byte[] bytes) {
        Assert.assertNotEmpty(bytes);

        return new BigInteger(bytes).shortValue();
    }

    /**
     * @param bytes - The byte to be merged
     */
    public static long combineBytesToLong(byte[] bytes) {
        Assert.assertNotEmpty(bytes);

        return new BigInteger(bytes).longValue();
    }


    /**
     * @param bytes - The byte to be merged
     */
    public static double combineBytesToDouble(byte[] bytes) {
        Assert.assertNotEmpty(bytes);

        return ByteBuffer.wrap(bytes).getDouble();
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
     * Convert an array of short to a list
     *
     * @param arr
     * @return
     */
    public static List<Short> shortArrayToList(short[] arr) {
        Assert.assertNotEmpty(arr);

        List<Short> values = new ArrayList<Short>();

        for (short b : arr) {
            values.add(b);
        }

        return values;
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
     * @param list - The list
     * @return the current list if not null, or an empty ArrayList otherwise
     */
    public static <V> List<V> safeList(List<V> list) {
        return (list != null) ? list : new ArrayList<V>();
    }


    /**
     * @param col - The Collection
     * @return the current list if not null, or an empty ArrayList otherwise
     */
    public static <V> Collection<V> safeCollection(Collection<V> col) {
        return (col != null) ? col : new ArrayList<V>();
    }

    /**
     * @param clazzArr - An array of class to be transformed into a array of object
     * @return An array of object
     */
    public static Object[] createNewArrayOfObject(Class<?>... clazzArr) {
        Assert.assertNotEmpty(clazzArr);

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
     * @return A list containing the ITEMS
     */
    public static <T> List safeToList(T... varArgs) {
        Assert.assertNotEmpty(varArgs);

        return Arrays.asList(varArgs);
    }

    /**
     * @param buffer - An array to be converted
     * @return An array of byte
     */
    public static byte[] convertIntArrayToByteArray(int[] buffer) {
        Assert.assertNotEmpty(buffer);

        byte[] values = new byte[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            values[i] = (byte) buffer[i];
        }

        return values;
    }

    /**
     * Cast an object to the passed type
     *
     * @param obj
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T as(Object obj, Class<T> type) {
        Assert.assertNotNull(obj);
        Assert.assertNotNull(type);

        return type.cast(obj);
    }

    public static String getBytecodeAsFormattedString(List<Short> bytecode, Map<Integer, Object> constPool) {
        Assert.assertNotEmpty(bytecode);

        int size = bytecode.size();
        StringBuilder stringBuffer = new StringBuilder();
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
            stringBuffer.append('\n');
        }

        return stringBuffer.toString();
    }

    /**
     * Convert an array of byte to an array of short
     *
     * @param buffer
     * @return
     */
    public static short[] convertByteToUnsigned(byte[] buffer) {
        Assert.assertNotEmpty(buffer);

        int length = buffer.length;

        short[] shortBuffer = new short[length];

        for (int i = 0; i < length; i++) {
            shortBuffer[i] = (short) ((short) buffer[i] & 0xFF);
        }

        return shortBuffer;
    }
}
