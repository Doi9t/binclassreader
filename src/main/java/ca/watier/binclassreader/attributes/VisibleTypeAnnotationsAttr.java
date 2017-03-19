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

package ca.watier.binclassreader.attributes;

import ca.watier.binclassreader.abstracts.AbstractIterableAttribute;
import ca.watier.binclassreader.abstracts.Readable;
import ca.watier.binclassreader.annotations.BinClassParser;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 10/27/2016.
 */
public class VisibleTypeAnnotationsAttr extends AbstractIterableAttribute {
    public VisibleTypeAnnotationsAttr() {
        super(Annotation.class);
    }

    /*
        type_annotation {
             u1 target_type;

             union {
                 type_parameter_target;
                 supertype_target;
                 type_parameter_bound_target;
                 empty_target;
                 method_formal_parameter_target;
                 throws_target;
                 localvar_target;
                 catch_target;
                 offset_target;
                 type_argument_target;
             } target_info;

             type_path target_path;
             u2 type_index;

             u2 num_element_value_pairs;
             {
                u2 element_name_index;
                element_value value;
             } element_value_pairs[num_element_value_pairs];
        }
     */

    public class Annotation extends Readable {
        @BinClassParser
        private byte[] target_type;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private byte[] num_element_value_pairs;

        private final List<ElementPair> ELEMENTS;

        public Annotation() {
            ELEMENTS = new ArrayList<ElementPair>();
        }

        public int getTypeIndex() {
            return BaseUtils.combineBytesToInt(target_type);
        }

        public int getNbElementPair() {
            return BaseUtils.combineBytesToInt(num_element_value_pairs);
        }


        @Override
        public void afterFieldsInitialized(ClassReader reader) {
            int nbElementPair = getNbElementPair();

            for (int i = 0; i < nbElementPair; i++) {
                ELEMENTS.add(reader.read(new ElementPair(getAttributeType())));
            }
        }
    }
}
