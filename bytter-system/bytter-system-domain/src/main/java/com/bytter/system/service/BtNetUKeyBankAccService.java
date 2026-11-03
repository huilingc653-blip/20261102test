package com.bytter.system.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.dao.BtNetUKeyBankAccDao;
import com.bytter.system.entity.BtNetUKeyBankAcc;
/**
 * @author qinl
 * @version 1.0
 * @Description: BtNetUKeyBankAccService
 * @date 2020-04-17
 */
@Service("btNetUKeyBankAccService")
@Transactional
public class BtNetUKeyBankAccService extends BaseService<BtNetUKeyBankAcc, String> {
    @Autowired
    private BtNetUKeyBankAccDao btNetUKeyDao;
    @Override
    public BaseDao<BtNetUKeyBankAcc, String> getEntityDao() {
        return btNetUKeyDao;
    }
    /**
     * 获取Ukey关联的银行账号数量
     *
     * @param uKeyId
     * @return
     */
    public long associateBankAccNum(String uKeyId) {
        long check = 0;
        Map<String, String> map = new HashMap<>();
        map.put("UKEY_ID", uKeyId);
        check = check + btNetUKeyDao.addSum("BT_NET_UKEY_BANKACC", map);
        return check;
    }
    public List<BtNetUKeyBankAcc> getListByUKeyId(String uKeyId){
        return btNetUKeyDao.selectListByUKeyId(uKeyId);
    }
    public List<BtNetUKeyBankAcc> getListByUKeyCode(String uKeyCode){
        return btNetUKeyDao.selectListByUKeyCode(uKeyCode);
    }
}
