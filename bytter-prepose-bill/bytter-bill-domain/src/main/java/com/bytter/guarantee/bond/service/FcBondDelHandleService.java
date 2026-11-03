package com.bytter.guarantee.bond.service;

import com.bytter.core.base.AbstractService;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 保证金账户过期销户服务
 * @date 2024-09-10
 */
@Service("fcBondDelHandleService")
@Transactional
public class FcBondDelHandleService extends AbstractService implements ITaskService {

    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @DubboReference(check = false)
    private IUserService userService;

    @Autowired
    private FcBondDetailService fcBondDetailService;

    @Autowired
    private FcBondBalService fcBondBalService;

    @Override
    @XxlJob("FcBondDelHandleServiceJob")
    public void run(String arguments) {
        try {
            Date closeDate = DateUtils.getDate();
            String curDate = DateUtils.formatDate(closeDate,DateUtils.PATTERN_CLASSICAL_SIMPLE);
            XxlJobHelper.log("---当前日期【{}】，保证金账户到期自动销户，开始执行定时任务---",curDate);
            List<BtBankAccDTO> list = btBankAccService.getExpireBailBankAcc(curDate);
            if (EmptyUtils.isEmptyList(list)) {
                XxlJobHelper.log("---当前日期【{}】，保证金账户到期的数量为0----",curDate);
            } else {
                List<BtBankAccDTO> delBankAccDTOS = this.checkBailAccountApplyAmt(list);
                XxlJobHelper.log("---满足注销条件保证金账户【{}】个---",delBankAccDTOS.size());
                if(!EmptyUtils.isEmptyList(delBankAccDTOS)){
                    List<String> ids = delBankAccDTOS.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
                    //注销帐号
                    int num = btBankAccService.updateBankAccByIds(ids, closeDate);
                    XxlJobHelper.log("---成功注销【{}】个保证金银行账户---",num);
                    //存入作废
                    for(BtBankAccDTO dto:list){
                        FcBondDetailVO bondDetailVO = fcBondDetailService.getFcBondByBondBankAcc(dto.getBankAcc());
                        fcBondDetailService.deleteBond(bondDetailVO.getId(),userService.getScheduler());
                    }
                }
            }
            XxlJobHelper.log("---当前日期【{}】，保证金账户到期自动销户，结束执行定时任务",curDate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description 检查保证金占用金额是否释放完毕
     * @author zhous
     * @date 2024/9/10 15:42
     */
    private List<BtBankAccDTO> checkBailAccountApplyAmt(List<BtBankAccDTO> list) {
        List<BtBankAccDTO> delBankAccDTOS = new ArrayList<>();
        for(BtBankAccDTO dto:list){
            FcBondDetailVO bondDetailVO = fcBondDetailService.getFcBondByBondBankAcc(dto.getBankAcc());
            FcBondBal bondBal = fcBondBalService.getBondBalById(bondDetailVO.getId());
            if(BigDecimal.ZERO.compareTo(bondBal.getApplyMoney()) == 0){
                //占用金额为0的保证金账户才能进行销户
                delBankAccDTOS.add(dto);
            }
        }
        return delBankAccDTOS;
    }

}