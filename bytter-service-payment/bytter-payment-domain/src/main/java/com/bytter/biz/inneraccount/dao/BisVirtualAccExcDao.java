package com.bytter.biz.inneraccount.dao;
import com.bytter.biz.inneraccount.entity.BisVirtualAccExc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.BdlinkConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 虚拟账号操作指令表
 * @date 2022-08-01 17:50:16
 */
@Repository
public class BisVirtualAccExcDao extends BaseDao<BisVirtualAccExc, String> {
    /**
     * 根据账户ID获取虚拟账号子单元信息
     * @param bankAccId
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public List<BisVirtualAccExc> getListByBankAccId(String bankAccId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BisVirtualAccExc where bankAcc = :bankAccId and voucherStat=:voucherStat");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bankAccId", bankAccId);
        params.put("voucherStat", BdlinkConstant.SUCCESS);
        return this.createQuery(hql.toString()).setProperties(params).list();
    }
}
