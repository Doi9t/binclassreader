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

package ca.watier.binclassreader.structs;

import ca.watier.binclassreader.abstracts.AbstractAttribute;
import ca.watier.binclassreader.reader.ClassReader;

import java.io.IOException;

/**
 * Created by Yannick on 4/18/2016.
 */
//https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7
public class AttributesInfo extends AbstractAttribute {
    @Override
    public String toString() {
        return "ConstAttributeInfo{" +
                "attribute_name_index=" + getNameIndex() +
                ", attribute_length=" + getLength() +
                '}';
    }

    public void afterFieldsInitialized(ClassReader reader) {
        try {
            reader.skipFromCurrentStream(getLength());//FIXME: Parse the bytes instead of wasting them ...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}