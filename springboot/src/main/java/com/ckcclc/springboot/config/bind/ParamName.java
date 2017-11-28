/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-9
 */

package com.ckcclc.springboot.config.bind;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamName {

    /**
     * The name of the request parameter to bind to.
     */
    String value();

}