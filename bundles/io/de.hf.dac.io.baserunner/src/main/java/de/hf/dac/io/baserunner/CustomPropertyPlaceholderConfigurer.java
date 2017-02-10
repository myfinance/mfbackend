/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : CustomPropertyPlaceholderConfigurer.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.baserunner;

import de.hf.dac.api.io.efmb.DatabaseInfo;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * Ermöglicht es Spring configuration vor der Initialisierung (Konstruktion des Spring Application Contexts)
 * mit dynamischen Werten (z.B. aus der KOmmandozeile) zu überschreiben.
 */
public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    //
    private static Properties overrideProperties = new Properties();

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props) {
        if (overrideProperties != null) {
            String value = overrideProperties.getProperty(placeholder);
            if (value != null) {
                // override properties haben vorrang
                return value;
            }
        }
        //
        return super.resolvePlaceholder(placeholder, props);
    }

    /**
     * Sets a property that will be overriden.
     *
     * @param key
     *        The key of the property
     * @param value
     *        The value of the property
     */
    public static void setOverrideProperty(String key, String value) {
        overrideProperties.setProperty(key, value);
    }

    /**
     * Set all parameter of the database configuration as overrides.
     *
     * @param prefix
     *        The prefix in for properties
     * @param dbInfo
     *        The database info
     */
    public static void setDatabaseInfo(String prefix, DatabaseInfo dbInfo) {
        setOverrideProperty(prefix + ".url", dbInfo.getUrl());
        setOverrideProperty(prefix + ".username", dbInfo.getUser());
        setOverrideProperty(prefix + ".password", dbInfo.getPassword());
        setOverrideProperty(prefix + ".driver.class.name", dbInfo.getDriver());
    }
}

