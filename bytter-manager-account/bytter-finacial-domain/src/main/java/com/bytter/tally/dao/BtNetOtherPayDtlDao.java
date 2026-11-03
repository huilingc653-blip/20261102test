package com.bytter.tally.dao;

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
 * @date 2024-11-05 13:50
 */
@Repository
public class BtNetOtherPayDtlDao extends BaseDao {

    /**
     * 查询待记账数据
     *
     * @return
     */
    public List<BtNetOtherDtlDTO> queryTallyData(String num) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, acc.corp_id as corpId, t.trans_time as transTime, t.cd_sign as cdSign ");
        sql.append("       ,t.amt as amt,t.amt as money,sc.name as corpName,t.cur_id as curId,c.cur_name as curName ");
        sql.append("       ,acc.id as bankAccId,acc.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName ");
        sql.append("       ,t.opp_acc_no as oppAccNo,t.opp_acc_bank as oppBankName,t.opp_acc_name as oppAccName ");
        sql.append("       ,t.uses as uses,t.remark as remark,t.postscript as postscript ");
        sql.append("       ,case when t.remark is null or t.remark = '' then '其他' else t.remark end as purpose ");
        sql.append("  from bis_acc_dtl t ");
        sql.append("  left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append("  left join sys_corp sc on sc.id = acc.corp_id ");
        sql.append("  left join bt_currency c on c.id = t.cur_id ");
        sql.append(" where 1 = 1 ");
        sql.append("  and t.cd_sign = '1' and sc.type = 0 ");
        sql.append("  and (t.estate is null or t.estate = '0' or t.estate = '6' or t.estate = '8') "); //未记账
        sql.append("  and t.bank_acc is not null ");
        sql.append("  and (t.TALLY_FAIL_MSG is null or t.TALLY_FAIL_MSG = '') "); //自动记账失败不进行下一次匹配
        //不符合归集（对方账户没有父子关系）
        sql.append("   and not exists (select bb.prent_acc_id from bt_bank_acc_cur bb, bt_bank_acc bc ");
        sql.append("                where bb.bank_acc_id = bc.id and bc.bank_acc = t.opp_Acc_No and acc.id = bb.prent_acc_id) ");
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
        sql.append(" AND a.cd_sign = '1' ");
        sql.append(" AND a.valid_sign = :validSign");
        sql.append(" AND ").append(SqlUtil.getInSql(netIds, "a.net_id", true));
        sql.append(" order by a.code asc ");
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
        String sb = " update bis_acc_dtl set TALLY_FAIL_MSG = :message, estate = :estate " +
                " where 1 = 1  " +
                " and id = :id  ";
        Map<String, Object> param = new HashMap<>();
        param.put("message", message);
        param.put("estate", estate);
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
     * 记录流水记账信息
     * @param id
     * @param bookId
     * @param busGlobalId
     * @param eState
     * @param voucherNo
     * @param orderNo
     * @return
     */
    public int updateTallySign(String id,String bookId,String busGlobalId,String eState,String voucherNo,String orderNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update BIS_ACC_DTL set book_id = :bookId, Bus_Global_Id = :busGlobalId, ");
        sql.append("       eState = :eState, tally_voucher_no = :voucherNo, order_no = :orderNo,tally_fail_msg = null ");
        sql.append(" where 1 = 1  ");
        sql.append(" and id = :id ");
        sql.append(" and (eState = '0' or eState = '6' or eState = '8'  or eState = '' or eState is null ) ");

        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("bookId", bookId);
        param.put("busGlobalId", busGlobalId);
        param.put("eState", eState);
        param.put("voucherNo", voucherNo);
        param.put("orderNo", orderNo);
        return createSQLQuery(sql.toString(), param).executeUpdate();
    }

}
