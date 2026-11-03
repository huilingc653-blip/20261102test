package com.bytter.system.vo;
import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.BankTypes;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author huangx
 * @version 1.0
 * @Title: 离线流水智能导入配置表VO
 * @Description: BIS_ACC_DTL_IMPORT_CONFIG
 * @date 2020-07-31 15:02:30
 */
public class BisAccDtlImportConfigVO implements java.io.Serializable {
    /**
     * ID
     */
    private String id;
    /**
     * 银行类型ID
     */
    @BankTypes(
            {
                    @BankType(inject="bankTypeName"),
            }
    )
    @BtCompositeSearch(fieldName = "b.bank_Type_Id")
    private String bankTypeId;
    private String bankTypeName;
    /**
     * 文件类型
     */
    @BtCompositeSearch(fieldName = "b.file_Type")
    @Dict(dictGroup = "RAS_IMPORT_FILE_DICT", inject = "fileTypeName")
    private String fileType;
    private String fileTypeName;
    /**
     * 明细读取起始行
     */
    private Integer dtlStartLine;
    /**
     * 明细读取截止内容
     */
    private String readEndContent;
    /**
     * 明细分隔符
     */
    private String dtlSeparator;
    /**
     * 收支标志
     */
    private String cdSign;
    /**
     * 日期所在列
     */
    private String dateRow;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**详细配置信息类**/
    private List<BisAccDtlImportConfigdtlVO> dtlList = new ArrayList<BisAccDtlImportConfigdtlVO>();
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
    /** 详细表信息ID**/
    private String ricdId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public String getFileTypeName() {
        return fileTypeName;
    }
    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }
    public Integer getDtlStartLine() {
        return dtlStartLine;
    }
    public void setDtlStartLine(Integer dtlStartLine) {
        this.dtlStartLine = dtlStartLine;
    }
    public String getReadEndContent() {
        return readEndContent;
    }
    public void setReadEndContent(String readEndContent) {
        this.readEndContent = readEndContent;
    }
    public String getDtlSeparator() {
        return dtlSeparator;
    }
    public void setDtlSeparator(String dtlSeparator) {
        this.dtlSeparator = dtlSeparator;
    }
    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }
    public String getDateRow() {
        return dateRow;
    }
    public void setDateRow(String dateRow) {
        this.dateRow = dateRow;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public List<BisAccDtlImportConfigdtlVO> getDtlList() {
        return dtlList;
    }
    public void setDtlList(List<BisAccDtlImportConfigdtlVO> dtlList) {
        this.dtlList = dtlList;
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
    public String getRicdId() {
        return ricdId;
    }
    public void setRicdId(String ricdId) {
        this.ricdId = ricdId;
    }
}
