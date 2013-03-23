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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NonNls;
import com.intellij.ide.util.PropertiesComponent;

/**
 * Utility functions for preferences handling
 * All preferences of the ClearCache plugin are stored on project level
 */
public class ClearCachePreferences {

    // @NonNls = element is not a string requiring internationalization and it does not contain such strings.
    @NonNls
    private static final String PROPERTY_PATHS = "PluginClearCache.Paths";

    @NonNls
    private static final String PROPERTY_DELETE_DIRECTORIES = "PluginClearCache.DeleteDirectories";

    @NonNls
    private static final String PROPERTY_DELETE_HIDDEN = "PluginClearCache.DeleteHidden";



	private static Project getOpenProject() {
		Project[] projects = ProjectManager.getInstance().getOpenProjects();

		return (projects.length > 0) ? projects[0] : null;
	}

	private static PropertiesComponent getPropertiesComponent() {
		Project project = getOpenProject();

		return project != null ? PropertiesComponent.getInstance(project) : null;
	}



    /**
     * Store path(s) preference
     *
     * @param paths Contents to be stored in paths preference
     */
    public static void savePaths(String paths) {
		PropertiesComponent propertiesComponent = getPropertiesComponent();

		if( propertiesComponent != null ) {
			propertiesComponent.setValue(PROPERTY_PATHS, paths);
		}
    }

    public static void saveDeleteDirectories(Boolean delete) {
		PropertiesComponent propertiesComponent = getPropertiesComponent();

		if( propertiesComponent != null ) {
			propertiesComponent.setValue(PROPERTY_DELETE_DIRECTORIES, delete? "1":"0");
		}
    }

    public static void saveDeleteHidden(Boolean delete) {
		PropertiesComponent propertiesComponent = getPropertiesComponent();

		if( propertiesComponent != null ) {
			propertiesComponent.setValue(PROPERTY_DELETE_HIDDEN, delete ? "1":"0");
		}
    }



    /**
     * Get path(s) preference
     *
     * @return String
     */
    public static String getPaths() {
		PropertiesComponent propertiesComponent = getPropertiesComponent();
		String paths	= null;

		if( propertiesComponent != null ) {
			paths	= propertiesComponent.getValue(PROPERTY_PATHS);
		}

        return paths == null ? "" : paths;
    }



    public static Boolean getDeleteDirectories() {
		PropertiesComponent propertiesComponent = getPropertiesComponent();
		Boolean delete	= false;

		if( propertiesComponent != null ) {
			String pref	= propertiesComponent.getValue(PROPERTY_DELETE_DIRECTORIES);
			delete	= pref != null && pref.equals("1");
		}

        return delete;
    }

    public static Boolean getDeleteHidden() {
		PropertiesComponent propertiesComponent = getPropertiesComponent();
		Boolean delete	= false;

		if( propertiesComponent != null ) {
			String pref	= propertiesComponent.getValue(PROPERTY_DELETE_HIDDEN);
			delete	= pref != null && pref.equals("1");
		}

        return delete;
    }

}
