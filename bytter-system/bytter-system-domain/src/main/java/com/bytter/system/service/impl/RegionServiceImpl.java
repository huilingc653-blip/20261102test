package com.bytter.system.service.impl;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IRegionService;
import com.bytter.system.api.dto.RegionDTO;
import com.bytter.system.entity.Region;
import com.bytter.system.service.RegionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author zhoul
 * @date 2017/7/31
 */
@Service("regionServiceI")
@DubboService
public class RegionServiceImpl implements IRegionService {
    @Autowired
    RegionService regionService;
    @Override
    public RegionDTO getCityBy(String parentId, String name) {
        if (EmptyUtils.isBlank(name)) {
            return null;
        }
        Region region = this.regionService.getCityBy(parentId, name.trim());
        if (EmptyUtils.isEmpty(region)) {
            return null;
        }
        RegionDTO dto = new RegionDTO();
        BeanUtils.copyNotNullProperties(region, dto);
        return dto;
    }
    @Override
    public RegionDTO getByName(String name, Boolean prov) {
        if (EmptyUtils.isBlank(name)) {
            return null;
        }
        Region region = this.regionService.getByName(name.trim(), prov);
        if (EmptyUtils.isEmpty(region)) {
            return null;
        }
        RegionDTO dto = new RegionDTO();
        BeanUtils.copyNotNullProperties(region, dto);
        return dto;
    }
    @Override
    public RegionDTO getRegionByLikeProvName(String provName) {
        Region region = this.regionService.getRegionByLikeProvName(provName);
        if (EmptyUtils.isEmpty(region)) {
            return null;
        }
        RegionDTO dto = new RegionDTO();
        BeanUtils.copyNotNullProperties(region, dto);
        return dto;
    }
    @Override
    public List<RegionDTO> listProvAll() {
        List<Region> list = this.regionService.listProvAll();
        return BeanUtils.copyListProperties(list, RegionDTO.class);
    }
    @Override
    public List<RegionDTO> listCityByParentId(String parentId) {
        List<Region> list = this.regionService.listCityByParentId(parentId);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return BeanUtils.copyListProperties(list, RegionDTO.class);
    }
    @Override
    public RegionDTO getById(String id) {
        Region region = this.regionService.getById(id);
        if (EmptyUtils.isEmpty(region)) {
            return null;
        }
        RegionDTO dto = new RegionDTO();
        BeanUtils.copyNotNullProperties(region, dto);
        return dto;
    }

    @Override
    public Map<String, RegionDTO> getRegionMap() {
        List<Region> list = this.regionService.listAll();
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<RegionDTO> resultList = BeanUtils.copyListProperties(list, RegionDTO.class);
        return resultList.stream().collect(Collectors.toMap(RegionDTO::getId, Function.identity()));
    }

    @Override
    public Map<String, RegionDTO> listByIds(List<String> regionIds) {
        List<RegionDTO> regionList = regionService.listByIds(regionIds);
        if (EmptyUtils.isEmptyList(regionList)) {
            return new HashMap<>();
        }
        return regionList.stream().collect(Collectors.toMap(RegionDTO::getId, Function.identity()));
    }

    @Override
    public List<RegionDTO> findByIds(List<String> ids) {
        List<Region> list =  this.regionService.findByIds(ids);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(list, RegionDTO.class);
    }

    @Override
    public Map<String, RegionDTO> findRegionMapByIds(List<String> ids) {
        Map<String, RegionDTO> regionDTOMap = new HashMap<>();
        List<Region> list =  this.regionService.findByIds(ids);
        if (CollectionUtil.isNotEmpty(list)){
            List<RegionDTO> regionDTOS = BeanUtils.copyListProperties(list, RegionDTO.class);
            regionDTOMap = regionDTOS.stream()
                    .collect(Collectors.toMap(
                            RegionDTO::getId,
                            dto -> dto,
                            (oldVal, newVal) -> oldVal
                    ));
        }
        return regionDTOMap;
    }
}
