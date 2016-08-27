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

import org.junit.Test;

/**
 * Created by Yannick on 8/21/2016.
 */
public class ClassGeneratorTest {

    private String testClassName = "TestClassName";

    private String method = "public static void test(String test) {\n" +
            "        System.out.println(test);\n" +
            "    }";
    private String field = "public static String test = \"sjdhasjkhdjkashdjhj\";";

    private String constructor = "private " + testClassName + "() {\n" +
            "        System.out.println(getClass().getSimpleName());\n" +
            "    }\n";

    @Test
    public void addComponents() {

        ClassGenerator.setClassName(testClassName);

        try {
            ClassGenerator.addMethods(constructor, method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println(ClassGenerator.getCtClass());
    }
}