package com.bytter.supply.domain.platform.vo;

import java.util.Date;

/**
 * @ClassName ScfEntBankOpenVo
 * @Description 平台接口开通配置视图对象
 * @version 1.0
 */
public class ScfEntBankOpenVo {

    private String id;
    /** 接口类型 */
    private String type;
    /** 接口中文名称 */
    private String apiNameCn;
    /** 接口名称 */
    private String apiName;
    /** 是否具备直连接口 */
    private String hasDirect;
    /** 直连接口 */
    private String direct;
    /** 平台id */
    private String entPlatformId;
    /** 创建人 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEntPlatformId() { return entPlatformId; }
    public void setEntPlatformId(String entPlatformId) { this.entPlatformId = entPlatformId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getApiNameCn() { return apiNameCn; }
    public void setApiNameCn(String apiNameCn) { this.apiNameCn = apiNameCn; }

    public String getApiName() { return apiName; }
    public void setApiName(String apiName) { this.apiName = apiName; }

    public String getHasDirect() { return hasDirect; }
    public void setHasDirect(String hasDirect) { this.hasDirect = hasDirect; }

    public String getDirect() { return direct; }
    public void setDirect(String direct) { this.direct = direct; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
