package com.bytter.biz.finacial.paramconfig.dao;

import com.bytter.biz.finacial.paramconfig.entity.FcBusinessRuleDefine;
import com.bytter.biz.finacial.paramconfig.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FC_BUSINESS_RULE_DEFINE
 * @date 2017-05-22 19:23:12
 */
@Repository
public class FcBusinessRuleDefineDao extends BaseDao<FcBusinessRuleDefine, String> {

    /**
     * 功能介绍 : 业务记账规则列表数据查询。<br>
     * <b>日期：</b>2017年5月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput getBusninessRuleVoList(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        /*sql.append("select fb.id as id,fs.id as sysRaceId,sc.name as netName,fb.sys_race_code as sysRaceCode,fs.sys_race_name as sysRacename, ")
                .append(" sb.subject_code as subjCode, sb.subject_name as subName, bc.cur_code as curCode, ")
                .append(" bc.CUR_name as curName,fb.valid_sign as validSign,bc.id as curId,sb.id as subjId ")
                .append(" from fc_business_rule_define fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
                .append(" left join fc_subject sb on fb.subj_code = sb.id ")
                .append(" left join sys_corp sc on fb.net_id = sc.id ")
                .append(" left join bt_currency bc on fb.cur_code = bc.id");*/
        sql.append("select fb.id as id,fs.id as sysRaceId,fb.net_id as netId,fb.sys_race_code as sysRaceCode,fs.sys_race_name as sysRacename, ")
                .append(" fb.valid_sign as validSign,fb.cur_code as curId,fb.subj_code as subjId ")
                .append(" from fc_business_rule_define fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ");
        return getDynamicBySQL(input, sql.toString(), FcBusinessRuleDefineListVo.class, "fs", true);
    }

    /**
     * 功能介绍 : 业务记账规则列表数据查询。<br>
     * <b>日期：</b>2017年5月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput getInitBusninessRuleVoList(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        /*sql.append("select fb.id as id,fb.sys_race_code as sysRaceCode,fs.sys_race_name as sysRacename, ")
                .append(" sb.subject_code as subjCode, sb.subject_name as subName,  ")
                .append(" fb.valid_sign as validSign ")
                .append(" from fc_business_rule_init fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
                .append(" left join fc_subject sb on fb.subj_code = sb.id ");
*/
        sql.append("select fb.id as id,fb.subj_code as subjCode, fb.sys_race_code as sysRaceCode,fs.sys_race_name as sysRacename, ")
                .append(" fb.valid_sign as validSign ")
                .append(" from fc_business_rule_init fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ");
        return getDynamicBySQL(input, sql.toString(), FcBusinessRuleDefineListVo.class, "fs", true);
    }

    /**
     * <b>功能：</b>对外查询业务规则<br>
     * <b>作者：</b>lixi<br>
     * <b>日期：</b>2017-7-7<br>
     *
     * @param map
     * @return
     */
    public List<FcBusinessRuleDefineVo> getBusinessRuleExp(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select  a.id as id , a.net_id as netId , a.sys_race_code as sysRaceCode ,a.voucher_Type as voucherType, ");
        sb.append(" a.subj_code as subjCode , a.cur_code as curCode , a.valid_sign as validSign ");
        sb.append(" from fc_business_rule_define a  where a.valid_sign = :validSign ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (EmptyUtils.isNotEmpty((String) map.get("sysRaceCode"))) {
            sb.append(" and a.sys_race_code =:sysRaceCode");
            params.put("sysRaceCode", map.get("sysRaceCode"));
        }
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        sb.append(" order by a.create_time desc ");
        return this.listBySQLAliasToBean(sb.toString(), params, FcBusinessRuleDefineVo.class);
    }

    /***
     * 查询凭证分录定义
     */
    public List<FcVoucherEntryDefineVo> getVoucher(String defineId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select  b.id as id , b.business_rule_define_id as businessruledefineid , b.belong_type as belongtype , b.balance_way as balanceway , b.subject_code as subjectcode , ");
        sb.append(" b.account as account ,b.cur_code as curcode , b.summary as summary , b.entry_type as entrytype , b.group_num as groupnum ,b.voucher_type as voucherType, b.blnctr_acc as blnctrAcc ");
        sb.append(" from fc_voucher_entry_define b where b.business_rule_define_id =:businessRuleDefineId ");
        Map<String, Object> params = new HashMap<String, Object>();
        sb.append(" order by b.group_num, b.balance_way desc ");
        params.put("businessRuleDefineId", defineId);
        return this.listBySQLAliasToBean(sb.toString(), params, FcVoucherEntryDefineVo.class);
    }


    /**
     * 获取有效的业务种类
     *
     * @return
     */
    public List<FcSysBusRaceVO> getFcSysBusRace() {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select fc.id as id ,fc.sys_race_code as sysRaceCode,fc.sys_race_name as sysRaceName ");
        sb.append(" from FC_SYS_BUS_RACE fc where fc.valid_sign = :validSign  order by fc.sys_race_code");
        params.put("validSign", "1");
        return this.listBySQLAliasToBean(sb.toString(), params, FcSysBusRaceVO.class);
    }

    /**
     * 功能介绍 : 根据id查看业务记账规则数据详情。<br>
     * <b>日期：</b>2017年9月21日<br>
     *
     * @version 1.0
     * @author yangl
     */
    public FcBusinessRuleDefineVo getBusninessRuleVo(String id) {
        StringBuilder sql = new StringBuilder();
        /*sql.append("select fb.id as id,sc.name as netId,fs.sys_race_name as sysRaceCode,sb.subject_name as subjCode, ")
                .append(" bc.CUR_name as curCode,fb.valid_sign as validSign,ft.voucher_name as voucherType, ")
                .append(" sb.id as subjectId,bc.id as curId,ft.id as voucherId,fs.id as raceId ")
                .append(" from fc_business_rule_define fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
                .append(" left join fc_subject sb on fb.subj_code = sb.id ")
                .append(" left join sys_corp sc on fb.net_id = sc.id ")
                .append(" left join bt_currency bc on fb.cur_code = bc.id")
                .append(" left join fc_voucher_type ft on fb.voucher_type=ft.id where fb.id = :id");*/
        //ft.voucher_name as voucherType
        sql.append("select fb.id as id,fb.net_id as netId,fs.sys_race_name as sysRaceCode, ")
                .append(" fb.cur_code as curId,fb.valid_sign as validSign, ")
                .append(" fb.subj_code as subjectId,fb.voucher_type as voucherId,fs.id as raceId ")
                .append(" from fc_business_rule_define fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
//				.append(" left join fc_voucher_type ft on fb.voucher_type=ft.id where fb.id = :id");
                .append(" where fb.id = :id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FcBusinessRuleDefineVo.class);
    }

    /**
     * 功能介绍 : 根据id查看业务记账规则数据详情。<br>
     * <b>日期：</b>2017年9月21日<br>
     *
     * @version 1.0
     * @author yangl
     */
    public FcBusinessRuleDefine getInitBusninessRuleVo(String id) {
        StringBuilder sql = new StringBuilder();
        /*sql.append("select fb.id as id,sc.name as netId,fs.sys_race_name as sysRaceCode,sb.subject_name as subjCode, ")
                .append(" bc.CUR_name as curCode,fb.valid_sign as validSign,ft.voucher_name as voucherType ")
                .append(" from fc_business_rule_init fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
                .append(" left join fc_subject sb on fb.subj_code = sb.id ")
                .append(" left join sys_corp sc on fb.net_id = sc.id ")
                .append(" left join bt_currency bc on fb.cur_code = bc.id")
                .append(" left join fc_voucher_type ft on fb.voucher_type=ft.id where fb.id = :id");*/
        sql.append("select fb.id as id,fb.net_id as netId,fs.sys_race_name as sysRaceCode,fb.subj_code as subjCode, ")
                .append(" fb.cur_code as curCode,fb.valid_sign as validSign,fb.voucher_type as voucherType ")
                .append(" from fc_business_rule_init fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
                .append(" where fb.id = :id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FcBusinessRuleDefine.class);
    }

    /**
     * 获取有效的会计科目
     *
     * @return
     * @author yangl
     * @date 2017-10-20
     * 由基础模块提供
     */
    @SuppressWarnings("unchecked")
    public List<FcSubjectVo> listValidSubject() {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from Fc_Subject where valid_Sign='1' ");
        Map<String, String> values = null;
        sql.append(" order by subject_Code");
        return listBySQLAliasToBean(sql.toString(), values, FcSubjectVo.class);
    }

    /**
     * 功能介绍 : 记账规则导入。<br>
     * <b>日期：</b>2017年11月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void importInitBusinessRule() {
        StringBuilder sql = new StringBuilder();
        sql.append(" insert into FC_BUSINESS_RULE_DEFINE (ID, NET_ID, SYS_RACE_CODE, ");
        sql.append(" SUBJ_CODE, CUR_CODE, VALID_SIGN, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, VOUCHER_TYPE) ");
        sql.append(" select fb.ID,fb.NET_ID,fb.SYS_RACE_CODE,fb.SUBJ_CODE,fb.CUR_CODE, ");
        sql.append(" fb.VALID_SIGN,fb.CREATE_TIME,fb.CREATE_BY,fb.UPDATE_TIME,fb.UPDATE_BY,fb.VOUCHER_TYPE ");
        sql.append(" from FC_BUSINESS_RULE_INIT fb ");
        sql.append(" where not exists (select 1 from FC_BUSINESS_RULE_DEFINE fd where fd.id = fb.id) ");
        sql.append(" and fb.sys_race_code not in ('5001','5002','5003')");
        createSQLQuery(sql.toString()).executeUpdate();
    }

    /***
     * 删除已初始化的记账规则
     */
    public void deleteInitBusinessRule() {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from FC_BUSINESS_RULE_DEFINE fb where fb.IS_INIT = '1' ");
        createSQLQuery(sql.toString()).executeUpdate();
    }

    /***
     * 获取初始化业务规则定义，所有租户共用
     *
     * @return 业务规则定义
     */
    public List<FcBusinessRuleDefine> listInitBusinessRule() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select fb.ID, fb.NET_ID, fb.SYS_RACE_CODE, fb.SUBJ_CODE, fb.CUR_CODE, fb.VALID_SIGN, fb.VOUCHER_TYPE ");
        sql.append(" from FC_BUSINESS_RULE_INIT fb ");
        sql.append(" WHERE fb.SYS_RACE_CODE not in ('5001', '5002', '5003')");
        return listBySQLAliasToBean(sql.toString(), null, FcBusinessRuleDefine.class);
    }

    /**
     * 功能介绍 : 查询是否存在相同的业务种类。<br>
     * <b>日期：</b>2017年11月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcBusinessRuleDefineVo> existBusinessRule(FcBusinessRuleDefineVo ruleDefineVo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select  a.id as id , a.net_id as netId , a.sys_race_code as sysRaceCode ,");
        sb.append(" a.voucher_Type as voucherType, a.subj_code as subjCode , ");
        sb.append(" a.cur_code as curCode , a.valid_sign as validSign ");
        sb.append(" from fc_business_rule_define a where a.valid_sign = :validSign ");
        if (!EmptyUtils.isEmpty(ruleDefineVo.getId())) {//ID
            sb.append(" and a.id <> :id ");
            params.put("id", ruleDefineVo.getId());
        }
        if (EmptyUtils.isEmpty(ruleDefineVo.getNetId())) {//网点
            sb.append(" and a.net_id is null ");
        } else {
            sb.append(" and a.net_id = :netId ");
            params.put("netId", ruleDefineVo.getNetId());
        }
        if (EmptyUtils.isEmpty(ruleDefineVo.getSysRaceCode())) {//系统业务种类
            sb.append(" and a.sys_race_code is null ");
        } else {
            sb.append(" and a.sys_race_code = :sysRaceCode ");
            params.put("sysRaceCode", ruleDefineVo.getSysRaceCode());
        }
        if (EmptyUtils.isEmpty(ruleDefineVo.getSubjCode())) {//科目
            sb.append(" and a.subj_code is null ");
        } else {
            sb.append(" and a.subj_code = :subjCode ");
            params.put("subjCode", ruleDefineVo.getSubjCode());
        }
        if (EmptyUtils.isEmpty(ruleDefineVo.getCurCode())) {//币别
            sb.append(" and a.cur_code is null ");
        } else {
            sb.append(" and a.cur_code = :curCode ");
            params.put("curCode", ruleDefineVo.getCurCode());
        }
        if (EmptyUtils.isEmpty(ruleDefineVo.getVoucherType())) {//凭证种类
            sb.append(" and a.voucher_type is null ");
        } else {
            sb.append(" and a.voucher_type = :voucherType ");
            params.put("voucherType", ruleDefineVo.getVoucherType());
        }
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        return this.listBySQLAliasToBean(sb.toString(), params, FcBusinessRuleDefineVo.class);
    }

}
