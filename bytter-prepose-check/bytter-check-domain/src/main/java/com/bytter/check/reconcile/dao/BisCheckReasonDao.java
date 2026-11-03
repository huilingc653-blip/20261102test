package com.bytter.check.reconcile.dao;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.check.reconcile.entity.BisCheckReason;
import com.bytter.check.reconcile.vo.BisCheckReasonVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class BisCheckReasonDao extends BaseDao<BisCheckReason, String> {
	
	public static StringBuilder initSql(){
		return new StringBuilder(" select br.id as id,").
				append(" br.reason_code as reasonCode,br.reason_name as reasonName,").
				append(" br.bank_type_id as bankTypeId,br.app_scope as appScope,").
				append(" br.valid_sign as validSign,br.create_by as createBy,br.create_date as createDate").
				append(" from BIS_CHECK_REASON br").
				append(" where 1=1" ); 
	}
	
	/**
	 * @param
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		StringBuilder sql = new StringBuilder().append(initSql());
		sql.append(" and br.valid_sign <> :validSign2");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("validSign2",ReceiptConstant.BIS_BANK_VALID_SIGN_2);
        return getDynamicBySQL(input, sql.toString(), BisCheckReasonVO.class, null, false,params);
	}
	
	public BisCheckReasonVO getBisCheckReason(Map<String,Object> params){
	    StringBuilder sql = new StringBuilder(initSql());
		sql.append(" and br.valid_sign <> :validSign2");
	    sql.append(" and br.id =:brId");
		List<BisCheckReasonVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, BisCheckReasonVO.class);
		return ListUtils.isNotNullOrEmpty(listInfo)?listInfo.get(0):null;
	}
	
	   
    public int updateValidSign(Object ids){
    	Map<String, Object> params = new HashMap<String, Object>();
    	String hql = " update BisCheckReason set validSign =:validSign where id =:id";
    	params.put("id",ids);
    	params.put("validSign",ReceiptConstant.BIS_BANK_VALID_SIGN_2);
    	return this.batchExecute(hql, params);
    }
    
    /**
     * 重复唯一校验
     * @param id
     * @param bankTypeId
     * @return
     */
    public boolean repeatCheck(Map<String, Object> params){
    	StringBuilder sql = new StringBuilder(initSql());
    	sql.append(" and br.valid_sign <> :validSign2");
    	params.put("validSign2",ReceiptConstant.BIS_BANK_VALID_SIGN_2);
    	if(params.containsKey("brId")){
       	    sql.append(" and br.id <> :brId");
    	}
//    	if(params.containsKey("brId")){
    		sql.append(" and (br.reason_code = :reasonCode or br.reason_name=:reasonName)");
//    	}
   		List<BisCheckReasonVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, BisCheckReasonVO.class);
   		return ListUtils.isNotNullOrEmpty(listInfo)?true:false;
    }
    
    public List<BisCheckReasonVO> getListByValidSign(Map<String,Object> params){
	    StringBuilder sql = new StringBuilder(initSql());
	    if(params.containsKey("bankTypeId")){
			sql.append(" and br.bank_type_id =:bankTypeId");
		}
	    if(params.containsKey("validSign")){
		    sql.append(" and br.valid_sign =:validSign");
		}
	    if(params.containsKey("rides")){
	    	sql.append(" and br.id in (:rides)");
	    }
		List<BisCheckReasonVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, BisCheckReasonVO.class);
		return listInfo;
	}
    
}
