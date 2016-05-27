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

/**
 * Created by Yannick on 5/15/2016.
 */

import com.sun.scenario.effect.ZoomRadialBlur;
import com.sun.scenario.effect.impl.state.ZoomRadialBlurState;

import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleHyperlink;
import javax.accessibility.AccessibleHypertext;
import javax.swing.text.AttributeSet;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlEnum;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;

@XmlEnum
@XmlAccessorOrder
public class TestOne extends ZoomRadialBlurState implements Serializable, Appendable, AccessibleAction, AccessibleHypertext {

    public transient volatile int integerPrimitive = 10;
    private static String TEST_STR = "SALUT_VARIABLE";
    private Object obj = new Object();
    public Integer integer = 10;
    public String test = null;

    public TestOne(ZoomRadialBlur effect) {
        super(effect);
    }

    public Appendable append(CharSequence csq) throws IOException {
        return null;
    }

    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return null;
    }

    public Appendable append(char c) throws IOException {
        return null;
    }

    public int getAccessibleActionCount() {
        return 0;
    }

    public String getAccessibleActionDescription(int i) {
        return null;
    }

    public boolean doAccessibleAction(int i) {
        return false;
    }

    public int getLinkCount() {
        return 0;
    }

    public AccessibleHyperlink getLink(int linkIndex) {
        return null;
    }

    public int getLinkIndex(int charIndex) {
        return 0;
    }

    public int getIndexAtPoint(Point p) {
        return 0;
    }

    public Rectangle getCharacterBounds(int i) {
        return null;
    }

    public int getCharCount() {
        return 0;
    }

    public int getCaretPosition() {
        return 0;
    }

    public String getAtIndex(int part, int index) {
        return null;
    }

    public String getAfterIndex(int part, int index) {
        return null;
    }

    public String getBeforeIndex(int part, int index) {
        return null;
    }

    public AttributeSet getCharacterAttribute(int i) {
        return null;
    }

    public int getSelectionStart() {
        return 0;
    }

    public int getSelectionEnd() {
        return 0;
    }

    public String getSelectedText() {
        return null;
    }
}


