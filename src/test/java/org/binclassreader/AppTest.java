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

import org.apache.commons.io.IOUtils;
import org.binclassreader.attributes.CodeAttr;
import org.binclassreader.enums.FieldAccessFlagsEnum;
import org.binclassreader.enums.PoolTypeEnum;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.services.ClassReadingService;
import org.binclassreader.structs.*;
import org.binclassreader.testclasses.TestOne;
import org.binclassreader.tree.Tree;
import org.binclassreader.tree.TreeElement;
import org.junit.Test;
import org.multiarraymap.MultiArrayMap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Yannick on 2/3/2016.
 */
public class AppTest {

    /*
        - One super class
        - Four class implementations
        - Twenty functions
        - One constructor (Not the default)
        - Five class variables
        - Two class annotations
     */

    @Test
    public void classTestOne() throws Exception {

        URL classResource = TestOne.class.getResource("TestOne.class");

        if (classResource != null) {
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(new File(classResource.toURI())));

            if (bytes != null && bytes.length > 0) {
                ClassReadingService.readNewClass(new ByteArrayInputStream(bytes));

                List<ClassReader> readerList = ClassReadingService.getReaderList();

                for (ClassReader classReader : readerList) {
                    System.out.println("\n\n************************************* NEW CLASS INFO *************************************");

                    MultiArrayMap<PoolTypeEnum, Object> mappedPool = classReader.getMappedPool();
                    Map<Class<?>, Object> sections = classReader.getSections();
                    Map<Integer, Object> constPool = classReader.getConstPool();


                    System.out.println("\n--------------------------- SUPER_CLASS ---------------------------");

                    ConstThisClassInfo thisClassInfo = (ConstThisClassInfo) sections.get(ConstThisClassInfo.class);

                    ConstClassInfo ConstClassInfoSuperClass = (ConstClassInfo) constPool.get(thisClassInfo.getIndex());
                    ConstUtf8Info constUtf8InfoSuperClassName = (ConstUtf8Info) constPool.get(ConstClassInfoSuperClass.getNameIndex() - 1);

                    System.out.println(constUtf8InfoSuperClassName.getAsNewString());

                    System.out.println("\n--------------------------- FIELD ---------------------------");
                    for (Object o : mappedPool.get(PoolTypeEnum.FIELD)) {
                        if (o instanceof Tree) {
                            TreeElement element = ((Tree) o).getRoot();
                            List<FieldAccessFlagsEnum> accessFlags = ((ConstFieldInfo) element.getCurrent()).getAccessFlags();
                            List<TreeElement> child = element.getChild();

                            ConstUtf8Info constUtf8InfoName = (ConstUtf8Info) child.get(0).getCurrent();
                            ConstUtf8Info constUtf8InfoDescriptor = (ConstUtf8Info) child.get(1).getCurrent();

                            System.out.println(accessFlags + " " + constUtf8InfoName.getAsNewString() + " " + constUtf8InfoDescriptor.getAsNewString());
                        }
                    }


                    System.out.println("\n--------------------------- INTERFACES ---------------------------");
                    for (Object o : mappedPool.get(PoolTypeEnum.INTERFACE)) {
                        if (o instanceof Tree) {
                            TreeElement element = ((Tree) o).getRoot();
                            TreeElement child = element.getChild().get(0);

                            System.out.println(((ConstUtf8Info) child.getCurrent()).getAsNewString());
                        }
                    }

                    System.out.println("\n--------------------------- METHODS ---------------------------");
                    for (Object o : mappedPool.get(PoolTypeEnum.METHOD)) {
                        if (o instanceof Tree) {
                            TreeElement element = ((Tree) o).getRoot();
                            CodeAttr codeAttr = ((ConstMethodInfo) element.getCurrent()).getCodeAttr();
                            List<TreeElement> child = element.getChild();

                            ConstUtf8Info constUtf8InfoName = (ConstUtf8Info) child.get(0).getCurrent();
                            ConstUtf8Info constUtf8InfoDescriptor = (ConstUtf8Info) child.get(1).getCurrent();


                            System.out.println(constUtf8InfoName.getAsNewString() + "(" + constUtf8InfoDescriptor.getAsNewString() + ") Bytecode => " + codeAttr.getCODE());
                        }
                    }
                }
            }
        }
    }
}
