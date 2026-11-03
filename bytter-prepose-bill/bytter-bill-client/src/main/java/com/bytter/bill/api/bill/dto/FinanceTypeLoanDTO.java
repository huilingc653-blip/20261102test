package com.bytter.bill.api.bill.dto;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
/**
* <br>
* <b>功能：</b> 融资类别余额数据DTO <br>
* <b>作者：</b>liyh<br>
* <b>日期：</b> 2020/12/8 0008 <br>
*/
@Data
public class FinanceTypeLoanDTO implements Serializable {
    /**业务模块*/
    private String sysCode;
    /**
     * 融资品种编号
     * */
    private String typeCode;
    /**
     * 融资品种
     * */
    private String typeName;
    /**保证金金额*/
    private BigDecimal bondMoney;
    /**业务金额*/
    private BigDecimal money;
    /**敞口金额*/
    private BigDecimal openMoney;
}
