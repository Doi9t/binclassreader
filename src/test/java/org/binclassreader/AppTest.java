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
import org.binclassreader.reader.ClassReader;
import org.binclassreader.services.ClassReadingService;
import org.binclassreader.testclasses.TestOne;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;

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
                    System.out.println("\n\n--------------------------- NEW CLASS INFO ---------------------------");
                    for (Object item : classReader.getSections().values()) {
                        System.out.println(item); //Print one per line
                    }
                }
            }
        }
    }
}
