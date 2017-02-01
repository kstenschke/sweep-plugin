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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.kstenschke.sweep.resources.forms.SweepConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SweepSettings implements ApplicationComponent {

    public SweepConfiguration settingsPanel = null;

    private ImageIcon icon = new ImageIcon("/com/kstenschke/sweep/resources/icons/broom.png");

    /**
     * Constructor
     */
    public SweepSettings() {
        this.settingsPanel = new SweepConfiguration();
    }

    public static SweepSettings getInstance() {
        return ApplicationManager.getApplication().getComponent(SweepSettings.class);
    }

    /**
     * Get the icon of this {@link Configurable}.
     */
    public Icon getIcon() {
        if (icon == null) {
            icon = new ImageIcon("/com/kstenschke/sweep/resources/icons/broom.png");
        }

        return icon;
    }

   public void initComponent() {

    }

    public void disposeComponent() {

    }

    @NotNull
    public String getComponentName() {
        return "Sweep";
    }

}
