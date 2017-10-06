/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : TestExecutionException.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution;

public class TestExecutionException extends RuntimeException {
    public TestExecutionException(String s) {
        super(s);
    }

    public TestExecutionException(String s, Throwable e) {
        super(s, e);
    }

    public TestExecutionException(Exception e) {
        super(e);
    }
}
