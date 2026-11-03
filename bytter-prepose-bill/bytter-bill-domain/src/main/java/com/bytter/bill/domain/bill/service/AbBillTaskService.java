package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillEndorseDao;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhous
 * @Description 票据定时任务操作服务类
 * @date 2023/9/28 13:45
 */
@Service("abBillTaskService")
public class AbBillTaskService extends AbstractService implements ITaskService {

    @Autowired
    private AbBillEndorseDao abBillEndorseDao;

    @Autowired
    private AbBillEndorseService abBillEndorseService;

    @Autowired
    private AbUserService abUserService;

    @Autowired
    private AbAccrecordsService abAccrecordsService;

    @Override
    @XxlJob("AbBillTaskService")
    public void run(String arguments) {
        Date startDate = DateUtils.parseDate(arguments);
        if (ObjectUtil.isNull(startDate)) {
            throw new BusinessException(AccebillMessageEnum.DATE_FORMAT_ERROR);
        }
        try {
            this.logger.info("---启用线上账户对内背书给线下账户自动登记票据到收票登记定时任务，开始执行定时任务---");
            insideEndorseOnlineToOffline(startDate);
            this.logger.info("---启用线上账户对内背书给线下账户自动登记票据到收票登记定时任务，结束执行定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param startDate 背书申请开始时间
     * @Description 对内背书线上账户背给线下账户自动将票据作为持有票据登记到被背书人单位下，票据状态为暂存状态
     * @author zhous
     * @date 2023/9/28 11:16
     */
    private void insideEndorseOnlineToOffline(Date startDate) {
        ICurrentUser userVo = abUserService.getScheduler();
        List<String> list = abBillEndorseDao.queryInsideEndorseOnlineToOfflineAccrecordId(startDate);
        if (CollectionUtil.isNotEmpty(list)) {
            for (String accId : list) {
                //批量入库
                abBillEndorseService.batchStorage(accId, userVo, AccebillConstant.INSIDE_ENDORSE_AUTO_REG_SIGN_Y);
            }
            //设置流水对内背书自动登记标志
            AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
            accRecordsVO.setIdArray(list.toArray(new String[]{}));
            accRecordsVO.setInsideEndorseAutoRegSign(AccebillConstant.INSIDE_ENDORSE_AUTO_REG_SIGN_Y);
            abAccrecordsService.batchUpdate(accRecordsVO, userVo);
        }
    }

}
