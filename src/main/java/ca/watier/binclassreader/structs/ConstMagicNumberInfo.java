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
import ca.watier.binclassreader.utils.BaseUtils;

import java.util.Arrays;

/**
 * Created by Yannick on 1/26/2016.
 */
public class ConstMagicNumberInfo {

    @BinClassParser(byteToRead = 4)
    private byte[] bytes_data;

    public byte[] getBytes_data() {
        return BaseUtils.safeArrayClone(bytes_data);
    }

    @Override
    public String toString() {
        return "ConstMagicNumberInfo{" +
                "bytes_data=" + Arrays.toString(getBytes_data()) +
                '}';
    }
}
