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

package ca.watier.binclassreader.attributes;

import ca.watier.binclassreader.abstracts.AbstractAttribute;
import ca.watier.binclassreader.annotations.BinClassParser;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.utils.AttributeUtils;
import ca.watier.binclassreader.utils.BaseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 5/23/2016.
 */

//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3
public class CodeAttr extends AbstractAttribute {

    private final List<Short> CODE;
    private final List<ExceptionTableItem> EXCEPTIONS_TABLE_ITEMS;
    private final List<AbstractAttribute> ATTRIBUTES_TABLE_ITEMS;
    @BinClassParser(readOrder = 3, byteToRead = 2)
    private byte[] max_stack;
    @BinClassParser(readOrder = 4, byteToRead = 2)
    private byte[] max_locals;
    @BinClassParser(readOrder = 5, byteToRead = 4)
    private byte[] code_length;

    public CodeAttr() {
        CODE = new ArrayList<Short>();
        EXCEPTIONS_TABLE_ITEMS = new ArrayList<ExceptionTableItem>();
        ATTRIBUTES_TABLE_ITEMS = new ArrayList<AbstractAttribute>();
    }

    public void afterFieldsInitialized(ClassReader reader) {
        int codeLength = BaseUtils.combineBytesToInt(code_length);

        try {
            //Read the code
            if (codeLength < 65536) {
                for (short i = 0; i < codeLength; i++) {
                    CODE.add((short) reader.readFromCurrentStream());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            int exceptionLength = BaseUtils.combineBytesToInt(reader.readFromCurrentStream(2));
            for (int i = 0; i < exceptionLength; i++) {
                EXCEPTIONS_TABLE_ITEMS.add(reader.read(new ExceptionTableItem()));
            }
            int attributesLength = BaseUtils.combineBytesToInt(reader.readFromCurrentStream(2));

            for (int i = 0; i < attributesLength; i++) {
                AbstractAttribute attributeByName = AttributeUtils.getNextAttribute(reader);

                if (attributeByName != null) {
                    ATTRIBUTES_TABLE_ITEMS.add(attributeByName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public List<Short> getRawBytecode() {
        return CODE;
    }

    public List<ExceptionTableItem> getExceptionTable() {
        return EXCEPTIONS_TABLE_ITEMS;
    }

    public class ExceptionTableItem {

        @BinClassParser(byteToRead = 2)
        private byte[] start_pc;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private byte[] end_pc;

        @BinClassParser(readOrder = 3, byteToRead = 2)
        private byte[] handler_pc;

        @BinClassParser(readOrder = 4, byteToRead = 2)
        private byte[] catch_type;

        public int getStartPc() {
            return BaseUtils.combineBytesToInt(start_pc);
        }

        public int getEndPc() {
            return BaseUtils.combineBytesToInt(end_pc);
        }

        public int getHandlerPc() {
            return BaseUtils.combineBytesToInt(handler_pc);
        }

        public int getCatchType() {
            return BaseUtils.combineBytesToInt(catch_type);
        }

        public boolean compareValues(int startPc, int endPc, int handlePc, int catchPc) {
            return getStartPc() == startPc &&
                    getEndPc() == endPc &&
                    getHandlerPc() == handlePc &&
                    getCatchType() == catchPc;
        }
    }

    public List<AbstractAttribute> getAttributes() {
        return ATTRIBUTES_TABLE_ITEMS;
    }
}
