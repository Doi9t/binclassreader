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
import ca.watier.binclassreader.enums.AttributeTypeEnum;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 11/2/2016.
 */
public class VisibleAndInvisibleAnnotationsAttr extends AbstractIterableAttribute<VisibleAndInvisibleAnnotationsAttr.Annotation> {

    public VisibleAndInvisibleAnnotationsAttr() {
        super(Annotation.class);
    }

    public class Annotation extends Readable {
        @BinClassParser(byteToRead = 2)
        private byte[] type_index;

        @BinClassParser(readOrder = 2, byteToRead = 2)
        private byte[] num_element_value_pairs;

        private final List<ElementPair> ELEMENTS;

        public Annotation() {
            ELEMENTS = new ArrayList<ElementPair>();
        }

        public int getTypeIndex() {
            return BaseUtils.combineBytesToInt(type_index);
        }

        public int getNbElementPair() {
            return BaseUtils.combineBytesToInt(num_element_value_pairs);
        }

        public AttributeTypeEnum getAttributeType() {
            return attributeName;
        }

        public List<ElementPair> getElementPairs() {
            return ELEMENTS;
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
