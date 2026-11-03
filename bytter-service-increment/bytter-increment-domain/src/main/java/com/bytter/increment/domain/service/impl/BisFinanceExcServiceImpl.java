package com.bytter.increment.domain.service.impl;

import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.increment.api.dto.BisFinanceExcDTO;
import com.bytter.increment.api.service.IBisFinanceExcService;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.increment.domain.entity.BisFinanceExc;
import com.bytter.increment.domain.service.BisFinanceExcService;
import com.bytter.increment.domain.service.ChoMessageService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 理财指令
 * @date 2017-08-23 11:08:59
 */
@Service("bisFinanceExcServiceImpl")
@Transactional
@DubboService
public class BisFinanceExcServiceImpl implements IBisFinanceExcService {
    @Autowired
    private BisFinanceExcService bisFinanceExcService;
    @DubboReference(check = false)
	private INotificationService notificationService;

    @Autowired
    private ChoMessageService choMessageService;
    /**
     * 新增理财指令
     */
    @Override
    public void addBisFinanceExc(BisFinanceExcDTO bisFinanceExc, ICurrentUser user) {
        //根据业务Id获取指令
        BisFinanceExc oldExc = bisFinanceExcService.getByCbsBillId(bisFinanceExc.getCbsBillId());
        if (oldExc != null) {
            //指令状态为B-指令退回或者7-指令无效
            if (BdlinkConstant.BACK.equals(oldExc.getVoucherStat()) || BdlinkConstant.INVALIDATE.equals(oldExc.getVoucherStat())) {
                int result = bisFinanceExcService.updateBisFinanceExcCbsBillNo(oldExc);
                if(result<1) {
                    throw new ParameterException(NetbankMessageEnum.NIS_BISEXC_MESSAGE_EXIST);
                }
            } else {
                throw new ParameterException(NetbankMessageEnum.NIS_BISEXC_MESSAGE_EXIST);
            }
        }
        BisFinanceExc entity = new BisFinanceExc();
        BeanUtils.copyNotNullProperties(bisFinanceExc, entity);
        entity.setVoucherStat(BdlinkConstant.WAIT_FILL);
        //流水ID可能过长
        String voucherNo=SequenceSingleton.getSequence(SequenceEnum.BIS_FINANCE_EXC);
        if(voucherNo.length()>12){
            entity.setVoucherNo(voucherNo.substring(0,12));
        }else {
            entity.setVoucherNo(voucherNo);
        }
        entity.setNoticeSign(CommonConstant.SEND_SIGN_NO);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        bisFinanceExcService.save(entity);
        //日志记录
        choMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_AUTO_ADD, "", NisMessageConstant.BUS_TYPE_BDLINKFINANCE, user.getLoginName(), user.getUsername());
        //消息提醒
        notificationService.send(entity.getId(), entity.getCorpId(), NotificationConstant.NETBANK.BISFINANCEEXC_SEND, entity);
    }

    @Override
    public BisFinanceExcDTO getByVoucherNo(String voucherNo) {
        return bisFinanceExcService.getByVoucherNo(voucherNo);
    }

    @Override
    public void updateBisFinanceExc(BisFinanceExcDTO bisFinanceExcDTO) {
        BisFinanceExc bisFinanceExc = new BisFinanceExc();
        BeanUtils.copyProperties(bisFinanceExcDTO,bisFinanceExc);
        bisFinanceExcService.update(bisFinanceExc);
    }
}
