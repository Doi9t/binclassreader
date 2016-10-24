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

package org.binclassreader;

import org.binclassreader.enums.BytecodeInstructionEnum;
import org.binclassreader.enums.ClassHelperEnum;
import org.binclassreader.utils.KeyValueHolder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Yannick on 9/28/2016.
 */
public class KeyValueHolderTest {
    @Test
    public void build() throws Exception {
        BytecodeInstructionEnum iconst3 = BytecodeInstructionEnum.ICONST_3;
        List<String> iconstValues = Arrays.asList("ICONST_3", "ICONST_3_2", "ICONST_3_2_3");

        KeyValueHolder<Enum, String> holder = new KeyValueHolder<Enum, String>();
        holder.addPair(ClassHelperEnum.DESCRIPTOR, "decp1");
        holder.addPair(ClassHelperEnum.NAME, "name");
        holder.addPair(BytecodeInstructionEnum.FMUL, "FMUL");

        for (String iconstValue : iconstValues) {
            holder.addPair(iconst3, iconstValue);
        }

        Assert.assertEquals(holder.getValues(iconst3), iconstValues);
        Assert.assertEquals(holder.getAllValues().size(), 6);
    }
}