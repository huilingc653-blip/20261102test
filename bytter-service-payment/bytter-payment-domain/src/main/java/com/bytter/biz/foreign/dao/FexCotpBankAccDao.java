package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCotpBankAcc;
import com.bytter.biz.foreign.vo.FexCotpBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fusb
 *	@version 1.0
 *  @Title FexCotpBankAccDao
 *  @Description 交易对手银行账户管理Dao
 *  @date 2020年2月11日
 */
@Repository
public class FexCotpBankAccDao extends BaseDao<FexCotpBankAcc,String> {

    /**
     * 获取查询交易对手银行账户信息sql
     * @param
     * @return
     */
    public String getSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,");
        sql.append("       b.cotp_id as cotpId,");
        sql.append("       b.corp_id as corpId,");
        sql.append("       a.cotp_name as cotpName,");
        sql.append("       b.bank_acc as bankAcc,");
        sql.append("       b.iban_code as iBanCode,");
        sql.append("       b.acc_name as accName,");
        sql.append("       b.cur_id as curId,");
        sql.append("       b.rmk as rmk,");
        sql.append("       b.bank_id as bankId,");
        sql.append("       b.bank_name as bankName,");
        sql.append("       b.bic_code as bicCode,");
        sql.append("       b.pay_type as payType,");
        sql.append("       b.tran_bank_acc as tran_bank_acc,");
        sql.append("       b.bank_addr as bankAddr,");
        sql.append("       b.bank_rmk as bankRmk,");
        sql.append("       b.tran_bank as tranBank,");
        sql.append("       b.tran_bic_code as tranBicCode,");
        sql.append("       b.tran_bank_addr as tranBankAddr,");
        sql.append("       b.status as status,");
        sql.append("       b.create_by as createBy,");
        sql.append("       b.create_date as createDate,");
        sql.append("       b.update_by as updateBy,");
        sql.append("       b.update_date as updateDate ");
        return sql.toString();
    }

    /**
     * 交易对手银行账户管理列表数据
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder(this.getSql());
        sql.append("       from fex_cotp_bankacc b left join fex_cotp a on b.cotp_id=a.id ");
        sql.append("       left join fex_cotp c on c.id=b.bank_id left join fex_cotp d on d.id=b.tran_bank " );
        sql.append("       where b.status in(-1,0,11,95,-3) ");
        if(corpAuth!=null && corpAuth.size()>0){
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "b.corp_id "));
        }
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), FexCotpBankAccVO.class, "bt", false);
    }

    /**
     * 获取交易对手银行账户信息
     * @param id
     * @return
     */
    public FexCotpBankAccVO getFexFexCotpBankAcc(String id){
        StringBuilder sql = new StringBuilder(this.getSql());
        sql.append("       from fex_cotp_bankacc b left join fex_cotp a on b.cotp_id=a.id ");
//        sql.append("       left join fex_cotp c on c.id=b.bank_id left join fex_cotp d on d.id=b.tran_bank" );
        sql.append("       where b.id=:id ");
        Map<String,String> params = new HashMap<String,String>();
        params.put("id",id);
        return this.getBySQLAliasToBean(sql.toString(),params,FexCotpBankAccVO.class);
    }

    /**
     * 获取交易对手账户信息
     *
     * @param id
     * @return
     */
    public FexCotpBankAccVO getCotpBankAccById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.corp_id as corpId, b.cotp_id as cotpId,b.bank_acc as bankAcc, b.acc_name as accName,b.cur_id as curId, b.bic_code as bicCode, ");
        sql.append(" b.tran_bank_acc as tranBankAcc,b.bank_addr as bankAddr, b.tran_bank as tranBank, b.tran_bank_addr as tranBankAddr, b.bank_name as bankName");
        sql.append(" from fex_cotp_bankacc b where b.id=:id ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, FexCotpBankAccVO.class);
    }

    /**
     * 修改交易对手银行账户信息状态
     * @param id
     * @param status
     * @return
     */
    public void updateFexCotpBankAccSatus(String id,Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update fex_cotp_bankacc a set a.status=:status where id=:id ");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",id);
        params.put("status",status);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }

    /**
     * 交易对手银行账户管理审批数据
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getApproveDataTables(DataTablesInput dataTablesInput, String  corpIds) {
        StringBuilder sql = new StringBuilder(this.getSql());
        sql.append("       from fex_cotp_bankacc b left join fex_cotp a on b.cotp_id=a.id ");
        sql.append("       left join fex_cotp c on c.id=b.bank_id left join fex_cotp d on d.id=b.tran_bank" );
        sql.append("       where 1=1");
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)){
            sql.append(" and b.status="+ ForeignConstant.STATUE_WAIT_CHECK);
        }
        if(StringUtils.isNoneBlank(corpIds)){
            sql.append(" and ").append(corpIds);
        }
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), FexCotpBankAccVO.class, "bt", false);
    }

    public List<FexCotpBankAccVO> isRepeatData(FexCotpBankAccVO vo) {
        StringBuilder sql=new StringBuilder();
        sql.append("select b.*  from fex_cotp_bankacc b  ");
        sql.append("where 1 = 1 and b.bank_acc=:bankAcc ");
        sql.append(" and b.cur_id=:curId and b.status<>-2 ");
        Map<String,Object> params=new HashMap<>();
//        params.put("cotpId", vo.getCotpId());
        params.put("bankAcc", vo.getBankAcc());
        params.put("curId",vo.getCurId());
        return listBySQLAliasToBean(sql.toString(), params, FexCotpBankAccVO.class);
    }

    /**
     * 获取交易对手银行账户下拉框数据
     * @return
     */
    public List<FexCotpBankAccVO> getSelectFexCotpBankAcc(String cotpId,String curCode,String bankId){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder(this.getSql());
        sql.append("       from fex_cotp_bankacc b left join fex_cotp a on b.cotp_id=a.id ");
        sql.append("       left join fex_cotp c on c.id=b.bank_id left join fex_cotp d on d.id=b.tran_bank " );
        sql.append("       where b.status=95 and c.status=95");//控制交易对手账号对应的交易对手也必须是审批通过
        if (StringUtils.isNotBlank(bankId)){
            sql.append("  and  b.bank_id=:bankId ");
            params.put("bankId",bankId);
        }
        if (StringUtils.isNotBlank(cotpId)){
            sql.append("  and  b.cotp_id=:cotpId ");
            params.put("cotpId",cotpId);
        }
        if (StringUtils.isNotBlank(curCode)){
            sql.append(" and (b.CUR_ID  = :curCode or exists (select id from fex_cotp_bankacc_cur cur where cur.bank_acc_id=b.id and cur.cur_id=:curCode)) ");
            params.put("curCode",curCode);
        }
        return this.listBySQLAliasToBean(sql.toString(),params,FexCotpBankAccVO.class);
    }

    /**
     * 根据交易对手id获取交易对手账户信息
     * @param cotpId
     * @return
     */
    public FexCotpBankAcc getFexCotpBankAccByCotpId(String cotpId){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder hql = new StringBuilder();
        hql.append("from FexCotpBankAcc where cotpId =:cotpId ");
        params.put("cotpId", cotpId);
        //return (FexCotpBankAcc)this.createQuery(hql.toString()).setProperties(params).uniqueResult();
        List<FexCotpBankAcc> list = this.listByHQLAliasToBean(hql.toString(),params,FexCotpBankAcc.class);
        if (EmptyUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取交易对手银行账户下拉框数据（关联币种表）
     * @return
     */
    public List<FexCotpBankAccVO> getSelectFexCotpBankAccJoinCur(String cotpId,String curCode,String bankId){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,");
        sql.append("       b.cotp_id as cotpId,");
        sql.append("       b.corp_id as corpId,");
        sql.append("       a.cotp_name as cotpName,");
        sql.append("       b.bank_acc as bankAcc,");
        sql.append("       b.iban_code as iBanCode,");
        sql.append("       b.acc_name as accName,");
        sql.append("       bc.cur_id as curId,");
        sql.append("       cur.english_Code as curName,");
        sql.append("       b.rmk as rmk,");
        sql.append("       b.bank_id as bankId,");
        sql.append("       c.cotp_name as bankName,");
        sql.append("       b.bic_code as bicCode,");
        sql.append("       b.pay_type as payType,");
        sql.append("       b.tran_bank_acc as tran_bank_acc,");
        sql.append("       b.bank_addr as bankAddr,");
        sql.append("       b.bank_rmk as bankRmk,");
        sql.append("       b.tran_bank as tranBank,");
        sql.append("       d.cotp_name as tranBankName,");
        sql.append("       b.tran_bic_code as tranBicCode,");
        sql.append("       b.tran_bank_addr as tranBankAddr,");
        sql.append("       c.local_Clear_No as localClearNo,");
        sql.append("       b.status as status,");
        sql.append("       b.create_by as createBy,");
        sql.append("       b.create_date as createDate,");
        sql.append("       b.update_by as updateBy,");
        sql.append("       b.update_date as updateDate ");
        sql.append("       from fex_cotp_bankacc b left join fex_cotp a on b.cotp_id=a.id ");
        sql.append("       left join fex_cotp c on c.id=b.bank_id left join fex_cotp d on d.id=b.tran_bank " );
        sql.append(" left join fex_cotp_bankacc_cur bc on bc.bank_acc_id = b.id");
        sql.append(" left join bt_currency cur on cur.id=bc.cur_id");
        sql.append("       where b.status=95 and c.status=95");//控制交易对手账号对应的交易对手也必须是审批通过
        if (StringUtils.isNotBlank(bankId)){
            sql.append("  and  b.bank_id=:bankId ");
            params.put("bankId",bankId);
        }
        if (StringUtils.isNotBlank(cotpId)){
            sql.append("  and  b.cotp_id=:cotpId ");
            params.put("cotpId",cotpId);
        }
        if (StringUtils.isNotBlank(curCode)){
            sql.append("  and bc.cur_id=:curCode ");
            params.put("curCode",curCode);
        }
        return this.listBySQLAliasToBean(sql.toString(),params,FexCotpBankAccVO.class);
    }
}
