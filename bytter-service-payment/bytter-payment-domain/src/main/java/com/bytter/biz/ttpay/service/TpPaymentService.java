package com.bytter.biz.ttpay.service;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.fundwarn.spi.service.IFundWarnService;
import com.bytter.biz.netbank.service.NisCorpBusService;
import com.bytter.biz.netbank.vo.NisCorpBusVO;
import com.bytter.biz.payment.api.constant.*;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.biz.ttpay.dao.TpPaymentDao;
import com.bytter.biz.ttpay.entity.TpMerchant;
import com.bytter.biz.ttpay.entity.TpPayment;
import com.bytter.biz.ttpay.vo.TpPayBillBalVO;
import com.bytter.biz.ttpay.vo.TpPaymentVO;
import com.bytter.biz.ttpay.vo.TpQuotaVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.bpm.IWorkFlowSubmitVerifyService;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author chenf
 * @version 1.0
 * @Title: Service
 * @Description: 第三方支付service
 * @date 2022-5-9
 */
@WorkFlow(TtPayWorkFlowConstant.TP_PAYMENT)
@Service("tpPaymentService")
@Transactional
@DubboService(interfaceClass = IWorkFlowListener.class,group = TtPayWorkFlowConstant.TP_PAYMENT)
public class TpPaymentService extends BaseService<TpPayment, String> implements IWorkFlowListener, IExcelVerifyHandler<TpPaymentVO>, IWorkFlowRevokeListener, IWorkFlowSubmitVerifyService, ICorpDisableVerifyService {
    @Autowired
    private TpPaymentDao tpPaymentDao;
    @Autowired(required = false)
    private TpQuotaService tpQuotaService;
    @Autowired(required = false)
    private TpLogService tpLogService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
	private IAttachmentService attachmentService;
    @DubboReference(check = false)
	private IConfigService configService;
    @Autowired(required = false)
    private TpPayBillBalService tpPayBillBalService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private IFbsItemService fbsItemService;
    @DubboReference(check = false)
	private IFbsBudgetService fbsBudgetService;
    @Autowired(required = false)
    private TpShopTypeService tpShopTypeService;
    @Autowired(required = false)
    private TpMerchantService tpMerchantService;
    @DubboReference(check = false)
	private IFundWarnService fundWarnService;
    @DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
    @DubboReference(check = false)
	private IDictService dictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
	private IUserService userService;
    @Autowired(required = false)
	private NisCorpBusService nisCorpBusService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(TpPayment entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(TpPayment entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(TpPayment entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(TpPayment entity) {
        super.saveOrUpdate(entity);
        getSession().flush();
    }
    @Override
    public BaseDao<TpPayment, String> getEntityDao() {
        return tpPaymentDao;
    }
    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        sql.append(" and (n.create_by ='").append(user.getLoginName()).append("' or n.create_by is null or n.create_by='')");
        sql.append(" and n.status in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK + ") ");
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_PAYMENT_QUERY, user.getId()), "n.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput data = tpPaymentDao.getDataTablesQuery(dataTablesInput, sql.toString(), null);
        return this.setJoinField(data);
    }
    /**
     * 设置关联字段
     * @param output
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output) {
        if(!EmptyUtils.isEmpty(output.getData())) {
            List<TpPaymentVO> list = output.getData();
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(TtPayConstant.TTPAY_PAYTYPE_DICT,
                    CommonConstant.COMMON_SF_YN_DICT,NetbankConstant.NETBANK_STATUS_DICT,BdlinkConstant.EXC_VOUCHERSTAT_DICT);
            for (TpPaymentVO vo:list) {
                if (corpMap.containsKey(vo.getCorpId())) {
                    vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
                }
                //字典注入
                this.injectValues(vo, listDictMap);
            }
        }
        return output;
    }
    /**
     * 字典注入
     *
     * @param vo
     * @param listDictMap
     */
    private void injectValues(TpPaymentVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
        // 支付方式
        Optional.ofNullable(vo.getPayType()).ifPresent(payType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTPAY_PAYTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(payType)))
                    .ifPresent(dict -> vo.setPayTypeName(dict.getName()));
        });
        // 状态
        Optional.ofNullable(vo.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(NetbankConstant.NETBANK_STATUS_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status)))
                    .ifPresent(dict -> vo.setStatusName(dict.getName()));
        });
        // 是否对私付款
        Optional.ofNullable(vo.getIsForindividual()).ifPresent(isForindividual -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(CommonConstant.COMMON_SF_YN_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isForindividual)))
                    .ifPresent(dict -> vo.setIsForindividualName(dict.getName()));
        });
        // 交易状态
        Optional.ofNullable(vo.getVoucherStat()).ifPresent(voucherStat -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_VOUCHERSTAT_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(voucherStat)))
                    .ifPresent(dict -> vo.setVoucherStatName(dict.getName()));
        });
    }
    /**
     * 根据单位、商户获取限额信息
     *
     * @param corpId
     * @param merchantNo
     * @param payType    待定
     * @return
     */
    public TpQuotaVO getQuotaInfo(String corpId, String merchantNo, String payType,Date wishPayday) {
        if(StringUtils.isEmpty(corpId) || StringUtils.isEmpty(merchantNo) ){
            return null;
        }
        return tpQuotaService.getTpQuotaInfo(corpId, merchantNo, payType,wishPayday);
    }
    /**
     * 通过商户进行限额校验
     *
     * @param id          编辑时，id
     * @param corpId      单位id
     * @param merchantNo  商户号
     * @param payMoney    支付金额
     * @param payType     支付方式
     * @param wishPayDate 期望支付日期
     * @param temp        0新增 1编辑
     * @return
     */
    public int limitForMerchant(String id, String corpId, String merchantNo, BigDecimal payMoney, String payType, String wishPayDate, String temp) {
        Date wishPaydayDate =null;
        if(StringUtils.isNotEmpty(wishPayDate)){
            wishPaydayDate = DateUtils.parse(wishPayDate,"yyyy-MM-dd");
        }
        TpQuotaVO tpQuotaVO = this.getQuotaInfo(corpId, merchantNo, payType,wishPaydayDate);
        if(tpQuotaVO!=null){
            if (tpQuotaVO.getDaySingle() != null && tpQuotaVO.getDaySingle().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                if (tpQuotaVO.getDaySingle().compareTo(payMoney) < 0) {
                    return 1;
                }
            }
            if (tpQuotaVO.getDayTotalAvail() != null) {//限额不为零才判断
                if (tpQuotaVO.getDayTotalAvail().compareTo(payMoney) < 0) {
                    return 2;
                }
            }
            if (tpQuotaVO.getMonthTotalAvail() != null) {//限额不为零才判断
                if (tpQuotaVO.getMonthTotalAvail().compareTo(payMoney) < 0) {
                    return 3;
                }
            }
        }
        return 0;
    }
    /**
     * 更新打印次数
     *
     * @param id
     *//*
    public void updatePrint(String id) {
        tpPaymentDao.updatePrint(id);
    }
    */
    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public TpPaymentVO getByIdUnion(String id) {
        TpPaymentVO tpPayment = tpPaymentDao.getByIdUnion(id);
        //获取余额
        if (tpPayment != null) {
            TpPayBillBalVO balVo = this.getPayBillBal(tpPayment.getCorpId(), tpPayment.getPayerAcc(), tpPayment.getPayType());
            if (balVo != null && balVo.getTotalAmount() != null) {
                tpPayment.setBalance(balVo.getTotalAmount());
            } else {
                tpPayment.setBalance(BigDecimal.ZERO);
            }
            if (balVo != null && balVo.getAvailableAmount() != null) {
                //可用余额 = 可用余额（可用-所有在途）+本笔在途金额
                tpPayment.setAvailBal(balVo.getAvailableAmount().add(tpPayment.getPayMoney()));
            } else {
                tpPayment.setAvailBal(BigDecimal.ZERO);
            }
        }
        return tpPayment;
    }
    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public TpPaymentVO getVoById(String id) {
        return tpPaymentDao.getVoById(id);
    }
    /**
     * 保存
     *
     * @param tpPayment
     */
    public void saveTpPayment(TpPaymentVO tpPayment, ICurrentUser user) {
        //校验时间戳，是否存在重复提交
        if (this.isExistTimeStamp(tpPayment.getTimeStamp())) {
            throw new BusinessException(NetbankMessageEnum.NIS_MESSAGE_REPEAT_SUBMIT);
        }
        TpPayment entity = new TpPayment();
        BeanUtils.copyNotNullProperties(tpPayment, entity);
        if (StringUtils.isBlank(entity.getBillNo())) {
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.TP_PAYMENT));
        }
        entity.setCreateUserName(user.getUsername());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        super.saveEntity(entity);
        //写入计划执行
        tpPayment.setId(entity.getId());
        tpPayment.setBillNo(entity.getBillNo());
        this.writeBudgetProcess(tpPayment, user);
        //启动审批
        if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            //传入付款方式审批条件
            TpPaymentVO tpPaymentVO = new TpPaymentVO();
            BeanUtils.copyNotNullProperties(entity, tpPaymentVO);
            //调用审批后方法
             getStartHandler().handler(TtPayWorkFlowConstant.TP_PAYMENT, entity.getId(), tpPaymentVO, entity.getCorpId(), user.getId());
        }
        if (user != null) {
            //生成日志
            tpLogService.saveTpLog(entity.getId(), TtPayConstant.TTPAY_LOGTYPE_ADD, TtPayConstant.COMMON_MESSAGE_ADDSUCCESS, user);
        }
    }
    /**
     * 写入计划执行
     *
     * @param tpPayment
     */
    private void writeBudgetProcess(TpPaymentVO tpPayment, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(tpPayment.getItemCode())) {
            //写入之前先删除
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP, tpPayment.getBillNo());
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < tpPayment.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(tpPayment, user);
                fbsBudgetProcessDTO.setItemCode(tpPayment.getItemCode()[i]);
                fbsBudgetProcessDTO.setItemName(tpPayment.getItemName()[i]);
                fbsBudgetProcessDTO.setProcessMoney(tpPayment.getProcessMoney()[i]);
                list.add(fbsBudgetProcessDTO);
            }
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        } else {
            //计划执行状态同步更新（批量提交）
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP,
                    tpPayment.getBillNo(), tpPayment.getStatus(), user);
        }
    }
    /**
     * 构建计划执行
     *
     * @param tpPayment
     * @return
     */
    private FbsBudgetProcessDTO bulidFbsBudgetProcessDTO(TpPaymentVO tpPayment, ICurrentUser user) {
        FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
        fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
        fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
        fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setStatus(tpPayment.getStatus());
        fbsBudgetProcessDTO.setBillNo(tpPayment.getBillNo());
        fbsBudgetProcessDTO.setBillId(tpPayment.getId());
        fbsBudgetProcessDTO.setProcessDate(tpPayment.getWishPayday());
        fbsBudgetProcessDTO.setCorpId(tpPayment.getCorpId());
        fbsBudgetProcessDTO.setCurId(tpPayment.getCurId());
        fbsBudgetProcessDTO.setPayerAcc(tpPayment.getPayerAcc());
        fbsBudgetProcessDTO.setPayeeAcc(tpPayment.getPayeeAcc());
        fbsBudgetProcessDTO.setPayeeAccName(tpPayment.getPayeeAccName());
        fbsBudgetProcessDTO.setWishPayday(tpPayment.getWishPayday());
        fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_TTPAYMENT);
        fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_TP);
        return fbsBudgetProcessDTO;
    }
    /**
     * 编辑付款单
     *
     * @param tpPayment
     */
    public void updateTpPayment(TpPaymentVO tpPayment, ICurrentUser user, Boolean isBatch) {
        TpPayment entity = null;
        String updateMsg = "";
        if (isBatch) {
            entity = new TpPayment();
        } else {
            entity = tpPaymentDao.get(tpPayment.getId(), true);
        }
        if (EmptyUtils.isBlank(tpPayment.getCreateBy())) {
            tpPayment.setCreateBy(user.getLoginName());
        }
        if (tpPayment.getCreateDate() == null) {
            tpPayment.setCreateDate(DateUtils.getDate());
        }
        tpPayment.setUpdateBy(user.getLoginName());
        tpPayment.setUpdateDate(DateUtils.getDate());
        //编辑付款单，控制原状态为0暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        this.updateTpPaymentStatus(tpPayment, oldStatus);
        BeanUtils.copyNotNullProperties(tpPayment, entity);
        //写入计划执行
        this.writeBudgetProcess(tpPayment, user);
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_UPDATE_TEMPSAVE_SUCC;
        //启动审批
        if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = tpPayment.getBatch() ? NisMessageConstant.NIS_LOG_BATCH_SUBMIT_SUCC : NisMessageConstant.NIS_LOG_UPDATE_SUBMIT_SUCC;
            //传入付款方式审批条件
            TpPaymentVO tpPaymentVO = new TpPaymentVO();
            BeanUtils.copyNotNullProperties(entity, tpPaymentVO);
            //调用审批后方法
             getStartHandler().handler(TtPayWorkFlowConstant.TP_PAYMENT, entity.getId(), tpPaymentVO, entity.getCorpId(), user.getId());
        }
        if (StringUtils.isNotBlank(updateMsg)) {
            message = updateMsg;
        }
        //生成日志
        tpLogService.saveTpLog(entity.getId(), TtPayConstant.TTPAY_LOGTYPE_SUBMIT, TtPayConstant.COMMON_MESSAGE_SUBMITSUCCESS, user);
        //完成通知提醒
        /*notificationService.complete(NotificationConstant.NETBANK.PAY_APPLY, entity.getId(), user.getId());
        String tag = NotificationConstant.NETBANK.PAY_APPLY;
        notificationService.complete(tag, entity.getId(), user.getId());*/
    }
    /**
     * 逻辑删除付款单
     *
     * @param id
     */
    public void delTpPayment(String id, ICurrentUser user, boolean isBatch, String chargeBackType) {
        TpPayment tpPayment = this.get(id, true);
        this.deleteTpPayment(tpPayment, user, isBatch, false);
    }
    /**
     * 删除第三方支付单据
     * @param entity 被删除对象
     * @param user 当前用户
     * @param isBatch 是否批量
     * @param backDetail
     */
    public void deleteTpPayment(TpPayment entity, ICurrentUser user, boolean isBatch, boolean backDetail) {
        //删除付款单，控制原状态为0-暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        TpPaymentVO vo = new TpPaymentVO();
        BeanUtils.copyNotNullProperties(entity, vo);
        vo.setStatus(NetbankConstant.STATUE_DEL);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        this.updateTpPaymentStatus(vo, oldStatus);
        //删除资金计划
        fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP, entity.getBillNo());
        if (backDetail) {
            //生成日志
            tpLogService.saveTpLog(entity.getId(), TtPayConstant.TTPAY_LOGTYPE_DELETE, TtPayConstant.COMMON_MESSAGE_DELETESUCCESS, user);
        } else {
            String message = isBatch ? NisMessageConstant.NIS_LOG_BATCH_DELETE_SUCC : NisMessageConstant.NIS_LOG_DELETE_SUCC;
            //生成日志
            tpLogService.saveTpLog(entity.getId(), TtPayConstant.TTPAY_LOGTYPE_DELETE, TtPayConstant.COMMON_MESSAGE_DELETESUCCESS, user);
        }
        //完成通知提醒
        /*notificationService.complete(NotificationConstant.NETBANK.PAY_APPLY, entity.getId(), user.getId());
        String tag = NotificationConstant.NETBANK.PAY_APPLY;
        notificationService.complete(tag, entity.getId(), user.getId());*/
    }
    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(TtPayWorkFlowConstant.TP_PAYMENT, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "n.id", true);
        DataTablesOutput dp = this.tpPaymentDao.dataTableApprove(dataTablesInput, businessSql);
        if (dp != null && dp.getData() != null) {
            List<TpPaymentVO> list = dp.getData();
            for (TpPaymentVO tpPayment : list) {
                BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(tpPayment.getPayerAcc(), tpPayment.getCurId());
                if (bal != null) {
                    tpPayment.setBalance(bal.getBal());
                }
            }
        }
        return workFlowBusiness.merge(dp);
    }
    /**
     * 批量提交付款单
     *
     * @param tpPayment
     * @param random
     * @param sourceData
     * @param caCoding
     * @param user
     */
    public void batchSubmitTpPayment(TpPaymentVO tpPayment, String random, String sourceData, String caCoding, ICurrentUser user) {
        tpPayment.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
        tpPayment.setBatch(true);
        //期望支付日期小于系统当前日期，则默认更新为系统当前日期
        if (tpPayment.getWishPayday().before(DateUtils.getDateYMD())) {
            tpPayment.setWishPayday(DateUtils.getDateYMD());
            //计划执行日期同步调整
            this.fbsBudgetProcessService.updateBudgetProcessDate(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP, tpPayment.getBillNo(), DateUtils.getDateYMD(), user);
        }
        this.updateTpPayment(tpPayment, user, true);
        //付款预警增加
        this.warnExecuteNis(tpPayment, user);
    }
    /**
     * 批量删除付款单
     *
     * @param ids
     */
    public void batchDelTpPayment(String ids, ICurrentUser user, String chargeBackType) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.delTpPayment(id, user, true, chargeBackType);
        }
    }
    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<TpPaymentVO> list, ICurrentUser user) {
        //判断是否挂计划,计划科目校验
        if (this.isBudgetSel()) {
            HashMap<String, FbsBudgetProcessDTO> map = new HashMap<>();
            for (TpPaymentVO tpPayment : list) {
                //判断单位是否上报计划
                if (CommonConstant.YES.equals(tpPayment.getIsBudget()) && !EmptyUtils.isEmpty(tpPayment.getItemCode()) && StringUtils.isNotBlank(tpPayment.getItemCode()[0])) {
                    String key = tpPayment.getCorpId() + tpPayment.getItemCode()[0] + tpPayment.getWishPayday();
                    FbsBudgetProcessDTO dto = null;
                    if (map.containsKey(key)) {
                        dto = map.get(key);
                        dto.setProcessMoney(dto.getProcessMoney().add(tpPayment.getPayMoney()));
                        map.put(key, dto);
                    } else {
                        dto = new FbsBudgetProcessDTO();
                        dto.setCorpId(tpPayment.getCorpId());
                        dto.setItemCode(tpPayment.getItemCode()[0]);
                        dto.setProcessMoney(tpPayment.getPayMoney());
                        dto.setProcessDate(tpPayment.getWishPayday());
                        map.put(key, dto);
                    }
                }
            }
            //资金计划余额校验
            List<FbsBudgetProcessDTO> listBudget = new ArrayList<>();
            for (String key : map.keySet()) {
                listBudget.add(map.get(key));
            }
            for (FbsBudgetProcessDTO processDTO : listBudget) {
                //判断科目是否受控
                if (CommonConstant.YES.equals(processDTO.getIsControl())) {
                    FbsBudgetProcessCommonDTO dto = fbsBudgetService.getFbsBudgetProcessByItemCode(processDTO.getCorpId(), processDTO.getItemCode(), processDTO.getProcessDate(), null, null);
                    //计划执行规则为5-先执行后控制时无需校验
                    if (!BudgetConstant.PROCESS_RULE_5.equals(dto.getProcessRule())) {
                        //计划执行科目余额控制方式
                        String itemBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ITEMBALANCE).getValue();
                        if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_ONESELF.equals(itemBalance)) {
                            //受本科目控制
                            if (processDTO.getProcessMoney().doubleValue() > dto.getItemMoney().doubleValue()) {
                                throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_ITEMBALANCE);
                            }
                        }
                        if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_SUPER.equals(itemBalance)) {
                            //受上级科目控制
                            if (processDTO.getProcessMoney().doubleValue() > dto.getSuperItemMoney().doubleValue()) {
                                throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_SUPERITEMBALANCE);
                            }
                        }
                        if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_TOP.equals(itemBalance)) {
                            //受大科目控制
                            if (processDTO.getProcessMoney().doubleValue() > dto.getTopItemMoney().doubleValue()) {
                                throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_TOPITEMBALANCE);
                            }
                        }
                    }
                }
            }
        }
        HashMap<String, TpPaymentVO> bankMap = new HashMap<>();
        for (TpPaymentVO tpPayment : list) {
            tpPayment.setStatus(NetbankConstant.STATUE_TEMP);
            this.saveTpPaymentForImport(tpPayment, user);
        }
    }
    /**
     * 导入保存
     *
     * @param tpPayment
     */
    public void saveTpPaymentForImport(TpPaymentVO tpPayment, ICurrentUser user) {
        TpPayment entity = new TpPayment();
        BeanUtils.copyNotNullProperties(tpPayment, entity);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.TP_PAYMENT));
        entity.setCreateUserName(user.getUsername());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        super.saveEntity(entity);
        //写入计划执行
        tpPayment.setId(entity.getId());
        tpPayment.setBillNo(entity.getBillNo());
        this.writeBudgetProcess(tpPayment, user);
        //生成日志
        tpLogService.saveTpLog(entity.getId(), TtPayConstant.TTPAY_LOGTYPE_ADD, TtPayConstant.COMMON_MESSAGE_IMPSUCCESS, user);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(TpPaymentVO vo) {
        return null;
    }
    @Override
    public void verifyHandler(List<TpPaymentVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //显示已付款是标识
            boolean isBudgetSel = this.isBudgetSel();
            //单位集合
            HashMap<String, CorpDTO> corpMap = new HashMap<>();
            //付款方式集合
            HashMap<String, String> voucherTypeMap = new HashMap<>();
            //银行账号集合
            HashMap<String, TpMerchant> bankAccMap = new HashMap<>();
            //单位计划上报集合
            HashMap<String, Boolean> reportBudgetMap = new HashMap<>();
            //获取人民币
            BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
            for (TpPaymentVO vo : list) {
                List<String> msgList = new ArrayList<>();
                // 判断单位在系统中是否存在
                CorpDTO corp = corpMap.get(vo.getCorpName());
                if (corp == null) {
                    corp = this.corpService.getCorpIdByName(vo.getCorpName());
                    corpMap.put(vo.getCorpName(), corp);
                }
                if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                    corp = new CorpDTO();
                    msgList.add("nis.netbank.message.corp.null");
                } else {
                    vo.setCorpId(corp.getId());
                }
                //判断付款方式是否存在
                String payType = voucherTypeMap.get(vo.getPayTypeName());
                if (payType == null) {
                    payType = dictService.getCodeByName("TTPAY_PAY_TYPE", vo.getPayTypeName());
                    if (StringUtils.isEmpty(payType)) {
                        msgList.add("ttpay.message.paytype.isnotexist");
                    } else {
                        voucherTypeMap.put(vo.getPayTypeName(), payType);
                    }
                }
                if (!EmptyUtils.isEmpty(payType)) {
                    if (!EmptyUtils.isEmpty(corp) && !EmptyUtils.isEmpty(payType)) {
                        vo.setPayType(payType);
                        vo.setCurId(rmbCurrency.getId());
                        //付款账号校验
                        if (StringUtils.isEmpty(vo.getPayerAcc())) {
                            msgList.add("ttpay.message.payeracc.null");
                        } else {
                            //获取付款账号
                            TpMerchant merchant = bankAccMap.get(vo.getCorpId() + vo.getPayerAcc());
                            if (merchant == null) {
                                merchant = tpMerchantService.getTpMerchant(vo.getPayerAcc());
                                bankAccMap.put(vo.getCorpId() + vo.getPayerAcc(), merchant);
                            }
                            if (EmptyUtils.isEmpty(merchant)) {
                                msgList.add("ttpay.message.payeracc.null");
                            }
                            if (!StringUtils.isEmpty(vo.getPayerAcc())) {
                                //收付款账号不能相同
                                if (vo.getPayerAcc().equals(vo.getPayeeAcc())) {
                                    msgList.add("ttpay.message.payeeacc.noteq.payeracc");
                                }
                            }
                        }
                    }
                    //收款人类别校验
                    if (EmptyUtils.isEmpty(vo.getPayeeModeName())) {
                        msgList.add("ttpay.message.payeeMode.null");
                    } else {
                        String payeeMode = dictService.getCodeByName("ttpay.payeeMode", vo.getPayeeModeName());
                        if (StringUtils.isEmpty(payeeMode)) {
                            msgList.add("ttpay.message.payeeMode.null");
                        } else {
                            vo.setPayeeMode(payeeMode);
                        }
                    }
                    //金额不能为空且必须大于0
                    if (EmptyUtils.isEmpty(vo.getPayMoney())) {
                        msgList.add("nis.netbank.message.money.null");
                    } else {
                        if (vo.getPayMoney().compareTo(BigDecimal.ZERO) <= 0) {
                            msgList.add("nis.netbank.message.money.mismatch");
                        }
                    }
                    //是否对私付款
                    if (!NetbankConstant.FORINDIVIDUAL_0.equals(vo.getIsForindividual())
                            && !NetbankConstant.FORINDIVIDUAL_1.equals(vo.getIsForindividual())) {
                        msgList.add("nis.netbank.message.forindividual.format");
                    }
                    //期望支付日期
                    if (vo.getWishPayday() != null) {
                        if (vo.getWishPayday().compareTo(DateUtils.getDateYMD()) == -1) {
                            msgList.add("nis.netbank.message.wishpayday.mismatch");
                        }
                    }
                    //业务类别校验
                    if (!EmptyUtils.isEmpty(vo.getBusTypeName())) {
                        NisCorpBusVO busType = nisCorpBusService.getBusTypeByName(vo.getCorpId(), "1", vo.getBusTypeName());
                        if (busType == null) {
                            msgList.add("nis.netbank.message.bustype.mismatch");
                        } else {
                            vo.setBusType(busType.getBusId());
                        }
                    }
                    //判断是否挂计划,计划科目校验
                    if (isBudgetSel && StringUtils.isNotEmpty(vo.getCorpId())) {
                        Boolean reportBudget = reportBudgetMap.get(vo.getCorpId());
                        if (reportBudget == null) {
                            reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(vo.getCorpId());
                            reportBudgetMap.put(vo.getCorpId(), reportBudget);
                        }
                        //判断单位是否上报计划
                        if (reportBudget) {
                            if (EmptyUtils.isEmpty(vo.getItemCodeImp())) {
                                msgList.add("nis.assign.message.itemcode.null");
                            } else {
                                FbsItemDTO fbsItemDTO = this.fbsItemService.getFbsItemByCode(vo.getItemCodeImp());
                                if (EmptyUtils.isEmpty(fbsItemDTO)) {
                                    //判断计划科目是否存在
                                    msgList.add("nis.assign.message.bustype.itemcode.null");
                                } else {
                                    vo.setItemCode(new String[]{fbsItemDTO.getItemCode()});
                                    vo.setItemName(new String[]{fbsItemDTO.getItemName()});
                                    vo.setProcessMoney(new BigDecimal[]{vo.getPayMoney()});
                                }
                            }
                        }
                    }
                }
                vo.addErrorMsg(msgList);
            }
        }
    }
    /**
     * 重复付款校验
     *
     * @param corpId   单位
     * @param payMoney 金额
     * @param payeeAcc 收款账号
     * @param id       单据号（编辑时校验需要）
     * @param tempFlag true - 查询暂存打回状态， false-查询已提交之后的状态数据
     * @return
     */
    public boolean isExistRepay(String corpId, BigDecimal payMoney, String payeeAcc, String curId, String id, boolean tempFlag) {
        String repayAlertDaysStr = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_REPAYALERTDAYS).getValue();
        if (!StringUtils.isBlank(repayAlertDaysStr)) {
            Integer repayAlertDays = Integer.parseInt(repayAlertDaysStr);
            String endDate = DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd");
            String beginDate = endDate;
            if (repayAlertDays != 0) {
                beginDate = DateUtils.format(DateUtils.addDay(DateUtils.getDate(), -repayAlertDays), "yyyy-MM-dd");
            }
            beginDate = beginDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
            List<TpPayment> list = tpPaymentDao.listByRePay(corpId, payMoney, payeeAcc, beginDate, endDate, curId, id, tempFlag);
            if (!EmptyUtils.isEmptyList(list)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 功能介绍 : 是否挂资金计划。<br>
     * <b>日期：</b>2018年3月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean isBudgetSel() {
        //挂资金计划参数
        String budgetSel = configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BUDGETSEL).getValue();
        if (CommonConstant.VALID_SIGN_YES.equals(budgetSel)) {
            //挂资金计划
            return true;
        }
        return false;
    }
    /**
     * 获取审批列表单位树
     *
     * @param flowStatus track 流程跟踪 todo 审批中
     * @param user
     * @return
     */
    public Object treeByCorpIds(ICurrentUser user, String flowStatus) {
        List<Map<String, Object>> tempList = new ArrayList<>();
        String businessSql = getBusinessSql(TtPayWorkFlowConstant.TP_PAYMENT, user.getId(), flowStatus);
        List<String> corpIds = this.tpPaymentDao.getCorpIdsByDataTableApprove(businessSql);
        if (EmptyUtils.isEmptyList(corpIds)) {
            return tempList;
        }
        List<CorpDTO> corpList = this.corpService.listCorpDTOByCorpIds2(corpIds);
        if (EmptyUtils.isEmptyList(corpList)) {
            return tempList;
        }
        for (CorpDTO corp : corpList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", corp.getId());
            map.put("pId", corp.getParentId());
            map.put("name", corp.getName());
            map.put("type", corp.getType());
            map.put("code", corp.getCode());
            map.put("netId", corp.getNetId());
            tempList.add(map);
        }
        return tempList;
    }
    /**
     * 限额预警
     *
     * @param entity
     * @param user
     */
    public void warnExecuteNis(TpPaymentVO entity, ICurrentUser user) {
        //限额预警
        /*if (NetbankConstant.ISACCOUNT_0.equals(entity.getIsAccount())) {
            fundWarnService.warnExecuteByEqCorpPay(entity, user);
            fundWarnService.warnExecuteBankAccPay(entity, user);
            //是否启动审批，如果审批调用单日累计支付预警
            if (NetbankConstant.STATUE_WAIT_CHECK.eqisExistRepayuals(entity.getStatus())) {
                fundWarnService.warnExecuteDaySumPay(entity, user);
            }
        } else {
            fundWarnService.warnExecuteCashPay(entity, user);
        }*/
    }
    private String getTaskWorkSql(String processDefinitionKey, String userId) {
        StringBuffer businessSql = new StringBuffer();
        businessSql.append("select exe.BUSINESS_KEY_ from ACT_RU_TASK task ");
        businessSql.append(" INNER JOIN ACT_RE_PROCDEF pd ON task.PROC_DEF_ID_ = pd.ID_ ");
        businessSql.append(" INNER JOIN ACT_RU_EXECUTION exe ON exe.PROC_DEF_ID_ = pd.ID_ AND exe.PARENT_ID_ is null AND exe.ID_ = task.PROC_INST_ID_ AND exe.PROC_INST_ID_ = task.PROC_INST_ID_ ");
        businessSql.append(" WHERE task.SUSPENSION_STATE_ = 1 ");
        businessSql.append(" AND task.ASSIGNEE_ = '").append(userId).append("'");
        businessSql.append(" AND pd.KEY_ = '").append(processDefinitionKey).append("'");
        return businessSql.toString();
    }
    private String getTrackWorkSql(String processDefinitionKey, String userId) {
        StringBuffer businessSql = new StringBuffer();
        businessSql.append("select RES.BUSINESS_KEY_ from ACT_HI_PROCINST RES ");
        businessSql.append(" left outer join ACT_RE_PROCDEF DEF on RES.PROC_DEF_ID_ = DEF.ID_ ");
        businessSql.append(" WHERE RES.END_TIME_ IS NULL ");
        businessSql.append(" AND DEF.KEY_ = '").append(processDefinitionKey).append("'");
        businessSql.append(" and ((exists (select LINK.USER_ID_ from ACT_HI_IDENTITYLINK LINK ");
        businessSql.append(" where USER_ID_ =  '").append(userId).append("'");
        businessSql.append(" and LINK.PROC_INST_ID_ = RES.ID_)) or ");
        businessSql.append(" RES.START_USER_ID_ = '").append(userId).append("')");
        return businessSql.toString();
    }
    private String getPassWorkSql(String processDefinitionKey, String userId) {
        StringBuilder businessSql = new StringBuilder();
        businessSql.append(" select DISTINCT ahp.BUSINESS_KEY_ ");
        businessSql.append(" from ACT_HI_TASKINST RES ");
        businessSql.append(" LEFT JOIN ACT_HI_PROCINST ahp ON RES.PROC_INST_ID_ = ahp.PROC_INST_ID_ ");
        businessSql.append(" LEFT JOIN ACT_RE_PROCDEF pd ON RES.PROC_DEF_ID_  = pd.ID_ ");
        businessSql.append(" WHERE  RES.END_TIME_ is not NULL  AND pd.KEY_ = '").append(processDefinitionKey).append("'");
        businessSql.append(" AND ( EXISTS( ");
        businessSql.append(" SELECT LINK.USER_ID_ ");
        businessSql.append(" FROM ACT_HI_IDENTITYLINK LINK ");
        businessSql.append(" WHERE USER_ID_ = '").append(userId).append("'");
        businessSql.append(" AND LINK.TASK_ID_ = RES.ID_) ");
        businessSql.append(" OR RES.ASSIGNEE_ = '").append(userId).append("'");
        businessSql.append(" OR RES.OWNER_ = '").append(userId).append("')");
        return businessSql.toString();
    }
    private String getBusinessSql(String processDefinitionKey, String userId, String flowStatus) {
        String businessSql = "";
        switch (flowStatus) {
            case IWorkFlowService.TODO:
                businessSql = getTaskWorkSql(processDefinitionKey, userId);
                break;
            case IWorkFlowService.TRACK:
                businessSql = getTrackWorkSql(processDefinitionKey, userId);
                break;
            case IWorkFlowService.PASS:
                businessSql = getPassWorkSql(processDefinitionKey, userId);
                break;
            default:
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" n.id in (").append(businessSql).append(") ");
        return sql.toString();
    }

    /**
     * 工作流审批校验
     *
     * @param params       Map<流程分类KEY, 业务ID列表>
     * @param workflowPass 审批操作标识：1，同意；3，打回上一级；4，打回制单处。
     * @param isNeedVerify 是否要校验工作流审批：true表示要校验；false表示不用校验。用户在看到校验失败提示信息后，仍然确定提交审批，则isNeedVerify为false。
     * @throws WorkFlowVerifyException
     */
    @Override
    public void submitVerify(Map<String, List<String>> params, String workflowPass, boolean isNeedVerify) throws WorkFlowVerifyException {
        if (isNeedVerify) {            // 模拟校验审批
            if ("1".equals(workflowPass)) {
                //银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
                String bankaccOver = configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BANKACCOVER_APPROVE).getValue();
                //付款余额控制方式  0-账户余额 1-可用余额
                String balControlType = configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BALCONTROLTYPE).getValue();
                if ("0".equals(bankaccOver)) {
                    return;
                }
                List<String> ids = params.get(TtPayWorkFlowConstant.TP_PAYMENT);
                //账户余额（批量提交校验账户余额)
                HashMap<String, BigDecimal> balMap = new HashMap<>();
                for (String id : ids) {
                    TpPaymentVO tpPayment = this.getByIdUnion(id);
                    //校验账户余额
                    BigDecimal curBal = tpPayment.getBalance();
                    if (CommonConstant.YES.equals(balControlType)) {
                        curBal = tpPayment.getAvailBal();
                    } else {
                        if (balMap.containsKey(tpPayment.getPayerAcc())) {
                            curBal = balMap.get(tpPayment.getPayerAcc());
                        }
                    }
                    if (tpPayment.getPayMoney().compareTo(curBal) > 0) {
                        if ("1".equals(bankaccOver)) {
                            //1-只提醒不控制
                            throw new WorkFlowVerifyException(WorkFlowMessageEnum.NETBANK_MESSAGE_CONFIRM_BALANCE, WorkFlowConstant.VERITY_STATUS_CONFIRM);
                        } else if ("2".equals(bankaccOver)) {
                            //2-控制
                            throw new WorkFlowVerifyException(WorkFlowMessageEnum.NETBANK_MESSAGE_BALANCE, WorkFlowConstant.VERITY_STATUS_ALERT);
                        }
                    }
                    curBal = curBal.subtract(tpPayment.getPayMoney());
                    balMap.put(tpPayment.getPayerAcc(), curBal);
                }
            }
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的付款单据，不允许禁用！
            int num = this.tpPaymentDao.getBillheadCountByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_BILLHEAD,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BILLHEAD_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    /**
     * 审批完成业务处理
     *
     * @param workFlowResult
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        TpPayment tpPayment = this.get(workFlowResult.getBusinessKey(), true);
        this.approvePassEnd(tpPayment, workFlowResult.isPass(), user);
    }
    /**
     * 撤回
     *
     * @param workFlowResult
     */
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        TpPayment tpPayment = this.get(workFlowResult.getBusinessKey(), true);
        //更新审批状态
        tpPayment.setStatus(NetbankConstant.STATUE_TEMP);
        tpPayment.setUpdateBy(user.getLoginName());
        tpPayment.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(tpPayment);
        //计划执行状态同步更新
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP, tpPayment.getBillNo(), NetbankConstant.STATUE_TEMP, user);
        //生成日志
        tpLogService.saveTpLog(tpPayment.getId(), TtPayConstant.TTPAY_LOGTYPE_WITHDRAW, TtPayConstant.COMMON_MESSAGE_WITHDRAW, user);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 获取已用余额
     *
     * @param corpId     单位id
     * @param merchantNo 商户号
     * @param payType    支付方式
     * @return 限额方式，日：1，月：2
     */
    public BigDecimal getSumPayMoney(String corpId, String merchantNo, String payType, String beginDate, String endDate) {
        return tpPaymentDao.getSumPayMoney(corpId, merchantNo, payType, beginDate, endDate);
    }
    /**
     * 判断是否存在相同的时间戳
     *
     * @param timeStamp
     * @return
     */
    public boolean isExistTimeStamp(String timeStamp) {
        if (StringUtils.isNotBlank(timeStamp)) {
            List<TpPayment> list = this.tpPaymentDao.listBy("timeStamp", timeStamp);
            if (EmptyUtils.isNotEmpty(list)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 带原状态更新付款单
     *
     * @param vo        付款对象
     * @param oldStatus 原状态控制
     * @return
     */
    public void updateTpPaymentStatus(TpPaymentVO vo, Integer[] oldStatus) {
        int result = this.tpPaymentDao.updateTpPaymentStatus(vo, oldStatus);
        if (result < 1) {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getBillNo());
        }
    }
    /**
     * 获取账户在途余额
     *
     * @param corpId
     * @param merchantNo
     * @param payType
     * @return
     */
    public TpPayBillBalVO getPayBillBal(String corpId, String merchantNo, String payType) {
        BigDecimal bal = BigDecimal.ZERO;
        if (StringUtils.isBlank(merchantNo) || StringUtils.isBlank(payType)) {
            return new TpPayBillBalVO();
        }
        TpPayBillBalVO balVO = tpPayBillBalService.getTpPayBillBal(corpId, merchantNo, payType);
        if (balVO != null) {    //商户余额不为空
            BigDecimal usePayMoney = tpPaymentDao.getUseBal(corpId, merchantNo, payType);
            if (balVO.getAvailableAmount() != null) {   //可用余额不为空，则：可用余额 = 账户可用余额-在途支付金额
                balVO.setAvailableAmount(balVO.getAvailableAmount().subtract(usePayMoney));
            } else {  //可用余额为空，则可用余额 = 账户余额-在途支付金额
                balVO.setAvailableAmount(balVO.getTotalAmount().subtract(usePayMoney));
            }
        } else {
            balVO = new TpPayBillBalVO();
        }
        return balVO;
    }
    /**
     * 审批完成后处理 （审批通过或打回）
     *
     * @param tpPayment
     * @param result
     */
    public void approvePassEnd(TpPayment tpPayment, boolean result, ICurrentUser user) {
        if (EmptyUtils.isBlank(tpPayment.getCreateBy())) {
            tpPayment.setCreateBy(user.getLoginName());
        }
        if (tpPayment.getCreateDate() == null) {
            tpPayment.setCreateDate(DateUtils.getDate());
        }
        TpPaymentVO vo = new TpPaymentVO();
        BeanUtils.copyNotNullProperties(tpPayment, vo);
        if (!result) {  //审批打回
            //更新审批状态
            vo.setStatus(NetbankConstant.STATUE_BACK);
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            //审批付款单，更新原状态待审批 为审批打回
            Integer[] oldStatus = {NetbankConstant.STATUE_WAIT_CHECK};
            this.updateTpPaymentStatus(vo, oldStatus);
            //计划执行状态同步更新
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP,
                    tpPayment.getBillNo(), NetbankConstant.STATUE_BACK, user);
            //生成日志
            tpLogService.saveTpLog(tpPayment.getId(), TtPayConstant.TTPAY_LOGTYPE_APPROVE, TtPayConstant.COMMON_MESSAGE_APPROVEBACK, user);
        } else {    //审批通过
            //更新审批状态95审批通过
            vo.setStatus(NetbankConstant.STATUE_CHECK);
            TpMerchant tpMerchant = tpMerchantService.getTpMerchant(tpPayment.getPayerAcc());
            //业务单据银行接口指令待发送状态（银行接口根据该状态发送银行，商户具备直连条件的才允许生成待发送指令，否则直接设为处理成功）
            if(tpMerchant!=null && "1".equals(tpMerchant.getIsOnline())){
                vo.setVoucherStat(TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS3);
                vo.setTrandNo(SequenceSingleton.getSequence(SequenceEnum.TP_PAYMENT_BIS_EXC));
            }else{  //非直连商户，审批通过后，设为处理完成。
                vo.setVoucherStat(TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS0);
            }
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            //审批付款单，更新原状态控制为审批审批通过
            Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK, NetbankConstant.STATUE_WAIT_CHECK};
            this.updateTpPaymentStatus(vo, oldStatus);
            //计划执行状态同步更新
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP,
                    tpPayment.getBillNo(), NetbankConstant.STATUE_CHECK, user);
            //生成日志
            tpLogService.saveTpLog(tpPayment.getId(), TtPayConstant.TTPAY_LOGTYPE_APPROVE, TtPayConstant.COMMON_MESSAGE_APPROVESUCCESS, user);
        }
    }
    /**
     * 付款失败，单据退回至打回/暂存
     * @param id 主键
     * @param user 当前用户
     */
    public void recallTpPayment(String id, ICurrentUser user) {
        TpPayment tpPayment = this.get(id);
        //更新审批状态为打回
        tpPayment.setStatus(NetbankConstant.STATUE_BACK);
        //更新指令状态为空
        tpPayment.setVoucherStat(null);
        //更新商户订单号为空
        tpPayment.setTrandNo(null);
        tpPayment.setUpdateBy(user.getLoginName());
        tpPayment.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(tpPayment);
        //计划执行状态同步更新
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP, tpPayment.getBillNo(), NetbankConstant.STATUE_BACK, user);
        //生成日志
        tpLogService.saveTpLog(tpPayment.getId(), TtPayConstant.TTPAY_LOGTYPE_RECALL, TtPayConstant.COMMON_MESSAGE_RECALL, user);
    }
}
