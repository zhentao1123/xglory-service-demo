package cn.xglory.service.common.dao.entitydao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.xglory.service.common.dao.JdbcDao;
import cn.xglory.service.common.dao.entitydao.annotation.Column;
import cn.xglory.service.common.dao.entitydao.annotation.Table;
import cn.xglory.service.common.dao.entitydao.classbuilder.bean.ColumnInfo;
import cn.xglory.service.common.dao.entitydao.classbuilder.bean.EntityValue;

public class BaseEntityDao<E extends BaseEntity> extends JdbcDao implements EntityDao<E>{

	public E getEntityById(int id) throws Exception {
		String sql = "SELECT * FROM `" + getTableName() + "` WHERE id = ?;";
//System.out.println("Select Sql:"+sql);
		return queryObject(sql, getEntityClass(), id);
	}
	
	public E getEntityByUuid(String uuid) throws Exception{
		String sql = "SELECT * FROM `" + getTableName() + "` WHERE uuid = ?;";
//System.out.println("Select Sql:"+sql);
		return queryObject(sql, getEntityClass(), uuid);
	}

	public List<E> getAllEntity() throws Exception {
		String sql = "SELECT * FROM `" + getTableName() + "`;";
//System.out.println("Select Sql:"+sql);
		return queryObjectList(sql, getEntityClass());
	}

	public void insertEntity(E entity) throws Exception {
//		EntityValue entityValue = getColumnsNotNull(entity);
		EntityValue entityValue = getCachedColumnsNotNull(entity);
		List<ColumnInfo> columnInfos = entityValue.getColumnList();
		String sql = buildInsertSql(getTableName(), columnInfos);
//System.out.println("Insert Sql:"+sql);
		Object[] values = new Object[columnInfos.size()];
		for(int i=0; i<columnInfos.size(); i++){
			values[i] = columnInfos.get(i).getValue();
//System.out.println("Insert PK Value:"+values[i]);
		}
		update(sql, values);
	}

	public void updateEntity(E entity) throws Exception {
//		EntityValue entityValue = getColumnsNotNull(entity);
		EntityValue entityValue = getCachedColumnsNotNull(entity);
		List<ColumnInfo> columnInfos = entityValue.getColumnList();
		List<ColumnInfo> pk = entityValue.getPk();
		String pkName = pk.get(0).getField();
		Object pkValue = pk.get(0).getValue();
		
		String sql = buildUpdateSql(getTableName(), pkName, columnInfos);//暂且实现单主键的情况，多主键后续可以实现
//System.out.println("Update Sql:"+sql);
		Object[] values = new Object[columnInfos.size()+1]; //若实现多主键，修改为加上主键数量
		for(int i=0; i<columnInfos.size(); i++){
			values[i] = columnInfos.get(i).getValue();
//System.out.println("Update Column Value:"+values[i]);
		}
		values[values.length-1] = pkValue;
//System.out.println("Update PK Value:"+pkValue);
		update(sql, values);
	}

	public void deleteEntityById(int id) throws Exception {
		String sql = "DELETE FROM `" + getTableName() + "` WHERE id = ?;";
		update(sql, id);
	}
	
	public void deleteEntityByUuid(String uuid) throws Exception{
		String sql = "DELETE FROM `" + getTableName() + "` WHERE uuid = ?;";
		update(sql, uuid);
	}
	//----------------------------------------------------------------------------------
	
	private Class<E> entityClass;
	private String tableName;
	
	/**
	 * 获取非Null的字段信息及值(缓存反射获取的类信息)
	 * @param entity
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private EntityValue getCachedColumnsNotNull(E entity) throws IllegalArgumentException, IllegalAccessException{
		EntityValue entityValue = new EntityValue();
		List<ColumnInfo> columnList = new ArrayList<ColumnInfo>();
		List<ColumnInfo> pk = new ArrayList<ColumnInfo>();
		
		Class<? extends BaseEntity> clazz = entity.getClass();
		
		String className = clazz.getName();
		
		//读取缓存的EntityValue
		EntityInfo entityInfo = EntityInfoCache.getEntityInfo(className, clazz);
		
		List<FieldInfo> fieldsInfo = entityInfo.getFieldsInfo();
		for(FieldInfo fieldInfo : fieldsInfo)
		{
			Field field = fieldInfo.getField();
			field.setAccessible(true); // 抑制Java的访问控制检查,可以调用私有属性
			Column annoColumn = fieldInfo.getColumn();
			
			Object value = field.get(entity);
			if(null!=value && null!=annoColumn){//只处理带Column标签且值不为空的属性
				ColumnInfo column = new ColumnInfo();
				column.setPk(annoColumn.pk());
				column.setField(annoColumn.field());
				column.setValue(value);
				//TODO others
				if(annoColumn.pk()){
					pk.add(column);
				}else{
					columnList.add(column);
				}
			}
		}
		
		entityValue.setPk(pk);
		entityValue.setColumnList(columnList);
		return entityValue;
	}
	
	/**
	 * 获取非Null的字段信息及值
	 * @param entity
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private EntityValue getColumnsNotNull(E entity) throws IllegalArgumentException, IllegalAccessException{
		EntityValue entityValue = new EntityValue();
		List<ColumnInfo> columnList = new ArrayList<ColumnInfo>();
		List<ColumnInfo> pk = new ArrayList<ColumnInfo>();
		
		Class<? extends BaseEntity> clazz = entity.getClass();
		
		Field[] fields = clazz.getDeclaredFields();
		
		for(Field field : fields){ 
			field.setAccessible(true); // 抑制Java的访问控制检查,可以调用私有属性
			Column annoColumn = field.getAnnotation(Column.class);
			Object value = field.get(entity);
			if(null!=value && null!=annoColumn){//只处理带Column标签且值不为空的属性
				ColumnInfo column = new ColumnInfo();
				column.setPk(annoColumn.pk());
				column.setField(annoColumn.field());
				column.setValue(value);
				//TODO others
				if(annoColumn.pk()){
					pk.add(column);
				}else{
					columnList.add(column);
				}
			}
		}
		entityValue.setPk(pk);
		entityValue.setColumnList(columnList);
		return entityValue;
	}
	
	/**
	 * 构建新增sql
	 * @param tableName
	 * @param columnInfos 不包括主键的字段信息及值
	 * @return
	 */
	private String buildInsertSql(String tableName, List<ColumnInfo> columnInfos){
		//check
		if(tableName==null || tableName.trim().equals("")){
			return null;
		}
		if(columnInfos==null || columnInfos.size()==0){
			return null;
		}
		
		//移除主键,传入数据已不包含主键
//		CopyOnWriteArrayList<ColumnInfo> columnInfos_ = new CopyOnWriteArrayList<ColumnInfo>(columnInfos);
//		for(ColumnInfo columnInfo : columnInfos_) {
//			if(columnInfo.isPk()){
//				columnInfos_.remove(columnInfo);
//			}
//		}
//		columnInfos = (List<ColumnInfo>)columnInfos_;
		
		String sql = null;
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ").append("`"+tableName+"`")
		.append(" ( ");
		
		int i = 1;
		for(ColumnInfo columnInfo : columnInfos) {
			if(i > 1){
				sb.append(" , ");
			}
			sb.append("`"+columnInfo.getField()+"`");
		    i++;
		}
		sb.append(" ) values ( ");
		i = 1;
		for(ColumnInfo columnInfo : columnInfos) {
			if(i > 1){
				sb.append(" , ");
			}
			sb.append("?");
		    i++;
		}
		sb.append(" )");
		sql = sb.toString();
		return sql;
	}
	
	/**
	 * 构建更新sql
	 * @param tableName
	 * @param idName
	 * @param columnInfos 不包括主键的字段信息及值
	 * @return
	 */
	private String buildUpdateSql(String tableName, String idName, List<ColumnInfo> columnInfos){
		//check
		if(tableName==null || tableName.trim().equals("")){
			return null;
		}
		if(columnInfos==null || columnInfos.size()==0){
			return null;
		}
		
		//移除主键，传入数据已不包含主键
//		CopyOnWriteArrayList<ColumnInfo> columnInfos_ = new CopyOnWriteArrayList<ColumnInfo>(columnInfos);
//		for(ColumnInfo columnInfo : columnInfos_) {
//			if(columnInfo.isPk()){
//				columnInfos_.remove(columnInfo);
//			}
//		}
//		columnInfos = (List<ColumnInfo>)columnInfos_;
		
		if(null==idName){
			idName = "id";
		}
		
		String sql = null;
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append("`"+tableName+"`").append(" SET");
		for(ColumnInfo columnInfo : columnInfos) {
			sb.append(" , ").append("`"+columnInfo.getField()+"`").append(" = ?"); 
		}
		sb.append(" WHERE ").append("`"+idName+"`").append(" = ?");
		sql = sb.toString().replaceFirst(",","");
		return sql;
	}
	
	/**
	 * 获取第一个实际定义的泛型类型
	 * @return
	 */
	protected Class<E> getEntityClass() {
		if(null == entityClass)
		{
			Type genericSuperclass = getClass().getGenericSuperclass();
			Type[] actualTypeArguments=null;
			if(genericSuperclass instanceof ParameterizedType){
			 actualTypeArguments = ((ParameterizedType) genericSuperclass)
					.getActualTypeArguments();
			}
			//实际测试是在超类上,这里只做一层判定,后期要是有特殊结构,可改为循环查找
			if(actualTypeArguments==null || actualTypeArguments.length==0){
				genericSuperclass = getClass().getSuperclass().getGenericSuperclass();
				if(genericSuperclass instanceof ParameterizedType){
					 actualTypeArguments = ((ParameterizedType) genericSuperclass)
							.getActualTypeArguments();
					}
			}
			if(actualTypeArguments==null || actualTypeArguments.length==0){
				return null;
			}
			@SuppressWarnings("unchecked")
			Class<E> type = (Class<E>) actualTypeArguments[0];
			
			this.entityClass = type;
		}
		return this.entityClass;
	}
	
	/**
	 * 从实体类的注解获取表名
	 * @return
	 */
	protected String getTableName(){
		if(null == tableName){
			Table table = getEntityClass().getAnnotation(Table.class);
			this.tableName = table.name();
		}
		return this.tableName;
	}
	
	//-----------------------------------------------------------------------------------
	
	/**
	 * 实体类类信息（反射获取）
	 * @author Bob
	 *
	 */
	public static class EntityInfo {
		
		private String className;
		private List<FieldInfo> fieldsInfo = new ArrayList<FieldInfo>();
		
		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public List<FieldInfo> getFieldsInfo() {
			return fieldsInfo;
		}

		public void setFieldsInfo(List<FieldInfo> fieldsInfo) {
			this.fieldsInfo = fieldsInfo;
		}
	}
	
	/**
	 * 实体类类属性信息（附带Column注释的信息）
	 * @author Bob
	 *
	 */
	public static class FieldInfo{
		
		private Field field;
		private Column column;
		public FieldInfo(Field field, Column column) {
			super();
			this.field = field;
			this.column = column;
		}
		public Field getField() {
			return field;
		}
		public void setField(Field field) {
			this.field = field;
		}
		public Column getColumn() {
			return column;
		}
		public void setColumn(Column column) {
			this.column = column;
		}
	}

	/**
	 * 缓存反射获得的EntityValue信息，key为Entity类名
	 * @author Bob
	 *
	 */
	public static class EntityInfoCache {
		
		private static ConcurrentHashMap<String, EntityInfo> entityInfoCache = new ConcurrentHashMap<String, EntityInfo>();
		
		/**
		 * 获取表实体信息
		 * 为提高并发性能，更换容器为ConcurrentHashMap，并取消了synchronized定义，实际需要测试
		 * @param className
		 * @param clazz
		 * @return
		 */
		public static EntityInfo getEntityInfo(String className, Class<?> clazz){
			if(entityInfoCache.containsKey(className)){
				return entityInfoCache.get(className);
			}else{
				setEntityInfo(className, clazz);
				return entityInfoCache.get(className);
			}
		}
		
		public static EntityInfo getEntityInfo(String className){
			return entityInfoCache.get(className);
		}
		
		public static void setEntityInfo(String className, Class<?> clazz){
			EntityInfo entityInfo = new EntityInfo();
			entityInfo.setClassName(className);
			
			Field[] fields = clazz.getDeclaredFields();
			if(null != fields){
				for(Field field : fields)
				{ 	
					Column annoColumn = field.getAnnotation(Column.class);
					FieldInfo fieldInfo = new FieldInfo(field, annoColumn);
					entityInfo.getFieldsInfo().add(fieldInfo);
				}
			}
			
			entityInfoCache.putIfAbsent(className, entityInfo);
		}
		
	}
}
