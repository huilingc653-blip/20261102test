package com.bytter.report.domain.billl.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.system.api.dto.DictDTO;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
/**
 * @Title: Vo
 * @Description: 即将到期票据分析VO
 * @author zhenghj
 * @date 2020-02-10
 * @version 1.0
 *
 */
@Data
public class AbBillSoonExpireVO implements Serializable {
    private String id;
    /**单位ID*/
    // @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName="f.corpId")
    private String corpId;
    @BtCompositeSearch(fieldName="f.corpId")
    private String corpName;
    private String opType;
    /**流通介质：10 纸票、11 电票*/
    // @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    private Integer billFlag;
    @BtCompositeSearch()
    private String billFlagName;
    /**流通介质：10 银票、11 商票*/
    // @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    @BtCompositeSearch()
    private String billTypeName;
    /**票据类型  0应付票据，1应收票据*/
    @BtCompositeSearch()
    private String type;
    /**银票金额合计*/
    private BigDecimal bankMoneyTotal;
    /**商票金额合计*/
    private BigDecimal businessMoneyTotal;
    /**合计*/
    private BigDecimal total;
    /**汇总*/
    private BigDecimal collect;
    /**期限Id*/
    private String termId;
    public void injectValues(Map<String, Map<String, DictDTO>> listDictMap){
        // 票据种类
        Optional.ofNullable(this.getBillFlag()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
                    .ifPresent(dict -> this.setBillFlagName(dict.getName()));
        });
        // 票据种类
        Optional.ofNullable(this.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
                    .ifPresent(dict -> this.setBillTypeName(dict.getName()));
        });
    }
}
