package com.bytter.biz.foreign.service;

import cn.hutool.json.JSONUtil;
import com.bytter.biz.foreign.dao.FexTranLifeCycleDao;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.enums.LifeCycleOperationEnum;
import com.bytter.biz.foreign.service.lifeCycleOptype.factory.LifeCycleOperationFactory;
import com.bytter.biz.foreign.service.lifeCycleOptype.strategy.LifeCycleOperationStrategy;
import com.bytter.biz.foreign.utils.FexForeignConverter;
import com.bytter.biz.foreign.utils.FexTranReqUtil;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.system.api.IBtCurrencyPairService;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.BtCurrencyPairDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("fexTranLifeCycleService")
@WorkFlow({FexWorkFlowConstant.FEX_TRAN_LIFE_CYCLE})
public class FexTranLifeCycleService extends BaseService<FexTranLifeCycle, String> implements IWorkFlowListener, IWorkFlowRevokeListener {

    @Autowired
    private ICorpService corpService;

    @Autowired
    private IUserService userService;

    @Autowired
    private FexTranReqService fexTranReqService;

    @Autowired
    private FexMessageService fexMessageService;

    @Autowired
    private FexTranLifeCycleDao fexTranLifeCycleDao;

    @Autowired
    private FexProfitLossManageService fexProfitLossManageService;

    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;

    @Autowired(required = false)
    private IConfigService configService;

    @Autowired
    private FexCashFlowService fexCashFlowService;

    @Autowired
    private IWorkFlowService workFlowService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Autowired
    private LifeCycleOperationFactory lifeCycleOperationFactory;

    @Autowired
    private FexTranLifeCycleService fexTranLifeCycleService;

    @DubboReference(check = false)
    private IBtCurrencyPairService currencyPairService;

    @Autowired
    private FexProductService fexProductService;

    @Override
    public BaseDao<FexTranLifeCycle, String> getEntityDao() {
        return fexTranLifeCycleDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput input, ICurrentUser user){
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.FOREIGN_FEXTRAN_LIFECYCLE_QUERY, user.getId()), "ftlc.corp_id", true);
        DataTablesOutput output = fexTranLifeCycleDao.getDataTablesUnion(input, corpAuthSql);
        if (!EmptyUtils.isEmpty(output.getData())) {
            Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
            for (Object data : output.getData()) {
                FexTranLifeCycleVo fexTranLifeCycleVo = (FexTranLifeCycleVo)data;
                if (StringUtils.isNotEmpty(fexTranLifeCycleVo.getTranCurPairId())) {
                    BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranLifeCycleVo.getTranCurPairId());
                    fexTranLifeCycleVo.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
                }
                if (StringUtils.isNotEmpty(fexTranLifeCycleVo.getProfitLossCur()) && StringUtils.isNotEmpty(fexTranLifeCycleVo.getProfitLossCur().trim())) {
                    fexTranLifeCycleVo.setProfitLossCur(currencyMap.get(fexTranLifeCycleVo.getProfitLossCur()).getEnglishCode());
                }
            }
        }
        return output;
    }

    public FexTranLifeCycleVo getByUnion(String id) {
        FexTranLifeCycleVo fexTranLifeCycleVo = fexTranLifeCycleDao.getByUnion(id);
        if (null != fexTranLifeCycleVo) {
            Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
            if (StringUtils.isNotEmpty(fexTranLifeCycleVo.getTranCurPairId())) {
                BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranLifeCycleVo.getTranCurPairId());
                fexTranLifeCycleVo.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
            }
            if (StringUtils.isNotEmpty(fexTranLifeCycleVo.getProfitLossCur())) {
                fexTranLifeCycleVo.setProfitLossCur(currencyMap.get(fexTranLifeCycleVo.getProfitLossCur()).getEnglishCode());
            }
            if (StringUtils.isNotEmpty(fexTranLifeCycleVo.getBuyCurId())) {
                fexTranLifeCycleVo.setBuyCurCode(currencyMap.get(fexTranLifeCycleVo.getBuyCurId()).getEnglishCode());
            }
            if (StringUtils.isNotEmpty(fexTranLifeCycleVo.getSellCurId())) {
                fexTranLifeCycleVo.setSellCurCode(currencyMap.get(fexTranLifeCycleVo.getSellCurId()).getEnglishCode());
            }
        }
        return fexTranLifeCycleVo;
    }

    /**
     * 保存交易生命周期
     * @param fexTranReqVO
     */
    public FexTranLifeCycle saveFexTranLifeCycle(FexTranLifeCycleVo fexTranReqVO, String loginUserName) {
        FexTranLifeCycle fexTranLifeCycle = new FexTranLifeCycle();
        BeanUtils.copyNotNullProperties(fexTranReqVO, fexTranLifeCycle);
        fexTranLifeCycle.setReqId(fexTranReqVO.getId());
        fexTranLifeCycle.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_LIFE_CYCLE));
        fexTranLifeCycle.setLifeStatus(String.valueOf(ForeignConstant.STATUE_TEMP));
        fexTranLifeCycle.setCreateDate(new Date());
        fexTranLifeCycle.setCreateBy(loginUserName);
        saveEntity(fexTranLifeCycle);
        return fexTranLifeCycle;
    }

    /**
     *
     * 通过ID查询生命周期
     *
     * @param id
     * @return
     */
    public FexTranLifeCycleVo getById(String id){
        FexTranLifeCycle fexTranLifeCycle = fexTranLifeCycleDao.get(id);
        FexTranLifeCycleVo fexTranLifeCycleVo = new FexTranLifeCycleVo();
        if (!EmptyUtils.isEmpty(fexTranLifeCycle)){
            BeanUtils.copyProperties(fexTranLifeCycle, fexTranLifeCycleVo);
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
            if(currencyMap.containsKey(fexTranLifeCycleVo.getBuyCurId())){
                fexTranLifeCycleVo.setBuyCurCode(currencyMap.get(fexTranLifeCycleVo.getBuyCurId()).getEnglishCode());
            }
            if(currencyMap.containsKey(fexTranLifeCycleVo.getSellCurId())){
                fexTranLifeCycleVo.setSellCurCode(currencyMap.get(fexTranLifeCycleVo.getSellCurId()).getEnglishCode());
            }
            if(StringUtils.isNotEmpty(fexTranLifeCycleVo.getProfitLossCur())
                    && currencyMap.containsKey(fexTranLifeCycleVo.getProfitLossCur())){
                fexTranLifeCycleVo.setProfitLossCurCode(currencyMap.get(fexTranLifeCycleVo.getProfitLossCur()).getEnglishCode());
            }
        }
        return fexTranLifeCycleVo;
    }
    /**
     *
     * 通过交易ID查询生命周期
     *
     * @param reqId
     * @return
     */
    public FexTranLifeCycleVo getByReqId(String reqId, String opType){
        FexTranLifeCycleVo fexTranLifeCycleVo = fexTranLifeCycleDao.getByReqId(reqId, opType);
        if (null != fexTranLifeCycleVo) {
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
            fexTranLifeCycleVo.setBuyCurCode(currencyMap.get(fexTranLifeCycleVo.getBuyCurId()).getEnglishCode());
            fexTranLifeCycleVo.setSellCurCode(currencyMap.get(fexTranLifeCycleVo.getSellCurId()).getEnglishCode());
        }
        return fexTranLifeCycleVo;
    }
    /**
     *
     * 更新生命周期
     *
     * @param lifeCycleVo
     */
    @Transactional
    public void updateLifecycle(FexTranLifeCycleVo lifeCycleVo, ICurrentUser user) {
        FexTranLifeCycle entity = null;
        List<FexTranLifeCycleVo> tempLifecycles = fexTranLifeCycleDao.getTemporaryLifeCycle(Arrays.asList(lifeCycleVo.getReqId()));
        if (!EmptyUtils.isEmpty(tempLifecycles) && !tempLifecycles.get(0).getId().equals(lifeCycleVo.getId())) {
            throw new BusinessException(NetbankMessageEnum.NIS_MESSAGE_REPEAT_SUBMIT);
        }
        if(StringUtils.isNotBlank(lifeCycleVo.getId())){
            entity = fexTranLifeCycleService.get(lifeCycleVo.getId());
        }
        // 损益币种等于本位币，折本位币金额自动赋值
        if (StringUtils.isNotEmpty(lifeCycleVo.getProfitLossCur()) && StringUtils.isNotEmpty(lifeCycleVo.getCorpId())) {
            // 本位币
            CorpDTO corpDTO = corpService.get(lifeCycleVo.getCorpId());
            String corpBaseCur = corpDTO.getCurId();
            if (corpBaseCur.equals(lifeCycleVo.getProfitLossCur())) {
                lifeCycleVo.setConvertLossMoney(lifeCycleVo.getProfitLossMoney());
                if (EmptyUtils.isEmpty(lifeCycleVo.getProfitLossMoney()) && !EmptyUtils.isEmpty(lifeCycleVo.getAllLossMoney())) {
                    lifeCycleVo.setConvertLossMoney(lifeCycleVo.getAllLossMoney());
                }
            }
        }
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(lifeCycleVo.getReqId());
        if(null == entity){
            entity = new FexTranLifeCycle();
            BeanUtils.copyNotNullProperties(lifeCycleVo,entity);
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_LIFE_CYCLE));
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(new Date());
            if (EmptyUtils.isEmpty(entity.getProfitLossMoney()) && !EmptyUtils.isEmpty(lifeCycleVo.getAllLossMoney())) {
                entity.setProfitLossMoney(lifeCycleVo.getAllLossMoney());
            }
            entity.setPositionMoney(fexTranReqVO.getPositionMoney());
            if (LifeCycleOperationEnum.RATE_CONFIRM.getOpType().equals(lifeCycleVo.getOpType())) {
                entity.setSettleDate(fexTranReqVO.getSettleDate());
            }
            this.saveEntity(entity);
            logger.info("【新增】生命周期：" + LifeCycleOperationEnum.fromCode(lifeCycleVo.getOpType()).getOpName()
                    + ";操作人：" + user.getLoginName() + ";时间：" + DateUtils.format(new Date()) + "单据内容：" + JsonUtils.toJSONString(entity));
        } else{

            // 关键：更新前再次查询，确认实体未被删除（针对高并发场景）
            FexTranLifeCycle latestEntity = fexTranLifeCycleService.get(entity.getId());
            if (latestEntity == null) {
                return;
            }
            LifeCycleOperationStrategy strategy = lifeCycleOperationFactory.getStrategy(lifeCycleVo.getOpType());
            entity.setLifeStatus(lifeCycleVo.getLifeStatus());
            strategy.dealField(lifeCycleVo, user, entity);
            entity.setPositionMoney(fexTranReqVO.getPositionMoney());
            if (LifeCycleOperationEnum.RATE_CONFIRM.getOpType().equals(lifeCycleVo.getOpType())) {
                entity.setSettleDate(fexTranReqVO.getSettleDate());
            }
            this.updateEntity(entity);
            logger.info("【更新】生命周期：" + LifeCycleOperationEnum.fromCode(lifeCycleVo.getOpType()).getOpName()
                    + ";操作人：" + user.getLoginName() + ";时间：" + DateUtils.format(new Date()) + "单据内容：" + JsonUtils.toJSONString(entity));
        }
        //this.saveOrUpdate(entity);
        //启动审批
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(Integer.valueOf(lifeCycleVo.getLifeStatus()))) {
            // 更新交易状态
            FexTranReq fexTranReq = fexTranReqService.get(entity.getReqId());
            fexTranReq.setStatus(FexTranReqUtil.getReqStatusByOpType(entity.getOpType()));
            fexTranReqService.updateEntity(fexTranReq);

            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            //调用审批后方法
            getStartHandler().handler(FexWorkFlowConstant.FEX_TRAN_LIFE_CYCLE, entity.getId(), entity, entity.getCorpId(), user.getId());
            notificationService.complete(NotificationConstant.FOREIGN.FOREIGN_TRAN_LIFE_CYCLE, entity.getId(), user.getId());
        }
        if(user !=null) {
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_LIFE_CYCLE, user);
        }
    }
    /**
     * 生成资金流
     * @param fexTranReq
     * @param tranLifeCycle
     * @param user
     */
    public void saveFexCashFLow(FexTranReqVO fexTranReq, FexTranLifeCycle tranLifeCycle, ICurrentUser user) {
        FexCashFlow entity = new FexCashFlow();
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_CASH_FLOW));
        entity.setTranBillId(fexTranReq.getId());
        //结算类型：1-全额，2-净额
        entity.setSettleType(CommonConstant.YES);
        //现金流类型：0本金
        entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
        //现金流项目
        entity.setCashItem(fexTranReq.getCashItem());
        entity.setSettleDate(fexTranReq.getSettleDate());
        entity.setTranType(fexTranReq.getTranType());
        //跨行标志
        entity.setDifBank(fexTranReq.getDifBank());
        //备注
        entity.setRmk(fexTranReq.getRmk());
        //单据来源
        entity.setBillFrom(ForeignConstant.BILL_FROM_3);
        //获取主体单位账户
        FexCorpBankAccVO corpSearchVO = new FexCorpBankAccVO();
        //获取交易对手账户
        FexCorpBankAccVO cotpSearchVO = new FexCorpBankAccVO();
        // 收支标志
        String cdSign = "1";
        if(tranLifeCycle.getMoney().compareTo(BigDecimal.ZERO)>0){
            cdSign = "0";
        }
        if(ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())){
            entity.setCorpId(fexTranReq.getCorpId());
            entity.setCotpId(fexTranReq.getCotpId());
            if(CommonConstant.YES.equals(cdSign)){
                entity.setCorpAccId(fexTranReq.getSrcAccId());
                entity.setCorpBank(fexTranReq.getSrcBank());
                entity.setCorpAccName(fexTranReq.getSrcAccName());
                entity.setCorpBankAcc(fexTranReq.getSrcBankAcc());

                entity.setCotpBank(fexTranReq.getTargetBank());
                entity.setCotpAccId(fexTranReq.getTargetAccId());
                entity.setCotpAccName(fexTranReq.getTargetAccName());
                entity.setCotpBankAcc(fexTranReq.getTargetBankAcc());
            }else{
                entity.setCorpAccId(fexTranReq.getCorpAccId());
                entity.setCorpBank(fexTranReq.getCorpBank());
                entity.setCorpAccName(fexTranReq.getCorpAccName());
                entity.setCorpBankAcc(fexTranReq.getCorpBankAcc());

                entity.setCotpBank(fexTranReq.getCotpBank());
                entity.setCotpAccId(fexTranReq.getCotpAccId());
                entity.setCotpAccName(fexTranReq.getCotpAccName());
                entity.setCotpBankAcc(fexTranReq.getCotpBankAcc());
            }
            entity.setCdSign(cdSign);
            //外汇买卖交易-现金流付款账号对应的卖出币种
            entity.setPayerCurId(fexTranReq.getSellCurId());

            corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
            corpSearchVO.setTranType(fexTranReq.getTranType());
            corpSearchVO.setCorpId(fexTranReq.getCorpId());
            corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
            corpSearchVO.setProId(fexTranReq.getProId());
            FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
            if(!EmptyUtils.isEmpty(corpBankAcc)){
                //支付方式
                entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
            }
        }else if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())){//期权交易
            //设置现金流日期
            entity.setCashFlowDate(tranLifeCycle.getOpDate());
            //设置结算日期
            entity.setSettleDate(tranLifeCycle.getSettleDate());
            //设置现金流类型
            entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
            //设置币种
            //根据交易的买入方向和涨跌方向判断
            if((fexTranReq.getBusDirection().equals("1") && fexTranReq.getUpdownDirection().equals("0"))||
                    (fexTranReq.getBusDirection().equals("2") && fexTranReq.getUpdownDirection().equals("1"))){
                //取买入币种
                entity.setCurId(fexTranReq.getBuyCurId());
                //取买入金额
                entity.setPayMoney(tranLifeCycle.getBuyMoney());
            }else{
                //取卖出币种
                entity.setCurId(fexTranReq.getSellCurId());
                //取卖出金额（取负数）
                entity.setPayMoney(tranLifeCycle.getSellMoney().negate());
            }
            //设置银行
            entity.setCorpBank(fexTranReq.getCorpBank());
            //设置账户名称
            entity.setCorpBankAcc(fexTranReq.getCorpBankAcc());
            //设置银行账户
            //TODO
        }
        //是否跳过结算执行
        String isSkipSettle = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISSKIPSETTLE).getValue();
        if(CommonConstant.YES.equals(isSkipSettle)) {
            entity.setStatus(CommonConstant.STATUE_CHECK);
            entity.setPayStatus(CommonConstant.YES);
            if (CommonConstant.YES.equals(entity.getCdSign())) {
                //即期结汇的外汇买卖交易付款现金流产生付款指令
                if (ForeignConstant.FEX_BUS_TYPE_1.equals(fexTranReq.getFexBusType())
                        && ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())) {
                    entity.setPayStatus(CommonConstant.NO);
                }
            }else if(CommonConstant.NO.equals(entity.getCdSign())) {
                //外汇买卖交易：即期、购汇、本金交割，固定金额为1-买入金额，非掉期交易
                if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                        && ForeignConstant.FEX_BUS_TYPE_2.equals(fexTranReq.getFexBusType())
                        && CommonConstant.NO.equals(fexTranReq.getDeliveryType())
                        && CommonConstant.NO.equals(fexTranReq.getIsSwapTran())) {
                    if (CommonConstant.YES.equals(fexTranReq.getFixedMoney())) {
                        entity.setPayStatus(CommonConstant.NO);
                    }
                } else if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                        && ForeignConstant.FEX_BUS_TYPE_1.equals(fexTranReq.getFexBusType())
                        && CommonConstant.NO.equals(fexTranReq.getIsSwapTran())) {
                    //即期、结汇、非掉期交易
                    entity.setPayStatus(CommonConstant.NO);
                }
            }
        }else{
            entity.setStatus(CommonConstant.STATUE_TEMP);
            entity.setPayStatus(ForeignConstant.PAY_STATUS_0);
        }
        // 固定金额=买入金额
        if (CommonConstant.YES.equals(fexTranReq.getFixedMoney())) {
            entity.setCurId(fexTranReq.getSellCurId());
            entity.setPayMoney(fexTranReq.getSellMoney());
        } else {
            entity.setCurId(fexTranReq.getBuyCurId());
            entity.setPayMoney(fexTranReq.getBuyMoney());
        }
        entity.setCashFlowDate(tranLifeCycle.getOpDate());
        entity.setCreateBy(fexTranReq.getCreateBy());
        entity.setCreateDate(DateUtils.getDate());
        fexCashFlowService.saveOrUpdate(entity);
    }

    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(FexWorkFlowConstant.FEX_TRAN_LIFE_CYCLE, dataTablesInput);
        String businessSql =  SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "ftlc.id", true);
        DataTablesOutput<FexTranLifeCycleVo> dp  = fexTranLifeCycleDao.dataTableApprove(dataTablesInput, businessSql);
        if (!EmptyUtils.isEmpty(dp.getData())) {
            Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
            for (Object data : dp.getData()) {
                FexTranLifeCycleVo fexTranLifeCycleVo = (FexTranLifeCycleVo)data;
                if (StringUtils.isNotEmpty(fexTranLifeCycleVo.getTranCurPairId())) {
                    BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranLifeCycleVo.getTranCurPairId());
                    fexTranLifeCycleVo.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
                }
                if (StringUtils.isNotEmpty(fexTranLifeCycleVo.getProfitLossCur())) {
                    fexTranLifeCycleVo.setProfitLossCur(currencyMap.get(fexTranLifeCycleVo.getProfitLossCur()).getEnglishCode());
                }
            }
        }
        return workFlowBusiness.merge(dp);
    }



    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        FexTranLifeCycle entity = fexTranLifeCycleDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        Integer messageType = 0;
        String message = "";
        if (isPass) {
            entity.setLifeStatus(String.valueOf(ForeignConstant.STATUE_CHECK));
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            message = FexMessageConstant.FEX_LOG_APP_SUCC;
            // 审批回写处理
            LifeCycleOperationStrategy strategy = lifeCycleOperationFactory.getStrategy(entity.getOpType());
            logger.info("FexTranLifeCycleService flowCompleted entity:" + JSONUtil.toJsonStr(entity));
            strategy.approvePass(entity, user);
        } else {
            entity.setLifeStatus(String.valueOf(ForeignConstant.STATUE_BACK));
            if (LifeCycleOperationEnum.RATE_CONFIRM.getOpType().equals(entity.getOpType())) {
                //汇率确认审批打回后，交易恢复为待汇率确认
                fexTranReqService.updateFexTranReqStatus(Arrays.asList(entity.getReqId()),
                        ForeignConstant.STATUE_TOBE_RATE_CONFIRM,
                        new Integer[]{ForeignConstant.STATUE_RATE_CONFIRM});
            }
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
            message = FexMessageConstant.FEX_LOG_APP_BACK;
        }
        //生成日志
        fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_LIFE_CYCLE, user);
        fexTranLifeCycleDao.update(entity);
    }

    /**
     * 根据交易id获取生命周期集合
     * @param reqId
     * @return
     */
    public List<FexTranLifeCycleVo> listFexTranLifeCycle(String reqId) {
        return fexTranLifeCycleDao.listFexTranLifeCycle(reqId);
    }

    /**
     *
     * 即期自动交割
     *
     * @param fexTranReqVO
     * @param user
     */
    @Transactional
    public void autoSpotDelivery(FexTranReqVO fexTranReqVO, ICurrentUser user, FexTranLifeCycle fexTranLifeCycle) {
        this.getSession().flush();
        // 买卖交易 && 即期交易
        if (!ForeignConstant.TRAN_TYPE_1.equals(fexTranReqVO.getTranType()) ||
                !ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReqVO.getFexTranType()) ||
                !CommonConstant.NO.equals(fexTranReqVO.getIsSwapTran())) {
            return;
        }
        // 校验产品是否自动交割
        FexProductVO productVO = fexProductService.getByIdUnion(fexTranReqVO.getProId());
        if (null == productVO || !CommonConstant.YES.equals(productVO.getIsAutoDelivery())) {
            return;
        }

        FexTranLifeCycleVo fexTranLifeCycleVo = FexForeignConverter.converterFexTranLifeCycleBean(fexTranReqVO);
        fexTranLifeCycleVo.setOpDate(fexTranReqVO.getTranDate());
        // 外汇即期是否计算损益
        ConfigDTO profitAndLossConfig = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISPROFITANDLOSS);
        if (null != profitAndLossConfig && CommonConstant.YES.equals(profitAndLossConfig.getValue())) {
            if (null != fexTranLifeCycle) { // 汇率确认取生命周期生成损益
                fexTranLifeCycleVo.setProfitLossCur(fexTranLifeCycle.getProfitLossCur());
                fexTranLifeCycleVo.setProfitLossMoney(fexTranLifeCycle.getProfitLossMoney());

                fexTranLifeCycleVo.setConvertCurType(fexTranLifeCycle.getConvertCurType());
                fexTranLifeCycleVo.setConvertRate(fexTranLifeCycle.getConvertRate());
                fexTranLifeCycleVo.setConvertLossMoney(fexTranLifeCycle.getConvertLossMoney());
            } else {
                fexTranLifeCycleVo.setProfitLossCur(fexTranReqVO.getProfitLossCur());
                fexTranLifeCycleVo.setProfitLossMoney(fexTranReqVO.getProfitLossMoney());

                fexTranLifeCycleVo.setConvertCurType(fexTranReqVO.getConvertCurType());
                fexTranLifeCycleVo.setConvertRate(fexTranReqVO.getConvertRate());
                fexTranLifeCycleVo.setConvertLossMoney(fexTranReqVO.getConvertMoney());
            }
        }
        if (null != fexTranLifeCycle) {
            fexTranLifeCycleVo.setSellMoney(fexTranLifeCycle.getSellMoney());
            fexTranLifeCycleVo.setBuyMoney(fexTranLifeCycle.getBuyMoney());
            fexTranLifeCycleVo.setContractRate(fexTranLifeCycle.getContractRate());
            fexTranLifeCycleVo.setSpotRate(fexTranLifeCycle.getSpotRate());
        }
        if (ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReqVO.getBusDirection())) {
            fexTranLifeCycleVo.setOpMoney(fexTranReqVO.getBuyMoney());
        } else if (ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReqVO.getBusDirection())) {
            fexTranLifeCycleVo.setOpMoney(fexTranReqVO.getSellMoney());
        }
        fexTranLifeCycleVo.setPayerAcc(fexTranReqVO.getSrcBankAcc());
        fexTranLifeCycleVo.setPayeeAcc(fexTranReqVO.getCorpBankAcc());
        fexTranLifeCycleVo.setCreateBy(fexTranReqVO.getCreateBy());
        fexTranLifeCycleVo.setOpType(LifeCycleOperationEnum.DEADLINE_DELIVERY.getOpType());
        fexTranLifeCycleVo.setLifeStatus(String.valueOf(ForeignConstant.STATUE_WAIT_CHECK));
        fexTranLifeCycleVo.setBuyBal(BigDecimal.ZERO);
        fexTranLifeCycleVo.setSellBal(BigDecimal.ZERO);
        fexTranLifeCycleService.updateLifecycle(fexTranLifeCycleVo, user);
        logger.info("即期自动交割完成,交易单号：" + fexTranReqVO.getBillNo());
        this.getSession().flush();
    }

    /**
     *
     * 获取流程中的生命周期
     *
     * @param reqIds
     * @return
     */
    public List<FexTranLifeCycleVo> getTemporaryLifeCycleList(List<String> reqIds) {
        return fexTranLifeCycleDao.getTemporaryLifeCycle(reqIds);
    }

    /**
     * 删除历史生命周期，如果生命周期是打回，交易单状态变为续存
     * @param id
     */
    public void deleteLifeCycle(String id){
        FexTranLifeCycleVo vo=   fexTranLifeCycleDao.getByUnion(id);
        FexTranReqVO fexTranReqVO=   fexTranReqService.getByIdUnion(vo.getReqId());
        // 汇率确认打回删除不需要变为存续
        if(String.valueOf(ForeignConstant.STATUE_BACK).equals(vo.getLifeStatus())
                && !ForeignConstant.STATUE_TOBE_RATE_CONFIRM.equals(fexTranReqVO.getStatus())){

            Integer[] oldStatus={ fexTranReqVO.getStatus()};
            fexTranReqVO.setStatus(ForeignConstant.ONGOING);
            fexTranReqService.updateFexTranReqStatus(fexTranReqVO,oldStatus);
        }
        this.delete(id);
    }

}
