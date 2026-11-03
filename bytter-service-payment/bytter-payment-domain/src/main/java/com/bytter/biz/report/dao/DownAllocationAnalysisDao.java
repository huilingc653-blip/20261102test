package com.bytter.biz.report.dao;
import com.bytter.biz.report.vo.DownAllocationAnalysisSearchVO;
import com.bytter.biz.report.vo.DownAllocationAnalysisListVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zuoy
 * @Description 资金调拨明细表
 * @date 2017.10.10
 */
@Repository
public class DownAllocationAnalysisDao extends BaseDao<DownAllocationAnalysisListVO, String> {
	/**
	 * 资金调拨列表公用查询头部
	 * @return
	 */
	public String getHeadSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select BT_CONCAT(info.corpName, ';' ) AS groupKey,info.billNo,info.voucherType,info.voucherName,info.netId,info.corpId,info.corpName,");
		sql.append("info.payerAcc,info.payerAccName,	info.payerBank,info.payeeAcc,info.payeeAccName,info.payeeBank,info.cbsBillId,");
		sql.append("info.curName,info.payMoney,info.purpose,	info.status,info.isQuick,info.createBy,info.createDate,info.wishPayDay,info.cbsBillNo,info.voucherFrom ");
		sql.append(" from ( ");
		return sql.toString();
	}
	/**
	 * 资金调拨列表明细详情
	 * @return
	 */
	public String getDtlSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select a.voucher_no as billNo,b.voucher_type as voucherType,b.voucher_Name as voucherName,");
		sql.append(" case when a.voucher_from = '1' and b.sys_voucher_code = '02' or a.voucher_from = '9' and b.sys_voucher_code = '02' then g.bankAcc");
		sql.append(" else a.payer_acc end as payerAcc,");
		sql.append(" case when a.voucher_from = '1' and b.sys_voucher_code = '02' or a.voucher_from = '9' and b.sys_voucher_code = '02' then g.AccName");
		sql.append(" else a.payer_acc_name end as payerAccName,");
		sql.append(" case when a.voucher_from = '1' and b.sys_voucher_code = '02' or a.voucher_from = '9' and b.sys_voucher_code = '02' then g.bankName");
		sql.append(" else a.payer_bank end as payerBank,");
		sql.append(" a.payee_acc as payeeAcc,a.payee_acc_name as payeeAccName,a.payee_bank as payeeBank,s.parent_id as netId,a.corp_id as corpId,");
		sql.append(" s.name as corpName,a.cur_id as curId, c.cur_name as curName, a.purpose as purpose, a.pay_money as payMoney, a.status as status,a.cbs_bill_id as cbsBillId,");
		sql.append(" a.is_quick as isQuick,a.create_by as createBy,a.create_date as createDate,a.wish_payday as wishPayDay, a.cbs_bill_no as cbsBillNo,  a.voucher_From as voucherFrom");
		sql.append(" from bis_exc a left join fc_voucher_type b on a.voucher_type_id = b.id left join sys_corp s on a.corp_id = s.id");
		sql.append(" left join bt_currency c on a.cur_id = c.id");
		sql.append(" left join (select distinct e.corp_id as corpId, f.corp_id as prentCorpId, f.bank_acc as bankAcc, f.acc_name as AccName, f.bank_name as bankName, fcur.cur_id as curId ");
		sql.append(" from (select d.corp_id, dcur.prent_acc_id,d.bank_type_id from bt_bank_acc d  left join bt_bank_acc_cur dcur on d.id = dcur.bank_acc_id ");
		sql.append(" where dcur.prent_acc_id is not null) e");
		sql.append(" left join bt_bank_acc f on e.prent_acc_id = f.id left join bt_bank_acc_cur fcur on f.id = fcur.bank_acc_id ) g on a.corp_id = g.corpId and a.cur_id = g.curId ");
		//过滤交易成功的
		sql.append(" left join sys_corp h on h.id = g.prentCorpId where a.voucher_stat = '"+BdlinkConstant.SUCCESS+"' and (a.voucher_from in ("+ BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN+", "+BdlinkConstant.FROM_ALLOCATION_TYPE_ASK+")");
		sql.append(" or a.policy_id is not null");
		sql.append(" or (a.voucher_from = "+BdlinkConstant.FROM_NIS_PAY+" and b.sys_voucher_code = "+ NetbankConstant.PAY_TYPE_02+")");
		sql.append(" or (a.voucher_from = "+BdlinkConstant.FROM_COUNTER_TALLY+" and b.sys_voucher_code = "+ NetbankConstant.PAY_TYPE_02+" or b.sys_voucher_code = "+ NetbankConstant.PAY_TYPE_10+"))");
//		sql.append(" and a.status = 95 ) info where 1=1");
		//去掉指令表status状态过滤
		sql.append(" ) info where 1=1");
		return sql.toString();
	}
	/**
	 * 根据单位确定页数
	 * @param input
	 * @return
	 */
	public DataTablesOutput groupSize(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try { sql.append("select info.corpId from ( ");
			sql.append(getDtlSql());
			if(!EmptyUtils.isEmptyList(corpAuth)){
				sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corpId"));
			}
			sql.append(getWhereAllCondition(input, DownAllocationAnalysisSearchVO.class.newInstance(), "", false));
			sql.append("group by info.corpId order by info.corpId");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return getDynamicBySQL(input, sql.toString(), DownAllocationAnalysisListVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	/**
	 * 根据单位查询下拨明细列表
	 * @param input
	 * @return
	 */
	public List<DownAllocationAnalysisListVO> queryDownAllocationAnalysisDtl(DataTablesInput input){
		StringBuilder sqlNum=new StringBuilder();
		StringBuilder sqlDetail=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sqlNum.append("select count(*) as TATOLNUM from( ");
			sqlNum.append(getHeadSql());
			sqlNum.append(getDtlSql());
			sqlNum.append(getWhereAllCondition(input, DownAllocationAnalysisSearchVO.class.newInstance(), "", false));
			sqlNum.append(" ) num_info where 1=1 ");
			Map<String,Object> dtlNum = getBySQL(sqlNum.toString(),params);
			input.setLength(Integer.valueOf(String.valueOf(dtlNum.get("TATOLNUM"))));
			sqlDetail.append(getHeadSql());
			sqlDetail.append(getDtlSql());
			
			sqlDetail.append(getWhereAllCondition(input, DownAllocationAnalysisSearchVO.class.newInstance(), "", false));
			sqlDetail.append(" order by info.corpId, info.billNo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listBySQLAliasToBean(sqlDetail.toString(),params,DownAllocationAnalysisListVO.class);
	}
	/**
	 * 总账户余额查询
	 * @param corpAuth
	 * @return
	 */
	public List<Map<String,Object>> getTotalBisAccBal(List<String> corpAuth ){
		StringBuilder sql = new StringBuilder();
		sql.append("select tp.type_name as typeName, sum(BT_NVL_NUM(bal.avail_bal, 0)) as availBal, acc.bank_type_id as bankTypeId from bt_bank_acc acc");
		sql.append(" left join bt_bank_type tp on tp.id = acc.bank_type_id left join bis_acc_bal bal on acc.bank_acc = bal.bank_acc");
		sql.append(" where acc.parent_sign = '1' and acc.status = 95 ");
		if(!EmptyUtils.isEmptyList(corpAuth)){
			sql.append(" and ").append(getSQLIn(corpAuth,900,"acc.corp_id"));
		}
		sql.append(" group by acc.bank_type_id,tp.type_name");
		return listBySQL(sql.toString(),null);
	}
	/**
	 * 查询下拨明细总额
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public BigDecimal getTatolMoney(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append("select sum(totalMoney.payMoney) as totalMoney from( ");
			sql.append(getHeadSql());
			sql.append(getDtlSql());
			if(!EmptyUtils.isEmptyList(corpAuth)){
				sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corpId"));
			}
			sql.append(getWhereAllCondition(input, DownAllocationAnalysisSearchVO.class.newInstance(), "", false));
			sql.append(" ) totalMoney where 1=1 ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,BigDecimal> map = getBySQL(sql.toString(),params);
		BigDecimal big = map.get("TOTALMONEY");
		return big;
	}
	/**
	 * 根据单号查找下拨详情
	 * @param billNo
	 * @return
	 */
	public DownAllocationAnalysisListVO getByUnion(String billNo,String payerBank){
		StringBuilder sql=new StringBuilder();
		sql.append(getHeadSql());
		sql.append(getDtlSql());
		sql.append(" and info.billNo =:billNo ");
		sql.append(" and info.payerBank =:payerBank ");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("billNo", billNo);
		params.put("payerBank", payerBank);
		return getBySQLAliasToBean(sql.toString(), params, DownAllocationAnalysisListVO.class);
	}
	/**
	 * 导出
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public List<DownAllocationAnalysisListVO> getExcel(DataTablesInput input, List<String> corpAuth){
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try{
			sql.append(getHeadSql());
			sql.append(getDtlSql());
			sql.append(getWhereAllCondition(input, DownAllocationAnalysisSearchVO.class.newInstance(), "", false));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return listBySQLAliasToBean(sql.toString(),params,DownAllocationAnalysisListVO.class);
	}
}
