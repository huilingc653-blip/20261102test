package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbClaimAccount;
import com.bytter.bill.domain.bill.vo.AbClaimAccountVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class AbClaimAccountDao extends BaseDao<AbClaimAccount, String> {
    /**
     * 账号规则信息通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.id AS id,a.corp_id as corpId,a.CORP_NAME as corpName,a.bank_id AS bankId,a.bank_acc AS bankAcc, ");
        sql.append(" a.bank_name AS bankName,a.bank_type_id AS bankTypeId,a.BANK_TYPE_NAME as bankTypeName, ");
        sql.append(" a.bus_type AS busType,a.valid_sign as validSign ");
        sql.append(" FROM AB_CLAIM_ACCOUNT a ");
//        sql.append(" left join sys_corp corp on a.corp_id = corp.id ");
//        sql.append(" left join bt_bank_type banktype on a.bank_type_id = banktype.id ");
        sql.append(" where 1=1 ");
        return sql.toString();
    }
    /**
     * 根据单位和业务类型查询有效账号规则
     *
     * @param params 参数map
     * @return 有效账号规则集合
     */
    public List<AbClaimAccountVo> listAbClaimAccountInfo(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(" SELECT a.id AS id,a.corp_id as corpId,a.bank_id AS bankId,a.bank_acc AS bankAcc, ");
        sql.append(" a.bank_name AS bankName,a.bank_type_id AS bankTypeId,a.bus_type AS busType,a.valid_sign as validSign ");
        sql.append(" FROM AB_CLAIM_ACCOUNT a ");
        sql.append(" where 1=1 ");
        sql.append(" and valid_sign = :validSign ");
        sql.append(" and corp_id = :corpId ");
        sql.append(" and bus_type in (:busType) ");
        params.put("busType", new String[]{String.valueOf(params.get("businessType")), "4"});
        params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        return this.listBySQLAliasToBean(sql.toString(), params, AbClaimAccountVo.class);
    }
    /**
     * 列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbClaimAccountInfo(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder(" SELECT a.id AS id,a.corp_id as corpId,a.corp_name as corpName,a.bank_id AS bankId,a.bank_acc AS bankAcc, ");
        sql.append(" a.bank_name AS bankName,a.bank_type_id AS bankTypeId,a.BANK_TYPE_NAME AS bankTypeName,a.bus_type AS busType,a.valid_sign as validSign ");
        sql.append(" FROM AB_CLAIM_ACCOUNT a ");
        sql.append(" where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbClaimAccountVo.class, "", false);
    }
    /**
     * 根据ID获取账号规则信息
     *
     * @param id
     * @return
     */
    public AbClaimAccountVo getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and a.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, AbClaimAccountVo.class);
    }
    public void deleteAbClaimAccount(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from AB_CLAIM_ACCOUNT where id=:id ");
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        this.createSQLQuery(sql.toString(), params).executeUpdate();
    }
    public List<AbClaimAccountVo> getByRepeat(AbClaimAccountVo abClaimAccountVo) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        HashMap<String, String> params = new HashMap<String, String>();
        if (!StringUtils.isBlank(abClaimAccountVo.getCorpId())) {
            sql.append(" AND a.corp_id = :corpId");
            params.put("corpId", abClaimAccountVo.getCorpId());
        }
        if (!StringUtils.isBlank(abClaimAccountVo.getBankId())) {
            sql.append(" AND a.bank_id = :bankId");
            params.put("bankId", abClaimAccountVo.getBankId());
        }
        if (!StringUtils.isBlank(abClaimAccountVo.getBusType())) {
            sql.append(" AND a.bus_type = :busType");
            params.put("busType", abClaimAccountVo.getBusType());
        }
        if (StringUtil.isNotEmpty(abClaimAccountVo.getId())) {
            //编辑需排除本身
            sql.append(" AND a.id <> :id");
            params.put("id", abClaimAccountVo.getId());
        }
        List<AbClaimAccountVo> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbClaimAccountVo.class);
        return listInfo;
    }
}
