package com.bytter.system.vo;
import com.bytter.poi.handler.ExcelModel;
import java.io.Serializable;
/**
 * @author huangx
 * @version 1.0
 * @Title: 离线流水智能导入配置表VO
 * @Description: BIS_ACC_DTL_IMPORT_CONFIGDTL
 * @date 2020-07-31 15:02:30
 */
public class BisAccDtlImportConfigdtlVO extends ExcelModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7657353377247096295L;
    /** 导入格式主表ID */
    private String icId;
    /**
     * 对应字段
     * 本方账号bankAcc  对方账号oppAccNo  对方账户名oppAccName  对方开户行oppAccBank
     * 支出金额payMoney 收入金额incomeMoney  币别curId 交易日期transDate 交易时间transTime
     * 余额bal 摘要remark  用途uses 项目信息projectRemark
     */
    private String fieldKey;
    /** 对应起始值 */
    private String startVal;
    /** 对应结束值 */
    private String endVal;
    public String getIcId() {
        return icId;
    }
    public void setIcId(String icId) {
        this.icId = icId;
    }
    public String getFieldKey() {
        return fieldKey;
    }
    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }
    public String getStartVal() {
        return startVal;
    }
    public void setStartVal(String startVal) {
        this.startVal = startVal;
    }
    public String getEndVal() {
        return endVal;
    }
    public void setEndVal(String endVal) {
        this.endVal = endVal;
    }
    @Override
    public String toString() {
        return "BisAccDtlImportConfigDetailVO{" +
                "icId='" + icId + '\'' +
                ", fieldKey='" + fieldKey + '\'' +
                ", startVal='" + startVal + '\'' +
                ", endVal='" + endVal + '\'' +
                '}';
    }
    public BisAccDtlImportConfigdtlVO(){
    }
}
