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

import org.binclassreader.abstracts.Readable;
import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.utils.BaseUtils;


/**
 * Created by Yannick on 11/3/2016.
 */

public class Annotation extends Readable {
    @BinClassParser(byteToRead = 2)
    private short[] name_index;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private short[] num_element_value_pairs;


    public int getNameIndex() {
        return BaseUtils.combineBytesToInt(name_index);
    }

    public int getNbElementPair() {
        return BaseUtils.combineBytesToInt(num_element_value_pairs);
    }
}