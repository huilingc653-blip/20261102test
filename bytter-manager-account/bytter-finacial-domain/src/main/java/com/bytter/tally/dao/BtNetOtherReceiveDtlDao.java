package com.bytter.tally.dao;

import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.BtNetChalkDTO;
import com.bytter.biz.finacial.api.dto.BtNetOtherDtlDTO;
import com.bytter.biz.finacial.paramconfig.vo.FcNisBusTypeRuleVO;
import com.bytter.biz.finacial.paramconfig.vo.FcOtherBusinessRuleDefineVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.SysCacheConstant;
import com.bytter.system.api.dto.BtBankAccCurDTO;
import com.google.common.collect.Maps;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijinquan
 * @description:
 * @date 2024-11-01 14:57
 */
@Repository
public class BtNetOtherReceiveDtlDao extends BaseDao {

    /**
     * 查询待记账数据
     *
     * @return
     */
    public List<BtNetOtherDtlDTO> queryTallyData(String num) {
        StringBuilder sql = new StringBuilder();

        sql.append("select vbn.id, vbn.corp_id as corpId, vbn.trans_time as transTime, vbn.cd_sign as cdSign ");
        sql.append("       ,vbn.money as amt,vbn.money as money,s.name as corpName,vbn.cur_id as curId,c.cur_name as curName ");
        sql.append("       ,b.id as bankAccId,b.bank_acc as bankAcc,vbn.acc_name as accName,vbn.bank_name as bankName ");
        sql.append("       ,vbn.opp_acc_no as oppAccNo,vbn.opp_bank_name as oppBankName,vbn.opp_acc_name as oppAccName ");
        sql.append("       ,vbn.postscript as postscript,vbn.purpose as remark,vbn.remark as uses,vbn.purpose as purpose "); //附言、摘要、用途
        sql.append("from bt_net_chalk vbn  ");
        sql.append(" left join bt_bank_acc b on vbn.bank_acc_id = b.id ");
        sql.append(" left join sys_corp s on s.id = vbn.corp_id ");
        sql.append(" left join bt_currency c on c.id = vbn.cur_id ");
        sql.append(" where 1 = 1  ");
        sql.append(" and  vbn.cd_sign = '0' and s.type = 0 ");
        sql.append(" and (vbn.estate = '0' or vbn.estate = '6' or vbn.estate = '8' or  vbn.estate is null) "); //未记账
        sql.append(" and vbn.bank_acc is not null ");
        sql.append(" and (vbn.TALLY_FAIL_MSG is null or vbn.TALLY_FAIL_MSG = '') "); //自动记账失败不进行下一次匹配
        //不符合归集（对方账户没有父子关系）
        sql.append("   and not exists (select bb.prent_acc_id from bt_bank_acc_cur bb, bt_bank_acc bc ");
        sql.append("                where bb.bank_acc_id = bc.id and bc.bank_acc = vbn.opp_Acc_No and b.id = bb.prent_acc_id) ");
        if(StringUtils.isNotBlank(num)){
            sql.append(" limit 0,").append(num); //一次记账多少条
        }
        return listBySQLAliasToBean(sql.toString(), null, BtNetOtherDtlDTO.class);
    }


    /**
     * <b>功能：</b>对外查询业务规则<br>
     * <b>作者：</b>lixi<br>
     * <b>日期：</b>2017-7-7<br>
     * @param map
     * @return
     */
    public List<FcOtherBusinessRuleDefineVo> getBusinessRuleExp(List<String> netIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.id as id, a.code as code, a.name as name, a.net_id as netId,");
        sql.append(" a.sys_race_code as sysRaceCode ,a.voucher_Type as voucherType, a.cd_sign as cdSign," );
        sql.append(" a.cur_code as curCode , a.valid_sign as validSign ");
        sql.append(" from fc_other_business_rule_define a ");
        sql.append("where 1=1 ");
        sql.append(" AND a.cd_sign = '0' ");
        sql.append(" AND a.valid_sign = :validSign");
        sql.append(" AND ").append(SqlUtil.getInSql(netIds, "a.net_id", true));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validSign", CommonConstant.VALID_SIGN_YES);

        return this.listBySQLAliasToBean(sql.toString(), params, FcOtherBusinessRuleDefineVo.class);
    }


    /**
     * 根据单位+科目获取内部账号
     */
    public List<BtBankAccCurDTO> getCountertAccByCorpId(String corpId, String subjectId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select fa.blnctr_Acc as counterAcc  ");
        sql.append("  from Fc_Acc fa ");
        sql.append("  left join fc_subject fs on fa.subject_id = fs.id ");
        sql.append("  where 1=1 ");
        sql.append("  and fa.corp_id =:corpId ");
        sql.append("  and fa.ACC_STATE = 'N' ");
        sql.append("  and fs.id =:subjectId ");
        params.put("corpId", corpId);
        params.put("subjectId", subjectId);
        return listBySQLAliasToBean(sql.toString(), params, BtBankAccCurDTO.class);
    }

    /**
     * 获取银行账号关联的内部账号
     */
    public List<BtBankAccCurDTO> getCountertAcc(String bankAcc) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sb = " select b.counter_acc as counterAcc , b.super_counter_acc as superCounterAcc" +
                " from bt_bank_acc a inner join bt_bank_acc_cur b on a.id = b.bank_acc_id  where a.bank_acc =:bankAcc ";
        params.put("bankAcc", bankAcc);
        return listBySQLAliasToBean(sb, params, BtBankAccCurDTO.class);
    }

    /**
     * 获取银行账号关联的内部账号
     */
    public List<BtBankAccCurDTO> getCountertAcc(String bankAcc, String subjectId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sb = " select b.counter_acc as counterAcc , b.super_counter_acc as superCounterAcc" +
                " from bt_bank_acc a " +
                "  inner join bt_bank_acc_cur b on a.id = b.bank_acc_id " +
                "  inner join fc_acc fa on fa.blnctr_acc = b.counter_acc " +
                " where a.bank_acc =:bankAcc and fa.SUBJECT_ID = :subjectId ";
        params.put("bankAcc", bankAcc);
        params.put("subjectId", subjectId);
        return listBySQLAliasToBean(sb, params, BtBankAccCurDTO.class);
    }

    /**
     * <br>
     * <b>功能：</b> 更新收款流水复核信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateTallyFailMsg(String id, String estate, String message) {
        Map<String, Object> param = new HashMap<>();
        String sb = " update bt_net_chalk set TALLY_FAIL_MSG = :message ";
        if(StringUtils.isNotBlank(estate)){
            sb += " , estate = :estate ";
            param.put("estate", estate);
        }
        sb += " where 1 = 1  ";
        sb += " and id = :id  ";

        param.put("message", message);
        param.put("id", id);
        return createSQLQuery(sb, param).executeUpdate();
    }

    /**
     * 查询国际化信息
     *
     * @param langCode 语言
     * @param langKey  语言主键
     * @return
     */
    @Cacheable(value = SysCacheConstant.MULTI_LANG_CACHE_NAME, key = "#langCode.concat(#langKey)")
    public String getByCodeAndKey(String langCode, String langKey) {
        String sql = "select LANG_CODE,LANG_CONTEXT from sys_multi_lang where LANG_CODE=:langCode and LANG_KEY=:langKey";
        Map<String, Object> map = Maps.newHashMap();
        map.put("langCode", langCode);
        map.put("langKey", langKey);
        SQLQuery sqlQuery = createSQLQuery(sql, map);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map result = (Map) sqlQuery.uniqueResult();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return (String) result.get("LANG_CONTEXT");
    }

    /**
     * 根据业务id获取所有业务规则
     * @param busId
     * @return
     */
    public List<FcNisBusTypeRuleVO> getByByBusId(String busId){
        String sql = " select * from FC_NIS_BUS_TYPE_RULE where BUS_ID = :busId order by sort ";
        Map<String,Object> params = new HashMap<>();
        params.put("busId",busId);
        return this.listBySQLAliasToBean(sql,params,FcNisBusTypeRuleVO.class);
    }


    /**
     * <br>
     * <b>功能：</b> 收款流水记账自动复核信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateAutoCheckSign(BtNetChalkDTO entity, List<String> idList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update bt_net_chalk set order_no = :orderNo, voucher_no = :voucherNo, ");
        sb.append(" book_id = :bookId, tally_date = :tallyDate, ");
        sb.append(" operator_code = :operatorCode, Bus_Global_Id = :busGlobalId, ");
        sb.append(" checker_code = :checkerCode, check_time = :checkTime,tally_fail_msg = null, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '0' or eState = '6' or eState = '8' or eState = '' or eState is null ) ");
        sb.append(" and id in (:id)  ");

        Map<String, Object> param = new HashMap<>();
        param.put("orderNo", entity.getOrderNo());
        param.put("voucherNo", entity.getVoucherNo());
        param.put("bookId", entity.getBookId());
        param.put("tallyDate", entity.getTallyDate());
        param.put("operatorCode", entity.getOperatorCode());
        param.put("busGlobalId", entity.getBusGlobalId());
        param.put("checkerCode", entity.getCheckerCode());
        param.put("checkTime", entity.getCheckTime());
        param.put("eState", NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }

    /**
     * <br>
     * <b>功能：</b> 提交收款流水记账信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/3/5 0005 <br>
     */
    public int updateTallySign(BtNetChalkDTO entity, List<String> idList, String eState) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update bt_net_chalk set order_no = :orderNo, voucher_no = :voucherNo, ");
        sb.append(" book_id = :bookId, tally_date = :tallyDate, ");
        sb.append(" operator_code = :operatorCode, Bus_Global_Id = :busGlobalId,tally_fail_msg = null, ");
        sb.append(" eState = :eState ");
        sb.append(" where 1 = 1  ");
        sb.append(" and (eState = '0' or eState = '6' or eState = '8'  or eState = '' or eState is null ) ");
        sb.append(" and id in (:id)  ");

        Map<String, Object> param = new HashMap<>();
        param.put("orderNo", entity.getOrderNo());
        param.put("voucherNo", entity.getVoucherNo());
        param.put("bookId", entity.getBookId());
        param.put("tallyDate", entity.getTallyDate());
        param.put("operatorCode", entity.getOperatorCode());
        param.put("busGlobalId", entity.getBusGlobalId());
        param.put("eState", eState);
        param.put("id", idList);
        return createSQLQuery(sb.toString(), param).executeUpdate();
    }


}