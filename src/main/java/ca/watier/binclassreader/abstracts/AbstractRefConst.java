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

package ca.watier.binclassreader.abstracts;

import ca.watier.binclassreader.annotations.BinClassParser;
import ca.watier.binclassreader.annotations.PoolItemIndex;
import ca.watier.binclassreader.structs.ConstClassInfo;
import ca.watier.binclassreader.structs.ConstNameAndTypeInfo;
import ca.watier.binclassreader.utils.BaseUtils;

/**
 * Created by Yannick on 1/25/2016.
 */
public abstract class AbstractRefConst {

    @BinClassParser(byteToRead = 2)
    protected byte[] class_index;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    protected byte[] name_and_type_index;

    @PoolItemIndex(mustBeOfType = ConstClassInfo.class)
    public int getClassIndex() {
        return BaseUtils.combineBytesToInt(class_index);
    }

    @PoolItemIndex(mustBeOfType = ConstNameAndTypeInfo.class)
    public int getNameAndTypeIndex() {
        return BaseUtils.combineBytesToInt(name_and_type_index);
    }
}