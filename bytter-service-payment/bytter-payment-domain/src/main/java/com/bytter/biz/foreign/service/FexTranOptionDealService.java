package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranOptionDealDao;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexProduct;
import com.bytter.biz.foreign.entity.FexTranOptionDeal;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.biz.foreign.vo.FexTranOptionDealVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.payment.api.constant.BisAccBalConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
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
@Service("fexTranOptionDealService")
@Transactional
public class FexTranOptionDealService extends BaseService<FexTranOptionDeal, String> {

    @Autowired
    private FexTranOptionDealDao fexTranOptionDealDao;
    @Autowired
    private FexMessageService fexMessageService;
    @Autowired
    private FexTranReqService fexTranReqService;
    @Autowired
    private FexCashFlowService fexCashFlowService;
    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;
    @Autowired(required=false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private FexProductService fexProductService;

    @Override
    public BaseDao<FexTranOptionDeal, String> getEntityDao() {
        return fexTranOptionDealDao;
    }

    /**
     * 根据ID获取交易信息
     * @param id
     * @return
     */
    public FexTranOptionDealVO getByIdUnion(String id){
        return fexTranOptionDealDao.getByIdUnion(id);
    }

    /**
     * 根据交易ID获取交易管理处理信息
     * @param opType
     * @param tranReqId
     * @return
     */
    public FexTranOptionDealVO getByTranReqIdUnion(String opType, String tranReqId){
        return fexTranOptionDealDao.getByTranReqIdUnion(opType, tranReqId);
    }

    /**
     * 根据交易ID获取交易管理处理信息
     * @param tranReqId
     * @return
     */
    public List<FexTranOptionDealVO> listVOByTranReqId(String tranReqId){
        return fexTranOptionDealDao.listVOByTranReqId(tranReqId);
    }

    /**
     * 保存交易交易管理处理信息
     *
     * @param fexTranOptionDeal
     */
    public void saveFexTranOptionDeal(FexTranOptionDealVO fexTranOptionDeal, ICurrentUser user) {
        FexTranOptionDeal entity = new FexTranOptionDeal();
        BeanUtils.copyNotNullProperties(fexTranOptionDeal, entity);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        this.saveEntity(entity);
        /**操作类型 1-失效，2-已确认均价，3-行权，4-敲出，5-敲入*/
        FexTranReq fexTranReq = this.fexTranReqService.get(fexTranOptionDeal.getTranReqId(), true);
        if(ForeignConstant.OPTION_OP_TYPE_1.equals(fexTranOptionDeal.getOpType())){
            fexTranReq.setStatus(ForeignConstant.STATUE_INVALID);
        }else if(ForeignConstant.OPTION_OP_TYPE_2.equals(fexTranOptionDeal.getOpType())){
            fexTranReq.setStatus(ForeignConstant.STATUE_ENTERAVG);
        }else if(ForeignConstant.OPTION_OP_TYPE_3.equals(fexTranOptionDeal.getOpType())){
            fexTranReq.setStatus(ForeignConstant.STATUE_EXERCISE);
        }else if(ForeignConstant.OPTION_OP_TYPE_4.equals(fexTranOptionDeal.getOpType())){
            fexTranReq.setStatus(ForeignConstant.STATUE_KNOCKOUT);
        }else if(ForeignConstant.OPTION_OP_TYPE_5.equals(fexTranOptionDeal.getOpType())){
            fexTranReq.setStatus(ForeignConstant.STATUE_KNOCKIN);
        }
        fexTranReq.setUpdateBy(user.getLoginName());
        fexTranReq.setUpdateDate(DateUtils.getDate());
        this.fexTranReqService.saveOrUpdate(fexTranReq);

        Integer messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        if(user !=null) {
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_OPTION_DEAL, user);
        }

        if(ForeignConstant.OPTION_OP_TYPE_3.equals(fexTranOptionDeal.getOpType())){
            //行权
            //结算方式 0-现金、0-即期交易
            if(ForeignConstant.TRAN_SETTLE_METHOD_0.equals(fexTranOptionDeal.getTranSettleMethod())){
                //结算金额小于0时，表付款，才执行资金计划
                if(fexTranOptionDeal.getSettleMoney().compareTo(BigDecimal.ZERO)<0) {
                    //写入计划执行
                    fexTranOptionDeal.setId(entity.getId());
                    this.writeBudgetProcess(fexTranOptionDeal, user);
                }
                //结算金额不等于0才生成现金流
                if(fexTranOptionDeal.getSettleMoney().compareTo(BigDecimal.ZERO) != 0) {
                    //生成现金流
                    this.saveFexCashFLow(entity);
                }
            }else {
                //生成暂存状态的外汇买卖交易
                FexTranReq foreign = new FexTranReq();
                BeanUtils.copyNotNullProperties(fexTranReq, foreign);
                foreign.setId("");
                foreign.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_REQ));
                foreign.setTranType(ForeignConstant.TRAN_TYPE_1);
                foreign.setIsSwapTran(CommonConstant.NO);
                foreign.setCorpId(entity.getCorpId());
                foreign.setCotpId(entity.getCotpId());
                foreign.setProId(entity.getProId());
                FexProduct product = fexProductService.get(entity.getProId(), true);
                foreign.setFexBusType(product.getFexBusType());
                foreign.setFexTranType(product.getFexTranType());
                foreign.setDeliveryType(product.getDeliveryType());
                foreign.setDeliveryCurId(product.getDeliveryCurId());
                foreign.setTranDate(entity.getOpDate());
                foreign.setSettleDate(entity.getSettleDate());
                foreign.setDeliveryDeadline(DateUtils.dayDiffs(entity.getSettleDate(), entity.getOpDate()) + "");
                foreign.setStatus(ForeignConstant.STATUE_TEMP);
                foreign.setCreateBy(user.getLoginName());
                foreign.setCreateDate(DateUtils.getDate());
                foreign.setAttachmentId(null);
                foreign.setTranGroup(null);
                foreign.setTranAppDtlId(entity.getId());
                fexTranReqService.saveEntity(foreign);
            }
        }else if(ForeignConstant.OPTION_OP_TYPE_4.equals(fexTranOptionDeal.getOpType())){
            //敲出 涉及退款产生现金流
            if(CommonConstant.YES.equals(fexTranReq.getIsRefund())){
                //生成现金流
                this.saveFexCashFLow(entity);
            }
        }
    }

    /**
     * 写入计划执行
     *
     * @param fexTranOptionDeal
     */
    private void writeBudgetProcess(FexTranOptionDealVO fexTranOptionDeal, ICurrentUser user) {
        //写入之前先删除
        fbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_TRANEXERCISE, BudgetConstant.PROCESS_SYSNAME_FEX, fexTranOptionDeal.getId());
        List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
        if(fexTranOptionDeal.getItemCode() != null) {
            for (int i = 0; i < fexTranOptionDeal.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(fexTranOptionDeal, user);
                fbsBudgetProcessDTO.setItemCode(fexTranOptionDeal.getItemCode()[i]);
                fbsBudgetProcessDTO.setItemName(fexTranOptionDeal.getItemName()[i]);
                fbsBudgetProcessDTO.setProcessMoney(fexTranOptionDeal.getProcessMoney()[i]);
                fbsBudgetProcessDTO.setCurId(fexTranOptionDeal.getCurId());
                if (fexTranOptionDeal.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(fexTranOptionDeal.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(fexTranOptionDeal.getProcessCurMoney()[i]);
                }
                list.add(fbsBudgetProcessDTO);
            }
        }
        this.fbsBudgetProcessService.writeBudgetProcess(list, user);
    }
    /**
     * 构建计划执行
     *
     * @param fexTranOptionDeal
     * @return
     */
    private FbsBudgetProcessDTO bulidFbsBudgetProcessDTO(FexTranOptionDealVO fexTranOptionDeal, ICurrentUser user) {
        FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
        fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
        fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
        fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setStatus(ForeignConstant.STATUE_CHECK);
        fbsBudgetProcessDTO.setBillId(fexTranOptionDeal.getId());
        fbsBudgetProcessDTO.setProcessDate(fexTranOptionDeal.getOpDate());
        fbsBudgetProcessDTO.setCorpId(fexTranOptionDeal.getCorpId());
        fbsBudgetProcessDTO.setCurId(fexTranOptionDeal.getCurId());
        fbsBudgetProcessDTO.setSettlType("1");
        fbsBudgetProcessDTO.setPurpose(fexTranOptionDeal.getRmk());
        fbsBudgetProcessDTO.setWishPayday(fexTranOptionDeal.getOpDate());
        fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_TRANEXERCISE);
        fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_FEX);
        return fbsBudgetProcessDTO;
    }

    /**
     * 现金流生成
     * @param fexTranOptionDeal
     */
    public void saveFexCashFLow(FexTranOptionDeal fexTranOptionDeal) {
        //获取交易
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(fexTranOptionDeal.getTranReqId());
        FexCashFlow entity = new FexCashFlow();
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_CASH_FLOW));
        entity.setTranBillId(fexTranOptionDeal.getTranReqId());
        entity.setCashFlowDate(DateUtils.getDate());
        //结算类型：1-全额，2-净额
        entity.setSettleType(CommonConstant.YES);
        if(ForeignConstant.OPTION_OP_TYPE_3.equals(fexTranOptionDeal.getOpType())){
            //行权
            entity.setSettleDate(fexTranOptionDeal.getSettleDate());//行权的结算日期
            entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);//本金
            entity.setCurId(fexTranOptionDeal.getCurId());//行权的结算币种
            entity.setPayMoney(fexTranOptionDeal.getSettleMoney().abs());//行权的结算结算金额（按正负区分收付款）
            if(fexTranOptionDeal.getSettleMoney().compareTo(BigDecimal.ZERO)>0){
                entity.setCdSign(CommonConstant.NO);
            }else{
                entity.setCdSign(CommonConstant.YES);
            }
        }else {
            //退款
            entity.setSettleDate(fexTranOptionDeal.getRefundDate());//退款日期
            entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_4);//退款
            entity.setCurId(fexTranReq.getCurId());//结算币种
            entity.setPayMoney(fexTranOptionDeal.getRefundMoney());
            //期权退款由卖方支付给买方：买入期权为收入，卖出期权为支出
            entity.setCdSign(ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReq.getBusDirection())
                    ? BisAccBalConstant.CDSIGN_IN : BisAccBalConstant.CDSIGN_OUT);
        }
        //外汇买卖交易
        entity.setTranType(fexTranReq.getTranType());
        entity.setCorpId(fexTranReq.getCorpId());
        entity.setCotpId(fexTranReq.getCotpId());

        //获取主体单位账户
        FexCorpBankAccVO corpSearchVO = new FexCorpBankAccVO();
        corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
        corpSearchVO.setTranType(fexTranReq.getTranType());
        corpSearchVO.setProId(fexTranReq.getProId());
        corpSearchVO.setCashFlowType(entity.getCashFlowType());
        corpSearchVO.setCurId(entity.getCurId());
        corpSearchVO.setCdSign(entity.getCdSign());
        //获取交易对手账户
        FexCorpBankAccVO cotpSearchVO = new FexCorpBankAccVO();
        cotpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_COTP);
        cotpSearchVO.setTranType(fexTranReq.getTranType());
        cotpSearchVO.setProId(fexTranReq.getProId());
        cotpSearchVO.setCashFlowType(entity.getCashFlowType());
        cotpSearchVO.setCurId(entity.getCurId());
        if(CommonConstant.YES.equals(entity.getCdSign())){
            cotpSearchVO.setCdSign(CommonConstant.NO);
        }else {
            cotpSearchVO.setCdSign(CommonConstant.YES);
        }
        //单位-账户信息
        FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
        if(!EmptyUtils.isEmpty(corpBankAcc)){
            entity.setCorpAccId(corpBankAcc.getAccId());
            entity.setCorpBank(corpBankAcc.getBankType());
            entity.setCorpAccName(corpBankAcc.getAccName());
            entity.setCorpBankAcc(corpBankAcc.getBankAcc());
            entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
        }

        //交易对手-账户信息
        FexCorpBankAccDtlVO cotpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(cotpSearchVO);
        if(!EmptyUtils.isEmpty(cotpBankAcc)){
            entity.setCotpBank(cotpBankAcc.getBankType());
            entity.setCotpAccId(cotpBankAcc.getAccId());
            entity.setCotpAccName(cotpBankAcc.getAccName());
            entity.setCotpBankAcc(cotpBankAcc.getBankAcc());
        }
        entity.setStatus(CommonConstant.STATUE_TEMP);
        entity.setPayStatus(ForeignConstant.PAY_STATUS_0);
        entity.setCreateBy(fexTranReq.getCreateBy());
        entity.setCreateDate(DateUtils.getDate());
        fexCashFlowService.saveOrUpdate(entity);
    }
}
