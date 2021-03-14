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

import com.intellij.openapi.vfs.VirtualFile;
import com.kstenschke.sweep.SweepPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectedDirectoriesCollector {

    private final VirtualFile baseDir;

    private final List<VirtualFile> selectedVFDirectories;

    private String[] selectionPathStrings;

    public SelectedDirectoriesCollector(VirtualFile baseDir) {
        this.baseDir = baseDir;
        selectedVFDirectories = new ArrayList<>();
    }

    /**
     * @return Virtual files of selected directories
     */
    public VirtualFile[] getSelectedVFDirectories() {
        String selectionPathsPrefString =  SweepPreferences.getPaths();
        if (selectionPathsPrefString.isEmpty()) {
            return null;
        }

        selectionPathStrings = StringHelper.extractTreePathStringsFromPref(selectionPathsPrefString);
        if (selectionPathStrings != null && selectionPathStrings.length > 0) {
            String curPath = baseDir.getPath();
            if (Arrays.asList(selectionPathStrings).contains(curPath)) {
                selectedVFDirectories.add(baseDir);
            }

            this.findSelectedSubDirectories(baseDir);

            if (selectedVFDirectories.size() > 0) {
                return selectedVFDirectories.toArray(new VirtualFile[0]);
            }
        }

        return null;
    }

    /**
     * Iterate all children of given directory recursively,
     * if any path-string equals to a TreePath in the preferences: add it to the array of selected VirtualFiles
     *
     * @param directory
     */
    private void findSelectedSubDirectories(VirtualFile directory) {
        VirtualFile[] subDirectories = directory.getChildren();

        for (VirtualFile curDirectoryVF: subDirectories) {
            if (curDirectoryVF.isDirectory() /*&& ! isFileHidden(curDirectoryVF)*/) {
                String curPath = curDirectoryVF.getPath();

                if (Arrays.asList(selectionPathStrings).contains(curPath)) {
                    selectedVFDirectories.add(curDirectoryVF);
                }

                findSelectedSubDirectories(curDirectoryVF);
            }
        }
    }
}
