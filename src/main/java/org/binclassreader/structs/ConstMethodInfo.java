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

import org.binclassreader.abstracts.Readable;
import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.annotations.PoolItemIndex;
import org.binclassreader.attributes.CodeAttr;
import org.binclassreader.enums.ClassHelperEnum;
import org.binclassreader.enums.MethodAccessFlagsEnum;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.tree.Tree;
import org.binclassreader.tree.TreeElement;
import org.binclassreader.utils.BaseUtils;
import org.binclassreader.utils.MethodUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 4/18/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6
public class ConstMethodInfo extends Readable {

    @BinClassParser(byteToRead = 2)
    private short[] access_flags;

    @BinClassParser(readOrder = 2, byteToRead = 2)
    private short[] name_index;

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private short[] descriptor_index;

    @BinClassParser(readOrder = 4, byteToRead = 2)
    private short[] attributes_count;

    private CodeAttr codeAttr;

    private boolean isSpecialMethod;

    public ConstMethodInfo() {
        isSpecialMethod = false;
    }

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
        return MethodAccessFlagsEnum.getFlagsByMask((short) BaseUtils.combineBytesToInt(access_flags));
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
        return "ConstMethodInfo{" +
                "accessFlags=" + getAccessFlags() +
                ", nameIndex=" + getNameIndex() +
                ", descriptorIndex=" + getDescriptorIndex() +
                ", attributesCount=" + getAttributesCount() +
                '}';
    }

    public void afterFieldsInitialized(ClassReader reader) {
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

    @Override
    public void afterTreeIsBuilt(Tree tree) {
        if (tree == null) {
            return;
        }

        TreeElement root = tree.getRoot();

        if (root == null) {
            return;
        }

        for (TreeElement treeElement : BaseUtils.safeList(root.getChild())) {

            if (ClassHelperEnum.NAME.equals(treeElement.getMappingType())) {

                Object current = treeElement.getCurrent();

                if (current != null && MethodUtils.isSpecialMethod(((ConstUtf8Info) current).getAsNewString())) {
                    isSpecialMethod = true;
                }

                break;
            }
        }

    }

    public boolean isSpecialMethod() {
        return isSpecialMethod;
    }

    public CodeAttr getCodeAttr() {
        return codeAttr;
    }
}