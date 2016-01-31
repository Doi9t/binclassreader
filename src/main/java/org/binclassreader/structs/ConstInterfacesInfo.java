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
import org.binclassreader.reader.ClassReader;
import org.binclassreader.utils.GeneralUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yannick on 1/29/2016.
 */
public class ConstInterfacesInfo implements SelfReader {

    private List<Object> poolObjects;

    @BinClassParser(readOrder = 1, byteToRead = 2)
    private int[] countData;
    private int count;

    public ConstInterfacesInfo() {
        poolObjects = new ArrayList<Object>();
    }

    public short getCount() {
        return (short) (GeneralUtils.combineBytes(countData));
    }

    public void initReading(InputStream currentStream) {
        count = getCount();
        for (int i = 0; i < count; i++) {
            poolObjects.addAll(Arrays.asList(ClassReader.read(new ConstClassInfo())));
        }
    }

    @Override
    public String toString() {
        return "ConstInterfacesInfo{" +
                "poolObjects=" + poolObjects +
                ", countData=" + Arrays.toString(countData) +
                ", count=" + count +
                '}';
    }
}
