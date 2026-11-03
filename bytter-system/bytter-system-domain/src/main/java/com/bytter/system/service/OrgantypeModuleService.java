package com.bytter.system.service;
import java.util.*;

import com.bytter.system.api.dto.*;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.dao.OrgantypeModuleDao;
import com.bytter.system.entity.OrgantypeModule;
import com.bytter.system.vo.OrgantypeModuleVO;
import com.bytter.system.vo.OrgantypeVO;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Service
 * @Description: 组织架构对应关系维护表业务逻辑操作
 * @date 2020-05-14 16:01:41
 */
@Service("organtypeModuleService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class OrgantypeModuleService extends BaseService<OrgantypeModule, String> {
    @Autowired
    private OrgantypeModuleDao organtypeModuleDao;
    @Autowired
    private Ii18nService i18nServiceI;
    @Autowired
    //@Qualifier("organtypeService")
    private OrgantypeService organtypeService;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(OrgantypeModule entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        if (StringUtils.isNotEmpty(id)) {
            super.delete(id);
        }
    }
    /**
     * 根据ids删除
     *
     * @param ids
     */
    public void deletes(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                delete(id);
            }
        }
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(OrgantypeModule entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(OrgantypeModule entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(OrgantypeModule entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<OrgantypeModule, String> getEntityDao() {
        return organtypeModuleDao;
    }
    /**
     * 通VO保存
     *
     * @param organtypeModuleVO
     */
    public void saveOrgantypeModule(OrgantypeModuleVO organtypeModuleVO) {
        organtypeModuleVO.setId(null);
        saveOrUpdateOrgantypeModule(organtypeModuleVO);
    }
    /**
     * 通VO更新
     *
     * @param organtypeModuleVO
     */
    public void updateOrgantypeModule(OrgantypeModuleVO organtypeModuleVO) {
        saveOrUpdateOrgantypeModule(organtypeModuleVO);
    }
    /**
     * 通VO保存or更新
     *
     * @param organtypeModuleVO
     */
    public void saveOrUpdateOrgantypeModule(OrgantypeModuleVO organtypeModuleVO) {
        if (organtypeModuleVO != null) {
            OrgantypeModule organtypeModule = null;
            if (StringUtils.isNotEmpty(organtypeModuleVO.getId())) {
                organtypeModule = this.get(organtypeModuleVO.getId(), true);
            } else {
                organtypeModule = new OrgantypeModule();
            }
            BeanUtils.copyNotNullProperties(organtypeModule, organtypeModuleVO);
            saveOrUpdate(organtypeModule);
        }
    }
    /**
     * 根据ID查询组织架构对应关系维护表VO
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public OrgantypeModuleVO getOrgantypeModuleVO(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        OrgantypeModule organtypeModule = this.get(id, true);
        if (organtypeModule == null) {
            return null;
        }
        OrgantypeModuleVO organtypeModuleVO = new OrgantypeModuleVO();
        BeanUtils.copyNotNullProperties(organtypeModule, organtypeModuleVO);
        return organtypeModuleVO;
    }
    /**
     * 获取所有组织架构对应关系数据
     *
     * @return
     */
    public Map<String, Object> listOrgtModule() {
        List<OrgantypeModule> moduleList = organtypeModuleDao.listAll();
        Map<String, Object> map = new HashMap<String, Object>();
        OrgantypeModuleVO moduleVO;
        for (OrgantypeModule module : moduleList) {
            moduleVO = new OrgantypeModuleVO();
            BeanUtils.copyNotNullProperties(module, moduleVO);
            //根据模块代码获取模块名称
            String sysName = i18nServiceI.getBaseContext(SysConfigConstant.MODULETOMENUID.get(module.getSysCode()));
            moduleVO.setSysCodeName(sysName);
            if (StringUtils.isNotEmpty(module.getOrgtId())) {
                OrgantypeVO organtypeVO = organtypeService.getOrgantypeVO(module.getOrgtId());
                if (organtypeVO != null) {
                    moduleVO.setOrgtName(organtypeVO.getOrgtName());
                }
            }
            map.put(module.getSysCode(), moduleVO);
        }
        return map;
    }
    /**
     * 保存或更新配置
     *
     * @param parasMap
     */
    public void saveOrUpdate(Map<String, String[]> parasMap, ICurrentUser userVo) {
        for (Map.Entry<String, String[]> e : parasMap.entrySet()) {
            String paraKey = e.getKey();
            String values = e.getValue()[0];
            OrgantypeModule organtypeModule = organtypeModuleDao.getBySysCode(paraKey);
            organtypeModule.setOrgtId(values);
            if (userVo != null) {
                organtypeModule.setUpdateBy(userVo.getId());
            }
            organtypeModule.setUpdateTime(new Date());
            organtypeModuleDao.saveOrUpdate(organtypeModule);
        }
    }

    /**
     * 获取模块是否使用
     * @param sysCode
     * @return
     */
    public OrgantypeModuleDTO getBySysCode(String sysCode) {
        OrgantypeModuleDTO organtypeModuleDTO = new OrgantypeModuleDTO();
        organtypeModuleDTO.setIsValid(0);
        OrgantypeModule organtypeModule = organtypeModuleDao.getBySysCode(sysCode);
        if(!EmptyUtils.isEmpty(organtypeModule)){
            BeanUtils.copyNotNullProperties(organtypeModule,organtypeModuleDTO);
        }
        return  organtypeModuleDTO;
    }
    /**
     * 批量获取模块是否使用
     * @param sysCodes
     * @return
     */
    public  List<OrgantypeModuleDTO>  queryBySysCode(List<String> sysCodes) {
        List<OrgantypeModuleDTO> dtoList=new ArrayList<>();
        List<OrgantypeModule> organtypeModule = organtypeModuleDao.queryBySysCode(sysCodes);
        for (OrgantypeModule module:organtypeModule){
            OrgantypeModuleDTO organtypeModuleDTO = new OrgantypeModuleDTO();
            organtypeModuleDTO.setIsValid(0);
            BeanUtils.copyNotNullProperties(module,organtypeModuleDTO);
            dtoList.add(organtypeModuleDTO);
        }
        return  dtoList;
    }

}
