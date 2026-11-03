package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.BankAccQueryDao;
import com.bytter.biz.erp.entity.ErpBankAccQueryView;
import com.bytter.biz.erp.vo.request.BankAccQueryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName BankAccQueryServiceImpl
 * @Author zx
 * @Description 银行账户查询接口实现
 * @Date 2026/4/28 15:27
 */
@Service("bankAccQueryService")
public class BankAccQueryService {

    @Autowired
    private BankAccQueryDao bankAccQueryDao;

    /**
     * 查询银行账户信息
     *
     * @param reqVo
     * @param tenantId
     * @return
     */
    public List<ErpBankAccQueryView> queryBankAccList(BankAccQueryRequestVO reqVo, String tenantId) {
        return bankAccQueryDao.queryBankAccList(reqVo, tenantId);
    }

}
