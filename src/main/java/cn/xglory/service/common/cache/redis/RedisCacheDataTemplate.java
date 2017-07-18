package cn.xglory.service.common.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import cn.xglory.service.common.cache.BaseCacheDataTemplate;
import cn.xglory.service.common.cache.redis.RedisOperator.JedisRunner;

public abstract class RedisCacheDataTemplate<T> extends BaseCacheDataTemplate<T>{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public abstract RedisOperator getRedisOperator();
	
	@Override
	public void destroy() throws Exception {}

	/**
	 * 按缓存测落取数据
	 * 该方法只允许取源数据方法cacheData()的异常抛出；
	 * 其余情况抛出都应抓获，并直接调用cacheData()返回数据，保证缓存错误不影响数据返回
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getData(final Class<T> clazz, final CacheTimeOutTask cacheTimeOutTask) throws Exception {
		T data;
		
		if(!initData() || null!=getRedisOperator()){
			data = cacheData();
		}
		else{
			try{
				//数组包装Exception对象，为了能从内部传出，并抛出正常源操作业务异常
				final Exception[] cacheDataException = new Exception[1];
				data = (T)getRedisOperator().execute(new JedisRunner()
				{
					@Override
					public Object run(Jedis jedis) throws Exception
					{
						String strValue;
						T data = null;
						try{
							if(jedis.exists(cacheConfig().getCacheKey())){
								logger.debug("-----------[Redis] key:{} [Exists]", cacheConfig().getCacheKey());
								strValue = jedis.get(cacheConfig().getCacheKey());
								data = json2Object(strValue, clazz);
								if(cacheConfig().getUpdateCacheEveryTime()){
									logger.debug("-----------[Redis] key:{} [Exists, Update]", cacheConfig().getCacheKey());
									jedis.setex(cacheConfig().getCacheKey(), cacheConfig().getCacheTime(), strValue);
								}
							}else{
								logger.debug("-----------[Redis] key:{} [Not Exists, Update]", cacheConfig().getCacheKey());
								try {
									data = cacheData();
								} catch (Exception e1) {
									e1.printStackTrace();
									cacheDataException[0] = e1;
									return null;
								}
								strValue = object2Json(data);
								jedis.set(cacheConfig().getCacheKey(), strValue, "NX", "EX", cacheConfig().getCacheTime());
								if(null!=cacheTimeOutTask){
									cacheTimeOutTask.doTask();
								}
							}
						}catch(JedisException e){
							e.printStackTrace();
							try {
								data = cacheData();
							} catch (Exception e1) {
								e1.printStackTrace();
								cacheDataException[0] = e1;
								return null;
							}
						}catch(Exception e){
							e.printStackTrace();
							cacheDataException[0] = e;
							return null;
						}
						return data;
					}
				});
				if(null!=cacheDataException[0]){
					throw cacheDataException[0];
				}
			}catch(JedisException e){
				data = cacheData();
			}
		}
		return data;
	}

}
