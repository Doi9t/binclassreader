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
import org.binclassreader.enums.AccessFlagsEnum;
import org.binclassreader.utils.Utilities;

import java.util.Arrays;

/**
 * Created by Yannick on 1/29/2016.
 */
public class ConstAccessFlagsInfo {
    @BinClassParser(byteToRead = 2)
    private int[] flags;

    public int getFlagIndex() {
        return Utilities.combineBytesToInt(flags);
    }


    public AccessFlagsEnum getAccessFlag() {
        return AccessFlagsEnum.getFlagById((byte) getFlagIndex());
    }

    @Override
    public String toString() {
        return "ConstAccessFlagsInfo{" +
                "flag=" + getAccessFlag() +
                '}';
    }
}
