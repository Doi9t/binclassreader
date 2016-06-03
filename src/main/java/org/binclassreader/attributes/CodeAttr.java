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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 5/23/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.3
public class CodeAttr extends AbstractAttribute implements SelfReader {

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private int[] max_stack;

    @BinClassParser(readOrder = 4, byteToRead = 2)
    private int[] max_locals;

    @BinClassParser(readOrder = 5, byteToRead = 4)
    private int[] code_length;

    private List<Short> code; //Code instructions [https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5]

    @BinClassParser(readOrder = 6, byteToRead = 2)
    private int[] exception_table_length;

    private List<ExceptionHandler> exceptionTableHandlers;

    @BinClassParser(readOrder = 7, byteToRead = 2)
    private int[] attributes_count;

    /*
        -----------------------------Attributes-----------------------------
        LineNumberTable.................................................45.3 //DONE
        LocalVariableTable..............................................45.3 //DONE
        LocalVariableTypeTable..........................................49.0 //DONE
        StackMapTable...................................................50.0 //TODO
        RuntimeVisibleTypeAnnotations, RuntimeInvisibleTypeAnnotations..52.0 //TODO
        --------------------------------------------------------------------
     */
    private List<AbstractAttribute> attributesTable;

    public void initReading(ClassReader reader) {
        int codeLength = Utilities.combineBytesToInt(code_length);
        int exceptionLength = Utilities.combineBytesToInt(exception_table_length);
        int attrLength = Utilities.combineBytesToInt(attributes_count);

        if (codeLength > 0 && codeLength < 65536) {
            code = new ArrayList<Short>();


            for (int i = 0; i < codeLength; i++) {
                try {
                    //Read the code
                    code.add((short) Utilities.combineBytesToInt(reader.readFromCurrentStream(1)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (exceptionLength > 0) {
                exceptionTableHandlers = new ArrayList<ExceptionHandler>();

                //Read the exceptions
                for (int i = 0; i < exceptionLength; i++) {
                    exceptionTableHandlers.add(reader.read(new ExceptionHandler()));
                }
            }

            if (attrLength > 0) {
                attributesTable = new ArrayList<AbstractAttribute>();

                //Read the attributes
                for (int i = 0; i < attrLength; i++) {
                    //FIXME: Find and create the 6 attributes
                    // exceptionTableHandlers.add(reader.read(new ExceptionHandler()));
                }
            }
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
