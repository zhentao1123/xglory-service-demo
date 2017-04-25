package cn.xglory.service.common.dao.entitydao.classbuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import cn.xglory.service.common.dao.entitydao.classbuilder.bean.ColumnInfo;
import cn.xglory.service.common.dao.entitydao.classbuilder.bean.TableInfo;

public class SqlHelper {
	/*
	private String DRIVER = ConfigReader.readProperty("jdbc.driver");
	private String CONN_URL = ConfigReader.readProperty("jdbc.url") ;
	private String USERNAME = ConfigReader.readProperty("jdbc.username");
	private String PASSWORD = ConfigReader.readProperty("jdbc.password");
	private String DB_NAME = ConfigReader.readProperty("db.name");
	*/
	static final String DRIVER = "org.gjt.mm.mysql.Driver";
	static final String CONN_URL = "jdbc:mysql://localhost:3306/db1?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&maxReconnects=2";
	static final String USERNAME = "root";
	static final String PASSWORD = "123456";
	static final String DB_NAME = "db1";
	static final String STR_ENCODE = "UTF-8";
	
	private Connection conn = null;
	
	public SqlHelper(){
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Connection openConn() throws SQLException{
		if(null==conn){
			conn = DriverManager.getConnection(CONN_URL,USERNAME,PASSWORD);
		}
		return conn;
	}
	
	private void closeConn() throws SQLException{
		if(null!=conn && !conn.isClosed()){
			conn.close();
			conn=null;
		}
	}
	
	public List<String> readTableNames() throws SQLException{
		List<String> tableNames = new ArrayList<String>();
		Connection conn = openConn();
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("show table status from " + DB_NAME);

		while (rs.next()) {
			String tableName = rs.getString("Name");
			tableNames.add(tableName);
//		    System.out.println(tableName);
		}
		
		rs.close();
		stmt.close();
		closeConn();
		return tableNames;
	}
	
	public TableInfo getTableInfo(String tableName) throws SQLException
	{
		TableInfo tableInfo = new TableInfo();
		tableInfo.setName(tableName);
		List<ColumnInfo> tableFieldList = new ArrayList<ColumnInfo>();
		
		Connection conn = openConn();
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery("select * from `" + tableName + "` where 1=2");
		ResultSetMetaData rsmd = rs.getMetaData();
		ResultSet rs1 = stmt.executeQuery("show full columns from " + tableName);
		
		int i=0;
		while (rs1.next()) {
			i++;
			ColumnInfo columnInfo = new ColumnInfo();
			
			columnInfo.setIndex(i);
			columnInfo.setField(rs1.getString("Field"));
			String type = rs1.getString("Type");
			System.out.println(type);
			if(type.contains("(")){
				columnInfo.setType(type.substring(0, type.indexOf("(")));
				columnInfo.setLength(type.substring(type.indexOf("(")+1, type.indexOf(")")));
			}
			String comment = rs1.getString("Comment");
			try {
				comment = new String(comment.getBytes(STR_ENCODE));
			} catch (UnsupportedEncodingException e) {}
			columnInfo.setComment(comment);
			columnInfo.setJavaClass(rsmd.getColumnClassName(i));
			columnInfo.setPk("PRI".equals(rs1.getString("Key")));
			columnInfo.setrequire(!"YES".equals(rs1.getString("Null")));
			
			tableFieldList.add(columnInfo);
			
			if("PRI".equals(rs1.getString("Key"))){
				tableInfo.setPk(columnInfo);
			}
		}
		tableInfo.setColumns(tableFieldList);
		
		rs.close();
		rs1.close();
		closeConn();
		stmt.close();
		return tableInfo;
	}
	
	public static class ConfigReader {
		
		private static ConfigReader instance = null;
		
		private HashMap<String, String> propMap = new HashMap<String, String>();
		
		private ConfigReader(){};
		
		private static ConfigReader getInstance(){
			if(instance==null){
				instance = new ConfigReader();
				instance.readConfig();
			}
			return instance;
		}
		
		public void readConfig(){
			Properties prop = new Properties();
			InputStream in = getClass().getResourceAsStream("/config.properties");
			propMap = new HashMap<String, String>();
			try {
				prop.load(in);
				Set keyValue = prop.keySet();
				for (Iterator it = keyValue.iterator(); it.hasNext();){
					String key = (String) it.next();
					String value = prop.getProperty(key);
//					System.out.println(key+":"+value);
					propMap.put(key, value);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Read Config Error");
			}
		}
		
		public static String readProperty(String key){
			return getInstance().propMap.get(key);
		}
		
	}
	
	public static void main(String[] args){
		try {
			TableInfo tableInfo = new SqlHelper().getTableInfo("user");
			System.out.println(tableInfo.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

