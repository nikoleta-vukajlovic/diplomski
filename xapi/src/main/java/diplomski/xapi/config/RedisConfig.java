/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.dynamic.annotation.Value;
import java.lang.module.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dusan
 */
@Component
public class RedisConfig {

    private String url = "redis://dusan123@redis-17972.c15.us-east-1-2.ec2.cloud.redislabs.com:17972";

    @Bean
    public StatefulRedisConnection<String, String> createRedisConnection() {
        RedisClient redisClient = RedisClient.create(url);
        return redisClient.connect();
    }
}
