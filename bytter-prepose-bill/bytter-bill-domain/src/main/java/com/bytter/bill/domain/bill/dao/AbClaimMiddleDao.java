package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbClaimMiddle;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
/**
 * 票据认领中间表dao
 *
 * @author ouyj
 */
@Repository
public class AbClaimMiddleDao extends BaseDao<AbClaimMiddle, String> {
    /**
     * 根据id数组修改认领信息
     *
     * @param ids           id数组
     * @param abClaimMiddle 认领对象
     * @return 更新条数
     */
    public int updateByIds(String[] ids, AbClaimMiddle abClaimMiddle) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update AB_CLAIM_MIDDLE set claim_status = :claimStatus, claim_person = :claimPerson  ");
        sql.append(" ,update_date = :claimTime, claim_time = :claimTime, update_by = :claimPerson ");
        sql.append(" where id in (:ids) ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("ids", ids);
        params.put("claimStatus", abClaimMiddle.getClaimStatus());
        params.put("claimTime", abClaimMiddle.getClaimTime());
        params.put("claimPerson", abClaimMiddle.getClaimPerson());
        return this.batchSQLExecute(sql.toString(), params);
    }
}
