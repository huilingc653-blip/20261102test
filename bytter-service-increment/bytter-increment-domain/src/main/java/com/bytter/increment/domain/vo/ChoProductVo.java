package com.bytter.increment.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
/**
 * @Title: VO
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Data
public class ChoProductVo implements Serializable {
    private static final long serialVersionUID = 3650731121127660208L;
    private String id;
    /**银行类型*/
    // @Dict(tableName="bt_bank_type",valueFieldName="id",showFiledName="type_name",inject="bankTypeName")
    @BtCompositeSearch(fieldName = "p.bank_type_id")
    private String bankTypeId;
    /**银行类型名称*/
    @BtCompositeSearch(fieldName = "p.bank_type_id")
    private String bankTypeName;
    /**产品类型*/
    @BtCompositeSearch(fieldName = "p.product_type")
    // @Dict(dictGroup = "cho.productType", inject = "productTypeName")
    private String productType;
    /**产品代码*/
    @BtCompositeSearch(fieldName = "p.product_code")
    private String productCode;
    /**产品名称*/
    @BtCompositeSearch(fieldName = "p.product_name")
    private String productName;
    /**币别*/
    // @Currency(inject="curName")
    @BtCompositeSearch(fieldName = "p.cur_id")
    private String curId;
    @BtCompositeSearch(fieldName = "p.cur_id")
    private String curName;
    /**备注*/
    private String rmk;
    /**有效标志 1-有效 0-无效*/
    @BtCompositeSearch(fieldName = "p.valid_sign")
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**
     * 理财类型
     */
    @BtCompositeSearch(fieldName = "p.FINANCIAL_TYPE")
    // @Dict(dictGroup = "financial.type", inject = "financialTypeName")
    private String financialType;

    @BtCompositeSearch(fieldName = "p.FINANCIAL_TYPE")
    private String financialTypeName;
    private String productTypeName;
    /**
     * 理财类型是否在理财登记审批通过
     */
    private String isApprove;
    /**续存模式 1-本金续存 2-本息续存*/
    @BtCompositeSearch(fieldName = "p.CERTIFICATE_WAY")
    private Integer certificateWay;
}
