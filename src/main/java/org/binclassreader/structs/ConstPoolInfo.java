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

package org.binclassreader.structs;

import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.enums.ConstValuesEnum;
import org.binclassreader.interfaces.SelfReader;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.utils.GeneralUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yannick on 1/26/2016.
 */
public class ConstPoolInfo implements SelfReader {

    private List<Object> poolObjects;

    @BinClassParser(readOrder = 1, byteToRead = 2)
    private int[] bytes;

    public ConstPoolInfo() {
        poolObjects = new ArrayList<Object>();
    }

    public int[] getBytes() {
        return bytes;
    }

    public short getCount() {
        return (short) (GeneralUtils.combineBytes(bytes) - 1);
    }

    public void initReading(InputStream currentStream) {
        try {
            short idx = getCount();
            for (int i = 0; i < idx; i++) {
                ConstValuesEnum valuesEnum = GeneralUtils.getConstTypeByValue((byte) currentStream.read());
                Object obj = null;

                switch (valuesEnum) {
                    case UTF_8:
                        obj = new ConstUtf8Info();
                        break;
                    case INTEGER:
                        obj = new ConstIntegerInfo();
                        break;
                    case FLOAT:
                        obj = new ConstFloatInfo();
                        break;
                    case LONG:
                        obj = new ConstLongInfo();
                        break;
                    case DOUBLE:
                        obj = new ConstDoubleInfo();
                        break;
                    case CLASS:
                        obj = new ConstClassInfo();
                        break;
                    case STRING:
                        obj = new ConstStringInfo();
                        break;
                    case FIELD_REF:
                        obj = new ConstFieldRefInfo();
                        break;
                    case METHOD_REF:
                        obj = new ConstMethodRefInfo();
                        break;
                    case INTERFACE_METHOD_REF:
                        obj = new ConstInterfaceMethodRefInfo();
                        break;
                    case NAME_AND_TYPE:
                        obj = new ConstNameAndTypeInfo();
                        break;
                    case METHOD_HANDLE:
                        obj = new ConstMethodHandleInfo();
                        break;
                    case METHOD_TYPE:
                        obj = new ConstMethodTypeInfo();
                        break;
                    case INVOKE_DYNAMIC:
                        obj = new ConstInvokeDynamicInfo();
                        break;
                }
                poolObjects.addAll(Arrays.asList(ClassReader.read(obj)));
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ConstPoolInfo{" +
                "bytes=" + bytes +
                ", bytes_data=" + Arrays.toString(bytes) +
                ", count=" + getCount() +
                '}';
    }
}
