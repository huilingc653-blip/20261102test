package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCorpBankAccDtl;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fusb
 * @version 1.0
 * @Title: FexCorpBankAccDtlDao
 * @Description: 主体单位默认银行账户明细Dao
 * @date 2020-02-17 15:20:33
 */
@Repository
public class FexCorpBankAccDtlDao extends BaseDao<FexCorpBankAccDtl,String> {
    /**
     * 查询sql
     * @return
     */
    public String getSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("select  b.id as id,");
        sql.append("        b.main_id as mainId,");
        sql.append("        b.effect_date as effectDate,");
        sql.append("        b.bank_type as bankType,");
        sql.append("        b.pro_id as proId,");
        sql.append("        b.acc_id as accId,");
        sql.append("        b.bank_acc as bankAcc,");
        sql.append("        b.acc_name as accName,");
        sql.append("        b.cur_id as curId,");
        sql.append("        b.voucher_type_id as voucherTypeId,");
        sql.append("        b.pay_model as payModel,");
        sql.append("        b.pay_value as payValue,");
        sql.append("        c.voucher_name as voucherName ");
        return sql.toString();
    }

    /**
     * 通过主表id查询对应的主体单位默认银行账户明细VO
     * @param mainId
     * @return
     */
    public List<FexCorpBankAccDtlVO> getFecFexCorpBankAccDtlVOById(String mainId){
        StringBuilder sql = new StringBuilder();
        sql.append(this.getSql());
        sql.append("    from fex_corp_bankacc_dtl b left join fc_voucher_type c on c.id=b.voucher_type_id");
        sql.append("    where b.main_id=:mainId ");
        Map<String,String> params = new HashMap<String,String>();
        params.put("mainId",mainId);
        return this.listBySQLAliasToBean(sql.toString(),params,FexCorpBankAccDtlVO.class);
    }

    /**
    *@Description 根据主表id和银行类型查询主体单位默认银行账户明细
    *@Param mainId
    *@Param bankType
    *@return
    */
    public List<FexCorpBankAccDtlVO> getFecFexCorpBankAccDtlList(String mainId,String bankType){
        StringBuilder sql = new StringBuilder();
        Map<String,String> params = new HashMap<String,String>();
        sql.append(this.getSql());
        sql.append("    from fex_corp_bankacc_dtl b left join fc_voucher_type c on c.id=b.voucher_type_id");
        sql.append("    where b.main_id=:mainId ");
        if (StringUtils.isNotEmpty(bankType)) {
            sql.append("  and b.bank_type=:bankType");
            params.put("bankType",bankType);
        }
        params.put("mainId",mainId);
        return  this.listBySQLAliasToBean(sql.toString(),params,FexCorpBankAccDtlVO.class);
    }

    /**
     * 通过主表id查询对应的主体单位默认银行账户明细实体
     * @param mainId
     * @return
     */
    public List<FexCorpBankAccDtl> getFexCorpBankAccDtlById(String mainId){
        StringBuilder hql = new StringBuilder();
        hql.append(" from FexCorpBankAccDtl dtl where dtl.mainId=:mainId");
        Map<String,String> params = new HashMap<String,String>();
        params.put("mainId",mainId);
        return this.listByHQLAliasToBean(hql.toString(),params,FexCorpBankAccDtl.class);
    }

    /**
     * 通过主表id删除对应的主体单位默认银行账户明细
     * @param mainId
     */
    public void deleteFexCorpBankAccDtl(String mainId){
        StringBuilder sql = new StringBuilder();
        //mysql 删除时不能对表起别名
        sql.append(" delete from fex_corp_bankacc_dtl  where main_id=:mainId");
        Map<String,String> params = new HashMap<String,String>();
        params.put("mainId",mainId);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }

    /**
     * 根据查询条件获取账户信息
     * @param searchVo
     * @return
     */
    public List<FexCorpBankAccDtlVO> getBankAccToTran(FexCorpBankAccVO searchVo){
        StringBuilder sql = new StringBuilder();
        sql.append(this.getSql());
        sql.append(" , ");
        sql.append(" f.bus_type  busType");
        sql.append("    from fex_corp_bankacc_dtl b left join fc_voucher_type c on c.id=b.voucher_type_id");
        sql.append("  left join fex_corp_bankacc f on f.id=b.main_Id");
        sql.append("    where 1=1 ");
        Map<String,Object> params = new HashMap<String,Object>();
        if(StringUtils.isNotBlank(searchVo.getBusType())){
            sql.append(" and f.bus_type=:busType ");
            params.put("busType",searchVo.getBusType());
        }
        if(StringUtils.isNotBlank(searchVo.getCorpId())){
            if(StringUtils.isNotBlank(searchVo.getCotpParentId())){
                sql.append(" and (f.corp_id=:corpId or f.corp_id is null or f.corp_id=:cotpParentId)");
                params.put("cotpParentId",searchVo.getCotpParentId());
            }else {
                sql.append(" and (f.corp_id=:corpId or f.corp_id is null)");
            }
            params.put("corpId",searchVo.getCorpId());
        }
        if(StringUtils.isNotBlank(searchVo.getTranType())){
            sql.append(" and (f.tran_type=:tranType or f.tran_type is null)");
            params.put("tranType",searchVo.getTranType());
        }
        if(StringUtils.isNotBlank(searchVo.getCurId())){
            sql.append(" and (f.cur_Id=:curId or f.cur_Id is null)");
            //银行账户控制币种一致
            if("0".equals(searchVo.getBusType())){
                sql.append(" and b.cur_Id=:curId");
            }
            params.put("curId",searchVo.getCurId());
        }
        if(StringUtils.isNotBlank(searchVo.getCashFlowType())) {
            sql.append(" and (f.cash_Flow_Type=:cashFlowType or f.cash_Flow_Type is null)");
            params.put("cashFlowType", searchVo.getCashFlowType());
        }
        if(StringUtils.isNotBlank(searchVo.getProId())){
            sql.append(" and (f.pro_Id=:proId or f.pro_Id is null)");
            params.put("proId",searchVo.getProId());
        }
        if(StringUtils.isNotBlank(searchVo.getCdSign())){
            sql.append(" and (f.cd_sign=:cdSign or f.cd_sign is null or f.cd_sign='')");
            params.put("cdSign",searchVo.getCdSign());
        }
        if(StringUtils.isNotBlank(searchVo.getOppCorpId())){
            if(StringUtils.isNotBlank(searchVo.getCotpParentId())){
                sql.append(" and (f.opp_Corp_Id=:oppCorpId or f.opp_Corp_Id is null or f.opp_Corp_Id=:cotpParentId)");
                params.put("cotpParentId",searchVo.getCotpParentId());
            }else {
                sql.append(" and (f.opp_Corp_Id=:oppCorpId or f.opp_Corp_Id is null)");
            }
            params.put("oppCorpId",searchVo.getOppCorpId());
        }
        sql.append(" and f.status=:status");
        sql.append(" and (b.effect_date is null or b.effect_date<=:currentDate)");
        sql.append(" order by f.corp_id,f.tran_type,f.pro_id,f.opp_corp_id,f.cd_sign,f.cash_flow_type,f.cur_id asc,b.effect_date desc");
        params.put("status", CommonConstant.STATUE_CHECK.toString());
        params.put("currentDate", DateUtils.getDateYMD());
        return this.listBySQLAliasToBean(sql.toString(),params,FexCorpBankAccDtlVO.class);
    }

    /**
     * 根据查询条件获取账户信息
     *
     * @param searchVo
     * @return
     */
    public List<FexCorpBankAccDtlVO> getNewBankAccToTran(FexCorpBankAccVO searchVo) {
        StringBuilder sql = new StringBuilder();
        sql.append(this.getSql());
        sql.append(" , ");
        sql.append(" f.bus_type  busType");
        sql.append("    from fex_corp_bankacc_dtl b left join fc_voucher_type c on c.id=b.voucher_type_id");
        sql.append("  left join fex_corp_bankacc f on f.id=b.main_Id");
        sql.append(" where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(searchVo.getBusType())) {
            sql.append(" and f.bus_type=:busType ");
            params.put("busType", searchVo.getBusType());
        }
        //主体id：busType为0时主体单位id，busType为1时交易对手数据源id（客商id）
        if (StringUtils.isNotBlank(searchVo.getCorpId())) {
            sql.append(" and (f.corp_id=:corpId or f.corp_id is null)");
            params.put("corpId", searchVo.getCorpId());
        } else {
            sql.append(" and f.corp_id is null ");
        }
        //对方id：busType为0时交易对手数据源id（客商/单位/银行id），busType为1时主体单位id
        if (StringUtils.isNotBlank(searchVo.getOppCorpId())) {
            sql.append(" and (f.opp_corp_id=:oppCorpId or f.opp_corp_id is null)");
            params.put("oppCorpId", searchVo.getOppCorpId());
        } else {
            sql.append(" AND (f.opp_corp_id IS NULL OR f.opp_corp_id = '') ");
        }

        if (StringUtils.isNotBlank(searchVo.getTranType())) {
            sql.append(" and (f.tran_type=:tranType or f.tran_type is null OR f.tran_type = '')");
            params.put("tranType", searchVo.getTranType());
        }
        if (StringUtils.isNotBlank(searchVo.getCurId())) {
            sql.append(" and (f.cur_Id=:curId or f.cur_Id is null OR f.cur_Id = '')");
            //银行账户控制币种一致
            /*if ("0".equals(searchVo.getBusType())) {
                sql.append(" and b.cur_Id=:curId");
            }*/
            params.put("curId", searchVo.getCurId());
        }


        if (StringUtils.isNotBlank(searchVo.getCashFlowType())) {
            sql.append(" and (f.cash_Flow_Type=:cashFlowType or f.cash_Flow_Type is null OR f.cash_Flow_Type = '')");
            params.put("cashFlowType", searchVo.getCashFlowType());
        }
        if (StringUtils.isNotBlank(searchVo.getProId())) {
            sql.append(" and (f.pro_Id=:proId or f.pro_Id is null OR f.pro_Id = '')");
            params.put("proId", searchVo.getProId());
        }
        if (StringUtils.isNotBlank(searchVo.getCdSign())) {
            sql.append(" and (f.cd_sign=:cdSign or f.cd_sign is null or f.cd_sign='')");
            params.put("cdSign", searchVo.getCdSign());
        }
        sql.append(" and f.status=:status");
        sql.append(" and (b.effect_date is null  or b.effect_date<=:currentDate)");
        sql.append(" order by f.corp_id,f.tran_type,f.pro_id desc,f.opp_corp_id,f.cd_sign,f.cash_flow_type,f.cur_id asc,b.effect_date desc");
        params.put("status", CommonConstant.STATUE_CHECK.toString());
        params.put("currentDate", DateUtils.getDateYMD());
        return this.listBySQLAliasToBean(sql.toString(), params, FexCorpBankAccDtlVO.class);
    }

    public FexCorpBankAccDtlVO getBankAccInfo(String bankAcc) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> params = new HashMap<String, String>();
        sql.append(" select BIF_CODE as bifCode, ACC_TYPE as accType, BANK_TYPE_ID as bankTypeId, BANK_CODE as bankCode, LOCAL_CLEAR_NO as localClearNo, ");
        sql.append(" IS_DOMESTIC_BANK as isDomesticBank, PROV as prov ");
        sql.append(" from bt_bank_acc where bank_acc = :bankAcc ");
        params.put("bankAcc", bankAcc);
        return getBySQLAliasToBean(sql.toString(), params, FexCorpBankAccDtlVO.class);
    }
}
