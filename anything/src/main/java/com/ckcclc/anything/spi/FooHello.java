/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.anything.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FooHello implements Hello {

    private static final Logger logger = LoggerFactory.getLogger(FooHello.class);

    @Override
    public void sayHello() {
        logger.info("hello foo!");
    }
}
