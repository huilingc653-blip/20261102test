package com.bytter.bill.domain.billbank.pool.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolDtl;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import com.bytter.core.utils.BeanUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>保证金操作DAO<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-19 <br>
 */
@Repository("billPoolDtlImpDao")
public class BillPoolDtlImpDao extends BaseDao<AbPoolDtl,String> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 根据业务id修改票据明细表指令状态
	 * @param busId 业务ID
	 * @param optStatus 指令状态
	 * @param validSign 是否有效
	 * @return 结果数
	 */
	public int updatePoolDtlOptStatusByBusId(String busId,String optStatus, String validSign){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("update ab_pool_dtl set opt_status=:optStatus ");
		if(!StrUtil.isEmpty(validSign)){
			sql.append(",valid_sign=:validSign ");
			params.put("validSign", validSign);
		}
		sql.append(" where id=:busId ");
		params.put("optStatus", optStatus);
		params.put("busId", busId);
		return this.batchSQLExecute(sql.toString(), params);
	}
	/**
	 * 根据业务id修改票据明细表有效标记
	 * @param busId 业务ID
	 * @param validSign 有效标记
	 * @return
	 */
	public int updatePoolDtlValidSignByBusId(String busId,String validSign){
		StringBuffer sql = new StringBuffer("update ab_pool_dtl set valid_sign=:validSign where id=:busId ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("validSign", validSign);
		params.put("busId", busId);
		return this.batchSQLExecute(sql.toString(), params);
	}
	
//	/**
//	 * 根据ID获取流水信息
//	 * @param id
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public AbPoolDtl getAbPoolDtlById(String id){
//		if(StringUtil.isEmpty(id)){
//			return null;
//		}
//		Map<String,Object> params=new HashMap<String, Object>();
//		params.put("id", id);
//		List<AbPoolDtl> list=this.listByHQL("from AbPoolDtl where id=:id", params);
//		if(list!=null&&list.size()>0){
//			return list.get(0);
//		}
//		return null;
//	}
	
	/**
	 * 根据ID获取票据主信息
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbPoolDtl getAbPoolDtlByParams(Map<String,Object> params){
		if(params==null||params.isEmpty()){
			return null;
		}
		StringBuffer sql=new StringBuffer(" select d.id,d.bp_id as bpId, d.VV_ID as vvId,d.app_no as appNo, ");
		sql.append(" d.brief_id as briefId,d.orig_no as origNo,inout_Type as inoutType,status as status,opt_Status as optStatus, d.REPEAL_ID,");
		sql.append(" d.valid_Sign as validSign,d.create_Id as createId,d.update_Id as updateId,d.create_Time as createTime,d.update_Time as updateTime,d.lastid ");
		sql.append(" from Ab_Pool_Dtl d ");
		sql.append(" left join AB_VOUCHERINFO_VICE vv on  d.vv_id=vv.id where 1=1 ");
		if(params.containsKey("id")){
			sql.append(" and d.id=:id ");
		}
		if(params.containsKey("bpId")){
			sql.append(" and d.bp_Id=:bpId ");
		}
		if(params.containsKey("origNo")){
			sql.append(" and d.orig_No=:origNo ");
		}
		if(params.containsKey("briefIds")){
			sql.append(" and d.brief_Id in (:briefIds) ");
		}
		if(params.containsKey("validSign")){
			if("1".equals(params.get("validSign"))){
				sql.append(" and (d.valid_Sign=:validSign or vv.pool_status='1' ) ");
			}else{
				sql.append(" and d.valid_Sign=:validSign ");
			}
		}
		if(params.containsKey("corpId")){
			sql.append(" and vv.corp_id=:corpId ");
		}
		sql.append(" order by d.create_Time desc ");
		List<Map> list=this.listBySQL(sql.toString(), params);
		if(list!=null&&list.size()>0){
			return BeanUtils.copyMapProperties(list.get(0), AbPoolDtl.class);
		}
		return null;
	}
}
