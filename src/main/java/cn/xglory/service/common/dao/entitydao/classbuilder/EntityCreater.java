package cn.xglory.service.common.dao.entitydao.classbuilder;

import cn.xglory.service.common.dao.entitydao.classbuilder.IJavaClassCreater.VelocityJavaEntity;
import cn.xglory.service.common.dao.entitydao.classbuilder.bean.TableInfo;

public class EntityCreater extends BaseCreater{
	
	//vm文件路径
	private String vmPath;
	//vm文件名
	private String vmName;
	//输出文件路径
	private String classFilePath;
	//输出java类包目录  如：cn.xglory.modules.entity
	private String classPackagePath;
			
	private JavaClassCreater javaClassCreater;
	
	public EntityCreater(String vmPath, String vmName, String classPackagePath){
		this.javaClassCreater = new JavaClassCreater();
		this.vmPath = vmPath;
		this.vmName = vmName;
		this.classFilePath = System.getProperty("user.dir") + "/src/main/java/" + classPackagePath.replace(".", "/") + "/";
		this.classPackagePath = classPackagePath;
	}
	
	public void createFile(String tableName, TableInfo infoValue) throws Exception {
		//类名
		String className = buildClassName(tableName);
		//文件名
		String fileName =  className + ".java";
		
		//对应vm模板文件中的参数对象定义类
//		TableInfo infoValue = this.sqlHelper.getTableInfo(tableName);
		
		VelocityJavaEntity<TableInfo> info = new VelocityJavaEntity<TableInfo>();
		info.setFileName(fileName);
		info.setFilePath(classFilePath);
		info.setInfoValue(infoValue);
		info.setPackagePath(classPackagePath);
		info.setVmName(vmName);
		info.setVmPath(vmPath);
		info.setClassName(className);
		
		this.javaClassCreater.createFile(info);
	}
	
}
