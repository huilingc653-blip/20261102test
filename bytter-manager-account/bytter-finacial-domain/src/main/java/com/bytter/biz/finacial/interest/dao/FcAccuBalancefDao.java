package com.bytter.biz.finacial.interest.dao;

import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.vo.FcAccuBalancefVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: 利息管理-积数查询 
* @Description: FC_ACCU_BALANCE
* @author zhengxn
* @date 2017-06-28 14:27:04
* @version 1.0
*
*/
@Repository
public class FcAccuBalancefDao extends BaseDao<FcAccuBalancef,String>{

	public FcAccuBalancef getFcAccuBalancefById(String id) {
		StringBuilder hql = new StringBuilder("from FcAccuBalancef where id = ?0 ");
		List<FcAccuBalancef> list = createQuery(hql.toString(), id).list();
		if (!EmptyUtils.isEmptyList(list)) {
			return list.get(0);
		}
		return null;
	}
	/**积数查询列表
	 * 参数 List<String> corpAuth 改为 List<String> accounts
	 * **/
	public  DataTablesOutput listAll(DataTablesInput input,List<String> accounts,boolean isPool){
		StringBuilder sql = new StringBuilder();
        /*sql.append("select b.account as account,a.acc_name as accName,b.balance_date as balanceDate,");
		sql.append("b.cumu as cumu,b.adjust_cumu as adjustCumu,b.interest_sign as interestSign,s.id as subjectId ");
		if(true == isPool){
			sql.append(" ,s.subject_name as subjectPoolName ");
		}else {
			sql.append(" ,s.subject_name as subjectName ");
		}
		sql.append(" from fc_accu_balance b left join fc_acc a  on b.account=a.blnctr_acc  ");
		if(true == isPool){
			sql.append(" left join  fc_subject_pool s on a.subject_id = s.id ");
			sql.append(" where 1=1 and a.pool_sign = '1' ");
		}else{
			sql.append(" left join  fc_subject s on a.subject_id = s.id ");
			sql.append(" where 1=1 and a.pool_sign != '1' ");
		}
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));*/
		//拆掉 fc_acc/fc_subject  a.blnctr_acc\ a.acc_name \s.subject_id \s.subject_name where a.corp_id = corpAuth
		//注意修改后传入的参数 corpAuth 代表的是 b.account
		sql.append("select b.account as account, b.balance_date as balanceDate,");
		sql.append(" b.cumu as cumu, b.adjust_cumu as adjustCumu, b.interest_sign as interestSign ");
		sql.append(" from fc_accu_balance b left join fc_acc a on b.account=a.blnctr_acc where 1=1 ");
		sql.append(" and ").append(getSQLIn(accounts, 900, "b.account"));
		return getDynamicBySQL(input, sql.toString(), FcAccuBalancefVo.class,"",false);
	}
	public List<FcAccuBalancefVo> excel(FcAccuBalancefVo vo,List<String> corpAuth){
		StringBuilder sql = new StringBuilder();
        /*sql.append("select b.account as account,a.acc_name as accName,s.subject_name as subjectName,b.balance_date as balanceDate,b.cumu as cumu,b.adjust_cumu as adjustCumu,b.interest_sign as interestSign");
		//拆 fc_acc、fc_subject ，4个条件 查询 blnctr_acc, acc_name, subject_name,
		sql.append(" from fc_accu_balance b left join fc_acc a  on b.account=a.blnctr_acc  ");
		sql.append("left join  fc_subject s on a.subject_id = s.id ");
		sql.append(" where 1=1 and a.pool_sign != '1' ");
		if(EmptyUtils.isNotEmpty(vo.getEqs_netId()) && !" ".equals(vo.getEqs_netId())){
			sql.append(" and a.net_id = :netId");
		}
		if(vo.getGed_balanceDate() != null && vo.getLed_balanceDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String geDate = sdf.format(vo.getGed_balanceDate());
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			String leDate = sdf1.format(vo.getLed_balanceDate());
			sql.append(" and b.balance_date >= BT_TO_DATE(:geDate,'yyyy-mm-dd')");
			sql.append(" and b.balance_date <= BT_TO_DATE(:leDate,'yyyy-mm-dd')");
		}
		if(EmptyUtils.isNotEmpty(vo.getLikes_account()) && !" ".equals(vo.getLikes_account())){
			sql.append(" and b.account = :account");
		}
		if(EmptyUtils.isNotEmpty(vo.getEqs_interestSign()) && !" ".equals(vo.getEqs_interestSign())){
			sql.append(" and b.interest_sign = :interestSign");
		}
		if(EmptyUtils.isNotEmpty(vo.getIns_accType()) && !" ".equals(vo.getIns_accType())){
			sql.append(" and a.acc_type = :accType");
		}
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("netId", vo.getEqs_netId());
		param.put("geDate",vo.getGed_balanceDate());
		param.put("leDate",vo.getLed_balanceDate());
		param.put("account",vo.getLikes_account());
		param.put("interestSign", vo.getEqs_interestSign());
		param.put("accType", vo.getIns_accType());*/
		sql.append("select b.account as account,b.balance_date as balanceDate,b.cumu as cumu,b.adjust_cumu as adjustCumu,b.interest_sign as interestSign");
		//拆 fc_acc、fc_subject ; where fc_acc a on b.account=a.blnctr_acc
		sql.append(" from fc_accu_balance b ");
		sql.append(" where 1=1 ");
		if(vo.getGed_balanceDate() != null && vo.getLed_balanceDate() != null){
			sql.append(" and b.balance_date >= BT_TO_DATE(:geDate,'yyyy-mm-dd')");
			sql.append(" and b.balance_date <= BT_TO_DATE(:leDate,'yyyy-mm-dd')");
		}
		if(EmptyUtils.isNotEmpty(vo.getLikes_account()) && !" ".equals(vo.getLikes_account())){
			sql.append(" and b.account = :account");
		}
		if(EmptyUtils.isNotEmpty(vo.getEqs_interestSign()) && !" ".equals(vo.getEqs_interestSign())){
			sql.append(" and b.interest_sign = :interestSign");
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("geDate",vo.getGed_balanceDate());
		param.put("leDate",vo.getLed_balanceDate());
		param.put("account",vo.getLikes_account());
		param.put("interestSign", vo.getEqs_interestSign());
		List<FcAccuBalancefVo>  list = listBySQLAliasToBean(sql.toString(),param, FcAccuBalancefVo.class);
		return list;
	}
	/**调整积数表的全部数据
	 * 修改参数 List<String> corpAuth 改为 accounts
	 */
	public  DataTablesOutput getAll(DataTablesInput input,List<String> accounts){
		StringBuilder sql = new StringBuilder();
		/*sql.append("select b.id as id,b.account as account,a.acc_name as accName,s.subject_name as subjectName,b.balance_date as balanceDate,");
		sql.append("b.cumu as cumu,b.adjust_cumu as adjustCumu,b.interest_sign as interestSign,b.interest_time as interestTime,b.pre_sign as preSign");
		sql.append(" from fc_accu_balance b left join fc_acc a on b.account = a.blnctr_acc");
		sql.append(" left join fc_subject s on a.subject_id = s.id");
		sql.append(" where 1=1 and a.pool_sign != '1' ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));*/
		sql.append("select b.id as id,b.account as account,b.balance_date as balanceDate,");
		sql.append("b.cumu as cumu,b.adjust_cumu as adjustCumu,b.interest_sign as interestSign,b.interest_time as interestTime,b.pre_sign as preSign");
		sql.append(" from fc_accu_balance b ");
		sql.append(" where 1=1 ");
		sql.append(" and ").append(getSQLIn(accounts, 900, "b.account"));
		return getDynamicBySQL(input, sql.toString(), FcAccuBalancefVo.class,"b",false);
	}
}
