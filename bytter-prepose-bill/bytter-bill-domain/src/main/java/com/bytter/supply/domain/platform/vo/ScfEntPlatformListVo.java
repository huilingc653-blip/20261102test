package com.bytter.supply.domain.platform.vo;

import com.bytter.core.annotation.BtCompositeSearch;

/**
 * @ClassName ScfEntPlatformListVo
 * @Description 平台配置列表视图对象（SCF_PLATFORM + SCF_ENT_BANK_OPEN）
 * @version 1.0
 */
public class ScfEntPlatformListVo {

    private String id;
    /**
     * 平台编码
     */
    @BtCompositeSearch(fieldName = "p.platform_code")
    private String entCode;
    /**
     * 平台名称
     */
    @BtCompositeSearch(fieldName = "p.platform_name")
    private String entName;
    /**
     * 是否具备直连接口
     */
    private String hasDirect;
    /**
     * 直连接口
     */
    private String direct;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getHasDirect() {
        return hasDirect;
    }

    public void setHasDirect(String hasDirect) {
        this.hasDirect = hasDirect;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }
}
