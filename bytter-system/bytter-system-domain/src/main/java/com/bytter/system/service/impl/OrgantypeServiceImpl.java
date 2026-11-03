package com.bytter.system.service.impl;


import com.bytter.system.api.IOrgantypeService;
import com.bytter.system.api.dto.OrgantypeModuleDTO;
import com.bytter.system.dao.OrgantypeCorpDao;
import com.bytter.system.service.OrgantypeModuleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@DubboService
@Service("organtypeServiceImpl")
public class OrgantypeServiceImpl implements IOrgantypeService {
    @Autowired
    private OrgantypeModuleService organtypeModuleService;
    @Autowired
    private OrgantypeCorpDao organtypeCorpDao;

    @Override
    public OrgantypeModuleDTO getOrgantypeModule(String sysCode) {
        return organtypeModuleService.getBySysCode(sysCode);
    }

    @Override
    public Map<String, OrgantypeModuleDTO> getOrgantypeModuleMap(List<String> sysCodes) {
        List<OrgantypeModuleDTO> dtoList= organtypeModuleService.queryBySysCode(sysCodes);
        Map<String, OrgantypeModuleDTO> corpMap = dtoList.stream().collect(Collectors.toMap(OrgantypeModuleDTO::getSysCode, Function.identity()));
        return corpMap;
    }
    @Override
    public List<String> getCorpIdByOrgId(String orgId,String authCorpIds){
        return organtypeCorpDao.listOrgtCorpIdsByOrgtId(orgId,authCorpIds);
    }
}
