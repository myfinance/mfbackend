/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : LeafResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 31.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.services.resources.leaf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public abstract class LeafResource {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final SimpleDateFormat DATEIME_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    private final static JsonSerializer<LocalDate> ser = (localDate, type, jsonSerializationContext) -> localDate == null ? null : new JsonPrimitive(DATEIME_FORMATTER.format(localDate));

    private final static Gson gson = new GsonBuilder()
        .setDateFormat(DATETIME_FORMAT)
        .registerTypeAdapter(LocalDate.class,ser)
        .create();

    public static String SerializeToJSON(Object o) {
        String ret = null;

        String jsonString = gson.toJson(o);
        byte[] utf8JsonString = new byte[0];
        try {
            utf8JsonString = jsonString.getBytes("UTF8");
            ret = new String(utf8JsonString);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    public static String getUrl(@Context UriInfo uriInfo) {
        URI uri = uriInfo == null ? null
            : uriInfo.getRequestUri();

        return uri == null ? ""
            : uri.toString();
    }
}

