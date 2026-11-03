package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.entity.AbPoolBailBook;
import com.bytter.bill.domain.billPool.vo.AbPoolBailBookVO;
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
 * @Title: AbPoolBailBookDao
 * @Description: 保证金登记簿Dao
 * @author wangzy
 * @date 2020/3/17 17:11
 *
 */
@Repository
public class AbPoolBailBookDao   extends BaseDao<AbPoolBailBook, String> {
    /**
     * 通用查询数据sql
     *
     * @return
     */
    public String getCommonSql(boolean isBusQuery) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select bail.id as id,bail.type as type,bail.bail_acc as bailAcc,bail.money as money,bail.trans_date as transDate, ");
        sql.append(" bail.OPP_BANK_NAME as oppBankName, bail.OPP_BANK_NO as oppBankNo, bail.OPP_ACC_NAME as oppAccName, ");
        //sql.append(" bail.OPP_ACC as oppAcc,BT_CONCAT4('(',s.code,')',s.name) as corpCodeAndName, ");
        sql.append(" bail.OPP_ACC as oppAcc,BT_CONCAT4('(',pc.corp_code,')',pc.corp_name) as corpCodeAndName,");
        sql.append(" BT_CONCAT4('(',p.BP_CODE,')',p.BP_NAME) as pbCodeAndName,bail.CREATE_TIME as createTime from ");
        if(isBusQuery){
            //支持去业务数据表信息
            sql.append(" (select dtl.id as id,dtl.type as type,pb.bank_acc as bail_acc,dtl.money,dtl.APP_DATE as trans_date,dtl.CREATE_TIME, ");
            sql.append(" dtl.OPP_BANK_NAME,dtl.OPP_BANK_NO,dtl.OPP_ACC_NAME,dtl.OPP_ACC,pb.CORP_ID,pb.BP_ID from ");
            sql.append(" AB_POOL_BAIL_DTL dtl left join AB_POOL_BAIL pb on dtl.BAIL_ID=pb.id and pb.VALID_SIGN=:validSign ");
            sql.append(" and dtl.status=:status and dtl.opt_status=:optStatus ) ");
        }else {
            sql.append(" AB_POOL_BAIL_BOOK ");
        }
        sql.append(" bail left join ab_pool_corp pc on pc.corp_id=bail.corp_id and pc.valid_Sign=:validSign ");
        //sql.append(" left join sys_corp s on s.id=pc.corp_id ");
        sql.append(" left join ab_pool p on p.id=bail.bp_Id ");
        sql.append(" where 1=1 ");
        return sql.toString();
    }
    /**
     * 保证金登记簿列表查询
     * @param input
     * @return 列表信息
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        String etime = (String)input.getFormdata().get("led_transDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            input.getFormdata().put("led_transDate", etime);
        }
        StringBuilder sql = new StringBuilder(getCommonSql(true));
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "pc.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        params.put("status", AbPoolConstant.AB_POOL_STATUS95);
        params.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS19);
        return getDynamicBySQL(input, sql.toString(), AbPoolBailBookVO.class, "", false, params);
    }
    /**
     * 保证金登记簿信息查询
     * @param id 登记簿ID
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public AbPoolBailBookVO getAbPoolBailBookVOInfo(String id) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(getCommonSql(true));
        sql.append(" and bail.id=:id ");
        params.put("id", id);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        params.put("status", AbPoolConstant.AB_POOL_STATUS95);
        params.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS19);
        return getBySQLAliasToBean(sql.toString(), params, AbPoolBailBookVO.class);
    }
}
