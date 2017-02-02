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

import org.binclassreader.abstracts.AbstractLongDoubleConst;
import org.binclassreader.utils.BaseUtils;

/**
 * Created by Yannick on 1/25/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.5
public class ConstDoubleInfo extends AbstractLongDoubleConst {

    public Double getDoubleValue() {
        long bits = (BaseUtils.combineBytesToLong(getHigh_bytes_data()) << 32) + BaseUtils.combineBytesToLong(getLow_bytes_data());
        Double value;

        if ((bits >= 0x7ff0000000000001L && bits <= 0x7fffffffffffffffL) || (bits >= 0xfff0000000000001L && bits <= 0xffffffffffffffffL)) { //Nan
            value = Double.NaN;
        } else {
            if (bits == 0x7ff0000000000000L) { //positive infinity
                value = Double.POSITIVE_INFINITY;
            } else if (bits == 0xfff0000000000000L) { //negative infinity
                value = Double.NEGATIVE_INFINITY;
            } else {
                int s = ((bits >> 63) == 0) ? 1 : -1;
                int e = (int) ((bits >> 52) & 0x7ffL);
                long m = (e == 0) ?
                        (bits & 0xfffffffffffffL) << 1 :
                        (bits & 0xfffffffffffffL) | 0x10000000000000L;

                value = s * m * Math.pow(2, -1075);
            }
        }

        return value;
    }

    @Override
    public String toString() {
        return "ConstDoubleInfo{ Double = " + getDoubleValue() + "}";
    }
}
