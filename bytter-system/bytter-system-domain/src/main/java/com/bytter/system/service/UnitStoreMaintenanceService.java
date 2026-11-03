package com.bytter.system.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.system.dao.UnitStoreMaintenanceDao;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.UnitStoreMaintenance;
import com.bytter.system.vo.UnitStoreMaintenanceVo;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("unitStoreMaintenanceService")
public class UnitStoreMaintenanceService extends BaseService<UnitStoreMaintenance, String> implements IExcelVerifyHandler<UnitStoreMaintenanceVo> {

    @Autowired
    private UnitStoreMaintenanceDao unitStoreMaintenanceDao;

    @Autowired
    private CorpService corpService;

    @Override
    public BaseDao<UnitStoreMaintenance, String> getEntityDao() {
        return unitStoreMaintenanceDao;
    }

    public DataTablesOutput queryByCodeNadName(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder("select id as id, store_code as storeCode, ");
        sql.append(" store_name as storeName, corp_id as corpId, corp_code as corpCode, corp_name as corpName   ");
        sql.append(" from sys_unit_store_maintenance where 1 = 1 and status = '1' " );
        Map<String, Object> formdata = dataTablesInput.getFormdata();

        if (formdata.get("corp_Id") != null && !"".equals(formdata.get("corp_Id"))) {
            String corpId = String.valueOf(formdata.get("corp_Id"));
            sql.append(" and corp_id = '" + corpId + "' ");
        }
        if (formdata.get("likes_storeCode") != null && !"".equals(formdata.get("likes_storeCode"))) {
            String code = String.valueOf(formdata.get("likes_storeCode"));
            sql.append(" and store_code = '" + code + "' ");
        }
        if (formdata.get("likes_storeName") != null && !"".equals(formdata.get("likes_storeName"))) {
            String storeName = String.valueOf(formdata.get("likes_storeName"));
            sql.append(" and store_name = '" + storeName + "' ");
        }
        return unitStoreMaintenanceDao.queryByCodeNadName(dataTablesInput, sql.toString());
    }

    public void saveEquityMaintenance(UnitStoreMaintenance  unitStoreMaintenance , String userLoginName) {
//        UnitStoreMaintenance queryDb = unitStoreMaintenanceDao.queryByStroeCodOrName(unitStoreMaintenance.getStoreCode(), unitStoreMaintenance.getStoreName());
//        if (queryDb != null){
//            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_UNION);
//
//        }
        checkData(unitStoreMaintenance);
        Corp corp = corpService.get(unitStoreMaintenance.getCorpId());
        unitStoreMaintenance.setSTATUS("1");
        unitStoreMaintenance.setCorpCode(corp.getCode());
        unitStoreMaintenance.setCorpName(corp.getName());
        unitStoreMaintenance.setCreateBy(userLoginName);
        unitStoreMaintenance.setCreateTime(DateUtils.getDate());
        unitStoreMaintenance.setUpdateBy(userLoginName);
        unitStoreMaintenance.setUpdateTime(DateUtils.getDate());
        super.saveEntity(unitStoreMaintenance);

    }

    public void checkData(UnitStoreMaintenance vo){
        List<String> param = Lists.newArrayList();
        param.add(vo.getStoreCode());
        List<UnitStoreMaintenance> dbs = unitStoreMaintenanceDao.queryByCodes(param);
        if (dbs.size() > 0){
            throw new ParameterException("仓库编码("+vo.getStoreCode()+")已存在，请重新维护");
        }
    }

    public UnitStoreMaintenanceVo getByUnion(String id) {

        return unitStoreMaintenanceDao.getByUnion(id);
    }

    public void modify(UnitStoreMaintenanceVo vo, String userLoginName) {
        if (vo == null) {
            return;
        }
        Corp corp = corpService.get(vo.getCorpId(), true);
        UnitStoreMaintenance unitStoreDb = unitStoreMaintenanceDao.get(vo.getId(), true);
        unitStoreDb.setCorpId(corp.getId());
        unitStoreDb.setCorpName(corp.getName());
        unitStoreDb.setCorpCode(corp.getCode());
        unitStoreDb.setStoreName(vo.getStoreName());
        unitStoreDb.setUpdateBy(userLoginName);
        unitStoreDb.setUpdateTime(new Date());
        unitStoreMaintenanceDao.update(unitStoreDb);
    }

    public void deleteById(String id, String userLoginName) {
        UnitStoreMaintenanceVo vo = new  UnitStoreMaintenanceVo();
        UnitStoreMaintenance unitStoreDb = unitStoreMaintenanceDao.get(id, true);

        CorpDTO corpDTO = corpService.queryByAreaIdOrEquityIdOrStoreId(unitStoreDb.getStoreCode(), "3");
        if (corpDTO != null && StringUtils.isNotBlank(corpDTO.getId())){
            throw new BusinessException(corpDTO.getName()+"单位已经关联了该仓库，不允许删除");
        }
        unitStoreDb.setSTATUS(BaseDataConstant.STATUS_VALID_ZERO);
        BeanUtils.copyNotNullProperties(unitStoreDb,vo);
        this.modify(vo, userLoginName);
    }

    public UnitStoreMaintenance queryById(String id) {
        return unitStoreMaintenanceDao.get(id, true);

    }

    public List<UnitStoreMaintenance> queryByIds(List<String> ids) {
        return unitStoreMaintenanceDao.queryByIds(ids);
    }

    public List<UnitStoreMaintenanceVo> json(){
        List<UnitStoreMaintenanceVo> result = Lists.newArrayList();
        List<UnitStoreMaintenance> dbs = unitStoreMaintenanceDao.json();
        if (CollectionUtil.isNotEmpty(dbs)){
            result = BeanUtils.copyListProperties(dbs, UnitStoreMaintenanceVo.class);
        }
        return result;
    }

    public DataTablesOutput selectUnitStore(DataTablesInput dataTablesInput){
        StringBuilder sql = new StringBuilder("select id as id, store_code as storeCode, ");
        sql.append(" store_name as storeName, corp_id as corpId, corp_code as corpCode, corp_name as corpName   ");
        sql.append(" from sys_unit_store_maintenance where 1 = 1 and status = '1' " );
        Map<String, Object> formdata = dataTablesInput.getFormdata();

        if (formdata.get("corpId") != null && !"".equals(formdata.get("corpId"))) {
            String corpId = String.valueOf(formdata.get("corpId"));
            sql.append(" and corp_id like '%" + corpId + "%' ");
        }
        if (formdata.get("storeCode") != null && !"".equals(formdata.get("storeCode"))) {
            String code = String.valueOf(formdata.get("storeCode"));
            sql.append(" and store_code like '%" + code + "%' ");
        }
        if (formdata.get("storeName") != null && !"".equals(formdata.get("storeName"))) {
            String storeName = String.valueOf(formdata.get("storeName"));
            sql.append(" and store_name like '%" + storeName + "%' ");
        }
        return unitStoreMaintenanceDao.queryByCodeNadName(dataTablesInput, sql.toString());
    }

    public List<UnitStoreMaintenance> queryByCodes(List<String> ids) {
        return unitStoreMaintenanceDao.queryByCodes(ids);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(UnitStoreMaintenanceVo obj) {
        return null;
    }

    @Override
    public void verifyHandler(List<UnitStoreMaintenanceVo> list ) {
        List<String> msgList = null;
        List<UnitStoreMaintenance> unitStoreDbs = this.listAll();

        List<String> storeCodeDbs = unitStoreDbs.stream().distinct().map(UnitStoreMaintenance::getStoreCode).collect(Collectors.toList());
        List<String> storeNameDbs = unitStoreDbs.stream().distinct().map(UnitStoreMaintenance::getStoreName).collect(Collectors.toList());
        List<String> corpCodeDbs = unitStoreDbs.stream().distinct().map(UnitStoreMaintenance::getCorpCode).collect(Collectors.toList());
        List<String> corpNameDbs = unitStoreDbs.stream().distinct().map(UnitStoreMaintenance::getCorpName).collect(Collectors.toList());

        for (UnitStoreMaintenanceVo vo : list) {
            msgList = Lists.newArrayList();
            if (storeCodeDbs.contains(vo.getStoreCode())){
                msgList.add("仓库编码已经存在！");
            }
            if (storeNameDbs.contains(vo.getStoreName())){
                msgList.add("仓库名称已经存在！");
            }
            if (!msgList.isEmpty()){
                vo.addErrorMsg(msgList);
            }
        }
    }

    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<UnitStoreMaintenanceVo> list, String userLoginName) {
        if (!list.isEmpty()){
            List<UnitStoreMaintenance> savePo = Lists.newArrayList();
            List<String> corpCodes = list.stream().distinct().map(UnitStoreMaintenanceVo::getCorpCode).collect(Collectors.toList());
            List<CorpDTO> corpDTOS = corpService.queryCorpDTOByCodes(corpCodes);
            Map<String, CorpDTO> corpMaps = corpDTOS.stream().collect(Collectors.toMap(CorpDTO::getCode, dto -> dto));
            for (UnitStoreMaintenanceVo vo : list) {
                UnitStoreMaintenance unitStore = new UnitStoreMaintenance();
                unitStore.setCorpCode(vo.getCorpCode());
                CorpDTO corpDTO = corpMaps.get(vo.getCorpCode());
                unitStore.setCorpId(corpDTO.getId());
                unitStore.setCorpName(vo.getCorpName());
                unitStore.setStoreName(vo.getStoreName());
                unitStore.setStoreCode(vo.getStoreCode());
                unitStore.setSTATUS(BaseDataConstant.STATUS_VALID_ONE);
                unitStore.setCreateBy(userLoginName);
                unitStore.setCreateTime(DateUtils.getDate());
                unitStore.setUpdateBy(userLoginName);
                unitStore.setUpdateTime(DateUtils.getDate());
                savePo.add(unitStore);
            }
            if (!savePo.isEmpty()){
                unitStoreMaintenanceDao.batchSave(savePo);
            }
        }
    }
}
