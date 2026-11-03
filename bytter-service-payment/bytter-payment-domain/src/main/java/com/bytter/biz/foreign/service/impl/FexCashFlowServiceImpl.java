package com.bytter.biz.foreign.service.impl;

import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.FexMessageService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.BisAccDtlDTO;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.service.IBisForeignExcBackService;
import com.bytter.biz.payment.api.service.IFexForeignReqService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.INotificationService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huangx
 * @Description 现金流接口实现类
 * @date 2020/3/26
 */
@DubboService
@Transactional
@Service("fexCashFlowServiceImpl")
public class FexCashFlowServiceImpl implements  IFexForeignReqService,IBisForeignExcBackService {
    @Autowired
    private FexCashFlowService fexCashFlowService;

    @Autowired
    private FexMessageService fexMessageService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @Autowired(required = false)
    private IConfigService configService;

    @Autowired(required = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;

    @Autowired
    private FexTranReqService fexTranReqService;



    /**
     * 收款流水认领已确定时外汇修改
     * @param bisAccDtlDTO
     */
    @Override
    public void updateFexCashFlowForSure(BisAccDtlDTO bisAccDtlDTO) {
        FexCashFlow fexCashFlow = fexCashFlowService.get(bisAccDtlDTO.getBillId(), true);
        //现金流状态为暂存的，在认领确认后，现金流状态更新为已收款，并将流水中的发生额和交易时间（日期）回写到现金流中的实际收款金额和实际收款日期
        //modify 增加状态为打回的
        if(fexCashFlow.getStatus().equals(ForeignConstant.STATUE_TEMP)||
                fexCashFlow.getStatus().equals(ForeignConstant.STATUE_BACK)){
            fexCashFlow.setOldStatus(fexCashFlow.getStatus());
            fexCashFlow.setStatus(ForeignConstant.STATUE_CHECK);
            fexCashFlow.setFactsMoney(bisAccDtlDTO.getAmt());
            fexCashFlow.setFactsDate(bisAccDtlDTO.getTransTime());
            fexCashFlow.setPayStatus(ForeignConstant.PAY_STATUS_1);
        }
        fexCashFlow.setBankFlowSign(ForeignConstant.BANK_FLOW_SIGN_1);
        fexCashFlowService.updateEntity(fexCashFlow);

    }

    /**
     * 收款流水认领取消时外汇修改
     * @param bisAccDtlDTO
     */
    @Override
    public void updateFexCashFlowForCancel(BisAccDtlDTO bisAccDtlDTO) {
        FexCashFlow fexCashFlow = fexCashFlowService.get(bisAccDtlDTO.getBillId(), true);
        fexCashFlow.setBankFlowSign(ForeignConstant.BANK_FLOW_SIGN_0);
        if(!EmptyUtils.isEmpty(fexCashFlow.getOldStatus())) {
            fexCashFlow.setStatus(fexCashFlow.getOldStatus());
            fexCashFlow.setPayStatus(ForeignConstant.PAY_STATUS_0);
        }
        fexCashFlowService.updateEntity(fexCashFlow);
    }

    @Override
    public String getFexCashFlowBillNo(String id) {
        FexCashFlow fexCashFlow = fexCashFlowService.get(id, true);
        String billNo = "";
        if(!EmptyUtils.isEmpty(fexCashFlow)){
            billNo = fexCashFlow.getBillNo();
        }
        return billNo;
    }


    /**
     * 指令退回
     *
     * @param bisForeignExc
     * @param user
     */
    @Override
    public void backBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user) {
        if (ForeignConstant.EXC_SOURCE_REQ.equals(bisForeignExc.getExcSource())) {
            // 外汇买卖交易，预测现金流，不关联指令
            FexTranReq fexTranReq = fexTranReqService.get(bisForeignExc.getCbsBillId(), true);
            fexTranReq.setStatus(NetbankConstant.STATUE_BACK);
            fexTranReq.setUpdateBy(user.getLoginName());
            fexTranReq.setUpdateDate(DateUtils.getDate());
            fexTranReqService.updateEntity(fexTranReq);
            //生成日志
            fexMessageService.saveMessage(fexTranReq.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_BACK, bisForeignExc.getBackMsg(), FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
        } else {
            FexCashFlowVO fexCashFlowVO = fexCashFlowService.getByIdUnion(bisForeignExc.getCbsBillId());
            if (fexCashFlowVO != null) {
                List<FexCashFlow> listFexCashFlow = fexCashFlowService.listByTranBillId(fexCashFlowVO.getTranBillId());
                String firstId = "";
                for (FexCashFlow fexCashFlow : listFexCashFlow) {
                    //过滤删除状态 和 已退款状态 的数据
                    if (NetbankConstant.STATUE_DEL.toString().equals(fexCashFlow.getStatus().toString())
                            || ForeignConstant.PAY_STATUS_REFUNDED.equals(fexCashFlow.getPayStatus())) {
                        continue;
                    }
                    fexCashFlow.setStatus(NetbankConstant.STATUE_BACK);
                    fexCashFlow.setPayStatus(CommonConstant.NO);
                    fexCashFlow.setUpdateBy(user.getLoginName());
                    fexCashFlow.setUpdateDate(DateUtils.getDate());
                    fexCashFlowService.updateEntity(fexCashFlow);
                    //生成日志
                    fexMessageService.saveMessage(fexCashFlow.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_BACK, bisForeignExc.getBackMsg(), FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, user);
                    if (StringUtils.isBlank(firstId)) {
                        firstId = fexCashFlow.getId();
                    }
                }

                //是否跳过结算执行
                String isSkipSettle = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISSKIPSETTLE).getValue();
                if (CommonConstant.YES.equals(isSkipSettle)) {
                    fexCashFlowService.batchBack(firstId, user);
                }
            }
        }
    }

    /**
     * 指令作废
     *
     * @param bisForeignExc
     * @param user
     */
    @Override
    public void defeatBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user) {
        if (ForeignConstant.EXC_SOURCE_REQ.equals(bisForeignExc.getExcSource())) {
            // 外汇买卖交易，预测现金流，不关联指令
            FexTranReq fexTranReq = fexTranReqService.get(bisForeignExc.getCbsBillId(), true);
            //生成日志
            fexMessageService.saveMessage(fexTranReq.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
            //释放资金计划
            fbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, fexTranReq.getId());
        } else {
            FexCashFlow fexCashFlow = fexCashFlowService.get(bisForeignExc.getCbsBillId(), true);
            //生成日志
            fexMessageService.saveMessage(fexCashFlow.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, user);
            //释放资金计划
            fbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, fexCashFlow.getTranBillId());
        }
    }
}
