package com.bytter.increment.domain.dao;
import com.bytter.increment.domain.entity.BiRemainTermDetail;
import com.bytter.increment.domain.vo.BiRemainTermDetailVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @ClassName BiRemainTermDtlDao
 * @Description 剩余期限维护详细信息dao
 * @Author fusb
 * @Date 2020/1/10 17:43
 * @Version 1.0
 **/
@Repository
public class BiRemainTermDtlDao extends BaseDao {
    /**
    *@Description 根据剩余期限维护主表id获取相应子表信息
    *@Param remainTermId
    *@return java.util.List<com.bytter.biz.cms.basedata.entity.BiRemainTermDetail>
    *@Date 2020/1/10 17:46
    *@Author fusb
    */
    public List<BiRemainTermDetail> getBiRemainTermDtlByTermId(String remainTermId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BiRemainTermDetail dtl where dtl.remainTermId=:remainTermId");
        Map<String,String> parms = new HashMap<String,String>();
        parms.put("remainTermId",remainTermId);
        return this.listByHQLAliasToBean(hql.toString(),parms,BiRemainTermDetail.class);
    }
    /**
     *@Description 根据剩余期限维护主表id获取相应子表信息VO
     *@Param remainTermId
     *@return java.util.List<com.bytter.biz.cms.basedata.vo.BiRemainTermDetailVO>
     *@Date 2020/1/10 17:46
     *@Author fusb
     */
    public List<BiRemainTermDetailVO> getBiRemainTermDtlVOByTermId(String remainTermId){
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,");
        sql.append("        b.remain_term_id as remainTermId,");
        sql.append("        b.field1 as field1,");
        sql.append("        b.symbol as symbol,");
        sql.append("        b.field2 as field2,");
        sql.append("        b.conditions as conditions,");
        sql.append("        b.days as days,");
        sql.append("        b.unit as unit,");
        sql.append("        b.logical_relationship as logicalRelationship,");
        sql.append("        b.formula as formula,");
        sql.append("        b.sort as sort ");
        sql.append(" from bi_remain_term_detail b where b.remain_term_id=:remainTermId ");
        sql.append(" order by sort ");
        Map<String,String> parmas = new HashMap<String,String>();
        parmas.put("remainTermId",remainTermId);
        return this.listBySQLAliasToBean(sql.toString(),parmas,BiRemainTermDetailVO.class);
    }
    /**
    *@Description 根据剩余期限维护主表信息删除字表信息
    *@Param remainTermId
    *@return void
    *@Date 2020/1/10 18:31
    *@Author fusb
    */
    public void deleteRemainTermDtl (String remainTermId){
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from bi_remain_term_detail where remain_term_id=:remainTermId");
        Map<String,String> params = new HashMap<String,String>();
        params.put("remainTermId",remainTermId);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }
}
