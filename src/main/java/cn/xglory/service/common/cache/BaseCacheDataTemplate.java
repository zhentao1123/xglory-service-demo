package cn.xglory.service.common.cache;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.DisposableBean;

import cn.xglory.service.util.jackson.JsonUtil;

/**
 * 缓存数据管理基类
 * 设置缓存数据和参数，封装缓存数据的读写逻辑
 * @author Bob
 *
 * @param <T>
 */
public abstract class BaseCacheDataTemplate<T> implements DisposableBean{
	
	protected CacheConfig cacheConfig;
	
	/**
	 * 获取数据（按照缓存情况从缓存或者源获取，如有需要更新缓存）
	 * @param clazz
	 * @param cacheTimeOutTask
	 * @return
	 * @throws Exception
	 */
	public abstract T getData(final Class<T> clazz, final CacheTimeOutTask cacheTimeOutTask) throws Exception;
	
	/**
	 * 设置缓存配置信息
	 * @return
	 */
	public abstract CacheConfig cacheConfig();
	
	/**
	 * 设置缓存数据内容
	 * @return
	 */
	public abstract T cacheData() throws Exception;
	
	/**
	 * 缓存超时后需要执行的任务
	 * @author Bob
	 */
	public static interface CacheTimeOutTask{
//		public Object doTask() throws Exception;
		//暂时不抛出错误，内部捕获，附加任务默认不影响主操作
		public Object doTask();
	}
	
	/**
	 * 缓存配置对象
	 * @author Bob
	 *
	 */
	public static class CacheConfig implements Cloneable{
		/**
		 * 缓存key
		 */
		private String cacheKey;
		
		/**
		 * 设置缓存有效期
		 */
		private Integer cacheTime;
		
		/**
		 * 是否每次取数据都更新缓存
		 * 1）缓存以优化访问速度为目的，可以设为每次都更新，保证每次访问缓存
		 * 2）缓存以减少穿透访问（数据库）为目的，可以设为不每次都更新，待缓存失效后更新
		 */
		private Boolean updateCacheEveryTime;
		
		public CacheConfig(String cacheKey, Integer cacheTime,
				Boolean updateCacheEveryTime) {
			super();
			this.cacheKey = cacheKey;
			this.cacheTime = cacheTime;
			this.updateCacheEveryTime = updateCacheEveryTime;
		}
		
		public boolean dataValid(){
			if(StringUtils.isEmpty(this.cacheKey) || null==this.cacheTime || null==this.updateCacheEveryTime){
				return false;
			}
			return true;
		}

		public String getCacheKey() {
			return cacheKey;
		}

		public void setCacheKey(String cacheKey) {
			this.cacheKey = cacheKey;
		}

		public Integer getCacheTime() {
			return cacheTime;
		}

		public void setCacheTime(Integer cacheTime) {
			this.cacheTime = cacheTime;
		}

		public Boolean getUpdateCacheEveryTime() {
			return updateCacheEveryTime;
		}

		public void setUpdateCacheEveryTime(Boolean updateCacheEveryTime) {
			this.updateCacheEveryTime = updateCacheEveryTime;
		}
		
	    public Object clone() throws CloneNotSupportedException {
	        return super.clone();
	    }
	}
	
	//-- Tool Func ---------------------------------------------------------------------------------------
	
	/**
	 * 初始化并检查必要参数
	 * @return
	 * @throws Exception 
	 */
	protected boolean initData(){
		try{
			this.cacheConfig = cacheConfig();
		}catch(Exception e){
			return false;
		}
		if(!cacheConfig.dataValid()){
			return false;
		}
		return true;
	}
	
	protected T json2Object(String json, Class<T> clazz) {
		return JsonUtil.json2obj(json, clazz);
	}
	
	protected String object2Json(Object obj){
		return JsonUtil.obj2json(obj);
	}
	
	/**
	 * 为了统一缓存中Json对象的解析，缓存List<T>对象的时候用该类进行封装
	 * @author zhangzhentao
	 *
	 * @param <T>
	 */
	public static class ListWrap<T>{
		private List<T> list;
		
		public ListWrap() {}
		
		public ListWrap(List<T> list) {
			super();
			this.list = list;
		}

		public List<T> getList() {
			return list;
		}

		public void setList(List<T> list) {
			this.list = list;
		}
	}
}
