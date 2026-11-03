package com.bytter.biz.netbank.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.netbank.entity.NisDemand;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BisAccBalDTO;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 *资金需求单
 */
@Repository
public class NisDemandDao extends BaseDao<NisDemand, String> {
    /**
     * 资金需求单通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.bank_acc as bankAcc,n.bank_acc_id as bankAccId,");
        sql.append(" n.acc_name as accName,n.bank_name as bankName,n.demand_type as demandType,n.demand_date as demandDate,");
        sql.append(" n.demand_money as demandMoney,n.demand_allocation_money as demandAllocationMoney, n.pay_bill_id as payBillId,");
        sql.append(" n.pay_bill_no as payBillNo,n.pay_bill_date as payBillDate,n.super_corp_id as superCorpId,n.super_bank_acc as superBankAcc, ");
        sql.append(" n.super_bank_acc_id as superBankAccId,n.super_acc_name as superAccName,n.super_bank_name as superBankName,n.status as status, ");
        sql.append(" n.allocation_no as allocationNo,n.allocation_id as allocationId,b.trans_time as allocationDate,n.is_summary as isSummary, ");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate, ");
        sql.append(" n.corp_level as corpLevel,n.super_id as superId,n.tally_sign as tallySign,n.voucher_no as voucherNo,");
        sql.append(" n.is_converge as isConverge,n.is_pos as isPos,n.fail_reason as failReason,n.pos_status as posStatus,n.pos_num as posNum,");
        // 其他表
        sql.append(" b.voucher_stat as voucherStat,corp1.name as corpName,corp2.name as superCorpName ");
        sql.append(" from nis_demand n  ");
        sql.append(" left join sys_corp corp1 on n.corp_id = corp1.id ");
        sql.append(" left join sys_corp corp2 on n.super_corp_id = corp2.id ");
        sql.append(" left join bis_exc b on n.allocation_id = b.id ");
        sql.append(" where 1=1 ");
        return sql.toString();
    }


    /**
     * 根据id获取资金需求单
     *
     * @param id
     * @return
     */
    public NisDemandVO getByIdUnion(String id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(this.getCommonSql() + " and  n.id=:id", params, NisDemandVO.class);
    }

    /**
     * 根据id获取资金需求单
     * @param ids
     * @return
     */
    public List<NisDemandVO> getByIdUnion(List<String> ids) {
        String sql = this.getCommonSql();
        String whereSql = " and "+SqlUtil.getInSql(ids,"n.id",true);
        return listBySQLAliasToBean(sql+whereSql, null, NisDemandVO.class);
    }

    /**
     * 根据billNo获取资金需求单
     *
     * @param billNo
     * @return
     */
    public NisDemandVO getByBillNoUnion(String billNo) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        return getBySQLAliasToBean(this.getCommonSql() + " and  n.bill_No=:billNo", params, NisDemandVO.class);
    }

    /**
     * 查询资金需求单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<NisDemandVO> getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisDemandVO.class, "n", false);
    }

    /**
     * 根据单据id获取需求单
     * @param payBillId
     * @return
     */
    public List<NisDemandVO>  listByPayBillId(String payBillId){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.pay_bill_id = :payBillId and n.status in ('0','1')  ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("payBillId", payBillId);
        return listBySQLAliasToBean(sql.toString(),params,NisDemandVO.class);
    }

    /**
     * 根据单据id获取需求单 不带状态
     * @param payBillId
     * @return
     */
    public List<NisDemandVO>  listByPayBillIdNoStatus(String payBillId){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.pay_bill_id = :payBillId ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("payBillId", payBillId);
        return listBySQLAliasToBean(sql.toString(),params,NisDemandVO.class);
    }

    /**
     * 带原状态更新需求单
     * @param vo
     * @param oldStatus
     * @return
     */
    public int updateNisDemandStatus(NisDemandVO vo, Integer[] oldStatus) {
        StringBuilder hql = new StringBuilder();
        hql.append(" update NisDemand set corpId=:corpId,bankAcc = :bankAcc,bankAccId=:bankAccId,accName=:accName,bankName=:bankName,");
        hql.append(" demandType=:demandType,demandDate=:demandDate,demandMoney=:demandMoney,demandAllocationMoney=:demandAllocationMoney,");
        hql.append(" payBillId=:payBillId,payBillNo=:payBillNo,payBillDate=:payBillDate,superCorpId=:superCorpId,superBankAcc=:superBankAcc,");
        hql.append(" superBankAccId=:superBankAccId,superAccName=:superAccName,superBankName=:superBankName,status=:status,");
        hql.append(" allocationNo=:allocationNo,allocationId=:allocationId,allocationDate=:allocationDate,isSummary=:isSummary ");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", vo.getCorpId());
        params.put("bankAcc", vo.getBankAcc());
        params.put("bankAccId", vo.getBankAccId());
        params.put("accName", vo.getAccName());
        params.put("bankName", vo.getBankName());
        params.put("demandType", vo.getDemandType());
        params.put("demandDate", vo.getDemandDate());
        params.put("demandMoney", vo.getDemandMoney());
        params.put("demandAllocationMoney", vo.getDemandAllocationMoney());
        params.put("payBillId", vo.getPayBillId());
        params.put("payBillNo", vo.getPayBillNo());
        params.put("payBillDate", vo.getPayBillDate());
        params.put("superCorpId", vo.getSuperCorpId());
        params.put("superBankAcc", vo.getSuperBankAcc());
        params.put("superBankAccId", vo.getSuperBankAccId());
        params.put("superAccName", vo.getSuperAccName());
        params.put("superBankName", vo.getSuperBankName());
        params.put("status", vo.getStatus());
        params.put("allocationNo", vo.getAllocationNo());
        params.put("allocationId", vo.getAllocationId());
        params.put("allocationDate", vo.getAllocationDate());
        params.put("isSummary", vo.getIsSummary());

        if (ObjectUtil.isNotEmpty(vo.getVoucherNo())){
            hql.append(",voucherNo=:voucherNo ");
            params.put("voucherNo", vo.getVoucherNo());
        }
        if (ObjectUtil.isNotEmpty(vo.getTallySign())){
            hql.append(",tallySign=:tallySign ");
            params.put("tallySign", vo.getTallySign());
        }

        if (StringUtils.isNotEmpty(vo.getCreateBy())) {
            hql.append(",createBy=:createBy ");
            params.put("createBy", vo.getCreateBy());
        }
        if (vo.getCreateDate() != null) {
            hql.append(",createDate=:createDate ");
            params.put("createDate", vo.getCreateDate());
        }
        if (StringUtils.isNotEmpty(vo.getUpdateBy())) {
            hql.append(",updateBy=:updateBy ");
            params.put("updateBy", vo.getUpdateBy());
        }
        if (vo.getUpdateDate() != null) {
            hql.append(",updateDate=:updateDate ");
            params.put("updateDate", vo.getUpdateDate());
        }
        hql.append(" where id=:id and status in (:oldStatus) ");
        params.put("id", vo.getId());
        params.put("oldStatus", oldStatus);
        return this.batchExecute(hql.toString(), params);
    }

    /**
     * 查询所有待汇总的需求单
     * @return
     */
    public List<NisDemandVO> listByAutoSummary(List<String> demandTypes){
        Map<String, Object> params = new HashMap<>();
        String sql = " select * from nis_demand where (is_summary = 0 or IS_SUMMARY is null) and STATUS = 1 ";
        sql += " and bank_acc_id is not null and super_bank_acc_id  is not null ";
        sql += " and BT_TO_CHAR(demand_date, 'yyyy-MM-dd') =:queryDate ";
        sql += " and demand_type in (:demandTypes) ";
        params.put("queryDate", DateUtils.formatDate(DateUtils.getDate()));
        params.put("demandTypes", demandTypes);
        List<NisDemandVO> nisDemandVOList = this.listBySQLAliasToBean(sql,params,NisDemandVO.class);
        return nisDemandVOList;
    }

    /**
     * 更新汇总标志
     * @param ids
     * @return
     */
    public int updateSummary(List<String> ids){
        String sql = " update nis_demand a set is_summary = '1' where 1=1 " +
                " and " + getSQLIn(ids, 900, "a.id");
        return  createSQLQuery(sql).executeUpdate();
    }

    /**
     * 查询待生成指令的需求单 付款类
     * @return
     */
    public List<NisDemandVO> listByAutoAllocationPay(){
        String sql = " select * from nis_demand where is_summary = 1 and status = 1 and demand_type = '01' and demand_date = :date order by corp_level ";
        Map<String,Object> param = new HashMap<>();
        String date = DateUtils.format(DateUtils.getDate(),"yyyy-MM-dd");
        param.put("date", date);
        List<NisDemandVO> nisDemandVOList = this.listBySQLAliasToBean(sql,param,NisDemandVO.class);
        return nisDemandVOList;
    }

    /**
     * 查询待生成指令的需求单 付款类
     * @return
     */
    public List<NisDemandVO> listByAutoAllocationPayForPos(){
        String sql = " select * from nis_demand where is_summary = 1 and status >= 1 and demand_type = '01' and demand_date = :date and is_pos <> '1' order by corp_level ";
        Map<String,Object> param = new HashMap<>();
        String date = DateUtils.format(DateUtils.getDate(),"yyyy-MM-dd");
        param.put("date", date);
        List<NisDemandVO> nisDemandVOList = this.listBySQLAliasToBean(sql,param,NisDemandVO.class);
        return nisDemandVOList;
    }

    /**
     * 查询待生成下拨指令的需求单
     * @return
     */
    public List<NisDemandVO> listByAutoAllocation(String demandType){
        String sql = " select n.*,c.`NAME` corpName ";
        sql += " from nis_demand n ";
        sql += " LEFT JOIN sys_corp c ON n.CORP_ID = c.ID ";
        sql += " where n.is_summary = 1 and n.status = 1  and n.demand_date = :date ";
        sql += " and n.demand_type = :demandType ";
        sql += " order by n.corp_level  ";
        Map<String,Object> param = new HashMap<>();
        String date = DateUtils.format(DateUtils.getDate(),"yyyy-MM-dd");
        param.put("date", date);
        param.put("demandType", demandType);
        List<NisDemandVO> nisDemandVOList = this.listBySQLAliasToBean(sql,param,NisDemandVO.class);
        return nisDemandVOList;
    }

    /**
     * 根据ids 更新上级id
     * @param ids
     * @param superId
     */
    public int updateNisDemandSetSuperId(List<String> ids, String superId,Boolean isSummary) {
        Map<String,Object> params = new HashMap<>();
        String sql = "";
        if(isSummary){
            sql = " update nis_demand n set n.super_id = :superId,n.status = -2 where 1=1 and " + getSQLIn(ids, 900, "n.id");
        }else{
            sql = " update nis_demand n set n.super_id = :superId,n.is_summary = '1' where 1=1 and " + getSQLIn(ids, 900, "n.id");
        }
        params.put("superId", superId);
        return  createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 根据ids 更新上级id
     * @param id
     * @param superId
     */
    public int updateNisDemandSetSuperId(String id, String superId) {
        String sql = " update nis_demand n set n.super_id = :superId where n.id = :id ";
        Map<String,Object> params = new HashMap<>();
        params.put("superId", superId);
        params.put("id", id);
        return  createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 根据ids 更新上级id
     * @param ids
     * @param superId
     */
    public int updateNisDemandSetSuperIdbySuperId(List<String> ids, String superId) {
        Map<String,Object> params = new HashMap<>();
        String sql = " update nis_demand n set n.super_id = :superId,n.is_summary = '1' where 1=1 and " + getSQLIn(ids, 900, "n.super_id");
        params.put("superId", superId);
        return  createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    public int updateNisDemandClearSuperId(String superId){
        Map<String,Object> params = new HashMap<>();
        String sql = " update nis_demand n set n.super_id = '',n.is_summary = '0',n.status = 1 where  n.super_id = :superId ";
        params.put("superId", superId);
        return  createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 更新是否生成调拨单状态
     * @param id
     * @param isPos
     * @return
     */
    public int updatenisDemandIsPos(String id,String isPos,int posNum){
        Map<String,Object> params = new HashMap<>();
        String sql = " update nis_demand n set n.is_pos = :isPos,n.pos_status = :posStatus,n.pos_num = :posNum where  n.id = :id ";
        params.put("id", id);
        params.put("isPos", isPos);
        params.put("posStatus", NisDemandConstant.WAIT_RESULT);
        params.put("posNum", posNum);
        return  createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /***
     * 查询留底金额
     * @param bankAcc
     * @return TODO
     */
    public BigDecimal getLeaveMoney(String bankAcc) {
        BigDecimal result = new BigDecimal(0);
        Map<String,Object> params = new HashMap<>();
        params.put("bankAcc",bankAcc);
        // 先查询 银承账户留底金额配置
        String sql = " SELECT b.KEEP_AMT bal FROM bill_acc_keep_amt b WHERE b.`STATUS` = 1 AND b.BANK_ACC = :bankAcc ";
        BisAccBalDTO bisAccBalDTO = this.getBySQLAliasToBean(sql,params, BisAccBalDTO.class);
        if (ObjectUtil.isNotEmpty(bisAccBalDTO)){
            result = bisAccBalDTO.getBal();
        }else{
            // 再查归集留底金额
            sql = "SELECT \n" +
                    "    sub.GATHER_AMT bal \n" +
                    "FROM (\n" +
                    "    SELECT \n" +
                    "        t.BANK_ACC, t.ACC_NAME, t.BANK_NAME,\n" +
                    "        bg.GATHER_POLICY_NAME, bg.GATHER_POLICY_CODE,\n" +
                    "        bg.GATHER_AMT, bg.GATHER_MODE,\n" +
                    "        ROW_NUMBER() OVER (ORDER BY bg.GATHER_AMT DESC) AS rn   \n" +
                    "    FROM BIS_ACC_GATHER_POLICY v \n" +
                    "    LEFT JOIN bt_bank_acc t ON t.ID = v.BANK_ACC_ID\n" +
                    "    LEFT JOIN BIS_GATHER_POLICY bg ON v.GATHER_POLICY_ID = bg.ID\n" +
                    "    WHERE t.BANK_ACC = :bankAcc \n" +
                    "    AND bg.GATHER_MODE = '1'\n" +
                    ") sub\n" +
                    "WHERE sub.rn = 1 ";
            bisAccBalDTO = this.getBySQLAliasToBean(sql,params, BisAccBalDTO.class);
            if (ObjectUtil.isNotEmpty(bisAccBalDTO)){
                result = bisAccBalDTO.getBal();
            }
        }
        return result;
    }

    public void updateNisDemandStatusSuccess(List<String> ids,String updateBy) {
        Map<String,Object> params = new HashMap<>();
        String sql = " update nis_demand n set n.status = :status,n.update_date = NOW()  ";
        if (ObjectUtil.isNotEmpty(updateBy)){
            sql += " ,n.update_by = :updateBy ";
            params.put("updateBy", updateBy);
        }
        sql += " where n.id in ( :ids ) ";
        params.put("status", NisDemandConstant.STATUE_ALLOCATION);
        params.put("ids", ids);
        this.createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    public void updateNisDemandStatus(String id,Integer status, String failReason,String updateBy) {
        Map<String,Object> params = new HashMap<>();
        String sql = " update nis_demand n set  n.update_date = NOW() ";
        if (ObjectUtil.isNotEmpty( status)){
            sql += " ,n.status = :status ";
            params.put("status", status);
        }
        if (ObjectUtil.isNotEmpty(failReason)){
            sql += " ,n.fail_reason = :failReason ";
            params.put("failReason", failReason);
        }
        if (ObjectUtil.isNotEmpty(updateBy)){
            sql += " ,n.update_by = :updateBy ";
            params.put("updateBy", updateBy);
        }

        sql += " where n.id = :id ";
        params.put("id", id);
        this.createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    public void updateNisDemandPosStatus(List<String> ids,String posStatus) {
        String sql = " update nis_demand n set n.pos_status = :status where n.id in ( :ids ) ";
        Map<String,Object> params = new HashMap<>();
        params.put("status", posStatus);
        params.put("ids", ids);
        this.createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    public void updateNisDemandSummaryStatus(List<String> idList, Integer status, String isSummary, String updateBy) {
        Map<String,Object> params = new HashMap<>();
        String sql = " update nis_demand n set  n.update_date = NOW(),n.status = :status,n.is_summary = :isSummary ";
        params.put("status", status);
        params.put("isSummary", isSummary);

        if (ObjectUtil.isNotEmpty(updateBy)){
            sql += " ,n.update_by = :updateBy ";
            params.put("updateBy", updateBy);
        }

        sql += " where n.id in (:ids) ";
        params.put("ids", idList);
        this.createSQLQuery(sql).setProperties(params).executeUpdate();
    }


    public void updateNisDemandSuperId(String superId, String demandType, Date demandDate, String superBankAccId, String updateBy) {
        XxlJobHelper.log("根据父账号Id：{}类型：{}日期：{}更新上级单据Id:{}",superBankAccId, demandType,  DateUtil.formatDate(demandDate), superId);
        Map<String,Object> params = new HashMap<>();
        String sql = " update nis_demand n set  n.update_date = NOW() ";
        if (ObjectUtil.isNotEmpty(updateBy)){
            sql += " ,n.update_by = :updateBy ";
            params.put("updateBy", updateBy);
        }
        sql += " ,n.super_id = :superId where n.demand_type = :demandType and BT_TO_CHAR(n.demand_date, 'yyyy-MM-dd') = :demandDate ";
        sql += " and n.super_bank_acc_id = :superBankAccId and n.status = :status  ";
        params.put("superId", superId);
        params.put("demandType", demandType);
        params.put("demandDate", DateUtil.formatDate(demandDate));
        params.put("superBankAccId", superBankAccId);
        params.put("status", NisDemandConstant.STATUE_CHECK);
        this.createSQLQuery(sql).setProperties(params).executeUpdate();

    }
}
