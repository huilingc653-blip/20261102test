package com.bytter.biz.finacial.report.dao;
import com.bytter.biz.finacial.report.vo.BalanceQueryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class BalanceQueryDao extends BaseDao<BalanceQueryVo,String>{
	/****
	 * 
	 * 功能介绍： 查询账户余额表<br>
	 * <b>日期：</b>2018年1月9日<br>
	 * @version 1.0
	 * @author zhengxn 
	 * flags是录入标志，可以去掉,网点下属单位是否需要过滤
	 */
	//TODO 暂不处理报表数据 -- fc_subject 已经迁移到基础模块
	 public  List<BalanceQueryVo> queryResult(BalanceQueryVo bean,String userCode,List<String> corpAuth,String ...flag){
         /**
          * 余留用户权限控制
          */
         // fc_acc 表属于基础域; 这个 a.* 到底用了哪些字段？？？账号、账户名称、借贷方向
		 StringBuilder sql = new StringBuilder();
		 sql.append("select a.*,fo.TALLY_CNT,fo.CHECK_CNT,fo.ONLINE_BALANCE,fo.CHECK_BALANCE,fo.FREEZE_MONEY,c.name as corpName," +
				 "s.subject_name,s.id as subjectId  from fc_acc a "); //把 fc_acc 基础域-- 报表暂不处理
		 sql.append(" inner join fc_online_ledger fo ON a.blnctr_acc = fo.account ");
		 sql.append(" left join fc_subject s on s.id = a.subject_id ");  // fc_subject 基础域-- 报表暂不处理 s 账号、账户名称、借贷方向、结算中心账号；corp_code
		 sql.append(" left join sys_corp c on c.id = a.corp_id"); //把 sys_corp 基础域-- 报表暂不处理 c.name as corpName
		 sql.append(" where 1 = 1 ");
		 sql.append(" and exists (select 1 from sys_account_grant g  ");  //sys_account_grant; 需要排除这部分
		 sql.append("  where a.id = g.ACCOUNT_ID and g.user_id = :userCode ");
		 sql.append(" and g.CAN_QUERY_BALANCE = '1' ) ");
		 if(!checkIsNull(StringUtils.trimToEmpty(bean.getCorpId()))){
			 sql.append(" and a.corp_Id = :corpId");
		 }
		 if(!checkIsNull(StringUtils.trimToEmpty(bean.getNetId()))){
			 sql.append(" and a.net_Id = :netId");
		 }
		 if(!checkIsNull(StringUtils.trimToEmpty(bean.getBlnctrAcc()))){
			 sql.append(" and a.blnctr_acc = :blnctrAcc");
		 }
		 if(!checkIsNull(StringUtils.trimToEmpty(bean.getAccState()))){
			 String[] accState =  bean.getAccState().split(",");
			 List<String> accsStates = Arrays.asList(accState);
			 sql.append(" and ").append(getSQLIn(accsStates, 900, "a.acc_state"));
		 }
		 if(!checkIsNull(StringUtils.trimToEmpty(bean.getAccType()))){
			 if(flag == null || flag.length == 0){//账户到期明细表查询
      		   sql.append(" and a.acc_Type in ('03','04','05','06','09','11','12')");
			 }
			 sql.append(" and a.acc_type = :accType");
		 }
		 if(!checkIsNull(StringUtils.trimToEmpty(bean.getSubjectId()))){
			 sql.append(" and a.subject_Id = :subjectId");
		 }
		 if(!checkIsNull(StringUtils.trimToEmpty(bean.getCurId()))){
			 sql.append(" and a.cur_Id = :curId");
		 }
		 sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_Id"));
		 if ("4".equals(bean.getSumType())) {//科目+账号维度需过滤掉记账余额和复合余额为0的数据
		 	sql.append("and fo.ONLINE_BALANCE != 0 and fo.CHECK_BALANCE != 0");
		 }
		//按照统计维度进行排序：1-按单位+科目；2-按科目+单位；3-按科目+银行；4-按科目+账号
		if("2".equals(bean.getSumType())){
			 sql.append(" order by s.subject_code,c.code,a.blnctr_Acc ");
		}else if("3".equals(bean.getSumType()) || "4".equals(bean.getSumType())){
			 sql.append(" order by s.subject_code,a.blnctr_Acc ");
		}else {
			sql.append(" order by c.code, s.subject_code, a.blnctr_Acc");
		}
		 Map<String,Object> param = new HashMap<String,Object>();
		 param.put("corpId",bean.getCorpId());
		 param.put("netId", bean.getNetId());
		 param.put("blnctrAcc", bean.getBlnctrAcc());
		 param.put("accState", bean.getAccState());
		 param.put("accType", bean.getAccType());
		 param.put("subjectId", bean.getSubjectId());
		 param.put("curId", bean.getCurId());
		 param.put("userCode", userCode.trim());
		 List<BalanceQueryVo>  list = listBySQLAliasToBean(sql.toString(), param, BalanceQueryVo.class);
		 if(list==null||list.size()==0){
			 return null;
		 }
		 return list;
	}
	/**
	 *
	 * @param accounts fc_online_ledger.account
	 * @return
	 */
	public List<BalanceQueryVo> queryAccBalanceAndBank_2(List<String> accounts){
		StringBuilder sql = new StringBuilder();
		sql.append("select l.account as account, BT_NVL_NUM(l.freeze_money,0) freeze_money, BT_NVL_NUM(l.online_balance,0) online_balance,");
		sql.append(" BT_NVL_NUM(l.check_balance,0) check_balance, BT_NVL_NUM(l.tally_cnt,0) tally_cnt, BT_NVL_NUM(l.check_cnt,0) check_cnt  from fc_online_ledger l where 1 = 1");
		if (ListUtils.isNotNullOrEmpty(accounts)){
			sql.append(" and ").append(getSQLIn(accounts, 900, "l.account"));
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("account", accounts);
		List<BalanceQueryVo>  list = listBySQLAliasToBean(sql.toString(), param, BalanceQueryVo.class);
		if(list==null||list.size()==0){
			return null;
		}
		return list;
	}
	/**
	 * 弃用
	 * @param bean
	 * @param userId
	 * @param corpAuth
	 * @param flag
	 * @return
	 */
	//TODO SYS服务提供 -- queryAccBalanceAndBank_2；
	 public List<BalanceQueryVo> queryAccBalanceAndBank(BalanceQueryVo bean, String userId,List<String> corpAuth,String... flag) {
		/*StringBuilder sql = new StringBuilder();
		sql.append("select t.*,c.name as corpName, s.subject_name,s.id as subjectId,f.typeName,BT_NVL_NUM(l.freeze_money,0) freeze_money,BT_NVL_NUM(l.online_balance,0) online_balance,");
		sql.append("BT_NVL_NUM(l.check_balance,0) check_balance,BT_NVL_NUM(l.tally_cnt,0) tally_cnt,BT_NVL_NUM(l.check_cnt,0) check_cnt  from fc_acc t ");
		sql.append("  left join fc_online_ledger l  on t.blnctr_acc = l.account");
		sql.append(" left join fc_subject s on s.id = t.subject_id ");
		sql.append(" left join sys_corp c on c.id = t.corp_id"); // 拆出去
		sql.append("  left join (SELECT z.COUNTER_ACC, d.type_name as typeName, b.bank_type_id,d.bank_type ");
		sql.append("  FROM BT_BANK_ACC b,bt_bank_acc_cur z,bt_bank_type d,"); // 基础域
		sql.append("  (SELECT z.counter_acc  FROM BT_BANK_ACC c");
		sql.append("  inner join bt_bank_acc_cur z");  //基础域
		sql.append(" on c.id = z.bank_acc_id WHERE z.counter_acc IS NOT NULL");
		sql.append(" GROUP BY z.counter_acc  HAVING COUNT(1) = 1) e ");
		sql.append("  WHERE b.bank_type_id = d.id and b.id = z.bank_acc_id ");
		sql.append(" AND z.counter_acc = e.counter_acc) f ");
		sql.append(" on t.blnctr_acc = f.counter_acc ");
		sql.append("  where 1 = 1  and t.acc_State <> 'T' ");
		sql.append(" and exists (select 1 from sys_account_grant g where t.id = g.ACCOUNT_ID");
		sql.append(" and g.CAN_QUERY_BALANCE = '1'");
		sql.append(" and g.USER_ID = :userId)");
		*//*Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);*//*
	   *//**
	     * 余留用户权限控制
	   *//*
		if (flag == null || flag.length == 0) {//账户到期明细表查询
			sql.append(" and t.acc_State != 'D' ");
			sql.append(" and t.deadline_Date <= :deadlineDate ");
			if(EmptyUtils.isBlank(bean.getAccType())){
				sql.append(" and t.acc_Type_Code in ('03','04','05','06','09','11','12')");
			}
		}
		if(!checkIsNull(bean.getCorpId())){
			 sql.append(" and t.corp_Id = :corpId");
		}
		if(!checkIsNull(bean.getNetId())){
			 sql.append(" and t.net_Id = :netId");
		}
		if(!checkIsNull(bean.getBlnctrAcc())){
			 sql.append(" and t.blnctr_acc = :blnctrAcc");
		}
		if(!checkIsNull(bean.getAccState())){
			 sql.append(" and t.acc_state = :accState");
		}
		if(!checkIsNull(bean.getAccType())){
			if(flag == null || flag.length == 0){//账户到期明细表查询
		   sql.append(" and t.acc_Type in ('03','04','05','06','09','11','12')");
		}
			 sql.append(" and t.acc_type = :accType");
		}
		if(!checkIsNull(bean.getSubjectId())){
			 sql.append(" and t.subject_Id = :subjectId");
		}
		if(!checkIsNull(bean.getCurId())){
			 sql.append(" and t.cur_Id = :curId");
		}
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "t.corp_Id"));
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpId", bean.getCorpId());
		param.put("netId", bean.getNetId());
		param.put("blnctrAcc", bean.getBlnctrAcc());
		param.put("accState", bean.getAccState());
		param.put("accType", bean.getAccType());
		param.put("subjectId", bean.getSubjectId());
		param.put("curId", bean.getCurId());
		param.put("userId", userId);
		sql.append(" order by s.subject_code, f.bank_type,t.blnctr_Acc  ");
		List<BalanceQueryVo>  list = listBySQLAliasToBean(sql.toString(), param, BalanceQueryVo.class);
		if(list==null||list.size()==0){
            return null;
		}
		return list;*/
		 return null;
	 } 
	 
	 private static boolean checkIsNull(String str){
		if(EmptyUtils.isEmpty(str) || "".equals(str.trim()) || "undefined".equals(str)){
			return true;
		}
		return false;
	 }
	 
}
