package cn.xglory.service.common.exception;

@SuppressWarnings("serial")
public class BaseServiceException extends RuntimeException{
	
	public static final String CODE_COMM_FAIL = "1";
	public static final String CODE_PARAM_ERR = "100";
	
	protected String code;
	protected String message;
	
	public BaseServiceException(){
		super();
	}
	
	public BaseServiceException(Throwable throwable){
		super(throwable);
	}
	
	public BaseServiceException(Exception throwable){
		super(throwable);
	}
	
	public BaseServiceException(String message){ 
		super(message);
	}
	
	public BaseServiceException(String code, String message){ 
		super(code + "_" + message);
		this.code = code;
		this.message = message;
	}
	
	public BaseServiceException(String code, Exception throwable){
		super(throwable);
		this.code = code;
		this.message = throwable.getMessage();
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
	
	public static BaseServiceException buildCommonErr(Exception e){
		return new BaseServiceException(CODE_COMM_FAIL, e);
	}
	
	public static BaseServiceException buildCommonErr(){
		return new BaseServiceException(CODE_COMM_FAIL, "处理失败");
	}
	
	public static BaseServiceException buildParamErr(){
		return new BaseServiceException(CODE_PARAM_ERR, "参数错误");
	}
}
