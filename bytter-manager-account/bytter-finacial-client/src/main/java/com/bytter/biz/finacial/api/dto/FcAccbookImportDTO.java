package com.bytter.biz.finacial.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
public class FcAccbookImportDTO extends ExcelModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 记账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @BtCompositeSearch(fieldName = "tally_date")
    private Date tallyDate;
    /**
     * 起息日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date interestStartDate;
    /**
     * 摘要
     */
    private String summaryContent;
    /**
     * 序号
     */
    private BigDecimal lineNo;
    /**
     * 内部账号
     **/
    private String account;
    /**
     * 借方发生额
     */
    private BigDecimal debitMoney;
    /**
     * 贷方发生额
     */
    private BigDecimal creditMoney;
    /**
     * 业务品种
     **/
    private String busRaceId;
    /**
     * 凭证类型
     **/
    private String voucherType;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 附件张数
     */
    private Integer affixAmount;
    /**
     * 业务全局ID
     **/
    private String busGlobalId;
    /**
     * 操作人代码
     **/
    private String operatorCode;
    /**
     * 红蓝标志
     **/
    private String RedblueSign;
    /**
     * 录入日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enrolTime;
    /**
     * 是否加急
     **/
    private String emergencySign;
    /**
     * 凭证状态
     **/
    private String voucherState;
    /**
     * 产生方式
     **/
    private String generateMode;
    /**
     * 创建时间
     **/
    private Date createDate;
    private List<FcAccbookImportDTO> list;
    /**
     * 凭证种类
     **/
    private String voucherName;
    /**
     * 凭证种类
     **/
    private String raceName;
    /**
     * 对方名称
     */
    private String externalCorp;
    /**
     * 对方账号
     */
    private String externalAcc;
    /**
     * 对方账号开户行
     */
    private String externalBank;
}
