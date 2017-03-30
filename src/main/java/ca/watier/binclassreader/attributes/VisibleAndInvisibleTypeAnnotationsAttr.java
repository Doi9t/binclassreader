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
import ca.watier.binclassreader.enums.AnnotationTargetType;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.utils.BaseUtils;
import ca.watier.defassert.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 10/27/2016.
 */
public class VisibleAndInvisibleTypeAnnotationsAttr extends AbstractIterableAttribute {
    public VisibleAndInvisibleTypeAnnotationsAttr() {
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
        private Object targetObject;

        @BinClassParser
        private byte[] target_type;

        private int num_element_value_pairs;

        private final List<ElementPair> ELEMENTS;

        private TypePath typePath;

        int typeIndex;

        public Annotation() {
            ELEMENTS = new ArrayList<ElementPair>();
        }

        public int getTypeIndex() {
            return BaseUtils.combineBytesToInt(target_type);
        }

        public AnnotationTargetType getTarget() {
            return AnnotationTargetType.getTargetById(BaseUtils.combineBytesToShort(target_type));
        }


        @Override
        public void afterFieldsInitialized(ClassReader reader) {


            AnnotationTargetType target = getTarget();

            Assert.assertNotNull(target);

            switch (target) {
                case TYPE_PARAMETER:
                    targetObject = reader.read(new TypeParameterTarget());
                    break;
                case SUPERTYPE:
                    targetObject = reader.read(new SuperTypeTarget());
                    break;
                case TYPE_PARAMETER_BOUND:
                    targetObject = reader.read(new TypeParameterBoundTarget());
                    break;
                case EMPTY:
                    targetObject = AnnotationTargetType.EMPTY;
                    break;
                case METHOD_FORMAL_PARAMETER:
                    targetObject = reader.read(new FormalParameterTarget());
                    break;
                case THROWS:
                    targetObject = reader.read(new ThrowsTarget());
                    break;
                case LOCALVAR:
                    targetObject = reader.read(new LocalVarTarget());
                    break;
                case CATCH:
                    targetObject = reader.read(new CatchTarget());
                    break;
                case OFFSET:
                    targetObject = reader.read(new OffsetTarget());
                    break;
                case TYPE_ARGUMENT:
                    targetObject = reader.read(new TypeArgumentTarget());
                    break;
            }

            typePath = reader.read(new TypePath());


            try {
                typeIndex = BaseUtils.combineBytesToInt(reader.readFromCurrentStream(2));
                num_element_value_pairs = BaseUtils.combineBytesToInt(reader.readFromCurrentStream(2));
            } catch (IOException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < num_element_value_pairs; i++) {
                ELEMENTS.add(reader.read(new ElementPair(getAttributeType())));
            }
        }

        public class TypeParameterTarget extends Readable {
            @BinClassParser
            private byte[] type_parameter;
        }

        public class SuperTypeTarget extends Readable {
            @BinClassParser(byteToRead = 2)
            private byte[] type_parameter;
        }

        public class TypeParameterBoundTarget extends Readable {
            @BinClassParser
            private byte[] type_parameter;

            @BinClassParser(readOrder = 2)
            private byte[] bound_index;
        }

        public class FormalParameterTarget extends Readable {
            @BinClassParser
            private byte[] type_parameter;
        }

        public class ThrowsTarget extends Readable {
            @BinClassParser(byteToRead = 2)
            private byte[] type_parameter;
        }

        public class LocalVarTarget extends Readable {

            List<TableElement> elements = new ArrayList<>();

            @BinClassParser(byteToRead = 2)
            private byte[] table_length;

            public int tableLength() {
                return BaseUtils.combineBytesToInt(table_length);
            }

            @Override
            public void afterFieldsInitialized(ClassReader reader) {
                int length = tableLength();

                for (int i = 0; i < length; i++) {
                    elements.add(reader.read(new TableElement()));
                }
            }

            private class TableElement extends Readable {
                @BinClassParser(byteToRead = 2)
                private byte[] start_pc;

                @BinClassParser(readOrder = 2, byteToRead = 2)
                private byte[] length;

                @BinClassParser(readOrder = 3, byteToRead = 2)
                private byte[] index;
            }
        }

        public class CatchTarget extends Readable {
            @BinClassParser(byteToRead = 2)
            private byte[] exception_table_index;
        }

        public class OffsetTarget extends Readable {
            @BinClassParser(byteToRead = 2)
            private byte[] offset;
        }

        public class TypeArgumentTarget extends Readable {
            @BinClassParser(byteToRead = 2)
            private byte[] offset;

            @BinClassParser(readOrder = 2)
            private byte[] type_argument_index;
        }


        public class TypePath extends Readable {

            private List<Path> paths = new ArrayList<>();

            @BinClassParser
            private byte[] path_length;

            public int pathLength() {
                return BaseUtils.combineBytesToInt(path_length);
            }

            @Override
            public void afterFieldsInitialized(ClassReader reader) {
                int length = pathLength();

                for (int i = 0; i < length; i++) {
                    paths.add(reader.read(new Path()));
                }
            }

            public class Path extends Readable {
                @BinClassParser
                private byte[] type_path_kind;

                @BinClassParser(readOrder = 2)
                private byte[] type_argument_index;
            }
        }
    }
}
