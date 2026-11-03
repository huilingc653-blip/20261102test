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
import com.bytter.system.api.dto.OperationAreaDTO;
import com.bytter.system.api.dto.SystemProvinceDTO;
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.system.dao.SystemProvinceDao;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.OperationArea;
import com.bytter.system.entity.SystemProvince;
import com.bytter.system.vo.SystemProvinceVo;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("systemProvinceService")
public class SystemProVinceService extends BaseService<SystemProvince, String> implements IExcelVerifyHandler<SystemProvinceVo> {

    @Autowired
    private SystemProvinceDao systemProvinceDao;

    @Autowired
    private CorpService corpService;

    @Autowired
    private OperationAreaService operationAreaService;

    @Override
    public BaseDao<SystemProvince, String> getEntityDao() {
        return systemProvinceDao;
    }

    public DataTablesOutput queryByCodeNadName(DataTablesInput dataTablesInput){
        StringBuilder sql = new StringBuilder("select id as id, province_code as provinceCode, ");
        sql.append(" province_name as provinceName,status status from sys_province where 1 = 1 ");
        return systemProvinceDao.queryByCodeNadName(dataTablesInput, sql.toString());
    }

    public void saveSystemProvince(SystemProvince areaVo, String userLoginName){
        checkData(areaVo);
        areaVo.setSTATUS(BaseDataConstant.STATUS_VALID_ONE);
        areaVo.setCreateBy(userLoginName);
        areaVo.setCreateTime(DateUtils.getDate());
        areaVo.setUpdateBy(userLoginName);
        areaVo.setUpdateTime(DateUtils.getDate());
        super.saveEntity(areaVo);

    }
    public void checkData(SystemProvince vo){
        List <String> param = Lists.newArrayList();
        param.add(vo.getProvinceCode());
        List<SystemProvince> dbs = systemProvinceDao.queryByCodes(param);
        if (dbs.size() > 0){

            throw new ParameterException("省区编码("+vo.getProvinceCode()+")已存在，请重新维护");
        }
    }
    public SystemProvinceVo getByUnion(String id){

       return systemProvinceDao.getByUnion(id);
    }

    public void modify(SystemProvinceVo vo, String userLoginName){
        if (vo == null || StringUtils.isBlank(vo.getId())){
            return;
        }
        for(String id : vo.getId().split(",")){
            SystemProvince systemProvince = systemProvinceDao.get(id, true);
            systemProvince.setProvinceName(StringUtils.isNotBlank(vo.getProvinceName()) ? vo.getProvinceName() : systemProvince.getProvinceName());
            systemProvince.setUpdateBy(userLoginName);
            systemProvince.setUpdateTime(new Date());
            if(StringUtils.isNotBlank(vo.getStatus()) && "1".equals(StringUtils.isNotBlank(vo.getStatus()))){
                systemProvince.setSTATUS("1");
            }else if(StringUtils.isNotBlank(vo.getStatus()) && "0".equals(vo.getStatus())){
                List<OperationAreaDTO> operationAreaDTOS = operationAreaService.queryByProvinceId(vo.getId());
                if(CollectionUtil.isNotEmpty(operationAreaDTOS)){
                    for(OperationAreaDTO dto : operationAreaDTOS){
                        OperationArea operationArea = operationAreaService.get(dto.getId(), true);
                        operationArea.setProvinceId(operationArea.getProvinceId()
                                .replaceAll(","+systemProvince.getId(),"")
                                .replaceAll(systemProvince.getId()+",","")
                                .replaceAll(systemProvince.getId(),""));
                        operationAreaService.saveOrUpdate(operationArea);
                    }
                }
                List<CorpDTO> corpDTOS = corpService.listByAreaIdOrEquityIdOrStoreId(vo.getId(), "4");
                if(CollectionUtil.isNotEmpty(corpDTOS)){
                    for(CorpDTO corpDTO : corpDTOS){
                        Corp corp = corpService.get(corpDTO.getId(),true);
                        corp.setProvinceId(corp.getParentId()
                                .replaceAll(","+systemProvince.getId(),"")
                                .replaceAll(systemProvince.getId()+",","")
                                .replaceAll(systemProvince.getId(),""));
                        corpService.saveOrUpdate(corp);
                    }
                }
                systemProvince.setSTATUS("0");
            }
            systemProvince.setSTATUS(StringUtils.isNotBlank(vo.getStatus()) ? vo.getStatus() : systemProvince.getSTATUS());
            systemProvinceDao.update(systemProvince);
        }
    }

    public void deleteById(String id, String userLoginName){
        SystemProvince systemProvince = systemProvinceDao.get(id, true);
        CorpDTO corpDTO = corpService.queryByAreaIdOrEquityIdOrStoreId(systemProvince.getId(), "4");
        if (corpDTO != null && StringUtils.isNotBlank(corpDTO.getId())){
           throw new BusinessException(corpDTO.getName()+"单位已经关联了该省区，不允许删除");
        }
        List<OperationAreaDTO> operationAreaDTOS = operationAreaService.queryByProvinceId(systemProvince.getId());
        if(CollectionUtil.isNotEmpty(operationAreaDTOS)){
            throw new BusinessException(operationAreaDTOS.get(0).getOperationAreaName()+"营运区已经关联了该省区，不允许删除");
        }
        systemProvinceDao.delete(id);
//        SystemProvinceVo vo = new SystemProvinceVo();
//        SystemProvince.setSTATUS(BaseDataConstant.STATUS_VALID_ZERO);
//        SystemProvince.setUpdateBy(userLoginName);
//        SystemProvince.setUpdateTime(new Date());
//        BeanUtils.copyNotNullProperties( SystemProvince ,vo);
//        this.modify(vo, userLoginName);
    }

    public List<SystemProvince> queryByIds(List<String> ids){

        return systemProvinceDao.queryByIds(ids);

    }

    public List<SystemProvince> queryByCode(String code) {
        return systemProvinceDao.queryByCode(code);
    }

    public List<SystemProvinceVo> getSystemProvince(){
        List<SystemProvince> SystemProvince = systemProvinceDao.getSystemProvince();
        List<SystemProvinceVo> result = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(SystemProvince)){
            result = BeanUtils.copyListProperties(SystemProvince, SystemProvinceVo.class);
        }
        return result;
    }

    public DataTablesOutput selectSystemProvince(DataTablesInput dataTablesInput){
        StringBuilder sql = new StringBuilder("select id as id, province_code as provinceCode, ");
        sql.append(" province_name as provinceName from sys_province where status = '1' ");
        return systemProvinceDao.queryByCodeNadName(dataTablesInput, sql.toString());
    }

    public List<SystemProvinceDTO> queryByCods(List<String> codes){
        List<SystemProvince> systemProvinces = systemProvinceDao.queryByCodes(codes);

        List<SystemProvinceDTO> dtoList = Lists.newArrayList();
        if (!EmptyUtils.isEmpty(systemProvinces)) {
            dtoList = BeanUtils.copyListProperties(systemProvinces, SystemProvinceDTO.class);
        }
        return dtoList;

    }

    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<SystemProvinceVo> list, String userLoginName) {
        if (!list.isEmpty()){
            List<SystemProvince> systemProvinceDbs = systemProvinceDao.queryList(list);
            Map<String, SystemProvince> areaDbMap = Maps.newHashMap();
            List<String> codes = Lists.newArrayList();
            List<String> names = Lists.newArrayList();
            List<SystemProvince> savePo = Lists.newArrayList();

            if (!systemProvinceDbs.isEmpty()){
//                areaDbMap = SystemProvinceDbs.stream().distinct().collect(Collectors.toMap(SystemProvince::getSystemProvinceCode, dto -> dto));
                codes = systemProvinceDbs.stream().distinct().map(SystemProvince::getProvinceCode).collect(Collectors.toList());
                names = systemProvinceDbs.stream().distinct().map(SystemProvince::getProvinceName).collect(Collectors.toList());
            }
            for (SystemProvinceVo vo : list) {
                SystemProvince systemProvince = areaDbMap.get(vo.getProvinceCode());
                if ( systemProvince !=  null && codes.contains(systemProvince.getProvinceCode())){
                    continue;
                }
                if ( systemProvince !=  null && names.contains(systemProvince.getProvinceName())){
                    continue;
                }
                SystemProvince areaDb = new SystemProvince();
                areaDb.setProvinceName(vo.getProvinceName());
                areaDb.setProvinceCode(vo.getProvinceCode());
                areaDb.setSTATUS(BaseDataConstant.STATUS_VALID_ONE);
                areaDb.setCreateBy(userLoginName);
                areaDb.setCreateTime(DateUtils.getDate());
                areaDb.setUpdateBy(userLoginName);
                areaDb.setUpdateTime(DateUtils.getDate());
                savePo.add(areaDb);
            }
            if (!savePo.isEmpty()){
                systemProvinceDao.batchSave(savePo);
            }
        }
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(SystemProvinceVo obj) {
        return null;
    }

    @Override
    public void verifyHandler(List<SystemProvinceVo> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            List<SystemProvince> systemProvinces = this.listAll();
            List<String> names = systemProvinces.stream().distinct().map(SystemProvince::getProvinceName).collect(Collectors.toList());
            List<String> codes = systemProvinces.stream().distinct().map(SystemProvince::getProvinceCode).collect(Collectors.toList());
            for (SystemProvinceVo systemProvinceVo : list) {
                List<String> msgList = Lists.newArrayList();
                if(StringUtils.isNotBlank(systemProvinceVo.getProvinceCode())){
                    if (codes.contains(systemProvinceVo.getProvinceCode())){
                        msgList.add("省区编码已存在");
                    }
                }
                if(StringUtils.isNotBlank(systemProvinceVo.getProvinceName())){
                    if (names.contains(systemProvinceVo.getProvinceName())){
                        msgList.add("省区名称已存在");
                    }
                }
                if (!msgList.isEmpty()){
                    systemProvinceVo.addErrorMsg(msgList);
                }

            }
        }
    }
}
