package com.bytter.bill.domain.bill.vo;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.enums.CommonDictEnum;
import lombok.Data;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/**
 * @Title: AbBillSignStrategyVO
 * @Description: 电票自动签收策略VO
 * @author wangzy
 * @date 2020/7/13 16:59
 */
@Data
public class AbBillSignStrategyVO implements java.io.Serializable {
    /**电票自动签收策略Id*/
    private String id;
    /**单位Id*/
    @BtCompositeSearch(fieldName="ss.corp_id")
    private String corpId;
    private String corpCodeAndName;
    /**策略名称*/
    @BtCompositeSearch(fieldName="ss.name")
    private String name;
    private String code;
    /**票据类型*/
    @BtCompositeSearch(fieldName="ss.BILL_TYPE")
    // @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private String billType;
    private String billTypeName;
    /**签收类型*/
    // @Dict(dictGroup = "AB_BILL_SIGNERTYPE", inject = "signTypeName", multi = true)
    @BtCompositeSearch(fieldName="ss.sign_Type")
    private String signType;
    private String signTypeName;
    /**出票人*/
    private String payer;
    /**承兑人*/
    private String acceptoracc;
    /**业务发起人*/
    private String reqNm;
    /**不得转让标记：EM00 可再转让；EM01 不得转让*/
    // @Dict(dictGroup = "AB_BANEM", inject = "banendrsmtmkName")
    private String bankendrsmtmk;
    private String banendrsmtmkName;
    /**票面金额开始数值*/
    private BigDecimal beginNum;
    /**票面金额结束数值*/
    private BigDecimal endNum;
    /**开始数值比较运算符（10＞、11＞＝、20＜、21＜＝）*/
    private String beginOperator;
    /**结束数值比较运算符（10＞、11＞＝、20＜、21＜＝）*/
    private String endOperator;
    /**数值间运算符(01或者、02并且)*/
    private String numOperator;
    /**有效标记*/
    // @Dict(dictGroup = "sf_yn", inject = "validSignName")
    private String validSign;
    private String validSignName;
    private Locale locale;
    
    /** 出票人组合**/
    private String payerIds;
    private String payerExternalIds;
    /** 承兑人组合**/
    private String acceptorIds;
    private String acceptorExternalIds;
    /** 业务发起人组合**/
    private String reqIds;
    private String reqExternalIds;
    private Map<String,Object> payerMap=new HashMap<>();
    private Map<String,Object> acceptorMap=new HashMap<>();
    private Map<String,Object> applyMap=new HashMap<>();
    public String getValidSignName() {
        if (ObjectUtil.isNotEmpty(validSign)) {
            return CommonDictEnum.getDictValue("sf_yn", validSign);
        }
        return validSignName;
    }
}
