/*
 * Copyright Kay Stenschke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kstenschke.sweep.helpers;

public class StringHelper {

    /**
     * Remove given char if it pre-fixes the given string
     *
     * @param  sourceStr
     * @param  prefixChar
     * @param  prefixMustExist    true: if prefix isn't the given one: return null / otherwise return as is
     * @return String
     */
    private static String removePrefixChar(String sourceStr, String prefixChar, Boolean prefixMustExist) {
        if (sourceStr != null && sourceStr.indexOf(prefixChar)  == 0) {
            return sourceStr.substring(1);
        }

        return prefixMustExist ? null : sourceStr;
    }

    private static String removePrefixChar(String sourceStr, String prefixChar) {
        return removePrefixChar(sourceStr, prefixChar, true);
    }

    /**
     * Remove given char if it post-fixes the given string
     *
     * @param  sourceStr
     * @param  trailChar
     * @param  trailMustExist    true: if trail isn't the given one: return null / otherwise return as is
     * @return String
     */
    private static String removePostfixChar(String sourceStr, String trailChar, Boolean trailMustExist) {
        if (sourceStr != null && sourceStr.lastIndexOf(trailChar)  == sourceStr.length() -1) {
            return sourceStr.substring(0, sourceStr.length() -1 );
        }

        return trailMustExist ? null : sourceStr;
    }

    private static String removePostfixChar(String sourceStr, String trailChar) {
        return removePostfixChar(sourceStr, trailChar, true);
    }

    /**
     * @param  treePathsPrefStr    String representing an array of TreePath strings
     * @return Array of strings representing a treePath each | null if the string's format isn't as expected
     */
    public static String[] extractTreePathStringsFromPref(String treePathsPrefStr) {
        treePathsPrefStr = StringHelper.removePrefixChar(treePathsPrefStr, "[");
        treePathsPrefStr = StringHelper.removePostfixChar(treePathsPrefStr, "]");
        if (treePathsPrefStr == null) {
            return null;
        }

        if (treePathsPrefStr.contains("],")) {
            /* There are multiple TreePath strings contained */
            String[] treePathStrings = treePathsPrefStr.split("\\]");
            int count = 0;
            for (String curTreePath: treePathStrings) {
                curTreePath = StringHelper.removePrefixChar(curTreePath, ",", false);
                curTreePath = StringHelper.removePrefixChar(curTreePath, " ", false);
                curTreePath = StringHelper.removePrefixChar(curTreePath, "[");
                curTreePath = curTreePath.replaceAll(", ", "\\/");

                treePathStrings[count] = curTreePath;
                count++;
            }

            return treePathStrings;
        }

        /* There's only one TreePath string contained */
        treePathsPrefStr = StringHelper.removePrefixChar(treePathsPrefStr, "[");
        treePathsPrefStr = StringHelper.removePostfixChar(treePathsPrefStr, "]");
        treePathsPrefStr = treePathsPrefStr.replaceAll(", ", "\\/");

        return new String[] { treePathsPrefStr };
    }
}