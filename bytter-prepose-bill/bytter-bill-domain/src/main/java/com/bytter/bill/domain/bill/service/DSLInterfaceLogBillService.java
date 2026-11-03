package com.bytter.bill.domain.bill.service;

import com.bytter.bill.domain.bill.dao.DSLInterfaceLogBillDao;
import com.bytter.bill.domain.bill.entity.DSLInterfaceLogBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lijinquan
 * @description:
 * @date 2024-09-03 18:19
 */
@Service("dslInterfaceLogBillService")
@Transactional
public class DSLInterfaceLogBillService {

    @Autowired
    private DSLInterfaceLogBillDao dslInterfaceLogBillDao;

    /**
     * 保存日志
     * @param log
     * @param success
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveLog(DSLInterfaceLogBill log, boolean success) throws Exception{
        log.setType(success ? "1" : "0");
        log.setLog(log.getLogStr().toString());
        dslInterfaceLogBillDao.save(log);
    }

}
