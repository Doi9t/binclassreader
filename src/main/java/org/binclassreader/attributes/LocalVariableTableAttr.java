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
public class LocalVariableTableAttr extends AbstractAttribute implements SelfReader {

    @BinClassParser(readOrder = 3, byteToRead = 4)
    private short[] line_number_table_length;
    private List<LocalVariable> localVarTable;

    public int getLineTableLength() {
        return Utilities.combineBytesToInt(line_number_table_length);
    }

    public void initReading(ClassReader reader) {

        int lineTableLength = getLineTableLength();

        if (lineTableLength > 0) {
            localVarTable = new ArrayList<LocalVariable>();

            for (int i = 0; i < lineTableLength; i++) {
                localVarTable.add(reader.read(new LocalVariable()));
            }
        }
    }

    private class LocalVariable {
        @BinClassParser(byteToRead = 2)
        private short[] start_pc;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private short[] length;

        @BinClassParser(readOrder = 3, byteToRead = 2)
        private short[] name_index;

        @BinClassParser(readOrder = 4, byteToRead = 2)
        private short[] descriptor_index;

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

        public int getDescriptorIndex() {
            return Utilities.combineBytesToInt(descriptor_index);
        }

        public int getIndex() {
            return Utilities.combineBytesToInt(index);
        }
    }
}
