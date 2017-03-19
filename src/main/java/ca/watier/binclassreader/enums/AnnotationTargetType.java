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

package ca.watier.binclassreader.enums;

/**
 * Created by Yannick on 12/6/2016.
 */


public enum AnnotationTargetType {

    TYPE_PARAMETER((short) 0x00, (short) 0x01),
    SUPERTYPE((short) 0x10),
    TYPE_PARAMETER_BOUND((short) 0x11, (short) 0x12),
    EMPTY((short) 0x13, (short) 0x14, (short) 0x15),
    METHOD_FORMAL_PARAMETER((short) 0x16),
    THROWS((short) 0x17),
    LOCALVAR((short) 0x40, (short) 0x41),
    CATCH((short) 0x42),
    OFFSET((short) 0x43, (short) 0x44, (short) 0x45, (short) 0x46),
    TYPE_ARGUMENT((short) 0x47, (short) 0x48, (short) 0x49, (short) 0x4A, (short) 0x4B);
    private short[] values;

    AnnotationTargetType(short... values) {
        this.values = values;
    }

    public short[] getValues() {
        return values;
    }
}

