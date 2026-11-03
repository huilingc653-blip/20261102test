package com.bytter.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.basic.service.BtInputBankInfoService;
import com.bytter.basic.service.BtSwiftInfoService;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.basic.vo.BtSwiftInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.SysBlackWhiteConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.ExternalCorpMessageEnum;
import com.bytter.system.constants.ExternalCorpConstants;
import com.bytter.system.dao.ExternalCorpDao;
import com.bytter.system.entity.*;
import com.bytter.system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 外部单位/客商
 *
 * @author zhoul on 2017/6/21.
 */
@Service("externalCorpService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExternalCorpService extends BaseService<ExternalCorp, String> implements IExcelVerifyHandler<ExternalCorpInfoImportVO> {
    @Autowired
    private ExternalCorpDao externalCorpDao;
    @Autowired
    private ExternalCorpBankACCService externalCorpBankACCService;
    @Autowired
    private ExternalCorpQuickPayService externalCorpQuickPayService;
    @Autowired
    private IRegionService regionService;
    @Autowired
    private DictService dictService;
    @Autowired
    private IDictService dictServiceI;
    @Autowired
    private ConfigService configServiceI;
    @Autowired
    private BtInputBankInfoService btInputBankInfoService;
    @Autowired
    private ExternalCorpAndCorpService externalCorpAndCorpService;
    @Autowired
    private ExternalCorpAuditLogService externalCorpAuditLogService;
    @Autowired
    private SupplierTypeService supplierTypeService;
    @Autowired
    private ISysCountryCustService sysCountryCustService;
    @Autowired
    private BtSwiftInfoService btSwiftInfoService;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IBlackWhiteService blackWhiteService;
    @Override
    public BaseDao<ExternalCorp, String> getEntityDao() {
        return externalCorpDao;
    }
    @Override
    @Transactional(readOnly = true)
    public ExternalCorp get(String id) {
        return externalCorpDao.get(id, true);
    }
    public void save(ExternalCorpVO externalCorp, ICurrentUser user) {
        //提交审核
        if (CommonConstant.STATUE_WAIT_CHECK.equals(externalCorp.getAuditStatus())) {
            //判断客商是否自动复核通过，是的话就变成复核通过，不然就是待复核
            boolean autoAudit = isAutoAudit();
            externalCorp.setAuditStatus(autoAudit ? CommonConstant.STATUE_CHECK : CommonConstant.STATUE_WAIT_CHECK);
            externalCorp.setAuditMessage(autoAudit ? ExternalCorpConstants.AUTO_AUDIT_PASS : null);
        }
        if (isSave(externalCorp)) {
            ExternalCorp ec = new ExternalCorp();
            BeanUtils.copyNotNullProperties(externalCorp, ec);
            if(ec.getCode().isEmpty()){
                ec.setCode(null);
            }
            if (StringUtils.isAllBlank(externalCorp.getCommitBy()) && StringUtils.isNotBlank(user.getId())) {
                externalCorp.setCommitBy(user.getId());
            }
            saveEntity(ec);
            //审核日志，提交的时候
            boolean needLog = CommonConstant.STATUE_WAIT_CHECK.equals(externalCorp.getAuditStatus()) || CommonConstant.STATUE_CHECK.equals(externalCorp.getAuditStatus());
            if (needLog) {
                ExternalCorpAuditLog externalCorpAuditLog = new ExternalCorpAuditLog(ec.getId(), CommonConstant.STATUE_WAIT_CHECK, externalCorp.getAuditMessage(), user);
                externalCorpAuditLogService.saveEntity(externalCorpAuditLog);
            }
            // 客商资料保存后,保存客商与单位关联关系表
            saveOrUpdateExternalCorpBankAcc(ec, externalCorp.getExternalAcc());
            saveOrUpdateExternalCorpQuickPay(ec.getId(), externalCorp.getQuickPayList(), user.getLoginName());
            if (!StringUtils.isBlank(externalCorp.getCorpId())) {
                String[] corpIds = externalCorp.getCorpId().split(",");
                for (String corpId : corpIds) {
                    ExternalCorpAndCorp external = new ExternalCorpAndCorp();
                    external.setExternalCorpId(ec.getId());
                    external.setCorpId(corpId);
                    this.externalCorpAndCorpService.save(external);
                }
            }
            //黑白名单关联
            if (CommonConstant.STATUE_CHECK.equals(externalCorp.getAuditStatus())) {
                saveUpdateBlackWhit(ec, user);
            }
        }
    }
    /**
     * 客商资料自动审核配置
     *
     * @return
     */
    private boolean isAutoAudit() {
        return "1".equals(configServiceI.getByTypeAndName("sys", "externalCorpAutoAudit").getValue());
    }
    /**
     * 批量提交客商资料（变成待复核）
     *
     * @param idList
     */
    public Integer batchSubmit(List<String> idList, ICurrentUser user) {
        /*
        1、除“待复核的都可以提交”（“暂存”或者“打回”，或者是“复核通过”）
        2、判断配置是否是自动审核
        3、更改数据状态（列表提交上来的数据都是已经填了必填数据，可以提交的数据了）
        4、自动审核的话添加审核日志
         */
        if (CollectionUtils.isEmpty(idList)) {
            return 0;
        }
        List<ExternalCorp> externalCorpList = getEntityDao().list(idList);
        List<ExternalCorp> corpToUpdate = new ArrayList<>(externalCorpList.size());
        List<ExternalCorpAuditLog> logList = new ArrayList<>(externalCorpList.size());
        //判断客商是否自动复核通过，是的话就变成复核通过，不然就是待复核
        int successNumber = 0; // 提交成功的条数
        boolean autoAudit = isAutoAudit();
        for (ExternalCorp externalCorp : externalCorpList) {
            if (externalCorp.getAuditStatus().equals(CommonConstant.STATUE_WAIT_CHECK) || externalCorp.getAuditStatus().equals(CommonConstant.STATUE_CHECK)) {
                continue;
            }
            externalCorp.setAuditStatus(autoAudit ? CommonConstant.STATUE_CHECK : CommonConstant.STATUE_WAIT_CHECK);
            externalCorp.setCommitBy(user.getId());
            corpToUpdate.add(externalCorp);
            String auditMessage = autoAudit ? ExternalCorpConstants.AUTO_AUDIT_PASS : null;
            ExternalCorpAuditLog externalCorpAuditLog = new ExternalCorpAuditLog(externalCorp.getId(), CommonConstant.STATUE_WAIT_CHECK, auditMessage, user);
            logList.add(externalCorpAuditLog);
            successNumber++;
            if (CommonConstant.STATUE_CHECK.equals(externalCorp.getAuditStatus())) {
                saveUpdateBlackWhit(externalCorp, user);
            }
        }
        batchUpdate(corpToUpdate);
        externalCorpAuditLogService.batchSave(logList);
        return successNumber;
    }
    public void saveOrUpdateExternalCorpBankAcc(ExternalCorp ec, List<ExternalCorpBankAcc> externalCorpBankAccList) {
        List<ExternalCorpBankAccVO> oldList = this.externalCorpBankACCService.listByExternalCorpId(ec.getId());
        if (!EmptyUtils.isEmptyList(oldList)) {
            this.externalCorpBankACCService.deleteBankAccByExternalCorpId(ec.getId());
        }
        //当客商关联的银行账号全部删除再进行保存时，只进行历史银行账号清空的操作
        if (EmptyUtils.isEmpty(externalCorpBankAccList)) {
            return;
        }
        for (ExternalCorpBankAcc externalCorpBankAcc : externalCorpBankAccList) {
            if (EmptyUtils.isBlank(externalCorpBankAcc.getExternalAcc())) {
                continue;
            }
            if (StringUtils.isBlank(externalCorpBankAcc.getRemark())) {
                externalCorpBankAcc.setRemark("");
            }
            //因数据的来源不同,getIsDefaultAcc的值也不一样,有(on,off)(0,1)(是,否)等
            if (CommonConstant.CHECKBOX_ON.equalsIgnoreCase(externalCorpBankAcc.getIsDefaultAcc())) {
                externalCorpBankAcc.setIsDefaultAcc("1");
            } else if (StringUtils.isBlank(externalCorpBankAcc.getIsDefaultAcc()) || CommonConstant.CHECKBOX_OFF.equalsIgnoreCase(externalCorpBankAcc.getIsDefaultAcc())) {
                externalCorpBankAcc.setIsDefaultAcc("0");
            }
            if (CommonConstant.CHECKBOX_ON.equalsIgnoreCase(externalCorpBankAcc.getIsSignAcc())) {
                externalCorpBankAcc.setIsSignAcc("1");
            } else if (CommonConstant.CHECKBOX_OFF.equalsIgnoreCase(externalCorpBankAcc.getIsSignAcc())) {
                externalCorpBankAcc.setIsSignAcc("0");
            }
            if (StringUtils.isBlank(externalCorpBankAcc.getId())) {
                externalCorpBankAcc.setExternalCorpId(ec.getId());
                this.externalCorpBankACCService.save(externalCorpBankAcc);
            } else {
                this.externalCorpBankACCService.update(externalCorpBankAcc);
            }
        }
    }

    /**
     * 保存或更新客商快捷支付信息
     *
     * @param id 客商实体id
     * @param quickPayList 快捷支付信息集合
     */
    public void saveOrUpdateExternalCorpQuickPay(String id, List<ExternalCorpQuickPay> quickPayList, String userName) {
        List<ExternalCorpQuickPay> oldList = this.externalCorpQuickPayService.listQuickPayByExternalCorpId(id);
        if (!EmptyUtils.isEmptyList(oldList)) {
            this.externalCorpQuickPayService.deleteQuickPayByExternalCorpId(id);
        }

        if (EmptyUtils.isEmpty(quickPayList)) {
            return;
        }
        for (ExternalCorpQuickPay externalCorpQuickPay : quickPayList) {
            if (CommonConstant.CHECKBOX_ON.equalsIgnoreCase(externalCorpQuickPay.getIsDefaultAcc())) {
                externalCorpQuickPay.setIsDefaultAcc("1");
            } else if (StringUtils.isBlank(externalCorpQuickPay.getIsDefaultAcc())
                    || CommonConstant.CHECKBOX_OFF.equalsIgnoreCase(externalCorpQuickPay.getIsDefaultAcc())) {
                externalCorpQuickPay.setIsDefaultAcc("0");
            }
            externalCorpQuickPay.setId(null);
            externalCorpQuickPay.setExternalCorpId(id);
            externalCorpQuickPay.setProId(ExternalCorpConstants.getCurrentTenantQuickPayProductId(externalCorpQuickPay.getProId()));
            externalCorpQuickPay.setCreateBy(userName);
            externalCorpQuickPay.setCreateDate(DateUtils.getDate());
            this.externalCorpQuickPayService.save(externalCorpQuickPay);
        }
    }
    public IPageResult<ExternalCorpBankAccDetailVO> listByExternalDetail(DataTablesInput dataTablesInput) {
        Map<String, Object> params = dataTablesInput.getFormdata();
        //类型：1-国内（默认），0-国外，2-国内外全部
        params.putIfAbsent("fex", "1");
        dataTablesInput.setFormdata(params);


        List<Column> columns= dataTablesInput.getColumns();
        List<Order>  orders= dataTablesInput.getOrder();
        String orderSql = "";
        if(!CollectionUtils.isEmpty(columns)&&!CollectionUtils.isEmpty(orders)){
            Order order=   orders.get(0);
            if(order!=null){
                String column= columns.get(order.getColumn()).getName();
                if(StringUtils.isNotEmpty(column)&&StringUtils.isNotEmpty(order.getDir())){
                    orderSql=" order by "+column+"  "+ order.getDir()+" ";
                }
            }
        }
        IPageResult pageResult=externalCorpDao.listByExternalDetail(dataTablesInput,orderSql);
        ArrayList<ExternalCorpBankAccDetailVO> externalCorpBankAccDetailVOS = new ArrayList<>();
        if (pageResult != null && !CollectionUtils.isEmpty(pageResult.getList())){
            for (Object datum:pageResult.getList()){
                ExternalCorpBankAccDetailVO vo= (ExternalCorpBankAccDetailVO) datum;
//                String externalTypeStr = dictService.getDictByDictGroupAndCode("external.type", vo.getExternalType()).getName();
                String isDefaultAccName=dictService.getDictByDictGroupAndCode("sf_yn",vo.getIsDefaultAcc()).getName();
                if(StringUtils.isNotEmpty(vo.getBwType())){
                    String bwTypeName=dictService.getDictByDictGroupAndCode("SYS_BUS_ENTITY_TYPE",vo.getBwType()).getName();
                    vo.setBwTypeName(bwTypeName);
                }
                vo.setIsDefaultAccName(isDefaultAccName);

                externalCorpBankAccDetailVOS.add(vo);
            }
            pageResult.setList(externalCorpBankAccDetailVOS);
        }
        return pageResult;
    }
    /**
     * 客商更新方法
     *
     * @param externalCorp
     */
    public void update(ExternalCorpVO externalCorp, ICurrentUser user) {
        //判断客商是否自动复核通过，是的话就变成复核通过，不然就是待复核
        boolean autoAudit = isAutoAudit();
        //提交动作
        if (CommonConstant.STATUE_WAIT_CHECK.equals(externalCorp.getAuditStatus())) {
            externalCorp.setAuditStatus(autoAudit ? CommonConstant.STATUE_CHECK : CommonConstant.STATUE_WAIT_CHECK);
            externalCorp.setAuditMessage(autoAudit ? ExternalCorpConstants.AUTO_AUDIT_PASS : null);
            //审核日志
            boolean needLog = CommonConstant.STATUE_WAIT_CHECK.equals(externalCorp.getAuditStatus()) || CommonConstant.STATUE_CHECK.equals(externalCorp.getAuditStatus());
            if (needLog) {
                ExternalCorpAuditLog externalCorpAuditLog = new ExternalCorpAuditLog(externalCorp.getId(), CommonConstant.STATUE_WAIT_CHECK, externalCorp.getAuditMessage(), user);
                externalCorpAuditLogService.saveEntity(externalCorpAuditLog);
            }
        }
        if (isSave(externalCorp)) {
            ExternalCorp dbCorp = externalCorpDao.get(externalCorp.getId());
            BeanUtils.copyNotNullProperties(externalCorp, dbCorp);
            if(dbCorp.getCode().isEmpty()){
                dbCorp.setCode(null);
            }
            externalCorpDao.update(dbCorp);
            saveOrUpdateExternalCorpBankAcc(dbCorp, externalCorp.getExternalAcc());
            saveOrUpdateExternalCorpQuickPay(dbCorp.getId(), externalCorp.getQuickPayList(), user.getLoginName());
            this.externalCorpAndCorpService.deleteExternalCorpAndCorp(externalCorp.getId());
            //所有客户都保存与单位的关系
            saveOrUpdateExternalCorpAndCorp(externalCorp.getCorpId(), externalCorp);
            if (CommonConstant.STATUE_CHECK.equals(dbCorp.getAuditStatus())) {
                saveUpdateBlackWhit(dbCorp, user);
            }
        }
    }
    /**
     * 检查是否可保存
     *
     * @param vo
     * @return
     */
    public boolean isSave(ExternalCorpVO vo) {
        ExternalCorp externalCorpByCode = this.externalCorpDao.getByCorpIdAndCode(vo.getCode());
        if (!EmptyUtils.isEmpty(externalCorpByCode) && !externalCorpByCode.getId().equalsIgnoreCase(vo.getId())) {
            throw new ParameterException(ExternalCorpMessageEnum.SYS_EXTERNAL_CORP_MESSAGE_CODE_EXIST);
        }
        ExternalCorp externalCorpByName = this.externalCorpDao.findByCorpIdAndName(vo.getName());
        if (EmptyUtils.isEmpty(vo.getCode()) && !EmptyUtils.isEmpty(externalCorpByName) && !externalCorpByName.getId().equalsIgnoreCase(vo.getId())) {
            throw new ParameterException(ExternalCorpMessageEnum.SYS_EXTERNAL_CORP_MESSAGE_NAME_EXIST);
        }
//        if (EmptyUtils.isEmptyList(vo.getExternalAcc()) && EmptyUtils.isEmptyList(vo.getExternalAccVO())) {
//            throw new ParameterException(ExternalCorpMessageEnum.SYS_EXTERNAL_CORP_MESSAGE_BANK_NULL);
//        }
        return true;
    }
    /**
     * 根据 客商ID，获取客商的帐号列表
     *
     * @param externalCorpId
     * @return
     */
    public List<ExternalCorpBankAcc> listBankAccByExternalCorpId(String externalCorpId) {
        return externalCorpBankACCService.listBankAccByExternalCorpId(externalCorpId);
    }
    /**
     * 删除客商相关信息
     *
     * @param id 客商ID
     */
    @Override
    public void delete(String id) {
        //先删除与客商相关的帐号信息
        this.externalCorpBankACCService.deleteBankAccByExternalCorpId(id);
        //删除客商快捷支付信息
        this.externalCorpQuickPayService.deleteQuickPayByExternalCorpId(id);
        //删除客商对应的单位关联关系
        this.externalCorpAndCorpService.deleteExternalCorpAndCorp(id);
        //再对客商的资料进行删除
        externalCorpDao.delete(id);
    }
    @Override
    public void delete(List<String> ids) {
        for (String id : ids) {
            this.delete(id);
            externalCorpAuditLogService.deleteByCorpId(id);
            blackWhiteService.deleteBlackWhiteByExternalId(id, null);
        }
    }
    /**
     * 删除客商的帐号
     *
     * @param id 帐号ID
     */
    public void deleteBankAcc(String id) {
        this.externalCorpBankACCService.delete(id);
    }
    /**
     * 根据客商ID查询
     *
     * @param externalCorpId 客商ID
     * @return List<ExternalCorpBankAccVO>
     * @author zuoy 2017/11/04
     */
    public List<ExternalCorpBankAccVO> listByExternalCorpId(String externalCorpId) {
        List<ExternalCorpBankAccVO> externalCorpBankAccVOS = this.externalCorpBankACCService.listByExternalCorpId(externalCorpId);
        if(null != externalCorpBankAccVOS && !externalCorpBankAccVOS.isEmpty()){
            List<String> regionQuery = externalCorpBankAccVOS.stream().map(it -> it.getAccProvince() + "@" + it.getAccCity())
                    .flatMap(it -> Arrays.stream(it.split("@"))).collect(Collectors.toList());
            Map<String, RegionDTO> regionDTOMap = regionService.listByIds(regionQuery);
            externalCorpBankAccVOS.stream().forEach(it -> {
                if (StringUtils.isBlank(it.getAccProvName()) && StringUtils.isNotBlank(it.getAccProvince()) && regionDTOMap.containsKey(it.getAccProvince())){

                    it.setAccProvName(regionDTOMap.get(it.getAccProvince()).getName());
                }
                if (StringUtils.isBlank(it.getAccCityName()) && StringUtils.isNotBlank(it.getAccCity()) && regionDTOMap.containsKey(it.getAccCity())){

                    it.setAccCityName(regionDTOMap.get(it.getAccCity()).getName());
                }
            });
        }
        return externalCorpBankAccVOS;
    }

    public List<ExternalCorpQuickPayVO> listQuickPayByExternalCorpId(String externalCorpId) {
        return this.externalCorpQuickPayService.listByExternalCorpId(externalCorpId);
    }
    /**
     * 根据ID查询
     *
     * @param id
     * @return ExternalCorpVO
     * @author zuoy 2017/11/04
     */
    public ExternalCorpVO getByUnion(String id) {
        ExternalCorpVO vo = this.externalCorpDao.getByUnion(id);
        return initCorp(vo);
    }

    /**
     * 根据客商id获取快捷支付信息
     *
     * @param externalCorpId 客商id
     * @return 按产品分组的快捷支付信息
     */
    public Map<String, List<ExternalCorpQuickPayVO>> getQuickPayInfo(String externalCorpId) {
        if (StringUtils.isNotBlank(externalCorpId)) {
            List<ExternalCorpQuickPayVO> externalCorpQuickPayList = externalCorpQuickPayService.listByExternalCorpId(externalCorpId);
            if (!CollectionUtils.isEmpty(externalCorpQuickPayList)) {
                return externalCorpQuickPayList.stream().collect(Collectors.groupingBy(ExternalCorpQuickPayVO::getProId));
            }
        }
        return null;
    }
    public DataTablesOutput getDataTables(DataTablesInput input, String userId) {
        String resource = SysResourceConstant.SYS_EXTERNAL_CORP_VIEW;
        String corpAuthSql = "";
        String isCommon = (String) input.getFormdata().get("isCommon");
        if (EmptyUtils.isNotEmpty(isCommon) && "0".equals(isCommon)) {
            corpAuthSql = SqlUtil.getInSql(corpServiceI.getUserCorpIds(resource, userId), "ecc.corp_id", true);
        }
        return initVo(externalCorpDao.listExternalCorp(input, corpAuthSql));
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(ExternalCorpInfoImportVO vo) {
        return null;
    }
    @Override
    public void verifyHandler(List<ExternalCorpInfoImportVO> list) {
        final String YES_ZN = "是";
        final String DEFAULT_SOURCE_SYSTEM = "BFS";
        final String DEFAULT_EXTERNAL_TYPE = "客户";
        // 数据源字典
        List<String> sourceSystemDict = dictService.listDictNameByDictGroup("sys.externalCorp.source");
        Map<String, DictDTO> bwTypeDict = dictServiceI.listMap(SysBlackWhiteConstant.EXTERNALCORP_BWTYPE_DICTKEY);
        DictVO defaultSourceSystem = dictService.getDictByDictGroupAndName("sys.externalCorp.source", DEFAULT_SOURCE_SYSTEM);
        Map<String, String> sourceSystemMap = new HashMap<>();
        // 客商类型
        List<String> externalTypeDict = dictService.listDictNameByDictGroup("external.type");
        DictVO defaultExternalType = dictService.getDictByDictGroupAndName("external.type", DEFAULT_EXTERNAL_TYPE);
        Map<String, String> externalTypeMap = new HashMap<>();
        // 是否有效-默认有效
        String defaultStatus = dictService.getDictByDictGroupAndCode("sf_yn", 1).getName();
        // 首选账号-默认否
        String defaultAcc = dictService.getDictByDictGroupAndCode("sf_yn", 0).getName();
        Map<String, String> idMap = new HashMap<>();
        Map<String, Boolean> nameExistsMap = new HashMap<>();
        Map<String, String> nameMap = new HashMap<>();
        Map<String, String> codeMap = new HashMap<>();
        Map<String, String> nameAndCodeAndExternalAccMap = new HashMap<>();
        Map<String, String> corpName = new HashMap<>();

        String countryLevel0="0";
        String countryLevel1="1";
        //获取国家地区信息
        List<SysCountryCustDTO> sysCountryCustList = sysCountryCustService.listAll();
        Map<String, SysCountryCustDTO> sysCountryCustMap = new HashMap<>();
        for (SysCountryCustDTO sysCountryCustDTO : sysCountryCustList) {
            if("0".equals(sysCountryCustDTO.getCountryLevel())){
                sysCountryCustMap.put(sysCountryCustDTO.getNameZh() + countryLevel0,sysCountryCustDTO);
            }else{
                sysCountryCustMap.put(sysCountryCustDTO.getNameZh() + sysCountryCustDTO.getSysId() + countryLevel1,sysCountryCustDTO);
            }
        }

        int num = 0;
        for (ExternalCorpInfoImportVO vo : list) {
            List<String> msgList = new ArrayList<>();
            String bankSourceCode = vo.getBankSourceCode();
            // 校验系统中已存在的客商名称和编码，填写编码时也必须检查名称。
            if (!EmptyUtils.isBlank(vo.getName())) {
                if (!nameExistsMap.containsKey(vo.getName())) {
                    nameExistsMap.put(vo.getName(), this.externalCorpDao.findByCorpIdAndName(vo.getName()) != null);
                }
                if (nameExistsMap.get(vo.getName())) {
                    msgList.add("sys.external.corp.message.name.exist");
                }
                if (!EmptyUtils.isBlank(vo.getCode())) {
                    if (idMap.containsKey(vo.getCode())) {
                        msgList.add("sys.external.corp.message.code.exist");
                    } else {
                        ExternalCorp externalCorpByCode = this.externalCorpDao.getByCorpIdAndCode(vo.getCode());
                        if (null != externalCorpByCode) {
                            idMap.put(vo.getCode(), externalCorpByCode.getId());
                            msgList.add("sys.external.corp.message.code.exist");
                        }
                    }
                }
                // 相同客商的客户名称或者客户编号需一致
                if (nameMap.containsKey(vo.getName())) {
//                    if (null != nameMap.get(vo.getName()) && !nameMap.get(vo.getName()).equalsIgnoreCase(vo.getCode())) {
//                        msgList.add("sys.externalCorp.import.nameAndCode.diff");
//                    }
                } else {
                    nameMap.put(vo.getName(), vo.getCode());
                }
                // 相同客商的客户名称或者客户编号需一致
                if (!EmptyUtils.isBlank(vo.getCode())) {
                    if (codeMap.containsKey(vo.getCode())) {
                        if (!vo.getName().equalsIgnoreCase(codeMap.get(vo.getCode()))) {
                            //msgList.add("sys.externalCorp.import.nameAndCode.diff");
                        }
                    } else {
                        codeMap.put(vo.getCode(), vo.getName());
                    }
                }
            }
            // 导入模版无「是否共享」列，缺省按共享处理
            if (EmptyUtils.isBlank(vo.getIsCommonStr())) {
                vo.setIsCommonStr(YES_ZN);
            }
            // 导入模版的所属单位为必填项，无论是否共享都需要解析单位ID以保存关联关系
            if (EmptyUtils.isBlank(vo.getCorpName())) {
                msgList.add("sys.externalCorp.import.externalCorp.null");
            } else {
                // 且导入时需校验是否在系统中存在；
                if (corpName.containsKey(vo.getCorpName())) {
                    vo.setCorpId(corpName.get(vo.getCorpName()));
                } else {
                    CorpDTO corpIdByName = corpServiceI.getCorpIdByName(vo.getCorpName());
                    if (EmptyUtils.isEmpty(corpIdByName)) {
                        msgList.add("sys.externalCorp.import.externalCorp.not.exists");
                    } else {
                        corpName.put(vo.getCorpName(), corpIdByName.getId());
                        vo.setCorpId(corpIdByName.getId());
                    }
                }
            }
            vo.setIsCommon(YES_ZN.equals(vo.getIsCommonStr()) ? "1" : "0");
            // 是否国内必填
            if (EmptyUtils.isBlank(vo.getIsNativeStr())) {
                msgList.add("sys.externalCorp.import.isNative.null");
            }
            //国外判断国家，城市
            if (StringUtils.isNotBlank(vo.getIsNativeStr()) && "否".equals(vo.getIsNativeStr())) {
                vo.setIsNative(0);

                if(StringUtils.isNotBlank(vo.getProvName()) || StringUtils.isNotBlank(vo.getCityName())){
                    SysCountryCustDTO provinceDTO = sysCountryCustMap.get(vo.getProvName() + countryLevel0);
                    if (EmptyUtils.isEmpty(provinceDTO) || StringUtils.isBlank(provinceDTO.getNameZh()) || CommonConstant.YES.equals(provinceDTO.getCountryLevel())) {
                        msgList.add("sys.externalcorp.import.country.mismatch");
                    } else {
                        vo.setProvince(provinceDTO.getId());
                    }

                    SysCountryCustDTO cityDTO = sysCountryCustMap.get(vo.getCityName() + vo.getProvince() + countryLevel1);
                    if (null == cityDTO || StringUtils.isBlank(cityDTO.getNameZh())) {
                        msgList.add("sys.externalcorp.import.city.mismatch");
                    } else {
                        vo.setCity(cityDTO.getId());
                    }
                }else{
                    msgList.add("sys.externalcorp.import.countryAndcity.null");
                }

                //校验swiftcode
                if(EmptyUtils.isEmpty(vo.getSwiftCode())){
                    msgList.add("sys.externalcorp.message.swiftcode.null");
                }
                //机构名称非空校验
                if(EmptyUtils.isEmpty(vo.getBank())){
                    msgList.add("table.btswiftinfo.bankname.null");
                }
                //账户名称非空校验
                if(EmptyUtils.isEmpty(vo.getExternalAccName())){
                    msgList.add("sys.externalcorp.message.externalAccName.null");
                }
                //校验所在省/州
                if(EmptyUtils.isNotEmpty(vo.getProvinceState()) && !vo.getProvinceState().matches(ExternalCorpConstants.EXTERNALCORP_ENGLISHONLY_REGEX)){
                    msgList.add("sys.externalCorp.provinceState.englishOnly");
                }
                //校验地址1
                if(EmptyUtils.isNotEmpty(vo.getAddress1()) && !vo.getAddress1().matches(ExternalCorpConstants.EXTERNALCORP_ENGLISHONLY_REGEX)){
                    msgList.add("sys.externalCorp.address1.englishOnly");
                }
                //校验地址2
                if(EmptyUtils.isNotEmpty(vo.getAddress2()) && !vo.getAddress2().matches(ExternalCorpConstants.EXTERNALCORP_ENGLISHONLY_REGEX)){
                    msgList.add("sys.externalCorp.address2.englishOnly");
                }
                //校验邮编
                if(EmptyUtils.isNotEmpty(vo.getZipCode()) && !vo.getZipCode().matches(ExternalCorpConstants.EXTERNALCORP_ALPHANUMERIC_REGEX)){
                    msgList.add("sys.externalCorp.zipCode.alphaNumeric");
                }
            } else {
                vo.setIsNative(1);
                //国内判断省市
                // 判断省市在系统中是否存在和是否一致
                if (null != vo.getProvName() && null != vo.getCityName()) {//导入模板里省市不为空时检验,为空则跳过
                    RegionDTO provRegion = this.regionService.getByName(vo.getProvName(), true);
                    RegionDTO cityRegion = this.regionService.getByName(vo.getCityName(), false);
                    if (EmptyUtils.isEmpty(provRegion)) {
                        msgList.add("sys.externalCorp.message.provName.null");
                    } else if (EmptyUtils.isEmpty(cityRegion)) {
                        msgList.add("sys.externalCorp.message.cityName.null");
                    } else if (EmptyUtils.isEmpty(cityRegion.getParentId())) {
                        if (vo.getProvName().equals(vo.getCityName())) {
                            vo.setProvince(cityRegion.getId());
                            vo.setCity(cityRegion.getId());
                        } else {
                            msgList.add("nis.netbank.message.cityname.mismatch");
                        }
                    } else {
                        //判断省市是否一致
                        if (!cityRegion.getParentId().equals(provRegion.getId())) {
                            msgList.add("nis.netbank.message.cityname.mismatch");
                        } else {
                            vo.setProvince(provRegion.getId());
                            vo.setCity(cityRegion.getId());
                        }
                    }
                }
                //判断是否存在多个首选账号
                String defaultAccSystem = vo.getIsDefaultAccString();
                if (!EmptyUtils.isBlank(defaultAccSystem)) {
                    if ("是".equals(vo.getIsDefaultAccString())) {
                        num++;
                    }
                    //只能选择一个首选账号
                    if (num > 1) {
                        msgList.add("sys.corp.message.isdefaultacc");
                    }
                }
                // 判断联行号是否存在及关联账号信息是否正确
                //联行号不为空时才校验是否正确 是否国内为是才验证联行号
                if (!StringUtils.isBlank(bankSourceCode) && "是".equals(vo.getIsNativeStr())) {
                    BtInputBankInfoVO tempbankInfoVO = btInputBankInfoService.getByBankCodeOrBankNameAndImport(bankSourceCode, null);
                    //根据联行号查询出来的银行信息
                    if (!EmptyUtils.isEmpty(tempbankInfoVO)) {
                        if (tempbankInfoVO.getBankCode().equals(bankSourceCode)) {
                            if (null != vo.getBank()) {
                                if (!tempbankInfoVO.getBankName().equals(vo.getBank())) {
                                    msgList.add("sys.externalCorp.import.bank.match");
                                }
                            } else {
                                vo.setBank(tempbankInfoVO.getBankName());
                            }
                            //导入模板里,帐号所在省,所在市不存在时,直接使用联行号关联的信息获取所在省市
                            //模板里所在省市存在时,根据联行号信息校验输入信息的正确性
                            if (null != vo.getAccProvName() && !tempbankInfoVO.getProvName().equals(vo.getAccProvName())) {
                                msgList.add("sys.externalCorp.import.bankProv.match");
                            } else {
                                vo.setAccProvince(tempbankInfoVO.getProv());
                                vo.setAccProvName(tempbankInfoVO.getProvName());
                            }
                            if (null != vo.getAccCityName() && !tempbankInfoVO.getCityName().equals(vo.getAccCityName())) {
                                msgList.add("sys.externalCorp.import.bankCity.match");
                            } else {
                                vo.setAccCity(tempbankInfoVO.getCity());
                                vo.setAccCityName(tempbankInfoVO.getCityName());
                            }
                        }
                    } else {
                        msgList.add("sys.externalCorp.import.bankSourceCode.null");
                    }
                }
            }
            //判断数据源是否存在
            String sourceSystem = vo.getSourceSystemStr();
            if (!EmptyUtils.isBlank(sourceSystem)) {
                if (sourceSystemDict.contains(sourceSystem)) {
                    // 设置数据源value值
                    if (sourceSystemMap.containsKey(sourceSystem)) {
                        vo.setSourceSystem(sourceSystemMap.get(sourceSystem));
                    } else {
                        DictVO dict = dictService.getDictByDictGroupAndName("sys.externalCorp.source", sourceSystem);
                        sourceSystemMap.put(sourceSystem, dict.getCode());
                        vo.setSourceSystem(dict.getCode());
                    }
                } else {
                    msgList.add("sys.externalCorp.import.sourceSystem.null");
                }
            } else {
                vo.setSourceSystem(defaultSourceSystem.getCode());
            }
            //判断客商属性是否准确
            String bwTypeName = vo.getBwTypeName();
            if (StringUtils.isNotBlank(bwTypeName)) {
                if (bwTypeDict.containsKey(bwTypeName)) {
                    vo.setBwType(bwTypeDict.get(bwTypeName).getCode());
                    vo.setBwTypeName(bwTypeDict.get(bwTypeName).getName());
                } else {
                    msgList.add("sys.externalCorp.import.bwTypeName.null");
                }
            } else {
                vo.setBwType(SysBlackWhiteConstant.EXTERNALCORP_BWTYPE_00);
            }
            //判断客商类型是否存在
            String externalType = vo.getExternalTypeStr();
            if (!EmptyUtils.isBlank(externalType)) {
                if (externalTypeDict.contains(externalType)) {
                    // 设置客商类型value值
                    if (externalTypeMap.containsKey(externalType)) {
                        vo.setExternalType(externalTypeMap.get(externalType));
                    } else {
                        DictVO dict = dictService.getDictByDictGroupAndName("external.type", externalType);
                        externalTypeMap.put(externalType, dict.getCode());
                        vo.setExternalType(dict.getCode());
                    }
                    // 客商类型非客户时，校验供应商类型
                    if (!DEFAULT_EXTERNAL_TYPE.equalsIgnoreCase(externalType)) {
                        // 供应商类型为空
                        if (EmptyUtils.isBlank(vo.getSupplierName())) {
                            msgList.add("sys.externalCorp.import.supplierName.null");
                        } else {
                            // 供应商类型在系统中是否存在
                            SupplierType supplierType = supplierTypeService.getEntityByName(vo.getSupplierName());
                            if (null == supplierType) {
                                msgList.add("sys.externalCorp.import.supplierName.info.null");
                            } else {
                                vo.setSupplierId(supplierType.getId());
                            }
                        }
                    }
                } else {
                    msgList.add("sys.externalCorp.import.externalType.match");
                }
            } else {
                msgList.add("sys.externalCorp.import.externalType.null");
                vo.setExternalType(defaultExternalType.getCode());
            }
            // 是否有效-默认有效
            if (EmptyUtils.isEmpty(vo.getStatusString())) {
                msgList.add("sys.externalCorp.import.status.null");
            } else {
                vo.setStatus(YES_ZN.equals(vo.getStatusString()) ? 1 : 0);
            }
            // 首选账号-默认否
            if (EmptyUtils.isBlank(vo.getIsDefaultAccString())) {
                vo.setIsDefaultAccString(defaultAcc);
            }
            vo.setIsDefaultAcc(YES_ZN.equals(vo.getIsDefaultAccString()) ? "1" : "0");
            // 判断是否 相同客商名称+客商编码+账户
            if ("是".equals(vo.getIsCommonStr())) {
                String nameAndCodeAndExternalAcc = vo.getName() + vo.getCode() + vo.getExternalAcc();
                if (nameAndCodeAndExternalAccMap.containsKey(nameAndCodeAndExternalAcc)) {
                    msgList.add("sys.externalCorp.import.bankAcc.same");
                } else {
                    nameAndCodeAndExternalAccMap.put(nameAndCodeAndExternalAcc, nameAndCodeAndExternalAcc);
                }
            } else {
                String nameAndCodeAndExternalAcc = vo.getName() + vo.getCode() + vo.getExternalAcc() + vo.getCorpName();
                if (nameAndCodeAndExternalAccMap.containsKey(nameAndCodeAndExternalAcc)) {
                    msgList.add("sys.externalCorp.import.bankAcc.same");
                } else {
                    nameAndCodeAndExternalAccMap.put(nameAndCodeAndExternalAcc, nameAndCodeAndExternalAcc);
                }
            }
            vo.addErrorMsg(msgList);
        }
    }
    /**
     * 检查是否可保存,不抛出异常,在批量操作时跳过已存在的记录
     * created by shenc
     *
     * @param vo
     * @return
     */
    public boolean isSaveNoException(ExternalCorpVO vo) {
        if ("是".equals(vo.getIsCommonStr())) {
            ExternalCorp externalCorpByCode = this.externalCorpDao.getByCorpIdAndCode(vo.getCode());
            if (!EmptyUtils.isEmpty(externalCorpByCode) && !externalCorpByCode.getId().equalsIgnoreCase(vo.getId())) {
                return false;
            }
            ExternalCorp externalCorpByName = this.externalCorpDao.findByCorpIdAndName(vo.getName());
            if (!EmptyUtils.isEmpty(externalCorpByName) && !externalCorpByName.getId().equalsIgnoreCase(vo.getId())) {
                return false;
            }
        } else {
            ExternalCorp externalCorpByCode = this.externalCorpDao.getIdByCorpIdAndCode(vo.getCorpId(), vo.getCode());
            if (!EmptyUtils.isEmpty(externalCorpByCode) && !externalCorpByCode.getId().equalsIgnoreCase(vo.getId())) {
                return false;
            }
            ExternalCorp externalCorpByName = this.externalCorpDao.findIdByCorpIdAndName(vo.getCorpId(), vo.getName());
            if (!EmptyUtils.isEmpty(externalCorpByName) && !externalCorpByName.getId().equalsIgnoreCase(vo.getId())) {
                return false;
            }
        }
        return true;
    }
    /**
     * 导入客商资料,默认保存为共享模式
     * created by shenc
     *
     * @param list
     */
    public void saveImport(List<ExternalCorpInfoImportVO> list) {
        //使用Map加List,将名称不同的客商分组存储,同一个List中的信息属于同一个客商,仅银行账号相关信息不同
        Map<String, List<ExternalCorpInfoImportVO>> externalCorpAndBankAccVOMap = new HashMap<>();
        for (ExternalCorpInfoImportVO vo : list) {
            if ("是".equals(vo.getIsCommonStr())) {
                if (null != externalCorpAndBankAccVOMap.get(vo.getName())) {
                    externalCorpAndBankAccVOMap.get(vo.getName()).add(vo);
                } else {
                    externalCorpAndBankAccVOMap.put(vo.getName(), new ArrayList<ExternalCorpInfoImportVO>());
                    externalCorpAndBankAccVOMap.get(vo.getName()).add(vo);
                }
            } else {
                String name = vo.getName() + vo.getCorpName();
                if (null != externalCorpAndBankAccVOMap.get(name)) {
                    externalCorpAndBankAccVOMap.get(name).add(vo);
                } else {
                    externalCorpAndBankAccVOMap.put(name, new ArrayList<ExternalCorpInfoImportVO>());
                    externalCorpAndBankAccVOMap.get(name).add(vo);
                }
            }
        }
        //遍历Map,不同的客商信息为外层循环
        for (Map.Entry entry : externalCorpAndBankAccVOMap.entrySet()) {
            //客商不同的银行账号信息
            List<ExternalCorpInfoImportVO> externalCorpAndBankAcc = (List<ExternalCorpInfoImportVO>) entry.getValue();
            // 因为客商的基本信息一致,取第一个,为后续存储客商表做准备
            ExternalCorpInfoImportVO corpBankAccVO = externalCorpAndBankAcc.get(0);
            ExternalCorpVO externalCorpVO = new ExternalCorpVO();
            ExternalCorp externalCorp = new ExternalCorp();
            // 从ExternalCorpInfoImportVO中复制保存客商需要的信息
            BeanUtils.copyNotNullProperties(corpBankAccVO, externalCorpVO);
            //将用来保存的客商与银行账号关联关系模板
            List<ExternalCorpBankAcc> corpBankAccList = BeanUtils.copyListProperties(externalCorpAndBankAcc, ExternalCorpBankAcc.class);
            //设置将要转换为客商实体的VO的单位ID
            if (isSaveNoException(externalCorpVO)) {
                //从ExternalCorpVO中复制客商信息到ExternalCorp
                BeanUtils.copyNotNullProperties(externalCorpVO, externalCorp);
                // 引用传递时,externalCorp的ID将会自动赋值,影响下次"相同客商信息不同单位"时保存数据
                ExternalCorp saveCorp = new ExternalCorp();
                saveCorp.setIsCommon("1");
                //默认为客商类型
                saveCorp.setExternalType("0");
                //审核状态默认为暂存
                externalCorp.setAuditStatus(CommonConstant.STATUE_TEMP);
                BeanUtils.copyNotNullProperties(externalCorp, saveCorp);
                saveEntity(saveCorp);
                // 为Map中存储的客商ID与银行账号绑定
                List<ExternalCorpBankAccVO> oldList = this.externalCorpBankACCService.listByExternalCorpId(saveCorp.getId());
                if (!EmptyUtils.isEmptyList(oldList)) {
                    this.externalCorpBankACCService.deleteBankAccByExternalCorpId(saveCorp.getId());
                }
                if (!EmptyUtils.isBlank(corpBankAccVO.getCorpId())) {
                    saveExternalCorpAndCorp(saveCorp.getId(), corpBankAccVO.getCorpId());
                }
                for (int i = 0; i < corpBankAccList.size(); i++) {
                    ExternalCorpBankAcc acc = corpBankAccList.get(i);
                    if (!EmptyUtils.isEmpty(acc)) {
                        //判断是否为首选账号
                        if (externalCorpAndBankAcc.get(i).getIsDefaultAccString().equals(dictService.getDictByDictGroupAndCode("sf_yn", 1).getName())) {
                            acc.setIsDefaultAcc("1");
                        } else {
                            acc.setIsDefaultAcc("0");
                        }
                        acc.setExternalCorpId(saveCorp.getId());
                        // 引用传递时,避免模板文件中的ID被赋值,重新生成对象
                        ExternalCorpBankAcc saveAcc = new ExternalCorpBankAcc();
                        BeanUtils.copyNotNullProperties(acc, saveAcc);
                        saveOrUpdateExternalCorpBankAcc(saveCorp, saveAcc);
                    }
                }
            }
        }
    }
    /**
     * 导入保存客商与银行账户信息
     *
     * @param ec
     * @param externalCorpBankAcc
     */
    public void saveOrUpdateExternalCorpBankAcc(ExternalCorp ec, ExternalCorpBankAcc externalCorpBankAcc) {
        if (EmptyUtils.isBlank(externalCorpBankAcc.getExternalAcc())) {
            return;
        }
        if (StringUtils.isBlank(externalCorpBankAcc.getRemark())) {
            externalCorpBankAcc.setRemark("");
        }
        if (StringUtils.isBlank(externalCorpBankAcc.getId())) {
            externalCorpBankAcc.setExternalCorpId(ec.getId());
            this.externalCorpBankACCService.save(externalCorpBankAcc);
        } else {
            this.externalCorpBankACCService.update(externalCorpBankAcc);
        }
    }
    /**
     * 保存客商与单位关联关系表
     *
     * @param externalId
     * @param corpId
     */
    public void saveExternalCorpAndCorp(String externalId, String corpId) {
        ExternalCorpAndCorp save = new ExternalCorpAndCorp();
        save.setExternalCorpId(externalId);
        save.setCorpId(corpId);
        this.externalCorpAndCorpService.save(save);
    }
    /**
     * 在客商资料保存后,保存客商与单位的对应关系表
     *
     * @param corps
     * @param externalCorp
     */
    private void saveOrUpdateExternalCorpAndCorp(String corps, ExternalCorpVO externalCorp) {
        if (!StringUtils.isBlank(corps)) {
            String[] corpIds = corps.split(",");
            for (String corpId : corpIds) {
                saveExternalCorpAndCorp(externalCorp.getId(), corpId);
            }
        }
    }
    /**
     * 根据客商Id查询对应的单位ID
     *
     * @param externalCorpId
     * @return
     */
    public List<String> getCorpIdsByExternalCorpId(String externalCorpId) {
        return this.externalCorpAndCorpService.getCorpIdsByExternalCorpId(externalCorpId);
    }
    /**
     * 加载单位信息
     *
     * @param vo
     * @return
     */
    public ExternalCorpVO initCorp(ExternalCorpVO vo) {
        if (!EmptyUtils.isEmpty(vo)) {
            initCorp(Collections.singletonList(vo));
        }
        return vo;
    }
    /**
     * 下拉框客商组件查询
     *
     * @param corpId
     * @return
     * @author zuoy 2018-07-04
     */
    public List<ExternalCorpBankAccDetailVO> listByInputExternalCorp(String corpId, String seach) {
        DataTablesInput dataTablesInput = new DataTablesInput();
        dataTablesInput.setLength(50);
        Map<String, Object> params = dataTablesInput.getFormdata();
        params.put("fex", "1");
        dataTablesInput.setFormdata(params);
        IPageResult<ExternalCorpBankAccDetailVO> pageResult = this.externalCorpDao.listByInputExternalCorp(dataTablesInput, corpId, seach);
        if (EmptyUtils.isEmpty(pageResult) || EmptyUtils.isEmptyList(pageResult.getList())) {
            return new ArrayList<>();
        } else {
            List<ExternalCorpBankAccDetailVO> list = pageResult.getList();
            for (ExternalCorpBankAccDetailVO vo : list) {
                vo.setInputExternalText(vo.getExternalAcc() + " " + vo.getName());
                vo.setInputExternalValue(vo.getExternalAcc() + "_" + vo.getName());
            }
        }
        return pageResult.getList();
    }
    /**
     * 审核客商资料
     *
     * @param target
     */
    public void saveAudit(String[] ids, Integer target, String auditMessage, ICurrentUser user) {
        if (ids == null || ids.length == 0) {
            throw new BusinessException("id不能为空");
        }
        List<String> idList = Arrays.asList(ids);
        List<ExternalCorp> externalCorpList = externalCorpDao.getExternalCorpByIds(idList);
        List<ExternalCorp> corpToUpdate = new ArrayList<>();
        List<ExternalCorpAuditLog> logList = new ArrayList<>();
        for (ExternalCorp externalCorp : externalCorpList) {
            //只更新为待审核的
            if (externalCorp.getAuditStatus().equals(CommonConstant.STATUE_WAIT_CHECK)) {
                ExternalCorp temp = new ExternalCorp();
                BeanUtils.copyNotNullProperties(externalCorp, temp);
                temp.setAuditStatus(target);
                corpToUpdate.add(temp);
                ExternalCorpAuditLog externalCorpAuditLog = new ExternalCorpAuditLog(temp.getId(), temp.getAuditStatus(), auditMessage, user);
                logList.add(externalCorpAuditLog);
                if (CommonConstant.STATUE_CHECK.equals(temp.getAuditStatus())) {
                    saveUpdateBlackWhit(temp, user);
                }
            }
        }
        batchUpdate(corpToUpdate);
        externalCorpAuditLogService.batchSave(logList);
    }
    /**
     * 查找国外客商信息
     *
     * @param dataTablesInput
     * @return
     */
    public IPageResult<ExternalCorpBankAccDetailVO> listByForeignExternalDetail(DataTablesInput dataTablesInput) {
        Map<String, Object> params = dataTablesInput.getFormdata();
        params.put("fex", "0");
        dataTablesInput.setFormdata(params);
        return externalCorpDao.listByExternalDetail(dataTablesInput,null);
    }
    /**
     * 下拉框境外客商组件查询
     *
     * @param corpId
     * @return
     * @author wangzx 2019-07-19
     */
    public List<ExternalCorpBankAccDetailVO> listByInputExternalCorpFex(String corpId, String seach) {
        DataTablesInput dataTablesInput = new DataTablesInput();
        dataTablesInput.setLength(50);
        Map<String, Object> params = dataTablesInput.getFormdata();
        params.put("fex", "0");
        dataTablesInput.setFormdata(params);
        IPageResult<ExternalCorpBankAccDetailVO> pageResult = this.externalCorpDao.listByInputExternalCorp(dataTablesInput, corpId, seach);
        if (EmptyUtils.isEmpty(pageResult) || EmptyUtils.isEmptyList(pageResult.getList())) {
            return new ArrayList<>();
        }
        return pageResult.getList();
    }
    /**
     * 根据客商表Id和客商银行账号查找客商账号信息
     *
     * @param externalCorpId
     * @param bankAcc
     * @return
     */
    public ExternalCorpBankAccVO getExternalCorpBankAccByIdAndAcc(String externalCorpId, String bankAcc) {
        return this.externalCorpDao.getExternalCorpBankAccByIdAndAcc(externalCorpId, bankAcc);
    }
    /**
     * 根据Id获取客商资料
     *
     * @param externalCorp
     * @return
     */
    @Transactional(readOnly = true)
    public List<ExternalCorpVO> selectExternalCorpView(ExternalCorpVO externalCorp) {
        if (externalCorp == null || StringUtils.isBlank(externalCorp.getId())) {
            return null;
        }
        String[] ids = StringUtils.split(externalCorp.getId(), CommonConstant.COMMA_SPLIT);
        return externalCorpDao.listByIds(ids);
    }
    public DataTablesOutput<ExternalCorpVO> dataTableForSelect(DataTablesInput input) {
        return externalCorpDao.dataTableForSelect(input);
    }

    /**
     * 辅助方法：注入数据。
     *
     * @param listDictMap 字典表数据。
     * @param item        注入对象。
     */
    private static void injectValues(Map<String, Map<String, DictVO>> listDictMap, ExternalCorpVO item) {
        Optional.ofNullable(item.getAuditStatus()).flatMap(auditStatus -> Optional.ofNullable(listDictMap.get("externalCorp.auditStatus"))
                        .flatMap(dictMap -> Optional.ofNullable(dictMap.get(auditStatus.toString()))))
                .ifPresent(dict -> item.setAuditStatusName(dict.getName()));
        Optional.ofNullable(item.getIsCommon()).flatMap(isCommon -> Optional.ofNullable(listDictMap.get("sf_yn"))
                        .flatMap(dictMap -> Optional.ofNullable(dictMap.get(isCommon))))
                .ifPresent(dict -> item.setIsCommonStr(dict.getName()));
        Optional.ofNullable(item.getIsNative()).flatMap(isNative -> Optional.ofNullable(listDictMap.get("sf_yn"))
                        .flatMap(dictMap -> Optional.ofNullable(dictMap.get(isNative.toString()))))
                .ifPresent(dict -> item.setIsNativeStr(dict.getName()));
        Optional.ofNullable(item.getStatus()).flatMap(status -> Optional.ofNullable(listDictMap.get("sf_yn"))
                        .flatMap(dictMap -> Optional.ofNullable(dictMap.get(status.toString()))))
                .ifPresent(dict -> item.setStatusString(dict.getName()));
        Optional.ofNullable(item.getExternalType()).flatMap(externalType -> Optional.ofNullable(listDictMap.get("external.type"))
                        .flatMap(dictMap -> Optional.ofNullable(dictMap.get(externalType))))
                .ifPresent(dict -> item.setExternalTypeStr(dict.getName()));
        Optional.ofNullable(item.getBwType()).flatMap(bwType -> Optional.ofNullable(listDictMap.get("SYS_BUS_ENTITY_TYPE"))
                        .flatMap(dictMap -> Optional.ofNullable(dictMap.get(bwType))))
                .ifPresent(dict -> item.setBwTypeName(dict.getName()));
    }

    /**
     * 新增修改白名单信息
     *
     * @param externalCorp 客商信息
     * @param user         用户
     */
    public void saveUpdateBlackWhit(ExternalCorp externalCorp, ICurrentUser user) {
        //黑白名单不提醒时不更新
        Config config = configServiceI.getByTypeAndName(SysConfigConstant.Module.SYS, SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK);
        String blackListCheck = SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_NOWARN;
        if (config != null && StringUtils.isNotBlank(config.getValue())) {
            blackListCheck = config.getValue();
        }
        if (SysBlackWhiteConstant.SYS_CONFIG_BLACKLISTCHECK_NOWARN.equals(blackListCheck)) {
            return;
        }
        String corpId = null;
        String countryId = null;
        if (!"1".equals(externalCorp.getIsCommon())) {
            List<String> corpIdList = getCorpIdsByExternalCorpId(externalCorp.getId());
            if (ListUtils.isNotNullOrEmpty(corpIdList)) {
                corpId = corpIdList.get(0);
            }
        }
        if (externalCorp.getIsNative() != null && externalCorp.getIsNative() == 0) {
            countryId = externalCorp.getProvince();
        } else {
            SysCountryCustDTO custDTO = sysCountryCustService.getByNameZh("中国");
            if (custDTO != null) {
                countryId = custDTO.getId();
            }
        }
        BlackWhiteDTO bwDTO = new BlackWhiteDTO(
                externalCorp.getCode(),
                externalCorp.getName(),
                externalCorp.getBwType(),
                null,
                corpId,
                externalCorp.getIsCommon() == null ? "0" : externalCorp.getIsCommon(),
                null,
                countryId,
                externalCorp.getStatus().toString(),
                externalCorp.getId());
        blackWhiteService.saveOrUpdateBlackWhite(bwDTO, user);
    }
    public DataTablesOutput getAllDatas(DataTablesInput input, String userId) {
        return initVo(externalCorpDao.getAllDatas(input, userId));
    }
    public DataTablesOutput initVo(DataTablesOutput output){
        if (output == null || EmptyUtils.isEmptyList(output.getData())) {
            return output;
        }
        List<ExternalCorpVO> list = output.getData();
        initCorp(list);
        return output;
    }

    /**
     * 批量加载客商扩展信息，避免列表场景逐条查询关联单位、字典、地区和交付产品。
     *
     * @param list 客商列表
     */
    public void initCorp(List<ExternalCorpVO> list) {
        if (EmptyUtils.isEmptyList(list)) {
            return;
        }
        List<String> externalCorpIds = list.stream()
                .filter(Objects::nonNull)
                .map(ExternalCorpVO::getId)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, List<ExternalCorpAndCorp>> corpRelationMap = listCorpRelationMap(externalCorpIds);
        List<String> corpIds = corpRelationMap.values().stream()
                .flatMap(Collection::stream)
                .map(ExternalCorpAndCorp::getCorpId)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, CorpDTO> corpMap = new HashMap<>();
        if (!EmptyUtils.isEmptyList(corpIds)) {
            Map<String, CorpDTO> queryCorpMap = corpServiceI.listCorpDtoByCorpIds(corpIds);
            if (queryCorpMap != null) {
                corpMap = queryCorpMap;
            }
        }
        Map<String, Map<String, DictVO>> listDictMap = dictService.listDictMap(
                "externalCorp.auditStatus", "sf_yn", "external.type", "SYS_BUS_ENTITY_TYPE");
        Map<String, RegionDTO> regionDTOMap = listRegionMap(list);
        Map<String, String> quickPayNameMap = listQuickPayNameMap(externalCorpIds);
        for (ExternalCorpVO vo : list) {
            if (vo == null) {
                continue;
            }
            injectCorpValues(vo, corpRelationMap.get(vo.getId()), corpMap);
            injectValues(listDictMap, vo);
            injectRegionValues(vo, regionDTOMap);
            String proName = quickPayNameMap.get(vo.getId());
            if (StringUtils.isNotBlank(proName)) {
                vo.setProName(proName);
            }
        }
    }

    /**
     * 根据客商ID批量查询客商与单位关联关系，并按客商ID分组。
     *
     * @param externalCorpIds 客商ID集合
     * @return key为客商ID，value为关联单位关系集合
     */
    private Map<String, List<ExternalCorpAndCorp>> listCorpRelationMap(List<String> externalCorpIds) {
        Map<String, List<ExternalCorpAndCorp>> corpRelationMap = new HashMap<>();
        if (EmptyUtils.isEmptyList(externalCorpIds)) {
            return corpRelationMap;
        }
        List<ExternalCorpAndCorp> relationList = externalCorpAndCorpService.listByExternalCorpIds(externalCorpIds);
        if (EmptyUtils.isEmptyList(relationList)) {
            return corpRelationMap;
        }
        return relationList.stream()
                .filter(relation -> relation != null && StringUtils.isNotBlank(relation.getExternalCorpId()))
                .collect(Collectors.groupingBy(ExternalCorpAndCorp::getExternalCorpId));
    }

    /**
     * 将批量查询出的单位ID、单位编码和单位名称回填到客商VO。
     *
     * @param vo 客商VO
     * @param relationList 客商与单位关联关系
     * @param corpMap 单位信息Map，key为单位ID
     */
    private void injectCorpValues(ExternalCorpVO vo, List<ExternalCorpAndCorp> relationList, Map<String, CorpDTO> corpMap) {
        if (EmptyUtils.isEmptyList(relationList)) {
            return;
        }
        StringBuilder corpIds = new StringBuilder();
        StringBuilder corpCodes = new StringBuilder();
        StringBuilder corpNames = new StringBuilder();
        for (ExternalCorpAndCorp relation : relationList) {
            if (relation == null) {
                continue;
            }
            String corpId = relation.getCorpId();
            appendWithComma(corpIds, corpId);
            CorpDTO corpDTO = corpMap.get(corpId);
            if (corpDTO != null) {
                appendWithComma(corpCodes, corpDTO.getCode());
                appendWithComma(corpNames, corpDTO.getName());
            }
        }
        vo.setCorpId(corpIds.toString());
        vo.setCorpCode(corpCodes.toString());
        vo.setCorpName(corpNames.toString());
    }

    /**
     * 批量查询客商列表中涉及的地区信息。
     *
     * @param list 客商列表
     * @return key为地区ID，value为地区信息
     */
    private Map<String, RegionDTO> listRegionMap(List<ExternalCorpVO> list) {
        List<String> regionIds = list.stream()
                .filter(Objects::nonNull)
                .flatMap(vo -> Arrays.asList(vo.getCity(), vo.getProvince()).stream())
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (EmptyUtils.isEmptyList(regionIds)) {
            return new HashMap<>();
        }
        Map<String, RegionDTO> regionDTOMap = regionService.listByIds(regionIds);
        return regionDTOMap == null ? new HashMap<>() : regionDTOMap;
    }

    /**
     * 将批量查询出的地区名称回填到客商VO。
     *
     * @param vo 客商VO
     * @param regionDTOMap 地区信息Map，key为地区ID
     */
    private void injectRegionValues(ExternalCorpVO vo, Map<String, RegionDTO> regionDTOMap) {
        if (regionDTOMap == null || regionDTOMap.isEmpty()) {
            return;
        }
        String city = vo.getCity();
        String province = vo.getProvince();
        if (StringUtils.isNotBlank(city) && regionDTOMap.containsKey(city)) {
            vo.setCityName(regionDTOMap.get(city).getName());
        }
        if (StringUtils.isBlank(vo.getCityFexName()) && StringUtils.isNotBlank(city) && regionDTOMap.containsKey(city)) {
            vo.setCityFexName(regionDTOMap.get(city).getName());
        }
        if (StringUtils.isNotBlank(province) && regionDTOMap.containsKey(province)) {
            vo.setProvName(regionDTOMap.get(province).getName());
        }
        if (StringUtils.isBlank(vo.getProvinceFexName()) && StringUtils.isNotBlank(province) && regionDTOMap.containsKey(province)) {
            vo.setProvinceFexName(regionDTOMap.get(province).getName());
        }
    }

    /**
     * 根据客商ID批量查询交付产品名称，并按客商ID拼接。
     *
     * @param externalCorpIds 客商ID集合
     * @return key为客商ID，value为逗号分隔的交付产品名称
     */
    private Map<String, String> listQuickPayNameMap(List<String> externalCorpIds) {
        Map<String, LinkedHashSet<String>> quickPayNameSetMap = new HashMap<>();
        if (EmptyUtils.isEmptyList(externalCorpIds)) {
            return new HashMap<>();
        }
        List<ExternalCorpQuickPayVO> quickPayList = externalCorpQuickPayService.listByExternalCorpIds(externalCorpIds);
        if (EmptyUtils.isEmptyList(quickPayList)) {
            return new HashMap<>();
        }
        for (ExternalCorpQuickPayVO quickPayVO : quickPayList) {
            if (quickPayVO == null || StringUtils.isBlank(quickPayVO.getExternalCorpId()) || StringUtils.isBlank(quickPayVO.getProName())) {
                continue;
            }
            quickPayNameSetMap.computeIfAbsent(quickPayVO.getExternalCorpId(), externalCorpId -> new LinkedHashSet<>())
                    .add(quickPayVO.getProName());
        }
        Map<String, String> quickPayNameMap = new HashMap<>();
        quickPayNameSetMap.forEach((externalCorpId, proNames) -> quickPayNameMap.put(externalCorpId, String.join(CommonConstant.COMMA_SPLIT, proNames)));
        return quickPayNameMap;
    }

    /**
     * 使用系统统一逗号分隔符追加字符串。
     *
     * @param builder 字符串拼接器
     * @param value 待追加值
     */
    private void appendWithComma(StringBuilder builder, String value) {
        if (EmptyUtils.isBlank(builder.toString())) {
            builder.append(value);
        } else {
            builder.append(CommonConstant.COMMA_SPLIT).append(value);
        }
    }


    /**
     * 所有客商信息组件取数
     */
    public DataTablesOutput selectAllExternalDate(DataTablesInput input){
        DataTablesOutput output=externalCorpDao.selectAllExternalDate(input);
        makeAllExternalDate(output);
        return output;
    }


    /**
     * 对客商数据的字段进行填充，所属单位，交付产品
     * @param output
     */
    public void makeAllExternalDate(DataTablesOutput output){
        List<ExternalCorpVO> externalCorpVOS=output.getData();
        if (EmptyUtils.isEmptyList(externalCorpVOS)) {
            return;
        }
        Map<String, Map<String, DictVO>> statusDictMap = dictService.listDictMap("sf_yn");
        Map<String, String> supplierNameMap = supplierTypeService.getValidSign().stream()
                .collect(Collectors.toMap(SupplierType::getId, SupplierType::getName, (first, second) -> first));
        for(ExternalCorpVO vo:externalCorpVOS){
            injectValues(statusDictMap, vo);
            if (StringUtils.isNotBlank(vo.getSupplierId()) && supplierNameMap.containsKey(vo.getSupplierId())) {
                vo.setSupplierName(supplierNameMap.get(vo.getSupplierId()));
            }
            List<String> corpNames= externalCorpAndCorpService.getCorpNameByExternalId(vo.getId());
//           if(!EmptyUtils.isEmpty(corpNames) && corpNames.size()>0){
            vo.setCorpName(String.join(",",corpNames));
            List<String> productNames= externalCorpQuickPayService.getProductNameByExternalId(vo.getId());
            vo.setPaymentProducts(String.join(",",productNames));
        }
    }

    /**
     *
     * 查询客商信息
     *
     * @param id
     * @return
     */
    public ExternalCorpDTO getById(String id) {
        String[] ids = new String[]{id};
        ExternalCorpDTO dto = new ExternalCorpDTO();
        List<ExternalCorpVO> externalCorpVOS = externalCorpDao.listByIds(ids);
        if(CollectionUtil.isNotEmpty(externalCorpVOS)){
            ExternalCorpVO externalCorpVO = externalCorpVOS.get(0);
            BeanUtil.copyProperties(externalCorpVO,dto);
            if(1== externalCorpVO.getIsNative()){ // 国内
                SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getByNameZhLevelTop("中国");
                if(null != sysCountryCustDTO){
                    dto.setCountryId(sysCountryCustDTO.getId());
                    dto.setCountryName(sysCountryCustDTO.getNameZh());
                    dto.setCountryNameEn(sysCountryCustDTO.getNameEn());
                }

                List<String> regionIds = new ArrayList<>();
                if(StringUtils.isNotEmpty(externalCorpVO.getCity())){
                    regionIds.add(externalCorpVO.getCity());
                }
                if(StringUtils.isNotEmpty(externalCorpVO.getProvince())){
                    regionIds.add(externalCorpVO.getProvince());
                }
                if(CollectionUtil.isNotEmpty(regionIds)){
                    Map<String, RegionDTO> regionMap = regionService.findRegionMapByIds(regionIds);
                    RegionDTO cityDTO = regionMap.get(externalCorpVO.getCity());
                    RegionDTO provinceDTO = regionMap.get(externalCorpVO.getProvince());
                    if(null != cityDTO){
                        dto.setCityName(cityDTO.getName());
                    }
                    if(null != provinceDTO){
                        dto.setProvName(provinceDTO.getName());
                    }
                }
            } else {
                dto.setCountryId(externalCorpVO.getProvince());
                dto.setCountryName(externalCorpVO.getProvinceFexName());
                dto.setCityName(externalCorpVO.getCityFexName());
                dto.setProvinceState(externalCorpVO.getProvinceState());
            }


        }
        return dto;
    }
}
