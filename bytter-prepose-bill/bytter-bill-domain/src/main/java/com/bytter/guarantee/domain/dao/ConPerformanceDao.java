package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.vo.ConChangeVO;
import com.bytter.guarantee.domain.vo.ConPerformanceVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.guarantee.constant.ConConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @version V1.0
 * @Title: 合同履约业务Dao
 * @Description: 合同履约业务数据操作类
 * @author: zhenghj
 * @date: 2019-11-14
 */
@Repository
public class ConPerformanceDao extends BaseDao<ConPerformanceVO, String> {
    /**
     * 获取查询的sql语句
     *
     * @return
     */
    private StringBuilder getQuerySql() {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("ci.ID AS mid,ci.con_no as conNo,ci.con_name as conName,ci.corp_id as corpId,ci.sign_type as signType,ci.type_id as typeId,");
        sql.append("ci.attachment_id as conAttachmentId,ci.begin_date as beginDate,ci.end_date as endDate,ci.performance_num as performanceNum,");
        sql.append("ci.change_num as changeNum,ci.DRAFT_DATE as draftDate,ci.SIGN_DATE as signDate,cir.attachment_id as attachmentId,cir.rmk as rmk,");
        sql.append("cir.id as rid,cir.lastid as lastId,cir.bus_type as busType,cir.status as status,cir.back_msg as backMsg,cir.op_no as opNo,cir.op_money as opMoney,");
        sql.append("ct.con_name as typeName,ci.corp_code as corpCode,ci.corp_name as corpName,cir.id as businessKey,cir.op_date as opDate,cir.op_msg as opMsg ");
        sql.append(" from CON_INFO_RECORD cir ");
        sql.append(" left join CON_INFO ci on cir.mid = ci.id");
        sql.append(" left join CON_TYPE ct on ci.TYPE_ID = ct.id");
        return sql;
    }
    /**
     * 合同履约列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String userId) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE cir.VALID_SIGN in (:validSign)  ");
        sql.append(" and cir.BUS_TYPE = :busType ");
        sql.append(" and cir.STATUS <>:status ");
        sql.append(" and (cir.CREATE_ID = :userId )");// 变更人操作限制：只能由变更人处理
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", new String[] { ConConstant.CON_VALID_SIGN_0, ConConstant.CON_VALID_SIGN_1 });
        params.put("status", ConConstant.CON_OTHER_STATUS_99);
        params.put("busType", ConConstant.CON_BUS_TYPE_05);
        params.put("userId", userId);
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "ci.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append("  ");
        input.addOrder("conName", true);
        return getDynamicBySQL(input, sql.toString(), ConPerformanceVO.class, "", false, params);
    }
    /**
     * 根据流水Id查询合同履约信息
     *
     * @param rid
     *            合同履约流水id
     * @return
     */
    public List<ConPerformanceVO> getConPerformanceVOByRid(String rid) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE cir.id = :rid  ");
        Map<String, Object> params = new HashMap<>();
        params.put("rid", rid);
        List<ConPerformanceVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, ConPerformanceVO.class);
        return listInfo;
    }
    /**
     * 根据合同主表Id查询合同流水信息
     *
     * @param mid
     *            合同主表id
     * @return
     */
    public List<ConPerformanceVO> getConPerformanceVOByMid(String mid) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE ci.id = :mid  ");
        Map<String, Object> params = new HashMap<>();
        params.put("mid", mid);
        List<ConPerformanceVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, ConPerformanceVO.class);
        return listInfo;
    }
    /**
     * 审批页面查询当前操作员可审批的合同履约数据
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "cir.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), ConPerformanceVO.class, "", false, params);
    }
}
