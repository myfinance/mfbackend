/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MsgKey.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 05.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.base.exceptions;


/**
 * Interface defining the methods for message keys used for exception handling.
 *
 */
public interface MsgKey {

    /**
     * Every message key has a uniquely defined id.
     *
     * @return the id
     */
    int getId();

    /**
     * Get the message prefix associated with the message key.
     * Typically a message key would look like "XXX-id: " where XXX is an abbreviation for a framework or application
     * for which a set of message keys is defined and id is just the id of the message key.
     *
     * @return the message prefix
     */
    String getMsgPrefix();
}
