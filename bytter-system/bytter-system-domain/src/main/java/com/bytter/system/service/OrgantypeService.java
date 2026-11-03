package com.bytter.system.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.SysOrgantypeConstant;
import com.bytter.system.api.enums.OrgantypeMessageEnum;
import com.bytter.system.dao.OrgantypeDao;
import com.bytter.system.entity.Organtype;
import com.bytter.system.entity.OrgantypeCorp;
import com.bytter.system.entity.OrgantypeCorpRelease;
import com.bytter.system.vo.CorpVO;
import com.bytter.system.vo.OrgantypeCorpVO;
import com.bytter.system.vo.OrgantypeReleaseVO;
import com.bytter.system.vo.OrgantypeVO;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Service
 * @Description: 组织架构表业务逻辑操作
 * @date 2020-05-14 16:01:40
 */
@Service("organtypeService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class OrgantypeService extends BaseService<Organtype, String> {
    @Autowired
    private OrgantypeDao organtypeDao;
    @Autowired
    //@Qualifier("corpService")
    private CorpService corpService;
    @Autowired
    //@Qualifier("organtypeCorpService")
    private OrgantypeCorpService organtypeCorpService;
    @Autowired
    //@Qualifier("organtypeReleaseService")
    private OrgantypeReleaseService organtypeReleaseService;
    @Autowired
    //@Qualifier("organtypeCorpReleaseService")
    private OrgantypeCorpReleaseService organtypeCorpReleaseService;
    /**
     * 组织架构表查询列表加载
     *
     * @param dataTablesInput
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String resource) {
        //获取该用户有权限的单位id集
        //List<String> corpAuthList = corpService.getUserCorpIds(resource,dataTablesInput.getCurrentUserId());
        DataTablesOutput data = organtypeDao.getDataTablesUnion(dataTablesInput);
        @SuppressWarnings("unchecked")
        List<OrgantypeVO> infoList = data.getData();
        for (OrgantypeVO info : infoList) {
            List<OrgantypeCorpVO> organtypeCorpVOS = organtypeCorpService.getOrgtCorpByOrgtId(info.getId());
            if (organtypeCorpVOS != null && !organtypeCorpVOS.isEmpty()) {
                info.setIsAddCorp(1);
            }
        }
        return data;
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(Organtype entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据ids删除
     *
     * @param ids
     */
    public boolean deletes(String ids, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idArray = ids.split(",");
            OrgantypeVO organtypeVO;
            for (String id : idArray) {
                organtypeVO = new OrgantypeVO();
                organtypeVO.setId(id);
                organtypeVO.setValidSign(SysOrgantypeConstant.SYS_ORGANTYPE_VALID_SIGN_2);
                saveOrUpdateOrgantype(organtypeVO, userVo);
            }
            return true;
        }
        return false;
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(Organtype entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(Organtype entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(Organtype entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<Organtype, String> getEntityDao() {
        return organtypeDao;
    }
    /**
     * 通VO保存
     *
     * @param organtypeVO
     */
    public boolean saveOrgantype(OrgantypeVO organtypeVO, ICurrentUser userVo) {
        organtypeVO.setId(null);
        return saveOrUpdateOrgantype(organtypeVO, userVo);
    }
    /**
     * 通VO更新
     *
     * @param organtypeVO
     */
    public boolean updateOrgantype(OrgantypeVO organtypeVO, ICurrentUser userVo) {
        return saveOrUpdateOrgantype(organtypeVO, userVo);
    }
    /**
     * 通VO保存or更新
     *
     * @param organtypeVO
     */
    public boolean saveOrUpdateOrgantype(OrgantypeVO organtypeVO, ICurrentUser userVo) {
        if (organtypeVO != null) {
            Organtype organtype = null;
            if (StringUtils.isNotEmpty(organtypeVO.getId())) {
                organtype = this.get(organtypeVO.getId(), true);
                if (userVo != null) {
                    organtype.setUpdateBy(userVo.getId());
                }
                organtype.setUpdateTime(new Date());
            } else {
                organtype = new Organtype();
                if (userVo != null) {
                    organtype.setCreateBy(userVo.getId());
                }
                organtype.setCreateTime(new Date());
            }
            BeanUtils.copyNotNullProperties(organtypeVO, organtype);
            saveOrUpdate(organtype);
            return true;
        }
        return false;
    }
    /**
     * 根据ID查询组织架构表VO
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public OrgantypeVO getOrgantypeVO(String id) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(id)) {
            params.put("id", id);
        } else {
            return null;
        }
        List<OrgantypeVO> list = organtypeDao.getOrgantypeByParams(params);
        if (list != null && !list.isEmpty()) {
            List<OrgantypeCorpVO> organtypeCorpVOS = organtypeCorpService.getOrgtCorpByOrgtId(list.get(0).getId());
            if (organtypeCorpVOS != null && !organtypeCorpVOS.isEmpty()) {
                list.get(0).setIsAddCorp(1);
            }
            return list.get(0);
        }
        return null;
    }
    /**
     * 判断是否有相同的组织架构名称
     *
     * @param orgtName 组织架构名称
     * @return
     */
    public boolean checkOrgtName(String orgtName) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(orgtName)) {
            params.put("orgtName", orgtName);
        } else {
            return false;
        }
        params.put("validSign", new String[]{SysOrgantypeConstant.SYS_ORGANTYPE_VALID_SIGN_0, SysOrgantypeConstant.SYS_ORGANTYPE_VALID_SIGN_1});
        List<OrgantypeVO> list = organtypeDao.getOrgantypeByParams(params);
        if (list != null && !list.isEmpty()) {
            return false;
        }
        return true;
    }
    /**
     * 根据id初始化单位
     * (把网点/单位维护的单位初始化到组织架构单位层级关联表)
     *
     * @param id
     */
    public boolean init(String id, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(id)) {
            //获取所有启用的菜单
            List<CorpVO> corpVOS = corpService.getAllCorpByValid();
            List<OrgantypeCorp> orgaCorplist;
            if (corpVOS != null && !corpVOS.isEmpty()) {
                orgaCorplist = new ArrayList<OrgantypeCorp>();
                for (CorpVO corpVO : corpVOS) {
                    OrgantypeCorp orgaCorp = new OrgantypeCorp();
                    orgaCorp.setCorpId(corpVO.getId());
                    orgaCorp.setParentId(corpVO.getParentId());
                    orgaCorp.setOrgtId(id);
                    if (userVo != null) {
                        orgaCorp.setCreateId(userVo.getId());
                    }
                    orgaCorp.setCreateTime(new Date());
                    orgaCorplist.add(orgaCorp);
                }
                organtypeCorpService.batchSave(orgaCorplist);
                //更新组织架构为已初始化
                OrgantypeVO organtypeVO = new OrgantypeVO();
                organtypeVO.setId(id);
                organtypeVO.setIsInit(SysOrgantypeConstant.SYS_ORGANTYPE_IS_INIT_1);
                organtypeVO.setIsChangCorp(SysOrgantypeConstant.SYS_ORGANTYPE_IS_CHANG_CORP_1);
                this.saveOrUpdateOrgantype(organtypeVO, userVo);
                return true;
            }
        }
        return false;
    }
    /**
     * 发布组织架构
     *
     * @param organtypeVO
     */
    public boolean release(OrgantypeVO organtypeVO, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(organtypeVO.getId())) {
            //获取组织架构
            OrgantypeVO orgtVO = this.getOrgantypeVO(organtypeVO.getId());
            if (orgtVO != null) {
                orgtVO.setEffectiveDate(organtypeVO.getEffectiveDate());
                orgtVO.setReleaseDate(new Date());
                //版本号
                if (StringUtils.isNotEmpty(orgtVO.getOrgtVersion())) {
                    Double version = Double.valueOf(orgtVO.getOrgtVersion().substring(1));
                    version = (double) Math.round((version + 0.1) * 100) / 100;
                    orgtVO.setOrgtVersion("V" + version);
                } else {
                    orgtVO.setOrgtVersion("V1.0");
                }
                //发版后把是否改变单位标识改为否
                orgtVO.setIsChangCorp(SysOrgantypeConstant.SYS_ORGANTYPE_IS_CHANG_CORP_0);
                //更新组织架构信息
                this.saveOrUpdateOrgantype(orgtVO, userVo);
                //发布归档表
                OrgantypeReleaseVO organtypeReleaseVO = new OrgantypeReleaseVO();
                organtypeReleaseVO.setOrgtId(orgtVO.getId());
                organtypeReleaseVO.setOrgtName(orgtVO.getOrgtName());
                organtypeReleaseVO.setEffectiveDate(orgtVO.getEffectiveDate());
                organtypeReleaseVO.setReleaseDate(orgtVO.getReleaseDate());
                organtypeReleaseVO.setOrgtVersion(orgtVO.getOrgtVersion());
                organtypeReleaseVO.setRmk(orgtVO.getRmk());
                organtypeReleaseService.saveOrUpdateOrgantypeRelease(organtypeReleaseVO, userVo);
                //发布组织架构单位层级关联归档表
                Map<String, Object> params = new HashMap<>();
                params.put("orgtId", orgtVO.getId());
                //根据组织架构id获取单位层级关联表
                List<OrgantypeCorpVO> organtypeCorpVOS = organtypeCorpService.getOrgtCorpByParams(params);
                if (organtypeCorpVOS != null && !organtypeCorpVOS.isEmpty()) {
                    List<OrgantypeCorpRelease> orgaCorpReleaseList = new ArrayList<OrgantypeCorpRelease>();
                    //组装组织架构单位层级关联归档数据
                    for (OrgantypeCorpVO OrgantCorpVO : organtypeCorpVOS) {
                        OrgantypeCorpRelease orgaCorpRelease = new OrgantypeCorpRelease();
                        orgaCorpRelease.setCorpId(OrgantCorpVO.getCorpId());
                        orgaCorpRelease.setParentId(OrgantCorpVO.getParentId());
                        orgaCorpRelease.setReleaseId(organtypeReleaseVO.getId());
                        if (userVo != null) {
                            orgaCorpRelease.setCreateId(userVo.getId());
                        }
                        orgaCorpRelease.setCreateTime(new Date());
                        orgaCorpReleaseList.add(orgaCorpRelease);
                    }
                    //保存组织架构单位层级关联归档数据
                    organtypeCorpReleaseService.batchSave(orgaCorpReleaseList);
                }
                return true;
            }
        }
        return false;
    }
    /**
     * 更新组织架构是否改变单位标识为1 是
     *
     * @param id
     * @return
     */
    public int updateOrgantypeIsChangCorpById(String id, ICurrentUser userVo) {
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(OrgantypeMessageEnum.SYS_ORGANTYPE_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("isChangCorp", SysOrgantypeConstant.SYS_ORGANTYPE_IS_CHANG_CORP_1);
        if (userVo != null) {
            params.put("updateBy", userVo.getLoginName());
        }
        params.put("updateTime", new Date());
        return organtypeDao.updateOrgantype(params);
    }
    /**
     * 根据组织架构Id查询组织架构单位层级关联corpId字符串
     *
     * @param orgtId 组织架构Id
     * @return
     */
    public String getOrgtCorpByOrgtId(String orgtId) {
        StringBuilder corpIds = new StringBuilder();
        List<OrgantypeCorpVO> organtypeCorpVOS = organtypeCorpService.getOrgtCorpByOrgtId(orgtId);
        if (organtypeCorpVOS != null && !organtypeCorpVOS.isEmpty()) {
            for (OrgantypeCorpVO corpVO : organtypeCorpVOS) {
                if (corpIds.length() > 0) {
                    corpIds.append(",");
                }
                corpIds.append(corpVO.getCorpId());
            }
        }
        return corpIds.toString();
    }
    /**
     * 根据ID查询组织架构表已发版的数据
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<OrgantypeVO> getOrgantypeIsRelease(String id) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(id)) {
            params.put("id", id);
        }
        List<OrgantypeVO> list = organtypeDao.getOrgantypeIsRelease(params);
        return list;
    }
}
