/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-10-30
 */

package com.ckcclc.springboot.config;

import com.ckcclc.springboot.service.RetryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
public class RetryConfig {

    @Bean
    public RetryService getRetryService() {
        return new RetryService();
    }

}
