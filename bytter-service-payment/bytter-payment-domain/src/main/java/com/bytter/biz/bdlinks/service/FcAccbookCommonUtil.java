package com.bytter.biz.bdlinks.service;

import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author luoyj
 * @date 2024/3/28.
 */
@Slf4j
@Component
public class FcAccbookCommonUtil {

    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;

    public String getVoucherNo(String netId, Date tallyDate, String curId){

        CorpDTO corpDTO = iCorpService.get(netId);
        BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(curId);
        return this.getAccbookVouvherNo(corpDTO.getCode(),tallyDate,btCurrencyDTO.getCurCode());
    }

    /**
     * 网点和币别在 联机余额里面获取的
     * 功能介绍 ： 获取凭证号
     * @param netCode 网点code
     * @param tallyDate 记账日期
     * @param curCode 币别
     * @return
     */
    private String getAccbookVouvherNo(String netCode, Date tallyDate, String curCode) {
        //默认规则为网点+币别+6位系统年月
        //根据参数返回生成凭证号规则
        String prefix = netCode + curCode;
        SequenceEnum sequenceEnum = SequenceEnum.VOUCHER_NO;//按照系统日期
        boolean voucherNoFlag = false;//参数默认为按照系统日期生成
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, "voucherNoRule");//取记账凭证号生成规则
        if (null != configDTO) {
            String voucherNoRule = configDTO.getValue();
            if (EmptyUtils.isNotEmpty(voucherNoRule) && "0".equals(voucherNoRule)) {//生成规则为按照记账日期
                voucherNoFlag = true;
            }
        }
        if (voucherNoFlag) {
            //生成规则为网点+币别+6位记账年月
            sequenceEnum = SequenceEnum.VOUCHER_NO_TALLY;//按照记账日期
            String tallyDate1 = DateUtils.formatDate(tallyDate, "yyyyMM");
            prefix = netCode + curCode + tallyDate1;//凭证号生成规则
        }
        String voucherNo = SequenceSingleton.getSequenceBySpecifyLockNoCache(sequenceEnum, prefix);
        log.info("------------凭证号为" + voucherNo);
        return voucherNo;
    }
}
