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

import com.google.common.base.Stopwatch;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
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
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public void classTestOne() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();

        URL classResource = AppTest.class.getResource("testclasses/TestOne.class");

        if (classResource != null) {
            ClassHelperService.loadClass(new FileInputStream(new File(classResource.toURI())));

            List<KeyValueHolder<ClassHelperEnum, Object>> fields = ClassHelperService.getFields();
            List<KeyValueHolder<ClassHelperEnum, Object>> methods = ClassHelperService.getMethods(false);
            List<String> interfaces = ClassHelperService.getInterfaces();

            System.out.println("SuperClassName -> " + ClassHelperService.getSuperClassName());
            System.out.println("********************************************************");
            System.out.println("Fields (" + fields.size() + ") -> " + fields);
            System.out.println("********************************************************");
            System.out.println("Methods (" + methods.size() + ") -> " + methods);
            System.out.println("********************************************************");
            System.out.println("Interfaces (" + interfaces.size() + ") -> " + interfaces);
            System.out.println("********************************************************");
        }
        System.out.println("Elapsed time => " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " MILLISECONDS");
    }

    @Test
    public void classBasicEmptyFunctionMultiplesRandomTest() throws Exception {
        long totalTime = 0, start = 0;

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

            start = System.nanoTime();
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

            totalTime += System.nanoTime() - start;

            Assert.assertEquals(ClassHelperService.getClassName(), ctClass.getName()); //Compare the class name
            Assert.assertEquals(superClassName, simpleName); //Compare the super class name
            Assert.assertTrue("The fields are not similar !", TestBaseUtils.signatureCtMemberComparator(fields, ctFields)); //Compare the fields
            Assert.assertTrue("The methods are not similar !", TestBaseUtils.signatureCtMemberComparator(methods, ctMethods)); //Compare the methods
            Assert.assertTrue("The interfaces are not similar !", interfaces.equals(ctInterfaces) && interfaces.size() == ctInterfaces.size()); //Compare the interfaces
        }

        System.out.println("Elapsed time => " + (totalTime / 1000000000) + " second(s)");
    }


    /*
        com.sun.java.accessibility.AccessBridge
        com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl
        com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDHandler
     */
    @Test
    public void existingClassComparisonTest() throws Exception {

        //CtClass accessBridgeCtClass = POOL.get("com.sun.java.accessibility.AccessBridge");
        CtClass accessBridgeCtClass = POOL.get("com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDHandler");

        ClassHelperService.loadClass(new ByteArrayInputStream(accessBridgeCtClass.toBytecode()));

        List<KeyValueHolder<ClassHelperEnum, Object>> fields = ClassHelperService.getFields();
        CtField[] ctFields = accessBridgeCtClass.getDeclaredFields();

        List<KeyValueHolder<ClassHelperEnum, Object>> methods = ClassHelperService.getMethods(false);
        CtMethod[] ctMethods = accessBridgeCtClass.getDeclaredMethods();

        List<String> interfaces = ClassUtil.getBinaryPath(ClassHelperService.getInterfaces());
        List<String> ctInterfaces = TestBaseUtils.extractInterfaceFromCtClass(accessBridgeCtClass.getInterfaces());

        Assert.assertEquals(ClassUtil.getBinaryPath(ClassHelperService.getClassName()),
                ClassUtil.getBinaryPath(accessBridgeCtClass.getName())); //Compare the class name
        Assert.assertEquals(ClassUtil.getBinaryPath(ClassHelperService.getSuperClassName()),
                ClassUtil.getBinaryPath(accessBridgeCtClass.getSuperclass().getName())); //Compare the super class name
        Assert.assertTrue("The fields are not similar !", TestBaseUtils.signatureCtMemberComparator(fields, ctFields)); //Compare the fields
        Assert.assertTrue("The methods are not similar !", TestBaseUtils.deepMethodComparator(methods, ctMethods)); //Compare the methods
        Assert.assertTrue("The interfaces are not similar !", interfaces.equals(ctInterfaces) && interfaces.size() == ctInterfaces.size()); //Compare the interfaces
    }
}
