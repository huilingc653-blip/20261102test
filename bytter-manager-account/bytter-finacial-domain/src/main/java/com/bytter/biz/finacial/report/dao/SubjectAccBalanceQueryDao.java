package com.bytter.biz.finacial.report.dao;
import com.bytter.biz.finacial.report.vo.BalanceQueryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class SubjectAccBalanceQueryDao extends BaseDao<BalanceQueryVo,String>{
	/****
	 *
	 * 功能介绍： 查询账户余额表<br>
	 */
	//TODO SYS服务提供 -- 报表数据，暂不处理
	public  List<BalanceQueryVo> queryResult(BalanceQueryVo bean,String userCode,List<String> corpAuth,String ...flag){
		/**
		 * 余留用户权限控制
		 */
		StringBuilder sql = new StringBuilder();//max(subject_id)
		sql.append("select t.* from (select s.id as subject_Id,s.subject_code,s.father_id,s.subject_name,c.parent_id,c.net_id,acc.corp_id, c.name as corpName, acc.balance_way,acc.cur_id,");
		if("4".equals(bean.getSumType())){
			sql.append(" acc.blnctr_Acc as blnctrAcc, acc.acc_name,");
		}
		sql.append(" bt_nvl_num(sum(f.credit_money), 0) credit_money,bt_nvl_num(sum(f.debit_money), 0) debit_money,bt_nvl_num(sum(fmin.yester_balance), 0) yester_balance,");
		sql.append(" bt_nvl_num(sum(fmax.balance), 0) balance ");
		sql.append("  from (select bt_nvl_num(fcl.credit_money, 0) credit_money, bt_nvl_num(fcl.debit_money, 0) debit_money,  fcl.account ");//借贷
		sql.append("  from (select bal.account, sum(bal.credit_money) credit_money, sum(bal.debit_money) debit_money");
		sql.append("  from fc_acc_balance bal where 1 = 1 ");
		if(bean.getGedCloseDate() != null){
			sql.append(" and bal.balance_date >= to_date(:gedCloseDate, 'yyyy-MM-dd') ");
		}
		if(bean.getLedCloseDate() != null){
			sql.append(" and bal.balance_date <= to_date(:ledCloseDate, 'yyyy-MM-dd') ");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getBlnctrAcc()))){
			sql.append(" and bal.account like :blnctrAcc");
		}
		sql.append("  group by account) fcl ) f ");//借贷金额
		sql.append(" inner join fc_acc acc on acc.blnctr_acc = f.account "); // 报表数据，暂不处理
		sql.append(" inner join fc_subject s on s.id = acc.subject_id "); // 报表数据，暂不处理
		sql.append(" inner join sys_corp c on c.id = acc.corp_id ");  // parent_id、net_id、corp_name
		sql.append(" left join (select account, balance_date,yester_balance ");
		sql.append(" from fc_acc_balance b where 1=1 ");
		if(bean.getGedCloseDate() != null){
			sql.append(" and balance_date = (select min(balance_date) from fc_acc_balance b1 where b1.balance_date >= to_date(:gedCloseDate, 'yyyy-MM-dd') and b1.account=b.account) ");
		}
		sql.append(" ) fmin  on acc.BLNCTR_ACC = fmin.account "); //期初
		sql.append(" left join (select account,balance_date,balance ");
		sql.append(" from fc_acc_balance where 1=1 ");
		if(bean.getLedCloseDate() != null){
			sql.append(" and balance_date = to_date(:ledCloseDate, 'yyyy-MM-dd') ");
		}
		sql.append("  ) fmax  on acc.BLNCTR_ACC = fmax.account "); // 期末
		sql.append(" where 1=1 ");
//		sql.append(" and exists (select 1 from sys_account_grant g  ");
//		sql.append("  where acc.id = g.ACCOUNT_ID and g.user_id = :userCode ");
//		sql.append(" and g.CAN_QUERY_BALANCE = '1' ) ");
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getNetId()))){
			sql.append(" and acc.net_Id = :netId");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getCorpId()))){
			sql.append(" and acc.corp_Id = :corpId");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getSubjectId()))){
			sql.append(" and acc.subject_Id = :subjectId");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getCurId()))){
			sql.append(" and acc.cur_Id = :curId");
		}
		if(StringUtils.isEmpty(bean.getNetId().trim()) && StringUtils.isEmpty(bean.getCorpId().trim())){
			sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_Id"));
		}
		//sql.append(" order by acc.cur_id, acc.subject_id, fcl.account)");
		sql.append(" group by c.parent_id,c.net_id,acc.corp_id,c.name, s.id,s.father_id,s.subject_name,s.subject_code,  ");
		if("4".equals(bean.getSumType())){
			sql.append(" acc.blnctr_Acc, acc.acc_name, ");
		}
		sql.append("   acc.cur_id,acc.balance_way ) t ");
		sql.append(" WHERE (t.credit_money <> 0 OR t.debit_money <> 0 OR ");
		sql.append(" yester_balance <> 0 OR balance <> 0) ");
		if(!"1".equals(bean.getSumType())){
			if ("4".equals(bean.getSumType())) {
				sql.append(" ORDER BY t.subject_code,t.blnctrAcc ");
			} else {
				sql.append(" ORDER BY t.subject_id desc ");
			}
		}else {
			sql.append(" ORDER BY t.corp_id desc ");
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpId",bean.getCorpId());
		param.put("netId", bean.getNetId());
		param.put("blnctrAcc", "%"+bean.getBlnctrAcc()+"%");
		param.put("subjectId", bean.getSubjectId());
		param.put("curId", bean.getCurId());
		param.put("userCode", userCode.trim());
		param.put("gedCloseDate", DateUtils.format(bean.getGedCloseDate(),"yyyy-MM-dd"));
		param.put("ledCloseDate", DateUtils.format(bean.getLedCloseDate(),"yyyy-MM-dd"));
		List<BalanceQueryVo>  list = listBySQLAliasToBean(sql.toString(), param, BalanceQueryVo.class);
		if(list==null||list.size()==0){
			return null;
		}
		return list;
	}
	//TODO SYS服务提供
	public List<BalanceQueryVo> queryBalanceBySubject(BalanceQueryVo bean, String userId,List<String> corpAuth,String... flag) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from (select s.id as subject_Id,  s.father_id,s.subject_name, s.subject_code,acc.balance_way, acc.cur_id,");
		sql.append(" bt_nvl_num(sum(f.credit_money), 0) credit_money, bt_nvl_num(sum(f.debit_money), 0) debit_money,");
		sql.append(" bt_nvl_num(sum(fmin.yester_balance), 0) yester_balance,bt_nvl_num(sum(fmax.balance), 0) balance ");
		sql.append(" from (select bt_nvl_num(fcl.credit_money, 0) credit_money, bt_nvl_num(fcl.debit_money, 0) debit_money,fcl.account ");
		sql.append(" from (select bal.account,sum(bal.credit_money) credit_money,sum(bal.debit_money) debit_money ");
		sql.append(" from fc_acc_balance bal where 1 = 1");
		if(bean.getGedCloseDate() != null){
			sql.append(" and bal.balance_date >= to_date(:gedCloseDate, 'yyyy-MM-dd') ");
		}
		if(bean.getLedCloseDate() != null){
			sql.append(" and bal.balance_date <= to_date(:ledCloseDate, 'yyyy-MM-dd') ");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getBlnctrAcc()))){
			sql.append(" and bal.account like :blnctrAcc");
		}
		sql.append(" group by account) fcl) f ");
		sql.append(" inner join fc_acc acc  on acc.blnctr_acc = f.account "); // 报表相关 暂不处理
		sql.append(" inner join fc_subject s  on s.id = acc.subject_id ");
//		sql.append(" inner join sys_corp c on c.id = acc.corp_id "); // 没有实际作用
		sql.append(" left join (select account, balance_date, yester_balance as yester_balance from fc_acc_balance b  where 1 = 1 ");
		if(bean.getGedCloseDate() != null){
			sql.append(" and balance_date = (select min(balance_date) from fc_acc_balance b1 where b1.balance_date >= to_date(:gedCloseDate, 'yyyy-MM-dd') and b1.account=b.account) ");
		}
		sql.append(" ) fmin on acc.BLNCTR_ACC = fmin.account");
		sql.append("  left join (select account, balance_date, balance  from fc_acc_balance where 1 = 1 ");
		if(bean.getLedCloseDate() != null){
			sql.append(" and balance_date = to_date(:ledCloseDate, 'yyyy-MM-dd') ");
		}
		sql.append(" ) fmax on acc.BLNCTR_ACC = fmax.account ");
		sql.append(" where 1 = 1 ");
//		sql.append(" and exists (select 1 from sys_account_grant g  ");
//		sql.append("  where acc.id = g.ACCOUNT_ID and g.user_id = :userCode ");
//		sql.append(" and g.CAN_QUERY_BALANCE = '1' ) ");
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getNetId()))){
			sql.append(" and acc.net_Id = :netId");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getCorpId()))){
			sql.append(" and acc.corp_Id = :corpId");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getSubjectId()))){
			sql.append(" and acc.subject_Id = :subjectId");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getCurId()))){
			sql.append(" and acc.cur_Id = :curId");
		}
		if(StringUtils.isEmpty(bean.getNetId().trim()) && StringUtils.isEmpty(bean.getCorpId().trim())){
			sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_Id"));
		}
		sql.append(" group by s.id, s.father_id, s.subject_name,s.subject_code, acc.cur_id,acc.balance_way) t ");
		sql.append("  WHERE (t.credit_money <> 0 OR t.debit_money <> 0 OR yester_balance <> 0 OR balance <> 0)");
		sql.append("  ORDER BY t.subject_code asc ");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpId",bean.getCorpId());
		param.put("netId", bean.getNetId());
		param.put("subjectId", bean.getSubjectId());
		param.put("blnctrAcc", "%"+bean.getBlnctrAcc()+"%");
		param.put("curId", bean.getCurId());
		param.put("userCode", userId.trim());
		param.put("gedCloseDate", DateUtils.format(bean.getGedCloseDate(),"yyyy-MM-dd"));
		param.put("ledCloseDate", DateUtils.format(bean.getLedCloseDate(),"yyyy-MM-dd"));
		List<BalanceQueryVo>  list = listBySQLAliasToBean(sql.toString(), param, BalanceQueryVo.class);
		if(list==null||list.size()==0){
			return null;
		}
		return list;
	}
	//TODO SYS服务提供 方法没有被调用，暂注释
	/*public List<BalanceQueryVo> queryAccBalanceBySubject(BalanceQueryVo bean, String userId,List<String> corpAuth,String... flag) {
		StringBuilder sql = new StringBuilder();//max(subject_id)
		sql.append(" select d.* ");
		sql.append(" from ( select t.subject_id,t.subject_code,t.father_id,t.subject_name,t.balance_way,t.cur_id,nvl(sum(t.credit_money), 0) credit_money,");
		sql.append(" nvl(sum(t.debit_money), 0) debit_money,nvl(sum(t.yester_balance), 0) yester_balance,nvl(sum(t.balance), 0) balance");
		sql.append(" from (select fcl.subject_id, s.subject_code, s.subject_name, s.father_id,s.balance_way,fcl.cur_id,"); //c.net_id
		sql.append(" nvl(fcl.credit_money, 0) credit_money,nvl(fcl.debit_money, 0) debit_money,nvl(fmin.yester_balance, 0) yester_balance,nvl(fmax.balance, 0) balance  ");
		sql.append(" from (select bal.subject_id, bal.cur_id,sum(bal.tr_credit_money) credit_money,sum(bal.tr_debit_money) debit_money ");//借贷
		sql.append(" from fc_subject_day bal  where 1 = 1 ");
		if(bean.getGedCloseDate() != null){
			sql.append(" and bal.opt_date >= to_date(:gedCloseDate, 'yyyy-MM-dd') ");
		}
		if(bean.getLedCloseDate() != null){
			sql.append(" and bal.opt_date <= to_date(:ledCloseDate, 'yyyy-MM-dd') ");
		}
		sql.append("group by subject_id,cur_id) fcl ");//借贷
		sql.append(" left join (select subject_id, sum(yest_balance) as yester_balance");
		sql.append(" from fc_subject_day where 1 = 1 ");
		if(bean.getGedCloseDate() != null){
			sql.append(" and opt_date = to_date(:gedCloseDate, 'yyyy-MM-dd') ");
		}
		sql.append(" group by subject_id  ) fmin  on fcl.subject_id = fmin.subject_id");//昨日
		sql.append(" left join (select subject_id, sum(today_balance) as balance ");
		sql.append(" from fc_subject_day  where 1 = 1");
		if(bean.getLedCloseDate() != null){
			sql.append(" and opt_date = to_date(:ledCloseDate, 'yyyy-MM-dd') ");
		}
		sql.append(" group by subject_id ) fmax on fcl.subject_id = fmax.subject_id ");//期末
		sql.append(" left join fc_subject s on s.id = fcl.subject_id "); // 方法没有使用
		sql.append(" left join fc_acc acc on acc.subject_id = s.id ");
//		sql.append(" left join sys_corp c on c.id = acc.net_id ");  // 没有实际作用
		sql.append(" and exists (select 1 from sys_account_grant g  ");
		sql.append("  where acc.id = g.ACCOUNT_ID and g.user_id = :userCode ");
		sql.append(" and g.CAN_QUERY_BALANCE = '1' ) ");
		sql.append(" where 1 = 1 ");
		*//* if("5".equals(bean.getSumType())){
		 	sql.append(" and s.father_id in ('I','A','L','B','E','G')");
		 }*//*
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getNetId()))){
			sql.append(" and c.net_Id = :netId");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getCorpId()))){
			sql.append(" and c.id = :corpId");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getSubjectId()))){
			sql.append(" and s.id = :subjectId");
		}
		if(!checkIsNull(StringUtils.trimToEmpty(bean.getCurId()))){
			sql.append(" and fcl.cur_Id = :curId");
		}
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "c.id"));
		sql.append("  group by  fcl.subject_id, s.subject_code,s.subject_name, s.father_id,s.balance_way,fcl.cur_id,");
		sql.append(" fcl.credit_money, fcl.debit_money,fmin.yester_balance,fmax.balance ) t ");
		sql.append("  group by t.subject_id,t.subject_code,t.subject_name,t.father_id,t.balance_way, t.cur_id  )d ");
		sql.append("  WHERE (d.credit_money <> 0 OR d.debit_money <> 0 OR d.yester_balance <> 0 OR  d.balance <> 0)");
		sql.append("  ORDER BY d.subject_code asc");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpId",bean.getCorpId());
		param.put("netId", bean.getNetId());
		param.put("subjectId", bean.getSubjectId());
		param.put("curId", bean.getCurId());
		param.put("userCode", userId.trim());
		param.put("gedCloseDate", DateUtils.format(bean.getGedCloseDate(),"yyyy-MM-dd"));
		param.put("ledCloseDate", DateUtils.format(bean.getLedCloseDate(),"yyyy-MM-dd"));
		List<BalanceQueryVo>  list = listBySQLAliasToBean(sql.toString(), param, BalanceQueryVo.class);
		if(list==null||list.size()==0){
			return null;
		}
		return list;
	}*/
	private static boolean checkIsNull(String str){
		if(EmptyUtils.isEmpty(str) || "".equals(str.trim()) || "undefined".equals(str)){
			return true;
		}
		return false;
	}
}
