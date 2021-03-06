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

import ca.watier.binclassreader.abstracts.AbstractAttribute;
import ca.watier.binclassreader.abstracts.Readable;
import ca.watier.binclassreader.annotations.BinClassParser;
import ca.watier.binclassreader.annotations.PoolItemIndex;
import ca.watier.binclassreader.enums.ClassHelperEnum;
import ca.watier.binclassreader.enums.FieldAccessFlagsEnum;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.utils.AttributeUtils;
import ca.watier.binclassreader.utils.BaseUtils;
import ca.watier.multiarraymap.MultiArrayMap;

import java.io.IOException;
import java.util.List;

/**
 * Created by Yannick on 1/30/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.5
public class ConstFieldInfo extends Readable {

    @BinClassParser(byteToRead = 2)
    private byte[] access_flags;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private byte[] name_index;

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private byte[] descriptor_index;

    @BinClassParser(readOrder = 4, byteToRead = 2)
    private byte[] attributes_count;

    private MultiArrayMap<Class<?>, AbstractAttribute> attList;

    public List<FieldAccessFlagsEnum> getAccessFlags() {
        return FieldAccessFlagsEnum.getFlagsByMask((short) BaseUtils.combineBytesToInt(access_flags));
    }

    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class, type = ClassHelperEnum.NAME)
    public int getNameIndex() {
        return BaseUtils.combineBytesToInt(name_index);
    }

    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class, type = ClassHelperEnum.DESCRIPTOR)
    public int getDescriptorIndex() {
        return BaseUtils.combineBytesToInt(descriptor_index);
    }

    public int getAttributesCount() {
        return BaseUtils.combineBytesToInt(attributes_count);
    }

    @Override
    public String toString() {
        return "ConstFieldInfo{" +
                "accessFlags=" + getAccessFlags() +
                ", nameIndex=" + getNameIndex() +
                ", descriptorIndex=" + getDescriptorIndex() +
                ", attributesCount=" + getAttributesCount() +
                '}';
    }

    public MultiArrayMap<Class<?>, AbstractAttribute> getAttributesList() {
        return attList;
    }

    public void afterFieldsInitialized(ClassReader reader) {
        attList = new MultiArrayMap<Class<?>, AbstractAttribute>();

        int attributesCount = getAttributesCount();
        for (int i = 0; i < attributesCount; i++) {
            try {
                AbstractAttribute nextAttribute = AttributeUtils.getNextAttribute(reader);
                attList.put(nextAttribute.getClass(), nextAttribute);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
