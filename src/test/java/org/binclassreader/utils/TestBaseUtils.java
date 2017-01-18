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

package org.binclassreader.utils;

import javassist.CtClass;
import javassist.CtField;
import javassist.CtMember;
import javassist.CtMethod;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ExceptionTable;
import org.binclassreader.abstracts.AbstractAttribute;
import org.binclassreader.abstracts.AbstractIterableAttribute;
import org.binclassreader.attributes.CodeAttr;
import org.binclassreader.attributes.VisibleAndInvisibleAnnotationsAttr;
import org.binclassreader.enums.AttributeTypeEnum;
import org.binclassreader.enums.ClassHelperEnum;
import org.binclassreader.enums.MethodAccessFlagsEnum;
import org.binclassreader.structs.ConstUtf8Info;
import org.binclassreader.tree.TreeElement;
import org.multiarraymap.MultiArrayMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.binclassreader.utils.BaseUtils.safeList;

/**
 * Created by Yannick on 10/23/2016.
 */
public class TestBaseUtils {

    public static List<String> extractInterfaceFromCtClass(CtClass... classes) {
        List<String> values = new ArrayList<String>();

        if (classes == null || classes.length == 0) {
            return values;
        }

        for (CtClass aClass : classes) {
            values.add(aClass.getName());
        }

        return values;
    }

    /**
     * Compare the prototype of the methods / fields
     *
     * @param holders
     * @param members
     * @return
     */
    public static boolean deepCtMemberComparator(List<KeyValueHolder<ClassHelperEnum, Object>> holders, CtMember[] members) {
        boolean isAll = true, isCurrent;

        for (KeyValueHolder<ClassHelperEnum, Object> holder : holders) {

            ConstUtf8Info utfName = null;
            ConstUtf8Info utfDescriptor = null;

            Object firstMatchingValueName = holder.getFirstMatchingValue(ClassHelperEnum.NAME);
            Object firstMatchingValueDescriptor = holder.getFirstMatchingValue(ClassHelperEnum.DESCRIPTOR);

            if (firstMatchingValueName instanceof TreeElement) {
                utfName = (ConstUtf8Info) ((TreeElement) firstMatchingValueName).getCurrent();
            } else if (firstMatchingValueName instanceof ConstUtf8Info) {
                utfName = (ConstUtf8Info) firstMatchingValueName;
            }

            if (firstMatchingValueDescriptor instanceof TreeElement) {
                utfDescriptor = (ConstUtf8Info) ((TreeElement) firstMatchingValueDescriptor).getCurrent();
            } else if (firstMatchingValueDescriptor instanceof ConstUtf8Info) {
                utfDescriptor = (ConstUtf8Info) firstMatchingValueDescriptor;
            }

            if (utfDescriptor == null || utfName == null) {
                continue;
            }

            MultiArrayMap<Class<?>, AbstractAttribute> attributes = (MultiArrayMap<Class<?>, AbstractAttribute>) holder.getFirstMatchingValue(ClassHelperEnum.OTHER_ATTR);

            isCurrent = false;
            for (CtMember ctMember : members) {
                if (ctMember.getSignature().equals(utfDescriptor.getAsNewString()) && ctMember.getName().equals(utfName.getAsNewString())) {
                    if (attributes != null && !attributes.isEmpty()) {
                        for (Map.Entry<Class<?>, List<AbstractAttribute>> entry : attributes.entrySet()) {
                            for (AbstractAttribute attribute : entry.getValue()) {
                                if (attribute instanceof AbstractIterableAttribute) {
                                    if (entry.getKey().equals(VisibleAndInvisibleAnnotationsAttr.class)) {
                                        VisibleAndInvisibleAnnotationsAttr currentAttribute = (VisibleAndInvisibleAnnotationsAttr) attribute;
                                        for (VisibleAndInvisibleAnnotationsAttr.Annotation innerAnnotation : BaseUtils.safeList(currentAttribute.getItems())) {

                                            AttributeTypeEnum attributeType = innerAnnotation.getAttributeType();
                                            String tag = attributeType.getAttributeName();


                                            AttributeInfo attributeInfo = null;

                                            if (ctMember instanceof CtField) {
                                                attributeInfo = ((CtField) ctMember).getFieldInfo2().getAttribute(tag);
                                            } else if (ctMember instanceof CtMethod) {
                                                attributeInfo = ((CtMethod) ctMember).getMethodInfo2().getAttribute(tag);
                                            }

                                            System.out.println(attributeInfo);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    isCurrent = true;
                    break;
                }
            }
            isAll &= isCurrent;
        }

        return isAll && members.length == holders.size();
    }

    public static boolean deepMethodComparator(List<KeyValueHolder<ClassHelperEnum, Object>> methods, CtMethod[] ctMethods) {
        boolean value = true;

        if (methods == null || ctMethods == null || ctMethods.length != methods.size()) {
            return !value;
        }

        for (KeyValueHolder<ClassHelperEnum, Object> keyValueHolder : methods) {

            CodeAttr codeAttr = (CodeAttr) keyValueHolder.getFirstMatchingValue(ClassHelperEnum.CODE_ATTR);
            String name = ((ConstUtf8Info) keyValueHolder.getFirstMatchingValue(ClassHelperEnum.NAME)).getAsNewString();
            String descriptor = ((ConstUtf8Info) keyValueHolder.getFirstMatchingValue(ClassHelperEnum.DESCRIPTOR)).getAsNewString();
            List<MethodAccessFlagsEnum> accessFlags = (List<MethodAccessFlagsEnum>) keyValueHolder.getFirstMatchingValue(ClassHelperEnum.ACCESS_FLAGS);
            List<Short> rawBytecode;
            List<CodeAttr.ExceptionTableItem> exceptionsTable;
            List<String> methodAttributeName = null;

            if (codeAttr != null) {
                rawBytecode = codeAttr.getRawBytecode();
                exceptionsTable = codeAttr.getExceptionTable();
                methodAttributeName = new ArrayList<String>();

                for (AbstractAttribute o : safeList(codeAttr.getAttributes())) {

                    if (o instanceof AbstractIterableAttribute) {
                        AttributeTypeEnum attributeType = o.getAttributeType();
                        methodAttributeName.add((attributeType != null) ? attributeType.getAttributeName() : null);
                    }
                }
            } else {
                rawBytecode = new ArrayList<Short>();
                exceptionsTable = new ArrayList<CodeAttr.ExceptionTableItem>();
            }

            for (CtMethod ctMember : ctMethods) {
                String ctMemberDescriptor = ctMember.getSignature();
                String ctName = ctMember.getName();
                List<String> methodAttributeNameJavaAssist = null;

                if (ctName.equals(name) && ctMemberDescriptor.equals(descriptor)) {
                    CodeAttribute codeAttribute = ctMember.getMethodInfo2().getCodeAttribute();

                    ExceptionTable exceptionTable = null;
                    byte[] code = null;

                    if (codeAttribute != null) {
                        code = codeAttribute.getCode();
                        exceptionTable = codeAttribute.getExceptionTable();
                        methodAttributeNameJavaAssist = new ArrayList<String>();

                        for (AttributeInfo o : (List<AttributeInfo>) safeList(codeAttribute.getAttributes())) {
                            methodAttributeNameJavaAssist.add(o.getName());
                        }
                    }

                    List<Short> list = BaseUtils.shortArrayToList(BaseUtils.convertByteToUnsigned(code));
                    int ctRawAccessFlags = ctMember.getModifiers();
                    short mask = MethodAccessFlagsEnum.getMask(accessFlags);

                    boolean isExceptionsEquals = true;
                    for (int i = 0; i < exceptionsTable.size(); i++) {
                        CodeAttr.ExceptionTableItem exceptionTableItem = exceptionsTable.get(i);
                        isExceptionsEquals &= exceptionTableItem.compareValues(
                                exceptionTable.startPc(i),
                                exceptionTable.endPc(i),
                                exceptionTable.handlerPc(i),
                                exceptionTable.catchType(i));
                    }


                    methodAttributeName = safeList(methodAttributeName);
                    Collections.sort(methodAttributeName);
                    methodAttributeNameJavaAssist = safeList(methodAttributeNameJavaAssist);
                    Collections.sort(methodAttributeNameJavaAssist);


                    value &= (list != null && rawBytecode != null && list.equals(rawBytecode) || list == null && rawBytecode == null) //Compare the bytecode
                            && mask == ctRawAccessFlags //Compare the access flag mask
                            && isExceptionsEquals //Compare the exception tables
                            && methodAttributeName.size() == methodAttributeNameJavaAssist.size(); //Compare size of the attributes (to ignore the unimplemented)
                }
            }
        }
        return value;
    }
}
