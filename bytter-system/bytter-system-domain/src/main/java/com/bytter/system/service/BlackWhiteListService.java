package com.bytter.system.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.constant.SysBlackWhiteConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import com.bytter.system.api.enums.SysBlackWhiteEnum;
import com.bytter.system.dao.BlackWhiteListDao;
import com.bytter.system.entity.BlackWhiteList;
import com.bytter.system.vo.BlackWhiteListVO;
import com.bytter.system.vo.DictVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import javax.annotation.PostConstruct;
import java.util.*;
@Service("blackWhiteListService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class BlackWhiteListService extends BaseService<BlackWhiteList, String>
        implements IUserExcelVerifyHandler<BlackWhiteListVO> {
    @Autowired
    public BlackWhiteListDao blackWhiteListDao;
    @Autowired
    public ICorpService corpServiceI;
    @Autowired
    public ISysCountryCustService sysCountryCustService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DictService dictService;
    /**
     * 通过map参数获黑白名单列表数据
     *
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    public List<BlackWhiteList> getBlackWhiteListByParams(Map<String, Object> params) {
        return blackWhiteListDao.getBlackWhiteListByParams(params);
    }
    /**
     * 根据外部ID删除黑白名单信息（逻辑删除validSign=-2）
     *
     * @param externalId
     */
    public void deleteBlackWhiteListByExternalId(String externalId, ICurrentUser user) {
        blackWhiteListDao.deleteBlackWhiteListByExternalId(externalId, user);
    }
    /**
     * 通过名称获取黑白名单信息
     *
     * @param name     黑白名单客商名称
     * @param listType 客商承兑行类型
     * @return
     */
    @Transactional(readOnly = true)
    //@Cacheable(value = SysBlackWhiteConstant.CACHE_NAME_CHECK, key = "#name")
    public List<BlackWhiteList> getBlackWhiteListByParams(String name, String listType) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(name)) {
            params.put("name", name);
        } else {
            return null;
        }
        if (StringUtils.isNotEmpty(listType)) {
            params.put("listType", listType);
        }
        return getBlackWhiteListByParams(params);
    }
    @Transactional(readOnly = true)
    public DataTablesOutput<BlackWhiteListVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        DataTablesOutput output = blackWhiteListDao.listObject(dataTablesInput);
        List<BlackWhiteListVO> voList=output.getData();
        for(BlackWhiteListVO vo:voList) {
            initVo(vo);
        }
        return output;
    }
    /**
     * 加载单位信息
     *
     * @param vo
     * @return
     */
    public BlackWhiteListVO initVo(BlackWhiteListVO vo) {
        if (!EmptyUtils.isEmpty(vo)) {
            String listTypeName = dictService.getDictByDictGroupAndCode("SYS_BM_LIST_TYPE", vo.getListType()).getName();
            vo.setListTypeName(listTypeName);
            String bwTypeName = dictService.getDictByDictGroupAndCode("SYS_BUS_ENTITY_TYPE", vo.getBwType()).getName();
            vo.setBwTypeName(bwTypeName);
            if (StringUtils.isNotBlank(vo.getIsCommon())) {
                DictVO isCommonDict = dictService.getDictByDictGroupAndCode("sf_yn", vo.getIsCommon());
                vo.setIsCommonName(isCommonDict == null ? null : isCommonDict.getName());
            }
            String validSignName = dictService.getDictByDictGroupAndCode("sf_yn", vo.getValidSign()).getName();
            vo.setValidSignName(validSignName);
            String dataSourcesName = dictService.getDictByDictGroupAndCode("SYS_BM_LIST_DATA_SOURCES", vo.getDataSources()).getName();
            vo.setDataSourcesName(dataSourcesName);
        }
        return vo;
    }
    /**
     * 详情
     *
     * @param model
     */
    public void getInfoVo(String id, Model model) {
        BlackWhiteListVO entityVo = blackWhiteListDao.getBlackWhiteListVO(id);
        initVo(entityVo);
        model.addAttribute("blackWhite", entityVo);
        model.addAttribute("intercept", entityVo.getBwType());
    }

    public BlackWhiteListVO getInfoVoV1(String id) {
        BlackWhiteListVO entityVo = blackWhiteListDao.getBlackWhiteListVO(id);
        initVo(entityVo);
        return   entityVo;
    }
    /**
     * 删除，新增，修改
     *
     * @param operator
     * @param blackWhiteListVO
     * @param user
     * @return
     */
    //@CacheEvict(value = SysBlackWhiteConstant.CACHE_NAME_CHECK)
    public AjaxJson saveOrUpdate(String operator, BlackWhiteListVO blackWhiteListVO, ICurrentUser user) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        BlackWhiteList blackWhiteList = new BlackWhiteList();
        BeanUtils.copyNotNullProperties(blackWhiteListVO, blackWhiteList);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", blackWhiteListVO.getName());
        if (!StringUtils.isBlank(blackWhiteListVO.getId())) {
            params.put("id", blackWhiteListVO.getId());
        }
        //重复校验
        if (blackWhiteListDao.repeatFegin(params)) {
            throw new BusinessException(SysBlackWhiteEnum.SYS_BLACK_WHITE_REPEAT);
        }
        // 新增或修改
        if (AccebillConstant.BILL_OPERATE_ADD.equals(operator)
                && StringUtils.isBlank(blackWhiteListVO.getId())) {
            blackWhiteList.setCreateDate(new Date());
            blackWhiteList.setCreateId(user.getId());
            if(StringUtils.isBlank(blackWhiteListVO.getCorpId())){
                blackWhiteList.setCorpId(null);
            }
            blackWhiteListDao.save(blackWhiteList);
        } else if (AccebillConstant.BILL_OPERATE_UPDATE.equals(operator)
                && !StringUtils.isBlank(blackWhiteListVO.getId())) {
            BlackWhiteList hEntity = blackWhiteListDao.get(blackWhiteList.getId(), true);
            BeanUtils.copyNotNullProperties(blackWhiteList, hEntity);
            hEntity.setUpdateDate(new Date());
            hEntity.setUpdateId(user.getId());
            if(StringUtils.isBlank(hEntity.getCorpId())){
                hEntity.setCorpId(null);
            }
            blackWhiteListDao.saveOrUpdate(hEntity);
        }
        reJson.setSuccess(true);
        return reJson;
    }

    @CacheEvict(value = SysBlackWhiteConstant.CACHE_NAME_CHECK, key = "#ids")
    public AjaxJson batchdata(String ids, ICurrentUser user) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        if (StringUtils.isNotEmpty(ids)) {
            String[] idArray = ids.split(",");
            if (blackWhiteListDao.blackWithDel(Arrays.asList(idArray)) > 0) {
                reJson.setSuccess(true);
            }
        }
        return reJson;
    }
    /**
     * 导入申请
     *
     * @param list
     * @return 处理结果
     */
    public boolean saveImport(List<BlackWhiteListVO> list, ICurrentUser userVo, String intercept) {
        for (int i = 0; i < list.size(); i++) {
            BlackWhiteListVO blackWhiteListVO = list.get(i);
            BlackWhiteList entity = new BlackWhiteList();
            BeanUtils.copyProperties(blackWhiteListVO, entity);
            entity.setValidSign(SysBlackWhiteConstant.SYS_BLACK_WHITE_VALID_SIGN_1);
            entity.setDataSources(SysBlackWhiteConstant.SYS_BLACK_WHITE_DATASOURCES_02);
            entity.setCreateDate(new Date());
            entity.setCreateId(userVo.getId());
            blackWhiteListDao.save(entity);
        }
        return true;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(BlackWhiteListVO obj,
                                                  Object... user) {
        return null;
    }
    @Override
    public void verifyHandler(List<BlackWhiteListVO> list, Object... user) {
        if (ListUtils.isNotNullOrEmpty(list)) {
            for (BlackWhiteListVO info : list) {
                //数据来源：02手工
                info.setDataSources(SysBlackWhiteConstant.SYS_BLACK_WHITE_DATASOURCES_02);
                List<String> errorMsgList = new ArrayList<String>();
                //单位
                if (StringUtils.isNotBlank(info.getCorpCode())) {
                    CorpDTO corpDTO = corpServiceI.getCorpByCode(info.getCorpCode());
                    if (corpDTO == null) {
                        errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_CORP_TIPS);// 导入的单位不存在系统
                    } else {
                        info.setCorpCodeName(corpDTO.getName());
                        info.setCorpId(corpDTO.getId());// 所属单位
                    }
                }
                //类型 黑名单/白名单
                if (StringUtils.isNotBlank(info.getBwTypeName())) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> params = (Map<String, Object>) user[1];
                    String intercept = ((String[]) params.get("intercept"))[0];//数据来源
                    String bwType = null;
                    if (info.getBwTypeName().equals(SysBlackWhiteConstant.SYS_BLACK_NAME)) {
                        bwType = SysBlackWhiteConstant.EXTERNALCORP_BWTYPE_01;
                    } else if (info.getBwTypeName().equals(SysBlackWhiteConstant.SYS_WHITE_NAME)) {
                        bwType = SysBlackWhiteConstant.EXTERNALCORP_BWTYPE_02;
                    } else {
                        errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_BWTYPE_NONENTITY_TIPS);
                    }
                    if (StringUtils.isNotBlank(bwType)) {
                        if (bwType.equals(intercept)) {
                            info.setBwType(bwType);
                        } else {
                            // 根据当前维护页面允许的类型提示，而不是导入数据的类型。
                            String tipsCreat = SysBlackWhiteConstant.EXTERNALCORP_BWTYPE_01.equals(intercept)
                                    ? SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_BWTYPE_BLACK_TIPS : SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_BWTYPE_WHITE_TIPS;
                            errorMsgList.add(tipsCreat);
                        }
                    }
                } else {
                    errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_BWTYPE_EMPTY_TIPS);
                }
                //类别 客商，承兑人
                if (StringUtils.isNotBlank(info.getListTypeName())) {
                    if (info.getListTypeName().equals(SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPENAME01)) {
                        info.setListType(SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPE_01);
                    } else if (info.getListTypeName().equals(SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPENAME02)) {
                        info.setListType(SysBlackWhiteConstant.SYS_BLACK_WHITE_LISTTYPE_02);
                    } else {
                        errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_LISTTYPE_NONENTITY_TIPS);
                    }
                } else {
                    errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_LISTTYPE_EMPTY_TIPS);
                }
                //名称非空
                if (StringUtils.isBlank(info.getName())) {
                    errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_NAME_EMPTY_TIPS);
                } else {
                    //重复
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("name", info.getName());
                    //重复校验
                    if (blackWhiteListDao.repeatFegin(params)) {
                        errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_REPEAT);
                    }
                }
                //国家地区
                if (StringUtils.isNotBlank(info.getCountryName())) {
                    SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getByNameZh(info.getCountryName());
                    if (null != sysCountryCustDTO) {
                        info.setCountryId(sysCountryCustDTO.getId());
                    } else {
                        errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_COUNTRY_NONENTITY_TIPS);
                    }
                } else {
                    errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_COUNTRY_EMPTY_TIPS);
                }
                //是否共享
                if (StringUtils.isNotBlank(info.getIsCommonName())) {
                    if (info.getIsCommonName().equals("是")) {
                        info.setIsCommon(SysBlackWhiteConstant.SYS_BLACK_WHITE_ISCONTROL_Y);
                    } else if (info.getIsCommonName().equals("否")) {
                        info.setIsCommon(SysBlackWhiteConstant.SYS_BLACK_WHITE_ISCONTROL_N);
                    } else {
                        errorMsgList.add(SysBlackWhiteConstant.SYS_BLACK_WHITE_IMPORT_ISCOMMON_NONENTITY_TIPS);
                    }
                }
                //校验数据
                info.addErrorMsg(errorMsgList);
            }
        }
    }
    @Override
    public BaseDao<BlackWhiteList, String> getEntityDao() {
        return blackWhiteListDao;
    }
    @PostConstruct
    public void tst() {
        Map<String, ICorpService> beansOfType = applicationContext.getBeansOfType(ICorpService.class);
        logger.warn("单位bean长度" + beansOfType.size());
    }
}
