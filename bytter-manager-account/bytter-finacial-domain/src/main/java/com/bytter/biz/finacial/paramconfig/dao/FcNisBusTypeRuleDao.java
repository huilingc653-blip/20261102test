package com.bytter.biz.finacial.paramconfig.dao;

import com.bytter.biz.finacial.paramconfig.entity.FcNisBusTypeRule;
import com.bytter.biz.finacial.paramconfig.vo.FcNisBusTypeRuleVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FcNisBusTypeRuleDao extends BaseDao<FcNisBusTypeRule, String> {

    /**
     * 根据业务类别id删除业务规则
     * @param busId
     */
    public void deleteByBusId(String busId){
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from FC_NIS_BUS_TYPE_RULE where BUS_ID = '"+busId+"'");
        createSQLQuery(sql.toString()).executeUpdate();
    }

    /**
     * 根据业务id获取所有业务规则
     * @param busId
     * @return
     */
    public List<FcNisBusTypeRuleVO> getByByBusId(String busId){
        String sql = " select * from FC_NIS_BUS_TYPE_RULE where BUS_ID = :busId order by sort ";
        Map<String,Object> params = new HashMap<>();
        params.put("busId",busId);
        return this.listBySQLAliasToBean(sql,params,FcNisBusTypeRuleVO.class);
    }
}
