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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 6/2/2016.
 */
public class LocalVariableTypeTableAttr extends AbstractAttribute implements SelfReader {

    @BinClassParser(readOrder = 3, byteToRead = 4)
    private short[] local_variable_type_table_length;
    private List<LocalVariableType> localVarTable;

    public int getLocalVariableTypeLength() {
        return Utilities.combineBytesToInt(local_variable_type_table_length);
    }

    public void initReading(ClassReader reader) {

        int localTableLength = getLocalVariableTypeLength();

        if (localTableLength > 0) {
            localVarTable = new ArrayList<LocalVariableType>();

            for (int i = 0; i < localTableLength; i++) {
                localVarTable.add(reader.read(new LocalVariableType()));
            }
        }
    }


    private class LocalVariableType {
        @BinClassParser(byteToRead = 2)
        private short[] start_pc;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private short[] length;

        @BinClassParser(readOrder = 3, byteToRead = 2)
        private short[] name_index;

        @BinClassParser(readOrder = 4, byteToRead = 2)
        private short[] signature_index;

        @BinClassParser(readOrder = 5, byteToRead = 2)
        private short[] index;


        public int getStartPc() {
            return Utilities.combineBytesToInt(start_pc);
        }

        public int getLength() {
            return Utilities.combineBytesToInt(length);
        }

        public int getNameIndex() {
            return Utilities.combineBytesToInt(name_index);
        }

        public int getSignatureIndex() {
            return Utilities.combineBytesToInt(signature_index);
        }

        public int getIndex() {
            return Utilities.combineBytesToInt(index);
        }
    }
}
