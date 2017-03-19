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

package ca.watier.binclassreader.parsers;

import ca.watier.binclassreader.abstracts.AbstractParser;
import ca.watier.binclassreader.annotations.PoolDataOptions;
import ca.watier.binclassreader.enums.CollectionTypeEnum;
import ca.watier.binclassreader.enums.ConstValuesEnum;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.structs.*;
import ca.watier.binclassreader.utils.BaseUtils;

import java.io.IOException;

/**
 * Created by Yannick on 1/26/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4
@PoolDataOptions(storageType = CollectionTypeEnum.MAP)
public class PoolParser extends AbstractParser {

    public void afterFieldsInitialized(ClassReader reader) {
        try {
            int idx = (getCount() - 1);
            Object obj = null;

            if (idx > 65535) {
                return;
            }

            for (int i = 0; i < idx; i++) {
                ConstValuesEnum valuesEnum = BaseUtils.getConstTypeByValue((byte) reader.readFromCurrentStream());

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
                    default:
                        continue;
                }

                addItemToMap(i, reader.read(obj));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
