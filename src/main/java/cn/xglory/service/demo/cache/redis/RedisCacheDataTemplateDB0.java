package cn.xglory.service.demo.cache.redis;

import javax.annotation.Resource;

import cn.xglory.service.common.cache.redis.RedisCacheDataTemplate;
import cn.xglory.service.common.cache.redis.RedisOperator;

public abstract class RedisCacheDataTemplateDB0<T> extends RedisCacheDataTemplate<T>{

	@Resource(name="db0")
	RedisOperator redisOperator;
	
	@Override
	public RedisOperator getRedisOperator() {
		return redisOperator;
	}

}
