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

import org.binclassreader.abstracts.AbstractIterableAttribute;
import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.utils.BaseUtils;

/**
 * Created by Yannick on 10/26/2016.
 */
public class LocalVariableTableAttr extends AbstractIterableAttribute<LocalVariableTableAttr.LocalVariable> {

    public LocalVariableTableAttr() {
        super(LocalVariable.class);
    }

    public static class LocalVariable {
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
            return BaseUtils.combineBytesToInt(start_pc);
        }

        public int getLength() {
            return BaseUtils.combineBytesToInt(length);
        }

        public int getNameIndex() {
            return BaseUtils.combineBytesToInt(name_index);
        }

        public int getDescriptorIndex() {
            return BaseUtils.combineBytesToInt(descriptor_index);
        }

        public int getIndex() {
            return BaseUtils.combineBytesToInt(index);
        }
    }
}
