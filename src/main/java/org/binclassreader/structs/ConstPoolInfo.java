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
import org.binclassreader.interfaces.SelfReader;
import org.binclassreader.utils.GeneralUtils;

import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Yannick on 1/26/2016.
 */
public class ConstPoolInfo implements SelfReader {

    @BinClassParser(readOrder = 1, byteToRead = 2)
    private int[] bytes;

    public int[] getBytes() {
        return bytes;
    }

    public short getCount() {
        return (short) (GeneralUtils.combineBytes(bytes) - 1);
    }

    public void initReading(InputStream currentStream) {
        initPoolIndex(currentStream);
        initPoolValues(currentStream);
    }

    private void initPoolIndex(InputStream currentStream) {

    }

    private void initPoolValues(InputStream currentStream) {

    }

    @Override
    public String toString() {
        return "ConstPoolInfo{" +
                "bytes=" + bytes +
                ", bytes_data=" + Arrays.toString(bytes) +
                ", count=" + getCount() +
                '}';
    }
}
