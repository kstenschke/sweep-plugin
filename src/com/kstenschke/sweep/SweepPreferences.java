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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NonNls;
import com.intellij.ide.util.PropertiesComponent;

/**
 * Utility functions for preferences handling
 * All preferences of the Sweep plugin are stored on project level
 */
public class SweepPreferences {

    // @NonNls = element is not a string requiring internationalization and it does not contain such strings.
    @NonNls
    private static final String PROPERTY_PATHS = "PluginSweep.Paths";

    @NonNls
    private static final String PROPERTY_DELETE_DIRECTORIES = "PluginSweep.DeleteDirectories";

    @NonNls
    private static final String PROPERTY_DELETE_HIDDEN = "PluginSweep.DeleteHidden";

    @NonNls
    private static final String PROPERTY_IGNORE_PATTERNS = "PluginSweep.IgnorePatterns";



	/**
	 * @return	The currently opened project
	 */
	private static Project getOpenProject() {
		Project[] projects = ProjectManager.getInstance().getOpenProjects();

		return (projects.length > 0) ? projects[0] : null;
	}

	/**
	 * @return	PropertiesComponent (project level)
	 */
	private static PropertiesComponent getPropertiesComponent() {
		Project project = getOpenProject();

		return project != null ? PropertiesComponent.getInstance(project) : null;
	}

    /**
     * Store preference: paths of cache directories
     *
     * @param	paths Contents to be stored in paths preference
     */
    public static void savePaths(String paths) {
		PropertiesComponent propertiesComponent = getPropertiesComponent();

		if (propertiesComponent != null) {
			propertiesComponent.setValue(PROPERTY_PATHS, paths);
		}
    }

	/**
	 * Store preference: delete directories?
	 *
	 * @param	delete
	 */
    public static void saveDeleteDirectories(Boolean delete) {
		PropertiesComponent propertiesComponent = getPropertiesComponent();

		if (propertiesComponent != null) {
			propertiesComponent.setValue(PROPERTY_DELETE_DIRECTORIES, delete? "1":"0");
		}
    }

	/**
	 * Store preference: delete hidden directories and files?
	 *
	 * @param	delete
	 */
    public static void saveDeleteHidden(Boolean delete) {
		PropertiesComponent propertiesComponent = getPropertiesComponent();

		if (propertiesComponent != null) {
			propertiesComponent.setValue(PROPERTY_DELETE_HIDDEN, delete ? "1":"0");
		}
    }

    /**
     * Get preference: paths of cache directories
     *
     * @return String
     */
    public static String getPaths() {
		PropertiesComponent propertiesComponent = getPropertiesComponent();
		String paths	= null;

		if (propertiesComponent != null) {
			paths	= propertiesComponent.getValue(PROPERTY_PATHS);
		}

        return paths == null ? "" : paths;
    }

	/**
	 * Get preference: delete directories?
	 *
	 * @return	Boolean
	 */
    public static Boolean getDeleteDirectories() {
		PropertiesComponent propertiesComponent = getPropertiesComponent();
		Boolean delete	= false;

		if (propertiesComponent != null) {
			String pref	= propertiesComponent.getValue(PROPERTY_DELETE_DIRECTORIES);
			delete	= pref != null && pref.equals("1");
		}

        return delete;
    }

	/**
	 * Get preference: delete hidden directories and files?
	 *
	 * @return	Boolean
	 */
	public static Boolean getDeleteHidden() {
		PropertiesComponent propertiesComponent = getPropertiesComponent();
		Boolean delete	= false;

		if (propertiesComponent != null) {
			String pref	= propertiesComponent.getValue(PROPERTY_DELETE_HIDDEN);
			delete	= pref != null && pref.equals("1");
		}

		return delete;
	}

	/**
	 * Get preference: Ignore patterns
	 *
	 * @return	String
	 */
	public static String getIgnorePatterns() {
		PropertiesComponent propertiesComponent = getPropertiesComponent();
		String ignorePatterns	= "";

		if (propertiesComponent != null) {
			String pref	= propertiesComponent.getValue(PROPERTY_IGNORE_PATTERNS);
			ignorePatterns	= pref != null ? pref : "";
		}

		return ignorePatterns;
	}

	/**
	 * Store preference: ignorePatterns
	 *
	 * @param	ignorePatterns
	 */
	public static void saveIgnorePatterns(String ignorePatterns) {
		PropertiesComponent propertiesComponent = getPropertiesComponent();

		if (propertiesComponent != null) {
			propertiesComponent.setValue(PROPERTY_IGNORE_PATTERNS, ignorePatterns);
		}
	}

}
