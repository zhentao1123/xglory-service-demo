package cn.xglory.service.common.dao.entitydao;

import java.util.List;

public interface EntityDao<E extends BaseEntity> {
	
	public E getEntityById(Object id) throws Exception;
	
	public E getEntityByUuid(String uuid) throws Exception;
	
	public List<E> getAllEntity() throws Exception;
	
	public void insertEntity(E entity) throws Exception;
	
	public void updateEntity(E entity) throws Exception;
	
	public void deleteEntityById(Object id) throws Exception;
	
	public void deleteEntityByUuid(String uuid) throws Exception;
	
}
