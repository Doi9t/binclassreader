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

package org.binclassreader.parsers;

import org.binclassreader.abstracts.AbstractParser;
import org.binclassreader.annotations.PoolDataOptions;
import org.binclassreader.enums.CollectionType;
import org.binclassreader.enums.ConstValuesEnum;
import org.binclassreader.interfaces.SelfReader;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.structs.*;
import org.binclassreader.utils.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Yannick on 1/26/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4
@PoolDataOptions(storageType = CollectionType.MAP)
public class PoolParser extends AbstractParser implements SelfReader {

    public void initReading(ClassReader reader, InputStream currentStream) {
        try {
            int idx = (getCount() - 1);

            if (idx > 65535) {
                return;
            }

            for (int i = 0; i < idx; i++) {
                ConstValuesEnum valuesEnum = Utilities.getConstTypeByValue((byte) currentStream.read());
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
                        i++;
                        break;
                    case DOUBLE:
                        obj = new ConstDoubleInfo();
                        i++;
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

                addItemToMap(i, reader.read(obj));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        Map<Short, Object> pool = (Map<Short, Object>) getPool();

        if (pool != null) {
            buffer.append("\n~~~~~~~~~~~~ Constant pool [START] ~~~~~~~~~~~~\n");
            for (Map.Entry<Short, Object> shortObjectEntry : pool.entrySet()) {
                buffer.append("\t").append(shortObjectEntry.getKey()).append("\t->\t").append(shortObjectEntry.getValue()).append(" ,\n");
            }
            buffer.append("~~~~~~~~~~~~~ Constant pool [END] ~~~~~~~~~~~~~\n");
        } else {
            buffer.append("~~~~~~~~~~~~~ NO Constant pool values ~~~~~~~~~~~~~\n");
        }

        return "PoolParser{" +
                "poolObjects=" + buffer.toString() +
                ", count=" + getCount() +
                '}';
    }
}
