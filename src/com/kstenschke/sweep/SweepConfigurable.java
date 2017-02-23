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

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.kstenschke.sweep.resources.forms.SweepConfiguration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SweepConfigurable implements Configurable {

    private SweepConfiguration settingsPanel;

    @Nls
    @Override
    public String getDisplayName() {
        return "Sweep";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingsPanel == null) {
            settingsPanel = new SweepConfiguration();
        }

        return settingsPanel.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return settingsPanel != null && settingsPanel.isModified();
    }

    /**
     * Store project preference: directories to be swept
     *
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        if (settingsPanel != null) {
            String paths = settingsPanel.getData();
            if (paths != null) {
                SweepPreferences.savePaths(paths);
            }

            SweepPreferences.saveDeleteDirectories(settingsPanel.isSelectedDeleteDirectories());
            SweepPreferences.saveDeleteHidden(settingsPanel.isSelectedDeleteHidden());
            SweepPreferences.saveIgnorePatterns(settingsPanel.getIgnorePatterns());
        }
    }

    @Override
    public void reset() {

    }

    public void disposeUIResources() {
        settingsPanel = null;
    }

}
