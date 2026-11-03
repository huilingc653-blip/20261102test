package com.bytter.ras.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.bytter.ras.entity.RasOtherCardBinding;
import com.bytter.ras.vo.RasOtherCardBindingVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;

/**
 * 
* @Title:  营收稽核第三方账户绑定DAO类   
* @Package com.bytter.ras.vo   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-6 下午4:17:43   
 */
@Repository
public class RasOtherCardBindingDao extends BaseDao<RasOtherCardBinding,String>{
	
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,
            List<String> corpAuthList,List<String> shopAuthList){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" ras.id as id,ras.corp_id as corpId,s.Name as corpName,ras.shop_id as shopId,r.Name as shopName,r.code as shopCode,");
		sql.append(" ras.valid_sign as validSign,ras.acc_No as accNo,ras.acc_Name as accName, ");
		sql.append(" ras.pay_type as payType,ras.rmk as rmk,s.code as corpCode,");
		sql.append(" ras.create_name as createName,ras.create_by as createBy,ras.create_date as createDate,");
		sql.append(" ras.update_name as updateName,ras.update_by as updateBy,ras.update_date as updateDate");
		sql.append(" from RAS_OTHER_CARD_BINDING ras");
		sql.append(" left join sys_corp s on s.id=ras.corp_id");
		sql.append(" left join ras_shop r on r.id=ras.shop_id");
		sql.append(" where 1=1");
		Map<String,Object> params=new HashMap<String,Object>();
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "s.id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		String shopAuthSql= SqlUtil.getInSqlByParams(shopAuthList, "r.id","shopId", params);
		sql.append(" and ").append(shopAuthSql).append(" ");
		DataTablesOutput q=getDynamicBySQL(input, sql.toString(),RasOtherCardBindingVO.class,"", false,params);
		return q;
	}
	
	/**
	 * 
	* @Title: checkRasBankCardBindingNoUnique  
	* @Description: TODO(查询判断第三方账号绑定是否存在重复)  
	* @param @param rasOtherCardBindingVO
	* @param @param operate
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	public boolean checkRasOtherCardBindingNoUnique(RasOtherCardBindingVO rasOtherCardBindingVO,String operate){
		StringBuilder sql = new StringBuilder();
		sql.append(" select ras.id from RAS_OTHER_CARD_BINDING ras");
		sql.append(" where 1=1");
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getId())
				&&operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_UPDATE)){
			sql.append(" and ras.id <>:id");
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getPayType())){
			sql.append(" and ras.pay_Type =:payType");
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getShopId())){
			if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_OPEN)){
				sql.append(" and ras.shop_Id <>:shopId");
			}else{
				sql.append(" and ras.shop_Id =:shopId");
			}
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getAccNo())){
			sql.append(" and ras.acc_No =:accNo");
		}
		SQLQuery query=createSQLQuery(sql.toString());
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_UPDATE)
				&& !StringUtils.isBlank(rasOtherCardBindingVO.getId())){
			query.setString("id", rasOtherCardBindingVO.getId());
	    }
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getPayType())){
			query.setString("payType", rasOtherCardBindingVO.getPayType());
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getShopId())){
			query.setString("shopId", rasOtherCardBindingVO.getShopId());
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getAccNo())){
			query.setString("accNo", rasOtherCardBindingVO.getAccNo());
		}
		if(!query.list().isEmpty()){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	* @Title: getRasOtherCardBindingVOign  
	* @Description: TODO()  
	* @param @param id
	* @param @return    参数  
	* @return List<RasOtherCardBindingVO>    返回类型  
	* @throws
	 */
	public List<RasOtherCardBindingVO> getRasOtherCardBindingVoSign(String id) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" ras.id as id,ras.corp_id as corpId,s.Name as corpName,ras.shop_id as shopId,r.Name as shopName,r.code as shopCode,");
		sql.append(" ras.valid_sign as validSign,ras.acc_No as accNo,ras.acc_Name as accName, ");
		sql.append(" ras.pay_type as payType,ras.rmk as rmk,s.code as corpCode,");
		sql.append(" ras.create_name as createName,ras.create_by as createBy,ras.create_date as createDate,");
		sql.append(" ras.update_name as updateName,ras.update_by as updateBy,ras.update_date as updateDate");
		sql.append(" from RAS_OTHER_CARD_BINDING ras");
		sql.append(" left join sys_corp s on s.id=ras.corp_id");
		sql.append(" left join ras_shop r on r.id=ras.shop_id");
		sql.append(" where 1=1");
		sql.append(" and ras.id=:id");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id",id);
		List<RasOtherCardBindingVO> list = this.listBySQLAliasToBean(sql.toString(), params, RasOtherCardBindingVO.class);
		return list;
	}
	
	
	/**
	 * 
	* @Title: updateValidSign  
	* @Description: TODO(更新启用/禁用)  
	* @param @param id    参数  
	* @return void    返回类型  
	* @throws
	 */
	public void updateValidSign(String id,String validSign){
		StringBuilder hql=new StringBuilder("update RasOtherCardBinding ");
		hql.append(" set validSign =:validSign");
		hql.append(" where id=:id");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("validSign", validSign);
		params.put("id", id);
		this.batchExecute(hql.toString(), params);
	}
	
	public List<String> queryDictList(String dictType){
		StringBuilder sql =new StringBuilder();
		sql.append(" select sd.type_name from SYS_DICT sd,sys_dict_group sdg ");
		sql.append(" where 1=1");
		sql.append(" and sd.type_group_id=sdg.id");
		sql.append(" and sdg.type_group_code =:typeCode ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("typeCode", dictType);
		@SuppressWarnings("unchecked")
		List<String> dictList = createSQLQuery(sql.toString(), params).list();
		return dictList;
	}
	
	public String queryDictList(String typeName,String dictType){
		StringBuilder sql =new StringBuilder();
		sql.append(" select sd.type_code from SYS_DICT sd,sys_dict_group sdg ");
		sql.append(" where 1=1");
		sql.append(" and sd.type_group_id=sdg.id");
		sql.append(" and sdg.type_group_code =:dictType");
		sql.append(" and sd.type_name =:typeName");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dictType", dictType);
		params.put("typeName", typeName);
		try{
			List<String> dictList = createSQLQuery(sql.toString(), params).list();
			if(null!=typeName &&dictList.size()>0){
				return dictList.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public int updateRasOtherCardBinding(RasOtherCardBindingVO rasOtherCardBindingVO){
		StringBuilder hql=new StringBuilder("update RasOtherCardBinding  ");
		Map<String,Object> params = new HashMap<String,Object>();
		hql.append(" set ");
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getCorpId())){
			hql.append(" corpId=:corpId");
			params.put("corpId", rasOtherCardBindingVO.getCorpId());
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getShopId())){
			hql.append(",shopId=:shopId");
			params.put("shopId", rasOtherCardBindingVO.getShopId());
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getValidSign())){
			hql.append(",validSign=:validSign");
			params.put("validSign", rasOtherCardBindingVO.getValidSign());
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getPayType())){
			hql.append(",payType=:payType");
			params.put("payType", rasOtherCardBindingVO.getPayType());
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getAccNo())){
			hql.append(",accNo=:accNo");
			params.put("accNo", rasOtherCardBindingVO.getAccNo());
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getAccName())){
			hql.append(",accName=:accName");
			params.put("accName", rasOtherCardBindingVO.getAccName());
		}
//		if(!StringUtils.isBlank(rasOtherCardBindingVO.getRmk())){
			hql.append(",rmk=:rmk");
			params.put("rmk", rasOtherCardBindingVO.getRmk());
//		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getUpdateName())){
			hql.append(",updateName=:updateName");
			params.put("updateName", rasOtherCardBindingVO.getUpdateName());
		}
		if(!StringUtils.isBlank(rasOtherCardBindingVO.getUpdateBy())){
			hql.append(",updateBy=:updateBy");
			params.put("updateBy", rasOtherCardBindingVO.getUpdateBy());
		}
		if(null!=rasOtherCardBindingVO.getUpdateDate()){
			hql.append(",updateDate=:updateDate");
			params.put("updateDate", rasOtherCardBindingVO.getUpdateDate());
		}
		hql.append(" where id=:id");
		params.put("id", rasOtherCardBindingVO.getId());
		return this.batchExecute(hql.toString(), params);
	}

	/**
	 * 根据参数获取第三方账号绑定数据
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RasOtherCardBinding> getRasOtherCardBindingByParams(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("from RasOtherCardBinding where 1=1 ");
		if(params.containsKey("id")){
			hql.append(" and id=:id ");
		}
		if(params.containsKey("accNo")){
			hql.append(" and accNo=:accNo ");
		}
		if(params.containsKey("validSign")){
			hql.append(" and validSign=:validSign ");
		}
		return this.listByHQL(hql.toString(), params);
	}
}
