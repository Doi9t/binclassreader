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

import org.binclassreader.abstracts.AbstractIntFloatConst;
import org.binclassreader.utils.BaseUtils;

/**
 * Created by Yannick on 1/25/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.4
public class ConstFloatInfo extends AbstractIntFloatConst {

    public Float getFloatValue() {
        int bits = BaseUtils.combineBytesToInt(getBytes());
        Float value;

        if ((bits >= 0x7f800001 && bits <= 0x7fffffff) || (bits >= 0xff800001 && bits <= 0xffffffff)) { //Nan
            value = Float.NaN;
        } else {
            if (bits == 0x7f800000) { //positive infinity
                value = Float.POSITIVE_INFINITY;
            } else if (bits == 0xff800000) { //negative infinity
                value = Float.NEGATIVE_INFINITY;
            } else {
                int s = ((bits >> 31) == 0) ? 1 : -1;
                int e = ((bits >> 23) & 0xff);
                int m = (e == 0) ?
                        (bits & 0x7fffff) << 1 :
                        (bits & 0x7fffff) | 0x800000;


                value = (float) (s * m * Math.pow(2, -150));
            }
        }

        return value;
    }

    @Override
    public String toString() {
        return "ConstFloatInfo{ Float = " + getFloatValue() + "}";
    }
}
