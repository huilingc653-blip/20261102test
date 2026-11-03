package com.bytter.biz.allocation.service.impl;
import com.bytter.biz.allocation.entity.NisAllocationReq;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.payment.api.dto.NisAllocationReqListDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.biz.payment.api.service.IBisGatherCmdBackService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.INotificationService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("allocationBisGatherCmdBackServiceImpl")
@Transactional
@DubboService
public class AllocationBisGatherCmdBackServiceImpl implements IBisGatherCmdBackService {
    @Autowired
    private NisAllocationReqService nisAllocationReqService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private NisMessageService nisMessageService;
    /**
     * 收款指令退回
     */
    @Override
    public void backBisGatherCmd(BisGatherCmdDTO bisGatherCmd, ICurrentUser user) {
        NisAllocationReq entity = null;
        if(StringUtils.isNotBlank(bisGatherCmd.getCbsBillId())){
            entity = this.nisAllocationReqService.get(bisGatherCmd.getCbsBillId());
        }
        if (!EmptyUtils.isEmpty(entity)) {
            NisAllocationReqListDTO vo = new NisAllocationReqListDTO();
            BeanUtils.copyNotNullProperties(entity, vo);
            vo.setStatus(AllocationConstant.STATUE_BACK);
            //指令退回，控制付款单原状态为95审批通过/100记账复核通过
            Integer[] oldStatus = { NetbankConstant.STATUE_CHECK, NetbankConstant.STATUE_TALLY_CHECKPASS };
            nisAllocationReqService.updateNisAllocationReqStatus(vo, oldStatus);
            //计划执行状态同步更新
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_ALLOCATION, BudgetConstant.PROCESS_SYSNAME_NIS,
                    entity.getBillNo(), AllocationConstant.STATUE_BACK, user);
            //生成日志
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_BACK, bisGatherCmd.getBackMsg(), NisMessageConstant.BUS_TYPE_ALLOCATION, user);
            //消息提醒
            String tag = nisAllocationReqService.getTag(entity.getType());
            if (!StringUtils.isBlank(tag)) {
                notificationService.sendToUserByLoginName(entity.getId(), entity.getCreateBy(), tag, entity);
            }
        }
    }
    /**
     * 收款指令退回
     */
    @Override
    public void defeatBisGatherCmd(BisGatherCmdDTO bisGatherCmd, ICurrentUser user) {
        NisAllocationReq entity = null;
        if(StringUtils.isNotBlank(bisGatherCmd.getCbsBillId())){
            entity = this.nisAllocationReqService.get(bisGatherCmd.getCbsBillId());
        }
        if (!EmptyUtils.isEmpty(entity)) {
            //生成日志
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
        }
    }
}
