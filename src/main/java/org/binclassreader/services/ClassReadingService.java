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

package org.binclassreader.services;

import org.binclassreader.parsers.*;
import org.binclassreader.reader.ClassReader;
import org.binclassreader.structs.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yannick on 2/3/2016.
 */
public class ClassReadingService {
    private final static List<ClassReader> readerList;
    private static final Class<?>[] DEFAULT_SECTIONS;
    private static ExecutorService executorService;
    private static Class<?>[] userSections;

    static {
        readerList = Collections.synchronizedList(new ArrayList<ClassReader>());
        executorService = Executors.newFixedThreadPool(10);

        DEFAULT_SECTIONS = new Class<?>[]{
                ConstMagicNumberInfo.class,
                ConstMinorVersionInfo.class,
                ConstMajorVersionInfo.class,
                PoolParser.class,
                ConstAccessFlagsInfo.class,
                ConstThisClassInfo.class,
                ConstSuperClassInfo.class,
                InterfacesParser.class,
                FieldsParser.class,
                MethodsParser.class,
                AttributesParser.class
        };
    }

    public static void init(Class<?>... sections) {
        if (sections == null || sections.length == 0) {
            return;
        }

        userSections = sections.clone();
    }

    public static void readNewClass(final InputStream stream) {
        if (stream != null) {

            if (executorService.isShutdown()) {
                executorService = Executors.newFixedThreadPool(10);
                readerList.clear();
            }

            executorService.execute(new Runnable() {
                public void run() {
                    readerList.add(new ClassReader(stream, (userSections != null) ? userSections : DEFAULT_SECTIONS));
                }
            });
        }
    }

    private static void shutdownExecutor() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
    }

    public static List<ClassReader> getReaderList() {
        shutdownExecutor();
        return readerList;
    }
}
