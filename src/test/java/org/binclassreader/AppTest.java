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

package org.binclassreader;

import javassist.*;
import org.apache.commons.lang.WordUtils;
import org.binclassreader.enums.ClassHelperEnum;
import org.binclassreader.services.ClassHelperService;
import org.binclassreader.utils.ClassGenerator;
import org.binclassreader.utils.ClassUtil;
import org.binclassreader.utils.KeyValueHolder;
import org.binclassreader.utils.TestBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.binclassreader.utils.ClassGenerator.getRandomName;
import static org.binclassreader.utils.ClassGenerator.getRandomParameters;

/**
 * Created by Yannick on 2/3/2016.
 */
public class AppTest {

    private static ClassPool POOL = ClassPool.getDefault();
    private String method = "public static void %s(%s) {}";
    private String field = "public static String %s = \"%s\";";
    //FIXME: GENERATE A RANDOM LIST [...]
    private String[] interfacesList = {"java.io.Serializable", "java.lang.Runnable", "java.lang.CharSequence", "java.lang.Comparable"};

    @Test
    public void classBasicEmptyFunctionMultiplesRandomTest() throws Exception {
        for (short i = 0; i < 1000; i++) {
            ClassGenerator classGenerator = new ClassGenerator();

            classGenerator.setClassName(WordUtils.capitalize(getRandomName((byte) 25)));

            for (byte j = 0; j < 25; j++) {
                classGenerator.addComponents(CtMethod.class, String.format(method, getRandomName((byte) 25), getRandomParameters()));
            }

            for (byte j = 0; j < 10; j++) {
                classGenerator.addComponents(CtField.class, String.format(field, getRandomName((byte) 10), getRandomName((byte) 25)));
            }

            classGenerator.addInterfaces(interfacesList);
            ClassHelperService.loadClass(new ByteArrayInputStream(classGenerator.getRawCtClass()));
            CtClass ctClass = classGenerator.getCtClass();

            List<KeyValueHolder<ClassHelperEnum, Object>> fields = ClassHelperService.getFields();
            CtField[] ctFields = ctClass.getDeclaredFields();

            List<KeyValueHolder<ClassHelperEnum, Object>> methods = ClassHelperService.getMethods(false);
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();

            List<String> interfaces = ClassUtil.getBinaryPath(ClassHelperService.getInterfaces());
            List<String> ctInterfaces = TestBaseUtils.extractInterfaceFromCtClass(ctClass.getInterfaces());

            String superClassName = ClassUtil.getBinaryPath(ClassHelperService.getSuperClassName());
            String simpleName = ctClass.getSuperclass().getName();

            Assert.assertEquals(ClassHelperService.getClassName(), ctClass.getName()); //Compare the class name
            Assert.assertEquals(superClassName, simpleName); //Compare the super class name
            Assert.assertTrue("The fields are not similar !", TestBaseUtils.deepCtMemberComparator(fields, ctFields)); //Compare the fields
            Assert.assertTrue("The methods are not similar !", TestBaseUtils.deepCtMemberComparator(methods, ctMethods)); //Compare the methods
            Assert.assertTrue("The interfaces are not similar !", interfaces.equals(ctInterfaces) && interfaces.size() == ctInterfaces.size()); //Compare the interfaces
        }
    }


    @Test
    public void existingClassAnnotationComparisonTest() throws Exception {
        deepClassComparator(POOL.get("org.binclassreader.testclasses.TestAnnotations"));
    }

    @Test
    public void existingClassComparisonTest() throws Exception {
        deepClassComparator(
                POOL.get("com.sun.java.accessibility.AccessBridge"),
                POOL.get("com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl"),
                POOL.get("com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDHandler"),
                POOL.get("org.binclassreader.testclasses.TestAnnotations"));
    }


    private void deepClassComparator(CtClass... ctClasses) throws NotFoundException, IOException, CannotCompileException {

        if (ctClasses == null) {
            return;
        }

        for (CtClass ctClass : ctClasses) {

            ClassHelperService.loadClass(new ByteArrayInputStream(ctClass.toBytecode()));

            List<KeyValueHolder<ClassHelperEnum, Object>> fields = ClassHelperService.getFields();
            CtField[] ctFields = ctClass.getDeclaredFields();

            List<KeyValueHolder<ClassHelperEnum, Object>> methods = ClassHelperService.getMethods(false);
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();

            List<String> interfaces = ClassUtil.getBinaryPath(ClassHelperService.getInterfaces());
            List<String> ctInterfaces = TestBaseUtils.extractInterfaceFromCtClass(ctClass.getInterfaces());

            String name = ctClass.getName();
            Assert.assertEquals(ClassUtil.getBinaryPath(ClassHelperService.getClassName()),
                    ClassUtil.getBinaryPath(name)); //Compare the class name
            Assert.assertEquals(ClassUtil.getBinaryPath(ClassHelperService.getSuperClassName()),
                    ClassUtil.getBinaryPath(ctClass.getSuperclass().getName())); //Compare the super class name

            Assert.assertTrue("The fields are not similar ! ( " + name + " )", TestBaseUtils.deepCtMemberComparator(fields, ctFields)); //Compare the fields
            Assert.assertTrue("The methods are not similar ! ( " + name + " )", TestBaseUtils.deepMethodComparator(methods, ctMethods)); //Compare the methods
            Assert.assertTrue("The interfaces are not similar ! ( " + name + " )", interfaces.equals(ctInterfaces) && interfaces.size() == ctInterfaces.size()); //Compare the interfaces
        }
    }
}
