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
import org.binclassreader.annotations.PoolItemIndex;
import org.binclassreader.utils.BaseUtils;

/**
 * Created by Yannick on 1/25/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.10
public class ConstInvokeDynamicInfo {
    @BinClassParser(byteToRead = 2)
    private short[] bootstrap_method_attr_index;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private short[] name_and_type_index;

    @PoolItemIndex
    public int getBootstrapMethodAttrIndex() {
        return BaseUtils.combineBytesToInt(bootstrap_method_attr_index);
    }

    @PoolItemIndex(mustBeOfType = ConstNameAndTypeInfo.class)
    public int getNameAndTypeIndex() {
        return BaseUtils.combineBytesToInt(name_and_type_index);
    }

    @Override
    public String toString() {
        return "ConstInvokeDynamicInfo{" +
                "bootstrapMethodAttrIndex=" + getBootstrapMethodAttrIndex() +
                ", nameAndTypeIndex=" + getNameAndTypeIndex() +
                '}';
    }
}
