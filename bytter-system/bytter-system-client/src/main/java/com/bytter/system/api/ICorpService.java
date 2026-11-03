package com.bytter.system.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.bean.FexCorpExposureVO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/5/23.
 */
public interface ICorpService {
    /**
     * 通过ID查找网点、单位
     *
     * @param id
     * @return
     */
    CorpDTO get(String id);
    CorpDTO getParentById(String id);
    /**
     * 仅获取所有网点
     *
     * @return
     */
    List<CorpDTO> listNet();

    /**
     * 功能介绍 : 获取当前登陆用户的网点ID。<br>
     */
    CorpDTO getUserNet(ICurrentUser user);

    /**
     * 根据网点ID，获取该网点下所有的单位(跨网点获取)
     *
     * @param netId
     * @return
     */
    List<CorpDTO> listCorpByNetId(String netId);
    /**
     * 根据网点ID，获取该网点下所有的单位(跨网点获取)
     *
     * @param netId
     * @return
     */
    List<CorpDTO> listCorpBasicInfoByNetId(String netId);
    /**
     * 根据网点ID，获取该网点下直属单位和直属网点(不跨网点获取)
     * update zuoy 从只获取直属单位改为获取直属单位和直属网点 20200107
     *
     * @param netId
     * @return
     */
    List<CorpDTO> listUnderCorpByNetId(String netId);
    /**
     * 根据父单位ID，获取该单位下直属的单位(不跨单位获取)
     *
     * @param corpId 父单位ID
     * @return
     */
    List<CorpDTO> listUnderCorpByParentCorpId(String corpId);
    /**
     * 根据父单位ID，获取该单位下直属的单位(不跨单位获取)
     * @param corpId 父单位ID
     * @param equityMaintenanceList 股权属性
     * @return
     */
    List<CorpDTO> listCorpByParentId(String corpId,List<String> equityMaintenanceList);
    /**
     * 根据父单位ID，获取该单位下直属的单位(跨级)
     *
     * @param corpId 父单位ID
     * @return
     */
    List<CorpDTO> listUnderCorpByParentCorpIdSkipLevel(String corpId);
    /**
     * 根据单位ID，获取该单位的直属网点
     *
     * @param corpId
     * @return
     */
    CorpDTO getNetByCorpId(String corpId);
    List<CorpDTO> listEffectiveCorp();
    List<CorpDTO> listEffectiveCorpBasicInfo();
    /**
     * 单位集合树形拼装
     *
     * @param corpDTOList
     * @return
     */
    List<CorpDTO> listCorpTree(List<CorpDTO> corpDTOList);
    /**
     * 获取 指定层级 用户对应资源的单位，及其下属所有子单位
     *
     * @param resource 资源权限
     * @param level    大于0的整数，单位层级：比如2就是二级成员单位
     * @return 单位
     */
    List<CorpDTO> listLevelCorpAndChildrenCorp(String resource, String userId, int level);
    /**
     * 获取当前用户对应资源的单位或网点id
     *
     * @param resource 资源
     * @param userId   用户ID
     * @return
     */
    List<String> getUserCorpIds(String resource, String userId);
    List<String> getCorpIdsByUserResource(String resource, String userId);
    /**
     * @param resource
     * @param type
     * @return
     * @see #getUserNetIds
     */
    List<String> getUserNetIds(String resource, Integer type, String userId);
    /**
     * @param resource
     * @return
     * @see #listUserCorp
     */
    List<CorpDTO> listUserCorp(String resource, String userId);
    /**
     * @param resource
     * @return
     * @see #listUserCorp
     */
    List<CorpDTO> listUserResourceCorps(String resource, String userId);
    /**
     * 获取当前用户对应资源的单位或网点的SQL语句
     *
     * @return
     */
    String getCurrentUserCorpSQL(String resource);
    String getCurrentUserCorpSQL(String resource, String filed);
    /**
     * 获取当前用户对应资源的单位或网点的SQL语句，传入占位符
     *
     * @param placeholder hibernate查询中map的占位符
     * @return
     */
    String getCurrentUserCorpSQLByPlaceholder(String placeholder);
    String getCurrentUserCorpSQLByPlaceholder(String placeholder, String filed);
    /**
     * 根据单位名称查找单位
     *
     * @param name
     * @return
     */
    CorpDTO getCorpIdByName(String name);
    /**
     * 根据单位代码查找单位信息
     *
     * @param code
     * @return
     */
    CorpDTO getCorpByCode(String code);
    /**
     * 根据父单位ID，获取该单位下受控制的直属的单位(不跨单位获取)
     *
     * @param corpId 父单位ID
     * @return
     */
    List<CorpDTO> listUnderCorpByParentCorpIdCtl(String corpId);
    /**
     * 功能介绍 : 获取授信主体的指定单位资源权限。<br>
     * <b>日期：</b>2017年11月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    List<CorpDTO> getRatGroupCorpNet(String resource,String userId);
    /**
     * 判断两个单位是否所属同一网点下
     *
     * @param corpId1
     * @param corpId2
     * @return
     */
    boolean isTheSameNetByCorpId(String corpId1, String corpId2);
    /**
     * 根据单位ID集合查询数据
     *
     * @param corpIds
     * @return
     * @author zuoy 2018-05-26
     */
    List<CorpDTO> listCorpDTOByCorpIds2(List<String> corpIds);
    /**
     * 获取企业授信的二次授信对象
     *
     * @author luxy
     */
    List<CorpDTO> getCpRationGroupCorpNet(String resource, String netId, String userId);
    /**
     * 新增单位信息
     */
    void save(List<CorpDTO> corps);
    /**
     * 编辑单位信息
     */
    void update(List<CorpDTO> corps);
    /**
     * 删除用户
     */
    void delete(List<CorpDTO> corpCodes);
    /**
     * 获取最顶级网点/单位
     *
     * @param corpId
     * @return
     */
    CorpDTO getTopCorpByCorpId(String corpId);

    /**
     * 获取上级网点/单位
     *
     * @param corpId
     * @return
     */
    CorpDTO getSecoCorpByCorpId(String corpId);
    /**
     * 根据单位ID集合查询数据
     *
     * @param corpIds
     * @return
     * @author zuoy 2018-05-26
     */
    List<CorpDTO> listTreeCorpDTOByCorpIds(List<String> corpIds);
    /**
     * 根据单位ID集合查询数据
     *
     * @param corpIds
     * @return
     * @author zuoy 2018-05-26
     */
    List<CorpDTO> listTreeCorpDTOByCorpIdsV2(List<String> corpIds);
    /**
     * 获取资源下的单位ID（添加所用账号单位）
     *
     * @param resource 资源
     * @param userId   用户id
     * @return
     * @author zhenghj 2022-5-31
     */
    List<String> getUserCorpIdsByUseAccount(String resource, String userId);
    /**
     * 根据单位id获取单位信息，key=id value=单位信息
     *
     * @param corpIds 单位id集合
     * @return 单位信息
     */
    Map<String, CorpDTO> listCorpDtoByCorpIds(List<String> corpIds);
    public Map<String, CorpDTO> listCorpCodeByCorpIds(List<String> corpIds);

    Map<String, CorpDTO> listCorpNameByCorpIds(List<String> corpIds);
    /**
     * 根据单位id获取单位信息，key=id value=单位信息
     *
     * @return 单位信息
     */
    Map<String, CorpDTO> getCorpMap();
    /**
     * 根据单位ID集合查询数据
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
    DataTablesOutput getDataTablesCorpByCorpIds(DataTablesInput dataTablesInput, List<String> corpIds);
    /**
     * 根据条件查询没有删除且是有效的银行账户
     * @param dataTablesInput
     * @param bankacc
     * @return
     */
    DataTablesOutput getNotDeleteAndValidAndBankAcc(DataTablesInput dataTablesInput, List<String> bankacc);
    /**
     * 根据内部账号和币别查询状态正常的内部账号数据
     * @param dataTablesInput
     * @param acc
     * @return
     */
    DataTablesOutput<FcAccDTO> getblnctrAccByAccAndcurId(DataTablesInput dataTablesInput, List<String> acc);
    /**
     * 还原所有单位集团统一授信主体标识。
     */
    void resetRatGroup();
    /**
     * 还原所有单位集团统一授信主体标识
     *
     * @param corpIds 单位id集合
     */
    void updateRatGroup(List<String> corpIds);
    /**
     * 获取有效单位Id集合
     * @return
     */
    List<String> getCorpIds();
    /**
     * 根据查询条件获取有效的单位Id集合
     * @param seach 单位名称 or 单位编码
     * @return
     */
    List<String> listCorpIdsBySeach(String seach);
    /**
     * 根据查询条件获取有效的单位信息集合
     * @param seach 单位名称 or 单位编码
     * @return
     */
    List<CorpDTO> listCorpBySeach(String seach);
    /**
     * 根据网点Id获取单位信息集合
     * @param netId
     * @return
     */
    List<CorpDTO> getCorpByNetId(String netId);
    /**
     * 根据Id集合获取单位信息（包含下级单位数量）
     * @param corpIds 单位Id
     * @return
     */
    List<CorpDTO> getCorpByIdsHadChildCount(List<String> corpIds);
    /**
     * 根据上级单位Id获取子单位信息
     * @param parentId 上级单位Id
     * @return
     */
    List<CorpDTO> getCorpByParentId(String parentId);
    /**
     * 根据上级单位Id获取子单位信息
     * @param parentIds 上级单位Ids
     * @return
     */
    List<CorpDTO> getCorpByParentIds(List<String> parentIds);
    /**
     * 根据ids，查询 corpName, 返回 k=id , v=name
     * @param corpIds
     * @return
     */
    Map<String, String> getNameByIds(List<String> corpIds);

    Map<String, String> getNameByNetIds(List<String> netIds);

    /**
     * 根据ids 和 type ，查询 sys_corp 表；corpName, 返回 k=id , v=name
     * @param corpIds
     * @param type
     * @return
     */
    Map<String, String> getNameByTypeAndIds(List<String> corpIds, Integer type);
    /**
     * 根据 parentId 查询corpId集合
     * @param parentId
     * @return
     */
    List<String> getCorpIdByParentId(String parentId);
    /**
     * 根据单位ID，查询返回单位名称
     * @param corpId
     * @return
     */
    String getCorpName(String corpId);
    String getCorpCode(String corpId);
    /**
     * 单位敞口设置集合,从FexCorpExposureDao迁移过来，查询主表是sys_corp
     * @param orgType
     * @param search
     * @return
     */
    List<FexCorpExposureVO> listFbsCorpExposure(String orgType, String search);
    /**
     * 获取单位列表信息,从FexCorpExposureDao迁移过来，查询主表是sys_corp
     * @param orgType
     * @param search
     * @return
     */
    public List<String> listCorpIdsBySeach(String orgType, String search);
    /**
     * 根据单位ID获取上级单位敞口信息,从FexCorpExposureDao迁移过来，查询主表是sys_corp
     * @param corpId
     * @return
     */
    FexCorpExposureVO getParentCorpExposureByCorpId(String corpId);
    /**
     * 根据上级单位ID获取下级单位的集合,从FexCorpExposureDao迁移过来，查询主表是sys_corp
     * @param parentId
     * @return
     */
    List<FexCorpExposureVO> listCorpExposureByParentId(String parentId);
    /**
     * 根据单位ID获取上级单位敞口信息,从FexCorpExposureDao迁移过来，查询主表是sys_corp
     * @param corpId
     * @return
     */
    FexCorpExposureVO getCorpExposureByCorpId(String corpId);
    /**
     * 根据 corpIds 查询 sys_corp 返回集合，不需要排序
     * @param corpIds
     * @return
     */
    List<CorpDTO> listCorpDTOByCorpIds(List<String> corpIds);
    /**
     * 获取有效的策略关联数据。
     *
     * @param dataValList 策略值列表。
     * @return 级联数据列表。
     */
    List<Map<String,Object>> listStrategyDataByDataVal(List<String> dataValList);
    /**
     * 根据corpId获取单位信息（code,name）和网点信息（code,name）。
     *
     * @param corpIdList 单位编号集合。
     * @return 单位和网点信息（code,name）。
     */
    List<Map<String, Object>> getCorpAndNetInfoByCorpId(List<String> corpIdList);

    /**
     * 根据corpId获取单位和网点信息（code,name）
     *
     * @param corpIdList 单位编号集合。
     * @return 单位信息（code,name）。
     */
    Map<String, CorpDTO> getCorpMapByCorpId(List<String> corpIdList);

    /**
     * 根据网点id获取单位信息
     * @param netIds 网点id集合
     * @return 单位集合信息
     */
    List<CorpDTO> listCorpDTOByNetIds(List<String> netIds);
    /**
     * @description  根据银行账号获取单位ID与组织机构代码
     * @param bankAcc
     * @return
     * @author  15706
     * @date    2023/7/6-16:55
     * @version 1.
     * */
    List<Map<String, Object>> queryVAccListVo(String bankAcc);
    /**
     * @description 根据单位类型获取单位ID列表
     * @param type
     * @return
     */
    List<String> getCorpIdsByType(String type);
    /**
     * 根据单位id获取单位信息，key=id value=单位信息
     *
     * @return 单位信息
     */
    Map<String, CorpDTO> getCorpNetMap();
    /**
     * 查看所有大区
     */
    List<CorpDTO> getRegion(String id);

    CorpDTO getRegionByCorpId(String corpId,String regionId);

    List<CorpDTO> getFilterBySource(String corpVOList, List<String> sourceCorpIds);

    Map<String, CorpDTO> getCorpListByNames();

    Map<String, CorpDTO> getCorpListByNamesAll();

    /**
     * 根据单位仓库编码获取仓库名
     *
     * @return 单位信息
     */
    String getUnitStoreMaintenanceNameByCode(String code);

    /**
     * 根据单位code集合获取单位信息
     * @param codes
     * @return TODO
     */
    List<CorpDTO> listByCodes(List<String> codes);

    /**
     * 根据传入的id或者ids获取所有子级单位信息
     * @param ids
     * @param includeThisCorp 是否包含当前单位，默认为true
     * @return TODO
     */
    List<CorpDTO> listSubCorp(List<String> ids,boolean includeThisCorp);
    /**
     * 根据传入的id或者ids获取所有父级单位信息
     * @param ids
     * @param includeThisCorp 是否包含当前单位，默认为true
     * @return TODO
     */
    List<CorpDTO> listParentCorp(List<String> ids,boolean includeThisCorp);

    /**
     * 单位组件-
     * 获取过滤权限资源的结构 数量
     *
     * @param resource 资源权限
     * @param currentUser     用户
     * @return 集合数据
     */
    public String getFilterCorpW(String resource, ICurrentUser currentUser);
    /**
     * corpIds
     * @param corpIds
     * @return
     */
    List<CorpDTO> listCorpDTOByCorpIdsForOrgant(List<String> corpIds,String orgtId);

    String getEffectiveOrgIdByModuld(String moduleCode);

}
