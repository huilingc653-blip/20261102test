package com.bytter.erp.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

public class DSLIntefaceFlagDTO implements Serializable {

		private String id;

	    private String source_id; //接口表id

	    private String return_message; //信息

	    private String return_code;  //信息代码

	    private String table_source; //数据来源
	    private String reverse1;
	    private String reverse2;
	    private String reverse3;
	    private String reverse4;
	    private String reverse5;//永远记录操作时间

	    private String type;//接口类型

	    /**
	     * 数据发送状态 F失败 S成功
	     */
	    private String sync_status;


	    private Date create_date; //日期

	    private Date op_time;//操作日期

	    /**
	     * 全量流水接口共享单号字段
	     */
	    private String SOURCE_NUM;

	    public String getId() {
	        return id;
	    }

	    public void setId(String id) {
	        this.id = id;
	    }

	    public Date getCreate_date() {
	        return create_date;
	    }

	    public void setCreate_date(Date create_date) {
	        this.create_date = create_date;
	    }

	    public String getSource_id() {
	        return source_id;
	    }

	    public void setSource_id(String source_id) {
	        this.source_id = source_id;
	    }

	    public String getReturn_message() {
	        return return_message;
	    }

	    public void setReturn_message(String return_message) {
	        this.return_message = return_message;
	    }

	    public String getReturn_code() {
	        return return_code;
	    }

	    public void setReturn_code(String return_code) {
	        this.return_code = return_code;
	    }

	    public String getTable_source() {
	        return table_source;
	    }

	    public void setTable_source(String table_source) {
	        this.table_source = table_source;
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

	    public String getSync_status() {
	        return sync_status;
	    }

	    public void setSync_status(String sync_status) {
	        this.sync_status = sync_status;
	    }

	    public String getReverse1() {
	        return reverse1;
	    }

	    public void setReverse1(String reverse1) {
	        this.reverse1 = reverse1;
	    }

	    public String getReverse2() {
	        return reverse2;
	    }

	    public void setReverse2(String reverse2) {
	        this.reverse2 = reverse2;
	    }

	    public String getReverse3() {
	        return reverse3;
	    }

	    public void setReverse3(String reverse3) {
	        this.reverse3 = reverse3;
	    }

	    public String getReverse4() {
	        return reverse4;
	    }

	    public void setReverse4(String reverse4) {
	        this.reverse4 = reverse4;
	    }

	    public String getReverse5() {
	        return reverse5;
	    }

	    public void setReverse5(String reverse5) {
	        this.reverse5 = reverse5;
	    }

	    public Date getOp_time() {
	        return op_time;
	    }

	    public void setOp_time(Date op_time) {
	        this.op_time = op_time;
	    }

	    public String getSOURCE_NUM() {
	        return SOURCE_NUM;
	    }

	    public void setSOURCE_NUM(String SOURCE_NUM) {
	        this.SOURCE_NUM = SOURCE_NUM;
	    }

	    public String toString() {
	        return "[id=" + id + ",source_id=" + source_id + ",sync_status=" + sync_status + ",return_code=" + return_code + ",return_message=" + return_message + ",table_source=" + table_source + ",op_time=" + op_time + "]";
	    }
}
