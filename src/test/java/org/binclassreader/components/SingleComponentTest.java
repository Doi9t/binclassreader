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

package org.binclassreader.components;

import org.binclassreader.attributes.VisibleAndInvisibleAnnotationsAttr;
import org.binclassreader.reader.ClassReader;
import org.junit.Test;

/**
 * Created by Yannick on 9/28/2016.
 */
public class SingleComponentTest {


    private static final ClassReader reader;

    static {
        reader = new ClassReader();
    }

    @Test
    public void annotationComponentTest() throws Exception {
        //FIXME
        reader.overwriteStreamWithBytes(new byte[]{0, 1, 0, 24, 0, 1, 0, 25, 91, 0, 2, 115, 0, 26, 115, 0, 27});

        System.out.println(reader.read(new VisibleAndInvisibleAnnotationsAttr()));
    }
}