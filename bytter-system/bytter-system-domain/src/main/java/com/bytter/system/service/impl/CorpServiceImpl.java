package com.bytter.system.service.impl;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.bean.FexCorpExposureVO;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.OperationAreaDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.enums.CorpMessageEnum;
import com.bytter.system.dao.CorpDao;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.SystemProvince;
import com.bytter.system.entity.UnitStoreMaintenance;
import com.bytter.system.service.CorpService;
import com.bytter.system.service.SystemProVinceService;
import com.bytter.system.service.UnitStoreMaintenanceService;
import com.bytter.system.vo.CorpComponentVO;
import com.bytter.system.vo.CorpVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * Created by zhoul on 2017/5/23.
 */
@Service("corpServiceI")
@DubboService
public class CorpServiceImpl implements ICorpService {
    @Autowired
    private CorpService corpService;
    @Autowired
    private CorpDao corpDao;

    @Autowired UserServiceImpl userServiceImpl;

    @Autowired
    private SystemProVinceService systemProVinceService;

    @Autowired
    private UnitStoreMaintenanceService unitStoreMaintenanceService;

    @Autowired
    private OperationAreaServiceImpl operationAreaServiceImpl;

    @Override
    public CorpDTO get(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_ID_NULL);
        }
        Corp corp = corpService.get(id, true);
        if (EmptyUtils.isEmpty(corp)) {
            return null;
        }
        CorpDTO corpDTO = new CorpDTO();
        BeanUtils.copyProperties(corp, corpDTO);
        String provinceName = "";
        if(StringUtils.isNotBlank(corpDTO.getProvinceId())){
            for(String pid : corpDTO.getProvinceId().split(",")){
                if(StringUtils.isBlank(pid)){
                    continue;
                }
                SystemProvince systemProvince = systemProVinceService.get(pid,true);
                if("".equals(provinceName)){
                    provinceName = systemProvince.getProvinceName();
                }else {
                    provinceName = provinceName + "," + systemProvince.getProvinceName();
                }
            }
            corpDTO.setProvinceName(provinceName);
        }
        return corpDTO;
    }
    @Override
    public CorpDTO getParentById(String id) {
        Corp corp = corpService.getParentById(id);
        if (corp == null) {
            return null;
        }
        return BeanUtils.copyNewInstanceProperties(corp, CorpDTO.class);
    }
    @Override
    public List<CorpDTO> listNet() {
        List<Corp> corpList = corpDao.listCorpByTypeAndStatus(null, CorpConstant.CORP_TYPE_CODE_NET, CorpConstant.CORP_STATUS_ENABLE);
        if (EmptyUtils.isEmptyList(corpList)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(corpList, CorpDTO.class);
    }

    @Override
    public CorpDTO getUserNet(ICurrentUser user) {
        UserDTO userDTO = userServiceImpl.getByLoginName(user.getLoginName());
        CorpDTO corpDto = userDTO.getCorp();
        while (CorpConstant.CORP_TYPE_CODE_CORP.toString().equals(corpDto.getType().toString())) {
            String parentId = corpDto.getParentId();
            if (!StringUtils.isEmpty(parentId)) {
                corpDto = this.get(parentId);
            } else {
                break;
            }
        }
        return corpDto;
    }

    @Override
    public List<CorpDTO> listCorpByNetId(String netId) {
        List<Corp> corpList = corpDao.listCorpByTypeAndStatus(netId, CorpConstant.CORP_TYPE_CODE_CORP, CorpConstant.CORP_STATUS_ENABLE);
        if (EmptyUtils.isEmptyList(corpList)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(corpList, CorpDTO.class);
    }
    @Override
    public List<CorpDTO> listCorpBasicInfoByNetId(String netId) {
        List<Corp> corpList = corpDao.listCorpBasicInfoByTypeAndStatus(netId, CorpConstant.CORP_TYPE_CODE_CORP, CorpConstant.CORP_STATUS_ENABLE);
        if (EmptyUtils.isEmptyList(corpList)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(corpList, CorpDTO.class);
    }
    /**
     * 获取有效的网点、单位
     *
     * @return
     */
    @Override
    public List<CorpDTO> listEffectiveCorp() {
        List<Corp> corpList = corpService.listEffectiveCorp();
        if (EmptyUtils.isEmptyList(corpList)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(corpList, CorpDTO.class);
    }
    /**
     * 获取有效的网点、单位
     *
     * @return
     */
    @Override
    public List<CorpDTO> listEffectiveCorpBasicInfo() {
        List<CorpDTO> result = Lists.newArrayList();
        List<Corp> corpList = corpService.listEffectiveCorpBaiscInfo();
        if (EmptyUtils.isEmptyList(corpList)) {
            return result;
        }
        for (Corp corp : corpList) {
            CorpDTO corpDTO = new CorpDTO();
            BeanUtils.copyNotNullProperties(corp, corpDTO);
            result.add(corpDTO);
        }
        return result;
    }
    /**
     * 单位集合树形拼装
     *
     * @param corpDTOList
     * @return
     */
    @Override
    public List<CorpDTO> listCorpTree(List<CorpDTO> corpDTOList) {
        List<Corp> corpList = BeanUtils.copyListProperties(corpDTOList, Corp.class);
        if (EmptyUtils.isEmptyList(corpList)) {
            return null;
        }
        List<CorpVO> corpListNew = corpService.transformToTreeWithOutInject(corpList);
        corpListNew = TreeNodeUtils.transformToList(corpListNew);
        if (EmptyUtils.isEmptyList(corpListNew)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(corpListNew, CorpDTO.class);
    }
    @Override
    public List<CorpDTO> listLevelCorpAndChildrenCorp(String resource, String userId, int level) {
        if (level <= 0) {
            return new ArrayList<>();
        }
        // 获取有效的网点、单位
        List<Corp> corpList = corpService.listEffectiveCorp();
        if (EmptyUtils.isEmptyList(corpList)) {
            return new ArrayList<>();
        }
        List<CorpVO> corpListNew = corpService.transformToTree(corpList);
        if (EmptyUtils.isEmptyList(corpListNew)) {
            return new ArrayList<>();
        }
        Set<String> havePermissionCorpIds = new HashSet<>(getUserCorpIds(resource, userId));
        List<CorpVO> levelCorpList = new ArrayList<>();
        // 递归遍历获取指定层级的成员单位
        dfsLevelCorp(1, level, levelCorpList, corpListNew);
        // 根据权限进行过滤
        levelCorpList = getHasPermissionCorp(levelCorpList, havePermissionCorpIds);
        List<CorpDTO> corpDTOList = new ArrayList<>(levelCorpList.size());
        for (CorpVO corpVO : levelCorpList) {
            CorpDTO corpDTO = new CorpDTO();
            BeanUtils.copyProperties(corpVO, corpDTO);
            corpDTO.setChildrenCorp(getChildrenCorp(corpVO));
            corpDTOList.add(corpDTO);
        }
        return corpDTOList;
    }
    /**
     * 获取有权限的VO
     *
     * @param levelCorpList
     * @param havePermissionCorpIds
     * @return
     */
    private List<CorpVO> getHasPermissionCorp(List<CorpVO> levelCorpList, Set<String> havePermissionCorpIds) {
        List<CorpVO> voList = new ArrayList<>();
        for (CorpVO corpVO : levelCorpList) {
            if (havePermissionCorpIds.contains(corpVO.getId())) {
                voList.add(corpVO);
            }
        }
        return voList;
    }
    /**
     * 递归获取 指定成员单位下面所有的子单位
     *
     * @param corpVO VO对象
     * @return 指定成员单位下面所有的子单位
     */
    private List<CorpDTO> getChildrenCorp(CorpVO corpVO) {
        List<CorpVO> childrenCorps = new ArrayList<>();
        dfsGetChildrenCorp(childrenCorps, corpVO.getChildren());
        return BeanUtils.copyListProperties(childrenCorps, CorpDTO.class);
    }
    /**
     * 递归获取 指定成员单位下面所有的子单位
     *
     * @param childrenCorps 子单位集合（存放结果）
     * @param dataCorpList  子单位数据
     */
    private void dfsGetChildrenCorp(List<CorpVO> childrenCorps, List<CorpVO> dataCorpList) {
        if (CollectionUtils.isEmpty(dataCorpList)) {
            return;
        }
        for (CorpVO corpVO : dataCorpList) {
            childrenCorps.add(corpVO);
            dfsGetChildrenCorp(childrenCorps, corpVO.getChildren());
        }
    }
    /**
     * 递归遍历获取指定层级的成员单位
     *
     * @param currentLevel
     * @param targetLevel
     * @param targetCorpList 指定层级的成员单位
     * @param dataCorpList
     */
    private void dfsLevelCorp(int currentLevel,
                              int targetLevel,
                              List<CorpVO> targetCorpList,
                              List<CorpVO> dataCorpList) {
        if (currentLevel == targetLevel) {
            targetCorpList.addAll(dataCorpList);
            return;
        }
        for (CorpVO corpVO : dataCorpList) {
            if (!CollectionUtils.isEmpty(corpVO.getChildren())) {
                dfsLevelCorp(currentLevel + 1, targetLevel, targetCorpList, corpVO.getChildren());
            }
        }
    }
    @Override
    public List<String> getUserCorpIds(String resource, String userId) {
        return corpService.getUserCorpIds(resource, userId);
    }
    public List<String> getCorpIdsByUserResource(String resource, String userId) {
        return corpService.getCorpIdsByUserResource(resource, userId);
    }
    @Override
    public List<String> getUserNetIds(String resource, Integer type, String userId) {
        return corpService.getUserNetIds(resource, userId, type);
    }
    @Override
    public List<CorpDTO> listUserCorp(String resource, String userId) {
        List<String> currentUserCorpIds = getUserCorpIds(resource, userId);
        List<CorpDTO> corpList = null;
        if (EmptyUtils.isEmpty(currentUserCorpIds)) {
            return new ArrayList<>();
        }
        //hql语句中新增排序，update by zhoul 2018-05-17,增加status = 1过滤掉禁用的单位
        StringBuilder hql = new StringBuilder();
        hql.append("from Corp where status = 1");
        String corpAuthSql = SqlUtil.getInSql(currentUserCorpIds, "id", true);
        hql.append(" and ").append(corpAuthSql);
        hql.append(" order by sort asc");
        List<Corp> list = corpService.getEntityDao().createQuery(hql.toString()).list();
        if (!EmptyUtils.isEmpty(list)) {
            corpList = BeanUtils.copyListProperties(list, CorpDTO.class);
        }
        return corpList;
    }
    @Override
    public List<CorpDTO> listUserResourceCorps(String resource, String userId) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("listUserResourceCorps getCorpIdsByUserResource spends:");
        List<String> currentUserCorpIds = this.corpService.getCorpIdsByUserResource(resource, userId);
        List<CorpDTO> corpList = new ArrayList<>();
        if (EmptyUtils.isEmpty(currentUserCorpIds)) {
            return corpList;
        }
        //hql语句中新增排序，update by zhoul 2018-05-17,增加status = 1过滤掉禁用的单位
      /*  StringBuilder hql = new StringBuilder();
        hql.append("from Corp where status = 1");
        String corpAuthSql = SqlUtil.getInSql(currentUserCorpIds, "id", true);
        hql.append(" and ").append(corpAuthSql);
        hql.append(" order by sort asc");
        List<Corp> list = corpService.getEntityDao().createQuery(hql.toString()).list();
        if (!EmptyUtils.isEmpty(list)) {
            corpList = BeanUtils.copyListProperties(list, CorpDTO.class);
        }
        return corpList;*/
        stopWatch.stop();
        stopWatch.start("listUserResourceCorps getCorpBasicInfoByCorpIds spends:");
        List<Corp> corps = corpService.getCorpBasicInfoByCorpIds(Sets.newHashSet(currentUserCorpIds));
        stopWatch.stop();
        System.out.println("listUserResourceCorps total spends :" + stopWatch.prettyPrint());
        if (EmptyUtils.isEmpty(corps)) {
            return corpList;
        }
        for (Corp corp : corps) {
            CorpDTO corpDTO = new CorpDTO();
            BeanUtils.copyNotNullProperties(corp, corpDTO);
            corpList.add(corpDTO);
        }
        return corpList;
    }
    //todo 获取用户方式变更
    @Override
    public String getCurrentUserCorpSQL(String resource) {
        ShiroUser shiroUser = new ShiroUser();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT 1 FROM SYS_RESOURCES resources INNER JOIN SYS_CORP_USER_RESOURCE cur ON cur.RESOURCE_ID = resources.id WHERE ");
        sql.append(" and cur.USER_ID  = '").append(shiroUser.getId()).append("' ");
        sql.append(" and resources.PERMISSION = '").append(resource).append("'");
        return sql.toString();
    }
    @Override
    public String getCurrentUserCorpSQL(String resource, String filed) {
        ShiroUser shiroUser = new ShiroUser();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT 1 FROM SYS_RESOURCES resources INNER JOIN SYS_CORP_USER_RESOURCE cur ON cur.RESOURCE_ID = resources.id WHERE ");
        sql.append(" and cur.USER_ID  = '").append(shiroUser.getId()).append("' ");
        sql.append(" and resources.PERMISSION = '").append(resource).append("' ");
        sql.append(" and cur.CORP_ID=").append(filed);
        return sql.toString();
    }
    @Override
    public String getCurrentUserCorpSQLByPlaceholder(String placeholder) {
        ShiroUser shiroUser = new ShiroUser();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT 1 FROM SYS_RESOURCES resources INNER JOIN SYS_CORP_USER_RESOURCE cur ON cur.RESOURCE_ID = resources.id WHERE ");
        sql.append(" and cur.USER_ID  = '").append(shiroUser.getId()).append("' ");
        sql.append(" and resources.PERMISSION = :").append(placeholder);
        return sql.toString();
    }
    @Override
    public String getCurrentUserCorpSQLByPlaceholder(String placeholder, String filed) {
        ShiroUser shiroUser = new ShiroUser();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT 1 FROM SYS_RESOURCES resources INNER JOIN SYS_CORP_USER_RESOURCE cur ON cur.RESOURCE_ID = resources.id WHERE ");
        sql.append(" and cur.USER_ID  = '").append(shiroUser.getId()).append("' ");
        sql.append(" and resources.PERMISSION = :").append(placeholder);
        sql.append(" and cur.CORP_ID=").append(filed);
        return sql.toString();
    }
    @Override
    public CorpDTO getCorpIdByName(String name) {
        Corp corp = corpService.getCorpByName(name);
        if (EmptyUtils.isEmpty(corp)) {
            return null;
        }
        if (EmptyUtils.isEmpty(corp.getUserAccountCode())) {
            corp.setUserAccountCode(corp.getId());
        }
        return BeanUtils.copyNewInstanceProperties(corp, CorpDTO.class);
    }
    @Override
    public CorpDTO getCorpByCode(String code) {
        Corp corp = corpService.getCorpByCode(code);
        if (EmptyUtils.isEmpty(corp)) {
            return null;
        }
        if (EmptyUtils.isEmpty(corp.getUserAccountCode())) {
            corp.setUserAccountCode(corp.getId());
        }
        CorpDTO corpDTO = BeanUtils.copyNewInstanceProperties(corp, CorpDTO.class);
        // 省区
        if (ObjectUtil.isNotEmpty(corpDTO.getProvinceId())){
            List<String> provinceIdList = Arrays.asList(corpDTO.getProvinceId().split(","));
            if (ObjectUtil.isNotEmpty(provinceIdList)){
                List<SystemProvince> systemProvinces = systemProVinceService.queryByIds(provinceIdList);
                String provinceName = systemProvinces.stream().map(SystemProvince::getProvinceName).collect(Collectors.joining(","));
                corpDTO.setProvinceName(provinceName);
            }
        }


        // 转换营运区
        if (ObjectUtil.isNotEmpty(corpDTO.getOperationAreaId())){
            List<String> operatingAreaCodes = Arrays.asList(corpDTO.getOperationAreaId().split(","));
            if (ObjectUtil.isNotEmpty(operatingAreaCodes)){
                List<OperationAreaDTO> operationAreas = operationAreaServiceImpl.queryByCodes(operatingAreaCodes);
                String operationAreaName = operationAreas.stream().map(OperationAreaDTO::getOperationAreaName).collect(Collectors.joining(","));
                corpDTO.setOperationAreaName(operationAreaName);
            }
        }


        return corpDTO;
    }
    @Override
    public List<CorpDTO> listUnderCorpByNetId(String netId) {
        List<Corp> corpList = corpDao.listValidCorpByNetId(netId);
        return BeanUtils.copyListProperties(corpList, CorpDTO.class);
    }
    @Override
    public List<CorpDTO> listUnderCorpByParentCorpId(String corpId) {
        return this.listUnderCorpByParentCorpIdCtl(corpId);
    }
    @Override
    public List<CorpDTO> listCorpByParentId(String corpId,List<String> equityMaintenanceList) {
        List<Corp> corpList = corpDao.listCorpByParentId(corpId,equityMaintenanceList);
        if (EmptyUtils.isEmpty(corpList)) {
            return null;
        }

        // 转换单位省区
        List<CorpDTO> corpDTOList = BeanUtils.copyListProperties(corpList, CorpDTO.class);
        List<String> provinceIdList = corpDTOList.stream().filter(corpDTO -> ObjectUtil.isNotEmpty(corpDTO.getProvinceId())).map(CorpDTO::getProvinceId).collect(Collectors.toList());
        Map<String,String> provinceMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(provinceIdList)){
            List<SystemProvince> systemProvinces = systemProVinceService.queryByIds(provinceIdList);
            // 把systemProvinces转换成map
            provinceMap = systemProvinces.stream().collect(Collectors.toMap(SystemProvince::getId, SystemProvince::getProvinceName));
        }

        // 转换营运区
        List<String> operatingAreaCodes = corpDTOList.stream().filter(corpDTO -> ObjectUtil.isNotEmpty(corpDTO.getOperationAreaId())).map(CorpDTO::getOperationAreaId).collect(Collectors.toList());
        Map<String,String> operatingAreaMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(operatingAreaCodes)){
            List<OperationAreaDTO> operationAreas = operationAreaServiceImpl.queryByCodes(operatingAreaCodes);
            operatingAreaMap = operationAreas.stream().collect(Collectors.toMap(OperationAreaDTO::getOperationAreaCode, OperationAreaDTO::getOperationAreaName));
        }


        for (CorpDTO corpDTO : corpDTOList){
            // 转换单位省区
            if (ObjectUtil.isNotEmpty(corpDTO.getProvinceId())){
                String provinceName = "";
                for(String pid : corpDTO.getProvinceId().split(",")){
                    if(StringUtils.isBlank(pid)){
                        continue;
                    }
                    if(StringUtils.isBlank(provinceName)){
                        provinceName = provinceMap.get(pid);
                    }else{
                        provinceName = provinceName + "," + provinceMap.get(pid);
                    }
                }
                corpDTO.setProvinceName(provinceName);
            }
            // 转换营运区
            if (ObjectUtil.isNotEmpty(corpDTO.getOperationAreaId())){
                String  operationAreaName = "";
                for(String oid : corpDTO.getOperationAreaId().split(",")){
                    if(StringUtils.isBlank(oid)){
                        continue;
                    }

                    if(StringUtils.isBlank(operationAreaName)){
                        operationAreaName = operatingAreaMap.get(oid);
                    }else{
                        operationAreaName = operationAreaName + "," + operatingAreaMap.get(oid);
                    }
                }
                corpDTO.setOperationAreaName(operationAreaName);
            }
        }

        return corpDTOList;
    }
    @Override
    public List<CorpDTO> listUnderCorpByParentCorpIdSkipLevel(String corpId) {
        List<Corp> corpList = corpDao.listUnderCorpByParentCorpIdSkipLevel(corpId);
        if (EmptyUtils.isEmpty(corpList)) {
            return null;
        }
        return BeanUtils.copyListProperties(corpList, CorpDTO.class);
    }
    @Override
    public CorpDTO getNetByCorpId(String corpId) {
        CorpDTO netDTO = null;
        Corp corpInfo = corpDao.get(corpId, true);
        if (corpInfo != null) {
            if (StringUtils.isNotBlank(corpInfo.getNetId())) {
                netDTO = new CorpDTO();
                Corp netInfo = corpDao.get(corpInfo.getNetId());
                BeanUtils.copyProperties(netInfo, netDTO);
            }
        }
        return netDTO;
    }
    /**
     * 根据父单位ID，获取该单位下受控制的直属的单位(不跨单位获取)
     *
     * @param corpId 父单位ID
     * @return
     */
    @Override
    public List<CorpDTO> listUnderCorpByParentCorpIdCtl(String corpId) {
        List<Corp> corpList = corpDao.listUnderCorpByParentCorpId(corpId);
        if (EmptyUtils.isEmpty(corpList)) {
            return null;
        }
        return BeanUtils.copyListProperties(corpList, CorpDTO.class);
    }

    /**
     * 功能介绍 : 获取授信主体的指定单位资源权限。<br>
     * <b>日期：</b>2017年11月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Override
    public List<CorpDTO> getRatGroupCorpNet(String resource,String userId) {
        List<CorpDTO> corpDTOList = new ArrayList<CorpDTO>();
        List<Corp> corpList = corpService.listRatGroupCorpNet(resource, userId);
        if (!EmptyUtils.isEmpty(corpList)) {
            corpDTOList = BeanUtils.copyListProperties(corpList, CorpDTO.class);
        }
        return corpDTOList;
    }
    /**
     * 判断两个单位是否所属同一网点下
     *
     * @param corpId1
     * @param corpId2
     * @return
     */
    @Override
    public boolean isTheSameNetByCorpId(String corpId1, String corpId2) {
        if (EmptyUtils.isBlank(corpId1) || EmptyUtils.isBlank(corpId2)) {
            return false;
        }
        Corp corp1 = this.corpService.getCropById(corpId1);
        Corp corp2 = this.corpService.getCropById(corpId2);
        if (EmptyUtils.isEmpty(corp1)
                || EmptyUtils.isEmpty(corp2)
                || StringUtils.isEmpty(corp1.getNetId())
                || StringUtils.isEmpty(corp2.getNetId())) {
            return false;
        }
        return corp1.getNetId().equalsIgnoreCase(corp2.getNetId());
    }
    @Override
    public List<CorpDTO> listCorpDTOByCorpIds2(List<String> corpIds) {
        if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        List<Corp> list = this.corpDao.listCorpByCorpIds(corpIds);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return BeanUtils.copyListProperties(list, CorpDTO.class);
    }
    @Override
    public List<CorpDTO> getCpRationGroupCorpNet(String resource, String netId, String userId) {
        List<CorpDTO> corpDTOList = new ArrayList<CorpDTO>();
        List<Corp> corpList = corpService.listCpRationCorpNet(resource, netId, userId);
        if (!EmptyUtils.isEmpty(corpList)) {
            corpDTOList = BeanUtils.copyListProperties(corpList, CorpDTO.class);
        }
        return corpDTOList;
    }
    public Corp initCorp(CorpDTO c, boolean isEdit) {
        Corp corp = corpService.getCorpByCode(c.getCode());
        if (isEdit) {
            if (EmptyUtils.isEmpty(corp)) {
                throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_CORP_CODE_NON_EXISTENT, c.getCode());
            }
        } else {
            if (!EmptyUtils.isEmpty(corp)) {
                throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_CORP_ALREADY_EXIST);
            }
            corp = new Corp();
        }
        corp.setCode(c.getCode());
        corp.setName(c.getName());
        corp.setType(c.getType());
        Corp netCorp = corpService.getCorpByCode(c.getNetCode());
        if (EmptyUtils.isEmpty(netCorp)) {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_CORP_NET_CORP_NOT_FOUND, c.getCode());
        }
        corp.setNetId(netCorp.getId());
        Corp parentCorp = corpService.getCorpByCode(c.getParentCode());
        if (EmptyUtils.isEmpty(parentCorp)) {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_CORP_PARENT_CORP_NOT_FOUND, c.getCode());
        }
        corp.setParentId(parentCorp.getId());
        corp.setStatus(c.getStatus());
        corp.setListedCompany(1);
        return corp;
    }
    @Override
    public void save(List<CorpDTO> corps) {
        for (CorpDTO c : corps) {
            Corp corp = initCorp(c, false);
            corpService.save(corp, "ERP");
        }
    }
    @Override
    public void update(List<CorpDTO> corps) {
        for (CorpDTO c : corps) {
            Corp corp = initCorp(c, true);
            corpService.update(corp, "ERP");
        }
    }
    @Override
    public void delete(List<CorpDTO> corpCodes) {
        for (CorpDTO c : corpCodes) {
            Corp corp = corpService.getCorpByCode(c.getCode());
            if (EmptyUtils.isEmpty(corp)) {
                throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_CORP_CODE_NON_EXISTENT, c.getCode());
            }
            corpService.delete(corp.getId());
        }
    }
    @Override
    public CorpDTO getTopCorpByCorpId(String corpId) {
        Corp corp = corpService.get(corpId, true);
        if (EmptyUtils.isEmpty(corp)) {
            return null;
        }
        String[] split = corp.getParentIds().split(CommonConstant.COMMA_SPLIT);
        Corp topNet = corpService.get(split[0], true);
        if (EmptyUtils.isEmpty(topNet)) {
            return null;
        }
        CorpDTO corpDTO = new CorpDTO();
        BeanUtils.copyProperties(topNet, corpDTO);
        return corpDTO;
    }

    @Override
    public CorpDTO getSecoCorpByCorpId(String corpId) {
        Corp corp = corpService.get(corpId, true);
        if (EmptyUtils.isEmpty(corp)) {
            return null;
        }
        String[] split = corp.getParentIds().split(CommonConstant.COMMA_SPLIT);
        Corp topNet = corpService.get(split[1], true);
        if (EmptyUtils.isEmpty(topNet)) {
            return null;
        }
        CorpDTO corpDTO = new CorpDTO();
        BeanUtils.copyProperties(topNet, corpDTO);
        return corpDTO;
    }

    @Override
    public List<CorpDTO> listTreeCorpDTOByCorpIds(List<String> corpIds) {
        if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        List<Corp> list = this.corpDao.listCorpByCorpIds(corpIds);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        // 树化
        List<CorpVO> corpListNew = corpService.transformToTree(list);
        corpListNew = TreeNodeUtils.transformToList(corpListNew);
        if (EmptyUtils.isEmptyList(corpListNew)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(corpListNew, CorpDTO.class);
    }
    @Override
    public List<CorpDTO> listTreeCorpDTOByCorpIdsV2(List<String> corpIds) {
        StopWatch stopWatch = new StopWatch("listTreeCorpDTOByCorpIds");
        stopWatch.start("corpDao.listCorpByCorpIds");
        if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        List<Corp> list = this.corpDao.listCorpByCorpIds(corpIds);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        stopWatch.stop();
        stopWatch.start("corpService.transformToTree");
        // 树化
        List<CorpVO> corpListNew = corpService.transformToTreeWithOutInject(list);
        stopWatch.stop();
        stopWatch.start("TreeNodeUtils.transformToList");
        corpListNew = TreeNodeUtils.transformToList(corpListNew);
        if (EmptyUtils.isEmptyList(corpListNew)) {
            return new ArrayList<>();
        }
        stopWatch.stop();
        stopWatch.start("the rest of operations");
        List<CorpDTO> result = BeanUtils.copyListProperties(corpListNew, CorpDTO.class);
        stopWatch.stop();
        System.out.println("listTreeCorpDTOByCorpIds all spends :" + stopWatch.prettyPrint());
        return result;
    }
    @Override
    public List<String> getUserCorpIdsByUseAccount(String resource, String userId) {
        return corpService.getUserCorpIdsByUseAccount(resource, userId);
    }
	@Override
	public Map<String, CorpDTO> listCorpDtoByCorpIds(List<String> corpIds) {
		if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        List<Corp> list = this.corpDao.listCorpByCorpIds(corpIds);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<CorpDTO> resultList = BeanUtils.copyListProperties(list, CorpDTO.class);
        return resultList.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
	}
    @Override
    public Map<String, CorpDTO> listCorpCodeByCorpIds(List<String> corpIds) {
        if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        List<Corp> list = this.corpDao.listCorpByCorpIds(corpIds);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<CorpDTO> resultList = BeanUtils.copyListProperties(list, CorpDTO.class);
        return resultList.stream().collect(Collectors.toMap(CorpDTO::getCode, Function.identity()));
    }

    @Override
    public Map<String, CorpDTO> listCorpNameByCorpIds(List<String> corpIds) {
        if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        List<Corp> list = this.corpDao.listCorpByCorpIds(corpIds);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<CorpDTO> resultList = BeanUtils.copyListProperties(list, CorpDTO.class);
        return resultList.stream().collect(Collectors.toMap(CorpDTO::getName, Function.identity()));
    }

    @Override
    public Map<String, CorpDTO> getCorpMap() {
        List<Corp> list = this.corpService.listEffectiveCorp();
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<CorpDTO> resultList = BeanUtils.copyListProperties(list, CorpDTO.class);
        return resultList.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
    }
    @Override
    public DataTablesOutput getDataTablesCorpByCorpIds(DataTablesInput dataTablesInput, List<String> corpIds) {
        return corpDao.getDataTablesCorpByCorpIds(dataTablesInput, corpIds);
    }
    @Override
    public DataTablesOutput getNotDeleteAndValidAndBankAcc(DataTablesInput dataTablesInput, List<String> bankacc) {
        return corpDao.getNotDeleteAndValidAndBankAcc(dataTablesInput,bankacc);
    }
    @Override
    public DataTablesOutput<FcAccDTO> getblnctrAccByAccAndcurId(DataTablesInput dataTablesInput, List<String> acc) {
        return corpDao.getblnctrAccByAccAndcurId(dataTablesInput,acc);
    }
    @Override
    public void resetRatGroup() {
        corpDao.resetRatGroup();
    }
    @Override
    public void updateRatGroup(List<String> corpIds) {
        corpDao.updateRatGroup(corpIds);
    }
    /**
     * 获取有效单位Id集合
     * @return
     */
    @Override
    public List<String> getCorpIds() {
        return corpService.getCorpIds();
    }
    /**
     * 根据查询条件(单位名称 or 单位编码)获取有效的单位Id集合
     * @param seach
     * @return
     */
    @Override
    public List<String> listCorpIdsBySeach(String seach) {
        return corpService.listCorpIdsBySeach(seach);
    }
    /**
     * 根据查询条件(单位名称 or 单位编码)获取有效的单位信息集合
     * @param seach
     * @return
     */
    @Override
    public List<CorpDTO> listCorpBySeach(String seach) {
        return corpService.listCorpBySeach(seach);
    }
    /**
     * 根据网点Id获取单位信息集合
     * @param netId
     * @return
     */
    @Override
    public List<CorpDTO> getCorpByNetId(String netId) {
        return corpService.getCorpByNetId(netId);
    }
    /**
     * 根据Id集合获取单位信息（包含下级单位数量）
     * @param corpIds 单位Id
     * @return
     */
    @Override
    public List<CorpDTO> getCorpByIdsHadChildCount(List<String> corpIds) {
        return corpService.getCorpByIdsHadChildCount(corpIds);
    }
    /**
     * 根据上级单位Id获取子单位信息
     * @param parentId 上级单位Id
     * @return
     */
    @Override
    public List<CorpDTO> getCorpByParentId(String parentId) {
        return corpService.getCorpByParentId(parentId);
    }
    /**
     * 根据上级单位Id集合获取子单位信息
     * @param parentIds 上级单位Ids
     * @return
     */
    @Override
    public List<CorpDTO> getCorpByParentIds(List<String> parentIds) {
        return corpDao.getCorpByParentIds(parentIds);
    }
    /**
     * TODO 根据ids，查询 corpName, 返回 k=id , v=name
     * @param corpIds
     * @return
     */
    @Override
    public Map<String, String> getNameByIds(List<String> corpIds) {
        return corpService.getNameByIds(corpIds);
    }

    @Override
    public Map<String, String> getNameByNetIds(List<String> netIds) {
        return corpService.getNameByNetIds(netIds);
    }

    @Override
    public Map<String, String> getNameByTypeAndIds(List<String> corpIds, Integer type){
        return corpService.getNameByTypeAndIds(corpIds, type);
    }
    /**
     * 根据 parentId 查询corpId集合
     * @param parentId 上级单位Id
     * @return
     */
    @Override
    public List<String> getCorpIdByParentId(String parentId) {
        return corpService.getCorpIdByParentId(parentId);
    }
    /**
     * 根据单位ID，查询返回单位名称
     * @param corpId
     * @return
     */
    @Override
    public String getCorpName(String corpId) {
        Corp corp = corpService.get(corpId, true);
        if (corp != null) {
            return corp.getName();
        }
        return "";
    }
    @Override
    public String getCorpCode(String corpId) {
        Corp corp = corpService.get(corpId, true);
        if (corp != null) {
            return corp.getCode();
        }
        return "";
    }
    @Override
    public List<FexCorpExposureVO> listFbsCorpExposure(String orgType, String search) {
        return corpDao.listFbsCorpExposure(orgType, search);
    }
    @Override
    public List<String> listCorpIdsBySeach(String orgType, String search) {
        return corpDao.listCorpIdsBySeach(orgType, search);
    }
    @Override
    public FexCorpExposureVO getParentCorpExposureByCorpId(String corpId) {
        return corpDao.getParentCorpExposureByCorpId(corpId);
    }
    @Override
    public List<FexCorpExposureVO> listCorpExposureByParentId(String parentId) {
        return corpDao.listCorpExposureByParentId(parentId);
    }
    @Override
    public FexCorpExposureVO getCorpExposureByCorpId(String corpId) {
        return corpDao.getCorpExposureByCorpId(corpId);
    }
    @Override
    public List<CorpDTO> listCorpDTOByCorpIds(List<String> corpIds){
        return corpService.listCorpDTOByCorpIds(corpIds);
    }
    @Override
    public List<Map<String, Object>> listStrategyDataByDataVal(List<String> dataValList) {
        return corpDao.getStrategyDataByDataVal(dataValList);
    }
    @Override
    public List<Map<String, Object>> getCorpAndNetInfoByCorpId(List<String> corpIdList) {
        return corpDao.listCorpAndNetByCorpIds(corpIdList);
    }

    @Override
    public Map<String, CorpDTO> getCorpMapByCorpId(List<String> corpIdList) {
        if (ObjectUtil.isEmpty(corpIdList)) {
            return new HashMap<>();
        }
        List<CorpDTO> corpList = corpDao.listCorpAndNetByCorpId(corpIdList);
        return corpList.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity(),(key1,key2) -> key2));
    }

    @Override
    public List<CorpDTO> listCorpDTOByNetIds(List<String> netIds) {
        return corpDao.listCorpDTOByNetIds(netIds);
    }
    @Override
    public List<Map<String, Object>> queryVAccListVo(String bankAcc) {
        return corpDao.queryVAccListVo(bankAcc);
    }
    @Override
    public List<String> getCorpIdsByType(String type) {
        return corpService.getCorpIdsByType(type);
    }

    @Override
    public Map<String, CorpDTO> getCorpNetMap(){
        List<CorpDTO> list = corpService.getCorpNetList();
        Map<String ,CorpDTO> corpMap=new HashMap<>();
        for(CorpDTO corp:list){
            corpMap.put(corp.getId(),corp);
        }
        return corpMap;
    }

    @Override
    public List<CorpDTO> getRegion(String id) {
        return corpDao.getRegion(id);
    }

    @Override
    public CorpDTO getRegionByCorpId(String corpId,String regionId) {
        return corpDao.getRegionByCorpId(corpId,regionId);
    }

    @Override
    public List<CorpDTO> getFilterBySource(String corpVOList, List<String> sourceCorpIds) {
        return corpService.getFilterBySource(corpVOList, sourceCorpIds);
    }
	@Override
	public Map<String, CorpDTO> getCorpListByNames() {
		    List<CorpDTO> list = corpDao.getSimpleCorpList();
	        Map<String ,CorpDTO> corpMap = list.stream().collect(Collectors.toMap(CorpDTO::getName, Function.identity(),(key1,key2) -> key2));
	        return corpMap;
	}

	@Override
	public Map<String, CorpDTO> getCorpListByNamesAll() {
			List<CorpDTO> list = corpDao.getCorpListAll();
	        Map<String ,CorpDTO> corpMap = list.stream().collect(Collectors.toMap(CorpDTO::getName, Function.identity(),(key1,key2) -> key2));
	        return corpMap;
	}

    @Override
    public String getUnitStoreMaintenanceNameByCode(String code) {
        List<String> list = new ArrayList<>();
        list.add(code);
        List<UnitStoreMaintenance> unitStoreMaintenances = unitStoreMaintenanceService.queryByCodes(list);
        if(!CollectionUtils.isEmpty(unitStoreMaintenances)){
            return unitStoreMaintenances.get(0).getStoreName();
        }
        return null;
    }

    @Override
    public List<CorpDTO> listByCodes(List<String> codes) {
        List<CorpVO> corpVOS = corpService.listByCodes(codes);
        return BeanUtils.copyListProperties(corpVOS, CorpDTO.class);
    }

    /**
     * 根据传入的id或者ids获取所有子级单位信息
     * @param ids
     * @param includeThisCorp 是否包含当前单位，默认为true
     * @return TODO
     */
    @Override
    public List<CorpDTO> listSubCorp(List<String> ids,boolean includeThisCorp) {
        List<CorpVO> corpVOS = corpService.listSubCorp(ids,includeThisCorp);
        return BeanUtils.copyListProperties(corpVOS, CorpDTO.class);
    }

    /**
     * 根据传入的id或者ids获取所有父级单位信息
     * @param ids
     * @param includeThisCorp 是否包含当前单位，默认为true
     * @return TODO
     */
    @Override
    public List<CorpDTO> listParentCorp(List<String> ids, boolean includeThisCorp) {
        List<CorpVO> corpVOS = corpService.listParentCorp(ids,includeThisCorp);
        return BeanUtils.copyListProperties(corpVOS, CorpDTO.class);
    }

    /**
     * 单位组件-
     * 获取过滤权限资源的结构 数量
     *
     * @param resource 资源权限
     * @param currentUser     用户
     * @return 集合数据
     */
    public String getFilterCorpW(String resource, ICurrentUser currentUser) {
        CorpComponentVO corpComponentVO = new CorpComponentVO();
        corpComponentVO.setResource(resource);
        List<Corp> corps = corpService.getFilterCorpW(corpComponentVO, currentUser);
        return String.valueOf(corps.size());
    }

    @Override
    public List<CorpDTO> listCorpDTOByCorpIdsForOrgant(List<String> corpIds,String orgtId) {
        if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        //获取多组织版本号id
        String releaseId =  corpService.getEffectiveOrgIdByModuld(ConfigConstant.FBS);
        List<Corp> list = this.corpDao.listCorpByCorpIdsOrgan(corpIds,releaseId);

        List<CorpDTO> corpDTOList = new ArrayList<>();
        if(!EmptyUtils.isEmpty(list)){
            corpDTOList =  BeanUtils.copyListProperties(list, CorpDTO.class);
        }
        return corpDTOList;
    }

    /**
     * 获取模块对应生效的组织ID
     * @param moduleCode
     * @return
     */
    @Override
    public String getEffectiveOrgIdByModuld(String moduleCode) {
        return corpService.getEffectiveOrgIdByModuld(moduleCode);
    }
}
