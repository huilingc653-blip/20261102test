package com.bytter.biz.inneraccount.service;
import com.bytter.biz.inneraccount.dao.BtBankAccVirtualDao;
import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 虚拟账号记账单元信息
 * @date 2022-08-01 17:50:16
 */
@Service("btBankAccVirtualService")
@Transactional
public class BtBankAccVirtualService extends BaseService<BtBankAccVirtual, String> {
    @Autowired
    private BtBankAccVirtualDao btBankAccVirtualDao;
    @Override
    public BaseDao<BtBankAccVirtual, String> getEntityDao() {
        return btBankAccVirtualDao;
    }
    /**
     * 根据账户ID获取虚拟账号子单元信息
     * @param bankAccId
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public List<BtBankAccVirtual> getListByBankAccId(String bankAccId) {
        return btBankAccVirtualDao.getListByBankAccId(bankAccId);
    }
    /**
     * 根据账户ID删除虚拟账户子单元信息
     * @param bankAccId
     */
    public void deleteByBankAccId(String bankAccId) {
        btBankAccVirtualDao.deleteByBankAccId(bankAccId);
    }
}
