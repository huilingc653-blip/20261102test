package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.dataexchange.basic.service.ErpDataTypeService;
import com.bytter.biz.transfer.swap.dao.ErpTemplateDao;
import com.bytter.biz.transfer.swap.entity.ErpInterConfigFill;
import com.bytter.biz.transfer.swap.entity.ErpTemplate;
import com.bytter.biz.transfer.swap.entity.ErpTemplateDtl;
import com.bytter.biz.transfer.swap.util.ErpTemplateUtil;
import com.bytter.biz.transfer.swap.vo.ErpTemplateSourceVO;
import com.bytter.biz.transfer.swap.vo.ErpTemplateVO;
import com.bytter.check.dto.ErpDataTypeDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/23.
 */
@Service("erpTemplateService")
@Transactional
public class ErpTemplateService extends BaseService<ErpTemplate,String> {
    @Autowired
    private ErpTemplateDao templateDao;
    @Autowired
    private ErpTemplateDtlService erpTemplateDtlService;
    @Autowired
    private ErpInterConfigFillService erpInterConfigFillService;
    @Autowired
    private ErpDataTypeService erpDataTypeService;
    @Override
    public BaseDao<ErpTemplate, String> getEntityDao() {
        return templateDao;
    }
    public List<ErpTemplateSourceVO> selectSourceByCode(String code) {
        ErpDataTypeDTO erpDataTypeDTO = erpDataTypeService.getErpDataTypeDTOByCode(code);
        if(EmptyUtils.isEmpty(erpDataTypeDTO)){
            return new ArrayList<ErpTemplateSourceVO>();
        }
        List<ErpTemplateSourceVO> list = templateDao.selectSourceByCode(erpDataTypeDTO);
        if(EmptyUtils.isEmptyList(list)){
            return new ArrayList<ErpTemplateSourceVO>();
        }
        return list;
    }
    /**
     * 动态组装数据源
     * @param type
     * @return
     */
    public List<ErpTemplateSourceVO> dynaSource(String type) {
        List<ErpInterConfigFill> fills = erpInterConfigFillService.queryErpFieldValid(type);
        List<ErpTemplateSourceVO> voList= new ArrayList<>();
        for (ErpInterConfigFill fill : fills){
            if(StringUtils.isNotBlank(fill.getDataSource())){
                voList.addAll(this.selectSourceByCode(fill.getDataSource()));
            }
        }
        return voList;
    }
    public boolean findExistCode(String templateCode) {
        List<ErpTemplate> list = templateDao.listBy("templateCode",templateCode);
        return !EmptyUtils.isEmptyList(list);
    }
    /**
     * 获取所有的已生效的补录配置
     * @return
     */
    public List<ErpTemplateSourceVO> dynaDataSource() {
        List<ErpTemplateSourceVO> list = templateDao.selectDataSource();
        if(EmptyUtils.isEmptyList(list)){
            return new ArrayList<ErpTemplateSourceVO>();
        }
        return list;
    }
    /**
     * 保存模块和模板凭证信息
     * @param templateVO
     */
    public void saveTemplateAndDtl(ErpTemplateVO templateVO) {
        Map<String,Object> headMap = templateVO.getMap();
        List<Map<String,Object>> childList = templateVO.getList();
        ErpTemplate erpTemplate = new ErpTemplate(templateVO);
        List<ErpTemplateDtl> list = new ArrayList<ErpTemplateDtl>();
        //表头赋值
        for (String key : headMap.keySet()) {
            ErpTemplateUtil.setHeadValueByMap(erpTemplate,key,headMap.get(key));
        }
        //保存模板数据 获得id
        templateDao.save(erpTemplate);
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
        //保存凭证数据
        erpTemplateDtlService.batchSave(list);
    }
    public ErpTemplate selectDtlMapById(String id) {
        String hql = "from ErpTemplate where id=:id";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);
        List list = templateDao.listByHQL(hql,params);
        if(EmptyUtils.isEmptyList(list)){
            return new ErpTemplate();
        }else{
            return (ErpTemplate) list.get(0);
        }
    }
    public boolean findExistCodeMyself(String id, String templateCode) {
        String hql = "from ErpTemplate where templateCode=:code and id!=:id";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("code",templateCode);
        params.put("id",id);
        List<ErpTemplate> list = templateDao.listByHQL(hql,params);
        return !EmptyUtils.isEmptyList(list);
    }
    public void updateTemplateAndDtl(ErpTemplateVO templateVO) {
        Map<String,Object> headMap = templateVO.getMap();
        List<Map<String,Object>> childList = templateVO.getList();
        ErpTemplate erpTemplate = new ErpTemplate(templateVO,System.currentTimeMillis());
        List<ErpTemplateDtl> list = new ArrayList<ErpTemplateDtl>();
        //表头赋值
        for (String key : headMap.keySet()) {
            ErpTemplateUtil.setHeadValueByMap(erpTemplate,key,headMap.get(key));
        }
        //修改type
        templateDao.update(erpTemplate);
        //保存
        if(!EmptyUtils.isEmptyList(childList)){
            for(Map<String,Object> childMap : childList){
                ErpTemplateDtl templateDtl = new ErpTemplateDtl(childMap.containsKey("templateId")?childMap.get("templateId").toString():null,erpTemplate.getId());
                for (String key : childMap.keySet()) {
                    ErpTemplateUtil.setChildValueByMap(templateDtl,key,childMap.get(key));
                }
                list.add(templateDtl);
            }
        }
        erpTemplateDtlService.deleteByTemplateId(erpTemplate.getId());
        erpTemplateDtlService.batchSave(list);
    }
    public List<ErpTemplate> selectTypeListById(String typeId) {
        return templateDao.listBy("templateTypeId",typeId);
    }
    /**
     * 跳转弹窗选择数据加载列表
     * @return
     */
    public DataTablesOutput inputDataTable(DataTablesInput dataTablesInput) {
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String code = (String)formdata.get("code");
        String inputId = (String)formdata.get("inputId");
        ErpDataTypeDTO erpDataTypeDTO = erpDataTypeService.getErpDataTypeDTOByCode(code);
        if(EmptyUtils.isEmpty(erpDataTypeDTO)) return new DataTablesOutput();
        return templateDao.inputDataTable(dataTablesInput,erpDataTypeDTO,inputId);
    }
    /**
     * 根据弹窗控件的code找到对应的name
     * @param code 对应值
     * @param sourceCode 数据来源code
     * @return
     */
    public Object getInputDate(String code, String sourceCode) {
        ErpDataTypeDTO erpDataTypeDTO = erpDataTypeService.getErpDataTypeDTOByCode(sourceCode);
        if(EmptyUtils.isEmpty(erpDataTypeDTO)) return "";
        return templateDao.getInputDate(code,erpDataTypeDTO);
    }
    /**
     * 校验该模板类型下是否存在模板
     * @param id
     */
    public boolean checkDateByTemplateTypeId(String id) {
        return templateDao.checkDateByTemplateTypeId(id);
    }
    /**
     * 校验该模板否存被使用
     * @param id
     */
    public boolean checkDateByTemplateId(String id) {
        return templateDao.checkDateByTemplateId(id);
    }
}
