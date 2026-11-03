package com.bytter.biz.report.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.biz.payment.api.dto.InnerAccChangingDTO;
import org.springframework.stereotype.Repository;
import com.bytter.biz.report.vo.InnerAccChangingVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
/**
 * @author zhangxb
 * @Description 内部账户改户
 */
@Repository
public class InnerAccChangingDao extends BaseDao<InnerAccChangingDTO, String>{
	
	public List<InnerAccChangingVO> getList(String startTime,String endTime,String corpId,String netId,List<String> corpAuth){
		StringBuilder sql = new StringBuilder();
		List<String> corpIdList = null;
		if(StringUtils.isNotBlank(corpId)){
			corpIdList = StringUtils.splitToList(",",corpId);
		}
		List<String> netIdList = StringUtils.splitToList(",",netId);
		sql.append("select a.net_id as netId,a.corp_id as corpId,count(ac.blnctr_acc) as acc,count(v.blnctr_acc) as history, ");
		sql.append("count(w.blnctr_acc) as writeoff,count(f.blnctr_acc) as freeze,count(t.blnctr_acc) as thaw ");
		sql.append(" from fc_acc a");
		
		sql.append(" left join (select distinct  h.blnctr_acc from fc_acc_history h ");
		sql.append(" where h.check_back_reason is null and h.checker_code is not null ");
		if(EmptyUtils.isNotEmpty(startTime) && EmptyUtils.isNotEmpty(endTime)){
			sql.append(" and h.check_time >= bt_to_date('"+startTime+"','yyyy-MM-dd')");
			sql.append(" and h.check_time <= bt_to_date('"+endTime+"','yyyy-MM-dd') ");
		}
		sql.append(" group by h.blnctr_acc) v on a.blnctr_acc = v.blnctr_acc "); //改户
		
		sql.append(" left join (select distinct  tt.blnctr_acc from fc_freeze tt where tt.freeze_sign = 'T' ");
		if(EmptyUtils.isNotEmpty(startTime) && EmptyUtils.isNotEmpty(endTime)){
			sql.append(" and tt.operate_time >= bt_to_date('"+startTime+"','yyyy-MM-dd')");
			sql.append(" and tt.operate_time <= bt_to_date('"+endTime+"','yyyy-MM-dd') ");
		}
		sql.append(" group by tt.blnctr_acc) t on a.Blnctr_Acc = t.Blnctr_Acc");//解冻
		
		sql.append(" left join (select distinct  ww.blnctr_acc from fc_acc ww where ww.acc_state='D' ");
		if(EmptyUtils.isNotEmpty(startTime) && EmptyUtils.isNotEmpty(endTime)){
			sql.append(" and ww.check_time >= bt_to_date('"+startTime+"','yyyy-MM-dd')");
			sql.append(" and ww.check_time <= bt_to_date('"+endTime+"','yyyy-MM-dd') ");
		}
		sql.append("  group by ww.blnctr_acc) w  on a.Blnctr_Acc = w.Blnctr_Acc ");//注销
		
		sql.append(" left join (select distinct  ff.blnctr_acc from fc_freeze ff  where  ff.freeze_sign = 'C' ");
		if(EmptyUtils.isNotEmpty(startTime) && EmptyUtils.isNotEmpty(endTime)){
			sql.append(" and ff.check_time >= bt_to_date('"+startTime+"','yyyy-MM-dd')");
			sql.append(" and ff.check_time <= bt_to_date('"+endTime+"','yyyy-MM-dd')");
		}
		sql.append(" group by ff.blnctr_acc) f on a.Blnctr_Acc = f.Blnctr_Acc ");//冻结
		
		sql.append(" left join (select distinct  aa.blnctr_acc from fc_acc aa where aa.acc_state ='N' ");
		if(EmptyUtils.isNotEmpty(startTime) && EmptyUtils.isNotEmpty(endTime)){
			sql.append(" and aa.acc_open_time >= bt_to_date('"+startTime+"','yyyy-MM-dd')");
			sql.append(" and aa.acc_open_time <= bt_to_date('"+endTime+"','yyyy-MM-dd')");
		}
		sql.append(" group by aa.blnctr_acc) ac on a.Blnctr_Acc = ac.Blnctr_Acc "); //正常
		sql.append(" where 1 = 1 ");
		if(null != corpIdList && corpIdList.size() > 0){
			sql.append(" and ").append(getSQLIn(corpIdList, 900, "a.corp_id"));
		}
		sql.append(" and ").append(getSQLIn(netIdList, 900, "a.net_id"));
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));
		sql.append(" group by a.net_id,a.corp_id ");
		return listBySQLAliasToBean(sql.toString(), null,InnerAccChangingVO.class);
	}
	
	
	public List<InnerAccChangingVO> getAccByCorpId(String corpId,String startTime,String endTime,String type){
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct  fa.blnctr_acc as blnctrAcc,fa.acc_name as accName,fa.net_id as netId,fa.corp_id as corpId,fa.acc_type as accType,");
		sql.append("fa.cur_id as curId,fc.check_balance as checkBalance,(fc.check_balance - fc.freeze_money) as availableBalance,");
		sql.append("fa.overdraft_money as overdraftMoney,fa.acc_open_time as accOpenTime ");
		sql.append(",wfm.wfMoney ,fm.freezeMoney ");
		if("freeze".equals(type) || "thaw".equals(type)){
			sql.append(",fe.id as feId");
		}else if("history".equals(type)){
			sql.append(",h.id as hId");
		}else{
			sql.append(",fa.id as id");
		}
		
		sql.append(" from fc_online_ledger fc, fc_subject fs,fc_acc fa");
		
		sql.append(" left join (select SUM(t.freeze_money) as freezeMoney ,t.blnctr_acc from Fc_Freeze t where t.freeze_sign = 'T' group by t.blnctr_acc) fm");
		sql.append(" on fm.blnctr_acc = fa.blnctr_acc");
		
		sql.append(" left join (select SUM(wf.freeze_money) as wfMoney,wf.blnctr_acc from  Fc_Freeze wf where wf.freeze_sign = 'C' group by wf.blnctr_acc) wfm");
		sql.append(" on wfm.blnctr_acc = fa.blnctr_acc");
		
		sql.append(" left join fc_acc_history h on   fa.blnctr_acc  = h.blnctr_acc");
		sql.append(" left join fc_freeze fe on fa.blnctr_acc = fe.blnctr_acc");
		sql.append(" where fc.account = fa.blnctr_acc  and fa.subject_id = fs.id ");
		sql.append(" and fa.corp_id =:corpId");
		if(EmptyUtils.isNotEmpty(startTime) && EmptyUtils.isNotEmpty(endTime)){
			if("acc".equals(type)){
				sql.append(" and fa.acc_state ='N' ");
				sql.append(" and fa.acc_open_time >= bt_to_date(:startTime,'yyyy-MM-dd')");
				sql.append(" and fa.acc_open_time <= bt_to_date(:endTime,'yyyy-MM-dd')");
			}
			
			if("history".equals(type)){
				sql.append(" and h.check_back_reason is null and h.checker_code is not null ");
				sql.append(" and h.check_time >= bt_to_date(:startTime,'yyyy-MM-dd')");
				sql.append(" and h.check_time <= bt_to_date(:endTime,'yyyy-MM-dd')");
			}
			if("writeoff".equals(type)){
				sql.append(" and  fa.acc_state='D' ");
				sql.append(" and  fa.check_time>= bt_to_date(:startTime,'yyyy-MM-dd')");
				sql.append(" and  fa.check_time<= bt_to_date(:endTime,'yyyy-MM-dd')");
			}
			if("freeze".equals(type)){
				sql.append(" and fe.freeze_sign = 'C' ");
				sql.append(" and fe.operate_time >= bt_to_date(:startTime,'yyyy-MM-dd')");
				sql.append(" and fe.operate_time <= bt_to_date(:endTime,'yyyy-MM-dd')");
			}
			if("thaw".equals(type)){
				sql.append(" and fe.freeze_sign = 'T'");
				sql.append(" and fe.operate_time >= bt_to_date(:startTime,'yyyy-MM-dd')");
				sql.append(" and fe.operate_time <= bt_to_date(:endTime,'yyyy-MM-dd')");
			}
			
		}
		
		if("freeze".equals(type) || "thaw".equals(type)){
			sql.append("order by fe.id ");
		}else if("history".equals(type)){
			sql.append("order by h.id ");
		}else{
			sql.append(" order by fa.id ");
		}
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpId", corpId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		
		return listBySQLAliasToBean(sql.toString(), param,InnerAccChangingVO.class);
	}
	
	/**改户**/
	public  List<InnerAccChangingVO> getHistoryByAccount(String account,String startTime,String endTime){
		StringBuilder sql = new StringBuilder();
		sql.append(" select h.blnctr_acc as blnctrAcc, h.acc_name as accName,h.net_id as netId,h.corp_id as corpId,h.cur_id as curId,");
		sql.append(" h.subject_id as subjectId,h.acc_type as accType,h.nature_code_id as natureCodeId,h.attribute_id as attributeId,");
		sql.append(" h.predraw_sign as predrawSign,h.overdraft_rate as overdraftRate,h.acc_start_date as accStartDate,h.check_time as checkTime ");
		sql.append(" from  fc_acc_history h  where h.check_back_reason is null and h.checker_code is not null  ");
		sql.append(" and h.blnctr_acc = :account ");
		sql.append(" and h.check_time >= bt_to_date(:startTime,'yyyy-MM-dd')");
		sql.append(" and h.check_time <= bt_to_date(:endTime,'yyyy-MM-dd')");
		sql.append(" order by h.check_time desc");
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("account", account);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
	
		return listBySQLAliasToBean(sql.toString(), param,InnerAccChangingVO.class);
	}
	
	/**冻结/解冻**/
	public  List<InnerAccChangingVO> getFreezeByAccount(String account,String startTime,String endTime,String freezeSign){	
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.blnctr_acc as blnctrAcc,t.freeze_sign as freezeSign,t.freeze_Type as freezeType,t.check_time as checkTime,");
		sql.append(" t.freeze_money as freezeMoney, t.begin_Date as beginDate,t.end_date as endDate,t.freeze_reason as freezeReason ");
		sql.append(" from  Fc_Freeze t");
		sql.append(" where t.blnctr_acc = :account");
		sql.append(" and t.operate_time >= to_date(:startTime,'yyyy-MM-dd')");
		sql.append(" and t.operate_time <= to_date(:endTime,'yyyy-MM-dd')");
		sql.append(" order by t.check_time desc");
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("account", account);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return listBySQLAliasToBean(sql.toString(), param,InnerAccChangingVO.class);
	}
}
