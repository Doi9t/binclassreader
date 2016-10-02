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
import org.binclassreader.enums.ClassHelperEnum;
import org.binclassreader.services.ClassHelperService;
import org.binclassreader.structs.ConstUtf8Info;
import org.binclassreader.utils.KeyValueHolder;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yannick on 2/3/2016.
 */
public class AppTest {

    @Test
    public void classTestOne() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();

        URL classResource = AppTest.class.getResource("testclasses/TestOne.class");

        if (classResource != null) {
            ClassHelperService.loadClass(new FileInputStream(new File(classResource.toURI())));

            List<KeyValueHolder<ClassHelperEnum, Object>> fields = ClassHelperService.getFields();
            List<KeyValueHolder<ClassHelperEnum, ConstUtf8Info>> methods = ClassHelperService.getMethods();
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
}
