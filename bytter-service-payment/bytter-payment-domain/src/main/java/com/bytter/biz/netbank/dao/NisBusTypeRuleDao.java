package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.NisBusTypeRule;
import com.bytter.biz.netbank.vo.NisBusTypeRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class NisBusTypeRuleDao extends BaseDao<NisBusTypeRule, String> {
    /**
     * 根据业务类别id删除业务规则
     * @param busId
     */
    public void deleteByBusId(String busId){
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from NIS_BUS_TYPE_RULE where BUS_ID = '"+busId+"'");
        createSQLQuery(sql.toString()).executeUpdate();
    }
    /**
     * 根据业务id获取所有业务规则
     * @param busId
     * @return
     */
    public List<NisBusTypeRuleVO> getByByBusId(String busId){
        String sql = " select * from NIS_BUS_TYPE_RULE where BUS_ID = :busId order by sort ";
        Map<String,Object> params = new HashMap<>();
        params.put("busId",busId);
        return this.listBySQLAliasToBean(sql,params,NisBusTypeRuleVO.class);
    }
    /**
     * 根据业务id获取所有业务规则 不包含有括号的
     * @param busId
     * @return
     */
    public List<NisBusTypeRuleVO> getByByBusIdNoParenthesis(String busId){
        String sql = " select * from NIS_BUS_TYPE_RULE where BUS_ID = :busId and (left_parenthesis is null or left_parenthesis='') " +
                " and (right_parenthesis is  null or right_parenthesis='')  order by sort ";
        Map<String,Object> params = new HashMap<>();
        params.put("busId",busId);
        return this.listBySQLAliasToBean(sql,params,NisBusTypeRuleVO.class);
    }
    /**
     * 根据业务id获取所有业务规则 不包含有括号的
     * @param busId
     * @return
     */
    public List<NisBusTypeRuleVO> getByByBusIdParenthesis(String busId){
        String sql = " select * from NIS_BUS_TYPE_RULE where BUS_ID = :busId and (left_parenthesis ='(' or right_parenthesis = ')' ) order by sort " ;
        Map<String,Object> params = new HashMap<>();
        params.put("busId",busId);
        return this.listBySQLAliasToBean(sql,params,NisBusTypeRuleVO.class);
    }
    /**
     * 判断业务规则是否重复
     * @param matchRule
     * @param matchElements
     * @param matchRelations
     * @param matchContent
     * @return
     */
    public boolean isHas(String matchRule,String matchElements,String matchRelations,String matchContent){
        StringBuilder hql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        hql.append(" from NisBusTypeRule where matchRule = :matchRule and  matchElements = :matchElements and matchRelations = :matchRelations and matchContent=:matchContent");
        params.put("matchRule", matchRule);
        params.put("matchElements", matchElements);
        params.put("matchRelations", matchRelations);
        params.put("matchContent", matchContent);
        List list = this.listByHQL(hql.toString(), params);
        return EmptyUtils.isNotEmpty(list);
    }
    /**
     * 判断业务规则是否重复
     * @param matchRule
     * @param matchElements
     * @param matchRelations
     * @param matchContent
     * @return
     */
    public boolean isHasForUpdate(String matchRule,String matchElements,String matchRelations,String matchContent,String busId){
        StringBuilder hql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        hql.append(" from NisBusTypeRule where  matchElements = :matchElements and matchRelations = :matchRelations and matchContent=:matchContent");
        params.put("matchElements", matchElements);
        params.put("matchRelations", matchRelations);
        params.put("matchContent", matchContent.trim());
        List<NisBusTypeRule> list = this.listByHQLAliasToBean(hql.toString(),params,NisBusTypeRule.class);
        boolean result = false;
        if(list.size() > 0 && !list.get(0).getBusId().equals(busId)){
            result = true;
        }
        if(list.size() >1){
            result = true;
        }
        return result;
    }
}
