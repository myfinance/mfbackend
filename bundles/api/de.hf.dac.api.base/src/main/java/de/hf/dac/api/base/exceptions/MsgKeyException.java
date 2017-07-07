/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MsgKeyException.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 05.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.base.exceptions;

/**
 * Base exception with message key.
 */
public class MsgKeyException extends RuntimeException {
    /**  **/
    private static final long serialVersionUID = 1L;

    /** the message key associated with the exception */
    protected MsgKey msgKey;

    /**
     * Create instance using message key and message string.
     * @param msgKey the message key
     * @param message the message describing a problem
     */
    public MsgKeyException(MsgKey msgKey, String message) {
        super(message);
        this.msgKey = msgKey;
    }

    /**
     * Create instance using message key, message string and throwable.
     * @param msgKey the message key
     * @param message the message describing a problem
     * @param e the throwable to create the exception from
     */
    public MsgKeyException(MsgKey msgKey, String message, Throwable e) {
        super(message, e);
        this.msgKey = msgKey;
    }

    /**
     * Create instance using message key and throwable.
     * @param msgKey the message key
     * @param e the throwable to create the exception from
     */
    public MsgKeyException(MsgKey msgKey, Throwable e) {
        super(e);
        this.msgKey = msgKey;
    }

    public static MsgKeyException getFromThrowable(Throwable e) {
        if (e instanceof MsgKeyException) {
            return (MsgKeyException) e;
        } else {
            return new MsgKeyException(DacMsgKey.UNSPECIFIED, e);
        }

    }

    /**
     * Get the message key.
     *
     * @return the message key
     */
    public MsgKey getMsgKey() {
        return msgKey;
    }

    /**
     * Get the id of the message key.
     *
     * @return the message key's id
     */
    public int getMsgKeyId() {
        return msgKey.getId();
    }

    /**
     * Get the message prefixed with the message key's message prefix.
     *
     * @return the prefixed message
     */
    public String getPrefixedMsg() {
        return msgKey.getMsgPrefix() + super.getMessage();
    }
}

