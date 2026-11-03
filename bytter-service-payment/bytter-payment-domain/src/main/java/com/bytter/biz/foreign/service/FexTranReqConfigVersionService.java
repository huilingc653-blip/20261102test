package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranReqConfigVersionDao;
import com.bytter.biz.foreign.entity.FexTranReqConfigVersion;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Title: Entity
 * @Description: 外汇交易模板配置版本表
 * @author wuxc
 * @date 2026-05-14
 */
@Service("fexTranReqConfigVersionService")
public class FexTranReqConfigVersionService extends BaseService<FexTranReqConfigVersion, String> {

    @Autowired
    private FexTranReqConfigVersionDao fexTranReqConfigVersionDao;

    @Override
    public BaseDao<FexTranReqConfigVersion, String> getEntityDao() {
        return fexTranReqConfigVersionDao;
    }


    public String getMaxVersionNo(String templateId) {
        return fexTranReqConfigVersionDao.getMaxVersionNo(templateId);
    }


    public List<String> listTmplateHisVersionNos(String templateId) {
        return fexTranReqConfigVersionDao.listTmplateHisVersionNos(templateId);
    }

    public void delByTemplateId(String templateId, String versionNo) {
        fexTranReqConfigVersionDao.delByTemplateId(templateId, versionNo);
    }

    public FexTranReqConfigVersion getByTemplateId(String templateId, String versionNo) {
      return  fexTranReqConfigVersionDao.getByTemplateId(templateId, versionNo);
    }
}
