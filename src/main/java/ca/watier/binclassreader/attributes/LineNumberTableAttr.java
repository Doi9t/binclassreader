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

import ca.watier.binclassreader.abstracts.AbstractIterableAttribute;
import ca.watier.binclassreader.annotations.BinClassParser;
import ca.watier.binclassreader.utils.BaseUtils;

/**
 * Created by Yannick on 10/26/2016.
 */
public class LineNumberTableAttr extends AbstractIterableAttribute<LineNumberTableAttr.LineHolder> {

    public LineNumberTableAttr() {
        super(LineHolder.class);
    }

    public static class LineHolder {
        @BinClassParser(byteToRead = 2)
        private byte[] start_pc;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private byte[] line_number;

        public int getStartPc() {
            return BaseUtils.combineBytesToInt(start_pc);
        }

        public int getLineNumber() {
            return BaseUtils.combineBytesToInt(line_number);
        }
    }
}
