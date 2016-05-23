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

/**
 * Created by Yannick on 4/11/2016.
 */
public class Assert {

    /**
     * @param arr - The array to be checked
     * @return - Return true if the array is not null and there's at least one item
     */
    public static boolean isArrayReadable(Object[] arr) {
        return arr != null && arr.length > 0;
    }

    /**
     * @param arr - The array to be checked
     * @return - Return true if the array is not null and there's at least one item
     */
    public static boolean isArrayReadable(int[] arr) {
        return arr != null && arr.length > 0;
    }
}
