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

package ca.watier.binclassreader.structs;

import ca.watier.binclassreader.annotations.BinClassParser;
import ca.watier.binclassreader.annotations.PoolItemIndex;
import ca.watier.binclassreader.utils.BaseUtils;

import java.util.Arrays;

/**
 * Created by Yannick on 1/25/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.8
public class ConstMethodHandleInfo {

    @BinClassParser
    private byte[] reference_kind;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private byte[] reference_index;

    @PoolItemIndex
    public int getReferenceKind() {
        return BaseUtils.combineBytesToInt(reference_kind);
    }

    @PoolItemIndex
    public int getReferenceIndex() {
        return BaseUtils.combineBytesToInt(reference_index);
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
