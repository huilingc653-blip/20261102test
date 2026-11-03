package com.bytter.biz.finacial.paramconfig.dao;

import com.bytter.biz.finacial.paramconfig.entity.FcOtherBusinessRuleDefine;
import com.bytter.biz.finacial.paramconfig.vo.FcOtherBusinessRuleDefineListVo;
import com.bytter.biz.finacial.paramconfig.vo.FcOtherBusinessRuleDefineVo;
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
 * @author lijinquan
 * @description:
 * @date 2024-10-28 14:27
 */
@Repository
public class FcOtherBusinessRuleDefineDao extends BaseDao<FcOtherBusinessRuleDefine,String> {


    /**
     * 功能介绍 : 业务记账规则列表数据查询。<br>
     * <b>日期：</b>2017年5月26日<br>
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput getBusninessRuleVoList(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fb.id as id,fb.code as code,fb.name as name,fb.net_id as netId, ")
           .append(" fb.cd_sign as cdSign,fb.valid_sign as validSign,fb.cur_code as curId ")
           .append(" from fc_other_business_rule_define fb ");
        return getDynamicBySQL(input, sql.toString(), FcOtherBusinessRuleDefineListVo.class, null, true);
    }


    /**
     * 功能介绍 : 查询是否存在相同的业务种类。<br>
     * <b>日期：</b>2017年11月23日<br>
     * @version 1.0
     * @author liyh
     */
    public List<FcOtherBusinessRuleDefineVo> existBusinessRule(FcOtherBusinessRuleDefineVo ruleDefineVo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select  a.id as id ,a.code as code ,a.name as name , a.net_id as netId , ");
        sb.append(" a.voucher_Type as voucherType, a.cd_sign as cdSign , ");
        sb.append(" a.cur_code as curCode , a.valid_sign as validSign ");
        sb.append(" from fc_other_business_rule_define a where a.valid_sign = :validSign ");
        if (!EmptyUtils.isEmpty(ruleDefineVo.getId())) {//ID
            sb.append(" and a.id <> :id ");
            params.put("id", ruleDefineVo.getId());
        }
        if (EmptyUtils.isEmpty(ruleDefineVo.getCode())) {//规则编码
            sb.append(" and a.code is null ");
        } else {
            sb.append(" and a.code = :code ");
            params.put("code", ruleDefineVo.getCode());
        }
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        return this.listBySQLAliasToBean(sb.toString(), params, FcOtherBusinessRuleDefineVo.class);
    }

    /**
     * 功能介绍 : 根据id查看业务记账规则数据详情。<br>
     * <b>日期：</b>2017年9月21日<br>
     * @version 1.0
     * @author yangl
     */
    public FcOtherBusinessRuleDefineVo getBusninessRuleVo(String id) {
        StringBuilder sql = new StringBuilder();

        sql.append("select fb.id as id,fb.net_id as netId,fb.code as code,fb.name as name,")
           .append(" fb.cd_sign as cdSign,fb.cur_code as curId,fb.valid_sign as validSign, ")
           .append(" fb.voucher_type as voucherId, fs.sys_race_name as sysRaceCode ")
           .append(" from fc_other_business_rule_define fb inner join fc_sys_bus_race fs on fb.sys_race_code = fs.sys_race_code ")
           .append(" where fb.id = :id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FcOtherBusinessRuleDefineVo.class);
    }

    public String getMaxCodeBy(String prefix) {
        String sql = "select max(code) as maxCode from FC_OTHER_BUSINESS_RULE_DEFINE where code like '" + prefix + "%'";
        List<String> list = this.createSQLQuery(sql.toString()).list();
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
