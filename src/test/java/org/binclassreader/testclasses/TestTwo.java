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

package org.binclassreader.testclasses;

import javax.xml.ws.Action;
import javax.xml.ws.soap.MTOM;

/**
 * Created by Yannick on 5/15/2016.
 */
public class TestTwo {

    private transient Object x, y;
    private Object z;

    public TestTwo() {
        Object obj = new Object();
    }

    @Deprecated
    @Action
    @MTOM
    public Object returnFunction(Object x, final Object y) {
        this.x = x;
        this.y = y;

        z = new Object();

        return y;
    }
}


