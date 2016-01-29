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

package org.binclassreader;


import org.binclassreader.reader.ClassReader;
import org.binclassreader.structs.ConstMagicNumberInfo;
import org.binclassreader.structs.ConstMajorVersionInfo;
import org.binclassreader.structs.ConstMinorVersionInfo;
import org.binclassreader.structs.ConstPoolInfo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Created by Yannick on 1/25/2016.
 */
public class App {
    public static void main(String[] args) {
        try {
            Object[] read = ClassReader.read(
                    new BufferedInputStream(new FileInputStream("X:\\Programmation\\Java\\OpenRatty - PluginsV3\\OpenRatty - TestPlugin\\target\\classes\\org\\yannick\\PluginApp.class")),
                    new ConstMagicNumberInfo(),
                    new ConstMinorVersionInfo(),
                    new ConstMajorVersionInfo(),
                    new ConstPoolInfo());

            System.out.println(Arrays.toString(read));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
