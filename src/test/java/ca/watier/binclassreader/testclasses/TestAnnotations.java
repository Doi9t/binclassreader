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
package ca.watier.binclassreader.testclasses;

import java.lang.annotation.ElementType;

/**
 * Created by Yannick on 5/15/2016.
 */
/*
@MTOM
@TestAnnotations.GlobalAnnotationSource
@TestAnnotations.GlobalAnnotationRuntime
@TestAnnotations.GlobalAnnotationClass
@TestAnnotations.GlobalAnnotationWithParameters(
        stringArray = {"Class annotation", "arrayParam", "arrayParam2", "arrayParam3"},
        integerArray = {1, 2, 385644898},
        bool = false,
        annotation = ElementType.TYPE, clazz = Integer.class)*/
public class TestAnnotations {

    @GlobalAnnotationWithParameters(
            /*
            stringArray = {"Class variable annotation [1]", "Class variable annotation [2]", "Class variable annotation [3]"}           ,
            byteValue = 13,
            charValue = 'a',
            shortValue = 14,
            intValue = 15,
            longValue = 16,
            floatValue = 17f,
            doubleValue = 18d,
            boolValue = true,
            annotationValue = ElementType.FIELD,       */
            annotationArray = {ElementType.CONSTRUCTOR, ElementType.FIELD}


            //FIXME
            //doubleArray = {0.00003555d, 0.256415d, -1324156.545455654d, 156456465456.01d}

    )
    private transient Object x, y;
    private Object z;

    public TestAnnotations() {
        //@TestAnnotations.GlobalAnnotationWithParameters(stringArray = {"Member variable annotation"})
        Object obj = new Object();
        returnFunction(10, this);
    }

    /*
        @Deprecated
        @Action
        @MTOM
    */
    public Object returnFunction(/*@GlobalAnnotationWithParameters(stringArray = {"Parameter function variable annotation"})*/ Integer x, final Object y) {

        if (x == 5000)
            return Integer.MAX_VALUE;

        this.x = x;
        this.y = y;

        z = new Object();

        return returnFunction(x, y);
    }

    /*
        @Retention(RetentionPolicy.SOURCE)
        @GlobalAnnotationWithParameters(stringArray = {"Annotation on annotation"})
    */
    public @interface GlobalAnnotationSource {
    }

    //@Retention(RetentionPolicy.CLASS)
    public @interface GlobalAnnotationClass {
    }

    //@Retention(RetentionPolicy.RUNTIME)
    public @interface GlobalAnnotationRuntime {
    }

    public @interface GlobalAnnotationWithParameters {
        String stringValue() default "";

        String[] stringArray() default {};

        int integerValue() default 0;

        int[] integerArray() default {};

        byte byteValue() default 0;

        byte[] byteArray() default {};

        char charValue() default ' ';

        char[] charArray() default {};

        short shortValue() default 0;

        short[] shortArray() default {};

        int intValue() default 0;

        int[] intArray() default {};

        long longValue() default 0;

        long[] longArray() default {};

        float floatValue() default 0;

        float[] floatArray() default {};

        double doubleValue() default 0;

        double[] doubleArray() default {};

        boolean boolValue() default false;

        boolean[] boolArray() default {};

        Class<?> clazzValue() default Object.class;

        Class<?>[] clazzArray() default {};

        ElementType annotationValue() default ElementType.CONSTRUCTOR;

        ElementType[] annotationArray() default {};
    }
}


