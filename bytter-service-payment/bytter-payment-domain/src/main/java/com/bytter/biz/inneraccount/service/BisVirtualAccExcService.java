package com.bytter.biz.inneraccount.service;
import com.bytter.biz.inneraccount.dao.BisVirtualAccExcDao;
import com.bytter.biz.inneraccount.entity.BisVirtualAccExc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 虚拟账号操作指令表
 * @date 2022-08-01 17:50:16
 */
@Service("bisVirtualAccExcService")
@Transactional
public class BisVirtualAccExcService extends BaseService<BisVirtualAccExc, String> {
    @Autowired
    private BisVirtualAccExcDao bisVirtualAccExcDao;
    @Override
    public BaseDao<BisVirtualAccExc, String> getEntityDao() {
        return bisVirtualAccExcDao;
    }
    /**
     * 获取虚拟账号操作指令
     * @param bankAccId
     * @return
     */
    public HashMap<String, BisVirtualAccExc> getBisVirtualAccExcMap(String bankAccId) {
        HashMap<String, BisVirtualAccExc> excMap = new HashMap<>();
        List<BisVirtualAccExc> list = this.bisVirtualAccExcDao.getListByBankAccId(bankAccId);
        for (BisVirtualAccExc exc:list) {
            excMap.put(exc.getBankAcc() + CommonConstant.COMMA_SPLIT + exc.getVirtualNo(), exc);
        }
        return excMap;
    }
}
