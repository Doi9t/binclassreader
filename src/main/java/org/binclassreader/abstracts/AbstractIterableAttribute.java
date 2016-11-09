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

package org.binclassreader.abstracts;

import org.binclassreader.annotations.BinClassParser;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.utils.BaseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 10/27/2016.
 */
public abstract class AbstractIterableAttribute<T> extends AbstractAttribute {

    @BinClassParser(readOrder = 3, byteToRead = 2)
    private short[] nb_entries;

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

        if (ITERABLE_CLASS != null) {
            int nbEntries = getNbOfEntries();

            for (int i = 0; i < nbEntries; i++) {
                try {
                    ITEMS.add(reader.read(ITERABLE_CLASS.newInstance()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            int length = getLength();

            if (length == 0) {
                return;
            }

            try {
                reader.skipFromCurrentStream(length - 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<T> getItems() {
        return ITEMS;
    }
}
