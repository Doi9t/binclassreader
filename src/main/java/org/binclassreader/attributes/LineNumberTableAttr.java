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

import org.binclassreader.abstracts.Readable;
import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 10/26/2016.
 */
public class LineNumberTableAttr extends Readable {

    @BinClassParser(byteToRead = 2)
    private short[] name_index;

    @BinClassParser(readOrder = 2, byteToRead = 4)
    private short[] length;

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private short[] nb_entries;

    private List<LineHolder> lines;

    public LineNumberTableAttr() {
        lines = new ArrayList<LineHolder>();
    }

    public int getNameIndex() {
        return BaseUtils.combineBytesToInt(name_index);
    }

    public int getLength() {
        return BaseUtils.combineBytesToInt(length);
    }

    public int getNbEntries() {
        return BaseUtils.combineBytesToInt(nb_entries);
    }


    @Override
    public void afterFieldsInitialized(ClassReader reader) {
        int nbEntries = getNbEntries();

        for (int i = 0; i < nbEntries; i++) {
            lines.add(reader.read(new LineHolder()));
        }
    }

    private class LineHolder {
        @BinClassParser(byteToRead = 2)
        private short[] start_pc;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private short[] line_number;

        public int getStartPc() {
            return BaseUtils.combineBytesToInt(start_pc);
        }

        public int getLineNumber() {
            return BaseUtils.combineBytesToInt(line_number);
        }
    }
}
