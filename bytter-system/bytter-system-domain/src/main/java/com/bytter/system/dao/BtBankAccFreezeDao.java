package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.entity.BtBankAccFreeze;
import com.bytter.system.vo.BtBankAccFreezeVO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class BtBankAccFreezeDao extends BaseDao<BtBankAccFreeze, String> {
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,corp.name as corpName, ");
        sql.append(" acccur.cur_id as curId, bc.cur_name curName, ");
        sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, ");
        sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName,acc.reg_date, ");
        sql.append(" acc.electric_bill as electricBill ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_currency bc on bc.id = acccur.cur_id ");
        sql.append(" where 1=1 and acc.status = " + BtBankAccConstant.STATUE_CHECK + " and acc.VALID_SIGN = " + BtBankAccConstant.VALIDSIGN_1 + " and acc.IS_ONLINE = " + BtBankAccConstant.ISONLINE_0 + " ");
		sql.append("  and not exists (select 1 from BT_BANK_ACC_FREEZE bf where bf.BANK_ACC = acc.BANK_ACC  and bf.status<>-2 group by bf.bank_acc_id,bf.cur_id having avg(bf.bal) - sum(bf.freeze_money) = 0 ) ");
        sql.append(" and acc.bank_acc not in (select bank_acc from bt_bank_acc_app where status <> " + BtBankAccConstant.STATUE_CANCELLATION + " and bank_acc is not null ) ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if(EmptyUtils.isEmptyList(dataTablesInput.getOrder())){
            dataTablesInput.setOrderString(new OrderString("acc.reg_date", "desc"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false);
    }
    /**
     * 根据id和币别得到银行账号
     *
     * @param id
     * @param curId
     * @return
     */
    public BtBankAccDTO getAccVOById(String id, String curId) {
        BtBankAccDTO vo = new BtBankAccDTO();
        StringBuilder sql = new StringBuilder();
        sql.append(" select app.id as id,app.bank_acc as bankAcc,app.acc_name as accName,app.corp_id as corpId,");
        sql.append(" acccur.cur_id as curId ,app.REG_DATE as regDate, app.RATES as rates, ");
        sql.append(" app.acc_type as accType,att.attribute_name as attributeName, ");
        sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,app.bank_name as bankName, ");
        sql.append(" app.prov as prov,app.city as city,app.electric_bill as electricBill,bis.name as  bifName,app.rates_float as ratesFloat, ");
        sql.append(" app.valid_sign as validSign,app.is_online as isOnline,app.scan_flag as scanFlag,app.rates_float_per as ratesFloatPer");
        sql.append(" from bt_bank_acc app  ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on app.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on app.nature_id = nat.id ");
        sql.append(" left join bt_bank_type banktype on app.bank_type_id = banktype.id ");
        sql.append(" left join bis_bif_init bis on app.BIF_CODE = bis.id ");
        sql.append(" left join bt_bank_acc_cur acccur on app.id = acccur.bank_acc_id ");
        sql.append(" where app.id = :id and acccur.cur_id = :curId ");
        Map<String, Object> param = new HashMap();
        param.put("id", id);
        param.put("curId", curId);
        vo  = this.getBySQLAliasToBean(sql.toString(), param, BtBankAccDTO.class);
        vo.setRatesFloatPer(vo.getRatesFloatPer());
		return vo;
    }

    /**
     * 获取冻结金额
     * @param bankAcc
     * @return
     */
    public BigDecimal getFreezeMoneyByBankAcc(String bankAcc, String curId, String ufFlag, Date balDate){
        String sql="select sum(freeze_Money) from bt_bank_acc_freeze where bank_Acc=:bankAcc and cur_Id=:curId";
        sql += " and uf_flag = :ufFlag and status =:status";
        if (balDate != null) {
            sql += " and bt_to_char(freeze_Date,'yyyy-MM-dd')<=:freezeDate";
        }
        HashMap<String,Object> params=new HashMap<>();
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        params.put("ufFlag", ufFlag);
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("freezeDate", DateUtils.formatDate(balDate));
        Object result = this.createSQLQuery(sql,params).uniqueResult();
        if (result != null) {
            return new BigDecimal(String.valueOf(result));
        } else {
            return  BigDecimal.ZERO;
        }
    }

    /**
     * 账户全部冻结解冻
     *
     * @param accId
     * @param curId
     * @return
     */
    public List<BtBankAccFreezeDTO> getBtBankAccFreeze(String accId, String curId) {
        List<BtBankAccFreezeDTO> list = new ArrayList<BtBankAccFreezeDTO>();
        StringBuilder hql = new StringBuilder();
        hql.append(" from BtBankAccFreeze bf where bf.bankAccId = :bankAccId and bf.curId = :curId and bf.status =  " + BtBankAccConstant.STATUE_CHECK + "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("bankAccId", accId);
        params.put("curId", curId);
        list = this.listByHQLAliasToBean(hql.toString(), params, BtBankAccFreezeDTO.class);;
        return list;
    }

    /**
     * 账户冻结暂存
     *
     * @param accId
     * @param curId
     * @return
     */
    public List<BtBankAccFreezeDTO> getBtBankAccFreezeTemp(String accId, String curId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BtBankAccFreeze bf where bf.bankAccId = :bankAccId and bf.curId = :curId and (bf.status = " + BtBankAccConstant.STATUE_TEMP + " or bf.status = " + BtBankAccConstant.STATUE_BACK + ") ");
        hql.append(" and bf.ufFlag = " + BtBankAccConstant.UFFLAG_0 + " ");
        hql.append(" order by bf.freezeNo desc ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("bankAccId", accId);
        params.put("curId", curId);
        this.getSession().clear();
        List<BtBankAccFreezeDTO> list = this.listByHQLAliasToBean(hql.toString(), params, BtBankAccFreezeDTO.class);
        return list;
    }

    /**
     * 得到账户冻结金额
     *
     * @param accId
     * @param curId
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BigDecimal getFreezeMoney(String accId, String curId) {
        BigDecimal freezeNum = BigDecimal.ZERO;
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(bf.freeze_money) as freezeMoney from BT_BANK_ACC_FREEZE bf ");
        sql.append(" where bf.BANK_ACC_ID = :bankAccId and bf.cur_id = :curId and bf.uf_flag = " + BtBankAccConstant.UFFLAG_0 + " and bf.status = " + BtBankAccConstant.STATUE_CHECK + " ");
        Map params = new HashMap();
        params.put("bankAccId", accId);
        params.put("curId", curId);
        List<BtBankAccFreezeVO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccFreezeVO.class);
        if (null != list && list.size() > 0 && null != list.get(0).getFreezeMoney()) {
            freezeNum = list.get(0).getFreezeMoney();
        }
        return freezeNum;
    }

    /**
     * 得到账户解冻金额
     *
     * @param accId
     * @param curId
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BigDecimal getUfFreezeMoney(String accId, String curId) {
        BigDecimal ufFreezeNum = BigDecimal.ZERO;
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(bf.freeze_money) as freezeMoney from BT_BANK_ACC_FREEZE bf ");
        sql.append(" where bf.BANK_ACC_ID = :bankAccId and bf.cur_id = :curId and bf.uf_flag = " + BtBankAccConstant.UFFLAG_0 + " and status = " + BtBankAccConstant.STATUE_CHECK + " ");
        Map params = new HashMap();
        params.put("bankAccId", accId);
        params.put("curId", curId);
        List<BtBankAccFreezeVO> list = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccFreezeVO.class);
        if (null != list && list.size() > 0 && null != list.get(0).getFreezeMoney()) {
            ufFreezeNum = list.get(0).getFreezeMoney();
        }
        return ufFreezeNum;
    }

    /**
     * 查询审核列表信息
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select freeze.id as id,freeze.FREEZE_NO as freezeNo,freeze.corp_id as corpId,freeze.cur_id as curId,cur.cur_name as curName, ");
        sql.append(" freeze.FREEZE_MONEY as freezeMoney,freeze.FREEZE_DATE as freezeDate, freeze.bank_acc_id AS bankAccId ");
        sql.append(" from bt_bank_acc_freeze freeze ");
        sql.append(" LEFT JOIN bt_currency cur ON freeze.cur_id = cur.id");
        sql.append(" where 1=1");
        sql.append(" and ").append(businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccFreezeDTO.class, "freeze", false);
    }

    /**
     * 账户冻结待审批
     *
     * @param accId
     * @param curId
     * @return
     */
    public List<BtBankAccFreezeDTO> getBtBankAccFreezeWait(String accId, String curId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BtBankAccFreeze bf where bf.bankAccId = :bankAccId and bf.curId = :curId and (bf.status = " + BtBankAccConstant.STATUE_WAIT_CHECK + ") ");
        hql.append(" and bf.ufFlag = " + BtBankAccConstant.UFFLAG_0 + " ");
        hql.append(" order by bf.freezeNo desc ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("bankAccId", accId);
        params.put("curId", curId);
        List<BtBankAccFreezeDTO> list = this.listByHQLAliasToBean(hql.toString(), params, BtBankAccFreezeDTO.class);
        return list;
    }

    /**
     * 根据单位统计银行账户(离线)
     * @param corpId
     * @return
     */
    public int getBankAccFreezeCountByCorpId(String corpId){
        String sql="select count(*) from bt_bank_acc_freeze where corp_id=:corpId";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql+=" and status in ('0','11','-1')";
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }

    /**
     * 获取冻结解冻列表信息
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select freeze.id,freeze.freeze_No as freezeNo,freeze.bank_Acc_Id as bankAccId,freeze.cur_Id as curId,freeze.corp_id as corpId,");
        sql.append(" freeze.uf_Flag as ufFlag,freeze.freeze_Money as freezeMoney,freeze.freeze_Date as freezeDate,freeze.freeze_Ression as freezeRession,freeze.status,freeze.f_Uuid as fUuid");
        sql.append(" from Bt_Bank_Acc_Freeze freeze where 1=1");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccFreezeDTO.class, "", false);
    }

    public BtBankAccFreeze getBtBankAccFreezeByFreezeNo(String freezeNo) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BtBankAccFreeze bf where bf.freezeNo = :freezeNo");
        hql.append(" and bf.ufFlag = " + BtBankAccConstant.UFFLAG_0 + " ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("freezeNo", freezeNo);
        BtBankAccFreeze freezeInfo = this.getUnique(hql.toString(), params);
        return freezeInfo;
    }

    public BigDecimal getUnFreezedMoneyById(String freezeId) {
        String sql="select sum(freeze_money) from bt_bank_acc_freeze  where F_UUID  = :freezeId and UF_FLAG = '1' and status = '95' ";
        HashMap<String,Object> params=new HashMap<>();
        params.put("freezeId",freezeId);
        return BigDecimal.valueOf(((Number)this.createSQLQuery(sql,params).uniqueResult()).doubleValue());
    }
}
