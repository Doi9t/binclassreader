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
import org.binclassreader.attributes.CodeAttr;
import org.binclassreader.enums.MethodAccessFlagsEnum;
import org.binclassreader.interfaces.SelfReader;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 4/18/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6
public class ConstMethodInfo implements SelfReader {

    @BinClassParser(byteToRead = 2)
    private int[] access_flags;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private int[] name_index;

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private int[] descriptor_index;

    @BinClassParser(readOrder = 4, byteToRead = 2)
    private int[] attributes_count;

    private CodeAttr codeAttr;


    /*
        ----------------------------------Attributes----------------------------------
        Code......................................................................45.3
        Exceptions................................................................45.3
        RuntimeVisibleParameterAnnotations, RuntimeInvisibleParameterAnnotations..49.0
        AnnotationDefault.........................................................49.0
        MethodParameters..........................................................52.0
        Synthetic.................................................................45.3
        Deprecated................................................................45.3
        Signature.................................................................49.0
        RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations....................49.0
        RuntimeVisibleTypeAnnotations, RuntimeInvisibleTypeAnnotations............52.0
        ------------------------------------------------------------------------------
     */

    private List<AttributesInfo> attList;

    public List<MethodAccessFlagsEnum> getAccessFlags() {
        return MethodAccessFlagsEnum.getFlagsByMask((byte) Utilities.combineBytesToInt(access_flags));
    }

    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class)
    public int getNameIndex() {
        return Utilities.combineBytesToInt(name_index);
    }

    @PoolItemIndex(mustBeOfType = ConstUtf8Info.class)
    public int getDescriptorIndex() {
        return Utilities.combineBytesToInt(descriptor_index);
    }

    public int getAttributesCount() {
        return Utilities.combineBytesToInt(attributes_count);
    }

    @Override
    public String toString() {
        return "ConstMethodInfo{" +
                "accessFlags=" + getAccessFlags() +
                ", nameIndex=" + getNameIndex() +
                ", descriptorIndex=" + getDescriptorIndex() +
                ", attributesCount=" + getAttributesCount() +
                '}';
    }

    public void initReading(ClassReader reader) {
        attList = new ArrayList<AttributesInfo>();

        List<MethodAccessFlagsEnum> flags = getAccessFlags();
        boolean isCodeAttrPresent = flags != null && !flags.contains(MethodAccessFlagsEnum.UNKNOWN) && //Code Attribute
                !flags.contains(MethodAccessFlagsEnum.ACC_NATIVE) &&
                !flags.contains(MethodAccessFlagsEnum.ACC_ABSTRACT);

        if (isCodeAttrPresent) {
            codeAttr = reader.read(new CodeAttr());
        }

        int attributesCount = getAttributesCount();
        attributesCount = ((isCodeAttrPresent) ? attributesCount - 1 : attributesCount);

        if (attributesCount >= 0) {
            for (int i = 0; i < attributesCount; i++) {
                attList.add(reader.read(new AttributesInfo()));
            }
        }
    }
}