package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.GuaExguaranteeCorp;
import com.bytter.guarantee.domain.vo.GuaExguaranteeCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class GuaExguaranteeCorpDao extends BaseDao<GuaExguaranteeCorp, String> {
    public List<GuaExguaranteeCorpVO> getGuaranteeCorpByid(String exAmountId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select gc.ex_amount_id, gc.corp_id, gc.corp_name,gc.cur_id,gc.cur_name from GUA_EXGUARANTEE_CORP gc where gc.EX_AMOUNT_ID = :exAmountId ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("exAmountId", exAmountId);
        return this.listBySQLAliasToBean(sql.toString(), params, GuaExguaranteeCorpVO.class);
    }
    /*
     * 维护的单位id是否已经存在
     */
    public List<GuaExguaranteeCorpVO> checkGuaranteeCorp(String corpId, String curId,String amountId) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(" select gc.ex_amount_id, gc.corp_id,gc.corp_name,gc.cur_id,gc.cur_name from GUA_EXGUARANTEE_CORP gc where gc.corp_id=:corpId");
        sql.append(" and gc.cur_id = :curId ");
        if (StringUtils.isNotEmpty(amountId)) {
            sql.append(" and gc.ex_amount_id <> :exAmountId ");
            params.put("exAmountId", amountId);
        }
        params.put("corpId", corpId);
        params.put("curId", curId);
        return this.listBySQLAliasToBean(sql.toString(), params, GuaExguaranteeCorpVO.class);
    }
    public void deleteExguaranteeCorp(String exAmountId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " delete from GUA_EXGUARANTEE_CORP where EX_AMOUNT_ID = :exAmountId ";
        params.put("exAmountId", exAmountId);
        createSQLQuery(sql, params).executeUpdate();
    }
    public List<GuaExguaranteeCorpVO> getGuaranteeCorptreeByid(String exAmountId) {
        StringBuilder sql = new StringBuilder();
//        sql.append(
//                " select gc.EX_AMOUNT_ID, gc.corp_id,gc.corp_name, c.parent_id as parentCorpId, c.code as corpCode from GUA_EXGUARANTEE_CORP gc  ");
//        sql.append(" left join sys_corp c on c.id = gc.corp_id ");
//        sql.append(" where gc.EX_AMOUNT_ID = :exAmountId order by c.sort");
        sql.append(
                " select gc.EX_AMOUNT_ID, gc.corp_id,gc.corp_name from GUA_EXGUARANTEE_CORP gc where gc.EX_AMOUNT_ID = :exAmountId order by gc.sort ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("exAmountId", exAmountId);
        return this.listBySQLAliasToBean(sql.toString(), params, GuaExguaranteeCorpVO.class);
    }
}
