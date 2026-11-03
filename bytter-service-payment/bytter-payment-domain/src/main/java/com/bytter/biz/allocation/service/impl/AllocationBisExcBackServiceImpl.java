package com.bytter.biz.allocation.service.impl;
import com.bytter.biz.allocation.entity.NisAllocationReq;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.payment.api.dto.NisAllocationReqListDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcBackService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("allocationBisExcBackService")
@Transactional
@Slf4j
@DubboService(group="nisAllocationReq")
public class AllocationBisExcBackServiceImpl implements IBisExcBackService {
    @Autowired
    private NisAllocationReqService nisAllocationReqService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private NisMessageService nisMessageService;
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 付款指令退回
     */
    @Override
    public void backBisExc(BisExcDTO bisExc, ICurrentUser user) {
        NisAllocationReq entity = null;
        if(StringUtils.isNotBlank(bisExc.getCbsBillId())){
            entity = this.nisAllocationReqService.get(bisExc.getCbsBillId());
        }
        if (!EmptyUtils.isEmpty(entity)) {
            NisAllocationReqListDTO vo = new NisAllocationReqListDTO();
            BeanUtils.copyNotNullProperties(entity, vo);
            if (CommonConstant.YES.equals(bisExc.getBackNode())) {
                vo.setStatus(AllocationConstant.STATUE_CHECK);
            } else {
                vo.setStatus(AllocationConstant.STATUE_BACK);
            }
            //清空记账标志
            vo.setTallySign("");
            //指令退回，控制付款单原状态为95审批通过/100记账复核通过
            Integer[] oldStatus = { NetbankConstant.STATUE_CHECK, NetbankConstant.STATUE_TALLY_CHECKPASS };
            nisAllocationReqService.updateNisAllocationReqStatus(vo, oldStatus);
            String tag = nisAllocationReqService.getTag(entity.getType());
            if (!StringUtils.isBlank(tag)) {
                //消息提醒
                notificationService.sendToUserByLoginName(entity.getId(), entity.getCreateBy(), tag, entity);
            }
            //计划执行状态同步更新
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_ALLOCATION, BudgetConstant.PROCESS_SYSNAME_NIS,
                    entity.getBillNo(), AllocationConstant.STATUE_BACK, user);
            //生成日志
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_BACK, bisExc.getBackMsg(), NisMessageConstant.BUS_TYPE_ALLOCATION, user);
        }
    }
    /**
     * 付款指令作废
     */
    @Override
    public void defeatBisExc(BisExcDTO bisExc, ICurrentUser user) {
        NisAllocationReq entity = null;
        if(StringUtils.isNotBlank(bisExc.getCbsBillId())){
            entity = this.nisAllocationReqService.get(bisExc.getCbsBillId());
        }
        if (!EmptyUtils.isEmpty(entity)) {
            //生成日志
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
            //释放资金计划
            this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_ALLOCATION, BudgetConstant.PROCESS_SYSNAME_NIS, entity.getBillNo());
        }
    }
    /**
     * 付款指令发送
     */
    @Override
    public void sendBisExcAccounting(BisExcDTO bisExc, ICurrentUser user) {
        NisAllocationReq entity = this.nisAllocationReqService.get(bisExc.getCbsBillId(),true);
        log.error("自动记账方法");
        //非资金池调拨才判断记账
        if(!AllocationConstant.POOL_SIGN_1.equals(entity.getPoolSign())) {
            //判断资金调拨是否需要记账(头寸调拨单据来源是银行借款的话，是否记账是根据银行借款，借款管理是否记账参数来确定)
            String isAccount = "";
            if (AllocationConstant.BILL_FROM_2.equals(entity.getBillFrom())) {
                isAccount = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_TALLY).getValue();
            } else {
                isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
            }
            //头寸调拨是否记账
            String isAccountPos = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue();
            if ((CommonConstant.YES.equals(isAccount) && !AllocationConstant.ALLOCATION_TYPE_POS.equals(entity.getType()))
                    || (CommonConstant.YES.equals(isAccountPos) && AllocationConstant.ALLOCATION_TYPE_POS.equals(entity.getType()))) {
                String isAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue();
                if (CommonConstant.YES.equals(isAutoAccount)) { // 手动记账则不会往下走，也是打破核算和结算循环的一个点
                    //自动记账
                    log.error("自动记账入口");
                    nisAllocationReqService.submitVoucher(entity, user);  //里面有调用核算 TODO 注意 bisExc的voucherNo 和 entity的voucherNo 不是同一个意思
                }
            }
        }
    }
    /**
     * 付款指令重发
     */
    @Override
    public void resendBisExc(BisExcDTO bisExc, ICurrentUser user) {
        //资金调拨无需处理
    }
    /**
     * 付款指令支付状态更新
     */
    @Override
    public void setPayResultBisExc(BisExcDTO bisExc, ICurrentUser user) {
        NisAllocationReq entity = null;
        if(StringUtils.isNotBlank(bisExc.getCbsBillId())){
            entity = this.nisAllocationReqService.get(bisExc.getCbsBillId());
        }
        if (!EmptyUtils.isEmpty(entity)) {
            //生成日志
            Integer messageType = BdlinkConstant.SUCCESS.equals(bisExc.getVoucherStat()) ? NisMessageConstant.MESSAGE_TYPE_PAY_SUCCESS : NisMessageConstant.MESSAGE_TYPE_PAY_FAILURE;
            nisMessageService.saveMessage(entity.getId(), messageType, bisExc.getHandleOpinion(), NisMessageConstant.BUS_TYPE_ALLOCATION, user);
        }
    }
    /**
     * 付款指令查询结果
     */
    @Override
    public void queryResultBisExc(BisExcDTO bisExc, ICurrentUser user) {
    }
    
	@Override
	public void checkSendBisExcAccounting(BisExcDTO bisExc, ICurrentUser user) {

        NisAllocationReq entity = this.nisAllocationReqService.get(bisExc.getCbsBillId(),true);
        log.error("自动记账方法");
        //非资金池调拨才判断记账
        if(!AllocationConstant.POOL_SIGN_1.equals(entity.getPoolSign())) {
            //判断资金调拨是否需要记账(头寸调拨单据来源是银行借款的话，是否记账是根据银行借款，借款管理是否记账参数来确定)
            String isAccount = "";
            if (AllocationConstant.BILL_FROM_2.equals(entity.getBillFrom())) {
                isAccount = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_TALLY).getValue();
            } else {
                isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
            }
            //头寸调拨是否记账
            String isAccountPos = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue();
            if ((CommonConstant.YES.equals(isAccount) && !AllocationConstant.ALLOCATION_TYPE_POS.equals(entity.getType()))
                    || (CommonConstant.YES.equals(isAccountPos) && AllocationConstant.ALLOCATION_TYPE_POS.equals(entity.getType()))) {
                String isAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue();
                if (CommonConstant.YES.equals(isAutoAccount)) { // 手动记账则不会往下走，也是打破核算和结算循环的一个点
                    //自动记账
                    log.error("自动记账入口");
                    nisAllocationReqService.checkSubmitVoucher(entity, user);  //里面有调用核算 TODO 注意 bisExc的voucherNo 和 entity的voucherNo 不是同一个意思
                }
            }
        }
    
		
	}
}
