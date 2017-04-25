package cn.xglory.service.common.dao.entitydao.classbuilder;

import java.sql.SQLException;
import java.util.List;

import cn.xglory.service.common.dao.entitydao.classbuilder.bean.TableInfo;

public class RunCreate {
	
	public static void main(String[] args){
		SqlHelper sqlHelper = new SqlHelper();
		try {
			createEntities(sqlHelper);
			createEntityDaos(sqlHelper);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static final String VM_FOLDER = "vm";
	static final String ENTITY_VM_FILE = "Entity.vm";
	static final String DAO_VM_FILE = "EntityDao.vm";
	static final String ENTITY_PACKAGE = "cn.xglory.service.demo.entity";
	static final String DAO_PACKAGE = "cn.xglory.service.demo.dao.entitydao";
	
	static void createEntities(SqlHelper sqlHelper) throws SQLException{
		List<String> tableNames = sqlHelper.readTableNames();
		EntityCreater creater = new EntityCreater(VM_FOLDER, ENTITY_VM_FILE, ENTITY_PACKAGE);
		for(String tableName : tableNames){
			try {
				TableInfo infoValue = sqlHelper.getTableInfo(tableName);
				creater.createFile(tableName, infoValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static void createEntityDaos(SqlHelper sqlHelper) throws SQLException{
		List<String> tableNames = sqlHelper.readTableNames();
		EntityDaoCreater creater = new EntityDaoCreater(VM_FOLDER, DAO_VM_FILE, DAO_PACKAGE, ENTITY_PACKAGE);
		for(String tableName : tableNames){
			try {
				creater.createFile(tableName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}