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

import javassist.CtField;
import javassist.CtMember;
import javassist.CtMethod;
import org.apache.commons.lang.WordUtils;
import org.binclassreader.utils.ClassGenerator;
import org.junit.Test;

import static org.binclassreader.utils.ClassGenerator.getRandomName;
import static org.binclassreader.utils.ClassGenerator.getRandomParameters;

/**
 * Created by Yannick on 8/21/2016.
 */
public class ClassGeneratorTest {

    private String testClassName = WordUtils.capitalize(getRandomName((byte) 25));

    private String method = "public static void %s(%s) {}";
    private String field = "public static String %s = \"%s\";";

    @Test
    public void addComponents() throws NoSuchMethodException {

        ClassGenerator classGenerator = new ClassGenerator();

        classGenerator.setClassName(testClassName);

        for (byte i = 0; i < 25; i++) {
            classGenerator.addComponents(CtMethod.class, String.format(method, getRandomName((byte) 25), getRandomParameters()));
        }

        for (byte i = 0; i < 10; i++) {
            classGenerator.addComponents(CtField.class, String.format(field, getRandomName((byte) 10), getRandomName((byte) 25)));
        }

        for (CtMember member : classGenerator.getClassComponents().get(CtMethod.class)) {
            System.out.println(member.getSignature());
        }
    }
}