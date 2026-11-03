package com.bytter.credential.domain.international.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.vo.GuaContractInternationalVO;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-10 18:32
 * @Version 1.0
 */
@Repository
public class SupplementCreditDao extends BaseDao<LcEnrol, String> {
    /**
     * 待补录列表
     *
     * @param dataTablesInput
     * @param corpIds
     * @return DataTablesOutput
     */
    public DataTablesOutput listSupplementCredit(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  lc.id id, ");
        sql.append("        lc.lc_no lcNo, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.issuing_date issuingDate, ");
        sql.append("        lc.expiry_date expiryDate, ");
        sql.append("        lc.credit_money creditMoney, ");
        sql.append("        lc.issuing_cur_id issuingCurId, ");
        sql.append("        lc.cur_name curName, ");
        sql.append("        lc.beneficiary beneficiary, ");
        sql.append("        lc.status status,");
        sql.append("        lc.purpose purpose");
        sql.append(" from LC_ENROL lc ");
        sql.append(" where 1 = 1 and (lc.status = 99 or lc.status = 2 or lc.status = 3 or lc.status = 95) and lc.lc_type = '0' and ");
        sql.append(getSQLIn(corpIds, 900, "lc.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcEnrolVO.class, "lc", false, null, null);
    }
    /**
     * 审批列表
     *
     * @param dataTablesInput
     * @param ids
     * @return DataTablesOutput
     */
    public DataTablesOutput listSupplementCreditApprove(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  lc.id id, ");
        sql.append("        lc.lc_no lcNo, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.issuing_date issuingDate, ");
        sql.append("        lc.expiry_date expiryDate, ");
        sql.append("        lc.credit_money creditMoney, ");
        sql.append("        lc.issuing_cur_id issuingCurId, ");
        sql.append("        lc.cur_name curName, ");
        sql.append("        lc.beneficiary beneficiary, ");
        sql.append("        lc.status status,");
        sql.append("        lc.purpose purpose");
        sql.append(" from LC_ENROL lc ");
        sql.append(" where 1 = 1 and lc.status >=21 and lc.status < 95 and lc.lc_type = '0'  and ");
        sql.append(getSQLIn(ids, 900, "lc.id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcEnrolVO.class, "lc", false, null, null);
    }
    /**
     * 查询该信用证号是否重复
     *
     * @param params
     * @return long
     */
    public long getcreditNoCount(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder();
        hql.append("select ");
        hql.append("count(creditNo) ");
        hql.append(" from ");
        hql.append(" LcEnrol ");
        hql.append(" where ");
        hql.append(" 1 = 1 and lcType = '0' and creditNo = :creditNo");
        return countHqlResult(hql.toString(), params);
    }
    public DataTablesOutput<GuaContractInternationalVO> getGuaForInternational(DataTablesInput input, List<String> corpIds, List<String> businessIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select lc.applicant,lc.lc_no as billCode,lc.id as id,lc.beneficiary as beneficiary, ");
        sql.append(" lc.issuing_bank as issuingBank,lc.bank_name as bankName,lc.credit_no as creditNo,lc.issuing_cur_id as curId, ");
        sql.append(" lc.lc_inter as lcInter,lc.credit_money as creditMoney,lc.contract_no as contractNo,lc.cur_name as curName, ");
        sql.append(" lc.issuing_date as issuingDate,lc.expiry_date as expiryDate,lc.applicant as corpId,lc.applicant_corp_name as corpName ");
        sql.append(" from lc_enrol lc where lc.is_hang_guarantee = '1' and lc.lc_type = '0' ");
        sql.append(" and " + getSQLIn(businessIdList, 900, "lc.id"));
        if (EmptyUtils.isNotEmpty(corpIds)) {
            sql.append(" and " + getSQLIn(corpIds, 900, "lc.applicant"));
        } else {
            sql.append(" and 1 <> 1 ");
        }
        return getDynamicBySQL(input, sql.toString(), GuaContractInternationalVO.class, "gco", false);
    }
}
