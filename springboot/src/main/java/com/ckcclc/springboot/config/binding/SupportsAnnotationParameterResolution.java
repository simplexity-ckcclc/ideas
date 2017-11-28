/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-9
 */

package com.ckcclc.springboot.config.binding;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SupportsAnnotationParameterResolution {

}