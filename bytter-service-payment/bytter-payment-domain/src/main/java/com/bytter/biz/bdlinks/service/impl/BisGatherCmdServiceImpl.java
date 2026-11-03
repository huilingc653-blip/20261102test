package com.bytter.biz.bdlinks.service.impl;

import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlinks.entity.BisGatherCmd;
import com.bytter.biz.bdlinks.service.BisGatherCmdService;
import com.bytter.biz.bdlinks.vo.BisGatherCmdVO;
import com.bytter.biz.payment.api.constant.NisMessageConstant;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.biz.payment.api.service.IBisGatherCmdService;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 收款指令
 * @date 2017-08-23 11:08:59
 */
@Service("bisGatherCmdServiceImpl")
@Transactional
@DubboService
public class BisGatherCmdServiceImpl implements IBisGatherCmdService {
    @Autowired
    private BisGatherCmdService bisGatherCmdService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
	private INisMessageService nisMessageService;
    /**
     * 新增收款指令
     *
     * @param bisGatherCmd
     */
    @Override
    public void addBisGatherCmd(BisGatherCmdDTO bisGatherCmd, ICurrentUser user) {
        this.updateOldBisGatherCmd(bisGatherCmd.getCbsBillId(), user);
        BisGatherCmd entity = new BisGatherCmd();
        BeanUtils.copyNotNullProperties(bisGatherCmd, entity);
        entity.setCmdStat(BdlinkConstant.WAIT_FILL);
        entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_GATHER_CMD));
        //加密信息
        entity.setVerifyCode(bisGatherCmdService.setVerifyCode(entity));
        //指令编号重复校验
        if (bisGatherCmdService.getByVoucherNo(entity.getVoucherNo()) != null) { //TODO 校验需要前置
            throw new ParameterException(NetbankMessageEnum.BIS_EXC_MESSAGE_VOUCHERNO_REPAY, entity.getVoucherNo());
        } else {
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
            entity.setNoticeSign(CommonConstant.SEND_SIGN_NO);
            bisGatherCmdService.save(entity);
            //日志记录
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_AUTO_ADD, "", NisMessageConstant.BUS_TYPE_BDLINKGATHER, user.getLoginName(), user.getUsername());
            //消息提醒
            BtBankAccDTO bankAccDto = btBankAccService.getBankAccById(entity.getChildAccId());
            if (bankAccDto != null) {
                //消息提醒
                notificationService.send(entity.getId(), bankAccDto.getCorpId(), NotificationConstant.NETBANK.BISGATHERCMD_SEND, entity);
            }
        }
    }

    /**
     * 接收到消息后,调用该方法
     * @param bisGatherCmd 收款指令
     * @param user 用户
     */
    @Override
    public void autoSendBisGatherCmd(BisGatherCmdDTO bisGatherCmd, ICurrentUser user) {

        this.updateOldBisGatherCmd(bisGatherCmd.getCbsBillId(), user);
        BisGatherCmd entity = new BisGatherCmd();
        BeanUtils.copyNotNullProperties(bisGatherCmd, entity);
        if (EmptyUtils.isBlank(entity.getVoucherNo())) {
            entity.setVoucherNo(SequenceSingleton.getSequence(SequenceEnum.BIS_GATHER_CMD));
        }
        entity.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
        //加密信息
        entity.setVerifyCode(bisGatherCmdService.setVerifyCode(entity));
        this.bisGatherCmdService.sendBisGatherCmd(entity, user);
    }
    /**
     * 根据单号更新旧指令
     *
     * @param cbsBillId
     * @param user
     * @author zuoy 2018-08-07
     */
    private void updateOldBisGatherCmd(String cbsBillId, ICurrentUser user) {
        //根据业务Id获取指令
        BisGatherCmd oldCmd = bisGatherCmdService.getByCbsBillId(cbsBillId);
        if (!EmptyUtils.isEmpty(oldCmd)) {
            //指令状态为B-指令退回或者7-指令无效
            if (BdlinkConstant.BACK.equals(oldCmd.getCmdStat()) || BdlinkConstant.INVALIDATE.equals(oldCmd.getCmdStat())) {
                int result = bisGatherCmdService.updateBisGatherCmdCbsBillNo(oldCmd);
                if(result<1) {
                    throw new ParameterException(NetbankMessageEnum.NIS_BISEXC_MESSAGE_EXIST);
                }
            } else {
                throw new ParameterException(NetbankMessageEnum.NIS_BISEXC_MESSAGE_EXIST);
            }
        }
    }
    /**
     * 根据业务单号ID集合和指令来源，获取指令Map
     * @param billIds
     * @param voucherFrom
     * @return
     */
    @Override
    public Map<String, BisGatherCmdDTO> getBisGatherCmdMapByCbsBillIds(List<String> billIds, String voucherFrom) {
        return bisGatherCmdService.getBisGatherCmdMapByCbsBillIds(billIds, voucherFrom);
    }
    /**
     * 根据业务单号集合和指令来源，获取指令Map
     * @param billNos
     * @param voucherFrom
     * @return
     */
    @Override
    public Map<String, BisGatherCmdDTO> getBisGatherCmdMapByCbsBillNos(List<String> billNos, String voucherFrom) {
        return bisGatherCmdService.getBisGatherCmdMapByCbsBillNos(billNos, voucherFrom);
    }

    @Override
    public BisGatherCmdDTO getByVoucherNo(String voucherNo) {
        BisGatherCmdVO vo = bisGatherCmdService.getByVoucherNo(voucherNo);
        BisGatherCmdDTO dto = new BisGatherCmdDTO();
        BeanUtils.copyNotNullProperties(vo,dto);
        return dto;
    }
}
