package com.bytter.biz.finacial.interest.dao;

import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.interest.bean.AccTypeInterestInfoBean;
import com.bytter.biz.finacial.interest.entity.FcInterestSingle;
import com.bytter.biz.finacial.interest.vo.FcInterestDtlVo;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class FcInterestSingleDdaDao extends BaseDao<FcInterestSingle, String> {
    public DataTablesOutput<FcInterestSingleVO> queryFcInterestSingleDda(DataTablesInput input, String userId, String commonSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (");
        sql.append("SELECT fis.id,fis.INTEREST_TIME,fis.CORP_ID,fis.ACC_TYPE,fis.ACCOUNT,fis.START_DATE,fis.END_DATE,fis.MONEY,fis.INTEREST,fis.TALLY_DATE,fis.ORDER_NO,fis.CHECKER_CODE,fis.CHECK_DATE,fis.PAY_WAY,fis.STATUS,\n" +
                "fis.AFFIX_AMOUNT,fis.BUS_GLOBAL_ID,fis.CREATE_BY,fis.CREATE_DATE,fis.UPDATE_BY,fis.UPDATE_DATE,fis.ACC_NAME,fis.CUR_ID,fis.POOL_SIGN, ");
        sql.append("fa.NET_ID from FC_INTEREST_SINGLE fis ");
        sql.append(" LEFT JOIN FC_ACC fa on FIS.CORP_ID=fa.CORP_ID and FIS.ACCOUNT=Fa.BLNCTR_ACC");
        sql.append(") t where 1=1 ");
        /*sql.append("select * from (");
        sql.append("SELECT FIS.* from FC_INTEREST_SINGLE fis ");
        sql.append(") t where 1=1 ");*/
        //默认查询当前登录人所有管理单位
        if (EmptyUtils.isNotEmpty(commonSql)) {
            sql.append(commonSql);
        }
        //业务状态
        String eqs_status = (String) input.getFormdata().get("eqs_status");
        if (EmptyUtils.isEmpty(eqs_status)) {
            String opType = (String) input.getFormdata().get("opType");
            if (EmptyUtils.isNotEmpty(opType) && opType.equals("checkList")) {
                sql.append(" and t.status = ").append(InterestConstant.INTEREST_WAIT_CHECK);
//                sql.append(" and t.create_by <> '").append(userId).append("'");//复核界面数据只能复核他人经办数据
                input.getFormdata().put("nes_createBy", userId);
            } else {
                //经办-界面加载暂存、打回数据
                sql.append(" and t.status in(").append(InterestConstant.INTEREST_STATUE_TEMP).append(",").append(InterestConstant.INTEREST_BACK).append(")");
//                sql.append(" and t.create_by = '").append(userId).append("'");//经办界面数据只能自己新增的数据
                input.getFormdata().put("eqs_createBy", userId);
            }
        }
        input.getFormdata().remove("opType");
        return getDynamicBySQL(input, sql.toString(), FcInterestSingleVO.class, "t", false);
    }
    public FcInterestSingleVO getFcInterestSingleDda(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT fis.* from FC_INTEREST_SINGLE fis ");
        sql.append(" where fis.id=:id");
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FcInterestSingleVO.class);
    }
    /**
     * 查询暂存利息明细的记账日期和起息日期
     *
     * @param netId     网点id
     * @param accType   账户类别 01-企业活期存款
     * @param account   内部账号
     * @param userId    登录用户id
     * @param busStatus 业务状态
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AccTypeInterestInfoBean> queryAccTypeInterestInfo(String netId, String accType, String account, String userId, Integer[] busStatus) {
        Map<String, Object> param = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct dtl.ACC_TYPE,dtl.BLN_INST_DATE,dtl.TALLY_DATE ");
        sql.append("from FC_INTEREST_DTL_TEMP dtl ");
        sql.append("where   dtl.NET_id = :netId ");
        sql.append("and dtl.acc_type = :accType ");
        sql.append("and dtl.account = :account ");
        sql.append("and dtl.BUS_STATUS in (:busStatus) ");
        sql.append("and dtl.USER_SESSION_ID = :userId ");
        sql.append("and dtl.generate_mode = :generate_mode ");
        param.put("netId", netId);
        param.put("accType", accType);
        param.put("account", account);
        param.put("busStatus", busStatus);
        param.put("userId", userId);
        param.put("generate_mode", InterestConstant.GENERATEMODE_INTEREST_PART);
        return listBySQLAliasToBean(sql.toString(), param, AccTypeInterestInfoBean.class);
    }
    /**
     * 查询指定账号是否存在个别计息
     *
     * @param account
     * @return
     */
    public FcInterestSingleVO queryFcInterestSingle(String account) {
        String sql = "SELECT * from FC_INTEREST_SINGLE where ACCOUNT = :account and STATUS <> :status and status <> :deleteStatus ";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("status", InterestConstant.INTEREST_STATUE_CHECK);
        params.put("deleteStatus", InterestConstant.INTEREST_STATUE_DELETE);
        return getBySQLAliasToBean(sql, params, FcInterestSingleVO.class);
    }
    public void updateFcInterestSingleById(List<String> ids, String userId, Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE FC_INTEREST_SINGLE set CHECKER_CODE=?, CHECK_DATE=?, STATUS=? ");
        sql.append(" where 1=1 ").append(" and ").append(SqlUtil.getInSql(ids, "id", true));
        this.createSQLQuery(sql.toString(), userId, DateUtils.getDate(), status).executeUpdate();
    }
    /**
     * <br>
     * <b>功能：</b> 删除个别计息数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/20 0020 <br>
     */
    public void deleteInterestSingle(FcInterestSingle entity, Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE FC_INTEREST_SINGLE set STATUS = :status ");
        sql.append(" where id = :id and status = :oldStatus ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("status", status);
        params.put("oldStatus", entity.getStatus());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
    * <br>
    * <b>功能：</b> 查询个别计息数据 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/20 0020 <br>
    */
    public DataTablesOutput listAccount(DataTablesInput input, List<String> accounts) {
        /*StringBuilder sql = new StringBuilder();
        sql.append("select dtl.account,acc.acc_name as accName, c.cur_name,dtl.cur_code,dtl.interest_start_date,");
        sql.append("dtl.bln_inst_date,dtl.days,dtl.cumulation,dtl.interest_rate,dtl.interest,dtl.interest_sign,dtl.interest_time");
        sql.append(" from fc_interest_dtl dtl ");
        sql.append(" inner join fc_interest_single fis on fis.account = dtl.account and fis.id = dtl.single_id ");
        sql.append(" inner join fc_acc acc on acc.blnctr_acc = dtl.account");
        sql.append(" left join  bt_currency c on dtl.cur_code = c.cur_code");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_id"));*/
        StringBuilder sql = new StringBuilder();
        sql.append("select dtl.account,dtl.cur_code,dtl.interest_start_date,");
        sql.append("dtl.bln_inst_date,dtl.days,dtl.cumulation,dtl.interest_rate,dtl.interest,dtl.interest_sign,dtl.interest_time");
        sql.append(" from fc_interest_dtl dtl ");
        sql.append(" inner join fc_interest_single fis on fis.account = dtl.account and fis.id = dtl.single_id ");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(accounts, 900, "dtl.account"));
        return getDynamicBySQL(input, sql.toString(), FcInterestDtlVo.class, "", false);
    }
}
