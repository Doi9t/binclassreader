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

import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.tree.Tree;

/**
 * Created by Yannick on 10/12/2016.
 */
public abstract class Readable {

    private boolean eventsEnabled = true;

    /**
     * This method is called after the Fields have been initialized.
     *
     * @param reader
     */
    public void afterFieldsInitialized(final ClassReader reader) {
    }

    /**
     * This method is called after the tree have been initialized.
     *
     * @param tree
     */
    public void afterTreeIsBuilt(final Tree tree) {
    }

    /**
     * This value allow to block the events propagation
     */
    public final void enableEvents(boolean enabled) {
        eventsEnabled = enabled;
    }

    public final boolean isEventsEnabled() {
        return eventsEnabled;
    }
}
