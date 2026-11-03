package com.bytter.ras.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bytter.ras.entity.RasAuditTask;
import com.bytter.ras.vo.RasAuditTaskVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;

/**
 * 
* @Title:  营收稽核任务维护数据库操作类
* @author: zhengg   
* @date:   2019-5-15 下午5:56:08   
 */
@Repository
public class RasAuditTaskDao extends BaseDao<RasAuditTask,String>{

	
	/**
	 * 
	* @Title: 稽核规则获取策略信息查询列表  
	* @param  input
	* @return DataTablesOutput    返回类型  
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" ID,CODE,NAME,PAY_TYPE as PAYTYPE,VALID_SIGN as VALIDSIGN,START_DATE as STARTDATE,LAST_DATE as LASTDATE,RUN_STATUS as RUNSTATUS,RUN_MSG as RUNMSG,RMK,CREATE_DATE as CREATEDATE,CREATE_BY as CREATEBY,CREATE_NAME as CREATENAME,UPDATE_DATE as UPDATEDATE,UPDATE_BY as UPDATEBY,UPDATE_NAME as UPDATENAME");
		sql.append(" from RAS_AUDIT_Task ");
		sql.append(" where 1=1 ");
		Map<String,Object> params=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(input.getFormdata().get("ins_payType"))){
			String payType=input.getFormdata().get("ins_payType").toString();
			String[] payTypes=payType.split(RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS);
			String or="";
			sql.append(" and (");
			for(int i=0;i<payTypes.length;i++){
				if(StringUtils.isNotEmpty(payTypes[i])){
					sql.append(or);
					sql.append(" BT_CONCAT3(:separatorchars,pay_Type,:separatorchars) like :payType").append(i).append(" ");
					params.put("payType"+i, "%"+RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS+payTypes[i]+RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS+"%");
					if(or.length()==0){
						or=" or ";
					}
				}
			}
			sql.append(" ) ");
			params.put("separatorchars", RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS);
			input.getFormdata().remove("ins_payType");
		}
		return getDynamicBySQL(input, sql.toString(),RasAuditTaskVO.class,"", false,params);
	}
	
	/**
	 * 通过缴款方式获取策略信息
	 * @param payType 缴款方式
	 * @param id 数据ID
	 * @return
	 */
	public List<RasAuditTask> getRasAuditTaskListByPayType(String payType,String id){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select id,pay_Type as payType from RAS_AUDIT_TASK where 1=1 ");
		params.put("separatorchars", RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS);
		String[] payTypes=payType.split(RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS);
		String or="";
		sql.append(" and (");
		for(int i=0;i<payTypes.length;i++){
			if(StringUtils.isNotEmpty(payTypes[i])){
				sql.append(or);
				sql.append(" BT_CONCAT3(:separatorchars,pay_Type,:separatorchars) like :payType").append(i).append(" ");
				params.put("payType"+i, "%"+RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS+payTypes[i]+RasConstant.RAS_AUDIT_TASK_SEPARATORCHARS+"%");
				if(or.length()==0){
					or=" or ";
				}
			}
		}
		sql.append(" ) ");
		
		if(StringUtils.isNotEmpty(id)){
			sql.append(" and id!=:id ");
			params.put("id", id);
		}
		return this.listBySQLAliasToBean(sql.toString(), params, RasAuditTask.class);
	}
	

	/**
	 * 通过任务都没获取策略信息排除传递的ID
	 * @param code 任务code
	 * @param id 需要排除的ID（判断修改的时候需要排除自身）
	 * @return 返回符合条件的数据
	 */
	@SuppressWarnings("unchecked")
	public List<RasAuditTask> codeCheck(String code,String id){
		Map<String,Object> params=new HashMap<>();
		String hql="from RasAuditTask where code=:code ";
		params.put("code", code);
		if(StringUtils.isNotEmpty(id)){
			hql+=" and id!=:id ";
			params.put("id", id);
		}
		return this.listByHQL(hql, params);
	}

	/**
	 * 
	* @Title: 根据条件修改定时器信息
	* @param  params
	* @return 修改笔数
	* @throws
	 */
	public int updateRasAuditTaskByParams(Map<String,Object> params){
		if(params!=null){
			StringBuilder hql=new StringBuilder("update RasAuditTask set validSign=validSign");
			if(params.containsKey("lastDate")){
				hql.append(" ,lastDate=:lastDate ");
			}
			if(params.containsKey("runStatus")){
				hql.append(" ,runStatus=:runStatus ");
			}
			if(params.containsKey("runMsg")){
				if(params.get("runMsg")==null){
					hql.append(" ,runMsg=null ");
				}else{
					hql.append(" ,runMsg=:runMsg ");
				}
			}
			if(params.containsKey("updateDate")){
				hql.append(" ,updateDate=:updateDate ");
			}
			if(params.containsKey("updateBy")){
				hql.append(" ,updateBy=:updateBy ");
			}
			if(params.containsKey("updateName")){
				hql.append(" ,updateName=:updateName ");
			}
			hql.append(" where 1=1 ");
			if(params.containsKey("wId")){
				hql.append(" and id=:wId ");
			}
			return this.batchExecute(hql.toString(), params);
		}
		return 0;
	}
}
