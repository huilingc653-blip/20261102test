package com.bytter.cashVoucher.dao;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.cashVoucher.vo.BvPayeeChequeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.bytter.cashVoucher.entity.BvPayeeCheque;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: BvPayeeChequeDao
 * @Description: 支票收款登记数据操作类
 * @author Jinyong.Cao
 * @date 2018-06-13 13:17:17
 * @version 1.0
 *
 */
@Repository
public class BvPayeeChequeDao  extends BaseDao<BvPayeeCheque,String>{
	/**
	 *  查询支票收款入库凭证
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BvPayeeChequeVO> getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String userId) {
		StringBuilder sql=new StringBuilder("select ");
		if(input.getFormdata().containsKey("checkboxHide")){
			sql.append("case when bpc.status=1 then '1' when bpc.status = 6 then '1' when bpc.status = 3 then '1' when bpc.status = 4 then '1' else '0' end as checkboxHide,");
			input.getFormdata().remove("checkboxHide");
		}
		sql.append(" bpc.id as Id,bpc.corp_id as corpId ,bpc.orig_no as origNo,bpc.input_date as inputDate,bpc.bill_money as billMoney,");
		sql.append(" bpc.start_Date as startDate,bpc.end_Date as endDate,bpc.cur_Id as curId,bpc.cur_name as curName,bpc.cur_exrate as curExrate,");
		sql.append(" bpc.payee as payee,bpc.payee_bank_name as payeeBankName,bpc.payee_bank_no as payee_bank_no,bpc.payee_acc as payeeAcc,bpc.payer as payer,");
		sql.append(" bpc.payer_bank_name as payerBankName,bpc.payer_bank_no as payerBankNo,bpc.payer_acc as payerAcc,bpc.`use` as uses,bpc.status as status,bpc.corp_name as corpName,bpc.corp_code as corpCode ");
		sql.append(" From Bv_Payee_Cheque bpc");
		sql.append(" where 1=1 ");
		if(input.getFormdata().containsKey("eqs_id")){
			sql.append(" and bpc.id = '"+input.getFormdata().get("eqs_id")+"'");
		}
		if(input.getFormdata().containsKey("createBy")){
			sql.append(" and bpc.create_by != '"+input.getFormdata().get("createBy")+"'");
		}
		Map<String, Object> params = new HashMap<String, Object> ();
		if(corpAuthList.size()>0){
			String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "bpc.corp_id", params);
			sql.append(" and ").append(corpAuthSql).append(" ");
		}
		return getDynamicBySQL(input, sql.toString(), BvPayeeChequeVO.class, "", false,params);
	}
	/**
	 *  查询票据信息是否已存在 origNo
	 * @param bvPayeeCheque
	 * @return
	 */
	public boolean checkPayeeChequeNoUnique(BvPayeeCheque bvPayeeCheque,String operate){
		StringBuffer sql = new StringBuffer();
		sql.append(" select ID from Bv_Payee_Cheque ");
		sql.append(" where 1=1 ");
		sql.append(" and orig_no = :orig_no ");
		sql.append(" and status <> :status ");
		if(BlankvouConstant.BILL_OPERATE_STORAGE.equals(operate)){
			sql.append("  and id <> :id");
		}
		SQLQuery query=createSQLQuery(sql.toString());
		query.setString("orig_no", bvPayeeCheque.getOrigNo());
		query.setInteger("status", BlankvouConstant.BV_CHEQUE_STATUS_DELETE);
		if(BlankvouConstant.BILL_OPERATE_STORAGE.equals(operate)){
			query.setString("id", bvPayeeCheque.getId());
		}
		if(!query.list().isEmpty()){
			return false;
		}
		return true;
	}
	/**
	 * 根据ids批量操作数据
	 * @param params
	 * @return
	 */
	public int batchDeleteBvPayeeCheque(Map<String,Object> params,ICurrentUser user){
		String hql="update BvPayeeCheque set status = :status"
				+ ",updateBy = :updateBy "
				+ ",updateName = :updateName "
				+ ",updateDate = :updateDate "
				+ "where id in (:ids)";
		params.put("updateName", user.getUsername());
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", new Date());
		return this.batchExecute(hql, params);
	}
	
	
	 /**
     * 根据单位id获取此单位中是否存在
     * @param corpId
     * @return 有效用户笔数
     */
    public int getValidBvPayNumByCorpId(String corpId){
        String sql="select count(*) from BV_PAYEE_CHEQUE where 1=1  and corp_id=:corpId";
        Map<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
}
