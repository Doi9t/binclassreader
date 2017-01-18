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
package org.binclassreader.testclasses;

import javax.xml.ws.Action;
import javax.xml.ws.soap.MTOM;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Yannick on 5/15/2016.
 */
@MTOM
@TestAnnotations.GlobalAnnotationSource
@TestAnnotations.GlobalAnnotationRuntime
@TestAnnotations.GlobalAnnotationClass
@TestAnnotations.GlobalAnnotationWithParameters(
        stringArray = {"Class annotation", "arrayParam", "arrayParam2", "arrayParam3"},
        integerArray = {1, 2, 385644898},
        bool = false,
        annotation = ElementType.TYPE, clazz = Integer.class)
public class TestAnnotations {

    @GlobalAnnotationWithParameters(stringArray = {"Class variable annotation [1]", "Class variable annotation [2]"})
    private transient Object x, y;
    private Object z;

    public TestAnnotations() {
        @TestAnnotations.GlobalAnnotationWithParameters(stringArray = {"Member variable annotation"})
        Object obj = new Object();
        returnFunction(10, this);
    }

    @Deprecated
    @Action
    @MTOM
    public Object returnFunction(@GlobalAnnotationWithParameters(stringArray = {"Parameter function variable annotation"}) Integer x, final Object y) {

        if (x == 5000)
            return Integer.MAX_VALUE;

        this.x = x;
        this.y = y;

        z = new Object();

        return returnFunction(x, y);
    }

    @Retention(RetentionPolicy.SOURCE)
    @GlobalAnnotationWithParameters(stringArray = {"Annotation on annotation"})
    public @interface GlobalAnnotationSource {
    }

    @Retention(RetentionPolicy.CLASS)
    public @interface GlobalAnnotationClass {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface GlobalAnnotationRuntime {
    }

    public @interface GlobalAnnotationWithParameters {
        @GlobalAnnotationWithParameters(stringArray = {"Annotation element annotation"}) String[] stringArray() default {};

        int[] integerArray() default {};

        boolean bool() default true;

        ElementType annotation() default ElementType.CONSTRUCTOR;

        Class<?> clazz() default Object.class;
    }
}


