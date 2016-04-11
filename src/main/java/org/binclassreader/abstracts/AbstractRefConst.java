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

package org.binclassreader.abstracts;

import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.annotations.ConstPoolItemValidation;
import org.binclassreader.structs.ConstClassInfo;
import org.binclassreader.structs.ConstNameAndTypeInfo;
import org.binclassreader.utils.Utilities;

/**
 * Created by Yannick on 1/25/2016.
 */
public abstract class AbstractRefConst {

    @BinClassParser(readOrder = 1, byteToRead = 2)
    protected int[] class_index;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    protected int[] name_and_type_index;

    @ConstPoolItemValidation(mustBeOfType = ConstClassInfo.class)
    protected int getClassIndex() {
        return Utilities.combineBytesToInt(class_index);
    }

    @ConstPoolItemValidation(mustBeOfType = ConstNameAndTypeInfo.class)
    protected int getNameAndTypeIndex() {
        return Utilities.combineBytesToInt(name_and_type_index);
    }
}