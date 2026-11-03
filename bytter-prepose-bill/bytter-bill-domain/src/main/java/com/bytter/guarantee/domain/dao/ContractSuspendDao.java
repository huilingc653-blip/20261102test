package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.domain.vo.ContractSuspendVO;
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
 *
 * @Title: 合同终止VO
 * @Package com.bytter.biz.contract.dao
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-13 下午4:34:09
 * @version V1.0
 */
@Repository
public class ContractSuspendDao extends BaseDao<ContractSuspendVO, String> {
    protected static StringBuilder publicSqlVoid() {
        return new StringBuilder().append("select ").append(
                "ci.ID AS mid,ci.con_no as conNo,ci.con_name as conName,ci.corp_id as corpId,ci.sign_type as signType,ci.type_id as typeId,ci.status as cStatus,")
                .append("ci.attachment_id as attachmentId,ci.begin_date as beginDate,ci.end_date as endDate,ci.performance_num as performanceNum,")
                .append("cir.attachment_id as reAttachmentIde,")
                .append("ci.change_num as changeNum,ci.DRAFT_DATE as draftDate,ci.SIGN_DATE as signDate,")
                .append("cir.id as rid,cir.lastid as lastId,cir.bus_type as busType,cir.back_msg as backMsg,  ")
                .append("ct.con_name as typeName,ci.corp_code as corpCode,ci.corp_name as corpName,")
                .append(" case when cir.status=95 and (cir.bus_type=02 or cir.bus_type=03) then 1")
                .append(" else cir.status end as status,").append(" ci.status as ciStatus,")
                .append(" cir.id as businessKey,cir.OP_MSG as opMsg,cir.OP_DATE as opDate")
                .append(" from CON_INFO_RECORD cir ").append(" left join CON_INFO ci on cir.mid = ci.id ")
                .append(" left join CON_TYPE ct on ci.TYPE_ID = ct.id ")
                .append(" WHERE 1=1 ");
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String userId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(ContractSuspendDao.publicSqlVoid());
        sql.append("  and ci.status <> -99  ");
        // sql.append(" and (cir.CREATE_ID = :userId )" );//登记人操作限制：只能由签署人处理
        if (StringUtils.isNotEmpty(input.getFormdata().get("eqs_apprveStatusName"))) {
            if (input.getFormdata().get("eqs_apprveStatusName").equals(ConConstant.CON_VALID_SIGN_1)) {
                sql.append(" and cir.BUS_TYPE in(:busTypeOne) and cir.status=:state and cir.VALID_SIGN =:validSign");
                params.put("busTypeOne", new String[] { ConConstant.CON_BUS_TYPE_02, ConConstant.CON_BUS_TYPE_03 });
                params.put("state", ConConstant.CON_DRAFT_STATUS_95);
                params.put("validSign", ConConstant.CON_VALID_SIGN_1);
                input.getFormdata().remove("eqs_apprveStatusName");
            } else {
                sql.append(" and cir.BUS_TYPE =:busTypeTwo and cir.VALID_SIGN in (:valueEnd) ");
                sql.append(" and cir.CREATE_ID = :userId ");
                params.put("busTypeTwo", ConConstant.CON_BUS_TYPE_06);
                params.put("userId", userId);
                params.put("valueEnd", new String[] { ConConstant.CON_VALID_SIGN_0, ConConstant.CON_VALID_SIGN_1 });
            }
        } else {
            sql.append("  and ((cir.BUS_TYPE in(:busTypeOne) and cir.status=:state and cir.VALID_SIGN =:validSign) ");
            sql.append(" or ");
            sql.append(" (cir.BUS_TYPE =:busTypeTwo and cir.VALID_SIGN in (:valueEnd) and cir.CREATE_ID = :userId))");
            params.put("busTypeOne", new String[] { ConConstant.CON_BUS_TYPE_02, ConConstant.CON_BUS_TYPE_03 });
            params.put("state", ConConstant.CON_DRAFT_STATUS_95);
            params.put("validSign", ConConstant.CON_VALID_SIGN_1);
            params.put("userId", userId);
            params.put("busTypeTwo", ConConstant.CON_BUS_TYPE_06);
            params.put("valueEnd", new String[] { ConConstant.CON_VALID_SIGN_0, ConConstant.CON_VALID_SIGN_1 });
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "ci.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append("  ");
        input.addOrder("conName", true);
        return this.getDynamicBySQL(input, sql.toString(), ContractSuspendVO.class, null, false, params);
    }
    /**
     * 查询终止详细信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public ContractSuspendVO querySuspendById(String rid) {
        StringBuilder sql = new StringBuilder(ContractSuspendDao.publicSqlVoid());
        sql.append(" and cir.id = :rid  ");
        Map<String, Object> params = new HashMap<>();
        params.put("rid", rid);
        List<ContractSuspendVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, ContractSuspendVO.class);
        return StringUtils.isNotEmpty(listInfo) && listInfo.size() > 0 ? listInfo.get(0) : null;
    }
    /**
     *
     * @Title: dataTableApprove @Description: TODO(查询审批页面信息) @param @param input @param @param
     *         businessList @param @return 参数 @return DataTablesOutput 返回类型 @throws
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        StringBuilder sql = new StringBuilder(ContractSuspendDao.publicSqlVoid());
        Map<String, Object> params = new HashMap<>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "cir.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), ContractSuspendVO.class, "", false, params);
    }
    public DataTablesOutput<ContractSuspendVO> getQueryDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String userId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder().append("select ")
                .append("ci.ID AS mid,ci.con_no as conNo,ci.con_name as conName,ci.corp_id as corpId,ci.sign_type as signType,ci.type_id as typeId,")
                .append("ci.attachment_id as attachmentId,ci.begin_date as beginDate,ci.end_date as endDate,ci.performance_num as performanceNum,")
                .append("ci.change_num as changeNum,ci.DRAFT_DATE as draftDate,ci.SIGN_DATE as signDate,")
                .append("ct.con_name as typeName,ci.corp_code as corpCode,ci.corp_name as corpName,")
                .append(" ci.status as ciStatus").append(" ,b.op_date as opDate").append(" from CON_INFO ci ")
                .append(" left join CON_TYPE ct on ci.TYPE_ID = ct.id ")
                .append(" left join (select  cir.* from con_info_record cir where cir.bus_type=06 and cir.status in(11,95) and cir.VALID_SIGN in(0,1)")
                .append(" ) b on ci.id=b.mid ").append(" WHERE 1=1 ");
        sql.append("  and ci.status <> -99  ");
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "ci.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append("  ");
        Map<String, Object> formdata = input.getFormdata();
        String transDate = (String) formdata.get("led_transDate");
        // 手动拼接时间条件，时间戳类型
        if (StringUtils.isNotEmpty(transDate)) {
            transDate += " 23:59:59";
            formdata.put("led_transDate", transDate);
            input.setFormdata(formdata);
        }
        input.addOrder("conName", true);
        return this.getDynamicBySQL(input, sql.toString(), ContractSuspendVO.class, null, false, params);
    }
}
