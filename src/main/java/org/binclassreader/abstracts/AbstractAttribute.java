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

package org.binclassreader.abstracts;

import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.annotations.PoolItemIndex;
import org.binclassreader.structs.ConstUtf8Info;
import org.binclassreader.utils.Utilities;

/**
 * Created by Yannick on 5/23/2016.
 */
public class AbstractAttribute {

    @BinClassParser(byteToRead = 2)
    private int[] attribute_name_index;

    @BinClassParser(readOrder = 2, byteToRead = 4)
    private int[] attribute_length;


    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class)
    public int getAttributeNameIndex() {
        return Utilities.combineBytesToInt(attribute_name_index);
    }

    public int getAttributeLength() {
        return Utilities.combineBytesToInt(attribute_length);
    }
}