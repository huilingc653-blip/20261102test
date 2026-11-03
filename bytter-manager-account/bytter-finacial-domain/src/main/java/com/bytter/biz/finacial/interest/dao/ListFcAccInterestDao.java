package com.bytter.biz.finacial.interest.dao;

import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.biz.finacial.interest.vo.ListFcAccInterestVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class ListFcAccInterestDao extends BaseDao<FcInterestDtl,String> {
    /**
     * 计息清单查询
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput findlistBySql(DataTablesInput dataTablesInput,String account,String orderNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" d.id as id,");
        sql.append(" d.account as blnctrAcc,");
        sql.append(" d.interest_start_date as interestStartDate,");
        sql.append(" d.bln_inst_date as blnInstDate,");
        sql.append(" d.days as days,");
        sql.append(" d.interest_rate as interestRate,");
        sql.append(" d.interest as interest,");
        sql.append(" d.cumulation as cumulation,");
        sql.append(" d.interest_time as interestTime");
        sql.append(" from fc_interest_dtl d ");
        sql.append("where 1=1 ");
        sql.append(" and d.account = '" + account + "'"); // 条件
        if(StringUtils.isNotBlank(orderNo)) {
            sql.append(" and d.order_no = '" + orderNo + "'");
        } else {
            sql.append(" and (d.order_no is null or d.order_no='')");
        }
        sql.append(" and d.cumulation <> 0");
        OrderString orderString = new OrderString("d.interest_start_date,d.bln_inst_date","asc");
        dataTablesInput.setOrderString(orderString);
        dataTablesInput.getFormdata().remove("ins_corpId");
        dataTablesInput.getFormdata().remove("eqs_netId");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ListFcAccInterestVO.class, "d", false);
    }
    /**
     * 查询账户和流水号分类数据
     * @param dataTablesInput
     * @return 新增参数 List<String> blnctrAccLists，用于in 过滤
     */
    public List<ListFcAccInterestVO> getTotal(DataTablesInput dataTablesInput, List<String> blnctrAccLists){
        Map<String, Object> inputParams = dataTablesInput.getFormdata();
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.order_no orderNo,d.account blnctrAcc");
        sql.append(" from fc_interest_dtl d ");
        sql.append("where 1=1 ");
        sql.append(" and d.cumulation <> 0");
        //a.blnctr_acc = d.account 所以在上层查询，传入 blnctrAccLists
        /*if (StringUtils.isNotEmpty(inputParams.get("likes_blnctrAcc").toString())) {
            sql.append(" and d.account like '%" + inputParams.get("likes_blnctrAcc").toString() + "%'");
        }*/
        if (StringUtils.isNotEmpty(inputParams.get("ged_interestTime").toString())) {
            sql.append(" and d.interest_time >= bt_to_date('" + inputParams.get("ged_interestTime").toString() + "','yyyy-mm-dd')");
        }
        if (StringUtils.isNotEmpty(inputParams.get("led_interestTime").toString())) {
            sql.append(" and d.interest_time <= bt_to_date('" + inputParams.get("led_interestTime").toString() + "','yyyy-mm-dd')");
        }
        if (StringUtils.isNotEmpty(inputParams.get("ged_interestStartDate").toString())) {
            sql.append(" and d.interest_start_date >=  bt_to_date('" + inputParams.get("ged_interestStartDate").toString() + "','yyyy-mm-dd')");
        }
        if (StringUtils.isNotEmpty(inputParams.get("led_interestStartDate").toString())) {
            sql.append(" and d.interest_start_date <=  bt_to_date('" + inputParams.get("led_interestStartDate").toString() + "','yyyy-mm-dd')");
        }
        if (ListUtils.isNotNullOrEmpty(blnctrAccLists)){
            sql.append(" and d.account in (:blnctrAccLists) ");
        }
        sql.append(" group by d.account,d.order_no");
        sql.append(" order by d.account, d.order_no desc");
        Map<String, Object> params = new HashMap<>();
        params.put("blnctrAccLists",blnctrAccLists);
        return listBySQLAliasToBean(sql.toString(), params, ListFcAccInterestVO.class);
    }
    /**
     * 通过accName返回对象的集合
     * @param params
     * @return
     */
    public List<ListFcAccInterestVO> getFcAccInterestDtl(Map<String,Object> params){
        StringBuilder sql=new StringBuilder ();
        sql.append(" select ");
        sql.append(" d.id as id,");
        sql.append(" d.account as blnctrAcc,");
        sql.append(" d.interest_start_date as interestStartDate,");
        sql.append(" d.bln_inst_date as blnInstDate,");
        sql.append(" d.days as days,");
        sql.append(" d.interest_rate as interestRate,");
        sql.append(" d.interest as interest,");
        sql.append(" d.cumulation as cumulation,");
        sql.append(" d.interest_time as interestTime,");
        sql.append(" d.checker_code as checkerCode,");
        sql.append(" d.operator_code as operatorCode,");
        sql.append(" d.interest_type as interestType, ");
        sql.append(" d.rmk rmk, ");
        sql.append(" d.interest_sign interestSign, ");
        sql.append(" d.generate_mode as generateMode, ");
        sql.append(" d.order_no as orderNo ");
        sql.append(" from fc_interest_dtl d ");
//        sql.append(" left join fc_acc a ");
//        sql.append(" on d.account = a.blnctr_acc "); // 这个是拆分的关联 条件
        sql.append (" where 1 = 1" );
        sql.append(" and d.cumulation <> 0");
        if (null != params && StringUtils.isNotEmpty(params.get("likes_blnctrAcc"))) {
            sql.append(" and d.account like '%" + params.get("likes_blnctrAcc").toString() + "%'");
        }
        if (null != params && StringUtils.isNotEmpty(params.get("ged_interestTime"))) {
            sql.append (" and d.interest_time >= :ged_interestTime ");
        }
        if (null != params && StringUtils.isNotEmpty(params.get("led_interestTime"))) {
            sql.append (" and d.interest_time <= :led_interestTime ");
        }
        if (null != params && StringUtils.isNotEmpty(params.get("ged_interestStartDate"))) {
            sql.append (" and d.interest_start_date >= :ged_interestStartDate ");
        }
        if (null != params && StringUtils.isNotEmpty(params.get("led_interestStartDate"))) {
            sql.append (" and d.interest_start_date <= :led_interestStartDate ");
        }
        if (null != params && null != params.get("id")) {
            List<String> id = (List<String>) params.get("id");
            if (ListUtils.isNotNullOrEmpty(id)) {
                sql.append (" and d.id in (:id)");
            }
        }
        sql.append(" order by d.account, d.order_no desc,d.interest_start_date ");
        return listBySQLAliasToBean(sql.toString(), params, ListFcAccInterestVO.class);
    }
}
