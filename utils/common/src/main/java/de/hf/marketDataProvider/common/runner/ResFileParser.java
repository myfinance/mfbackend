/** ----------------------------------------------------------------------------
 *
 * ---                                 ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : ResFileParser.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 08.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.common.runner;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResFileParser {
    private static final Logger LOG = LoggerFactory.getLogger(ResFileParser.class);
    private Map<String, Map<String, String>> sectionMap = new TreeMap();

    public ResFileParser() {
    }

    public String getString(String section, String key) {
        Map paramMap = (Map)this.sectionMap.get(section);
        return paramMap == null?null:(String)paramMap.get(key);
    }

    public String getString(String section, String key, String def) {
        String param = this.getString(section, key);
        return param == null?def:param;
    }

    Double getDouble(String section, String key) {
        String param = this.getString(section, key);
        return param == null?null:Double.valueOf(Double.parseDouble(param));
    }

    void load(String filename) {
        String section = "default";
        LineNumberReader reader = null;
        String line = "";
        int lineNumber = 1;

        try {
            reader = new LineNumberReader(new FileReader(filename));

            while(true) {
                while(true) {
                    if((line = reader.readLine()) == null) {
                        return;
                    }

                    line = trimWs(line);
                    if(line.isEmpty()) {
                        break;
                    }

                    if(line.charAt(0) != 35) {
                        String e;
                        while(line.endsWith("\\")) {
                            line = line.substring(0, line.length() - 1);
                            e = reader.readLine();
                            ++lineNumber;
                            if(e != null) {
                                e = trimWs(e);
                                line = line + e;
                            }
                        }

                        if(line.charAt(0) == 91 && line.charAt(line.length() - 1) == 93) {
                            section = line.substring(1, line.length() - 1);
                            if(section.isEmpty()) {
                                throw new RuntimeException("Invalid syntax: empty section name not allowed");
                            }
                            break;
                        }

                        int n = line.indexOf(61);
                        if(n == -1) {
                            throw new RuntimeException("Invalid syntax: " + line);
                        }

                        e = trimWs(line.substring(0, n));
                        String value = trimWs(line.substring(n + 1));
                        if(e.isEmpty()) {
                            throw new RuntimeException("Invalid syntax: empty key name not allowed");
                        }

                        Map<String, String> paramMap = (Map)this.sectionMap.get(section);
                        if(paramMap == null) {
                            paramMap = new TreeMap<>();
                            this.sectionMap.put(section, paramMap);
                        }

                        if(value != null) {
                            value = decrypt(value);
                        }

                        ((Map)paramMap).put(e, value);
                        break;
                    }
                }

                ++lineNumber;
            }
        } catch (Exception var18) {
            LOG.error("Error in line " + lineNumber + " " + line, var18);
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException var17) {
                    LOG.debug("Could not close res file " + filename, var17);
                }
            }

        }

    }

    public boolean getBoolean(String section, String key, boolean def) {
        String param = this.getString(section, key);
        return param == null?def:this.convertToBool(param).booleanValue();
    }

    Boolean convertToBool(String str) {
        if(str.length() == 0) {
            return null;
        } else {
            char ch = str.charAt(0);
            return ch != 121 && ch != 89 && ch != 49 && ch != 116 && ch != 84?(ch != 110 && ch != 78 && ch != 48 && ch != 102 && ch != 70?null:Boolean.valueOf(false)):Boolean.valueOf(true);
        }
    }

    static String trimWs(String s) {
        if(s.length() == 0) {
            return s;
        } else {
            int start = 0;

            int end;
            for(end = s.length(); start < end && Character.isWhitespace(s.charAt(start)); ++start) {
                ;
            }

            while(end > 0 && Character.isWhitespace(s.charAt(end - 1))) {
                --end;
            }

            return end <= start?"":s.substring(start, end);
        }
    }

    public static String decrypt(String value) {
        if(value.length() > 2 && value.substring(0, 2).equals("@@")) {
            String key = "achievingMoreTogether";
            StringBuffer sb = new StringBuffer();
            String str = value.substring(2);
            byte r = (byte)(str.charAt(0) - 65 + (str.charAt(1) - 65) * 16);
            int i = 2;

            for(int j = 0; i < str.length(); j = (j + 1) % "achievingMoreTogether".length()) {
                byte c = (byte)(str.charAt(i) - 65 + (str.charAt(i + 1) - 65) * 16);
                c = (byte)(c ^ "achievingMoreTogether".charAt(j));
                c ^= r;
                sb.append((char)c);
                i += 2;
            }

            return sb.toString();
        } else {
            return value;
        }
    }

    public static void main(String[] args) {
        String dec = decrypt("@@LNJMJNGNBNBNNJ");
        System.out.println(dec);
        String dec2 = decrypt("@@" + dec);
        System.out.println(dec2);
    }
}

