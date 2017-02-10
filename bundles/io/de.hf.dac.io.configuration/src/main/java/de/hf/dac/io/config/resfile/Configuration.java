/** ----------------------------------------------------------------------------
 *
 * ---          HF- Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : Configuration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.config.resfile;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Reads and manages static configuration from .res files.
 *
 */
public class Configuration {

    /** The res file parser. */
    static ResFileParser resFileParser = new ResFileParser();
    private static boolean isDebug = false;

    /**
     * Instantiates a new configuration.
     */
    protected Configuration() {
        super();
    }

    /**
     * Parses a res file and stores it in the map.
     *
     * @param filename
     *        The name of the res file
     */
    public static void parseResFile(String filename) {
        resFileParser.load(filename);
    }

    /**
     * Reads an entry in the configuration.
     *
     * @param section
     *        The name of the section
     * @param key
     *        The key to read
     * @return The value of the key in the section, or null
     */
    public static String getString(String section, String key) {
        return resFileParser.getString(section, key);
    }

    public static Set<String> getStringList(String section, String key) {
        String temp = resFileParser.getString(section, key);
        String[] splitted = temp.split(",");
        if (splitted != null) {
            Set<String> result = new HashSet<>();
            for (String part : splitted) {
                result.add(part.trim());
            }
            return result;
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * Reads an entry as a path (adding trailing slash if necessary) in the configuration.
     *
     * @param section
     *        The name of the section
     * @param key
     *        The key to read
     * @return The value of the key in the section, or null
     */
    public static String getPath(String section, String key) {
        String path = resFileParser.getString(section, key);
        if (path == null) {
            return null;
        }
        if (!path.endsWith("\\") && !path.endsWith("/")) {
            return path + "/";
        }
        return path;
    }

    /**
     * Reads a string entry in the configuration, with a default value.
     *
     * @param section
     *        The name of the section
     * @param key
     *        The key to read
     * @param def
     *        The default value to return if the key does not exist
     * @return The value of the key in the section or def
     */
    public static String getString(String section, String key, String def) {
        return resFileParser.getString(section, key, def);
    }

    /**
     * Reads a int entry in the configuration, with a default value.
     *
     * @param section
     *        The name of the section
     * @param key
     *        The key to read
     * @param def
     *        The default value to return if the key does not exist
     * @return The value of the key in the section or def
     */
    public static int getInt(String section, String key, int def) {
        String str = resFileParser.getString(section, key);
        if (str == null) {
            return def;
        }

        return Integer.parseInt(str.trim());
    }

    /**
     * Reads a boolean entry in the configuration, with a default value.
     *
     * @param section
     *        The name of the section
     * @param key
     *        The key to read
     * @param def
     *        The default value to return if the key does not exist
     * @return The value of the key in the section or def
     */
    public static boolean getBoolean(String section, String key, boolean def) {
        return resFileParser.getBoolean(section, key, def);
    }

    /**
     * Reads a double entry in the configuration.
     *
     * @param section
     *        The name of the section
     * @param key
     *        The key to read
     * @return The value of the key in the section, or null
     */
    public static Double getDouble(String section, String key) {
        return resFileParser.getDouble(section, key);
    }

    public static void setResFileParser(ResFileParser resFileParser) {
        Configuration.resFileParser = resFileParser;
    }

    public static boolean isDebug() {
        return Configuration.isDebug;
    }

    public static void setIsDebug(boolean b) {
        Configuration.isDebug = b;
    }
}
