package com.bytter.system.inneraccount.service;
import com.bytter.system.api.ICmsFcAccService;
import com.bytter.system.inneraccount.dao.FcAccDao;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 融资内部账号相关接口
 *
 * @author xiay
 */
@Service("cmsFcAccService")
@DubboService
@Transactional
public class CmsFcAccService implements ICmsFcAccService {
    @Autowired
    private FcAccDao fcAccDao;
    /**
     * 更新贷款账号展期信息
     * @param loanAcc
     * @param deferredDate
     * @param deferredRate
     */
    @Override
    public void updateLoanAccDeferredDate(String loanAcc, Date deferredDate, BigDecimal deferredRate) {
        fcAccDao.updateLoanAccDeferredDate(loanAcc,deferredDate,deferredRate);
    }
}
