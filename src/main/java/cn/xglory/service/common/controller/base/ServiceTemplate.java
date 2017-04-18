package cn.xglory.service.common.controller.base;

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.xglory.service.common.exception.BaseServiceException;
import cn.xglory.service.util.jackson.JsonUtil;

/**
 * 服务实现代理类
 * 模板方法包装响应对象，处理日志，处理方法调用计时
 * @author Bob
 *
 * @param <R>
 * @param <S>
 */
public abstract class ServiceTemplate<R,S> {
	
	static Log logger = LogFactory.getLog(ServiceTemplate.class);
	
	/**
	 * 抽象处理方法，需要代理创建时候实现
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public abstract S doProcess(R req) throws BaseServiceException, Exception;
	
	/**
	 * 通用业务处理方法，对处理结果做通用封装
	 * @param req
	 * @return
	 */
	public CommonRsp<S> process(CommonReq<R> req){
		logger.info("[ Proxy Service Request ] : " + JsonUtil.obj2json(req));
		
		long beginTime = System.currentTimeMillis();
		CommonRsp<S> rsp = new CommonRsp<S>();
		R reqData = req.getData();
		if(null==reqData){
			/** 参数错误处理 */
			rsp.init4Exception(BaseServiceException.buildParamErr());
		}else{ 
			/** 调用实现方法 */
			try{
				S rspData = doProcess(reqData);
				rsp.setData(rspData);
				rsp.init4Succeed();
			}catch(BaseServiceException e){
				rsp.init4Exception(e);
			}
			catch(Exception e){
				e.printStackTrace();
				rsp.init4Exception(BaseServiceException.buildCommonErr(e));
			}
		}
		long endTime = System.currentTimeMillis();
		rsp.setTime(BigInteger.valueOf((endTime - beginTime) / 1000));
		
		logger.info("[ Proxy Service Response ] : " + JsonUtil.obj2json(rsp));
		return rsp;
	}
}
