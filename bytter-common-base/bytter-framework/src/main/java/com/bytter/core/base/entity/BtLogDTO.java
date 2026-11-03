package com.bytter.core.base.entity;

import java.util.Date;
import lombok.Data;

@Data
public class BtLogDTO extends IdEntity implements java.io.Serializable {

	private Short loglevel;
	private Date operatetime;
	private Short operatetype;
	private String logcontent;
	private String broswer;//用户浏览器类型
	private String note;
	private String userId;
	private String tenantId;
}
