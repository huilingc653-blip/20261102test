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
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.system.dao.OperationAreaDao;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.OperationArea;
import com.bytter.system.entity.SystemProvince;
import com.bytter.system.vo.OperationAreaVo;
import com.bytter.system.vo.SystemProvinceVo;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("operationAreaService")
public class OperationAreaService extends BaseService<OperationArea, String> implements IExcelVerifyHandler<OperationAreaVo> {

    @Autowired
    private OperationAreaDao operationAreaDao;

    @Autowired
    private CorpService corpService;

    @Autowired
    private SystemProVinceService systemProVinceService;

    @Override
    public BaseDao<OperationArea, String> getEntityDao() {
        return operationAreaDao;
    }

    public DataTablesOutput queryByCodeNadName(DataTablesInput dataTablesInput){
        StringBuilder sql = new StringBuilder("select id as id, operation_area_code as operationAreaCode, ");
        sql.append(" operation_area_name as operationAreaName,status status,province_id provinceId from sys_operation_area where 1 = 1 ");

        if(dataTablesInput.getFormdata().get("likes_provinceCode") != null && StringUtils.isNotBlank(dataTablesInput.getFormdata().get("likes_provinceCode").toString())
        || dataTablesInput.getFormdata().get("likes_provinceName") != null && StringUtils.isNotBlank(dataTablesInput.getFormdata().get("likes_provinceName").toString())){
            DataTablesInput dataTablesInput2 = new DataTablesInput();
            dataTablesInput2.setLength(Integer.MAX_VALUE);
            Map<String,Object> map = new HashMap<>();
            map.put("likes_provinceCode",dataTablesInput.getFormdata().get("likes_provinceCode"));
            map.put("likes_provinceName",dataTablesInput.getFormdata().get("likes_provinceName"));
            dataTablesInput2.setFormdata(map);
            DataTablesOutput output2 = systemProVinceService.queryByCodeNadName(dataTablesInput2);
            if(output2 != null && CollectionUtil.isNotEmpty(output2.getData())){
                sql.append(" and (");
                for(int i = 0; i < output2.getData().size(); i++){
                    if(i > 0){
                        sql.append(" or ");
                    }
                    sql.append(" LOCATE('"+((SystemProvinceVo)output2.getData().get(i)).getId()+"', PROVINCE_ID) > 0 ");
                }
                sql.append(" ) ");
            }
        }

        DataTablesOutput output = operationAreaDao.queryByCodeNadName(dataTablesInput, sql.toString());
        if(output == null || CollectionUtil.isEmpty(output.getData())){
            return output;
        }
        for(Object o : output.getData()){
            OperationAreaVo vo = (OperationAreaVo)o;
            if(StringUtils.isBlank(vo.getProvinceId())){
                continue;
            }
            String[] split = vo.getProvinceId().split(",");
            String provinceCodes = "";
            String provinceNames = "";
            for(String pid : split){
                SystemProvince systemProvince = systemProVinceService.get(pid,true);
                if("".equals(provinceCodes)){
                    provinceCodes = systemProvince.getProvinceCode();
                }else {
                    provinceCodes = provinceCodes + "," + systemProvince.getProvinceCode();
                }
                if("".equals(provinceNames)){
                    provinceNames = systemProvince.getProvinceName();
                }else {
                    provinceNames = provinceNames + "," + systemProvince.getProvinceName();
                }
                vo.setProvinceCode(provinceCodes);
                vo.setProvinceName(provinceNames);
            }
        }
        return output;
    }

    public void saveOperationArea(OperationArea areaVo, String userLoginName){
        checkData(areaVo);
        areaVo.setSTATUS(BaseDataConstant.STATUS_VALID_ONE);
        areaVo.setCreateBy(userLoginName);
        areaVo.setCreateTime(DateUtils.getDate());
        areaVo.setUpdateBy(userLoginName);
        areaVo.setUpdateTime(DateUtils.getDate());
        super.saveEntity(areaVo);

    }
    public void checkData(OperationArea vo){
        List <String> param = Lists.newArrayList();
        param.add(vo.getOperationAreaCode());
        List<OperationArea> dbs = operationAreaDao.queryByCodes(param);
        if (dbs.size() > 0){

            throw new ParameterException("营运区编码("+vo.getOperationAreaCode()+")已存在，请重新维护");
        }
    }
    public OperationAreaVo getByUnion(String id){
        OperationAreaVo vo = operationAreaDao.getByUnion(id);
        if(StringUtils.isBlank(vo.getProvinceId())){
            return vo;
        }
        String[] split = vo.getProvinceId().split(",");
        String provinceCodes = "";
        String provinceNames = "";
        for(String pid : split){
            SystemProvince systemProvince = systemProVinceService.get(pid,true);
            if("".equals(provinceCodes)){
                provinceCodes = systemProvince.getProvinceCode();
            }else {
                provinceCodes = provinceCodes + "," + systemProvince.getProvinceCode();
            }
            if("".equals(provinceNames)){
                provinceNames = systemProvince.getProvinceName();
            }else {
                provinceNames = provinceNames + "," + systemProvince.getProvinceName();
            }
            vo.setProvinceCode(provinceCodes);
            vo.setProvinceName(provinceNames);
        }
        return vo;
    }

    public void modify(OperationAreaVo vo, String userLoginName){
        if (vo == null || StringUtils.isBlank(vo.getId())){
            return;
        }
        for(String vid : vo.getId().split(",")){
            OperationArea operationArea = operationAreaDao.get(vid, true);
            operationArea.setOperationAreaName(StringUtils.isNotBlank(vo.getOperationAreaName()) ? vo.getOperationAreaName() : operationArea.getOperationAreaName());
            operationArea.setUpdateBy(userLoginName);
            operationArea.setUpdateTime(new Date());
            operationArea.setProvinceId(StringUtils.isNotBlank(vo.getProvinceId()) ? vo.getProvinceId() : operationArea.getProvinceId());
            if(StringUtils.isNotBlank(vo.getStatus()) && "1".equals(vo.getStatus())){
                operationArea.setSTATUS("1");
            }else if(StringUtils.isNotBlank(vo.getStatus()) && "0".equals(vo.getStatus())){
                List<CorpDTO> corpDTOS = corpService.listByAreaIdOrEquityIdOrStoreId(operationArea.getOperationAreaCode(), "1");
                if(CollectionUtil.isNotEmpty(corpDTOS)){
                    for(CorpDTO corpDTO : corpDTOS){
                        Corp corp = corpService.get(corpDTO.getId(),true);
                        corp.setOperationAreaId(corp.getOperationAreaId()
                                .replaceAll(","+operationArea.getOperationAreaCode(),"")
                                .replaceAll(operationArea.getOperationAreaCode()+",","")
                                .replaceAll(operationArea.getOperationAreaCode(),""));
                        corpService.saveOrUpdate(corp);
                    }
                }
                operationArea.setSTATUS("0");
            }
            operationAreaDao.update(operationArea);
        }
    }

    public void deleteById(String id, String userLoginName){
        if(StringUtils.isBlank(id)){
            return;
        }
        OperationArea operationArea = operationAreaDao.get(id, true);
        CorpDTO corpDTO = corpService.queryByAreaIdOrEquityIdOrStoreId(operationArea.getOperationAreaCode(), "1");
        if (corpDTO != null && StringUtils.isNotBlank(corpDTO.getId())){
           throw new BusinessException(corpDTO.getName()+"单位已经关联了该营运区，不允许删除");
        }
        delete(id);
    }

    public List<OperationArea> queryByIds(List<String> ids){

        return operationAreaDao.queryByIds(ids);

    }

    public List<OperationArea> queryByCode(String code) {
        return operationAreaDao.queryByCode(code);
    }

    public List<OperationAreaVo> getOperationArea(){
        List<OperationArea> operationArea = operationAreaDao.getOperationArea();
        List<OperationAreaVo> result = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(operationArea)){
            result = BeanUtils.copyListProperties(operationArea, OperationAreaVo.class);
        }
        return result;
    }

    public List<OperationAreaDTO> queryByCods(List<String> codes){
        List<OperationArea> operationAreas = operationAreaDao.queryByCodes(codes);

        List<OperationAreaDTO> dtoList = Lists.newArrayList();
        if (!EmptyUtils.isEmpty(operationAreas)) {
            dtoList = BeanUtils.copyListProperties(operationAreas, OperationAreaDTO.class);
        }
        return dtoList;

    }

    public List<OperationAreaDTO> queryByProvinceId(String provinces){
        List<OperationArea> operationAreas = operationAreaDao.queryByProvinceId(provinces);

        List<OperationAreaDTO> dtoList = Lists.newArrayList();
        if (!EmptyUtils.isEmpty(operationAreas)) {
            dtoList = BeanUtils.copyListProperties(operationAreas, OperationAreaDTO.class);
        }
        return dtoList;

    }

    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<OperationAreaVo> list, String userLoginName) {
        if (!list.isEmpty()){
            List<OperationArea> operationAreaDbs = operationAreaDao.queryList(list);
            Map<String, OperationArea> areaDbMap = Maps.newHashMap();
            List<String> codes = Lists.newArrayList();
            List<String> names = Lists.newArrayList();
            List<OperationArea> savePo = Lists.newArrayList();

            if (!operationAreaDbs.isEmpty()){
//                areaDbMap = operationAreaDbs.stream().distinct().collect(Collectors.toMap(OperationArea::getOperationAreaCode, dto -> dto));
                codes = operationAreaDbs.stream().distinct().map(OperationArea::getOperationAreaCode).collect(Collectors.toList());
                names = operationAreaDbs.stream().distinct().map(OperationArea::getOperationAreaName).collect(Collectors.toList());
            }
            for (OperationAreaVo vo : list) {
                OperationArea operationArea = areaDbMap.get(vo.getOperationAreaCode());
                if ( operationArea !=  null && codes.contains(operationArea.getOperationAreaCode())){
                    continue;
                }
                if ( operationArea !=  null && names.contains(operationArea.getOperationAreaName())){
                    continue;
                }
                String provinceId = "";
                if(StringUtils.isNotBlank(vo.getProvinceCode()) && StringUtils.isNotBlank(vo.getProvinceName())){
                    String[] splitCode = vo.getProvinceCode().split(",");
                    String[] splitName = vo.getProvinceName().split(",");
                    if(splitCode.length != splitName.length){
                        continue;
                    }
                    for(int i = 0; i < splitCode.length; i++){
                        List<SystemProvince> systemProvinces = systemProVinceService.queryByCode(splitCode[i]);
                        if(CollectionUtil.isEmpty(systemProvinces)){
                            continue;
                        }
                        if(!systemProvinces.get(0).getProvinceName().equals(splitName[i])){
                            continue;
                        }
                        if(i == 0){
                            provinceId = systemProvinces.get(0).getId();
                        }else {
                            provinceId = provinceId + "," + systemProvinces.get(0).getId();
                        }
                    }
                    if("".equals(provinceId)){
                        continue;
                    }
                }
                OperationArea areaDb = new OperationArea();
                areaDb.setOperationAreaName(vo.getOperationAreaName());
                areaDb.setOperationAreaCode(vo.getOperationAreaCode());
                areaDb.setSTATUS(BaseDataConstant.STATUS_VALID_ONE);
                areaDb.setCreateBy(userLoginName);
                areaDb.setCreateTime(DateUtils.getDate());
                areaDb.setUpdateBy(userLoginName);
                areaDb.setUpdateTime(DateUtils.getDate());
                areaDb.setProvinceId(provinceId);
                savePo.add(areaDb);
            }
            if (!savePo.isEmpty()){
                operationAreaDao.batchSave(savePo);
            }
        }
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(OperationAreaVo obj) {
        return null;
    }

    @Override
    public void verifyHandler(List<OperationAreaVo> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            List<OperationArea> operationAreas = this.listAll();
            List<String> names = operationAreas.stream().distinct().map(OperationArea::getOperationAreaName).collect(Collectors.toList());
            List<String> codes = operationAreas.stream().distinct().map(OperationArea::getOperationAreaCode).collect(Collectors.toList());
            for (OperationAreaVo operationAreaVo : list) {
                List<String> msgList = Lists.newArrayList();
                if(StringUtils.isNotBlank(operationAreaVo.getOperationAreaCode())){
                    if (codes.contains(operationAreaVo.getOperationAreaCode())){
                        msgList.add("营运区编码已存在");
                    }
                }
                if(StringUtils.isNotBlank(operationAreaVo.getOperationAreaName())){
                    if (names.contains(operationAreaVo.getOperationAreaName())){
                        msgList.add("营运区名称已存在");
                    }
                }
                if(StringUtils.isNotBlank(operationAreaVo.getProvinceCode()) && StringUtils.isNotBlank(operationAreaVo.getProvinceName())){
                    String[] splitCode = operationAreaVo.getProvinceCode().split(",");
                    String[] splitName = operationAreaVo.getProvinceName().split(",");
                    if(splitCode.length != splitName.length){
                        msgList.add("所属省区编码和所属省区名称个数不相等");
                    }

                    for(int i = 0; i < splitCode.length; i++){
                        List<SystemProvince> systemProvinces = systemProVinceService.queryByCode(splitCode[i]);
                        if(CollectionUtil.isEmpty(systemProvinces)){
                            msgList.add("所属省区编码"+splitCode[i]+"不存在");
                        }
                        if(!systemProvinces.get(0).getProvinceName().equals(splitName[i])){
                            msgList.add("所属省区名称："+splitName[i]+"，不存在");
                        }

                    }
                }
                if (!msgList.isEmpty()){
                    operationAreaVo.addErrorMsg(msgList);
                }

            }
        }
    }

    public List<OperationArea> queryByCodes(List<String> codes) {
        return operationAreaDao.queryByCodes(codes);
    }
}
