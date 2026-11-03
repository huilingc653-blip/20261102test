package com.bytter.system.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.basic.entity.BtCurrency;
import com.bytter.basic.service.BtCurrencyService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.BaseCodeEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.CorpMessageEnum;
import com.bytter.system.api.vo.ErpBasicDataVOVO;
import com.bytter.system.dao.CellManageDao;
import com.bytter.system.dao.CorpDao;
import com.bytter.system.dao.SysTenantManagerDao;
import com.bytter.system.dao.UserDao;
import com.bytter.system.entity.*;
import com.bytter.system.producer.InitNetDayCloseMQProducer;
import com.bytter.system.vo.CorpAndBtBankAccVO;
import com.bytter.system.vo.CorpComponentVO;
import com.bytter.system.vo.CorpSimpleVO;
import com.bytter.system.vo.CorpVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.bytter.system.api.constant.CorpConstant.CORP_TYPE_CODE_CORP;
import static com.bytter.system.api.constant.CorpConstant.CORP_TYPE_CODE_NET;

/**
 * 基础平台单位service
 * Created by zhoul on 2017/4/12.
 */
@Service("corpService")
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CorpService extends BaseService<Corp, String> implements IExcelVerifyHandler<CorpVO> {

    @Autowired
    private OrgantypeModuleService organtypeModuleService;
    @Autowired
    private OrgantypeReleaseService organtypeReleaseService;
    @Autowired
    private CorpDao corpDao;
    // @Autowired
    // private IFcDayCloseService fcDayCloseServiceImpl;
    @Autowired
    private CorpCmMapService corpCmMapService;
    @Autowired
    private ILicenseService licenseServiceImpl;
    @Autowired
    private IRegionService regionServiceI;
    @Autowired
    private CorpUserResourcesService corpUserResourcesService;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    private Ii18nService i18nServiceI;
    @Autowired
    private IDictService dictServiceI;
    @Autowired
    private CellManageService cellManageService;
    @Autowired(required = false)
    private List<ICorpDeleteVerifyService> corpDeleteVerifyServiceList;
    @Autowired(required = false)
    private List<ICorpDisableVerifyService> corpDisableVerifyServiceList;
    @Autowired(required = false)
    private List<ICorpParentChangVerifyService> corpParentChangVerifyServiceList;
    @Autowired
    private ISysCountryCustService sysCountryCustService;
    @Autowired
    //@Qualifier("resourcesService")
    private ResourcesService resourcesService;
    @Autowired
    private InitNetDayCloseMQProducer initNetDayCloseMQProducer;
//    @Autowired
    //@Qualifier("userService")
//    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Resource
    private RedisUtils redisUtils;

    @Autowired
    private BtCurrencyService btCurrencyServiceBase;

    @Autowired
    private CellManageDao cellManageDao;

    @Autowired
    private SysTenantManagerService sysTenantManagerService;

    @Autowired
    private SysTenantManagerDao sysTenantManagerDao;

    public List<String> listUserAdminByCorpIds(List<String> corpIds) {
        return this.userDao.listUserAdminByCorpIds(corpIds);
    }
    @Override
    public BaseDao<Corp, String> getEntityDao() {
        return corpDao;
    }
    @Override
    public void saveEntity(Corp corp) {
        super.saveEntity(corp);
    }

    public Corp getCropById(String id) {
        return corpDao.getCropById(id);
    }

    /**
     * 根据id和租户ID获取单位。
     *
     * @param id 单位ID。
     * @param tenantId 租户ID。
     * @return 单位。
     */
    public Corp getCropById(String id, String tenantId) {
        return corpDao.getCropById(id, tenantId);
    }

    /**
     * 检测许可控制的数量
     *
     * @param corp
     */
    private void licenseNetOrCorpControl(Corp corp) {
        if (CorpConstant.CORP_STATUS_DISABLE.equals(corp.getStatus())) {
            return;
        }
        if (CORP_TYPE_CODE_NET.equals(corp.getType())) {
            Integer netNumber = licenseServiceImpl.getNetNumber();
            if (netNumber > 0 && corpDao.countCorpByType(CORP_TYPE_CODE_NET) > netNumber) {
                throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_NET_NUMBER_MORE_THAN_LICENSE_NUMBER, netNumber.toString());
            }
        }
        if (CORP_TYPE_CODE_CORP.equals(corp.getType())) {
            Integer corpNumber = licenseServiceImpl.getCorpNumber() == null ? 0:licenseServiceImpl.getCorpNumber();
            if (corpNumber > 0 && corpDao.countCorpByType(CORP_TYPE_CODE_CORP) > corpNumber) {
                throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_CORP_NUMBER_MORE_THAN_LICENSE_NUMBER, corpNumber.toString());
            }
        }
    }
    private void getLockAndCheck(String userId,Integer addNum , boolean isImport) {
        if(addNum < 1){
            throw new BusinessException("添加数据为空，请确认");
        }
        User user = userDao.get(userId);
        sysTenantManagerDao.updateByTenantId(user.getTenantId());
        SysTenantManager byTenantCode = sysTenantManagerService.getByTenantCode(user.getTenantId());
        if(byTenantCode == null){
            logger.error(String.format("通过租户id%s查询租户信息错误",user.getTenantId()));
            throw new BusinessException("租户不存在，请确认");
        }

        long existCorpCount = corpDao.countCorpByTenantId(user.getTenantId());
        //导入时必须hibernate保存一次 不好调整
        if (isImport){
            existCorpCount = existCorpCount - addNum;
        }
        long expectTotal = existCorpCount + addNum;
        Integer tenantCorpLimit = byTenantCode.getCorpNumber();
        // 上限为null代表不限制，不校验
        if (tenantCorpLimit != null && tenantCorpLimit < expectTotal) {
            throw new ParameterException(CorpMessageEnum.SYS_TENANT_CORP_NUM_OVER_LIMIT,
                    String.valueOf(existCorpCount),
                    String.valueOf(addNum),
                    String.valueOf(tenantCorpLimit));
        }
    }
    @Transactional
    public void save(Corp corp, String userId) {
        if (StringUtils.isBlank(corp.getName())) {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_NAME_NOT_NULL);
        }
        //2018-12-10 by zhoul
        if (getCorpByName(corp.getName()) != null) {
            throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_NAME_REPEAT);
        }
        if (StringUtils.isBlank(corp.getCode())) {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_CODE_NOT_NULL);
        }
        if (getCorpByCode(corp.getCode()) != null) {
            throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_CODE_REPEAT);
        }
        //校验 不允许在单位下增加网点
        validateNetUnderCorp(corp);
        //检测许可控制的数量
        licenseNetOrCorpControl(corp);
        corp.setCreateBy(userId);
        corp.setCreateTime(new Date());
        if (corp.getSort() == null) {
            Integer maxSort = corpDao.getMaxSort();
            corp.setSort(++maxSort);
        }
        if(StringUtils.isEmpty((corp.getParentId()))) {
        	corp.setParentId(null);
        }
        getLockAndCheck(userId,1, false);
        doAfterLock(corp, userId);
    }

    public void doAfterLock(Corp corp, String userId) {
        User user = userDao.get(userId);
        corp.setTenantId(user.getTenantId());
        saveEntity(corp);
        corpCmMapService.saveCorpCmMap(corp.getId(), corp.getUnitCm());
        //设置单位所用账号
        if (StringUtils.isEmpty(corp.getUserAccountCode())) {
            corp.setUserAccountCode(corp.getId());
        }
        //由成员单位改成网点，初始化网点日结日期
        if (CORP_TYPE_CODE_NET.equals(corp.getType()) ) {
        /*if (dayCloseService == null) {
            dayCloseService = (IFcDayCloseService") ApplicationContextUtil.getContext().getBean("fcDayCloseSerciceImpl");
        }
        if (dayCloseService != null) {
        }*/
//            fcDayCloseServiceImpl.initNetDayClose(corp.getId(), userId);
            InitNetDayCloseMessageDTO messageDto = InitNetDayCloseMessageDTO.builder().netId(corp.getId()).userId(userId).build();
            messageDto.setMsgType(InitNetDayCloseMessageDTO.Msg.N);
            initNetDayCloseMQProducer.sendMessage(messageDto);
        }
        //设置父级网点
        Corp parentNet = getParentNetByCorpId(corp.getId());
        if (parentNet == null) {
            corp.setNetId(corp.getId());
        } else {
            corp.setNetId(parentNet.getId());
        }
        levelUpTopSetNetId(corp);
        //设置单位或网点的全路径，by zhoul 2018-05-11
        corp.setParentIds(getCorpRouter(corp.getId(), null));
        //管理员自动授权用户管理菜单的资源权限
        this.autoUserAssignResourceToAdmin(corp);
        // 清理单位缓存
        clearCache();
    }

    public static void main(String[] args) {
        String parentIds = "9218f781bff2434195fa6ff3d0b3c90e";
        int length = parentIds.split(CommonConstant.COMMA_SPLIT).length;
        System.out.println("length = " + length);
    }
    public void transferCorp(List<String> corpIds, String parentId, String userId) {
        if (ObjectUtil.isAllNotEmpty(corpIds, parentId)) {
            Corp parentCorp = corpDao.get(parentId);
            List<Corp> corpList = corpDao.listByIds(corpIds);
            List<Corp> batchList = new ArrayList<>();
            for (Corp corp : corpList) {
                // 移除二级及以上单位，只保留三级及以下单位
                int level = corp.getParentIds().split(CommonConstant.COMMA_SPLIT).length;
                if (level < 3) {
                    continue;
                }
                if (corp.getId().equals(parentCorp.getId())) {
                    throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_PARENT_ID_CAN_NOT_BE_SELF_ID);
                }
                // 只有变更了父节点才做变更操作
                if (ObjectUtil.notEqual(corp.getParentId(), parentId)) {
                    // 设置父节点
                    String oldParentId = corp.getParentId();
                    corp.setParentId(parentId);
                    // 设置单位或网点的全路径
                    corp.setParentIds(getCorpRouter(parentId, null).concat(corp.getId()).concat(CommonConstant.COMMA_SPLIT));
                    /*if (level == 3){
                        corp.setParentId(parentId);
                        // 设置单位或网点的全路径
                        corp.setParentIds(getCorpRouter(parentId, null).concat(corp.getId()).concat(CommonConstant.COMMA_SPLIT));
                    } else {
                        Optional<Corp> optional = batchList.stream().filter(item -> ObjectUtil.equal(oldParentId, item.getId())).distinct().findFirst();
                        optional.ifPresent(parent -> {
                            corp.setParentIds(parent.getParentIds().concat(corp.getId()).concat(CommonConstant.COMMA_SPLIT));
                        });
                    }*/
                    corp.setUpdateBy(userId);
                    corp.setUpdateTime(new Date());
                    batchList.add(corp);
                } else {
                    logger.info("单位【{}/{}】的父节点未变更，不需要更新！", corp.getCode(), corp.getName());
                }
            }
            if (ObjectUtil.isNotEmpty(batchList)){
                corpDao.batchUpdate(batchList);
                logger.info("批量变更【{}】单位信息。。。。。", batchList.size());
            } else {
                throw new ParameterException("没有符合条件的迁移单位！");
            }
        } else {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_ID_NULL);
        }
    }

    /**
     * 管理员自动授权用户管理菜单的资源权限
     *
     * @param corp
     */
    public void autoUserAssignResourceToAdmin(Corp corp) {
        List<String> corpIdList = new ArrayList<>();
        if (StringUtils.isBlank(corp.getParentIds())) {
            return;
        } else {
            String[] corpIds = corp.getParentIds().split(CommonConstant.COMMA_SPLIT);
            corpIdList = Arrays.asList(corpIds);
        }
        //获取上级单位/网点的管理员
        List<String> userIds = userDao.listUserAdminByCorpIds(corpIdList);
        if (EmptyUtils.isNotEmpty(userIds)) {
            //用户管理菜单
            String menuId = "402880425b951777015b95203d670005";
            List<Resources> listResources = resourcesService.listByMenuId(menuId);
            List<CorpUserResource> corpUserResourcesList = new ArrayList<>();
            for (String userId : userIds) {
                for (Resources resources : listResources) {
                    CorpUserResource corpUserResource = new CorpUserResource();
                    corpUserResource.setCorpId(corp.getId());
                    corpUserResource.setUserId(userId);
                    corpUserResource.setResourceId(resources.getId());
                    corpUserResource.setPermission(resources.getPermission());
                    corpUserResource.setMenuId(menuId);
                    corpUserResourcesList.add(corpUserResource);
                }
            }
            corpUserResourcesService.batchSaveData(corpUserResourcesList);
        }
    }
    @Transactional
    public AjaxJson update(Corp corp, String userId) {
        AjaxJson ajaxJson = new AjaxJson(true);
        if (corp.getId().equals(corp.getParentId())) {
            throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_PARENT_ID_CAN_NOT_BE_SELF_ID);
        }
        Corp dbCorp = corpDao.get(corp.getId());
        String oldParentId = dbCorp.getParentId();
        //如果对单位代码进行修改，则校验该代码是否唯一
        if (!corp.getCode().equals(dbCorp.getCode()) && getCorpByCode(corp.getCode()) != null) {
            throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_CODE_REPEAT);
        }
        // 由成员单位改成网点，初始化网点日结日期，这里做一个调整，只要是网点的，都发送初始化网点日结消息，消费端做了初始化判断，用来做为日结初始化失败的兜底
        if (/*dbCorp.getType()!=null && dbCorp.getType().equals(CORP_TYPE_CODE_CORP) &&*/ corp.getType().equals(CORP_TYPE_CODE_NET)) {
//            fcDayCloseServiceImpl.initNetDayClose(corp.getId(), userId);
            InitNetDayCloseMessageDTO messageDto = InitNetDayCloseMessageDTO.builder().netId(corp.getId()).userId(userId).build();
            messageDto.setMsgType(InitNetDayCloseMessageDTO.Msg.N);
            initNetDayCloseMQProducer.sendMessage(messageDto);
        }
        //如果是启用改为禁用需要校验单位下面不允许有效的单位
        if (corp.getStatus() == CorpConstant.CORP_STATUS_DISABLE
                && dbCorp.getStatus() == CorpConstant.CORP_STATUS_ENABLE) {
            if (!EmptyUtils.isEmptyList(listCorpByParentId(corp.getId()))) {
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg(multiLangServiceI.getLang(CorpConstant.SYS_CORP_MESSAGE_CHILD_DISABLE_ERROR));
                return ajaxJson;
            }
            //对禁用进行业务校验
            List<Map<String, Object>> errorInfo = getCorpVerifyErrorInfo(dbCorp, CorpConstant.CORP_VERIFY_DISABLE);
            if (ListUtils.isNotNullOrEmpty(errorInfo)) {
                ajaxJson.setSuccess(false);
                ajaxJson.setObj(errorInfo);
                ajaxJson.setMsg(multiLangServiceI.getLang(CorpConstant.SYS_CORP_MESSAGE_VERIFY_DISABLE_ERROR));
                return ajaxJson;
            }
        }
        //修改上级单位校验
        if (StringUtils.isNotEmpty(oldParentId)
                && !oldParentId.equals(corp.getParentId())) {
            List<Map<String, Object>> errorInfo = getCorpVerifyErrorInfo(dbCorp, CorpConstant.CORP_VERIFY_PARENTCHANG);
            if (ListUtils.isNotNullOrEmpty(errorInfo)) {
                ajaxJson.setSuccess(false);
                ajaxJson.setObj(errorInfo);
                ajaxJson.setMsg(multiLangServiceI.getLang(CorpConstant.SYS_CORP_MESSAGE_VERIFY_PARENTCHANG_ERROR));
                return ajaxJson;
            }
        }
        BeanUtils.copyNotNullProperties(corp, dbCorp);
        //添加默认排序
        if (dbCorp.getSort() == null) {
            dbCorp.setSort(500);
        }
        //校验 不允许在单位下增加网点
        validateNetUnderCorp(corp);
        //检测许可控制的数量
        licenseNetOrCorpControl(corp);
        //设置单位所用账号
        if (StringUtils.isEmpty(corp.getUserAccountCode())) {
            dbCorp.setUserAccountCode(corp.getId());
        }
        dbCorp.setParentId(corp.getParentId());
        dbCorp.setUpdateBy(userId);
        dbCorp.setUpdateTime(new Date());
        String oldNetId = dbCorp.getNetId();
        //设置父级网点
        Corp parentNet = getParentNetByCorpId(corp.getId());
        if (parentNet == null) {
            dbCorp.setNetId(corp.getId());
        } else {
            dbCorp.setNetId(parentNet.getId());
        }
        //单位修改不允许变更网点
        if (StringUtils.isNotEmpty(oldNetId)
                && !oldNetId.equals(dbCorp.getNetId())) {
            throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_CORP_UPDATENETCORP_ERROR);
        }
        levelUpTopSetNetId(dbCorp);
        //设置单位或网点的全路径，by zhoul 2018-05-11
        dbCorp.setParentIds(getCorpRouter(dbCorp.getId(), null));
        dbCorp.setProvinceId(corp.getProvinceId());
        corpDao.update(dbCorp);
        corpCmMapService.saveCorpCmMap(dbCorp.getId(), dbCorp.getUnitCm());
        // 清理单位缓存
        clearCache();
        return ajaxJson;
    }
    /**
     * 不允许在单位下增加网点
     *
     * @param corp
     */
    private void validateNetUnderCorp(Corp corp) {
        if (CORP_TYPE_CODE_NET.equals(corp.getType()) && StringUtils.isNotBlank(corp.getParentId())) {
            Corp parentCorp = get(corp.getParentId());
            if (parentCorp != null && CORP_TYPE_CODE_CORP.equals(parentCorp.getType())) {
                throw new BusinessException(CorpMessageEnum.SYS_CORP_MESSAGE_NOT_ALLOW_NET_UNDER_CORP);
            }
        }
    }
    /**
     * 如果网点/单位被置为顶级，则判断是否为网点，是的话则netId为自身，否则置空
     * by zhoul 2018-05-16
     *
     * @param dbCorp
     */
    private void levelUpTopSetNetId(Corp dbCorp) {
        if (StringUtils.isBlank(dbCorp.getParentId())) {
            if (dbCorp.getType().equals(CORP_TYPE_CODE_NET)) {
                dbCorp.setNetId(dbCorp.getId());
            } else {
                dbCorp.setNetId(null);
            }
        }
    }
    public AjaxJson deleteByid(String id) {
        AjaxJson ajaxJson = new AjaxJson(true,BaseCodeEnum.COMMON_TIPS_SUCCESS.getMsg().toString());
        //是否有下级单位验证
        if (!EmptyUtils.isEmptyList(listCorpByParentId(id, null))) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg(multiLangServiceI.getLang(CorpConstant.SYS_CORP_MESSAGE_CHILD_DELETE_ERROR));
        } else {
            //对删除进行业务校验
            List<Map<String, Object>> errorInfo = getCorpVerifyErrorInfo(get(id, true), CorpConstant.CORP_VERIFY_DELETE);
            if (ListUtils.isNullOrEmpty(errorInfo)) {
                corpDao.delete(id);
                // 清理单位缓存
                clearCache();
            } else {
                ajaxJson.setSuccess(false);
                ajaxJson.setObj(errorInfo);
                ajaxJson.setMsg(multiLangServiceI.getLang(CorpConstant.SYS_CORP_MESSAGE_VERIFY_DELETE_ERROR));
            }
        }
        return ajaxJson;
    }

    private void clearCache() {
        // 清理单位缓存
        String hashKey = RedisCacheKey.makeRedisKey("bytter-system", RedisCacheKey.SYS_CORP);
        redisUtils.remove(hashKey);
    }

    /**
     * 获取所有的菜单，排序为树形顺序机构
     *
     * @return
     */
    public List<CorpVO> getTreeTable(CorpVO search) {
        List<Corp> list = corpDao.listTreeTableBySearch(search);
        List<CorpVO> corpVOS = transformToTree(list);
        return corpVOS;
    }
    /**
     * 获取所有的菜单，排序为树形顺序机构
     *
     * @return
     */
    public List<CorpVO> getTreeTableV2(CorpVO search) {
        DictGroupDTO dictGroupIdForUnitAttribute = this.dictServiceI.get(CorpConstant.CORP_ATTRIBUTE_TYPE_DICT);
        List<CorpVO> list = corpDao.listTreeTableBySearch(search, dictGroupIdForUnitAttribute.getId());
        for (CorpVO corpVO : list) {
            corpVO.setParentIds(corpVO.getId() + ",");
        }
        List<CorpVO> corpVOS = transformCorpVOToTreeWithOutInject(list);
        return corpVOS;
    }
    /**
     * 获取所有启用的菜单
     *
     * @return
     */
    public List<CorpVO> getAllCorpByValid() {
        CorpVO search = new CorpVO();
        search.setStatus(CorpConstant.CORP_STATUS_ENABLE);
        List<Corp> list = corpDao.listTreeTableBySearch(search);
        List<CorpVO> corpVOList = BeanUtils.copyListProperties(list, CorpVO.class);
        return corpVOList;
    }
    public List<CorpVO> listAllTree() {
        // 取所有单位
        List<Corp> corpList = corpDao.listEffectiveCorp(null);
        return transformToTree(corpList);
    }
    public List<CorpVO> listAllTreeV2() {
        return listAllTreeV2(null);
    }
    /**
     * 单位组件，根据类型组装单位树
     *
     * @param type 类型
     * @return
     */
    public List<CorpVO> listAllTree(String type) {
        return listAllTree(type, null, null);
    }
    /**
     * 单位组件，根据类型组装单位树
     *
     * @param type 类型
     * @return
     */
    public List<CorpVO> listAllTreeV2(String type) {
        return listAllTreeV2(type, null, null);
    }

    /**
     * 获取该用户所拥有的单位，根据类型组装单位树
     *
     * @param type     类型
     * @param resource 操作资源
     * @param user     查询用户
     * @return
     */
    public List<CorpVO> listOwnerTree(String type, String resource, ICurrentUser user) {
        String userId = null;
        if (user != null) {
            userId = user.getId();
        }else {
        	userId = UserContextHelper.helper().getUserId();
        }
        //如果type，resource都为空，就查看当前用户所拥有单位的权限
        List<String> corpAuth = corpDao.getCorpIdsByUser(null,userId);
        List<Corp>   list = corpDao.listEffectiveCorp(corpAuth);
        if(CollectionUtils.isEmpty(corpAuth) || CollectionUtils.isEmpty(list))
        {
        	return null;
        }
        return transformToTree(list);
    }
    /**
     * 单位组件，根据类型组装单位树
     *
     * @param type     类型
     * @param resource 操作资源
     * @param user     查询用户
     * @return
     */
    public List<CorpVO> listAllTree(String type, String resource, ICurrentUser user) {
        return listAllTree(type, resource, user, null);
    }
    /**
     * 单位组件，根据类型组装单位树
     *
     * @param type      类型
     * @param resource  操作资源
     * @param user      查询用户
     * @param addCorpId 添加额外的单位信息
     * @return
     */
    public List<CorpVO> listAllTree(String type, String resource, ICurrentUser user, String addCorpId) {
        List<String> corpAuth = null;
        String userId = null;
        if (user != null) {
            userId = user.getId();
        }
        if (StringUtils.isNotEmpty(userId)
                && StringUtils.isNotEmpty(resource)
                && !CorpConstant.CORP_TYPE_RAT.equalsIgnoreCase(type)) {
            corpAuth = this.getUserCorpIds(resource, userId);
        }
        List<Corp> list;
        if (CorpConstant.CORP_TYPE_NET.equalsIgnoreCase(type)) {
            list = corpDao.listEffectiveCorpByNet(corpAuth);
        } else if (CorpConstant.CORP_TYPE_RAT.equalsIgnoreCase(type)) {
            list = listRatGroupCorpNet(resource, userId);
        } else if (CorpConstant.CORP_TYPE_CORP.equalsIgnoreCase(type)) {
            list = corpDao.listEffectiveCorpByCorp(corpAuth);
        } else {
            list = corpDao.listEffectiveCorp(corpAuth);
        }
        return transformToTree(list);
    }

    /**
     * 所用账号单位组件，组装单位树
     *
     * @param corpId 单位id
     * @return
     */
    public List<CorpVO> listUserAccountTreeByCorpId(String corpId, List<String> corpIds) {
        List<String> corpIdList = new ArrayList<>();
        if (StringUtils.isNotEmpty(corpId)) {
            corpIdList.addAll(Arrays.asList(corpId.split(",")));
        }
        if (!CollectionUtils.isEmpty(corpIds)) {
            corpIdList.addAll(corpIds);
        }
        List<Corp> corpIdlist = corpDao.listEffectiveCorp(corpIdList);
        List<String> corpAuth = new ArrayList<>();
        List<Corp> corpList = new ArrayList<>();
        for (Corp corp : corpIdlist) {
            corpList.add(corp);
            if (StringUtils.isNotEmpty(corp.getUserAccountCode())) {
                corpAuth.addAll(Arrays.asList(corp.getUserAccountCode().split(CommonConstant.COMMA_SPLIT)));
                if (corpAuth.contains(corpId)) {
                    corpAuth.remove(corpId);
                }
            }
        }
        List<Corp> list = corpDao.listEffectiveCorp(corpAuth);
        if (!EmptyUtils.isEmptyList(list)) {
            corpList.addAll(list);
        }
        HashSet h = new HashSet(corpList);
        corpList.clear();
        corpList.addAll(h);
        return transformToTree(corpList);
    }
    /**
     * 根据单位获取所用账号单位数据
     *
     * @param corpIds 单位ids
     * @return
     */
    public List<String> listUserAccountCorpByCorpId(List<String> corpIds) {
        if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        List<String> corpAuth = new ArrayList<>();
        List<Corp> list = corpDao.listEffectiveCorp(corpIds);
        for (Corp corp : list) {
            if (StringUtils.isNotEmpty(corp.getUserAccountCode())) {
                corpAuth.addAll(Arrays.asList(corp.getUserAccountCode().split(CommonConstant.COMMA_SPLIT)));
            }
        }
        return corpAuth;
    }
    /**
     * 单位组件，根据类型组装单位树
     *
     * @param type     类型
     * @param resource 操作资源
     * @param user     查询用户
     * @return
     */
    public List<CorpVO> listAllTreeV2(String type, String resource, ICurrentUser user) {
        List<String> corpAuth = null;
        String userId = null;
        if (user != null) {
            userId = user.getId();
        }
        if (StringUtils.isNotEmpty(userId)
                && StringUtils.isNotEmpty(resource)
                && !CorpConstant.CORP_TYPE_RAT.equalsIgnoreCase(type)) {
            corpAuth = this.getUserCorpIds(resource, userId);
        }
        List<Corp> list;
        if (CorpConstant.CORP_TYPE_NET.equalsIgnoreCase(type)) {
            list = corpDao.listEffectiveCorpByNet(corpAuth);
        } else if (CorpConstant.CORP_TYPE_RAT.equalsIgnoreCase(type)) {
            list = listRatGroupCorpNet(resource, userId);
        } else if (CorpConstant.CORP_TYPE_CORP.equalsIgnoreCase(type)) {
            list = corpDao.listEffectiveCorpByCorp(corpAuth);
        } else {
            list = corpDao.listEffectiveCorp(corpAuth);
        }
        return transformToTreeWithOutInject(list);
    }
    /**
     * 通过ID查找网点单位
     *
     * @param id
     * @return
     */
    public List<CorpSimpleVO> listById(String id) {
        return corpDao.listByCorpId(id);
    }
    public List<Corp> listByIds(List<String> ids) {
        return corpDao.listByIds(ids);
    }
    public List<Corp> listByType(String type) {
        if (StringUtils.isEmpty(type)) {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_TYPE_NULL);
        }
        int corpType;
        if (CorpConstant.CORP_TYPE_CORP.equalsIgnoreCase(type)) {
            corpType = CORP_TYPE_CODE_CORP;
        } else if (CorpConstant.CORP_TYPE_NET.equalsIgnoreCase(type)) {
            corpType = CORP_TYPE_CODE_NET;
            return corpDao.listByCorpType(corpType);
        } else {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_TYPE_ERROR);
        }
        return corpDao.listBy("type", corpType, "sort", true);
    }
    public String getInjectAttributeAnnotation(Field field) {
        com.bytter.core.annotation.Corp corp = field.getAnnotation(com.bytter.core.annotation.Corp.class);
        return corp == null ? null : corp.inject();
    }
    /**
     * 获取有效的网点、单位
     *
     * @return
     */
    public List<Corp> listEffectiveCorp() {
        return corpDao.listEffectiveCorp(null);
    }
    /**
     * 获取有效的网点、单位
     *
     * @return
     */
    public List<Corp> listEffectiveCorpBaiscInfo() {
        return corpDao.listEffectiveCorpBaiscInfo();
    }
    /**
     * 获取传入用户对应资源的单位或网点id
     * <br>
     * <b>作者：</b>周亮<br>
     * <b>日期：</b>2017年6月20日<br>
     *
     * @param resource
     * @param userId
     * @return 单位id
     */
    public List<String> getUserCorpIds(String resource, String userId) {
        List<String> list = this.corpDao.getUserCorpIds(resource, userId);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return distinctList(list);
    }
    /**
     * 获取传入用户对应资源的单位或网点id
     * <br>
     * <b>作者：</b>周亮<br>
     * <b>日期：</b>2017年6月20日<br>
     *
     * @param resource
     * @param userId
     * @return 单位id
     */
    public List<String> getCorpIdsByUserResource(String resource, String userId) {
        return this.corpDao.getCorpIdsByUserResource(resource, userId);
    }
    /**
     * 根据单位id获取单位基础信息
     * <br>
     * <b>作者：</b>周亮<br>
     * <b>日期：</b>2017年6月20日<br>
     *
     * @param corpIds
     * @return 单位id
     */
    public List<Corp> getCorpBasicInfoByCorpIds(Set<String> corpIds) {
        return this.corpDao.getCorpBasicInfoByCorpIds(corpIds);
    }
    /**
     * 获取传入用户对应资源有效的网点或单位id
     * <br>
     * <b>作者：</b>吕伟林<br>
     *
     * @param resource
     * @param userId
     * @param type     CorpConstant.CORP_TYPE_CODE_NET or CorpConstant.CORP_TYPE_CODE_CORP
     * @return 单位id
     */
    public List<String> getUserNetIds(String resource, String userId, Integer type) {
        List<String> list = this.corpDao.getUserNetIds(resource, userId, type);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list;
    }
    /**
     * List非对象去重复
     *
     * @param list
     * @return
     * @author zuoy 2019-07-01
     */
    public List<String> distinctList(List<String> list) {
        HashSet<String> set = new HashSet<>(list);
        return new ArrayList<>(set);
    }
    /**
     * 单位组件-
     * 获取过滤权限资源的结构
     *
     * @param resource 资源值
     * @param id       网点ID
     * @return 集合数据
     */
    public List<CorpVO> getFilterCorp(String resource, String id, String type, String userId, String parentId) {
        List<String> corpIds = getUserCorpIds(resource, userId);
        String corpAuthSql = SqlUtil.getInSql(corpIds, "c.id", true);
        Integer typeTemp = null;
        if (!EmptyUtils.isBlank(type)) {
            typeTemp = CorpConstant.CORP_TYPE_NET.equalsIgnoreCase(type) ? CORP_TYPE_CODE_NET : CORP_TYPE_CODE_CORP;
        }
        List<Corp> corpList = corpDao.listCorpByTypeAndStatus(corpAuthSql, id, typeTemp, CorpConstant.CORP_STATUS_ENABLE, parentId);
        return transformToTree(corpList);
    }
    /**
     * 单位组件-
     * 获取过滤权限资源的结构
     *
     * @param corpComponentVO 单位组件查询VO对象
     * @param currentUser     用户
     * @return 集合数据
     */
    public List<CorpVO> getFilterCorp(CorpComponentVO corpComponentVO, ICurrentUser currentUser) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("getFilterCorp getCorpIdsByUserResource spends:");
        long startTime = System.currentTimeMillis();
        // 获取传入用户对应资源的单位或网点id
        //List<String> corpIds = getUserCorpIds(corpComponentVO.getResource(), currentUser.getId());
        List<String> corpIds = getCorpIdsByUserResource(corpComponentVO.getResource(), currentUser.getId());
        //判断是否加载所属账号单位
        if ("accCorp".equals(corpComponentVO.getType())) {
            return this.listUserAccountTreeByCorpId(null, corpIds);
        } else {
            stopWatch.stop();
            stopWatch.start("getFilterCorp getFilterCorp spends:");
            logger.info("getUserCorpIds：" + (System.currentTimeMillis() - startTime));
            // 组装In SQL
            String corpAuthSql = SqlUtil.getInSql(corpIds, "c.id", true);
            long startTime2 = System.currentTimeMillis();
            // 查出符合条件的单位
            List<Corp> corpList = corpDao.getFilterCorp(corpAuthSql, corpComponentVO);
            logger.info("getFilterCorp：" + (System.currentTimeMillis() - startTime2));
            long startTime3 = System.currentTimeMillis();
            stopWatch.stop();
            logger.info("getFilterCorp total spends:{},corpComponentVO:{}", stopWatch.prettyPrint(), JsonUtils.toJSONString(corpComponentVO));
            // stopWatch.start("getFilterCorp transformToTree spends:");
            // 树化
            List<CorpVO> corpVOS = transformToTree(corpList);
            logger.info("transformToTree：" + (System.currentTimeMillis() - startTime3));
            return corpVOS;
        }
    }

    /**
     * 单位组件-
     * 获取过滤权限资源的结构 (返回"全部单位"筛选项)
     *
     * @param corpComponentVO 单位组件查询VO对象
     * @param currentUser     用户
     * @return 集合数据
     */
    public List<CorpVO> getFilterCorp1(CorpComponentVO corpComponentVO, ICurrentUser currentUser) {
        StopWatch stopWatch=new StopWatch();
        stopWatch.start("getFilterCorp getCorpIdsByUserResource spends:");
        long startTime = System.currentTimeMillis();
        // 获取传入用户对应资源的单位或网点id
        //List<String> corpIds = getUserCorpIds(corpComponentVO.getResource(), currentUser.getId());
        List<String> corpIds = getCorpIdsByUserResource(corpComponentVO.getResource(), currentUser.getId());
        //判断是否加载所属账号单位
        if("accCorp".equals(corpComponentVO.getType())){
            return this.listUserAccountTreeByCorpId(null,corpIds);
        }else{
            stopWatch.stop();
            stopWatch.start("getFilterCorp getFilterCorp spends:");
            logger.info("getUserCorpIds：" + (System.currentTimeMillis() - startTime));
            // 组装In SQL
            String corpAuthSql = SqlUtil.getInSql(corpIds, "c.id", true);
            long startTime2 = System.currentTimeMillis();
            // 查出符合条件的单位
            List<Corp> corpList = corpDao.getFilterCorp(corpAuthSql, corpComponentVO);
            logger.info("getFilterCorp：" + (System.currentTimeMillis() - startTime2));
            long startTime3 = System.currentTimeMillis();
            stopWatch.stop();
            logger.info("getFilterCorp total spends:{},corpComponentVO:{}",stopWatch.prettyPrint(),JsonUtils.toJSONString(corpComponentVO));
            // stopWatch.start("getFilterCorp transformToTree spends:");
            // 树化
            List<CorpVO> corpVOS = transformToTree(corpList);
            if (!EmptyUtils.isEmpty(corpVOS)) {
                CorpVO corpVO = new CorpVO();
                corpVO.setName("全部单位");
                corpVO.setId("allNode");
                corpVO.setSort(0);
                corpVOS.add(0,corpVO);
            }
            logger.info("transformToTree：" + (System.currentTimeMillis() - startTime3));
            return corpVOS;
        }
    }

    /**
     * 获取单位树排序规则
     *
     * @return
     */
    public List<Comparator<CorpVO>> getCompareList() {
        final List<Comparator<CorpVO>> compareList = new ArrayList<>();
        // sort排序
        Comparator<CorpVO> compareSort = new Comparator<CorpVO>() {
            @Override
            public int compare(CorpVO o1, CorpVO o2) {
                if (o1.getSort() == null && o2.getSort() == null) {
                    return 0;
                } else if (o1.getSort() == null) {
                    return 1;
                } else if (o2.getSort() == null) {
                    return -1;
                }
                return o1.getSort().compareTo(o2.getSort());
            }
        };
        compareList.add(compareSort);
        // code排序
        Comparator<CorpVO> compareCode = new Comparator<CorpVO>() {
            @Override
            public int compare(CorpVO o1, CorpVO o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        };
        compareList.add(compareCode);
        return compareList;
    }
    /**
     * 单位转换树结构
     *
     * @param list
     * @return
     */
    public List<CorpVO> transformToTree(List<Corp> list) {
        List<CorpVO> corpVOList = BeanUtils.copyListProperties(list, CorpVO.class);
        final List<Comparator<CorpVO>> compareList = getCompareList();
        // 组装树，并排序
        return TreeNodeUtils.transformToTree(corpVOList, new Comparator<CorpVO>() {
            @Override
            public int compare(CorpVO o1, CorpVO o2) {
                for (Comparator<CorpVO> comparator : compareList) {
                    if (comparator.compare(o1, o2) > 0) {
                        return 1;
                    } else if (comparator.compare(o1, o2) < 0) {
                        return -1;
                    }
                }
                return 0;
            }
        });
    }
    /**
     * 单位转换树结构
     *
     * @param list
     * @return
     */
    public List<CorpVO> transformToTreeWithOutInject(List<Corp> list) {
        List<CorpVO> corpVOList = BeanUtils.copyListProperties(list, CorpVO.class);
        return transformCorpVOToTreeWithOutInject(corpVOList);
    }
    /**
     * 单位转换树结构
     *
     * @param list
     * @return
     */
    public List<CorpVO> transformCorpVOToTreeWithOutInject(List<CorpVO> list) {
        // 管理单元多个时，列表不展示
        HashMap<String, CellManage> unitMap = new HashMap();
        for (CorpVO corp : list) {
            if (StringUtils.isNotBlank(corp.getUnitCm()) && StringUtils.isBlank(corp.getUnitCmName())) {
                String[] unitCms = corp.getUnitCm().split(CommonConstant.COMMA_SPLIT);
                for (String unitCm : unitCms) {
                    CellManage cell = unitMap.get(unitCm);
                    if (cell == null) {
                        cell = cellManageService.get(unitCm, true);
                        unitMap.put(unitCm, cell);
                    }
                    if (cell != null) {
                        if (StringUtils.isBlank(corp.getUnitCmName())) {
                            corp.setUnitCmName(cell.getCellName());
                        } else {
                            corp.setUnitCmName(corp.getUnitCmName() + CommonConstant.COMMA_SPLIT + cell.getCellName());
                        }
                    }
                }
            }
        }
        StopWatch stopWatch = new StopWatch("transformToTree");
        stopWatch.start("inject");
        //inject(corpVOList);
        stopWatch.stop();
        stopWatch.start("getCompareList");
        final List<Comparator<CorpVO>> compareList = getCompareList();
        stopWatch.stop();
        stopWatch.start("TreeNodeUtils.transformToTree");
        // 组装树，并排序
        List<CorpVO> result = TreeNodeUtils.transformToTree(list, new Comparator<CorpVO>() {
            @Override
            public int compare(CorpVO o1, CorpVO o2) {
                for (Comparator<CorpVO> comparator : compareList) {
                    if (comparator.compare(o1, o2) > 0) {
                        return 1;
                    } else if (comparator.compare(o1, o2) < 0) {
                        return -1;
                    }
                }
                return 0;
            }
        });
        stopWatch.stop();
        logger.debug("transformToTree all spends :" + stopWatch.prettyPrint());
        return result;
    }
    @Transactional
    public void updateSort(String id, Integer sort) {
        Corp corp = get(id);
        corp.setSort(sort);
    }
    /**
     * 初始化所有单位网点的全路径
     */
    @Transactional
    public void initFullRouter() {
        if (corpDao.checkParentIdAndParentIdsIsNull()) {
            Query from_corp = corpDao.createQuery("from Corp");
            List list = from_corp.list();
            for (Object obj : list) {
                Corp corp = (Corp) obj;
                corp.setParentIds(getCorpRouter(corp.getId(), null));
            }
        }
    }
    /**
     * 获取该单位的全路径
     *
     * @param id 当前单位或网点id
     * @return
     */
    private String getCorpRouter(String id, StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        Corp corp = get(id);
        if (corp != null) {
            sb.insert(0, ",");
            sb.insert(0, corp.getId());
        }
        if (StringUtils.isNotBlank(corp.getParentId())) {
            getCorpRouter(corp.getParentId(), sb);
        }
        return sb.toString();
    }
    private String getParentIds(String name, StringBuffer sb, Map<String, Corp> nameMap, Map<String, CorpVO> importMap) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        Corp corp = nameMap.get(name);
        if (corp != null) {
            sb.insert(0, CommonConstant.COMMA_SPLIT);
            sb.insert(0, corp.getId());
            CorpVO corpImportVo = importMap.get(name);
            if (nameMap.containsKey(corpImportVo.getParentName())) {
                getParentIds(corpImportVo.getParentName(), sb, nameMap, importMap);
            }
        }
        return sb.toString();
    }
    /**
     * 根据corpId获取该单位的上级网点
     *
     * @return 网点对象
     * @author 周亮
     */
    public Corp getParentNetByCorpId(String corpId) {
        Corp corp = get(corpId);
        if (StringUtils.isBlank(corp.getParentId())) {
            return corp;
        }
        // zuoy 2019-12-19 如果本身是网点，则网点ID为本身
        if (corp.isNet()) {
            return corp;
        }
        return getParentNetByParentCorpId(corp.getParentId());
    }
    /**
     * 获取该单位的上级网点
     *
     * @param parentCorpId 上级单位
     * @return
     */
    public Corp getParentNetByParentCorpId(String parentCorpId) {
        if (StringUtils.isEmpty(parentCorpId)) {
            return null;
        }
        Corp corp = get(parentCorpId);
        if (corp != null && !corp.isNet()) {
            if (StringUtils.isBlank(corp.getParentId())) {
                return corp;
            }
            return getParentNetByParentCorpId(corp.getParentId());
        }
        return corp;
    }
    public Corp getCorpByName(String name) {
        return corpDao.getCorpByName(name);
    }
    public Corp getCorpByCode(String code) {
        return corpDao.getCorpByCode(code);
    }
    public Corp getParentById(String id) {
        return corpDao.getParentById(id);
    }
    /**
     * 功能介绍 : 获取集团统一授信主体单位。<br>
     * <b>日期：</b>2017年7月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<Corp> listRatGroupCorpNet(String resource, String userId) {
        if (EmptyUtils.isBlank(resource)) {
            return null;
        }
        List<String> corpIds = getUserCorpIds(resource, userId);
        return corpDao.listRatGroupCorpNet(corpIds);
    }
    public List<Corp> listCorpByParentId(String parentId) {
        return listCorpByParentId(parentId, CorpConstant.CORP_STATUS_ENABLE);
    }
    public List<Corp> listCorpByParentId(String parentId, Integer status) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder hql = new StringBuilder("from Corp c where c.parentId =:parentId ");
        params.put("parentId", parentId);
        if (status != null) {
            hql.append(" and c.status = :status ");
            params.put("status", status);
        }
        hql.append(" order by c.sort ");
        return corpDao.listByHQL(hql.toString(), params);
    }
    public IPageResult<CorpVO> listByCorpDetail(DataTablesInput dataTablesInput) {
        String corpId = dataTablesInput.getParameter().get("corpId");
        if (StringUtils.isEmpty(corpId) || EmptyUtils.isEmpty(get(corpId, true))) {//没有该单位，查所有单位
            corpId = null;
        } else if (EmptyUtils.isEmptyList(listCorpByParentId(corpId)) && !EmptyUtils.isEmpty(getParentById(corpId))) {//没有子单位，查同级单位
            corpId = getParentById(corpId).getId();
        }
        Map<String, Object> params = dataTablesInput.getFormdata();
        params.put("corpId", corpId);
        dataTablesInput.setFormdata(params);
        return corpDao.listByCorpDetail(dataTablesInput);
    }
    /**
     * Created by chenlj 2018/4/24
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput listCorpAndBtBankAccVO(DataTablesInput dataTablesInput) {
        String corpId = dataTablesInput.getParameter().get("corpId");
        if (StringUtils.isNotBlank(corpId)) {
            Corp corp = corpDao.get(corpId, true);
            if (corp != null) {
                Corp netCorp = getParentNetByParentCorpId(corp.getParentId());
                if (netCorp != null) {
                    List<String> idsByUnderTheNet = corpDao.getIdsByUnderTheNet(netCorp.getNetId());
                    Map<String, Object> params = dataTablesInput.getFormdata();
                    params.put("corpIds", idsByUnderTheNet);
                    dataTablesInput.setFormdata(params);
                }
            }
        }
//
//        if (StringUtils.isEmpty(corpId) || EmptyUtils.isEmpty(get(corpId, true))) {//没有该单位，查所有单位
//            corpId = null;
//        } else if (EmptyUtils.isEmptyList(listCorpByParentId(corpId)) && !EmptyUtils.isEmpty(getParentById(corpId))) {//没有子单位，查同级单位
//            corpId = getParentById(corpId).getId();
//        }
        IPageResult<CorpAndBtBankAccVO> pageResult = corpDao.listByCorpDetailAndBtBankAccVO(dataTablesInput);
        DataTablesOutput dataTablesOutput = pageResult.convert(DataTablesOutput.class);
        return dataTablesOutput;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        return corpDao.getDataTablesUnion(input);
    }
    /**
     * 根据corpIds组装单位树
     *
     * @param corpIds
     * @return
     * @author zuoy 2018-06-11
     */
    public List<CorpVO> treeByCorpIds(String corpIds) {
        if (EmptyUtils.isBlank(corpIds)) {
            return null;
        }
        List<String> corpId = Arrays.asList(corpIds.split(CommonConstant.COMMA_SPLIT));
        List<Corp> corpList = this.corpDao.listCorpByCorpIds(corpId);
        if (EmptyUtils.isEmptyList(corpList)) {
            return null;
        }
        return transformToTree(corpList);
    }
    /**
     * 查询二次授信单位
     *
     * @param
     * @return
     */
    public List<Corp> listCpRationCorpNet(String resource, String netId, String userId) {
        // TODO Auto-generated method stub
        String corpAuthSql;
        if (!EmptyUtils.isBlank(resource)) {
            corpAuthSql = SqlUtil.getInSql(getUserCorpIds(resource, userId), "c.id", true);
        } else {
            return null;
        }
        return this.corpDao.listRatCpCorpNet(netId, corpAuthSql);
    }
//    @Override
    public ExcelVerifyHanlderResult verifyHandler(CorpVO vo) {
        return null;
    }
//    @Override
    public void verifyHandler(List<CorpVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            // 单位名称集合，用来校验单位是否一致，上级单位是否一起导入
            Map<String, CorpVO> corpNameMap = new HashMap<>();
            // 机构代码集合，用来校验单位是否一致，所用帐号单位代码是否一起导入
            Map<String, CorpVO> corpCodeMap = new HashMap<>();
            //管理单元信息信息缓存
            Map<String, CellManage> unitCmMap = new HashMap<>();
            //管理单元信息异常信息缓存
            Map<String, String> unitCmErrorMap = new HashMap<>();
            // 单位属性的错误信息
            Map<String, String> unitName = new HashMap<>();
            // 国家信息缓存
            Map<String, SysCountryCustDTO> sysCountryMap = new HashMap<>();
            List<SysCountryCustDTO> sysCountryCustDTOS = sysCountryCustService.listAll();
            for (SysCountryCustDTO sysCountryCustDTO : sysCountryCustDTOS) {
                sysCountryMap.put(sysCountryCustDTO.getNameZh(), sysCountryCustDTO);
            }
            // 获取所有的单位
            List<CorpDTO> corpList = corpDao.getSimpleCorpList();
            // 获取Name和Code集合
            Map<String, String> corpNameAndId = corpList.stream().collect(Collectors.toMap(CorpDTO::getName, CorpDTO::getId, (existing, replacement) -> existing));
            List<String> codeList = corpList.stream().map(CorpDTO::getCode).collect(Collectors.toList());
            List<String> nameList = corpList.stream().map(CorpDTO::getName).collect(Collectors.toList());
            // 获取有效的货币资料信息
            List<BtCurrencyDTO> currencyDTOS = btCurrencyServiceI.getBtCurrency();
            // 有效货币信息的缓存
            Map<String, BtCurrencyDTO> btCurrencyMap = new HashMap<>();
            for (BtCurrencyDTO currencyDTO : currencyDTOS) {
                btCurrencyMap.put(currencyDTO.getCurName(), currencyDTO);
            }
            String reg = "^[a-zA-Z0-9|-]*$";
            Pattern pattern = Pattern.compile(reg);
            // 字典缓存
            Map<String, DictDTO> unitAttributeMap = dictServiceI.listMap(CorpConstant.CORP_ATTRIBUTE_TYPE_DICT);
            int length = 300;
            String langName = multiLangServiceI.getLang(CorpConstant.SYS_CORP_MESSAGE_NAMEEN_MAXLENGTH_ERROR, String.valueOf(length));
            String langAddress = multiLangServiceI.getLang(CorpConstant.SYS_CORP_MESSAGE_ADDRESSEN_MAXLENGTH_ERROR, String.valueOf(length));
            // 省市信息的缓存
            Map<String, RegionDTO> regionDTOMap = new HashMap<>();
            List<RegionDTO> regionDTOS = regionServiceI.listProvAll();
            for (RegionDTO regionDTO : regionDTOS) {
                regionDTOMap.put(regionDTO.getName(), regionDTO);
            }
            for (CorpVO vo : list) {
                List<String> msgList = new ArrayList<>();
                if (StringUtils.isNotEmpty(vo.getTypeName())) {
                    if (vo.getTypeName().equals("1")) {
                        vo.setType(1);
                        vo.setTypeName("成员单位");
                    } else if (vo.getTypeName().equals("0")) {
                        vo.setType(0);
                        vo.setTypeName("结算中心");
                    } else {
                        msgList.add("sys.corp.message.type.error");
                    }
                } else {
                    msgList.add("sys.corp.message.type.not.null");
                }
                if (StringUtils.isNotEmpty(vo.getStatusName())) {
                    if (vo.getStatusName().equals("1")) {
                        vo.setStatus(1);
                        vo.setStatusName("启用");
                    } else if (vo.getStatusName().equals("0")) {
                        vo.setStatus(0);
                        vo.setStatusName("禁用");
                    } else {
                        msgList.add("sys.corp.message.status.error");
                    }
                } else {
                    vo.setStatus(1);
                    vo.setStatusName("启用");
                }
                if (StringUtils.isNotEmpty(vo.getNameEn())) {
                    // 单位名称英文不能错过300长度
                    if (vo.getNameEn().length() > length) {
                        msgList.add(langName);
                    }
                }
                if (StringUtils.isNotEmpty(vo.getAddressEn())) {
                    // 单位地址英文不能错过300长度
                    if (vo.getAddressEn().length() > length) {
                        msgList.add(langAddress);
                    }
                }
                if (ObjectUtil.isNotEmpty(vo.getCode())) {
                    // 机构代码不能大于30个字符！
                    if (vo.getCode().length() > 30) {
                        msgList.add("sys.sysCorp.code.maxLength");
                    }
                    // 机构代码只能填写英文数字及'-'符号
                    if (!pattern.matcher(vo.getCode()).matches()) {
                        msgList.add("sys.sysCorp.code.pattern");
                    }
                    // 校验单位code是否重复
                    if (codeList.contains(vo.getCode()) || corpCodeMap.containsKey(vo.getCode())) {
                        msgList.add("sys.corp.message.code.repeat");
                    }
                } else {
                    msgList.add("sys.corp.message.code.not.null");
                }
                if (ObjectUtil.isNotEmpty(vo.getName())) {
                    // 组织名称至少得3个字符！
                    if (vo.getName().length() < 3) {
                        msgList.add("sys.sysCorp.minLength");
                    }
                    // 组织名称不能大于50个字符！
                    if (vo.getName().length() > 200) {
                        msgList.add("sys.sysCorp.maxLength");
                    }
                    //2019-04-10 by zuoyu
                    if (nameList.contains(vo.getName()) || corpNameMap.containsKey(vo.getName())) {
                        msgList.add("sys.corp.message.name.repeat");
                    }
                } else {
                    msgList.add("sys.corp.message.name.not.null");
                }
                //校验单位类型是否存在
                if (StringUtils.isNotBlank(vo.getUnitAttributeName())) {
                    if (!unitAttributeMap.containsKey(vo.getUnitAttributeName())) {
                        // 踢出去
                        msgList.add(multiLangServiceI.getLang(CorpConstant.SYS_CORP_MESSAGE_UNITATTRIBUTE_ERROR, vo.getUnitAttributeName()));
                    } else {
                        vo.setUnitAttribute(unitAttributeMap.get(vo.getUnitAttributeName()).getCode());
                        vo.setUnitAttributeName(unitAttributeMap.get(vo.getUnitAttributeName()).getName());
                    }
                }
                //校验管理单元
                if (CorpConstant.CORP_ATTRIBUTE_TYPE_02.equals(vo.getUnitAttribute())) {
                    if (StringUtils.isEmpty(vo.getUnitCmName())) {
                        msgList.add(CorpConstant.SYS_CORP_MESSAGE_UNITCM_NOT_FOUND);
                    }
                }
                //判断管理单元是否存在
                if (StringUtils.isNotEmpty(vo.getUnitCmName())) {
                    String[] unitCms = vo.getUnitCmName().replace("，", ",").split(",");
                    StringBuilder unitCmSb = new StringBuilder();
                    for (String unitCm : unitCms) {
                        if (StringUtils.isNotEmpty(unitCm)) {
                            if (unitCmErrorMap.containsKey(unitCm)) {
                                msgList.add(unitCmErrorMap.get(unitCm));
                                continue;
                            }
                            if (unitCmMap.containsKey(unitCm)) {
                                if (unitCmSb.length() > 0) {
                                    unitCmSb.append(",");
                                }
                                unitCmSb.append(unitCmMap.get(unitCm).getId());
                                continue;
                            }
                            CellManage info = cellManageService.getCellManageByCodeOrName(unitCm, null);
                            if (info == null) {
                                info = cellManageService.getCellManageByCodeOrName(null, unitCm);
                            }
                            if (info == null) {
                                String msg = multiLangServiceI.getLang(CorpConstant.SYS_CORP_MESSAGE_UNITCM_ERROR, unitCm);
                                msgList.add(msg);
                                unitCmErrorMap.put(unitCm, msg);
                            } else {
                                unitCmMap.put(info.getCellCode(), info);
                                unitCmMap.put(info.getCellName(), info);
                                if (unitCmSb.length() > 0) {
                                    unitCmSb.append(",");
                                }
                                unitCmSb.append(info.getId());
                            }
                        }
                    }
                    if (unitCmSb.length() > 0) {
                        vo.setUnitCm(unitCmSb.toString());
                    }
                }
                if (!EmptyUtils.isBlank(vo.getCurName())) {
                    // 拿出去 getBtCurrency
                    BtCurrencyDTO btCurrencyDTO = btCurrencyMap.get(vo.getCurName());
                    String curId = btCurrencyDTO.getId();
                    // String curId = btCurrencyServiceI.getCurrencyByName(vo.getCurName());
                    if (EmptyUtils.isBlank(curId)) {
                        msgList.add("nis.netbank.message.cur.null");
                    } else {
                        vo.setCurId(curId);
                    }
                }
                if (!EmptyUtils.isBlank(vo.getParentName()) && !corpNameMap.containsKey(vo.getParentName())) {
                    // 拿出去
                    // Corp parentCorp = corpMap.get(vo.getParentName());
                    String corpId = corpNameAndId.get(vo.getParentName());
                    if (EmptyUtils.isEmpty(corpId)) {
                        msgList.add("sys.corp.message.parent.not.info");
                    } else {
                        vo.setParentId(corpId);
                    }
                }
                if (!EmptyUtils.isBlank(vo.getUserAccountCodeName())
                        && !vo.getCode().equalsIgnoreCase(vo.getUserAccountCodeName())
                        && !corpCodeMap.containsKey(vo.getUserAccountCodeName())) {
                    // 拿出去
                    // Corp userAccountCrop = corpMap.get(vo.getUserAccountCodeName());
                    String corpId = corpNameAndId.get(vo.getUserAccountCodeName());
                    if (EmptyUtils.isEmpty(corpId)) {
                        msgList.add("sys.corp.message.useraccountcode.not.info");
                    } else {
                        vo.setUserAccountCode(corpId);
                    }
                }
                //判断国家/地区在系统中是否存在
                if (!EmptyUtils.isBlank(vo.getCountryName())) {
                    SysCountryCustDTO countryCustDTO = sysCountryMap.get(vo.getCountryName());
//                    SysCountryCustDTO countryCustDTO = sysCountryCustService.getByNameZh(vo.getCountryName());
                    if (countryCustDTO == null) {
                        msgList.add("sys.black.white.import.country.nonentity.tips");
                    } else {
                        vo.setCountryId(countryCustDTO.getId());
                        vo.setCountryName(countryCustDTO.getNameZh());
                    }
                }
                // 判断省市在系统中是否存在
                RegionDTO provRegion = regionDTOMap.get(vo.getProvName());
                RegionDTO cityRegion = regionDTOMap.get(vo.getCityName());
                if (!EmptyUtils.isEmpty(provRegion) && !EmptyUtils.isEmpty(cityRegion)) {
                    //判断省市是否一致
                    if (!cityRegion.getParentId().equals(provRegion.getId())) {
                        msgList.add("nis.netbank.message.cityname.mismatch");
                    } else {
                        vo.setProvince(provRegion.getId());
                        vo.setCity(cityRegion.getId());
                    }
                }
                vo.addErrorMsg(msgList);
                corpNameMap.put(vo.getName(), vo);
                corpCodeMap.put(vo.getCode(), vo);
            }
        }
    }
    /**
     * 导入保存（优化后的）
     * update by huangw 2021-12-7
     *
     * @param list
     * @param user
     */
    @Transactional
    public void saveImport(List<CorpVO> list, ICurrentUser user) {
        String userId = user.getId();
        Date curDate = DateUtils.getDate();
        List<Corp> corpBefore = this.listAll();
        List<Corp> corpList = new ArrayList<>();
        List<DictDTO> listDict = dictServiceI.list("sys_corp_attribute_type");
        // 存放所有单位信息的Map
        Map<String, CorpVO> corpVoMap = new HashMap<>();
        for (CorpVO vo : list) {
            corpVoMap.put(vo.getName(), vo);
            Corp corp = new Corp();
            BeanUtils.copyNotNullProperties(vo, corp);
            if (EmptyUtils.isEmpty(corp.getStatus())) {
                corp.setStatus(1);
            }
            if (EmptyUtils.isEmpty(corp.getListedCompany())) {
                corp.setListedCompany(1);
            }
            if(StringUtils.isNotBlank(vo.getCurName())) {
            	BtCurrency btCurrency = btCurrencyServiceBase.getBtCurrencyByName(vo.getCurName());
            	if(!ObjectUtils.isEmpty(btCurrency)) {
            		corp.setCurId(btCurrency.getId());
            	}
            }
            if(StringUtils.isNotBlank(vo.getUnitCmName())) {
            	CellManage cellManage = cellManageDao.getCellManageByCodeOrName(null,vo.getUnitCmName());
            	if(!ObjectUtils.isEmpty(cellManage)) {
            		corp.setUnitCm(cellManage.getId());
            	}
            }
            if(!CollectionUtils.isEmpty(listDict)) {
            	listDict.stream().map(map -> listDict.stream()
            	.filter(m -> m.getName().equals(vo.getUnitAttributeName()))
            	.findFirst().map(m -> {
            		corp.setUnitAttribute(m.getCode());
                    return map;
                }).orElse(null))
        	.filter(Objects::nonNull).collect(Collectors.toList());
            }
            corp.setReportDisplay(CommonConstant.YES);
            corp.setCreateBy(userId);
            corp.setCreateTime(curDate);
            // 批处理
            corpList.add(corp);
        }
        this.batchSave(corpList);
        // 获取单位ID/单位名Map对象
        Map<String, String> corpNameMap = corpBefore.stream().collect(Collectors.toMap(Corp::getId, Corp::getName, (first, second) -> first));
        for (Corp corp : corpBefore) {
            CorpVO vo = new CorpVO();
            BeanUtils.copyNotNullProperties(corp, vo);
            if (ObjectUtil.isNotEmpty(vo.getParentId())) {
                String corpName = corpNameMap.get(vo.getParentId());
                vo.setParentName(corpName);
            }
            corpVoMap.put(vo.getName(), vo);
        }
        List<Corp> corps = this.listAll();
        Map<String, Corp> corpMap = Maps.newHashMap();
        for (Corp corp : corps) {
            corpMap.put(corp.getName(), corp);
        }
        List<Corp> corpList1 = new ArrayList<>();
        for (CorpVO vo : list) {
            Corp corp = corpMap.get(vo.getName());
            if (!EmptyUtils.isEmpty(vo.getUserAccountCodeName())) {
                corp.setUserAccountCode(corp.getId());
            }
            if (EmptyUtils.isBlank(vo.getParentName())) {
                corp.setParentId("");
                corp.setParentIds(corp.getId() + CommonConstant.COMMA_SPLIT);
                corpList1.add(corp);
                continue;
            }
            Corp parentCorp1 = corpMap.get(vo.getParentName());
            if(!ObjectUtils.isEmpty(parentCorp1)) {
            	corp.setParentId(parentCorp1.getId());
            }
            if (EmptyUtils.isBlank(corp.getNetId())) {
                corp.setNetId(getNetId(corpMap, corpVoMap, vo.getParentName()));
            }
            corp.setParentIds(getParentIds(corp.getName(), null, corpMap, corpVoMap));
            //新增网点，初始化网点日结日期
            if (CORP_TYPE_CODE_NET.equals(corp.getType())) {
                // 网点的初始化，初始化完成后
                InitNetDayCloseMessageDTO messageDto = InitNetDayCloseMessageDTO.builder().netId(corp.getId()).userId(userId).build();
                messageDto.setMsgType(InitNetDayCloseMessageDTO.Msg.N);
                initNetDayCloseMQProducer.sendMessage(messageDto);
//                fcDayCloseServiceImpl.initNetDayClose(corp.getId(), userId);
            }
            corpList1.add(corp);
        }
        getLockAndCheck(userId, corpList1.size(), true);
    }

    public void batchSaveOrUpdate(List<Corp> corpList1){
        batchSaveOrUpdate(corpList1);
    }
    /**
     * 导入保存(以前的)
     *
     * @param list
     * @param user
     */
    private void saveImport2(List<CorpVO> list, ICurrentUser user) {
        String userId = user.getId();
        Date curDate = DateUtils.getDate();
        // 单位名称集合，用来校验单位是否一致，上级单位是否一起导入
        Map<String, Corp> corpNameMap = new HashMap<>();
        // 机构代码集合，用来校验单位是否一致，所用帐号单位代码是否一起导入
        Map<String, Corp> corpCodeMap = new HashMap<>();
        for (CorpVO vo : list) {
            Corp corp = new Corp();
            BeanUtils.copyNotNullProperties(vo, corp);
            if (EmptyUtils.isEmpty(corp.getStatus())) {
                corp.setStatus(1);
            }
            if (EmptyUtils.isEmpty(corp.getListedCompany())) {
                corp.setListedCompany(1);
            }
            corp.setCreateBy(userId);
            corp.setCreateTime(curDate);
            saveEntity(corp);
            if (EmptyUtils.isBlank(vo.getParentId()) && !EmptyUtils.isBlank(vo.getParentName())) {
                Corp parentCorp = corpNameMap.get(vo.getParentName());
                if (!EmptyUtils.isEmpty(parentCorp)) {
                    corp.setParentId(parentCorp.getId());
                    corp.setNetId(parentCorp.getId());
                }
            } else {
                Corp parentNet = getParentNetByParentCorpId(vo.getParentId());
                if (!EmptyUtils.isEmpty(parentNet)) {
                    corp.setNetId(parentNet.getId());
                }
            }
            if (EmptyUtils.isBlank(vo.getUserAccountCode()) && !EmptyUtils.isBlank(vo.getUserAccountCodeName())) {
                if (vo.getCode().equalsIgnoreCase(vo.getUserAccountCodeName())) {
                    corp.setUserAccountCode(corp.getId());
                } else {
                    Corp userAccountCrop = corpCodeMap.get(vo.getUserAccountCodeName());
                    if (!EmptyUtils.isEmpty(userAccountCrop)) {
                        corp.setUserAccountCode(userAccountCrop.getId());
                    }
                }
            }
            if (EmptyUtils.isBlank(corp.getNetId())) {
                corp.setNetId(corp.getId());
            }
            if (EmptyUtils.isBlank(corp.getUserAccountCode())) {
                corp.setUserAccountCode(corp.getId());
            }
            //设置单位或网点的全路径，by zhoul 2018-05-11
            corp.setParentIds(getCorpRouter(corp.getId(), null));
            updateEntity(corp);
            //新增网点，初始化网点日结日期
            if (CORP_TYPE_CODE_NET.equals(corp.getType())) {
                InitNetDayCloseMessageDTO messageDto = InitNetDayCloseMessageDTO.builder().netId(corp.getId()).userId(userId).build();
                messageDto.setMsgType(InitNetDayCloseMessageDTO.Msg.N);
                initNetDayCloseMQProducer.sendMessage(messageDto);
//                fcDayCloseServiceImpl.initNetDayClose(corp.getId(), userId);
            }
            corpNameMap.put(corp.getName(), corp);
            corpCodeMap.put(corp.getCode(), corp);
        }
    }
    public String getNetId(Map<String, Corp> nameMap, Map<String, CorpVO> importMap, String parentName) {
        if (nameMap.containsKey(parentName)) {
            Corp corp = nameMap.get(parentName);
            if (ObjectUtil.equal(0, corp.getType())) {
                return corp.getId();
            } else {
                CorpVO corpImportVo = importMap.get(parentName);
                return getNetId(nameMap, importMap, corpImportVo.getParentName());
            }
        }
        return "";
    }
    /**
     * 根据用户ID获取用户拥有的单位树
     *
     * @param userId 用户ID
     * @return 单位树
     */
    public List<CorpVO> listCorpTreeByUserId(String userId) {
        List<String> corpIds = corpUserResourcesService.listCorpIdByUserId(userId);
        List<Corp> corpList = corpDao.listCorpByCorpIds(corpIds);
        if (CollectionUtils.isEmpty(corpList)) {
            return new ArrayList<>();
        }
        return transformToTree(corpList);
    }
    /**
     * 获取传入单位对应资源的用户ID
     * <br>
     * <b>作者：</b>周亮<br>
     * <b>日期：</b>2017年6月20日<br>
     *
     * @param resource
     * @param userCorpId
     * @return 用户ID
     */
    public List<String> getUserIdsByCorpId(String resource, String userCorpId) {
        List<String> list = this.corpDao.getUserIdsByCorpId(resource, userCorpId);
        return distinctList(list);
    }
    /**
     * 查询用户拥有权限的成员单位列表
     *
     * @param userId 用户ID
     * @return 成员单位列表
     */
    public List<Map> getCorpListByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        return corpDao.getCorpListByUserId(userId);
    }
    /**
     * 查询用户拥有权限的成员单位列表
     *
     * @param resource
     * @param userId
     * @return
     */
    public List<Map> getCorpListByUserId(String resource, String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        // 重复单位去重
        List<Map> corpList = corpDao.getUserCorpList(resource, userId);
        List<Map> resultList = new ArrayList<>(corpList.size());
        Set<String> corpIdSet = new HashSet<>(corpList.size());
        String corpId;
        for (Map map : corpList) {
            corpId = String.valueOf(map.get("id"));
            if (!corpIdSet.contains(corpId)) {
                corpIdSet.add(corpId);
                resultList.add(map);
            }
        }
        return resultList;
    }
    /**
     * 根据单位获取单位上级变更校验异常数据
     *
     * @param corp
     * @return 返回单位异常集合信息
     */
    public List<Map<String, Object>> getCorpVerifyErrorInfo(Corp corp, String type) {
        List<Map<String, Object>> verifyErrorList = new ArrayList<>();
        if (CorpConstant.CORP_VERIFY_PARENTCHANG.equals(type)) {
            //如果是上级结构变化场景需要验证他的下级单位信息
            getSonCorpVerifyErrorInfo(corp, verifyErrorList);
        } else {
            List<CorpVerifyErrorDTO> corpVerifyErrorDTOList = getCorpVerifyErrorInfo(corp.getId(), type);
            if (ListUtils.isNotNullOrEmpty(corpVerifyErrorDTOList)) {
                Map<String, Object> verifyErrorMap = new HashMap<>();
                verifyErrorMap.put("corpCode", corp.getCode());
                verifyErrorMap.put("corpName", corp.getName());
                initVerifyErrorMap(verifyErrorMap, corpVerifyErrorDTOList);
                verifyErrorList.add(verifyErrorMap);
            }
        }
        return verifyErrorList;
    }
    /**
     * 设置单位下级异常单位列表集合
     *
     * @param corp            单位信息
     * @param verifyErrorList 验证异常单位列表集合
     */
    public void getSonCorpVerifyErrorInfo(Corp corp, List<Map<String, Object>> verifyErrorList) {
        if (corp != null && StringUtils.isNotEmpty(corp.getId())) {
            List<CorpVerifyErrorDTO> corpVerifyErrorDTOList = getCorpVerifyErrorInfo(corp.getId(), CorpConstant.CORP_VERIFY_PARENTCHANG);
            if (ListUtils.isNotNullOrEmpty(corpVerifyErrorDTOList)) {
                Map<String, Object> verifyErrorMap = new HashMap<>();
                verifyErrorMap.put("corpCode", corp.getCode());
                verifyErrorMap.put("corpName", corp.getName());
                initVerifyErrorMap(verifyErrorMap, corpVerifyErrorDTOList);
                verifyErrorList.add(verifyErrorMap);
            }
            List<Corp> listSonCorp = listCorpByParentId(corp.getId());
            if (ListUtils.isNotNullOrEmpty(listSonCorp)) {
                for (Corp sonCorp : listSonCorp) {
                    getSonCorpVerifyErrorInfo(sonCorp, verifyErrorList);
                }
            }
        }
    }
    /**
     * 根据业务异常集合封装异常信息列表
     *
     * @param verifyErrorMap         显示异常信息Map
     * @param corpVerifyErrorDTOList 业务异常集合
     */
    public void initVerifyErrorMap(Map<String, Object> verifyErrorMap, List<CorpVerifyErrorDTO> corpVerifyErrorDTOList) {
        Map<String, Object> sysMap = new HashMap<>();
        Map<String, Object> sysBusMap = new HashMap<>();
        List<Map<String, Object>> errorInfoList = new ArrayList<>();
        String key = null;
        for (CorpVerifyErrorDTO errorDTO : corpVerifyErrorDTOList) {
            key = errorDTO.getSysCode() + "_" + errorDTO.getBusCode();
            //设置系统信息
            if (sysMap.containsKey(errorDTO.getSysCode())) {
                Map<String, Object> sysInfo = (Map<String, Object>) sysMap.get(errorDTO.getSysCode());
                int size = (Integer) sysInfo.get("size");
                sysInfo.put("size", ++size);
            } else {
                Map<String, Object> sysInfo = new HashMap<>();
                sysInfo.put("size", 1);
                sysInfo.put("sysCode", errorDTO.getSysCode());
                if (SysConfigConstant.MODULETOMENUID.containsKey(errorDTO.getSysCode())) {
                    String sysName = i18nServiceI.getBaseContext(SysConfigConstant.MODULETOMENUID.get(errorDTO.getSysCode()));
                    sysInfo.put("sysName", sysName);
                } else {
                    sysInfo.put("sysName", multiLangServiceI.getLang(errorDTO.getSysCode()));
                }
                List<Map<String, Object>> busList = new ArrayList<>();
                sysInfo.put("busList", busList);
                sysMap.put(errorDTO.getSysCode(), sysInfo);
                errorInfoList.add(sysInfo);
            }
            //设置业务信息和异常消息
            if (sysBusMap.containsKey(key)) {
                Map<String, Object> errorInfo = (Map<String, Object>) sysBusMap.get(key);
                int size = (Integer) errorInfo.get("size");
                errorInfo.put("size", ++size);
                List<String> errorMsgs = (List<String>) errorInfo.get("errorMsgs");
                errorMsgs.add(multiLangServiceI.getLang(errorDTO.getErrorMsg()));
            } else {
                Map<String, Object> errorInfo = new HashMap<>();
                errorInfo.put("size", 1);
                errorInfo.put("busCode", errorDTO.getBusCode());
                if (SysConfigConstant.BUSTYPETOMENUID.containsKey(errorDTO.getBusCode())) {
                    String busName = i18nServiceI.getBaseContext(SysConfigConstant.BUSTYPETOMENUID.get(errorDTO.getBusCode()));
                    errorInfo.put("busName", busName);
                } else {
                    errorInfo.put("busName", multiLangServiceI.getLang(errorDTO.getBusCode()));
                }
                List<String> errorMsgs = new ArrayList<>();
                errorMsgs.add(multiLangServiceI.getLang(errorDTO.getErrorMsg()));
                errorInfo.put("errorMsgs", errorMsgs);
                sysBusMap.put(key, errorInfo);
                Map<String, Object> sysInfo = (Map<String, Object>) sysMap.get(errorDTO.getSysCode());
                List<Map<String, Object>> busList = (List<Map<String, Object>>) sysInfo.get("busList");
                busList.add(errorInfo);
            }
        }
        verifyErrorMap.put("sysList", errorInfoList);
    }
    /**
     * 根据单位Id获取校验异常数据
     *
     * @param corpId 单位id
     * @param type   校验业务类型 CorpConstant.CORP_VERIFY_DELETE 删除、CorpConstant.CORP_VERIFY_DISABLE 禁用、CorpConstant.CORP_VERIFY_PARENTCHANG 变更上级单位
     * @return 异常数据VO集合
     */
    public List<CorpVerifyErrorDTO> getCorpVerifyErrorInfo(String corpId, String type) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOList = new ArrayList<>();
        if (CorpConstant.CORP_VERIFY_DELETE.equals(type)) {
            //删除业务校验
            if (corpDeleteVerifyServiceList != null) {
                for (ICorpDeleteVerifyService verifyService : corpDeleteVerifyServiceList) {
                    if (verifyService != null) {
                        List<CorpVerifyErrorDTO> infoList = verifyService.verifyCorpDelete(corpId);
                        if (ListUtils.isNotNullOrEmpty(infoList)) {
                            corpVerifyErrorDTOList.addAll(infoList);
                        }
                    }
                }
            }
        } else if (CorpConstant.CORP_VERIFY_DISABLE.equals(type)) {
            //禁用业务校验
            if (corpDisableVerifyServiceList != null) {
                for (ICorpDisableVerifyService verifyService : corpDisableVerifyServiceList) {
                    if (verifyService != null) {
                        List<CorpVerifyErrorDTO> infoList = verifyService.verifyCorpDisable(corpId);
                        if (ListUtils.isNotNullOrEmpty(infoList)) {
                            corpVerifyErrorDTOList.addAll(infoList);
                        }
                    }
                }
            }
        } else if (CorpConstant.CORP_VERIFY_PARENTCHANG.equals(type)) {
            //上级单位变更业务校验
            if (corpParentChangVerifyServiceList != null) {
                for (ICorpParentChangVerifyService verifyService : corpParentChangVerifyServiceList) {
                    if (verifyService != null) {
                        List<CorpVerifyErrorDTO> infoList = verifyService.verifyCorpParentChang(corpId);
                        if (ListUtils.isNotNullOrEmpty(infoList)) {
                            corpVerifyErrorDTOList.addAll(infoList);
                        }
                    }
                }
            }
        }
        return corpVerifyErrorDTOList;
    }
    /**
     * 获取资源下的单位ID（添加所用账号单位）
     *
     * @param resource 资源
     * @param userId   用户id
     * @return
     * @author zhenghj 2022-5-31
     */
    public List<String> getUserCorpIdsByUseAccount(String resource, String userId) {
        // 获取资源下的单位ID
        List<String> userCorpIds = new ArrayList<>();
        if (!EmptyUtils.isBlank(resource)) {
            userCorpIds = this.getUserCorpIds(resource, userId);
            if (!EmptyUtils.isEmptyList(userCorpIds)) {
                // 获取单位下的所用账号单位
                List<String> userAccountCorpList = this.listUserAccountCorpByCorpId(userCorpIds);
                if (!EmptyUtils.isEmptyList(userAccountCorpList)) {
                    userCorpIds.addAll(userAccountCorpList);
                    //去重
                    HashSet h = new HashSet(userCorpIds);
                    userCorpIds.clear();
                    userCorpIds.addAll(h);
                }
            }
        }
        return userCorpIds;
    }
    /**
     * 获取有效单位Id集合
     * @return
     */
    public List<String> getCorpIds() {
        return corpDao.getCorpIds();
    }
    /**
     * 根据查询条件(单位名称 or 单位编码)获取有效的单位Id集合
     * @param seach
     * @return
     */
    public List<String> listCorpIdsBySeach(String seach) {
        return corpDao.listCorpIdsBySeach(seach);
    }
    /**
     * 根据查询条件(单位名称 or 单位编码)获取有效的单位信息集合
     * @param seach
     * @return
     */
    public List<CorpDTO> listCorpBySeach(String seach) {
        return corpDao.listCorpBySeach(seach);
    }
    /**
     * 根据网点Id获取单位信息集合
     * @param netId
     * @return
     */
    public List<CorpDTO> getCorpByNetId(String netId) {
        return corpDao.getCorpByNetId(netId);
    }
    /**
     * 根据Id集合获取单位信息（包含下级单位数量）
     * @param corpIds 单位Id
     * @return
     */
    public List<CorpDTO> getCorpByIdsHadChildCount(List<String> corpIds) {
        return corpDao.getCorpByIdsHadChildCount(corpIds);
    }
    /**
     * 根据上级单位Id获取子单位信息
     * @param parentId 上级单位Id
     * @return
     */
    public List<CorpDTO> getCorpByParentId(String parentId) {
        return corpDao.getCorpByParentId(parentId);
    }
    /**
     * 根据上级单位Id集合获取子单位信息
     * @param parentIds 上级单位Id
     * @return
     */
    public List<CorpDTO> getCorpByParentIds(List<String> parentIds) {
        return corpDao.getCorpByParentIds(parentIds);
    }
    /**
     * 根据 parentId 查询corpId集合
     * @param parentId 上级单位Id
     * @return
     */
    public List<String> getCorpIdByParentId(String parentId) {
        return corpDao.getCorpIdByParentId(parentId);
    }
    public Map<String, String> getNameByIds(List<String> corpIds) {
        List<CorpDTO> lists = corpDao.getNameByIds(corpIds);
        return lists.stream().collect(Collectors.toMap(CorpDTO::getId,CorpDTO::getName));
    }

    public Map<String, String> getNameByNetIds(List<String> netIds) {
        List<CorpDTO> lists = corpDao.getNameByNetIds(netIds);
        return lists.stream().collect(Collectors.toMap(CorpDTO::getNetId,CorpDTO::getName,(a,b)->a));
    }


    public Map<String, String> getNameByTypeAndIds(List<String> corpIds, Integer type) {
        List<CorpDTO> lists = corpDao.getNameByTypeAndIds(corpIds, type);
        return lists.stream().collect(Collectors.toMap(CorpDTO::getId,CorpDTO::getName));
    }
    public List<CorpDTO> listCorpDTOByCorpIds(List<String> corpIds) {
        List<CorpDTO> list = corpDao.listCorpDTOByCorpIds(corpIds);
        if (ListUtils.isNotNullOrEmpty(list)) {
            list.forEach(corp -> {
                if (ObjectUtil.isNotEmpty(corp.getUnitCm())) {
                    CellManage cellManage = cellManageService.get(corp.getUnitCm(),true);
                    if (cellManage != null) {
                        corp.setUnitCmCode(cellManage.getCellCode());
                        corp.setUnitCmName(cellManage.getCellName());
                    }
                }
            });
        }
        return list;
    }
    public List listBySQL(String sql, final Map<String, ?> params){
        return corpDao.listBySQL(sql.toString(), null);
    }
    public <B> List<B> listBySQLAliasToBean(String sql, final Map<String, ?> params, final Class<B> clazz) {
        return corpDao.listBySQLAliasToBean(sql.toString(), null, clazz);
    }
    public List<ErpBasicDataVOVO> listErpBasicDataVOVO(String sql) {
        return corpDao.listErpBasicDataVOVO(sql.toString());
    }
    public List<String> getCorpIdsByType(String type){
        return corpDao.getCorpIdsByType(type);
    }

    public List<CorpDTO> getCorpNetList(){
        return corpDao.getCorpNetList();
    }

    /**
     *
     * @param id
     * @param type 1-operationArea_ID;2-equityMaintenance_ID;3-unitStoreMaintenance_ID;
     * @return
     */
    public CorpDTO queryByAreaIdOrEquityIdOrStoreId(String id, String type){
        CorpDTO dto = new CorpDTO();
        Corp corp = corpDao.queryByAreaIdOrEquityIdOrStoreId(id, type);
        BeanUtils.copyNotNullProperties(corp,dto);
        return dto;
    }

    public List<CorpDTO> listByAreaIdOrEquityIdOrStoreId(String id, String type){
        List<Corp> corps = corpDao.listByAreaIdOrEquityIdOrStoreId(id, type);
        if(CollectionUtils.isEmpty(corps)){
            return null;
        }
        List<CorpDTO> corpDTOS = BeanUtils.copyListProperties(corps, CorpDTO.class);
        return corpDTOS;
    }

    public List<CorpDTO> queryCorpDTOByCodes(List<String> codes) {
        return corpDao.queryCorpDTOByCodes(codes);
    }

    public List<CorpDTO> getFilterBySource(String corpVOList, List<String> sourceCorpIds) {
        corpVOList = corpVOList.replaceAll("&quot;", "\"");
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(corpVOList);
        List<String> corpIds = new ArrayList<>();
        getCorpIds(corpIds, jsonNode);

        String corpAuthSql;
        List<String> resultCorpIds = new ArrayList<>();
        if(!EmptyUtils.isEmpty(sourceCorpIds)){
            for(String sourceCorpId:sourceCorpIds){
                if(corpIds.contains(sourceCorpId)){
                    resultCorpIds.add(sourceCorpId);
                }
            }
            corpAuthSql = SqlUtil.getInSql(resultCorpIds, "c.id", true);
        }else{
            corpAuthSql = SqlUtil.getInSql(corpIds, "c.id", true);
        }

        CorpComponentVO corpComponentVO = new CorpComponentVO();
        List<Corp> corpList = corpDao.getFilterCorp(corpAuthSql, corpComponentVO);
        List<CorpVO> corpVOS = transformToTree(corpList);
        dealName(corpVOS);
        return BeanUtils.copyListProperties(corpVOS, CorpDTO.class);
    }

    public void getCorpIds(List<String> corpIds, JsonNode jsonNode){
        if(!EmptyUtils.isEmpty(jsonNode)){
            for(JsonNode jsonNode1:jsonNode){
                corpIds.add(jsonNode1.get("id").asText());
                JsonNode childrenJsonNode = jsonNode1.get("children");
                getCorpIds(corpIds, childrenJsonNode);
            }
        }
    }

    public void dealName(List<CorpVO> corpVOS){
        if(!EmptyUtils.isEmpty(corpVOS)){
            for(CorpVO corpVO:corpVOS){
                if(!EmptyUtils.isEmpty(corpVO)){
                    corpVO.setName("("+corpVO.getCode()+")"+corpVO.getName());
                    List<CorpVO> childrenCorpVOS = corpVO.getChildren();
                    dealName(childrenCorpVOS);
                }
            }
        }
    }

    public List<CorpVO> listCorpByModuleId(String moduleId) {
//        OrgantypeModule organtypeModule = organtypeModuleService.getById(moduleId);
//        if(organtypeModule==null){
//            return Lists.newArrayList();
//        }
        List<Corp> corps = new ArrayList<>();
        if (StringUtils.isEmpty(moduleId)) {//查询基础组织架构的单位
            corps = corpDao.listEffectiveCorp(null);
        } else { //查询其他组织架构的单位

            //查询最新发布的关联单位而且是有效期内的
//            OrgantypeRelease organtypeReleaseEffective = null;
//            List<OrgantypeRelease> organtypeReleaseList = organtypeReleaseService.getByOrgId(organtypeModule.getOrgtId());
//            for(OrgantypeRelease organtypeRelease : organtypeReleaseList){
//                Date effectiveDate = organtypeRelease.getEffectiveDate();
//                if(effectiveDate!=null && new Date().after(effectiveDate)){
//                    organtypeReleaseEffective = organtypeRelease;
//                    break;
//                }
//            }
//            if(organtypeReleaseEffective!=null){
//                List<OrgantypeCorpRelease> releaseCorpList = organtypeCorpReleaseService.listByReleaseId(organtypeReleaseEffective.getId());
//                List<String> corpIdList = new ArrayList<>();
//                if(CollectionUtils.isNotEmpty(releaseCorpList)){
//                    corpIdList = releaseCorpList.stream().map(item->item.getCorpId()).collect(Collectors.toList());
//                    corps = corpDao.listCorpByCorpId(corpIdList);
//                    List<CorpManage> corpManages = corpMangeDao.listCorpByCorpIds(corpIdList);
//                    corps.addAll(BeanUtils.copyListProperties(corpManages, Corp.class));
//
//                    //将SYS_ORGANTYPE_CORP的层级关系带到sys_corp中。目的是页面展示层级关系
//                    corps = copyParent(releaseCorpList, corps);
//                }
//            }

        }
        return transformToTree(corps);
    }

    public List<CorpVO> listByCodes(List<String> codes) {
        List<Corp> list = corpDao.listByCodes(codes);
        return BeanUtils.copyListProperties(list, CorpVO.class);
    }

    /**
     * 根据传入的id或者ids获取所有子级单位信息
     * @param ids
     * @param includeThisCorp 是否包含当前单位，默认为true
     * @return TODO
     */
    public List<CorpVO> listSubCorp(List<String> ids,boolean includeThisCorp) {
        List<Corp> list = corpDao.listSubCorp(ids,includeThisCorp);
        return BeanUtils.copyListProperties(list, CorpVO.class);
    }

    /**
     * 根据传入的id或者ids获取所有父级单位信息
     * @param ids
     * @param includeThisCorp 是否包含当前单位，默认为true
     * @return TODO
     */
    public List<CorpVO> listParentCorp(List<String> ids, boolean includeThisCorp) {
        List<Corp> list = corpDao.listParentCorp(ids,includeThisCorp);
        return BeanUtils.copyListProperties(list, CorpVO.class);
    }

    /**
     * 单位组件-
     * 获取过滤权限资源的结构
     *
     * @param corpComponentVO 单位组件查询VO对象
     * @param currentUser     用户
     * @return 集合数据
     */
    public List<Corp> getFilterCorpW(CorpComponentVO corpComponentVO, ICurrentUser currentUser) {
        StopWatch stopWatch=new StopWatch();
        stopWatch.start("getFilterCorp getCorpIdsByUserResource spends:");
        long startTime = System.currentTimeMillis();
        // 获取传入用户对应资源的单位或网点id
        //List<String> corpIds = getUserCorpIds(corpComponentVO.getResource(), currentUser.getId());
        List<String> corpIds = getCorpIdsByUserResource(corpComponentVO.getResource(), currentUser.getId());
        //判断是否加载所属账号单位
        if("accCorp".equals(corpComponentVO.getType())){
            return this.listUserAccountTreeByCorpIdW(null,corpIds);
        }else{
            stopWatch.stop();
            stopWatch.start("getFilterCorp getFilterCorp spends:");
            logger.info("getUserCorpIds：" + (System.currentTimeMillis() - startTime));
            // 组装In SQL
            String corpAuthSql = SqlUtil.getInSql(corpIds, "c.id", true);
            long startTime2 = System.currentTimeMillis();
            // 查出符合条件的单位
            List<Corp> corpList = corpDao.getFilterCorp(corpAuthSql, corpComponentVO);
            logger.info("getFilterCorp：" + (System.currentTimeMillis() - startTime2));
            return corpList;
        }
    }

    /**
     * 所用账号单位组件，组装单位树
     *
     * @param corpId    单位id
     * @return
     */
    public List<Corp> listUserAccountTreeByCorpIdW(String corpId,List<String> corpIds) {
        List<String> corpIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(corpId)){
            corpIdList.addAll(Arrays.asList(corpId.split(",")));
        }
        if(!CollectionUtils.isEmpty(corpIds)){
            corpIdList.addAll(corpIds);
        }
        List<Corp> corpIdlist = corpDao.listEffectiveCorp(corpIdList);
        List<String> corpAuth = new ArrayList<>();
        List<Corp> corpList = new ArrayList<>();
        for(Corp corp: corpIdlist){
            corpList.add(corp);
            if(StringUtils.isNotEmpty(corp.getUserAccountCode())){
                corpAuth.addAll(Arrays.asList(corp.getUserAccountCode().split(CommonConstant.COMMA_SPLIT)));
                if(corpAuth.contains(corpId)){
                    corpAuth.remove(corpId);
                }
            }
        }

        List<Corp> list = corpDao.listEffectiveCorp(corpAuth);
        if (!EmptyUtils.isEmptyList(list)) {
            corpList.addAll(list);
        }
        HashSet h = new HashSet(corpList);
        corpList.clear();
        corpList.addAll(h);
        return corpList;
    }


    /**
     * 客商组件 单位网点数据信息
     * @param input
     * @return
     */
    public DataTablesOutput getAllCorpData(DataTablesInput input) {
        DataTablesOutput allCorpData = corpDao.getAllCorpData(input);
        if(CollectionUtil.isNotEmpty(allCorpData.getData())){
            List<String> netIds = new ArrayList<>();
            for (Object data : allCorpData.getData()) {
                CorpVO corpVO = (CorpVO) data;
                if (StringUtils.isNotBlank(corpVO.getNetId())) {
                    netIds.add(corpVO.getNetId());
                }
            }
            // 字典缓存
            Map<String, DictDTO> unitAttributeMap = dictServiceI.listMap(CorpConstant.CORP_ATTRIBUTE_TYPE_DICT);
            // 所属网点
            Map<String, String> netNameMap = netIds.isEmpty()
                    ? Collections.emptyMap()
                    : getNameByIds(netIds);
            for (Object data : allCorpData.getData()) {
                CorpVO corpVO = (CorpVO) data;
                if (StringUtils.isNotBlank(corpVO.getUnitAttribute())
                        && unitAttributeMap.containsKey(corpVO.getUnitAttribute())) {
                    corpVO.setUnitAttributeName(unitAttributeMap.get(corpVO.getUnitAttribute()).getName());
                }
                if (StringUtils.isNotBlank(corpVO.getNetId()) && netNameMap.containsKey(corpVO.getNetId())) {
                    corpVO.setNetName(netNameMap.get(corpVO.getNetId()));
                }
            }
        }
        return allCorpData;
    }

    public String getEffectiveOrgIdByModuld(String moduleCode) {
        OrgantypeModuleDTO moduleDTO = organtypeModuleService.getBySysCode(moduleCode);
        if(moduleDTO==null){
            return "";
        }
        //查询最新发布的关联单位而且是有效期内的
        OrgantypeRelease organtypeReleaseEffective = null;
        List<OrgantypeRelease> organtypeReleaseList = organtypeReleaseService.getByOrgId(moduleDTO.getOrgtId());
        for (OrgantypeRelease organtypeRelease : organtypeReleaseList) {
            Date effectiveDate = organtypeRelease.getEffectiveDate();
            if (effectiveDate != null && new Date().after(effectiveDate)) {
                organtypeReleaseEffective = organtypeRelease;
                break;
            }
        }
        return organtypeReleaseEffective.getId();
    }


}
