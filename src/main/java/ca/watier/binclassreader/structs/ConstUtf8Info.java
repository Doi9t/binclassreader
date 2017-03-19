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

import ca.watier.binclassreader.abstracts.Readable;
import ca.watier.binclassreader.annotations.BinClassParser;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.utils.BaseUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Yannick on 1/25/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.7
public class ConstUtf8Info extends Readable {

    @BinClassParser(byteToRead = 2)
    private byte[] length;

    private byte[] bytes;

    public int getLength() {
        return BaseUtils.combineBytesToInt(length);
    }

    public byte[] getAsBytes() {
        return BaseUtils.safeArrayClone(bytes);
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

    public void afterFieldsInitialized(ClassReader reader) {
        int lengthArray = BaseUtils.combineBytesToInt(length);

        if (lengthArray > 0) {

            try {
                byte[] read = reader.readFromCurrentStream(lengthArray);

                if (read == null || lengthArray != read.length) {
                    return;
                }

                bytes = new byte[lengthArray];

                //No byte may have the value (byte)0 or lie in the range (byte)0xf0 - (byte)0xff
                for (int i = 0; i < read.length; ++i) {
                    int aByte = read[i];
                    if (aByte == (byte) 0x00) {
                        bytes[i] = 0x20; //Put a space char for the invalid byte
                    } else {
                        bytes[i] = (byte) aByte;
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
