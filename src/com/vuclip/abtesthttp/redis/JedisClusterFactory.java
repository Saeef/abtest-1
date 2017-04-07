package com.vuclip.abtesthttp.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.blueapple.tools.TripleDesVuclip;

public class JedisClusterFactory {

	private static final Logger vulogger = Logger.getLogger(JedisClusterFactory.class);

	private static final String FILE_PATH="/redisConfig.properties";

	private static final ConcurrentHashMap<String, JedisClusterClient> map = new ConcurrentHashMap<String, JedisClusterClient>();
	private static Object lock = new Object();
	private static String redisHost = "";
	private static String encypt = "";
	public static Boolean init(String name) {
		Properties properties = new Properties();
		InputStream inputStream = RedisFactory.class.getResourceAsStream(FILE_PATH);
		if(null != inputStream) {
			try {
				properties.load(inputStream);
				redisHost = properties.getProperty("redis."+name+".host");
				encypt = properties.getProperty("redis." + name + ".password");
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

	public static JedisClusterClient get(String name){
		if ((name=StringUtils.trimToNull(name))==null)
			return null;
		if(init(name)){
			JedisClusterClient JedisClusterClient = map.get(name);
			if (JedisClusterClient==null){
				synchronized (lock){
					JedisClusterClient = map.get(name);
					if (JedisClusterClient==null){
						try {
							if ((encypt=StringUtils.trimToNull(encypt))!=null){
								String redisPassword = TripleDesVuclip.decrypt(encypt);
								if ((redisHost=StringUtils.trimToNull(redisHost))!=null && (redisPassword=StringUtils.trimToNull(redisPassword))!=null){
									JedisClusterClient = new JedisClusterClient(redisHost, redisPassword);
									map.put(name, JedisClusterClient);
									vulogger.info("redis "+name+" created");
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			return JedisClusterClient;
		}
		return null;
	}
}
