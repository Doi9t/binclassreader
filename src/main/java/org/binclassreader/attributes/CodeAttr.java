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
import org.binclassreader.utils.BaseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 5/23/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3
public class CodeAttr extends AbstractAttribute {

    private final List<Short> CODE;
    private final List<ExceptionHandler> EXCEPTIONS;
    @BinClassParser(readOrder = 3, byteToRead = 2)
    private short[] max_stack;
    @BinClassParser(readOrder = 4, byteToRead = 2)
    private short[] max_locals;
    @BinClassParser(readOrder = 5, byteToRead = 4)
    private short[] code_length;

    public CodeAttr() {
        CODE = new ArrayList<Short>();
        EXCEPTIONS = new ArrayList<ExceptionHandler>();
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
                EXCEPTIONS.add(reader.read(new ExceptionHandler()));
            }
            // 10 = max_stack + max_locals + code_length + exceptionLength
            reader.skipFromCurrentStream(getAttributeLength() - 10 - codeLength - (EXCEPTIONS.size() * 8));//FIXME: Parse the bytes instead of wasting them ...
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Short> getRawBytecode() {
        return CODE;
    }

    public class ExceptionHandler {

        @BinClassParser(byteToRead = 2)
        private short[] start_pc;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private short[] end_pc;

        @BinClassParser(readOrder = 3, byteToRead = 2)
        private short[] handler_pc;

        @BinClassParser(readOrder = 4, byteToRead = 2)
        private short[] catch_type;

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
    }
}
