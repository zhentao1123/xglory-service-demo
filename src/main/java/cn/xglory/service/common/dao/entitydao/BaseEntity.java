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
}
