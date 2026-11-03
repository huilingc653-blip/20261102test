package com.bytter.biz.finacial.accbook.dao;

import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookDTO;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
* @Title: Dao
* @Description: FC_ACCBOOK
* @author onlineGenerator
* @date 2017-05-31 15:02:10
* @version 1.0
*
*/
@Repository
public class FcAccbookDao extends FcAccbookBaseDao<FcAccbook, String> {
	@Override
	protected String getAccbookTable() {
		return "fc_accbook";
	} 
	@Override
	protected String getAccbookDtlTable() {
		return "fc_accbook_dtl";
	}
	@Override
	protected String getAccbookEntity() {
		return "com.bytter.biz.finacial.accbook.entity.FcAccbook";
	}
	@Override
	protected String getAccbookDtlEntity() {
		return "com.bytter.biz.finacial.accbook.entity.FcAccbookDtl";
	}
	
	/**
	 * 功能介绍 : 根据原凭证号查询已计提过手续费的手续费凭证数据。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<Map<String, Object>> findTargetChargeAccbooksBy(Set<String> bookIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("select charge.id as id, charge.tally_Sign as tallySign, charge.order_no as bookId ")
			.append(" from fc_charge_accrue charge ")
			.append("where charge.target_order_no in (:orderNo) ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", bookIds);
		params.put("oriType", FinaBookConstant.BusOriType.HANDLE_CHARGE.get());
		params.put("tallySign", FinacialConstant.TALLY_SIN_Y);
		return listBySQL(sql.toString(), params);
	}
	
	/**
	 * 功能介绍 : 根据凭证流水号查询冲账作废生成的手续费凭证<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<Map<String, Object>> findRushChargeAccbooksBy(String orderNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select accbook.id as id,accbook.redblue_sign as redblueSign,accbook.orig_Voucher_No as origVoucherNo")
			.append("from fc_accbook_blned accbook_blned,fc_accbook accbook,fc_charge_accrue charge ")
			.append("where accbook_blned.id = charge.target_order_no ")
			.append("and accbook.orig_voucher_no = accbook_blned.id and charge.order_no = :orderNo ")
			.append("and accbook.voucher_state = :voucherStatus and accbook_blned.bus_ori_type= :oriType ")
			.append("and charge.tally_sign = :tallySign ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo);
		params.put("oriType", FinaBookConstant.BusOriType.HANDLE_CHARGE.get());
		params.put("tallySign", FinacialConstant.TALLY_SIN_Y);
		params.put("voucherStatus", FinaBookConstant.VoucherStatus.STATUS_CHECK.get());
		return listBySQL(sql.toString(), params);
	}
	
	/**
	 * <b>功能介绍：</b>按业务来源和状态查询账务<br>
	 * <b>日期：</b>2018年3月13日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param busOriType
	 * @param voucherState
	 * @param netCode
	 * @param curCode
	 * @param userId
	 * @return
	 */
	// TODO 没有被使用 暂注释
	/*public List<FcAccbookVO> findTallyAccbookByBusOriTypeAndState(Integer busOriType, String[] voucherState,
			String netCode, String curCode,String userId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("voucherState", voucherState);
		param.put("busOriType", busOriType);
		param.put("userId", userId);
		if (StringUtils.isNotEmpty(netCode)) {
			param.put("netCode", netCode);
		}
		if (StringUtils.isNotEmpty(curCode)) {
			param.put("curCode", curCode);
		}
		return findTallyAccbookByBusOriTypeAndState(param);
	}*/
	/**
	 * 功能介绍 : 还原原业务数据记账状态。<br>
	 * <b>日期：</b>2018年12月4日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void updateBusinessData(String tableName, String serId) {
		StringBuffer sql = new StringBuffer(" update ");
		sql.append(tableName).append(" set tally_sign = :tallySign ");
		sql.append(" where id = :serId ");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("serId", serId);
		param.put("tallySign", CommonConstant.VALID_SIGN_NO);
		createSQLQuery(sql.toString(), param).executeUpdate();
	}
    /**
     * 功能介绍 : 还原原业务数据记账状态。<br>
     * <b>日期：</b>2018年12月4日<br>
     * @version 1.0
     * @author liyh
     */
    public int updateChalkBusinessData(String tableName, String serId) {
        StringBuffer sql = new StringBuffer(" update ");
        sql.append(tableName).append(" set estate = :estate ");
        sql.append(" where book_id = :serId and estate = :oriEState ");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("serId", serId);
        param.put("estate", NbconsoleConstant.NbcEstate.E_STATE_CHECK.get());
        param.put("oriEState", NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.get());
        return createSQLQuery(sql.toString(), param).executeUpdate();
    }

	/**
	 * 功能介绍 : 清除流水的记账信息，更新记账标识为0。<br>
	 * <b>日期：</b>2018年12月4日<br>
	 * @version 1.0
	 * @author lijq
	 */
	public int updateBisAccDtlData(String tableName, String serId) {
		StringBuffer sql = new StringBuffer(" update ");
		sql.append(tableName);
		sql.append(" set TALLY_SIGN = '0',estate = :estate ");
		sql.append("    ,BOOK_ID = null,BUS_GLOBAL_ID = null,OPERATOR_CODE = null,TALLY_VOUCHER_NO = null, ORDER_NO = null,TALLY_FAIL_MSG = null ");
		sql.append(" where book_id = :serId and estate = :oriEState ");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("serId", serId);
		param.put("estate", NbconsoleConstant.NbcEstate.E_STATE_CHECK.get());
		param.put("oriEState", NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.get());
		return createSQLQuery(sql.toString(), param).executeUpdate();
	}

	/**
	 *单位禁用校验使用--网络记账
	 *@Param corpId
	 *@return
	 */
	public Integer verifyCorpDisableByCorpIdTally(String corpId){
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(a.id)  from fc_accbook a ");
		sql.append("  inner join fc_accbook_dtl d  on a.id = d.book_id "); //fc_accbook_dtl 增加冗余字段 corp_id
		/*sql.append("  inner join fc_acc c  on c.blnctr_acc = d.account ");
		sql.append("  where a.voucher_state not in ( 'N','B') ");
		sql.append(" and a.bus_ori_type <> 10 and c.corp_id = :corpId ");*/
		sql.append(" where a.voucher_state not in ( 'N','B') ");
		sql.append(" and a.bus_ori_type <> 10 and d.corp_id = :corpId ");
		Map<String,Object> param = new HashMap<>();
		param.put("corpId",corpId);
		return Integer.valueOf(createSQLQuery(sql.toString(), param).uniqueResult().toString());
	}
	/**
	 *单位禁用校验使用-手工帐
	 *@Param corpId
	 *@return
	 */
	public Integer verifyCorpDisableByCorpIdH(String corpId){
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(a.id)  from fc_accbook a ");
		sql.append("  inner join fc_accbook_dtl d  on a.id = d.book_id ");
//		sql.append("  inner join fc_acc c  on c.blnctr_acc = d.account ");
		sql.append("  where a.voucher_state not in ( 'N','B') ");
		sql.append(" and a.bus_ori_type = 10 and d.corp_id = ? ");
		return Integer.valueOf(createSQLQuery(sql.toString(), corpId).uniqueResult().toString());
	}
	/**
	 * 更新为已修改过现金流项目
	 * @param id
	 */
	public void updateChangeCashflux(String id){
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		StringBuilder sql = new StringBuilder();
		sql.append("update FC_ACCBOOK set IS_CHANGE_CASHFLUX = '1' where id = :id");
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	/**
	 * 获取已有记账的bill_id
	 * @param billId
	 * @return
	 */
	public List<String> getTallyRecordListId(List<String> billId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select bill_id from fc_tally_remind_dtl where 1=1 ");
		sql.append(" and ").append(getSQLIn(billId, 900, "bill_id")).append(" ");
		return createQuery(sql.toString()).list();
	}
	/**
	 * 根据全局业务ID、流水号、凭证号、凭证状态 获取流水号、凭证号、凭证状态。
	 *
	 * @param busGlobalId 全局业务ID。
	 * @return 流水号、凭证号、凭证状态。
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOrderVoucherByGlobalId(List<String> busGlobalId , String voucherState, String orderNo, String voucherNo) {
		String sql = "select order_no as orderNo,voucher_no as voucherNo,voucher_state as voucherState,bus_global_id as busGlobalId " +
				"from fc_accbook where bus_global_id in ( :busGlobalId)";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("busGlobalId", busGlobalId);
		if (StringUtils.isNotBlank(voucherState)){
			sql += " and voucher_state = voucherState";
			param.put("voucherState", voucherState);
		}
		if (StringUtils.isNotBlank(orderNo)){
			sql += " and order_no = orderNo";
			param.put("orderNo", orderNo);
		}
		if (StringUtils.isNotBlank(voucherNo)){
			sql += " and voucher_no = voucherNo";
			param.put("voucherNo", voucherNo);
		}
		return listBySQL(sql, param);
	}

	public List<String> getVoucherNoByNetIdAndVoucherState(String netId, String voucherState) {
		Map<String,Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select voucher_no from fc_accbook where net_id = '" + netId + "' and voucher_state = '" + voucherState + "' ");
		List<String> list = createSQLQuery(sql.toString(), params).list();
		return list;
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("netId", netId);
//		params.put("voucherState", voucherState);
//		Object o = createSQLQuery(sql.toString(), params).uniqueResult();
//		if (o != null){
//			return o.toString();
//		}
//		return null;
	}

	public List<FcAccbookDTO> listByInCorpIdAndBusOriTypeAndInState(List<String> netIds, String busOriType, List<String> voucherStates) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from fc_accbook where net_id in (:netIds) and voucher_state in(:voucherStates) and bus_ori_type = :busOriType");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netIds", netIds);
		params.put("voucherStates", voucherStates);
		params.put("busOriType", busOriType);
		return listBySQLAliasToBean(sql.toString(), params, FcAccbookDTO.class);
	}
}
