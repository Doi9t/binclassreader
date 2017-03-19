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

package ca.watier.binclassreader.parsers;

import ca.watier.binclassreader.abstracts.AbstractParser;
import ca.watier.binclassreader.annotations.PoolDataOptions;
import ca.watier.binclassreader.enums.CollectionTypeEnum;
import ca.watier.binclassreader.reader.ClassReader;
import ca.watier.binclassreader.utils.AttributeUtils;

import java.io.IOException;

/**
 * Created by Yannick on 4/18/2016.
 */

@PoolDataOptions(storageType = CollectionTypeEnum.LIST)
public class AttributesParser extends AbstractParser {

    public void afterFieldsInitialized(ClassReader reader) {
        count = getCount();

        if (count > 65535) {
            return;
        }

        for (int i = 0; i < count; i++) {
            try {
                addItemToList(AttributeUtils.getNextAttribute(reader));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
