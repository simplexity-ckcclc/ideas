/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-10-25
 */

package com.ckcclc.springboot.config;

import com.sensetime.engineering.sebw.fs.fastdfs.FastDfsFileSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    JedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, String> getRedisTemplate(RedisConnectionFactory factory) {
        logger.info("Init redis template to template<String,String>.");
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }

}
