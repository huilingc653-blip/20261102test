package com.bytter.biz.finacial.tally.dao;
import com.bytter.biz.finacial.tally.entity.FcTallyRemindDtl;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * @author
 * @version 1.0
 * @Title: Dao
 * @Description: FcTallyRemindDtl
 * @date 2017-07-04 14:58:22
 */
@Repository
public class FinacialTallyRemindDao extends BaseDao<FcTallyRemindDtl, String> {
    public void deleteByBusId(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("busId", id);
        createSQLQuery(" delete from FC_TALLY_REMIND_DTL where bill_id = :busId ", params).executeUpdate();
    }
}
