package cn.xglory.service.common.controller.base;

import java.math.BigInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cn.xglory.service.common.bean.BaseBean;
import cn.xglory.service.common.exception.BaseServiceException;
import cn.xglory.service.util.jackson.JsonUtil;

/**
 * 通用响应结果类
 * @author Bob
 *
 */
public class CommonRsp<D> extends BaseBean{
	
	public static final String RESPONSE_CODE_SUCCEED = "0";
	public static final String RESPONSE_CODE_FAIL = "1";
	
	public static final String RESPONSE_MSG_SUCCEED = "succeed";
	public static final String RESPONSE_MSG_FAIL = "fail";
	
	/**
	 *  响应结果编码（0：成功 1：失败 [n]:其他具体错误编码）
	 */
	private String code;
	/**
	 *  响应信息
	 */
	private String message;
	/**
	 *  响应时间(毫秒值)
	 */
	private BigInteger time;
	
	/**
	 *  封装返回的数据
	 */
	private D data;
	
	public void init4Succeed(){
		this.code = RESPONSE_CODE_SUCCEED;
		this.message = RESPONSE_MSG_SUCCEED;
	}
	
	public void init4Fail(){
		this.code = RESPONSE_CODE_FAIL;
		this.message = RESPONSE_MSG_FAIL;
	}
	
	public void init4Exception(BaseServiceException serviceException){
		this.code = serviceException.getCode();
		this.message = serviceException.getMessage();
	}
	
	public void init4Exception(String code, String message){
		this.code = code;
		this.message = message;
	}
	
	@SuppressWarnings("rawtypes")
	public static CommonRsp getCommonSucceedInstance(){
		CommonRsp commRsp = new CommonRsp();
		commRsp.init4Succeed();
		return commRsp;
	}
	
	@SuppressWarnings("rawtypes")
	public static CommonRsp getCommonFailInstance(){
		CommonRsp commRsp = new CommonRsp();
		commRsp.init4Fail();
		return commRsp;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getCommonSucceedJson(){
		CommonRsp commRsp = CommonRsp.getCommonSucceedInstance();
		return obj2json(commRsp);
	}
	
	@SuppressWarnings("rawtypes")
	public static String getCommonFailJson(){
		CommonRsp commRsp = CommonRsp.getCommonFailInstance();
		return obj2json(commRsp);
	}
	
	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
	
	private static String obj2json(Object obj){
		return JsonUtil.obj2json(obj);
	}
	
	//------------------------------------------------------------------------------------------------------
	
	public D getData() {
		return data;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BigInteger getTime() {
		return time;
	}

	public void setTime(BigInteger time) {
		this.time = time;
	}

	public void setData(D data) {
		this.data = data;
	}

}
