package com.bytter.erp.dto;
import com.bytter.core.annotation.IgnoreField;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.erp.constant.AccountGrantConstant;
import java.io.Serializable;
import java.util.List;
/**
 * 银行账户搜索条件
 *
 * @author zuoy    2017/7/13
 */
public class AccountSearchDTO implements Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 账户
     */
    private String bankAcc;
    /**
     * 币别ID
     */
    private String curId;
    /**
     * 银行接口
     */
    private String bifCode;
    /**
     * 账户类型   1-内部账号  2-银行账号  3-离线账号
     */
    @IgnoreField
    private String accountType;
    /**
     * d-详情   b-余额查询   t-转账
     */
    @IgnoreField
    private String permission;
    /**
     * 账户属性
     */
    private String attributeId;
    /**
     * 是否仅查询具有子账户的银行账户信息
     */
    private Boolean existChilds;
    /**
     * 查询结果是否包含父账户信息
     */
    private Boolean containParent;
    /**
     * 去除账户重复项
     */
    private Boolean distinct;
    /**
     * 具有权限的账户ID
     */
    private List<String> accountGrantList;
    /**
     * 具有权限的单位ID
     */
    private List<String> corpGrantList;
    /**
     * 是否电票专户
     */
    private String electricBill;
    /**
     * 资源信息
     */
    @IgnoreField
    private String resource;
    /**
     * 是否展示内部账户余额信息
     */
    @IgnoreField
    private Boolean fcBal;
    /**
     * 是否上线
     */
    private String isOnline;
    @IgnoreField
    private String userId;
    /**
     * 稽核新增 是否虚拟账户(1=是 0=否)
     **/
    @IgnoreField
    private String isVirtualAcc;
    /**
     * 银行账号忽略授权
     */
    @IgnoreField
    private Boolean ignorePermission;
    /**
     * 单位是否必填
     */
    @IgnoreField
    private String isMustCorp;
    /**
     * 银行类型ID
     */
    private String bankTypeId;
    /**
     * 查询结果是否包含已注销账户
     */
    private Boolean containDelBankAcc;
    /**
     * 组件是否查询账户余额
     */
    private Boolean searchBal;
    public AccountSearchDTO() {
    }
    public AccountSearchDTO(boolean init) {
        if (init) {
            this.accountType = AccountGrantConstant.ONLINE_BANK_TYPE;
            this.permission = AccountGrantConstant.TRANSFER_ACCOUNT;
            this.existChilds = false;
            this.containParent = false;
            this.fcBal = false;
        }
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    public String getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }
    public Boolean getExistChilds() {
        return existChilds;
    }
    public void setExistChilds(Boolean existChilds) {
        this.existChilds = existChilds;
    }
    public Boolean getContainParent() {
        return containParent;
    }
    public void setContainParent(Boolean containParent) {
        this.containParent = containParent;
    }
    public List<String> getAccountGrantList() {
        return accountGrantList;
    }
    public List<String> getCorpGrantList() {
        return corpGrantList;
    }
    public void setCorpGrantList(List<String> corpGrantList) {
        this.corpGrantList = corpGrantList;
    }
    public Boolean getDistinct() {
        return distinct;
    }
    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }
    public void setAccountGrantList(List<String> accountGrantList) {
        this.accountGrantList = accountGrantList;
    }
    public String getAccountGrantListToString() {
        String ids = "";
        if (!EmptyUtils.isEmptyList(this.accountGrantList)) {
            for (String id : accountGrantList) {
                if (EmptyUtils.isBlank(ids)) {
                    ids += "'" + id + "'";
                } else {
                    ids += ",'" + id + "'";
                }
            }
        }
        return ids;
    }
    public String getElectricBill() {
        return electricBill;
    }
    public void setElectricBill(String electricBill) {
        this.electricBill = electricBill;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public Boolean getFcBal() {
        return fcBal;
    }
    public void setFcBal(Boolean fcBal) {
        this.fcBal = fcBal;
    }
    public String getIsOnline() {
        return isOnline;
    }
    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getIsVirtualAcc() {
        return isVirtualAcc;
    }
    public void setIsVirtualAcc(String isVirtualAcc) {
        this.isVirtualAcc = isVirtualAcc;
    }
    public Boolean getIgnorePermission() {
        return ignorePermission;
    }
    public void setIgnorePermission(Boolean ignorePermission) {
        this.ignorePermission = ignorePermission;
    }
    public String getIsMustCorp() {
        return isMustCorp;
    }
    public void setIsMustCorp(String isMustCorp) {
        this.isMustCorp = isMustCorp;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public Boolean getContainDelBankAcc() {
        return containDelBankAcc;
    }
    public void setContainDelBankAcc(Boolean containDelBankAcc) {
        this.containDelBankAcc = containDelBankAcc;
    }
    public Boolean getSearchBal() {
        return searchBal;
    }
    public void setSearchBal(Boolean searchBal) {
        this.searchBal = searchBal;
    }
    
}
