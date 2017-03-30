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

import ca.watier.binclassreader.abstracts.AbstractLongDoubleConst;
import ca.watier.binclassreader.utils.BaseUtils;

/**
 * Created by Yannick on 1/25/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5
public class ConstDoubleInfo extends AbstractLongDoubleConst {

    public Double getDoubleValue() {

        byte[] high_bytes_data = getHigh_bytes_data();
        byte[] low_bytes_data = getLow_bytes_data();

        byte[] arr = new byte[8];
        arr[0] = high_bytes_data[0];
        arr[1] = high_bytes_data[1];
        arr[2] = high_bytes_data[2];
        arr[3] = high_bytes_data[3];
        arr[4] = low_bytes_data[0];
        arr[5] = low_bytes_data[1];
        arr[6] = low_bytes_data[2];
        arr[7] = low_bytes_data[3];

        return BaseUtils.combineBytesToDouble(arr);
    }

    @Override
    public String toString() {
        return "ConstDoubleInfo{ Double = " + getDoubleValue() + "}";
    }
}
