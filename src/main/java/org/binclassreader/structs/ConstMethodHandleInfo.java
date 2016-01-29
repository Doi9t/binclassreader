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

import org.binclassreader.abstracts.AbstractGenericConst;
import org.binclassreader.annotations.BinClassParser;

import java.util.Arrays;

/**
 * Created by Yannick on 1/25/2016.
 */
public class ConstMethodHandleInfo extends AbstractGenericConst {

    @BinClassParser(readOrder = 2, byteToRead = 1)
    private int[] reference_kind;

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private int[] reference_index;


    public int[] getReference_kind_data() {
        return reference_kind;
    }

    public int[] getReference_index_data() {
        return reference_index;
    }
}
