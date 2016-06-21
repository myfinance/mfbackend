/** ----------------------------------------------------------------------------
 *
 * ---                                 ---
 *              Copyright (c) 2015, ... All Rights Reserved
 *
 *
 *  Project     : finance-secure-service
 *
 *  File        : AuthFilter.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 14.08.2015
 *
 * ----------------------------------------------------------------------------
 */
package de.hf.finance.secureservice.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpStatus;
import org.jboss.security.SecurityContextAssociation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements javax.servlet.Filter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);
    private static final Logger AUDIT_LOG = LoggerFactory.getLogger("audit");
    public static final String SECURITYDOMAIN = "MYDOMAIN";

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean basic_auth = false;

        HttpServletRequest request = (HttpServletRequest) req;

        String auth = request.getHeader("Authorization");

        if (auth != null && !auth.isEmpty() && auth.toUpperCase().startsWith(HttpServletRequest.BASIC_AUTH)) {

            basic_auth = true;

            final String[] values = decode(auth);

            if(values == null || values.length != 2) {
                basic_auth = false;
            } else {
                String user = values[0];
                String pwd = values[1];

                //transfer the login information from Servlet to ejb layer
                //Login-Module für JBOSS muss noch gebaut werden das dann den SimplePrincipal verarbeiten kann
                //SecurityContextAssociation.setPrincipal(new SimplePrincipal(user));
                SecurityContextAssociation.setCredential(pwd.toCharArray());
            }
        }

        if(basic_auth) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse re = (HttpServletResponse) response;

            re.addHeader("WWW-Authenticate","Basic realm=\"" + SECURITYDOMAIN +  "\"");

            re.setStatus(HttpStatus.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    private static String[] decode(String auth) {
        //Replacing "Basic THE_BASE_64" to "THE_BASE_64" directly
        auth = auth.replaceFirst("[B|b]asic ", "");

        //Decode the Base64 into byte[]
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(auth);

        //If the decode fails in any case
        if(decodedBytes == null || decodedBytes.length == 0){
            return null;
        }

        //Now we can convert the byte[] into a splitted array :
        //  - the first one is login,
        //  - the second one password
        return new String(decodedBytes).split(":", 2);
    }

}
