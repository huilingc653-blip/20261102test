package com.bytter.biz.inneraccount.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author huangx
 * @version 1.0
 * @Title: 离线流水智能导入配置表Entity
 * @Description: BIS_ACC_DTL_IMPORT_CONFIG
 * @date 2020-07-31 15:02:30
 */
@Entity
@Table(name = "BIS_ACC_DTL_IMPORT_CONFIG")
public class BisAccDtlImportConfig extends IdEntity implements java.io.Serializable {
    /**
     * 银行类型ID
     */
    private String bankTypeId;
    /**
     * 文件类型
     */
    private String fileType;
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
    @Column(name = "BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    @Column(name = "FILE_TYPE")
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    @Column(name = "DTL_START_LINE")
    public Integer getDtlStartLine() {
        return dtlStartLine;
    }
    public void setDtlStartLine(Integer dtlStartLine) {
        this.dtlStartLine = dtlStartLine;
    }
    @Column(name = "READ_END_CONTENT")
    public String getReadEndContent() {
        return readEndContent;
    }
    public void setReadEndContent(String readEndContent) {
        this.readEndContent = readEndContent;
    }
    @Column(name = "DTL_SEPARATOR")
    public String getDtlSeparator() {
        return dtlSeparator;
    }
    public void setDtlSeparator(String dtlSeparator) {
        this.dtlSeparator = dtlSeparator;
    }
    @Column(name = "CD_SIGN")
    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }
    @Column(name = "DATE_ROW")
    public String getDateRow() {
        return dateRow;
    }
    public void setDateRow(String dateRow) {
        this.dateRow = dateRow;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
