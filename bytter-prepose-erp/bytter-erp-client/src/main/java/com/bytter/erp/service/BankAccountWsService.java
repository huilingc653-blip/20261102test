package com.bytter.erp.service;

import javax.jws.WebService;

/**
 * @author yuwj
 * @date 2019/8/9 13:51
 */
@WebService
public interface BankAccountWsService {
    /**
     * 新增银行账户
     * @param params json参数
     * @return
     */
    String save(String params);

    /**
     * 更新银行账户
     * @param params json参数
     * @return
     */
    String update(String params);

    /**
     * 删除银行账户
     * @param params json参数
     * @return
     */
    String delete(String params);

}
