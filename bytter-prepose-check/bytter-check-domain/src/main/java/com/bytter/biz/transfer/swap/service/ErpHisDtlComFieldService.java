package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.transfer.swap.dao.ErpHisDtlComFieldDao;
import com.bytter.biz.transfer.swap.entity.ErpHisDtlComField;
import com.bytter.biz.transfer.swap.entity.ErpHisDtlTemplate;
import com.bytter.biz.transfer.swap.entity.ErpInterConfigFill;
import com.bytter.biz.transfer.swap.vo.ErpHisDtlComFieldVO;
import com.bytter.biz.transfer.swap.vo.ErpHisDtlTemplateVO;
import com.bytter.biz.transfer.swap.vo.ErpHisDtlVO;
import com.bytter.biz.transfer.swap.vo.ErpTemplateStoreVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.*;
/**
 * * 收付补录信息
 */
@Service("erpHisDtlComFieldService")
@Transactional
public class ErpHisDtlComFieldService extends BaseService<ErpHisDtlComField,String> {
    @Autowired
    private ErpHisDtlTemplateService erpHisDtlTemplateService;
    @Autowired
    private ErpInterConfigFillService erpInterConfigFillService;
    @Autowired
    private ErpHisDtlComFieldDao erpHisDtlComFieldDao;
    @Override
    public BaseDao<ErpHisDtlComField, String> getEntityDao() {
        return erpHisDtlComFieldDao;
    }
    public ErpHisDtlVO selectInfoById(String id,String type) {
        ErpHisDtlVO erpHisDtlVO = erpHisDtlComFieldDao.selectInfoById(id);
        if(StringUtils.isNotEmpty(erpHisDtlVO)) {
            List<ErpHisDtlComFieldVO> list = erpHisDtlComFieldDao.getListByTemplateId(erpHisDtlVO.getTemplateId(),type);
            erpHisDtlVO.setErpHisDtlComFields(list);
        }
        return erpHisDtlVO;
    }
    /**
     * 单据列表
     * @param dataTablesInput
     * @param corpAuthList
     * @return
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        String dealType = (String) dataTablesInput.getFormdata().get("dealType");
        //获取配置好的补录字段
        List<ErpInterConfigFill> configFills = erpInterConfigFillService.queryErpFieldValid(dealType);
        List<String> code = new ArrayList();
        for (ErpInterConfigFill fill : configFills){
            code.add("'"+fill.getFieldCode()+"'"+fill.getFieldCode());
        }
        String appCode = StringUtils.listTtoString(code);
        DataTablesOutput output = erpHisDtlComFieldDao.getDataTable(dataTablesInput,corpAuthList,appCode);
        List<ErpTemplateStoreVO>  list = output.getData();
        List<ErpTemplateStoreVO>  listAll = new ArrayList<>();
        Map<String ,Object> map = null;
        for(ErpTemplateStoreVO vo : list){
            map = new HashMap<>();
            List<ErpHisDtlComField> fields = erpHisDtlComFieldDao.getByTemplateId(vo.getTemplateId());
            for (ErpHisDtlComField field : fields) {
                map.put(field.getFieldCode(), field.getTextValue());
            }
            ErpTemplateStoreVO storeVO = BeanUtils.copyMapProperties(map,vo.getClass());
            BeanUtils.copyNotNullProperties(vo,storeVO);
            listAll.add(storeVO);
        }
        output.setData(listAll);
        return output;
    }
    /**
     * 获取配置好的补录字段
     * @param type
     * @return
     */
    public List<ErpInterConfigFill> queryErpFieldValid(String type) {
        return erpInterConfigFillService.queryErpFieldValid(type);
    }
    /**
     *
     * @param hisDtlTemplateVO
     * @param opType
     * @param user
     */
    public void saveHisDtlComField(ErpHisDtlTemplateVO hisDtlTemplateVO, String opType, ICurrentUser user) {
        //根据流水id判断是否存在 新增or修改
        for(String sid : hisDtlTemplateVO.getSerialId()){
            List<ErpHisDtlTemplate> hlist = erpHisDtlTemplateService.getEntityDao().listBy("serialId",sid);
            if(!EmptyUtils.isEmptyList(hlist)){
                ErpHisDtlTemplate hisDtlTemplate = hlist.get(0);
                String storeId = hisDtlTemplate.getTemplateId();
                if(StringUtils.isNotBlank(storeId)){
                    //删除
                    erpHisDtlComFieldDao.deleteEntity(hisDtlTemplate.getTemplateId());
                    //新增
                    saveErpHisDtlComField(hisDtlTemplateVO,hisDtlTemplate);
                }else{
                    saveErpHisDtlComField(hisDtlTemplateVO,hisDtlTemplate);
                }
                hisDtlTemplate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                hisDtlTemplate.setUpdateDy(user.getId());
                erpHisDtlTemplateService.getEntityDao().update(hisDtlTemplate);
            }else{
                String templateId =StringUtils.getUUId();
                //保存数据交换业务和模板中间表
                ErpHisDtlTemplate erpHisDtlTemplate = new ErpHisDtlTemplate(sid,templateId,hisDtlTemplateVO.getDealType(),0L);
                erpHisDtlTemplate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                erpHisDtlTemplate.setUpdateDy(user.getId());
                erpHisDtlTemplateService.getEntityDao().save(erpHisDtlTemplate);
                //新增
                saveErpHisDtlComField(hisDtlTemplateVO,erpHisDtlTemplate);
            }
        }
    }
    /**
     * 新增补录的数据
     * @param hisDtlTemplateVO
     * @param hisDtlTemplate
     * @return
     */
    public void saveErpHisDtlComField(ErpHisDtlTemplateVO hisDtlTemplateVO,ErpHisDtlTemplate hisDtlTemplate){
        ErpHisDtlComField erpHisDtlComField = null;
        Map<String,Object> map = hisDtlTemplateVO.getMap();
        Iterator<Map.Entry<String,Object>> entries = map.entrySet().iterator();
        List<ErpHisDtlComField> lists =new ArrayList<ErpHisDtlComField>();
        while (entries.hasNext()) {
            Map.Entry<String,Object> entry = entries.next();
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            erpHisDtlComField = new ErpHisDtlComField();
            erpHisDtlComField.setFieldCode(entry.getKey());
            erpHisDtlComField.setTextValue(entry.getValue().toString());
            erpHisDtlComField.setTemplateId(hisDtlTemplate.getTemplateId());
            erpHisDtlComField.setType(hisDtlTemplateVO.getDealType());
            lists.add(erpHisDtlComField);
        }
        this.batchSave(lists);
    }
}
