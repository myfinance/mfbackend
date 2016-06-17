/** ----------------------------------------------------------------------------
 *
 * ---          t                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : Configuration.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 08.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.common.runner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Configuration {
    static ResFileParser resFileParser = new ResFileParser();
    private static boolean isDebug = false;

    protected Configuration() {
    }

    public static void parseResFile(String filename) {
        resFileParser.load(filename);
    }

    public static String getString(String section, String key) {
        return resFileParser.getString(section, key);
    }

    public static Set<String> getStringList(String section, String key) {
        String temp = resFileParser.getString(section, key);
        String[] splitted = temp.split(",");
        if(splitted == null) {
            return Collections.emptySet();
        } else {
            HashSet result = new HashSet();
            String[] arr$ = splitted;
            int len$ = splitted.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String part = arr$[i$];
                result.add(part.trim());
            }

            return result;
        }
    }

    public static String getPath(String section, String key) {
        String path = resFileParser.getString(section, key);
        return path == null?null:(!path.endsWith("\\") && !path.endsWith("/")?path + "/":path);
    }

    public static String getString(String section, String key, String def) {
        return resFileParser.getString(section, key, def);
    }

    public static int getInt(String section, String key, int def) {
        String str = resFileParser.getString(section, key);
        return str == null?def:Integer.parseInt(str.trim());
    }

    public static boolean getBoolean(String section, String key, boolean def) {
        return resFileParser.getBoolean(section, key, def);
    }

    public static Double getDouble(String section, String key) {
        return resFileParser.getDouble(section, key);
    }

    public static void setResFileParser(ResFileParser resFileParser) {
        resFileParser = resFileParser;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setIsDebug(boolean b) {
        isDebug = b;
    }
}

