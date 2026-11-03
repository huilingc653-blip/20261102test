package com.bytter.biz.bdlinks.service;

import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlinks.dao.BpDubiousTradeDao;
import com.bytter.biz.bdlinks.entity.BisExc;
import com.bytter.biz.bdlinks.entity.BpDubiousTrade;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.service.IBisForeignExcService;
import com.bytter.biz.payment.api.service.IFexMessageService;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: BP_DUBIOUS_TRADE 可疑交易
 * @date 2021-3-5 11:30:51
 */
@Service("bpDubiousTradeService")
@Transactional
public class BpDubiousTradeService extends BaseService<BpDubiousTrade, String> {
    @Autowired
    private BpDubiousTradeDao bpDubiousTradeDao;
    @DubboReference(check = false)
	private INisMessageService nisMessageService;
    @DubboReference(check = false)
	private IFexMessageService fexMessageService;
    @Autowired
    private BisExcService bisExcService;
    @DubboReference(check = false)
	private IBisForeignExcService bisForeignExcService;
    @DubboReference(check = false)
	private INotificationService notificationService;

    @Autowired
    private BisExcStatusChangeHandler bisExcStatusChangeHandler;

    @Override
    public BaseDao<BpDubiousTrade, String> getEntityDao() {
        return bpDubiousTradeDao;
    }
    /**
     * 指令确认发送
     *
     * @param voucherNo
     */
    public void sendBisExc(String id, String voucherNo, String foreignFlag, ICurrentUser user) {
        //指令防篡改校验
        BisExc bisExc = null;
        String verifyCodeNew = "";
        Date reqDateNew = DateUtils.getDate();
        if(CommonConstant.YES.equals(foreignFlag)){
            BisForeignExcDTO bisForeignExc = bisForeignExcService.getBisForeignExcByVoucherNo(voucherNo);
            if(!bisForeignExc.getVerifyCode().equals(bisForeignExcService.setVerifyCode(bisForeignExc))){
                throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYINFO_CHANGED, bisForeignExc.getVoucherNo());
            }
            bisForeignExc.setReqDate(reqDateNew);
            bisForeignExc.setVoucherStat(BdlinkConstant.PRE_SEND);
            verifyCodeNew = bisForeignExcService.setVerifyCode(bisForeignExc);
            bisForeignExc.setNoticeSign(CommonConstant.NO);
            bisForeignExc.setVerifyCode(verifyCodeNew);
        }else{
            bisExc = bisExcService.get(id, true);
            //自动下拨的指令，银行接口未写入加密信息，无法验证
            if(!BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN.equals(bisExc.getVoucherFrom())) {
                if (!bisExc.getVerifyCode().equals(bisExcService.setVerifyCode(bisExc))) {
                    throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_PAYINFO_CHANGED, bisExc.getVoucherNo());
                }
            }
            bisExc.setReqDate(reqDateNew);
            bisExc.setVoucherStat(BdlinkConstant.PRE_SEND);
            verifyCodeNew = bisExcService.setVerifyCode(bisExc);
            bisExc.setNoticeSign(CommonConstant.NO);
            bisExc.setVerifyCode(verifyCodeNew);
        }
        int result = this.bpDubiousTradeDao.updateBisExcVoucherStat(voucherNo, BdlinkConstant.PRE_SEND, foreignFlag, reqDateNew, verifyCodeNew);
        if (result > 0) {

            //更新可疑交易信息表，中间表有且仅有一笔待发送记录
            int row = this.bpDubiousTradeDao.updateBpDubiousTrade(voucherNo, "0", foreignFlag);
            if(row != 1){
                throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_EXCEPTION_DATA, voucherNo);
            }
            if(bisExc != null){
                // 判断是否是对私付款，对私付款需同步更新可疑状态的对私明细交易状态为3-准备发送
                if (BdlinkConstant.TRANS_MODE_PRIVATE.equals(bisExc.getTransMode())) {
                    this.bpDubiousTradeDao.updateExpenseVoucherStatByBillId(bisExc.getCbsBillId(), BdlinkConstant.PRE_SEND);
                }
//                bisMQService.sendBisExcChangeMsg(bisExc, BdlinkConstant.PRE_SEND);
                bisExcStatusChangeHandler.processBisExcChangeMsg(bisExc.getVoucherFrom(),bisExc.getCbsBillId(),BdlinkConstant.PRE_SEND);
            }
            //生成日志&完成通知
            if(CommonConstant.YES.equals(foreignFlag)){
                fexMessageService.saveMessage(id, FexMessageConstant.MESSAGE_TYPE_EXC_SEND, NisMessageConstant.NIS_LOG_EXC_CONFIRM_SEND, FexMessageConstant.BUS_TYPE_FOREIGN, user.getLoginName(), user.getUsername());
                notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_OVERTIME, id, user.getId());
                notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_DOUBTFUL_OVERTIME, id, user.getId());
            }else {
                nisMessageService.saveMessage(id, NisMessageConstant.MESSAGE_TYPE_EXC_SEND, NisMessageConstant.NIS_LOG_EXC_CONFIRM_SEND, NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
                notificationService.complete(NotificationConstant.NETBANK.BISEXC_OVERTIME, id, user.getId());
                notificationService.complete(NotificationConstant.NETBANK.BISEXC_DOUBTFUL_OVERTIME, id, user.getId());
            }
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, voucherNo);
        }
    }
    /**
     * 指令作废
     *
     * @param voucherNo
     */
    public void defeatBisExc(String id, String voucherNo, String foreignFlag, ICurrentUser user) {
        //指令作废，更新原指令状态为7-指令无效
        int result = this.bpDubiousTradeDao.updateBisExcVoucherStat(voucherNo, BdlinkConstant.INVALIDATE, foreignFlag,null,null);
        if(result>0) {
            BisExc bisExc = bisExcService.get(id);
            if (Objects.nonNull(bisExc)) {
//                bisMQService.sendBisExcChangeMsg(bisExc, BdlinkConstant.INVALIDATE);
                bisExcStatusChangeHandler.processBisExcChangeMsg(bisExc.getVoucherFrom(),bisExc.getCbsBillId(),BdlinkConstant.INVALIDATE);
            }
            //更新可疑交易信息表，中间表有且仅有一笔待发送记录
            int row = this.bpDubiousTradeDao.updateBpDubiousTrade(voucherNo, "2", foreignFlag);
            if(row != 1){
                throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_EXCEPTION_DATA, voucherNo);
            }
            //生成日志
            if(CommonConstant.YES.equals(foreignFlag)){
                fexMessageService.saveMessage(id, FexMessageConstant.MESSAGE_TYPE_EXC_REPEAL, NisMessageConstant.NIS_LOG_EXC_CONFIRM_DEFEAT, FexMessageConstant.BUS_TYPE_FOREIGN, user.getLoginName(), user.getUsername());
                notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_OVERTIME, id, user.getId());
                notificationService.complete(NotificationConstant.FOREIGN.BISFOREIGNEXC_DOUBTFUL_OVERTIME, id, user.getId());
            }else {
                nisMessageService.saveMessage(id, NisMessageConstant.MESSAGE_TYPE_EXC_REPEAL, NisMessageConstant.NIS_LOG_EXC_CONFIRM_DEFEAT, NisMessageConstant.BUS_TYPE_BDLINKPAY, user.getLoginName(), user.getUsername());
                notificationService.complete(NotificationConstant.NETBANK.BISEXC_OVERTIME, id, user.getId());
                notificationService.complete(NotificationConstant.NETBANK.BISEXC_DOUBTFUL_OVERTIME, id, user.getId());
            }
        } else {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, voucherNo);
        }
    }
}
