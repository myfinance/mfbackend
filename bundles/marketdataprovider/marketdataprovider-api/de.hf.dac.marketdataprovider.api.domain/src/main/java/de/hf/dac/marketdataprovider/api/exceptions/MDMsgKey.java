/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDMsgKey.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 05.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.exceptions;

import de.hf.dac.api.base.exceptions.MsgKey;

/** Message key to be part of CCRException providing a unique id and a unique type (message type) */
public enum MDMsgKey implements MsgKey {
    // 10000 - 14999: system errors
    SYSTEM(10001),
    NULL_POINTER(10003),
    ILLEGAL_ARGUMENTS(10004),
    INPUT_OUTPUT(10005),
    FILE_NOT_FOUND(10100),
    INTERRUPTED_EXCEPTION(10101),
    EXECUTION_EXCEPTION(10102),
    // 15000 - 19999: hibernate, database, sybase
    NO_TARGET_CONFIG_EXCEPTION(15001),
    // 20000 - 20999: memory, caching, couchbase
    // 21000 - 24999: imports
    // 25000 - 29999: analysis
    MD_UNSPECIFIED(30000);

    private final String globalPrefix = "MD";

    private final int id;

    MDMsgKey(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getMsgPrefix() {
        if (id != 1) {
            return globalPrefix + "-" + getId() + ": ";
        }
        return globalPrefix + "-" + getId() + ": System error - ";
    }

    public String getName() {
        return toString();
    }
}
