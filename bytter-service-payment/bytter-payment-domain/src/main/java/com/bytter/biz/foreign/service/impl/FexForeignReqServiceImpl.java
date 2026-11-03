package com.bytter.biz.foreign.service.impl;

import com.bytter.biz.foreign.entity.FexForeignReq;
import com.bytter.biz.foreign.service.FexForeignReqService;
import com.bytter.biz.foreign.service.FexMessageService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.service.IBisForeignExcBackService;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.system.api.INotificationService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author huangx
 * @Description 外币汇款单接口实现类
 * @date 2019/7/19
 */
@Service("fexForeignReqServiceImpl")
@Transactional
public class FexForeignReqServiceImpl implements IBisForeignExcBackService {
    @Autowired
    private FexForeignReqService fexForeignReqService;

    @Autowired
    private FexMessageService fexMessageService;
    
    @Autowired(required=false)
    private IFbsBudgetProcessService fbsBudgetProcessService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    /**
     * 指令退回
     * @param bisForeignExc
     * @param user
     */
    @Override
    public void backBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user) {
        FexForeignReq fexForeignReq = fexForeignReqService.get(bisForeignExc.getCbsBillId(), true);
        fexForeignReq.setStatus(NetbankConstant.STATUE_BACK);
        fexForeignReq.setUpdateBy(user.getLoginName());
        fexForeignReq.setUpdateDate(DateUtils.getDate());
        fexForeignReqService.saveOrUpdate(fexForeignReq);
        //计划执行状态同步更新
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_FOREIGN, BudgetConstant.PROCESS_SYSNAME_FEX,
                fexForeignReq.getBillNo(), ForeignConstant.STATUE_BACK, user);
        //生成日志
        fexMessageService.saveMessage(fexForeignReq.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_BACK, bisForeignExc.getBackMsg(), FexMessageConstant.BUS_TYPE_FOREIGN, user);
    }

    /**
     * 指令作废
     * @param bisForeignExc
     * @param user
     */
    @Override
    public void defeatBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user) {
        FexForeignReq fexForeignReq = fexForeignReqService.get(bisForeignExc.getCbsBillId(), true);
        //生成日志
        fexMessageService.saveMessage(fexForeignReq.getId(), FexMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", FexMessageConstant.BUS_TYPE_FOREIGN, user);
        //释放资金计划
        fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_FOREIGN, BudgetConstant.PROCESS_SYSNAME_FEX, fexForeignReq.getBillNo());
    }
}
