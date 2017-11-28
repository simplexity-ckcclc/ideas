/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-9
 */

package com.ckcclc.springboot.config.binding;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResolverConfiguration {

    @Bean
    public AnnotationHandlerMappingPostProcessor processor() {
        return new AnnotationHandlerMappingPostProcessor();
    }
}
