package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.Dict;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class InstitutionDetailsVo implements Serializable {
    /**
     * 原始数据Id
     */
    private String id;
    /**
     * 机构id
     */
    private String insId;

    private String dataName;
    /**
     *数据代码
     */
    private String dataCode;
    /**
     *成立时间 (yyyy-MM-dd)
     */
    private Date establishmentDate;
    /**
     *注册资本
     */
    private BigDecimal registeredCapital;
    /**
     *法定代表人
     */
    private String legalRepresentative;
    /**
     *经营范围
     */
    private String businessScope;
    /**
     *数据来源业务表主建(供应商、子企业、机构的主键)
     */
    private String dataId;
    /**
     *数据来源类别名称;数据来源(Data_Source)Data_Source_Category_Name
     */
    private String dataSourceCategoryName;
    /**
     *数据来源类别代码;数据来源(Data_Source)Data_Source_Category_Code。D001 供应商
     */
    private String dataSourceCategoryCode;
    /**
     *外键(数据来源主键);数据来源(Data_Source)主键 id
     */
    private String dataSourceId;

    /**
     * 机构类别
     */
    @Dict(dictGroup = "INSTITUTIONA_TYPE", inject = "institutionTypeName")
    private String institutionType;

    /**
     * 机构类别名称
     */
    private String institutionTypeName;
    /**
     * 机构名称
     */
    private String institutionName;

    private String institutionNameCode;
    /**
     * 机构编码
     */
    private String institutionCode;
    /**
     * 银行种类:001-中央银行;002-国有商业银行;003-股份制商业银行;004-政策性银行;005-城市商业银行;006-农村商业银行;007-外资银行;008-其他
     */
    @Dict(dictGroup = "institution_bankType", inject = "bankTypeName")
    private String bankType;

    private String bankTypeName;
    /**
     * 金融许可证机构编码
     */
    private String financialLicenseInstCode;
    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;
    /**
     * 国际/地区编码 International/Regional Code
     */
    private String regionalCode;

    private String regionalCodeName;
    /**
     * 地点
     */
    private String place;
    /**
     * 注册资本
     */
    private BigDecimal RegisteredCapital;
    /**
     * 开始营业期限
     */
    private Date startBusinessPeriod;
    /**
     * 结束营业期限
     */
    private Date endBusinessPeriod;
    /**
     * 电话
     */
    private String tel;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 人员规模
     */
    private BigInteger employeeCount;
    /**
     * 经营状态
     */

    @Dict(dictGroup = "business_status", inject = "businessStatusName")
    private String businessStatus;
    /**
     * 经营状态中文描述
     */
    private String businessStatusName;

    /**
     * 数据流转状态：000-未准入;001-准入审批中;095-已准入;
     */
    @Dict(dictGroup = "institution_status", inject = "instStatusName")
    private String instStatus;


    private String instStatusName;

    /**
     * 原始数据id
     */
    private String rawDataId;

    /**
     * 附件Id
     */
    private String attachmentId;

    /**
     * 股权结构
     */
//    private List<CrOwnershipStructureDataVo> ownershipStructureDataVos;

    private String ssIds;

    /**
     * 联系人通讯录
     */
//    private List<CrContactAddressBookVo> contactAddressBookVos;

    private String bookIds;

    /**
     * 拜访记录登记
     */
//    private List<CrVisitRecordRegistrationVo> visitRecordRegistrationVos;

    private String visitIds;

    private String city;

    private String prov;

    private String rmk;

    private String bankTypeId;

    private String bankCode;

    private String tilde;

}
