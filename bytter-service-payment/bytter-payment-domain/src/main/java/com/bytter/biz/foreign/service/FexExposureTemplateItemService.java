package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexExposureTemplateItemDao;
import com.bytter.biz.foreign.entity.FexExposureTemplateItem;
import com.bytter.biz.foreign.vo.FexExposureTemplateItemVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: Service
 * @Description: 敞口模板表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Service("fexExposureTemplateItemService")
@Transactional
public class FexExposureTemplateItemService extends BaseService<FexExposureTemplateItem, String> {

    @Autowired
    private FexExposureTemplateItemDao fexExposureTemplateItemDao;

    @Override
    public BaseDao<FexExposureTemplateItem, String> getEntityDao() {
        return fexExposureTemplateItemDao;
    }

    /**
     * 根据模板ID获取模板对应的科目列表
     * @param templateId
     * @return
     */
    public List<FexExposureTemplateItem> listByTemplateId(String templateId) {
        return this.fexExposureTemplateItemDao.listBy("templateId", templateId);
    }

    /**
     * 根据模板ID删除科目信息
     * @param templateId
     */
    public void deleteByTemplateId(String templateId) {
        this.fexExposureTemplateItemDao.deleteByTemplateId(templateId);
    }

    /**
     * 根据模板ID获取模板对应的科目列表
     * @param templateId
     * @return
     */
    public List<FexExposureTemplateItemVO> listVOByTemplateId(String templateId) {
        return this.fexExposureTemplateItemDao.listVOByTemplateId(templateId);
    }

    /**
     * 根据科目获取模板信息
     * @param itemCodes
     * @return
     */
    public List<FexExposureTemplateItemVO> listVOByItemCodes(List<String> itemCodes) {
        return fexExposureTemplateItemDao.listVOByItemCodes(itemCodes);
    }
}
