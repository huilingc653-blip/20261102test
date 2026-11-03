package com.bytter.bill.domain.billbank.pool.dao;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.entity.*;
import com.bytter.bill.domain.billbank.pool.vo.PoolQueryExcVO;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * <br>
 * <b>功能：</b>票据池查询业务DAO<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-28 <br>
 */
@SuppressWarnings("rawtypes")
@Repository("billPoolQueryImpDao")
public class BillPoolQueryImpDao extends BaseDao {
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * 根据单位ID和池Id获取额度信息
	 * @param corpId 单位ID
	 * @param bpId 票据池ID
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public AbPoolLimit getAbPoolLimitByCorpIdDpId(String corpId, String bpId){
		if(StrUtil.isEmpty(corpId)||StrUtil.isEmpty(bpId)){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("corpId", corpId);
		params.put("bpId", bpId);
		List<AbPoolLimit> list=this.listByHQL("from AbPoolLimit where corpId=:corpId and bpId=:bpId ", params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据单位ID和池Id获取额度信息
	 * @param corpId 单位ID
	 * @param bpId 票据池ID
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public AbPoolLimitHis getAbPoolLimitHisByCorpIdDpId(String corpId, String bpId, Date limitDate){
		if(StrUtil.isEmpty(corpId)||StrUtil.isEmpty(bpId)||limitDate==null){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("corpId", corpId);
		params.put("bpId", bpId);
		params.put("limitDate", DateUtils.getDateYMD(limitDate));
		List<AbPoolLimitHis> list=this.listByHQL("from AbPoolLimitHis where corpId=:corpId and bpId=:bpId and limitDate=:limitDate ", params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据单位ID和池Id获取签约信息
	 * @param corpId 单位ID
	 * @param bpId 票据池ID
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public AbPoolSignInfo getAbPoolSignInfoByCorpIdDpId(String corpId, String bpId){
		if(StrUtil.isEmpty(corpId)||StrUtil.isEmpty(bpId)){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("corpId", corpId);
		params.put("bpId", bpId);
		List<AbPoolSignInfo> list=this.listByHQL("from AbPoolSignInfo where corpId=:corpId and bpId=:bpId ", params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据delMap删除保证金明细存在数据库的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int deletePoolBail(Map<String, AbPoolBailBook> delMap){
		StringBuffer sql=new StringBuffer("delete from AB_POOL_BAIL_BOOK where ");
		Map<String,Object> params=new HashMap<String, Object>();
		if(delMap.isEmpty()){
			return 0;
		}
		int i=0;
		sql.append("(");
		for(Entry<String,AbPoolBailBook> info : delMap.entrySet()){
			if(i>0){
				sql.append(") or (");
			}
			sql.append(" corp_Id=:corpId").append(i).append(" ");
			params.put("corpId"+i, info.getValue().getCorpId());
			sql.append(" and bp_Id=:bpId").append(i).append(" ");
			params.put("bpId"+i, info.getValue().getBpId());
			sql.append(" and TRANS_DATE=:transDate").append(i).append(" ");
			params.put("transDate"+i, info.getValue().getTransDate());
			i++;
		}
		sql.append(")");
		return this.batchSQLExecute(sql.toString(), params);
	}
	
	/**
	 * 根据delMap删除交易明细账簿存在数据库的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int deletePoolDtlBook(Map<String, AbPoolDtlBook> delMap, boolean keyIsBillflag){
		StringBuffer sql=new StringBuffer("delete from AB_POOL_DTL_BOOK where ");
		Map<String,Object> params=new HashMap<String, Object>();
		if(delMap.isEmpty()){
			return 0;
		}
		int i=0;
		sql.append("(");
		for(Entry<String,AbPoolDtlBook> info : delMap.entrySet()){
			if(i>0){
				sql.append(") or (");
			}
			sql.append(" corp_Id=:corpId").append(i).append(" ");
			params.put("corpId"+i, info.getValue().getCorpId());
			sql.append(" and bp_id=:bpId").append(i).append(" ");
			params.put("bpId"+i, info.getValue().getBpId());
			sql.append(" and brief_Id=:briefId").append(i).append(" ");
			params.put("briefId"+i, info.getValue().getBriefId());
			sql.append(" and create_time>=:createTime").append(i).append(" ");
			sql.append(" and create_time<:createTimeEnd").append(i).append(" ");
			params.put("createTime"+i, DateUtils.getDateYMD(info.getValue().getCreateTime()));
			params.put("createTimeEnd"+i, DateUtils.addDay(DateUtils.getDateYMD(info.getValue().getCreateTime()),1));
			if(keyIsBillflag){
				if(info.getValue().getOrigNo()==null||info.getValue().getOrigNo().length()<28){
					sql.append(" and (orig_no is null or BT_LENGTH(orig_no)<28 ) ");
				}else{
					sql.append(" and BT_LENGTH(orig_no)>28 ");
				}
			}
			i++;
		}
		sql.append(")");
		return this.batchSQLExecute(sql.toString(), params);
	}
	
	/**
	 * 判断票据号是否存在
	 * @param origNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean isOrigNo(String origNo){
		StringBuilder sql=new StringBuilder("select count(*) as num from ab_voucherinfo where orig_no=:origNo ");
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("origNo", origNo);
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
	 * 根据delMap删除在池明细存在数据库的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int deletePoolHoldBook(Map<String,Object> delMap){
		StringBuffer sql=new StringBuffer("delete from AB_POOL_HOLD_BOOK where ");
		if(delMap.isEmpty()){
			return 0;
		}
		sql.append(" bp_Id=:bpId ");
		if(delMap.containsKey("corpId")){
			if(delMap.get("corpId")!=null){
				sql.append(" and  corp_Id in (:corpId) ");
			}
		}
		if(delMap.containsKey("beginDate")){
			if(delMap.get("beginDate")!=null){
				sql.append(" and create_time>=:beginDate ");
			}
		}
		if(delMap.containsKey("endDate")){
			if(delMap.get("endDate")!=null){
				sql.append(" and create_time<:endDate ");
			}
		}
//		Map<String,Object> params=new HashMap<String, Object>();
//		int i=0;
//		sql.append("(");
//		for(Entry<String,AbPoolHoldBook> info : delMap.entrySet()){
//			if(i>0){
//				sql.append(") or (");
//			}
//			sql.append(" corp_Id=:corpId").append(i).append(" ");
//			params.put("corpId"+i, info.getValue().getCorpId());
//			sql.append(" and bp_Id=:bpId").append(i).append(" ");
//			params.put("bpId"+i, info.getValue().getBpId());
//			sql.append(" and create_time=:createTime").append(i).append(" ");
//			params.put("createTime"+i, info.getValue().getCreateTime());
//			i++;
//		}
//		sql.append(")");
		return this.batchSQLExecute(sql.toString(), delMap);
	}
	/**
	 * 入池明细修改在池登记簿的入池日期
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public int updatePoolHoldBookCreateTimeByDtlBook(AbPoolDtlBook dtlBook){
		StringBuffer sql=new StringBuffer("update AB_POOL_HOLD_BOOK set create_time=:createTime where corp_id=:corpId and orig_no=:origNo and inout_type=:inoutType and bp_id=:bpId and create_time<:createTime ");
		Map<String,Object> params=new HashMap<String, Object>();
		if(dtlBook==null){
			return 0;
		}
		params.put("createTime", dtlBook.getCreateTime());
		params.put("corpId", dtlBook.getCorpId());
		params.put("origNo", dtlBook.getOrigNo());
		params.put("inoutType", dtlBook.getInoutType());
		params.put("bpId", dtlBook.getBpId());
		logger.debug("updatePoolHoldBookCreateTimeByDtlBook：sql="+sql.toString()+""+params.toString());
		StringBuffer paramsStr=new StringBuffer("");
		for(String key:params.keySet()){
			paramsStr.append(key).append("=").append(params.get(key)).append(",");
		}
		logger.debug("params="+paramsStr.toString());
		return this.batchSQLExecute(sql.toString(), params);
	}
	
	/**
	 * 出池明细删除在池登记簿的信息
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "null" })
	public int deletePoolHoldBookByDtlBook(AbPoolDtlBook dtlBook){
		StringBuffer sql=new StringBuffer("delete from AB_POOL_HOLD_BOOK where corp_id=:corpId and orig_no=:origNo and inout_type=:inoutType and bp_id=:bpId and create_time<:createTime ");
		Map<String,Object> params=new HashMap<String, Object>();
		if(dtlBook==null){
			return 0;
		}
		params.put("createTime", dtlBook.getCreateTime());
		params.put("corpId", dtlBook.getCorpId());
		params.put("origNo", dtlBook.getOrigNo());
		params.put("inoutType", dtlBook.getInoutType());
		params.put("bpId", dtlBook.getBpId());
		return this.batchSQLExecute(sql.toString(), params);
	}
	
	/**
	 * <b>功能：</b>获取对应银行类型和查询类型需要的票据池查询指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-14 <br>
	 * 
	 * @param bifCode
	 *            银行接口代码
	 * @param queryType
	 *            查询指令类型
	 * @return 指令信息
	 */
	@SuppressWarnings("unchecked")
	public List<PoolQueryExcVO> queryPoolExcByBifCode(String bifCode, String queryType) {
		if(StrUtil.isEmpty(bifCode)||StrUtil.isEmpty(queryType)){
			return null;
		}
		StringBuffer sql = new StringBuffer("select ");
		sql.append("pe.id as busId,pe.corp_id as corpId,ac.name as corpName,pe.bp_id as bpId, ");
		sql.append("ap.bp_code as poolNo,ap.bp_name as poolName,pe.exc_type as queryType,api.bif_code as bifCode, ");
		sql.append("bba.bank_acc as reqAcc,BT_NVL_CHAR(s.orig_code,bba.BANK_ORG_CODE) as bankOrgCode,pe.oper_date_start as beginDate,pe.oper_date_end as endDate, ");
		sql.append("pe.status as optStatus,be.re_code as reCode,be.re_code as reMsg ");
		sql.append("from  ");
		sql.append("AB_POOL_QUERY_EXC pe  ");
		sql.append("left join ab_pool ap on ap.id=pe.bp_id ");
		sql.append("left join Ab_Poolif api on api.id=ap.pif_id ");
		sql.append("left join sys_corp ac on pe.corp_id=ac.id ");
		sql.append("left join ab_pool_sign_info s on s.corp_id = ac.id and s.bp_id=ap.id ");
		sql.append("left join BIS_BIF_INIT bbi on api.bif_code=bbi.bif_code  ");
		sql.append("left join bt_bank_acc bba on bbi.id=bba.bif_code and bba.corp_id=ac.id and bba.electric_bill='1' and bba.circulation_mode='1' ");
		sql.append("left join ab_pool_bank_exc be on be.id=pe.id  ");
		sql.append("where 1=1 and (be.re_code=3 or be.re_code is null) ");
		sql.append("and api.bif_code =:bifCode ");
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("bifCode", bifCode);
		if(StrUtil.isNotEmpty(queryType)){
			params.put("queryType", queryType.split(","));
			sql.append("and pe.exc_type in (:queryType) ");
		}
		List<Map<String, Object>> objStr = this.listBySQL(sql.toString(), params);
		List<PoolQueryExcVO> queryExcVOList = BeanUtils.copyMapListProperties(objStr, PoolQueryExcVO.class);
		return queryExcVOList;
	}
	/**
	 * <b>功能：</b>根据查询业务ID修改票据池查询指令状态<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-12 <br>
	 * @param busId 指令id
	 * @param optStatus 指令状态
	 * @return 修改成功还是失败 true成功false失败
	 */
	public boolean updatePoolQueryExc(String busId,String optStatus){
		if(StrUtil.isEmpty(busId)||StrUtil.isEmpty(busId)){
			return false;
		}
		StringBuffer sql = new StringBuffer("update AB_POOL_QUERY_EXC ");
		sql.append(" set status=:optStatus where id=:busId ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("optStatus", optStatus);
		params.put("busId", busId);
		int size= this.batchSQLExecute(sql.toString(), params);
		return size>0?true:false;
	}
	
//
//	/**
//	 * （同一票据池、单位、金额、业务类型、风险类型、申请日期、结束日期 存在且业务未结束数据 ）
//	 * 判断额度数据是否重复： 是返回ture 否返回false
//	 * @param params 查询条件
//	 * @return  是返回ture 否返回false
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean isLimitCheckRepeated(AbPoolLimitDtl params){
//		StringBuffer sqlselect = new StringBuffer();
//		sqlselect.append("select a.id,a.corp_id from ");
//		sqlselect.append(BillPoolUtils.getLimitTableByType(type)).append(" a ");
//		sqlselect.append("where (a.opt_status not in ('-1','-2','-3','19') or a.opt_status is null ) ");
//		sqlselect.append("and a.corp_id=:corpId ");
//		sqlselect.append("and a.apply_money=:money ");
//		sqlselect.append("and a.bp_id=:bpId ");
//		Map<String, Object> paramsMap = new HashMap<String, Object>();
//		paramsMap.put("corpId", params.getCorpId());
//		paramsMap.put("money", params.getMoney());
//		paramsMap.put("bpId", params.getPoolNo());
//		//风险类型
//		if(StringUtil.isEmpty(params.getLimitType())){
//			sqlselect.append("and a.limt_Type is null ");
//		}else{
//			sqlselect.append("and a.limt_Type=:limitType ");
//			paramsMap.put("limitType", params.getLimitType());
//		}
//		//业务类型ID
//		if(StringUtil.isEmpty(params.getBusId())){
//			sqlselect.append("and a.bustype_id is null ");
//		}else{
//			sqlselect.append("and a.bustype_id=:busTypeId ");
//			paramsMap.put("busTypeId", params.getBusId());
//		}
//		//结束日期
//		if(params.getEndDate()==null){
//			sqlselect.append("and a.end_date is null ");
//		}else{
//			sqlselect.append("and a.end_date=:endDate ");
//			paramsMap.put("endDate", params.getEndDate());
//		}
//		//申请日期
//		if(params.getAppDate()==null){
//			sqlselect.append("and a.create_time is null ");
//		}else{
//			sqlselect.append("and a.create_time=:appDate ");
//			paramsMap.put("appDate", params.getAppDate());
//		}
//		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), paramsMap);
//		if(mapList!=null&&mapList.size()>0){
//			return true;
//		}
//		return false;
//	}
//	
	/**
	 * 根据传递参数删除额度明细记录
	 * @param params 删除条件
	 */
	public void deleteLimitDtl(Map<String,Object> params){
		if(params==null||params.isEmpty()){
			return;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("delete from AB_POOL_LIMIT_DTL ");
		sql.append("where 1=1 ");
		if(params.containsKey("corpIds")){
			sql.append("and corp_id in (:corpIds) ");
		}
		if(params.containsKey("bpId")){
			sql.append("and bp_id=:bpId ");
		}
		if(params.containsKey("beginDate")){
			sql.append("and CREATE_time>=:beginDate ");
		}
		if(params.containsKey("endDate")){
			sql.append("and CREATE_time<:endDate ");
		}
		this.batchSQLExecute(sql.toString(), params);
	}
	
	/**
	 * 根据查询条件获取对应的最近的额度信息
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getPoolMoneyByParams(Map<String,Object> params){
		StringBuilder sql=new StringBuilder(" ");
		sql.append("select dtl.pool_money as poolmoney from ab_pool_limit_dtl dtl ");
		sql.append("where dtl.create_time <:startDate and dtl.corp_id=:corpId and dtl.bp_id=:bpId ");
		sql.append("order by dtl.create_time desc,dtl.id desc ");
		List<Map<String, Object>> mapList = this.listBySQL(sql.toString(),0,1, params);
		if(mapList!=null&&mapList.size()>0){
			Object poolMoney=mapList.get(0).get("poolmoney");
			if(poolMoney!=null){
				if(poolMoney instanceof BigDecimal){
					return (BigDecimal)poolMoney;
				}else if(poolMoney instanceof Double){
					return new BigDecimal((Double)poolMoney);
				}else{
					return new BigDecimal(String.valueOf(poolMoney));
				}
			}
		}
		return new BigDecimal(0);
	}
	
	/**
	 * 根据申请代码、单位ID和、池Id和申请日期获取出票融资申请登记薄信息
	 * @param applyCode 申请代码
	 * @param corpId 单位ID
	 * @param bpId 票据池ID
	 * @param applyDate 申请日期
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public AbPoolFinaApplyBook getAbPoolFinaApplyBookByParams(String applyCode,String corpId,String bpId,Date applyDate){
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("from AbPoolFinaApplyBook where 1=1 ");
		Map<String, Object> params = new HashMap<String, Object>();
		if(StrUtil.isNotEmpty(applyCode)){
			sqlselect.append(" and applyCode=:applyCode ");
			params.put("applyCode", applyCode);
		}
		if(StrUtil.isNotEmpty(corpId)){
			sqlselect.append(" and corpId=:corpId ");
			params.put("corpId", corpId);
		}
		if(StrUtil.isNotEmpty(bpId)){
			sqlselect.append(" and bpId=:bpId  ");
			params.put("bpId", bpId);
		}
		if(ObjectUtil.isNotEmpty(applyDate)){
			sqlselect.append(" and applyDate=:applyDate ");
			params.put("applyDate", DateUtils.getDateYMD(applyDate));
		}
		List<AbPoolFinaApplyBook> list=this.listByHQL(sqlselect.toString(), params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据出票融资申请登记薄id或者票据号获取明细信息
	 * @param bookId 登记薄id
	 * @param origNo 票据号
	 * @return 出票融资申请明细信息
	 */
	@SuppressWarnings("unchecked")
	public AbPoolFinaApplyDtl getAbPoolFinaApplyDtlByParams(String bookId,String origNo){
		if(StrUtil.isEmpty(bookId) && StrUtil.isEmpty(bookId)){
			return null;
		}
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("from AbPoolFinaApplyDtl where  1=1  ");
		Map<String, Object> params = new HashMap<String, Object>();
		if(StrUtil.isNotEmpty(bookId)){
			sqlselect.append(" and bookId=:bookId ");
			params.put("bookId", bookId);
		}
		if(StrUtil.isNotEmpty(origNo)){
			sqlselect.append(" and origNo=:origNo ");
			params.put("origNo", origNo);
		}
		
		List<AbPoolFinaApplyDtl> list=this.listByHQL(sqlselect.toString(), params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
