package com.bytter.system.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.bytter.core.utils.*;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.SysOrgantypeConstant;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.system.api.enums.OrgantypeMessageEnum;
import com.bytter.system.dao.OrgantypeCorpDao;
import com.bytter.system.entity.OrgantypeCorp;
import com.bytter.system.vo.OrgantypeCorpVO;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Service
 * @Description: 组织架构单位层级关联表业务逻辑操作
 * @date 2020-05-14 16:01:41
 */
@Service("organtypeCorpService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class OrgantypeCorpService extends BaseService<OrgantypeCorp, String> {
    @Autowired
    private OrgantypeCorpDao organtypeCorpDao;
    @Autowired
    //@Qualifier("corpService")
    private CorpService corpService;
    @Autowired
    private IDictService dictServiceI;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(OrgantypeCorp entity) {
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
    public void save(OrgantypeCorp entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(OrgantypeCorp entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(OrgantypeCorp entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<OrgantypeCorp, String> getEntityDao() {
        return organtypeCorpDao;
    }
    /**
     * 通VO保存
     *
     * @param organtypeCorpVO
     */
    public void saveOrgantypeCorp(OrgantypeCorpVO organtypeCorpVO) {
        organtypeCorpVO.setId(null);
        saveOrUpdateOrgantypeCorp(organtypeCorpVO);
    }
    /**
     * 通VO更新
     *
     * @param organtypeCorpVO
     */
    public void updateOrgantypeCorp(OrgantypeCorpVO organtypeCorpVO) {
        saveOrUpdateOrgantypeCorp(organtypeCorpVO);
    }
    /**
     * 通VO保存or更新
     *
     * @param organtypeCorpVO
     */
    public void saveOrUpdateOrgantypeCorp(OrgantypeCorpVO organtypeCorpVO) {
        if (organtypeCorpVO != null) {
            OrgantypeCorp organtypeCorp = null;
            if (StringUtils.isNotEmpty(organtypeCorpVO.getId())) {
                organtypeCorp = this.get(organtypeCorpVO.getId(), true);
            } else {
                organtypeCorp = new OrgantypeCorp();
            }
            BeanUtils.copyNotNullProperties(organtypeCorpVO, organtypeCorp);
            saveOrUpdate(organtypeCorp);
        }
    }
    /**
     * 保存一级网点单位页面
     *
     * @param orgtCorpVO
     */
    public boolean saveOrgtFirstCorp(OrgantypeCorpVO orgtCorpVO, ICurrentUser userVO) {
        if (orgtCorpVO != null && StringUtils.isNotEmpty(orgtCorpVO.getOrgtId()) && StringUtils.isNotEmpty(orgtCorpVO.getCorpId())) {
            String[] corpId = orgtCorpVO.getCorpId().split(",");
            for (int i = 0; i < corpId.length; i++) {
                OrgantypeCorp organtypeCorp = new OrgantypeCorp();
                organtypeCorp.setOrgtId(orgtCorpVO.getOrgtId());
                organtypeCorp.setCorpId(corpId[i]);
                if (userVO != null) {
                    organtypeCorp.setCreateId(userVO.getId());
                }
                organtypeCorp.setCreateTime(new Date());
                saveOrUpdate(organtypeCorp);
            }
            return true;
        }
        return false;
    }
    /**
     * 保存组织架构单位上下级关系
     *
     * @param orgtCorpVO
     */
    public boolean saveOrgtSupCorp(OrgantypeCorpVO orgtCorpVO, ICurrentUser userVO) {
        if (orgtCorpVO != null
                && StringUtils.isNotEmpty(orgtCorpVO.getOrgtId())
                && StringUtils.isNotEmpty(orgtCorpVO.getCorpId())
                && StringUtils.isNotEmpty(orgtCorpVO.getParentId())) {
            OrgantypeCorp organtypeCorp = organtypeCorpDao.getOrgtCorpByCorpIdAndRgatId(orgtCorpVO.getCorpId(), orgtCorpVO.getOrgtId());
            organtypeCorp.setParentId(orgtCorpVO.getParentId());
            if (userVO != null) {
                organtypeCorp.setUpdateId(userVO.getId());
            }
            organtypeCorp.setUpdateTime(new Date());
            saveOrUpdate(organtypeCorp);
            return true;
        }
        return false;
    }
    /**
     * 保存子单位页面
     *
     * @param orgtCorpVO
     */
    public boolean saveOrgtChildCorp(OrgantypeCorpVO orgtCorpVO, ICurrentUser userVO) {
        if (orgtCorpVO != null
                && StringUtils.isNotEmpty(orgtCorpVO.getOrgtId())
                && StringUtils.isNotEmpty(orgtCorpVO.getCorpId())
                && StringUtils.isNotEmpty(orgtCorpVO.getParentId())) {
            String[] corpId = orgtCorpVO.getCorpId().split(",");
            for (int i = 0; i < corpId.length; i++) {
                OrgantypeCorp organtypeCorp = new OrgantypeCorp();
                organtypeCorp.setOrgtId(orgtCorpVO.getOrgtId());
                organtypeCorp.setCorpId(corpId[i]);
                organtypeCorp.setParentId(orgtCorpVO.getParentId());
                if (userVO != null) {
                    organtypeCorp.setCreateId(userVO.getId());
                }
                organtypeCorp.setCreateTime(new Date());
                saveOrUpdate(organtypeCorp);
            }
            return true;
        }
        return false;
    }
    /**
     * 组织架构单位层级关联表 列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        return organtypeCorpDao.getDataTables(dataTablesInput);
    }
    /**
     * 根据ID查询组织架构单位层级关联表VO
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public OrgantypeCorpVO getOrgantypeCorpVO(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        OrgantypeCorp organtypeCorp = this.get(id, true);
        if (organtypeCorp == null) {
            return null;
        }
        OrgantypeCorpVO organtypeCorpVO = new OrgantypeCorpVO();
        BeanUtils.copyNotNullProperties(organtypeCorp, organtypeCorpVO);
        return organtypeCorpVO;
    }
    /**
     * 根据id删除组织架构id及所有子单位
     *
     * @param id
     */
    public boolean deleteOrgtCorpById(String id, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(id)) {
            OrgantypeCorp organtypeCorp = this.get(id, true);
            if (organtypeCorp == null) {
                return false;
            }
            //根据corpId和orgtId查询是否有子单位
            Map<String, Object> params = new HashMap<>();
            params.put("orgtId", organtypeCorp.getOrgtId());
            params.put("parentId", organtypeCorp.getCorpId());
            //根据组织架构id获取单位层级关联表
            List<OrgantypeCorpVO> organtypeCorpVOS = this.getOrgtCorpByParams(params);
            if (organtypeCorpVOS != null && !organtypeCorpVOS.isEmpty()) {
                throw new BusinessException(OrgantypeMessageEnum.SYS_ORGANTYPE_CORPSON_NOTDELETE);
            }
            //删除单位
            this.deletes(id);
            return true;
        }
        return false;
    }
    /**
     * 根据参数查询组织架构单位层级关联信息
     *
     * @param params 查询参数
     * @return
     */
    public List<OrgantypeCorpVO> getOrgtCorpByParams(Map<String, Object> params) {
        List<OrgantypeCorpVO> list = organtypeCorpDao.getOrgtCorpByParams(params);
        if (EmptyUtils.isNotEmpty(list)) {
            //状态
            Map<String, DictDTO> statusDictMap = dictServiceI.listMap(SysOrgantypeConstant.ORGANTYPE_STATUS_DICT);
            //组织类型
            Map<String, DictDTO> typeDictMap = dictServiceI.listMap(SysOrgantypeConstant.ORGANTYPE_TYPE_DICT);
            //单位属性
            Map<String, DictDTO> unitattributeDictMap = dictServiceI.listMap(SysOrgantypeConstant.ORGANTYPE_UNITATTRIBUTE_DICT);
            for (OrgantypeCorpVO vo:list) {
                if (vo.getStatus() != null && statusDictMap.containsKey(vo.getStatus().toString())) {
                    vo.setStatusName(statusDictMap.get(vo.getStatus().toString()).getName());
                }
                if (vo.getType() != null && typeDictMap.containsKey(vo.getType().toString())) {
                    vo.setTypeName(typeDictMap.get(vo.getType().toString()).getName());
                }
                if (!StringUtil.isBlank(vo.getUnitAttribute()) && unitattributeDictMap.containsKey(vo.getUnitAttribute())) {
                    vo.setUnitAttributeName(unitattributeDictMap.get(vo.getUnitAttribute()).getName());
                }
            }
        }
        return list;
    }
    /**
     * 根据组织架构Id查询组织架构单位层级关联信息
     *
     * @param orgtId 组织架构Id
     * @return
     */
    public List<OrgantypeCorpVO> getOrgtCorpByOrgtId(String orgtId) {
        if (StringUtils.isBlank(orgtId)) {
            return null;
        }
        //发布组织架构单位层级关联归档表
        Map<String, Object> params = new HashMap<>();
        params.put("orgtId", orgtId);
        //根据组织架构id获取单位层级关联表
        List<OrgantypeCorpVO> organtypeCorpVOS = this.getOrgtCorpByParams(params);
        return organtypeCorpVOS;
    }
    /**
     * 获取所有组织架构单位层级关联信息，排序为树形顺序机构
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<OrgantypeCorpVO> listOrgantypeCorpVOTree(OrgantypeCorpVO search) {
        // 查询数据
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isBlank(search.getOrgtId())) {
            return null;
        }
        params.put("orgtId", search.getOrgtId());
        if (StringUtils.isNotEmpty(search.getCorpName())) {
            params.put("corpName", "%" + search.getCorpName() + "%");
        }
        if (StringUtils.isNotEmpty(search.getId())) {
            params.put("id", search.getId());
        }
        List<OrgantypeCorpVO> list = this.getOrgtCorpByParams(params);
        // 将数据转成树结构
        List<OrgantypeCorpVO> listTree = initOrgtCorpVOTree(list, search.getOrgtId());
        return listTree;
    }
    /**
     * 对树表结构进行转换
     *
     * @param list 组织架构单位LIST
     * @return
     */
    public List<OrgantypeCorpVO> initOrgtCorpVOTree(List<OrgantypeCorpVO> list, String orgtId) {
        List<OrgantypeCorpVO> listTree = null;
        if (list != null && list.size() > 0) {
            listTree = new ArrayList<>();
            // 所有含有子单位的rootMap
            Map<String, OrgantypeCorpVO> rootMap = new LinkedHashMap<>();
            initRootMap(list, rootMap, orgtId);
            for (OrgantypeCorpVO info : list) {
                if (StringUtils.isEmpty(info.getParentId()) || !rootMap.containsKey(info.getParentId())) {
                    listTree.add(info);
                    if (rootMap.containsKey(info.getCorpId())) {
                        // 生成每个子单位下的单位
                        this.initAllSonOrgtCorp(info.getSonOrgtCorpVOCorpList(), listTree, rootMap);
                    }
                }
            }
        }
        return listTree;
    }
    /**
     * 生成每个含有子单位的Map
     *
     * @param list    所有单位LIST
     * @param rootMap 含有子单位的Map
     * @return
     */
    public void initRootMap(List<OrgantypeCorpVO> list, Map<String, OrgantypeCorpVO> rootMap, String orgtId) {
        // 查询所有ParentIds
        List<String> ids = organtypeCorpDao.getAllOrgtCorpParentIds(orgtId);
        for (OrgantypeCorpVO info : list) {
            if (StringUtils.isEmpty(info.getParentId()) || ids.contains(info.getCorpId())) {
                rootMap.put(info.getCorpId(), info);
            }
        }
        for (OrgantypeCorpVO info : list) {
            if (rootMap.containsKey(info.getParentId())) {
                List<OrgantypeCorpVO> sonList = rootMap.get(info.getParentId()).getSonOrgtCorpVOCorpList();
                if (sonList == null) {
                    sonList = new ArrayList<>();
                    rootMap.get(info.getParentId()).setSonOrgtCorpVOCorpList(sonList);
                }
                sonList.add(info);
            }
        }
    }
    /**
     * 组织架构子单位添加
     *
     * @param list     子单位List
     * @param listTree 单位树List
     * @param rootMap  含有子单位的Map
     * @return
     */
    public void initAllSonOrgtCorp(List<OrgantypeCorpVO> list, List<OrgantypeCorpVO> listTree, Map<String, OrgantypeCorpVO> rootMap) {
        if (list != null && list.size() > 0) {
            for (OrgantypeCorpVO orgtCorpVo : list) {
                listTree.add(orgtCorpVo);
                OrgantypeCorpVO orgtCorp = rootMap.get(orgtCorpVo.getCorpId());
                if (orgtCorp != null) {
                    // 含有子单位时进行回调
                    this.initAllSonOrgtCorp(orgtCorp.getSonOrgtCorpVOCorpList(), listTree, rootMap);
                }
            }
        }
    }
    /**
     * 下拉树组织架构单位查询
     *
     * @param resource 资源值
     * @param user
     * @return 列表数据
     * @throws
     */
    @Transactional(readOnly = true)
    public Object getCorpTreeMapList(String orgtId, String resource, ICurrentUser user) {
        if (StringUtils.isNotEmpty(orgtId)) {
            String corpAuthSql = null;
            // 添加资源控制单位
            if (StringUtils.isNotEmpty(resource)) {
                List<String> corpIds = corpService.getUserCorpIds(resource, user.getId());
                corpAuthSql = SqlUtil.getInSql(corpIds, "c.corp_Id", true);
            }
            // 查询有资源的单位LIST
            List<String> list = organtypeCorpDao.listOrgtCorpIdsByOrgtId(orgtId, corpAuthSql);
            List<Map<String, Object>> listTree = null;
            listTree = initOrgtCorpVOTreeMap(list, orgtId);
            return listTree;
        }
        return null;
    }
    /**
     * 单位下拉树结构进行转换
     *
     * @param list 查询到的单位List
     * @return
     */
    public List<Map<String, Object>> initOrgtCorpVOTreeMap(List<String> list, String orgtId) {
        List<Map<String, Object>> listTree = null;
        if (list != null && list.size() > 0) {
            listTree = new ArrayList<>();
            Map<String, OrgantypeCorpVO> rootMap = new LinkedHashMap<>();
            // 查询组织架构所有单位
            List<OrgantypeCorpVO> listAll = this.getOrgtCorpByOrgtId(orgtId);
            initRootMap(listAll, rootMap, orgtId);
            for (Map.Entry<String, OrgantypeCorpVO> info : rootMap.entrySet()) {
                if (StringUtils.isEmpty(info.getValue().getParentId())) {
                    Map<String, Object> mapInfo = new HashMap<>();
                    // 当前单位有效且有资源
                    if (list.contains(info.getValue().getCorpId())) {
                        mapInfo.put("corpId", info.getValue().getCorpId());
                        mapInfo.put("name", "(" + info.getValue().getCorpCode() + ")" + info.getValue().getCorpName());
                    }
                    // 根节点被禁用，取子节点
                    OrgantypeCorpVO organtypeCorpVO = rootMap.get(info.getValue().getCorpId());
                    if (organtypeCorpVO != null) {
                        this.initAllSonOrgtCorpTree(organtypeCorpVO.getSonOrgtCorpVOCorpList(), mapInfo, rootMap, listTree, list);
                    }
                    if (mapInfo.size() > 0) {
                        listTree.add(mapInfo);
                    }
                }
            }
        }
        return listTree;
    }
    /**
     * 子单位树初始化
     *
     * @param sonList  子单位List
     * @param mapInfo  根单位信息
     * @param rootMap  含有子单位的Map
     * @param listTree 单位下拉树List
     * @param list     有资源的单位LIST
     * @return
     */
    public void initAllSonOrgtCorpTree(List<OrgantypeCorpVO> sonList, Map<String, Object> mapInfo, Map<String, OrgantypeCorpVO> rootMap,
                                       List<Map<String, Object>> listTree, List<String> list) {
        List<Map<String, Object>> children = new ArrayList<>();
        if (sonList != null && sonList.size() > 0) {
            for (OrgantypeCorpVO sonInfo : sonList) {
                OrgantypeCorpVO ab = rootMap.get(sonInfo.getCorpId());
                if (list.contains(sonInfo.getCorpId())) {
                    Map<String, Object> mapInfo2 = new HashMap<>();
                    mapInfo2.put("corpId", sonInfo.getCorpId());
                    mapInfo2.put("name", "(" + sonInfo.getCorpCode() + ")" + sonInfo.getCorpName());
                    if (ab != null) {
                        this.initAllSonOrgtCorpTree(ab.getSonOrgtCorpVOCorpList(), mapInfo2, rootMap, listTree, list);
                    }
                    // mapInfo小于1表示根节点被禁用
                    if (mapInfo.size() < 1) {
                        // 添加节点
                        listTree.add(mapInfo2);
                    } else {
                        // 添加子节点
                        children.add(mapInfo2);
                    }
                }
            }
            if (mapInfo.containsKey("children")) {
                children.addAll((List<Map<String, Object>>) mapInfo.get("children"));
            }
            if (children.size() > 0) {
                mapInfo.put("children", children);
            }
        }
    }
    /**
     * 管理单元导出数据处理
     *
     * @param queryVO 查询条件VO
     * @return 列表数据
     */
    @Transactional(readOnly = true)
    public List<OrgantypeCorpVO> listExcelCorpTreeVOTree(OrgantypeCorpVO queryVO) {
        List<OrgantypeCorpVO> listTree = new ArrayList<>();
        // map用来存空格与parentId的对应关系
        Map<String, String> map = new HashMap();
        // 查询数据
        List<OrgantypeCorpVO> list = this.listOrgantypeCorpVOTree(queryVO);
        String parentId = null;
        // 空格数，起始没空格
        String s = "";
        for (OrgantypeCorpVO corpVO : list) {
            if (StringUtils.isEmpty(corpVO.getParentId())) {
                s = "";
            }
            // parentId不为空且map中没有时，进行index增加
            if (StringUtils.isNotEmpty(corpVO.getParentId()) && StringUtils.isEmpty(map.get(corpVO.getParentId()))) {
                parentId = corpVO.getParentId();
                s += "    ";
            }
            // map中有时，空格取map中的
            if (StringUtils.isNotEmpty(map.get(corpVO.getParentId()))) {
                s = map.get(corpVO.getParentId());
            }
            corpVO.setCorpName(s + "(" + corpVO.getCorpCode() + ")" + corpVO.getCorpName());
            map.put(parentId, s);
            listTree.add(corpVO);
        }
        return listTree;
    }
}
