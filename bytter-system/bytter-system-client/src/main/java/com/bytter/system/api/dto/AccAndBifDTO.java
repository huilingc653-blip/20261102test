package com.bytter.system.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
/**
 * 银行账户
 *
 * @author zuoy    2017/7/13
 */
@Data
public class AccAndBifDTO implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 接口代码
     */
    private String bifCode;
    /**
        * 账户ID
     */
    private String accId; 
	/**
	  * 户名
     */
    private String accName;
    /**
          * 单位ID
     */
    private String corpId;
    /**
          * 开户行
     */
    private String bankName;
    /**
          * 接口代码名称
     */
    private String bisName;
    
    
    
    
   
}
