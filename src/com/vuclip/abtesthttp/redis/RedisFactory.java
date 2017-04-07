package com.vuclip.abtesthttp.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class RedisFactory {
	
	private static String redisIp;
	private static int redisPort;
	private static int maxActive;
	private static int maxIdle;
	private static long maxWait;
	private static boolean testOnBorrow;
	private static final String REDIS_IP = "redis.ip";
	private static final String REDIS_PORT = "redis.port";
	private static final String MAX_ACTIVE = "jedis.pool.maxActive";
	private static final String MAX_IDLE = "jedis.pool.maxIdle";
	private static final String MAX_WAIT = "jedis.pool.maxWait";
	private static final String TEST_ON_BORROW = "jedis.pool.testOnBorrow";
	private static final String FILE_PATH="/redisConfig.properties";
	private static JedisPool jedisPool = null;
	
	public static Boolean init() {
		Properties properties = new Properties();
		InputStream inputStream = RedisFactory.class.getResourceAsStream(FILE_PATH);
		if(null != inputStream) {
			try {
				properties.load(inputStream);
				redisIp = properties.getProperty(REDIS_IP);
				redisPort = Integer.parseInt(properties.getProperty(REDIS_PORT));
				maxActive = Integer.parseInt(properties.getProperty(MAX_ACTIVE));
				maxIdle = Integer.parseInt(properties.getProperty(MAX_IDLE));
				maxWait = Long.parseLong(properties.getProperty(MAX_WAIT));
				testOnBorrow = Boolean.parseBoolean(properties.getProperty(TEST_ON_BORROW));
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
	
	static {
		if(init()) {
			try {
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxIdle(maxIdle);
				config.setMaxWaitMillis(maxWait);
				config.setMaxTotal(maxActive);
				config.setTestOnBorrow(testOnBorrow);
				jedisPool = new JedisPool(config, redisIp, redisPort);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized static Jedis getJedis() {
		int count=0;
		while(count<3){
			try {
				if (null != jedisPool) {
					Jedis resource = jedisPool.getResource();
					return resource;
				}
			} catch (Exception e) {
				e.printStackTrace();
				count++;
			}
		}
		return null;
	}

}
