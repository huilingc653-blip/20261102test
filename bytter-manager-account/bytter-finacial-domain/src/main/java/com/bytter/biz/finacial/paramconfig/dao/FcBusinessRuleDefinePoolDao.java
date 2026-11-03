package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.FcBusinessRuleDefinePool;
import com.bytter.biz.finacial.paramconfig.entity.FcSysBusRacePool;
import com.bytter.biz.finacial.paramconfig.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_BUSINESS_RULE_DEFINE_POOL
* @author onlineGenerator
* @date 2017-05-22 19:23:12 
* @version 1.0
*
*/
@Repository
public class FcBusinessRuleDefinePoolDao extends BaseDao<FcBusinessRuleDefinePool,String>{
	
	/**
	 * 功能介绍 : 资金池业务记账规则列表数据查询。<br>-------
	 */
	public DataTablesOutput getBusninessRuleVoList(DataTablesInput input) {
		StringBuilder sql = new StringBuilder();
		// sb.subject_code as subjCode, sb.subject_name  as subName;
		//sc.pool_name as netName;
		// bc.cur_code as curCode,bc.cur_name as curName
		/*sql.append("select fb.id as id,sc.pool_name as netName,fb.sys_race_code as sysRaceCode,fs.sys_race_name as sysRacename,")
			.append(" sb.id as subjId,sb.subject_code as subjCode, sb.subject_name  as subName,bc.id as curId,bc.cur_code as curCode, ")
			.append(" bc.cur_name as curName,fb.valid_sign as validSign ")
			.append(" from fc_business_rule_define_pool fb ")
			.append(" inner join fc_sys_bus_race_pool fs on fb.sys_race_code = fs.sys_race_code ")
			.append(" left join fc_subject_pool sb on fb.subject_id = sb.id ")
			.append(" left join fc_corp_pool sc on fb.pool_id = sc.id ")
			.append(" left join bt_currency bc on fb.cur_id = bc.id");*/
		sql.append("select distinct (fb.id) as id,fb.pool_id as poolId,fb.sys_race_code as sysRaceCode,fs.sys_race_name as sysRacename,")
				.append(" fb.subject_id as subjId, fb.cur_id as curId,")
				.append(" fb.valid_sign as validSign ")
				.append(" from fc_business_rule_define_pool fb ")
				.append(" inner join fc_sys_bus_race_pool fs on fb.sys_race_code = fs.sys_race_code ")
				.append(" group by fb.id,fb.pool_id,fb.sys_race_code,fs.sys_race_name, fb.subject_id, fb.cur_id, fb.valid_sign");
		
		return getDynamicBySQL(input, sql.toString(), FcBusinessRuleDefinePoolListVo.class, "fs", true);
	}
	
	/**
	 * 功能介绍 : 初始化业务记账规则列表数据查询。<br>
	 */
	public DataTablesOutput getInitBusninessRuleVoList(DataTablesInput input) {
		StringBuilder sql = new StringBuilder();
		/*sql.append("select fb.id as id,fb.sys_race_code as sysRaceCode,fs.sys_race_name as sysRacename, ")
				.append(" sb.subject_code as subjCode, sb.subject_name as subName,  ")
				.append(" fb.valid_sign as validSign ")
				.append(" from fc_business_rule_init fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
				.append(" left join fc_subject sb on fb.subj_code = sb.id ")
				.append(" where fb.sys_race_code in ('5001','5002','5003') ");*/
		sql.append("select fb.id as id,fb.sys_race_code as sysRaceCode,fs.sys_race_name as sysRacename, ")
				.append(" fb.subj_code as subjId,")
				.append(" fb.valid_sign as validSign ")
				.append(" from fc_business_rule_init fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
				.append(" where fb.sys_race_code in ('5001','5002','5003') ");
		return getDynamicBySQL(input, sql.toString(), FcBusinessRuleDefinePoolListVo.class, "fs", true);
	}
	
	/**
     * <b>功能：</b>对外查询业务规则<br>
     */
    public List<FcBusinessRuleDefinePoolVo> getBusinessRuleExp(String poolId, String curId, String sysRaceCode) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select  a.id as id , a.pool_id as poolId , a.sys_race_code as sysRaceCode ,a.voucher_Type as voucherType, ");
    	sb.append(" a.subject_id as subjectId , a.cur_id as curId , a.valid_sign as validSign");
    	sb.append(" from fc_business_rule_define_pool a  where a.valid_sign = :validSign ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (EmptyUtils.isNotEmpty(poolId)) {
            sb.append(" and a.pool_id = :poolId");
            params.put("poolId", poolId);
        }
        if (EmptyUtils.isNotEmpty(curId)) {
            sb.append(" and a.cur_id = :curId");
            params.put("curId", curId);
        }
        if (EmptyUtils.isNotEmpty(sysRaceCode)) {
            sb.append(" and a.sys_race_code = :sysRaceCode");
            params.put("sysRaceCode", sysRaceCode);
        }
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        sb.append(" order by a.create_time desc ");
        return this.listBySQLAliasToBean(sb.toString(), params, FcBusinessRuleDefinePoolVo.class);
    }
    
    /***
     * 查询凭证分录定义
     */
    public List<FcVoucherEntryDefineVo> getVoucher(String defineId) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select  b.id as id , b.business_rule_define_id as businessruledefineid , b.belong_type as belongtype , b.balance_way as balanceway , b.subject_code as subjectcode , ");
    	sb.append(" b.account as account ,b.cur_id as curcode , b.summary as summary , b.entry_type as entrytype , b.group_num as groupnum ,b.voucher_type as voucherType ");
    	sb.append(" from fc_voucher_entry_define_pool b where b.business_rule_define_id =:businessRuleDefineId ");
        Map<String, Object> params = new HashMap<String, Object>();
        sb.append(" order by b.group_num, b.balance_way desc ");
        params.put("businessRuleDefineId", defineId);
        return this.listBySQLAliasToBean(sb.toString(), params, FcVoucherEntryDefineVo.class);
    }
    
    
    /**
     * 获取有效的业务种类-------
     * @return
     */
    public List<FcSysBusRacePool> getFcSysBusRacePool() {
    	Map<String, Object> params = new HashMap<String, Object>();
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select fc.sys_race_code as sysRaceCode,fc.sys_race_name as sysRaceName ");
    	sb.append(" from FC_SYS_BUS_RACE_POOL fc where fc.valid_sign = :validSign  order by fc.sys_race_code");
        params.put("validSign", "1");
        return this.listBySQLAliasToBean(sb.toString(),params,FcSysBusRacePool.class);
    }
    
    /**
	 * 功能介绍 : 根据id查看业务记账规则数据详情。<br>
	 */
	public FcBusinessRuleDefinePoolVo getBusninessRulePoolVo(String id) {
		StringBuilder sql = new StringBuilder();
		/*sql.append("select fb.id as id,sc.pool_name as poolId,fs.sys_race_name as sysRaceCode,sb.id as subjectId, ")
				.append(" bc.id as curId,fb.valid_sign as validSign,ft.id as voucherType ")
				.append(" from fc_business_rule_define_pool fb inner join fc_sys_bus_race_pool fs on fb.sys_race_code = fs.sys_race_code ")
				.append(" left join fc_subject_pool sb on fb.subject_id = sb.id ")
				.append(" left join fc_corp_pool sc on fb.pool_id = sc.id ")
				.append(" left join bt_currency bc on fb.cur_id = bc.id")
				.append(" left join fc_voucher_type ft on fb.voucher_type=ft.id where fb.id = :id");*/
		sql.append("select fb.id as id,fb.pool_id as poolId,fs.sys_race_name as sysRaceCode,fb.subject_id as subjectId, ")
				.append(" fb.cur_id as curId,fb.valid_sign as validSign,fb.voucher_type as voucherType ")
				.append(" from fc_business_rule_define_pool fb inner join fc_sys_bus_race_pool fs on fb.sys_race_code = fs.sys_race_code ")
//				.append(" left join fc_voucher_type ft on fb.voucher_type=ft.id where fb.id = :id");
				.append(" where fb.id = :id");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, FcBusinessRuleDefinePoolVo.class);
	}
	
	/**
	 * 功能介绍 : 根据id查看业务记账规则数据详情。<br>
	 */
	public FcBusinessRuleDefineVo getInitBusninessRuleVo(String id) {
		StringBuilder sql = new StringBuilder();
		//sb.subject_name as subjCode; sc.pool_name as netId ;bc.CUR_name as curCode
		/*sql.append("select fb.id as id,sc.pool_name as netId,fs.sys_race_name as sysRaceCode,sb.subject_name as subjCode, ")
				.append(" bc.CUR_name as curCode,fb.valid_sign as validSign,ft.voucher_name as voucherType ")
				.append(" from fc_business_rule_init fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
				.append(" left join fc_subject sb on fb.subj_code = sb.id ")
				.append(" left join fc_corp_pool sc on fb.net_id = sc.id ")
				.append(" left join bt_currency bc on fb.cur_code = bc.id")
				.append(" left join fc_voucher_type ft on fb.voucher_type=ft.id where fb.id = :id");*/
		//ft.voucher_name as voucherType
		sql.append("select fb.id as id,fb.net_id as netId,fs.sys_race_name as sysRaceCode,fb.subj_code as subjectId, ")
				.append(" fb.cur_code as curId,fb.valid_sign as validSign,fb.voucher_type as voucherId ")
				.append(" from fc_business_rule_init fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
				.append(" where fb.id = :id");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, FcBusinessRuleDefineVo.class);
	}
	
	/**
	 * 获取有效的会计科目
	 *
	 * 改为由基础模块提供
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectPoolVO> listValidSubject(){
		/*StringBuilder sql = new StringBuilder();
		sql.append("select * from Fc_Subject_Pool where valid_Sign='1' ");
		sql.append(" order by subject_Code");
		return listBySQLAliasToBean(sql.toString(),null, FcSubjectPoolVO.class);*/
		return null;
	}
    
	/**
	 * 功能介绍 : 记账规则导入。<br>-----
	 */
	public void importInitBusinessRule() {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into fc_business_rule_define_pool (id, pool_id,subject_id, cur_id,valid_sign,");
		sql.append(" voucher_type,create_time, create_by,update_time,update_by, sys_race_code) ");
		sql.append(" select fb.id,fb.net_id,fb.subj_code,fb.cur_code,fb.valid_sign,fb.voucher_type,");
		sql.append(" fb.create_time,fb.create_by,fb.update_time,fb.update_by, fb.sys_race_code ");
		sql.append(" from fc_business_rule_init fb ");
		sql.append(" where not exists (select 1 from fc_business_rule_define_pool fd ");
		sql.append(" where fd.id = fb.id) and fb.sys_race_code in ('5001','5002','5003') ");
		createSQLQuery(sql.toString()).executeUpdate();
	}
	
	public void deleteBusinessRule(){
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from fc_business_rule_define_pool ");
		createSQLQuery(sql.toString()).executeUpdate();
	}
	
	/**
	 * 功能介绍 : 查询是否存在相同的业务种类。<br>
	 * <b>日期：</b>2017年11月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
    public List<FcBusinessRuleDefinePoolVo> existBusinessRule(FcBusinessRuleDefinePoolVo ruleDefinePoolVo) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select  a.id as id , a.pool_id as poolId , a.sys_race_code as sysRaceCode ,");
    	sb.append(" a.voucher_Type as voucherType, a.subject_id as subjectId , ");
    	sb.append(" a.cur_id as curId , a.valid_sign as validSign ");
    	sb.append(" from fc_business_rule_define_pool a where a.valid_sign = :validSign ");
    	if (!EmptyUtils.isEmpty(ruleDefinePoolVo.getId())) {//ID
    		sb.append(" and a.id <> :id ");
    		params.put("id", ruleDefinePoolVo.getId());
    	}
    	if (EmptyUtils.isEmpty(ruleDefinePoolVo.getPoolId())) {//网点
			sb.append(" and a.pool_id is null ");
		} else {
			sb.append(" and a.pool_id = :poolId ");
			params.put("poolId", ruleDefinePoolVo.getPoolId());
		}
    	if (EmptyUtils.isEmpty(ruleDefinePoolVo.getSysRaceCode())) {//系统业务种类
    		sb.append(" and a.sys_race_code is null ");
    	} else {
    		sb.append(" and a.sys_race_code = :sysRaceCode ");
    		params.put("sysRaceCode", ruleDefinePoolVo.getSysRaceCode());
    	}
    	if (EmptyUtils.isEmpty(ruleDefinePoolVo.getSubjectId())) {//科目
    		sb.append(" and a.subject_id is null ");
    	} else {
    		sb.append(" and a.subject_id = :subjectId ");
    		params.put("subjectId", ruleDefinePoolVo.getSubjectId());
    	}
    	if (EmptyUtils.isEmpty(ruleDefinePoolVo.getCurId())) {//币别
    		sb.append(" and a.cur_id is null ");
    	} else {
    		sb.append(" and a.cur_id = :curId ");
    		params.put("curId", ruleDefinePoolVo.getCurId());
    	}
    	if (EmptyUtils.isEmpty(ruleDefinePoolVo.getVoucherType())) {//凭证种类
    		sb.append(" and a.voucher_type is null ");
    	} else {
    		sb.append(" and a.voucher_type = :voucherType ");
    		params.put("voucherType", ruleDefinePoolVo.getVoucherType());
    	}
    	params.put("validSign", CommonConstant.VALID_SIGN_YES);
        return this.listBySQLAliasToBean(sb.toString(), params, FcBusinessRuleDefinePoolVo.class);
    }
    
    public FcBusinessRuleDefinePoolVo queryById(String id){
    	String sql = "select * from FC_BUSINESS_RULE_DEFINE_POOL where id = :id";
    	Map<String,String> param = new HashMap<>();
    	param.put("id",id);
    	return getBySQLAliasToBean(sql,param,FcBusinessRuleDefinePoolVo.class);
	}
	
}
