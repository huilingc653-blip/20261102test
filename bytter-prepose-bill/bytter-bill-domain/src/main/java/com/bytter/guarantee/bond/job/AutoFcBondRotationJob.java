package com.bytter.guarantee.bond.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.guarantee.bond.dao.FcBondApplyDao;
import com.bytter.guarantee.bond.dao.FcBondBackDao;
import com.bytter.guarantee.bond.entity.FcBondBack;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.bond.service.FcBondBalService;
import com.bytter.guarantee.vo.FcBondApplyVO;
import com.bytter.guarantee.vo.FcBondBalVO;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author qijh
 * @date 2024/12/27.
 * 释放保证金自动转回定时任务
 */
@Slf4j
@Component
public class AutoFcBondRotationJob implements ITaskService {
    @Autowired
    private FcBondApplyDao fcBondApplyDao;
    @Autowired
    FcBondBackDao fcBondBackDao;
    @Autowired
    FcBondBalService fcBondBalService;
    @XxlJob("AutoFcBondRotationJob")
    @Transactional
    @Override
    public void run(String arguments) {
        log.info("释放保证金自动转回定时任务开始");
        //List<FcBondApplyVO> list = fcBondApplyDao.getFcBondApplyListForAutoFcBondRotationJob();
        List<FcBondBalVO> list = fcBondBackDao.getFcBondBalListForAutoFcBondRotationJob();
        if(EmptyUtils.isNotEmpty(list)){
            for(FcBondBalVO vo : list){
                if(EmptyUtils.isNotEmpty(vo.getBondId())){
                    FcBondBack entity = new FcBondBack();
                    // 生成单号
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_FCBOND_BACK));
                    entity.setBackMoney(vo.getAvailMoney());
                    entity.setBackInterest(BigDecimal.ZERO);
                    entity.setPurpose("银承保证金");
                    entity.setOutBankAcc("");
                    entity.setBondId(vo.getBondId());
                    entity.setStatus(95);
                    entity.setBackDate(new Date());
                    entity.setCreateDate(new Date());
                    entity.setCreateBy("admin");
                    fcBondBackDao.save(entity);

                    BigDecimal money = vo.getAvailMoney();
                    if(ObjectUtil.isNotEmpty(vo.getBackMoney())){
                        money = money.add(vo.getBackMoney());
                    }
                    fcBondBackDao.updateFcBondBack(entity.getBondId(), money);
                }
            }
        }
        log.info("释放保证金自动转回定时任务结束");
    }
}
