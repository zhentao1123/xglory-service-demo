package cn.xglory.service.common.dao.entitydao.classbuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class JavaClassCreater implements IJavaClassCreater{

	public <E> void createFile(VelocityJavaEntity<E> info) {
		try{			
			FileWriter fw = new FileWriter(createFile(info.getFilePath(), info.getFileName()));
			String fileContent = createCode(info);
			fw.write(fileContent);
			fw.flush();
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回模板信息和动态信息拼合后的文本，可用于直接写入文件
	 * @param velocityEntity
	 * @return
	 * @throws Exception
	 */
	private <E> String createCode(VelocityJavaEntity<E> velocityEntity) throws Exception{
    	VelocityEngine velocityEngine = new VelocityEngine();
    	
    	Properties p = new Properties(); 
    	//设置输入输出编码类型。和这次说的解决的问题无关  
        p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");  
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");  
        //指定一个路径，不过这样要求你所有的模板都放在该路径下 
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, velocityEntity.getVmPath());
        //加载类路径里的模板而不是文件系统路径里的模板  
//       p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");  
    	
		velocityEngine.init(p);
		
		Template template = velocityEngine.getTemplate(velocityEntity.getVmName());
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put(velocityEntity.getInfoParam(), velocityEntity); //约定vm中从对象info中取信息
		StringWriter stringWriter = new StringWriter();
		template.merge(velocityContext, stringWriter);
		return stringWriter.toString();
    }
	
	/**
	 * 创建文件及相应目录
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	private File createFile(String filePath, String fileName) throws Exception{
		File filePath_ = new File(filePath);
		if(!filePath_.exists()){
			filePath_.mkdirs();
        }
		File file = new File(filePath + fileName);
		return file;
	}

}
