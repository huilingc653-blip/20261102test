package com.bytter.check.vo;

import java.sql.Timestamp;
import com.bytter.core.base.entity.IdEntity;
import lombok.Data;
/**
 * Created by zhouj on 2018/5/18.
 */
@Data
public class ErpTemplateVO extends IdEntity implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4439036864176853704L;
	
	private String templateTypeId;
    private String templateCode;
    private String templateName;
    private String rmk;
    private Timestamp opDate;
    private String voucherTypeCode;
    private String referCode;
    private String voucherPreText;
    private String itemCode;
    private String refer;
    private String abs;
    private String purpose;
    private String payType;
    private String reverse1;
    private String reverse2;
}
