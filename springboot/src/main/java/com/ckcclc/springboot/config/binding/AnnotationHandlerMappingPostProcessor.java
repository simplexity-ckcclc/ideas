/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-9
 */

package com.ckcclc.springboot.config.binding;

import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

public class AnnotationHandlerMappingPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String arg1)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String arg1)
            throws BeansException {
        if (bean instanceof RequestMappingHandlerAdapter) {
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            List<HandlerMethodArgumentResolver> resolvers = adapter.getCustomArgumentResolvers();
            if (resolvers == null) {
                resolvers = Lists.newArrayList();
            }
            resolvers.add(new AnnotationServletModelAttributeResolver(false));
            adapter.setCustomArgumentResolvers(resolvers);
        }

        return bean;
    }

}