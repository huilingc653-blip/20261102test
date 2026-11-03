package com.bytter.credit.domain.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.*;
import com.bytter.credit.domain.entity.*;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vm.RatBkrationCommonDTO;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.credit.dto.RatUrationBeanDTO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Lists;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 授信额度录入表
 * @date 2017-07-21 14:35:43
 */
@Service("ratBkrationService")
@Transactional
@WorkFlow(value = {RatWorkFlowConstant.RAT_BKRATION})
@DubboService(interfaceClass = IWorkFlowListener.class, group = RatWorkFlowConstant.RAT_BKRATION)
public class RatBkrationService extends BaseService<RatBkration, String> implements
        IUserExcelVerifyHandler<RatBkrationVO>, ITaskService, ICorpDeleteVerifyService, ICorpDisableVerifyService, IWorkFlowListener {
    @Autowired
    private RatBkrationDao ratBkrationDao;
    @Autowired
    private RatBkrationDetailDao detailDao;
    @Autowired
    private RatBkrationDetailService detailService;
    @Autowired
    private RatBkrationDisService disService;
    @Autowired
    private RatBkrationDisDetailService disDtlService;
    @DubboReference
    private IConfigService configService;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private IBtBankService btBankService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference
    private INotificationService notificationService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Autowired
    private RatUrationService ratUrationService;
    @Autowired
    private RatUrationDao ratUrationDao;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie contractOccupServie;
    @DubboReference
    private IBtCurrencyService currencyService;
    @Resource
    private IConService conCommonService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private RatBkrationDisDao ratBkrationDisDao;
    @Autowired
    private RatBkrationDisDetailDao disDetailDao;
    @DubboReference
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference
    private IBankTypeService bankTypeService;
    @Autowired
    private RatItemCustomService ratItemCustomService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(RatBkration entity) {
        super.deleteEntity(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(RatBkration entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(RatBkration entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RatBkration entity) {
        super.saveOrUpdate(entity);
    }

    public BaseDao<RatBkration, String> getEntityDao() {
        return ratBkrationDao;
    }

    /**
     * 功能介绍 : 查询授信录入银行。<br>
     * <b>日期：</b>2017年9月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<BtBankDTO> queryRationBank() {
        return ratBkrationDao.queryRationBank();
    }

    /**
     * 功能介绍 : 查询授信录入数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @param resource
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listOutputData(DataTablesInput input, String resource, String userId) {
        List<String> corpIds = corpService.getUserCorpIds(resource, userId);
        return ratBkrationDao.listOutputData(input, corpIds);
    }

    /**
     * 查询授信录入数据
     *
     * @param input
     * @param resource
     * @param corpIds
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listOutputData(DataTablesInput input, String resource, List<String> corpIds,
                                           String userId) {
        if (ListUtils.isNullOrEmpty(corpIds)) {
            corpIds = corpService.getUserCorpIds(resource, userId);
        }
        return ratBkrationDao.listOutputData(input, corpIds);
    }

    /*
     * 获取所有的授信录入通过的授信单
     */
    public List<RatBkRationVm> queryRationAll(RatBkRationVm uration) {
        return ratBkrationDao.queryRationAll(uration);
    }

    /**
     * 功能介绍 : 查询授信录入审批数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listApproveOutputData(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_BKRATION, input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rb.id", true);
        }
        DataTablesOutput dp = ratBkrationDao.listApproveOutputData(input, businessSql);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 功能介绍 : 查询授信录入列表。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryRatBkrationData(DataTablesInput input, String resource, String userId) {
        DataTablesOutput output = listOutputData(input, resource, userId);
        return setOutputPageData(output);
    }

    /**
     * 查询授信录入列表
     *
     * @param input
     * @param resource
     * @param userId
     * @param corpIds
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryRatBkrationData(DataTablesInput input, String resource, String userId, List<String> corpIds, List<RatItemSumVO> ratItemSumList) {
        DataTablesOutput output = listOutputData(input, resource, corpIds, userId);
        DataTablesOutput outputData = setOutputPageData(output);

        List<RatBkrationVO> totalData = outputData.getData();
        if (EmptyUtils.isEmptyList(totalData)) {
            return outputData;
        }
        Map<String, BigDecimal> itemSumMap = new HashMap<>();
        Map<String, BigDecimal> bankTypeItemSumMap = new HashMap<>();
        for (RatBkrationVO ratVO : totalData) {
            if (EmptyUtils.isEmpty(ratVO)) {
                continue;
            }
            List<RatBkRationDtlVm> rationDtls = ratVO.getRationDtls();
            if (EmptyUtils.isEmptyList(rationDtls)) {
                continue;
            }
            for (RatBkRationDtlVm dtl : rationDtls) {
                BigDecimal maxMoney = EmptyUtils.isEmpty(dtl.getMaxMoney()) ? BigDecimal.ZERO : dtl.getMaxMoney();
                itemSumMap.merge(dtl.getItemName(), maxMoney, BigDecimal::add);

                String bankItemKey = ratVO.getBankTypeName() + dtl.getItemName();
                bankTypeItemSumMap.merge(bankItemKey, maxMoney, BigDecimal::add);
            }
        }
        for (String key : itemSumMap.keySet()) {
            RatItemSumVO ratItemSumDO = new RatItemSumVO();
            ratItemSumDO.setSumBusName(key + "小计：");
            ratItemSumDO.setSumMoney(itemSumMap.get(key));
            ratItemSumList.add(ratItemSumDO);
        }
        LinkedHashMap<String, BigDecimal> sortBankTypeItemSumMap = bankTypeItemSumMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
        for (String key : sortBankTypeItemSumMap.keySet()) {
            RatItemSumVO ratItemSumDO = new RatItemSumVO();
            ratItemSumDO.setSumBusName(key + "小计：");
            ratItemSumDO.setSumMoney(sortBankTypeItemSumMap.get(key));
            ratItemSumList.add(ratItemSumDO);
        }
        return outputData;
    }

    /**
     * 功能介绍 : 设置单品种和综合品种数据。<br>
     * <b>日期：</b>2017年8月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput setOutputPageData(DataTablesOutput output) {
        List<String> ratCodes = new ArrayList<String>();
        List<RatBkrationVO> data = output.getData();
        if (null == data) {
            return output;
        }
        Date nowDate = DateUtils.getDate();// 当前日期
        for (RatBkrationVO bkrationVO : data) {
            if (DateUtils.dayDiffs(bkrationVO.getEndDate(), nowDate) < 0) {
                bkrationVO.setOverdueSign(RationConstant.RAT_COMMON_Y);// 结束日期小于当前日期标识过期
            }
            ratCodes.add(bkrationVO.getId());
        }
        // 设置明细集合
        List<RatBkRationDtlVm> dtlDTOs = detailService.getBkrationDtlByRatCodes(ratCodes);
        //将授信明细中最高额度进行赋值
        setMaxMoneyData(dtlDTOs);
        Map<String, List<RatBkRationDtlVm>> ratMap = new HashMap<String, List<RatBkRationDtlVm>>();
        if (!CollectionUtils.isEmpty(dtlDTOs)) {
            // 授信品种明细按授信编码分组
            for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
                List<RatBkRationDtlVm> ratList = ratMap.computeIfAbsent(dtlDTO.getRatCode(), k -> new ArrayList<>());
                ratList.add(dtlDTO);
            }
        }
        for (RatBkrationVO bkrationVO : data) {
            String ratCode = bkrationVO.getId();
            List<RatBkRationDtlVm> ratList = ratMap.get(ratCode);
            if (CollectionUtils.isEmpty(ratList)) {
                continue;
            }
            // 设置品种单项和综合条数
            setRationDtls(ratList);
            // 单品种条数
            Integer singItemSize = Integer.valueOf(0);
            // 综合品种条数
            Integer compItemSize = Integer.valueOf(0);
            boolean compFlag = false;
            // Integer itemOrder = Integer.valueOf(0);
            if (!EmptyUtils.isEmptyList(ratList)) {
                for (RatBkRationDtlVm dtlDTO : ratList) {
                    dtlDTO.setMoney(dtlDTO.getMoney().add(dtlDTO.getSuperAddMoney()));
                    dtlDTO.setOpenMoney(dtlDTO.getOpenMoney().add(dtlDTO.getSuperAddOpenMoney()));
                    dtlDTO.setMaxMoney(dtlDTO.getMaxMoney().add(dtlDTO.getSuperAddMaxMoney()));
                    if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                        singItemSize++;
                    } else {
                        if (!compFlag) {// 没有设置过，则设置第一个品种的综合标识
                            dtlDTO.setFirstComp("1");
                            compFlag = true;
                        }
                        compItemSize++;
                    }
                }
                bkrationVO.setItemSize(ratList.size());
            }
            bkrationVO.setSingItemSize(singItemSize);// 单品种条数
            bkrationVO.setCompItemSize(compItemSize);// 综合品种条数
            bkrationVO.setRationDtls(ratList);
            if (null == bkrationVO.getDelayStatus()) {
                bkrationVO.setDelayStatus(RationConstant.STATUS_DELETE);// 待延期状态
            }
        }
        return output;
    }

    /**
     * 最高额度赋值
     *
     * @param dtlDTOs
     */
    public void setMaxMoneyData(List<RatBkRationDtlVm> dtlDTOs) {
        if (EmptyUtils.isEmptyList(dtlDTOs)) {
            return;
        }
        for (RatBkRationDtlVm ratDtlDO : dtlDTOs) {
            if (EmptyUtils.isEmpty(ratDtlDO.getMaxMoney()) || ratDtlDO.getMaxMoney().compareTo(BigDecimal.ZERO) == 0) {
                ratDtlDO.setMaxMoney(ratDtlDO.getMoney());
            }
        }
    }

    /**
     * 功能介绍 : 按照品种类型和品种编号排序。<br>
     * <b>日期：</b>2017年8月9日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setRationDtls(List<RatBkRationDtlVm> ratList) {
        if (EmptyUtils.isEmptyList(ratList)) {
            return;
        }
        // 按照单品种和综合品种排序
        Collections.sort(ratList, new Comparator<RatBkRationDtlVm>() {
            public int compare(RatBkRationDtlVm o1, RatBkRationDtlVm o2) {
                if (o1.getItemFlag().equals(o2.getItemFlag())) {
                    return o1.getItemOrder().compareTo(o2.getItemOrder());
                }
                return o1.getItemFlag().compareTo(o2.getItemFlag());
            }
        });
        int size = 1;
        for (RatBkRationDtlVm addDtlDTO : ratList) {
            addDtlDTO.setItemOrder(size++);
        }
    }

    /**
     * 功能介绍 : 查询授信合同号是否存在。<br>
     * <b>日期：</b>2017年9月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean checkContractNo(String contractNo, String ratCode) {
        List<RatBkration> bkrations = ratBkrationDao.checkContractNo(contractNo, ratCode);
        return EmptyUtils.isEmptyList(bkrations);// 不存在
// 存在
    }

    /**
     * 功能介绍 : 获取银行授信录入主表数据。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public RatBkrationPageVO getRatBkrationInfo(String id) {
        return ratBkrationDao.getBkRationBeanById(id);
    }

    /**
     * 功能介绍 : 获取授信录入关联信息。<br>
     * <b>日期：</b>2017年7月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public RatBkrationPageVO getBkRationBeanById(String id) {
        RatBkrationPageVO pageVO = getRatBkrationInfo(id);
        //补齐币别信息
        if (StringUtils.isBlank(pageVO.getCurName()) && StringUtils.isNotBlank(pageVO.getCurCode())){
            pageVO.setCurName(btCurrencyService.getObjectById(pageVO.getCurCode()).getCurName());
        }

        List<String> ratCodes = new ArrayList<String>();
        ratCodes.add(id);
        List<RatBkRationDtlVm> dtlDTOs = detailService.getBkrationDtlByRatCodes(ratCodes);
        for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
            dtlDTO.setMoney(dtlDTO.getMoney().add(dtlDTO.getSuperAddMoney()));
            dtlDTO.setOpenMoney(dtlDTO.getOpenMoney().add(dtlDTO.getSuperAddOpenMoney()));
            dtlDTO.setMaxMoney(dtlDTO.getMaxMoney().add(dtlDTO.getSuperAddMaxMoney()));
        }
        List<RatBkRationDtlVm> compRationDtls = new ArrayList<RatBkRationDtlVm>();
        List<RatBkRationDtlVm> singRationDtls = new ArrayList<RatBkRationDtlVm>();
        pageVO.setCompRationDtls(compRationDtls);// 综合品种明细
        pageVO.setSingRationDtls(singRationDtls);// 单品种明细
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal openMoney = BigDecimal.ZERO;
        for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
            money = money.add(dtlDTO.getMoney());
            openMoney = openMoney.add(dtlDTO.getOpenMoney());
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                singRationDtls.add(dtlDTO);
            } else {
                compRationDtls.add(dtlDTO);
            }
        }
        pageVO.setMoney(money);
        pageVO.setOpenMoney(openMoney);
        pageVO.setItemSize(dtlDTOs.size());
        pageVO.setSingItemSize(singRationDtls.size());// 单品种条数
        pageVO.setCompItemSize(compRationDtls.size());// 综合品种条数
        return pageVO;
    }

    /**
     * 功能介绍 : 额度包含或者交叉允许选择。<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRatContains() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_RATCONTAINS);
    }

    /**
     * 功能介绍 : 是否启用敞口额度。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public ConfigDTO getStartOpenRat() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_OPENRAT);
    }

    /**
     * 功能介绍 : 获取授信参数设置是否启用敞口
     */
    @Transactional(readOnly = true)
    public ConfigDTO getIsOpenRat() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_STARTRATEXP);
    }

    /**
     * 功能介绍 : 获取参数设置是否挂接保证金<br>
     * <b>日期：</b>2019年5月27日<br>
     *
     * @version 1.0
     * @author luxy
     */
    @Transactional(readOnly = true)
    public ConfigDTO getIsHangBail() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_ISHANGBAIL);
    }

    /**
     * 获取是否对接合同管理配置
     *
     * @return
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRelateCon() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_RELATECON);
    }

    /**
     * 对接合同管理模式配置
     *
     * @return
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRatConType() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_RATCONTYPE);
    }

    /**
     * 功能介绍 : 获取参数设置是否挂接担保<br>
     * <b>日期：</b>2019年5月27日<br>
     *
     * @version 1.0
     * @author luxy
     */
    @Transactional(readOnly = true)
    public ConfigDTO getMountGuarantee() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_ISGUARANTEE);
    }

    /**
     * 功能介绍 : 获取参数设置授信合同必须控制
     *
     * @author fangcf
     * @date 2024-06-18
     */
    @Transactional(readOnly = true)
    public ConfigDTO getCreditContractControl() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_CREDITCONTRACT_CONTROL);
    }

    /**
     * 功能介绍 : 检查是否存在包含或交叉的授信。<br>
     * <b>日期：</b>2017年7月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkBkration(RatBkRationVm bkrationDTO) {
        /**
         * 额度包含或者交叉允许选择 先判断是否允许交叉
         */
        ConfigDTO configDTO = getRatContains();
        // 不允许则出现交叉或包含都不能保存
        if (RationConstant.RAT_CONTAIN_0.equals(configDTO.getValue())) {
            checkContianRatBankRation(bkrationDTO, "1");
        } else {
            bkrationDTO.setUpdateSign(false);// 设置交叉的授信不可过期，只做检查
            checkContianRatBankRation(bkrationDTO, "2");
        }
    }

    /**
     * 功能介绍 : 生成授信编号。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    protected String getRatCodeNo(SequenceEnum senum, String prefix) {
        return SequenceSingleton.getSequence(senum, prefix);
    }

    /**
     * 设置授信额度录入明细
     */
    public void setRatBkrationDtls(RatBkRationVm rationtDTO) {

        if (EmptyUtils.isEmpty(rationtDTO.getIncludeBailmoney())) {
            rationtDTO.setIncludeBailmoney(RationConstant.RAT_COMMON_Y);//包含保证金
        }

        List<RatBkRationDtlVm> rationDtls = rationtDTO.getRationDtls();

        if (!CollectionUtils.isEmpty(rationDtls)) {
            // 过滤品种标识不为空的数据
            rationDtls = rationDtls.stream().filter(item -> !ObjectUtils.isEmpty(item) && !StringUtils.isEmpty(item.getItemFlag())).collect(Collectors.toList());

            rationDtls.forEach(item -> {
                if (RationConstant.RAT_COMFLAG_1.equals(item.getItemFlag()) && null != item.getMoney()) {
                    item.setCompSign(RationConstant.RAT_COMP_SIGN_1);//综合授信使用标识
                } else {
                    item.setCompSign(RationConstant.RAT_COMP_SIGN_0);//默认都是0
                }
                if (null == item.getMoney()) {
                    item.setMoney(BigDecimal.ZERO);
                }
                if (null == item.getOpenMoney()) {
                    item.setOpenMoney(BigDecimal.ZERO);
                }
                if (item.getOpenMoney().compareTo(BigDecimal.ZERO) == 0 && item.getMoney().compareTo(BigDecimal.ZERO) != 0) {
                    item.setOpenMoney(item.getMoney());
                }
            });

            // 单品种集合
            List<RatBkRationDtlVm> singleItemList = rationDtls.stream().filter(single -> RationConstant.RAT_COMFLAG_0.equals(single.getItemFlag())).collect(Collectors.toList());
            // 综合品种集合
            List<RatBkRationDtlVm> compItemList = rationDtls.stream().filter(comp -> RationConstant.RAT_COMFLAG_1.equals(comp.getItemFlag())).collect(Collectors.toList());
            compItemList = compItemList.stream().sorted(Comparator.comparing(RatBkRationDtlVm::getCompSign).reversed()).collect(Collectors.toList());

            List<RatBkRationDtlVm> resultList = Lists.newArrayList();
            resultList.addAll(singleItemList);
            resultList.addAll(compItemList);

            // 重设itemOrder
            AtomicInteger size = new AtomicInteger(1);
            resultList.forEach(item -> item.setItemOrder(size.getAndIncrement()));

            rationtDTO.setRationDtls(resultList);
        }
    }

    /**
     * 功能介绍 : 授信录入保存。<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveRatBkration(RatBkRationVm bkrationDTO, ICurrentUser user) {
        btBankService.saveBtBank(bkrationDTO.getBankCode(), bkrationDTO.getBankType());// 保存银行信息
        /**
         * 额度包含或者交叉允许选择 先判断是否允许交叉
         */
        ConfigDTO configDTO = getRatContains();
        // 不允许则出现交叉或包含都不能保存
        if (RationConstant.RAT_CONTAIN_0.equals(configDTO.getValue())) {
            checkContianRatBankRation(bkrationDTO, "1");
        }
        // 保存授信数据
        RatBkration bkration = new RatBkration();
        BeanUtils.copyProperties(bkrationDTO, bkration);
        bkration.setRatCode(getRatCodeNo(SequenceEnum.RAT_BKRATION_CODE, RationConstant.RAT_BKRATION_CODE));// 授信编号
        CorpDTO corpDTO = corpService.get(bkrationDTO.getCorpId());
        bkration.setCorpName(corpDTO.getName());
        if (CorpConstant.CORP_TYPE_CODE_NET.equals(corpDTO.getType())) {
            bkration.setNetCode(corpDTO.getId());
        } else {
            CorpDTO netDTO = corpService.getNetByCorpId(bkrationDTO.getCorpId());
            bkration.setNetCode(netDTO.getId());
        }
        if (null == bkrationDTO.getOpenMoneysum()) {// 敞口标识
            bkration.setOpenSign(RationConstant.OPEN_SIGN_0);
        } else {
            bkration.setOpenSign(RationConstant.OPEN_SIGN_1);
        }
        bkration.setMoney(bkrationDTO.getMoneysum());// 授信合计金额
        bkration.setOpenmoney(bkrationDTO.getOpenMoneysum());// 敞口合计
        if (null == bkration.getOpenmoney()) {
            bkration.setOpenmoney(bkration.getMoney());// 设置为授信额度
        }
        bkration.setCreateBy(user.getId());
        bkration.setUserName(user.getUsername());
        bkration.setCreateDate(DateUtils.getDate());
        bkration.setSendSign(CommonConstant.SEND_SIGN_NO);// 未发送
        saveEntity(bkration);
        this.getSession().flush();
        this.getSession().clear();
        if (!EmptyUtils.isEmpty(bkration.getOldRatCode())) {
            this.checkChangeBkration(bkrationDTO);
        }
        /**
         * 合同挂接处理
         */
        flowStartConHandle(bkration, user);
        ConfigDTO bailConfig = getIsHangBail();// 获取参数设置是否挂接保证金
        if (bailConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            // 判断是否挂接了保证金
            if (bkrationDTO.getIsHookBond().equals("1")) {// 等于1则表示挂接了保证金
                // 判断占用金额是否大于可用余额
                FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(bkrationDTO.getBondId());
                BigDecimal applyMoney = bkrationDTO.getApplyMoney();
                BigDecimal availMoney = fcBondDTO.getAvailMoney();
                BigDecimal dif = availMoney.subtract(applyMoney);
                if ((dif.compareTo(BigDecimal.ZERO)) == -1) {// 占用金额大于可用余额
                    throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
                }
                // 保存保证金的占用信息
                this.addFcBondApply(bkration, bkrationDTO, user);
            }
        }
        ConfigDTO guaConfig = getMountGuarantee();// 获取参数设置是否挂接担保
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            // 判断是否挂接担保
            if (bkration.getIsHangGuarantee() != null
                    && bkration.getIsHangGuarantee().equals(RationConstant.IS_HANG_GUA_1)) {// 挂接担保
                saveGuaContract(bkration, bkrationDTO);
            }
        }
        processBkRation(bkration, bkrationDTO, configDTO, user.getId());
    }

    /**
     * 结转授信品种判断
     */
    public void checkChangeBkration(RatBkRationVm bkrationDTO) {
        // 结转id不为空，判断是否能结转
        List<Integer> statuss = new ArrayList<>();
        statuss.add(RationConstant.RAT_USE_STATUS_TEMP);
        statuss.add(RationConstant.RAT_USE_STATUS_FREEZE);
        List<RatUrationDisBean> ratUrationDisBeans = ratUrationService.queryByCodeStatus(bkrationDTO.getOldRatCode(),
                statuss);
        if (ratUrationDisBeans.size() > 0) {
            throw new ParameterException(RatMessageEnum.CMS_CHANGE_RATBKRATION_CHECK);
        }
        // 进行结转品种判断
        List<RatBkRationDtlVm> newBkrationDtls = bkrationDTO.getRationDtls();
        Map<String, String> newDtlMap = new HashMap<>();
        for (RatBkRationDtlVm bkrationDtlDTO : newBkrationDtls) {
            if (bkrationDtlDTO.getCreditType().equals("1")) {
                newDtlMap.put(bkrationDtlDTO.getItemCode(), bkrationDtlDTO.getItemFlag());
            }
        }
        List<RatBkRationDtlVm> oldBkrationDtls = detailService.getBkrationDtlDTOByRatCode(bkrationDTO.getOldRatCode(),
                null);
        boolean flag = false;// 是否品种不匹配
        for (RatBkRationDtlVm RatBkRationDtlVm : oldBkrationDtls) {
            if (RatBkRationDtlVm.getCreditType().equals("1")) {
                if (EmptyUtils.isEmpty(newDtlMap.get(RatBkRationDtlVm.getItemCode()))) {
                    flag = true;
                } else {
                    if (!newDtlMap.get(RatBkRationDtlVm.getItemCode()).equals(RatBkRationDtlVm.getItemFlag())) {
                        flag = true;
                    }
                }
            }
        }
        if (flag) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
        }
    }

    /**
     * 保存担保合同信息
     */
    public void saveGuaContract(RatBkration bkration, RatBkRationVm bkrationDTO) {
        GuaContractOccupDTO guaContractOccup = new GuaContractOccupDTO();
        guaContractOccup.setBusinessId(bkration.getId());
        guaContractOccup.setContractIds(bkrationDTO.getContractIds());
        guaContractOccup.setItemNo(bkration.getRatCode());
        guaContractOccup.setSysCode(ConfigConstant.RAT);
        guaContractOccup.setUseDate(DateUtils.getDate());
        guaContractOccup.setGuaRmk(bkrationDTO.getGuaranteeRmk());
        if (bkration.getStatus().equals(RationConstant.STATUS_TEMP)) {// 暂存
            contractOccupServie.tempUseGuaContractOccup(guaContractOccup);
        } else if (bkration.getStatus().equals(RationConstant.STATUS_WAIT_CHECK)) {// 提交待审批
            contractOccupServie.submitUseGuaContractOccup(guaContractOccup);
        }
    }

    /**
     * 功能介绍 : 保存授信明细。<br>
     * <b>日期：</b>2017年9月9日<br>
     *
     * @param userId
     * @version 1.0
     * @author liyh
     */
    private void processBkRation(RatBkration bkration, RatBkRationVm bkrationDTO, ConfigDTO configDTO,
                                 String userId) {
        List<RatBkrationDetail> details = new ArrayList<RatBkrationDetail>();
        details = BeanUtils.copyListProperties(bkrationDTO.getRationDtls(), RatBkrationDetail.class);
        for (RatBkrationDetail detail : details) {
            detail.setRatCode(bkration.getId());
            detailService.saveRatBkrationDetail(detail);
        }
        // 参数为允许，如果时间有交叉 ，则把以前的授信 设为已过期
        if (RationConstant.RAT_CONTAIN_1.equals(configDTO.getValue())) {
            bkrationDTO.setUpdateSign(true);// 设置交叉的授信可过期
            // checkContianRatBankRation(bkrationDTO, "2");
        }
        // 审批调用
        if (RationConstant.STATUS_WAIT_CHECK.equals(bkration.getStatus())) {
            getStartHandler().handler(RatWorkFlowConstant.RAT_BKRATION, bkration.getId(), bkration,
                    bkration.getCorpId(), userId);
            this.getSession().flush();
        }
    }

    /**
     * 功能介绍 : 自主授信审批通过后分配。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void processOwnRatMode(String ratCode, String userId) {
        RatBkRationVm bkrationDTO = geBkrationListDTOByRatCode(ratCode, null);
        // 判断是否自主录入,是，则不需要分配,直接写入分配明细表，待后续完成分配直接调用接口
        if (RationConstant.RATMODE_1.equals(bkrationDTO.getRatMode())) {
            RatBkrationDis dis = disService.processOwnModeDis(bkrationDTO, userId);
            // 更新已分配额度
            updateOwnRationDisMoney(dis);
        } else {
            RatBkration bkration = get(ratCode, true);
            // 发送消息提醒，授信分配
            notificationService.send(bkration.getId(), bkration.getCorpId(), NotificationConstant.CMS.RAT_DIS,
                    bkration);
        }
    }

    public void updateOwnRationDisMoney(RatBkrationDis dis) {
        String ratCode = dis.getRatCode();// 授信录入ID
        List<RatBkrationDetail> bkrationDetails = detailService.getBkrationDetailByRatCode(ratCode);
        Map<String, RatBkrationDetail> dtlMap = new HashMap<String, RatBkrationDetail>();
        // 按授信品种分类
        for (RatBkrationDetail detail : bkrationDetails) {
            dtlMap.put(detail.getItemCode(), detail);
        }
        List<RatBkrationDisDetail> rationDtls = disDtlService.getRationDisDtlByDisCode(dis.getId(), null, null);
        for (RatBkrationDisDetail disDtl : rationDtls) {
            String itemCode = disDtl.getItemCode();// 授信品种
            RatBkrationDetail detail = dtlMap.get(itemCode);
            if (null == detail) {
                // 新生成授信明细数据
                // createRationDtl(ratCode, dtlDTO);
                continue;
            }
            BigDecimal money = NumberUtil.formatNumericNull(detail.getSuperDistributeMoney());// 授信额度
            BigDecimal openMoney = NumberUtil.formatNumericNull(detail.getSuperDistributeOpenMoney());// 授信敞口额度
            detail.setSuperDistributeMoney(money.add(disDtl.getMoney()));// 已分配授信金额
            detail.setSuperDistributeOpenMoney(openMoney.add(disDtl.getOpenMoney()));// 已分配授信敞口金额
            detailService.updateEntity(detail);// 更新已分配金额
        }
    }

    /**
     * 功能介绍 : 获取单位已录入的同银行同币别的授信明细。<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkRationVm> getRatBankRationListByCorpId(RatBkRationVm bkrationDTO) {
        List<RatBkRationVm> dtos = ratBkrationDao.getCorpBkrationListDTOs(bkrationDTO);
        List<RatBkRationDtlVm> dtlDTOs = detailDao.getCorpBkrationDtlDTOs(bkrationDTO);
        setRattionListDTO(dtos, dtlDTOs);
        return dtos;
    }

    /**
     * 功能介绍 : 分组设置授信录入明细数据。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setRattionListDTO(List<RatBkRationVm> dtos, List<RatBkRationDtlVm> dtlDTOs) {
        Map<String, List<RatBkRationDtlVm>> ratMap = new HashMap<String, List<RatBkRationDtlVm>>();
        if (null != dtlDTOs) {
            // 授信品种明细按授信编码分组
            for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
                List<RatBkRationDtlVm> ratList = ratMap.get(dtlDTO.getRatCode());
                if (null == ratList) {
                    ratList = new ArrayList<RatBkRationDtlVm>();
                    ratMap.put(dtlDTO.getRatCode(), ratList);
                }
                ratList.add(dtlDTO);
            }
        }
        if (null != dtos) {
            for (RatBkRationVm dto : dtos) {
                String id = dto.getId();// 授信主键
                List<RatBkRationDtlVm> rationDtls = ratMap.get(id);
                if (null != rationDtls) {
                    dto.setRationDtls(rationDtls);// 授信明细
                }
            }
        }
    }

    /**
     * 功能介绍 : 检查授信是否包含。<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkContianRatBankRation(RatBkRationVm bkrationDTO, String checkType) {
        List<RatBkRationVm> dtos = getRatBankRationListByCorpId(bkrationDTO);
        if (null == dtos || dtos.isEmpty()) {
            return;
        }
        for (RatBkRationVm dto : dtos) {
            compareDateRelation(bkrationDTO, dto, checkType);
        }
    }

    /**
     * 功能介绍 : 比较授信品种日期关系。<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private boolean compareDateRelation(RatBkRationVm dest, RatBkRationVm source, String checkType) {
        Date destStartDate = dest.getBegindate();// 起始日期
        Date destEndDate = dest.getEnddate();// 结束日期
        Date sourceBeginDate = source.getBegindate();// 已保存的起始日期
        Date sourceEndDate = source.getEnddate();// 已保存的结束日期
        if ("2".equals(checkType)) {
            // 时间有交叉,输入的开始时间大于 以前的开始时间 且小于以前的结束时间且 现在的结束大于以前的结束时间
            if (destStartDate.getTime() > sourceBeginDate.getTime() && destStartDate.getTime() < sourceEndDate.getTime()
                    && destEndDate.getTime() > sourceEndDate.getTime()) {
                // 更新原授信为已过期
                if (dest.isUpdateSign()) {
                    //do nothing
                } else {
                    dest.setResultFlag("2");// 存在交叉的授信
                }
            }
            if (destStartDate.getTime() < sourceBeginDate.getTime() && destEndDate.getTime() > sourceBeginDate.getTime()
                    && destEndDate.getTime() < sourceEndDate.getTime()) {
                // 更新现在的授信为已过期 ，即为不能保存
                throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMDATE_BEFORE);
            }
        } else {
            if (destStartDate.getTime() > sourceBeginDate.getTime()) {
                if (destStartDate.getTime() <= sourceEndDate.getTime()) {
                    // 包含或交叉，不能提交
                    throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMDATE_CONTAIN);
                }
            } else if (destStartDate.getTime() == sourceBeginDate.getTime()) {
                // 不管后面时间都是包含或重复
                throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMDATE_CONTAIN);
            } else if (destStartDate.getTime() < sourceBeginDate.getTime()) {
                if (destEndDate.getTime() >= sourceBeginDate.getTime()) {// 包含或包含
                    throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMDATE_CONTAIN);
                }
            }
        }
        return true;
    }

    /**
     * 功能介绍 : 授信录入修改<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRatBkration(RatBkRationVm bkrationDTO, ICurrentUser user) {
        btBankService.saveBtBank(bkrationDTO.getBankCode(), bkrationDTO.getBankType());//保存银行信息
        notificationService.complete(NotificationConstant.CMS.RAT_RATION, bkrationDTO.getId(), user.getId());
        /**
         * 额度包含或者交叉允许选择 先判断是否允许交叉
         */
        ConfigDTO configDTO = getRatContains();
        // 不允许则出现交叉或包含都不能保存
        if (RationConstant.RAT_CONTAIN_0.equals(configDTO.getValue())) {
            checkContianRatBankRation(bkrationDTO, "1");
        }
        // 保存授信数据
        RatBkration bkration = get(bkrationDTO.getId(), true);
        BeanUtils.copyProperties(bkrationDTO, bkration, "id", "createDate", "createBy");

        CorpDTO corpDTO = corpService.get(bkrationDTO.getCorpId());
        bkration.setCorpName(corpDTO.getName());
        if (CorpConstant.CORP_TYPE_CODE_NET.equals(corpDTO.getType())) {
            bkration.setNetCode(corpDTO.getId());
        } else {
            CorpDTO netDTO = corpService.getNetByCorpId(bkrationDTO.getCorpId());
            bkration.setNetCode(netDTO.getId());
        }
        if (null == bkrationDTO.getOpenMoney()) {// 敞口标识
            bkration.setOpenSign(RationConstant.OPEN_SIGN_0);
        } else {
            bkration.setOpenSign(RationConstant.OPEN_SIGN_1);
        }
        bkration.setMoney(bkrationDTO.getMoneysum());
        bkration.setOpenmoney(bkrationDTO.getOpenMoneysum());
        if (null == bkration.getOpenmoney()) {
            bkration.setOpenmoney(bkration.getMoney());
        }
        bkration.setUpdateBy(user.getId());
        bkration.setUpdateDate(DateUtils.getDate());
        bkration.setConId(bkrationDTO.getConId());
        merge(bkration);
        if (!EmptyUtils.isEmpty(bkration.getOldRatCode())) {
            this.checkChangeBkration(bkrationDTO);
        }
        /**
         * 合同挂接处理
         */
        flowStartConHandle(bkration, user);
        // 先删除授信明细，再保存修改后的明细
        detailService.deleteBkrationDtlByRatCode(bkrationDTO);
        // 删除已分配的分配明细
        disDetailDao.deleteBkrationDisDtlByRatCode(bkrationDTO.getId());
        // 删除已分配的主数据
        ratBkrationDisDao.deleteBkrationDisByRatCode(bkrationDTO.getId());
        // 判断是否挂接了保证金
        if ((bkrationDTO.getIsHookBond() != null) && (bkrationDTO.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y))) {
            FcBondApplyDTO dto = fcBondHookService.getFcBondApply(bkration.getId(), bkration.getRatCode(),
                    bkrationDTO.getBondId());
            FcBondApplyDTO fcBondApplyDTOPre = fcBondHookService.getFcBondApply(bkration.getId(), bkration.getRatCode(),
                    bkrationDTO.getBondIdPre());
            // 判断占用金额是否大于可用余额
            FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(bkrationDTO.getBondId());
            BigDecimal applyMoney = bkrationDTO.getApplyMoney();
            BigDecimal availMoney = fcBondDTO.getAvailMoney();
            if (fcBondApplyDTOPre != null) {
                availMoney = availMoney.add(fcBondApplyDTOPre.getApplyMoney());// 编辑状态可用金额要加上修改之前的额度
            }
            BigDecimal dif = availMoney.subtract(applyMoney);
            if ((dif.compareTo(BigDecimal.ZERO)) == -1) {// 占用金额大于可用余额
                throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
            if (dto == null) {// 没有则新增
                if (fcBondApplyDTOPre != null) {// 查询修改之前占用的保证金信息，不为空进行数据修改（修改状态为释放）
                    fcBondApplyDTOPre.setUpdateBy(user.getLoginName());
                    fcBondApplyDTOPre.setUpdateDate(DateUtils.getDate());
                    fcBondHookService.updateFcBondApplyData(fcBondApplyDTOPre);
                }
                this.addFcBondApply(bkration, bkrationDTO, user);
            } else {
                // 修改保证金占用信息
                this.updateFcBondApply(bkrationDTO, dto, user);
            }
        }
        if ((bkrationDTO.getIsHookBond() == null) || (bkrationDTO.getIsHookBond().equals(FcBondConstant.IS_HOOK_N))) {// 不挂保证金
            FcBondApplyDTO fcBondApplyDTO = fcBondHookService.getFcBondApplyByItemIds(bkration.getId(),
                    bkration.getRatCode());
            if (fcBondApplyDTO != null) {// fcBondApplyDTO不为空,修改保证金的状态为已释放
                fcBondApplyDTO.setStatus(FcBondConstant.STATUE_RELEASE);
                fcBondApplyDTO.setUpdateBy(user.getLoginName());
                fcBondApplyDTO.setUpdateDate(DateUtils.getDate());
                fcBondHookService.updateFcBondApply(fcBondApplyDTO);
            }
        }
        // 判断是否挂接担保
        if ((bkration.getIsHangGuarantee() != null)) {
            if (bkration.getIsHangGuarantee().equals(RationConstant.IS_HANG_GUA_1)) {// 挂接担保
                saveGuaContract(bkration, bkrationDTO);// 重新保存担保合同占用信息
            } else if (bkration.getIsHangGuarantee().equals(RationConstant.IS_HANG_GUA_0)) {
                RatBkration rat = get(bkrationDTO.getId());
                if (StringUtils.isNotEmpty(rat.getGuaContract())) {// 之前挂接，修改后不挂接
                    contractOccupServie.deleteGuaContractOccup(bkration.getId(), ConfigConstant.RAT);// 删除之前的担保合同占用信息
                }
            }
        }
        processBkRation(bkration, bkrationDTO, configDTO, user.getId());
    }

    /**
     * 功能介绍 : 获取授信录入数据明细。<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public RatBkRationVm geBkrationListDTOByRatCode(String ratCode, String itemFlag) {
        RatBkRationVm bkrationDTO = new RatBkRationVm();
        RatBkration bkration = get(ratCode, true);
        BeanUtils.copyProperties(bkration, bkrationDTO);
        bkrationDTO.setOpenMoney(bkration.getOpenmoney());// 敞口额度

        if (StringUtils.isNotEmpty(bkration.getBankCode())) {
            BtBankDTO bankDTO = btBankService.getByBankCode(bkration.getBankCode());
            if (null != bankDTO) {
                bkrationDTO.setBankName(bankDTO.getBankName());
            }
        }

        if (StringUtils.isNotEmpty(bkration.getBankType())) {
            BankTypeDTO btBankType = bankTypeService.getObjectById(bkration.getBankType());
            if (!ObjectUtils.isEmpty(btBankType) && StringUtils.isNotEmpty(btBankType.getCreditContractRequired())) {
                bkrationDTO.setCreditContractRequired(btBankType.getCreditContractRequired());
            }
        }
        // 过滤条件品种
        List<RatBkRationDtlVm> compDtls = new ArrayList<RatBkRationDtlVm>();
        List<String> itemCodeList = new ArrayList<String>();// 品种集合
        List<RatBkRationDtlVm> rationDtls = detailService.getBkrationDtlDTOByRatCode(ratCode, null);
        for (RatBkRationDtlVm dtlDTO : rationDtls) {
            if (StringUtils.isEmpty(itemFlag)) {
                compDtls.add(dtlDTO);
            } else if (itemFlag.equals(dtlDTO.getItemFlag())) {
                compDtls.add(dtlDTO);
            }
            itemCodeList.add(dtlDTO.getItemCode());
        }
        bkrationDTO.setRationDtls(compDtls);
        bkrationDTO.setItemSize(rationDtls.size());// 总品种数
        bkrationDTO.setCompItemSize(compDtls.size());// 综合品种数
        bkrationDTO.setSingItemSize(bkrationDTO.getItemSize() - bkrationDTO.getCompItemSize());// 单品种数
        // 需要循环设置授信品种不可编辑项
        setDisItemCodes(compDtls, itemCodeList);
        return bkrationDTO;
    }

    /**
     * 功能介绍 : 设置品种中不可选品种。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setDisItemCodes(List<RatBkRationDtlVm> compDtls, List<String> itemCodeList) {
        String itemCodes = StringUtils.listTtoString(itemCodeList);
        for (RatBkRationDtlVm dtlDTO : compDtls) {
            String tempCode = itemCodes;
            String itemCode = dtlDTO.getItemCode();// 授信品种
            // 所有品种总去除当前品种即为不可选择的品种
            if (tempCode.indexOf(itemCode + ",") != -1) {
                dtlDTO.setDisItemCodes(tempCode.replaceAll(itemCode + ",", ""));// 不可编辑
                continue;
            } else if (tempCode.indexOf("," + itemCode) != -1) {
                dtlDTO.setDisItemCodes(tempCode.replaceAll("," + itemCode, ""));// 不可编辑
            }
        }
    }

    /**
     * 功能介绍 : 更新授信录入明细中已分配金额。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDisMoney(RatBkrationDisListDTO disDTO) {
        String ratCode = disDTO.getRatCode();// 授信录入ID
        List<RatBkrationDetail> bkrationDetails = detailService.getBkrationDetailByRatCode(ratCode);
        Map<String, RatBkrationDetail> dtlMap = new HashMap<String, RatBkrationDetail>();
        // 按授信品种分类
        for (RatBkrationDetail detail : bkrationDetails) {
            dtlMap.put(detail.getItemCode(), detail);
        }
        List<RatBkrationDisDtlDTO> rationDtls = disDTO.getRationDtls();
        for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            RatBkrationDetail detail = dtlMap.get(itemCode);
            if (null == detail) {
                // 新生成授信明细数据
                // createRationDtl(ratCode, dtlDTO);
                continue;
            }
            BigDecimal money = NumberUtil.formatNumericNull(detail.getSuperDistributeMoney());// 授信额度
            BigDecimal openMoney = NumberUtil.formatNumericNull(detail.getSuperDistributeOpenMoney());// 授信敞口额度
            detail.setSuperDistributeMoney(money.add(dtlDTO.getMoney()));// 已分配授信金额
            detail.setSuperDistributeOpenMoney(openMoney.add(dtlDTO.getOpenMoney()));// 已分配授信敞口金额
            detailService.updateEntity(detail);// 更新已分配金额
        }
    }

    /**
     * 功能介绍 : 获取已分配的授信录入明细。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkRationVm> getBkRationDised(RatBkrationDisListDTO disDTO) {
        List<RatBkRationVm> dtos = ratBkrationDao.getBkRationDised(disDTO);
        if (null == dtos || dtos.isEmpty()) {
            return new ArrayList<RatBkRationVm>();
        }
        List<String> ratCodes = new ArrayList<String>();
        for (RatBkRationVm dto : dtos) {
            ratCodes.add(dto.getId());
        }
        List<RatBkRationDtlVm> dtlDTOs = detailService.getBkrationDtlByRatCodes(ratCodes);
        setRattionListDTO(dtos, dtlDTOs);
        return dtos;
    }

    /**
     * 功能介绍 : 获取相同单位银行币别的其他已分配授信录入信息。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkRationVm> queryCorpOtherRation(RatUrationBeanDTO uration, String changeType) {
        return ratBkrationDao.queryCorpOtherRation(uration, changeType);
    }

    /**
     * 功能介绍 : 根据授信额度单ID获取授信信息。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkRationVm> queryRationByRatCode(RatUrationBeanDTO uration) {
        return ratBkrationDao.queryRationByRatCode(uration);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();// 业务ID
        boolean result = workFlowResult.isPass();// 是否通过
        ICurrentUser user = workFlowResult.getUser();
        String userId = user.getId();// 审批人
        getEntityDao().flush();
        RatBkration ration = get(businessKey, true);
        /**
         * 合同挂接处理
         */
        flowCompletedConHandle(ration, userService.getByUserId(ration.getCreateBy()), result);
        if (result) {
            ration.setStatus(RationConstant.STATUS_CHECK);
            update(ration);
            // 判断是否自主录入,则不需要分配,直接写入分配明细表
            processOwnRatMode(businessKey, userId);
        } else {
            ration.setStatus(RationConstant.STATUS_BACK);
            update(ration);
        }
        getEntityDao().flush();
        if (result && !EmptyUtils.isEmpty(ration.getOldRatCode())) {
            // 结转id不为空，判断是否能结转
            List<Integer> statuss = new ArrayList<>();
            statuss.add(RationConstant.RAT_USE_STATUS_TEMP);
            statuss.add(RationConstant.RAT_USE_STATUS_FREEZE);
            List<RatUrationDisBean> ratUrationDisBeans = ratUrationService.queryByCodeStatus(ration.getOldRatCode(),
                    statuss);
            if (ratUrationDisBeans.size() > 0) {
                throw new ParameterException(RatMessageEnum.CMS_CHANGE_RATBKRATION_CHECK);
            }
            RatBkration oldRat = get(ration.getOldRatCode());
            // 进行结转判断并进行结转
            if (oldRat.getStatus().equals(RationConstant.STATUS_CHECK)) {
                ratUrationService.addBkrationChange(ration.getOldRatCode(), ration.getId(), userId);
                oldRat.setStatus(RationConstant.STATUS_CLOSE);
                update(oldRat);
                this.getSession().flush();
                this.getSession().clear();
            } else {
                throw new ParameterException(RatMessageEnum.CMS_CHANGE_RATBKRATION_CLOSE);
            }
        }
        // 判断是否挂接担保,改变担保合同占用信息的状态
        ConfigDTO guaConfig = this.getMountGuarantee();// 是否挂接担保
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            if (ration.getIsHangGuarantee() != null
                    && ration.getIsHangGuarantee().equals(RationConstant.IS_HANG_GUA_1)) {// 挂接担保
                GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
                guaContractOccupDTO.setBusinessId(businessKey);
                if (ration.getStatus().equals(RationConstant.STATUS_CHECK)) {
                    contractOccupServie.saveUseGuaContractOccup(guaContractOccupDTO);
                } else if (ration.getStatus().equals(RationConstant.STATUS_BACK)) {
                    contractOccupServie.backUseGuaContractOccup(guaContractOccupDTO);
                }
            }
        }
    }

    /**
     * 定时任务，提醒授信到期
     */
    @Override
    @XxlJob("RatBkrationService")
    public void run(String arguments) {
        // 授信到期提醒天数：以英文逗号隔开的多个天数
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_NOTIFYDAYS);
        String notifyDays = configDTO.getValue();

        if (StringUtils.isNotEmpty(notifyDays)) {
            String[] notifyDayStr = notifyDays.split(CommonConstant.COMMA_SPLIT);
            for (String s : notifyDayStr) {
                if (EmptyUtils.isNumeric(s)) {
                    // 必须为数字类型
                    int notifyDay = Integer.parseInt(s);

                    Date endDate = DateUtils.getDateYMD();
                    endDate = DateUtils.addDay(endDate, notifyDay);

                    List<RatBkration> ratBkrations = ratBkrationDao.queryBkrationByEndDate(endDate);

                    if (!EmptyUtils.isEmptyList(ratBkrations)) {
                        for (RatBkration bkration : ratBkrations) {
                            RatBkrationVO bkrationVO = new RatBkrationVO();
                            BeanUtils.copyProperties(bkration, bkrationVO);
                            bkrationVO.setEndDateStr(DateUtil.formatDate(bkration.getEnddate(), "yyyy-MM-dd"));
                            notificationService.send(bkration.getId(), bkration.getCorpId(), NotificationConstant.CMS.RAT_MATURE, bkrationVO);
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据不同过去天数发送消息
     * @param notifyDays
     */
//    private void sendMsgWithNotifyDays(int notifyDays) {
//        Date endDate = DateUtils.getDateYMD();
//        endDate = DateUtils.addDay(endDate, notifyDays);
//        // 查询授信到期数据
//        List<RatBkration> ratBkrations = ratBkrationDao.queryBkrationListDTOs(endDate);
//        if (!EmptyUtils.isEmptyList(ratBkrations)) {
//            for (RatBkration bkration : ratBkrations) {
//                RatBkrationVO bkrationVO = new RatBkrationVO();
//                BeanUtils.copyProperties(bkration, bkrationVO);
//                notificationService.send(bkration.getId(), bkration.getCorpId(), NotificationConstant.CMS.RAT_MATURE, bkrationVO);
//                logger.info("授信合同到期提醒，编码：" + bkration.getRatCode() + "，合同：" + bkration.getContractNo());
//                bkration.setSendSign(CommonConstant.SEND_SIGN_YES);// 更改发送标识
//                update(bkration);
//            }
//        }
//    }

    /**
     * 新增保证金占用的相关信息
     *
     * @param bkrationDTO
     */
    public void addFcBondApply(RatBkration bkration, RatBkRationVm bkrationDTO, ICurrentUser user) {
        FcBondApplyDTO dto = this.setFcBondApplyDTO(bkration, bkrationDTO, user);
        fcBondHookService.addFcBondApply(dto);// 新增保证金占用的相关信息
    }

    /**
     * 修改保证金占用的相关信息
     *
     * @param bkrationDTO
     */
    public void updateFcBondApply(RatBkRationVm bkrationDTO, FcBondApplyDTO dto, ICurrentUser user) {
        dto.setBondPercent(bkrationDTO.getBondPercent());
        dto.setApplyMoney(bkrationDTO.getApplyMoney());
        dto.setBondContract(bkrationDTO.getBondContract());
        dto.setBankId(bkrationDTO.getBankCode());
        BigDecimal itemMoney = new BigDecimal(0);
        if ((bkrationDTO.getMoneysum()) != null) {
            itemMoney = itemMoney.add(bkrationDTO.getMoneysum());
        }
        if ((bkrationDTO.getOpenMoneysum()) != null) {
            itemMoney.add(bkrationDTO.getOpenMoneysum());
        }
        dto.setItemMoney(itemMoney);// 项目金额
        dto.setStartDate(bkrationDTO.getBegindate());// 项目开始时间
        dto.setEndDate(bkrationDTO.getEnddate());// 项目结束时间
        dto.setRemark(bkrationDTO.getRemark());
        dto.setStatus(FcBondConstant.STATUE_APPLY);// 占用状态
        dto.setBondId(bkrationDTO.getBondId());
        dto.setCorpId(bkrationDTO.getCorpId());// 单位id
        dto.setUpdateBy(user.getLoginName());
        dto.setUpdateDate(DateUtils.getDate());
        fcBondHookService.updateFcBondApply(dto);// 修改保证金占用的相关信息
    }

    /**
     * 设置FcBondApplyDTO各属性值
     *
     * @param bkrationDTO
     * @return
     */
    public FcBondApplyDTO setFcBondApplyDTO(RatBkration bkration, RatBkRationVm bkrationDTO, ICurrentUser user) {
        FcBondApplyDTO dto = new FcBondApplyDTO();
        dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);// 占用类型0:业务占用，1手工占用
        dto.setItemType(FcBondConstant.ITEM_TYPE_CREDIT);// 0银行授信1银行借款2承兑汇票
        dto.setItemNo(bkration.getRatCode());// 项目单号
        dto.setItemId(bkration.getId());// 占用保证金的项目Id
        dto.setCorpId(bkration.getCorpId());// 单位id
        dto.setBankId(bkration.getBankCode());// 联行号
        BigDecimal itemMoney = new BigDecimal(0);
        if ((bkrationDTO.getMoneysum()) != null) {
            itemMoney = itemMoney.add(bkrationDTO.getMoneysum());
        }
        if ((bkrationDTO.getOpenMoneysum()) != null) {
            itemMoney.add(bkrationDTO.getOpenMoneysum());
        }
        dto.setItemMoney(itemMoney);// 项目金额
        dto.setBondPercent(bkrationDTO.getBondPercent());
        dto.setStartDate(bkrationDTO.getBegindate());// 项目开始时间
        dto.setEndDate(bkrationDTO.getEnddate());// 项目结束时间
        // dto.setItemUnit(bkrationDTO.getItemUnit());//项目期限
        dto.setBondContract(bkrationDTO.getBondContract());// 保证金合同
        dto.setBondId(bkrationDTO.getBondId());// 保证金id
        dto.setApplyMoney(bkrationDTO.getApplyMoney());// 保证金金额,即占用金额
        dto.setRemark(bkrationDTO.getRemark());// 备注
        dto.setStatus(FcBondConstant.STATUE_APPLY);// 占用状态
        dto.setCreateBy(user.getLoginName());// 创建人登录名称
        dto.setCreateDate(DateUtils.getDate());// 创建日期
        return dto;
    }

    /**
     * 授信详情
     *
     * @param sysCode
     * @param billId
     * @return
     */
    public RatBkrationCommonDTO getRatCommonDTOByBillId(String sysCode, String billId) {
        List<RatUration> ratUrations = ratUrationDao.queryRatUrationById(sysCode, billId);
        RatBkrationCommonDTO dto = new RatBkrationCommonDTO();
        if (!EmptyUtils.isEmptyList(ratUrations)) {
            RatUration uration = ratUrations.get(0);
            String ratCode = uration.getRatCode();
            String iteamCode = uration.getItemCode();
            RatBkrationPageVO pageVO = getRatBkrationInfo(ratCode);
            BeanUtils.copyProperties(pageVO, dto);
            List<RatBkRationDtlVm> dtlDTOs = detailService.getBkrationDtlByRatAndIteamCode(ratCode, iteamCode);
            BigDecimal occupyLimit = uration.getMoney();
            for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
                dtlDTO.setMoney(dtlDTO.getMoney().add(dtlDTO.getSuperAddMoney()));
                dtlDTO.setOpenMoney(dtlDTO.getOpenMoney().add(dtlDTO.getSuperAddOpenMoney()));
                dtlDTO.setDisMoney(occupyLimit);
                // dtlDTO.setMaxMoney(dtlDTO.getMaxMoney().add(dtlDTO.getSuperAddMaxMoney()));
            }
            List<RatBkRationDtlVm> compRationDtls = new ArrayList<RatBkRationDtlVm>();
            List<RatBkRationDtlVm> singRationDtls = new ArrayList<RatBkRationDtlVm>();
            dto.setCompRationDtls(compRationDtls);// 综合品种明细
            dto.setSingRationDtls(singRationDtls);// 单品种明细
            BigDecimal money = BigDecimal.ZERO;
            BigDecimal openMoney = BigDecimal.ZERO;
            for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
                money = money.add(dtlDTO.getMoney());
                openMoney = openMoney.add(dtlDTO.getOpenMoney());
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    singRationDtls.add(dtlDTO);
                } else {
                    compRationDtls.add(dtlDTO);
                }
                dtlDTO.setIsOpenRat(uration.getIsOpenRat());
            }
            dto.setMoney(money);
            dto.setOpenMoney(openMoney);
            /*
             * dto.setItemSize(dtlDTOs.size()); dto.setSingItemSize(singRationDtls.size());//单品种条数
             */
            dto.setCompItemSize(compRationDtls.size());// 综合品种条数
        }
        return dto;
    }

    /**
     * 功能介绍 : 保存验证<br>
     */
    public void validata(RatBkRationVm rbl) {
        if (!EmptyUtils.isEmptyList(rbl.getRationDtls())) {
            for (RatBkRationDtlVm dtl : rbl.getRationDtls()) {
                if (EmptyUtils.isEmpty(dtl.getItemFlag())) {
                    continue;
                }
                if (EmptyUtils.isEmpty(dtl.getItemCode())) {
                    throw new ParameterException(RatMessageEnum.CMS_RAT_DTL_ITEMCODE_ISNULL);
                }
            }
        }
    }

    /**
     * 授信挂接合同处理
     *
     * @param ratBkration
     * @param currentUser
     */
    private void flowCompletedConHandle(RatBkration ratBkration, ICurrentUser currentUser, boolean result) {
        String relateCon = getRelateCon().getValue();
        String ratConType = getRatConType().getValue();
        // 判断是否挂接合同
        if (RationConstant.RAT_COMMON_Y.equals(relateCon)) {
            if (RationConstant.RAT_CONFIG_RATCONTYPE_0.equals(ratConType)) {// 判断挂接合同类型
                if (result) {
                    RatBkrationVO ratBkrationVO = new RatBkrationVO();
                    String corpName = corpService.get(ratBkration.getCorpId()).getName();
                    ConInfoDTO conInfoDTO = new ConInfoDTO();
                    conInfoDTO.setCorpId(ratBkration.getCorpId());
                    conInfoDTO.setBusId(ratBkration.getId());
                    conInfoDTO.setConName(corpName + "银行授信合同");
                    conInfoDTO.setTypeId(ratBkration.getConTypeId());
                    conInfoDTO.setBeginDate(ratBkration.getBegindate());
                    conInfoDTO.setEndDate(ratBkration.getEnddate());
                    BeanUtils.copyProperties(ratBkration, ratBkrationVO);
                    ratBkrationVO.setCurName(currencyService.getObjectById(ratBkration.getCurCode()).getCurName());
                    ratBkrationVO.setBeginDate(ratBkration.getBegindate());
                    ratBkrationVO.setEndDate(ratBkration.getEnddate());
                    conInfoDTO.setOutBusData(ratBkrationVO);
                    conInfoDTO.setConNo(ratBkration.getContractNo());
                    // 授信生成合同
                    conCommonService.registerConOp(conInfoDTO, RationConstant.RAT_CONFIG_CONPDTYPE, currentUser);
                    ratBkration.setConId(conInfoDTO.getId());
                }
            } else {
                if (!result) {
                    conCommonService.unHookConOp(ratBkration.getConId(), ratBkration.getId(), currentUser);
                    ratBkration.setConId(null);
                    ratBkration.setConTypeId(null);
                }
            }
        }
    }

    private void flowStartConHandle(RatBkration ratBkration, ICurrentUser currentUser) {
        String relateCon = getRelateCon().getValue();
        String ratConType = getRatConType().getValue();
        // 判断是否挂接合同
        if (RationConstant.RAT_COMMON_Y.equals(relateCon)) {
            if (RationConstant.RAT_CONFIG_RATCONTYPE_1.equals(ratConType)) {// 判断挂接合同类型
                if (StringUtils.isEmpty(ratBkration.getConId())) {
                    conCommonService.hookConOp(ratBkration.getConId(), ratBkration.getId(), currentUser);
                }
            }
        }
    }

    /**
     * 获取银行授信录入审批通过的数据
     *
     * @param input
     * @return
     * @author luxy
     */
    @Transactional(readOnly = true)
    public DataTablesOutput ratBkData(DataTablesInput input) {
        Map<String, Object> params = input.getFormdata();
        String ratDate = (String) params.get("ratDate");
        if (EmptyUtils.isNotEmpty(ratDate)) {
            params.put("led_sDate", DateUtils.formatDate(ratDate));// 大于等于授信开始日期
            params.put("ged_eDate", DateUtils.formatDate(ratDate));// 小于等于授信结束日期
        }
        params.remove("ratDate");
        input.setFormdata(params);
        return ratBkrationDao.ratBkData(input);
    }

    /**
     * 根据授信id更新授信挂接担保合同的参数
     *
     * @param ratId
     */
    public void updateGuaranteeConfig(String ratId, String configSatus) {
        RatBkration ratBkration = this.get(ratId);
        ratBkration.setIsHangGuarantee(configSatus);// 设置挂接担保合同参数
        this.saveOrUpdate(ratBkration);
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = ratBkrationDao.queryBusBillByCorpId(corpId, null);// 授信录入数据
            int disDataSize = ratBkrationDisDao.queryBusBillByCorpId(corpId, null);// 授信分配数据
            if (dataSize > 0 || disDataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DEL_BANKRATION_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = ratBkrationDao.queryBusBillByCorpId(corpId, "-1,0,11");// 授信录入在途数据
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_BANKRATION_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }

    public void checkBkrationInother(DataTablesOutput output) {
        List<RatBkrationVO> data = output.getData();
        for (RatBkrationVO vo : data) {
            boolean flag = ratBkrationDao.checkBkrationInother(vo.getId());// 授信录入是否关联其他业务，true 关联 false 不关联
            vo.setToUpdate(!flag);
        }
    }

    public void saveImport(RatBkrationVO vo, ICurrentUser user) {
        RatBkRationVm bkration = new RatBkRationVm();
        BeanUtils.copyNotNullProperties(vo, bkration);
        bkration.setStatus(RationConstant.STATUS_TEMP);
        bkration.setRatType(RationConstant.RAT_TYPE_0);
        bkration.setBegindate(vo.getBeginDate());
        bkration.setEnddate(vo.getEndDate());
        bkration.setRatCode(getRatCodeNo(SequenceEnum.RAT_BKRATION_CODE, RationConstant.RAT_BKRATION_CODE));// 授信编号
        CorpDTO corpDTO = corpService.get(bkration.getCorpId());
        if (CorpConstant.CORP_TYPE_CODE_NET.equals(corpDTO.getType())) {
            bkration.setNetCode(corpDTO.getId());
        } else {
            CorpDTO netDTO = corpService.getNetByCorpId(bkration.getCorpId());
            bkration.setNetCode(netDTO.getId());
        }

        this.setRatBkrationDtls(bkration);
        this.saveRatBkration(bkration, user);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(RatBkrationVO obj, Object... user) {
        return null;
    }

    @Override
    public void verifyHandler(List<RatBkrationVO> list, Object... user) {
        if (!ListUtils.isNullOrEmpty(list)) {
            String userId = null;
            if (!EmptyUtils.isEmpty(user) && !EmptyUtils.isEmpty(user[0])) {
                ICurrentUser userInfo = (ICurrentUser) user[0];
                userId = userInfo.getId();
            }
            for (RatBkrationVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 单位
                if (EmptyUtils.isEmpty(vo.getCorpName())) {
                    errorMsgList.add(RationConstant.CMS_RATBKRATION_CORP_ISNULL);
                } else {
                    CorpDTO corpDTO = corpService.getCorpIdByName(vo.getCorpName());
                    if (EmptyUtils.isEmpty(corpDTO)) {
                        errorMsgList.add(RationConstant.CMS_RATBKRATION_CORP_ERROR);
                    } else {
                        vo.setCorpId(corpDTO.getId());
                        // 单位权限
                        List<String> corpIds = corpService.getUserCorpIds(RationConstant.CMS_RATION_RATION_APPLY, userId);
                        if (!corpIds.contains(corpDTO.getId())) {
                            errorMsgList.add("当前用户没有该授信单位权限！");
                        }
                    }
                }
                // 授信模式
                if (EmptyUtils.isEmpty(vo.getRatModeName())) {
                    errorMsgList.add(RationConstant.CMS_RATBKRATION_MODEL_ISNULL);
                } else {
                    String ratMode = dictService.getCodeByName("RatBkration.RatMode", vo.getRatModeName());
                    if (EmptyUtils.isEmpty(ratMode)) {
                        errorMsgList.add(RationConstant.CMS_RATBKRATION_MODEL_ERROR);
                    } else {
                        vo.setRatMode(ratMode);
                        if (ratMode.equals("0") && !EmptyUtils.isEmpty(vo.getCorpId())) {
                            List<CorpDTO> corpDTOs = corpService.getRatGroupCorpNet(RationConstant.CMS_RATION_RATION_APPLY, userId);
                            boolean flag = true;
                            for (CorpDTO corpDTO : corpDTOs) {
                                if (corpDTO.getId().equals(vo.getCorpId())) {
                                    flag = false;
                                }
                            }
                            if (flag) {
                                errorMsgList.add(RationConstant.CMS_RATBKRATION_CORP_MODEL);
                            }
                        }
                    }
                }
                //授信合同号
                if (EmptyUtils.isEmpty(vo.getContractNo())) {
                    // 授信合同号不能为空
                    errorMsgList.add("授信合同号不能为空！");
                } else {
                    if (vo.getContractNo().length() > 32) {
                        errorMsgList.add("授信合同号字符长度不能大于32！");
                    }
                }
                // 银行信息
                if (EmptyUtils.isEmpty(vo.getBankCode())) {
                    errorMsgList.add(RationConstant.CMS_RATBKRATION_BANKCODE_ISNULL);
                } else {
                    BtBankDTO btBankDTO = btBankService.getByBankCode(vo.getBankCode());
                    if (EmptyUtils.isEmpty(btBankDTO)) {
                        errorMsgList.add(RationConstant.CMS_RATBKRATION_BANK_ERROR);// 借款银行不存在
                    } else {
                        vo.setBankName(btBankDTO.getBankName());
                        if (StringUtils.isNotEmpty(btBankDTO.getBankTypeId())) {
                            vo.setBankType(btBankDTO.getBankTypeId());
                            vo.setBankTypeName(bankTypeService.getObjectById(btBankDTO.getBankTypeId()).getTypeName());
                        } else {
                            List<BankTypeDTO> bankTypeDTOS = bankTypeService.listBySignOrPrefix(null, btBankDTO.getBankPrefix());
                            if (bankTypeDTOS.size() > 0) {
                                vo.setBankType(bankTypeDTOS.get(0).getId());
                                vo.setBankTypeName(bankTypeService.getObjectById(bankTypeDTOS.get(0).getId()).getTypeName());
                            } else {
                                errorMsgList.add("银行类别不能为空！");//银行类别不能为空
                            }
                        }
                    }
                }
                // 授信币种
                if (EmptyUtils.isEmpty(vo.getCurName())) {
                    errorMsgList.add(RationConstant.CMS_GUACONTRACT_CUR_ISNULL);
                } else {
                    String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                    if (EmptyUtils.isEmpty(curId)) {
                        errorMsgList.add(RationConstant.CMS_GUACONTRACT_CUR_ERROR);
                    } else {
                        vo.setCurCode(curId);
                    }
                }
                // 授信时间
                if (EmptyUtils.isEmpty(vo.getBeginDate())) {
                    errorMsgList.add(RationConstant.CMS_RATBKRATION_STARTDATE_ISNULL);
                }
                if (EmptyUtils.isEmpty(vo.getEndDate())) {
                    errorMsgList.add(RationConstant.CMS_RATBKRATION_ENDDATE_ISNULL);
                }
                if (!EmptyUtils.isEmpty(vo.getBeginDate()) && !EmptyUtils.isEmpty(vo.getEndDate())) {
                    if (vo.getBeginDate().compareTo(vo.getEndDate()) > 0) {
                        errorMsgList.add(RationConstant.CMS_RATBKRATION_DATE_ERROR);
                    }
                }
                // 结转授信编号
                if (!EmptyUtils.isEmpty(vo.getOldRatCodeCode()) && !EmptyUtils.isEmpty(vo.getRatMode())) {
                    if (vo.getRatMode().equals("1")) {
                        if (StringUtils.isNotEmpty(vo.getCurCode()) && StringUtils.isNotEmpty(vo.getCorpId()) && StringUtils.isNotEmpty(vo.getBankCode())) {
                            RatBkRationVm uration = new RatBkRationVm();
                            uration.setCurCode(vo.getCurCode());
                            uration.setCorpId(vo.getCorpId());
                            uration.setBankCode(vo.getBankCode());
                            uration.setBankType(vo.getBankType());
                            List<RatBkRationVm> ratList = this.queryRationAll(uration);
                            String oratCode = null;
                            for (RatBkRationVm ratBk : ratList) {
                                if (ratBk.getRatCode().equals(vo.getOldRatCodeCode())) {
                                    oratCode = ratBk.getId();
                                }
                            }
                            if (StringUtils.isNotEmpty(oratCode)) {
                                vo.setOldRatCode(oratCode);
                            } else {
                                errorMsgList.add(RationConstant.CMS_RATBKRATION_ORAT_ERROR);
                            }
                        }
                    } else {
                        errorMsgList.add(RationConstant.CMS_RATBKRATION_RATCAN_ERROR);
                    }
                }
                //授信类型
                if (!EmptyUtils.isEmpty(vo.getItemFlagName())) {
                    String itemFlag = dictService.getCodeByName("RatModel.ItemFlag", vo.getItemFlagName());
                    if (EmptyUtils.isEmpty(itemFlag)) {
                        errorMsgList.add("授信类型不存在");
                    } else {
                        vo.setItemFlag(itemFlag);
                    }
                } else {
                    errorMsgList.add("授信类型不能为空");
                }
                //授信品种
                if (!EmptyUtils.isEmpty(vo.getItemCodeName())) {
                    String itemCode = ratItemCustomService.getItemCodeByName(vo.getItemCodeName(), "0");
                    if (EmptyUtils.isEmpty(itemCode)) {
                        errorMsgList.add("授信品种不存在");
                    } else {
                        vo.setItemCode(itemCode);
                    }
                } else {
                    errorMsgList.add("授信品种不能为空");
                }
                //授信方式
                if (!EmptyUtils.isEmpty(vo.getCreditTypeName())) {
                    String creditType = dictService.getCodeByName("rat.creditType", vo.getCreditTypeName());
                    if (EmptyUtils.isEmpty(creditType)) {
                        errorMsgList.add("授信方式不存在");
                    } else {
                        vo.setCreditType(creditType);
                    }
                } else {
                    errorMsgList.add("授信方式不能为空");
                }
                //授信金额
                if (EmptyUtils.isEmpty(vo.getMoney())) {
                    errorMsgList.add("授信金额不能为空");
                }
                // 综合品种时，最高额度不能为空，并且最高额度不能大于授信额度
                if (RationConstant.RAT_COMFLAG_1.equals(vo.getItemFlag())) {
                    if (EmptyUtils.isEmpty(vo.getMaxMoney())) {
                        errorMsgList.add("最高额度不能为空");
                    } else {
                        // 最高额度不能大于授信额度
                        if (vo.getMaxMoney().compareTo(vo.getMoney()) > 0) {
                            errorMsgList.add("最高额度不能大于授信金额");
                        }
                    }
                }
                vo.addErrorMsg(errorMsgList);
            }
            /*
             * 判断在同一笔授信记录中，综合品种相同时，授信金额需要保持一致
             * 判断在同一笔授信记录中，授信品种不能重复
             */
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    //防止空指针异常
                    if (StringUtils.isNotEmpty(list.get(i).getItemFlag()) && StringUtils.isNotEmpty(list.get(j).getItemFlag()) && StringUtils.isNotEmpty(list.get(i).getItemCode()) && StringUtils.isNotEmpty(list.get(j).getItemCode())) {
                        //key表示是否为同一笔授信的标识
                        String key1 = list.get(i).getRatModeName() + "," + list.get(i).getCorpName() + "," + list.get(i).getContractNo() + ","
                                + list.get(i).getBankCode() + "," + list.get(i).getCurName() + "," + list.get(i).getOldRatCodeCode() + ","
                                + (null == list.get(i).getBeginDate() ? "" : DateUtils.formatDate(list.get(i).getBeginDate())) + ","
                                + (null == list.get(i).getEndDate() ? "" : DateUtils.formatDate(list.get(i).getEndDate()));
                        String key2 = list.get(j).getRatModeName() + "," + list.get(j).getCorpName() + "," + list.get(j).getContractNo() + ","
                                + list.get(j).getBankCode() + "," + list.get(j).getCurName() + "," + list.get(j).getOldRatCodeCode() + ","
                                + (null == list.get(j).getBeginDate() ? "" : DateUtils.formatDate(list.get(j).getBeginDate())) + ","
                                + (null == list.get(j).getEndDate() ? "" : DateUtils.formatDate(list.get(j).getEndDate()));
                        //一笔记录与除自身外的所有记录比较，如果为同一笔授信，并且都为综合品种，并且授信金额不同
                        if (i != j && key1.equals(key2) && list.get(i).getItemFlag().equals(RationConstant.RAT_COMFLAG_1) && list.get(j).getItemFlag().equals(RationConstant.RAT_COMFLAG_1) &&
                                list.get(i).getMoney().compareTo(list.get(j).getMoney()) != 0) {
                            list.get(j).setErrorMsg("同一笔授信综合品种相同时，授信金额不一致");
                        }
                        //一笔记录与除自身外的所有记录比较，如果为同一笔授信，不能够有相同的授信品种
                        if (i != j && key1.equals(key2) && list.get(i).getItemCode().equals(list.get(j).getItemCode())) {
                            list.get(j).setErrorMsg("同一笔授信，授信品种不能相同");
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据id集合获取授信数据
     *
     * @param input 查询参数
     * @return 列表数据
     */
    public DataTablesOutput<GuaContractOccupVO> getGuaForRat(DataTablesInput input, ICurrentUser user) {
        String sysCode = StrUtil.toStringOrNull(input.getFormdata().get("eqs_sysCode"));
        String startDate = StrUtil.toStringOrNull(input.getFormdata().get("ged_useDate"));
        String endDate = StrUtil.toStringOrNull(input.getFormdata().get("led_useDate"));
        String status = StrUtil.toStringOrNull(input.getFormdata().get("eqg_status"));
        String contractId = StrUtil.toStringOrNull(input.getFormdata().get("eqs_contractId"));
        // 当contractId不为空时表示被担保合同使用情况
        //获取挂接授信的业务id集合和封装参数
        List<GuaContractOccupDTO> guaList;
        if (StrUtil.isBlank(contractId)) {
            guaList = contractOccupServie.listUseGua(sysCode, startDate, endDate, status);
        } else {
            guaList = contractOccupServie.getGua(sysCode, startDate, endDate, status,contractId);
        }
        List<String> corpIds = corpService.getUserCorpIds(GuaranteeConstant.CMS_GUARANTEE_CHANGE_QUERY, user.getId());
        return buildGuaContractVO(input, guaList, corpIds);
    }

    private DataTablesOutput<GuaContractOccupVO> buildGuaContractVO(DataTablesInput input, List<GuaContractOccupDTO> guaList, List<String> corpIds) {
        if (EmptyUtils.isEmpty(guaList)) {
            return new DataTablesOutput<>();
        }
        Map<String, GuaContractOccupDTO> guaMap = new HashMap<>(guaList.size());
        List<String> businessIdList = new ArrayList<>();
        guaList.forEach(item -> {
            guaMap.put(item.getBusinessId(), item);
            businessIdList.add(item.getBusinessId());
        });
        //组装参数
        DataTablesOutput<GuaContractOccupVO> dataTablesOutput = ratBkrationDao.getGuaForRat(input, corpIds, businessIdList);
        if (EmptyUtils.isNotEmpty(dataTablesOutput.getData())) {
            for (GuaContractOccupVO item : dataTablesOutput.getData()) {
                if (guaMap.containsKey(item.getId())) {
                    item.setStatus(guaMap.get(item.getId()).getStatus());
                    item.setUseDate(guaMap.get(item.getId()).getUseDate());
                    item.setSysCode(guaMap.get(item.getId()).getSysCode());
                    item.setContractId(guaMap.get(item.getId()).getContractCode());
                    item.setSysCode(guaMap.get(item.getId()).getSysCode());
                }
            }
        }
        return dataTablesOutput;
    }

    /**
     * 获取所有挂接担保的授信数据
     */
    public List<com.bytter.credit.dto.GuaContractOccupDTO> getUseContractList() {
        return ratBkrationDao.getUseContractList();
    }

    /**
     * 银行授信额度导出
     *
     * @param output            数据
     * @param sheetName         sheet页名称
     * @param isRb              是否是银行授信额度录入菜单，此菜单多了，担保合同开始和结束时间与创建人列
     * @param isHasDisCorpCol   是否添加额度使用单位列
     * @param isHasDelayDateCol 是否添加授信延期日期列
     * @param isRatDis          【最高额度调整】菜单：【授信分配】页签
     * @return 表格
     */
    public HSSFWorkbook exportListRatBkration(DataTablesOutput output, String sheetName, String isRb, String isHasDisCorpCol, String isHasDelayDateCol, String isRatDis) {
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet(sheetName);
        if (isRatDis.equals(CommonConstant.YES)) {
            // 设置列宽
            this.setColumnWidthDis(sheet);
            // 创建Excel表头
            createExcelHeaderDis(workbook, sheet);
            // 创建Excel表中的内容
            createExcelBodyDis(workbook, sheet, output.getData());
        } else {
            // 设置列宽
            this.setColumnWidth(sheet, isRb, isHasDisCorpCol, isHasDelayDateCol);
            // 创建Excel表头
            createExcelHeader(workbook, sheet, isRb, isHasDisCorpCol, isHasDelayDateCol);
            // 创建Excel表中的内容
            createExcelBody(workbook, sheet, output.getData(), isRb, isHasDisCorpCol, isHasDelayDateCol);
        }
        return workbook;
    }

    /**
     * 【最高额度调整】菜单：【授信分配】页签
     *
     * @param workbook 表格对象
     * @param sheet    当前页
     * @param data     数据
     */
    private void createExcelBodyDis(HSSFWorkbook workbook, HSSFSheet sheet, List<RatBkrationDisVO> data) {
        // 居中样式
        HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(workbook, true);
        // 金额靠右
        HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(workbook, true);
        DataFormat currencyDataFormat = workbook.createDataFormat();
        rightCellStyle.setDataFormat(currencyDataFormat.getFormat("#,##0.00"));

        // 指向所在行
        Map<String, String> statusMap = new HashMap<>();
        Map<String, String> creditTypeMap = new HashMap<>();
        Map<String, String> itemFlagMap = new HashMap<>();
        Map<String, String> distributeWayMap = new HashMap<>();
        Integer index = 1;
        Integer dIndex = 0;
        Integer cIndex = 0;
        getDictGroup(null, statusMap, creditTypeMap, itemFlagMap, distributeWayMap);
        for (RatBkrationDisVO ratBkrationVO : data) {
            List<RatBkRationDtlVm> rationDtls = ratBkrationVO.getRationDtls();
            // 过滤出单品种集合
            List<RatBkRationDtlVm> singleItemList = rationDtls.stream().filter(single -> RationConstant.RAT_COMFLAG_0.equals(single.getItemFlag())).collect(Collectors.toList());
            // 过滤出综合品种集合
            List<RatBkRationDtlVm> compItemList = rationDtls.stream().filter(comp -> RationConstant.RAT_COMFLAG_1.equals(comp.getItemFlag())).collect(Collectors.toList());
            // 分别设置单品种与综合品种数量
            ratBkrationVO.setSingItemSize(singleItemList.size());
            ratBkrationVO.setCompItemSize(compItemList.size());

            // 创建行
            HSSFRow row = sheet.createRow(index);
            if (index != dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize()) {
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 4, 4));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 5, 5));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 11, 11));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 12, 12));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 13, 13));
            }

            //分配编号
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellStyle(centerCellStyle);
            cell1.setCellValue(ratBkrationVO.getDisCode());

            //授信单位
            HSSFCell cell2 = row.createCell(1);
            cell2.setCellStyle(centerCellStyle);
            cell2.setCellValue(ratBkrationVO.getRatCorpName());

            //额度使用单位
            HSSFCell cell3 = row.createCell(2);
            cell3.setCellStyle(centerCellStyle);
            cell3.setCellValue(ratBkrationVO.getCorpName());

            //银行信息
            HSSFCell cell4 = row.createCell(3);
            cell4.setCellStyle(centerCellStyle);
            cell4.setCellValue(ratBkrationVO.getBankName());

            //分配方式
            HSSFCell cell5 = row.createCell(4);
            cell5.setCellStyle(centerCellStyle);
            cell5.setCellValue(distributeWayMap.get(ratBkrationVO.getDistributeWay()));

            //授信币种
            HSSFCell cell6 = row.createCell(5);
            cell6.setCellStyle(centerCellStyle);
            cell6.setCellValue(ratBkrationVO.getCurName());

            //授信开始时间
            HSSFCell cell12 = row.createCell(11);
            cell12.setCellStyle(centerCellStyle);
            cell12.setCellValue(DateUtils.formatDate(ratBkrationVO.getBeginDate()));

            //授信结束时间
            HSSFCell cell13 = row.createCell(12);
            cell13.setCellStyle(centerCellStyle);
            cell13.setCellValue(DateUtils.formatDate(ratBkrationVO.getEndDate()));

            //状态
            HSSFCell cell14 = row.createCell(13);
            cell14.setCellStyle(centerCellStyle);
            if (RationConstant.RAT_COMMON_Y.equals(ratBkrationVO.getOverdueSign())) {
                cell14.setCellValue(multiLangServiceI.getLang("common.ration.overdue"));
            } else {
                cell14.setCellValue(statusMap.get(ratBkrationVO.getStatus().toString()));
            }

            // 单品种明细集合
            if (!CollectionUtils.isEmpty(singleItemList)) {
                for (RatBkRationDtlVm rationDtl : singleItemList) {
                    if (cIndex != 0) {
                        HSSFRow rowDtl = sheet.createRow(index);
                        // 创建单元格格式
                        for (int k = 0; k <= 5; k++) {
                            rowDtl.createCell(k);
                            rowDtl.getCell((short) k).setCellStyle(centerCellStyle);
                        }
                        //授信类型
                        HSSFCell cell7 = rowDtl.createCell(6);
                        cell7.setCellStyle(centerCellStyle);
                        cell7.setCellValue("");
                        if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                            cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                        }
                        //授信品种
                        HSSFCell cell8 = rowDtl.createCell(7);
                        cell8.setCellStyle(centerCellStyle);
                        cell8.setCellValue(rationDtl.getItemName());
                        //授信方式
                        HSSFCell cell9 = rowDtl.createCell(8);
                        cell9.setCellStyle(centerCellStyle);
                        cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                        //授信额度
                        HSSFCell cell10 = rowDtl.createCell(9);
                        cell10.setCellStyle(rightCellStyle);
                        cell10.setCellValue(rationDtl.getMoney().doubleValue());
                        //最高额度
                        HSSFCell cell11 = rowDtl.createCell(10);
                        cell11.setCellStyle(rightCellStyle);
                        cell11.setCellValue(rationDtl.getMaxMoney().doubleValue());
                        for (int k = 11; k <= 13; k++) {
                            rowDtl.createCell(k);
                            rowDtl.getCell((short) k).setCellStyle(centerCellStyle);
                        }
                    } else {
                        //授信类型
                        HSSFCell cell7 = row.createCell(6);
                        cell7.setCellStyle(centerCellStyle);
                        cell7.setCellValue("");
                        if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                            cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                        }
                        //授信品种
                        HSSFCell cell8 = row.createCell(7);
                        cell8.setCellStyle(centerCellStyle);
                        cell8.setCellValue(rationDtl.getItemName());
                        //授信方式
                        HSSFCell cell9 = row.createCell(8);
                        cell9.setCellStyle(centerCellStyle);
                        cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                        //授信额度
                        HSSFCell cell10 = row.createCell(9);
                        cell10.setCellValue(rationDtl.getMoney().doubleValue());
                        cell10.setCellStyle(rightCellStyle);
                        //最高额度
                        HSSFCell cell11 = row.createCell(10);
                        cell11.setCellValue(rationDtl.getMaxMoney().doubleValue());
                        cell11.setCellStyle(rightCellStyle);
                        cIndex = 1;
                    }
                    dIndex++;
                    index++;
                }
            }

            // 综合品种明细集合
            if (!CollectionUtils.isEmpty(compItemList)) {
                // 综合品种授信类型分组，并对每个综合品种的授信品种进行排序
                Map<String, List<RatBkRationDtlVm>> map = compItemList
                        .stream()
                        .collect(Collectors.groupingBy(RatBkRationDtlVm::getItemFlag))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().stream().sorted(Comparator.comparing(RatBkRationDtlVm::getCompSign, Comparator.nullsFirst(String::compareTo)).reversed()).collect(Collectors.toList())));

                for (Map.Entry<String, List<RatBkRationDtlVm>> entry : map.entrySet()) {
                    List<RatBkRationDtlVm> v = entry.getValue();

                    if (index != dIndex + v.size()) {
                        sheet.addMergedRegion(new CellRangeAddress(index, dIndex + v.size(), 6, 6));
                        sheet.addMergedRegion(new CellRangeAddress(index, dIndex + v.size(), 10, 10));
                    }

                    for (RatBkRationDtlVm rationDtl : v) {
                        if (cIndex != 0) {
                            HSSFRow rowDtl = sheet.createRow(index);
                            for (int k = 0; k <= 5; k++) {
                                rowDtl.createCell(k);
                                rowDtl.getCell((short) k).setCellStyle(centerCellStyle);
                            }
                            //授信类型
                            HSSFCell cell7 = rowDtl.createCell(6);
                            cell7.setCellStyle(centerCellStyle);
                            cell7.setCellValue("");
                            if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                                cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                            }
                            //授信品种
                            HSSFCell cell8 = rowDtl.createCell(7);
                            cell8.setCellStyle(centerCellStyle);
                            cell8.setCellValue(rationDtl.getItemName());
                            //授信方式
                            HSSFCell cell9 = rowDtl.createCell(8);
                            cell9.setCellStyle(centerCellStyle);
                            cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                            //授信额度
                            HSSFCell cell10 = rowDtl.createCell(9);
                            cell10.setCellStyle(rightCellStyle);
                            cell10.setCellValue(rationDtl.getMoney().doubleValue());
                            //最高额度
                            HSSFCell cell11 = rowDtl.createCell(10);
                            cell11.setCellStyle(rightCellStyle);
                            cell11.setCellValue(rationDtl.getMaxMoney().doubleValue());
                            for (int k = 11; k <= 13; k++) {
                                rowDtl.createCell(k);
                                rowDtl.getCell((short) k).setCellStyle(centerCellStyle);
                            }
                        } else {
                            //授信类型
                            HSSFCell cell7 = row.createCell(6);
                            cell7.setCellStyle(centerCellStyle);
                            cell7.setCellValue("");
                            if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                                cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                            }
                            //授信品种
                            HSSFCell cell8 = row.createCell(7);
                            cell8.setCellStyle(centerCellStyle);
                            cell8.setCellValue(rationDtl.getItemName());
                            //授信方式
                            HSSFCell cell9 = row.createCell(8);
                            cell9.setCellStyle(centerCellStyle);
                            cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                            //授信额度
                            HSSFCell cell10 = row.createCell(9);
                            cell10.setCellValue(rationDtl.getMoney().doubleValue());
                            cell10.setCellStyle(rightCellStyle);
                            //最高额度
                            HSSFCell cell11 = row.createCell(10);
                            cell11.setCellStyle(rightCellStyle);
                            cell11.setCellValue(rationDtl.getMaxMoney().doubleValue());
                            cIndex = 1;
                        }
                        dIndex++;
                        index++;
                    }
                }
            }
            index = dIndex + 1;
            cIndex = 0;
        }
    }

    /**
     * 设置列宽
     *
     * @param sheet sheet页
     */
    private void setColumnWidthDis(HSSFSheet sheet) {
        // 设置单元格默认的列宽
        sheet.setDefaultColumnWidth((short) 20);
        // 分配编号
        sheet.setColumnWidth(0, 20 * 256);
        // 授信单位
        sheet.setColumnWidth(1, 35 * 256);
        // 额度使用单位
        sheet.setColumnWidth(2, 35 * 256);
        // 银行信息
        sheet.setColumnWidth(3, 35 * 256);
        for (int i = 4; i < 13; i++) {
            sheet.setColumnWidth(i, 20 * 256);
        }
    }

    /**
     * 设置列宽
     *
     * @param sheet             sheet页
     * @param isRb              是否是银行授信额度录入菜单，此菜单多了，担保合同开始和结束时间与创建人列
     * @param isHasDisCorpCol   是否添加额度使用单位列
     * @param isHasDelayDateCol 是否添加授信延期日期列
     */
    private void setColumnWidth(HSSFSheet sheet, String isRb, String isHasDisCorpCol, String isHasDelayDateCol) {
        // 设置单元格默认的列宽
        sheet.setDefaultColumnWidth((short) 20);
        if (CommonConstant.YES.equals(isRb)) {
            for (int i = 0; i < 3; i++) {
                sheet.setColumnWidth(i, 20 * 256);
            }
            // 授信单位
            sheet.setColumnWidth(3, 35 * 256);
            // 银行信息
            sheet.setColumnWidth(4, 35 * 256);
            for (int i = 5; i < 17; i++) {
                sheet.setColumnWidth(i, 20 * 256);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                sheet.setColumnWidth(i, 20 * 256);
            }
            if (CommonConstant.YES.equals(isHasDisCorpCol)) {
                // 授信单位
                sheet.setColumnWidth(3, 35 * 256);
                // 额度使用单位
                sheet.setColumnWidth(4, 35 * 256);
                // 银行信息
                sheet.setColumnWidth(5, 35 * 256);
                if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                    for (int i = 6; i < 15; i++) {
                        sheet.setColumnWidth(i, 20 * 256);
                    }
                } else {
                    for (int i = 6; i < 14; i++) {
                        sheet.setColumnWidth(i, 20 * 256);
                    }
                }
            } else {
                // 授信单位
                sheet.setColumnWidth(3, 35 * 256);
                // 银行信息
                sheet.setColumnWidth(4, 35 * 256);
                if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                    for (int i = 5; i < 14; i++) {
                        sheet.setColumnWidth(i, 20 * 256);
                    }
                } else {
                    for (int i = 5; i < 13; i++) {
                        sheet.setColumnWidth(i, 20 * 256);
                    }
                }
            }
        }
    }

    /**
     * 设置单元格样式
     *
     * @param row       某行
     * @param cellStyle 单元格样式
     */
    private void setCellStyle(HSSFRow row, HSSFCellStyle cellStyle) {
        Iterator<Cell> iterator = row.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            cell.setCellStyle(cellStyle);
        }
    }

    /**
     * 【最高额度调整】菜单：【授信分配】页签
     *
     * @param workbook 表格对象
     * @param sheet    表格
     */
    private void createExcelHeaderDis(HSSFWorkbook workbook, HSSFSheet sheet) {
        // 设置表头样式
        HSSFCellStyle headStyle = ExportUtils.getCenterCellStyle(workbook, true);
        // 设置表头字体
        headStyle.setFont(ExportUtils.getHeadFont(workbook, "宋体", (short) 12));
        // 第一行
        HSSFRow row1 = sheet.createRow(0);
        row1.setHeightInPoints(20);
        row1.createCell(0).setCellValue("分配编号");
        row1.createCell(1).setCellValue("授信单位");
        row1.createCell(2).setCellValue("额度使用单位");
        row1.createCell(3).setCellValue("银行信息");
        row1.createCell(4).setCellValue("分配方式");
        row1.createCell(5).setCellValue("授信币种");
        row1.createCell(6).setCellValue("授信类型");
        row1.createCell(7).setCellValue("授信品种");
        row1.createCell(8).setCellValue("授信方式");
        row1.createCell(9).setCellValue("授信额度");
        row1.createCell(10).setCellValue("最高额度");
        row1.createCell(11).setCellValue("授信开始时间");
        row1.createCell(12).setCellValue("授信结束时间");
        row1.createCell(13).setCellValue("状态");
        this.setCellStyle(row1, headStyle);
    }

    /**
     * 创建其他表格
     *
     * @param workbook          表格对象
     * @param sheet             表格
     * @param isRb              是否是银行授信额度录入菜单，此菜单多了，担保合同开始和结束时间与创建人列
     * @param isHasDisCorpCol   是否添加额度使用单位列
     * @param isHasDelayDateCol 是否添加授信延期日期列
     */
    private void createExcelHeader(HSSFWorkbook workbook, HSSFSheet sheet, String isRb, String isHasDisCorpCol, String isHasDelayDateCol) {
        // 设置表头样式
        HSSFCellStyle headStyle = ExportUtils.getCenterCellStyle(workbook, true);
        // 设置表头字体
        headStyle.setFont(ExportUtils.getHeadFont(workbook, "宋体", (short) 12));
        // 第一行
        HSSFRow row1 = sheet.createRow(0);
        row1.setHeightInPoints(20);
        row1.createCell(0).setCellValue("授信模式");
        row1.createCell(1).setCellValue("授信编号");
        row1.createCell(2).setCellValue("授信合同号");
        row1.createCell(3).setCellValue("授信单位");
        if (isRb.equals(CommonConstant.YES)) {
            // 银行授信额度录入
            row1.createCell(4).setCellValue("银行信息");
            row1.createCell(5).setCellValue("授信币种");
            row1.createCell(6).setCellValue("授信类型");
            row1.createCell(7).setCellValue("授信品种");
            row1.createCell(8).setCellValue("授信方式");
            row1.createCell(9).setCellValue("最高额度");
            row1.createCell(10).setCellValue("授信额度");
            row1.createCell(11).setCellValue("授信开始时间");
            row1.createCell(12).setCellValue("授信结束时间");
            row1.createCell(13).setCellValue("担保开始时间");
            row1.createCell(14).setCellValue("担保结束时间");
            row1.createCell(15).setCellValue("创建人");
            row1.createCell(16).setCellValue("状态");
        } else {
            // 其他
            if (isHasDisCorpCol.equals(CommonConstant.YES)) {
                row1.createCell(4).setCellValue("额度使用单位");
                row1.createCell(5).setCellValue("银行信息");
                row1.createCell(6).setCellValue("授信币种");
                row1.createCell(7).setCellValue("授信类型");
                row1.createCell(8).setCellValue("授信品种");
                row1.createCell(9).setCellValue("授信方式");
                row1.createCell(10).setCellValue("授信额度");
                row1.createCell(11).setCellValue("授信开始时间");
                row1.createCell(12).setCellValue("授信结束时间");
                if (isHasDelayDateCol.equals(CommonConstant.YES)) {
                    row1.createCell(13).setCellValue("授信延期日期");
                    row1.createCell(14).setCellValue("状态");
                } else {
                    row1.createCell(13).setCellValue("状态");
                }
            } else {
                row1.createCell(4).setCellValue("银行信息");
                row1.createCell(5).setCellValue("授信币种");
                row1.createCell(6).setCellValue("授信类型");
                row1.createCell(7).setCellValue("授信品种");
                row1.createCell(8).setCellValue("授信方式");
                row1.createCell(9).setCellValue("授信额度");
                row1.createCell(10).setCellValue("授信开始时间");
                row1.createCell(11).setCellValue("授信结束时间");
                if (isHasDelayDateCol.equals(CommonConstant.YES)) {
                    row1.createCell(12).setCellValue("授信延期日期");
                    row1.createCell(13).setCellValue("状态");
                } else {
                    row1.createCell(12).setCellValue("状态");
                }
            }
        }
        this.setCellStyle(row1, headStyle);
    }

    /**
     * 设置表格内容
     *
     * @param workbook          表格
     * @param sheet             当前页
     * @param data              数据
     * @param isRb              是否是银行授信额度录入菜单，此菜单多了，担保合同开始和结束时间与创建人列
     * @param isHasDisCorpCol   是否添加额度使用单位列
     * @param isHasDelayDateCol 是否添加授信延期日期列
     */
    private void createExcelBody(HSSFWorkbook workbook, HSSFSheet sheet, List<RatBkrationVO> data, String isRb, String isHasDisCorpCol, String isHasDelayDateCol) {
        // 居中样式
        HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(workbook, true);
        // 居右样式
        HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(workbook, true);
        DataFormat currencyDataFormat = workbook.createDataFormat();
        rightCellStyle.setDataFormat(currencyDataFormat.getFormat("#,##0.00"));

        // 数据字典
        Map<String, String> modeMap = new HashMap<>();
        Map<String, String> statusMap = new HashMap<>();
        Map<String, String> creditTypeMap = new HashMap<>();
        Map<String, String> itemFlagMap = new HashMap<>();
        getDictGroup(modeMap, statusMap, creditTypeMap, itemFlagMap, null);

        // 指向所在行
        Map<String, Integer> indexMap = new HashMap<>();
        indexMap.put("index", 1);
        indexMap.put("dIndex", 0);
        indexMap.put("cIndex", 0);

        for (RatBkrationVO ratBkrationVO : data) {
            // 创建行
            HSSFRow row = sheet.createRow(indexMap.get("index"));
            //授信模式
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellStyle(centerCellStyle);
            cell1.setCellValue(modeMap.get(ratBkrationVO.getRatMode()));
            //授信编号
            HSSFCell cell2 = row.createCell(1);
            cell2.setCellStyle(centerCellStyle);
            cell2.setCellValue(ratBkrationVO.getRatCode());
            //授信合同号
            HSSFCell cell3 = row.createCell(2);
            cell3.setCellStyle(centerCellStyle);
            cell3.setCellValue(ratBkrationVO.getContractNo());
            //授信单位
            HSSFCell cell4 = row.createCell(3);
            cell4.setCellStyle(centerCellStyle);
            cell4.setCellValue(ratBkrationVO.getCorpName());

            // 授信菜单导出列，不同菜单有不同的列
            if (CommonConstant.YES.equals(isRb)) {
                // 【银行授信额度录入】菜单
                createExcelBodyDataRb(indexMap, statusMap, creditTypeMap, itemFlagMap, ratBkrationVO, sheet, row, centerCellStyle, rightCellStyle);
            } else {
                // 其他
                createExcelBodyDataOther(indexMap, statusMap, creditTypeMap, itemFlagMap, ratBkrationVO, sheet, row, centerCellStyle, rightCellStyle, isHasDisCorpCol, isHasDelayDateCol);
            }
        }
//        addSummaryData(sheet, ratItemSumList, centerCellStyle, rightCellStyle, indexMap);
    }

    /**
     * 创建有额度使用单位内容的表格
     *
     * @param indexMap       索引
     * @param statusMap      审批状态
     * @param creditTypeMap  授信额度类型字段
     * @param itemFlagMap    授信类型字典
     * @param ratBkrationVO  数据
     * @param sheet          表格
     * @param row            当前行
     * @param cellStyle      表格样式
     * @param rightCellStyle 表格样式
     */
    private void createExcelBodyDataRb(Map<String, Integer> indexMap, Map<String, String> statusMap, Map<String, String> creditTypeMap, Map<String, String> itemFlagMap,
                                       RatBkrationVO ratBkrationVO, HSSFSheet sheet, HSSFRow row, HSSFCellStyle cellStyle, HSSFCellStyle rightCellStyle) {
        List<RatBkRationDtlVm> rationDtls = ratBkrationVO.getRationDtls();
        // 过滤出单品种集合
        List<RatBkRationDtlVm> singleItemList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(rationDtls)) {
            singleItemList = rationDtls.stream().filter(single -> RationConstant.RAT_COMFLAG_0.equals(single.getItemFlag())).collect(Collectors.toList());
        }
        // 过滤出综合品种集合
        List<RatBkRationDtlVm> compItemList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(rationDtls)) {
            compItemList = rationDtls.stream().filter(comp -> RationConstant.RAT_COMFLAG_1.equals(comp.getItemFlag())).collect(Collectors.toList());
        }
        // 分别设置单品种与综合品种数量
        ratBkrationVO.setSingItemSize(singleItemList.size());
        ratBkrationVO.setCompItemSize(compItemList.size());
        Integer index = indexMap.get("index");
        Integer dIndex = indexMap.get("dIndex");
        Integer cIndex = indexMap.get("cIndex");
        if (index != dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize()) {
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 3, 3));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 4, 4));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 5, 5));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 11, 11));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 12, 12));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 13, 13));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 14, 14));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 15, 15));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 16, 16));
        }

        //银行信息
        HSSFCell cell5 = row.createCell(4);
        cell5.setCellStyle(cellStyle);
        cell5.setCellValue(ratBkrationVO.getBankName());
        //授信币种
        HSSFCell cell6 = row.createCell(5);
        cell6.setCellStyle(cellStyle);
        cell6.setCellValue(ratBkrationVO.getCurName());
        //授信开始时间
        HSSFCell cell12 = row.createCell(11);
        cell12.setCellStyle(cellStyle);
        cell12.setCellValue(null != ratBkrationVO.getBeginDate() ? DateUtils.formatDate(ratBkrationVO.getBeginDate()) : "");
        //授信结束时间
        HSSFCell cell13 = row.createCell(12);
        cell13.setCellStyle(cellStyle);
        cell13.setCellValue(null != ratBkrationVO.getEndDate() ? DateUtils.formatDate(ratBkrationVO.getEndDate()) : "");
        //担保开始时间
        HSSFCell cell14 = row.createCell(13);
        cell14.setCellStyle(cellStyle);
        cell14.setCellValue(null != ratBkrationVO.getGuaranteeBeginDate() ? DateUtils.formatDate(ratBkrationVO.getGuaranteeBeginDate()) : "");
        //担保结束时间
        HSSFCell cell15 = row.createCell(14);
        cell15.setCellStyle(cellStyle);
        cell15.setCellValue(null != ratBkrationVO.getGuaranteeEndDate() ? DateUtils.formatDate(ratBkrationVO.getGuaranteeEndDate()) : "");
        //创建人
        HSSFCell cell16 = row.createCell(15);
        cell16.setCellStyle(cellStyle);
        cell16.setCellValue(ratBkrationVO.getUserName());
        //状态
        HSSFCell cell17 = row.createCell(16);
        cell17.setCellStyle(cellStyle);
        if (RationConstant.RAT_COMMON_Y.equals(ratBkrationVO.getOverdueSign())) {
            cell17.setCellValue(multiLangServiceI.getLang("common.ration.overdue"));
        } else {
            cell17.setCellValue(statusMap.get(ratBkrationVO.getStatus().toString()));
        }

        // 单品种明细集合
        if (!CollectionUtils.isEmpty(singleItemList)) {
            for (RatBkRationDtlVm rationDtl : singleItemList) {
                if (cIndex != 0) {
                    HSSFRow rowDtl = sheet.createRow(index);
                    // 创建单元格格式
                    for (int k = 0; k <= 5; k++) {
                        rowDtl.createCell(k);
                        rowDtl.getCell((short) k).setCellStyle(cellStyle);
                    }
                    //授信类型
                    HSSFCell cell7 = rowDtl.createCell(6);
                    cell7.setCellStyle(cellStyle);
                    cell7.setCellValue("");
                    if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                        cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                    }
                    //授信品种
                    HSSFCell cell8 = rowDtl.createCell(7);
                    cell8.setCellStyle(cellStyle);
                    cell8.setCellValue(rationDtl.getItemName());
                    //授信方式
                    HSSFCell cell9 = rowDtl.createCell(8);
                    cell9.setCellStyle(cellStyle);
                    cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                    //最高额度
                    HSSFCell cell10 = rowDtl.createCell(9);
                    cell10.setCellStyle(rightCellStyle);
                    cell10.setCellValue(rationDtl.getMaxMoney().doubleValue());
                    //授信额度
                    HSSFCell cell11 = rowDtl.createCell(10);
                    cell11.setCellStyle(rightCellStyle);
                    cell11.setCellValue(rationDtl.getMoney().doubleValue());
                    for (int k = 11; k <= 16; k++) {
                        rowDtl.createCell(k);
                        rowDtl.getCell((short) k).setCellStyle(cellStyle);
                    }
                } else {
                    //授信类型
                    HSSFCell cell7 = row.createCell(6);
                    cell7.setCellStyle(cellStyle);
                    cell7.setCellValue("");
                    if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                        cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                    }
                    //授信品种
                    HSSFCell cell8 = row.createCell(7);
                    cell8.setCellStyle(cellStyle);
                    cell8.setCellValue(rationDtl.getItemName());
                    //授信方式
                    HSSFCell cell9 = row.createCell(8);
                    cell9.setCellStyle(cellStyle);
                    cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                    //最高额度
                    HSSFCell cell10 = row.createCell(9);
                    cell10.setCellStyle(rightCellStyle);
                    cell10.setCellValue(rationDtl.getMaxMoney().doubleValue());
                    //授信额度
                    HSSFCell cell1 = row.createCell(10);
                    cell1.setCellValue(rationDtl.getMoney().doubleValue());
                    cell1.setCellStyle(rightCellStyle);
                    cIndex = 1;
                }
                dIndex++;
                index++;
            }
        }

        // 综合品种明细集合
        if (!CollectionUtils.isEmpty(compItemList)) {
            // 综合品种授信类型分组，并对每个综合品种的授信品种进行排序
            Map<String, List<RatBkRationDtlVm>> map = compItemList
                    .stream()
                    .collect(Collectors.groupingBy(RatBkRationDtlVm::getItemFlag))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().stream().sorted(Comparator.comparing(RatBkRationDtlVm::getCompSign, Comparator.nullsFirst(String::compareTo)).reversed()).collect(Collectors.toList())));

            for (Map.Entry<String, List<RatBkRationDtlVm>> entry : map.entrySet()) {
                List<RatBkRationDtlVm> v = entry.getValue();

                if (index != dIndex + v.size()) {
                    sheet.addMergedRegion(new CellRangeAddress(index, dIndex + v.size(), 6, 6));
                    sheet.addMergedRegion(new CellRangeAddress(index, dIndex + v.size(), 10, 10));
                }

                for (RatBkRationDtlVm rationDtl : v) {
                    if (cIndex != 0) {
                        HSSFRow rowDtl = sheet.createRow(index);
                        // 创建单元格格式
                        for (int k = 0; k <= 5; k++) {
                            rowDtl.createCell(k);
                            rowDtl.getCell((short) k).setCellStyle(cellStyle);
                        }
                        //授信类型
                        HSSFCell cell7 = rowDtl.createCell(6);
                        cell7.setCellStyle(cellStyle);
                        cell7.setCellValue("");
                        if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                            cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                        }
                        //授信品种
                        HSSFCell cell8 = rowDtl.createCell(7);
                        cell8.setCellStyle(cellStyle);
                        cell8.setCellValue(rationDtl.getItemName());
                        //授信方式
                        HSSFCell cell9 = rowDtl.createCell(8);
                        cell9.setCellStyle(cellStyle);
                        cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                        //最高额度
                        HSSFCell cell10 = rowDtl.createCell(9);
                        cell10.setCellStyle(rightCellStyle);
                        cell10.setCellValue(rationDtl.getMaxMoney().doubleValue());
                        //授信额度
                        HSSFCell cell1 = rowDtl.createCell(10);
                        cell1.setCellStyle(rightCellStyle);
                        cell1.setCellValue(rationDtl.getMoney().doubleValue());
                        for (int k = 11; k <= 16; k++) {
                            rowDtl.createCell(k);
                            rowDtl.getCell((short) k).setCellStyle(cellStyle);
                        }
                    } else {
                        //授信类型
                        HSSFCell cell7 = row.createCell(6);
                        cell7.setCellStyle(cellStyle);
                        cell7.setCellValue("");
                        if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                            cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                        }
                        //授信品种
                        HSSFCell cell8 = row.createCell(7);
                        cell8.setCellStyle(cellStyle);
                        cell8.setCellValue(rationDtl.getItemName());
                        //授信方式
                        HSSFCell cell9 = row.createCell(8);
                        cell9.setCellStyle(cellStyle);
                        cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                        //最高额度
                        HSSFCell cell10 = row.createCell(9);
                        cell10.setCellStyle(rightCellStyle);
                        cell10.setCellValue(rationDtl.getMaxMoney().doubleValue());
                        //授信额度
                        HSSFCell cell1 = row.createCell(10);
                        cell1.setCellValue(rationDtl.getMoney().doubleValue());
                        cell1.setCellStyle(rightCellStyle);
                        cIndex = 1;
                    }
                    dIndex++;
                    index++;
                }
            }
        }

        // 如果没有明细
        if (ObjectUtil.isEmpty(rationDtls)) {
            //授信类型
            HSSFCell cell7 = row.createCell(6);
            cell7.setCellStyle(cellStyle);
            cell7.setCellValue("");
            //授信品种
            HSSFCell cell8 = row.createCell(7);
            cell8.setCellStyle(cellStyle);
            cell8.setCellValue("");
            //授信方式
            HSSFCell cell9 = row.createCell(8);
            cell9.setCellStyle(cellStyle);
            cell9.setCellValue("");
            //最高额度
            HSSFCell cell10 = row.createCell(9);
            cell10.setCellStyle(rightCellStyle);
            cell10.setCellValue("");
            //授信额度
            HSSFCell cell11 = row.createCell(10);
            cell11.setCellStyle(rightCellStyle);
            cell11.setCellValue("");

            dIndex++;
            index++;
        }

        indexMap.put("index", dIndex + 1);
        indexMap.put("cIndex", 0);
        indexMap.put("dIndex", dIndex);
    }

    /**
     * 创建有额度使用单位内容的表格
     *
     * @param indexMap          索引
     * @param statusMap         审批状态
     * @param creditTypeMap     授信额度类型字段
     * @param itemFlagMap       授信类型字典
     * @param ratBkrationVO     数据
     * @param sheet             表格
     * @param row               当前行
     * @param cellStyle         表格样式
     * @param isHasDisCorpCol   是否添加额度使用单位列
     * @param isHasDelayDateCol 是否添加授信延期日期列
     */
    private void createExcelBodyDataOther(Map<String, Integer> indexMap, Map<String, String> statusMap, Map<String, String> creditTypeMap, Map<String, String> itemFlagMap,
                                          RatBkrationVO ratBkrationVO, HSSFSheet sheet, HSSFRow row, HSSFCellStyle cellStyle, HSSFCellStyle rightCellStyle, String isHasDisCorpCol, String isHasDelayDateCol) {
        List<RatBkRationDtlVm> rationDtls = ratBkrationVO.getRationDtls();
        // 过滤出单品种集合
        List<RatBkRationDtlVm> singleItemList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(rationDtls)) {
            singleItemList = rationDtls.stream().filter(single -> RationConstant.RAT_COMFLAG_0.equals(single.getItemFlag())).collect(Collectors.toList());
        }
        // 过滤出综合品种集合
        List<RatBkRationDtlVm> compItemList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(rationDtls)) {
            compItemList = rationDtls.stream().filter(comp -> RationConstant.RAT_COMFLAG_1.equals(comp.getItemFlag())).collect(Collectors.toList());
        }
        // 分别设置单品种与综合品种数量
        ratBkrationVO.setSingItemSize(singleItemList.size());
        ratBkrationVO.setCompItemSize(compItemList.size());
        Integer index = indexMap.get("index");
        Integer dIndex = indexMap.get("dIndex");
        Integer cIndex = indexMap.get("cIndex");
        if (index != dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize()) {
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 3, 3));
            // 是否添加额度使用单位列
            if (CommonConstant.YES.equals(isHasDisCorpCol)) {
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 4, 4));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 5, 5));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 6, 6));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 11, 11));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 12, 12));
                //是否添加授信延期日期列
                if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                    sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 13, 13));
                    sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 14, 14));
                } else {
                    sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 13, 13));
                }
            } else {
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 4, 4));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 5, 5));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 10, 10));
                sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 11, 11));
                //是否添加授信延期日期列
                if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                    sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 12, 12));
                    sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 13, 13));
                } else {
                    sheet.addMergedRegion(new CellRangeAddress(index, dIndex + ratBkrationVO.getSingItemSize() + ratBkrationVO.getCompItemSize(), 12, 12));
                }
            }
        }

        // 是否添加额度使用单位列
        if (CommonConstant.YES.equals(isHasDisCorpCol)) {
            //额度使用单位
            HSSFCell cell5 = row.createCell(4);
            cell5.setCellStyle(cellStyle);
            cell5.setCellValue(ratBkrationVO.getDisCorpName());
            //银行信息
            HSSFCell cell6 = row.createCell(5);
            cell6.setCellStyle(cellStyle);
            cell6.setCellValue(ratBkrationVO.getBankName());
            //授信币种
            HSSFCell cell7 = row.createCell(6);
            cell7.setCellStyle(cellStyle);
            cell7.setCellValue(ratBkrationVO.getCurName());
            //授信开始时间
            HSSFCell cell12 = row.createCell(11);
            cell12.setCellStyle(cellStyle);
            cell12.setCellValue(DateUtils.formatDate(ratBkrationVO.getBeginDate()));
            //授信结束时间
            HSSFCell cell13 = row.createCell(12);
            cell13.setCellStyle(cellStyle);
            cell13.setCellValue(DateUtils.formatDate(ratBkrationVO.getEndDate()));
            //是否添加授信延期日期列
            if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                //授信延期日期
                HSSFCell cell14 = row.createCell(13);
                cell14.setCellStyle(cellStyle);
                cell14.setCellValue(null != ratBkrationVO.getDelayDate() ? DateUtils.formatDate(ratBkrationVO.getDelayDate()) : "");
                //状态
                HSSFCell cell15 = row.createCell(14);
                cell15.setCellStyle(cellStyle);
                if (RationConstant.RAT_COMMON_Y.equals(ratBkrationVO.getOverdueSign())) {
                    cell15.setCellValue(multiLangServiceI.getLang("common.ration.overdue"));
                } else {
                    if (CommonConstant.STATUE_DELETE.equals(ratBkrationVO.getDelayStatus())) {
                        cell14.setCellValue(multiLangServiceI.getLang("common.ratBkrationDelay.delayStatus"));
                    } else {
                        cell14.setCellValue(statusMap.get(ratBkrationVO.getDelayStatus().toString()));
                    }
                }
            } else {
                //状态
                HSSFCell cell14 = row.createCell(13);
                cell14.setCellStyle(cellStyle);
                if (RationConstant.RAT_COMMON_Y.equals(ratBkrationVO.getOverdueSign())) {
                    cell14.setCellValue(multiLangServiceI.getLang("common.ration.overdue"));
                } else {
                    cell14.setCellValue(statusMap.get(ratBkrationVO.getStatus().toString()));
                }
            }
        } else {
            //银行信息
            HSSFCell cell5 = row.createCell(4);
            cell5.setCellStyle(cellStyle);
            cell5.setCellValue(ratBkrationVO.getBankName());
            //授信币种
            HSSFCell cell6 = row.createCell(5);
            cell6.setCellStyle(cellStyle);
            cell6.setCellValue(ratBkrationVO.getCurName());
            //授信开始时间
            HSSFCell cell11 = row.createCell(10);
            cell11.setCellStyle(cellStyle);
            cell11.setCellValue(DateUtils.formatDate(ratBkrationVO.getBeginDate()));
            //授信结束时间
            HSSFCell cell12 = row.createCell(11);
            cell12.setCellStyle(cellStyle);
            cell12.setCellValue(DateUtils.formatDate(ratBkrationVO.getEndDate()));
            //是否添加授信延期日期列
            if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                //授信延期日期
                HSSFCell cell13 = row.createCell(12);
                cell13.setCellStyle(cellStyle);
                cell13.setCellValue(null != ratBkrationVO.getDelayDate() ? DateUtils.formatDate(ratBkrationVO.getDelayDate()) : "");
                //状态
                HSSFCell cell14 = row.createCell(13);
                cell14.setCellStyle(cellStyle);
                if (RationConstant.RAT_COMMON_Y.equals(ratBkrationVO.getOverdueSign())) {
                    cell14.setCellValue(multiLangServiceI.getLang("common.ration.overdue"));
                } else {
                    if (CommonConstant.STATUE_DELETE.equals(ratBkrationVO.getDelayStatus())) {
                        cell14.setCellValue(multiLangServiceI.getLang("common.ratBkrationDelay.delayStatus"));
                    } else {
                        cell14.setCellValue(statusMap.get(ratBkrationVO.getDelayStatus().toString()));
                    }
                }
            } else {
                //状态
                HSSFCell cell13 = row.createCell(12);
                cell13.setCellStyle(cellStyle);
                if (RationConstant.RAT_COMMON_Y.equals(ratBkrationVO.getOverdueSign())) {
                    cell13.setCellValue(multiLangServiceI.getLang("common.ration.overdue"));
                } else {
                    cell13.setCellValue(statusMap.get(ratBkrationVO.getStatus().toString()));
                }
            }
        }

        // 单品种明细集合
        if (!CollectionUtils.isEmpty(singleItemList)) {
            for (RatBkRationDtlVm rationDtl : singleItemList) {
                if (cIndex != 0) {
                    HSSFRow rowDtl = sheet.createRow(index);
                    // 是否添加额度使用单位列
                    if (CommonConstant.YES.equals(isHasDisCorpCol)) {
                        // 创建单元格格式
                        for (int k = 0; k <= 6; k++) {
                            rowDtl.createCell(k);
                            rowDtl.getCell((short) k).setCellStyle(cellStyle);
                        }
                        //授信类型
                        HSSFCell cell8 = rowDtl.createCell(7);
                        cell8.setCellStyle(cellStyle);
                        cell8.setCellValue("");
                        if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                            cell8.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                        }
                        //授信品种
                        HSSFCell cell9 = rowDtl.createCell(8);
                        cell9.setCellStyle(cellStyle);
                        cell9.setCellValue(rationDtl.getItemName());
                        //授信方式
                        HSSFCell cell10 = rowDtl.createCell(9);
                        cell10.setCellStyle(cellStyle);
                        cell10.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                        //授信额度
                        HSSFCell cell11 = rowDtl.createCell(10);
                        cell11.setCellStyle(rightCellStyle);
                        cell11.setCellValue(rationDtl.getMoney().doubleValue());
                        //是否添加授信延期日期列
                        if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                            for (int k = 11; k <= 14; k++) {
                                rowDtl.createCell(k);
                                rowDtl.getCell((short) k).setCellStyle(cellStyle);
                            }
                        } else {
                            for (int k = 11; k <= 13; k++) {
                                rowDtl.createCell(k);
                                rowDtl.getCell((short) k).setCellStyle(cellStyle);
                            }
                        }
                    } else {
                        // 创建单元格格式
                        for (int k = 0; k <= 5; k++) {
                            rowDtl.createCell(k);
                            rowDtl.getCell((short) k).setCellStyle(cellStyle);
                        }
                        //授信类型
                        HSSFCell cell7 = rowDtl.createCell(6);
                        cell7.setCellStyle(cellStyle);
                        cell7.setCellValue("");
                        if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                            cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                        }
                        //授信品种
                        HSSFCell cell8 = rowDtl.createCell(7);
                        cell8.setCellStyle(cellStyle);
                        cell8.setCellValue(rationDtl.getItemName());
                        //授信方式
                        HSSFCell cell9 = rowDtl.createCell(8);
                        cell9.setCellStyle(cellStyle);
                        cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                        //授信额度
                        HSSFCell cell10 = rowDtl.createCell(9);
                        cell10.setCellStyle(rightCellStyle);
                        cell10.setCellValue(rationDtl.getMoney().doubleValue());
                        //是否添加授信延期日期列
                        if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                            for (int k = 10; k <= 13; k++) {
                                rowDtl.createCell(k);
                                rowDtl.getCell((short) k).setCellStyle(cellStyle);
                            }
                        } else {
                            for (int k = 10; k <= 12; k++) {
                                rowDtl.createCell(k);
                                rowDtl.getCell((short) k).setCellStyle(cellStyle);
                            }
                        }
                    }
                } else {
                    // 是否添加额度使用单位列
                    if (CommonConstant.YES.equals(isHasDisCorpCol)) {
                        //授信类型
                        HSSFCell cell8 = row.createCell(7);
                        cell8.setCellStyle(cellStyle);
                        cell8.setCellValue("");
                        if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                            cell8.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                        }
                        //授信品种
                        HSSFCell cell9 = row.createCell(8);
                        cell9.setCellStyle(cellStyle);
                        cell9.setCellValue(rationDtl.getItemName());
                        //授信方式
                        HSSFCell cell10 = row.createCell(9);
                        cell10.setCellStyle(cellStyle);
                        cell10.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                        //授信额度
                        HSSFCell cell1 = row.createCell(10);
                        cell1.setCellValue(rationDtl.getMoney().doubleValue());
                        cell1.setCellStyle(rightCellStyle);
                    } else {
                        //授信类型
                        HSSFCell cell7 = row.createCell(6);
                        cell7.setCellStyle(cellStyle);
                        cell7.setCellValue("");
                        if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                            cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                        }
                        //授信品种
                        HSSFCell cell8 = row.createCell(7);
                        cell8.setCellStyle(cellStyle);
                        cell8.setCellValue(rationDtl.getItemName());
                        //授信方式
                        HSSFCell cell9 = row.createCell(8);
                        cell9.setCellStyle(cellStyle);
                        cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                        //授信额度
                        HSSFCell cell10 = row.createCell(9);
                        cell10.setCellValue(rationDtl.getMoney().doubleValue());
                        cell10.setCellStyle(rightCellStyle);
                    }
                    cIndex = 1;
                }
                dIndex++;
                index++;
            }
        }

        // 综合品种明细集合
        if (!CollectionUtils.isEmpty(compItemList)) {
            // 综合品种授信类型分组，并对每个综合品种的授信品种进行排序
            Map<String, List<RatBkRationDtlVm>> map = compItemList
                    .stream()
                    .collect(Collectors.groupingBy(RatBkRationDtlVm::getItemFlag))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().stream().sorted(Comparator.comparing(RatBkRationDtlVm::getCompSign, Comparator.nullsFirst(String::compareTo)).reversed()).collect(Collectors.toList())));

            for (Map.Entry<String, List<RatBkRationDtlVm>> entry : map.entrySet()) {
                List<RatBkRationDtlVm> v = entry.getValue();

                // 是否添加额度使用单位列
                if (CommonConstant.YES.equals(isHasDisCorpCol)) {
                    if (index != dIndex + v.size()) {
                        sheet.addMergedRegion(new CellRangeAddress(index, dIndex + v.size(), 7, 7));
                        sheet.addMergedRegion(new CellRangeAddress(index, dIndex + v.size(), 10, 10));
                    }
                } else {
                    if (index != dIndex + v.size()) {
                        sheet.addMergedRegion(new CellRangeAddress(index, dIndex + v.size(), 6, 6));
                        sheet.addMergedRegion(new CellRangeAddress(index, dIndex + v.size(), 9, 9));
                    }
                }

                for (RatBkRationDtlVm rationDtl : v) {
                    if (cIndex != 0) {
                        HSSFRow rowDtl = sheet.createRow(index);
                        // 是否添加额度使用单位列
                        if (CommonConstant.YES.equals(isHasDisCorpCol)) {
                            // 创建单元格格式
                            for (int k = 0; k <= 6; k++) {
                                rowDtl.createCell(k);
                                rowDtl.getCell((short) k).setCellStyle(cellStyle);
                            }
                            //授信类型
                            HSSFCell cell8 = rowDtl.createCell(7);
                            cell8.setCellStyle(cellStyle);
                            cell8.setCellValue("");
                            if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                                cell8.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                            }
                            //授信品种
                            HSSFCell cell9 = rowDtl.createCell(8);
                            cell9.setCellStyle(cellStyle);
                            cell9.setCellValue(rationDtl.getItemName());
                            //授信方式
                            HSSFCell cell10 = rowDtl.createCell(9);
                            cell10.setCellStyle(cellStyle);
                            cell10.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                            //授信额度
                            HSSFCell cell11 = rowDtl.createCell(10);
                            cell11.setCellStyle(rightCellStyle);
                            cell11.setCellValue(rationDtl.getMoney().doubleValue());
                            //是否添加授信延期日期列
                            if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                                for (int k = 11; k <= 14; k++) {
                                    rowDtl.createCell(k);
                                    rowDtl.getCell((short) k).setCellStyle(cellStyle);
                                }
                            } else {
                                for (int k = 11; k <= 13; k++) {
                                    rowDtl.createCell(k);
                                    rowDtl.getCell((short) k).setCellStyle(cellStyle);
                                }
                            }
                        } else {
                            // 创建单元格格式
                            for (int k = 0; k <= 5; k++) {
                                rowDtl.createCell(k);
                                rowDtl.getCell((short) k).setCellStyle(cellStyle);
                            }
                            //授信类型
                            HSSFCell cell7 = rowDtl.createCell(6);
                            cell7.setCellStyle(cellStyle);
                            cell7.setCellValue("");
                            if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                                cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                            }
                            //授信品种
                            HSSFCell cell8 = rowDtl.createCell(7);
                            cell8.setCellStyle(cellStyle);
                            cell8.setCellValue(rationDtl.getItemName());
                            //授信方式
                            HSSFCell cell9 = rowDtl.createCell(8);
                            cell9.setCellStyle(cellStyle);
                            cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                            //授信额度
                            HSSFCell cell10 = rowDtl.createCell(9);
                            cell10.setCellStyle(rightCellStyle);
                            cell10.setCellValue(rationDtl.getMoney().doubleValue());
                            //是否添加授信延期日期列
                            if (CommonConstant.YES.equals(isHasDelayDateCol)) {
                                for (int k = 10; k <= 13; k++) {
                                    rowDtl.createCell(k);
                                    rowDtl.getCell((short) k).setCellStyle(cellStyle);
                                }
                            } else {
                                for (int k = 10; k <= 12; k++) {
                                    rowDtl.createCell(k);
                                    rowDtl.getCell((short) k).setCellStyle(cellStyle);
                                }
                            }
                        }
                    } else {
                        // 是否添加额度使用单位列
                        if (CommonConstant.YES.equals(isHasDisCorpCol)) {
                            //授信类型
                            HSSFCell cell8 = row.createCell(7);
                            cell8.setCellStyle(cellStyle);
                            cell8.setCellValue("");
                            if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                                cell8.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                            }
                            //授信品种
                            HSSFCell cell9 = row.createCell(8);
                            cell9.setCellStyle(cellStyle);
                            cell9.setCellValue(rationDtl.getItemName());
                            //授信方式
                            HSSFCell cell10 = row.createCell(9);
                            cell10.setCellStyle(cellStyle);
                            cell10.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                            //授信额度
                            HSSFCell cell11 = row.createCell(10);
                            cell11.setCellValue(rationDtl.getMoney().doubleValue());
                            cell11.setCellStyle(rightCellStyle);
                        } else {
                            //授信类型
                            HSSFCell cell7 = row.createCell(6);
                            cell7.setCellStyle(cellStyle);
                            cell7.setCellValue("");
                            if (ObjectUtil.isNotEmpty(rationDtl.getItemFlag())) {
                                cell7.setCellValue(itemFlagMap.get(rationDtl.getItemFlag()));
                            }
                            //授信品种
                            HSSFCell cell8 = row.createCell(7);
                            cell8.setCellStyle(cellStyle);
                            cell8.setCellValue(rationDtl.getItemName());
                            //授信方式
                            HSSFCell cell9 = row.createCell(8);
                            cell9.setCellStyle(cellStyle);
                            cell9.setCellValue(creditTypeMap.get(rationDtl.getCreditType()));
                            //授信额度
                            HSSFCell cell10 = row.createCell(9);
                            cell10.setCellValue(rationDtl.getMoney().doubleValue());
                            cell10.setCellStyle(rightCellStyle);
                        }
                        cIndex = 1;
                    }
                    dIndex++;
                    index++;
                }
            }
        }
        indexMap.put("index", dIndex + 1);
        indexMap.put("cIndex", 0);
        indexMap.put("dIndex", dIndex);
    }

    /**
     * @param sheet
     * @param ratItemSumList
     * @param cellStyle
     * @param rightCellStyle
     * @param indexMap
     */
    public void addSummaryData(HSSFSheet sheet, List<RatItemSumVO> ratItemSumList, HSSFCellStyle cellStyle, HSSFCellStyle rightCellStyle, Map<String, Integer> indexMap) {
        int currentRowIndex = indexMap.get("index");

        // 添加汇总数据
        for (RatItemSumVO sumItem : ratItemSumList) {
            HSSFRow sumRow = sheet.createRow(currentRowIndex);

            // 第一列设置业务名称
            HSSFCell nameCell = sumRow.createCell(0);
            nameCell.setCellValue(sumItem.getSumBusName());
            nameCell.setCellStyle(rightCellStyle);

            for (int k = 1; k <= 8; k++) {
                sumRow.createCell(k);
                sumRow.getCell((short) k).setCellStyle(cellStyle);
            }

            // 合并前7列（0-6列）
            sheet.addMergedRegion(new CellRangeAddress(currentRowIndex, currentRowIndex, 0, 8));

            // 第7列设置金额（右对齐）
            HSSFCell moneyCell = sumRow.createCell(9);
            moneyCell.setCellValue(sumItem.getSumMoney().doubleValue());
            moneyCell.setCellStyle(rightCellStyle);

            for (int k = 10; k <= 16; k++) {
                sumRow.createCell(k);
                sumRow.getCell((short) k).setCellStyle(cellStyle);
            }
            currentRowIndex++;
        }
        // 更新索引
        indexMap.put("index", currentRowIndex);
    }

    /**
     * 封装字典Map
     *
     * @param modeMap         授信模式字典
     * @param statusMap       审批状态
     * @param creditTypeMap   授信额度类型字段
     * @param itemFlagMap     授信类型字典
     * @param distriuteWayMap 授信分配字典
     */
    private void getDictGroup(Map<String, String> modeMap, Map<String, String> statusMap, Map<String, String> creditTypeMap, Map<String, String> itemFlagMap, Map<String, String> distriuteWayMap) {
        //获取字典授信模式
        if (modeMap != null) {
            List<DictDTO> modeList = dictService.list(RationConstant.RAT_BKRATION_RAT_MODE);
            for (DictDTO dto : modeList) {
                modeMap.put(dto.getCode(), dto.getName());
            }
        }
        if (statusMap != null) {
            List<DictDTO> statusList = dictService.list(RationConstant.COMMON_STATUS);
            for (DictDTO dto : statusList) {
                statusMap.put(dto.getCode(), dto.getName());
            }
        }
        if (creditTypeMap != null) {
            List<DictDTO> creditTypeList = dictService.list(RationConstant.RAT_CREDIT_TYPE);
            for (DictDTO dto : creditTypeList) {
                creditTypeMap.put(dto.getCode(), dto.getName());
            }
        }
        if (itemFlagMap != null) {
            List<DictDTO> itemFlagList = dictService.list(RationConstant.RAT_MODE_ITEM_FLAG);
            for (DictDTO dto : itemFlagList) {
                itemFlagMap.put(dto.getCode(), dto.getName());
            }
        }
        if (distriuteWayMap != null) {
            List<DictDTO> distributWayList = dictService.list(RationConstant.RAT_DISTRIBUTE_WAY);
            for (DictDTO dto : distributWayList) {
                distriuteWayMap.put(dto.getCode(), dto.getName());
            }
        }
    }

    /**
     * 银行授信额度录入导入
     */
    public void dealImportData(List<RatBkrationVO> list, ICurrentUser currentUser) {
        // 定义授信额度主信息集合
        List<RatBkrationVO> ratBkrationList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            // 授信模式、授信单位、授信合同号（不为空）、银行开户行行号、授信币种、授信开始时间、授信结束时间、结转授信编号（可为空）相同，把这些字段用逗号拼接用来作为map的key，进行分组
            Map<String, List<RatBkrationVO>> map = list
                    .stream()
                    .collect(Collectors.groupingBy(vo -> vo.getRatModeName() + "," + vo.getCorpName() + "," + vo.getContractNo() + "," + vo.getBankType()
                            + "," + vo.getBankCode() + "," + vo.getCurName() + "," + DateUtils.formatDate(vo.getBeginDate()) + "," + DateUtils.formatDate(vo.getEndDate()) + "," + vo.getOldRatCodeCode()));

            // k为授信录入主信息 对应 多个授信明细
            map.forEach((k, v) -> {
                List<RatBkRationDtlVm> rationDtls = new ArrayList<>();
                RatBkrationVO obj = null;
                // 将合并的数据的备注拼接
                StringBuilder remark = new StringBuilder();
                // 定义总计额度
                BigDecimal moneySum = BigDecimal.ZERO;

                // 单品种集合
                List<RatBkrationVO> singleItemList = v.stream().filter(single -> StringUtils.isNotEmpty(single.getItemFlag()) && RationConstant.RAT_COMFLAG_0.equals(single.getItemFlag())).collect(Collectors.toList());
                // 设置单品种明细
                if (!CollectionUtils.isEmpty(singleItemList)) {
                    // 初始化
                    obj = new RatBkrationVO();
                    for (RatBkrationVO singleItem : singleItemList) {
                        BeanUtils.copyNotNullProperties(singleItem, obj);
                        RatBkRationDtlVm rationDtl = new RatBkRationDtlVm();
                        rationDtl.setItemFlag(singleItem.getItemFlag());
                        rationDtl.setItemCode(singleItem.getItemCode());
                        rationDtl.setItemName(singleItem.getItemCodeName());
                        rationDtl.setMoney(singleItem.getMoney());
                        rationDtl.setMaxMoney(BigDecimal.ZERO); // 单品种最大金额为0
                        rationDtl.setAssurepercent(singleItem.getAssurepercent());
                        rationDtl.setBondMoney(singleItem.getBondMoney());
                        rationDtl.setProcedureFeeRate(singleItem.getProcedureFeeRate());
                        rationDtl.setProcedureFee(singleItem.getProcedureFee());
                        rationDtl.setCreditType(singleItem.getCreditType());
                        rationDtl.setCompSign(RationConstant.RAT_COMP_SIGN_0);// 单品种 综合额度使用标识默认为0
                        rationDtls.add(rationDtl);
                        moneySum = moneySum.add(singleItem.getMoney());
                        if (StringUtils.isNotEmpty(singleItem.getRemark())) {
                            remark.append(singleItem.getRemark()).append(CommonConstant.SEMI_SPLIT);
                        }
                    }
                }

                // 综合品种集合
                List<RatBkrationVO> compItemList = v.stream().filter(comp -> StringUtils.isNotEmpty(comp.getItemFlag()) && RationConstant.RAT_COMFLAG_1.equals(comp.getItemFlag())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(compItemList)) {
                    if (ObjectUtils.isEmpty(obj)) {
                        // 初始化
                        obj = new RatBkrationVO();
                    }

                    int index = 0; // 下标
                    for (RatBkrationVO compItem : compItemList) {
                        BeanUtils.copyNotNullProperties(compItem, obj);
                        RatBkRationDtlVm rationDtl = new RatBkRationDtlVm();
                        rationDtl.setItemFlag(compItem.getItemFlag());
                        rationDtl.setItemCode(compItem.getItemCode());
                        rationDtl.setItemName(compItem.getItemCodeName());
                        if (index == 0) {
                            // 综合品种中，相同授信类型，只需给一个明细设置授信额度
                            rationDtl.setMoney(compItem.getMoney());
                            rationDtl.setCompSign(RationConstant.RAT_COMP_SIGN_1);
                            moneySum = moneySum.add(compItem.getMoney());
                        } else {
                            rationDtl.setMoney(null);
                            rationDtl.setCompSign(RationConstant.RAT_COMP_SIGN_0);
                        }
                        rationDtl.setMaxMoney(compItem.getMaxMoney()); // 单品种最大金额为0
                        rationDtl.setAssurepercent(compItem.getAssurepercent());
                        rationDtl.setBondMoney(compItem.getBondMoney());
                        rationDtl.setProcedureFeeRate(compItem.getProcedureFeeRate());
                        rationDtl.setProcedureFee(compItem.getProcedureFee());
                        rationDtl.setCreditType(compItem.getCreditType());

                        rationDtls.add(rationDtl);
                        if (StringUtils.isNotEmpty(compItem.getRemark())) {
                            remark.append(compItem.getRemark()).append(CommonConstant.SEMI_SPLIT);
                        }
                        index++;
                    }
                }

                if (!ObjectUtils.isEmpty(obj)) {
                    obj.setRationDtls(rationDtls);
                    obj.setMoneysum(moneySum);
                    obj.setRemark(remark.toString());
                    ratBkrationList.add(obj);
                }
            });
        }
        if (!CollectionUtils.isEmpty(ratBkrationList)) {
            ratBkrationList.forEach(item -> this.saveImport(item, currentUser));
        }
    }
}
