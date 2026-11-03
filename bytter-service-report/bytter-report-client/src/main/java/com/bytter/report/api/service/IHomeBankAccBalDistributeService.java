package com.bytter.report.api.service;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.report.api.vo.HomeBankAccBalDistributeSearchVO;

import java.util.Map;

/**
 * 银行账户余额分布Service接口。
 *
 * @author zhaohc
 * @date 2024/4/18 16:31
 */
public interface IHomeBankAccBalDistributeService {

    public Map<String, Object> createBankAccBalBarToMobile(HomeBankAccBalDistributeSearchVO searchVO, ICurrentUser user);
}
