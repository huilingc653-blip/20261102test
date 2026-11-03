package com.bytter.bill.domain.bill.dao;

import com.bytter.bill.domain.bill.entity.AbBillRuleBillDtlRmk;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillDtlRmkDtl;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillDtlRmkDtlVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description 票据流水备注规则明细dao
* @author zhous
* @date 2025/2/25 8:49
*/
@Repository
public class AbBillRuleBillDtlRmkDtlDao extends BaseDao<AbBillRuleBillDtlRmkDtl, String> {

    /**
    * @Description 根据业务id删除对应明细
    * @author zhous
    * @date 2025/2/25 8:49
    */
    public void deleteByBusId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from AB_BILL_RULEBILLDTLRMK_DTL where BUS_ID = '"+id+"'");
        createSQLQuery(sql.toString()).executeUpdate();
    }

    /**
    * @Description 根据业务id获取对应明细
    * @author zhous
    * @date 2025/2/25 8:49
    */
    public List<AbBillRuleBillDtlRmkDtlVO> getByByBusId(String id) {
        String sql = " select * from AB_BILL_RULEBILLDTLRMK_DTL where BUS_ID = :busId order by sort ";
        Map<String,Object> params = new HashMap<>();
        params.put("busId",id);
        return this.listBySQLAliasToBean(sql,params,AbBillRuleBillDtlRmkDtlVO.class);
    }
}
