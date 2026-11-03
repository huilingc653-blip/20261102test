package com.bytter.erp.service;

import javax.jws.WebService;

/**
 * @author yuwj
 * @date 2019/8/12 13:51
 */
@WebService
public interface NisBillheadWsService {
    /**
     * 同步付款单
     * @param params json参数
     * @return
     */
    String save(String params);

    /**
     * 获取交易状态
     * @param params json参数
     * @return
     */
    String getVoucherStat(String params);

    /**
     * 删除银行账户
     * @param params json参数
     * @return
     */
    String delete(String params);

}
