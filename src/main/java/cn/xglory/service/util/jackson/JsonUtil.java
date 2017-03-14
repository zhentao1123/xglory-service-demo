package cn.xglory.service.util.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
	
private static ObjectMapper objectMapper;
	
	public static <T> T json2obj(String json, Class<T> clazz){
		try {
			return (T) getObjectMapper().readValue(json, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
			return null;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Json字符串转换为泛型类型对象
	 * @param json
	 * @param collectionClass 容器对象类
	 * @param elementClasses 容物对象类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T json2obj(String json, Class<?> collectionClass, Class<?>... elementClasses){
		try {
			JavaType javaType = getCollectionType(collectionClass, elementClasses); 
			return (T) getObjectMapper().readValue(json, javaType);
		} catch (JsonParseException e) {
			//e.printStackTrace();
			return null;
		} catch (JsonMappingException e) {
			//e.printStackTrace();
			return null;
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
	}
	
	public static String obj2json(Object obj){
		ObjectMapper mapper = getObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据容器和容物类获取JavaType对象
	 * @param collectionClass
	 * @param elementClasses
	 * @return
	 */
	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		ObjectMapper mapper = getObjectMapper();
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
    }
	
	/**
	 * 该处可根据需要返回自定义的ObjectMapper
	 * @return
	 */
	public static ObjectMapper getObjectMapper(){
		if(objectMapper == null){
			objectMapper = new ObjectMapper();
			objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return objectMapper;
	}
}
