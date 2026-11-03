package com.bytter.credit.domain.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.service.IAbVoucherinfoViceService;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.core.base.AbstractService;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatUration;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author zhous
 * @version 1.0
 * @Title: Service
 * @Description: 授信占用电票业务单号同步定时任务
 * @date 2017-08-19 11:36:37
 */
@Service("ratBillNoSupplyTaskService")
@Transactional
public class RatBillNoSupplyTaskService extends AbstractService implements ITaskService {

    @Autowired
    private RatUrationService ratUrationService;
    @DubboReference(check = false)
    private IAbVoucherinfoViceService abVoucherinfoViceService;

    @Override
    @XxlJob("RatBillNoSupplyTaskService")
    public void run(String arguments) {
        try {
            this.logger.info("---启用授信占用电票业务单号同步定时任务，开始执行定时任务---");
            rationDtlOccupyBillNoSupply();
            this.logger.info("---启用授信占用电票业务单号同步定时任务，结束执行定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
    * @Description 授信占用电票业务单号同步
    * @author zhous
    * @date 2024/8/20 15:54
    */
    private void rationDtlOccupyBillNoSupply() {
        //查询授信占用中电票业务的业务单据为空的数据
        List<RatUration> ratUrationList = ratUrationService.getRationBillNoIsNullList(AccebillConstant.AB_CONFIG_TYPE, RationConstant.BUSINESS_NAME_04);
        if(CollectionUtil.isNotEmpty(ratUrationList)){
            for(RatUration ratUration : ratUrationList){
                String origNo = abVoucherinfoViceService.getOrigNoByVid(ratUration.getBillId());
                if(StrUtil.isNotEmpty(origNo)){
                    ratUrationService.updateBillNoSupply(ratUration.getId(),origNo);
                }else{
                    this.logger.info("---从表ID为{"+ratUration.getBillId()+"未查询到票据号---");
                }
            }
        }
    }
}
