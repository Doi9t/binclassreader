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

package ca.watier.binclassreader.enums;

/**
 * Created by Yannick on 11/7/2016.
 */
public enum AttributeTypeEnum {
    ANNOTATION_DEFAULT("AnnotationDefault"),
    CODE("Code"),
    BOOTSTRAP_METHODS("BootstrapMethods"),
    CONSTANT_VALUE("ConstantValue"),
    DEPRECATED("Deprecated"),
    ENCLOSING_METHOD("EnclosingMethod"),
    EXCEPTIONS("Exceptions"),
    INNER_CLASSES("InnerClasses"),
    LINE_NUMBER_TABLE("LineNumberTable"),
    LOCAL_VARIABLE_TABLE("LocalVariableTable"),
    LOCAL_VARIABLE_TYPE_TABLE("LocalVariableTypeTable"),
    METHOD_PARAMETERS("MethodParameters"),
    RUNTIME_INVISIBLE_ANNOTATIONS("RuntimeInvisibleAnnotations"),
    RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS("RuntimeInvisibleParameterAnnotations"),
    RUNTIME_INVISIBLE_TYPE_ANNOTATIONS("RuntimeInvisibleTypeAnnotations"),
    RUNTIME_VISIBLE_ANNOTATIONS("RuntimeVisibleAnnotations"),
    RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS("RuntimeVisibleParameterAnnotations"),
    RUNTIME_VISIBLE_TYPE_ANNOTATIONS("RuntimeVisibleTypeAnnotations"),
    SIGNATURE("Signature"),
    SOURCE_DEBUG_EXTENSION("SourceDebugExtension"),
    SOURCE_FILE("SourceFile"),
    STACK_MAP_TABLE("StackMapTable"),
    SYNTHETIC("Synthetic"),
    UNKNOWN("UnimplementedAttribute");

    private String attributeName;

    AttributeTypeEnum(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
