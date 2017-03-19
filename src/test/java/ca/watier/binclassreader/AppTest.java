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

package ca.watier.binclassreader;

import ca.watier.binclassreader.enums.ClassHelperEnum;
import ca.watier.binclassreader.services.ClassHelperService;
import ca.watier.binclassreader.testutils.TestUtils;
import ca.watier.binclassreader.utils.ClassUtil;
import ca.watier.binclassreader.utils.KeyValueHolder;
import javassist.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Yannick on 2/3/2016.
 */
public class AppTest {

    private static ClassPool POOL = ClassPool.getDefault();

    @Test
    public void existingClassAnnotationComparisonTest() throws Exception {
        deepClassComparator(POOL.get("ca.watier.binclassreader.testclasses.TestAnnotations"));
    }

    @Test
    public void existingClassComparisonTest() throws Exception {
        deepClassComparator(
                POOL.get("com.sun.java.accessibility.AccessBridge"),
                POOL.get("com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl"),
                POOL.get("com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDHandler"),
                POOL.get("ca.watier.binclassreader.testclasses.TestAnnotations"));
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
            List<String> ctInterfaces = TestUtils.extractInterfaceFromCtClass(ctClass.getInterfaces());

            String name = ctClass.getName();
            Assert.assertEquals(ClassUtil.getBinaryPath(ClassHelperService.getClassName()),
                    ClassUtil.getBinaryPath(name)); //Compare the class name
            Assert.assertEquals(ClassUtil.getBinaryPath(ClassHelperService.getSuperClassName()),
                    ClassUtil.getBinaryPath(ctClass.getSuperclass().getName())); //Compare the super class name

            Assert.assertTrue("The fields are not similar ! ( " + name + " )", TestUtils.deepCtMemberComparator(fields, ctFields)); //Compare the fields
            Assert.assertTrue("The methods are not similar ! ( " + name + " )", TestUtils.deepMethodComparator(methods, ctMethods)); //Compare the methods
            Assert.assertTrue("The interfaces are not similar ! ( " + name + " )", interfaces.equals(ctInterfaces) && interfaces.size() == ctInterfaces.size()); //Compare the interfaces
        }
    }
}
