package cn.xglory.service.common.dao.entitydao.classbuilder.bean;

import org.apache.commons.lang3.StringUtils;

public class ColumnInfo {
	//表字段索引
	private int index;
	//表字段名称
	private String field;
	//表字段类型
	private String type;
	//注释
	private String comment;
	//是否主键
	private boolean pk;
	//是否必须
	private boolean require;
	//字段长度
	private String length;
	//对应java类全名
	private String javaClass;
	//对应java类名
	private String javaClassName;
	//表字段名称，首字母大写
	private String fieldUpperCase;
	//字段值
	private Object value;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		/**
		 * 处理java关键字
		 */
		field = processJavaKeyWord(field);
		this.fieldUpperCase = toUpperCaseFirstOne(field);
		this.field = field;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		//importClass需要处理成类中对应的文本
//		String type_ = typeProcess(type);
//		this.javaClass = javaClassProcess(type_);
//		this.type = type_;
		this.type = type;
	}
	public String getFieldUpperCase() {
		return fieldUpperCase;
	}
	public void setFieldUpperCase(String fieldUpperCase) {
		this.fieldUpperCase = fieldUpperCase;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		/**
		 * 处理特殊字符
		 */
		comment = processSpecialChar(comment);
		this.comment = comment;
	}
	public boolean isPk() {
		return pk;
	}
	public void setPk(boolean pK) {
		pk = pK;
	}
	public boolean isrequire() {
		return require;
	}
	public void setrequire(boolean require) {
		this.require = require;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getJavaClass() {
		return javaClass;
	}
	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
		this.javaClassName = javaClass.substring(javaClass.lastIndexOf(".")+1);
	}
	public String getJavaClassName() {
		return javaClassName;
	}
	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	//首字母转小写
	protected String toLowerCaseFirstOne(String s){
        return Character.isLowerCase(s.charAt(0)) ? 
        		s : (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    
    //首字母转大写
	protected String toUpperCaseFirstOne(String s){
        return Character.isUpperCase(s.charAt(0)) ? 
        		s : (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
	
//	private String typeProcess(String type){
//	if(type==null)return "";
//	String result = type;
//	
//	for(String[] info : types){
//		String supportType = info[0];
//		String showType = info[1];
//		String javaClass = info[2];
//		if(type.startsWith(supportType)) {
//			result = showType;
//			break;
//		}
//	}
//	return result;
//}

//private String javaClassProcess(String type){
//	if(type==null)return "";
//	String result = type;
//	
//	for(String[] info : types){
//		String supportType = info[0];
//		String showType = info[1];
//		String javaClass = info[2];
//		if(type.startsWith(showType)) {
//			result = javaClass;
//			break;
//		}
//	}
//	return result;
//}
	
//	private String[][] types = {
//				{"char",		"String",	""},
//				{"varchar",		"String",	""},
//				{"int",			"Integer",	""},
//				{"timestamp",	"Date",		"java.util.Date"},
//				{"date",		"Date",		"java.util.Date"},
//				{"time",		"Date",		"java.util.Date"},
//				{"text",		"String",	""},
//			};
	
	private static String processSpecialChar(String orig){
		String[] specialChars = new String[]{"\""};
		if(StringUtils.isBlank(orig)){
			return orig;
		}else{
			for(String specialChar : specialChars){
				if(orig.contains(specialChar)){
					return orig.replace(specialChar, "\\" + specialChar);
				}
			}
			return orig;
		}
	}
	
	private static String processJavaKeyWord(String orig){
		String[] javaKeyWords = new String[]{"short","boolean","long","double","int","char","byte"};
		if(StringUtils.isBlank(orig)){
			return orig;
		}else{
			for(String keyword : javaKeyWords){
				if(orig.equals(keyword)){
					return orig+"_";
				}
			}
			return orig;
		}
	}
}
