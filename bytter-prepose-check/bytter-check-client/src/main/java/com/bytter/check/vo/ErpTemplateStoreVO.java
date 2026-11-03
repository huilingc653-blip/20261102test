package com.bytter.check.vo;
import com.bytter.core.base.entity.IdEntity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
/**
 * Created by zhouj on 2018/5/18.
 */
@Data
public class ErpTemplateStoreVO extends IdEntity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3799764902895210014L;
	
	private String dataId;
    private String templateId;
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
    private String templateName;
    private String typeName;
   
}
