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
import org.binclassreader.interfaces.SelfReader;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.utils.Utilities;

/**
 * Created by Yannick on 4/18/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7
public class AttributesInfo implements SelfReader {

    @BinClassParser(byteToRead = 2)
    private int[] attribute_name_index;

    @BinClassParser(readOrder = 2, byteToRead = 4)
    private int[] attribute_length;

    private int[] info;

    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class)
    public int getAttributeNameIndex() {
        return Utilities.combineBytesToInt(attribute_name_index);
    }

    public int getAttributeLength() {
        return Utilities.combineBytesToInt(attribute_length);
    }


    @Override
    public String toString() {
        return "ConstAttributeInfo{" +
                "attribute_name_index=" + getAttributeNameIndex() +
                ", attribute_length=" + getAttributeLength() +
                '}';
    }

    /*
        For all attributes, the attribute_name_index must be a valid unsigned 16-bit index into the constant pool of the
        class. The constant_pool entry at attribute_name_index must be a CONSTANT_Utf8_info structure (§4.4.7) representing
        the name of the attribute. The value of the attribute_length item indicates the length of the subsequent information
        in bytes. The length does not include the initial six bytes that contain the attribute_name_index and attribute_length items.
    */

    public void initReading(ClassReader reader) {
        int attributeLength = getAttributeLength();

        if (attributeLength > 0) {
/*            try {
                info = reader.readFromCurrentStream((byte) (attributeLength + 6));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }
}
