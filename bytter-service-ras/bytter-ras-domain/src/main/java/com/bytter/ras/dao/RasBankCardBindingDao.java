package com.bytter.ras.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.bytter.ras.entity.RasBankCardBinding;
import com.bytter.ras.vo.RasBankCardBindingVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.system.api.dto.AccountSearchDTO;

/**
 * 
* @Title:  营收稽核银行卡绑定数据库操作类   
* @Package com.bytter.ras.dao   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-5 下午5:56:08   
 */
@Repository
public class RasBankCardBindingDao extends BaseDao<RasBankCardBinding,String>{

	
	/**
	 * 
	* @Title: getDataTablesUnion  
	* @Description: TODO(营收稽核银行卡查询列表)  
	* @param @param input
	* @param @param corpAuthList
	* @param @return    参数  
	* @return DataTablesOutput    返回类型  
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,
            List<String> corpAuthList,List<String> shopAuthList){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" ras.id as id,ras.corp_id as corpId,s.Name as corpName,ras.shop_id as shopId,r.Name as shopName,r.code as shopCode,");
		sql.append(" ras.valid_sign as validSign,ras.is_virtual_acc as isVirtualAcc,");
		sql.append(" ras.acc_id as accId,ras.acc_no as accNo,ras.acc_name as accName,");
		sql.append(" ras.acc_bank_name as accBankName,ras.entity_acc_id as entityAccId,");
		sql.append(" ras.entity_acc_no as entityAccNo,ras.rmk as rmk,s.code as corpCode,");
		sql.append(" ras.create_name as createName,ras.create_by as createBy,ras.create_date as createDate,");
		sql.append(" ras.update_name as updateName,ras.update_by as updateBy,ras.update_date as updateDate");
		sql.append(" from ras_bank_card_binding ras");
		sql.append(" left join sys_corp s on s.id=ras.corp_id");
		sql.append(" left join ras_shop r on r.id=ras.shop_id");
		sql.append(" where 1=1");
		Map<String,Object> params=new HashMap<String,Object>();
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "s.id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		String shopAuthSql= SqlUtil.getInSqlByParams(shopAuthList, "r.id","shopId", params);
		sql.append(" and ").append(shopAuthSql).append(" ");
		DataTablesOutput q=getDynamicBySQL(input, sql.toString(),RasBankCardBindingVO.class,"", false,params);
		return q;
	}
	
	/**
	 * 
	* @Title: checkRasBankCardBindingNoUnique  
	* @Description: TODO(验证营收稽核银行卡是否存在)  
	* @param @param bankCardBindingVO
	* @param @param operate
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	public boolean checkRasBankCardBindingNoUnique(RasBankCardBindingVO bankCardBindingVO,String operate){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" bta.ID");
		sql.append(" from RAS_BANK_CARD_BINDING bta ");
		sql.append(" where 1=1");
		sql.append(" and bta.acc_id  =:accId");
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_UPDATE)){
			sql.append(" and bta.shop_id =:shopId");
		}
		if(!StringUtils.isBlank(bankCardBindingVO.getId())){
			sql.append(" and bta.id <>:id");
		}
		SQLQuery query=createSQLQuery(sql.toString());
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_UPDATE)){
			query.setString("shopId", bankCardBindingVO.getShopId());
		}
		query.setString("accId", bankCardBindingVO.getAccId());

		if(!StringUtils.isBlank(bankCardBindingVO.getId())){
			query.setString("id", bankCardBindingVO.getId());
	    }
		
		if(!query.list().isEmpty()){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 
	* @Title: getRasBankCardBindingSign  
	* @Description: TODO(单条营收稽核银行卡详细信息)  
	* @param @param id
	* @param @return    参数  
	* @return List<RasBankCardBindingVO>    返回类型  
	* @throws
	 */
	public List<RasBankCardBindingVO> getRasBankCardBindingSign(String id) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" ras.id as id,ras.corp_id as corpId,s.Name as corpName,ras.shop_id as shopId,r.Name as shopName,r.code as shopCode,");
		sql.append(" ras.valid_sign as validSign,ras.is_virtual_acc as isVirtualAcc,");
		sql.append(" ras.acc_id as accId,ras.acc_no as accNo,ras.acc_name as accName,");
		sql.append(" ras.acc_bank_name as accBankName,ras.entity_acc_id as entityAccId,");
		sql.append(" ras.entity_acc_no as entityAccNo,ras.rmk as rmk,s.code as corpCode,");
		sql.append(" ras.create_name as createName,ras.create_by as createBy,ras.create_date as createDate,");
		sql.append(" ras.update_name as updateName,ras.update_by as updateBy,ras.update_date as updateDate");
		sql.append(" from ras_bank_card_binding ras");
		sql.append(" left join sys_corp s on s.id=ras.corp_id");
		sql.append(" left join ras_shop r on r.id=ras.shop_id");
		sql.append(" where 1=1");
		sql.append(" and ras.id=:id");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id",id);
		List<RasBankCardBindingVO> list = this.listBySQLAliasToBean(sql.toString(), params, RasBankCardBindingVO.class);
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
		StringBuilder hql=new StringBuilder("update RasBankCardBinding ");
		hql.append(" set validSign =:validSign");
		hql.append(" where id=:id");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("validSign", validSign);
		params.put("id", id);
		this.batchExecute(hql.toString(), params);
	}

	/**
	 * 根据参数获取稽核银行卡绑定数据库实体类
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RasBankCardBinding> getRasBankCardBindingByParams(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("from RasBankCardBinding where 1=1 ");
		if(params.containsKey("id")){
			hql.append(" and id=:id ");
		}
		if(params.containsKey("accNo")){
			hql.append(" and accNo=:accNo ");
		}
		if(params.containsKey("entityAccNo")){
			hql.append(" and entityAccNo =:entityAccNo ");
		}

		return this.listByHQL(hql.toString(), params);
	}
}
