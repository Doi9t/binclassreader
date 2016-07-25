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

    private final List<Short> CODE;
    private final List<ExceptionHandler> EXCEPTION_TABLE;
    @BinClassParser(readOrder = 3, byteToRead = 2)
    private int[] max_stack;
    @BinClassParser(readOrder = 4, byteToRead = 2)
    private int[] max_locals;
    @BinClassParser(readOrder = 5, byteToRead = 4)
    private int[] code_length;
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

    public CodeAttr() {
        CODE = new ArrayList<Short>();
        EXCEPTION_TABLE = new ArrayList<ExceptionHandler>();
    }

    public void initReading(ClassReader reader) {
        int codeLength = Utilities.combineBytesToInt(code_length);

        try {
            //Read the code
            for (short i = 0; i < codeLength; i++) {
                CODE.add((short) reader.readFromCurrentStream());
            }

            //Read the exceptions
            int exceptionCount = Utilities.combineBytesToInt(reader.readFromCurrentStream(2));
            int attrCount = Utilities.combineBytesToInt(reader.readFromCurrentStream(2));

            for (short i = 0; i < exceptionCount; i++) {
                EXCEPTION_TABLE.add(new ExceptionHandler());
            }

            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
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
