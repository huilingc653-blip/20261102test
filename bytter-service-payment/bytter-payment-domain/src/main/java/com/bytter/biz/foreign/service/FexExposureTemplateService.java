package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexExposureTemplateDao;
import com.bytter.biz.foreign.entity.FexExposureTemplate;
import com.bytter.biz.foreign.entity.FexExposureTemplateItem;
import com.bytter.biz.foreign.vo.FexCorpExposureTemplateVO;
import com.bytter.biz.foreign.vo.FexExposureTemplateVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Service
 * @Description: 敞口模板表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Service("fexExposureTemplateService")
@Transactional
public class FexExposureTemplateService extends BaseService<FexExposureTemplate, String> {

    @Autowired
    private FexExposureTemplateDao fexExposureTemplateDao;

    @Autowired
    private FexExposureTemplateItemService fexExposureTemplateItemService;

    @Autowired
    private FexCorpExposureTemplateService fexCorpExposureTemplateService;

    @Override
    public BaseDao<FexExposureTemplate, String> getEntityDao() {
        return fexExposureTemplateDao;
    }

    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput){
        return fexExposureTemplateDao.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 根据ID获取敞口模板信息
     *
     * @param id
     * @return
     */
    public FexExposureTemplateVO getByIdUnion(String id) {
        FexExposureTemplateVO fexExposureTemplateVO = fexExposureTemplateDao.getByIdUnion(id);
        if (fexExposureTemplateVO != null) {
            fexExposureTemplateVO.setListItem(fexExposureTemplateItemService.listVOByTemplateId(id));
        }
        return fexExposureTemplateVO;
    }

    /**
     * 根据模板名称获取模板信息
     *
     * @param templateName
     * @return
     */
    public FexExposureTemplate getByTemplateName(String templateName) {
        return fexExposureTemplateDao.getUniqueBy("templateName", templateName);
    }

    /**
     * 保存敞口模板
     * @param entity
     */
    public void saveFexExposureTemplate(FexExposureTemplate entity, List<String> itemIds, List<String> itemCodes) {
        entity.setTemplateCode(SequenceSingleton.getSequence(SequenceEnum.FEX_EXPOSURE_TEMPLATE));
        this.saveEntity(entity);
        // 新增科目
        if (!EmptyUtils.isEmptyList(itemIds)) {
            List<FexExposureTemplateItem> listItem = new ArrayList<>();
            for (int i=0; i<itemIds.size(); i++) {
                FexExposureTemplateItem item = new FexExposureTemplateItem();
                item.setTemplateId(entity.getId());
                item.setItemId(itemIds.get(i));
                item.setItemCode(itemCodes.get(i));
                listItem.add(item);
            }
            if(EmptyUtils.isNotEmpty(listItem)) {
                this.fexExposureTemplateItemService.batchSave(listItem);
            }
        }
    }

    /**
     * 更新敞口模板
     * @param entity
     */
    public void updateFexExposureTemplate(FexExposureTemplate entity, List<String> itemIds, List<String> itemCodes) {
        this.saveOrUpdate(entity);
        // 先删除科目，再新增
        fexExposureTemplateItemService.deleteByTemplateId(entity.getId());
        if (!EmptyUtils.isEmptyList(itemIds)) {
            List<FexExposureTemplateItem> listItem = new ArrayList<>();
            for (int i=0; i<itemIds.size(); i++) {
                FexExposureTemplateItem item = new FexExposureTemplateItem();
                item.setTemplateId(entity.getId());
                item.setItemId(itemIds.get(i));
                item.setItemCode(itemCodes.get(i));
                listItem.add(item);
            }
            if(EmptyUtils.isNotEmpty(listItem)) {
                this.fexExposureTemplateItemService.batchSave(listItem);
            }
        }
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteFexExposureTemplate(FexExposureTemplate entity) {
        //校验已使用的模板不允许删除
        List<FexCorpExposureTemplateVO> corpExposureTemplateList = fexCorpExposureTemplateService.listByTemplateId(entity.getId());
        if (!EmptyUtils.isEmptyList(corpExposureTemplateList)) {
            throw new ParameterException(ForeignMessageEnum.FEX_EXPOSURE_TEMPLATE_MESSAGE_EXISTS, entity.getTemplateName());
        } else {
            this.fexExposureTemplateItemService.deleteByTemplateId(entity.getId());
            this.deleteEntity(entity);
        }
    }

    /**
     * 批量删除对象
     *
     * @param ids
     */
    public void batchDelete(String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexExposureTemplate fexExposureTemplate = this.get(id);
            this.deleteFexExposureTemplate(fexExposureTemplate);
        }
    }

    /**
     * 获取所有有效敞口模板信息
     * @return
     */
    public List<FexExposureTemplate> listAllValid() {
        return fexExposureTemplateDao.listBy("validSign", CommonConstant.YES);
    }
}
