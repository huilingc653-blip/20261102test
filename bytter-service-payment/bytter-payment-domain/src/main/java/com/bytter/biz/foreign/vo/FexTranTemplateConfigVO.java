package com.bytter.biz.foreign.vo;

import com.bytter.check.vo.IBaseDataInject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 交易模板配置
 */

@Data
public class FexTranTemplateConfigVO implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = -556469445334493467L;
    private String id;
    /**
     * 父级ID
     */
    private String parentId;
    /**
     *模板编号
     */
    private String templateNo;
    /**
     * 模板名称
     */
    private String templateName;
    private String templateNameParent;
    /**
     * 模板描述
     */
    private String templateDesc;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否有效表
     */
    private String validSign;
    /**
     * 是否有效
     */
    private String validSignName;
    /**
     * 银行类型ID
     */
    private String bankTypeId;
    /**
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 接口代码ID
     */
    private String bifId;
    /**
     * 接口代码名称
     */
    private String bifName;
    /**
     * 国家ID
     */
    private String countryId;
    /**
     * 国家名称
     */
    private String countryZhName;
    /**
     *支付类型名称
     */
    private String payTypeName;
    /**
     *支付类型编码
     */
    private String bankPayType;
    /**
     *支付类型
     */
    private String payTypeId;
    /**
     * 配置版本
     */
    private String versionNo;
    /**
     *二级数据
     */
    private List<FexTranTemplateConfigDetailVO> children;
}
