package com.bytter.biz.receivable.dao;
import com.bytter.biz.receivable.entity.NisReceivable;
import com.bytter.biz.receivable.vo.NisReceivableVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 收款通知表
 * @date 2019-07-02 17:30:33
 */
@Repository
public class NisReceivableDao extends BaseDao<NisReceivable, String> {
    /**
     * 收款通知表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id, n.bill_no as billNo, n.corp_id as corpId, t.id as busTypeId, n.bus_type as busType, t.bus_type_name as busTypeName, n.bank_acc as bankAcc,");
        sql.append("n.acc_name as accName, n.bank_name as bankName, n.cur_id as curId, n.amt  as amt, n.opp_acc_no as oppAccNo, n.opp_acc_name as oppAccName,");
        sql.append("n.opp_bank_code as oppBankCode, n.opp_acc_bank as oppAccBank,  n.rmk as rmk, n.bill_from as billFrom, n.status as status,");
        sql.append("n.bank_serial_no as bankSerialNo, n.trans_time as transTime, n.attachment_id as attachmentId, n.create_by as createBy, n.create_date as createDate,");
        sql.append("n.update_by as updateBy, n.update_date as updateDate, n.approve_Date as approveDate");
        sql.append(" ,n.bill_id,n.bus_bill_no,n.sys_bus_type");
        sql.append(" from nis_receivable n left join nis_bus_type t on n.bus_type = t.id where 1=1 ");
        return sql.toString();
    }
    /**
     * 查询收款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisReceivableVO.class, "n", false);
    }
    /**
     * 根据收款ID获取收款信息
     *
     * @param id
     * @return
     */
    public NisReceivableVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, NisReceivableVO.class);
    }
    /**
     * 根据收款单号获取收款信息
     *
     * @param billNo
     * @return
     */
    public NisReceivableVO getByBillNoUnion(String billNo) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.bill_no=:billNo and n.status <> -2 ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        return getBySQLAliasToBean(sql.toString(), params, NisReceivableVO.class);
    }
    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and  n.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        if(!EmptyUtils.isEmpty(businessSql)){
            sql.append(" and ").append(businessSql);
        }
        //审批时间
        String gedApproveDate = (String) dataTablesInput.getFormdata().get("ged_approveDate");
        String ledApproveDate = (String) dataTablesInput.getFormdata().get("led_approveDate");
        if(StringUtils.isNotEmpty(gedApproveDate)){
            gedApproveDate = gedApproveDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_createDate", gedApproveDate);
        }
        if(StringUtils.isNotEmpty(ledApproveDate)){
            ledApproveDate = ledApproveDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_createDate", ledApproveDate);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisReceivableVO.class, "n", false);
    }
    /**
     * 获取审批单位集合
     *
     * @param businessSql
     * @return
     */
    public List<String> getCorpIdsByDataTableApprove(String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.corp_Id from nis_receivable n left join nis_bus_type t on n.bus_type = t.id where ");
        sql.append(businessSql);
        return this.getSession().createSQLQuery(sql.toString()).list();
    }
    /**
     * 根据银行流水号查询收款单信息
     *
     * @param bankSerialNo 银行流水号
     */
    public List<NisReceivableVO> getNisReceivableByBankSerialNo(String bankSerialNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(this.getCommonSql());
        sql.append(" and n.bank_serial_no =:bankSerialNo");
        sql.append(" and n.status <> "+ BtBankAccConstant.STATUE_DEL);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("bankSerialNo", bankSerialNo);
        return listBySQLAliasToBean(sql.toString(), params, NisReceivableVO.class);
    }
    /**
     * 根据流水信息查询收款信息
     *
     * @param vo 收款信息
     */
    public List<NisReceivableVO> getNisRecivableByInfo(NisReceivableVO vo) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        sql.append(this.getCommonSql());
        sql.append(" and n.bank_acc =:bankAcc");
        sql.append(" and n.amt =:amt");
        sql.append(" and n.cur_id =:curId");
        if(!StringUtils.isNotBlank(vo.getOppAccNo())){
            sql.append(" and n.opp_acc_no =:oppAccNo");
            params.put("oppAccNo", vo.getOppAccNo());
        }
        params.put("bankAcc", vo.getBankAcc());
        params.put("amt", vo.getAmt());
        params.put("curId", vo.getCurId());
        return listBySQLAliasToBean(sql.toString(), params, NisReceivableVO.class);
    }
    /**
     * 根据银行流水号清空流水号关联
     *
     * @param bankSerialNo 银行流水号
     */
    public void updateNisReceivableByBankSerialNo(String bankSerialNo) {
        String sql = " update nis_receivable n set n.bank_serial_no = '',n.status = 95 where n.bank_serial_no = :bankSerialNo ";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("bankSerialNo", bankSerialNo);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }
    /**
     * 根据单位统计收款信息
     * @param corpId
     * @return
     */
    public int getReceivableCountByCorpId(String corpId){
        String sql="select count(*) from nis_receivable where corp_id=:corpId";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql+=" and status in ('0','11','-1')";
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
    /**
     * 根据单位业务类型查询收款单
     * @param corpId
     * @param busType
     * @return
     */
    public List<NisReceivableVO> getNisRecivableByBusType(String corpId,String busType) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        sql.append(this.getCommonSql());
        sql.append(" and n.status <> -2 and n.corp_id =:corpId");
        sql.append(" and n.bus_type =:busType");
        params.put("corpId", corpId);
        params.put("busType", busType);
        return listBySQLAliasToBean(sql.toString(), params, NisReceivableVO.class);
    }
    /**
     * 根据收款单号获取收款信息
     * @param billId
     * @return
     */
    public NisReceivableVO getByBillId(String billId) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.BILL_ID = :billId and (n.status = -1 or n.status = 0) ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billId", billId);
        return getBySQLAliasToBean(sql.toString(), params, NisReceivableVO.class);
    }
}
