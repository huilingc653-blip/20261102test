package com.bytter.supply.domain.platform.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ScfEntPlatformViewVo
 * @Description 平台详情视图对象
 * @version 1.0
 */
public class ScfEntPlatformViewVo implements Serializable {

    /**
     * 平台基本信息
     */
    private ScfPlatformVo platform;
    /**
     * 全部接口字典项（供前端展示勾选框）
     */
    private List<ScfBankOpenVo> allItems;
    /**
     * 内管端已配置的接口
     */
    private List<ScfBankOpenVo> bankOpenConfig;
    /**
     * 企业端已保存的接口配置
     */
    private List<ScfEntBankOpenVo> configList;
    /**
     * 是否具备直连接口（除平台直联标记外还有其他接口，0-否 1-是）
     */
    private String hasDirect;

    public ScfPlatformVo getPlatform() {
        return platform;
    }

    public void setPlatform(ScfPlatformVo platform) {
        this.platform = platform;
    }

    public List<ScfBankOpenVo> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<ScfBankOpenVo> allItems) {
        this.allItems = allItems;
    }

    public List<ScfBankOpenVo> getBankOpenConfig() {
        return bankOpenConfig;
    }

    public void setBankOpenConfig(List<ScfBankOpenVo> bankOpenConfig) {
        this.bankOpenConfig = bankOpenConfig;
    }

    public List<ScfEntBankOpenVo> getConfigList() {
        return configList;
    }

    public void setConfigList(List<ScfEntBankOpenVo> configList) {
        this.configList = configList;
    }

    public String getHasDirect() {
        return hasDirect;
    }

    public void setHasDirect(String hasDirect) {
        this.hasDirect = hasDirect;
    }
}
