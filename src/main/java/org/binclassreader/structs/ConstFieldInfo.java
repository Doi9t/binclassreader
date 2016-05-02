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
import org.binclassreader.enums.AccessFlagsEnum;
import org.binclassreader.utils.Utilities;

/**
 * Created by Yannick on 1/30/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.5
public class ConstFieldInfo {

    @BinClassParser(byteToRead = 2)
    private int[] access_flags;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private int[] name_index;

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private int[] descriptor_index;

    @BinClassParser(readOrder = 4, byteToRead = 2)
    private int[] attributes_count;

    public AccessFlagsEnum getAccessFlags() {
        return AccessFlagsEnum.getFlagById((byte) Utilities.combineBytesToInt(access_flags));
    }

    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class)
    public int getNameIndex() {
        return Utilities.combineBytesToInt(name_index);
    }

    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class)
    public int getDescriptorIndex() {
        return Utilities.combineBytesToInt(descriptor_index);
    }

    public int getAttributesCount() {
        return Utilities.combineBytesToInt(attributes_count);
    }

    @Override
    public String toString() {
        return "ConstFieldInfo{" +
                "accessFlags=" + getAccessFlags() +
                ", nameIndex=" + getNameIndex() +
                ", descriptorIndex=" + getDescriptorIndex() +
                ", attributesCount=" + getAttributesCount() +
                '}';
    }
}
