package com.bytter.credit.domain.vm;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.List;
@Data
public class RatBkRationVm extends BaseBkRationVm {
    /**
     * 敞口授信
     */
    private String openSign;
    /**
     * 授信模式
     */
    private String ratMode;
    /**
     * 授信币种
     */
    private String curCode;
    /**
     * 授信开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date begindate;
    /**
     * 授信结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date enddate;
    /**
     * 授信金额
     */
    private java.math.BigDecimal money;
    /**
     * 敞口额度
     */
    private java.math.BigDecimal openMoney;
    /**
     * 授信金额 合计
     */
    private java.math.BigDecimal moneysum;
    /**
     * 敞口金额 合计
     */
    private java.math.BigDecimal openMoneysum;
    /**
     * 额度类型
     */
    private String type;
    /**
     * 银行类别名称
     */
    private String bankTypeName;
    /**
     * 通过时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date passDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 授信合同号
     */
    private String contractNo;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 额度是否包含保证金
     */
    private String includeBailmoney;
    /**
     * 是否挂接保证金
     */
    private String isHookBond;
    /**
     * 担保类型
     */
    private String guaType;
    /**
     * 担保单位
     */
    private String guaCorpId;
    /**
     * 担保合同
     */
    private String guaContract;
    /**
     * 授信录入明细数据
     */
    private List<RatBkRationDtlVm> rationDtls;
    /**
     * 单品种条数
     */
    private Integer singItemSize;
    /**
     * 综合品种条数
     */
    private Integer compItemSize;
    /**
     * 总品种数
     */
    private Integer itemSize;
    private String resultFlag;// 失败信息提示标识
    /**
     * 更新授信标识
     */
    private boolean updateSign = false;
    // 以下是挂接保证金的信息
    /**
     * 保证金Id
     */
    private String bondId;
    /**
     * 银行Id
     */
    private String bankId;
    /**
     * 保证金合同号
     */
    private String bondContract;
    /**
     * 保证金金额
     */
    private java.math.BigDecimal applyMoney;
    /**
     * 保证金比例
     */
    private java.math.BigDecimal bondPercent;
    /**
     * 修改前挂接保证金Id
     */
    private String bondIdPre;
    /**
     * 是否挂接担保
     */
    private String isHangGuarantee;
    /**
     * 附件路径
     */
    private String attachfilepath;
    /**
     * 合同附件
     */
    private String contractAttachfilepath;
    /**
     * 担保备注
     **/
    private String guaranteeRmk;
    /**
     * 担保方式
     **/
    private String guaWay;
    /**
     * 担保合同id
     */
    private List<String> contractIds;
    /**
     * 挂接的合同ID
     */
    private String conId;
    /**
     * 合同类型ID
     **/
    private String conTypeId;
    /**
     * 结转授信编号(存储为id)
     **/
    private String oldRatCode;
    /**
     * 授信合同是否必须
     */
    private String creditContractRequired;
    /**
     * 担保开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date guaranteeBeginDate;
    /**
     * 担保结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date guaranteeEndDate;
}
