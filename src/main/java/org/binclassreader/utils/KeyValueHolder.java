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

package org.binclassreader.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yannick on 9/28/2016.
 */
public class KeyValueHolder<K, V> {
    private final List<Holder<K, V>> valueHolder;

    public KeyValueHolder() {
        valueHolder = new ArrayList<Holder<K, V>>();
    }

    public void addPair(K key, V value) {
        valueHolder.add(new Holder<K, V>(key, value));
    }

    public List<V> getValues(K key) {

        List<V> values = new ArrayList<V>();

        for (Holder<K, V> kvHolder : valueHolder) {
            if (kvHolder.getKey() == key) {
                values.add(kvHolder.getValue());
            }
        }

        return values;
    }

    public List<V> getAllValues() {
        List<V> values = new ArrayList<V>();

        for (Holder<K, V> kvHolder : valueHolder) {
            values.add(kvHolder.getValue());
        }

        return values;
    }

    private class Holder<X, Y> {
        private X key;
        private Y value;

        public Holder(X key, Y value) {
            this.key = key;
            this.value = value;
        }

        public X getKey() {
            return key;
        }

        public Y getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Holder{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();


        for (Holder<K, V> kvHolder : valueHolder) {
            builder.append(kvHolder).append('\n');
        }

        return "KeyValueHolder{" +
                "valueHolder=" + builder +
                '}';
    }
}
