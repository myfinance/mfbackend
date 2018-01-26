/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SimpleAuthenticator.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 26.01.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.web;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class SimpleAuthenticator extends Authenticator
{
    private String username;
    private String password;

    public SimpleAuthenticator(String username,String password)
    {
        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(
            username,password.toCharArray());
    }
}