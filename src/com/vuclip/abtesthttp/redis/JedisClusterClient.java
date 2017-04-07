package com.vuclip.abtesthttp.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

public class JedisClusterClient {
    private static final String SET_IF_NOT_EXIST = "NX", SET_IF_EXIST = "XX";
    private JedisCluster jedis;

    public JedisClusterClient(String hosts, String password) {
        try{
            String[] redisServers = hosts.split(",");
            Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
            for (int i=0; i<redisServers.length; i++){
                try{
                    String[] redisServer = redisServers[i].split(":"); 
                    jedisClusterNodes.add(new HostAndPort(redisServer[0], Integer.parseInt(redisServer[1])));
                }catch(Exception e){e.printStackTrace();}
            }
            jedis = new JedisCluster(jedisClusterNodes, new JedisPoolConfig(), isValidParameter(password)?password:null);
            
        }catch(Exception e){e.printStackTrace();}
    }

//    public Jedis jedis(){
//    	return jedis;
//    }

	static final public boolean isValidParameter(String p) {
		return (p != null && p.length() > 0 && !"null".equals(p));
	}
    
    public boolean exists(String key) {
    	try {
			if (jedis != null)
				return jedis.exists(key);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public String getString(String key) {
    	try {
			if (jedis != null)
				return jedis.get(key);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
    }
    
    public Object get(String key) {
    	try {
			if (jedis != null)
				return deserialize(jedis.get(key.getBytes()));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
    }
    
    public long incr(String key) {
    	try {
			if (jedis != null)
				return jedis.incr(key);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return 0;
    }

    public long decr(String key) {
    	try {
			if (jedis != null)
				return jedis.decr(key);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return 0;
    }

    public boolean storeCounter(String key, long l) {
        return set(key, String.valueOf(l));
    }

    public long getCounter(String key) {
        try {
        	String s = getString(key);
        	if (s != null)
        		return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
        return 0;
    }

    public boolean set(String key, String value) {
    	try {
			if (jedis != null)
				return "OK".equals(jedis.set(key, value));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public boolean set(String key, String value, Date expiration) {
    	try {
			if (jedis != null)
				return "OK".equals(jedis.setex(key, (int)(expiration.getTime()-System.currentTimeMillis())/1000, value));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public boolean set(String key, Object value) {
    	try {
			if (jedis != null)
				return "OK".equals(jedis.set(key.getBytes(), serialize(value)));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public boolean set(String key, Object value, Date expiration) {
    	try {
			if (jedis != null)
				return "OK".equals(jedis.setex(key.getBytes(), (int)(expiration.getTime()-System.currentTimeMillis())/1000, serialize(value)));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public boolean add(String key, String value) {
    	try {
			if (jedis != null)
				return 1==jedis.setnx(key, value);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }

    public boolean add(String key, String value, Date expiration) {
    	try {
			if (jedis != null) {
		        boolean b = 1==jedis.setnx(key, value);
		        if (b)
		        	jedis.expireAt(key, expiration.getTime()/1000);
		        return b;
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }

    public boolean add(String key, Object value) {
    	try {
			if (jedis != null)
				return 1==jedis.setnx(key.getBytes(), serialize(value));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public boolean add(String key, Object value, Date expiration) {
    	try {
			if (jedis != null) {
		        byte[] keyb = key.getBytes();
		        boolean b = 1==jedis.setnx(keyb, serialize(value));
		        if (b)
		        	jedis.expireAt(keyb, expiration.getTime()/1000);
		        return b;
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }

    public boolean replace(String key, String value){
    	try {
			if (jedis != null)
		        return jedis.exists(key)?
		        		"OK".equals(jedis.set(key, value)):
		                false;
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public boolean replace(String key, String value, Date expiration){
    	try {
			if (jedis != null)
		        return jedis.exists(key)?
		        		"OK".equals(jedis.setex(key, (int)(expiration.getTime()-System.currentTimeMillis())/1000, value)):
		                false;
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }

    public boolean replace(String key, Object value){
    	try {
			if (jedis != null)
		        return jedis.exists(key.getBytes())?
		        		"OK".equals(jedis.set(key.getBytes(), serialize(value))):
		                false;
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public boolean replace(String key, Object value, Date expiration){
    	try {
			if (jedis != null)
		        return jedis.exists(key.getBytes())?
		        		"OK".equals(jedis.setex(key.getBytes(), (int)(expiration.getTime()-System.currentTimeMillis())/1000, serialize(value))):
		                false;
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }

    public Object delete(String key) {
    	try {
			if (jedis != null)
				return jedis.del(key);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
    }

    public long sadd(String key, String value) {
    	try {
			if (jedis != null) {
		        return jedis.sadd(key, value);
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return 0;
    }

    public long hset(String key, String field, String value) {
    	try {
			if (jedis != null)
				return jedis.hset(key, field, value);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return -1;
    }
    
    public long hset(byte[] key, byte[] field, Object value) {
    	try {
			if (jedis != null)
				return jedis.hset(key, field, serialize(value));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return -1;
    }
    
    public long hsetnx(String key, String field, String value) {
    	try {
			if (jedis != null)
				return jedis.hsetnx(key, field, value);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return -1;
    }
    
    public String hget(String key, String field) {
    	try {
			if (jedis != null)
				return jedis.hget(key, field);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
    }
    
    public Object hget(byte[] key, byte[] field) {
    	try {
			if (jedis != null)
				return deserialize(jedis.hget(key, field));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
    }
    
    public boolean hmset(String key, Map<String, String> hash) {
    	try {
			if (jedis != null)
				return "OK".equals(jedis.hmset(key, hash));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public boolean hmset(byte[] key, Map<byte[], byte[]> hash) {
    	try {
			if (jedis != null)
				return "OK".equals(jedis.hmset(key, hash));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public List<String> hmget(String key, String... fields) {
    	try {
			if (jedis != null)
				return jedis.hmget(key, fields);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
    }
    
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
    	try {
			if (jedis != null)
				return jedis.hmget(key, fields);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
    }
    
    public long hincrBy(String key, String field, long value) {
    	try {
			if (jedis != null)
				return jedis.hincrBy(key, field, value);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return -1;
    }
    
    public boolean hexists(String key, String field) {
    	try {
			if (jedis != null)
				return jedis.hexists(key, field);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return false;
    }
    
    public long hdel(String key, String field) {
    	try {
			if (jedis != null)
				return jedis.hdel(key, field);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return -1;
    }
    
    public Object[] hmgetToArray(byte[] key, byte[]... fields) {
    	try {
			if (jedis != null){
				List<byte[]> b = jedis.hmget(key, fields);
				if (b!=null && b.size()>0){
					return deserialize(b.toArray());
				}
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
    }

    public Map<String,String> hgetAll(String key) {
    	try {
			if (jedis != null)
				return jedis.hgetAll(key);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
    }
	public Long zcard(String key) {
    	try {
			if (jedis != null)
				return jedis.zcard(key);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}

	public Double zscore(String key, String member) {
    	try {
			if (jedis != null)
				return jedis.zscore(key, member);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}
	
	public Long zadd(String key, double score, String member) {
    	try {
			if (jedis != null)
				return jedis.zadd(key, score, member);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}
	
	public Long zadd(byte[] key, double score, Object member) {
    	try {
			if (jedis != null)
				return jedis.zadd(key, score, serialize(member));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}
	
	public Long zadd(String key, Map<String,Double> scoreMembers) {
    	try {
			if (jedis != null)
				return jedis.zadd(key, scoreMembers);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}
	
	public Long zadd(byte[] key, Map<byte[],Double> scoreMembers) {
    	try {
			if (jedis != null)
				return null;//jedis.zadd(key, serialize(member));
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}
	
	public Set<byte[]> zrange(byte[] key, long start, final long end) {
    	try {
			if (jedis != null)
				return jedis.zrange(key, start, end);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}
	
	public String[] zrangeToArray(String key, long start, final long end) {
    	try {
			if (jedis != null){
				Set<String> set = jedis.zrange(key, start, end);
		        if (set!=null && set.size()>0){
		        	return set.toArray(new String[set.size()]);
		        }
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}

	public Object[] zrangeToArray(byte[] key, long start, final long end) {
    	try {
			if (jedis != null){
				Set<byte[]> set = jedis.zrange(key, start, end);
		        if (set!=null && set.size()>0){
		        	Object[] b = set.toArray(); //object itself is a bytearray
		        	return deserialize(b);
		        }
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}

	public <T> int zrangeToArray(byte[] key, long start, final long end, T[] array, int offset) {
    	try {
			if (jedis != null){
				Set<byte[]> set = jedis.zrange(key, start, end);
		        if (set!=null){
		        	Object[] b = set.toArray(); //object itself is a bytearray
		        	for (int i=0; i<b.length; i++){
		        		array[offset+i] = (T) deserialize((byte[])b[i]);
		        	}
					return b.length;
		        }
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return 0;
	}

	public List<String> blpop(int timeout, String key) {
    	try {
			if (jedis != null)
				return jedis.blpop(timeout, key);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}

	public Long lpush(String key, String... strings) {
    	try {
			if (jedis != null)
				return jedis.lpush(key, strings);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}

	public String[] lrangeToArray(String key, long start, final long end) {
    	try {
			if (jedis != null){
				List<String> list = jedis.lrange(key, start, end);
		        if (list!=null && list.size()>0){
		        	return list.toArray(new String[list.size()]);
		        }
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}

	public Object[] lrangeToArray(byte[] key, long start, final long end) {
    	try {
			if (jedis != null){
				List<byte[]> list = jedis.lrange(key, start, end);
		        if (list!=null && list.size()>0){
		        	Object[] b = list.toArray(); //object itself is a bytearray
		        	return deserialize(b);
		        }
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return null;
	}

	public <T> int lrangeToArray(byte[] key, long start, final long end, T[] array, int offset) {
    	try {
			if (jedis != null){
				List<byte[]> list = jedis.lrange(key, start, end);
		        if (list!=null){
		        	Object[] b = list.toArray(); //object itself is a bytearray
		        	for (int i=0; i<b.length; i++){
		        		array[offset+i] = (T) deserialize((byte[])b[i]);
		        	}
					return b.length;
		        }
			}
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
		}
    	return 0;
	}

	public Long setnx(String key, String value) {
    	try {
			if (jedis != null)
				return jedis.setnx(key, value);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
			}
    	return null;
	}

	public Long expire(String key, int seconds) {
    	try {
			if (jedis != null)
				return jedis.expire(key, seconds);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
			}
    	return null;
	}

	public Long ttl(String key) {
    	try {
			if (jedis != null)
				return jedis.ttl(key);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
			}
    	return null;
	}

	public Object eval(String script, int keyCount, String... params) {
    	try {
			if (jedis != null)
				return jedis.eval(script, keyCount, params);
    	} catch (JedisException e) {
    		e.printStackTrace();
		} finally {
			}
    	return null;
	}

	public byte[] serialize(Object o){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(b);
            oos.writeObject(o);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b.toByteArray();
    }
    public Object deserialize(byte[] b){
        if (b!=null){
            try {
                return new ObjectInputStream(new ByteArrayInputStream(b)).readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	private Object[] deserialize(Object[] b) {
		Object o = deserialize((byte[])b[0]);
		Object[] o2 = (Object[]) Array.newInstance(o.getClass(), b.length);
		o2[0] = o;
		for (int i=1; i<b.length; i++){
			o2[i] = deserialize((byte[])b[i]);
		}
		return o2;
	}

}
