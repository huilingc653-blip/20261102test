package com.bytter.bill.domain.bill.service.impl;
import com.bytter.bill.api.bill.service.IVoucherPrintService;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.service.AbEmitBillSupplyService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
/**
 * 开票套打回调接口实现
 * @author luop
 * 2017-08-25 17:14:11
 */
@Service("abEmitBillPrintServiceImpl")
@Transactional
@DubboService
public class AbEmitBillPrintServiceImpl implements IVoucherPrintService {
	@Autowired
	private AbEmitBillSupplyService abEmitBillSupplyService;
	@Autowired
	private AbVoucherinfoViceDao abVoucherinfoViceDao;

	/**
	 * 根据支票编号获取支票数据。
	 *
	 * @param listFields 查询字段。
	 * @param dataId 支票编号。
	 * @return 支票数据。
	 */
	public Map<String,Object> queryBvPrintVouOpById(String listFields, String dataId, String tableName){
		String sqlField = "select " + listFields +
				" from "+tableName+" s " +
				" where s.id=:dataId ";
		Map<String,Object> params = new HashMap<>();
		params.put("dataId",dataId);
		return abVoucherinfoViceDao.getBySQL(sqlField,params);
	}
	
	@Override
	public void printComplete(String dataId, String vouNo,ICurrentUser userVo) {
		abEmitBillSupplyService.printComplete(dataId, vouNo, userVo);
	}
	@Override
	public DataTablesOutput<Map<String, Object>> listBvPrintVouOp(DataTablesInput input, String listFields, String where,
																  Map<String, Object> params, boolean isOrder) {
		String sqlField = "select " + listFields +
				" from (SELECT " +
				" vv.ID AS id, " +
				" vv.CORP_ID AS corp_id, " +
				" vv.CORP_NAME AS corp_name, " +
				" v.BILL_MONEY AS bill_money, " +
				" v.START_DATE AS start_date, " +
				" v.END_DATE AS end_date, " +
				" v.PAYER AS payer, " +
				" v.PAYERACC AS payeracc, " +
				" v.PAYER_BANK_NAME AS payer_bank_name, " +
				" v.PAYEE AS payee, " +
				" v.PAYEEACC AS payeeacc, " +
				" v.PAYEE_BANK_NAME AS payee_bank_name, " +
				" v.ACCEPTOR AS acceptor, " +
				" v.ACCEPTORACC AS acceptoracc, " +
				" v.ACCEPTORBANKNO AS acceptorbankno, " +
				" v.ACCEPTORBANKADDRESS AS acceptorbankaddress, " +
				" v.A_CONTRACT_NO AS a_contract_no, " +
				" 'abEmitBillPrintServiceImpl' AS servicebean " +
				"FROM ab_voucherinfo_vice vv " +
				"LEFT JOIN ab_voucherinfo v ON vv.VID = v.ID " +
				"LEFT JOIN ab_accrecords a ON a.VVID = vv.ID " +
				"WHERE a.BRIEF_ID = '001' " +
				"AND a.STATUS = 95 " +
				"AND v.BILL_TYPE = '11' " +
				"AND v.BILL_FLAG = '10' " +
				"AND vv.VALID_SIGN = '1') s " +
				" where 1=1 and " + where;
		if (isOrder){
			// 移除当前排序
			input.getOrder().remove(0);
		}
		return abVoucherinfoViceDao.getDynamicMapBySQL(input,sqlField,"",false,params);
	}
}
