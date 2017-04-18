package cn.xglory.service.common.dao.entitydao.classbuilder;

public interface IJavaClassCreater {
	
	public <T> void createFile(VelocityJavaEntity<T> info);
	
	/**
	 * 封装Velocity工具生成一个Java类文件需要的定义信息
	 * @author Bob
	 *
	 * @param <T>
	 */
	public static final class VelocityJavaEntity<T>{
		
		//对应vm模板文件中的参数对象名,默认'info'
		private String infoParam = "info";
		//对应vm模板文件中的参数对象定义类
		private T infoValue;
		//vm文件路径
		private String vmPath;
		//vm文件名
		private String vmName;
		
		//文件名
		private String fileName;
		//文件路径
		private String filePath;
		//java类包目录
		private String packagePath;
		//类名
		private String className;
		
		public String getInfoParam() {
			return infoParam;
		}
		public void setInfoParam(String infoParam) {
			this.infoParam = infoParam;
		}
		public T getInfoValue() {
			return infoValue;
		}
		public void setInfoValue(T infoValue) {
			this.infoValue = infoValue;
		}
		public String getVmPath() {
			return vmPath;
		}
		public void setVmPath(String vmPath) {
			this.vmPath = vmPath;
		}
		public String getVmName() {
			return vmName;
		}
		public void setVmName(String vmName) {
			this.vmName = vmName;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public String getPackagePath() {
			return packagePath;
		}
		public void setPackagePath(String packagePath) {
			this.packagePath = packagePath;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
	}
}
