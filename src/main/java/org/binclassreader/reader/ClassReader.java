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

package org.binclassreader.reader;

import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.interfaces.SelfReader;
import org.binclassreader.pojos.FieldPojo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Yannick on 1/25/2016.
 */
public class ClassReader {

    private static Map<Short, FieldPojo> fieldSorter;

    static {
        fieldSorter = new TreeMap<Short, FieldPojo>();
    }

    public static Object[] read(InputStream classData, Object... type) {
        if (classData != null && type != null) {
            for (Object obj : type) {
                fieldSorter.clear();

                Class<?> currentClass = obj.getClass();

                while (currentClass != null) {
                    for (Field field : currentClass.getDeclaredFields()) {
                        field.setAccessible(true);
                        for (Annotation annotation : field.getDeclaredAnnotations()) {
                            if (annotation instanceof BinClassParser) {
                                BinClassParser anno = (BinClassParser) annotation;
                                fieldSorter.put(anno.readOrder(), new FieldPojo(field, anno.byteToRead()));
                            }
                        }
                    }
                    currentClass = currentClass.getSuperclass();
                }

                if (!fieldSorter.isEmpty()) {
                    for (FieldPojo value : fieldSorter.values()) {
                        Field fieldToWrite = value.getFieldToWrite();
                        fieldToWrite.setAccessible(true);

                        byte nbByteToRead = value.getNbByteToRead();
                        int[] buffer = new int[nbByteToRead];

                        try {

                            for (int i = 0; i < nbByteToRead; i++) {
                                buffer[i] = classData.read();
                            }
                            fieldToWrite.set(obj, buffer);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }


                    if (obj instanceof SelfReader) {
                        ((SelfReader) obj).initReading(classData);
                    }
                }
            }
        }

        return type;
    }
}
