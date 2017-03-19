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

package ca.watier.binclassreader.abstracts;

import ca.watier.binclassreader.annotations.BinClassParser;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.utils.BaseUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 10/27/2016.
 */
public abstract class AbstractIterableAttribute<T> extends AbstractAttribute {

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private byte[] nb_entries;

    protected final List<T> ITEMS;
    protected final Class<T> ITERABLE_CLASS;

    public AbstractIterableAttribute(Class<T> iterableItemClass) {
        this.ITERABLE_CLASS = iterableItemClass;
        ITEMS = iterableItemClass != null ? new ArrayList<T>() : null;
    }

    public int getNbOfEntries() {
        return BaseUtils.combineBytesToInt(nb_entries);
    }

    @Override
    public final void afterFieldsInitialized(ClassReader reader) {

        if (ITERABLE_CLASS == null) {
            int length = getLength();

            if (length < 3) {
                return;
            }

            try {
                reader.skipFromCurrentStream(length - 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            int nbEntries = getNbOfEntries();

            for (int i = 0; i < nbEntries; i++) {
                try {
                    T instance = null;
                    if (ITERABLE_CLASS.isMemberClass()) {
                        try {
                            Constructor<?> ctor = ITERABLE_CLASS.getDeclaredConstructor(getClass());
                            instance = (T) ctor.newInstance(this);
                        } catch (NoSuchMethodException e) { //Static inner class ?
                            instance = ITERABLE_CLASS.newInstance();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    } else {
                        instance = ITERABLE_CLASS.newInstance();
                    }

                    if (instance == null) {
                        continue;
                    }

                    ITEMS.add(reader.read(instance));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<T> getItems() {
        return ITEMS;
    }
}
