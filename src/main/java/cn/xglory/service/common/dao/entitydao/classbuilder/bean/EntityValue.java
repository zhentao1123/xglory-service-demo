package cn.xglory.service.common.dao.entitydao.classbuilder.bean;

import java.util.List;

public class EntityValue {
	private List<ColumnInfo> pk;
	private List<ColumnInfo> columnList;
	
	public List<ColumnInfo> getPk() {
		return pk;
	}
	public void setPk(List<ColumnInfo> pk) {
		this.pk = pk;
	}
	public List<ColumnInfo> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<ColumnInfo> columnList) {
		this.columnList = columnList;
	}
}
