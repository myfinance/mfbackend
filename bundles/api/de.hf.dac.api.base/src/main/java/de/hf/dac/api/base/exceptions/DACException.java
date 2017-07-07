/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DACException.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.base.exceptions;

public class DACException extends MsgKeyException {
    /**  **/
    private static final long serialVersionUID = 1L;

    public DACException(MsgKey msgKey, String message, Throwable e) {
        super(msgKey, message, e);
    }

    public DACException(MsgKey msgKey, String message) {
        super(msgKey, message);
    }

    /**
     * Constructs exception from message.
     *
     * @param message
     *        The exception message
     */
    public DACException(String message) {
        super(DACMsgKey.UNSPECIFIED, message);
    }

    public DACException(Throwable e) {
        super(DACMsgKey.SYSTEM, e);

        if (e instanceof DACException) {
            msgKey = ((DACException) e).getMsgKey();
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
    public DACException(String message, Throwable e) {
        super(DACMsgKey.SYSTEM, message, e);

        if (e instanceof DACException) {
            msgKey = ((DACException) e).getMsgKey();
        }
    }

    public static DACException getFromThrowable(Throwable e) {
        if (e instanceof DACException) {
            return (DACException) e;
        } else {
            return new DACException(DACMsgKey.UNSPECIFIED, "Unexpected error occurred.", e);
        }

    }
}

