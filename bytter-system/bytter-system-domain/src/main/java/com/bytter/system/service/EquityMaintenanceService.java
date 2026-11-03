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
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.system.dao.EquityMaintenanceDao;
import com.bytter.system.entity.EquityMaintenance;
import com.bytter.system.vo.EquityMaintenanceVo;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("equityMaintenanceService")
public class EquityMaintenanceService extends BaseService<EquityMaintenance, String> implements IExcelVerifyHandler<EquityMaintenanceVo> {

    @Autowired
    private EquityMaintenanceDao equityMaintenanceDao;

    @Autowired
    private CorpService corpService;


    @Override
    public ExcelVerifyHanlderResult verifyHandler(EquityMaintenanceVo obj) {
        return null;
    }

    @Override
    public void verifyHandler(List<EquityMaintenanceVo> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> msgList = Lists.newArrayList();
            List<EquityMaintenance> equityMaintenances = this.listAll();
            List<String> names = equityMaintenances.stream().distinct().map(EquityMaintenance::getEquityName).collect(Collectors.toList());
            List<String> codes = equityMaintenances.stream().distinct().map(EquityMaintenance::getEquityCode).collect(Collectors.toList());
            for (EquityMaintenanceVo vo : list) {
                msgList = Lists.newArrayList();
                if(StringUtils.isNotBlank(vo.getEquityCode())){
                    if (codes.contains(vo.getEquityCode())){
                        msgList.add("股权编码已存在");
                    }
                }
                if(StringUtils.isNotBlank(vo.getEquityName())){
                    if (names.contains(vo.getEquityName())){
                        msgList.add("股权名称已存在");
                    }
                }
                if (!msgList.isEmpty()){
                    vo.addErrorMsg(msgList);
                }
            }
        }
    }


    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<EquityMaintenanceVo> list, String userLoginName) {
        if (!list.isEmpty()){
            List<EquityMaintenance> savePo = Lists.newArrayList();
            for (EquityMaintenanceVo vo : list) {
                EquityMaintenance areaDb = new EquityMaintenance();
                areaDb.setEquityName(vo.getEquityName());
                areaDb.setEquityCode(vo.getEquityCode());
                areaDb.setSTATUS(BaseDataConstant.STATUS_VALID_ONE);
                areaDb.setCreateBy(userLoginName);
                areaDb.setCreateTime(DateUtils.getDate());
                areaDb.setUpdateBy(userLoginName);
                areaDb.setUpdateTime(DateUtils.getDate());
                savePo.add(areaDb);
            }
            if (!savePo.isEmpty()){
                equityMaintenanceDao.batchSave(savePo);
            }
        }
    }


    @Override
    public BaseDao<EquityMaintenance, String> getEntityDao() {
        return equityMaintenanceDao;
    }

    public DataTablesOutput queryByCodeNadName(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder("select id as id, equity_code as equityCode, ");
        sql.append(" equity_name as equityName from sys_equity_maintenance where 1 = 1 and status = '1' ");
        Map<String, Object> formdata = dataTablesInput.getFormdata();

        if (formdata.get("likes_name") != null && !"".equals(formdata.get("likes_name"))) {
            String name = String.valueOf(formdata.get("likes_name"));
            sql.append(" and equity_name like '%" + name + "%' ");
        }
        if (formdata.get("likes_code") != null && !"".equals(formdata.get("likes_code"))) {
            String code = String.valueOf(formdata.get("likes_code"));
            sql.append(" and equity_code like '%" + code + "%' ");
        }
        return equityMaintenanceDao.queryByCodeAndName(dataTablesInput, sql.toString());
    }

    public void saveEquityMaintenance(EquityMaintenance equityMaintenance, String userLoginName) {
        //检查数据
        checkData(equityMaintenance);
        equityMaintenance.setCreateBy(userLoginName);
        equityMaintenance.setCreateTime(DateUtils.getDate());
        equityMaintenance.setUpdateBy(userLoginName);
        equityMaintenance.setUpdateTime(DateUtils.getDate());
        equityMaintenance.setSTATUS("1");
        super.saveEntity(equityMaintenance);

    }

    public void checkData(EquityMaintenance vo){
        List <String> param = Lists.newArrayList();
        param.add(vo.getEquityCode());
        List<EquityMaintenance> dbs = equityMaintenanceDao.queryByCodes(param);
        if (dbs.size() > 0){
            throw new ParameterException("股权编码("+vo.getEquityCode()+")已存在，请重新维护");
        }
    }

    public EquityMaintenanceVo getByUnion(String id) {

        return equityMaintenanceDao.getByUnion(id);
    }

    public void modify(EquityMaintenanceVo vo, String userLoginName) {
        if (vo == null) {
            return;
        }
        EquityMaintenance equityMaintenance = equityMaintenanceDao.get(vo.getId(), true);
        equityMaintenance.setEquityName(vo.getEquityName());
        equityMaintenance.setUpdateBy(userLoginName);
        equityMaintenance.setUpdateTime(new Date());
        equityMaintenanceDao.update(equityMaintenance);
    }

    public void deleteById(String id, String userLoginName) {
        EquityMaintenance equity = equityMaintenanceDao.get(id, true);
        EquityMaintenanceVo vo = new EquityMaintenanceVo();
        CorpDTO corpDTO = corpService.queryByAreaIdOrEquityIdOrStoreId(equity.getEquityCode(), "2");
        if (corpDTO != null && StringUtils.isNotBlank(corpDTO.getId())){
            throw new BusinessException(corpDTO.getName()+"单位已经关联了该股权，不允许刪除");
        }
        equity.setSTATUS(BaseDataConstant.STATUS_VALID_ZERO);
        BeanUtils.copyNotNullProperties(equity,vo);
        this.modify(vo, userLoginName);
    }

    public EquityMaintenance queryById(String id) {
        return equityMaintenanceDao.get(id, true);

    }

    public List<EquityMaintenance> queryByIds(List<String> ids) {
        return equityMaintenanceDao.queryByIds(ids);
    }

    public List<EquityMaintenance> queryByCodes(List<String> codes) {
        return equityMaintenanceDao.queryByCodes(codes);
    }


    public List<EquityMaintenanceVo> json(){
        List<EquityMaintenance> dbList = equityMaintenanceDao.json();

        List<EquityMaintenanceVo> result = Lists.newArrayList();
       if (CollectionUtil.isNotEmpty(dbList)){
           result = BeanUtils.copyListProperties(dbList, EquityMaintenanceVo.class);
       }
        return result;

    }
}
