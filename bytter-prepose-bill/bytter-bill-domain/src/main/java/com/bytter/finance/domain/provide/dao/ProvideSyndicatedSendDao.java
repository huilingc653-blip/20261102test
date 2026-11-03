package com.bytter.finance.domain.provide.dao;
import com.bytter.finance.domain.provide.entity.ProvideSyndicatedSend;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedSendVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银团贷款发放Dao
 * @date 2022-07-22 10:45:09
 */
@Repository
public class ProvideSyndicatedSendDao extends BaseDao<ProvideSyndicatedSend, String> {
    /**
     * 通过主表删除中间费用数据
     *
     * @param params
     */
    public void deleteLoanSyndicated(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from CMS_PROVIDE_SYNDICATED_SEND where PROVIDE_LOAN_SEND_ID = :id ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    public List<ProvideSyndicatedSendVo> getSendByProvideLoanSendId(String provideLoanSendId) {
        if (!StringUtils.isNotEmpty(provideLoanSendId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select s.id as id, s.direct_finance_id as directFinanceId, s.provide_loan_send_id as provideLoanSendId, s.leading_bank as leadingBank, ");
        sql.append(
                " s.bank_name as bankName, s.bank_code as bankCode, s.notsend_money as notsendMoney, s.send_money as sendMoney, s.send_money_share as sendMoneyShare, ");
        sql.append(
                " s.create_by as createBy, s.create_date as createDate, s.update_by as updateBy, s.update_date as updateDate ");
        sql.append(" from CMS_PROVIDE_SYNDICATED_SEND s ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(provideLoanSendId)) {
            sql.append(" and s.provide_loan_send_id=:provideLoanSendId");
            params.put("provideLoanSendId", provideLoanSendId);
        }
        List<ProvideSyndicatedSendVo> list = listBySQLAliasToBean(sql.toString(), params,
                ProvideSyndicatedSendVo.class);
        return list;
    }
}
