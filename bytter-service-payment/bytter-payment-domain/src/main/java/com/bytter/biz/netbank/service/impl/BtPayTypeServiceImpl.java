package com.bytter.biz.netbank.service.impl;

import com.bytter.biz.netbank.dao.BtPayTypeDao;
import com.bytter.biz.payment.api.dto.BtPayTypeDTO;
import com.bytter.biz.payment.api.service.IBtPayTypeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统结算方式服务实现类。
 *
 * @author zhaohc
 * @since 2026/4/10 10:56
 */
@Service("btPayTypeServiceImpl")
@Transactional
@DubboService
public class BtPayTypeServiceImpl implements IBtPayTypeService {

    @Autowired
    private BtPayTypeDao btPayTypeDao;

    @Override
    public BtPayTypeDTO getPayTypeByName(String voucherName) {
        return btPayTypeDao.getPayTypeByVoucherName(voucherName);
    }
}
