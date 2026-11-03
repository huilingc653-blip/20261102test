package com.bytter.system.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.dao.DictDao;
import com.bytter.system.dao.DictGroupDao;
import com.bytter.system.entity.Dict;
import com.bytter.system.entity.DictGroup;
import com.bytter.system.vo.DictVO;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 字典管理Service
 *
 * @author zhoul
 * @date 2017/5/15
 */
@Service("dictService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DictService extends BaseService<Dict, String> {
    @Autowired
    private DictDao dictDao;
    @Autowired
    private DictGroupDao dictGroupDao;
    @Autowired
    private Ii18nService i18nServiceI;
    @Override
    public BaseDao<Dict, String> getEntityDao() {
        return dictDao;
    }
    /**
     * 获取字典菜单列表数据
     *
     * @param input
     * @return
     */
    public TableGridResponse<Dict> getTreeGrid(TableGridInput input) {
        return dictDao.listDict(input);
    }
    /**
     * 列出所有字典组
     *
     * @return
     */
    public List<DictGroup> listAllDictGroup() {
        return dictGroupDao.listAll();
    }
    /**
     * 根据字典组code查找
     *
     * @param typeGroupCode 字典组code
     * @return
     */
    public DictGroup getDictGroupByGroupCode(String typeGroupCode) {
        return dictGroupDao.getUniqueBy("groupCode", typeGroupCode);
    }
    /**
     * 根据字典组code获取所属字典集合
     *
     * @param typeGroupCode 字典组code
     * @return
     */
    public List<DictVO> getDictGroupByCode(String typeGroupCode) {
        List<DictVO> dictByDictGroup = dictDao.getDictByDictGroup(typeGroupCode);
        for (DictVO dictVO : dictByDictGroup) {
            String baseContext = i18nServiceI.getBaseContext(dictVO.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                dictVO.setName(baseContext);
            }
        }
        return dictByDictGroup;
    }
    /**
     * 根据字典组code获取所属字典集合，并根据根据Locate自动国际化。
     *
     * @param groupCode 字典组code。
     * @return 字典数据。
     */
    public List<DictVO> getDictListByCode(String... groupCode) {
        List<DictVO> dictList = dictDao.getDictListByGroupCode(groupCode);
        for (DictVO dictVO : dictList) {
            // 国际化
            String baseContext = i18nServiceI.getBaseContext(dictVO.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                dictVO.setName(baseContext);
            }
        }
        return dictList;
    }

    public List<DictVO> getDictListByCode(List<String> groupCode) {
        List<DictVO> dictList = dictDao.getDictListByGroupCodes(groupCode);
        for (DictVO dictVO : dictList) {
            // 国际化
            String baseContext = i18nServiceI.getBaseContext(dictVO.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                dictVO.setName(baseContext);
            }
        }
        return dictList;
    }

    /**
     * 根据字典组code获取字典数据Map，并根据Locate自动国际化。
     *
     * @param groupCode 字典组code。
     * @return 字典数据。
     */
    public Map<String, Map<String, DictVO>> listDictMap(String... groupCode) {
        List<DictVO> listDictData = getDictListByCode(groupCode);
        Map<String, Map<String, DictVO>> listData = Maps.newHashMap();
        Optional.ofNullable(listDictData).ifPresent(listDict -> {
            // 先分组
            Map<String, List<DictVO>> collect = listDict.stream().collect(Collectors.groupingBy(DictVO::getGroupCode));
            // 再分Map
            collect.forEach((key, list) -> {
                Map<String, DictVO> dictMap = list.stream().collect(Collectors.toMap(DictVO::getCode, dict -> dict));
                listData.put(key, dictMap);
            });
        });
        return listData;
    }
    /**
     * 根据字典组code获取字典名称集合
     *
     * @param typeGroupCode 字典组code
     * @return 字典名称集合
     */
    public List<String> listDictNameByDictGroup(String typeGroupCode) {
        return dictDao.listDictNameByDictGroup(typeGroupCode);
    }
    /**
     * 根据字典组，和字典值获取字典名
     *
     * @param groupCode
     * @param code
     * @return
     */
    public DictVO getDictByDictGroupAndCode(String groupCode, Object code) {
        DictVO dictVO = dictDao.getDictByDictGroupAndCode(groupCode, code);
        if (dictVO == null) {
            return null;
        }
        //查找字典国际化内容
        String baseContext = i18nServiceI.getBaseContext(dictVO.getId());
        //容错处理，如果有内容则设置国际化内容
        if (StringUtils.isNotBlank(baseContext)) {
            dictVO.setName(baseContext);
        }
        return dictVO;
    }
    /**
     * 根据字典组和字典的名称获取字典的值
     *
     * @param groupCode
     * @param name
     * @return
     */
    public DictVO getDictByDictGroupAndName(String groupCode, Object name) {
        return dictDao.getDictByDictGroupAndName(groupCode, name);
    }
    /**
     * 根据字典组和字典的代码获取Name的值
     *
     * @param groupCode
     * @param code
     * @return
     */
    public DictVO getDictByDictName(String groupCode, String code){
        return dictDao.getDictByDictName(groupCode,code);
    }
    /**
     * 保存字典
     *
     * @param dict
     */
    public void saveDict(Dict dict) {
        DictGroup dictGroup = dict.getDictGroup();
        //新增字典，如果字典组为空，那么先往字典组中添加
        if (ObjectUtil.isNotEmpty(dictGroup) && ObjectUtil.isNotEmpty(dictGroup.getGroupCode())){
            DictGroup dbDictGroup = dictGroupDao.getUniqueByCode(dictGroup.getGroupCode());
            if (ObjectUtil.isNotEmpty(dbDictGroup)){
                dictGroup = dbDictGroup;
                dict.setDictGroup(dictGroup);
            }
        }
        if (null != dictGroup && StringUtils.isEmpty(dictGroup.getId())) {
            dictGroup.setCreateDate(new Date());
            dictGroup.setCreateName(dict.getCreateName());
            dictGroupDao.save(dictGroup);
        }
        dict.setCreateDate(new Date());
        dictDao.save(dict);
    }
    /**
     * 修改字典
     *
     * @param dict
     */
    public void updateDict(Dict dict) {
        DictGroup dictGroup = dict.getDictGroup();
        DictGroup db = dictGroupDao.get(dictGroup.getId());
        db.setGroupCode(dictGroup.getGroupCode());
        dict.setCreateDate(new Date());
        dictDao.update(dict);
        dictGroupDao.update(db);
    }
    /**
     * 删除字典
     *
     * @param dictIds
     */
    public void deleteDict(List<String> dictIds) {
        Dict dict = dictDao.get(dictIds.get(0));
        DictGroup dictGroup = dict.getDictGroup();
        dictDao.batchDelete(dictIds);
        if (dictIds.isEmpty()) {
            return;
        }
        List<Dict> dictList = dictDao.listBy("dictGroup", dictGroup);
        if (dictList.isEmpty()) {
            dictGroupDao.delete(dictGroup);
        }
    }
    /**
     * 首页加载字典数据
     *
     * @param locale
     * @return
     */
    public List<DictVO> buildDictJs(String locale) {
        return dictDao.buildDictJS(locale);
    }
    /**
     * 获取字典返回数据，有可能是字典表，也有可能是其他的数据表的字典数据
     *
     * @param showFieldName 显示字段名称
     * @param tableName     表名
     * @param valueFileName
     * @param value
     * @return
     */
    public String getDictValue(String showFieldName, String tableName, String valueFileName, Object value) {
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(valueFileName) || StringUtils.isBlank(showFieldName)) {
            return "";
        }
        /*
            如果传进来的valueFileName为id，则尝试到国际化表中查找数据
            update by zhoul 2019-11-12
         */
        if ("id".equalsIgnoreCase(valueFileName)) {
            String businessBaseContext = i18nServiceI.getBusinessBaseContext(value.toString());
            if (StringUtils.isNotBlank(businessBaseContext)) {
                return businessBaseContext;
            }
        }
        StringBuilder sql = new StringBuilder();
        Session session = getSession();
        sql.append("select ").append(showFieldName).append(" as ").append(showFieldName).append(" from ").append(tableName).append(" where ").append(valueFileName).append("=:valueFileName");
        SQLQuery query = session.createSQLQuery(sql.toString());
        query.setParameter("valueFileName", value);
        query.addScalar(showFieldName, StandardBasicTypes.STRING);
        query.setCacheable(true);
        String returnValue = (String) query.uniqueResult();
        return StringUtils.isBlank(returnValue) ? "" : returnValue;
    }
    public List<DictGroup> findList(String sqlWhere) {
        return dictDao.findList(sqlWhere);
    }
    /**
     * 根据字典组code获取所属字典集合
     *
     * @param typeGroupCode 字典组code
     * @return
     */
    public List<DictVO> listDictVOByCode(String typeGroupCode) {
        return dictDao.getDictByDictGroup(typeGroupCode);
    }
    /**
     * 查询字典组
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableDictGroup(DataTablesInput dataTablesInput) {
        return dictGroupDao.findPage(dataTablesInput);
    }

    public List<DictVO> getNameByTypeCodesAndTypeGroupCode(List<String> typeCodes, String groupCode) {
        return dictDao.getNameByTypeCodesAndTypeGroupCode(typeCodes,groupCode);
    }
}
