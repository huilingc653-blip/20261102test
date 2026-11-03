package com.bytter.credential.domain.international.dao;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.credential.api.dto.GuaContractInternationalDTO;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import com.bytter.credential.domain.international.vo.LcReceiveLetterReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class ReceiveLetterDao  extends BaseDao<LcEnrol,String> {
    /**
     * 收到信用证登记列表
     * @param input
     * @param corpAuth 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput queryAll(DataTablesInput input, List<String> corpAuth){
        StringBuilder sql = new StringBuilder();
        sql.append(" select lc.id as id, lc.lc_no as lcNo,lc.lc_inter as lcInter,lc.credit_no AS creditNo,");
        sql.append(" lc.contract_no AS contractNo,lc.issuing_date AS issuingDate,lc.expiry_date AS expiryDate,");
        sql.append(" lc.credit_money AS creditMoney,lc.issuing_cur_id AS issuingCurId, lc.beneficiary AS beneficiary,");
        //TODO 补充名称信息
        sql.append(" lc.status AS status,lc.corp_id as corpId ,lc.cur_name as curName, lc.corp_name as corpName ,lc.exchange_rate ratExchangeRate,");
        sql.append(" lc.OPENING_BANK_NAME openingBankName ");
        sql.append(" from lc_enrol lc ");
        sql.append(" where lc_type = 1  and status in(0,-1)  ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "lc.corp_id"));
        //sql.append(" Order By issuing_date desc ");
        return getDynamicBySQL(input,sql.toString(),LcEnrolVO.class,"lc",false);
    }
    /**
     * 通过lcno获取信用证信息
     * @param lcNo 信用证单号
     * @return LcEnrol
     */
    public LcEnrol getInfoByLcNo(String lcNo){
        StringBuilder sql = new StringBuilder();
        sql.append("select * from lc_enrol where lc_no = :lcNo");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("lcNo",lcNo);
        return getBySQLAliasToBean(sql.toString(),map,LcEnrol.class);
    }
    
    /**
     *	功能介绍 : 判断信用证号是否重复<br>	
     *	@return 重复：true    否则false
     */
    public boolean checkCreditNoRepeat(LcEnrol lcEnrol){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select BT_NVL_NUM(count(lc.credit_no),0) as COU from LC_ENROL lc ");
        sql.append(" where lc.credit_no = :creditNo and  lc_type = 1 ");
        if(!EmptyUtils.isEmpty(lcEnrol.getId())){
            sql.append(" and lc.id <> :id ");
            params.put("id", lcEnrol.getId());
        }
        params.put("creditNo", lcEnrol.getCreditNo());
        Map rs = getBySQL(sql.toString(), params);
        if(rs == null){
            return false;
        }
        BigDecimal count = new BigDecimal(rs.get("COU").toString());
        if(count.compareTo(BigDecimal.ZERO) == 0){
            return false;
        }
        return true;
    }
    /**
     * 功能介绍 : 申请审批列表表查询<br>
     * @param dataTablesInput
     * @param ids 单位集合
     * @return
     */
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput,List<String> ids) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select lc.id as id,lc.corp_Id as corpId,lc.corp_Name as corpName,lc.lc_Inter as lcInter,lc.lc_Type as lcType,");
        sql.append(" lc.lc_No as lcNo,lc.credit_No as creditNo,lc.contract_No as contractNo,lc.issuing_Cur_Id as issuingCurId,lc.cur_Name as curName,");
        sql.append(" lc.exchange_Rate as exchangeRate,lc.issuing_Date as issuingDate,lc.expiry_Date as expiryDate,lc.credit_Money as creditMoney,");
        sql.append(" lc.native_Credit_Money as nativeCreditMoney,lc.applicant as applicant,lc.applicant_Corp_Name as applicantCorpName,lc.applicant_Acc_Id as applicantAccId,");
        sql.append(" lc.applicant_Acc as applicantAcc,lc.issuing_Bank as issuingBank,lc.bank_Name as bankName,lc.addr as addr,lc.post_Num as postNum,");
        sql.append(" lc.beneficiary as beneficiary,lc.beneficiary_Acc_Id as beneficiaryAccId,lc.beneficiary_Acc as beneficiaryAcc,lc.beneficiary_Bank as beneficiaryBank,");
        sql.append(" lc.addr_Ben as addrBen,lc.post_Num_Ben as postNumBen,lc.time_Limit as timeLimit,lc.delay_Dates as delayDates,lc.pay_Charge_Acc as payChargeAcc,");
        sql.append(" lc.pay_Charge_Acc_Id as payChargeAccId,lc.charge_Cur_Id as chargeCurId,lc.charge_Money as chargeMoney,lc.acceptance_Money as acceptanceMoney,");
        sql.append(" lc.acceptance_Fee as acceptanceFee,lc.other_Fee as otherFee,lc.discount_Rate as discountRate,lc.discount_Interest as discountInterest,");
        sql.append(" lc.trade_Deficit as tradeDeficit,lc.comp_Cost as compCost,lc.reback_Money as rebackMoney,lc.open_Money as openMoney,");
        sql.append(" lc.goods_Descript as goodsDescript,lc.expiry_Place as expiryPlace,lc.purpose as purpose,lc.rmk as rmk,lc.status as status,");
        sql.append(" lc.re_Credit_No as reCreditNo,lc.re_Bank as reBank,lc.need_Licence as needLicence,lc.licence_Code as licenceCode,");
        sql.append(" lc.is_Hang_Credit as isHangCredit,lc.ratmodel as ratmodel,lc.ration_Code as rationCode,lc.item_Code as itemCode,");
        sql.append(" lc.bond_Percent as bondPercent,lc.bond_Money as bondMoney,lc.bond_Contract as bondContract,lc.is_Hang_Bond as isHangBond,");
        sql.append(" lc.bond_Id as bondId,lc.bond_Bank_Acc as bondBankAcc,lc.item_Money as itemMoney,lc.is_Re_Credit as isReCredit,");
        sql.append(" lc.re_Corp_Code as reCorpCode,lc.is_Spill_Over as isSpillOver,lc.spill_Over_Rate as spillOverRate,lc.is_Native_Cur as isNativeCur,");
        sql.append(" lc.bond_Cur_Id as bondCurId,lc.bill_Name as billName,lc.is_Open_Rat as isOpenRat,lc.is_Hang_Guarantee as isHangGuarantee,");
        sql.append(" lc.guarantee_Rmk as guaranteeRmk,lc.bond_Rate as bondRate,lc.bond_Money_Contract as bondMoneyContract,lc.opening_Bank as openingBank,");
        sql.append(" lc.is_In_Country as isInCountry,lc.opening_Bank_Name as openingBankName,lc.convert_Cur_Id as convertCurId,lc.attachment_Id as attachmentId,");
        sql.append(" lc.create_By as createBy,lc.create_Date as createDate,lc.update_By as updateBy,lc.update_Date as updateDate,lc.tenant_Id as tenantId,");
        sql.append(" lc.username as username");
        // FROM + 关联表预留位
        sql.append(" from lc_enrol lc ");
        sql.append(" where 1 = 1 and lc.lc_type = 1 and ");
        sql.append(getSQLIn(ids, 900, "lc.id"));
        return getDynamicBySQL(dataTablesInput,sql.toString(),LcEnrolVO.class,"lc",false);
       
    }
    /**
    *收证查询报表
    *@Param input
    *@Param corpIds
    *@return
    */
    public DataTablesOutput queryReportDataTable(DataTablesInput input,List<String> corpIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select lc.corp_id, ");
        sql.append("       lc.corp_name, ");
        sql.append("       lc.lc_inter, ");
        sql.append("       lc.id, ");
        sql.append("       lc.credit_No creditNo, ");
        sql.append("       lc.contract_no, ");
        sql.append("       lc.create_by, ");
        sql.append("       lc.opening_bank_name, ");
        sql.append("       lc.credit_money, ");
        sql.append("       lc.issuing_cur_id, ");
        //TODO 补充币种名称
        sql.append("       lc.cur_name issuingCurName, ");
        sql.append("       lc.create_date, ");
        sql.append("       lc.applicant createName, ");
        sql.append("       lc.EXPIRY_DATE, ");
        sql.append("       bt_nvl_num(lba.sum_bill_money, 0) sum_bill_money, ");
        sql.append("       bt_nvl_num(li.sum_incoming_money, 0) sum_incoming_money, ");
        sql.append("       lc.status ");
        sql.append("  from LC_ENROL lc ");
        sql.append("  left join (select lb.lc_id lc_id, sum(lb.bill_money) sum_bill_money ");
        sql.append("               from lc_bill_arrival lb ");
        sql.append("               left join LC_MORTGAGE lm ");
        sql.append("                 on lb.id = lm.arrival_id ");
        sql.append("                and lm.status not in (-2, -3) ");
        sql.append("              where lb.arrival_type = 1 ");
        sql.append("                and (lb.status = 4 or lb.status = 5 or lb.status = 99 or ");
        sql.append("                    (lb.status = 11 and lm.status is not null)) ");
        sql.append("              group by lb.lc_id) lba ");
        sql.append("    on lc.id = lba.lc_id ");
        sql.append("  left join (select lci.lc_id lc_id, ");
        sql.append("                    sum(lci.incoming_money) sum_incoming_money ");
        sql.append("               from lc_incoming lci ");
        sql.append("              group by lci.lc_id) li ");
        sql.append("    on lc.id = li.lc_id ");
        sql.append("  where 1 = 1");
        sql.append(" and lc.lc_type = '1' and lc.status !=-2 ");//剔除已删除的状态
        sql.append(" and ("+getSQLIn(corpIds,900,"lc.corp_id")+") ");

        if (input.getOrder() != null && !input.getOrder().isEmpty()
                && input.getOrder().get(0) != null) {
            Integer orderColumnIndex = input.getOrder().get(0).getColumn();
            List<Column> columns = input.getColumns();
            if (orderColumnIndex != null && columns != null && orderColumnIndex >= 0
                    && orderColumnIndex < columns.size()) {
                String orderColumnName = columns.get(orderColumnIndex).getName();
                String orderDir = input.getOrder().get(0).getDir();
                if ("sumIncomingMoney".equals(orderColumnName)) {
                    input.setOrderString(new OrderString(
                            "(bt_nvl_num(li.sum_incoming_money, 0))", orderDir));
                }
            }
        }

        return getDynamicBySQL(input, sql.toString(), LcReceiveLetterReportVO.class, "lc", false, null);
    }
    /**
     * 获取挂接授信的信用证ID集合
     * @return
     */
    public List<String> getIdByRat(){
        return createSQLQuery("select id from lc_enrol where is_hang_guarantee = '1'").list();
    }
    public List<GuaContractInternationalDTO> getGuaForInternational() {
        StringBuilder sql = new StringBuilder();
        sql.append("select lc.applicant, ");
        sql.append(" lc.applicant_corp_name as applicantName, ");
        sql.append(" lc.beneficiary as beneficiary, ");
        sql.append(" lc.issuing_bank as issuingBank, ");
        sql.append(" lc.bank_name as bankName, ");
        sql.append(" lc.credit_no as creditNo, ");
        sql.append(" lc.lc_inter as lcInter, ");
        sql.append(" lc.credit_money as creditMoney, ");
        sql.append(" lc.contract_no as contractNo, ");
        sql.append(" lc.issuing_date as issuingDate, ");
        sql.append(" lc.expiry_date as expiryDate, lc.id as id");
        sql.append(" from lc_enrol lc ");
        sql.append("where lc.lc_type = '0' and lc.is_hang_guarantee= 1 ");// 0开证1收证
        return this.listBySQLAliasToBean(sql.toString(), null, GuaContractInternationalDTO.class);
    }
}
