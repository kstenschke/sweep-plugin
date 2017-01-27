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

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.kstenschke.sweep.resources.forms.PluginConfiguration;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SweepSettingsComponent implements ProjectComponent, Configurable {

    private ImageIcon icon = new ImageIcon("/com/kstenschke/sweep/resources/images/blank32x32.png");

    private PluginConfiguration settingsPanel = null;



	public JComponent createComponent() {
		if (settingsPanel == null) {
			settingsPanel = new PluginConfiguration();
		}

		reset();

		return settingsPanel.getRootPanel();
	}

	@Nls
	public String getDisplayName() {
		return "Sweep";
	}

	public boolean isModified() {
		return settingsPanel != null && settingsPanel.isModified();
	}

	public void disposeUIResources() {
		settingsPanel = null;
	}

	public void reset() {
		if (settingsPanel != null ) {

		}
	}

	/**
	 * Get the icon of this {@link com.intellij.openapi.options.Configurable}.
	 */
	public Icon getIcon() {
		if (icon == null) {
			icon	= new ImageIcon("/com/kstenschke/sweep/resources/images/blank32x32.png");
		}

		return icon;
	}

	/**
	 * Store project preference: directories to be swept
	 *
	 * @throws ConfigurationException
	 */
	public void apply() throws ConfigurationException {
		if (settingsPanel != null) {
			String paths	= settingsPanel.getData();
			if (paths != null) {
				SweepPreferences.savePaths(paths);
			}

			SweepPreferences.saveDeleteDirectories(settingsPanel.isSelectedDeleteDirectories());
			SweepPreferences.saveDeleteHidden(settingsPanel.isSelectedDeleteHidden());
			SweepPreferences.saveIgnorePatterns(settingsPanel.getIgnorePatterns());

			applyGlobalSettings();
	  }
	}

	public String getHelpTopic() {
		return null;
	}

	private void applyGlobalSettings() {

	}

	public SweepSettingsComponent(Project project) {

	}

   public void initComponent() {

	}

	public void disposeComponent() {
		settingsPanel = null;
	}

	@NotNull
	public String getComponentName() {
		return "Sweep";
	}

	/**
	 * called when project is opened
	 */
	public void projectOpened() {

	}

	/**
	 * called when project is being closed
	 */
	public void projectClosed() {

	}

}
