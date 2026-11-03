package com.bytter.bill.domain.billbank.pool.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaApplyVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaConVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolContract;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>票据池入池申请操作DAO<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-19 <br>
 */
@SuppressWarnings("rawtypes")
@Repository("billPoolIntoApplyImpDao")
public class BillPoolIntoApplyImpDao extends BaseDao {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <b>功能：</b>根据查询参数获取票据池入池申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param busId 业务ID
	 * @return
	 */
	public List<PoolEmitFinaApplyVO> queryEmitFinaApplyInfo(String busId){
		return queryEmitFinaApplyInfo(null, null, null, busId, null);
	}
	
	/**
	 * <b>功能：</b>根据查询参数获取票据池入池申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param appNo 申请编号
	 * @return
	 */
	public List<PoolEmitFinaApplyVO> queryEmitFinaApplyInfoByAppNo(String appNo){
		return queryEmitFinaApplyInfo(null, null, null, null, appNo);
	}
	
	/**
	 * <b>功能：</b>根据查询参数获取票据池入池申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param optStatus 指令状态
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<PoolEmitFinaApplyVO> queryEmitFinaApplyInfo(String optStatus,String bifCode,String reCode){
		return queryEmitFinaApplyInfo(optStatus, bifCode, reCode, null, null);
	}
	/**
	 * <b>功能：</b>根据查询参数获取票据池入池申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param bifCode 银行接口代码
	 * @param optStatus 指令状态
	 * @param reCode 银行返回代码
	 * @param busId 业务ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PoolEmitFinaApplyVO> queryEmitFinaApplyInfo(String optStatus,String bifCode,String reCode,String busId,String appNo){
		List<PoolEmitFinaApplyVO> list = new ArrayList<PoolEmitFinaApplyVO>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("select a.id          as busId, ");
		sqlselect.append("pi.bif_code          as bifCode, ");
		sqlselect.append("p.bp_code            as poolNo, ");
		sqlselect.append("p.bp_name            as poolName, ");
		sqlselect.append("s.orig_code          as bankOrgCode, ");
		sqlselect.append("s.orig_name          as bankOrgName, ");
		sqlselect.append("be.bank_id as bankid, ");
		sqlselect.append("be.apply_id as applyId, ");
		sqlselect.append("a.opt_status         as optstatus, ");
		sqlselect.append("a.return_code        as reCode, ");
		sqlselect.append("a.return_msg         as reMsg, ");
		sqlselect.append("a.pd_id         as pdId, ");
		sqlselect.append("d.app_no             as busAppNo, ");
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
		sqlselect.append("vv.abs, ");
		sqlselect.append("vv.rmk, ");
		sqlselect.append("v.a_contract_no      as acontractNo, ");
		sqlselect.append("v.a_invcnb           as ainvcnb, ");
		sqlselect.append("d.create_time        as tradeDate ");
		sqlselect.append("from ab_pool_dtl d ");
		sqlselect.append("left join ab_accrecords a ");
		sqlselect.append("  on d.id = a.pd_id ");
		sqlselect.append("left join ab_voucherinfo v ");
		sqlselect.append("  on a.vid = v.id ");
		sqlselect.append("left join ab_voucherinfo_vice vv ");
		sqlselect.append("  on vv.id = a.vvid ");
		sqlselect.append("left join ab_pool p ");
		sqlselect.append("  on d.bp_id = p.id ");
		sqlselect.append("left join ab_poolif pi ");
		sqlselect.append("  on pi.id = p.pif_id ");
		sqlselect.append("left join ab_pool_bank_exc be ");
		sqlselect.append("  on be.id = d.id ");
		sqlselect.append("left join ab_pool_sign_info s ");
		sqlselect.append("  on s.corp_id = vv.corp_id ");
		sqlselect.append("  and s.bp_id = p.id where 1=1 ");
		Map<String, Object> params = new HashMap<String, Object>();
		if(StrUtil.isNotEmpty(optStatus)){
			sqlselect.append("and a.opt_status=:optStatus  ");
			params.put("optStatus", optStatus);
		}
		if(StrUtil.isNotEmpty(reCode)){
			sqlselect.append("and (a.return_code is null or a.return_code=:reCode) ");
			params.put("reCode", reCode);
		}
		if(StrUtil.isNotEmpty(busId)){
			sqlselect.append("and a.id=:busId  ");
			params.put("busId", busId);
		}
		if(StrUtil.isNotEmpty(appNo)){
			sqlselect.append("and d.app_no=:appNo  ");
			params.put("appNo", appNo);
		}
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		list = BeanUtils.copyMapListProperties(mapList, PoolEmitFinaApplyVO.class);
		return list;
	}
	
	/**
	 * 根据参数获取数据
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AbPoolContract> queryAbPoolContractByParams(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("from AbPoolContract where 1=1 ");
		if(params.containsKey("dtlId")){
			hql.append(" and dtlId=:dtlId ");
		}
		if(params.containsKey("status")){
			hql.append(" and status=:status ");
		}
		return this.listByHQL(hql.toString(), params);
	}
	
	/**
	 * 根据参数判断是否有数据
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isAbPoolContract(Map<String,Object> params){
		StringBuilder sql=new StringBuilder("select count(*) as num from AB_POOL_CONTRACT where 1=1 ");
		if(params.containsKey("dtlId")){
			sql.append(" and dtl_Id=:dtlId ");
		}
		if(params.containsKey("status")){
			sql.append(" and status=:status ");
		}
		List<Map<String, Object>> mapList = this.listBySQL(sql.toString(), params);
		if(mapList!=null&&mapList.size()>0){
			int num=Integer.parseInt(String.valueOf(mapList.get(0).get("num")));
			if(num>0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <b>功能：</b>根据查询参数获取票据池入池合同数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param busId 业务ID
	 * @return
	 */
	public List<PoolEmitFinaConVO> queryEmitFinaConInfo(String busId){
		return queryEmitFinaConInfo(null, null, null, null, busId);
	}
	
	/**
	 * <b>功能：</b>根据查询参数获取票据池入池合同数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param status 合同操作状态
	 * @param optStatus 指令状态
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<PoolEmitFinaConVO> queryEmitFinaConInfo(String status,String optStatus,String bifCode,String reCode){
		return queryEmitFinaConInfo(status, optStatus, bifCode, reCode, null);
	}
	/**
	 * <b>功能：</b>根据查询参数获取票据池入池合同数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param status 合同操作状态
	 * @param bifCode 银行接口代码
	 * @param optStatus 指令状态
	 * @param reCode 银行返回代码
	 * @param busId 业务ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PoolEmitFinaConVO> queryEmitFinaConInfo(String status,String optStatus,String bifCode,String reCode,String busId){
		List<PoolEmitFinaConVO> list = new ArrayList<PoolEmitFinaConVO>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("select pc.id          as busId, ");
		sqlselect.append("pi.bif_code          as bifCode, ");
		sqlselect.append("p.bp_code            as poolNo, ");
		sqlselect.append("p.bp_name            as poolName, ");
		sqlselect.append("s.orig_code          as bankOrgCode, ");
		sqlselect.append("s.orig_name          as bankOrgName, ");
		sqlselect.append("be.bank_id as bankid, ");
		sqlselect.append("be.apply_id as applyId, ");
		sqlselect.append("pc.opt_status         as optstatus, ");
		sqlselect.append("d.app_no             as busAppNo, ");
		sqlselect.append("d.id             as dtlId ");
		sqlselect.append("from ab_pool_dtl d ");
		sqlselect.append("left join ab_accrecords a ");
		sqlselect.append("  on d.id = a.pd_id ");
		sqlselect.append("left join ab_voucherinfo v ");
		sqlselect.append("  on a.vid = v.id ");
		sqlselect.append("left join ab_voucherinfo_vice vv ");
		sqlselect.append("  on vv.id = a.vvid ");
		sqlselect.append("left join ab_pool p ");
		sqlselect.append("  on d.bp_id = p.id ");
		sqlselect.append("left join ab_poolif pi ");
		sqlselect.append("  on pi.id = p.pif_id ");
		sqlselect.append("left join AB_POOL_CONTRACT pc ");
		sqlselect.append("  on d.id = pc.dtl_id ");
		sqlselect.append("left join ab_pool_bank_exc be ");
		sqlselect.append("  on be.id = d.id ");
		sqlselect.append("left join ab_pool_bank_exc be1 ");
		sqlselect.append("  on be1.id = pc.id ");
		sqlselect.append("left join ab_pool_sign_info s ");
		sqlselect.append("  on s.corp_id = vv.corp_id ");
		sqlselect.append("  and s.bp_id = p.id where pc.status=:status ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", status);
		if(StrUtil.isNotEmpty(bifCode)){
			sqlselect.append("and pi.bif_code = :bifCode ");
			params.put("bifCode", bifCode);
		}
		if(StrUtil.isNotEmpty(optStatus)){
			sqlselect.append("and pc.opt_status=:optStatus  ");
			params.put("optStatus", optStatus);
		}
		if(StrUtil.isNotEmpty(reCode)){
			sqlselect.append("and (be1.return_code is null or be1.return_code=:reCode) ");
			params.put("reCode", reCode);
		}
		if(StrUtil.isNotEmpty(busId)){
			sqlselect.append("and pc.id=:busId  ");
			params.put("busId", busId);
		}
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		list = BeanUtils.copyMapListProperties(mapList, PoolEmitFinaConVO.class);
		return list;
	}
	/**
	 * 根据业务id修改票据池合同指令状态
	 * @param busId 业务ID
	 * @param optStatus
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int updatePoolConByBusId(String busId,String optStatus){
		StringBuffer sql = new StringBuffer("update AB_POOL_CONTRACT set opt_status=:optStatus where id=:busId ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("optStatus", optStatus);
		params.put("busId", busId);
		return this.batchSQLExecute(sql.toString(), params);
	}
	
}
