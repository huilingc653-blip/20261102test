package com.bytter.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.ConfigEntityDTO;
import com.bytter.system.dao.ConfigDao;
import com.bytter.system.entity.Config;
import com.bytter.system.service.ConfigService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by huangx on 2017/5/25.
 */
@DubboService
@Transactional
@Service("configServiceImpl")
public class ConfigServiceImpl implements IConfigService {

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private ConfigService configService;
    public Map<String, Object> listByType(String type) {
        return configService.listByType(type);
    }
    /**
     * 保存或更新配置
     *
     * @param parasMap
     */
    public void saveOrUpdate(Map<String, String[]> parasMap) {
        configService.saveOrUpdate(parasMap, null);
    }

    public void saveOrUpdate2(Map<String,String> configDTO){
        configService.saveOrUpdate(configDTO);
    }

    public void deleteByTypeName(String type, String name){
        Config byTypeAndName = configService.getByTypeAndName(type, name);
        configService.delete(byTypeAndName.getId());
    }

    public Map<String,Object> getConfigMap(String type,String name){
        return configService.getConfig(type,name);
    }

    private String getByTypeAndName(String type, String name) {
        Config config = configService.getByTypeAndName(type, name);
        return config == null ? null : config.getValue();
    }
    @Transactional(readOnly = true)
    public ConfigDTO getConfig(String type, String name) {
        String value = getByTypeAndName(type, name);
        return new ConfigDTO(value);
    }

    @Transactional(readOnly = true)
    @Override
    public ConfigDTO getGlobalConfig(String type, String name) {
        Config config = configService.getGlobalConfig(type, name);
        return new ConfigDTO(config == null ? null : config.getValue());
    }

    @Override
    public List<ConfigEntityDTO> listConfigEntityDTO(String type, String value, List<String> names) {
        List<Config> list = configService.listConfig(type, value,names);
        if (ObjectUtil.isNotEmpty( list)){
            return BeanUtils.copyListProperties(list, ConfigEntityDTO.class);
        }else{
            return null;
        }
    }



    /**
     * 重置basic参数
     */
    @Transactional
    public void goAccountReceivableReset(){
        String type = "basic";
        List<String> names = new ArrayList<>();
        names.add("budgetSel");
        names.add("defaultBudgetSel");
        names.add("paymentMeans");
        names.add("remind");
        for (String name : names) {
            Config config = configDao.getByTypeAndName(type, name);
            if (EmptyUtils.isEmpty(config)) {
                Config newConfig = new Config();
//                newConfig.setTenantId(getCurrentUser().getTenantId());
                newConfig.setType(type);
                newConfig.setName(name);
                if ("budgetSel".equals(name)) {
                    newConfig.setValue("0");
                } else if ("defaultBudgetSel".equals(name)) {
                    newConfig.setValue("200");
                } else if ("paymentMeans".equals(name)) {
                    newConfig.setValue("[1]");
                } else if ("remind".equals(name)) {
                    newConfig.setValue("30,15,7,3");
                }
                configDao.saveEntity(newConfig);
            } else {
                if ("budgetSel".equals(name)) {
                    config.setValue("0");
                } else if ("defaultBudgetSel".equals(name)) {
                    config.setValue("200");
                } else if ("paymentMeans".equals(name)) {
                    config.setValue("[1]");
                } else if ("remind".equals(name)) {
                    config.setValue("30,15,7,3");
                }
                configDao.updateEntity(config);
            }
        }

    }

    public void deleteById(String id){
        configService.delete(id);
    }


    @Override
    public Map<String, Map<String, String>> listByTypes(List<String> types) {
        Map<String, Map<String, String>> result = new HashMap<>();
        List<Config> list = configDao.listConfigByTypes(types);
        if (list.isEmpty()) {
            return result;
        }
        Map<String, List<Config>> typeMap = list.stream().collect(Collectors.groupingBy(Config::getType));
        typeMap.keySet().forEach(key -> {
            Map<String, String> name = new HashMap<>();
            List<Config> names = typeMap.get(key);
            names.forEach(item -> {
                name.put(item.getName(), item.getValue());
            });
            result.put(key, name);
        });
        return result;
    }
}
