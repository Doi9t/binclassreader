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

/**
 * Created by Yannick on 1/25/2016.
 */
public class ConstInvokeDynamicInfo {
    @BinClassParser(readOrder = 1, byteToRead = 2)
    private int[] bootstrap_method_attr_index;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private int[] name_and_type_index;

    public int[] getBootstrap_method_attr_index_data() {
        return bootstrap_method_attr_index;
    }

    public int[] getName_and_type_index_data() {
        return name_and_type_index;
    }
}
