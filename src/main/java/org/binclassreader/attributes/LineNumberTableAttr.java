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
public class LineNumberTableAttr extends AbstractAttribute implements SelfReader {

    @BinClassParser(readOrder = 3, byteToRead = 4)
    private short[] line_number_table_length;
    private List<LineNumber> lineTable;

    public int getLineTableLength() {
        return Utilities.combineBytesToInt(line_number_table_length);
    }

    public void initReading(ClassReader reader) {

        int lineTableLength = getLineTableLength();

        if (lineTableLength > 0) {
            lineTable = new ArrayList<LineNumber>();

            for (int i = 0; i < lineTableLength; i++) {
                lineTable.add(reader.read(new LineNumber()));
            }
        }
    }

    private class LineNumber {
        @BinClassParser(byteToRead = 2)
        private short[] start_pc;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private short[] line_number;

        public int getStartPc() {
            return Utilities.combineBytesToInt(start_pc);
        }

        public int getLineNumber() {
            return Utilities.combineBytesToInt(line_number);
        }
    }
}
