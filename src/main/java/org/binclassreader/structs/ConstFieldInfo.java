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

import java.util.Arrays;

/**
 * Created by Yannick on 1/30/2016.
 */
public class ConstFieldInfo {

    @BinClassParser(readOrder = 1, byteToRead = 2)
    private int[] access_flags;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private int[] name_index;

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private int[] descriptor_index;

    @BinClassParser(readOrder = 4, byteToRead = 2)
    private int[] attributes_count;

    public int[] getAccess_flags() {
        return access_flags;
    }

    public int[] getName_index() {
        return name_index;
    }

    public int[] getDescriptor_index() {
        return descriptor_index;
    }

    public int[] getAttributes_count() {
        return attributes_count;
    }

    @Override
    public String toString() {
        return "ConstFieldInfo{" +
                "access_flags=" + Arrays.toString(access_flags) +
                ", name_index=" + Arrays.toString(name_index) +
                ", descriptor_index=" + Arrays.toString(descriptor_index) +
                ", attributes_count=" + Arrays.toString(attributes_count) +
                '}';
    }
}
