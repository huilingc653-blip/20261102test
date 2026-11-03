package com.bytter.credit.domain.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkrationDisDetailDao;
import com.bytter.credit.domain.dao.RatBusinessRaceDao;
import com.bytter.credit.domain.dao.RatUrationApplyDao;
import com.bytter.credit.domain.dao.RatUrationApplyDtlDao;
import com.bytter.credit.domain.entity.*;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.*;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.dto.GuaWayDTO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import com.bytter.guarantee.dto.GuaContractOccupDTO;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 授信占用申请表
 * @date 2017-08-19 11:36:28
 */
@Service("ratUrationApplyService")
@Transactional
@WorkFlow(value = {RatWorkFlowConstant.RAT_URATION_APPLY})
@DubboService(interfaceClass = IWorkFlowListener.class,group = RatWorkFlowConstant.RAT_URATION_APPLY)
public class RatUrationApplyService extends BaseService<RatUrationApply, String> implements IWorkFlowListener, IUserExcelVerifyHandler<RatUrationApplyVO>, ICorpDisableVerifyService {
    @Autowired
    private RatUrationApplyDao ratUrationApplyDao;
    @Autowired
    private RatBkrationService rationService;
    @Autowired
    private RatBkrationDisService disService;
    @Autowired
    private RatBkrationDisDetailService disDtlService;
    @Autowired
    private RatUrationApplyDtlService applyDtlService;
    @Autowired
    private RatUrationApplyDtlDao applyDtlDao;
    @Autowired
    private RatUrationService urationService;
    @DubboReference
    private IBtCurrencyService currencyService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private INotificationService notificationService;
    @DubboReference
    private IInputBankInfoService bankInfoService;
    @Autowired
    private IGuaWayServie iGuaWayServie;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired(required = false)
    private RatBkUseStatisticsSercive statisticsSercive;
    @Autowired(required = false)
    private RatUrationService ratUrationService;
    @Autowired
    private RatBkrationDisDetailDao ratBkrationDisDetailDao;
    @Autowired
    private RatBusinessRaceDao ratBusinessRaceDao;
    @DubboReference
    private IFcExcrateAdjustService excrateAdjustService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private IGuaWayServie guaWayServie;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(RatUrationApply entity) {
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
    public void save(RatUrationApply entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(RatUrationApply entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RatUrationApply entity) {
        super.saveOrUpdate(entity);
    }

    public BaseDao<RatUrationApply, String> getEntityDao() {
        return ratUrationApplyDao;
    }

    /**
     * 功能介绍 : 获取授信占用列表。<br>
     * <b>日期：</b>2017年8月19日<br>
     *
     * @param resource
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryURationApplyData(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "ru.corp_Id", true);
        DataTablesOutput datas = ratUrationApplyDao.queryURationApplyData(input, corpAuthSql);
        List<RatUrationListVO> ratUrationListVOList = datas.getData();
        List<String> curIds = ratUrationListVOList.stream().map(RatUrationListVO::getCurCode).collect(Collectors.toList());
        Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(curIds);
        for (RatUrationListVO ratUration : ratUrationListVOList) {
            ratUration.setCurName(currencyNameMap.get(ratUration.getCurCode()));
            if (ratUration.getItemFlag().equals(RationConstant.RAT_COMFLAG_1)) {// 综合品种时需查明细表得到授信品种名称
                List<RatUrationApplyDtlDTO> applyDtlDTOS = applyDtlService.queryRatUrationByApplyId(ratUration.getId());
                if (applyDtlDTOS != null && applyDtlDTOS.size() > 0) {
                    for (RatUrationApplyDtlDTO urationApplyDtlDTO : applyDtlDTOS) {
                        if (urationApplyDtlDTO.getMaxMoney().compareTo(BigDecimal.ZERO) != 0) {// 取占用金额非零的数据
                            ratUration.setItemName(urationApplyDtlDTO.getItemName());
                            ratUration.setCreditType(urationApplyDtlDTO.getCreditType());
                        }
                    }
                }
            }
        }
        DataTablesOutput<RatUrationListVO> dataTablesOutput = new DataTablesOutput();
        dataTablesOutput.setData(ratUrationListVOList);// 重新放入数据
        dataTablesOutput.setRecordsTotal(datas.getRecordsTotal());// 重新设置，便于分页数据显示
        dataTablesOutput.setDraw(datas.getDraw());
        dataTablesOutput.setRecordsFiltered(datas.getRecordsFiltered());
        return dataTablesOutput;
    }

    /**
     * 功能介绍 : 查询占用待审批数据。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataApplyApproveTables(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_URATION_APPLY,
                input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "ru.id", true);
        }
        DataTablesOutput dp = ratUrationApplyDao.getDataApplyApproveTables(input, businessSql);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 功能介绍 : 初始化授信占用情况。<br>
     * <b>日期：</b>2017年8月19日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public RatBkrationCpAddListDTO initRatUrationInfo(String ratCode, String corpId) {
        // 获取授信分配数据
        return disService.queryPassRationDisData(ratCode, corpId, null);
    }

    public RatUrationApplyListDTO editRatUrationInfo(String urationId) {
        List<RatUrationApplyListDTO> applyListDTOs = ratUrationApplyDao.queryRatUrationById(urationId);
        if (!EmptyUtils.isEmptyList(applyListDTOs)) {
            RatUrationApplyListDTO applyDTO = applyListDTOs.get(0);
            if (RationConstant.RAT_COMFLAG_1.equals(applyDTO.getItemFlag())) {// 如果占用综合，则查询综合占用明细
                List<RatUrationApplyDtlDTO> applyDtls = applyDtlService.queryRatUrationByApplyId(urationId);
                if (!EmptyUtils.isEmptyList(applyDtls)) {
                    for (RatUrationApplyDtlDTO dtlDTO : applyDtls) {
                        if (dtlDTO.getItemCode().equals(applyDTO.getSingItemCode())) {
                            List<RatUrationApplyDtlDTO> applyDtlDTOs = new ArrayList<RatUrationApplyDtlDTO>(1);
                            applyDtlDTOs.add(dtlDTO);
                            applyDTO.setApplyDtls(applyDtlDTOs);
                            applyDTO.setApplyDtlSize(applyDtlDTOs.size());
                        }
                    }
                }
            }
            if(StringUtils.isNotEmpty(applyDTO.getDisCode())){
                RatBkrationDis bkrationDis = disService.get(applyDTO.getDisCode(), true);
                applyDTO.setOriRatCode(bkrationDis.getRatCode());// 授信录入单号
            }
            setLeftApplyMoney(applyDTO, initRatUrationInfo(applyDTO.getOriRatCode(), applyDTO.getCorpId()));
            // 设置占用期限
            Calendar calendar = Calendar.getInstance();
            int term = DateUtils.dateDiff('d', this.dataToCalendar(applyDTO.getEndDate()),
                    this.dataToCalendar(applyDTO.getBeginDate()));
            applyDTO.setApplyDays(term + 1);
            return applyDTO;
        }
        return new RatUrationApplyListDTO();
    }

    /**
     * date转Calendar的方法
     *
     * @param date
     * @return
     */
    public Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 功能介绍 : 设置占用剩余额度。<br>
     * <b>日期：</b>2017年9月22日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setLeftApplyMoney(RatUrationApplyListDTO applyDTO, RatBkrationCpAddListDTO pageVO) {
        Map<String, RatBkrationCpAddDtlDTO> dtlMap = new HashMap<String, RatBkrationCpAddDtlDTO>();
        if (RationConstant.RAT_COMFLAG_1.equals(applyDTO.getItemFlag())) {// 如果占用综合，则查询综合占用明细
            List<RatUrationApplyDtlDTO> applyDtls = applyDTO.getApplyDtls();// 综合明细
            if (!EmptyUtils.isEmptyList(applyDtls)) {
                List<RatBkrationCpAddDtlDTO> compRationDtls = null == pageVO ? null : pageVO.getCompRationDtls();
                if (!EmptyUtils.isEmptyList(compRationDtls)) {
                    dtlMap = Maps.uniqueIndex(compRationDtls, new Function<RatBkrationCpAddDtlDTO, String>() {
                        @Override
                        public String apply(RatBkrationCpAddDtlDTO input) {
                            return input.getItemCode();
                        }
                    });
                }
                for (RatUrationApplyDtlDTO applyDtlDTO : applyDtls) {
                    RatBkrationCpAddDtlDTO dtlDTO = dtlMap.get(applyDtlDTO.getItemCode());
                    if (null != dtlDTO) {
                        applyDtlDTO.setLeftMaxMoney(dtlDTO.getAllDisMaxMoney());
                    }
                }
            }
        } else {
            List<RatBkrationCpAddDtlDTO> singRationDtls = null == pageVO ? null : pageVO.getSingRationDtls();
            if (!EmptyUtils.isEmptyList(singRationDtls)) {
                dtlMap = Maps.uniqueIndex(singRationDtls, new Function<RatBkrationCpAddDtlDTO, String>() {
                    @Override
                    public String apply(RatBkrationCpAddDtlDTO input) {
                        return input.getItemCode();
                    }
                });
            }
            RatBkrationCpAddDtlDTO dtlDTO = dtlMap.get(applyDTO.getSingItemCode());
            if (null != dtlDTO) {
                applyDTO.setLeftMoney(
                        dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney())
                                .subtract(dtlDTO.getFreezeMoney()).subtract(dtlDTO.getSuperProcessMoney())
                                .subtract(dtlDTO.getReliefAndUrationMoney()).subtract(dtlDTO.getVirtualUration()));
            }
        }
    }

    /**
     * 功能介绍 : 保存授信占用。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveRatUrationApply(RatUrationApplyDTO applyDTO, String userId) {
        List<RatUrationApplyListDTO> applyDtls = applyDTO.getApplyDtls();
        // BtCurrencyDTO curDTO = currencyService.getObjectById(applyDTO.getCurCode());
        if (null != applyDtls && applyDtls.size() > 0) {
            List<String> curIds = applyDtls.stream().map(RatUrationApplyListDTO::getInitCurCode).collect(Collectors.toList());
            Map<String, BtCurrencyDTO> curMap = currencyService.getCurrencyByIds(curIds);
            for (RatUrationApplyListDTO dto : applyDtls) {
                if (curMap.containsKey(dto.getInitCurCode())) {
                    // 业务币别汇率
                    dto.setExchange(curMap.get(dto.getInitCurCode()).getCurExrate());
                    dto.setCurName(curMap.get(dto.getInitCurCode()).getCurName());
                }
                processRatUrationApply(dto, userId);
            }
        }
    }

    /**
     * 功能介绍 : 保存综合授信占用明细记录。<br>
     * <b>日期：</b>2017年10月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveRationApplyDtl(String disCode, String applyCode, String applyType,
                                   List<RatUrationApplyDtlDTO> applyDtls) {
        Map<String, RatUrationApplyDtlDTO> applyMap = Maps.uniqueIndex(applyDtls,
                new Function<RatUrationApplyDtlDTO, String>() {
                    @Override
                    public String apply(RatUrationApplyDtlDTO input) {
                        return input.getItemCode();
                    }
                });
        List<RatBkrationDisDetail> disDtlDTOs = disDtlService.getRationDisDtlByDisCode(disCode,
                RationConstant.RAT_COMFLAG_1, null);
        for (RatBkrationDisDetail dtlDTO : disDtlDTOs) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            Integer itemOrder = dtlDTO.getItemOrder();// 品种顺序
            RatUrationApplyDtlDTO applyDTO = applyMap.get(itemCode);
            BigDecimal maxMoney = BigDecimal.ZERO;
            if (null != applyDTO) {
                maxMoney = applyDTO.getMaxMoney();// 综合占用额度
            }
            RatUrationApplyDtl applyDtl = new RatUrationApplyDtl();
            applyDtl.setApplyType(applyType);// 占用类型
            applyDtl.setApplyCode(applyCode);
            applyDtl.setItemFlag(dtlDTO.getItemFlag());
            applyDtl.setItemOrder(itemOrder);
            applyDtl.setItemCode(itemCode);
            applyDtl.setMaxMoney(maxMoney);
            applyDtl.setApplyMoney(BigDecimal.ZERO);
            applyDtl.setCreditType(dtlDTO.getCreditType());
            applyDtlService.saveEntity(applyDtl);
        }
    }

    /**
     * 功能介绍 : 处理授信占用。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void processRatUrationApply(RatUrationApplyListDTO applyDTO, String userId) {
        checkUrationMoney(applyDTO);
        // 占用申请表
        RatUrationApply apply = new RatUrationApply();
        BeanUtils.copyProperties(applyDTO, apply);
        apply.setApplyCode(
                rationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_APPLYCODE, RationConstant.RAT_URATION_APPLY_CODE));// 授信占用申请编号
        apply.setIsCur(RationConstant.RAT_COMMON_N);// 不折合
        apply.setCreateBy(userId);
        apply.setCreateDate(DateUtils.getDate());
        if (RationConstant.RAT_COMFLAG_0.equals(applyDTO.getItemFlag())) {
            apply.setItemCode(applyDTO.getItemCode());
        }
        saveOrUpdateUrationApply(apply);
        if (RationConstant.RAT_COMFLAG_1.equals(applyDTO.getItemFlag())) {
            // 根据分配明细生成多条占用记录
            List<RatUrationApplyDtlDTO> applyDtls = applyDTO.getApplyDtls();
            saveRationApplyDtl(apply.getDisCode(), apply.getId(), RationConstant.RAT_USE_TYPE_0, applyDtls);
        }
        // 写入占用表
        if (apply.getStatus().equals(CommonConstant.STATUE_WAIT_CHECK)) {
            RatUration uration = new RatUration();
            BeanUtils.copyProperties(apply, uration, "id");
            uration.setUrationCode(rationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_URATIONCODE,
                    RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
            uration.setPreMoney(apply.getMoney());// 折算前金额
            if (apply.getInitCurCode().equals(apply.getCurCode())) {
                uration.setConvertPreMoney(apply.getMoney().multiply(apply.getExchange()));// 折算本位币的占用金额
            } else {
                uration.setConvertPreMoney(apply.getMoney());
            }
            uration.setExchangeRate(apply.getExchange());// 汇率
            uration.setCreateBy(userId);
            uration.setCreateDate(DateUtils.getDate());
            uration.setBillId(apply.getId());// 占用申请ID
            uration.setBillNo(apply.getApplyCode());// 占用申请编号
            uration.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 授信占用的状态
            uration.setSyscode(ConfigConstant.RAT);// 授信
            uration.setBusinessName(RationConstant.BUSINESS_NAME_05);// 银行台账
            uration.setCreditType(apply.getRatCreditType());// 授信额度类型
            if (RationConstant.RAT_COMFLAG_0.equals(applyDTO.getItemFlag())) {
                uration.setItemCode(applyDTO.getItemCode());
            }
            if (StringUtils.isEmpty(applyDTO.getIsOpenRat())) {// 授信参数设置不启用敞口的时候
                uration.setIsOpenRat(RationConstant.OPEN_SIGN_0);
            }
            urationService.saveOrUpdateUration(uration);
            if (RationConstant.RAT_COMFLAG_1.equals(applyDTO.getItemFlag())) {
                List<RatUrationApplyDtlDTO> applyDtls = applyDTO.getApplyDtls();
                saveRationApplyDtl(apply.getDisCode(), uration.getId(), RationConstant.RAT_USE_TYPE_1, applyDtls);
            }
            getEntityDao().flush();
            // 冻结授信占用额度
            updateRationDisMoney(uration, true);
            // todo:挂审批
            getStartHandler().handler(RatWorkFlowConstant.RAT_URATION_APPLY, apply.getId(), apply, apply.getCorpId(),
                    userId);
        }
    }

    /**
     * 功能介绍 : 检查授信占用额度是否足够。<br>
     * <b>日期：</b>2017年8月22日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkUrationMoney(RatUrationApplyListDTO applyDTO) {
        if (applyDTO.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_APPLY_MONEY_NULL);
        }
        List<RatUrationApplyDtlDTO> applyDtls = applyDTO.getApplyDtls();
        if (EmptyUtils.isEmpty(applyDTO.getItemFlag())) {// 授信品种
            throw new ParameterException(RatMessageEnum.CMS_RAT_APPLY_ITEM_NULL);
        }
        if (RationConstant.RAT_COMFLAG_0.equals(applyDTO.getItemFlag())) {// 单品种
            String itemCode = applyDTO.getItemCode();
            if (EmptyUtils.isEmpty(itemCode)) {// 授信品种
                throw new ParameterException(RatMessageEnum.CMS_RAT_APPLY_ITEM_NULL);
            }
            List<RatBkrationCpAddDtlDTO> dtlDTOs = disService.getRationDisDtlByItemCode(applyDTO.getDisCode(),
                    applyDTO.getItemFlag(), itemCode);
            if (!EmptyUtils.isEmptyList(dtlDTOs)) {
                RatBkrationCpAddDtlDTO dtlDTO = dtlDTOs.get(0);
                BigDecimal money = dtlDTO.getMoney().subtract(dtlDTO.getSuperProcessMoney())
                        .subtract(dtlDTO.getFreezeMoney()).subtract(dtlDTO.getReliefAndUrationMoney())
                        .subtract(dtlDTO.getVirtualUration()).subtract(dtlDTO.getSuperReliefMoney())
                        .add(dtlDTO.getSuperByReliefMoney());// 剩余额度
                if (money.compareTo(applyDTO.getMoney()) < 0) {// 小于占用额度，则不能占用
                    throw new ParameterException(RatMessageEnum.CMS_RAT_APPLY_MONEY_BIGGER);
                }
            }
        } else {// 综合品种
            List<RatBkrationCpAddDtlDTO> dtlDTOs = disService.getRationDisDtlByItemCode(applyDTO.getDisCode(),
                    applyDTO.getItemFlag(), null);
            Map<String, RatBkrationCpAddDtlDTO> dtlMap = new HashMap<String, RatBkrationCpAddDtlDTO>();
            if (!EmptyUtils.isEmptyList(dtlDTOs)) {
                dtlMap = Maps.uniqueIndex(dtlDTOs, new Function<RatBkrationCpAddDtlDTO, String>() {
                    @Override
                    public String apply(RatBkrationCpAddDtlDTO input) {
                        return input.getItemCode();
                    }
                });
            }
            if (!EmptyUtils.isEmptyList(applyDtls)) {
                for (RatUrationApplyDtlDTO applDtl : applyDtls) {
                    String itemCode = applDtl.getItemCode();// 占用综合品种
                    RatBkrationCpAddDtlDTO dtlDTO = dtlMap.get(itemCode);
                    if (null != dtlDTO) {
                        if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getCompSign())) {// 综合使用标识
                            BigDecimal money = dtlDTO.getMoney().subtract(dtlDTO.getSuperProcessMoney())
                                    .subtract(dtlDTO.getFreezeMoney()).subtract(dtlDTO.getReliefAndUrationMoney())
                                    .subtract(dtlDTO.getVirtualUration()).subtract(dtlDTO.getSuperReliefMoney())
                                    .add(dtlDTO.getSuperByReliefMoney());// 剩余额度
                            if (money.compareTo(applyDTO.getMoney()) < 0) {// 小于占用额度，则不能占用
                                throw new ParameterException(RatMessageEnum.CMS_RAT_APPLY_MONEY_BIGGER);
                            }
                            break;
                        }
                    }
                }
                for (RatUrationApplyDtlDTO applDtl : applyDtls) {
                    String itemCode = applDtl.getItemCode();// 占用综合品种
                    RatBkrationCpAddDtlDTO dtlDTO = dtlMap.get(itemCode);
                    if (null != dtlDTO) {
                        if (dtlDTO.getMaxMoney().compareTo(applDtl.getMaxMoney()) < 0) {// 最高可用额度小于占用额度，则不能占用
                            throw new ParameterException(RatMessageEnum.CMS_RAT_APPLY_MAXMONEY_BIGGER,
                                    dtlDTO.getItemName());
                        }
                    }
                }
            }
        }
    }

    /**
     * 功能介绍: 审批通过，更新占用申请(95)和占用台账状态(2正式占用)。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateUrationPass(String urationId, String userId) {
        RatUrationApply apply = get(urationId, true);
        apply.setStatus(CommonConstant.STATUE_CHECK);
        apply.setUpdateBy(userId);
        apply.setUpdateDate(DateUtils.getDate());
        updateEntity(apply);
        List<RatUration> uList = urationService.queryRatUrationById(ConfigConstant.RAT, apply.getId());
        if (!EmptyUtils.isEmptyList(uList)) {
            RatUration uration = uList.get(0);
            passUration(uration, userId);
        }
    }

    /**
     * 功能介绍 : 授信占用审批通过正式占用。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void passUration(RatUration uration, String userId) {
        uration.setStatus(RationConstant.RAT_USE_STATUS_USE);// 正式占用
        uration.setUpdateBy(userId);
        uration.setUpdateDate(DateUtils.getDate());
        urationService.updateEntity(uration);
        // 更新分配表中占用额度super_process_money,冻结金额移动到占用金额,如果是总额共享的情况下还需要更新其他单位的虚拟占用额度
        if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
            // 更新分配表中占用额度，冻结金额减少
            updateRationDisMoney(uration, true);
        }
    }

    /**
     * 功能介绍 : 更新授信分配占用额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @param freezeFlag 是否更新冻结额度
     * @version 1.0
     * @author liyh
     */
    @Transactional
    public void updateRationDisMoney(RatUration uration, boolean freezeFlag) {
        getEntityDao().flush();
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(uration.getDisCode(), uration.getItemFlag(), uration.getItemCode());
            RatBkrationDisDetail disDetail = disDetails.get(0);
            if (RationConstant.RAT_USE_STATUS_FREEZE.equals(uration.getStatus())) {//占用待审批
                disDetail.setFreezeMoney(disDetail.getFreezeMoney().add(uration.getMoney()));//冻结额度减少
                disDetail.setFreezeOpenMoney(disDetail.getFreezeOpenMoney().add(uration.getOpenMoney()));
            } else if (RationConstant.RAT_USE_STATUS_USE.equals(uration.getStatus())) {//占用审批通过
                disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().add(uration.getMoney()));//占用额度增加
                disDetail.setSuperProcessOpenMoney(disDetail.getSuperProcessOpenMoney().add(uration.getOpenMoney()));
                if (freezeFlag) {//需要更新冻结额度
                    if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0 && disDetail.getFreezeMoney().compareTo(BigDecimal.ZERO) > 0) {
                        disDetail.setFreezeMoney(disDetail.getFreezeMoney().subtract(uration.getMoney()));//冻结额度减少
                        disDetail.setFreezeOpenMoney(disDetail.getFreezeOpenMoney().subtract(uration.getOpenMoney()));
                    }
                }
            } else if (RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {//打回
                disDetail.setFreezeMoney(disDetail.getFreezeMoney().subtract(uration.getMoney()));//冻结额度减少
                disDetail.setFreezeOpenMoney(disDetail.getFreezeOpenMoney().subtract(uration.getOpenMoney()));
            }
            disDtlService.merge(disDetail);
        } else {
            List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(uration.getDisCode(), uration.getItemFlag(), null);
            List<RatUrationApplyDtlDTO> applyDtls = applyDtlService.queryRatUrationById(uration.getId());//综合占用最高额度
            // 取综合占用最高额度
            for (RatBkrationDisDetail disDetail : disDetails) {
                // 处理占用额度
                if (RationConstant.RAT_COMFLAG_1.equals(disDetail.getCompSign())) {
                    if (RationConstant.RAT_USE_STATUS_FREEZE.equals(uration.getStatus())) {//占用待审批
                        disDetail.setFreezeMoney(disDetail.getFreezeMoney().add(uration.getMoney()));//冻结额度减少
                        disDetail.setFreezeOpenMoney(disDetail.getFreezeOpenMoney().add(uration.getOpenMoney()));
                    } else if (RationConstant.RAT_USE_STATUS_USE.equals(uration.getStatus())) {//占用审批通过
                        disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().add(uration.getMoney()));//占用额度增加
                        disDetail.setSuperProcessOpenMoney(disDetail.getSuperProcessOpenMoney().add(uration.getOpenMoney()));
                        if (freezeFlag) {//需要更新冻结额度
                            if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0 && disDetail.getFreezeMoney().compareTo(BigDecimal.ZERO) > 0) {
                                disDetail.setFreezeMoney(disDetail.getFreezeMoney().subtract(uration.getMoney()));//冻结额度减少
                                disDetail.setFreezeOpenMoney(disDetail.getFreezeOpenMoney().subtract(uration.getOpenMoney()));
                            }
                        }
                    } else if (RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {//打回
                        disDetail.setFreezeMoney(disDetail.getFreezeMoney().subtract(uration.getMoney()));//冻结额度减少
                        disDetail.setFreezeOpenMoney(disDetail.getFreezeOpenMoney().subtract(uration.getOpenMoney()));
                    }
                    disDtlService.merge(disDetail);
                }
            }
            setRationDisMaxMoney(uration, applyDtls, disDetails, false, freezeFlag);// 非共享单位最高额度处理
        }
        // 判断是否总额共享，如果总额共享，则相应的其他单位都需要调整虚拟占用额度
        RatBkrationDis rationDis = disService.get(uration.getDisCode(), true);
        if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(rationDis.getDistributeWay())) {
            processAmontShareDisMoney(uration, rationDis.getRatCode(), freezeFlag);
        }
    }

    /**
     * 更新旧占用的授信分配中的占用额度
     *
     * @param uration 要结转的数据
     */
    @Transactional
    public void updateOldRationDisMoney(RatUration uration) {
        getEntityDao().flush();
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(uration.getDisCode(), uration.getItemFlag(), uration.getItemCode());
            RatBkrationDisDetail disDetail = disDetails.get(0);
            if (RationConstant.RAT_USE_STATUS_CLOSE.equals(uration.getStatus())) {//占用审批通过，结转的前提必须是额度已占用状态
                if (disDetail.getSuperProcessMoney().compareTo(BigDecimal.ZERO) >= 0 && disDetail.getSuperProcessMoney().compareTo(uration.getMoney()) >= 0) {
                    disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().subtract(uration.getMoney()));//占用额度增加
                    disDetail.setSuperProcessOpenMoney(disDetail.getSuperProcessOpenMoney().subtract(uration.getOpenMoney()));
                }
            }
            disDtlService.merge(disDetail);
        } else {
            List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(uration.getDisCode(), uration.getItemFlag(), null);
            List<RatUrationApplyDtlDTO> applyDtls = applyDtlService.queryRatUrationById(uration.getId());//综合占用最高额度
            //取综合占用最高额度
            for (RatBkrationDisDetail disDetail : disDetails) {
                //处理占用额度
                if (RationConstant.RAT_COMFLAG_1.equals(disDetail.getCompSign())) {
                    if (RationConstant.RAT_USE_STATUS_CLOSE.equals(uration.getStatus())) {//占用审批通过
                        if (disDetail.getSuperProcessMoney().compareTo(BigDecimal.ZERO) >= 0 && disDetail.getSuperProcessMoney().compareTo(uration.getMoney()) >= 0) {
                            disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().subtract(uration.getMoney()));//占用额度增加
                            disDetail.setSuperProcessOpenMoney(disDetail.getSuperProcessOpenMoney().subtract(uration.getOpenMoney()));
                        }
                    }
                    disDtlService.merge(disDetail);
                }
            }
            // 非共享单位最高额度处理
            setRationDisMaxMoney(uration, applyDtls, disDetails, false);//非共享单位最高额度处理
        }
        //判断是否总额共享，如果总额共享，则相应的其他单位都需要调整虚拟占用额度
        RatBkrationDis rationDis = disService.get(uration.getDisCode(), true);
        if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(rationDis.getDistributeWay())) {
            List<RatBkrationDis> rationDisList;
            if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
                rationDisList = disService.getOtherRationDisByDisCode(rationDis.getRatCode(), uration.getDisCode(), uration.getItemFlag(), uration.getItemCode());
            } else {
                rationDisList = disService.getOtherRationDisByDisCode(rationDis.getRatCode(), uration.getDisCode(), uration.getItemFlag(), null);
            }
            if (!EmptyUtils.isEmptyList(rationDisList)) {
                for (RatBkrationDis bkrationDis : rationDisList) {
                    // 总额共享下处理占用单位外的分配详情的占用额度
                    if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
                        List<RatBkrationDisDetail> dtlList = disDtlService.getRationDisDtlByDisCode(bkrationDis.getId(), uration.getItemFlag(), uration.getItemCode());
                        if (EmptyUtils.isEmptyList(dtlList)) {
                            continue;
                        }
                        for (RatBkrationDisDetail disDetail : dtlList) {
                            if (RationConstant.RAT_USE_STATUS_CLOSE.equals(uration.getStatus())) {//占用审批通过
                                if (disDetail.getReliefAndUrationMoney().compareTo(BigDecimal.ZERO) >= 0 && disDetail.getReliefAndUrationMoney().compareTo(uration.getMoney()) >= 0) {
                                    disDetail.setReliefAndUrationMoney(disDetail.getReliefAndUrationMoney().subtract(uration.getMoney()));//占用额度增加
                                    disDetail.setUrationOpenMoney(disDetail.getUrationOpenMoney().subtract(uration.getOpenMoney()));
                                }
                            }
                            disDtlService.merge(disDetail);
                        }
                    } else {
                        List<RatBkrationDisDetail> dtlList = disDtlService.getRationDisDtlByDisCode(bkrationDis.getId(), uration.getItemFlag(), null);
                        if (EmptyUtils.isEmptyList(dtlList)) {
                            continue;
                        }
                        List<RatUrationApplyDtlDTO> applyDtls = applyDtlService.queryRatUrationById(uration.getId());//综合占用最高额度
                        //取综合占用最高额度
                        for (RatBkrationDisDetail disDetail : dtlList) {
                            //处理占用额度
                            if (RationConstant.RAT_COMFLAG_1.equals(disDetail.getCompSign())) {
                                if (RationConstant.RAT_USE_STATUS_CLOSE.equals(uration.getStatus())) {//占用审批通过
                                    if (disDetail.getReliefAndUrationMoney().compareTo(BigDecimal.ZERO) >= 0 && disDetail.getReliefAndUrationMoney().compareTo(uration.getMoney()) >= 0) {
                                        disDetail.setReliefAndUrationMoney(disDetail.getReliefAndUrationMoney().subtract(uration.getMoney()));//占用额度增加
                                        disDetail.setUrationOpenMoney(disDetail.getUrationOpenMoney().subtract(uration.getOpenMoney()));
                                    }
                                }
                            }
                        }
                        setRationDisMaxMoney(uration, applyDtls, dtlList, true);//总额共享下处理最高占用额度
                    }
                }
            }
        }
    }

    /**
     * 功能介绍 : 处理授信最高占用额度。<br>
     * <b>日期：</b>2017年9月21日<br>
     *
     * @param shareFlag 是否共享单位的最高额度处理
     * @version 1.0
     * @author liyh
     */
    private void setRationDisMaxMoney(RatUration uration, List<RatUrationApplyDtlDTO> applyDtls,
                                      List<RatBkrationDisDetail> disDetails, boolean shareFlag, boolean freezeFlag) {
        getEntityDao().flush();
        Integer status = uration.getStatus();
        //循环综合占用品种，判断占用后各综合品种的最高额度与综合额度的剩余额度
        Map<String, RatUrationApplyDtlDTO> applyMap = Maps.uniqueIndex(applyDtls, RatUrationApplyDtlDTO::getItemCode);// 授信占用信息

        for (RatBkrationDisDetail disDTO : disDetails) {
            if (!RationConstant.RAT_COMFLAG_1.equals(disDTO.getItemFlag())) {//非综合跳过
                continue;
            }
            String itemCode = disDTO.getItemCode();
            RatUrationApplyDtlDTO dtlDTO = applyMap.get(itemCode);//授信占用品种
            //判断额度是否足够
            BigDecimal applyMoney = BigDecimal.ZERO;
            BigDecimal money = BigDecimal.ZERO;
            if (null != dtlDTO) {
                applyMoney = dtlDTO.getApplyMoney();//实际应该占用额度
                if (dtlDTO.getMaxMoney() != null) {
                    money = dtlDTO.getMaxMoney();//综合占用额度
                }
            }
            if (RationConstant.RAT_USE_STATUS_RELEASE.equals(status)) {// 审批打回
                if (shareFlag) {//总额共享情况下
                    disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().subtract(applyMoney.add(money)));
                } else {
                    // 更新占用方冻结额度
                    disDTO.setFreezeMaxMoney(disDTO.getFreezeMaxMoney().subtract(money));
                    disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().subtract(applyMoney));
                }
            } else if (RationConstant.RAT_USE_STATUS_FREEZE.equals(status)) {// 待审批
                if (shareFlag) {
                    disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().add(applyMoney.add(money)));//在途占用的最高额度
                } else {
                    // 更新占用方冻结额度
                    disDTO.setFreezeMaxMoney(disDTO.getFreezeMaxMoney().add(money));
                    disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().add(applyMoney));
                }
            } else if (RationConstant.RAT_USE_STATUS_USE.equals(status)) {// 审批通过
                if (shareFlag) {
                    if (!freezeFlag) {
                        // freezeFlag 此字段在此处意思为仅在结转时有效
                        disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().add(applyMoney.add(money)));
                    }
                } else {
                    disDTO.setProcessMaxMoney(disDTO.getProcessMaxMoney().add(money));//占用额度增加
                    if (freezeFlag) {//需要更新冻结额度
                        if (disDTO.getFreezeMaxMoney().compareTo(BigDecimal.ZERO) > 0) {
                            disDTO.setFreezeMaxMoney(disDTO.getFreezeMaxMoney().subtract(money));//冻结额度减少
                        }
                    } else {
                        disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().add(applyMoney));
                    }
                }
            }
            // 更新占用金额
            disDtlService.merge(disDTO);
        }
    }

    /**
     * 处理旧占用授信最高占用额度
     *
     * @param uration
     * @param applyDtls
     * @param disDetails
     */
    private void setRationDisMaxMoney(RatUration uration, List<RatUrationApplyDtlDTO> applyDtls, List<RatBkrationDisDetail> disDetails, boolean shareFlag) {
        getEntityDao().flush();
        Map<String, RatUrationApplyDtlDTO> applyMap = Maps.uniqueIndex(applyDtls, RatUrationApplyDtlDTO::getItemCode);// 授信占用信息
        for (RatBkrationDisDetail disDTO : disDetails) {
            if (!RationConstant.RAT_COMFLAG_1.equals(disDTO.getItemFlag())) {//非综合跳过
                continue;
            }
            String itemCode = disDTO.getItemCode();
            RatUrationApplyDtlDTO dtlDTO = applyMap.get(itemCode);//授信占用品种
            //判断额度是否足够
            BigDecimal applyMoney = BigDecimal.ZERO;
            BigDecimal money = BigDecimal.ZERO;
            if (null != dtlDTO) {
                applyMoney = dtlDTO.getApplyMoney();//实际应该占用额度
                if (dtlDTO.getMaxMoney() != null) {
                    money = dtlDTO.getMaxMoney();//综合占用额度
                }
            }
            if (RationConstant.RAT_USE_STATUS_CLOSE.equals(uration.getStatus())) {// 审批通过
                if (shareFlag) {//不需要更新虚拟占用额度
                    disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().subtract(applyMoney.add(money)));
                } else {
                    disDTO.setProcessMaxMoney(disDTO.getProcessMaxMoney().subtract(money));
                    disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().subtract(applyMoney));
                }
            }
            // 更新占用金额
            disDtlService.merge(disDTO);
        }
    }

    /**
     * 功能介绍 : 总额共享下处理占用单位外的其他单位的占用额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @param freezeFlag
     * @version 1.0
     * @author liyh
     */
    private void processAmontShareDisMoney(RatUration uration, String ratCode, boolean freezeFlag) {
        List<RatBkrationDis> rationDisList = disService.getOtherRationDisByDisCode(ratCode, uration.getDisCode(),
                uration.getItemFlag(), uration.getItemCode());
        if (!EmptyUtils.isEmptyList(rationDisList)) {
            for (RatBkrationDis bkrationDis : rationDisList) {
                List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(bkrationDis.getId(),
                        uration.getItemFlag(), uration.getItemCode());
                if (EmptyUtils.isEmptyList(disDetails)) {
                    continue;
                }
                processAmontShareDisDtlMoney(uration, ratCode, disDetails, freezeFlag);
            }
        }
    }

    /**
     * 功能介绍 : 总额共享下处理占用单位外的分配详情的占用额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @param freezeFlag
     * @version 1.0
     * @author liyh
     */
    private void processAmontShareDisDtlMoney(RatUration uration, String ratCode, List<RatBkrationDisDetail> disDetails,
                                              boolean freezeFlag) {
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            for (RatBkrationDisDetail disDetail : disDetails) {
                if (RationConstant.RAT_USE_STATUS_FREEZE.equals(uration.getStatus())) {// 占用待审批
                    disDetail.setVirtualUration(disDetail.getVirtualUration().add(uration.getMoney()));// 冻结最高额度
                    disDetail.setVirtualOpenUration(disDetail.getVirtualOpenUration().add(uration.getOpenMoney()));
                } else if (RationConstant.RAT_USE_STATUS_USE.equals(uration.getStatus())) {// 占用审批通过
                    disDetail.setReliefAndUrationMoney(disDetail.getReliefAndUrationMoney().add(uration.getMoney()));// 占用额度增加
                    disDetail.setUrationOpenMoney(disDetail.getUrationOpenMoney().add(uration.getOpenMoney()));
                    if (freezeFlag) {
                        if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                            disDetail.setVirtualUration(disDetail.getVirtualUration().subtract(uration.getMoney()));// 冻结额度还原
                            disDetail.setVirtualOpenUration(
                                    disDetail.getVirtualOpenUration().subtract(uration.getOpenMoney()));
                        }
                    }
                } else if (RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {// 打回
                    disDetail.setVirtualUration(disDetail.getVirtualUration().subtract(uration.getMoney()));// 冻结额度还原
                    disDetail.setVirtualOpenUration(disDetail.getVirtualOpenUration().subtract(uration.getOpenMoney()));
                }
            }
        } else {
            List<RatUrationApplyDtlDTO> applyDtls = applyDtlService.queryRatUrationById(uration.getId());// 综合占用最高额度
            Map<String, RatUrationApplyDtlDTO> applyMap = Maps.uniqueIndex(applyDtls,
                    new Function<RatUrationApplyDtlDTO, String>() {
                        public String apply(RatUrationApplyDtlDTO input) {
                            return input.getItemCode();
                        }
                    });
            // 取综合占用最高额度
            for (RatBkrationDisDetail disDetail : disDetails) {
                // 处理占用额度
                if (RationConstant.RAT_COMFLAG_1.equals(disDetail.getCompSign())) {
                    if (RationConstant.RAT_USE_STATUS_FREEZE.equals(uration.getStatus())) {// 占用待审批
                        disDetail.setVirtualUration(disDetail.getVirtualUration().add(uration.getMoney()));// 冻结最高额度
                        disDetail.setVirtualOpenUration(disDetail.getVirtualOpenUration().add(uration.getOpenMoney()));
                    } else if (RationConstant.RAT_USE_STATUS_USE.equals(uration.getStatus())) {// 占用审批通过
                        disDetail
                                .setReliefAndUrationMoney(disDetail.getReliefAndUrationMoney().add(uration.getMoney()));// 占用额度增加
                        disDetail.setUrationOpenMoney(disDetail.getUrationOpenMoney().add(uration.getOpenMoney()));
                        if (freezeFlag) {
                            if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                                disDetail.setVirtualUration(disDetail.getVirtualUration().subtract(uration.getMoney()));// 冻结额度还原
                                disDetail.setVirtualOpenUration(
                                        disDetail.getVirtualOpenUration().subtract(uration.getOpenMoney()));
                            }
                        }
                    } else if (RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {// 打回
                        disDetail.setVirtualUration(disDetail.getVirtualUration().subtract(uration.getMoney()));// 冻结额度还原
                        disDetail.setVirtualOpenUration(
                                disDetail.getVirtualOpenUration().subtract(uration.getOpenMoney()));
                    }
                }
                // 处理综合占用最高额度
                String itemCode = disDetail.getItemCode();
                RatUrationApplyDtlDTO applyDtlDTO = applyMap.get(itemCode);
                if (null != applyDtlDTO) {
                    // processDisMaxMoney(disDetail, applyDtlDTO, uration.getStatus(), true, freezeFlag);
                }
            }
            setRationDisMaxMoney(uration, applyDtls, disDetails, true, freezeFlag);// 总额共享下处理最高占用额度
        }
    }

    /**
     * 功能介绍 : 处理冻结最高额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @param status    占用台账的状态
     * @param applyFlag 占用标识 true:占用 false:释放
     * @version 1.0
     * @author liyh
     */
    private void processDisMaxMoney2(RatBkrationDisDetail disDetail, RatUrationApplyDtlDTO applyDtlDTO, Integer status,
                                     boolean applyFlag, boolean freezeFlag) {
        if (applyFlag) {// 占用
            if (RationConstant.RAT_USE_STATUS_FREEZE.equals(status)) {// 占用暂存
                disDetail.setFreezeMaxMoney(disDetail.getFreezeMaxMoney().add(applyDtlDTO.getMaxMoney()));
            } else if (RationConstant.RAT_USE_STATUS_USE.equals(status)) {// 占用提交
                disDetail.setProcessMaxMoney(disDetail.getProcessMaxMoney().add(applyDtlDTO.getMaxMoney()));// 占用额度增加
                if (freezeFlag) {
                    disDetail.setFreezeMaxMoney(disDetail.getFreezeMaxMoney().subtract(applyDtlDTO.getMaxMoney()));// 还原冻结额度
                }
            } else if (RationConstant.RAT_USE_STATUS_RELEASE.equals(status)) {// 打回
                disDetail.setFreezeMaxMoney(disDetail.getFreezeMaxMoney().subtract(applyDtlDTO.getMaxMoney()));
            }
        } else {// 释放
            disDetail.setProcessMaxMoney(disDetail.getProcessMaxMoney().subtract(applyDtlDTO.getMaxMoney()));// 占用额度减少
        }
    }

    /**
     * 功能介绍 : 释放占用最高额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void releaseDisMaxMoney(RatBkrationDisDetail disDetail, RatUrationApplyDtlDTO applyDtlDTO, String isShare) {
        if (disDetail.getProcessMaxMoney().compareTo(BigDecimal.ZERO) > 0) {
            disDetail.setProcessMaxMoney(disDetail.getProcessMaxMoney().subtract(applyDtlDTO.getMaxMoney()).setScale(2,
                    BigDecimal.ROUND_HALF_UP));// 占用额度减少
        } else {
            disDetail.setProcessMaxMoney(BigDecimal.ZERO);
        }
        if ("1".equals(isShare) && EmptyUtils.isNotEmpty(isShare)) {
            if (applyDtlDTO.getApplyMoney().compareTo(BigDecimal.ZERO) > 0) {
                disDetail.setVirtualMaxMoney(disDetail.getVirtualMaxMoney().subtract(applyDtlDTO.getApplyMoney())
                        .setScale(2, BigDecimal.ROUND_HALF_UP));// 虚拟占用额度减少
            } else {
                disDetail.setVirtualMaxMoney(disDetail.getVirtualMaxMoney().subtract(applyDtlDTO.getMaxMoney())
                        .setScale(2, BigDecimal.ROUND_HALF_UP));// 虚拟占用额度减少
            }
        } else {
            disDetail.setVirtualMaxMoney(disDetail.getVirtualMaxMoney().subtract(applyDtlDTO.getApplyMoney())
                    .setScale(2, BigDecimal.ROUND_HALF_UP));// 虚拟占用额度减少
        }
        disDtlService.merge(disDetail);
    }

    /**
     * 功能介绍: 审批打回，更新占用申请(-1)和占用台账状态(3释放占用)。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateUrationBack(String urationId, String userId) {
        RatUrationApply apply = get(urationId, true);
        apply.setStatus(CommonConstant.STATUE_BACK);
        apply.setUpdateBy(userId);
        apply.setUpdateDate(DateUtils.getDate());
        updateEntity(apply);
        List<RatUration> uList = urationService.queryRatUrationById(ConfigConstant.RAT, apply.getId());
        if (!EmptyUtils.isEmptyList(uList)) {
            RatUration uration = uList.get(0);
            backUration(uration, userId);
        }
    }

    /**
     * 功能介绍 : 授信打回（还原冻结额度）。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void backUration(RatUration uration, String userId) {
        uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 额度释放
        uration.setUpdateBy(userId);
        uration.setUpdateDate(DateUtils.getDate());
        urationService.updateEntity(uration);
        // 还原分配表中冻结金额,如果是总额共享的情况下还需要更新其他单位的虚拟占用额度
        if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
            // 更新分配表中占用额度，冻结金额减少
            updateRationDisMoney(uration, true);
        }
    }

    /**
     * 功能介绍: 修改删除，更新占用申请(-2)和占用台账状态(3释放占用)。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateUrationDel(String urationId, String userId) {
        RatUrationApply apply = get(urationId, true);// 授信占用申请
        apply.setStatus(CommonConstant.STATUE_DELETE);// 删除状态
        apply.setUpdateBy(userId);
        apply.setUpdateDate(DateUtils.getDate());
        updateEntity(apply);
        List<RatUration> uList = urationService.queryRatUrationById(ConfigConstant.RAT, apply.getId(),
                apply.getRatType());
        if (!EmptyUtils.isEmptyList(uList)) {
            RatUration uration = uList.get(0);
            deleteUration(uration, userId);
            // 打回和占用暂存状态下未占用授信，不需要还原冻结额度
        }
    }

    /**
     * 功能介绍 : 授信删除（标识已释放）。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteUration(RatUration uration, String userId) {
        Integer status = uration.getStatus();
        if (!RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {
            uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 额度释放
            uration.setUpdateBy(userId);
            uration.setUpdateDate(DateUtils.getDate());
            urationService.updateEntity(uration);
            // 还原分配表中冻结金额,如果是总额共享的情况下还需要更新其他单位的虚拟占用额度
            if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0
                    && RationConstant.RAT_USE_STATUS_FREEZE.equals(status)) {
                // 更新分配表中占用额度，冻结金额减少
                updateRationDisMoney(uration, true);
            }
        }
    }

    /**
     * 功能介绍: 授信额度释放，还原占用额度和总额共享下的虚拟占用额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateUrationRelease(String urationId, String userId) {
        RatUrationApply apply = get(urationId, true);// 授信占用申请
        if (!RationConstant.STATUS_CHECK.equals(apply.getStatus())) {// 审批通过的才能释放占用
            throw new ParameterException(RatMessageEnum.CMS_RAT_USE_RELEASED);
        }
        apply.setStatus(RationConstant.STATUS_RELEASE);// 释放状态
        apply.setUpdateBy(userId);
        apply.setUpdateDate(DateUtils.getDate());
        updateEntity(apply);
        List<RatUration> uList = urationService.queryRatUrationById(ConfigConstant.RAT, apply.getId(),
                apply.getRatType());
        if (!EmptyUtils.isEmptyList(uList)) {
            RatUration uration = uList.get(0);
            releaseUration(uration, userId);
        }
    }

    /**
     * 功能介绍 : 授信释放。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void releaseUration(RatUration uration, String userId) {
        uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 额度释放
        uration.setUpdateBy(userId);
        uration.setUpdateDate(DateUtils.getDate());
        urationService.updateEntity(uration);
        // 还原授信占用额度，总额共享下还需要还原虚拟占用额度
        updateRationReleaseMoney(uration);
    }

    /**
     * 功能介绍 : 释放授信分配占用额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void updateRationReleaseMoney(RatUration uration) {
        getEntityDao().flush();
        List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(uration.getDisCode(),
                uration.getItemFlag(), uration.getItemCode());
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            RatBkrationDisDetail disDetail = disDetails.get(0);
            disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().subtract(uration.getMoney()));// 占用额度还原
            disDetail.setSuperProcessOpenMoney(disDetail.getSuperProcessOpenMoney().subtract(uration.getOpenMoney()));
            disDtlService.merge(disDetail);
        } else {
            List<RatUrationApplyDtlDTO> applyDtls = applyDtlService.queryRatUrationById(uration.getId());// 综合占用最高额度
            Map<String, RatUrationApplyDtlDTO> applyMap = Maps.uniqueIndex(applyDtls,
                    new Function<RatUrationApplyDtlDTO, String>() {
                        public String apply(RatUrationApplyDtlDTO input) {
                            return input.getItemCode();
                        }
                    });
            // 取综合占用最高额度
            for (RatBkrationDisDetail disDetail : disDetails) {
                // 处理占用额度
                if (RationConstant.RAT_COMFLAG_1.equals(disDetail.getCompSign())) {
                    disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().subtract(uration.getMoney()));// 占用额度还原
                    disDetail.setSuperProcessOpenMoney(
                            disDetail.getSuperProcessOpenMoney().subtract(uration.getOpenMoney()));
                }
                // 处理综合占用最高额度
                String itemCode = disDetail.getItemCode();
                RatUrationApplyDtlDTO applyDtlDTO = applyMap.get(itemCode);
                if (null != applyDtlDTO) {
                    // processDisMaxMoney(disDetail, applyDtlDTO, uration.getStatus(), false, true);
                    releaseDisMaxMoney(disDetail, applyDtlDTO, null);
                } else {
                    disDtlService.merge(disDetail);
                }
            }
            // setRationDisMaxMoney(uration, applyDtls, disDetails);
        }
        // disDtlService.batchUpdate(disDetails);
        // 判断是否总额共享，如果总额共享，则相应的其他单位都需要调整虚拟占用额度
        RatBkrationDis rationDis = disService.get(uration.getDisCode(), true);
        if (rationDis != null && RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(rationDis.getDistributeWay())) {
            processAmountShareReleaseMoney(uration, rationDis.getRatCode());
        }
    }

    /**
     * 功能介绍 : 总额共享下释放占用单位外的其他单位的占用额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void processAmountShareReleaseMoney(RatUration uration, String ratCode) {
        List<RatBkrationDisDetail> disDetails = disDtlService.getOtherRationDisDtlByDisCode(ratCode,
                uration.getDisCode(), uration.getItemFlag(), uration.getItemCode());
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            for (RatBkrationDisDetail disDetail : disDetails) {
                disDetail.setReliefAndUrationMoney(disDetail.getReliefAndUrationMoney().subtract(uration.getMoney()));// 虚拟占用额度还原
                disDetail.setUrationOpenMoney(disDetail.getUrationOpenMoney().subtract(uration.getOpenMoney()));
            }
        } else {
            List<RatUrationApplyDtlDTO> applyDtls = applyDtlService.queryRatUrationById(uration.getId());// 综合占用最高额度
            Map<String, RatUrationApplyDtlDTO> applyMap = Maps.uniqueIndex(applyDtls,
                    new Function<RatUrationApplyDtlDTO, String>() {
                        public String apply(RatUrationApplyDtlDTO input) {
                            return input.getItemCode();
                        }
                    });
            // 取综合占用最高额度
            for (RatBkrationDisDetail disDetail : disDetails) {
                // 处理占用额度
                if (RationConstant.RAT_COMFLAG_1.equals(disDetail.getCompSign())) {
                    disDetail.setReliefAndUrationMoney(
                            disDetail.getReliefAndUrationMoney().subtract(uration.getMoney()));// 虚拟占用额度还原
                    disDetail.setUrationOpenMoney(disDetail.getUrationOpenMoney().subtract(uration.getOpenMoney()));
                }
                // 处理综合占用最高额度
                String itemCode = disDetail.getItemCode();
                RatUrationApplyDtlDTO applyDtlDTO = applyMap.get(itemCode);
                if (null != applyDtlDTO) {
                    // processDisMaxMoney(disDetail, applyDtlDTO, uration.getStatus(), false, true);
                    releaseDisMaxMoney(disDetail, applyDtlDTO, "1");
                }
            }
            // setRationDisMaxMoney(uration, applyDtls, disDetails);
        }
    }

    /**
     * 功能介绍 : 更新授信占用信息。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRatUrationApply(RatUrationApplyListDTO applyDTO, String userId) {
        notificationService.complete(NotificationConstant.CMS.RAT_APPLY, applyDTO.getId(), userId);
        // 检查授信占用额度
        checkUrationMoney(applyDTO);
        RatUrationApply apply = get(applyDTO.getId(), true);// 授信占用申请
        if (RationConstant.RAT_COMFLAG_1.equals(apply.getItemFlag())) {
            // 删除原综合占用明细
            applyDtlService.deleteRatUrationDtlByCode(apply.getId());
        }
        apply.setItemFlag(applyDTO.getItemFlag());
        if (RationConstant.RAT_COMFLAG_0.equals(applyDTO.getItemFlag())) {
            apply.setItemCode(applyDTO.getItemCode());
        }
        String[] ignoreProperties = new String[]{"type", "corpId", "id", "ratType", "ratCode", "curCode", "isCur",
                "exchange", "createBy", "createDate"};
        BeanUtils.copyProperties(applyDTO, apply, ignoreProperties);
        apply.setUpdateBy(userId);
        apply.setUpdateDate(DateUtils.getDate());
        // 添加币种名称
        if (StrUtil.isBlank(apply.getCurName()) && StrUtil.isNotBlank(apply.getInitCurCode())) {
            BtCurrencyDTO currency = currencyService.getObjectById(apply.getInitCurCode());
            if (ObjectUtil.isNotEmpty(currency)) {
                apply.setCurName(currency.getCurName());
                apply.setExchange(currency.getCurExrate());
            }
        }
        saveOrUpdateUrationApply(apply);
        // 综合占用明细
        if (RationConstant.RAT_COMFLAG_1.equals(applyDTO.getItemFlag())) {
            List<RatUrationApplyDtlDTO> applyDtls = applyDTO.getApplyDtls();
            saveRationApplyDtl(apply.getDisCode(), apply.getId(), RationConstant.RAT_USE_TYPE_0, applyDtls);
        }
        // 写入占用表
        if (apply.getStatus().equals(CommonConstant.STATUE_WAIT_CHECK)) {
            // 获取原占用授信信息
            RatUration uration = null;
            List<RatUration> uList = urationService.queryRatUrationById(ConfigConstant.RAT, apply.getId());
            if (!EmptyUtils.isEmptyList(uList)) {
                uration = uList.get(0);
                // 删除原占用综合数据
                if (RationConstant.RAT_COMFLAG_1.equals(uration.getItemFlag())) {
                    applyDtlService.deleteRatUrationDtlByCode(uration.getId());
                }
                uration.setUpdateBy(userId);
                uration.setUpdateDate(DateUtils.getDate());
            } else {
                uration = new RatUration();
                uration.setCreateBy(userId);
                uration.setCreateDate(DateUtils.getDate());
                uration.setUrationCode(rationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_URATIONCODE,
                        RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
            }
            BeanUtils.copyProperties(apply, uration, "id");// ID不复制
            uration.setInitCurCode(apply.getCurCode());// 业务币别
            uration.setPreMoney(apply.getMoney());// 折算前金额
            if (apply.getInitCurCode().equals(apply.getCurCode())) {
                uration.setConvertPreMoney(apply.getMoney().multiply(apply.getExchange()));// 折算后的正式占用金额
            } else {
                uration.setConvertPreMoney(apply.getMoney());
            }
            uration.setExchangeRate(apply.getExchange());// 汇率
            uration.setBillId(apply.getId());// 占用申请ID
            uration.setBillNo(apply.getApplyCode());// 占用申请编号
            uration.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 授信占用的状态
            uration.setSyscode(ConfigConstant.RAT);// 授信
            uration.setBusinessName(RationConstant.BUSINESS_NAME_05);// 银行台账
            if (RationConstant.RAT_COMFLAG_0.equals(applyDTO.getItemFlag())) {
                uration.setItemCode(applyDTO.getItemCode());
            }
            urationService.saveOrUpdateUration(uration);
            if (RationConstant.RAT_COMFLAG_1.equals(applyDTO.getItemFlag())) {
                List<RatUrationApplyDtlDTO> applyDtls = applyDTO.getApplyDtls();
                saveRationApplyDtl(apply.getDisCode(), uration.getId(), RationConstant.RAT_USE_TYPE_1, applyDtls);
            }
            getEntityDao().flush();
            // 冻结授信占用额度
            updateRationDisMoney(uration, true);
            // 挂审批
            getStartHandler().handler(RatWorkFlowConstant.RAT_URATION_APPLY, apply.getId(), apply, apply.getCorpId(),
                    userId);
        }
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();// 业务ID
        boolean result = workFlowResult.isPass();// 是否通过
        String userId = workFlowResult.getUser().getId();
        getEntityDao().flush();
        if (result) {// 审批通过
            updateUrationPass(businessKey, userId);
        } else {// 审批打回
            updateUrationBack(businessKey, userId);
        }
    }

    /**
     * 功能介绍 : 金额非空处理。<br>
     * <b>日期：</b>2017年8月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveOrUpdateUrationApply(RatUrationApply apply) {
        Class userCla = apply.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(apply);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(apply, BigDecimal.ZERO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.saveOrUpdate(apply);
    }

    /**
     * 根据担保id设置担保方式 todo 没地方引用？？？？
     */
    public String getGuaWaysName(String guaWayIds) {
        // 设置担保方式 通过rpc接口调用
        List<GuaWayDTO> allGuaWay = iGuaWayServie.getAllGuaWay();
        String[] guaWayArr = guaWayIds.split(",");
        String guaWayNames = "";
        if (StringUtils.isNotEmpty(guaWayArr)) {
            for (GuaWayDTO guaRanWay : allGuaWay) {
                for (String guaWay : guaWayArr) {
                    if (guaRanWay.getId().equals(guaWay)) {
                        guaWayNames += guaRanWay.getName() + ",";
                    }
                }
            }
        }
        return guaWayNames;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 银行授信占用在途数据
            int dataSize1 = ratUrationApplyDao.queryBusBillByCorpId(corpId, "-1,0,11");
            if (dataSize1 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_BANKRATION_APPLY_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }

    /**
     * 自动更新占用授信数据
     *
     * @param arguments
     */
    @XxlJob("RatUrationApplyService")
    public void run(String arguments) {
        List<String> ratCodes = ratUrationApplyDao.queryAllRatDisList();
        DataTablesInput input = new DataTablesInput();
        // ----------- 每次更新10笔授信记录 -----------
        input.setLength(10);
    }

    public List<String> calcRationMoney(List<RatUrationDisBean> disBeans) {
        List<RatBkUseVO> ratLists = BeanUtils.copyListProperties(disBeans, RatBkUseVO.class);
        statisticsSercive.groupbyRatUserVo(ratLists);// 计算授信品种的在审、占用额度情况
        Map<String, RatBkUseVO> ratMap = new HashMap<>();
        for (RatBkUseVO useVO : ratLists) {
            ratMap.put(useVO.getId(), useVO);// 分配ID
        }
        List<String> ratCodes = new ArrayList<>();
        for (RatUrationDisBean disBean : disBeans) {
            String ratCode = disBean.getRatCode();
            RatBkUseVO useVO = ratMap.get(disBean.getId());// 分配ID
            if (null != useVO) {
                disBean.setFreezeMoney(useVO.getFreezeMoney());// 在审
                disBean.setUseMoney(useVO.getUseMoney());// 占用
                disBean.setVirtualUration(useVO.getVirtualUration());// 虚拟在途占用
                disBean.setReliefAndUrationMoney(useVO.getReliefAndUrationMoney());// 虚拟占用
                disBean.setFreezeMaxMoney(useVO.getFreezeMaxMoney());// 在审
                disBean.setUseMaxMoney(useVO.getUseMaxMoney());// 占用
                disBean.setVirtualUrationMaxMoney(useVO.getVirtualUrationMaxMoney());// 虚拟在途占用
                disBean.setVirtualMaxMoney(useVO.getVirtualMaxMoney());// 虚拟占用
            }
            if (!ratCodes.contains(ratCode)) {
                ratCodes.add(ratCode);
            }
        }
        return ratCodes;
    }

//    public List<RatUrationSysBean> queryRatUrationApply(List<String> ratCodes) {
//        List<RatUrationCurVO> urationCurVOS = statisticsSercive.queryRatApplyMoney(ratCodes);// 授信占用情况
//        Map<String, BigDecimal> rateMap = new HashMap<String, BigDecimal>();
//        Map<String, RatUrationSysBean> beanMap = new HashMap<>();
//        for (RatUrationCurVO curVO : urationCurVOS) {
//            String disCode = curVO.getDisCode();
//            String sysCode = curVO.getSysCode();
//            String itemCode = curVO.getItemCode();
//            Integer status = curVO.getStatus();
//            String key = disCode + "_" + sysCode + "_" + itemCode + "_" + status;
//            RatUrationSysBean sysBean = beanMap.get(key);
//            if (null == sysBean) {
//                sysBean = new RatUrationSysBean();
//                sysBean.setRatCode(disCode);
//                sysBean.setItemCode(itemCode);
//                sysBean.setSysCode(sysCode);
//                sysBean.setStatus(status.toString());
//                beanMap.put(key, sysBean);
//            }
//            BigDecimal money = statisticsSercive.calBillMoney(curVO, rateMap);// 业务金额
//            sysBean.setMoney(sysBean.getMoney().add(money));
//        }
//        return new ArrayList<RatUrationSysBean>(beanMap.values());
//    }

    /**
     * 功能介绍 : 更新授信合同占用数据 <br>
     * <b>日期：</b>2021年12月17日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRatUrationApply(String disId, String ratCode, String userId) {
        if (EmptyUtils.isEmpty(ratCode)) {
            return;
        }
        List<RatBkRationDtlVm> disDtlList = disDtlService.getBkrationDtlByRatCode(ratCode);
        if (EmptyUtils.isEmptyList(disDtlList)) {
            return;
        }
        Map<String, RatBkRationDtlVm> disDtlMap = new HashMap<>();
        for (RatBkRationDtlVm dtlDTO : disDtlList) {
            // String disCode = dtlDTO.getId();
            disDtlMap.put(dtlDTO.getId(), dtlDTO);
        }
        UrationParamDTO paramDTO = new UrationParamDTO();
        paramDTO.setBankCtlType(RationConstant.RAT_BANKTYPE_SAME);
        paramDTO.setStatus(RationConstant.STATUS_CHECK + "," + RationConstant.STATUS_INVALID);// 授信额度明细表可查已作废的数据
        paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);// 元
        paramDTO.setRatCode(ratCode);// 整个授信合同的数据一起更新
        List<RatUrationDisBean> disBeans = ratUrationService.queryRatUseMoney(paramDTO);
        // 银行类别相同
        if (!EmptyUtils.isEmpty(paramDTO.getBankCtlType()) && paramDTO.getBankCtlType().equals(RationConstant.RAT_BANKTYPE_SAME)) {
            List<String> bankCodes = disBeans.stream().map(RatUrationDisBean::getBankCode).collect(Collectors.toList());
            Map<String, InputBankInfoDTO> bankCodeMap = bankInfoService.getBankListCheck(bankCodes, paramDTO.getBankPrefix());
            disBeans.removeIf(bean -> !bankCodeMap.containsKey(bean.getBankCode()));
        }
        List<String> ratCodes = calcRationMoney(disBeans);
        List<RatUrationUseVO> rations = ratUrationService.transRationUserDtl(paramDTO, disBeans, ratCodes);
        for (RatUrationDisBean disBean : disBeans) {
            String itemFlag = disBean.getItemFlag();// 品种类型
            if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                compareSingleRation(disBean, disDtlMap);
            }
        }
        Map<String, RatUrationDisBean> disMap = new HashMap<>();
        // 根据授信额度信息更新分配表的占用金额和在审占用金额
        for (RatUrationDisBean disBean : disBeans) {
            disMap.put(disBean.getId(), disBean);
        }
        for (RatUrationUseVO useVO : rations) {
            List<RatUrationUseDtlVO> disList = useVO.getRationDlts();
            List<RatUrationDisBean> compList = new ArrayList<>();
            RatUrationDisBean compBean = new RatUrationDisBean();
            for (RatUrationUseDtlVO useDtlVO : disList) {
                String itemFlag = useDtlVO.getItemFlag();// 品种类型
                if (RationConstant.RAT_COMFLAG_1.equals(itemFlag)) {
                    compareCompRation(useDtlVO, disDtlMap);
                }
            }
        }
    }

    public void compareCompRation(RatUrationUseDtlVO useVO, Map<String, RatBkRationDtlVm> disDtlMap) {
        List<RatUrationUseDtlVO> rationDlts = useVO.getRationDlts();
        if (EmptyUtils.isEmptyList(rationDlts)) {
            return;
        }
        BigDecimal money = BigDecimal.ZERO;
        money = useVO.getUseMoney().add(useVO.getApproveMoney()).add(useVO.getVirtualMaxMoney())
                .add(useVO.getVirtualUrationMaxMoney());
        // 设置每个品种的总额度
        for (RatUrationUseDtlVO disBean : rationDlts) {
            String compSign = disBean.getCompSign();// 综合授信第一个标识
            if (RationConstant.RAT_COMFLAG_1.equals(compSign)) {
                // disBean.setUseMoney(compBean.getUseMaxMoney());
                // disBean.setReliefAndUrationMoney(compBean.getVirtualUrationMaxMoney());
            }
            String disId = disBean.getId();
            disBean.setVirtualMaxMoney(money.subtract(disBean.getUseMoney()).subtract(disBean.getApproveMoney()));
            updateCompRationDtlMoney(disBean);
        }
    }

    public void compareSingleRation(RatUrationDisBean disBean, Map<String, RatBkRationDtlVm> disDtlMap) {
        disDtlService.updateRationDisDetail(disBean);
    }

    public void updateCompRationDtlMoney(RatUrationUseDtlVO disBean) {
        disDtlService.updateCompRationDisDetail(disBean);
    }

    /**
    * @Description 票据指令失败打回
    * @author zhous
    * @date 2025/5/21 17:54
    */
    public void billReleaseRation(RatUration uration, String userId) {
        uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 额度释放
        uration.setUpdateBy(userId);
        uration.setUpdateDate(DateUtils.getDate());
        urationService.updateEntity(uration);
        if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
            // 更新分配表中占用额度，冻结金额减少
            billUpdateRationDisMoney(uration, true);
        }
    }

    /**
     * 功能介绍 : 更新授信分配占用额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @param freezeFlag 是否更新冻结额度
     * @version 1.0
     * @author liyh
     */
    @Transactional
    public void billUpdateRationDisMoney(RatUration uration, boolean freezeFlag) {
        getEntityDao().flush();
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(uration.getDisCode(), uration.getItemFlag(), uration.getItemCode());
            RatBkrationDisDetail disDetail = disDetails.get(0);
            if (RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {//打回
                disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().subtract(uration.getMoney()));//占用额度减少
                disDetail.setSuperProcessOpenMoney(disDetail.getSuperProcessOpenMoney().subtract(uration.getOpenMoney()));
            }
            disDtlService.merge(disDetail);
        } else {
            List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(uration.getDisCode(), uration.getItemFlag(), null);
            List<RatUrationApplyDtlDTO> applyDtls = applyDtlService.queryRatUrationById(uration.getId());//综合占用最高额度
            // 取综合占用最高额度
            for (RatBkrationDisDetail disDetail : disDetails) {
                // 处理占用额度
                if (RationConstant.RAT_COMFLAG_1.equals(disDetail.getCompSign())) {
                    if (RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {//打回
                        disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().subtract(uration.getMoney()));//占用额度增加
                        disDetail.setSuperProcessOpenMoney(disDetail.getSuperProcessOpenMoney().subtract(uration.getOpenMoney()));
                    }
                    disDtlService.merge(disDetail);
                }
            }
            billSetRationDisMaxMoney(uration, applyDtls, disDetails, false, freezeFlag);// 非共享单位最高额度处理
        }
        // 判断是否总额共享，如果总额共享，则相应的其他单位都需要调整虚拟占用额度
        RatBkrationDis rationDis = disService.get(uration.getDisCode(), true);
        if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(rationDis.getDistributeWay())) {
            List<RatBkrationDis> rationDisList = disService.getOtherRationDisByDisCode(rationDis.getRatCode(), uration.getDisCode(),
                    uration.getItemFlag(), uration.getItemCode());
            if (!EmptyUtils.isEmptyList(rationDisList)) {
                for (RatBkrationDis bkrationDis : rationDisList) {
                    List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(bkrationDis.getId(),
                            uration.getItemFlag(), uration.getItemCode());
                    if (EmptyUtils.isEmptyList(disDetails)) {
                        continue;
                    }
                    billProcessAmontShareDisDtlMoney(uration, rationDis.getRatCode(), disDetails, freezeFlag);
                }
            }
        }
    }

    /**
     * 功能介绍 : 处理票据授信最高占用额度。<br>
     * <b>日期：</b>2017年9月21日<br>
     *
     * @param shareFlag 是否共享单位的最高额度处理
     * @version 1.0
     * @author liyh
     */
    private void billSetRationDisMaxMoney(RatUration uration, List<RatUrationApplyDtlDTO> applyDtls,
                                      List<RatBkrationDisDetail> disDetails, boolean shareFlag, boolean freezeFlag) {
        getEntityDao().flush();
        Integer status = uration.getStatus();
        //循环综合占用品种，判断占用后各综合品种的最高额度与综合额度的剩余额度
        Map<String, RatUrationApplyDtlDTO> applyMap = Maps.uniqueIndex(applyDtls, RatUrationApplyDtlDTO::getItemCode);// 授信占用信息

        for (RatBkrationDisDetail disDTO : disDetails) {
            if (!RationConstant.RAT_COMFLAG_1.equals(disDTO.getItemFlag())) {//非综合跳过
                continue;
            }
            String itemCode = disDTO.getItemCode();
            RatUrationApplyDtlDTO dtlDTO = applyMap.get(itemCode);//授信占用品种
            //判断额度是否足够
            BigDecimal applyMoney = BigDecimal.ZERO;
            BigDecimal money = BigDecimal.ZERO;
            if (null != dtlDTO) {
                applyMoney = dtlDTO.getApplyMoney();//实际应该占用额度
                if (dtlDTO.getMaxMoney() != null) {
                    money = dtlDTO.getMaxMoney();//综合占用额度
                }
            }
            if (RationConstant.RAT_USE_STATUS_RELEASE.equals(status)) {// 审批打回
                if (shareFlag) {//总额共享情况下
                    disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().subtract(applyMoney.add(money)));
                } else {
                    // 更新占用金额
                    disDTO.setProcessMaxMoney(disDTO.getProcessMaxMoney().subtract(money));
                }
            }
            // 更新占用金额
            disDtlService.merge(disDTO);
        }
    }

    /**
     * 功能介绍 : 总额共享下处理占用单位外的分配详情的占用额度。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @param freezeFlag
     * @version 1.0
     * @author liyh
     */
    private void billProcessAmontShareDisDtlMoney(RatUration uration, String ratCode, List<RatBkrationDisDetail> disDetails,
                                              boolean freezeFlag) {
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
//            for (RatBkrationDisDetail disDetail : disDetails) {
//                if (RationConstant.RAT_USE_STATUS_FREEZE.equals(uration.getStatus())) {// 占用待审批
//                    disDetail.setVirtualUration(disDetail.getVirtualUration().add(uration.getMoney()));// 冻结最高额度
//                    disDetail.setVirtualOpenUration(disDetail.getVirtualOpenUration().add(uration.getOpenMoney()));
//                } else if (RationConstant.RAT_USE_STATUS_USE.equals(uration.getStatus())) {// 占用审批通过
//                    disDetail.setReliefAndUrationMoney(disDetail.getReliefAndUrationMoney().add(uration.getMoney()));// 占用额度增加
//                    disDetail.setUrationOpenMoney(disDetail.getUrationOpenMoney().add(uration.getOpenMoney()));
//                    if (freezeFlag) {
//                        if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
//                            disDetail.setVirtualUration(disDetail.getVirtualUration().subtract(uration.getMoney()));// 冻结额度还原
//                            disDetail.setVirtualOpenUration(
//                                    disDetail.getVirtualOpenUration().subtract(uration.getOpenMoney()));
//                        }
//                    }
//                } else if (RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {// 打回
//                    disDetail.setVirtualUration(disDetail.getVirtualUration().subtract(uration.getMoney()));// 冻结额度还原
//                    disDetail.setVirtualOpenUration(disDetail.getVirtualOpenUration().subtract(uration.getOpenMoney()));
//                }
//            }
        } else {
            List<RatUrationApplyDtlDTO> applyDtls = applyDtlService.queryRatUrationById(uration.getId());// 综合占用最高额度
            Map<String, RatUrationApplyDtlDTO> applyMap = Maps.uniqueIndex(applyDtls,
                    new Function<RatUrationApplyDtlDTO, String>() {
                        public String apply(RatUrationApplyDtlDTO input) {
                            return input.getItemCode();
                        }
                    });
//            // 取综合占用最高额度
//            for (RatBkrationDisDetail disDetail : disDetails) {
//                // 处理占用额度
//                if (RationConstant.RAT_COMFLAG_1.equals(disDetail.getCompSign())) {
//                    if (RationConstant.RAT_USE_STATUS_FREEZE.equals(uration.getStatus())) {// 占用待审批
//                        disDetail.setVirtualUration(disDetail.getVirtualUration().add(uration.getMoney()));// 冻结最高额度
//                        disDetail.setVirtualOpenUration(disDetail.getVirtualOpenUration().add(uration.getOpenMoney()));
//                    } else if (RationConstant.RAT_USE_STATUS_USE.equals(uration.getStatus())) {// 占用审批通过
//                        disDetail
//                                .setReliefAndUrationMoney(disDetail.getReliefAndUrationMoney().add(uration.getMoney()));// 占用额度增加
//                        disDetail.setUrationOpenMoney(disDetail.getUrationOpenMoney().add(uration.getOpenMoney()));
//                        if (freezeFlag) {
//                            if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
//                                disDetail.setVirtualUration(disDetail.getVirtualUration().subtract(uration.getMoney()));// 冻结额度还原
//                                disDetail.setVirtualOpenUration(
//                                        disDetail.getVirtualOpenUration().subtract(uration.getOpenMoney()));
//                            }
//                        }
//                    } else if (RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {// 打回
//                        disDetail.setVirtualUration(disDetail.getVirtualUration().subtract(uration.getMoney()));// 冻结额度还原
//                        disDetail.setVirtualOpenUration(
//                                disDetail.getVirtualOpenUration().subtract(uration.getOpenMoney()));
//                    }
//                }
//                // 处理综合占用最高额度
//                String itemCode = disDetail.getItemCode();
//                RatUrationApplyDtlDTO applyDtlDTO = applyMap.get(itemCode);
//                if (null != applyDtlDTO) {
//                    // processDisMaxMoney(disDetail, applyDtlDTO, uration.getStatus(), true, freezeFlag);
//                }
//            }
            billSetRationDisMaxMoney(uration, applyDtls, disDetails, true, freezeFlag);// 总额共享下处理最高占用额度
        }
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(RatUrationApplyVO ratUrationApplyVO, Object... objects) {
        return null;
    }

    /**
     * 银行授信占用新增导入数据校验
     *
     * @param list
     * @param obj
     */
    @Override
    public void verifyHandler(List<RatUrationApplyVO> list, Object... obj) {
        if (!ObjectUtils.isEmpty(list)) {
            if (!EmptyUtils.isEmpty(obj) && !EmptyUtils.isEmpty(obj[1])) {
                Map<String, String[]> params = (Map<String, String[]>) obj[1];
                String ratCode = ""; // 授信录入主键id
                String disCode = ""; // 授信分配主键id
                String ratCurId = "";// 授信币种
                String corpId = "";// 授信单位
                if (params.size() > 0) {
                    ratCode = params.get("ratCode")[0];
                    disCode = params.get("disCode")[0];
                    ratCurId = params.get("ratCurId")[0];
                    corpId = params.get("corpId")[0];
                }
                if (EmptyUtils.isEmpty(ratCode) || EmptyUtils.isEmpty(disCode) || EmptyUtils.isEmpty(ratCurId) || EmptyUtils.isEmpty(corpId)) {
                    for (RatUrationApplyVO vo : list) {
                        List<String> errorMsgList = new ArrayList<>();
                        errorMsgList.add("参数【ratCode】或【disCode】或【ratCurId】或【corpId】为空，请检查!");
                        vo.addErrorMsg(errorMsgList);
                    }
                }
                // 授信录入实体
                RatBkration ratBkration = rationService.get(ratCode, true);
                List<RatItemDisVO> disDetailList = ratBkrationDisDetailDao.getItemCustomDTOBySysItem(disCode, "");
                for (RatUrationApplyVO vo : list) {
                    List<String> errorMsgList = new ArrayList<>();
                    if (ObjectUtils.isEmpty(ratBkration)) {
                        errorMsgList.add("该授信已被删除，请刷新后重新操作");
                    } else {
                        // 占用授信品种（必填）
                        String itemName = vo.getItemCodeName();
                        if (!EmptyUtils.isEmpty(itemName)) {
                            if (CollectionUtils.isEmpty(disDetailList)) {
                                errorMsgList.add("占用授信品种未分配或不存在");
                            } else {
                                vo.setItemDisList(disDetailList);
                                List<String> itemNameList = disDetailList.stream().map(RatItemDisVO::getItemName).collect(Collectors.toList());
                                if (itemNameList.contains(itemName)) {
                                    RatItemDisVO disVo = disDetailList.stream().filter(item -> itemName.equals(item.getItemName())).collect(Collectors.toList()).get(0);
                                    vo.setItemCode(disVo.getItemCode());
                                    vo.setItemFlag(disVo.getItemFlag());
                                    vo.setItemFlagName(dictService.get("RatModel.ItemFlag", disVo.getItemFlag()));
                                    vo.setItemOrder(disVo.getItemOrder());
                                    vo.setCreditType(disVo.getCreditType());
                                    vo.setMaxMoney(disVo.getMaxMoney());
                                } else {
                                    errorMsgList.add("占用授信品种未分配或不存在");
                                }
                            }
                        } else {
                            errorMsgList.add("占用授信品种不能为空");
                        }
                        // 业务品种（非必填）
                        String raceName = vo.getRaceName();
                        if (!EmptyUtils.isEmpty(raceName) && !EmptyUtils.isEmpty(vo.getItemCode())) {
                            List<RatBusinessRaceVO> raceList = ratBusinessRaceDao.findAllByCustomId(vo.getItemCode());
                            if (CollectionUtils.isEmpty(raceList)) {
                                errorMsgList.add("业务品种不存在");
                            } else {
                                List<String> listA = raceList.stream().map(RatBusinessRaceVO::getRaceName).collect(Collectors.toList());
                                if (listA.contains(raceName)) {
                                    RatBusinessRaceVO raceVo = raceList.stream().filter(item -> raceName.equals(item.getRaceName())).collect(Collectors.toList()).get(0);
                                    vo.setBillName(raceVo.getId());
                                } else {
                                    errorMsgList.add("业务品种未维护");
                                }
                            }
                        }
                        // 是否启用银行授信敞口（必填）
                        ConfigDTO config = configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_STARTRATEXP);
                        if (CommonConstant.YES.equals(config.getValue()) && EmptyUtils.isEmpty(vo.getIsOpenRatName())) {
                            errorMsgList.add("是否启用银行授信敞口不能为空");
                        } else if (CommonConstant.YES.equals(config.getValue()) && EmptyUtils.isNotEmpty(vo.getIsOpenRatName())){
                            // 默认为否
                            if ("是".equals(vo.getIsOpenRatName())) {
                                vo.setIsOpenRat(CommonConstant.YES);
                            } else {
                                vo.setIsOpenRat(CommonConstant.NO);
                            }
                        }
                        // 业务币种（必填）
                        if (EmptyUtils.isEmpty(vo.getInitCurName())) {
                            errorMsgList.add("业务币种不能为空");
                        } else {
                            String initCurId = currencyService.getCurrencyByName(vo.getInitCurName());
                            if (EmptyUtils.isEmpty(initCurId)) {
                                errorMsgList.add("业务币种不存在");
                            } else {
                                vo.setInitCurCode(initCurId);
                            }
                        }
                        // 业务金额（必填）
                        if (EmptyUtils.isEmpty(vo.getBillMoney())) {
                            errorMsgList.add("业务金额不能为空");
                        } else {
                            if (vo.getBillMoney().compareTo(BigDecimal.ZERO) <= 0) {
                                errorMsgList.add("业务金额必须大于0");
                            }
                        }
                        // 保证金币种（非必填）
                        if (EmptyUtils.isEmpty(vo.getBondCurName())) {
                            // 如未填则默认为授信币种
                            vo.setBondCurId(ratCurId);
                        } else {
                            // 填了则保证金币种必须为：业务币种与本位币
                            if (!EmptyUtils.isEmpty(vo.getInitCurCode())) {
                                List<BtCurrencyDTO> listB = currencyService.getBondCurList(vo.getInitCurCode(), corpId);
                                // 获取 业务币种与本位币 币别名称集合
                                List<String> listC = listB.stream().map(BtCurrencyDTO::getCurName).collect(Collectors.toList());
                                if (listC.contains(vo.getBondCurName())) {
                                    String bondCurId = currencyService.getCurrencyByName(vo.getBondCurName());
                                    if (EmptyUtils.isEmpty(bondCurId)) {
                                        errorMsgList.add("保证金币种不存在");
                                    } else {
                                        vo.setBondCurId(bondCurId);
                                    }
                                } else {
                                    errorMsgList.add("保证金币种必须为业务币种或者授信单位的本位币币种");
                                }
                            }
                        }
                        // 业务币种
                        String initCurId = vo.getInitCurCode();
                        if (!EmptyUtils.isEmpty(initCurId) && !EmptyUtils.isEmpty(vo.getBillMoney())) {
                            if (!initCurId.equals(ratCurId)) {
                                // 授信币别与业务币别不一致需要查询汇率
                                BigDecimal exchangeRate = excrateAdjustService.getExchangeRateBy(initCurId, ratCurId, new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                                if (EmptyUtils.isEmpty(exchangeRate)) {
                                    exchangeRate = BigDecimal.ONE;
                                }
                                // 占用授信金额
                                vo.setMoney(vo.getBillMoney().multiply(exchangeRate));
                            } else {
                                // 占用授信金额
                                vo.setMoney(vo.getBillMoney());
                            }
                        }
                        // 授信占用额度是否足够
                        if (!EmptyUtils.isEmpty(vo.getMoney()) && !EmptyUtils.isEmpty(vo.getItemCode())) {
                            if (RationConstant.RAT_COMFLAG_0.equals(vo.getItemFlag())) {
                                // 单品种
                                List<RatBkrationCpAddDtlDTO> dtlDTOs = disService.getRationDisDtlByItemCode(disCode, vo.getItemFlag(), vo.getItemCode());
                                if (!EmptyUtils.isEmptyList(dtlDTOs)) {
                                    RatBkrationCpAddDtlDTO dtlDTO = dtlDTOs.get(0);
                                    BigDecimal money = dtlDTO.getMoney().subtract(dtlDTO.getSuperProcessMoney()).subtract(dtlDTO.getFreezeMoney())
                                            .subtract(dtlDTO.getReliefAndUrationMoney()).subtract(dtlDTO.getVirtualUration())
                                            .subtract(dtlDTO.getSuperReliefMoney()).add(dtlDTO.getSuperByReliefMoney());//剩余额度
                                    if (money.compareTo(vo.getMoney()) < 0) {//小于占用额度，则不能占用
                                        errorMsgList.add("授信占用品种的剩余可用额度不足，不能占用");
                                    }
                                }
                            } else {
                                List<RatBkrationCpAddDtlDTO> dtlDTOs = disService.getRationDisDtlByItemCode(disCode, vo.getItemFlag(), null);
                                Map<String, RatBkrationCpAddDtlDTO> dtlMap = new HashMap<>();
                                if (!EmptyUtils.isEmptyList(dtlDTOs)) {
                                    dtlMap = Maps.uniqueIndex(dtlDTOs, RatBkrationCpAddDtlDTO::getItemCode);
                                }
                                RatBkrationCpAddDtlDTO dtlDTO = dtlMap.get(vo.getItemCode());
                                if (!ObjectUtils.isEmpty(dtlDTO)) {
                                    if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getCompSign())) {//综合使用标识
                                        BigDecimal money = dtlDTO.getMoney().subtract(dtlDTO.getSuperProcessMoney()).subtract(dtlDTO.getFreezeMoney())
                                                .subtract(dtlDTO.getReliefAndUrationMoney()).subtract(dtlDTO.getVirtualUration())
                                                .subtract(dtlDTO.getSuperReliefMoney()).add(dtlDTO.getSuperByReliefMoney());//剩余额度
                                        if (money.compareTo(vo.getMoney()) < 0) {//小于占用额度，则不能占用
                                            errorMsgList.add("授信占用品种的剩余可用额度不足，不能占用");
                                        }
                                    }
                                    if (dtlDTO.getMaxMoney().compareTo(vo.getMoney()) < 0) {//最高可用额度小于占用额度，则不能占用
                                        errorMsgList.add("占用授信品种的最高剩余额度不足，不能占用");
                                    }
                                }
                            }
                        }
                        // 保证金比例（非必填）
                        BigDecimal assurepercent = vo.getAssurepercent();
                        if (!EmptyUtils.isEmpty(assurepercent) && !EmptyUtils.isEmpty(vo.getBillMoney()) && vo.getBillMoney().compareTo(BigDecimal.ZERO) > 0) {
                            // 保证金比例格式
                            if (assurepercent.compareTo(BigDecimal.ZERO) <= 0) {
                                // 保证金小于0则设置为0
                                assurepercent = BigDecimal.ZERO;
                            } else if (assurepercent.compareTo(new BigDecimal(100)) > 0) {
                                assurepercent = new BigDecimal(100);
                            }
                            vo.setAssurepercent(assurepercent.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));
                            // 保证金币种
                            String bondCurId = vo.getBondCurId();
                            if (!EmptyUtils.isEmpty(bondCurId) && !EmptyUtils.isEmpty(initCurId)) {
                                if (!bondCurId.equals(initCurId)) {
                                    // 业务币别与保证金币别不一致需要查询汇率
                                    BigDecimal exchangeRate = excrateAdjustService.getExchangeRateBy(initCurId, bondCurId, new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                                    if (EmptyUtils.isEmpty(exchangeRate)) {
                                        exchangeRate = BigDecimal.ONE;
                                    }
                                    // 保证金金额 = 保证金比例 * 业务金额 * 汇率
                                    vo.setBondMoney(assurepercent.multiply(vo.getBillMoney()).multiply(exchangeRate).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                                } else {
                                    // 保证金金额 = 保证金比例 * 业务金额
                                    vo.setBondMoney(assurepercent.multiply(vo.getBillMoney()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                                }
                            } else {
                                // 保证金金额 = 保证金比例 * 业务金额
                                vo.setBondMoney(assurepercent.multiply(vo.getBillMoney()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                            }
                        } else {
                            vo.setAssurepercent(BigDecimal.ZERO);
                            vo.setBondMoney(BigDecimal.ZERO);
                        }
                        // 担保方式（非必填）：1、判断系统参数是否挂接担保，是则必填，否则非必填。
                        ConfigDTO guaConfig = rationService.getMountGuarantee();//系统参数设置是否挂接担保

                        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
                            // 2、判断授信录入时是否挂接担保，是则默认为授信录入时挂接的担保，否则为导入的担保
                            String isHangGuarantee = ratBkration.getIsHangGuarantee();
                            if (!EmptyUtils.isEmpty(isHangGuarantee) && isHangGuarantee.equals(RationConstant.RAT_COMMON_Y)) {
                                GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
                                guaContractOccupDTO.setBusinessId(ratCode);
                                GuaContractOccupDTO guaContractDTO = guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
                                vo.setGuaWay(guaContractDTO.getGuaWay());
                                vo.setGuaWayName(guaContractDTO.getGuaWayName());
                            } else {
                                List<String> listZ = Lists.newArrayList();
                                // 填入的担保方式
                                String guaWayName = vo.getGuaWayName();
                                if (EmptyUtils.isEmpty(guaWayName)) {
                                    errorMsgList.add("系统参数挂接担保，担保方式不能为空");
                                } else {
                                    //设置担保方式
                                    List<GuaWayDTO> listD = guaWayServie.getAllGuaWay();
                                    // 担保方式名称集合
                                    List<String> listE = listD.stream().map(GuaWayDTO::getName).collect(Collectors.toList());
                                    boolean flag = false;
                                    String[] guaWayNames = guaWayName.split(CommonConstant.COMMA_SPLIT);
                                    for (String wayName: guaWayNames) {
                                        if (!listE.contains(wayName)) {
                                            // 其中担保方式未维护或不存在
                                            flag = true;
                                            errorMsgList.add("【" + wayName + "】担保方式未维护或不存在");
                                        } else {
                                            GuaWayDTO guaWayDTO = listD.stream().filter(item -> wayName.equals(item.getName())).collect(Collectors.toList()).get(0);
                                            if (!ObjectUtils.isEmpty(guaWayDTO)) {
                                                listZ.add(guaWayDTO.getCode());
                                            }
                                        }
                                    }
                                    if (flag) {
                                        vo.setGuaWay("");
                                        vo.setGuaWayName(guaWayName);
                                    } else {
                                        // 校验通过
                                        // 担保方式code集合
                                        StringBuilder guaWays = new StringBuilder();
                                        listZ.forEach(guaWay -> guaWays.append(guaWay).append(CommonConstant.COMMA_SPLIT));
                                        vo.setGuaWay(guaWays.toString());
                                        vo.setGuaWayName(guaWayName);
                                    }
                                }
                            }
                        } else {
                            // 系统参数不挂接担保，则清空担保方式字段
                            vo.setGuaWay("");
                            vo.setGuaWayName("");
                        }
                        // 开始日期（必填）
                        if (EmptyUtils.isEmpty(vo.getBeginDate())) {
                            errorMsgList.add("开始日期不能为空");
                        } else {
                            // 开始日期必须在授信开始时间到授信结束时间之间
                            Date beginDate = ratBkration.getBegindate();
                            Date endDate = ratBkration.getEnddate();
                            if (vo.getBeginDate().before(beginDate) || vo.getBeginDate().after(endDate)) {
                                errorMsgList.add("开始日期必须在授信开始时间到授信结束时间之间");
                            }
                        }
                        // 预计结束日期（必填）
                        if (EmptyUtils.isEmpty(vo.getEndDate())) {
                            errorMsgList.add("预计结束日期不能为空");
                        }
                        if (!EmptyUtils.isEmpty(vo.getBeginDate()) && !EmptyUtils.isEmpty(vo.getEndDate())) {
                            if (vo.getBeginDate().after(vo.getEndDate())) {
                                errorMsgList.add("开始日期必须小于结束日期");
                            } else {
                                // 计算占用期限
                                int days = DateUtils.getOffsetDays(vo.getBeginDate(), vo.getEndDate());
                                vo.setDays(days + 1);
                            }
                        }
                        // 合同号（必填）
                        if (EmptyUtils.isEmpty(vo.getContactNo())) {
                            errorMsgList.add("合同号不能为空");
                        } else {
                            // 合同号长度小于等于50
                            if (vo.getContactNo().length() > 50) {
                                errorMsgList.add("合同号字符长度不能大于50");
                            }
                        }
                        // 信用证号（非必填）
                        if (!EmptyUtils.isEmpty(vo.getLcNo()) && vo.getLcNo().length() > 32) {
                            errorMsgList.add("信用证号字符长度不能大于32");
                        }
                        // 票据（包）号码（非必填）
                        if (!EmptyUtils.isEmpty(vo.getBillNo()) && vo.getBillNo().length() > 32) {
                            errorMsgList.add("票据（包）号码字符长度不能大于32");
                        }
                        // 备注（非必填）
                        if (!EmptyUtils.isEmpty(vo.getRemark()) && vo.getRemark().length() > 100) {
                            errorMsgList.add("备注字符长度不能大于100");
                        }
                        vo.setLcNo(EmptyUtils.isEmpty(vo.getLcNo()) ? "" : vo.getLcNo());
                        vo.setBillNo(EmptyUtils.isEmpty(vo.getBillNo()) ? "" : vo.getBillNo());
                        vo.setRemark(EmptyUtils.isEmpty(vo.getRemark()) ? "" : vo.getRemark());

                        // 根据是否敞口重设占用金额
                        if (CommonConstant.YES.equals(vo.getIsOpenRat())){
                            BigDecimal money = vo.getMoney();
                            vo.setMoney(money.subtract(vo.getBondMoney()));
                        }
                    }

                    vo.addErrorMsg(errorMsgList);
                }
            }
        }
    }
}
