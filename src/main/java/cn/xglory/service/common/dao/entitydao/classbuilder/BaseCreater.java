package cn.xglory.service.common.dao.entitydao.classbuilder;

import org.apache.commons.lang3.StringUtils;

public class BaseCreater {
	//首字母转小写
	protected String toLowerCaseFirstOne(String s){
		if(StringUtils.isBlank(s)){
			return null;
		}else{
			return Character.isLowerCase(s.charAt(0)) ? 
	        		s : (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
		}
    }
    
    //首字母转大写
	protected String toUpperCaseFirstOne(String s){
		if(StringUtils.isBlank(s)){
			return null;
		}else{
			return Character.isUpperCase(s.charAt(0)) ? 
	        		s : (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
		}
    }
	
	//
	protected String buildClassName(String s){
		String str = toUpperCaseFirstOne(s);
		if(StringUtils.isBlank(str)){
			return null;
		}else{
			String[] strArr = str.split("_");
			for(int i=0; i<strArr.length; i++){
				strArr[i] = toUpperCaseFirstOne(strArr[i]);
			}
			return StringUtils.join(strArr);
		}
	}
}
