package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexSettleFillRule;
import com.bytter.biz.foreign.vo.FexSettleFillRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: FexSettleFillRuleDao
 * @ProjectName T3(3.2)
 * @Description: 结算自动填充规则数据访问层
 * @date 2020/2/18 10:23
 */
@Repository
public class FexSettleFillRuleDao extends BaseDao<FexSettleFillRule,String> {

    /**
     * 结算自动填充规则公共查询语句
     * @param sql
     * @return
     */
    public StringBuilder querySqlCommon(StringBuilder sql){
        // 保存时 cotpId 实际存的是 oppId（银行/单位/客商主键），不是 FEX_COTP.id；
        // 单位名、币种名需 SQL 直接关联，列表页不依赖注解注入。
        sql.append("SELECT")
                .append(" f.id,f.corp_id AS corpId,f.cash_flow_method AS cashFlowMethod,f.tran_type AS tranType,")
                .append(" f.cur_id AS curId,f.voucher_type_id AS voucherTypeId,f.cd_sign AS cdSign,f.country_id AS countryId,")
                .append(" f.acc_id AS accId,f.pro_id AS proId,f.cotp_id AS cotpId,f.valid_sign AS validSign, cus.name_zh AS countryName,")
                .append(" f.cotp_id AS oppId,")
                .append(" CASE WHEN f.tran_type = '4' THEN ext.name_en ELSE NULL END AS cotpNameEn,")
                .append(" sc.name AS corpName,cur.cur_name AS curName,cur.english_code AS curEnCode,")
                .append(" pro.pro_name AS proName,")
                .append(" CASE")
                .append("   WHEN f.tran_type IN ('1','2') THEN bank.bank_name")
                .append("   WHEN f.tran_type = '3' THEN cotpCorp.name")
                .append("   WHEN f.tran_type = '4' THEN ext.name")
                .append("   ELSE COALESCE(bank.bank_name, cotpCorp.name, ext.name)")
                .append(" END AS cotpName,")
                .append(" acc.bank_acc AS bankCode,vou.voucher_name AS voucherName ")
                .append(" from FEX_SETTLE_FILL_RULE f ")
                .append(" left join SYS_CORP sc on f.corp_id = sc.id ")
                .append(" left join BT_CURRENCY cur on f.cur_id = cur.id ")
                .append(" left join FEX_PRODUCT pro on f.pro_id = pro.id ")
                .append(" left join BT_INPUT_BANK_INFO bank on f.cotp_id = bank.id ")
                .append(" left join SYS_CORP cotpCorp on f.cotp_id = cotpCorp.id ")
                .append(" left join SYS_EXTERNAL_CORP ext on f.cotp_id = ext.id ")
                .append(" left join SYS_COUNTRY_CUST cus on f.country_id=cus.id ")
                .append(" left join BT_BANK_ACC acc on f.acc_id=acc.id ")
                .append(" left join FC_VOUCHER_TYPE vou on f.voucher_type_id=vou.id ")
                .append(" where 1=1 ");
        return sql;
    }

    /**
     * 查询结算自动填充规则列表数据
     */
    public DataTablesOutput datatable(DataTablesInput input, List<String> corpIds){
        StringBuilder sql=new StringBuilder();
        querySqlCommon(sql);
        Map<String, Object> params = input.getFormdata();
        String tranType=(String)input.getFormdata().get("tranType");
        params.remove("tranType");
        if(!StringUtils.isBlank(tranType)){
            params.put("eqs_tranType",tranType);
        }

        String corpId=(String)input.getFormdata().get("eqs_corpId");
        if(StringUtils.isBlank(corpId)){
            params.remove("eqs_corpId");
        }

        String proId=(String)input.getFormdata().get("eqs_proId");
        if(StringUtils.isBlank(proId)){
            params.remove("eqs_proId");
        }

        String cotpName=(String)input.getFormdata().get("likes_cotpName");
        Map<String, Object> extraParams = new HashMap<>();
        // cotp 名称来自银行/单位/客商关联，不能再用 cotp.cotp_name 自动拼条件
        params.remove("likes_cotpName");
        if(!StringUtils.isBlank(cotpName)){
            sql.append(" and (bank.bank_name like :cotpNameLike or cotpCorp.name like :cotpNameLike or ext.name like :cotpNameLike) ");
            extraParams.put("cotpNameLike", "%" + cotpName.trim() + "%");
        }

        String countryId=(String)input.getFormdata().get("eqs_countryId");
        if(StringUtils.isBlank(countryId)){
            params.remove("eqs_countryId");
        }

        String curId=(String)input.getFormdata().get("eqs_curId");
        if(StringUtils.isBlank(curId)){
            params.remove("eqs_curId");
        }
        return getDynamicBySQL(input,sql.toString(), FexSettleFillRuleVO.class,"",false, extraParams);
    }

    /**
     * 根据id查询结算自动填充规则数据
     */
    public FexSettleFillRuleVO getFillRuleById(String id){
        StringBuilder sql=new StringBuilder();
        HashMap<String,String> map=new HashMap<>();
        querySqlCommon(sql);
        sql.append(" and f.id=:id ");
        map.put("id",id);
        return getBySQLAliasToBean(sql.toString(), map, FexSettleFillRuleVO.class);
    }
}
