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
import org.binclassreader.interfaces.SelfReader;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.utils.Utilities;

/**
 * Created by Yannick on 5/23/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3
public class CodeAttr extends AbstractAttribute implements SelfReader {

    /*
        u2 exception_table_length;
        {   u2 start_pc;
            u2 end_pc;
            u2 handler_pc;
            u2 catch_type;
        } exception_table[exception_table_length];
        u2 attributes_count;
        attribute_info attributes[attributes_count];
     */

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private int[] max_stack;

    @BinClassParser(readOrder = 4, byteToRead = 2)
    private int[] max_locals;

    @BinClassParser(readOrder = 5, byteToRead = 4)
    private int[] code_length;

    private byte[] code; //Code instructions [https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5]

    public void initReading(ClassReader reader) {
        int codeLength = Utilities.combineBytesToInt(code_length);

        if (codeLength > 0 && codeLength < 65536) {
            code = new byte[codeLength];
        }
    }

    private class ExceptionHandler {

        @BinClassParser(byteToRead = 2)
        private int[] start_pc;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private int[] end_pc;

        @BinClassParser(readOrder = 3, byteToRead = 2)
        private int[] handler_pc;

        @BinClassParser(readOrder = 4, byteToRead = 2)
        private int[] catch_type;

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
