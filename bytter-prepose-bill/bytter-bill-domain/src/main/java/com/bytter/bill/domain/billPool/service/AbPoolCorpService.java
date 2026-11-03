package com.bytter.bill.domain.billPool.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.dao.AbPoolCorpDao;
import com.bytter.bill.domain.billPool.entity.AbPoolCorp;
import com.bytter.bill.domain.billPool.vo.AbPoolCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * @Title: AbPoolCorpService
 * @Description: 票据池单位Service
 * @author wangzy
 * @date 2019/12/30 15:19
 *
 */
@Service("abPoolCorpService")
@Transactional
public class AbPoolCorpService extends BaseService<AbPoolCorp, String> {
    @Autowired
    private AbPoolCorpDao abPoolCorpDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private AbPoolCheckService abPoolCheckService;
    @Override
    public BaseDao<AbPoolCorp, String> getEntityDao() {
        return abPoolCorpDao;
    }
    /**
     * 票据池单位查询
     * @param queryVO 查询条件VO
     * @return 列表数据
     *
     */
    @Transactional(readOnly = true)
    public List<AbPoolCorpVO> listAbPoolCorpVOTree(AbPoolCorpVO queryVO){
        // 查询数据
        List<AbPoolCorpVO> list = listAbPoolCorpVO(queryVO);
        // 将数据转成树结构
        return initAbPoolCorpVOTree(list);
    }
    /**
     * 票据池单位查询
     * @param queryVO 查询条件VO
     * @return 列表数据
     *
     */
    @Transactional(readOnly = true)
    public List<AbPoolCorpVO> listAbPoolCorpVO(AbPoolCorpVO queryVO){
        Map<String,Object> params=new HashMap<>();
        if (queryVO != null){
            if (StringUtils.isNotEmpty(queryVO.getCorpName())){
                params.put("corpName", "%"+queryVO.getCorpName()+"%");
            }
            if (StringUtils.isNotEmpty(queryVO.getCorpCode())){
                params.put("corpCode", "%"+queryVO.getCorpCode()+"%");
            }
        }
        return abPoolCorpDao.listAbPoolCorpVO(params);
    }
    /**
     * 对树表结构进行转换
     * @param list 票据池单位LIST
     * @return
     */
    public List<AbPoolCorpVO> initAbPoolCorpVOTree(List<AbPoolCorpVO> list){
        List<AbPoolCorpVO> listTree = null;
        if (list != null && list.size() > 0) {
            listTree = new ArrayList<>();
            // 所有含有子单位的rootMap
            Map<String, AbPoolCorpVO> rootMap = new LinkedHashMap<>();
            initRootMap(list, rootMap);
            for (AbPoolCorpVO info : list){
                if (StringUtils.isEmpty(info.getParentId()) || !rootMap.containsKey(info.getParentId())){
                    listTree.add(info);
                    if (rootMap.containsKey(info.getId())){
                        // 生成每个子单位下的单位
                        this.initAllSonPoolCorp(info.getSonAbPoolCorpList(), listTree, rootMap);
                    }
                }
            }
        }
        return listTree;
    }
    /**
     * 票据池子单位添加
     * @param list 子单位List
     * @param listTree 单位树List
     * @param rootMap 含有子单位的Map
     * @return
     */
    public void initAllSonPoolCorp(List<AbPoolCorpVO> list, List<AbPoolCorpVO> listTree, Map<String, AbPoolCorpVO> rootMap){
        if (list != null && list.size() > 0){
            for (AbPoolCorpVO abPoolCorpVO:list){
                listTree.add(abPoolCorpVO);
                AbPoolCorpVO ab = rootMap.get(abPoolCorpVO.getId());
                if (ab != null){
                    // 含有子单位时进行回调
                    this.initAllSonPoolCorp(ab.getSonAbPoolCorpList(), listTree, rootMap);
                }
            }
        }
    }
    /**
     * 下拉树票据池单位查询
     * @param resource 资源值
     * @param user
     * @return 列表数据
     * @throws
     */
    @Transactional(readOnly = true)
    public Object getCorpTreeMapList(String resource, ICurrentUser user){
        String corpAuthSql = null;
        // 添加资源控制单位
        if (StringUtils.isNotEmpty(resource)){
            List<String> corpIds = corpService.getUserCorpIds(resource, user.getId());
            corpAuthSql = SqlUtil.getInSql(corpIds, "p.corp_Id", true);
        }
        // 查询有资源的单位LIST
        List<String> list = abPoolCorpDao.listAllPoolCorpIds(corpAuthSql);
        List<Map<String, Object>> listTree = null;
        listTree = initAbPoolCorpVOTreeMap(list);
        return listTree;
    }
    /**
     * 单位下拉树结构进行转换
     * @param list 查询到的单位List
     * @return
     */
    public List<Map<String, Object>> initAbPoolCorpVOTreeMap(List<String> list){
        List<Map<String, Object>> listTree = null;
        if (list != null && list.size() > 0){
            listTree = new ArrayList<>();
            Map<String, AbPoolCorpVO> rootMap = new LinkedHashMap<>();
            // 查询票据池所有单位
            List<AbPoolCorpVO> listAll = this.listAbPoolCorpVO(null);
            initRootMap(listAll, rootMap);
            for (Map.Entry<String, AbPoolCorpVO> info:rootMap.entrySet()){
                if (StringUtils.isEmpty(info.getValue().getParentId())){
                    Map<String, Object> mapInfo = new HashMap<>();
                    // 当前单位有效且有资源
                    if (AbPoolConstant.AB_POLL_VALID_SIGN_1.equals(info.getValue().getValidSign()) && list.contains(info.getValue().getCorpId())){
                        mapInfo.put("corpId", info.getValue().getCorpId());
                        mapInfo.put("name", "("+info.getValue().getCorpCode()+")"+info.getValue().getCorpName());
                    }
                    // 根节点被禁用，取子节点
                    AbPoolCorpVO abPoolCorpVO = rootMap.get(info.getValue().getId());
                    if (abPoolCorpVO != null) {
                        this.initAllSonPoolCorpTree(abPoolCorpVO.getSonAbPoolCorpList(), mapInfo, rootMap, listTree, list);
                    }
                    if (mapInfo.size() > 0){
                        listTree.add(mapInfo);
                    }
                }
            }
        }
        return listTree;
    }
    /**
     * 子单位树初始化
     * @param sonList 子单位List
     * @param mapInfo 根单位信息
     * @param rootMap 含有子单位的Map
     * @param listTree 单位下拉树List
     * @param list 有资源的单位LIST
     * @return
     */
    public void initAllSonPoolCorpTree(List<AbPoolCorpVO> sonList, Map<String, Object> mapInfo, Map<String, AbPoolCorpVO> rootMap,
                                       List<Map<String, Object>> listTree, List<String> list){
        List<Map<String, Object>> children = new ArrayList<>();
        if (sonList != null && sonList.size() > 0){
            for (AbPoolCorpVO sonInfo : sonList){
                AbPoolCorpVO ab = rootMap.get(sonInfo.getId());
                if (AbPoolConstant.AB_POLL_VALID_SIGN_1.equals(sonInfo.getValidSign()) && list.contains(sonInfo.getCorpId())){
                    Map<String, Object> mapInfo2 = new HashMap<>();
                    mapInfo2.put("corpId", sonInfo.getCorpId());
                    mapInfo2.put("name", "("+sonInfo.getCorpCode()+")"+sonInfo.getCorpName());
                    if (ab != null){
                        this.initAllSonPoolCorpTree(ab.getSonAbPoolCorpList(), mapInfo2, rootMap, listTree, list);
                    }
                    // mapInfo小于1表示根节点被禁用
                    if (mapInfo.size() < 1){
                        // 添加节点
                        listTree.add(mapInfo2);
                    }else {
                        // 添加子节点
                        children.add(mapInfo2);
                    }
                }else {
                    // 当前单位节点被禁用时，查询其下面的子单位节点
                    if(ab != null){
                        this.initAllSonPoolCorpTree(ab.getSonAbPoolCorpList(), mapInfo, rootMap, listTree, list);
                    }
                }
            }
            if (mapInfo.containsKey("children")){
                children.addAll((List<Map<String, Object>>)mapInfo.get("children"));
            }
            if ( children.size() > 0){
                mapInfo.put("children", children);
            }
        }
    }
    /**
     * 生成每个含有子单位的Map
     * @param list 所有单位LIST
     * @param rootMap 含有子单位的Map
     * @return
     */
    public void initRootMap(List<AbPoolCorpVO> list, Map<String, AbPoolCorpVO> rootMap) {
        // 查询所有ParentIds
        List<String> ids = abPoolCorpDao.getAllPoolParentIds();
        for (AbPoolCorpVO info : list) {
            if (StringUtils.isEmpty(info.getParentId()) || ids.contains(info.getId())) {
                rootMap.put(info.getId(), info);
            }
            if (rootMap.containsKey(info.getParentId())) {
                List<AbPoolCorpVO> sonList = rootMap.get(info.getParentId()).getSonAbPoolCorpList();
                if (sonList == null) {
                    sonList = new ArrayList<>();
                    rootMap.get(info.getParentId()).setSonAbPoolCorpList(sonList);
                }
                sonList.add(info);
            }
        }
    }
    /**
     * 票据池单位维护新增或修改操作
     * @param infoVO 票据池单位信息
     * @param user 用户
     * @return 处理结果
     * @throws
     */
    public AjaxJson saveOrUpdate(AbPoolCorpVO infoVO, ICurrentUser user){
        AjaxJson reJson = new AjaxJson();
        AbPoolCorp abPoolCorp = null;
        List<String> corpIdList = new ArrayList<>();
        Optional.ofNullable(infoVO.getCorpId()).ifPresent(corpIdList::add);
        Optional.ofNullable(infoVO.getParentId()).ifPresent(corpIdList::add);
        Map<String, CorpDTO> corpMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(corpIdList)){
            corpMap = corpService.listCorpDtoByCorpIds(corpIdList);
        }
        // 如果有id 则为修改
        if (StringUtils.isNotEmpty(infoVO.getId())){
            abPoolCorp = this.get(infoVO.getId());
            abPoolCorp.setCorpId(infoVO.getCorpId());
            if (corpMap.containsKey(infoVO.getCorpId())){
                CorpDTO corpDTO = corpMap.get(infoVO.getCorpId());
                abPoolCorp.setCorpCode(corpDTO.getCode());
                abPoolCorp.setCorpName(corpDTO.getName());
            }
            abPoolCorp.setUpdateId(user.getId());
            abPoolCorp.setUpdateTime(new Date());
        }else {
            abPoolCorp = new AbPoolCorp();
            abPoolCorp.setCreateId(user.getId());
            abPoolCorp.setCreateTime(new Date());
            abPoolCorp.setCorpId(infoVO.getCorpId());
            if (corpMap.containsKey(infoVO.getCorpId())){
                CorpDTO corpDTO = corpMap.get(infoVO.getCorpId());
                abPoolCorp.setCorpCode(corpDTO.getCode());
                abPoolCorp.setCorpName(corpDTO.getName());
            }
            if (StringUtils.isNotEmpty(infoVO.getParentId())){
                abPoolCorp.setBustypeLevel(infoVO.getBustypeLevel()+1);
                abPoolCorp.setParentId(infoVO.getParentId());
            } else {
                abPoolCorp.setBustypeLevel(AbPoolConstant.AB_POLL_BUSTYPE_LEVEL_1);
            }
            abPoolCorp.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
        }
        this.saveOrUpdate(abPoolCorp);
        reJson.setSuccess(true);
        return reJson;
    }
    /**
     * 票据池单位修改
     * @param id 票据池单位ID
     * @return 列表数据
     * @throws
     */
    @Transactional(readOnly = true)
    public AbPoolCorpVO getAbPoolCorpInfo(String id){
        if (StringUtils.isEmpty(id)){
            return null;
        }
        return abPoolCorpDao.getAbPoolCorpInfo(id);
    }
    /**
     * 根据单位id进行逻辑删除
     * @param ids 选中的所有单位Id
     * @return
     */
    public AjaxJson deleteAbPoolCorp(String ids, ICurrentUser user){
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        List<AbPoolCorpVO> list = listAbPoolCorpVO(null);
        Map<String, AbPoolCorpVO> rootMap = new LinkedHashMap<>();
        if (list != null && list.size() > 0){
            initRootMap(list, rootMap);
        }
        AbPoolCorp abPoolCorp = null;
        if (StringUtils.isNotEmpty(ids)){
            String[] idArray = ids.split(",");
            for (String id : idArray){
                abPoolCorp = this.get(id);
                // 处于启用状态的单位无法删除
                if (AbPoolConstant.AB_POLL_VALID_SIGN_1.equals(abPoolCorp.getValidSign())){
                    throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_POOLCORP_ERROR);
                }
                // 删除子单位
                if (rootMap.containsKey(id)){
                    this.deleteSonPoolCorp(rootMap.get(id).getSonAbPoolCorpList(), user, rootMap);
                }
                // 改为无效
                abPoolCorp.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
                abPoolCorp.setUpdateTime(new Date());
                abPoolCorp.setUpdateId(user.getId());
                this.updateEntity(abPoolCorp);
            }
            reJson.setSuccess(true);
        }
        return reJson;
    }
    /**
     * 根据单位id进行逻辑删除
     * @param list 子单位LIST
     * @param user 用户
     * @param rootMap 含有子单位的Map
     * @return
     */
    public void deleteSonPoolCorp(List<AbPoolCorpVO> list, ICurrentUser user, Map<String, AbPoolCorpVO> rootMap){
        if (list != null && list.size() > 0){
            AbPoolCorp abPoolCorp = null;
            for (AbPoolCorpVO abPoolCorpVO : list){
                abPoolCorp = this.get(abPoolCorpVO.getId());
                if (AbPoolConstant.AB_POLL_VALID_SIGN_1.equals(abPoolCorp.getValidSign())){
                    throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_SONPOOLCORP_ERROR);
                }
                // 改为无效
                abPoolCorp.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
                abPoolCorp.setUpdateTime(new Date());
                abPoolCorp.setUpdateId(user.getId());
                this.updateEntity(abPoolCorp);
                if (rootMap.containsKey(abPoolCorpVO.getId())){
                    // 删除子单位节点
                    deleteSonPoolCorp(rootMap.get(abPoolCorpVO.getId()).getSonAbPoolCorpList(), user, rootMap);
                }
            }
        }
    }
    /**
     * 票据池单位禁用或启用
     * @param operate 操作
     * @param id 票据池单位id
     * @param user 当前用户
     * @return 处理结果
     *
     */
    public AjaxJson disableOrEnable(String operate, String id, ICurrentUser user){
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(operate)){
            AbPoolCorp abPoolCorp = this.get(id);
            abPoolCorp.setUpdateId(user.getId());
            abPoolCorp.setUpdateTime(new Date());
            abPoolCorp.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
            if(AbPoolConstant.AB_POLL_CORP_ENABLE.equals(operate)){
                // 启用
                abPoolCorp.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
            }else {
                //禁用检查是否在途
                abPoolCheckService.poolCorpCheckQuery(abPoolCorp.getCorpId());
            }
            this.updateEntity(abPoolCorp);
            reJson.setSuccess(true);
        }
        return reJson;
    }
    /**
     * 获得所有的票据池单位
     * @param id 票据池单位Id
     * @return 票据池单位
     *
     */
    @Transactional(readOnly = true)
    public Object listAllPoolCorpIds(String id){
        List<String> list = abPoolCorpDao.listAllPoolCorpIds(null);
        // 修改时去除自己的元素
        if(StringUtils.isNotEmpty(id)){
            list.remove(this.get(id).getCorpId());
        }
        return StringUtils.strip(list.toString(),"[]");
    }
    /**
     * 根据资源和用户查询票据池单位
     * @param resource 资源值
     * @param user 用户
     * @return 列表数据
     */
    @Transactional(readOnly = true)
    public List<String> getCorpListByPamas(String resource, ICurrentUser user){
        String corpAuthSql = null;
        // 添加资源控制单位
        if (StringUtils.isNotEmpty(resource)){
            List<String> corpIds = corpService.getUserCorpIds(resource, user.getId());
            corpAuthSql = SqlUtil.getInSql(corpIds, "p.corp_Id", true);
        }
        // 查询有资源且有效的票据池单位
        return abPoolCorpDao.getPoolCorpIdsByCorpId(corpAuthSql);
    }
}
