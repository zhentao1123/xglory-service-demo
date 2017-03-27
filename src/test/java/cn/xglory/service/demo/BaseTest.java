package cn.xglory.service.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml"})
public class BaseTest {

	/**
	 * 空实现，仅为通过对于JUnit类必要要有测试方法的校验
	 */
	@Test
	public void defaultTest() {}

}