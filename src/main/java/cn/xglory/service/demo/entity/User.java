package cn.xglory.service.demo.entity;

import cn.xglory.service.common.dao.entitydao.BaseEntity;
import cn.xglory.service.common.dao.entitydao.annotation.Column;
import cn.xglory.service.common.dao.entitydao.annotation.Table;

import java.math.BigInteger;
import java.lang.String;
import java.lang.Integer;
import java.sql.Date;
import java.sql.Timestamp;

@Table(name="user")
public class User extends BaseEntity{

	@Column(comment="ID", field="id", pk=true, type="bigint", length="20", require=true)
	private BigInteger id;
	
	@Column(comment="å§“å", field="name", type="varchar", length="20")
	private String name;
	
	@Column(comment="å¹´é¾„", field="age", type="tinyint", length="4")
	private Integer age;
	
	@Column(comment="ç”Ÿæ—¥", field="birthday", type="", length="")
	private Date birthday;
	
	@Column(comment="åˆ›å»ºæ—¥æœŸ", field="sys_create_date", type="", length="")
	private Timestamp sys_create_date;
	
	public void setId(BigInteger id){
		this.id = id;
	}
	
	public BigInteger getId(){
		return this.id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setAge(Integer age){
		this.age = age;
	}
	
	public Integer getAge(){
		return this.age;
	}
	
	public void setBirthday(Date birthday){
		this.birthday = birthday;
	}
	
	public Date getBirthday(){
		return this.birthday;
	}
	
	public void setSys_create_date(Timestamp sys_create_date){
		this.sys_create_date = sys_create_date;
	}
	
	public Timestamp getSys_create_date(){
		return this.sys_create_date;
	}
	
}