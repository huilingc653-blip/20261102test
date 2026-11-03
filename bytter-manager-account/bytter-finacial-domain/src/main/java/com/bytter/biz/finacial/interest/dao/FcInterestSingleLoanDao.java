package com.bytter.biz.finacial.interest.dao;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.FcAccDTO;
import com.bytter.biz.finacial.interest.bean.AccTypeInterestInfoBean;
import com.bytter.biz.finacial.interest.entity.FcInterestSingle;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.IFcAccService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
@Repository
public class FcInterestSingleLoanDao extends BaseDao<FcInterestSingle, String> {
    @DubboReference(check = false)
    IFcAccService iFcAccService;
    public DataTablesOutput queryFcInterestSingleLoan(DataTablesInput input, String userId, String commonSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (");
        sql.append(" SELECT FIS.* from FC_INTEREST_SINGLE fis ");
        sql.append(") t where 1=1 ");
        // 默认查询当前登录人所有管理单位POOL_SIGN
        if (EmptyUtils.isNotEmpty(commonSql)) {
            sql.append(commonSql);
        }
        // 业务状态
        String eqs_status = (String) input.getFormdata().get("eqs_status");
        if (EmptyUtils.isEmpty(eqs_status)) {
            String opType = (String) input.getFormdata().get("opType");
            if (EmptyUtils.isNotEmpty(opType) && opType.equals("checkList")) {
                sql.append(" and t.status = ").append(InterestConstant.INTEREST_WAIT_CHECK);
                //复核界面数据只能复核他人经办数据
                input.getFormdata().put("nes_createBy", userId);
            } else {
                //经办-界面加载暂存、打回数据
                sql.append(" and t.status in(").append(InterestConstant.INTEREST_STATUE_TEMP).append(",").append(InterestConstant.INTEREST_BACK).append(")");
                //经办界面数据只能自己新增的数据
                input.getFormdata().put("eqs_createBy", userId);
            }
        } else {
            sql.append(" and t.status = '").append(eqs_status).append("' ");
        }
        input.getFormdata().remove("opType");
        input.getFormdata().remove("eqs_status");
        return getDynamicBySQL(input, sql.toString(), FcInterestSingleVO.class, "", false);
    }
    public FcInterestSingleVO getFcInterestSingleLoan(String id) {
        StringBuilder sql = new StringBuilder();
        /*sql.append("SELECT fis.*,fa.CUR_ID,fa.NET_ID,fa.POOL_SIGN from FC_INTEREST_SINGLE fis ");
        sql.append(" LEFT JOIN FC_ACC fa on FIS.CORP_ID=fa.CORP_ID and FIS.ACCOUNT=Fa.BLNCTR_ACC");*/
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
    public List<AccTypeInterestInfoBean> queryAccTypeInterestInfo(List<String> accounts, String userId, Integer[] busStatus) {
        Map<String, Object> param = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select dtl.BLN_INST_DATE as blnInstDate,dtl.TALLY_DATE as tallyDate ");
        sql.append("from FC_INTEREST_DTL_TEMP dtl ");
        sql.append("where dtl.account in (:accounts) ");
        sql.append("and dtl.BUS_STATUS in (:busStatus) ");
        sql.append("and dtl.USER_SESSION_ID = :userId ");
        param.put("accounts", accounts);
        param.put("busStatus", busStatus);
        param.put("userId", userId);
        List<AccTypeInterestInfoBean> aliasToBean = listBySQLAliasToBean(sql.toString(), param, AccTypeInterestInfoBean.class);
        return aliasToBean;
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
     * <b>功能：</b> 更新个别计息复核状态 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/29 0029 <br>
     */
    public void updateFcInterestSingleById(List<String> ids, String userId, Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE FC_INTEREST_SINGLE set CHECKER_CODE=?, CHECK_DATE=?, STATUS=? ");
        sql.append(" where 1=1 ").append(" and ").append(SqlUtil.getInSql(ids, "id", true));
        ;
        this.createSQLQuery(sql.toString(), userId, DateUtils.getDate(), status).executeUpdate();
    }
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    public List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, List<String> accState, String joinSql) {
        List<com.bytter.system.api.dto.FcAccDTO> fcAccDTOLis = iFcAccService.getFcAccByCorpIdList(corpIdList, accState, joinSql);
        // StringBuilder sql = new StringBuilder("select t.id as id,");
        //        sql.append("t.blnctr_Acc as blnctrAcc,");
        //        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        //        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        //        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        //        sql.append("t.balance_Way as balanceWay,corp.name as corpName,t.acc_open_time as accOpenTime,t.acc_start_date as accStartDate, ");
        //        sql.append("t.interest_start_date as interestStartDate,t.deadline_date as deadlineDate,t.acc_State as accState,");
        //        sql.append("f.subject_name as subjectName,cur.cur_name as curName,fc.check_balance as fcBalance ");
        //        sql.append(" from Fc_Acc t left join fc_Subject f on t.subject_id=f.id ");
        //        sql.append(" left join sys_corp net on t.net_id=net.id ");
        //        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
        //        sql.append(" left join fc_online_ledger fc on fc.account = t.blnctr_acc ");
        //        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        //
        //        Map<String, Object> params = new HashMap<String, Object>();
        //        if(!EmptyUtils.isEmpty(corpIdList)) {
        //            sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "t.corp_id", true));
        //        }
        //        if (!EmptyUtils.isEmpty(accState)) {
        //            sql.append(" and ").append(SqlUtil.getInSql(accState, "t.acc_State", true));
        //        }
        //        sql.append(joinSql);
        //        sql.append(" and not exists (select 1 from cms_loan_send send where send.loan_acc = t.blnctr_acc ) ");
        //        sql.append(" order by t.blnctr_Acc ");
        List<String> accList = fcAccDTOLis.stream().map(com.bytter.system.api.dto.FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        StringBuilder sql = new StringBuilder("select fc.ACCOUNT as blnctrAcc, fc.check_balance as fcBalance");
        sql.append(" from fc_online_ledger fc ");
        sql.append(" where fc.account in(:accList)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accList", accList);
        List<FcAccDTO> fcBalance = listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
        Map<String, FcAccDTO> fcBalanceMap = fcBalance.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, dto -> dto));
        List<FcAccDTO> fcAccDTOS = new ArrayList<>();
        for (com.bytter.system.api.dto.FcAccDTO fc : fcAccDTOLis) {
            FcAccDTO dto = BeanUtils.copyNewInstanceProperties(fc, FcAccDTO.class);
            if (Objects.nonNull(fcBalanceMap.get(dto.getBlnctrAcc()))) {
                dto.setFcBalance(fcBalanceMap.get(dto.getBlnctrAcc()).getFcBalance());
            }else {
                dto.setFcBalance(BigDecimal.ZERO);
            }
            fcAccDTOS.add(dto);
        }
        return fcAccDTOS;
    }
}
