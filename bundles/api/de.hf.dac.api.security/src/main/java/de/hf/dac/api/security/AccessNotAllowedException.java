/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : AccessNotAllowedException.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.security;

/**
 * Created by xne0133 on 30.07.2015.
 */
public class AccessNotAllowedException extends SecurityException
{
    public AccessNotAllowedException(String message) {
        super(message);
    }
    public AccessNotAllowedException(String message, Throwable ex) {
        super(message, ex);
    }
}