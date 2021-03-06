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

package ca.watier.binclassreader.tree;

import ca.watier.binclassreader.enums.ClassHelperEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yannick on 2/24/2016.
 */
public class TreeElement {
    private final List<TreeElement> child;
    private TreeElement parent;
    private Object current;
    private ClassHelperEnum mappingType;

    public TreeElement(Object current, ClassHelperEnum mappingType) {
        this.current = current;
        child = new ArrayList<TreeElement>();
        this.mappingType = mappingType;
    }

    public TreeElement(Object current) {
        this.current = current;
        child = new ArrayList<TreeElement>();
        mappingType = ClassHelperEnum.NONE;
    }

    /**
     * @param elements - Add one or more children to the TreeElement
     */
    public void addChildren(TreeElement... elements) {
        if (elements != null && elements.length > 0) {
            child.addAll(Arrays.asList(elements));
        }
    }

    /**
     * @param elements - Set the current parent of the TreeElement
     */
    public void addParent(TreeElement elements) {
        parent = elements;
    }

    /**
     * @return - The parent of the TreeElement
     */
    public TreeElement getParent() {
        return parent;
    }

    /**
     * @return - The current object
     */
    public Object getCurrent() {
        return current;
    }

    /**
     * @param current - Set the current element
     */
    public void setCurrent(Object current) {
        this.current = current;
    }

    public List<TreeElement> getChild() {
        return child;
    }

    public ClassHelperEnum getMappingType() {
        return mappingType;
    }
}
