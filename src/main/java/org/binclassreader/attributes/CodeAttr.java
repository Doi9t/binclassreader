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

package org.binclassreader.attributes;

import org.binclassreader.abstracts.AbstractAttribute;
import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 5/23/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3
public class CodeAttr extends AbstractAttribute {

    private final List<Short> CODE;
    @BinClassParser(readOrder = 3, byteToRead = 2)
    private short[] max_stack;
    @BinClassParser(readOrder = 4, byteToRead = 2)
    private short[] max_locals;
    @BinClassParser(readOrder = 5, byteToRead = 4)
    private short[] code_length;

    public CodeAttr() {
        CODE = new ArrayList<Short>();
    }

    public void afterFieldsInitialized(ClassReader reader) {
        int codeLength = Utilities.combineBytesToInt(code_length);

        try {
            //Read the code
            for (short i = 0; i < codeLength; i++) {
                CODE.add((short) reader.readFromCurrentStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // The x - 8 is to remove the bytes that are already read.
            reader.skipFromCurrentStream(getAttributeLength() - 8 - codeLength);//FIXME: Parse the bytes instead of wasting them ...
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Short> getRawBytecode() {
        return CODE;
    }

    private class ExceptionHandler {

        @BinClassParser(byteToRead = 2)
        private short[] start_pc;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private short[] end_pc;

        @BinClassParser(readOrder = 3, byteToRead = 2)
        private short[] handler_pc;

        @BinClassParser(readOrder = 4, byteToRead = 2)
        private short[] catch_type;

        public int getStartPc() {
            return Utilities.combineBytesToInt(start_pc);
        }

        public int getEndPc() {
            return Utilities.combineBytesToInt(end_pc);
        }

        public int getHandlerPc() {
            return Utilities.combineBytesToInt(handler_pc);
        }

        public int getCatchType() {
            return Utilities.combineBytesToInt(catch_type);
        }
    }
}
