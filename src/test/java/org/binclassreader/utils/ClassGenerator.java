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

import javassist.*;
import org.multiarraymap.MultiArrayMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Yannick on 8/21/2016.
 */
public class ClassGenerator {

    private static final ClassPool POOL = ClassPool.getDefault();
    private static CtClass currentCtClass = null;
    private static String currentClassName = null;
    private static final MultiArrayMap<Class<? extends CtMember>, String> CLASS_COMPONENTS;

    static {
        CLASS_COMPONENTS = new MultiArrayMap<Class<? extends CtMember>, String>();
    }

    private ClassGenerator() {
        System.out.println(getClass().getSimpleName());
    }

    public static void setClassName(String className) {
        currentCtClass = POOL.makeClass(className);
        currentClassName = className;
    }

    public static void addComponents(Class<? extends CtMember> componentClass, String... code) {

        if (componentClass == null || code == null || code.length == 0) {
            return;
        }


        for (String functionCode : code) {

            if (functionCode == null || "".equals(functionCode)) {
                return;
            }


            CLASS_COMPONENTS.put(componentClass, functionCode);

            try {
                Method make = componentClass.getDeclaredMethod("make", String.class, CtClass.class);

                Object invoked = make.invoke(null, functionCode, currentCtClass);

                if (invoked == null) {
                    continue;
                }

                if (CtMethod.class.equals(componentClass)) {
                    currentCtClass.addMethod((CtMethod) invoked);
                } else if (CtField.class.equals(componentClass)) {
                    currentCtClass.addField((CtField) invoked);
                } else if (CtClass.class.equals(componentClass)) {
                    currentCtClass.addInterface((CtClass) invoked);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addMethods(String... code) throws NoSuchMethodException {

        if (code == null || code.length == 0)
            return;

        for (String functionCode : code) {

            if (functionCode == null || "".equals(functionCode)) {
                continue;
            }

            CLASS_COMPONENTS.put(CtMethod.class, functionCode);

            try {
                currentCtClass.addMethod(CtMethod.make(functionCode, currentCtClass));
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
    }

    public static CtClass getCtClass() {
        return currentCtClass;
    }
}
