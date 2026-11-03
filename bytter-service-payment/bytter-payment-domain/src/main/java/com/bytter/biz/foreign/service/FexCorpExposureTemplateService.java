package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCorpExposureTemplateDao;
import com.bytter.biz.foreign.entity.FexCorpExposureTemplate;
import com.bytter.biz.foreign.entity.FexExposureMain;
import com.bytter.biz.foreign.entity.FexExposureTemplate;
import com.bytter.biz.foreign.vo.FexCorpExposureTemplateVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Service
 * @Description: 单位敞口模板表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Service("fexCorpExposureTemplateService")
@Transactional
public class FexCorpExposureTemplateService extends BaseService<FexCorpExposureTemplate, String> {

    @Autowired
    private FexCorpExposureTemplateDao fexCorpExposureTemplateDao;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @Autowired(required = false)
    private ICorpService corpService;
    @Autowired(required = false)
    private FexExposureTemplateService fexExposureTemplateService;
    @Autowired(required = false)
    private FexExposureMainService fexExposureMainService;

    @Override
    public BaseDao<FexCorpExposureTemplate, String> getEntityDao() {
        return fexCorpExposureTemplateDao;
    }

    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput){
        DataTablesOutput data = fexCorpExposureTemplateDao.getDataTablesUnion(dataTablesInput);
        List<FexCorpExposureTemplateVO> list = data.getData();
        for(FexCorpExposureTemplateVO vo:list){
            vo.setEnglishCode(this.getEnglishCode(vo));
        }
        return data;
    }

    public String getEnglishCode(FexCorpExposureTemplateVO vo){
        String englishCode = "";
        if (StringUtils.isBlank(vo.getCurId())) {
            return englishCode;
        }
        String[] curIds = vo.getCurId().split(CommonConstant.COMMA_SPLIT);
        for (String curId:curIds) {
            BtCurrencyDTO cur = currencyService.getObjectById(curId);
            if(StringUtils.isNotBlank(englishCode)){
                englishCode = englishCode + CommonConstant.COMMA_SPLIT + cur.getEnglishCode();
            } else {
                englishCode = cur.getEnglishCode();
            }
        }
        return englishCode;
    }
    /**
     * 根据ID获取单位敞口模板信息
     *
     * @param id
     * @return
     */
    public FexCorpExposureTemplateVO getByIdUnion(String id) {
        FexCorpExposureTemplateVO vo = fexCorpExposureTemplateDao.getByIdUnion(id);
        vo.setEnglishCode(this.getEnglishCode(vo));
        return vo;
    }

    /**
     * 保存单位敞口模板
     * @param corpExposureTemplate
     */
    public void saveFexCorpExposureTemplate(FexCorpExposureTemplate corpExposureTemplate) {
        String[] corpIds = corpExposureTemplate.getCorpId().split(CommonConstant.COMMA_SPLIT);
        List<FexCorpExposureTemplate> list = new ArrayList<>();
        for (String corpId:corpIds) {
            if (this.existTemplate(corpId, corpExposureTemplate.getTemplateId(), null)) {
                CorpDTO corp = corpService.get(corpId);
                FexExposureTemplate template = fexExposureTemplateService.get(corpExposureTemplate.getTemplateId());
                // 单位已经存在对应的模板信息
                throw new ParameterException(ForeignMessageEnum.FEX_CORP_EXPOSURE_TEMPLATE_MESSAGE_EXISTS, corp.getName(), template.getTemplateName());
            }
            FexCorpExposureTemplate entity = new FexCorpExposureTemplate();
            BeanUtils.copyNotNullProperties(corpExposureTemplate, entity);
            entity.setCorpId(corpId);
            list.add(entity);
        }
        this.batchSave(list);
    }

    /**
     * 更新敞口模板
     * @param entity
     */
    public void updateFexCorpExposureTemplate(FexCorpExposureTemplate entity) {
        if (this.existTemplate(entity.getCorpId(), entity.getTemplateId(), entity.getId())) {
            CorpDTO corp = corpService.get(entity.getCorpId());
            FexExposureTemplate template = fexExposureTemplateService.get(entity.getTemplateId());
            // 单位已经存在对应的模板信息
            throw new ParameterException(ForeignMessageEnum.FEX_CORP_EXPOSURE_TEMPLATE_MESSAGE_EXISTS, corp.getName(), template.getTemplateName());
        }
        this.saveOrUpdate(entity);
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteFexCorpExposureTemplate(FexCorpExposureTemplate entity) {
        //校验已使用单位模板不允许删除
        List<FexExposureMain> exposureMainList = fexExposureMainService.listByCorpIdAndTemplateId(entity.getCorpId(), entity.getTemplateId());
        if (!EmptyUtils.isEmptyList(exposureMainList)) {
            throw new ParameterException(ForeignMessageEnum.FEX_CORP_EXPOSURE_TEMPLATE_MESSAGE_USED);
        } else {
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
            FexCorpExposureTemplate fexCorpExposureTemplate = this.get(id);
            this.deleteFexCorpExposureTemplate(fexCorpExposureTemplate);
        }
    }

    /**
     * 获取所有有效敞口模板信息
     * @return
     */
    public List<FexCorpExposureTemplateVO> listByCorpId(String corpId) {
        return fexCorpExposureTemplateDao.listByCorpId(corpId);
    }

    /**
     * 获取所有有效敞口模板信息
     * @return
     */
    public List<FexCorpExposureTemplateVO> listByCorpIdForCollectCorp(String corpId) {
        return fexCorpExposureTemplateDao.listByCorpIdForCollectCorp(corpId);
    }

    /**
     * 根据模板Id获取单位模板信息
     * @return
     */
    public List<FexCorpExposureTemplateVO> listByTemplateId(String templateId) {
        return fexCorpExposureTemplateDao.listByTemplateId(templateId);
    }

    /**
     * 根据单位ID&模板ID获取单位模板设置信息
     * @return
     */
    public FexCorpExposureTemplateVO getByCorpIdAndTemplateId(String corpId, String templateId) {
        return fexCorpExposureTemplateDao.getByCorpIdAndTemplateId(corpId, templateId);
    }

    /**
     * 判断单位是否已经存在模板
     * @param corpId
     * @param templateId
     * @return
     */
    public boolean existTemplate(String corpId, String templateId, String id) {
        boolean result = false;
        FexCorpExposureTemplateVO corpExposureTemplate = this.getByCorpIdAndTemplateId(corpId, templateId);
        if (corpExposureTemplate != null ) {
            if (StringUtils.isBlank(id)) {
                result = true;
            } else {
                if (!id.equals(corpExposureTemplate.getId())) {
                    result = true;
                }
            }
        }
        return result;
    }
}
