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

package com.kstenschke.sweep;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.awt.RelativePoint;
import com.kstenschke.sweep.helpers.StringHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SweepAction extends AnAction {

    private String[] ignorePatterns = null;

    /**
     * @param event    ActionSystem event
     */
    public void actionPerformed(AnActionEvent event) {
        String[] sweepPaths = {};

        String sweepPathsPrefString = SweepPreferences.getPaths();
        if (sweepPathsPrefString != null && ! sweepPathsPrefString.isEmpty()) {
            sweepPaths  = StringHelper.extractTreePathStringsFromPref(sweepPathsPrefString);
        }

        if (sweepPaths == null || sweepPaths.length == 0) {
            JOptionPane.showMessageDialog(null, "Please configure path(s) to be swept in the plugin preferences.");
        } else {
            Integer[] amountDeleted = this.sweepFoldersContent(sweepPaths);

            Balloon.Position pos = Balloon.Position.below;
            String balloonText   = "Deleted " + amountDeleted[0] + " directories and " + amountDeleted[1] + " files";

            JBColor backgroundColor = new JBColor(new Color(245, 245, 245), new Color(60, 63, 65));
            BalloonBuilder builder = JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(balloonText, null, backgroundColor, null);
            Balloon balloon = builder.createBalloon();
            balloon.setAnimationEnabled(true);

            Component eventComponent = event.getInputEvent().getComponent();
            Point componentLocation = eventComponent.getLocation();
            Integer    x= new Double(componentLocation.getX()).intValue() + eventComponent.getWidth() + 40;
            Integer    y= new Double(componentLocation.getY()).intValue() + eventComponent.getHeight() + 42;
            RelativePoint balloonPosition = new RelativePoint(new Point(x, y));

            balloon.show(balloonPosition, pos);
        }
    }

    /**
     * Remove contents of given directories
     *
     * @param  sweepPaths        Directory paths to be sweeped, from properties component preferences
     * @return Array of integers: amount of deleted 1. folders, 2. files
     */
    private Integer[] sweepFoldersContent(String[] sweepPaths) {
        Integer[] amountDeleted = {0, 0};
        Boolean deleteHidden = SweepPreferences.getDeleteHidden();

        for(String curPath: sweepPaths) {
            Integer[] addAmountDeleted = deleteFolderContents(curPath, false, deleteHidden);
            amountDeleted[0] += addAmountDeleted[0];
            amountDeleted[1] += addAmountDeleted[1];
        }

        return amountDeleted;
    }

    /**
     * @param  str
     * @return Does any of the "ignore patterns" match the given string?
     */
    private Boolean isMatchingIgnorePattern(String str) {
        if(this.ignorePatterns == null ) {
            this.ignorePatterns  = SweepPreferences.getIgnorePatterns().split(",");
        }

        if (this.ignorePatterns.length > 0) {
            for (String ignorePattern : this.ignorePatterns) {
                if (str.contains(ignorePattern)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Delete all files/folders inside given path recursively
     * Optionally also remove the folder at the path itself
     *
     * @param  path
     * @param  removeFolderItself
     * @param  deleteHidden
     * @return Array of integers - amount of deleted 1. folders, 2. files
     */
    private Integer[] deleteFolderContents(String path, Boolean removeFolderItself, Boolean deleteHidden) {
        Integer[] amountDeleted = {0, 0};
        Boolean deleteSubFolders= (removeFolderItself || deleteHidden) ? true : SweepPreferences.getDeleteDirectories();

        File folder = new File(path);
        File[] files= folder.listFiles();

        //some JVMs return null for empty dirs
        if(files != null) {
            for(File curFile: files) {
                if (!curFile.isHidden() || deleteHidden) {
                    if (curFile.isDirectory()) {
                        Integer[] addAmountDeleted = deleteFolderContents(curFile.getPath(), deleteSubFolders, deleteHidden);

                        amountDeleted[0]    += addAmountDeleted[0];
                        amountDeleted[1]    += addAmountDeleted[1];
                    } else {
                        if(! isMatchingIgnorePattern(curFile.toString())) {
                            amountDeleted[1]    += curFile.delete() ? 1:0;
                        }
                    }
                }
            }
        }

        if (removeFolderItself && (!folder.isHidden() || deleteHidden)) {
            if(! isMatchingIgnorePattern(folder.toString())) {
                amountDeleted[0]    += folder.delete() ? 1:0;
            }
        }

        return amountDeleted;
    }

}
