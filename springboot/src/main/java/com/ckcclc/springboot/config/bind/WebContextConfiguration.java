/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-9
 */

package com.ckcclc.springboot.config.bind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

//@Configuration
public class WebContextConfiguration extends WebMvcConfigurationSupport {

    private static final Logger logger = LoggerFactory.getLogger(WebContextConfiguration.class);

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(renamingProcessor());
        logger.debug("add argument resolver: renaming processor");
    }

    public RenamingProcessor renamingProcessor() {
        return new RenamingProcessor(true);
    }


    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof RequestMappingHandlerAdapter) {
                    RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
                    List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>(adapter.getArgumentResolvers());
                    argumentResolvers.add(0, new RenamingProcessor(true));
                    adapter.setArgumentResolvers(argumentResolvers);
                }

                return bean;
            }
        };
    }


}
