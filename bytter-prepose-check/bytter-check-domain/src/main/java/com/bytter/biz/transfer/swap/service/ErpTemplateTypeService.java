package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.transfer.swap.dao.ErpTemplateTypeDao;
import com.bytter.biz.transfer.swap.entity.ErpTemplateType;
import com.bytter.biz.transfer.swap.entity.ErpTemplateTypeDtl;
import com.bytter.biz.transfer.swap.vo.ErpTemplateTypeVO;
import com.bytter.check.enums.TemplateTypeEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/19.
 */
@Service("erpTemplateTypeService")
@Transactional
public class ErpTemplateTypeService extends BaseService<ErpTemplateType,String> {
    @Autowired
    private ErpTemplateTypeDao templateTypeDao;
    @Autowired
    private ErpTemplateTypeDtlService templateTypeDtlService;
    @Override
    public BaseDao<ErpTemplateType, String> getEntityDao() {
        return templateTypeDao;
    }
    public boolean findExistCode(String typeCode) {
        List<ErpTemplateType> list = templateTypeDao.listBy("typeCode",typeCode);
        return !EmptyUtils.isEmptyList(list);
    }
    public void saveTemplateTypeAndDtl(ErpTemplateTypeVO type) {
        //保存type
        ErpTemplateType erpTemplateType = new ErpTemplateType(type);
        erpTemplateType.setStatus(TemplateTypeEnum.TEMP_STATUS_SAVE.getCode());//提交
        templateTypeDao.save(erpTemplateType);
        //保存
        List<ErpTemplateTypeDtl> list = type.getTypeDtlList();
        int i=0;
        for(ErpTemplateTypeDtl templateTypeDtl : list){
            templateTypeDtl.setTypeId(erpTemplateType.getId());
            if(templateTypeDtl.getShow()==null){
                templateTypeDtl.setShow(false);
            }
            if(templateTypeDtl.getNotNull()==null){
                templateTypeDtl.setNotNull(false);
            }
            templateTypeDtl.setOrderNum(++i);
        }
        templateTypeDtlService.batchSave(type.getTypeDtlList());
    }
    
    public void saveTemplateTypeAndDtlForTemp(ErpTemplateTypeVO type) {
        //保存type
        ErpTemplateType erpTemplateType = new ErpTemplateType(type);
        erpTemplateType.setStatus(TemplateTypeEnum.TEMP_STATUS_TEMP.getCode());//暂存
        templateTypeDao.save(erpTemplateType);
        //保存
        List<ErpTemplateTypeDtl> list = type.getTypeDtlList();
        int i=0;
        for(ErpTemplateTypeDtl templateTypeDtl : list){
            templateTypeDtl.setTypeId(erpTemplateType.getId());
            if(templateTypeDtl.getShow()==null){
                templateTypeDtl.setShow(false);
            }
            if(templateTypeDtl.getNotNull()==null){
                templateTypeDtl.setNotNull(false);
            }
            templateTypeDtl.setOrderNum(++i);
        }
        templateTypeDtlService.batchSave(type.getTypeDtlList());
    }
    public ErpTemplateTypeVO selectTypeAndNameById(String id) {
        return templateTypeDao.selectTypeAndNameById(id);
    }
    public void updateTemplateTypeAndDtl(ErpTemplateTypeVO type) {
        //修改type
        ErpTemplateType erpTemplateType = new ErpTemplateType(type,System.currentTimeMillis());
        erpTemplateType.setStatus(TemplateTypeEnum.TEMP_STATUS_SAVE.getCode());
        templateTypeDao.update(erpTemplateType);
        //保存
        List<ErpTemplateTypeDtl> list = type.getTypeDtlList();
        int i=0;
        for(ErpTemplateTypeDtl templateTypeDtl : list){
            if(templateTypeDtl.getShow()==null){
                templateTypeDtl.setShow(false);
            }
            if(templateTypeDtl.getNotNull()==null){
                templateTypeDtl.setNotNull(false);
            }
            templateTypeDtl.setOrderNum(++i);
        }
        templateTypeDtlService.batchUpdate(type.getTypeDtlList());
    }
    
    public void updateTemplateTypeAndDtlForTemp(ErpTemplateTypeVO type) {
        //修改type
        ErpTemplateType erpTemplateType = new ErpTemplateType(type,System.currentTimeMillis());
        erpTemplateType.setStatus(TemplateTypeEnum.TEMP_STATUS_TEMP.getCode());
        templateTypeDao.update(erpTemplateType);
        //保存
        List<ErpTemplateTypeDtl> list = type.getTypeDtlList();
        int i=0;
        for(ErpTemplateTypeDtl templateTypeDtl : list){
            if(templateTypeDtl.getShow()==null){
                templateTypeDtl.setShow(false);
            }
            if(templateTypeDtl.getNotNull()==null){
                templateTypeDtl.setNotNull(false);
            }
            templateTypeDtl.setOrderNum(++i);
        }
        templateTypeDtlService.batchUpdate(type.getTypeDtlList());
    }
    
    public boolean findExistCodeMyself(String id, String typeCode) {
        String hql = "from ErpTemplateType where typeCode=:code and id!=:id";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("code",typeCode);
        params.put("id",id);
        List<ErpTemplateType> list = templateTypeDao.listByHQL(hql,params);
        return !EmptyUtils.isEmptyList(list);
    }
}
