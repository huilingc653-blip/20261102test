package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import com.bytter.system.api.dto.FcAccDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author zhaohc
 */
@Data
public class FcAgreedDepositVO implements Serializable, IBusinessKey {
    private static final long serialVersionUID = 2313478363314999846L;
    private String id;
    /**资金池id*/
    // @CorpPool(inject="netName")
    @BtCompositeSearch(fieldName = "d.net_id")
    private String netId;
    @BtCompositeSearch(fieldName = "d.net_name")
    private String netName;
    /**单位IDid*/
    // @Corp(inject="corpName")
    @BtCompositeSearch(fieldName = "d.corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName = "d.corp_name")
    private String corpName;
    /**账号id*/
    private String accId;
    /**币别*/
    @BtCompositeSearch(fieldName = "d.cur_id")
    // @Currency(inject = "curName")
    private String curId;
    @BtCompositeSearch(fieldName = "d.cur_name")
    private String curName;
    /**账号*/
    @BtCompositeSearch(fieldName = "d.blnctr_acc")
    private String blnctrAcc;
    @BtCompositeSearch(fieldName = "d.acc_name")
    private String accName;
    /**开始日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "d.begin_date")
    private Date beginDate;
    /**结束日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "d.end_date")
    private Date endDate;
    /**合同号*/
    @BtCompositeSearch(fieldName = "d.contract_no")
    private String contractNo;
    /**状态*/
    @BtCompositeSearch(fieldName = "d.status")
    @Dict(dictGroup = "agreed.status",inject = "statusName")
    private Integer status;
    private String statusName;
    /**单据状态*/
    private Integer billStatus;
    /**单据单据类型*/
    @BtCompositeSearch(fieldName = "d.bus_type")
    private String busType;
    /**延期申请前的登记ID*/
    private String agreedDepositOriId;
    /**终止日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "d.stop_date")
    private Date stopDate;
    /**复核人登录名称*/
    private String checkerBy;
    /**复核时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;
    /**资金池账户标识*/
    private String poolSign;
    /**创建日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "d.create_date")
    private Date createDate;
    /**创建人登录名称*/
    @BtCompositeSearch(fieldName = "d.create_by")
    private String createBy;
    private String userName;
    /**更新日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**更新人登录名称*/
    private String updateBy;
    /**旧开始日期(协定延期专用)*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date oldBeginDate;
    /**旧结束日期(协定延期专用)*/
    @BtCompositeSearch(fieldName = "d.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date oldEndDate;
    /**操作类型*/
    private String opType;
    private List<FcAgreedDetail> detailList = new ArrayList<>();
    /**打回原因*/
    private String checkBackReason;
    /**申请编号*/
    @BtCompositeSearch(fieldName = "d.bill_no")
    private String billNo;
    /**到期处理方式*/
    @BtCompositeSearch(fieldName = "d.certificate_way")
    private String certificateWay;
    /**是否存入协定账号*/
    private String isAgreedDepositAcc;
    /**协定利率*/
    private BigDecimal agrInterest;
    /**协定账号ID*/
    private String agrAccId;
    /**协定账号*/
    private String agrAcc;
    /**活期账号留存金额*/
    private BigDecimal retainedBal;

    private FcAccDTO dto;
    @Override
    public String getBusinessKey() {
        return getId();
    }
}
