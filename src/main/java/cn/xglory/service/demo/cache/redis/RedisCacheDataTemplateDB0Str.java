package cn.xglory.service.demo.cache.redis;

import java.util.Date;

import cn.xglory.service.util.DateUtil;

public class RedisCacheDataTemplateDB0Str extends RedisCacheDataTemplateDB0<java.lang.String>{

	@Override
	public cn.xglory.service.common.cache.BaseCacheDataTemplate.CacheConfig cacheConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cacheData() throws Exception {
		return DateUtil.getDateStr(new Date());
	}

	

}
