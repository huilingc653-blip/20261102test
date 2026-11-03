package com.bytter.tally.vo;
import java.util.Map;
public class InsertSqlBean implements java.io.Serializable{
	private StringBuilder insertSql;
	private Map<String,Object> params;
	
	public StringBuilder getInsertSql() {
		return insertSql;
	}
	public void setInsertSql(StringBuilder insertSql) {
		this.insertSql = insertSql;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
}
