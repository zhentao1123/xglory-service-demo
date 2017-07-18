package cn.xglory.service.demo;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.xglory.service.common.cache.redis.RedisOperator;

public class MyTest extends BaseTest{
	
	@Resource(name="db0")
	RedisOperator redisHelper_0;
	
	@Resource(name="db1")
	RedisOperator redisHelper_1;
	
	@Test
	public void redisTest(){
		try {
			redisHelper_0.set("kkk0", "vvv", null);
			redisHelper_1.set("kkk1", "vvv", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
