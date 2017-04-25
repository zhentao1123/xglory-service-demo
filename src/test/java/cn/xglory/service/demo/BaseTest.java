package cn.xglory.service.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import spring.Profiles;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.SIMULATE)
@ContextConfiguration(locations = { "classpath*:/applicationContext.xml", "classpath*:/applicationContext-import.xml" })
@TransactionConfiguration(transactionManager = "transactionManagerJDBCWrite")
@Transactional
public class BaseTest {

	/**
	 * 空实现，仅为通过对于JUnit类必要要有测试方法的校验
	 */
	@Test
	public void defaultTest() {}

}