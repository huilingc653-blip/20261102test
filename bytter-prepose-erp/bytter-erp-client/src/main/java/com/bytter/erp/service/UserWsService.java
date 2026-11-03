package com.bytter.erp.service;

import javax.jws.WebService;

/**
 * @author zhoul
 */
@WebService
public interface UserWsService {

    /**
     * 新增用户信息
     *
     * @param params 加密报文
     * @return  结果
     */
    String save(String params);

    /**
     * 修改用户信息
     *
     * @param params 加密报文
     * @return  结果
     */
    String update(String params);

    /**
     * 删除用户
     *
     * @param params 加密报文
     * @return  结果
     */
    String delete(String params);

    /**
     * 用户登录
     * @param params 用户名和密码json字符串
     * @return
     */
    String login(String params);
}
