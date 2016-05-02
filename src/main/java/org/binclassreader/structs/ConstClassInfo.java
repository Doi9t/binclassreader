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
 * Created by Yannick on 1/25/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.1
public class ConstClassInfo {
    @BinClassParser(byteToRead = 2)
    private int[] name_index;

    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class)
    public int getNameIndex() {
        return Utilities.combineBytesToInt(name_index);
    }

    @Override
    public String toString() {
        return "ConstClassInfo{" +
                "name_index=" + getNameIndex() +
                '}';
    }
}
