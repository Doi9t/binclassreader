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
import org.binclassreader.utils.Utilities;

import java.util.Arrays;

/**
 * Created by Yannick on 1/25/2016.
 */
public class ConstMethodHandleInfo {

    @BinClassParser(byteToRead = 1)
    private int[] reference_kind;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private int[] reference_index;

    public int getReferenceKind() {
        return Utilities.combineBytesToInt(reference_kind);
    }

    public int getReferenceIndex() {
        return Utilities.combineBytesToInt(reference_index);
    }

    @Override
    public String toString() {
        return "ConstMethodHandleInfo{" +
                "reference_kind=" + Arrays.toString(reference_kind) +
                ", reference_index=" + Arrays.toString(reference_index) +
                ", ReferenceKind=" + getReferenceKind() +
                ", ReferenceIndex=" + getReferenceIndex() +
                '}';
    }
}
