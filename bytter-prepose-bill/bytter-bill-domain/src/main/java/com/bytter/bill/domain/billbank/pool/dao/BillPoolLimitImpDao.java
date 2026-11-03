package com.bytter.bill.domain.billbank.pool.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolUtils;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.core.utils.BeanUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>额度操作DAO<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-19 <br>
 */
@SuppressWarnings("rawtypes")
@Repository("billPoolLimitImpDao")
public class BillPoolLimitImpDao extends BaseDao {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <b>功能：</b>根据查询参数获取额度数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param busId 业务ID
	 * @return
	 */
	public List<PoolLimitVO> queryLimitInfo(String type, String busId){
		return queryLimitInfo(type, null, null, null, busId);
	}
	
	/**
	 * <b>功能：</b>根据查询参数获取额度数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param optStatus 指令状态
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<PoolLimitVO> queryLimitInfo(String type,String optStatus,String bifCode,String reCode){
		return queryLimitInfo(type, optStatus, bifCode, reCode, null);
	}
	/**
	 * <b>功能：</b>根据查询参数获取额度数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-24 <br>
	 * @param type 业务类型
	 * @param bifCode 银行接口代码
	 * @param optStatus 指令状态
	 * @param reCode 银行返回代码
	 * @param busId 业务ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PoolLimitVO> queryLimitInfo(String type,String optStatus,String bifCode,String reCode,String busId){
		List<PoolLimitVO> list = new ArrayList<PoolLimitVO>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("select a.id              as busId, ");
		sqlselect.append("pi.bif_code       as bifCode, ");
		sqlselect.append("a.corp_id         as corpId, ");
		sqlselect.append("p.id              as bPId, ");
		sqlselect.append("p.bp_code         as poolNo, ");
		sqlselect.append("p.bp_name         as poolName, ");
		sqlselect.append("s.orig_code       as applyBankOrgCode, ");
		sqlselect.append("bt_nvl_char(s.orig_name,pc.corp_name)       as applyCorpName, ");
		sqlselect.append("fs.orig_code      as oppBankOrgCode, ");
		sqlselect.append("fs.orig_name      as oppBankOrgName, ");
		sqlselect.append("(case when butf.bustype_code is null then but.bustype_code else butf.bustype_code end) as bustypeCode, ");
		sqlselect.append("(case when butf.bustype_code is null then null else but.bustype_code end) as bustypeDtlCode, ");
		sqlselect.append("a.limt_type       as limitType, ");
		sqlselect.append("a.create_time     as appDate, ");
		sqlselect.append("a.end_date        as endDate, ");
		sqlselect.append("a.apply_money     as money, ");
		sqlselect.append("a.apply_money     as applyMoney, ");
		sqlselect.append("a.bill_id     	as billId, ");
		sqlselect.append("s.bank_acc        as bailBankAcc, ");
		sqlselect.append("be.bank_id as bankId,be.apply_id as applyId,a.opt_status as optStatus, ");
		// PoolLimitVO 对象中并无此字段
		// sqlselect.append("bba.bank_acc as reqAcc, ");
		sqlselect.append("a.rmk ");
		if(BillPoolConstant.LIMIT_TYPE_ALLOT.equals(type)){
			sqlselect.append(",a.pool_money      as oldMoney, ");
			// sqlselect.append("a.adjust_money+a.apply_money as finalMoney "); 去掉了a.adjust_money，表中无此字段
			sqlselect.append("a.apply_money as finalMoney ");
		}
		if(BillPoolConstant.LIMIT_TYPE_APPLYCHECK.equals(type)
				||BillPoolConstant.LIMIT_TYPE_ALLOTCHECK.equals(type)){
			sqlselect.append(",a.check_type      as checkType, ");
			sqlselect.append("a.check_msg       as checkMsg ");
		}
		       
//		sqlselect.append("from AB_POOL_LIMIT_ALLOT a ");
//		sqlselect.append("from AB_POOL_LIMIT_ALLOT a ");
//		sqlselect.append("from AB_POOL_LIMIT_ALLOTCHECK a ");
//		sqlselect.append("from AB_POOL_LIMIT_ALLOTCHECK a  ");
		sqlselect.append("from ").append(BillPoolUtils.getLimitTableByType(type)).append(" a ");
		sqlselect.append("left join ab_pool p on a.bp_id = p.id  ");
		sqlselect.append("left join ab_poolif pi on pi.id = p.pif_id  ");
		sqlselect.append("left join ab_pool_bustype but on but.id = a.bustype_id  ");
		sqlselect.append("left join ab_pool_bustype butf on butf.id = but.parent_id ");
		       
		sqlselect.append("left join ab_pool_corp pc on pc.corp_id = a.corp_id  ");
		sqlselect.append("left join ab_pool_sign_info s on s.corp_id = a.corp_id and s.bp_id=p.id ");
		       
		sqlselect.append("left join ab_pool_corp fpc on fpc.id = pc.parent_id  ");
		sqlselect.append("left join ab_pool_sign_info fs on fs.corp_id = fpc.corp_id and fs.bp_id=p.id ");
		sqlselect.append("left join ab_pool_bank_exc be on be.id=a.id ");
		// sqlselect.append("left join sys_corp sc on sc.id=a.corp_id ");
		// sqlselect.append("left join BIS_BIF_INIT bbi on pi.bif_code=bbi.bif_code ");
		// sqlselect.append("left join bt_bank_acc bba on bbi.id=bba.bif_code and bba.corp_id=sc.id and bba.electric_bill='1' and bba.circulation_mode='1' and bba.valid_sign='1'  ");
		sqlselect.append("where 1=1 ");
		Map<String, Object> params = new HashMap<String, Object>();
		if(StrUtil.isNotEmpty(bifCode)){
			sqlselect.append("and pi.bif_code = :bifCode ");
			params.put("bifCode", bifCode);
		}
		if(StrUtil.isNotEmpty(optStatus)){
			sqlselect.append("and a.opt_status=:optStatus  ");
			params.put("optStatus", optStatus);
		}
		if(StrUtil.isNotEmpty(reCode)){
			sqlselect.append("and (be.re_code is null or be.re_code=:reCode) ");
			params.put("reCode", reCode);
		}
		if(StrUtil.isNotEmpty(busId)){
			sqlselect.append("and a.id=:busId  ");
			params.put("busId", busId);
		}
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		list = BeanUtils.copyMapListProperties(mapList, PoolLimitVO.class);
		return list;
	}
	
	/**
	 * 根据业务id修改指令状态
	 * @param type 业务类型
	 * @param optStatus
	 * @param billId 外围系统流水号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int updateLimitOptStatusByBusId(String type,String busId,String optStatus,String billId){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("update ");
		sql.append(BillPoolUtils.getLimitTableByType(type));
		sql.append(" set opt_status=:optStatus ");
		if(StrUtil.isNotEmpty(billId)){
			sql.append(",bill_Id=:billId  ");
			params.put("billId", billId);
		}
		sql.append(" where id=:busId ");
		params.put("optStatus", optStatus);
		params.put("busId", busId);
		return this.batchSQLExecute(sql.toString(), params);
	}
	
	/**
	 * （同一票据池、单位、金额、业务类型、风险类型、申请日期、结束日期 存在且业务未结束数据 ）
	 * 判断额度数据是否重复： 是返回ture 否返回false
	 * @param params 查询条件
	 * @return  是返回ture 否返回false
	 */
	@SuppressWarnings("unchecked")
	public boolean isLimitCheckRepeated(String type,PoolLimitVO params){
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("select a.id,a.corp_id from ");
		sqlselect.append(BillPoolUtils.getLimitTableByType(type)).append(" a ");
		sqlselect.append("where (a.opt_status not in ('-1','-2','-3','19') or a.opt_status is null ) ");
		sqlselect.append("and a.corp_id=:corpId ");
		sqlselect.append("and a.apply_money=:money ");
		sqlselect.append("and a.bp_id=:bpId ");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("corpId", params.getCorpId());
		paramsMap.put("money", params.getMoney());
		paramsMap.put("bpId", params.getPoolNo());
		//风险类型
		if(StrUtil.isEmpty(params.getLimitType())){
			sqlselect.append("and a.limt_Type is null ");
		}else{
			sqlselect.append("and a.limt_Type=:limitType ");
			paramsMap.put("limitType", params.getLimitType());
		}
		//业务类型ID
		if(StrUtil.isEmpty(params.getBusId())){
			sqlselect.append("and a.bustype_id is null ");
		}else{
			sqlselect.append("and a.bustype_id=:busTypeId ");
			paramsMap.put("busTypeId", params.getBusId());
		}
		//结束日期
		if(params.getEndDate()==null){
			sqlselect.append("and a.end_date is null ");
		}else{
			sqlselect.append("and a.end_date=:endDate ");
			paramsMap.put("endDate", params.getEndDate());
		}
		//申请日期
		if(params.getAppDate()==null){
			sqlselect.append("and a.create_time is null ");
		}else{
			sqlselect.append("and a.create_time=:appDate ");
			paramsMap.put("appDate", params.getAppDate());
		}
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), paramsMap);
		if(mapList!=null&&mapList.size()>0){
			return true;
		}
		return false;
	}
}
