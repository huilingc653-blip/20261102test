package com.bytter.erp.service;

import javax.jws.WebService;

/**
 * @author zhoul
 */
@WebService
public interface CorpWsService {

    /**
     * 新增单位信息
     *
     * @param params 新增单位报文
     * @return 结果
     */
    String save(String params);

    /**
     * 编辑单位信息
     *
     * @param params 编辑单位报文
     * @return 结果
     */
    String update(String params);

    /**
     * 删除用户
     *
     * @param params 加密报文
     * @return 结果
     */
    String delete(String params);

    /**
     * 根据代码查询单位接口
     * @param params
     * @return
     */
    String getCorpByCode(String params);

}
