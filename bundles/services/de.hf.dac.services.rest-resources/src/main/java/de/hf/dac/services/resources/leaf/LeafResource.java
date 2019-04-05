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

import com.google.gson.*;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.http.HttpStatus;

import de.hf.dac.api.base.json.Exclude;
import de.hf.dac.api.rest.model.ModelBase;

public abstract class LeafResource {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    private final static JsonSerializer<LocalDate> serDate = (localDate, type, jsonSerializationContext) -> localDate == null ? null : new JsonPrimitive(localDate.format(DATE_FORMATTER));
    private final static JsonSerializer<LocalDateTime> serDateTime = (localDateTime, type, jsonSerializationContext) -> localDateTime == null ? null : new JsonPrimitive(localDateTime.format(DATETIME_FORMATTER));

    protected ModelBase data;

    private final static ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(Exclude.class) != null;
        }
    };

    private final static Gson gson = new GsonBuilder()
        .setDateFormat(DATETIME_FORMAT)
        .registerTypeAdapter(LocalDate.class,serDate)
        .registerTypeAdapter(LocalDateTime.class,serDateTime)
        .addSerializationExclusionStrategy(strategy)
        .create();

    public static String SerializeToJSON(Object o) {
        String ret;

        String jsonString = gson.toJson(o);
        byte[] utf8JsonString;
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

    public Response getData(@Context UriInfo uriInfo) {
        try {
            URI uri = uriInfo == null ? null
                : uriInfo.getRequestUri();

            String urs = uri == null ? ""
                : uri.toString();

            data.setUrl(urs);

            return uriInfo == null ? Response.ok(SerializeToJSON(data)).build()
                : Response.ok(SerializeToJSON(data)).location(uriInfo.getRequestUri()).build();
        } catch(Exception ex) {
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                           .entity(ex.getMessage())
                           .type(MediaType.APPLICATION_JSON)
                           .build();
        }

    }
}

