package com.bytter.basic.service;
import com.bytter.basic.dao.BtCurrencyInitDao;
import com.bytter.basic.entity.BtCurrencyInit;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 系统货币资料
 * @date 2022-3-1 11:14:22
 */
@Service("btCurrencyInitServiceBase")
@Transactional
public class BtCurrencyInitService extends BaseService<BtCurrencyInit, String> {
    @Autowired
    private BtCurrencyInitDao btCurrencyInitDao;
    @Override
    public BaseDao<BtCurrencyInit, String> getEntityDao() {
        return btCurrencyInitDao;
    }
}
