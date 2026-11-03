package com.bytter.finance.domain.provide.dao;
import com.bytter.finance.domain.provide.entity.ProvideSyndicated;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 银团明细信息Dao
 * @author onlineGenerator
 * @date 2022-07-22 10:45:09
 * @version 1.0
 *
 */
@Repository
public class ProvideSyndicatedDao extends BaseDao<ProvideSyndicated, String> {
    /**
     * 通过主表删除中间费用数据
     * 
     * @param params
     */
    public void deleteLoanSyndicated(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from CMS_PROVIDE_SYNDICATED where DIRECT_FINANCE_ID = :id ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 通过借款主信息id获取银团信息数据
     * 
     * @param directFinanceId
     * @return
     */
    public List<ProvideSyndicatedVo> getByDirectFinanceId(String directFinanceId) {
        if (!StringUtils.isNotEmpty(directFinanceId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select s.id as id, s.direct_finance_id as directFinanceId, s.leading_bank as leadingBank, s.bank_name as bankName, ");
        sql.append(
                " s.bank_code as bankCode, s.commitment_share as commitmentShare, s.commitment_money as commitmentMoney, s.rmk as rmk, ");
        sql.append(
                " s.create_by as createBy, s.create_date as createDate, s.update_by as updateBy, s.update_date as updateDate ");
        sql.append(" from CMS_PROVIDE_SYNDICATED s ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(directFinanceId)) {
            sql.append(" and s.direct_finance_id=:directFinanceId");
            params.put("directFinanceId", directFinanceId);
        }
        List<ProvideSyndicatedVo> list = listBySQLAliasToBean(sql.toString(), params, ProvideSyndicatedVo.class);
        return list;
    }
}
