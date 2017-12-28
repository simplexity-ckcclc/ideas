/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-27
 */

package com.ckcclc.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class JspController {

    private static final Logger logger = LoggerFactory.getLogger(JspController.class);

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        logger.info("hello invoke!");
        return "helloworld";
    }
}
