package cn.xglory.service.common.controller.base;

/**
 * 通用请求类
 * @author Bob
 *
 */
public class CommonReq<D> extends BaseBean{
	
	/**
	 * 是否模拟（返回演示用模拟数据）
	 */
	private Boolean mock;
	
	/**
	 * 封装请求的数据
	 */
	private D data;
	
	public CommonReq(){}
	
	public CommonReq(D data) {
		super();
		this.data = data;
	}

	public CommonReq(Boolean mock, D data) {
		super();
		this.mock = mock;
		this.data = data;
	}

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}

	public Boolean isMock() {
		if(mock==null){
			return false;
		}
		return mock;
	}

	public void setMock(Boolean mock) {
		this.mock = mock;
	}
	
}
