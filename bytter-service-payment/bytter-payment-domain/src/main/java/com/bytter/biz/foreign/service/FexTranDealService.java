package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranDealDao;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexTranDeal;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.biz.foreign.vo.FexTranDealVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Service
 * @Description: 交易管理处理表
 * @author huangx
 * @date 2020-02-27 15:20:33
 * @version 1.0
 *
 */
@WorkFlow({
        FexWorkFlowConstant.FEX_TRAN_DEAL_DF,
        FexWorkFlowConstant.FEX_TRAN_DEAL_DE,
        FexWorkFlowConstant.FEX_TRAN_DEAL_NDF})
@Service("fexTranDealService")
@Transactional
public class FexTranDealService extends BaseService<FexTranDeal, String> implements IWorkFlowListener, IWorkFlowRevokeListener, ICorpDisableVerifyService {

    @Autowired
    private FexTranDealDao fexTranDealDao;
    @Autowired
    private FexMessageService fexMessageService;
    @Autowired(required=false)
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired(required=false)
    private IWorkFlowService workFlowServiceImpl;
    @Autowired
    private FexTranReqService fexTranReqService;
    @Autowired
    private FexCashFlowService fexCashFlowService;
    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;
    @Autowired(required = false)
    private IMultiLangService multiLangService;
    @Autowired(required=false)
    private IFbsBudgetProcessService fbsBudgetProcessService;

    @Override
    public BaseDao<FexTranDeal, String> getEntityDao() {
        return fexTranDealDao;
    }

    /**
     * 根据ID获取交易信息
     * @param id
     * @return
     */
    public FexTranDealVO getByIdUnion(String id){
        return fexTranDealDao.getByIdUnion(id);
    }

    /**
     * 根据交易ID获取交易交割信息
     * @param tranReqId
     * @return
     */
    public List<FexTranDeal> listByTranReqId(String tranReqId){
        return fexTranDealDao.listBy("tranReqId", tranReqId);
    }

    /**
     * 根据交易ID获取交易交割信息
     * @param opType
     * @param tranReqId
     * @return
     */
    public List<FexTranDeal> listByTranReqId(String opType, String tranReqId){
        return fexTranDealDao.listByTranReqId(opType, tranReqId);
    }

    /**
     * 根据交易ID获取交易交割信息
     * @param opType
     * @param tranReqId
     * @return
     */
    public List<FexTranDealVO> listVOByTranReqId(String opType, String tranReqId){
        return fexTranDealDao.listVOByTranReqId(opType, tranReqId);
    }

    /**
     * 保存交易交割信息
     *
     * @param fexTranDeal
     */
    public void saveFexTranDeal(FexTranDealVO fexTranDeal, ICurrentUser user) {
        //结算日期交易(汇率确认无需校验)
        if(!ForeignConstant.OP_TYPE_4.equals(fexTranDeal.getOpType())){
            /**操作类型 1-提前/展期交割，2-违约，3-NDF交割，4-汇率确认*/
            //校验这笔交易现存的现金流，都必须是打回或暂存状态
            List<FexCashFlowVO> listCashFlow = fexCashFlowService.listByTranBillIdUnion(fexTranDeal.getTranReqId());
            for (FexCashFlowVO fexCashFlow : listCashFlow) {
                if (!CommonConstant.STATUE_TEMP.equals(fexCashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(fexCashFlow.getStatus())) {
                    throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, fexCashFlow.getBillNo());
                }
            }
        }
        FexTranDeal entity = new FexTranDeal();
        BeanUtils.copyNotNullProperties(fexTranDeal, entity);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        //违约：结算金额=违约金*违约汇率
        if(ForeignConstant.OP_TYPE_2.equals(fexTranDeal.getOpType())){
            entity.setSettleMoney(entity.getDefaultMoney().multiply(entity.getWishRate()));
        }
        this.saveEntity(entity);

        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            fexTranDeal.setId(entity.getId());
            fexTranDeal.setCreateBy(user.getLoginName());
            fexTranDeal.setCreateDate(DateUtils.getDate());
            //inject(fexTranDeal);
            //调用审批后方法
            getStartHandler().handler(this.getWorkFlowByOpType(entity.getOpType()), entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //汇率确认
        if(ForeignConstant.OP_TYPE_4.equals(entity.getOpType())){
            FexTranReq tranReq = this.fexTranReqService.get(entity.getTranReqId(), true);
            //买入
            if(ForeignConstant.DELIVERY_SIGN_1.equals(entity.getDeliverySign())){
                tranReq.setBuyMoney(entity.getSettleMoney());
            }else{
                tranReq.setSellMoney(entity.getSettleMoney());
            }
            tranReq.setContractRate(entity.getDeliveryRate());
            tranReq.setUpdateBy(user.getLoginName());
            tranReq.setUpdateDate(DateUtils.getDate());
            this.fexTranReqService.saveOrUpdate(tranReq);

            if(ForeignConstant.DELIVERY_SIGN_2.equals(entity.getDeliverySign())) {
                fexTranDeal.setId(entity.getId());
                this.writeBudgetProcess(fexTranDeal, tranReq, user);
            }
            //生成现金流
            this.saveFexCashFLow(entity, null);
        }
        if(user !=null) {
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_DEAL, user);
        }
    }

    /**
     * 写入计划执行
     *
     * @param fexTranDeal
     */
    private void writeBudgetProcess(FexTranDealVO fexTranDeal, FexTranReq fexTranReq, ICurrentUser user) {
        //写入之前先删除
        fbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_TRANEXERCISE, BudgetConstant.PROCESS_SYSNAME_FEX, fexTranDeal.getId());
        List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
        if(fexTranDeal.getItemCode() != null) {
            for (int i = 0; i < fexTranDeal.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(fexTranDeal, fexTranReq, user);
                fbsBudgetProcessDTO.setItemCode(fexTranDeal.getItemCode()[i]);
                fbsBudgetProcessDTO.setItemName(fexTranDeal.getItemName()[i]);
                fbsBudgetProcessDTO.setProcessMoney(fexTranDeal.getProcessMoney()[i]);
                fbsBudgetProcessDTO.setCurId(fexTranDeal.getCurId());
                if (fexTranDeal.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(fexTranDeal.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(fexTranDeal.getProcessCurMoney()[i]);
                }
                list.add(fbsBudgetProcessDTO);
            }
        }
        this.fbsBudgetProcessService.writeBudgetProcess(list, user);
    }
    /**
     * 构建计划执行
     *
     * @param fexTranDeal
     * @return
     */
    private FbsBudgetProcessDTO bulidFbsBudgetProcessDTO(FexTranDealVO fexTranDeal, FexTranReq fexTranReq, ICurrentUser user) {
        FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
        fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
        fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
        fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setStatus(ForeignConstant.STATUE_CHECK);
        fbsBudgetProcessDTO.setBillId(fexTranReq.getId());
        fbsBudgetProcessDTO.setBillNo(fexTranReq.getBillNo());
        fbsBudgetProcessDTO.setProcessDate(fexTranDeal.getDeliveryDate());
        fbsBudgetProcessDTO.setCorpId(fexTranDeal.getCorpId());
        fbsBudgetProcessDTO.setCurId(fexTranDeal.getCurId());
        fbsBudgetProcessDTO.setSettlType("1");
        fbsBudgetProcessDTO.setPurpose(fexTranDeal.getRmk());
        fbsBudgetProcessDTO.setWishPayday(fexTranDeal.getDeliveryDate());
        fbsBudgetProcessDTO.setPayerAcc(fexTranReq.getSrcBankAcc());
        fbsBudgetProcessDTO.setPayeeAcc(fexTranReq.getTargetBankAcc());
        fbsBudgetProcessDTO.setPayeeAccName(fexTranReq.getTargetAccName());
        //fbsBudgetProcessDTO.setPayeeBank(fexTranReq.getTargetBankName());
        fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_TRAN);
        fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_FEX);
        return fbsBudgetProcessDTO;
    }

    /**
     * 修改交易组信息
     *
     * @param fexTranDeal
     */
    public void updateFexTranDeal(FexTranDealVO fexTranDeal, ICurrentUser user) {
        /**操作类型 1-提前/展期交割，2-违约，3-NDF交割*/
        //校验这笔交易现存的现金流，都必须是打回或暂存状态
        List<FexCashFlowVO> listCashFlow = fexCashFlowService.listByTranBillIdUnion(fexTranDeal.getTranReqId());
        for (FexCashFlowVO fexCashFlow : listCashFlow) {
            if (!CommonConstant.STATUE_TEMP.equals(fexCashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(fexCashFlow.getStatus())) {
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, fexCashFlow.getBillNo());
            }
        }

        FexTranDeal entity = this.get(fexTranDeal.getId(), true);
        BeanUtils.copyNotNullProperties(fexTranDeal, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        //违约：结算金额=违约金*违约汇率
        if(ForeignConstant.OP_TYPE_2.equals(fexTranDeal.getOpType())){
            entity.setSettleMoney(entity.getDefaultMoney().multiply(entity.getWishRate()));
        }
        this.saveOrUpdate(entity);

        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_UPDATE_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_UPDATE_SUBMIT_SUCC;
            fexTranDeal.setCreateBy(entity.getCreateBy());
            fexTranDeal.setCreateDate(entity.getCreateDate());
            //inject(fexTranDeal);
            //调用审批后方法
            getStartHandler().handler(this.getWorkFlowByOpType(entity.getOpType()), entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //生成日志
        fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_DEAL, user);
        //完成通知提醒
        notificationService.complete(this.getTagByOpType(entity.getOpType()), entity.getId(), user.getId());
    }

    /**
     * 根据操作类型返回不同的跳转页面
     *
     * @param opType
     * @return
     */
    public String getWorkFlowByOpType(String opType) {
        /**操作类型 1-提前交割或展期、2-违约、3-NDF交割*/
        String workFlow = "";
        if (ForeignConstant.OP_TYPE_1.equals(opType)) {
            workFlow = FexWorkFlowConstant.FEX_TRAN_DEAL_DF;
        } else if (ForeignConstant.OP_TYPE_2.equals(opType)) {
            workFlow = FexWorkFlowConstant.FEX_TRAN_DEAL_DE;
        } else if (ForeignConstant.OP_TYPE_3.equals(opType)) {
            workFlow = FexWorkFlowConstant.FEX_TRAN_DEAL_NDF;
        }
        return workFlow;
    }

    /**
     * 根据操作类型返回不同的跳转页面
     *
     * @param opType
     * @return
     */
    public String getTagByOpType(String opType) {
        /**操作类型 1-提前交割或展期、2-违约、3-NDF交割*/
        String tag = "";
        if (ForeignConstant.OP_TYPE_1.equals(opType)) {
            tag = NotificationConstant.FOREIGN.TRANDEAL_DELIVERY_APPLY;
        } else if (ForeignConstant.OP_TYPE_2.equals(opType)) {
            tag = NotificationConstant.FOREIGN.TRANDEAL_DEFAULT_APPLY;
        } else if (ForeignConstant.OP_TYPE_3.equals(opType)) {
            tag = NotificationConstant.FOREIGN.TRANDEAL_NDFDELIVERY_APPLY;
        }
        return tag;
    }

    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FexTranDeal fexTranDeal = this.get(workFlowResult.getBusinessKey(), true);
        //更新审批状态
        fexTranDeal.setStatus(NetbankConstant.STATUE_TEMP);
        fexTranDeal.setUpdateBy(user.getLoginName());
        fexTranDeal.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(fexTranDeal);
        //生成日志
        fexMessageService.saveMessage(fexTranDeal.getId(), FexMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, FexMessageConstant.FEX_LOG_WITHDRAW_SUCC, FexMessageConstant.BUS_TYPE_FEX_TRAN_DEAL, user);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FexTranDeal entity = this.get(workFlowResult.getBusinessKey(), true);
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        if (workFlowResult.isPass()) {
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            entity.setStatus(CommonConstant.STATUE_CHECK);//审批通过
            String newTranReqId = "";
            /**操作类型 1-提前/展期交割，2-违约，3-NDF交割，4-汇率确认*/
            if(ForeignConstant.OP_TYPE_1.equals(entity.getOpType())){
                FexTranDeal fexTranDeal = this.buildFexTranDealOpp(entity);
                //生成现金流
                this.saveFexCashFLow(fexTranDeal, newTranReqId);
            } else if (ForeignConstant.OP_TYPE_2.equals(entity.getOpType())) {
                //生成反方向的交易
                newTranReqId = this.createFexTranReqContrary(entity, user);
            }
            //生成现金流
            this.saveFexCashFLow(entity, newTranReqId);
        } else {
            entity.setStatus(CommonConstant.STATUE_BACK);//审批打回
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);

        //生成日志
        fexMessageService.saveMessage(entity.getId(), messageType, "", FexMessageConstant.BUS_TYPE_FEX_TRAN_DEAL, user);
    }

    /**
     * 生成相反方向的交易
     * @param fexTranDeal
     */
    public String createFexTranReqContrary(FexTranDeal fexTranDeal, ICurrentUser user) {
        //获取原交易
        FexTranReq fexTranReq = fexTranReqService.get(fexTranDeal.getTranReqId(), true);
        //原交易对应的现金流
        List<FexCashFlowVO> listCashFlow = fexCashFlowService.listByTranBillIdUnion(fexTranDeal.getTranReqId());
        FexTranReq entity = new FexTranReq();
        BeanUtils.copyNotNullProperties(fexTranReq, entity);
        entity.setId(null);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_REQ));
        //关联交易号码=原交易号码
        entity.setRelBillNo(fexTranReq.getBillNo());
        //买入币种=原交易卖出币种
        entity.setBuyCurId(fexTranReq.getSellCurId());
        //卖出币种=原交易买入币种
        entity.setSellCurId(fexTranReq.getBuyCurId());
        for(FexCashFlowVO cashFlowVO:listCashFlow) {
            if (CommonConstant.YES.equals(cashFlowVO.getCdSign())) {
                //买入金额=原交易最新的一条卖出现金流金额，取绝对值（交易A若做了“提前交割/展期”，则取“提前交割/展期”后剩余的货币金额）
                entity.setBuyMoney(cashFlowVO.getPayMoney().abs());
                break;
            }
        }
        for(FexCashFlowVO cashFlowVO:listCashFlow) {
            if(CommonConstant.NO.equals(cashFlowVO.getCdSign())) {
                //卖出金额=原交易最新的一条买入现金流金额，取绝对值（交易A若做了“提前交割/展期”，则取“提前交割/展期”后剩余的货币金额）
                entity.setSellMoney(cashFlowVO.getPayMoney().abs());
                break;
            }
        }
        //收款账号=原交易付款账号
        entity.setCorpAccId(fexTranReq.getSrcAccId());
        entity.setCorpBank(fexTranReq.getSrcBank());
        entity.setCorpBankAcc(fexTranReq.getSrcBankAcc());
        entity.setCorpAccName(fexTranReq.getSrcAccName());
        //付款账号=原交易收款账号
        entity.setSrcAccId(fexTranReq.getCorpAccId());
        entity.setSrcBank(fexTranReq.getCorpBank());
        entity.setSrcBankAcc(fexTranReq.getCorpBankAcc());
        entity.setSrcAccName(fexTranReq.getCorpAccName());
        //交易日期、结算日期、申请日期=系统当前日
        entity.setTranDate(DateUtils.getDateYMD());
        entity.setSettleDate(DateUtils.getDateYMD());
        entity.setStatus(CommonConstant.STATUE_CHECK);
        entity.setCreateDate(DateUtils.getDate());
        entity.setCreateBy(user.getLoginName());
        fexTranReqService.saveEntity(entity);

        //更新原交易的状态为已违约
        fexTranReq.setStatus(ForeignConstant.STATUE_DEFAULT);
        //关联交易号码=新交易号码
        fexTranReq.setRelBillNo(entity.getBillNo());
        fexTranReqService.updateEntity(fexTranReq);

        //原交易的现金流转移到新的交易上
        List<FexCashFlow> addCashFlowList = new ArrayList<>();
        for(FexCashFlowVO fexCashFlow:listCashFlow){
            FexCashFlow cashFlow = new FexCashFlow();
            BeanUtils.copyNotNullProperties(fexCashFlow, cashFlow);
            cashFlow.setId(null);
            cashFlow.setTranBillId(entity.getId());
            cashFlow.setRmk("交易"+fexTranReq.getBillNo()+"违约生成的现金流");
            addCashFlowList.add(cashFlow);
        }
        fexCashFlowService.batchSave(addCashFlowList);
        return entity.getId();
    }

    /**
     * 根据固定金额生成非固定金额
     * @param entity
     * @return
     */
    private FexTranDeal buildFexTranDealOpp(FexTranDeal entity){
        FexTranDeal fexTranDeal = new FexTranDeal();
        //获取交易
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(entity.getTranReqId());
        fexTranDeal.setTranReqId(entity.getTranReqId());
        fexTranDeal.setCorpId(entity.getCorpId());
        fexTranDeal.setOldSettleDate(entity.getOldSettleDate());
        fexTranDeal.setNewSettleDate(entity.getNewSettleDate());
        fexTranDeal.setSettleRate(entity.getSettleRate());
        fexTranDeal.setWishRate(entity.getWishRate());
        fexTranDeal.setSettleMoney(entity.getSettleMoney().multiply(entity.getSettleRate()));
        fexTranDeal.setBal(entity.getBal().multiply(fexTranReq.getContractRate()));
        fexTranDeal.setOpType(entity.getOpType());
        fexTranDeal.setStatus(ForeignConstant.STATUE_CHECK);
        fexTranDeal.setCreateBy(entity.getCreateBy());
        fexTranDeal.setCreateDate(DateUtils.getDate());
        //固定金额  1-买入，2-卖出
        if(ForeignConstant.FIXED_MONEY_1.equals(fexTranReq.getFixedMoney())){
            fexTranDeal.setDeliverySign(ForeignConstant.DELIVERY_SIGN_2);
            fexTranDeal.setCurId(fexTranReq.getSellCurId());
        }else{
            fexTranDeal.setDeliverySign(ForeignConstant.DELIVERY_SIGN_1);
            fexTranDeal.setCurId(fexTranReq.getBuyCurId());
        }
        this.saveEntity(fexTranDeal);
        return fexTranDeal;
    }

    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        String sign = "";
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqi_opType"))) {
            String opType = String.valueOf(dataTablesInput.getFormdata().get("eqi_opType"));
            if (EmptyUtils.isNumeric(opType)) {
                sign = this.getWorkFlowByOpType(opType);
            }
        }
        WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "t.id", true);
        DataTablesOutput<FexTranDealVO> dp = this.fexTranDealDao.dataTableApprove(dataTablesInput, businessSql);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 删除交易组信息
     *
     * @param id
     */
    public void delFexTranDeal(String id, ICurrentUser user) {
        FexTranDeal entity = this.get(id, true);
        this.deleteEntity(entity);
        //生成日志
        fexMessageService.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_DELETE, FexMessageConstant.FEX_LOG_DELETE_SUCC, FexMessageConstant.BUS_TYPE_FEX_TRAN_DEAL, user);
        //完成通知提醒
        notificationService.complete(this.getTagByOpType(entity.getOpType()), entity.getId(), user.getId());
    }


    /**
     * 现金流生成
     * @param fexTranDeal
     */
    public void saveFexCashFLow(FexTranDeal fexTranDeal, String newTranReqId) {
        List<FexCashFlowVO> listCashFlow = null;
        //结算日期交易(汇率确认无需校验)
        if(!ForeignConstant.OP_TYPE_4.equals(fexTranDeal.getOpType())) {
            //校验这笔交易现存的现金流，都必须是打回或暂存状态
            listCashFlow = fexCashFlowService.listByTranBillIdUnion(fexTranDeal.getTranReqId());
            for (FexCashFlowVO fexCashFlow : listCashFlow) {
                if (!CommonConstant.STATUE_TEMP.equals(fexCashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(fexCashFlow.getStatus())) {
                    throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, fexCashFlow.getBillNo());
                }
            }
        }
        //无本金交割存在结算金额为0，无需生成现金流
        if(fexTranDeal.getSettleMoney().compareTo(BigDecimal.ZERO)==0){
            return;
        }
        //获取交易
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(fexTranDeal.getTranReqId());
        FexCashFlow entity = new FexCashFlow();
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_CASH_FLOW));
        if(StringUtils.isNotBlank(newTranReqId)){
            entity.setTranBillId(newTranReqId);
        } else {
            entity.setTranBillId(fexTranDeal.getTranReqId());
        }
        entity.setCashFlowDate(DateUtils.getDate());
        //结算类型：1-全额，2-净额
        entity.setSettleType(CommonConstant.YES);
        entity.setSettleDate(fexTranDeal.getNewSettleDate());
        //外汇买卖交易
        entity.setTranType(fexTranReq.getTranType());
        entity.setPayMoney(fexTranDeal.getSettleMoney().abs());
        entity.setCurId(fexTranDeal.getCurId());
        entity.setCorpId(fexTranDeal.getCorpId());
        entity.setCotpId(fexTranReq.getCotpId());

        //获取主体单位账户
        FexCorpBankAccVO corpSearchVO = new FexCorpBankAccVO();
        corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
        corpSearchVO.setTranType(fexTranReq.getTranType());
        corpSearchVO.setProId(fexTranReq.getProId());
        corpSearchVO.setCurId(entity.getCurId());
        //获取交易对手账户
        FexCorpBankAccVO cotpSearchVO = new FexCorpBankAccVO();
        cotpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_COTP);
        cotpSearchVO.setTranType(fexTranReq.getTranType());
        cotpSearchVO.setProId(fexTranReq.getProId());
        cotpSearchVO.setCurId(entity.getCurId());
        /**操作类型 1-提前/展期交割，2-违约，3-NDF交割，4-汇率确认*/
        if(ForeignConstant.OP_TYPE_1.equals(fexTranDeal.getOpType()) || ForeignConstant.OP_TYPE_3.equals(fexTranDeal.getOpType())
                || ForeignConstant.OP_TYPE_4.equals(fexTranDeal.getOpType())){
            //3-NDF交割 判断结算金额如果大于零，表示收款，小于零，表示付款
            if(ForeignConstant.OP_TYPE_3.equals(fexTranDeal.getOpType())){
                if(fexTranDeal.getSettleMoney().compareTo(BigDecimal.ZERO)>0){
                    fexTranDeal.setDeliverySign(ForeignConstant.DELIVERY_SIGN_1);
                }else{
                    fexTranDeal.setDeliverySign(ForeignConstant.DELIVERY_SIGN_2);
                }
            }
            //1-提前交割或展期
            //现金流类型：0本金，1利息，2违约金
            entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
            FexCashFlow fexCashFlow = null;
            if(CommonConstant.YES.equals(fexTranDeal.getDeliverySign())){
                //1-买入
                entity.setCdSign(CommonConstant.NO);
                if(ForeignConstant.OP_TYPE_3.equals(fexTranDeal.getOpType())){
                    entity.setCurId(fexTranDeal.getCurId());
                }else {
                    entity.setCurId(fexTranReq.getBuyCurId());
                    if(ForeignConstant.OP_TYPE_1.equals(fexTranDeal.getOpType())){
                        //获取交易买入（收款）的第一笔结算现金流
                        fexCashFlow = fexCashFlowService.getFirstCashFlowByTranReqId(fexTranDeal.getTranReqId(), entity.getCdSign());
                    }
                }
                corpSearchVO.setCdSign(CommonConstant.NO);//单位-收
                cotpSearchVO.setCdSign(CommonConstant.YES);//交易对手-付
            }else{
                //卖出
                entity.setCdSign(CommonConstant.YES);
                if(ForeignConstant.OP_TYPE_3.equals(fexTranDeal.getOpType())){
                    entity.setCurId(fexTranDeal.getCurId());
                }else {
                    entity.setCurId(fexTranReq.getSellCurId());
                    if(ForeignConstant.OP_TYPE_1.equals(fexTranDeal.getOpType())) {
                        //获取交易卖出（付款）的第一笔结算现金流
                        fexCashFlow = fexCashFlowService.getFirstCashFlowByTranReqId(fexTranDeal.getTranReqId(), entity.getCdSign());
                    }
                }
                corpSearchVO.setCdSign(CommonConstant.YES);//单位-付
                cotpSearchVO.setCdSign(CommonConstant.NO);//交易对手-收
            }
            if(ForeignConstant.OP_TYPE_1.equals(fexTranDeal.getOpType()) && fexCashFlow!=null){
                //现金流【状态】必须为0-暂存或者-1-打回
                if(!CommonConstant.STATUE_TEMP.equals(fexCashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(fexCashFlow.getStatus())){
                    throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, fexCashFlow.getBillNo());
                }
                fexCashFlow.setPayMoney(fexTranDeal.getBal());
                fexCashFlow.setUpdateBy(fexTranDeal.getCreateBy());
                fexCashFlow.setUpdateDate(DateUtils.getDate());
                fexCashFlowService.saveOrUpdate(fexCashFlow);
            }

            //单位-账户信息
            corpSearchVO.setCorpId(fexTranReq.getCorpId());
            corpSearchVO.setOppCorpId(fexTranReq.getCotpId());
            corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
            FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
            if(!EmptyUtils.isEmpty(corpBankAcc)){
                entity.setCorpAccId(corpBankAcc.getAccId());
                entity.setCorpBank(corpBankAcc.getBankType());
                entity.setCorpAccName(corpBankAcc.getAccName());
                entity.setCorpBankAcc(corpBankAcc.getBankAcc());
                entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
            }

            //交易对手-账户信息
            cotpSearchVO.setCorpId(fexTranReq.getCotpId());
            cotpSearchVO.setOppCorpId(fexTranReq.getCorpId());
            cotpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
            FexCorpBankAccDtlVO cotpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(cotpSearchVO);
            if(!EmptyUtils.isEmpty(cotpBankAcc)){
                entity.setCotpBank(cotpBankAcc.getBankType());
                entity.setCotpAccId(cotpBankAcc.getAccId());
                entity.setCotpAccName(cotpBankAcc.getAccName());
                entity.setCotpBankAcc(cotpBankAcc.getBankAcc());
            }
        }else if(ForeignConstant.OP_TYPE_2.equals(fexTranDeal.getOpType())){
            //2-违约
            entity.setPayMoney(fexTranDeal.getDefaultMoney());
            entity.setCdSign(CommonConstant.YES);
            //现金流类型：0本金，1利息，2违约金
            entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_2);

            //单位-账户信息
            corpSearchVO.setCorpId(fexTranReq.getCorpId());
            corpSearchVO.setOppCorpId(fexTranReq.getCotpId());
            corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_2);
            corpSearchVO.setCurId(fexTranDeal.getCurId());
            corpSearchVO.setCdSign(CommonConstant.YES);//付
            FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
            if(!EmptyUtils.isEmpty(corpBankAcc)){
                entity.setCorpAccId(corpBankAcc.getAccId());
                entity.setCorpBank(corpBankAcc.getBankType());
                entity.setCorpAccName(corpBankAcc.getAccName());
                entity.setCorpBankAcc(corpBankAcc.getBankAcc());
                entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
            }

            //交易对手-账户信息
            cotpSearchVO.setCorpId(fexTranReq.getCotpId());
            cotpSearchVO.setOppCorpId(fexTranReq.getCorpId());
            cotpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_2);
            cotpSearchVO.setCurId(fexTranDeal.getCurId());
            cotpSearchVO.setCdSign(CommonConstant.NO);//收
            FexCorpBankAccDtlVO cotpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(cotpSearchVO);
            if(!EmptyUtils.isEmpty(cotpBankAcc)){
                entity.setCotpBank(cotpBankAcc.getBankType());
                entity.setCotpAccId(cotpBankAcc.getAccId());
                entity.setCotpAccName(cotpBankAcc.getAccName());
                entity.setCotpBankAcc(cotpBankAcc.getBankAcc());
            }
            entity.setRmk("交易"+fexTranReq.getBillNo()+"违约生成的现金流");
        }
        entity.setStatus(CommonConstant.STATUE_TEMP);
        entity.setPayStatus(ForeignConstant.PAY_STATUS_0);
        entity.setCreateBy(fexTranReq.getCreateBy());
        entity.setCreateDate(DateUtils.getDate());
        //违约和NDF交割
        if(ForeignConstant.OP_TYPE_2.equals(fexTranDeal.getOpType()) || ForeignConstant.OP_TYPE_3.equals(fexTranDeal.getOpType())){
            //原交易删除
            for(FexCashFlowVO fexCashFlow:listCashFlow){
                FexCashFlow cashFlow = fexCashFlowService.get(fexCashFlow.getId(), true);
                cashFlow.setStatus(CommonConstant.STATUE_DELETE);
                fexCashFlowService.saveOrUpdate(cashFlow);
            }
        }
        fexCashFlowService.saveOrUpdate(entity);
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的外汇买卖交易单据，不允许禁用！
            int num = this.fexTranDealDao.getTranDealCountByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANFOREIGN,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DISABLE_TRANFOREIGN_ERROR)));
            }
        }
        return null;
    }

}
