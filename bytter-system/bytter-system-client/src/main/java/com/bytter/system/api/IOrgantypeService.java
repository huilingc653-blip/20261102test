package com.bytter.system.api;



import com.bytter.system.api.dto.OrgantypeModuleDTO;

import java.util.List;
import java.util.Map;

public interface IOrgantypeService {

    /**
     * 获取模块是否使用组织架构
     * @param sysCode
     * @return
     */
    OrgantypeModuleDTO getOrgantypeModule(String sysCode);

    /**
     * 获取模块是否使用组织架构
     * @param sysCodes
     * @return key---sysCode , value---OrgantypeModuleDTO
     */
    Map<String, OrgantypeModuleDTO> getOrgantypeModuleMap(List<String> sysCodes);

    /**
     * 通过组织架构id和单位权限获取所有组织
     */
    List<String> getCorpIdByOrgId(String orgId,String authCorpIds);
}
