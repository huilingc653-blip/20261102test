package com.bytter.biz.bdlink.api.service;

import com.bytter.biz.bdlink.api.dto.BisAccBalAutoDTO;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: BIS_ACC_BAL_AUTO
 * @date 2019-08-08 15:02:30
 */
public interface IBisAccBalAutoService {

    /**
     * 根据账号id币别id查询离线账户余额自动计算信息表
     *
     * @param bankAccId 账户ID
     * @param curId   币别ID
     * @return BisAccBalAuto
     */
    public BisAccBalAutoDTO getBisAccBalAutoByAccCurId(String bankAccId, String curId);

    public void updateEntity(BisAccBalAutoDTO bisAccBalAutoDTO);
}
