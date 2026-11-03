package com.bytter.increment.domain.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.BankFinanceConstant;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.dto.BisFinanceExcDTO;
import com.bytter.increment.api.service.IBisFinanceExcBackService;
import com.bytter.increment.domain.entity.ChoBankFinance;
import com.bytter.increment.domain.service.ChoBankFinanceService;
import com.bytter.increment.domain.service.ChoMessageService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@DubboService
public class BisFinanceExcBackServiceImpl implements IBisFinanceExcBackService {

    @Autowired
    private ChoBankFinanceService choBankFinanceService;

    @Autowired
    private ChoMessageService choMessageService;
    @DubboReference(check = false)
    private INotificationService notificationService;

    /**
     * 指令退回
     */
    @Override
    public void backBisFinanceExc(BisFinanceExcDTO bisFinanceExc, ICurrentUser user) {
        ChoBankFinance entity = choBankFinanceService.get(bisFinanceExc.getCbsBillId(), true);
        entity.setStatus(BankFinanceConstant.STATUE_BACK);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        choBankFinanceService.saveOrUpdate(entity);
        //生成日志
        choMessageService.saveMessage(entity.getId(), ChoMessageConstant.MESSAGE_TYPE_EXC_BACK, bisFinanceExc.getBackMsg(), ChoMessageConstant.BUS_TYPE_BANK, user);
        String tag = "";
        if (BankFinanceConstant.BUS_TYPE_C2T.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_C2T;
        } else if (BankFinanceConstant.BUS_TYPE_C2N.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_T2C;
        } else if (BankFinanceConstant.BUS_TYPE_T2C.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_C2N;
        } else if (BankFinanceConstant.BUS_TYPE_N2C.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_N2C;
        }
        if (!StringUtils.isBlank(tag)) {
            //消息提醒
            notificationService.sendToUserByLoginName(entity.getId(), entity.getCreateBy(), tag, entity);
        }
    }
    /**
     * 指令作废
     */
    @Override
    public void defeatBisFinanceExc(BisFinanceExcDTO bisFinanceExc, ICurrentUser user) {
        ChoBankFinance entity = choBankFinanceService.get(bisFinanceExc.getCbsBillId(), true);
        entity.setStatus(BankFinanceConstant.STATUE_DEL);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        choBankFinanceService.saveOrUpdate(entity);
        //生成日志
        choMessageService.saveMessage(entity.getId(), ChoMessageConstant.MESSAGE_TYPE_EXC_REPEAL, "", ChoMessageConstant.BUS_TYPE_BANK, user);
    }
}
