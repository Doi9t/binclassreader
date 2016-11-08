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

package org.binclassreader.attributes;

import org.binclassreader.abstracts.AbstractIterableAttribute;
import org.binclassreader.reader.ClassReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 10/27/2016.
 */
public class RuntimeVisibleTypeAnnotationsAttr extends AbstractIterableAttribute {

    private final List<Annotation> ANNOTATIONS;

    public RuntimeVisibleTypeAnnotationsAttr() {
        ANNOTATIONS = new ArrayList<Annotation>();
    }

    @Override
    public void afterFieldsInitialized(ClassReader reader) {

        int length = getLength();

        if (length == 0) {
            return;
        }

        try {

            int nbOfEntries = getNbOfEntries();

//            for (int i = 0; i < nbOfEntries; i++) {
//                System.out.println();
//            }

            bytes = reader.readFromCurrentStream(length - 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    annotation {
    u2 type_index;
    u2 num_element_value_pairs;
    {   u2            element_name_index;
        element_value value;
    } element_value_pairs[num_element_value_pairs];
}
     */
}
