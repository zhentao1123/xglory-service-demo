package cn.xglory.service.common.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class BaseService {
	
	private static Logger logger = LoggerFactory.getLogger(BaseService.class);
	
	@Resource(name = "transactionManagerJDBCRead")
	private DataSourceTransactionManager transactionManagerRead;
	
	@Resource(name = "transactionManagerJDBCWrite")
	private DataSourceTransactionManager transactionManagerWrite;
	
	protected void doTransaction(final TransCallbackWithoutResult callback) throws Exception
	{
		try{
			TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerWrite);  
			transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
			transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {  
				protected void doInTransactionWithoutResult(TransactionStatus status) {  
					try{
						callback.doInTransaction();
					}
					catch (Exception e) {
						logger.debug("writeData error :", e);
//						status.setRollbackOnly();  //触发回滚，但如果同时再抛出RuntimeException会出错。因为不能再抛出异常，不建议用。
						throw unchecked(e); //抛出RuntimeException同时会触发回滚
					}
			}}); 
		} 
		catch (Exception e) {  
			logger.debug("writeData error :", e);
	    	throw e;
	    }
		
		//该方法经证实也可以回滚的同时抛出异常
//		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerWrite);  
//		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
//		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		TransactionStatus status = transactionManagerWrite.getTransaction(transactionTemplate);  
//	    try {  
//	    	try{
//				callback.doInTransaction();
//			}
//			catch (Exception e) {
//				BaseLogger.info("writeData error 1:", e);
//				throw Exceptions.unchecked(e);
//			}
//	        transactionManagerWrite.commit(status);  
//	    } catch (RuntimeException e) {  
//	    	BaseLogger.debug("writeData error :", e);
//	    	transactionManagerWrite.rollback(status);  
//	    	throw e;
//	    } 

	}
	
	@SuppressWarnings("unchecked")
	protected <T> T doTransaction(final TransCallbackWithResult<T> callback) throws Exception
	{
		try{
			TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerWrite);  
			transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
			Object ret = transactionTemplate.execute(new TransactionCallback<Object>() {  
				@Override
				public Object doInTransaction(TransactionStatus status) {
					try{
						return callback.doInTransaction();
					}
					catch (Exception e) {
						logger.debug("writeDataWithResult error :", e);
//						status.setRollbackOnly();  //触发回滚，但如果同时再抛出RuntimeException会出错。因为不能再抛出异常，不建议用。
						throw unchecked(e); ////抛出RuntimeException同时会触发回滚
					}
				}}); 
			return (T)ret;
		} catch (Exception e) {  
			logger.debug("writeDataWithResult error :", e);
			throw e;
	    }
	}
	
	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	private static RuntimeException unchecked(Throwable ex) {
		if (ex instanceof RuntimeException) {
			return (RuntimeException) ex;
		} else {
			return new RuntimeException(ex);
		}
	}
	
	/**
	 * 有返回值的事务回调接口
	 */
	public static interface TransCallbackWithResult<T> {
		/**
		  * 要在事务中回调执行的方法
		  * @return 所指定类型的数据
		  */
		T doInTransaction() throws Exception;
	}
	
	/**
	 * 无返回值 的事务回调接口
	 */
	public static interface TransCallbackWithoutResult {
		/**
		  * 要在事务中回调执行的方法
		  */
		public void doInTransaction() throws Exception;
	}
	
}
