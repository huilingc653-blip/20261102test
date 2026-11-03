package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.vo.ConSignVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @version V1.0
 * @Title: ContractSignedDao.java
 * @Package com.bytter.biz.contract.dao
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-11 上午11:18:39
 */
@Repository
public class ContractSignedDao extends BaseDao<ConSignVO, String> {
    protected static StringBuilder publicSqlVoid() {
        return new StringBuilder().append("select ")
                .append("ci.ID AS mid,ci.con_no as conNo,ci.con_name as conName,ci.corp_id as corpId,ci.sign_type as signType,ci.type_id as typeId,ci.status as cStatus,")
                .append("ci.attachment_id as attachmentId,ci.begin_date as beginDate,ci.end_date as endDate,ci.performance_num as performanceNum,")
                .append("ci.change_num as changeNum,ci.DRAFT_DATE as draftDate,ci.SIGN_DATE as signDate,")
                .append("cir.id as rid,cir.lastid as lastId,cir.bus_type as busType,cir.back_msg as backMsg,ct.CON_NAME as typeName, ")
                .append("ci.corp_code as corpCode,ci.corp_name as corpName,")
                .append(" case when cir.status=95 and  cir.bus_type=01 then 0").append(" else cir.status end as status")
                .append(" from CON_INFO_RECORD cir ")
                .append(" left join CON_INFO ci on cir.mid = ci.id ")
                .append(" left join CON_TYPE ct on ci.type_id = ct.id ")
                .append(" WHERE 1=1 ");
    }
    /**
     * 合同签署列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String userId) {
        StringBuilder sql = new StringBuilder(ContractSignedDao.publicSqlVoid());
        Map<String, Object> params = new HashMap<>();
        sql.append("  and ci.status <> -99  ");
        sql.append(" and (cir.CREATE_ID = :userId )");// 登记人操作限制：只能由签署人处理
        params.put("userId", userId);
        if (StringUtils.isNotEmpty(input.getFormdata().get("eqs_statusName"))) {
            if (input.getFormdata().get("eqs_statusName").equals(ConConstant.CON_VALID_SIGN_0)) {
                sql.append("  and  cir.BUS_TYPE =:busType and cir.status=:state and cir.VALID_SIGN =:validSign");
                params.put("validSign", ConConstant.CON_VALID_SIGN_1);
                params.put("busType", ConConstant.CON_BUS_TYPE_01);
                params.put("state", ConConstant.CON_SIGN_STATUS_95);
                input.getFormdata().remove("eqs_statusName");
            } else {
                sql.append("  and  cir.BUS_TYPE =:busType and cir.VALID_SIGN in (:validSign)");
                params.put("validSign", new String[]{ConConstant.CON_VALID_SIGN_0, ConConstant.CON_VALID_SIGN_1});
                params.put("busType", ConConstant.CON_BUS_TYPE_02);
            }
        } else {
            sql.append("  and ((cir.BUS_TYPE =:busTypeOne and cir.status=:state and cir.VALID_SIGN in (:validSign))");
            sql.append(" or ");
            sql.append(" (cir.BUS_TYPE =:busTypeTwo and cir.VALID_SIGN in (:valueEnd)))");
            params.put("validSign", new String[]{ConConstant.CON_VALID_SIGN_0, ConConstant.CON_VALID_SIGN_1});
            params.put("valueEnd", new String[]{ConConstant.CON_VALID_SIGN_0, ConConstant.CON_VALID_SIGN_1});
            params.put("busTypeOne", ConConstant.CON_BUS_TYPE_01);
            params.put("busTypeTwo", ConConstant.CON_BUS_TYPE_02);
            params.put("state", ConConstant.CON_DRAFT_STATUS_95);
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "ci.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append("  ");
        input.addOrder("conName", true);
        return this.getDynamicBySQL(input, sql.toString(), ConSignVO.class, null, false, params);
    }
    /**
     * 审批页面查询当前操作员可审批的合同待签署数据
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        StringBuilder sql = new StringBuilder(ContractSignedDao.publicSqlVoid());
        Map<String, Object> params = new HashMap<>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "cir.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), ConSignVO.class, "", false, params);
    }
}
