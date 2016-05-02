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

package org.binclassreader.structs;

import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.annotations.PoolItemIndex;
import org.binclassreader.utils.Utilities;

/**
 * Created by Yannick on 1/31/2016.
 */

//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.2
public class ConstAttributeInfo {
    @BinClassParser(byteToRead = 2)
    private int[] attribute_name_index;

    @BinClassParser(readOrder = 2, byteToRead = 4)
    private int[] attribute_length;

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private int[] constant_value_index;

    @PoolItemIndex(mustBeOfType = ConstClassInfo.class)
    public int getAttributeNameIndex() {
        return Utilities.combineBytesToInt(attribute_name_index);
    }

    public int getAttributeLength() {
        return Utilities.combineBytesToInt(attribute_length);
    }

    @PoolItemIndex(mustBeOfType = {ConstLongInfo.class, ConstFloatInfo.class, ConstDoubleInfo.class, ConstIntegerInfo.class, ConstStringInfo.class})
    public int getConstantValueIndex() {
        return Utilities.combineBytesToInt(constant_value_index);
    }

    @Override
    public String toString() {
        return "ConstAttributeInfo{" +
                "attribute_name_index=" + getAttributeNameIndex() +
                ", attribute_length=" + getAttributeLength() +
                ", constant_value_index=" + getConstantValueIndex() +
                '}';
    }
}
