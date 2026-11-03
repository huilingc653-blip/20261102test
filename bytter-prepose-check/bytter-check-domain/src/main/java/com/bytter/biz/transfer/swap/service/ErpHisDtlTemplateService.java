package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.transfer.swap.dao.ErpHisDtlTemplateDao;
import com.bytter.biz.transfer.swap.entity.*;
import com.bytter.biz.transfer.swap.enums.HisDtlTemplateMessageEnum;
import com.bytter.biz.transfer.swap.enums.HisDtlTemplateStatusEnum;
import com.bytter.biz.transfer.swap.util.ErpTemplateUtil;
import com.bytter.biz.transfer.swap.vo.ErpHisDtlTemplateVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/29.
 */
@Service("erpHisDtlTemplateService")
@Transactional
public class ErpHisDtlTemplateService extends BaseService<ErpHisDtlTemplate,String> {
    @Autowired
    private ErpHisDtlTemplateDao hisDtlTemplateDao;
    @Autowired
    private ErpTemplateTypeService erpTemplateTypeService;
    @Autowired
    private ErpTemplateService erpTemplateService;
    @Autowired
    private ErpTemplateDtlService erpTemplateDtlService;
    @Autowired
    private ErpTemplateStoreService erpTemplateStoreService;
    @Autowired
    private ErpTemplateDtlStoreService erpTemplateDtlStoreService;
    @Autowired
    private ErpInterConfigService erpInterConfigService;
    @Override
    public BaseDao<ErpHisDtlTemplate, String> getEntityDao() {
        return hisDtlTemplateDao;
    }
    public void updateHisTemplate(ErpHisDtlTemplateVO hisDtlTemplateVO,String opType,ICurrentUser user) {
        //查询模板类型记录
        ErpTemplateType erpTemplateType = erpTemplateTypeService.get(hisDtlTemplateVO.getTypeId(),true);
        //查询模板和明细
        ErpTemplate erpTemplate = erpTemplateService.get(hisDtlTemplateVO.getTemplateId(),true);
        ErpTemplate erpTemplateStore = new ErpTemplate();
        List<ErpTemplateDtl> list = new ArrayList<ErpTemplateDtl>();
        if(!EmptyUtils.isEmpty(erpTemplate)){
            if("1".equals(erpTemplate.getEnteringIsEdit())){//针对模板可以编辑的特殊处理
            	Map<String,Object> headMap = hisDtlTemplateVO.getMap();
                List<Map<String,Object>> childList = hisDtlTemplateVO.getList();
                //表头赋值
                for (String key : headMap.keySet()) {
                    ErpTemplateUtil.setHeadValueByMap(erpTemplateStore,key,headMap.get(key));
                }
                //凭证赋值
                if(!EmptyUtils.isEmptyList(childList)){
                    for(Map<String,Object> childMap : childList){
                        ErpTemplateDtl templateDtl = new ErpTemplateDtl(erpTemplate.getId());
                        for (String key : childMap.keySet()) {
                            ErpTemplateUtil.setChildValueByMap(templateDtl,key,childMap.get(key));
                        }
                        list.add(templateDtl);
                    }
                }
            	erpTemplateStore.setId(erpTemplate.getId());
            	erpTemplateStore.setTemplateName(erpTemplate.getTemplateName());
            }else{
            	BeanUtils.copyProperties(erpTemplate,erpTemplateStore);
                list = erpTemplateDtlService.selectDtlByTypeId(erpTemplate.getId());
            }
        }
        //根据流水id判断是否存在 新增or修改
        for(String sid : hisDtlTemplateVO.getSerialId()){
            List<ErpHisDtlTemplate> hlist = hisDtlTemplateDao.listBy("serialId",sid);
            if(!EmptyUtils.isEmptyList(hlist)){
                ErpHisDtlTemplate hisDtlTemplate = hlist.get(0);
                String storeId = hisDtlTemplate.getTemplateId();
                if(StringUtils.isNotBlank(storeId)){
                    //修改
                	updateTemplateDtlStore(storeId,erpTemplateType, erpTemplateStore, list);
                }else{
                    //新增
                	ErpTemplateStore store=saveTemplateDtlStore(erpTemplateType, erpTemplateStore, list);
                	hisDtlTemplate.setTemplateId(store.getId());
                }
                hisDtlTemplate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            	hisDtlTemplate.setUpdateDy(user.getId());
            	hisDtlTemplateDao.update(hisDtlTemplate);
            }else{
                //新增
            	ErpTemplateStore store=saveTemplateDtlStore(erpTemplateType, erpTemplateStore, list);
                //保存数据交换业务和模板中间表
                ErpHisDtlTemplate erpHisDtlTemplate = new ErpHisDtlTemplate(sid,store.getId(),hisDtlTemplateVO.getDealType(),0L);
                erpHisDtlTemplate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                erpHisDtlTemplate.setUpdateDy(user.getId());
                hisDtlTemplateDao.save(erpHisDtlTemplate);
            }
        }
    }
    
    /**
     * 新增模板Store信息
     * @param erpTemplateType
     * @param erpTemplateStore
     * @param list
     * @return
     */
    public ErpTemplateStore saveTemplateDtlStore(ErpTemplateType erpTemplateType,ErpTemplate erpTemplateStore,List<ErpTemplateDtl> list){
    	ErpTemplateStore store = new ErpTemplateStore();
        BeanUtils.copyProperties(erpTemplateStore,store);
        store.setTypeName(erpTemplateType.getTypeName());
        store.setTemplateId(erpTemplateStore.getId());
        store.setId(null);
        erpTemplateStoreService.saveEntity(store);
        List<ErpTemplateDtlStore> dtllist = BeanUtils.copyListProperties(list,ErpTemplateDtlStore.class);
        for(ErpTemplateDtlStore dtlStore : dtllist){
            dtlStore.setId(null);
            dtlStore.setStoreId(store.getId());
        }
        erpTemplateDtlStoreService.batchSave(dtllist);
        return store;
    }
    
    /**
     * 修改模板Store信息
     * @param storeId
     * @param erpTemplateType
     * @param erpTemplateStore
     * @param list
     * @return
     */
    public ErpTemplateStore updateTemplateDtlStore(String storeId,ErpTemplateType erpTemplateType,ErpTemplate erpTemplateStore,List<ErpTemplateDtl> list){
    	 ErpTemplateStore store = erpTemplateStoreService.get(storeId,true);
         BeanUtils.copyProperties(erpTemplateStore,store);
         store.setTemplateId(erpTemplateStore.getId());
         store.setTypeName(erpTemplateType.getTypeName());
         store.setId(storeId);
         erpTemplateStoreService.updateEntity(store);
         //先删除
         erpTemplateDtlStoreService.deleteByStoreId(storeId);
         List<ErpTemplateDtlStore> dtllist = BeanUtils.copyListProperties(list,ErpTemplateDtlStore.class);
         for(ErpTemplateDtlStore dtlStore : dtllist){
             dtlStore.setStoreId(storeId);
         }
         erpTemplateDtlStoreService.batchSave(dtllist);
         return store;
    }
    public int updateHisTemplateStatus(List<String> ids,String type, Long status,ICurrentUser user) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        List<ErpHisDtlTemplate> list = hisDtlTemplateDao.listErpHisDtlTemplateByIds(params);
        for (ErpHisDtlTemplate hisDtlTemplate:list){
            checkTemplateStatus(type,status, hisDtlTemplate);
        }
        params.put("status", status);
        if (status == -1){
            params.put("reverse2",null);
        }
        params.put("updateDy", user.getId());
        params.put("updateDate", new Timestamp(System.currentTimeMillis()));
        return hisDtlTemplateDao.batchStatusByIds(params);
    }
    /**
     * 对比数据库中 ErpHisDtlTemplate 实体与设置的当前状态是否冲突
     * @param status
     * @param hisDtlTemplate
     */
    private void checkTemplateStatus(String type, Long status, ErpHisDtlTemplate hisDtlTemplate) {
        if (null == hisDtlTemplate){
            return;
        }
        if (status == 1 || status == 10) {
            if (hisDtlTemplate.getStatus() == 1 || hisDtlTemplate.getStatus() == 10) {
                throw new ParameterException(HisDtlTemplateMessageEnum.HIS_DTL_TEMPLATE_STATUS_CHANGED);
            }
        } else {
            if (hisDtlTemplate.getStatus() == status) {
                throw new ParameterException(HisDtlTemplateMessageEnum.HIS_DTL_TEMPLATE_STATUS_CHANGED);
            }
        }
    }
    public void updateFinishByIds(List<String> ids,String type, Long status,ICurrentUser user) {
        for(String id : ids){
            ErpHisDtlTemplate hisDtlTemplate = hisDtlTemplateDao.findExitBySid(id);
            checkTemplateStatus(type,status, hisDtlTemplate);
            if(EmptyUtils.isEmpty(hisDtlTemplate)){
                hisDtlTemplate = new ErpHisDtlTemplate(id,null,type,0L);
                hisDtlTemplate.setStatus(status);
                hisDtlTemplate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                hisDtlTemplate.setUpdateDy(user.getId());
                hisDtlTemplateDao.save(hisDtlTemplate);
            }else{
                hisDtlTemplate.setStatus(status);
                hisDtlTemplate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                hisDtlTemplate.setUpdateDy(user.getId());
                hisDtlTemplateDao.update(hisDtlTemplate);
            }
        }
    }
    /**
     * 自动发送
     * @param ids
     * @param type
     * @param status
     */
    public void updateConfirmByIds(List<String> ids, String type, Long status,ICurrentUser user) {
        Map<String, Object> config = erpInterConfigService.selectByConfig(type);
        if(!EmptyUtils.isEmpty(config) && !EmptyUtils.isEmpty(config.get("autosend")) && "1".equals(config.get("autosend"))){
            status = HisDtlTemplateStatusEnum.SENDING.getStatus().longValue();
        }
        updateHisTemplateStatus(ids,type,status,user);
    }
}
