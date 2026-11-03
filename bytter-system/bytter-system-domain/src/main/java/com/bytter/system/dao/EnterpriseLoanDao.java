package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.SubjectConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.FcAccPlusDTO;
import com.bytter.system.inneraccount.entity.FcAcc;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EnterpriseLoanDao extends BaseDao<FcAcc, String> {

	
	/**
	 * <b>功能介绍：</b>查询记账账号<br>
	 * <b>日期：</b>2018年3月24日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param netId
	 * @param corpId
	 * @param curId
	 * @param accs
	 * @return
	 */
	public List<FcAccPlusDTO> getTallyAccTypeAccs(String netId,String corpId,String curId,List<String> accs){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.blnctr_acc,t.acc_name,cur.cur_name,f.sys_subject_code,f.subject_name,t.shortcut ");
		sql.append("from fc_acc t left join  fc_Subject f on t.subject_id=f.id ");
		sql.append("left join sys_corp net on t.net_id=net.id ");
		sql.append("left join sys_corp corp on t.corp_id=corp.id ");
		sql.append("left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
		sql.append("and t.acc_state in('N','P','S') and f.sys_Subject_Code = :sysSubjectCode ");
		if(EmptyUtils.isNotEmpty(netId)){
			sql.append("and t.net_id=:netId ");
			params.put("netId", netId);
		}
		if(EmptyUtils.isNotEmpty(corpId)){
			sql.append("and t.corp_Id=:corpId ");
			params.put("corpId", corpId);
		}
		if(EmptyUtils.isNotEmpty(curId)){
			sql.append("and t.cur_id=:curId ");
			params.put("curId", curId);
		}
		sql.append("and " + getSQLIn(accs, 900, "t.id"));
		sql.append(" order by t.blnctr_acc,t.acc_open_time ");
		params.put("sysSubjectCode", SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get());
		return listBySQLAliasToBean(sql.toString(), params, FcAccPlusDTO.class);
		
	}

	/**
	 * 查询记账账号 （单位级权限）
	 *
	 * @param netId
	 * @param corpId
	 * @param curId
	 * @return
	 */
	public List<FcAccPlusDTO> getTallyAccByCorp(String netId, String corpId, String curId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.blnctr_acc,t.acc_name,cur.cur_name,f.sys_subject_code,f.subject_name,t.shortcut ");
		sql.append("from fc_acc t left join  fc_Subject f on t.subject_id=f.id ");
		sql.append("left join sys_corp net on t.net_id=net.id ");
		sql.append("left join sys_corp corp on t.corp_id=corp.id ");
		sql.append("left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
		sql.append("and t.acc_state in('N','P','S') and f.sys_Subject_Code = :sysSubjectCode ");
		if (EmptyUtils.isNotEmpty(netId)) {
			sql.append("and t.net_id=:netId ");
			params.put("netId", netId);
		}
		if (EmptyUtils.isNotEmpty(corpId)) {
			sql.append("and t.corp_Id=:corpId ");
			params.put("corpId", corpId);
		}
		if (EmptyUtils.isNotEmpty(curId)) {
			sql.append("and t.cur_id=:curId ");
			params.put("curId", curId);
		}
		sql.append(" order by t.blnctr_acc,t.acc_open_time ");
		params.put("sysSubjectCode", SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get());
		return listBySQLAliasToBean(sql.toString(), params, FcAccPlusDTO.class);
	}
	
}
