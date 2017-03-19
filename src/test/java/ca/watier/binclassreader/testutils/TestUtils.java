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

package ca.watier.binclassreader.testutils;

import ca.watier.binclassreader.abstracts.AbstractAttribute;
import ca.watier.binclassreader.abstracts.AbstractIterableAttribute;
import ca.watier.binclassreader.attributes.CodeAttr;
import ca.watier.binclassreader.attributes.ElementPair;
import ca.watier.binclassreader.attributes.VisibleAndInvisibleAnnotationsAttr;
import ca.watier.binclassreader.enums.AnnotationEnum;
import ca.watier.binclassreader.enums.AttributeTypeEnum;
import ca.watier.binclassreader.enums.ClassHelperEnum;
import ca.watier.binclassreader.enums.MethodAccessFlagsEnum;
import ca.watier.binclassreader.services.ClassHelperService;
import ca.watier.binclassreader.structs.*;
import ca.watier.binclassreader.tree.TreeElement;
import ca.watier.binclassreader.utils.BaseUtils;
import ca.watier.binclassreader.utils.ClassUtil;
import ca.watier.binclassreader.utils.KeyValueHolder;
import ca.watier.multiarraymap.MultiArrayMap;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMember;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.annotation.*;

import java.util.*;

import static ca.watier.binclassreader.utils.BaseUtils.safeList;

/**
 * Created by Yannick on 10/23/2016.
 */
public class TestUtils {
    private static Map<Integer, Object> CONST_POOL = ClassHelperService.getConstPool();

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

        if (members.length != holders.size()) {
            return false;
        }

        boolean isAllSame = true;

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
            CtMember currentCtMember = getCtMemberFromName(utfName.getAsNewString(), utfDescriptor.getAsNewString(), members);


            isAllSame &= isAnnotationEquals(attributes, currentCtMember);
        }

        return isAllSame && members.length == holders.size();
    }

    /**
     * Return the CtMember based on the name and the descriptor
     *
     * @param name
     * @param descriptor
     * @param members
     * @return
     */
    private static CtMember getCtMemberFromName(String name, String descriptor, CtMember[] members) {

        CtMember currentCtMember = null;

        for (CtMember ctMember : members) {
            if (ctMember.getSignature().equals(descriptor) && ctMember.getName().equals(name)) {
                currentCtMember = ctMember;
                break;
            }
        }

        return currentCtMember;
    }

    private static boolean isAnnotationEquals(MultiArrayMap<Class<?>, AbstractAttribute> attributes, CtMember ctMember) {

        boolean isEquals = true;

        if (attributes != null && !attributes.isEmpty()) {
            mainLoop:
            for (Map.Entry<Class<?>, List<AbstractAttribute>> entry : attributes.entrySet()) {
                for (AbstractAttribute attribute : entry.getValue()) {
                    if (attribute instanceof AbstractIterableAttribute) {
                        if (entry.getKey().equals(VisibleAndInvisibleAnnotationsAttr.class)) {
                            VisibleAndInvisibleAnnotationsAttr currentAttribute = (VisibleAndInvisibleAnnotationsAttr) attribute;
                            for (VisibleAndInvisibleAnnotationsAttr.Annotation innerAnnotation : BaseUtils.safeList(currentAttribute.getItems())) {

                                AttributeTypeEnum attributeType = innerAnnotation.getAttributeType();
                                String tag = attributeType.getAttributeName();
                                AnnotationsAttribute attributeInfo = null;


                                if (ctMember instanceof CtField) {
                                    CtField ctMember1 = (CtField) ctMember;
                                    attributeInfo = (AnnotationsAttribute) ctMember1.getFieldInfo2().getAttribute(tag);
                                } else if (ctMember instanceof CtMethod) {
                                    attributeInfo = (AnnotationsAttribute) ((CtMethod) ctMember).getMethodInfo2().getAttribute(tag);
                                }

                                ConstUtf8Info constUtf8InfoName = BaseUtils.as(CONST_POOL.get(innerAnnotation.getTypeIndex() - 1), ConstUtf8Info.class);
                                String completeName = ClassUtil.getBinaryPath(constUtf8InfoName.getAsNewString());
                                completeName = completeName.substring(1, completeName.length() - 1);

                                List<ElementPair> elementPairs = innerAnnotation.getElementPairs();
                                List<Annotation> annotationList = (List<Annotation>) BaseUtils.toList(attributeInfo.getAnnotations());
                                List<Object> elementPairValues = new ArrayList<Object>();

                                for (ElementPair elementPair : elementPairs) {
                                    AnnotationEnum annotationEnum = elementPair.getAnnotationEnum();
                                    if (AnnotationEnum.ARRAY_VALUE.equals(annotationEnum)) {
                                        ElementPair.ItemContainer value = elementPair.getValue();

                                        MultiArrayMap<Integer, ElementPair.ItemContainer> objValue = (MultiArrayMap<Integer, ElementPair.ItemContainer>) value.getObjValue();

                                        for (Map.Entry<Integer, List<ElementPair.ItemContainer>> integerListEntry : objValue.entrySet()) {
                                            elementPairValues.add(integerListEntry.getValue());
                                        }

                                    } else {
                                        elementPairValues.add(elementPair.getValue());
                                    }
                                }

                                Annotation currentAnnotation = getAnnotationFromName(completeName, annotationList);

                                if (currentAnnotation == null) {
                                    return false;
                                }

                                //Compare the values of the current annotation
                                for (String memberName : (Set<String>) currentAnnotation.getMemberNames()) {

                                    MemberValue memberValue = currentAnnotation.getMemberValue(memberName);
                                    List<MemberValue> memberValues = new ArrayList<MemberValue>();

                                    if (memberValue instanceof ArrayMemberValue) {
                                        memberValues.addAll(Arrays.asList(((ArrayMemberValue) memberValue).getValue()));
                                    } else {
                                        memberValues.add(memberValue);
                                    }

                                    //For each values in the current member
                                    for (MemberValue innerMemberValue : memberValues) {
                                        boolean isCurrentEquals = false;
                                        for (Object elementPairValue : elementPairValues) {
                                            Object obj = null;

                                            if (elementPairValue instanceof ArrayList) {
                                                List<ElementPair.ItemContainer> contValue = (List<ElementPair.ItemContainer>) elementPairValue;

                                                if (contValue == null || contValue.isEmpty()) {
                                                    continue;
                                                }

                                                obj = contValue.get(0);
                                            }


                                            if (obj instanceof ElementPair.ItemContainer) {
                                                obj = ElementPair.getLeafItem(((ElementPair.ItemContainer) obj).getObjValue());
                                            } else if (elementPairValue instanceof VisibleAndInvisibleAnnotationsAttr) {
                                                throw new UnsupportedOperationException();
                                            } else if (elementPairValue instanceof ConstIntegerInfo) {
                                                ConstIntegerInfo value = (ConstIntegerInfo) elementPairValue;
                                                obj = value.getIntValue();
                                            } else if (elementPairValue instanceof ConstLongInfo) {
                                                ConstLongInfo value = (ConstLongInfo) elementPairValue;
                                                obj = value.getLongValue();
                                            } else if (elementPairValue instanceof ConstFloatInfo) {
                                                ConstFloatInfo value = (ConstFloatInfo) elementPairValue;
                                                obj = value.getFloatValue();
                                            } else if (elementPairValue instanceof ConstDoubleInfo) {
                                                ConstDoubleInfo value = (ConstDoubleInfo) elementPairValue;
                                                obj = value.getDoubleValue();
                                            } else if (elementPairValue instanceof List) {
                                                System.out.println();
                                            } else {
                                                throw new UnsupportedOperationException("Unable to find the input type !");
                                            }

                                            if (obj instanceof Number) {
                                                String numberStrRep = obj.toString();

                                                if (innerMemberValue instanceof DoubleMemberValue) {
                                                    DoubleMemberValue annotationMember = (DoubleMemberValue) innerMemberValue;

                                                    if (Double.valueOf(numberStrRep).equals(annotationMember.getValue())) {
                                                        isCurrentEquals = true;
                                                        break;
                                                    }
                                                } else if (innerMemberValue instanceof FloatMemberValue) {
                                                    FloatMemberValue annotationMember = (FloatMemberValue) innerMemberValue;

                                                    if (Float.valueOf(numberStrRep).equals(annotationMember.getValue())) {
                                                        isCurrentEquals = true;
                                                        break;
                                                    }
                                                } else if (innerMemberValue instanceof IntegerMemberValue) {
                                                    IntegerMemberValue annotationMember = (IntegerMemberValue) innerMemberValue;

                                                    if (Integer.valueOf(numberStrRep).equals(annotationMember.getValue())) {
                                                        isCurrentEquals = true;
                                                        break;
                                                    }
                                                } else if (innerMemberValue instanceof LongMemberValue) {
                                                    LongMemberValue annotationMember = (LongMemberValue) innerMemberValue;

                                                    if (Long.valueOf(numberStrRep).equals(annotationMember.getValue())) {
                                                        isCurrentEquals = true;
                                                        break;
                                                    }
                                                } else if (innerMemberValue instanceof ShortMemberValue) {
                                                    ShortMemberValue annotationMember = (ShortMemberValue) innerMemberValue;

                                                    if (Short.valueOf(numberStrRep).equals(annotationMember.getValue())) {
                                                        isCurrentEquals = true;
                                                        break;
                                                    }
                                                } else if (innerMemberValue instanceof ByteMemberValue) {
                                                    ByteMemberValue annotationMember = (ByteMemberValue) innerMemberValue;

                                                    if (Byte.valueOf(numberStrRep).equals(annotationMember.getValue())) {
                                                        isCurrentEquals = true;
                                                        break;
                                                    }
                                                } else if (innerMemberValue instanceof CharMemberValue) {
                                                    CharMemberValue annotationMember = (CharMemberValue) innerMemberValue;
                                                    char value = annotationMember.getValue();

                                                    if ((int) value == Integer.valueOf(numberStrRep)) {
                                                        isCurrentEquals = true;
                                                        break;
                                                    }
                                                } else if (innerMemberValue instanceof BooleanMemberValue) {
                                                    BooleanMemberValue annotationMember = (BooleanMemberValue) innerMemberValue;
                                                    boolean value = annotationMember.getValue();

                                                    try {
                                                        Integer integer = Integer.valueOf(numberStrRep);

                                                        if (integer == 0 && !value || integer == 1 && value) {
                                                            isCurrentEquals = true;
                                                            break;
                                                        }
                                                    } catch (NumberFormatException nfe) {
                                                        System.err.println("Unable to convert " + numberStrRep + " to integer !");
                                                    }
                                                }
                                            } else {
                                                if (innerMemberValue instanceof AnnotationMemberValue) {
                                                    //TODO
                                                    AnnotationMemberValue annotationMember = (AnnotationMemberValue) innerMemberValue;
                                                    throw new UnsupportedOperationException();
                                                } else if (innerMemberValue instanceof ClassMemberValue) {
                                                    //TODO
                                                    ClassMemberValue annotationMember = (ClassMemberValue) innerMemberValue;
                                                    throw new UnsupportedOperationException();
                                                } else if (innerMemberValue instanceof EnumMemberValue && obj instanceof ElementPair.EnumValue) {

                                                    ElementPair.EnumValue enumValue = (ElementPair.EnumValue) obj;
                                                    EnumMemberValue annotationMember = (EnumMemberValue) innerMemberValue;

                                                    ConstUtf8Info constName = (ConstUtf8Info) CONST_POOL.get(enumValue.getConstNameIndex() - 1);
                                                    ConstUtf8Info constTypeName = (ConstUtf8Info) CONST_POOL.get(enumValue.getTypeNameIndex() - 1);

                                                    String parsedConstName = constName.getAsNewString();
                                                    String rawConstTypeName = constTypeName.getAsNewString();
                                                    String parsedConstTypeName = ClassUtil.getBinaryPath(rawConstTypeName).substring(1, rawConstTypeName.length() - 1);

                                                    String constNameAssist = annotationMember.getValue();
                                                    String constTypeNameAssist = annotationMember.getType();

                                                    if (parsedConstTypeName.equals(constTypeNameAssist) && parsedConstName.equals(constNameAssist)) {
                                                        isCurrentEquals = true;
                                                        break;
                                                    }

                                                } else if (innerMemberValue instanceof StringMemberValue) {
                                                    StringMemberValue annotationMember = (StringMemberValue) innerMemberValue;
                                                    String value = annotationMember.getValue();

                                                    ConstUtf8Info constUtf8Info = (ConstUtf8Info) obj;

                                                    if (constUtf8Info.getAsNewString().equals(value)) {
                                                        isCurrentEquals = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        if (!isCurrentEquals) {
                                            isEquals = false;
                                            break mainLoop;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return isEquals;
    }

    private static Annotation getAnnotationFromName(String completeName, List<Annotation> annotationList) {

        if (annotationList == null || annotationList.isEmpty()) {
            return null;
        }

        Annotation currentAnnotation = null;

        for (Annotation annotation : annotationList) {

            if (completeName.equals(annotation.getTypeName())) {
                currentAnnotation = annotation;
                break;
            }
        }

        return currentAnnotation;
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
