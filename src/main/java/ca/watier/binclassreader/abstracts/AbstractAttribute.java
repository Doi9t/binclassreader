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
import ca.watier.binclassreader.enums.AttributeTypeEnum;
import ca.watier.binclassreader.structs.ConstUtf8Info;
import ca.watier.binclassreader.utils.BaseUtils;

/**
 * Created by Yannick on 5/23/2016.
 */
public class AbstractAttribute extends Readable {

    protected AttributeTypeEnum attributeName = null;

    @BinClassParser(manualMode = true)
    private byte[] name_index;

    @BinClassParser(readOrder = 2, byteToRead = 4)
    private byte[] length;

    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class)
    public int getNameIndex() {
        return BaseUtils.combineBytesToInt(name_index);
    }

    public int getLength() {
        return BaseUtils.combineBytesToInt(length);
    }

    public AttributeTypeEnum getAttributeType() {
        return attributeName;
    }

    public void setAttributeType(AttributeTypeEnum attributeName) {
        this.attributeName = attributeName;
    }
}
