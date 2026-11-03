package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.check.vo.IBaseDataInject;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class FexRateInfoTableVO implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 3650731181127660208L;
    /**ID*/
    private String id;
    /**外汇牌价查询指令*/
    private String rateExcId;
    /**外汇牌价类型*/
    @BtCompositeSearch(fieldName = "r.rate_type_id")
    private String rateTypeId;
    @Excel(name = "*外汇牌价类型")

    @NotNull( message = "fex.rateInfo.message.rateType.null")
    @BtCompositeSearch(fieldName = "r.rate_type_id")
    private String rateTypeName;
    /**接口*/
    @Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
    private String bifCode;
    private String bifName;
    /**标准币种*/
    @BtCompositeSearch(fieldName = "r.cur")
    private String cur;
    private String englishCode;
    @Excel(name = "*标准币种")
    @NotNull( message = "fex.rateInfo.message.cur.null")
    @BtCompositeSearch(fieldName = "r.cur")
    private String curName;
    private String curEnCode;
    /**报价币种*/
    @BtCompositeSearch(fieldName = "r.refer_cur")
    private String referCur;
    private String referEnglishCode;
    @NotNull( message = "fex.rateInfo.message.referCur.null")
    @BtCompositeSearch(fieldName = "r.refer_cur")
    private String referCurName;
    private String referCurEnCode;
    /**汇买价*/
    @Excel(name = "*汇买价", align = HorizontalAlignment.RIGHT, numFormat = "##########0.000000")
    @NotNull( message = "fex.rateInfo.message.purchaseRate.null")
    @BtCompositeSearch(fieldName = "r.purchase_rate")
    private BigDecimal purchaseRate;
    /**汇卖价*/
    @Excel(name = "*汇卖价", align = HorizontalAlignment.RIGHT, numFormat = "##########0.000000")
    @NotNull( message = "fex.rateInfo.message.sellRate.null")
    @BtCompositeSearch(fieldName = "r.sell_rate")
    private BigDecimal sellRate;
    /**中间价*/
    @Excel(name = "中间价", align = HorizontalAlignment.RIGHT, numFormat = "##########0.000000")
    @BtCompositeSearch(fieldName = "r.middle_rate")
    private BigDecimal middleRate;
    /**汇率日期*/
    @Excel(name = "汇率日期", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "r.exchange_rate_date")
    private Date exchangeRateDate;
    /**期限*/
    @Excel(name = "期限")
    @BtCompositeSearch(fieldName = "r.term")
    private String term;
    /**备注*/
    @Excel(name = "备注")
    private String rmk;
    /**交易状态*/
    @Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
    private String voucherStat;
    private String voucherStatName;
    /**返回时间*/
    @BtCompositeSearch(fieldName = "r.return_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;


    private String publicationDate;
    /**返回信息*/
    private String returnMsg;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**参考汇率*/
    private BigDecimal referenceRate;

    /**发布时间*/
    @NotNull( message = "fex.rateInfo.message.returnTime.null")
    @BtCompositeSearch(fieldName = "r.effect_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectDate;
    /**报价金额*/
    @BtCompositeSearch(fieldName = "r.cur_unit")
    private String curUnit;

}
