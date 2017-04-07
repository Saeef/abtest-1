package com.vuclip.abtesthttp.service;

import com.vuclip.abtesthttp.redis.JedisClusterClient;
import com.vuclip.abtesthttp.redis.JedisClusterFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2016/3/1
 * Time: 15:52
 * To change this template use File | Settings | File Templates.
 */
@Service
public class LogViewService {
    private static JedisClusterClient jedisClusterClient = JedisClusterFactory.get("subtitle");
    public String[] getLogs(Map<String,Object> params){
        return jedisClusterClient.lrangeToArray(params.get("key").toString(),Long.valueOf(params.get("start").toString()), Long.valueOf(params.get("end").toString()));
    }
    public long getCount(String key){
        return jedisClusterClient.getCounter(key);
    }

    public void deleteLogs() {
        jedisClusterClient.delete("pageviewlogredis");
        jedisClusterClient.delete("robootpageviewlogredis");
    }
}
