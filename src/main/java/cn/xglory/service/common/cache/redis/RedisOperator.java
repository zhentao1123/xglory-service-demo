package cn.xglory.service.common.cache.redis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis工具类，封装常用操作
 * @author Bob
 *
 */
//@Component
public class RedisOperator implements InitializingBean, DisposableBean{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private JedisPool jedisPool;
	
	public RedisOperator(
			//final GenericObjectPoolConfig poolConfig, 
			final String host, int port,
		    //int timeout, 
		    final String password, final int database)
	{
		jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 5000, password, database);
	}
	
	/**
	 * 写入（单独调用，内部提供并管理Jedis）
	 * @param key
	 * @param value
	 * @param seconds
	 * @throws Exception 
	 */
	public void set(final String key, final String value, final Integer seconds) throws Exception{
		execute(getJedis(), new JedisRunner(){
			@Override
			public Object run(Jedis jedis) {
				jedis.set(key, value);
				if(null!=seconds){
					jedis.expire(key, seconds);
				}
				return null;
			}
		});
	}
	
	/**
	 * 读取（单独调用，内部提供并管理Jedis）
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public String get(final String key) throws Exception{
		return (String)execute(getJedis(), new JedisRunner(){
			@Override
			public Object run(Jedis jedis) {
				return getJedis().get(key);
			}
		});
	}
	
	/**
	 * 是否存在（单独调用，内部提供并管理Jedis）
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public boolean exists(final String key) throws Exception{
		return (Boolean)execute(getJedis(), new JedisRunner(){
			@Override
			public Object run(Jedis jedis) {
				return getJedis().exists(key);
			}
		});
	}
	
	/**
	 * 删除（单独调用，内部提供并管理Jedis）
	 * @param key
	 * @throws Exception 
	 */
	public void del(final String... key) throws Exception{
		execute(getJedis(), new JedisRunner(){
			@Override
			public Object run(Jedis jedis) {
				jedis.del(key);
				return null;
			}
		});
	}
	
	//---------------------------------------------------------------------
	
	/**
	 * 写入（组合调用，调用方提供并管理Jedis）
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public void set(Jedis jedis, final String key, final String value, final Integer seconds){
		jedis.set(key, value);
		if(null!=seconds){
			jedis.expire(key, seconds);
		}
	}
	
	/**
	 * 读取（组合调用，调用方提供并管理Jedis）
	 * @param key
	 * @return
	 */
	public String get(Jedis jedis, final String key){
		return jedis.get(key);
	}
	
	/**
	 * 是否存在（组合调用，调用方提供并管理Jedis）
	 * @param key
	 * @return
	 */
	public boolean exists(Jedis jedis, final String key){
		return jedis.exists(key);
	}
	
	/**
	 * 删除（组合调用，调用方提供并管理Jedis）
	 * @param key
	 */
	public void del(Jedis jedis, final String... key){
		jedis.del(key);
	}
	
	//---------------------------------------------------------------------
	
	/**
	 * 通用Jedis操作执行方法
	 * 封装Jedis操作及生命周期管理（关闭）
	 * @param jedis
	 * @param jedisRunner
	 * @return
	 * @throws Exception 
	 */
	public Object execute(Jedis jedis, JedisRunner jedisRunner) throws Exception{
		try {
			return jedisRunner.run(jedis);
		} finally {
			if (jedis != null) {
		  		jedis.close();
			}
		}
	}
	
	/**
	 * 通用Jedis操作执行方法
	 * 封装Jedis操作及生命周期管理（关闭）
	 * @param jedisRunner
	 * @return
	 * @throws Exception
	 */
	public Object execute(JedisRunner jedisRunner) throws Exception{
		return execute(getJedis(), jedisRunner);
	}
	
	/**
	 * 利用Jedis对象执行操作的接口
	 * @author Bob
	 *
	 */
	public static interface JedisRunner{
		Object run(Jedis jedis) throws Exception;;
	}
	
	public Jedis getJedis(){
		return getPool().getResource();
	}
	
	private JedisPool getPool(){
		return this.jedisPool;
	}
	
	//------------------------------------------------------------------------------------
	
	private String redisHost;
	
	public String getRedisHost() {
		return redisHost;
	}

	//@Value("${redis.host}")
	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public RedisOperator() {
		logger.info("执行RedisHelper: construct"); 
    }  
      
    @PostConstruct  
    public void postConstruct() {    
    	logger.info("执行RedisHelper: postConstruct");
    	//jedisPool = new JedisPool(new JedisPoolConfig(), getRedisHost());
    	//jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 10000, "123456", 0);
    }    
      
    @Override  
    public void afterPropertiesSet() throws Exception {  
    	logger.info("执行RedisHelper: afterPropertiesSet");   
    }  
      
    public void initMethod() {  
    	logger.info("执行RedisHelper: init-method");  
    }  
  
    @PreDestroy  
    public void preDestroy(){
    	if(null!=jedisPool){
    		jedisPool.destroy();
    	}
    	logger.info("执行RedisHelper: preDestroy");  
    }  
      
    @Override  
    public void destroy() throws Exception {  
    	logger.info("执行RedisHelper: destroy");  
    }  
      
    public void destroyMethod() {  
    	logger.info("执行RedisHelper: destroy-method");  
    }  
}
