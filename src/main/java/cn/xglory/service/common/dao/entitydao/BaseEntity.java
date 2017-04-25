package cn.xglory.service.common.dao.entitydao;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseEntity {
	
	@Override
	public String toString(){
		ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			return "";
		}
	}
	
	public java.sql.Date getSqlDate(java.util.Date date){
		if(null!=date){
			return new java.sql.Date(date.getTime());
		}else{
			return null;
		}
	}
	
	public java.sql.Timestamp getSqlTimestamp(java.util.Date date){
		if(null!=date){
			return new java.sql.Timestamp(date.getTime());
		}else{
			return null;
		}
	}
}
