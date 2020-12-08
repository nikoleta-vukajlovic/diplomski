/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import diplomski.xapi.states.App;
import diplomski.xapi.states.HomeMenu;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Value;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dusan
 */
@Service
public class RedisService {

    @Autowired
    StatefulRedisConnection redisConnection;

    @Value("${ussd.redisUserSessionTtl:30}")
    private Long redisUserSessionTtl;

    private static final String PREFIX_FOR_REDIS = "AFRICA_TALKING:USERS:";

    public void saveState(String key, App state) throws JsonProcessingException {
        RedisAsyncCommands<String, String> async = redisConnection.async();
        async.multi();
        Map<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        String stateJson = mapper.writeValueAsString(state);
        map.put("state", stateJson);
        Date date = new Date(System.currentTimeMillis());
        map.put("date", date.toString());
        async.hset(key, map);
        async.expire(key, TimeUnit.MINUTES.toSeconds(redisUserSessionTtl));
        RedisFuture<TransactionResult> execResult = async.exec();
    }

    public App buildStateFromRedisEntry(String phoneNumber) throws InterruptedException, JsonProcessingException {
        try {
            RedisAsyncCommands<String, String> async = redisConnection.async();
            String key = PREFIX_FOR_REDIS + phoneNumber;
            async.multi();
            RedisFuture<Map<String, String>> hgetall = async.hgetall(key);
            async.exec();
            Map<String, String> data = hgetall.get();
            String currentState = data.get("state");//uzimamo stanje aplikacije iz baze kao string
            if (currentState == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.enableDefaultTyping();
            App newMainState = mapper.readValue(currentState, App.class);
            return newMainState;
        } catch (ExecutionException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void deleteUser(String key) {
        RedisAsyncCommands<String, String> async = redisConnection.async();
        async.multi();
        async.del(key);
        async.exec();
    }

    public App loginUser(String phoneNumber) throws JsonProcessingException {
        try {
            RedisAsyncCommands<String, String> async = redisConnection.async();
            String key = PREFIX_FOR_REDIS + phoneNumber;
            App createdMainstate = new App();
            createdMainstate.setCurrentState(new HomeMenu());
            createdMainstate.setUserPhoneNumber(phoneNumber);
            saveState(key, createdMainstate);
            return createdMainstate;
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    // NE KORISTI SE
    public void saveInRedis() throws MalformedURLException, Exception {
        // RedisClient redisClient = RedisClient.create("redis://dusan123@redis-17972.c15.us-east-1-2.ec2.cloud.redislabs.com:17972");
        //RedisAsyncCommands<String, String> async = redisClient.connect().async();
        RedisAsyncCommands async = redisConnection.async();
        async.multi();
        Map<String, String> map = new HashMap<String, String>();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enableDefaultTyping();
        String key = "test2";
        map.put("state", "testState2");
        async.hset(key, map);
        async.expire(key, TimeUnit.MINUTES.toSeconds(30));
        RedisFuture<TransactionResult> execResult = async.exec();
    }

}
