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

import com.google.common.base.Strings;
import javassist.*;
import org.apache.commons.lang.RandomStringUtils;
import org.multiarraymap.MultiArrayMap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Yannick on 8/21/2016.
 */
public class ClassGenerator {

    private static final List<String> OBJECTS = Arrays.asList("String", "Integer", "Double", "Float", "Object");
    private static final Random random = new Random();
    private static final String PLACEHOLDER_PARAMETER = "%s %s,";
    private static final ClassPool POOL = ClassPool.getDefault();
    private static CtClass currentCtClass = null;

    private final MultiArrayMap<Class<? extends CtMember>, CtMember> CLASS_COMPONENTS;


    public ClassGenerator() {
        CLASS_COMPONENTS = new MultiArrayMap<Class<? extends CtMember>, CtMember>();
    }

    public static String getPlaceholderParameter(byte nbParameters) {
        if (nbParameters <= 0) {
            return "";
        }

        String repeated = Strings.repeat(PLACEHOLDER_PARAMETER, nbParameters);
        return repeated.substring(0, repeated.length() - 1);
    }

    public static String getRandomParameters() {
        List<String> placeHolders = new ArrayList<String>();

        byte nbParameters = (byte) random.nextInt();

        for (int i = 0; i < nbParameters; i++) {
            placeHolders.add(getRandomStrObject());
            placeHolders.add(getRandomName((byte) 10));
        }

        return String.format(getPlaceholderParameter(nbParameters), (Object[]) placeHolders.toArray());
    }

    public static String getRandomName(byte nb) {
        return RandomStringUtils.randomAlphabetic(nb).toLowerCase();
    }

    public static String getRandomStrObject() {
        return OBJECTS.get(random.nextInt(OBJECTS.size()));
    }

    public void setClassName(String className) {
        currentCtClass = POOL.makeClass(className);
    }

    public void addComponents(Class<? extends CtMember> componentClass, String... code) {

        if (componentClass == null || code == null || code.length == 0) {
            return;
        }

        for (String functionCode : code) {

            if (functionCode == null || "".equals(functionCode)) {
                return;
            }

            try {
                Method make = componentClass.getDeclaredMethod("make", String.class, CtClass.class);

                Object invoked = make.invoke(null, functionCode, currentCtClass);

                if (invoked == null) {
                    continue;
                }

                if (CtMethod.class.equals(componentClass)) {
                    CtMethod invokedTmp = (CtMethod) invoked;
                    currentCtClass.addMethod(invokedTmp);
                    CLASS_COMPONENTS.put(componentClass, invokedTmp);
                } else if (CtField.class.equals(componentClass)) {
                    CtField invokedTmp = (CtField) invoked;
                    currentCtClass.addField(invokedTmp);
                    CLASS_COMPONENTS.put(componentClass, invokedTmp);
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

    public CtClass getCtClass() {
        return currentCtClass;
    }

    public byte[] getRawCtClass() throws IOException, CannotCompileException {
        return currentCtClass.toBytecode();
    }

    public MultiArrayMap<Class<? extends CtMember>, CtMember> getClassComponents() {
        return CLASS_COMPONENTS;
    }
}
