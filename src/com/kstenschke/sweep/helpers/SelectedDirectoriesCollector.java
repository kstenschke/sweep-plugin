/*
 * Copyright 2013-2017 Kay Stenschke
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

	private VirtualFile baseDir;

	private List<VirtualFile> selectedVFDirectories;

	private String[] selectionPathStrings;



	/**
	 * Constructor
	 */
	public SelectedDirectoriesCollector(VirtualFile baseDir) {
		this.baseDir	= baseDir;
		selectedVFDirectories = new ArrayList<VirtualFile>();
	}


	/**
	 * @return	Virtual files of selected (=cache) directories
	 */
	public VirtualFile[] getSelectedVFDirectories() {
		String selectionPathsPrefString    =  SweepPreferences.getPaths();

		if (! selectionPathsPrefString.isEmpty()) {
			selectionPathStrings     = StringHelper.extractTreePathStringsFromPref(selectionPathsPrefString);

			if (selectionPathStrings != null && selectionPathStrings.length > 0) {
				String curPath = baseDir.getPath();
				if (Arrays.asList(selectionPathStrings).contains(curPath)) {
					selectedVFDirectories.add(baseDir);
				}

				this.findSelectedSubDirectories(baseDir);

				if (selectedVFDirectories != null && selectedVFDirectories.size() > 0) {
					return selectedVFDirectories.toArray(new VirtualFile[selectedVFDirectories.size()]);
				}
			}
		}

		return null;
	}



	/**
	 * Iterate all children of given directory recursively,
	 * if any path-string equals to a TreePath in the preferences: add it to the array of selected VirtualFiles
	 *
	 * @param	directory
	 */
	private void findSelectedSubDirectories(VirtualFile directory) {
		VirtualFile[] subDirectories = directory.getChildren();

		for(VirtualFile curDirectoryVF: subDirectories) {
			if (curDirectoryVF.isDirectory() /*&& ! isFileHidden(curDirectoryVF)*/) {
				String curPath = curDirectoryVF.getPath();

				if (Arrays.asList(selectionPathStrings).contains(curPath)) {
					selectedVFDirectories.add(curDirectoryVF);
				}

				findSelectedSubDirectories(curDirectoryVF);
			}
		}
	}




//	private static boolean isFileHidden(VirtualFile virtualFile) {
//		if (virtualFile == null || !virtualFile.isValid()) return false;
//		if (!virtualFile.isInLocalFileSystem()) return false;
//
//		File file = new File(virtualFile.getPath().replace('/', File.separatorChar));
//
//		return file.getParent() != null && file.isHidden(); // Under Windows logical driver files (e.g C:\) are hidden.
//	}
}
