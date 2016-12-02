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
import java.lang.annotation.Target;

/**
 * Created by Yannick on 5/15/2016.
 */
@MTOM
@TestTwo.GlobalAnnotation
@TestTwo.PackageAnnotation(stringArray = {"arrayParam", "arrayParam2", "arrayParam3"}, integerArray = {1, 2, 385644898}, bool = false, annotation = ElementType.TYPE, clazz = Integer.class)
public class TestTwo {

    @GlobalAnnotation
    private transient Object x, y;
    private Object z;

    public TestTwo() {
        Object obj = new Object();
        returnFunction(10, this);
    }

    @Deprecated
    @Action
    @MTOM
    @GlobalAnnotation
    public Object returnFunction(@GlobalAnnotation Integer x, @GlobalAnnotation final Object y) {

        if (x == 5000)
            return Integer.MAX_VALUE;

        this.x = x;
        this.y = y;

        z = new Object();

        return returnFunction(x, y);
    }


    @Retention(RetentionPolicy.RUNTIME)
    public @interface GlobalAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.PACKAGE})
    public @interface PackageAnnotation {
        String[] stringArray() default {};

        int[] integerArray() default {};

        boolean bool() default true;

        ElementType annotation() default ElementType.PACKAGE;

        Class<?> clazz() default Object.class;
    }

}


