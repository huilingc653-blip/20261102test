package com.bytter.biz.netbank.service.impl;

import com.bytter.biz.netbank.service.NisScfInfoService;
import com.bytter.biz.payment.api.service.INisScfInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 付款单-供应链金融债权凭证关联表（nis_scf_info）门面实现
 * <p>
 * 供 bytter-prepose-bill 等模块通过 dubbo 调用：按付款单ID+凭证类型查询关联的债权凭证ID集合。
 *
 * @author ztd
 * @since 2026/8/24
 */
@Service("NisScfInfoServiceImpl")
@DubboService(interfaceClass = INisScfInfoService.class)
public class NisScfInfoServiceImpl implements INisScfInfoService {

    @Autowired
    private NisScfInfoService nisScfInfoService;

    @Override
    public String getVoucherIds(String payId, String voucherType) {
        return nisScfInfoService.getVoucherIds(payId, voucherType);
    }

    @Override
    public String getTransferIds(String payId, String voucherType) {
        return nisScfInfoService.getTransferIds(payId, voucherType);
    }
}
