package com.bytter.system.service.impl;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.dict.DictInDTO;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.DictGroupDTO;
import com.bytter.system.entity.DictGroup;
import com.bytter.system.service.DictService;
import com.bytter.system.vo.DictVO;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboService;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 字典管理Service实现类
 *
 * @author zhoul
 * @date 2017/5/15
 */
@DubboService
public class DictServiceImpl implements IDictService , DictInjectProvider {
    @Autowired
    private DictService dictService;
    @Autowired
    private Ii18nService i18nServiceI;
    @Override
    public DictGroupDTO get(String groupCode) {
        DictGroup dictGroup = dictService.getDictGroupByGroupCode(groupCode);
        DictGroupDTO dictGroupDTO = new DictGroupDTO();
        if(dictGroup!=null){
            BeanUtils.copyProperties(dictGroup, dictGroupDTO);
        }
        return dictGroupDTO;
    }
    @Override
    public List<DictDTO> list(String groupCode) {
        List<DictVO> dictList = dictService.getDictGroupByCode(groupCode);
        for (DictVO dictVO : dictList) {
            String baseContext = i18nServiceI.getBaseContext(dictVO.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                dictVO.setName(baseContext);
            }
        }
        return BeanUtils.copyListProperties(dictList, DictDTO.class);
    }
    /**
     * 根据字典组code获取所属字典Map(code和名称为Key)
     *
     * @param groupCode 字典组code
     * @return
     */
    @Override
    public Map<String, DictDTO> listMap(String groupCode) {
        Map<String, DictDTO> dictMap = new HashMap<>();
        List<DictDTO> list = list(groupCode);
        if (ListUtils.isNotNullOrEmpty(list)) {
            for (DictDTO dictDTO : list) {
                dictMap.put(dictDTO.getCode(), dictDTO);
                dictMap.put(dictDTO.getName(), dictDTO);
            }
        }
        return dictMap;
    }
    @Override
    public Map<String, Map<String, DictDTO>> listDictMap(String... groupCode) {
        List<DictVO> listDictData = dictService.getDictListByCode(groupCode);
        Map<String, Map<String, DictDTO>> listData = Maps.newHashMap();
        Optional.ofNullable(listDictData).ifPresent(listDict -> {
            // 先分组
            Map<String, List<DictVO>> collect = listDict.stream().collect(Collectors.groupingBy(DictVO::getGroupCode));
            // 再分Map
            collect.forEach((key, list) -> {
                List<DictDTO> dictDTOList = BeanUtils.copyListProperties(list, DictDTO.class);
                Map<String, DictDTO> dictMap = dictDTOList.stream().collect(Collectors.toMap(DictDTO::getCode, dict -> dict));
                listData.put(key, dictMap);
            });
        });
        return listData;
    }



    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> groupCode) {
        List<DictVO> listDictData = dictService.getDictListByCode(groupCode);
        Map<String, Map<String, DictInDTO>> listData = Maps.newHashMap();
        Optional.ofNullable(listDictData).ifPresent(listDict -> {
            // 先分组
            Map<String, List<DictVO>> collect = listDict.stream().collect(Collectors.groupingBy(DictVO::getGroupCode));
            // 再分Map
            collect.forEach((key, list) -> {
                List<DictInDTO> dictDTOList = BeanUtils.copyListProperties(list, DictInDTO.class);
                Map<String, DictInDTO> dictMap = dictDTOList.stream().collect(Collectors.toMap(DictInDTO::getCode, dict -> dict));
                listData.put(key, dictMap);
            });
        });
        return listData;
    }
    @Override
    public List<String> listDictNameByDictGroup(String typeGroupCode) {
        return dictService.listDictNameByDictGroup(typeGroupCode);
    }
    @Override
    public String get(String groupCode, Object code) {
        if (EmptyUtils.isEmpty(code)) {
            return "";
        }
        DictVO dictVO = dictService.getDictByDictGroupAndCode(groupCode, code);
        if (EmptyUtils.isEmpty(dictVO)) {
            return "";
        }
        return dictVO.getName();
    }
    @Override
    public String getCodeByName(String groupCode, Object name) {
        if (EmptyUtils.isEmpty(name)) {
            return "";
        }
        DictVO dictVO = dictService.getDictByDictGroupAndName(groupCode, name);
        if (EmptyUtils.isEmpty(dictVO)) {
            return "";
        }
        return dictVO.getCode();
    }
    @Override
    public List<Map<String, Object>> getExtendTableData(String sql) {
        List<Map<String, Object>> list = dictService.getEntityDao().createSQLQuery(sql).setCacheable(false).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (EmptyUtils.isEmpty(list)) {
            list = new ArrayList<Map<String, Object>>();
        }
        return list;
    }
    @Override
    public String getByName(String groupCode,String code) {
        if (EmptyUtils.isEmpty(code)) {
            return "";
        }
        DictVO dictVO = dictService.getDictByDictName(groupCode,code);
        if (EmptyUtils.isEmpty(dictVO)) {
            return "";
        }
        return dictVO.getName();
    }
    @Override
    public Map<String, String> getNameByTypeCodesAndTypeGroupCode(List<String> typeCodes, String groupCode){
       List<DictVO> lists = dictService.getNameByTypeCodesAndTypeGroupCode(typeCodes,groupCode);
       return lists.stream().collect(Collectors.toMap(DictVO::getCode,DictVO::getName));
    }
}
