package cn.xglory.service.common.dao.entitydao.classbuilder;

import cn.xglory.service.common.dao.entitydao.classbuilder.IJavaClassCreater.VelocityJavaEntity;
import cn.xglory.service.common.dao.entitydao.classbuilder.bean.EntityDaoInfo;

public class EntityDaoCreater extends BaseCreater{
	
	//vm文件路径
	private String vmPath;
	//vm文件名
	private String vmName;
	//输出文件路径
	private String classFilePath;
	//输出java类包目录 如：cn.xglory.modules.entity.dao
	private String daoClassPackagePath;
	//相应entity类包目录 如：cn.xglory.modules.entity
	private String entityClassPackagePath;
			
	private JavaClassCreater javaClassCreater;
	
	public EntityDaoCreater(String vmPath, String vmName, String daoClassPackagePath, String entityClassPackagePath){
		this.javaClassCreater = new JavaClassCreater();
		this.vmPath = vmPath;
		this.vmName = vmName;
		this.classFilePath = System.getProperty("user.dir") + "/src/main/java/" + daoClassPackagePath.replace(".", "/") + "/";;
		this.daoClassPackagePath = daoClassPackagePath;
		this.entityClassPackagePath = entityClassPackagePath;
	}
	
	public void createFile(String tableName) throws Exception {
		String entityName = buildClassName(tableName);
		String className = entityName + "Dao";

		//文件名
		String fileName =  className + ".java";
		
		//对应vm模板文件中的参数对象定义类
		EntityDaoInfo infoValue = new EntityDaoInfo(entityClassPackagePath, entityName);
		
		VelocityJavaEntity<EntityDaoInfo> info = new VelocityJavaEntity<EntityDaoInfo>();
		info.setFileName(fileName);
		info.setFilePath(classFilePath);
		info.setInfoValue(infoValue);
		info.setPackagePath(daoClassPackagePath);
		info.setVmName(vmName);
		info.setVmPath(vmPath);
		info.setClassName(className);
		
		this.javaClassCreater.createFile(info);
	}
	
}
