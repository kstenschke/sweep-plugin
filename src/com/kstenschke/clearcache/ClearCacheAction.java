/*
 * Copyright 2013 Kay Stenschke
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

package com.kstenschke.clearcache;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.Gray;
import com.intellij.ui.awt.RelativePoint;
import com.kstenschke.clearcache.helpers.SelectedDirectoriesCollector;
import com.kstenschke.clearcache.helpers.StringHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ClearCacheAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
		String[] cachePaths = {};

		String cachePathsPrefString	= ClearCachePreferences.getPaths();
		if( cachePathsPrefString != null && ! cachePathsPrefString.isEmpty() ) {
			cachePaths     = StringHelper.extractTreePathStringsFromPref(cachePathsPrefString);
		}

		if( cachePaths == null || cachePaths.length == 0 ) {
			JOptionPane.showMessageDialog(null, "Please configure cache path(s) in the plugin preferences.");
		} else {
			Integer amountDeleted = this.clearFoldersContent(cachePaths);

			Balloon.Position pos = Balloon.Position.below;
			String balloonText   = "Removed " + amountDeleted + " files and folders";
			BalloonBuilder builder = JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(balloonText, null, new Color(245, 245, 245), null);
			Balloon balloon = builder.createBalloon();
			balloon.setAnimationEnabled(true);

			Component eventComponent	= e.getInputEvent().getComponent();
			Point componentLocation = eventComponent.getLocation();
			Integer	x= new Double(componentLocation.getX()).intValue() + eventComponent.getWidth() + 40;
			Integer	y= new Double(componentLocation.getY()).intValue() + eventComponent.getHeight() + 42;
			RelativePoint balloonPosition = new RelativePoint( new Point(x, y) );
			balloon.show(balloonPosition, pos);
		}
	}



	/**
	 * Remove all contents of given directories
	 *
	 * @param	paths
	 */
	private Integer clearFoldersContent(String[] paths) {
		Integer amountDeleted = 0;
		for(String curPath: paths) {
			amountDeleted += deleteFolderContents(curPath, false);
		}

		return amountDeleted;
	}



	/**
	 * Delete all files/folders inside given path recursively
	 * Optionally also remove the folder at the path itself
	 *
	 * @param	path
	 * @param	removeFolderItself
	 * @return	Amount of deleted files and folders
	 */
	private Integer deleteFolderContents(String path, Boolean removeFolderItself) {
		Integer amountDeleted = 0;
		File folder = new File(path);
		File[] files = folder.listFiles();

		if(files != null) { //some JVMs return null for empty dirs
			for(File curFile: files) {
				if( curFile.isDirectory() ) {
					amountDeleted	+= deleteFolderContents(curFile.getPath(), true);
				} else {
					amountDeleted	+= curFile.delete() ? 1:0;
				}
			}
		}
		if( removeFolderItself ) {
			amountDeleted+= folder.delete() ? 1:0;
		}

		return amountDeleted;
	}

}
