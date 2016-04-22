package de.hf.marketdataprovider.angular;
/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : IndexController.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 22.04.2016
 *
 * ----------------------------------------------------------------------------
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    String index(){
        return "index";
    }


}
