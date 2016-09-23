/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ResFileParser.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 22.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env.resfile;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parse the the .res files (windows configuration files) and puts results into an internal map.
 */
public class ResFileParser {
    private static final Logger LOG = LoggerFactory.getLogger(ResFileParser.class);

    // contains map from section to the param Map
    private Map<String, Map<String, String>> sectionMap = new TreeMap<String, Map<String, String>>();

    /**
     * The default constructor.
     */
    public ResFileParser() {
        super();
    }

    /**
     * Gets the string value from the specified property.
     *
     * @param section
     *        The section [section]
     * @param key
     *        The key
     * @return The string value or null if not found
     */
    public String getString(String section, String key) {
        Map<String, String> paramMap = sectionMap.get(section);
        if (paramMap == null) {
            return null;
        }
        return paramMap.get(key);
    }

    /**
     * Gets the string value from the specified property or the default value.
     *
     * @param section
     *        The section [section]
     * @param key
     *        The key
     * @param def
     *        The default value
     * @return The string value or def if not found
     */
    public String getString(String section, String key, String def) {
        String param = getString(section, key);
        if (param == null) {
            return def;
        }
        return param;
    }

    Double getDouble(String section, String key) {
        String param = getString(section, key);
        if (param == null) {
            return null;
        }
        return Double.parseDouble(param);
    }

    public void load(final String filename) {
        String section = "default"; // current section name
        int n;
        LineNumberReader reader = null;
        String line = ""; // current input line
        int lineNumber = 1;
        try {
            reader = new LineNumberReader(new FileReader(filename));

            // Read until the end of the file
            while ((line = reader.readLine()) != null) {
                line = trimWs(line); // Trim whitespace from both ends
                if (!line.isEmpty()) {
                    if (line.charAt(0) == '#') {
                        continue; // Skip commented line
                    }
                    while (line.endsWith("\\")) {
                        line = line.substring(0, line.length() - 1);
                        String nextLine = reader.readLine();
                        lineNumber++;
                        if (nextLine != null) {
                            nextLine = trimWs(nextLine); // Trim whitespace from both ends
                            line += nextLine;
                        }
                    }
                    // Handle section name
                    if (line.charAt(0) == '[' && line.charAt(line.length() - 1) == ']') {
                        section = line.substring(1, line.length() - 1);
                        if (section.isEmpty()) {
                            throw new RuntimeException("Invalid syntax: empty section name not allowed");
                        }
                    } else {
                        n = line.indexOf('=');
                        if (n != -1) {
                            String name = trimWs(line.substring(0, n));
                            String value = trimWs(line.substring(n + 1));
                            if (name.isEmpty()) {
                                throw new RuntimeException("Invalid syntax: empty key name not allowed");
                            }
                            Map<String, String> paramMap = sectionMap.get(section);
                            if (paramMap == null) {
                                paramMap = new TreeMap<String, String>();
                                sectionMap.put(section, paramMap);
                            }
                            if (value != null) {
                                value = decrypt(value);
                            }
                            paramMap.put(name, value);
                        } else {
                            throw new RuntimeException("Invalid syntax: " + line);
                        }
                    }
                }
                lineNumber++;
            }
        } catch (Exception e) {
            LOG.error("Error in line " + lineNumber + " " + line, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.debug("Could not close res file " + filename, e);
                }
            }

        }
    }

    /**
     * Gets the boolean value from the specified property or the default value.
     *
     * @param section
     *        The section [section]
     * @param key
     *        The key
     * @param def
     *        The default value
     * @return The string value or def if not found
     */
    public boolean getBoolean(String section, String key, boolean def) {
        String param = getString(section, key);
        if (param == null) {
            return def;
        }
        return convertToBool(param);
    }

    Boolean convertToBool(final String str) {
        if (str.length() == 0) {
            return null;
        }
        int ch = str.charAt(0);
        if (ch == 'y' || ch == 'Y' || ch == '1' || ch == 't' || ch == 'T') {
            return true;
        }
        if (ch == 'n' || ch == 'N' || ch == '0' || ch == 'f' || ch == 'F') {
            return false;
        }

        return null;
    }

    /**
     * Trims whitespace at start and end of string (handles nulls).
     *
     * @param s
     *        The string to trim, can be null
     * @return The trimmed string
     */
    static String trimWs(final String s) {
        if (s.length() == 0) {
            return s;
        }
        int start = 0;
        int end = s.length();
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }
        while (end > 0 && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }
        if (end <= start) {
            return "";
        }
        return s.substring(start, end);
    }

    // trim_ws ====================================================================

    /**
     * Decrypt the string if it contains a password.
     *
     * @param value
     *        The string to decrypt
     * @return The decrypted string or the original value
     */
    public static String decrypt(String value) {
        if (value.length() > 2 && value.substring(0, 2).equals("@@")) {
            final String key = "achievingMoreTogether";
            StringBuffer sb = new StringBuffer();
            String str = value.substring(2);
            final byte r = (byte) ((str.charAt(0)) - 65 + (str.charAt(1) - 65) * 16);
            for (int i = 2, j = 0; i < str.length(); i = i + 2, j = (j + 1) % key.length()) {
                byte c = (byte) (str.charAt(i) - 65 + (str.charAt(i + 1) - 65) * 16);
                c ^= key.charAt(j);
                c = (byte) (c ^ r);
                sb.append((char) c);
            }
            return sb.toString();
        }
        return value;
    }

    public Map<String, String> getProperties(String section) {
        return this.sectionMap.get(section);
    }

    /**
     * Main method for testing.
     *
     * @param args
     *        The command line arguments.
     */
    public static void main(String[] args) {
        // ResFileParser parser = new ResFileParser();
        // parser.load("poet.res");
        String dec = decrypt("@@LNJMJNGNBNBNNJ");
        System.out.println(dec);
        String dec2 = decrypt("@@" + dec);
        System.out.println(dec2);
    }
}
