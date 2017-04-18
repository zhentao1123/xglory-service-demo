package cn.xglory.service.common.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * 数据处理基类，封装读写数据库方法，读写对象的持有者。
 * 实现读写分离
 * @author Bob
 */
@Repository
public class BaseJdbcDao{
	
	private static Logger logger = LoggerFactory.getLogger(BaseJdbcDao.class);
	
	@Resource(name = "jdbcTemplateRead")
	protected JdbcTemplate jdbcTemplateRead;
	
	@Resource(name = "jdbcTemplateWrite")
	protected JdbcTemplate jdbcTemplateWrite;
	
	@Resource(name = "namedParameterJdbcTemplateRead")
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplateRead;
	
	@Resource(name = "namedParameterJdbcTemplateWrite")
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplateWrite;
	 
	/**
	 * 返回单个基本数据类型的查询（读库）
	 */
	public <T> T queryValue(String sql, Class<T> requiredType, Object... args) throws Exception{
	    try {  
	    	return (args!=null && args.length!=0) ? 
	    			jdbcTemplateRead.queryForObject(sql, args, requiredType) : 
	    			jdbcTemplateRead.queryForObject(sql, requiredType);
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回单个基本数据类型的查询（写库）
	 */
	public <T> T queryValue_W(String sql, Class<T> requiredType, Object... args) throws Exception{
	    try {  
	    	return (args!=null && args.length!=0) ? 
	    			jdbcTemplateWrite.queryForObject(sql, args, requiredType) : 
	    			jdbcTemplateWrite.queryForObject(sql, requiredType);
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回基本数据类型列表的查询（读库）
	 */
	public <R> List<R> queryValueList(String sql, Class<R> elementType, Object... args) throws Exception{
	    try {  
	    	return (args!=null && args.length!=0) ? 
	    			jdbcTemplateRead.queryForList(sql, elementType, args):
	    			jdbcTemplateRead.queryForList(sql, elementType);
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回Map的查询（读库）
	 */
	public Map<String, Object> queryMap(String sql, Object... args) throws Exception {
		try {
			return jdbcTemplateRead.queryForMap(sql, args);
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回Map列表的查询（读库）
	 */
	public List<Map<String, Object>> queryMapList(String sql, Object... args) throws Exception{
	    try {  
	    	return (args!=null && args.length!=0) ? 
	    			jdbcTemplateRead.queryForList(sql, args) : 
	    			jdbcTemplateRead.queryForList(sql);
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回单个对象的查询（读库）
	 */
	public <T> T queryObject(String sql, RowMapper<T> rowMapper, Object... args) throws Exception{
		try {  
			return jdbcTemplateRead.queryForObject(sql, args, rowMapper);
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回单个对象的查询（读库）
	 */
	public <T> T queryObject(String sql, Class<T> clazz, Object... args) throws Exception{
		try {
			return jdbcTemplateRead.queryForObject(sql, args, BeanPropertyRowMapper.newInstance(clazz));
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回对象列表的查询（读库）
	 */
	public <R> List<R> queryObjectList(String sql, RowMapper<R> rowMapper, Object... args) throws Exception{
	    try {  
	    	return (args!=null && args.length!=0) ? 
	    			jdbcTemplateRead.query(sql, args, rowMapper) : 
	    			jdbcTemplateRead.query(sql, rowMapper);
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回对象列表的查询（读库）
	 */
	public <R> List<R> queryObjectList(String sql, Class<R> clazz, Object... args) throws Exception{
	    try {  
	    	return (args!=null && args.length!=0) ? 
	    			jdbcTemplateRead.query(sql, args, BeanPropertyRowMapper.newInstance(clazz)) : 
	    			jdbcTemplateRead.query(sql, BeanPropertyRowMapper.newInstance(clazz));
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 数据写操作（写库）
	 */
	public int update(final String sql, final Object... args) throws Exception{
		return jdbcTemplateWrite.update(sql, args);
	}
	
	/**
	 * 数据写操作（自增长key获取）
	 */
	public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws Exception{
		return jdbcTemplateWrite.update(psc, generatedKeyHolder);
	}
	
	/**
	 * 执行存储过程，无参或者参数拼入sql（写库）
	 */
	public void execute(final String sql) throws Exception{
		jdbcTemplateWrite.execute(sql);
	}
	
	/**
	 * 写执行存储过程（写库）
	 * 写法参照：http://sunbin123.iteye.com/blog/1007556
	 */
	public <T> T execute(CallableStatementCreator callableStatementCreator, CallableStatementCallback<T> callableStatementCallback) throws Exception{
		return jdbcTemplateWrite.execute(callableStatementCreator, callableStatementCallback);
	}
	
	/**
	 * 执行存储过程（读库）
	 * 写法参照：http://sunbin123.iteye.com/blog/1007556
	 */
	public <T> T execute_R(CallableStatementCreator callableStatementCreator, CallableStatementCallback<T> callableStatementCallback) throws Exception{
		return jdbcTemplateRead.execute(callableStatementCreator, callableStatementCallback);
	}
	
	//-- For NamedParameterJdbcTemplate -----------------------------------------------

	/**
	 * 返回单个基本数据类型的查询（读库）
	 */
	public <T> T queryValue(String sql, Class<T> requiredType, Map<String,?> parameter) throws Exception{
		try{
			return namedParameterJdbcTemplateRead.queryForObject(sql, parameter, requiredType) ;
		} catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回基本数据类型列表的查询（读库）
	 */
	public <R> List<R> queryValueList(String sql, Map<String,?> paramMap, Class<R> elementType) throws Exception {
		try{
			return namedParameterJdbcTemplateRead.queryForList(sql, paramMap, elementType);
		} catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回Map的查询（读库）
	 */
	public Map<String, Object> queryMap(String sql, Map<String,?> paramMap) throws Exception {
		try{
			return namedParameterJdbcTemplateRead.queryForMap(sql, paramMap);
		} catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回Map列表的查询（读库）
	 */
	public List<Map<String, Object>> queryMapList(String sql, Map<String,?> paramMap) throws Exception {
		try{
			return namedParameterJdbcTemplateRead.queryForList(sql, paramMap);
		} catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回单个对象的查询（读库）
	 */
	public <T> T queryObject(String sql, Map<String,?> paramMap, RowMapper<T> rowMapper) throws Exception {
		try{
			return namedParameterJdbcTemplateRead.queryForObject(sql, paramMap, rowMapper);
		} catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回单个对象的查询（读库）
	 */
	public <T> T queryObject(String sql, Map<String,?> paramMap, Class<T> clazz) throws Exception{
		try {
			return namedParameterJdbcTemplateRead.queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(clazz));
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回对象列表的查询（读库）
	 */
	public <R> List<R> queryObjectList(String sql, Map<String,?> paramMap, RowMapper<R> rowMapper) throws Exception{
	    try {  
	    	return namedParameterJdbcTemplateRead.query(sql, paramMap, rowMapper);
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 返回对象列表的查询（读库）
	 */
	public <R> List<R> queryObjectList(String sql, Map<String,?> paramMap, Class<R> clazz) throws Exception{
	    try {  
	    	return namedParameterJdbcTemplateRead.query(sql, paramMap, BeanPropertyRowMapper.newInstance(clazz));
	    } catch (EmptyResultDataAccessException e){
	    	return null;
	    }
	}
	
	/**
	 * 数据写操作（写库）
	 */
	public int update(final String sql, final Map<String,?> paramMap) throws Exception{
		return namedParameterJdbcTemplateWrite.update(sql, paramMap);
	}
	
	/**
	 * 数据写操作（自增长key获取）
	 */
	public int update(final String sql, SqlParameterSource sps, KeyHolder generatedKeyHolder) throws Exception{
		return namedParameterJdbcTemplateWrite.update(sql, sps, generatedKeyHolder);
	}
	
	/**
	 * 执行存储过程（写库）
	 */
	public <T> void execute(final String sql, final Map<String,?> paramMap, PreparedStatementCallback<T> action) throws Exception{
		namedParameterJdbcTemplateWrite.execute(sql, paramMap, action);
	}
	
	/**
	 * 执行存储过程（读库）
	 */
	public <T> void execute_R(final String sql, final Map<String,?> paramMap, PreparedStatementCallback<T> action) throws Exception{
		namedParameterJdbcTemplateRead.execute(sql, paramMap, action);
	}
	
}
