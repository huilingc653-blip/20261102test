package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.ConWarnRules;
import com.bytter.guarantee.domain.vo.ConWarnRulesVO;
import com.bytter.core.base.BaseDao;
import com.bytter.guarantee.constant.ConConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: ConWarnRulesDao
 * @Description: 合同预警规则Dao
 * @author wangzy
 * @date 2019/11/4 15:21
 *
 */
@Repository
public class ConWarnRulesDao extends BaseDao<ConWarnRules, String> {
    /**
     * 查询所有合同类型预警规则
     * 
     * @return 合同类型预警规则LIST
     *
     */
    public List<ConWarnRulesVO> listConWarnRulesVO() {
        StringBuilder sql = new StringBuilder();
        sql.append("select r.con_Type_Id as conTypeId,r.BEFOR_DAY as beforDay,r.interval_Day as intervalDay, ");
        sql.append(" r.VALID_SIGN as validSign from CON_WARN_RULES r left join CON_TYPE t on t.id=r.CON_TYPE_ID");
        sql.append(" where t.VALID_SIGN=:validSign");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validSign", ConConstant.CON_VALID_SIGN_1);
        List<ConWarnRulesVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, ConWarnRulesVO.class);
        if (listInfo != null) {
            return listInfo;
        }
        return null;
    }
    /**
     * 删除所有的合同预警信息
     *
     */
    public int deleteConWarnRules() {
        String sql = "delete from CON_WARN_RULES";
        return this.batchSQLExecute(sql);
    }
    /**
     * 根据合同类型id查询所有合同信息
     * 
     * @return 合同信息LIST
     *
     */
    public List<ConWarnRulesVO> listConWarnInfoVO() {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select i.id as conId,t.CON_NAME as typeName,i.CON_NAME as conName,i.END_DATE as endDate,i.CON_NO as conNo,r.BEFOR_DAY as beforDay,");
        sql.append(
                " i.CORP_ID as corpId,r.INTERVAL_DAY as intervalDay from CON_WARN_RULES r left join CON_TYPE t on r.CON_TYPE_ID=t.id left join ");
        sql.append(
                " CON_INFO i on t.id=i.TYPE_ID where t.VALID_SIGN=:validSign and r.VALID_SIGN=:validSign and i.END_DATE is not null ");
        sql.append(" and i.status < :endStatus and i.status > :startStatus");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validSign", ConConstant.CON_VALID_SIGN_1);
        params.put("endStatus", ConConstant.CON_INFO_STATUS_90);
        params.put("startStatus", ConConstant.CON_INFO_STATUS_40);
        List<ConWarnRulesVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, ConWarnRulesVO.class);
        if (listInfo != null) {
            return listInfo;
        }
        return null;
    }
}
