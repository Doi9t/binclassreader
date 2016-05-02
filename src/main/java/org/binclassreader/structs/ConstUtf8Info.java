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
import org.binclassreader.utils.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Yannick on 1/25/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.7
public class ConstUtf8Info implements SelfReader {

    @BinClassParser(byteToRead = 2)
    private int[] length;

    private byte[] bytes;

    public int getLength() {
        return Utilities.combineBytesToInt(length);
    }

    public byte[] getAsBytes() {
        return Utilities.safeArrayClone(bytes);
    }

    public String getAsNewString() {

        String value = null;

        try {
            value = (bytes != null) ? new String(bytes, "UTF-8") : null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return value;
    }

    public void initReading(ClassReader reader, InputStream currentStream) {
        int lengthArray = Utilities.combineBytesToInt(length);

        if (lengthArray > 0) {
            bytes = new byte[lengthArray];
            try {
                if (lengthArray != currentStream.read(bytes, 0, lengthArray)) {
                    return;
                }

                //No byte may have the value (byte)0 or lie in the range (byte)0xf0 - (byte)0xff
                if (bytes != null) {
                    for (int i = 0; i < bytes.length; ++i) {
                        byte aByte = bytes[i];

                        if (aByte == 0x00) {
                            bytes[i] = 0x20; //Put a space char for the invalid byte
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "ConstUtf8Info{" +
                "Str=" + getAsNewString() +
                ", length=" + getLength() +
                '}';
    }
}
