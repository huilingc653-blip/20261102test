package com.bytter.check.dao;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.bdlink.vo.BisPaymentBlendingVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
/**
 * @author qijh
 */
@Repository
public class RefundBlendingDao extends BaseDao<BisAccDtlVO, String> {
//TODO 放到资金核对
    /**
     * 获取银行明细列表
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,cur.cur_name as curName,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark,case when t.BLEND_SIGN is null then '0' else t.BLEND_SIGN end as blendSign,t.bank_serial_no as bankSerialNo,");
        sql.append(" t.refund_Blend_Type as refundBlendType,t.refund_blend_time as refundBlendTime ");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bis_exc b on t.bank_serial_no = b.bank_serial_no ");
        sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sql.append(" where 1 = 1 and ((t.cd_sign = '0' and t.amt >0 )or(t.cd_sign = '1' and t.amt <0 ))  "  );
        sql.append(" and (b.voucher_from = '"+ BdlinkConstant.FROM_NIS_PAY+"' or b.voucher_from is null)  ");
        sql.append(" and (t.status  != "+ BtBankAccConstant.STATUE_DEL+" or t.status is null)  ");
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            if (!gedTransTime.contains("00:00:00")){
                gedTransTime = gedTransTime + " 00:00:00";
            }
            sql.append(" and t.trans_time >= '").append(gedTransTime).append("'");
            dataTablesInput.getFormdata().remove("ged_transTime");
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            if (!ledTransTime.contains("23:59:59")){
                ledTransTime = ledTransTime + " 23:59:59";
            }
            sql.append(" and t.trans_time <= '").append(ledTransTime).append("'");
            dataTablesInput.getFormdata().remove("led_transTime");
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        String eqsBlendSign = (String) dataTablesInput.getFormdata().get("eqs_blendSign");
        if(!EmptyUtils.isEmpty(eqsBlendSign)) {
            if (BtBankAccConstant.BLENDSIGN_0.equalsIgnoreCase(eqsBlendSign)) {
                sql.append(" and (t.BLEND_SIGN = '" + eqsBlendSign + "' or t.BLEND_SIGN is null) ");
            } else {
                sql.append(" and t.BLEND_SIGN = '" + eqsBlendSign + "' ");
            }
        }
        dataTablesInput.getFormdata().remove("eqs_blendSign");
        //根据查询条件及账户数据进行一次过滤
        List<String> corpList = null;
        String corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
        if (StringUtils.isNotBlank(corpId)) {
            String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
            corpList = Arrays.asList(corpIds);
        }
        if (EmptyUtils.isNotEmpty(corpList)) {
            String corpAuthSql = SqlUtil.getInSql(corpList, "acc.corp_id",true);
            sql.append(" and ").append(corpAuthSql);
        }
        logger.info("【退款勾兑】查询sql:" + sql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false);
    }
    /**
     * 保存付款单业务类型
     * @param id
     * @param busType
     */
    public int toSaveBillBusType(String id,String busType) {
        String sql = " update nis_billhead set bus_type = :busType where id = :id ";
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("busType",busType);
        return  createSQLQuery(sql,map).executeUpdate();
    }

    /**
     * 更新流水勾兑状态
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSign(String id,String blendSign,String refundBlendBy){
        StringBuilder sql = new StringBuilder();
        sql.append(" update bis_acc_dtl set blend_sign = :blendSign");
        if(EmptyUtils.isNotEmpty(refundBlendBy)){
            sql.append(" ,REFUND_BLEND_BY = :refundBlendBy");
            sql.append(" ,REFUND_BLEND_TIME = :refundBlendTime");
        }
        sql.append(" where id = :id");
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("blendSign",blendSign);
        if(EmptyUtils.isNotEmpty(refundBlendBy)){
            map.put("refundBlendBy",refundBlendBy);
            map.put("refundBlendTime", DateUtils.getDate());
        }
        return  createSQLQuery(sql.toString(),map).executeUpdate();
    }

    /**
     * 更新流水勾兑状态-定时任务使用
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSignForJob(String id,String blendSign,String refundBlendBy){
    	Session session = getSession();
    	session.getTransaction().begin();
        StringBuilder sql = new StringBuilder();
        sql.append(" update bis_acc_dtl set blend_sign = :blendSign");
        if(EmptyUtils.isNotEmpty(refundBlendBy)){
            sql.append(" ,REFUND_BLEND_BY = :refundBlendBy");
            sql.append(" ,REFUND_BLEND_TIME = :refundBlendTime");
        }
        sql.append(" where id = :id");
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("blendSign",blendSign);
        if(EmptyUtils.isNotEmpty(refundBlendBy)){
            map.put("refundBlendBy",refundBlendBy);
            map.put("refundBlendTime", DateUtils.getDate());
        }
        int result = createSQLQuery(sql.toString(),map).executeUpdate();
        session.flush();
        session.getTransaction().commit();
        return result;
    }
    /**
     * 更新流水勾兑状态
     * @param id
     * @param blendType
     * @return
     */
    public int toUpdateBlend(String id,String blendType){
        String sql = " update bis_acc_dtl set refund_blend_type = :blendType where id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("blendType",blendType);
        return  createSQLQuery(sql,map).executeUpdate();
    }
    /**
     * 更新流水银行流水号
     * @param id
     * @param bankSerialNo
     * @return
     */
    public int toUpdateBankSerialNo(String id,String bankSerialNo){
        String sql = " update bis_acc_dtl set bank_serial_no = :bankSerialNo where id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("bankSerialNo",bankSerialNo);
        return  createSQLQuery(sql,map).executeUpdate();
    }
    /**
     * 更新流水共享单号
     * @param id
     * @param sourceNum
     * @return
     */
    public int toUpdateSourceNum(String id,String sourceNum){
        String sql = " update bis_acc_dtl set SOURCE_NUM = :sourceNum where id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("sourceNum",sourceNum);
        return  createSQLQuery(sql,map).executeUpdate();
    }
    /**
     * 根据id 得到所有明细
     * @param ids
     * @return
     */
    public List<BisAccDtlVO> listDtlByids(String ids){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,cur.cur_name as curName,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark,t.BLEND_SIGN as blendSign,t.bank_serial_no as bankSerialNo");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sql.append(" where 1 = 1 and t.id in ("+ids+") ");
        return listBySQLAliasToBean(sql.toString(), null, BisAccDtlVO.class);
    }
    /**
     * 获取未勾兑的明细数量
     * @return
     */
    public Map<String,Object>  getDtlCountNoblendSign(){
        StringBuilder sql = new StringBuilder();
        String sqlnoIn = "select dtl.id from bis_acc_dtl dtl left join bt_bank_acc acc on dtl.bank_acc = acc.bank_acc\n " +
                " left join bt_bank_acc acc1 on dtl.opp_acc_no = acc1.bank_acc,bt_bank_acc_cur cur\n " +
                " where 1=1 and dtl.cd_sign = '1' and ((acc.id = cur.bank_acc_id and acc1.id = cur.prent_acc_id) or (acc.id=cur.prent_acc_id and acc1.id=cur.bank_acc_id)) " ;
        sql.append(" select count(*) as count from bis_acc_dtl t ");
        sql.append(" where 1 = 1 and (t.blend_sign = '0' or t.blend_sign is null) and t.cd_sign = '1' and t.id not in ("+sqlnoIn+")");
        List<Map<String, Object>> list = createSQLQuery(sql.toString()).setCacheable(false).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if(null != list && list.size() == 1){
            return list.get(0);
        }else{
            return null;
        }
    }

    /**
     * 退款勾对查询
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<BisPaymentBlendingVO> getDtlRules(Date beginDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from (select b.id as Id,e.rules as rules,e.rules_name as rulesName,b.BIS_DTL_NO as bisDtlNo, ");
        sql.append(" b.OPP_ACC_NO as oppAccNo,b.amt as amt,b.BANK_ACC as bankAcc,b.TRANS_TIME as transTime,b.ACC_NO as accNo, ");
        sql.append(" b.CUR_ID as curId,a.corp_id as corpId,b.cd_sign as cdSign,b.USES as uses,e.is_exist_id as isExistId, ");
        sql.append(" b.ACC_NAME as accName,b.OPP_ACC_NAME as oppAccName,b.bank_serial_no as bankSerialNo,e.trans_days as transDays, ");
        sql.append(" b.remark as abs,e.id  strategyId,b.AGAIN_SIGN as againSign,b.SOURCE_NUM as sourceNum ");
        sql.append(" from  bis_bank_execution_strategy e  left join BIS_BIF_INIT i on i.bank_type_id = e.bank_type_id");
        sql.append(" left join bis_acc_dtl b on i.id = b.BIF_CODE   left join bt_bank_acc a on b.bank_acc = a.bank_acc  ");
        sql.append(" where  e.type = '2' and e.is_enable = '1' ");
        sql.append(" and b.BLEND_SIGN='0' and b.auto_sign = '0' and ((b.cd_sign = '0' and b.amt >0 ) or (b.cd_sign = '1' and b.amt <0 ))) t ");
        sql.append(" where t.transTime >= :beginDate and t.transTime <= :endDate ");
        sql.append(" order by t.transTime desc limit 2000 ");
        Map<String,Object> params = new HashMap<>();
        params.put("beginDate",beginDate);
        params.put("endDate",endDate);
        List<BisPaymentBlendingVO> list = listBySQLAliasToBean(sql.toString(),params,BisPaymentBlendingVO.class);
        return list;
    }

    /**
     * 根据规则获取银行明细列表
     *
     * @param input
     * @return
     */
    public Boolean getBisAccDtlDataTable(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select COUNT(*) as count from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc a on t.bank_acc = a.bank_acc where 1= 1 ");
        sql.append(" and t.BLEND_SIGN='0' and ((t.cd_sign = '0' and t.amt >0 )or(t.cd_sign = '1' and t.amt <0 )) ");
        Map<String, Object> params = new HashMap<String, Object>();
        // 规则条件
        if (ObjectUtil.isNotEmpty(input.getFormdata().get("ruleSql"))){
            sql.append(input.getFormdata().get("ruleSql"));
            input.getFormdata().remove("ruleSql");
        }
        if (input.getFormdata().containsKey("oppAccName")) {
            sql.append(" and t.OPP_ACC_NAME=:oppAccName");
            params.put("oppAccName", input.getFormdata().get("oppAccName"));
        }
        if (input.getFormdata().containsKey("accName")) {
            sql.append(" and t.ACC_NAME=:accName");
            params.put("accName", input.getFormdata().get("accName"));
        }
        if (input.getFormdata().containsKey("uses")) {
            sql.append(" and t.USES=:uses");
            params.put("uses", input.getFormdata().get("uses"));

        }
        if (input.getFormdata().containsKey("corpId")) {
            sql.append(" and a.CORP_ID=:corpId");
            params.put("corpId", input.getFormdata().get("corpId"));
        }
        if (input.getFormdata().containsKey("cdSign")) {
            sql.append(" and t.CD_SIGN=:cdSign");
            params.put("cdSign", input.getFormdata().get("cdSign"));

        }
        if (input.getFormdata().containsKey("curId")) {
            sql.append(" and t.CUR_ID=:curId");
            params.put("curId", input.getFormdata().get("curId"));
        }
        if (input.getFormdata().containsKey("transTime")) {
            sql.append(" and BT_TO_CHAR(t.TRANS_TIME,'yyyy-mm-dd')=:transTime ");
            params.put("transTime", input.getFormdata().get("transTime"));
        }
        if (input.getFormdata().containsKey("oppAccNo")) {
            sql.append(" and t.OPP_ACC_NO=:oppAccNo");
            params.put("oppAccNo", input.getFormdata().get("oppAccNo"));
        }
        if (input.getFormdata().containsKey("bankAcc")) {
            sql.append(" and t.BANK_ACC=:bankAcc");
            params.put("bankAcc", input.getFormdata().get("bankAcc"));
        }
        if (input.getFormdata().containsKey("amt")) {
            sql.append(" and t.amt=:amt ");
            params.put("amt", input.getFormdata().get("amt"));
        }

        Map map = getBySQL(sql.toString(), params);
        if (!map.isEmpty()) {
            BigDecimal count = (BigDecimal) map.get("COUNT");
            if (count != null) {
                int i = count.compareTo(BigDecimal.ONE);
                if (i == 0) {
                    return true;
                }
            }else{
                BigInteger count1 = (BigInteger) map.get("count");
                if (count1 != null) {
                    int i = count1.compareTo(BigInteger.ONE);
                    if (i == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 退款勾对查询流水
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<BisAccDtlDTO> getDtl(Date beginDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as Id,b.BIS_DTL_NO as bisDtlNo, ");
        sql.append(" b.OPP_ACC_NO as oppAccNo,b.amt as amt,b.BANK_ACC as bankAcc,b.trans_time as transTime,b.ACC_NO as accNo, ");
        sql.append(" b.CUR_ID as curId,b.corp_id as corpId,b.cd_sign as cdSign,b.USES as uses, ");
        sql.append(" b.ACC_NAME as accName,b.OPP_ACC_NAME as oppAccName,b.bank_serial_no as bankSerialNo, ");
        sql.append(" b.remark as abs ");
        sql.append(" from bis_acc_dtl b ");
        sql.append(" where b.blend_sign='0' and b.auto_sign = '0' and ((b.cd_sign = '0' and b.amt >0 )or(b.cd_sign = '1' and b.amt <0 )) ");
        sql.append(" and b.trans_time >= :beginDate and b.trans_time <= :endDate ");
        sql.append(" order by b.trans_time desc ");
        Map<String,Object> params = new HashMap<>();
        params.put("beginDate",beginDate);
        params.put("endDate",endDate);
        List<BisAccDtlDTO> list = listBySQLAliasToBean(sql.toString(),params,BisAccDtlDTO.class);
        return list;
    }

}