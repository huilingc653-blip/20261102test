package com.bytter.biz.foreign.vo;

import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.util.List;


public class FexTemplateConfigVo implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 1L;
    private String templateId;
    private String versionNo;

    private List<FexTranReqConfigVo> templateConfigs;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public List<FexTranReqConfigVo> getTemplateConfigs() {
        return templateConfigs;
    }

    public void setTemplateConfigs(List<FexTranReqConfigVo> templateConfigs) {
        this.templateConfigs = templateConfigs;
    }
}
