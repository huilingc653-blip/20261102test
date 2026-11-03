package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbBillReceivebiz;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 票据签收信息
 * @date 2017-06-21 15:36:42
 */
@Repository
public class AbBillReceivebizDao extends BaseDao<AbBillReceivebiz, String> {
    @Autowired
    private IInputBankInfoService iInputBankInfoService;
    @Autowired
    private IBankTypeService iBankTypeService;
    /**
     * 展示票据签收信息页面列表查询信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillReceivebizInfo(DataTablesInput input, List<String> corpBankAuthList, List<String> corpMerchAuthList, List<String> corpOtherAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT p.bankendrsmtmk as bankendrsmtmk, ");
        sql.append("P.ID,P.BILL_TYPE AS BILLTYPE,P.MSGTYPE,P.ORIG_NO AS ORIGNO,p.REC_ACCT_TYPE_NAME as recBankTypeName, ");
        sql.append("P.BUSSDT,P.BILL_MONEY AS BILLMONEY,P.PAYER,P.ACCEPTOR,vv.type, ");
        sql.append("P.START_DATE AS STARTDATE,P.END_DATE AS ENDDATE,vv.id as vvid,p.REC_CORP_ID as recCorpId, p.sttlmmk as sttlmmk,");
        sql.append(" P.PAYERBANKNO payerbankno,P.ACCEPTORBANKNO acceptorbankno, ");
//        sql.append("P.REQ_NM AS REQNM,P.REC_NM AS RECNM,P.REC_ACCT_ID RECACCTID,p.REC_CORP_CODE corpCode,p.REC_CORP_NAME as corpName,P.STATUS,vv.BACK_ACID as lastid, ");
        sql.append("P.REQ_NM AS REQNM,P.REC_NM AS RECNM,P.REC_ACCT_ID RECACCTID,vv.CORP_NAME as corpName,vv.CORP_CODE as corpCode, ");
        sql.append("  P.STATUS,vv.BACK_ACID as lastid, ");
        //新一代票据 start
        sql.append(" P.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd, P.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd, P.sbbll_rng_strt_sn as sbbllRngStrtSn,");
        sql.append(" P.sbbll_rng_end_sn as sbbllRngEndSn,  P.bl_rmtr_unn_id_no as blRmtrUnnIdNo ,");
        //end
        //		--前手
        sql.append("case ");
        sql.append("when l.brief_id='6001' then l.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then l.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else vv.cn_accrecords end as cnAccrecords, ")
                .append("(select max(v.endorsement_count) from ab_voucherinfo v where p.orig_no=v.orig_no and p.sbbll_rng_strt_sn=v.sbbll_rng_strt_sn and p.sbbll_rng_end_sn=v.sbbll_rng_end_sn )")
                .append(" as endorsementCount,");
        sql.append(" vv.BUS_PERSONNEL as busPersonnel,");
//        sql.append( " vv.BUS_CORP_NAME as busCorpName,vv.APPLY_CORP_NAME as applyCorpName,vv.APPLY_CORP_CODE as applyCorpCode  ");
        sql.append(" P.req_acct_id as reqAcctId  ");

        //		--前手
        sql.append(" from AB_BILL_RECEIVEBIZ P ");
                //同意清偿业务，主表子票区间包含签收表子票区间
        sql.append(" left join AB_ACCRECORDS l on P.id=l.recid and l.status!=:status2 ");
        sql.append(" left join AB_VOUCHERINFO_VICE vv  on vv.id=l.vvid ");
//        sql.append(" left join (SELECT COUNT(*) AS ENDORSEMENTCOUNT, ORIG_NO AS ORIGNO, SBBLL_RNG_STRT_SN sbbllRngStrtSn,SBBLL_RNG_END_SN sbbllRngEndSn" +
//                " FROM AB_BACKBILLINFO BACK WHERE BACK.BUSTYPE = '010'");
//        sql.append(" GROUP BY BACK.ORIG_NO,BACK.SBBLL_RNG_STRT_SN,BACK.SBBLL_RNG_END_SN) back1 ON back1.origNo = p.orig_no and p.SBBLL_RNG_STRT_SN=back1.sbbllRngStrtSn and p.SBBLL_RNG_END_SN=back1.sbbllRngEndSn");
        sql.append(" left join ab_brief_status bs on bs.status=l.brief_id ");
        sql.append(" WHERE P.STATUS IN(:STATUS) ");//票据状态为18待签收和-2打回
        sql.append(" AND P.MSGTYPE IN (:MSGTYPE) ");//票据签收业务为：017提示保证002提示承兑003提示收票010背书申请012回购式贴现赎回018质押申请019质押解除020提示付款021逾期付款023追索同意清偿
        String corpMerchAuthSql = SqlUtil.getInSqlByParams(corpMerchAuthList, "p.REC_CORP_ID", "corpMerchAuth", params);
        String corpBankAuthSql = SqlUtil.getInSqlByParams(corpBankAuthList, "p.REC_CORP_ID", "corpBankAuth", params);
        String corpOtherAuthSql = SqlUtil.getInSqlByParams(corpOtherAuthList, "p.REC_CORP_ID", "corpOtherAuth", params);
        sql.append(" and (( P.BILL_TYPE=:billTypeMerch and ").append(corpMerchAuthSql)
                .append(") or (P.BILL_TYPE=:billTypeBank and ").append(corpBankAuthSql)
                .append(") or (P.BILL_TYPE not in (:billTypeOther) and ").append(corpOtherAuthSql).append(")) ");
        Map<String, Object> formdata = input.getFormdata();
        String bussdt = (String) formdata.get("led_bussdt");
        //手动拼接时间条件，时间戳类型
        if (StringUtils.isNotEmpty(bussdt)) {
            bussdt += " 23:59:59";
            formdata.put("led_bussdt", bussdt);
            input.setFormdata(formdata);
        }
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        params.put("VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("STATUS", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING, AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE});
        params.put("status2", AccebillConstant.AB_BILL_BUS_STATUS_2);
        params.put("billTypeMerch", Integer.parseInt(AccebillConstant.AB_BILL_TYPE_MERCH));
        params.put("billTypeBank", Integer.parseInt(AccebillConstant.AB_BILL_TYPE_BANK));
        params.put("billTypeOther", new Integer[]{Integer.parseInt(AccebillConstant.AB_BILL_TYPE_MERCH), Integer.parseInt(AccebillConstant.AB_BILL_TYPE_BANK)});
        //应付应收标识
        String type = (String) formdata.get("type");
        if (StringUtils.isNotEmpty(type)) {
            //应付
            if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE0.equals(type)) {
                params.put("MSGTYPE", new String[]{AccebillConstant.AB_MSGTYPE_ACCEPT,
                        AccebillConstant.AB_MSGTYPE_GUARGN,
                        AccebillConstant.AB_MSGTYPE_HINT_PAYER,
                        AccebillConstant.AB_MSGTYPE_HINT_PAYER_OVERDUE});
            }
            //应收
            else if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(type)) {
                params.put("MSGTYPE", new String[]{AccebillConstant.AB_MSGTYPE_RECBILL,
                        AccebillConstant.AB_MSGTYPE_RECITE,
                        AccebillConstant.AB_MSGTYPE_DISCOUNT_REDEM,
                        AccebillConstant.AB_MSGTYPE_PLEDGE,
                        AccebillConstant.AB_MSGTYPE_PLEDGE_RELIEVE,
                        AccebillConstant.AB_MSGTYPE_SEARCHFOR_PAYOFF,
                        AccebillConstant.BRIEF_STATUS_024});
            }
        } else {
            params.put("MSGTYPE", new String[]{AccebillConstant.AB_MSGTYPE_ACCEPT,
                    AccebillConstant.AB_MSGTYPE_RECBILL,
                    AccebillConstant.AB_MSGTYPE_RECITE,
                    AccebillConstant.AB_MSGTYPE_DISCOUNT_REDEM,
                    AccebillConstant.AB_MSGTYPE_GUARGN,
                    AccebillConstant.AB_MSGTYPE_PLEDGE,
                    AccebillConstant.AB_MSGTYPE_PLEDGE_RELIEVE,
                    AccebillConstant.AB_MSGTYPE_HINT_PAYER,
                    AccebillConstant.AB_MSGTYPE_HINT_PAYER_OVERDUE,
                    AccebillConstant.AB_MSGTYPE_SEARCHFOR_PAYOFF,
                    AccebillConstant.BRIEF_STATUS_024});
        }
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (p.REC_ACCT_ID is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.REC_ACCT_ID", "bankAccs", params)).append(")");
        }
        return this.getDynamicBySQL(input, sql.toString(), AbBillReceivebizVO.class, "", false, params);
    }
    /**
     * 获取商票提示承兑待签收数据且出票人和承兑人一致的待签收数据
     *
     * @return 待签收数据
     */
    public List<AbBillReceivebizVO> getAutoSignAcceptData() {
        StringBuilder sql = new StringBuilder("select distinct r.id ");
        sql.append("from ab_bill_receivebiz r ");
        sql.append("left join ab_voucherinfo v on r.orig_no=v.orig_no and r.SBBLL_RNG_STRT_SN=v.SBBLL_RNG_STRT_SN and r.SBBLL_RNG_END_SN=v.SBBLL_RNG_END_SN ");
        sql.append("where bt_nvl_num(r.bill_type,v.bill_type) =:billType ");
        sql.append("and r.msgtype=:msgtype ");
        sql.append("and r.status=:status ");
        sql.append("and (r.payer=r.acceptor or v.payer=v.acceptor) ");
        Map<String, Object> params = new HashMap<>();
        params.put("billType", Integer.parseInt(AccebillConstant.AB_BILL_TYPE_MERCH));
        params.put("msgtype", AccebillConstant.AB_MSGTYPE_ACCEPT);
        params.put("status", AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
        return this.listBySQLAliasToBean(sql.toString(), params, AbBillReceivebizVO.class);
    }
    /**
     * 获取商票提示承兑待签收数据且出票人和承兑人一致的待签收数据
     *
     * @return 待签收数据
     */
    public List<AbBillReceivebizVO> getAutoSignStrategyData(Map<String, Object> params) {
        if (params.isEmpty()) {
            return null;
        }
        StringBuilder sql = new StringBuilder("select distinct r.id,r.rec_corp_id as recCorpId,bt_nvl_char(r.payer,v.payer) as payer," +
                "r.req_Nm as reqNm,bt_nvl_char(r.acceptor,v.acceptor) as acceptor,bt_nvl_num(r.bill_type,v.bill_type) as billType,r.bankendrsmtmk,r.bill_money as billMoney,r.msgtype" +
                ",r.acceptorbankno acceptorbankno1,v.acceptorbankno acceptorbankno2,r.bill_type billType1,v.bill_type billType2, ");
        sql.append(" v.create_By as createBy ");
        sql.append("from ab_bill_receivebiz r ");
        sql.append("left join ab_voucherinfo v on r.orig_no=v.orig_no and r.SBBLL_RNG_STRT_SN=v.SBBLL_RNG_STRT_SN and r.SBBLL_RNG_END_SN=v.SBBLL_RNG_END_SN ");
        //sql.append("left join bt_input_bank_info bi on bt_nvl_char(r.acceptorbankno,v.acceptorbankno)=bi.sys_bank_code and bt_nvl_num(r.bill_type,v.bill_type)=:billTypeBank ");
        //sql.append("left join bt_bank_type bt on bt.bank_prefix=bi.bank_prefix and (bt.bank_name_prefix is null or bi.bank_name like bt_concat2(bt.bank_name_prefix,'%')) ");
        sql.append("where bt_nvl_num(r.bill_type,v.bill_type) in (:billType) ");
        sql.append("and r.msgtype in(:signType) ");
        sql.append("and r.status=:status ");
        String[] corpIds = (String[]) params.get("corpId");
        Object billTypeObj = params.get("billType");
        if (billTypeObj instanceof String[]) {
            String[] billTypeStrs = (String[]) billTypeObj;
            Integer[] billTypes = new Integer[billTypeStrs.length];
            for (int i = 0; i < billTypeStrs.length; i++) {
                billTypes[i] = Integer.parseInt(billTypeStrs[i]);
            }
            params.put("billType", billTypes);
        }
        sql.append("and ").append(SqlUtil.getInSqlByParams(corpIds, "r.rec_corp_id", params));
//		sql.append("and r.rec_corp_id in (:corpId) ");
        //params.put("billTypeBank", Integer.parseInt(AccebillConstant.AB_BILL_TYPE_BANK));
        params.put("status", AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
        List<AbBillReceivebizVO> billReceivebizVOList = this.listBySQLAliasToBean(sql.toString(), params, AbBillReceivebizVO.class);
        if(CollectionUtils.isEmpty(billReceivebizVOList)){
            return null;
        }
        List<AbBillReceivebizVO> rtList = new ArrayList<>();
        for(AbBillReceivebizVO vo : billReceivebizVOList){
            String acceptorbankno = (StringUtils.isEmpty(vo.getAcceptorbankno1()) && StringUtils.isEmpty(vo.getAcceptorbankno2())) ? null :
                    (StringUtils.isEmpty(vo.getAcceptorbankno1()) ? vo.getAcceptorbankno2() : vo.getAcceptorbankno1());
            Integer billType= (vo.getBillType1() == null && vo.getBillType2() == null) ? null :
                    (vo.getBillType1() == null ? vo.getBillType2() : vo.getBillType1());
            if(StringUtils.isEmpty(acceptorbankno) || billType == null){
                rtList.add(vo);
                continue;
            }
            if(Integer.parseInt(AccebillConstant.AB_BILL_TYPE_BANK) != billType){
                rtList.add(vo);
                continue;
            }
            InputBankInfoDTO bankInfo = iInputBankInfoService.getByBankCode(acceptorbankno);
            if(bankInfo == null || StringUtils.isEmpty(bankInfo.getBankPrefix())){
                rtList.add(vo);
                continue;
            }
            List<BankTypeDTO> bankTypeList =  iBankTypeService.listBySignOrPrefix(null, bankInfo.getBankPrefix());
            if(CollectionUtils.isEmpty(bankTypeList)){
                rtList.add(vo);
                continue;
            }
            for(BankTypeDTO bankType : bankTypeList){
                vo.setBankTypeId(bankType.getId());
                if(bankType.getBankNamePrefix() == null){
                    rtList.add(vo);
                    continue;
                }
                if(bankInfo.getBankName() != null && bankInfo.getBankName().contains(bankType.getBankNamePrefix())){
                    rtList.add(vo);
                }
            }
        }
        return rtList;
    }
    /**
     * 根据IDS查询票据签收表信息
     *
     * @param ids
     * @return
     */
    public List<AbBillReceivebiz> listAbBillReceivebizByIds(String[] ids, AbBillReceivebizVO billReceivebizVo) {
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        StringBuilder hql = new StringBuilder("from AbBillReceivebiz b where ").append(SqlUtil.getInSqlByParams("ids", "b.id", params));
        StringBuilder hqlWhere = new StringBuilder();
        if (billReceivebizVo.getStatus() == AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK) {
            hqlWhere.append(" and b.status in (:statusWhere)");
            Integer[] statusWhere = {
                    AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE
                    , AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING};
            params.put("statusWhere", statusWhere);
        } else if (billReceivebizVo.getStatus() == AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE) {
            hqlWhere.append(" and b.status in (:statusWhere)");
            Integer[] statusWhere = {
                    AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK};
            params.put("statusWhere", statusWhere);
        }
        hql.append(hqlWhere);
        return this.listByHQL(hql.toString(), params);
    }
    /**
     * 根据IDS查询票据签收表信息
     *
     * @param ids
     * @return
     */
    public List<AbBillReceivebiz> listAbBillReceivebizByIds(String[] ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        StringBuilder hql = new StringBuilder("from AbBillReceivebiz b where ").append(SqlUtil.getInSqlByParams("ids", "b.id", params));
        StringBuilder hqlWhere = new StringBuilder();
        hql.append(hqlWhere);
        return this.listByHQL(hql.toString(), params);
    }
    /**
     * 根据要求修改票据签收表信息
     *
     * @param ids
     * @param billReceivebizVo
     * @return
     */
    public int batchUpdateAbBillReceivebiz(String[] ids, AbBillReceivebizVO billReceivebizVo) {
        StringBuilder hql = new StringBuilder("update AbBillReceivebiz b set ");
        StringBuilder hqlSet = new StringBuilder();
        StringBuilder hqlWhere = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        hqlWhere.append(" where ").append(SqlUtil.getInSqlByParams("ids", "b.id", params));
        if (billReceivebizVo.getReplyDt() != null) {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.replyDt=:replyDt");
            params.put("replyDt", billReceivebizVo.getReplyDt());
        }
        if (billReceivebizVo.getUpdateDate() != null) {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.updateDate=:updateDate");
            params.put("updateDate", billReceivebizVo.getUpdateDate());
        }
        if (StringUtils.isNotEmpty(billReceivebizVo.getUpdateBy())) {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.updateBy=:updateBy");
            params.put("updateBy", billReceivebizVo.getUpdateBy());
        }
        if (StringUtils.isNotEmpty(billReceivebizVo.getUpdateName())) {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.updateName=:updateName");
            params.put("updateName", billReceivebizVo.getUpdateName());
        }
        if (AccebillConstant.AB_MSGTYPE_ACCEPT.equals(billReceivebizVo.getMsgtype())) {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            if (billReceivebizVo.getCdtratgduedt() != null) {
                hqlSet.append("b.cdtratgduedt=:cdtratgduedt");
                params.put("cdtratgduedt", billReceivebizVo.getCdtratgduedt());
            } else {
                hqlSet.append("b.cdtratgduedt=null");
            }
            if (StringUtils.isNotEmpty(billReceivebizVo.getCdtratgagcy())) {
                hqlSet.append(",b.cdtratgagcy=:cdtratgagcy");
                params.put("cdtratgagcy", billReceivebizVo.getCdtratgagcy());
            } else {
                hqlSet.append(",b.cdtratgagcy=null");
            }
            if (StringUtils.isNotEmpty(billReceivebizVo.getCdtratgs())) {
                hqlSet.append(",b.cdtratgs=:cdtratgs");
                params.put("cdtratgs", billReceivebizVo.getCdtratgs());
            } else {
                hqlSet.append(",b.cdtratgs=null");
            }
            if (StringUtils.isNotEmpty(billReceivebizVo.getAccptncagrmtnb())) {
                hqlSet.append(",b.accptncagrmtnb=:accptncagrmtnb");
                params.put("accptncagrmtnb", billReceivebizVo.getAccptncagrmtnb());
            } else {
                hqlSet.append(",b.accptncagrmtnb=null");
            }
        }
//		else if(AccebillConstant.AB_MSGTYPE_HINT_PAYER.equals(billReceivebizVo.getMsgtype())
//				||AccebillConstant.AB_MSGTYPE_HINT_PAYER_OVERDUE.equals(billReceivebizVo.getMsgtype())){
//			if(hqlSet.length()>0){
//				hqlSet.append(",");
//			}
//			if(StringUtils.isNotEmpty(billReceivebizVo.getDshnrcd())){
//				hqlSet.append("b.dshnrcd=:dshnrcd");
//				params.put("dshnrcd", billReceivebizVo.getDshnrcd());
//			}else{
//				hqlSet.append("b.dshnrcd=null");
//			}
//			if(StringUtils.isNotEmpty(billReceivebizVo.getDshnrrsn())){
//				hqlSet.append(",b.dshnrrsn=:dshnrrsn");
//				params.put("dshnrrsn", billReceivebizVo.getDshnrrsn());
//			}else{
//				hqlSet.append(",b.dshnrrsn=null");
//			}
//		}
        if (StringUtils.isNotEmpty(billReceivebizVo.getDshnrcd())) {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.dshnrcd=:dshnrcd");
            params.put("dshnrcd", billReceivebizVo.getDshnrcd());
        } else {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.dshnrcd=null");
        }
        if (StringUtils.isNotEmpty(billReceivebizVo.getDshnrrsn())) {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.dshnrrsn=:dshnrrsn");
            params.put("dshnrrsn", billReceivebizVo.getDshnrrsn());
        } else {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.dshnrrsn=null");
        }
        if (StringUtils.isNotEmpty(billReceivebizVo.getSgnupmk())) {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.sgnupmk=:sgnupmk");
            params.put("sgnupmk", billReceivebizVo.getSgnupmk());
        }
        if (billReceivebizVo.getStatus() != null) {
            if (hqlSet.length() > 0) {
                hqlSet.append(",");
            }
            hqlSet.append("b.status=:status");
            params.put("status", billReceivebizVo.getStatus());
            if (billReceivebizVo.getStatus() == AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK
                    || billReceivebizVo.getStatus() == AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_RECALL) {
                hqlWhere.append(" and b.status in (:statusWhere)");
                Integer[] statusWhere = {
                        AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE
                        , AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING};
                params.put("statusWhere", statusWhere);
            } else if (billReceivebizVo.getStatus() == AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE) {
                hqlWhere.append(" and b.status in (:statusWhere)");
                Integer[] statusWhere = {
                        AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK};
                params.put("statusWhere", statusWhere);
            }
        }
        hql.append(hqlSet).append(hqlWhere);
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * <b>功能：</b>根据MSGID获取签收数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-8-5 <br>
     *
     * @param msgID 消息ID
     * @return 签收数据
     */
    public AbBillReceivebiz getAbBillReceivebizByMsgId(String msgID) {
        String hql = "FROM AbBillReceivebiz  WHERE msgid=:msgID";
        Map<String, Object> params = new HashMap<>();
        params.put("msgID", msgID);
        List<AbBillReceivebiz> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
    /**
     * 展示票据签收信息审批页面列表查询信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT ");
         sql.append("P.ID,P.BILL_TYPE AS BILLTYPE,P.MSGTYPE,P.ORIG_NO AS ORIGNO,P.SBBLL_RNG_STRT_SN sbbllRngStrtSn, P.SBBLL_RNG_END_SN sbbllRngEndSn, P.sgnupmk,vv.TYPE_NAME as recBankTypeName, ");
        sql.append("P.BUSSDT,P.BILL_MONEY AS BILLMONEY,P.PAYER,P.ACCEPTOR,l.id as businessKey, ");
        sql.append("P.START_DATE AS STARTDATE,P.END_DATE AS ENDDATE,vv.id as vvid,p.REC_CORP_ID as recCorpId, ");
        sql.append("P.REQ_NM AS REQNM,P.REC_NM AS RECNM,P.REC_ACCT_ID RECACCTID,p.REC_CORP_CODE corpCode,p.REC_CORP_NAME corpName ");
        sql.append(",(case ");
        sql.append("when l.create_by is not null then BT_CONCAT4('(',l.create_by,')',l.create_name) ");
        sql.append("else null end) as applyName,");
        sql.append(" vv.APPLY_CORP_ID as applyCorpId,vv.APPLY_CORP_NAME as applyCorpName,vv.APPLY_CORP_CODE as applyCorpCode,vv.BUS_CORP_ID as busCorpId,vv.BUS_PERSONNEL as busPersonnel ");
        sql.append(" from AB_BILL_RECEIVEBIZ P ");
        sql.append(" left join AB_VOUCHERINFO v on v.orig_no=P.orig_no and P.SBBLL_RNG_STRT_SN=v.SBBLL_RNG_STRT_SN and P.SBBLL_RNG_END_SN=v.SBBLL_RNG_END_SN ");
        sql.append(" left join AB_ACCRECORDS l on P.id=l.recid ");
        sql.append(" left join AB_VOUCHERINFO_VICE vv  on vv.id=l.vvid ");
        sql.append(" WHERE 1=1 ");
        String applyName = input.getFormdata().get("applyName").toString();
        if (StringUtils.isNotEmpty(applyName)) {
            sql.append(" and BT_CONCAT4('(',l.create_by,')',l.create_name) like BT_CONCAT3('%',:applyName,'%')");
            params.put("applyName", applyName);
        }
        input.getFormdata().remove("applyName");
        String businessSql = SqlUtil.getInSqlByParams(businessList, "l.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        params.put("VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
        DataTablesOutput dataOutInfo = this.getDynamicBySQL(input, sql.toString(), AbBillReceivebizVO.class, "", false, params);
        return dataOutInfo;
    }
    /**
     * @param optType 拒签业务类型
     * @return void    返回类型
     * @throws
     * @Title: queryRefuseAutoData
     * @Description: TODO(查询自动任务拒绝签收的数据)
     */
    public List<AbAccrecords> queryRefuseAutoData(String optType) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,l.brief_id as briefId");
        sql.append(" from AB_VOUCHERINFO_VICE c");
        sql.append(" left join AB_VOUCHERINFO z");
        sql.append(" on z.id = c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" left join AB_ACCRECORDS l");
        sql.append(" on c.id = l.vvid");
//        sql.append(" left join sys_corp s");
//        sql.append(" on c.corp_id = s.id");
        if (optType.equals(AccebillConstant.AB_REFUSE_FBS)) {
            sql.append(" left join FBS_BUDGET_PROCESS f");
            sql.append(" on c.id = f.bill_id");
        }
        sql.append(" where c.valid_sign = '1'");
        sql.append(" and z.bill_flag = '11'");
        sql.append(" and l.valid_sign in ('2', '1')");
        sql.append(" and l.SEND_SIGN = '1'");
        sql.append(" and l.STATUS = '95'");
        if (optType.equals(AccebillConstant.AB_REFUSE_FBS)) {
            sql.append(" and l.brief_id in (010,9010,9003)");
            sql.append(" and f.status <> -2");
        }
        if (optType.equals(AccebillConstant.AB_REFUSE_RAT)) {
            sql.append(" and l.brief_id in (6011,011)");
            sql.append(" and c.RAT_MODEL <> 0");
            sql.append(" and z.Bill_Type=11");
        }
        sql.append(" and l.OPT_STATUS =-4");
        //sql.append(" and l.sign_up_flag='SU01'");
        List<AbAccrecords> list = this.listBySQLAliasToBean(sql.toString(), null, AbAccrecords.class);
        return list;
    }
    /**
     * 根据map查询 签收表数据
     *
     * @param params 查询参数
     * @return 返回签收列表
     */
    public List<AbBillReceivebiz> getAbBillReceivebizByParams(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("from AbBillReceivebiz b where 1=1 ");
        StringBuilder hqlWhere = new StringBuilder();
        if (params == null || params.isEmpty()) {
            return null;
        }
        if (params.containsKey("id")) {
            hqlWhere.append(" and id=:id ");
        }
        if (params.containsKey("status")) {
            hqlWhere.append(" and status=:status ");
        }
        if (params.containsKey("statusArray")) {
            hqlWhere.append(" and status in (:statusArray) ");
        }
        if (params.containsKey("origNo")) {
            hqlWhere.append(" and origNo=:origNo ");
        }
        if (params.containsKey("sbbllRngStrtSn")) {
            hqlWhere.append(" and sbbllRngStrtSn=:sbbllRngStrtSn ");
        }
        if (params.containsKey("sbbllRngEndSn")) {
            hqlWhere.append(" and sbbllRngEndSn=:sbbllRngEndSn ");
        }
        if (params.containsKey("msgtype")) {
            hqlWhere.append(" and msgtype=:msgtype ");
        }
        if (params.containsKey("acctId")) {
            hqlWhere.append(" and recAcctId=:acctId ");
        }
        hql.append(hqlWhere);
        hql.append(" ORDER BY bussdt DESC,createDate DESC ");
        return this.listByHQL(hql.toString(), params);
    }
    /**
     * 根据map查询 签收表VO数据
     *
     * @param params 查询参数
     * @return 返回签收VO list数据
     */
    public List<AbBillReceivebizVO> getAbBillReceivebizVOByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("select id,orig_No as origNo,status,SBBLL_RNG_STRT_SN sbbllRngStrtSn,SBBLL_RNG_END_SN sbbllRngEndSn from AB_BILL_RECEIVEBIZ b where 1=1 ");
        StringBuilder sqlWhere = new StringBuilder();
        if (params == null || params.isEmpty()) {
            return null;
        }
        if (params.containsKey("statusArray")) {
            sqlWhere.append(" and status in (:statusArray) ");
        }
        sql.append(sqlWhere);
        return this.listBySQLAliasToBean(sql.toString(), params, AbBillReceivebizVO.class);
    }
    /**
     * 根据传递查询参数map修改签收表数据
     *
     * @param params
     * @return
     */
    public int batchUpdateAbBillReceivebizByParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return 0;
        }
        StringBuilder sql = new StringBuilder("update AB_BILL_RECEIVEBIZ set ");
        sql.append(" update_date=:updateDate");
        params.put("updateDate", new Date());
        if (params.containsKey("status")) {
            sql.append(" ,status=:status ");
        }
        sql.append(" where 1=1 ");
        if (params.containsKey("statusOlds")) {
            sql.append(" and status in (:statusOlds) ");
        }
        return this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 根据单位id获取此单位中是否存在签收信息
     *
     * @param corpId
     * @return 有效笔数
     */
    public int getValidReceivebizNumByCorpId(String corpId) {
        String sql = "select count(*) from AB_BILL_RECEIVEBIZ P where P.REC_CORP_ID=:corpId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
//        params.put("type",type);
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 根据单位id获取此单位中是否存在签收审批数据
     *
     * @param corpId
     * @return 存在笔数
     */
    public int getValidBizApproveNumByCorpId(String corpId) {
        String sql = "select count(*) from AB_BILL_RECEIVEBIZ P where P.REC_CORP_ID=:corpId and p.status =:status ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        params.put("status", AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
//        params.put("type",type);
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 根据id批量修改状态
     *
     * @param ids          id数组
     * @param oldStatus    原状态
     * @param newStatus    状态
     * @return 返回成功成功几笔数据
     */
    public int batchUpdateStatus(String[] ids, Integer oldStatus, Integer newStatus) {
        StringBuilder sql = new StringBuilder("update AB_BILL_RECEIVEBIZ set status = :newStatus where id in (:ids) ");
        Map<String, Object> params = new HashMap<>(2);
        if (oldStatus != null) {
            sql.append(" and status = :oldStatus ");
            params.put("oldStatus", oldStatus);
        }
        params.put("newStatus", newStatus);
        params.put("ids", ids);
        return this.batchSQLExecute(sql.toString(), params);
    }
}
