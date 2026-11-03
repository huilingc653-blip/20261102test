package com.bytter.system.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICellManageService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CellManageDto;
import com.bytter.system.api.dto.DictGroupDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.CellManageMessageEnum;
import com.bytter.system.dao.CellManageDao;
import com.bytter.system.entity.CellManage;
import com.bytter.system.vo.CellManageVO;
/**
 * @author wangzy
 * @Title: CellManageService
 * @Description: 管理单元Service
 * @date 2020/5/12 11:45
 */
@Service("cellManageService")
@DubboService
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class CellManageService extends BaseService<CellManage, String> implements ICellManageService {
    @Autowired
    private CellManageDao cellManageDao;
    @Autowired
    private IDictService dictServiceI;
    @Override
    public BaseDao<CellManage, String> getEntityDao() {
        return cellManageDao;
    }
    /**
     * 管理单元查询
     *
     * @param queryVO 查询条件VO
     * @return 列表数据
     */
    @Transactional(readOnly = true)
    public List<CellManageVO> listCellManageVOTree(CellManageVO queryVO) {
        // 查询数据
        List<CellManageVO> list = listCellManageVO(queryVO);
        // 将数据转成树结构
        List<CellManageVO> listTree = initCellManageVOTree(list, queryVO.getValidSign());
        return listTree;
    }
    /**
     * 查询管理单元信息
     *
     * @param id 管理单元ID
     * @return 管理单元VO
     * @throws
     */
    @Transactional(readOnly = true)
    public CellManageVO getCellManageVOInfo(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return cellManageDao.getCellManageVOInfo(id);
    }
    /**
     * 管理单元新增或修改
     *
     * @param infoVO 管理单元信息
     * @param user   用户
     * @return 处理结果
     * @throws
     */
    public AjaxJson saveOrUpdate(CellManageVO infoVO, ICurrentUser user) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        if (infoVO != null) {
            CellManage cellManage = null;
            // 如果有id 则为修改
            if (StringUtils.isNotBlank(infoVO.getId())) {
                // 检查修改的单元
                this.checkUpdateCell(infoVO);
                cellManage = this.get(infoVO.getId());
                cellManage.setUpdateId(user.getId());
                cellManage.setUpdateTime(new Date());
            } else {
                this.checkCellCode(infoVO.getCellCode());
                infoVO.setId(null);
                cellManage = new CellManage();
                cellManage.setCreateId(user.getId());
                cellManage.setCreateTime(new Date());
            }
            BeanUtils.copyNotNullProperties(infoVO, cellManage);
            this.saveOrUpdate(cellManage);
            reJson.setSuccess(true);
        }
        return reJson;
    }
    /**
     * 检查单元代码是否重复
     *
     * @param cellCode 单元代码
     * @return
     */
    public void checkCellCode(String cellCode) {
        String s = cellManageDao.checkCellCode(cellCode);
        if (StringUtils.isNotEmpty(s)) {
            throw new BusinessException(CellManageMessageEnum.SYS_CELL_CODE_NOT_REPEAT);
        }
    }
    /**
     * 对修改的单元检查
     * 修改的上级单元不能是自己的下级单元
     * 有下级单元的不能修改为无效，被单位使用的不能修改为无效
     *
     * @param infoVO 单元修改的信息
     * @return
     */
    public void checkUpdateCell(CellManageVO infoVO) {
        // 修改无效检查
        if (CommonConstant.VALID_SIGN_NO.equals(infoVO.getValidSign())) {
            CellManageVO cellManageVO = cellManageDao.checkCellManageVOInfo(infoVO.getId());
            // 单元代码不为空时，则表示有下级单位
            if (StringUtils.isNotEmpty(cellManageVO.getCellCode())) {
                throw new BusinessException(CellManageMessageEnum.SYS_CELL_SON_NOT_UPDATE);
            }
            // id不为空时则有单位使用了单元
            if (StringUtils.isNotEmpty(cellManageVO.getId())) {
                throw new BusinessException(CellManageMessageEnum.SYS_CELL_USED_NOT_UPDATE);
            }
        }
    }
    /**
     * 管理单元查询
     *
     * @param queryVO 查询条件VO
     * @return 列表数据
     */
    @Transactional(readOnly = true)
    public List<CellManageVO> listCellManageVO(CellManageVO queryVO) {
        if (StringUtils.isEmpty(queryVO.getValidSign())) {
            queryVO.setValidSign("1");
        }
        Map<String, Object> params = new HashMap<>();
        if (queryVO != null) {
            if (StringUtils.isNotEmpty(queryVO.getCellCodeAndName())) {
                params.put("cellCodeAndName", "%" + queryVO.getCellCodeAndName() + "%");
            }
            if (StringUtils.isNotBlank(queryVO.getValidSign())) {
                params.put("validSign", queryVO.getValidSign());
            }
        }
        //有效标志
        DictGroupDTO dictGroupIdForValidSign = this.dictServiceI.get(CommonConstant.COMMON_VALID_SIGN_DICT);
        params.put("dictGroupId", dictGroupIdForValidSign.getId());
        List<CellManageVO> list = cellManageDao.listCellManageVO(params);
        return list;
    }
    /**
     * 对树表结构进行转换
     *
     * @param list 管理单元LIST
     * @return
     */
    public List<CellManageVO> initCellManageVOTree(List<CellManageVO> list, String validSign) {
        List<CellManageVO> listTree = null;
        if (list != null && list.size() > 0) {
            listTree = new ArrayList<>();
            // 所有含有子单位的rootMap
            Map<String, CellManageVO> rootMap = new HashMap<>();
            initRootMap(list, rootMap, validSign);
            for (CellManageVO info : list) {
                if (StringUtils.isEmpty(info.getParentId()) || !rootMap.containsKey(info.getParentId())) {
                    listTree.add(info);
                    if (rootMap.containsKey(info.getId())) {
                        // 生成每个子单位下的单位
                        this.initAllSonCell(info.getSonCellManageList(), listTree, rootMap);
                    }
                }
            }
        }
        return listTree;
    }
    /**
     * 生成每个含有子节点的Map
     *
     * @param list    所有单位LIST
     * @param rootMap 根节点和含有子节点的Map
     * @return
     */
    public void initRootMap(List<CellManageVO> list, Map<String, CellManageVO> rootMap, String validSign) {
        // 查询所有ParentIds
        List<String> ids = cellManageDao.getAllCellParentIds(validSign);
        for (CellManageVO info : list) {
            if (StringUtils.isEmpty(info.getParentId()) || ids.contains(info.getId())) {
                rootMap.put(info.getId(), info);
            }
        }
        for (CellManageVO info : list) {
            if (rootMap.containsKey(info.getParentId())) {
                List<CellManageVO> sonList = rootMap.get(info.getParentId()).getSonCellManageList();
                if (sonList == null) {
                    sonList = new ArrayList<>();
                    rootMap.get(info.getParentId()).setSonCellManageList(sonList);
                }
                sonList.add(info);
            }
        }
    }
    /**
     * 管理单元子单元添加
     *
     * @param list     子单元List
     * @param listTree 单元树List
     * @param rootMap  含有子单元的Map
     * @return
     */
    public void initAllSonCell(List<CellManageVO> list, List<CellManageVO> listTree, Map<String, CellManageVO> rootMap) {
        if (list != null && list.size() > 0) {
            for (CellManageVO cellManageVO : list) {
                listTree.add(cellManageVO);
                CellManageVO ab = rootMap.get(cellManageVO.getId());
                if (ab != null) {
                    // 含有子单元时进行回调
                    this.initAllSonCell(ab.getSonCellManageList(), listTree, rootMap);
                }
            }
        }
    }
    /**
     * 下拉树管理单元查询
     *
     * @return 列表数据
     * @throws
     */
    @Transactional(readOnly = true)
    public Object getCellTreeMapList() {
        CellManageVO queryVO = new CellManageVO();
        queryVO.setValidSign(CommonConstant.VALID_SIGN_YES);
        //
        List<CellManageVO> listAll = this.listCellManageVO(queryVO);
        Map<String, CellManageVO> rootMap = new LinkedHashMap<>();
        initRootMap(listAll, rootMap, CommonConstant.VALID_SIGN_YES);
        List<Map<String, Object>> listTree = new ArrayList<>();
        for (CellManageVO info : listAll) {
            if (StringUtils.isEmpty(info.getParentId())) {
                Map<String, Object> mapInfo = new HashMap<>();
                mapInfo.put("id", info.getId());
                mapInfo.put("name", "(" + info.getCellCode() + ")" + info.getCellName());
                this.initSonCellVOTreeMap(rootMap.get(info.getId()).getSonCellManageList(), rootMap, mapInfo);
                listTree.add(mapInfo);
            }
        }
        return listTree;
    }
    /**
     * 单元下拉树结构进行转换
     *
     * @param
     * @return
     */
    public void initSonCellVOTreeMap(List<CellManageVO> sonList, Map<String, CellManageVO> rootMap, Map<String, Object> mapInfo) {
        if (sonList != null && sonList.size() > 0) {
            List<Map<String, Object>> children = new ArrayList<>();
            for (CellManageVO cellManageVO : sonList) {
                Map<String, Object> mapInfo2 = new HashMap<>();
                mapInfo2.put("id", cellManageVO.getId());
                mapInfo2.put("name", "(" + cellManageVO.getCellCode() + ")" + cellManageVO.getCellName());
                if (rootMap.containsKey(cellManageVO.getId())) {
                    this.initSonCellVOTreeMap(rootMap.get(cellManageVO.getId()).getSonCellManageList(), rootMap, mapInfo2);
                }
                children.add(mapInfo2);
            }
            mapInfo.put("children", children);
        }
    }
    /**
     * 管理单元导出数据处理
     *
     * @param queryVO 查询条件VO
     * @return 列表数据
     */
    @Transactional(readOnly = true)
    public List<CellManageVO> listExcelCellManageVOTree(CellManageVO queryVO) {
        List<CellManageVO> listTree = new ArrayList<>();
        // map用来存空格与parentId的对应关系
        Map<String, String> map = new HashMap();
        // 查询数据
        List<CellManageVO> list = this.listCellManageVOTree(queryVO);
        // 空格数，起始没空格
        String s = "";
        for (CellManageVO cellManageVO : list) {
            if (StringUtils.isEmpty(cellManageVO.getParentId()) || StringUtils.isEmpty(map.get(cellManageVO.getParentId()))) {
                s = "";
            }
            // map中有时，空格取map中的
            if (map.containsKey(cellManageVO.getParentId())) {
                s = map.get(cellManageVO.getParentId());
                s += "    ";
            }
            cellManageVO.setCellCodeAndName(s + cellManageVO.getCellCodeAndName());
            map.put(cellManageVO.getId(), s);
            listTree.add(cellManageVO);
        }
        return listTree;
    }
    /**
     * 根据管理单元id进行逻辑删除
     *
     * @param id   管理单元Id
     * @param user
     * @return
     */
    public AjaxJson deleteCell(String id, ICurrentUser user) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        if (StringUtils.isNotEmpty(id)) {
            CellManageVO cellManageVO = cellManageDao.checkCellManageVOInfo(id);
            // 单元代码不为空时，则表示有下级单位
            if (StringUtils.isNotEmpty(cellManageVO.getCellCode())) {
                throw new BusinessException(CellManageMessageEnum.SYS_CELL_SON_NOT_DELETE);
            }
            // id不为空时则有单位使用了单元
            if (StringUtils.isNotEmpty(cellManageVO.getId())) {
                throw new BusinessException(CellManageMessageEnum.SYS_CELL_USED_NOT_DELETE);
            }
            CellManage cellManage = this.get(id);
            cellManage.setValidSign(CommonConstant.VALID_SIGN_DELETE);
            cellManage.setUpdateId(user.getId());
            cellManage.setUpdateTime(new Date());
            reJson.setSuccess(true);
        }
        return reJson;
    }
    /**
     * 根据管理单元代码或者名称获取管理单元信息
     */
    public CellManage getCellManageByCodeOrName(String code, String name) {
        return cellManageDao.getCellManageByCodeOrName(code, name);
    }
    /**
     * 暴露接口的实现方法根据管理单元代码或者名称获取管理单元信息
     */
    @Override
    public CellManageDto getCellManageByCodeOrName1(String code, String name) {
        CellManage cellManageByCodeOrName = cellManageDao.getCellManageByCodeOrName(code, name);
        CellManageDto cellManageDto = new CellManageDto();
        if (!EmptyUtils.isEmpty(cellManageByCodeOrName)){
            BeanUtils.copyNotNullProperties(cellManageByCodeOrName,cellManageDto);
        }
        return cellManageDto;
    }
}
