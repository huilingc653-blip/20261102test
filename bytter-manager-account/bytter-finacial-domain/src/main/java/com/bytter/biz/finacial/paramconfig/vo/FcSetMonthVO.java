package com.bytter.biz.finacial.paramconfig.vo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.bytter.biz.finacial.paramconfig.entity.FcSetMonth;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcSetMonthVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/****/
	private String id;
	
	private String status;
	private String corpName;
	private List<FcSetMonth> fsm = new ArrayList<FcSetMonth>();
	
	/***/
	/**所属网点*/
	private java.lang.String netId;
	/**月份*/
	private java.lang.String overMonth;
	/**开始日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date dateStart;
	/**结束日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date dateEnd;
	
	public FcSetMonthVO(){
	    super();
	}
	
	public FcSetMonthVO(String netId, String overMonth,Date dateStart,Date dateEnd,List<FcSetMonth> fsm){
	    this.netId = netId;
	    this.overMonth = overMonth;
	    this.dateStart = dateStart;
	    this.dateEnd = dateEnd;
	    this.fsm = fsm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getNetId(){
		return this.netId;
	}
	public void setNetId(java.lang.String netId){
		this.netId = netId;
	}
	public java.lang.String getOverMonth(){
		return this.overMonth;
	}
	public void setOverMonth(java.lang.String overMonth){
		this.overMonth = overMonth;
	}
	public java.util.Date getDateStart(){
		return this.dateStart;
	}
	public void setDateStart(java.util.Date dateStart){
		this.dateStart = dateStart;
	}
	public java.util.Date getDateEnd(){
		return this.dateEnd;
	}
	public void setDateEnd(java.util.Date dateEnd){
		this.dateEnd = dateEnd;
	}
	
	
	 public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	
	public List<FcSetMonth> getFsm() {
		return fsm;
	}
	public void setFsm(List<FcSetMonth> fsm) {
		this.fsm = fsm;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
