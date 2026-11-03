package com.bytter.bill.domain.billbank.pool.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.core.utils.BeanUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>票据池操作DAO<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-28 <br>
 */
@SuppressWarnings("rawtypes")
@Repository("billPoolManageyImpDao")
public class BillPoolManageyImpDao extends BaseDao {
	private static final long serialVersionUID = 1L;
	/**
	 * <b>功能：</b>根据查询参数获取票据池操作明细数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-28 <br>
	 * @param briefId 明细表业务类型
	 * @param bifCode 银行接口代码
	 * @param optStatus 指令状态
	 * @param reCode 银行返回代码
	 * @param busId 业务ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PoolBillVoucherVO> queryPoolBillVoucherInfo(String briefId,String optStatus,String bifCode,String reCode,String busId,String poolPledgeStatus){
		List<PoolBillVoucherVO> list = new ArrayList<PoolBillVoucherVO>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("select a.id          as busId, ");
		sqlselect.append("pi.bif_code          as bifCode, ");
		sqlselect.append("p.bp_code            as poolNo, ");
		sqlselect.append("p.bp_name            as poolName, ");
		sqlselect.append("p.bp_mode            as bpMode, ");
		sqlselect.append("s.orig_code          as bankOrgCode, ");
		sqlselect.append("s.orig_name          as bankOrgName, ");
		if(BillPoolConstant.BRIEF_TYPE_OUT.equals(briefId)){
			sqlselect.append("hb.ext_id as bankid, ");
		}else{
			sqlselect.append("be.bank_id as bankid, ");
		}
		sqlselect.append("be.apply_id as applyId, ");
		sqlselect.append("a.opt_status         as optstatus, ");
		sqlselect.append("a.return_code        as reCode, ");
		sqlselect.append("a.return_msg         as reMsg, ");
		sqlselect.append("a.pd_id         as pdId, ");
		sqlselect.append("a.bill_id         as billId, ");
		sqlselect.append("d.app_no             as busAppNo, ");
		sqlselect.append("d.INOUT_TYPE         as inoutType, ");
		sqlselect.append("d.brief_id         as briefId, ");
		sqlselect.append("(case when d.brief_id ='8118' then '01' when d.brief_id ='8218' then '02' end )  as pdTstType, ");
		if(BillPoolConstant.BRIEF_TYPE_IN.equals(briefId)){
			sqlselect.append("ds.inPoolSize, ");
		}
		if(BillPoolConstant.BRIEF_TYPE_IN_REPEAL.equals(briefId)){
			sqlselect.append("pir.BILLID_CODE as billIdCode, ");
		}
		if(BillPoolConstant.BRIEF_TYPE_PLEDGE_HOSTING.equals(briefId) || BillPoolConstant.BRIEF_TYPE_OUT.equals(briefId)){
			sqlselect.append("a2.business_date  as intoApplyDate, ");
		}
		sqlselect.append("v.orig_no         as origNo, ");
		sqlselect.append("v.bill_money         as billMoney, ");
		sqlselect.append("v.bill_type          as billType, ");
		sqlselect.append("v.bill_flag          as billFlag, ");
		sqlselect.append("v.banendrsmtmk, ");
		sqlselect.append("v.start_date         as startDate, ");
		sqlselect.append("v.end_date           as endDate, ");
		sqlselect.append("v.payer, ");
		sqlselect.append("v.payeracc, ");		
		sqlselect.append("v.payerbankno, ");
		sqlselect.append("v.payer_bank_name    as payerBankName, ");
		sqlselect.append("v.payee, ");
		sqlselect.append("v.payeeacc, ");
		sqlselect.append("v.payeebankno, ");
		sqlselect.append("v.payee_bank_name    as payeeBankName, ");
		sqlselect.append("v.acceptor, ");
		sqlselect.append("v.acceptoracc, ");
		sqlselect.append("v.acceptorbankno, ");
		sqlselect.append("v.acceptor_bank_name as acceptoreBankName, ");
		sqlselect.append("pp.bank_acc as pledgeeAcc, ");
		sqlselect.append("pp.acc_name as pledgeeName, ");
		sqlselect.append("pp.bank_no as pledgeeBankNo, ");
		sqlselect.append("pp.bank_name as pledgeeBankName, ");
		sqlselect.append("a.req_acct_id as reqAcc, ");
		sqlselect.append("a.req_nm as reqName, ");
		sqlselect.append("a.req_acctsvcr as reqBankNo, ");
		sqlselect.append("vv.abs, ");
		sqlselect.append("vv.rmk, ");
		sqlselect.append("v.a_contract_no      as acontractNo, ");
		sqlselect.append("v.a_invcnb           as ainvcnb, ");
		sqlselect.append("su.username        as opName, ");
		sqlselect.append("d.create_time        as tradeDate ");
		sqlselect.append("from ab_pool_dtl d ");
		sqlselect.append("left join sys_user su ");
		sqlselect.append("  on su.id = d.create_id ");
		sqlselect.append("left join ab_accrecords a ");
		sqlselect.append("  on d.id = a.pd_id ");
		sqlselect.append("left join ab_voucherinfo v ");
		sqlselect.append("  on a.vid = v.id ");
		sqlselect.append("left join ab_voucherinfo_vice vv ");
		sqlselect.append("  on vv.id = a.vvid ");
		sqlselect.append("left join ab_pool p ");
		sqlselect.append("  on d.bp_id = p.id ");
		sqlselect.append("left join AB_POOL_PLEDGEE pp ");
		sqlselect.append("  on pp.bp_id=p.id ");
		sqlselect.append("left join ab_poolif pi ");
		sqlselect.append("  on pi.id = p.pif_id ");
		sqlselect.append("left join ab_pool_bank_exc be ");
		sqlselect.append("  on be.id = d.id ");
		sqlselect.append("left join ab_pool_sign_info s ");
		sqlselect.append("  on s.corp_id = vv.corp_id ");
		sqlselect.append("  and s.bp_id = p.id ");
		if(BillPoolConstant.BRIEF_TYPE_IN_DEL.equals(briefId)){
			sqlselect.append("left join ab_pool_bank_exc indel ");
			sqlselect.append("  on indel.id =  ");
		}
		//出池获取一下在池登记簿的外部银行ID
		if(BillPoolConstant.BRIEF_TYPE_OUT.equals(briefId)){
			sqlselect.append("left join ab_pool_hold_book hb ");
			sqlselect.append("  on hb.orig_no = d.orig_no and hb.bp_id=d.bp_id ");
			//获取入池日期
			sqlselect.append(" left join ab_accrecords a2 on a2.vvid=a.vvid and a2.brief_id='8018' and a2.opt_status=19 ");
		}
		//设置入池次数
		if(BillPoolConstant.BRIEF_TYPE_IN.equals(briefId)){
			sqlselect.append(" left join ( ");
			sqlselect.append(" select count(apd.id) as inPoolSize,apd.orig_no,apd.bp_id from ab_pool_dtl apd where apd.opt_status=:optStatusOK and apd.brief_id=:briefId  group by apd.orig_no,apd.bp_id ");
			sqlselect.append(" ) ds  ");
			sqlselect.append(" on ds.orig_no=d.orig_no and ds.bp_id=d.bp_id ");
			params.put("optStatusOK", BillPoolConstant.optStatus_19);
		}
		if(BillPoolConstant.BRIEF_TYPE_IN_REPEAL.equals(briefId)){
			sqlselect.append("left join AB_POOL_INTO_REPEAL pir ");
			sqlselect.append("  on pir.id = d.REPEAL_ID ");
		}
		if(BillPoolConstant.BRIEF_TYPE_PLEDGE_HOSTING.equals(briefId)){
			sqlselect.append(" left join ab_accrecords a2 on a.lastid=a2.id ");
		}
		sqlselect.append(" where 1=1 ");
		if(StrUtil.isNotEmpty(bifCode)){
			sqlselect.append("and pi.bif_code = :bifCode ");
			params.put("bifCode", bifCode);
		}
		if(StrUtil.isNotEmpty(briefId)){
			//识别到是删除入池申请数据则需要添加银行中间表状态处理
			if(BillPoolConstant.BRIEF_TYPE_IN_DEL.equals(briefId)){
				String[] briefIds=briefId.split("_");
				sqlselect.append("and a.brief_id=:briefId and a.opt_status=:optStatusOK and d.opt_status=:optStatusBack ");
				params.put("briefId", briefIds[0]);
				params.put("optStatusOK", BillPoolConstant.optStatus_19);
				params.put("optStatusBack", BillPoolConstant.optStatus_2);
				
				if(StrUtil.isNotEmpty(optStatus)){
					if(BillPoolConstant.optStatus_16.equals(optStatus)){
						sqlselect.append("and (indel.opt_status is null or indel.opt_status=:optStatus) ");
					}else{
						sqlselect.append("and indel.opt_status=:optStatus  ");
					}
					params.put("optStatus", optStatus);
				}
				if(StrUtil.isNotEmpty(reCode)){
					sqlselect.append("and (indel.return_code is null or indel.return_code=:reCode) ");
					params.put("reCode", reCode);
				}
			}else if(BillPoolConstant.BRIEF_TYPE_INOP_HOSTING.equals(briefId)
					||BillPoolConstant.BRIEF_TYPE_INOP_PLEDGE.equals(briefId)){
				String[] briefIds=briefId.split("_");
				sqlselect.append("and a.brief_id=:briefId  ");
				params.put("briefId", briefIds[0]);
				
				sqlselect.append("and d.INOUT_TYPE=:inoutType  ");
				params.put("inoutType", briefIds[1]);
			}else if(BillPoolConstant.BRIEF_TYPE_PLEDGE_HOSTING.equals(briefId)){
				sqlselect.append("AND A.BRIEF_ID IN (:briefIds) and pi.bif_code !=:ccb ");
				params.put("briefIds", briefId.split(","));
				params.put("ccb", BillPoolConstant.BIF_CODE_CCB);
			}else if(BillPoolConstant.BRIEF_TYPE_TO_PLEDGE.equals(briefId)
					||BillPoolConstant.BRIEF_TYPE_TO_HOSTING.equals(briefId)){
				sqlselect.append("and a.brief_id=:briefId ");
				//sqlselect.append("and pi.bif_code =:ccb ");
				params.put("briefId", briefId);
				//params.put("ccb", BillPoolConstant.BIF_CODE_CCB);
			}else{
				sqlselect.append("and a.brief_id=:briefId  ");
				params.put("briefId", briefId);
			}
		}
		if(StrUtil.isNotEmpty(optStatus)
				&&!BillPoolConstant.BRIEF_TYPE_IN_DEL.equals(briefId)){
			sqlselect.append("and a.opt_status=:optStatus  ");
			params.put("optStatus", optStatus);
		}
		if(StrUtil.isNotEmpty(poolPledgeStatus)){
			sqlselect.append("and a.pool_Pledge_Status=:poolPledgeStatus  ");
			params.put("poolPledgeStatus", poolPledgeStatus);
		}
		if(StrUtil.isNotEmpty(reCode)
				&&!BillPoolConstant.BRIEF_TYPE_IN_DEL.equals(briefId)){
			sqlselect.append("and (a.return_code is null or a.return_code=:reCode) ");
			params.put("reCode", reCode);
		}
		if(StrUtil.isNotEmpty(busId)){
			sqlselect.append("and a.id=:busId  ");
			params.put("busId", busId);
		}
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		//System.out.println("查询sql====="+sqlselect.toString());
		//System.out.println("查询待发送数据====="+mapList.toString()+"====="+params);
		list = BeanUtils.copyMapListProperties(mapList, PoolBillVoucherVO.class);
		for(PoolBillVoucherVO voucherVO:list){
			if(BillPoolConstant.BRIEF_TYPE_TO_HOSTING.equals(voucherVO.getBriefId())){
				voucherVO.setPdTstType(BillPoolConstant.PDTSTTYPE_TO_HOSTING);
				//System.out.println("设置1setPdTstType==="+voucherVO.getPdTstType());
			}else if(BillPoolConstant.BRIEF_TYPE_TO_PLEDGE.equals(voucherVO.getBriefId())){
				voucherVO.setPdTstType(BillPoolConstant.PDTSTTYPE_TO_PLEDGE);
				//System.out.println("设置2setPdTstType==="+voucherVO.getPdTstType());
			}
		}
		//System.out.println("查询待发送数据结果====="+list);
		return list;
	}
}
