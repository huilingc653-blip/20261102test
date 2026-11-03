package com.bytter.biz.inneraccount.service;

import com.bytter.biz.inneraccount.dao.BisAccDtlImportConfigDao;
import com.bytter.biz.inneraccount.dao.BisAccDtlImportConfigdtlDao;
import com.bytter.biz.inneraccount.entity.BisAccDtlImportConfig;
import com.bytter.biz.inneraccount.entity.BisAccDtlImportConfigdtl;
import com.bytter.biz.inneraccount.util.BankAccUtils;
import com.bytter.biz.inneraccount.vo.BisAccDtlImportConfigVO;
import com.bytter.biz.inneraccount.vo.BisAccDtlImportConfigdtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BankTypeDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: 离线流水智能导入配置表Service
 * @Description: BIS_ACC_DTL_IMPORT_CONFIG
 * @date 2020-07-31 15:02:30
 */
@Service("bisAccDtlImportConfigService")
@Transactional
public class BisAccDtlImportConfigService extends BaseService<BisAccDtlImportConfig, String> {
//TODOZ注释的
    @Autowired
    private BisAccDtlImportConfigDao bisAccDtlImportConfigDao;
    @Autowired
    private BisAccDtlImportConfigdtlDao bisAccDtlImportConfigdtlDao;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @Override
    public BaseDao<BisAccDtlImportConfig, String> getEntityDao() {
        return bisAccDtlImportConfigDao;
    }
    /**
     * 列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisAccDtlImportConfigVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = bisAccDtlImportConfigDao.getDataTablesUnion(dataTablesInput);
        List<BisAccDtlImportConfigVO> bisAccDtlImportConfigVOList = dataTablesOutput.getData();
        for(BisAccDtlImportConfigVO bisAccDtlImportConfigVO:bisAccDtlImportConfigVOList){
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(bisAccDtlImportConfigVO.getBankTypeId());
            if(!EmptyUtils.isEmpty(bankTypeDTO)){
                bisAccDtlImportConfigVO.setBankTypeName(bankTypeDTO.getTypeName());
            }
        }
        return dataTablesOutput;
    }
    /**
     * 根据ID获取离线流水智能导入配置信息
     *
     * @param id
     * @return
     */
    public BisAccDtlImportConfigVO getByIdUnion(String id) {
        return bisAccDtlImportConfigDao.getByIdUnion(id);
    }
    /**
     * 校验是否存在相同银行类别且相同文件类型的规则
     * @param bankTypeId
     * @param fileType
     * @param id
     * @return
     */
    public boolean isExistFileType(String bankTypeId, String fileType, String id) {
        List<BisAccDtlImportConfigVO> list = bisAccDtlImportConfigDao.listByBankTypeIdAndFileType(bankTypeId, fileType, id);
        if(EmptyUtils.isNotEmpty(list)){
            return true;
        }
        return false;
    }
    /**
     * 根据银行类型和文件类型获取导入配置信息
     *
     * @param bankTypeId
     * @param fileType
     * @return
     */
    public BisAccDtlImportConfigVO getByBankTypeIdAndFileType(String bankTypeId, String fileType) {
        List<BisAccDtlImportConfigVO> list = bisAccDtlImportConfigDao.listByBankTypeIdAndFileType(bankTypeId, fileType, null);
        if(EmptyUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }
    /**
     * 查询离线流水导入配置信息
     * @param bisAccDtlImportConfigVO
     * @return
     */
    public List<BisAccDtlImportConfigVO> listByUnion(BisAccDtlImportConfigVO bisAccDtlImportConfigVO){
        return bisAccDtlImportConfigDao.listByUnion(bisAccDtlImportConfigVO);
    }
    /**
     * 获取除TXT以外业务类型数据 取结束的的业务数据
     * @param bisAccDtlImportConfigVO
     * @return
     */
    public List<Map<String,String>> getTableInfo(BisAccDtlImportConfigVO bisAccDtlImportConfigVO){
        List<Map<String,String>> defaultList = new ArrayList<Map<String,String>>();
        if(!StringUtils.isBlank(bisAccDtlImportConfigVO.getId()) || !StringUtils.isBlank(bisAccDtlImportConfigVO.getFileType())){
            List<BisAccDtlImportConfigVO> tableList = this.listByUnion(bisAccDtlImportConfigVO);
            if(EmptyUtils.isNotEmpty(tableList)){
                Map<String,String> valueMap=new HashMap<String,String>();
                for(BisAccDtlImportConfigVO info:tableList){
                    valueMap.put(info.getFieldKey(), info.getStartVal());
                }
                defaultList = BankAccUtils.getBisAccDtlFieldConfigList();
                for(Map<String,String> info:defaultList){
                    if(valueMap.containsKey(info.get("key"))){
                        info.put("value", valueMap.get(info.get("key")));
                    }
                }
            }
        }
        if(EmptyUtils.isEmpty(defaultList)){
            defaultList = BankAccUtils.getBisAccDtlFieldConfigList();
            for(Map<String,String> info:defaultList){
                info.put("value", "");
            }
        }
        return defaultList;
    }
    /**
     * 保存或更新
     * @param bisAccDtlImportConfigVO
     */
    public void saveOrUpdate(BisAccDtlImportConfigVO bisAccDtlImportConfigVO, ICurrentUser user){
        BisAccDtlImportConfig bisAccDtlImportConfig = null;
        if(StringUtils.isBlank(bisAccDtlImportConfigVO.getId())){
            bisAccDtlImportConfig = new BisAccDtlImportConfig();
            BeanUtils.copyNotNullProperties(bisAccDtlImportConfigVO, bisAccDtlImportConfig);
            bisAccDtlImportConfig.setCreateBy(user.getLoginName());
            bisAccDtlImportConfig.setCreateDate(DateUtils.getDate());
            this.saveEntity(bisAccDtlImportConfig);
        }else{
            bisAccDtlImportConfig = this.get(bisAccDtlImportConfigVO.getId());
            BeanUtils.copyNotNullProperties(bisAccDtlImportConfigVO, bisAccDtlImportConfig);
            bisAccDtlImportConfig.setUpdateBy(user.getLoginName());
            bisAccDtlImportConfig.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(bisAccDtlImportConfig);
            //删除明细
            bisAccDtlImportConfigdtlDao.delByIcId(bisAccDtlImportConfig.getId());
        }
        for(int i=1;i<bisAccDtlImportConfigVO.getDtlList().size();i++){
            BisAccDtlImportConfigdtlVO bisAccDtlImportConfigdtlVO =bisAccDtlImportConfigVO.getDtlList().get(i);
            BisAccDtlImportConfigdtl bisAccDtlImportConfigdtl = new BisAccDtlImportConfigdtl();
            bisAccDtlImportConfigdtl.setIcId(bisAccDtlImportConfig.getId());
            bisAccDtlImportConfigdtl.setFieldKey(bisAccDtlImportConfigdtlVO.getFieldKey());
            bisAccDtlImportConfigdtl.setStartVal(bisAccDtlImportConfigdtlVO.getStartVal());
            bisAccDtlImportConfigdtl.setEndVal(bisAccDtlImportConfigdtlVO.getEndVal());
            bisAccDtlImportConfigdtlDao.save(bisAccDtlImportConfigdtl);
        }
    }
    /**
     * 删除
     * @param id
     */
    public void delelteUnion(String id){
        //删除明细
        bisAccDtlImportConfigdtlDao.delByIcId(id);
        //删除主表
        BisAccDtlImportConfig bisAccDtlImportConfig = this.get(id);
        this.deleteEntity(bisAccDtlImportConfig);
    }
    /**
     * 获取txt的模板
     * @param bisAccDtlImportConfigVO
     * @return
     */
    public List<Map<String,String>> getTxtModelAnalysis(BisAccDtlImportConfigVO bisAccDtlImportConfigVO){
        List<BisAccDtlImportConfigVO> tableList = this.listByUnion(bisAccDtlImportConfigVO);
        Map<String,String> valueMap=new HashMap<String,String>();
        for(BisAccDtlImportConfigVO info:tableList){
            valueMap.put(info.getFieldKey(),info.getStartVal());
        }
        List<Map<String,String>> configList= null;//BankAccUtils.getBisAccDtlFieldConfigList();
        for(Map<String,String> info:configList){
            if(valueMap.containsKey(info.get("key"))){
                info.put("value", valueMap.get(info.get("key")));
            }
        }
        return configList;
    }
}
