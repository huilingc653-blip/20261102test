package com.bytter.system.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.Region;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelCollection;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.bytter.system.entity.ExternalCorpBankAcc;
import com.bytter.system.entity.ExternalCorpQuickPay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * 客商资料VO
 *
 * @author zhoul on 2017/6/22.
 * last update by lifh 添加“是否国内”属性，is_native
 */
@ApiModel("客商资料VO")
@ExcelTarget("ExternalCorpVO")
public class ExternalCorpVO extends ExcelModel {
    @ApiModelProperty("主键ID")
    private String id;

    /*客户名称*/
    @ApiModelProperty(value = "*客户名称", required = true)
    @Excel(name = "*客户名称")
    @NotNull(message = "sys.externalCorp.message.name.null")
    @BtCompositeSearch(fieldName = "ec.name")
    private String name;


    /**
     * 客商名称（英文）
     */
    private String nameEn;


    /*客户简称*/
    @ApiModelProperty(value = "*客户简称", required = true)
    @Excel(name = "*客户简称")
    @NotNull(message = "sys.externalCorp.message.abbreviate.null")
    @BtCompositeSearch(fieldName = "ec.abbreviate")
    private String abbreviate;

    @ApiModelProperty("所属单位ID")
    @BtCompositeSearch(fieldName = "ec.corp_id")
    private String corpId;

    @ApiModelProperty("所属单位名称")
    private String corpName;

    @ApiModelProperty("所属单位编码")
    private String corpCode;

    @ApiModelProperty("省份编码")
    @Region(inject = "provName")
    private String province;

    @ApiModelProperty(value = "*所在省", required = true)
    @Excel(name = "*所在省")
    @NotNull(message = "sys.externalCorp.message.provName.null")
    private String provName;

    @ApiModelProperty("城市编码")
    @Region(inject = "cityName")
    private String city;

    @ApiModelProperty(value = "*所在市", required = true)
    @Excel(name = "*所在市")
    @NotNull(message = "sys.externalCorp.message.cityName.null")
    private String cityName;

    @ApiModelProperty("详细地址")
    @Excel(name = "地址")
    private String address;

    @ApiModelProperty("联系人")
    @Excel(name = "联系人")
    private String linkMan;

    @ApiModelProperty("联系电话")
    @Excel(name = "联系电话")
    private String tel;

    @ApiModelProperty("传真")
    @Excel(name = "传真")
    private String fax;

    @ApiModelProperty("电子邮箱")
    @Excel(name = "email")
    private String email;

    /*数据来源*/
    @ApiModelProperty(value = "*数据来源", required = true)
    @Excel(name = "*数据来源")
    @NotNull(message = "sys.externalCorp.message.sourceSystem.null")
    @BtCompositeSearch(fieldName = "ec.source_System")
    private String sourceSystem;

    @ApiModelProperty("是否有效（字符串显示）")
    @Excel(name = "是否有效")
    private String statusString;

    @ApiModelProperty("是否有效（0:无效, 1:有效）")
    @BtCompositeSearch(fieldName = "ec.status")
    private Integer status;

    /**
     * 审核状态审核状态：0:暂存，11:待审核，95:审核通过，-1:打回
     */
    @ApiModelProperty("审核状态：0:暂存，11:待审核，95:审核通过，-1:打回")
    @Dict(dictGroup = "externalCorp.auditStatus", inject = "auditStatusName")
    @BtCompositeSearch(fieldName = "ec.audit_status")
    private Integer auditStatus;

    @ApiModelProperty("审核状态名称")

    private String auditStatusName;

    @ApiModelProperty("省份全称")
    private String provinceFexName;

    @ApiModelProperty("城市全称")
    private String cityFexName;

    /**
     * 审批信息
     */
    @ApiModelProperty("审批信息")
    private String auditMessage;

    /*客户编号*/
    @ApiModelProperty(value = "*客户编号", required = true)
    @Excel(name = "*客户编号")
    @NotNull(message = "sys.externalCorp.message.code.null")
    @BtCompositeSearch(fieldName = "ec.code")
    private String code;

    /*是否共享*/
    @ApiModelProperty("是否共享（0:否, 1:是）")
    @Dict(dictGroup = "sf_yn", inject = "isCommonStr")
    @BtCompositeSearch(fieldName = "ec.is_Common")
    private String isCommon;

    @ApiModelProperty("是否共享（字符串显示）")
    @BtCompositeSearch(fieldName = "ec.is_Common")
    private String isCommonStr;

    /*是否国内*/
    @ApiModelProperty("是否国内（0:否, 1:是）")
    @Dict(dictGroup = "sf_yn", inject = "isNativeStr")
    @BtCompositeSearch(fieldName = "ec.is_Native")
    private Integer isNative;

    @ApiModelProperty("是否国内（字符串显示）")
    private String isNativeStr;

    /**
     * 客商类型(0-客户,1-供应商,2-客户/供应商)
     */
    @ApiModelProperty("客商类型：0-客户,1-供应商,2-客户/供应商")
    @Dict(dictGroup = "external.type", inject = "externalTypeStr")
    @BtCompositeSearch(fieldName = "ec.external_type")
    private String externalType;

    @ApiModelProperty("客商类型名称")
    private String externalTypeStr;

    /**
     * 供应商ID
     */
    @ApiModelProperty("供应商ID")
    @BtCompositeSearch(fieldName = "ec.supplier_id")
    private String supplierId;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    @BtCompositeSearch(fieldName = "ec.supplier_id")
    private String supplierName;

    /**
     * 附件ID
     */
    @ApiModelProperty("附件ID")
    @BtCompositeSearch(fieldName = "ec.attachment_id")
    private String attachmentId;

    @ApiModelProperty("银行账户列表")
    private List<ExternalCorpBankAcc> externalAcc;

    @ApiModelProperty("银行账户VO列表")
    @ExcelCollection(name = "externalAccVO")
    private List<ExternalCorpBankAccVO> externalAccVO;

    /**
     * 快捷支付信息集合
     */
    @ApiModelProperty("快捷支付信息集合")
    private List<ExternalCorpQuickPay> quickPayList;

    @ApiModelProperty("提交人")
    private String commitBy;

    /**
     * 是否只是页面查看 1位是0和空为否
     */
    @ApiModelProperty("是否只是页面查看：1-是，0或空-否")
    private String isView;

    /**
     * 操作类型：1客商页面2单位页面
     */
    @ApiModelProperty("操作类型：1-客商页面，2-单位页面")
    private String opType;

    /**
     * 默认选中的单位ID多个用,隔开
     */
    @ApiModelProperty("默认选中的单位ID（多个用逗号隔开）")
    private String corpIdVal;

    /**
     * 默认选中的客商ID多个用,隔开
     */
    @ApiModelProperty("默认选中的客商ID（多个用逗号隔开）")
    private String externalIdVal;

    /**
     * 客商属性 00一般客商01黑名单02白名单
     */
    @ApiModelProperty("客商属性：00-一般客商，01-黑名单，02-白名单")
    @Dict(dictGroup = "SYS_BUS_ENTITY_TYPE", inject = "bwTypeName")
    private String bwType;

    /**
     * 客商属性名称
     */
    @ApiModelProperty("客商属性名称")
    @Excel(name = "客商属性")
    @BtCompositeSearch(fieldName = "ec.bw_type")
    private String bwTypeName;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
    @Excel(name = "统一社会信用代码")
    @BtCompositeSearch(fieldName="ec.soc_code")
    private String socCode;

    /**
     * 地址（英文）
     */
    @ApiModelProperty("地址（英文）")
    private String addressEn;

    /**
     * 地址1
     */
    @ApiModelProperty("地址1")
    private String address1;

    /**
     * 地址2
     */
    @ApiModelProperty("地址2")
    private String address2;

    /**
     * 所在省/州
     */
    @ApiModelProperty("所在省/州")
    private String provinceState;

    /**
     * 邮编
     */
    @ApiModelProperty("邮编")
    private String zipCode;

    /**
     * 资金收付产品 名称
     */
    @ApiModelProperty("资金收付产品名称")
    private String paymentProducts;

    /**
     * 资金收付产品名称
     */
    @ApiModelProperty("资金收付产品名称")
    private String proName;

    /**
     * 资金收付产品id
     */
    @ApiModelProperty("资金收付产品ID")
    private String proId;

    public String getSocCode() {
        return socCode;
    }

    public void setSocCode(String socCode) {
        this.socCode = socCode;
    }

    public String getBwType() {
        return bwType;
    }
    public void setBwType(String bwType) {
        this.bwType = bwType;
    }
    public String getBwTypeName() {
        return bwTypeName;
    }
    public void setBwTypeName(String bwTypeName) {
        this.bwTypeName = bwTypeName;
    }
    public String getOpType() {
        return opType;
    }
    public void setOpType(String opType) {
        this.opType = opType;
    }
    public String getIsView() {
        return isView;
    }
    public void setIsView(String isView) {
        this.isView = isView;
    }
    public String getCorpIdVal() {
        return corpIdVal;
    }
    public void setCorpIdVal(String corpIdVal) {
        this.corpIdVal = corpIdVal;
    }
    public String getExternalIdVal() {
        return externalIdVal;
    }
    public void setExternalIdVal(String externalIdVal) {
        this.externalIdVal = externalIdVal;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAbbreviate() {
        return abbreviate;
    }
    public void setAbbreviate(String abbreviate) {
        this.abbreviate = abbreviate;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getLinkMan() {
        return linkMan;
    }
    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSourceSystem() {
        return sourceSystem;
    }
    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getAuditStatus() {
        return auditStatus;
    }
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getAuditStatusName() {
        return auditStatusName;
    }
    public void setAuditStatusName(String auditStatusName) {
        this.auditStatusName = auditStatusName;
    }
    public String getAuditMessage() {
        return auditMessage;
    }
    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public List<ExternalCorpBankAcc> getExternalAcc() {
        return externalAcc;
    }
    public void setExternalAcc(List<ExternalCorpBankAcc> externalAcc) {
        this.externalAcc = externalAcc;
    }
    public List<ExternalCorpBankAccVO> getExternalAccVO() {
        return externalAccVO;
    }
    public void setExternalAccVO(List<ExternalCorpBankAccVO> externalAccVO) {
        this.externalAccVO = externalAccVO;
    }

    public List<ExternalCorpQuickPay> getQuickPayList() {
        return quickPayList;
    }

    public void setQuickPayList(List<ExternalCorpQuickPay> quickPayList) {
        this.quickPayList = quickPayList;
    }
    public String getStatusString() {
        return statusString;
    }
    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }
    public String getIsCommon() {
        return isCommon;
    }
    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }
    public String getIsCommonStr() {
        return isCommonStr;
    }
    public void setIsCommonStr(String isCommonStr) {
        this.isCommonStr = isCommonStr;
    }
    public Integer getIsNative() {
        return isNative;
    }
    public void setIsNative(Integer isNative) {
        this.isNative = isNative;
    }
    public String getIsNativeStr() {
        return isNativeStr;
    }
    public void setIsNativeStr(String isNativeStr) {
        this.isNativeStr = isNativeStr;
    }
    public String getExternalType() {
        return externalType;
    }
    public void setExternalType(String externalType) {
        this.externalType = externalType;
    }
    public String getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    @Override
    public String getBeanName() {
        return "externalCorpService";
    }
    public String getCommitBy() {
        return commitBy;
    }
    public void setCommitBy(String commitBy) {
        this.commitBy = commitBy;
    }
    public String getExternalTypeStr() {
        return externalTypeStr;
    }
    public void setExternalTypeStr(String externalTypeStr) {
        this.externalTypeStr = externalTypeStr;
    }
    public String getProvinceFexName() {
        return provinceFexName;
    }
    public void setProvinceFexName(String provinceFexName) {
        this.provinceFexName = provinceFexName;
    }
    public String getCityFexName() {
        return cityFexName;
    }
    public void setCityFexName(String cityFexName) {
        this.cityFexName = cityFexName;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPaymentProducts() {
        return paymentProducts;
    }

    public void setPaymentProducts(String paymentProducts) {
        this.paymentProducts = paymentProducts;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    @Override
    public String toString() {
        return "ExternalCorpVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", abbreviate='" + abbreviate + '\'' +
                ", externalTypeStr='" + externalTypeStr + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", province='" + province + '\'' +
                ", provName='" + provName + '\'' +
                ", city='" + city + '\'' +
                ", cityName='" + cityName + '\'' +
                ", address='" + address + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", sourceSystem='" + sourceSystem + '\'' +
                ", statusString='" + statusString + '\'' +
                ", status=" + status +
                ", auditStatus=" + auditStatus +
                ", code='" + code + '\'' +
                ", isCommon='" + isCommon + '\'' +
                ", externalAcc=" + externalAcc +
                ", externalAccVO=" + externalAccVO +
                '}';
    }
}
