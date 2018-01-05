/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.anything.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BarHello implements Hello {

    private static final Logger logger = LoggerFactory.getLogger(BarHello.class);

    @Override
    public void sayHello() {
        logger.info("hello bar!");
    }
}
