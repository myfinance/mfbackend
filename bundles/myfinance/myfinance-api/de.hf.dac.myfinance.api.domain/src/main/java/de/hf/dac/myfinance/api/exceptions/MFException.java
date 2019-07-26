/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MFException.java
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
public class MFException extends MsgKeyException {
    /**  **/
    private static final long serialVersionUID = 1L;

    /**
     * Transform a Throwable into a CCRException as far as needed
     * @param e the Throwable
     * @return instance of CCRException
     */
    public static MFException getFromThrowable(Throwable e) {
        if (e instanceof MFException) {
            return (MFException) e;
        }

        MFException r;

        // perform mappings for classification as needed...
        if (e instanceof NullPointerException) {
            r = new MFException(MFMsgKey.NULL_POINTER, "Access to undefined reference occurred (NullPointerException)", e);
        } else if (e instanceof IllegalArgumentException) {
            r = new MFException(MFMsgKey.ILLEGAL_ARGUMENTS, "Illegal arguments - " + e.getMessage(), e);
        } else if (e instanceof FileNotFoundException) {
            r = new MFException(MFMsgKey.FILE_NOT_FOUND, "File not found - " + e.getMessage(), e);
        } else {
            r = new MFException
                (e);
        }

        return r;
    }

    public MFException(MFMsgKey msgKey, String message, Throwable e) {
        super(msgKey, message, e);
    }

    public MFException(MFMsgKey msgKey, String message) {
        super(msgKey, message);
    }

    /**
     * Constructs exception from message.
     *
     * @param message
     *        The exception message
     */
    public MFException(String message) {
        super(MFMsgKey.MD_UNSPECIFIED, message);
    }

    public MFException(Throwable e) {
        super(MFMsgKey.SYSTEM, e);

        if (e instanceof MFException) {
            msgKey = ((MFException) e).getMsgKey();
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
    public MFException(String message, Throwable e) {
        super(MFMsgKey.SYSTEM, message, e);

        if (e instanceof MFException) {
            msgKey = ((MFException) e).getMsgKey();
        }
    }

}

