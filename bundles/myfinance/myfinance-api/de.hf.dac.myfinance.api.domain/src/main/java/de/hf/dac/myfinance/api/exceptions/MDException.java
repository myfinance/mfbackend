/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDException.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 05.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.exceptions;

import de.hf.dac.api.base.exceptions.MsgKeyException;

import java.io.FileNotFoundException;

/** Main exception for MD, it is a runtime exception to avoid having throw statement in every method. */
public class MDException extends MsgKeyException {
    /**  **/
    private static final long serialVersionUID = 1L;

    /**
     * Transform a Throwable into a CCRException as far as needed
     * @param e the Throwable
     * @return instance of CCRException
     */
    public static MDException getFromThrowable(Throwable e) {
        if (e instanceof MDException) {
            return (MDException) e;
        }

        MDException r;

        // perform mappings for classification as needed...
        if (e instanceof NullPointerException) {
            r = new MDException(MDMsgKey.NULL_POINTER, "Access to undefined reference occurred (NullPointerException)", e);
        } else if (e instanceof IllegalArgumentException) {
            r = new MDException(MDMsgKey.ILLEGAL_ARGUMENTS, "Illegal arguments - " + e.getMessage(), e);
        } else if (e instanceof FileNotFoundException) {
            r = new MDException(MDMsgKey.FILE_NOT_FOUND, "File not found - " + e.getMessage(), e);
        } else {
            r = new MDException
                (e);
        }

        return r;
    }

    public MDException(MDMsgKey msgKey, String message, Throwable e) {
        super(msgKey, message, e);
    }

    public MDException(MDMsgKey msgKey, String message) {
        super(msgKey, message);
    }

    /**
     * Constructs exception from message.
     *
     * @param message
     *        The exception message
     */
    public MDException(String message) {
        super(MDMsgKey.MD_UNSPECIFIED, message);
    }

    public MDException(Throwable e) {
        super(MDMsgKey.SYSTEM, e);

        if (e instanceof MDException) {
            msgKey = ((MDException) e).getMsgKey();
        }
    }

    /**
     * Constructs exception from exception and message.
     *
     * @param message
     *        The exception
     * @param e
     *        The exception
     */
    public MDException(String message, Throwable e) {
        super(MDMsgKey.SYSTEM, message, e);

        if (e instanceof MDException) {
            msgKey = ((MDException) e).getMsgKey();
        }
    }

}

