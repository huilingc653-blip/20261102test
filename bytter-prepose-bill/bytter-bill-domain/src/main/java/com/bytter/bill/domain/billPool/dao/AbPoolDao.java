package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @version V1.0
 * @Title: 票据池业务Dao
 * @Description: 票据池业务数据操作类
 * @author: zhenghj
 * @date: 2020-1-3
 */
@Repository
public class AbPoolDao extends BaseDao<AbPool, String> {
    /**
     * 获取查询的sql语句
     *
     * @return
     */
    private StringBuilder getQuerySql(String selectSql) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("AP.ID AS id,AP.BP_CODE AS bpCode,AP.BP_NAME AS bpName,AP.RMK AS rmk,AP.PIF_ID AS pifId,AP.BP_TYPE AS bpType,");
        sql.append("AP.BP_MODE AS bpMode,AP.BP_ORG AS bpOrg,AP.VALID_SIGN AS validSign,API.PIF_NAME as pifName,API.BIF_CODE AS bifCode  ");
        sql.append(selectSql);
        sql.append(" FROM AB_POOL AP ");
        sql.append(" LEFT JOIN AB_POOLIF API ON AP.PIF_ID=API.ID ");
        return sql;
    }
    /**
     * 票据池列表查询
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbPoolVO> getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder(getQuerySql(""));
        sql.append(" WHERE AP.VALID_SIGN in (:validSign)  ");
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        return getDynamicBySQL(input, sql.toString(), AbPoolVO.class, "", false, params);
    }
    /**
     * 根据参数查询票据池接口信息
     *
     * @param params 查询参数
     * @return
     */
    public List<AbPoolVO> getAbPoolVOByParams(Map<String, Object> params) {
        String selectPledgee = ",pp.BANK_ACC as bankAcc,pp.ACC_NAME as accName,pp.BANK_NO as bankNo,pp.BANK_NAME as bankName";
        StringBuilder sql = null;
        if (params.containsKey("pledgeeValidSign")) {
            sql = new StringBuilder(getQuerySql(selectPledgee));
            sql.append(" LEFT JOIN AB_POOL_PLEDGEE pp ON pp.bp_Id=AP.ID ");
        }else{
            sql = new StringBuilder(getQuerySql(""));
        }
        sql.append(" WHERE 1=1  ");
        if (params.containsKey("id")) {
            sql.append(" and AP.id=:id ");
        }
        if (params.containsKey("validSign")) {
            sql.append(" and AP.VALID_SIGN in (:validSign) ");
        }
        if (params.containsKey("bpCode")) {
            sql.append(" and AP.bp_Code=:bpCode ");
        }
        if (params.containsKey("bpName")) {
            sql.append(" and AP.bp_Name=:bpName ");
        }
        if (params.containsKey("pifId")) {
            sql.append(" and AP.pif_Id=:pifId ");
        }
        if (params.containsKey("notId")) {
            sql.append(" and AP.id <> :notId ");
        }
        if (params.containsKey("pledgeeValidSign")) {
            sql.append(" and pp.VALID_SIGN =:pledgeeValidSign ");
        }
        sql.append(" order by AP.bp_Code desc");
        return this.listBySQLAliasToBean(sql.toString(), params, AbPoolVO.class);
    }
    /**
     * 根据ids批量修改票据池接口数据
     *
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update AbPool "
                + " set "
                + "updateId = :updateId "
                + ",updateTime = :updateTime ");
        if (params.containsKey("validSign")) {
            hql.append(",validSign = :validSign ");
        }
        hql.append(" where ").append(SqlUtil.getInSqlByParams("ids","id",params));
        if (params.containsKey("validSign")) {
            hql.append(" and validSign<>:validSign ");
        }
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 获取有效票据池（银行自动配置的银行有效且为票据池开票开启的银行）
     *
     * @return 票据池
     */
    public List<AbPoolVO> getAbPoolByBankAuto(String bankTypeId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = null;
        sql = new StringBuilder(getQuerySql(""));
        // sql.append(" left join BIS_BIF_INIT bbi on bbi.bif_code=api.bif_code");
        sql.append(" left join AB_BILL_BANK_AUTO_DTL ad on ad.NEW_BIF_CODE=api.bif_code");
        sql.append(" where ap.valid_sign=1 and api.valid_sign=1  and ad.valid_sign=1 and ad.pool_emitbill=1 ");
        if(StringUtils.isNotEmpty(bankTypeId)){
            sql.append(" and ad.bank_type_id=:bankTypeId");
            params.put("bankTypeId",bankTypeId);
        }
        sql.append(" order by AP.bp_Name desc");
        List<AbPoolVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolVO.class);
        return listInfo;
    }
}
