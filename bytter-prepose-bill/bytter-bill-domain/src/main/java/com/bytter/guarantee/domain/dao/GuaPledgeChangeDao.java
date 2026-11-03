package com.bytter.guarantee.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.domain.entity.GuaPledgeInfoHis;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoHisVO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:GuaPledgeChangeDao
 * @Description:抵质押物变更Dao层
 * @date 2018年12月12日 下午1:50:37
 */
@Repository
public class GuaPledgeChangeDao extends BaseDao<GuaPledgeInfoHis, String> {
    /**
     * 根据抵质押物登记id查询变更记录
     */
    public DataTablesOutput queryChangeHisData(DataTablesInput input, String pledgeId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                " select gh.id as id,gh.pledge_id as pledgeId,gh.corp_id as corpId,gh.pledge_no as pledgeNo,")
                .append(" gh.create_by as createBy,gh.update_by as updateBy,gh.create_date as createDate")
                .append(" from GUA_PLEDGE_INFO_HIS gh")
                .append(" where 1=1 ")
                .append(" and gh.pledge_id=:pledgeId").append("  order by gh.create_date desc");
        params.put("pledgeId", pledgeId);
        return getDynamicBySQL(input, sql.toString(), GuaPledgeInfoHisVO.class, "", false, params);
    }
}
