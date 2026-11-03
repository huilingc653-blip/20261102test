package com.bytter.biz.netbank.service.impl;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.netbank.vo.NisBillheadVO;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.IBisExcBackService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author huangx
 */
@Service("billheadBisExcBackService")
@Transactional
@DubboService(group="nisBillhead")
public class BillheadBisExcBackServiceImpl implements IBisExcBackService {
    @Autowired
    private NisBillheadService nisBillheadService;
    @Autowired
    private NisExpenseDetailService nisExpenseDetailService;
    @Autowired
    private NisMessageService nisMessageService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    /**
     * 付款指令发送
     */
    @Override
    public void sendBisExcAccounting(BisExcDTO bisExc, ICurrentUser user) {
        NisBillhead entity = nisBillheadService.get(bisExc.getCbsBillId());
        if (NetbankConstant.DIRECTPAYFLAG_N.equals(entity.getDirectPayFlag())) {
            //判断是自动记账还是手工记账
            if (CommonConstant.YES.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISAUTOACCOUNT).getValue())) {
                //自动记账
                // todo:数据转换
                BisExcDTO bisExcDTO = BeanUtils.copyNewInstanceProperties(bisExc, BisExcDTO.class);
                nisBillheadService.submitVoucher(entity, bisExcDTO, user);
            }
        }
    }
    /**
     * 指令退回
     */
    @Override
    public void backBisExc(BisExcDTO bisExc, ICurrentUser user) {
        NisBillhead entity = nisBillheadService.get(bisExc.getCbsBillId(), true);
        NisBillheadVO nisBillhead = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(entity, nisBillhead);
        if (CommonConstant.YES.equals(bisExc.getBackNode())) {
            nisBillhead.setStatus(NetbankConstant.STATUE_CHECK);
        } else {
            nisBillhead.setStatus(NetbankConstant.STATUE_BACK);
        }
        //清空记账标志
        nisBillhead.setTallySign("");
        nisBillhead.setUpdateBy(user.getLoginName());
        nisBillhead.setUpdateDate(DateUtils.getDate());
        //清空凭证号
        nisBillhead.setVoucherNo("");
        nisBillhead.setOrderNo("");
        //指令退回，控制付款单原状态为95审批通过/100记账复核通过/104记账作废复核通过
        Integer[] oldStatus = { NetbankConstant.STATUE_CHECK, NetbankConstant.STATUE_TALLY_CHECKPASS, NetbankConstant.STATUE_TALLY_INVALID_CHECKPASS };
        nisBillheadService.updateNisBillheadStatus(nisBillhead, oldStatus);
        if (NetbankConstant.STATUE_BACK.equals(nisBillhead.getStatus())
                && !NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
            // 付款办理指令退回到制单处后仍释放；费用报销保留占用，
            // 由费用报销处理的删除/打回动作统一释放或缩减。指定节点退回仍保持95，不释放。
            nisBillheadService.releaseEnterpriseLimitForBillhead(entity.getId(), user);
        }
        //计划执行状态同步更新
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,
                nisBillhead.getBillNo(), NetbankConstant.STATUE_BACK, user);
        //生成日志
        nisMessageService.saveMessage(nisBillhead.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_BACK, bisExc.getBackMsg(), NisMessageConstant.BUS_TYPE_PAY, user);
        String tag = NotificationConstant.NETBANK.PAY_APPLY;
        if (NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
            tag = NotificationConstant.NETBANK.EXPENSE_DEAL;
        }
        if (!StringUtils.isBlank(tag)) {
            //消息提醒
            notificationService.sendToUserByLoginName(nisBillhead.getId(), nisBillhead.getCreateBy(), tag, nisBillhead);
        }
    }
    /**
     * 指令作废
     */
    @Override
    public void defeatBisExc(BisExcDTO bisExc, ICurrentUser user) {
        NisBillhead nisBillhead = nisBillheadService.get(bisExc.getCbsBillId(), true);
        //生成日志
        nisMessageService.saveMessage(nisBillhead.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", NisMessageConstant.BUS_TYPE_PAY, user);
        //释放资金计划
        fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo());
    }
    /**
     * 付款指令重发
     */
    @Override
    public void resendBisExc(BisExcDTO bisExc, ICurrentUser user) {
        NisBillhead nisBillhead = nisBillheadService.get(bisExc.getCbsBillId(), true);
        //对私付款
        if(NetbankConstant.FORINDIVIDUAL_1.equals(nisBillhead.getIsForindividual())){
            //对私付款，支付失败后重发，需要将交易失败的明细状态更新为3-准备发送
            int result = this.nisExpenseDetailService.updateVoucherStatByBillId(nisBillhead.getId());
            if(result<1){
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
            }
            //获取失败的明细，更新明细的交易状态为准备发送
            /*List<NisExpenseDetailVO> list = nisExpenseDetailService.listByBillIdFailure(nisBillhead.getId());
            for(NisExpenseDetailVO vo : list){
                NisExpenseDetail entity = new NisExpenseDetail();
                BeanUtils.copyNotNullProperties(vo, entity);
                //准备发送状态
                vo.setVoucherStat(BdlinkConstant.PRE_SEND);
                nisExpenseDetailService.saveOrUpdate(entity);
            }*/
        }
    }
    /**
     * 付款指令支付状态更新
     */
    @Override
    public void setPayResultBisExc(BisExcDTO bisExc, ICurrentUser user) {
        NisBillhead nisBillhead = nisBillheadService.get(bisExc.getCbsBillId(), true);
        if (!EmptyUtils.isEmpty(nisBillhead)) {
            //对私付款
            if(NetbankConstant.FORINDIVIDUAL_1.equals(nisBillhead.getIsForindividual())) {
                if(BdlinkConstant.BANK_FAILED.equals(bisExc.getVoucherStat())
                        && NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())){
                    //费用报销更新
                    if(StringUtils.isNotBlank(bisExc.getExpenseDtlIds())) {
                        //勾选的明细更新为失败
                        this.nisExpenseDetailService.updateVoucherStatByBillId(nisBillhead.getId(), bisExc.getVoucherStat(), bisExc.getExpenseDtlIds(), true);
                        //剩余的明细更新为成功
                        this.nisExpenseDetailService.updateVoucherStatByBillId(nisBillhead.getId(), BdlinkConstant.SUCCESS, bisExc.getExpenseDtlIds(), false);
                    }
                }else {
                    //单笔对私付款，同步更新等待结果的明细状态为成功/失败
                    this.nisExpenseDetailService.updateVoucherStatByBillId(nisBillhead.getId(), bisExc.getVoucherStat());
                }
            }
            //生成日志
            Integer messageType = BdlinkConstant.SUCCESS.equals(bisExc.getVoucherStat()) ? NisMessageConstant.MESSAGE_TYPE_PAY_SUCCESS : NisMessageConstant.MESSAGE_TYPE_PAY_FAILURE;
            nisMessageService.saveMessage(nisBillhead.getId(), messageType, bisExc.getHandleOpinion(), NisMessageConstant.BUS_TYPE_PAY, user);
        }
    }
    /**
     * 付款指令查询结果
     */
    @Override
    public void queryResultBisExc(BisExcDTO bisExc, ICurrentUser user) {
        NisBillhead nisBillhead = nisBillheadService.get(bisExc.getCbsBillId(), true);
        if (!EmptyUtils.isEmpty(nisBillhead)) {
            //对私付款
            if(NetbankConstant.FORINDIVIDUAL_1.equals(nisBillhead.getIsForindividual())) {
                //对私付款，支付失败后重新查询结果，需要将交易失败的明细状态更新为1-等待结果
                int result = this.nisExpenseDetailService.updateVoucherStatByBillIdQueryResult(nisBillhead.getId());
                if (result < 1) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
                }
            }
        }
    }
	@Override
	public void checkSendBisExcAccounting(BisExcDTO bisExc, ICurrentUser user) {
		// TODO Auto-generated method stub
		
	}
}
