package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.BankInputQueryDao;
import com.bytter.biz.erp.entity.ErpBankInputQueryView;
import com.bytter.biz.erp.vo.request.BankInputQueryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 银行联行号查询服务。
 *
 * @author zx
 * @date 2026/4/29
 */
@Service("bankInputQueryService")
public class BankInputQueryService {

    @Autowired
    private BankInputQueryDao bankInputQueryDao;

    /**
     * 根据查询条件获取银行联行号列表。
     *
     * @param reqVo 查询请求参数
     * @param tenantId 租户ID
     * @return 银行联行号结果列表
     */
    public List<ErpBankInputQueryView> queryBankInputList(BankInputQueryRequestVO reqVo, String tenantId) {
        return bankInputQueryDao.queryBankInputList(reqVo, tenantId);
    }

}
