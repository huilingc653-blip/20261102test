package com.bytter.bill.domain.bill.dao;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbVoucherinfoVODTO;
import com.bytter.bill.api.bill.dto.GuaContractBillDTO;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.AbBillNonassignableCancelVo;
import com.bytter.bill.domain.bill.vo.AbBillPayAndReceVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.bill.domain.bill.vo.GuaContractBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.utils.*;
import com.bytter.credit.dto.RatUrationDisDTO;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 票据主信息从表
 * @date 2017-06-21 15:50:03
 */
@Repository
public class AbVoucherinfoViceDao extends BaseDao<AbVoucherinfoVice, String> {
    /**
     * 审批打回或指令打回时设置从表打回业务类型
     *
     * @param id      从表ID
     * @param briefId 被修改的打回业务类型值
     * @param acid    被修改的打回业务流水ID
     * @return
     */
    public int updateBackBriefId(String id, String briefId, String acid) {
        StringBuilder hql = new StringBuilder(" update AbVoucherinfoVice set backBriefId=:briefId ");
        if (StringUtils.isNotEmpty(acid)) {
            hql.append(" ,backAcid=:acid ");
        }
        hql.append(" where id=:id ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("briefId", briefId);
        params.put("acid", acid);
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 根据ids批量修改票据从表所属库存信息
     *
     * @param params
     * @return
     */
    public int batchUpdateEnterStock(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update AbVoucherinfoVice "
                + " set enterStock = :enterStockNew"
                + ",updateDate = :updateDate ");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("updateName")) {
            hql.append(",updateName = :updateName ");
        }
        if (params.containsKey("poolStatus")) {
            hql.append(",poolStatus = :poolStatus ");
        }
        hql.append(" where ").append(SqlUtil.getInSqlByParams("ids", "id", params));
        if (params.containsKey("enterStockOld")) {
            hql.append(" and enterStock=:enterStockOld ");
        }
        if (params.containsKey("enterStockNew")) {
            hql.append(" and (enterStock!=:enterStockNew or enterStock is null) ");
        }
        return this.batchExecute(hql.toString(), params);
    }
	/**
	 * 根据ids批量修改票据从表所属库存信息
	 * @param params
	 * @return
	 */
	public int batchUpdateValidSign(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update AbVoucherinfoVice "
				+ " set validSign = (case when BT_NVL_NUM(payLockMoney,0) > 0 and type='1' then '0' else :validSignNew end)");
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName ");
		}
		hql.append(",updateDate = :updateDate ");
		hql.append(" where ").append(SqlUtil.getInSqlByParams("ids","id",params));
		if(params.containsKey("validSignOld")){
			hql.append(" and validSign=:validSignOld ");
		}
		return this.batchExecute(hql.toString(), params);
	}
    /**
     * 根据ids批量修改从表付款状态和付款单id
     *
     * @param params
     * @return
     */
    public int batchUpdateBillIsPayAndVoucherId(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update AbVoucherinfoVice "
                + " set isPay =:isPay,voucherId=:voucherId ");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("updateName")) {
            hql.append(",updateName = :updateName ");
        }
        hql.append(",updateDate = :updateDate ");
        hql.append(" where ").append(SqlUtil.getInSqlByParams("ids", "id", params));
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 根据票据主信息ID和单位ID获取从表数据
     *
     * @param vid    票据主表id
     * @param corpId 单位id
     * @param type   从表类型 0应收1应付
     * @return
     */
    public AbVoucherinfoVice getVoucherinfoViceByVidAndCorpId(String vid, String corpId, String type, String bankAcc) {
        String hql = "from AbVoucherinfoVice where vid=:vid and corpId=:corpId and type=:type ";
        Map<String, Object> params = new HashMap<>();
        params.put("vid", vid);
        params.put("corpId", corpId);
        params.put("type", type);
        if (StringUtils.isNotEmpty(bankAcc)) {
            params.put("bankAcc", bankAcc);
            hql += " and bankAcc=:bankAcc ";
        }
        hql += " order by createDate asc ";
        List<AbVoucherinfoVice> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 根据票据主信息ID和排除从表id获取是否存在从表数据
     *
     * @param vid  票据主表id
     * @param vvid 从表id
     * @return
     */
    public List<AbVoucherinfoVice> getVoucherinfoViceByVidAndVvid(String vid, String vvid) {
        String hql = "from AbVoucherinfoVice where vid=:vid and id<>:vvid  ";
        Map<String, Object> params = new HashMap<>();
        params.put("vid", vid);
        params.put("vvid", vvid);
        List<AbVoucherinfoVice> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return null;
    }
    /**
     * @param vid
     * @param validSign
     * @return
     */
    public List<AbVoucherinfoVice> getVoucherinfoViceByVidAndValidSign(String vid, String validSign) {
        String hql = "from AbVoucherinfoVice where vid=:vid and validSign=:validSign and  order by createDate desc";
        Map<String, Object> params = new HashMap<>();
        params.put("vid", vid);
        params.put("validSign", validSign);
        List<AbVoucherinfoVice> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return null;
    }
    /**
     * @param vids
     * @return
     */
    public List<AbVoucherinfoVice> getVoucherinfoViceByVids(String vids) {
        if (StringUtil.isBlank_new(vids)) {
            return null;
        }
        String hql = "from AbVoucherinfoVice where vid in(:vids)";
        Map<String, Object> params = new HashMap<>();
        params.put("vids", vids.split(","));
        List<AbVoucherinfoVice> list = this.listByHQL(hql, params);
        return list;
    }
    /**
     * 查看业务可用票据数据
     *
     * @param dataTablesInput 查询
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAccebillInfoDataTable(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" info.vid as vid,info.vvid as vvid,info.id as id,info.recid as recid,");
        sql.append(" info.billType as billType,info.billFlag as billFlag,info.payer as payer,");
        sql.append(" info.acceptor as acceptor,info.origNo as origNo,info.billMoney as billMoney,");
        sql.append(" info.startDate as startDate,info.endDate as endDate,info.expireDays as expireDays,info.payee as payee,");
        sql.append(" info.billStatus as billStatus,info.insideSing as insideSing,");
        // 新一代票据列表查询新增字段 -- start
        sql.append(" info.newStmIssuofpprInd AS newStmIssuofpprInd, info.blgrdbagcrclPrmtInd AS blgrdbagcrclPrmtInd, ");
        sql.append(" info.sbbllRngStrtSn AS sbbllRngStrtSn, info.sbbllRngEndSn AS sbbllRngEndSn, ");
        sql.append(" info.blStdAmt AS blStdAmt, info.blRmtrUnnIdNo AS blRmtrUnnIdNo, ");
        sql.append(" info.blRcvpymtpsUnnIdNo as blRcvpymtpsUnnIdNo, info.blAcptrUnnIdNo as blAcptrUnnIdNo, ");
        sql.append(" info.blHoldUnnIdNo as blHoldUnnIdNo, ");
        sql.append(" info.banendrsmtmk as banendrsmtmk, ");
        // -- end
        sql.append(" info.signUpFlag as signUpFlag,info.applyDate as applyDate,info.reqNm as reqNm,");
        sql.append(" info.recNm as recNm,info.recAcctId as recAcctId,info.bankAcc as bankAcc,");
        sql.append(" info.searchForMoney as searchForMoney,info.corpId as corpId,info.corpName as corpName,");
        sql.append(" info.optStatus as optStatus,info.briefId as briefId,info.source as source, ");
        sql.append(" info.poolStatus as poolStatus,info.bpName as bpName,info.inoutType as inoutType,info.lastId as lastId,info.bifCode as bifCode,");
        sql.append(" info.applyCorpId,info.applyCorpName,info.applyCorpCode,info.busCorpId,info.busPersonnel,info.balance as balance,info.onlineHandle,info.oldSelectSign ");
        sql.append(" from (select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
                .append("z.bill_type as billType,z.bill_flag as billFlag,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("case when BT_DATEDIFF2(z.END_DATE,BT_SYSDATE()) > 0 then BT_DATEDIFF2(z.END_DATE,BT_SYSDATE()) else null end as expireDays,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                // 新一代票据列表查询新增字段 -- start
                .append(" z.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, z.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ")
                .append(" z.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, z.SBBLL_RNG_END_SN AS sbbllRngEndSn, ")
                .append(" z.BL_STD_AMT AS blStdAmt, z.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ")
                .append(" z.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, z.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ")
                .append(" c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo, ")
                .append(" CASE WHEN l.BANKENDRSMTMK IS NULL THEN z.BANENDRSMTMK ELSE l.BANKENDRSMTMK END AS banendrsmtmk, ")
                // -- end
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,z.bill_money as searchForMoney,")
                .append("c.corp_id as corpId,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId, ")
                .append("sd.TYPE_CODE as source,bt_nvl_char(c.pool_status,'0')  as poolStatus, '' as bpName,'' as inoutType,'' as lastId,''  as bifCode,  ")
		.append(" CASE  ");
		if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
			sql.append(" WHEN c.pay_lock_money is not null and pnb.balance is not null THEN z.bill_money -c.pay_lock_money + pnb.balance ");
		}
		//trid票据下拨id
		else if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("trid"))){
			sql.append(" WHEN c.pay_lock_money is not null and tfdtl.trans_money is not null THEN z.bill_money - c.pay_lock_money + tfdtl.trans_money ");
		}
        sql.append(" WHEN c.pay_lock_money is not null THEN z.bill_money - c.pay_lock_money ");
        sql.append(" ELSE z.bill_money END AS balance, ");
        sql.append("c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ");
		if("1".equals(dataTablesInput.getFormdata().get("isPayed"))){
			sql.append(" , l.SEND_SIGN as onlineHandle ");
		} else {
			sql.append(" , CASE " +
					"WHEN acc.circulation_mode is null or acc.circulation_mode='0' THEN '0' " +
					"WHEN z.NEW_STM_ISSUOFPPR_IND='1' and (abbad.NEW_BILL_FLG is null or abbad.NEW_BILL_FLG='0') THEN '0' " +
					"ELSE '1' END as onlineHandle ");
		}

        //置顶破除获取缓存数据
        String oldIds = dataTablesInput.getFormdata().get("oldIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("oldIds"));
        String ids = dataTablesInput.getFormdata().get("ids") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ids"));
        if(!"1".equals(dataTablesInput.getFormdata().get("isCatchData"))) {
            //对已选票据置顶处理：内联 case when 判断，替代原来拼 dual 临时表的写法（oracle/mysql 通用）
            params.put("oldSelectIdsVal", StrUtil.isNotEmpty(oldIds) ? StrUtil.split(oldIds, ",") : StrUtil.split(ids, ","));
            sql.append(" ,case when ").append(SqlUtil.getInSqlByParams("oldSelectIdsVal", "l.id", params))
                    .append(" then '1' else '0' end as oldSelectSign ");
        }else{
            sql.append(" , '0' as oldSelectSign ");
        }
		sql.append(" from AB_VOUCHERINFO_VICE c ")
                .append("  join AB_VOUCHERINFO z on z.id=c.vid ");
		//付款办理查询  只查有效票据
		//付款单锁定票据，从表VALID_SIGN为0，需要支持查出这种数据（因为暂存锁定票据时，同一个单重新编辑时需要重新选到这张票）
		if("1".equals(dataTablesInput.getFormdata().get("ppFlg"))){
			sql.append(" and c.VALID_SIGN != 3 ");
		}
        sql.append("  join AB_ACCRECORDS l on c.id=l.vvid ")
//                .append(" left join sys_corp s on c.corp_id = s.id ")
//                .append(" left join sys_corp s2 on c.apply_corp_id = s2.id ")
                .append(" left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=l.brief_id "
                        + "and (c.source=sd.source "
                        + "or (c.source is null and l.bill_source=sd.source) "
                        + "or (c.source is null and l.bill_source is null and sd.source is null )"
                        + ") ");
		if("1".equals(dataTablesInput.getFormdata().get("isPayed"))){
			sql.append(" left join nis_bill nb on nb.bill_Id=l.id ");
		}
		sql.append(" left join bt_bank_acc acc on c.bank_acc=acc.BANK_ACC ");
		if(!"1".equals(dataTablesInput.getFormdata().get("isPayed"))){
			sql.append(" left join ab_bill_bank_auto_dtl abbad on abbad.bif_code=acc.bif_code ");
		}
//		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
		if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
			sql.append(" left join nis_bill pnb on pnb.bill_Id=l.id and pnb.pay_id=:payId ");
			params.put("payId",dataTablesInput.getFormdata().get("payId"));
		}
		//trid票据下拨id
		if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("trid"))){
			sql.append(" left join ab_bill_transfer tf on tf.id = :trid");
			sql.append(" left join ab_bill_transfer_dtl tfdtl on tfdtl.BILL_AID = l.id and tfdtl.TRANSFER_ID = tf.id ");
			params.put("trid",dataTablesInput.getFormdata().get("trid"));
		}
        sql.append(" where 1=1 ")
                .append(" and l.STATUS=:STATUS ")
                .append(" and l.OPT_STATUS =:OPT_STATUS ");
        sql.append(" and c.ENTER_STOCK in (:enterStock) ");
        if (corpAuthList != null) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "c.APPLY_CORP_ID", "corpAuthId", params));
        }
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
//        String oldIds = dataTablesInput.getFormdata().get("oldIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("oldIds"));
        if ("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
            sql.append(" and l.brief_id in (:brief_id010) ");
            sql.append(" and ((nb.id is null and l.valid_sign =:valid_sign ) ");
			if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
				sql.append(" or (c.pay_lock_money is null or (pnb.balance is not null and (z.bill_money - c.pay_lock_money + pnb.balance) > 0)) ");
				sql.append(" or (c.pay_lock_money is null or (pnb.balance is null and (z.bill_money - c.pay_lock_money) > 0)) ");
			}else{
				sql.append(" or (c.pay_lock_money is null or (z.bill_money - c.pay_lock_money) > 0) ");
			}
			//针对修改历史已选数据进行支持显示
			if(StringUtil.isNotEmpty(oldIds)){
				sql.append(" or ( ").append(SqlUtil.getInSqlByParams(oldIds.split(","),"l.id","oldIds",params)).append(")");
			}
			sql.append(" ) ");
			params.put("brief_id010", new String[]{AccebillConstant.BRIEF_STATUS_010});
		} else {
			sql.append(" and (c.box_status=:box_status ");
			//在同一个下拨单里多次选择同一张票
			if(dataTablesInput.getFormdata().get("trid") != null && !"".equals(dataTablesInput.getFormdata().get("trid"))){
				sql.append(" or (c.box_status=:box_status10 and tfdtl.id is not null and (tfdtl.trans_money is null or (z.bill_money - BT_NVL_NUM(tfdtl.trans_money,0) = 0)))) ");
				params.put("box_status10",10);
			}else {
				sql.append(")");
			}
			sql.append(" and BT_NVL_CHAR(l.BANKENDRSMTMK, z.BANENDRSMTMK)=:BANENDRSMTMK ")
                    .append(" and ( ")
                    .append(" ( ")
                    //线上电票可以业务
                    .append(" l.SEND_SIGN=:SEND_SIGN1 ")
                    .append(" and z.bill_flag =:bill_flag ")
                    .append(" and z.end_date>=:end_date ")
                    .append(" and l.brief_id in (:brief_id1) ) ")
                    //纸票或者线下电票可用业务
                    .append(" or   ")
                    //收票登记非质押来源
                    .append(" ( (")
                    .append(" l.brief_id in (:brief_id2) ")
                    .append(" and c.BILL_SOURCE <> :BILL_SOURCE ")
                    .append(" ) or ")
                    .append(" l.brief_id in (:brief_id3)")
                    .append(" ) ")
                    .append(" ) ");
            sql.append(" and ((l.valid_sign =:valid_sign and c.valid_sign=:valid_sign) ");
			if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
				sql.append(" or ( l.valid_sign =:valid_sign and (c.pay_lock_money is null or (z.bill_money - c.pay_lock_money + BT_NVL_NUM(pnb.balance,0) > 0))) ");
			}
			//在同一个下拨单里多次选择同一张票
			else if(dataTablesInput.getFormdata().get("trid") != null && !"".equals(dataTablesInput.getFormdata().get("trid"))){
					sql.append(" or (c.box_status=:box_status10 and tfdtl.id is not null and (tfdtl.trans_money is null or (z.bill_money - BT_NVL_NUM(tfdtl.trans_money,0) = 0))) ");
					params.put("box_status10",10);
			}
			else{
				sql.append(" or ( l.valid_sign =:valid_sign and (c.pay_lock_money is null or (z.bill_money - c.pay_lock_money > 0))) ");
			}
            //针对修改历史已选数据进行支持显示
            if (StringUtil.isNotEmpty(oldIds)) {
                sql.append(" or ").append(SqlUtil.getInSqlByParams(oldIds.split(","), "l.id", "oldIds", params));
            }
            sql.append(" ) ");
            params.put("SEND_SIGN1", AccebillConstant.AB_BILL_SEND_SIGN_Y);
            params.put("bill_flag", AccebillConstant.AB_BILL_FLAG_ELECT);
            params.put("end_date", DateUtils.getDateYMD());
            params.put("box_status", AccebillConstant.AB_BOX_STATUS_Y);
            params.put("SEND_SIGN2", AccebillConstant.AB_BILL_SEND_SIGN_N);
            params.put("brief_id1", new String[]{
                    AccebillConstant.BRIEF_STATUS_9003,
                    AccebillConstant.BRIEF_STATUS_9010,
                    AccebillConstant.BRIEF_STATUS_9012,
                    AccebillConstant.BRIEF_STATUS_9019});
            params.put("brief_id2", new String[]{AccebillConstant.BRIEF_STATUS_6001});
            params.put("BILL_SOURCE", AccebillConstant.AB_BILL_SOURCE_PLEDGE);
            params.put("brief_id3", new String[]{
                    AccebillConstant.BRIEF_STATUS_6019,
                    AccebillConstant.BRIEF_STATUS_6212});
            params.put("BANENDRSMTMK", AccebillConstant.AB_BANEM_Y);
        }
        //已选数据进行过滤显示
        String selectIds = dataTablesInput.getFormdata().get("selectIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("selectIds"));
        if (StringUtil.isNotEmpty(selectIds)) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(selectIds.split(","), "l.id", "selectIds", params));
        }
        //完善多个票据号同时查询的场景
        String origNo = dataTablesInput.getFormdata().get("origNo") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("origNo"));
        if (StringUtil.isNotEmpty(origNo)) {
            sql.append(" and ").append(AbAccebillUtils.getOrigNoQuerySql("z.orig_no", origNo, params));
            dataTablesInput.getFormdata().remove("origNo");
        }
        params.put("enterStock", new String[]{AccebillConstant.AB_ENTER_STOCK_CORP, AccebillConstant.AB_ENTER_STOCK_NET});
        params.put("valid_sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("STATUS", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
		if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("ins_corpId"))){
			sql.append(" and ").append(
					SqlUtil.getInSqlByParams(
							StrUtil.split((String)dataTablesInput.getFormdata().get("ins_corpId"),",")
							,"c.corp_id","corpIdParams",params));
		}
//        //显示票据池内可背书数据
//        sql.append(" union all ");
//        sql.append(" select z.id as vid,c.id as vvid,apd.id as id,'' as recid,z.bill_type as billType, z.bill_flag as billFlag,z.payer as payer,")
//                .append(" z.acceptor as acceptor,z.orig_no as origNo,z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
//                .append(" z.payee as payee,z.bill_status as billStatus,c.inside_sing  as insideSing, ")
//                // 新一代票据列表查询新增字段 -- start
//                .append(" z.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, z.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ")
//                .append(" z.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, z.SBBLL_RNG_END_SN AS sbbllRngEndSn, ")
//                .append(" z.BL_STD_AMT AS blStdAmt, z.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ")
//                .append(" z.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, z.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ")
//                .append(" c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo, ")
//                .append(" z.BANENDRSMTMK AS banendrsmtmk, ")
//                // -- end
//                .append(" '' as signUpFlag,null as applyDate,'' as reqNm,'' as recNm,'' as recAcctId,c.bank_acc  as bankAcc,z.bill_money as searchForMoney,c.corp_id as corpId,")
//                .append(" apc.CORP_NAME as corpName,apd.opt_status as optStatus,apd.brief_id as briefId,")
//                .append(" '' as source,bt_nvl_char(c.pool_status,'0')  as poolStatus, ap.bp_name as bpName,apd.inout_type as inoutType,apd.LASTID as lastId, api.bif_code as bifCode,")
//			.append(" CASE ");
//			if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
//				sql.append(" WHEN ifnull(c.pay_lock_money,0) > 0 THEN z.bill_money -c.pay_lock_money + ifnull(pnb.balance,0) ");
//			}else{
//				sql.append(" WHEN ifnull(c.pay_lock_money,0) > 0 THEN z.bill_money -c.pay_lock_money ");
//			}
//			sql.append(" WHEN M.BALANCE IS NULL THEN Z.BILL_MONEY ");
//        //针对修改历史已选数据进行支持显示
//        if (StringUtil.isNotEmpty(oldIds)) {
//            sql.append(" WHEN apd.id in (:oldIds) then m.balance + m.pay_money ");
//        }
//        sql.append(" ELSE M.BALANCE END AS balance, ");
//        sql.append("c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ")
//                .append(" from ab_pool_dtl apd ")
//                .append(" left join AB_VOUCHERINFO_VICE c on apd.vv_id = c.id ")
//                .append(" left join AB_VOUCHERINFO z on c.vid = z.id ")
//                .append(" left join ab_pool_corp apc on c.corp_id = apc.corp_id  and APC.valid_sign = :validSignPool ")
////                .append(" left join sys_corp sc on apc.corp_id = sc.id ")
////                .append(" left join sys_corp s2 on c.apply_corp_id = s2.id ")
//                .append(" left join ab_pool ap  on ap.id = apd.bp_id ")
//                .append(" left join AB_POOLIF api  on api.id = ap.pif_id ");
//        sql.append(" LEFT JOIN AB_BILL_PAY_MIDDLE M ON C.ID = M.BILL_ID ");
//        sql.append(" LEFT JOIN (SELECT MAX(CREATE_DATE) AS CREATE_DATE, BILL_ID FROM AB_BILL_PAY_MIDDLE GROUP BY BILL_ID) MM ON MM.BILL_ID = M.BILL_ID ");
//        if ("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
//            sql.append(" left join nis_bill nb on nb.bill_Id=apd.id ");
//            sql.append(" left join nis_billhead nbh on nbh.id=nb.pay_id ");
//        }
//		sql.append(" left join bt_bank_acc acc on c.bank_acc=acc.BANK_ACC ");
//		sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
//		if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
//			sql.append(" left join nis_bill pnb on pnb.bill_Id=apd.id and pnb.pay_id=:payId ");
//		}
//        sql.append(" where apd.brief_id in (:briefIdPool) ");
//        if ("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
////			sql.append(" and (nb.id is null or nbh.status=-2 ) ");
//            sql.append(" and ( m.balance > 0 ");
//            //针对修改历史已选数据进行支持显示
//            if (StringUtil.isNotEmpty(oldIds)) {
//                sql.append(" or (m.balance = 0 and apd.id in (:oldIds)) ");
//            }
//            sql.append(" ) AND (M.CREATE_DATE = MM.CREATE_DATE AND M.BILL_ID = MM.BILL_ID ) AND nbh.id = m.pay_id ");
//            sql.append(" AND ((nbh.status = -2 AND m.status = 1) OR (nbh.status <> -2 and m.status = 0 AND c.valid_sign = 1)) ");
//        } else {
//            sql.append(" and apd.status in (:statusPool) ")
//                    .append(" and apd.opt_status in (:optStatusPool)")
//                    .append(" and apd.valid_sign = :validSignPool")
//                    .append(" and c.pool_status = :poolStatusPool")
//                    .append(" and c.box_status = :boxStatusPool ")
//					.append(" AND ((M.CREATE_DATE = MM.CREATE_DATE AND M.BILL_ID = MM.BILL_ID) ");
//
//			if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
//				sql.append(" or ( apd.valid_sign = :validSignPool and M.balance is null and (z.bill_money - ifnull(c.pay_lock_money,0) + ifnull(pnb.balance,0)) > 0) ");
//			}else{
//				sql.append(" or ( apd.valid_sign = :validSignPool and M.balance is null and (z.bill_money - ifnull(c.pay_lock_money,0)) > 0) ");
//			}
//			sql.append(" ) ");
//		}
//        if (corpAuthList != null) {
//            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "apc.CORP_ID", "corpAuthId", params));
//        }
//        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
//            //添加账户授权控制
//            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
//            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
//        }
//        //已选数据进行过滤显示
//        if (StringUtil.isNotEmpty(ids)) {
//            sql.append(" and ").append(SqlUtil.getInSqlByParams(ids.split(","), "apd.id", "selectIds", params));
//        }
//        //完善多个票据号同时查询的场景
//        if (StringUtil.isNotEmpty(origNo)) {
//            sql.append(" and ").append(AbAccebillUtils.getOrigNoQuerySql("z.orig_no", origNo, params));
//        }
//        params.put("validSignPool", AccebillConstant.AB_BILL_VALID_SIGN_1);
//        params.put("briefIdPool", new String[]{AbPoolConstant.AB_POOL_BRIEF_8018, AbPoolConstant.AB_POOL_BRIEF_8118, AbPoolConstant.AB_POOL_BRIEF_8218});
//        params.put("statusPool", new Integer[]{AbPoolConstant.AB_POOL_STATUS95});
//        params.put("optStatusPool", new String[]{AbPoolConstant.AB_POOL_OPT_STATUS19});
//        params.put("poolStatusPool", AbPoolConstant.AB_BILL_POOL_STATUS_1);
//        params.put("boxStatusPool", AccebillConstant.AB_BOX_STATUS_Y);
//		if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("ins_corpId"))){
//			sql.append(" and ").append(
//					SqlUtil.getInSqlByParams(
//							StrUtil.split((String)dataTablesInput.getFormdata().get("ins_corpId"),",")
//							,"c.corp_id","corpPoolIdParams",params));
//		}
		dataTablesInput.getFormdata().remove("ins_corpId");
		//用户满足对应查询权限的单位数据
		sql.append(" ) info where 1=1 ");
		if("1".equals(dataTablesInput.getFormdata().get("batchPay"))
			&& StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("batchPayMoney"))){
			sql.append(" and info.blgrdbagcrclPrmtInd = '1' and info.balance>=:batchPayMoney ");
			params.put("batchPayMoney",new BigDecimal((String)dataTablesInput.getFormdata().get("batchPayMoney")));
		}
        dataTablesInput.getFormdata().remove("isPayed");
        //置顶破除获取缓存数据
        if(!"1".equals(dataTablesInput.getFormdata().get("isCatchData")) && dataTablesInput.getColumns().size()>26) {
            if ("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
                dataTablesInput.getOrder().add(0, new Order(26, "desc"));
            } else {
                dataTablesInput.getOrder().add(0, new Order(27, "desc"));
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbVoucherinfoViceVO.class, "info", false, params);
    }
    /**
     * 开票需要补录时：应付票据：开票补录审批通过的票据
     * 开票不需要补录时：应付票据：开票完成（线下电票）提示付款完成（线上电票）
     *
     * @param dataTablesInput 查询
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAccebillEmitInfoDataTable(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        boolean isEmitMake = dataTablesInput.getFormdata().containsKey(AccebillConstant.AB_CONFIG_ISEMITMAKE)
                && "true".equals(String.valueOf(dataTablesInput.getFormdata().get(AccebillConstant.AB_CONFIG_ISEMITMAKE)));
        dataTablesInput.getFormdata().remove(AccebillConstant.AB_CONFIG_ISEMITMAKE);
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" info.vid as vid,info.vvid as vvid,info.id as id,info.recid as recid,");
        sql.append(" info.billType as billType,info.billFlag as billFlag,info.payer as payer,");
        sql.append(" info.acceptor as acceptor,info.origNo as origNo,info.billMoney as billMoney,");
        sql.append(" info.startDate as startDate,info.endDate as endDate,info.payee as payee,");
        sql.append(" info.billStatus as billStatus,info.insideSing as insideSing,");
        // 新一代票据列表查询新增字段 -- start
        sql.append(" info.newStmIssuofpprInd AS newStmIssuofpprInd, info.blgrdbagcrclPrmtInd AS blgrdbagcrclPrmtInd, ");
        sql.append(" info.sbbllRngStrtSn AS sbbllRngStrtSn, info.sbbllRngEndSn AS sbbllRngEndSn, ");
        sql.append(" info.blStdAmt AS blStdAmt, info.blRmtrUnnIdNo AS blRmtrUnnIdNo, ");
        sql.append(" info.blRcvpymtpsUnnIdNo as blRcvpymtpsUnnIdNo, info.blAcptrUnnIdNo as blAcptrUnnIdNo, ");
        sql.append(" info.blHoldUnnIdNo as blHoldUnnIdNo, ");
        sql.append(" info.banendrsmtmk as banendrsmtmk, ");
        // -- end
        sql.append(" info.signUpFlag as signUpFlag,info.applyDate as applyDate,info.reqNm as reqNm,");
        sql.append(" info.recNm as recNm,info.recAcctId as recAcctId,info.bankAcc as bankAcc,");
        sql.append(" info.searchForMoney as searchForMoney,info.corpId as corpId,info.corpName as corpName,");
        sql.append(" info.optStatus as optStatus,info.briefId as briefId,info.source as source, info.balance as balance,");
        sql.append(" info.applyCorpId,info.applyCorpName,info.applyCorpCode,info.busCorpId,info.busPersonnel");
        sql.append(" from (select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
                .append("z.bill_type as billType,z.bill_flag as billFlag,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                // 新一代票据列表查询新增字段 -- start
                .append(" z.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, z.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ")
                .append(" z.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, z.SBBLL_RNG_END_SN AS sbbllRngEndSn, ")
                .append(" z.BL_STD_AMT AS blStdAmt, z.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ")
                .append(" z.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, z.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ")
                .append(" c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo, ")
                .append(" CASE WHEN l.BANKENDRSMTMK IS NULL THEN z.BANENDRSMTMK ELSE l.BANKENDRSMTMK END AS banendrsmtmk, ")
                // -- end
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,z.bill_money as searchForMoney,")
                .append("c.corp_id as corpId,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId,")
                .append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel,")
                .append("sd.TYPE_CODE as source, ");
				sql.append(" CASE  ");
				if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
					sql.append(" WHEN BT_NVL_NUM(c.pay_lock_money,0) > 0 THEN z.bill_money -c.pay_lock_money + BT_NVL_NUM(pnb.balance,0) ");
				}else{
					sql.append(" WHEN BT_NVL_NUM(c.pay_lock_money,0) > 0 THEN z.bill_money -c.pay_lock_money ");
				}
        sql.append(" WHEN M.BALANCE IS NULL THEN Z.BILL_MONEY ");
        String oldIds = dataTablesInput.getFormdata().get("oldIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("oldIds"));
        sql.append(" ELSE Z.BILL_MONEY END AS balance ");
        sql.append(" from AB_VOUCHERINFO_VICE c ")
                .append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ")
                .append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
//                .append(" left join sys_corp s on c.corp_id = s.id ")
//                .append(" left join sys_corp s2 on c.apply_corp_id = s2.id ")
//        .append(" left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=l.brief_id and (l.bill_source=sd.source or (l.bill_source is null and sd.source is null) ) ")
                .append(" left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=l.brief_id "
                        + "and (c.source=sd.source "
                        + "or (c.source is null and l.bill_source=sd.source) "
                        + "or (c.source is null and l.bill_source is null and sd.source is null )"
                        + ") ")
                .append(" left join AB_BILL_PAY_MIDDLE m on c.id = m.bill_id ")
                .append(" left join (SELECT MAX(CREATE_DATE) AS CREATE_DATE, BILL_ID FROM AB_BILL_PAY_MIDDLE GROUP BY BILL_ID) MM ON MM.BILL_ID = M.BILL_ID  ");
		if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
			sql.append(" left join nis_bill pnb on pnb.bill_Id=l.id and pnb.pay_id=:payId ");
			params.put("payId",dataTablesInput.getFormdata().get("payId"));
		}
        sql.append(" where 1=1 ");
        sql.append(" and ((M.CREATE_DATE = MM.CREATE_DATE AND M.BILL_ID = MM.BILL_ID) OR m.create_date IS NULL) ");
        sql.append(" and ( ");
        //是否已付款
        //if("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
        //在线票据：对方已收票/未在付款办理挂接（票据在承兑汇票中已开出）。
        //在线票据：对方已收票/已在付款办理挂接。
        //离线票据：已登记（已录入或已导入）/未在付款办理挂接。
        //离线票据：已登记（已录入或已导入）/已在付款办理挂接。
        //离线票据：未登记(不支持）
//			sql.append(" ((((C.IS_PAY = 0 OR C.IS_PAY IS NULL) AND C.VOUCHER_ID IS NULL  and (m.balance > 0 or m.balance is null))) or (c.is_pay = 1 and (m.balance > 0 or m.balance is null))) ");
			sql.append(" ((((C.IS_PAY = 0 OR C.IS_PAY IS NULL) AND C.VOUCHER_ID IS NULL  and (m.balance > 0))) or (c.is_pay = 1 and (m.balance > 0))) ");
        //} else {
        //在线票据：在线票据：已开票登记，对方未收票。
        //离线票据：不支持，即离线票据即为已付款票据
        //sql.append(" ((C.IS_PAY = 0 OR C.IS_PAY IS NULL) AND C.VOUCHER_ID IS NULL  and (m.balance > 0 or m.balance is null)) ");
        //}
        //针对修改历史已选数据进行支持显示
        if (StringUtil.isNotEmpty(oldIds)) {
            sql.append(" or ").append(SqlUtil.getInSqlByParams(oldIds.split(","), "l.id", "oldIds", params));
        }
		if(StrUtil.isNotEmpty((String)dataTablesInput.getFormdata().get("payId"))) {
			sql.append(" or ( M.balance is null and (z.bill_money - BT_NVL_NUM(c.pay_lock_money,0) + BT_NVL_NUM(pnb.balance,0)) > 0) ");
		}else{
			sql.append(" or ( M.balance is null and (z.bill_money - BT_NVL_NUM(c.pay_lock_money,0)) > 0) ");
		}
        sql.append(" ) ");
        params.put("C_VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
        if (corpAuthList != null) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "c.APPLY_CORP_ID", "corpAuthIds", params));
        }
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        //是否已付款选是时
        //在线票据：对方已收票/未在付款办理挂接（票据在承兑汇票中已开出）。
        //在线票据：对方已收票/已在付款办理挂接。
        //离线票据：已登记（已录入或已导入）/未在付款办理挂接。
        //离线票据：已登记（已录入或已导入）/已在付款办理挂接。
        //离线票据：未登记(不支持）
        if ("1".equals(dataTablesInput.getFormdata().get("isPayed"))) {
            // 从表 :处理状态 box_status -- 处理完成 11
            // 流水表 :处理标记 valid_sign -- 可用 1
            // 操作状态 opt_status -- 已签收19
            sql.append(" and c.box_status=:C_BOX_STATUS ");
            sql.append(" and l.valid_sign= :L_Valid_Sign ");
            sql.append(" and l.status= :L_STATUS95 ");
            sql.append(" and l.opt_status=:L_OPT_STATUS ");
            if (isEmitMake) {//系统需要开票补录功能时 业务类型 brief_id -- 6101(补录)
                sql.append(" and (l.brief_id=:L_BRIEF_ID6101 ");
                // 当线上开票为提示收票完成时也需要可选择
                //sql.append(" or (l.brief_id=:briefId001 and z.bill_status in (:billStatusList))  ");
                sql.append(" ) ");
            } else {//系统不需要开票补录功能时
                // 指令发送提示收票完成(线上)和开票登记完成(线下)就可以做 选择 不需要补录
                sql.append(" and (l.brief_id=:L_BRIEF_ID6101 or (l.brief_id in (:briefId003) and l.send_sign=:sendSign1 ) or (l.brief_id in (:briefId001) and l.send_sign=:sendSign0 ) ) ");
                //sql.append(" and l.brief_id in (:briefIds) ");
                params.put("sendSign0", new String[]{AccebillConstant.AB_BILL_SEND_SIGN_N});
                params.put("briefId003", new String[]{AccebillConstant.BRIEF_STATUS_003});
                params.put("briefIds", new String[]{AccebillConstant.BRIEF_STATUS_001, AccebillConstant.BRIEF_STATUS_003, AccebillConstant.BRIEF_STATUS_6101});
            }
        } else {
            //是否已付款为否时
            //在线票据：在线票据：已开票登记，对方未收票。
            //离线票据：不支持，即离线票据即为已付款票据
            sql.append(" and (((l.brief_id=:briefId001 ");
            sql.append(" and c.box_status=:C_BOX_STATUS ");
            sql.append(" and l.valid_sign= :L_Valid_Sign ");
            sql.append(" and l.status= :L_STATUS95 ");
            sql.append(" and l.opt_status=:L_OPT_STATUS ");
            sql.append(")  ");
            //提示承兑或提示收票在途的票据
            sql.append(" or (l.brief_id in (:briefId23) and l.valid_sign in(:validsignAll) and l.status in (:statusAll) and l.opt_status in (:optStatusAll)))");
            sql.append(" and z.bill_status in (:billStatusList) and l.send_sign=:sendSign1");
            sql.append(") ");
        }
        params.put("C_BOX_STATUS", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("L_Valid_Sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("L_STATUS95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("L_OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("briefId23", new String[]{AccebillConstant.BRIEF_STATUS_002, AccebillConstant.BRIEF_STATUS_003});
        params.put("validsignAll", new String[]{AccebillConstant.AB_BILL_VALID_SIGN_1, AccebillConstant.AB_BILL_VALID_SIGN_0});
        params.put("statusAll", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS95, AccebillConstant.AB_BILL_BUS_STATUS11, AccebillConstant.AB_BILL_BUS_STATUS94, AccebillConstant.AB_BILL_BUS_STATUS_1});
        params.put("optStatusAll", new String[]{AccebillConstant.AB_BILL_OPT_STATUS16, AccebillConstant.AB_BILL_OPT_STATUS18, AccebillConstant.AB_BILL_OPT_STATUS19,});
        params.put("sendSign1", new String[]{AccebillConstant.AB_BILL_SEND_SIGN_Y});
        params.put("L_BRIEF_ID6101", AccebillConstant.BRIEF_STATUS_6101);
        params.put("briefId001", AccebillConstant.BRIEF_STATUS_001);
		params.put("billStatusList", new String[]{AccebillConstant.AB_BILL_STATUS_010004,AccebillConstant.AB_BILL_STATUS_020001,
				AccebillConstant.AB_BILL_STATUS_020006,AccebillConstant.AB_BILL_STATUS_030001,AccebillConstant.NEWBILL_STATUS_CS01,AccebillConstant.NEWBILL_STATUS_CS02});
        //已选数据进行过滤显示
        String ids = dataTablesInput.getFormdata().get("selectIds") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("selectIds"));
        if (StringUtil.isNotEmpty(ids)) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(ids.split(","), "l.id", "selectIds", params));
            dataTablesInput.getFormdata().remove("selectIds");
        }
        //完善多个票据号同时查询的场景
        String origNo = dataTablesInput.getFormdata().get("origNo") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("origNo"));
        if (StringUtil.isNotEmpty(origNo)) {
            sql.append(" and ").append(AbAccebillUtils.getOrigNoQuerySql("z.orig_no", origNo, params));
            dataTablesInput.getFormdata().remove("origNo");
        }
        sql.append(" ) info ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbVoucherinfoViceVO.class, "info", true, params);
    }
    /**
     * 根据流水IDS查看票据信息
     *
     * @param ids 流水ID
     * @return
     */
    public List<AbVoucherinfoViceVO> listAccebillInfoViewByIds(String[] ids,String payId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        sql.append("select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
                .append("z.bill_type as billType,z.bill_flag as billFlag,c.type as type,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,c.inside_sing as insideSing,")
                .append("l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,")
                .append("c.corp_id as corpId,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId,bs.bus_type as busTypeBriefId,c.VOUCHER_ID as voucherId, ")
                .append("sd.TYPE_CODE as source,l.status as busStatus,l.opt_status as inoptStatus, ")
                .append("l.SEND_SIGN as sendSign,(case when c.type='0' then '1' else '0' end ) as isEmitBill, ")
                .append("tc.brief_id as tcBriefId,tc.status as tcBusStatus,tc.opt_status as tcInoptStatus, ")
                .append("ts.brief_id as tsBriefId,ts.status as tsBusStatus,ts.opt_status as tsInoptStatus, ")
                .append("tp.brief_id as tpBriefId,tp.status as tpBusStatus,tp.opt_status as tpInoptStatus, ")
                .append("c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel, ")
                .append("z.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, z.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ")
                .append(" z.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, z.SBBLL_RNG_END_SN AS sbbllRngEndSn, ")
                .append(" z.BL_STD_AMT AS blStdAmt, z.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ")
                .append(" z.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, z.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ")
                .append(" CASE WHEN l.BANKENDRSMTMK IS NULL THEN z.BANENDRSMTMK ELSE l.BANKENDRSMTMK END AS banendrsmtmk ");
                if(StringUtils.isNotBlank(payId)){
                    sql.append(" , BT_NVL_NUM(pnb.balance,bpm.PAY_MONEY) payMoney ");
                }
                sql.append(" from AB_VOUCHERINFO_VICE c ")
                .append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ")
                .append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
                .append(" left join AB_ACCRECORDS tc on c.id=tc.vvid and tc.valid_sign in(:validSign) and tc.brief_id=:tcBriefId and c.type=:viceType and l.send_sign=:sendSign and tc.opt_status not in (-2,-4,-3) and tc.status !=-2 ")
                .append(" left join AB_ACCRECORDS ts on c.id=ts.vvid and (ts.valid_sign in(:validSign) or (ts.opt_status=:tsOptStatus and ts.valid_sign=:validSign2) ) and ts.brief_id=:tsBriefId and c.type=:viceType and l.send_sign=:sendSign and ts.opt_status not in (-2,-4,-3) and ts.status !=-2  ")
                .append(" left join AB_ACCRECORDS tp on c.id=tp.vvid and tp.valid_sign in(:validSign) and tp.brief_id=:tpBriefId and c.type=:viceType and tp.status !=-2 ")
//                .append(" left join sys_corp s on c.corp_id = s.id ")
//                .append(" left join sys_corp s2 on c.APPLY_CORP_ID = s2.id ")
                .append(" left join ab_brief_status bs on bs.status = l.brief_id ")
                .append(" left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=l.brief_id and (l.bill_source=sd.source or (l.bill_source is null and sd.source is null) ) ");
                //若是编辑付款单来的查询，需要添加付款单的关联查询，查出相应的支付金额
                if(StringUtils.isNotBlank(payId)){
                    sql.append(" left join ab_bill_pay_middle bpm on bpm.BILL_ID = c.id and bpm.PAY_ID = :payId ");
                    sql.append(" left join nis_bill pnb on pnb.bill_Id=l.id and pnb.pay_id=:payId ");
                    params.put("payId", payId);
                }
//		.append(" where ((c.type=:viceType and l.id in (:ids)) or (c.type=:viceType1 and l.lastid in (:ids) and l.brief_id not in ('6007','6008') )) order by l.create_date ");
        sql.append(" where ").append(SqlUtil.getInSqlByParams("ids", "l.id", params)).append(" order by l.create_date ");
        params.put("tcBriefId", AccebillConstant.BRIEF_STATUS_002);
        params.put("tsBriefId", AccebillConstant.BRIEF_STATUS_003);
        params.put("tpBriefId", AccebillConstant.BRIEF_STATUS_004);
        params.put("viceType", AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
        params.put("viceType1", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        params.put("sendSign", AccebillConstant.AB_BILL_SEND_SIGN_Y);
        params.put("tsOptStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("validSign2", AccebillConstant.AB_BILL_VALID_SIGN_2);
        params.put("validSign", new String[]{AccebillConstant.AB_BILL_VALID_SIGN_0, AccebillConstant.AB_BILL_VALID_SIGN_1});
        List<AbVoucherinfoViceVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbVoucherinfoViceVO.class);
        return listInfo;
    }
    /**
     * 根据票据池明细流水IDS查看票据信息
     *
     * @param ids 流水ID
     * @return
     */
    public List<AbVoucherinfoViceVO> listAccebillInfoViewByPoolId(String[] ids,String payId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        sql.append(" select z.id as vid,c.id as vvid,apd.id as id,'' as recid,z.bill_type as billType, z.bill_flag as billFlag,z.payer as payer,")
                .append(" z.acceptor as acceptor,z.orig_no as origNo,z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append(" z.payee as payee,c.inside_sing  as insideSing,apd.CREATE_TIME as applyDate,c.type as type,")
                .append(" '' as reqNm,'' as recNm,'' as recAcctId,c.bank_acc  as bankAcc,c.corp_id as corpId,")
                .append(" apc.CORP_NAME as corpName,apd.opt_status as optStatus,apd.brief_id as briefId,apd.status as busStatus,apd.opt_status as inoptStatus,")
                .append(" '' as source,bt_nvl_char(c.pool_status,'0')  as poolStatus, ap.bp_name as bpName,apd.inout_type as inoutType,apd.LASTID as lastId, api.bif_code as bifCode, ")
                .append(" (case when 1 in (select a.send_sign from ab_accrecords a where a.pd_id=apd.id) then '1' else '0' end ) as sendSign, ")
                .append(" z.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, z.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ")
                .append(" z.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, z.SBBLL_RNG_END_SN AS sbbllRngEndSn, ")
                .append(" z.BL_STD_AMT AS blStdAmt, z.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ")
                .append(" z.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, z.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ")
                .append(" z.BANENDRSMTMK AS banendrsmtmk ");
                if(StringUtils.isNotBlank(payId)){
                    sql.append(" , BT_NVL_NUM(pnb.balance,bpm.PAY_MONEY) payMoney ");
                }
                sql.append(" from ab_pool_dtl apd ")
                .append(" left join AB_VOUCHERINFO_VICE c on apd.vv_id = c.id ")
                .append(" left join AB_VOUCHERINFO z on c.vid = z.id ")
                .append(" left join ab_pool_corp apc on c.corp_id = apc.corp_id  and APC.valid_sign = :validSignPool ")
//                .append(" left join sys_corp sc on apc.corp_id = sc.id ")
                .append(" left join ab_pool ap  on ap.id = apd.bp_id ")
                .append(" left join AB_POOLIF api  on api.id = ap.pif_id ");
                //若是编辑付款单来的查询，需要添加付款单的关联查询，查出相应的支付金额
                if(StringUtils.isNotBlank(payId)){
                    sql.append(" left join ab_bill_pay_middle bpm on bpm.BILL_ID = c.id and bpm.PAY_ID =:payId ");
                    sql.append(" left join nis_bill pnb on pnb.bill_Id=apd.id and pnb.pay_id=:payId ");
                    params.put("payId", payId);
                }
                sql.append(" where  ")
                .append(SqlUtil.getInSqlByParams("ids", "apd.id", params)).append("  ");
        params.put("validSignPool", AccebillConstant.AB_BILL_VALID_SIGN_1);
        List<AbVoucherinfoViceVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbVoucherinfoViceVO.class);
        return listInfo;
    }
    /**
     * 获取挂接授信的票据主表ID集合
     * @return
     */
    public List<String> getBillIdListByRat() {
        return createSQLQuery("select vid AB_VOUCHERINFO_VICE where bear_model = '1'").list();
    }
    /**
     * 根据ids批量修改从表数据
     *
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update AbVoucherinfoVice "
                + " set "
                + "updateDate = :updateDate ");
		if(!params.containsKey("updateDate")){
			params.put("updateDate",new Date());
		}
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("updateName")) {
            hql.append(",updateName = :updateName ");
        }
        if (params.containsKey("boxStatusNew")) {
            hql.append(",boxStatus = :boxStatusNew ");
        }
		if(params.containsKey("boxStatusAll")){
			hql.append(",boxStatus = :boxStatusAll ");
		}
        if (params.containsKey("poolStatus")) {
            hql.append(",poolStatus = :poolStatus ");
        }
		if(params.containsKey("validSign")){
			hql.append(",validSign = :validSign ");
		}
        if (params.containsKey("isNullBatchNo")) {
            hql.append(",batchNo = null ");
        }
		if(params.containsKey("batchNoNew")){
			hql.append(",batchNo = :batchNoNew ");
		}
		if(params.containsKey("payLockMoneyNew")){
			hql.append(",payLockMoney = :payLockMoneyNew ");
		}
		if(params.containsKey("billStatusNew")){
			hql.append(",billStatus = :billStatusNew ");
		}
		if(params.containsKey("ebillBlCrclIndcNew")){
			hql.append(",ebillBlCrclIndc = :ebillBlCrclIndcNew ");
		}
		if(params.containsKey("bailMoneyNew")){
			hql.append(",bailMoney = :bailMoneyNew ");
		}
		if(params.containsKey("bailpercentNew")){
			hql.append(",bailPercent = :bailpercentNew ");
		}
        hql.append(" where ").append(SqlUtil.getInSqlByParams("ids", "id", params));
        if (params.containsKey("boxStatusOld")) {
            hql.append(" and boxStatus=:boxStatusOld ");
        }
		if(params.containsKey("validSignNot")){
			hql.append(" and validSign!=:validSignNot ");
		}
        if (params.containsKey("boxStatusNew")) {
            hql.append(" and boxStatus!=:boxStatusNew ");
        }
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 根据开票登记单号获取从表信息
     *
     * @param appNo 开票登记单号
     * @return AbVoucherinfoVice
     */
    public AbVoucherinfoVice getAbVoucherinfoViceByAppNo(String appNo) {
        String hql = "from AbVoucherinfoVice where appNo=:appNo ";
        Map<String, Object> params = new HashMap<>();
        params.put("appNo", appNo);
        List<AbVoucherinfoVice> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 根据票据号和所属单位ID检查是否已经收入过此票据
     *
     * @param origNo 票据号
     * @param corpId 所属单位
     * @return 已收入返回true 或者 未收入返回false
     */
    public boolean checkPayeeOrigNo(String origNo, String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) num from ab_voucherinfo v ");
        sql.append("inner join ab_voucherinfo_vice vv on v.id=vv.vid ");
        sql.append("where orig_no=:origNo and vv.corp_id=:corpId ");
        Map<String, Object> params = new HashMap<>();
        params.put("origNo", origNo);
        params.put("corpId", corpId);
        @SuppressWarnings("rawtypes")
        Map info = this.getBySQL(sql.toString(), params);
        if (info != null) {
            Object val = info.get("NUM");
            if (val == null) {
                val = info.get("num");
            }
            if (val != null) {
                int num = Integer.parseInt(String.valueOf(val));
                return num > 0;
            }
        }
        return false;
    }
    /**
     * 根据vvid删除票据从表数据
     *
     * @param vvid
     * @return
     */
    public int deleteByVvId(String vvid) {
        Map<String, Object> params = new HashMap<>();
        params.put("vvid", vvid);
        String hql = "DELETE AbVoucherinfoVice where id=:vvid";
        return this.batchExecute(hql, params);
    }
    /**
     * 未挂接担保数据
     *
     * @return
     */
    public DataTablesOutput listGuaranteeDataTable(DataTablesInput input, boolean isEmitMake) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from (select v.id as viceId,i.BILL_FLAG as billFlag,i.BILL_TYPE as billType,i.ORIG_NO as origNo,");
        sql.append(" i.PAYER_BANK_NAME as payerBankName,i.START_DATE as startDate,i.END_DATE as endDate,i.BILL_MONEY as billMoney,i.payee as payee,");
        sql.append(" v.BAIL_MONEY as bailMoney,a.BRIEF_ID as briefId,v.emit_type as emitType,v.emit_guandis_sign as emitGuandisSign,");
        sql.append(" i.PAYER as payer,v.corp_Id as corpId,i.acceptor,i.A_CONTRACT_NO as contractNo ");
        sql.append(" from ab_voucherinfo_vice v inner join ab_voucherinfo i on v.vid=i.id  and (i.OLD_DRAFT_ID is null or i.OLD_DRAFT_ID not like '%-del')  inner join ab_accrecords a on v.id=a.vvid");
//        sql.append(" left join SYS_CORP sc on v.CORP_ID=sc.ID");
        sql.append(" where 1=1 and a.opt_status='19' and a.status=95 and v.type='0' and a.VALID_SIGN=:validSign  and (");
        if (isEmitMake) {
            sql.append(" (a.BRIEF_ID='6101')) ");
        } else {
            sql.append(" (a.SEND_SIGN=0 and a.BRIEF_ID='001' ");
            sql.append(" or (a.SEND_SIGN=1 and a.BRIEF_ID='003')))");
        }
        sql.append(" ) bill");
        params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        return getDynamicBySQL(input, sql.toString(), AbBillPayAndReceVO.class, "bill", true, params);
    }
    /**
     * 根据票据认领id数组修改对应从表信息的票据认领数据
     *
     * @param claimIdArr 票据认领id数组
     */
    public void updateByClaimArr(String[] claimIdArr) {
        Map<String, Object> params = new HashMap<>(2);
        StringBuilder sql = new StringBuilder();
        sql.append(" update ab_voucherinfo_vice set claim_id = null, claim_status = :claimStatus where claim_id in (:claimIdArr) ");
        params.put("claimStatus", AccebillConstant.BILL_CLAIM_STATUS_1);
        params.put("claimIdArr", claimIdArr);
        this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 通过id数组修改票据认领中间表id和状态
     *
     * @param vvidArr     从表id数组
     * @param claimId     中间表id
     * @param claimStatus 认领状态
     */
    public void updateClaimByIds(String[] vvidArr, String claimId, String claimStatus) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder();
        sql.append(" update ab_voucherinfo_vice set claim_id = :claimId ");
        if (!StringUtils.isEmpty(claimStatus)) {
            sql.append(" ,claim_status = :claimStatus ");
            params.put("claimStatus", claimStatus);
        }
        sql.append(" where id in (:vvidArr) ");
        params.put("claimId", claimId);
        params.put("vvidArr", vvidArr);
        this.batchSQLExecute(sql.toString(), params);
    }

    /**
     * 通过id修改传过来的字段，后续需要修改自行添加逻辑
     */
    public int updateFieldById(AbVoucherinfoVODTO voucherinfoVODTO) {
        if(StringUtils.isBlank(voucherinfoVODTO.getId())){
            return 0;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id",voucherinfoVODTO.getId());
        StringBuilder sql = new StringBuilder();
        sql.append(" update ab_voucherinfo_vice set ");
        StringBuilder sql2 = new StringBuilder();
        if(StringUtils.isNotBlank(voucherinfoVODTO.getBatchNo())){
            sql2.append(" batch_no = :batchNo,");
            params.put("batchNo",voucherinfoVODTO.getBatchNo());
        }



        if(StringUtils.isBlank(sql2.toString())){
            return 0;
        }
        if(sql2.toString().endsWith(",")){
            sql2.replace(sql2.length()-1,sql2.length()," ");
        }
        sql.append(sql2);
        sql.append(" where id = :id ");
        return this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 应收补录修改业务单位、业务人员
     *
     * @param abVoucherinfoVice 从表vo
     */
    public void updateBusCorpSupply(AbVoucherinfoViceVO abVoucherinfoVice) {
        Map<String, Object> params = new HashMap<>(8);
        StringBuilder sql = new StringBuilder();
        sql.append(" update ab_voucherinfo_vice set BUS_CORP_ID = :busCorpId, BUS_PERSONNEL = :busPersonnel, BUS_CORP_NAME = :busCorpName, BUS_SOC_CODE =:busSocCode ");
        sql.append(" where id in (:vvidArr) ");
        params.put("busCorpId", abVoucherinfoVice.getBusCorpId());
        params.put("busPersonnel", abVoucherinfoVice.getBusPersonnel());
        params.put("busSocCode", abVoucherinfoVice.getBusSocCode());
        params.put("busCorpName", abVoucherinfoVice.getBusCorpName());
        params.put("vvidArr", abVoucherinfoVice.getIds().split(","));
        this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 修改是否签收补录完成
     *
     * @param vvid         从表id
     * @param isSignSupply 是否签收补录完成
     */
    public void updateIsSignSupply(String vvid, int isSignSupply) {
        String sql = " update ab_voucherinfo_vice set is_sign_supply = :isSignSupply where id = :vvid ";
        Map<String, Object> params = new HashMap<>(4);
        params.put("isSignSupply", isSignSupply);
        params.put("vvid", vvid);
        this.batchSQLExecute(sql, params);
    }
    /**
     * 不得转让撤销业务数据查询
     *
     * @param dataTablesInput 查询
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput nonassignableCancelQuery(DataTablesInput dataTablesInput) {
        Map<String, Object> params = new HashMap<>(32);
        StringBuilder sql = new StringBuilder();
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        sql.append("select info.id as id, info.id as vid, vice.id as vvid, records.id as accid, vice.apply_corp_id as applyCorpId, vice.APPLY_CORP_CODE as applyCorpCode, ");
        sql.append(" vice.apply_corp_name as applyCorpName, vice.corp_id as corpID, vice.CORP_NAME as corpName, ");
        sql.append(" vice.bus_corp_id  as busCorpId, vice.BUS_CORP_NAME as busCorpName, ");
        sql.append(" vice.bus_personnel as busPersonnel , info.bill_flag as billFlag, info.bill_type as billType, info.orig_no as origNo, ");
        sql.append(" info.sbbll_rng_strt_sn as sbbllRngStrtSn, info.sbbll_rng_end_sn as sbbllRngEndSn, info.bl_std_amt as blStdAmt, ");
        sql.append(" info.bill_money as billMoney, info.payer as payer, info.payerbankno as payerBankNo, info.acceptor as acceptor, ");
        sql.append(" info.start_date as startDate, info.end_date as endDate, records.brief_id as briedId, records.send_sign as sendSign, ");
        sql.append(" info.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd, vice.SOC_CODE as socCode ,sc.soc_code as blHoldUnnIdNo, ");
        sql.append(" vice.cn_accrecords as cnAccrecords, records.bill_source as billSource");
        sql.append(" from ab_voucherinfo_vice vice left join ab_voucherinfo info on vice.vid = info.id and (info.OLD_DRAFT_ID is null or info.OLD_DRAFT_ID not like '%-del')  ");
        sql.append(" left join ab_accrecords records on vice.id = records.vvid and  records.status='95' and records.opt_status='19' and records.valid_sign='1'");
        sql.append(" left join sys_corp sc on vice.CORP_ID = sc.ID ");
        sql.append(" where BT_NVL_CHAR(records.BANKENDRSMTMK,info.BANENDRSMTMK)='EM01'  and info.NEW_STM_ISSUOFPPR_IND = '1' and info.bill_status='CS03'");
        sql.append(" and info.ebill_bl_crcl_indc='TF0303' and vice.box_status= '11' ");
        sql.append(" and vice.valid_sign='1' and vice.ENTER_STOCK in ('10','11') and vice.type = '1' " +
                "and ( (records.SEND_SIGN='1' and info.bill_flag ='11'  and records.brief_id in ('9003', '9010')  ) or " +
                "( vice.box_status='11'  and records.brief_id ='6001' and vice.BILL_SOURCE <>'12'))"
        );
        if (formdata.containsKey("bankTypeId")) {
            String bankTypeId = String.valueOf(formdata.get("bankTypeId"));
            if (StringUtils.isNotBlank(bankTypeId)) {
                sql.append(" and corp.id in (select cp.id from SYS_CORP cp left join bt_bank_acc acc on cp.id = acc.CORP_ID ");
                sql.append(" where 1=1 and acc.bank_type_id = :bankTypeId )");
                params.put("bankTypeId", bankTypeId);
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillNonassignableCancelVo.class, "", false, params);
    }
    /**
     * 根据vid 查询从表记录
     *
     * @param：vid
     * @Return
     */
    public List<AbVoucherinfoVice> qeryVoucherinfoViceRecodesByVid(String vid) {
        String hql = "from AbVoucherinfoVice where vid=:vid  ";
        Map<String, Object> params = new HashMap<>();
        params.put("vid", vid);
        List<AbVoucherinfoVice> list = this.listByHQL(hql, params);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list;
    }
    /**
     * 加载审批列表数据
     *
     * @param dataTablesInput 查询条件
     * @param businessList    审批中流水
     * @return 返回列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, List<String> businessList) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select  cords.id as id ,info.id as vid, vice.id as vvid, cords.id as accid, vice.apply_corp_id as applyCorpId, vice.APPLY_CORP_CODE as applyCorpCode,");
        sql.append(" vice.apply_corp_name as applyCorpName, vice.corp_id as corpID, vice.CORP_NAME as corpName, ");
        sql.append(" vice.bus_corp_id  as busCorpId, vice.BUS_CORP_NAME as busCorpName, ");
        sql.append(" vice.bus_personnel as busPersonnel , info.bill_flag as billFlag, info.bill_type as billType, info.orig_no as origNo, ");
        sql.append(" info.sbbll_rng_strt_sn as sbbllRngStrtSn, info.sbbll_rng_end_sn as sbbllRngEndSn, info.bl_std_amt as blStdAmt, ");
        sql.append(" info.bill_money as billMoney, info.payer as payer, info.payerbankno as payerBankNo, info.acceptor as acceptor, ");
        sql.append(" info.start_date as startDate, info.end_date as endDate, cords.brief_id as briedId, cords.send_sign as sendSign, ");
        sql.append(" info.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd, vice.SOC_CODE as socCode , vice.bl_hold_unn_id_no as blHoldUnnIdNo, ");
        sql.append(" vice.cn_accrecords as cnAccrecords, cords.bill_source as billSource");
        sql.append(" from ab_voucherinfo_vice vice");
        sql.append(" left join ab_voucherinfo info on info.id=vice.vid and (info.OLD_DRAFT_ID is null or info.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" left join ab_accrecords cords on vice.id=cords.vvid and cords.status = '11'");
        sql.append(" where 1=1  and  vice.box_status=:boxStatus  and cords.brief_id = :briefId  and cords.opt_status = :OptStatus  ");
        param.put("boxStatus", AccebillConstant.AB_BOX_STATUS_N);
        param.put("briefId", AccebillConstant.BRIEF_STATUS_024);
        param.put("OptStatus", AccebillConstant.AB_BILL_OPT_STATUS15);
        String businessSql = SqlUtil.getInSqlByParams(businessList, "cords.id", param);
        sql.append(" and ").append(businessSql).append(" ");
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), AbBillNonassignableCancelVo.class, "", false, param);
    }
    public DataTablesOutput<GuaContractBillVO> getGuaForBill(DataTablesInput input, List<String> corpIds, List<String> businessIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select vice.app_no as appNo,vice.app_no as billCode,vice.id as id,v.bill_type as billType,v.BILL_FLAG as billFlag,v.orig_no as origNo, ");
        sql.append(" v.bill_money as billMoney,v.Payer as payer,v.Payee as payee,v.acceptor as acceptor,v.start_date as startDate,v.end_date as endDate, ");
        sql.append(" vice.corp_id as corpId ");
        sql.append(" from ab_voucherinfo_vice vice inner join ab_voucherinfo v on vice.vid = v.id ");
        sql.append(" where vice.bear_model = '1'");
        sql.append(" and " + getSQLIn(businessIdList, 900, "vice.id"));
        if (EmptyUtils.isNotEmpty(corpIds)) {
            sql.append(" and " + getSQLIn(corpIds, 900, "vice.corp_id"));
        } else {
            sql.append(" and 1 <> 1 ");
        }
        return getDynamicBySQL(input, sql.toString(), GuaContractBillVO.class, "gco", false);
    }
    public List<GuaContractBillDTO> getGuaForbill() {
        StringBuilder sql = new StringBuilder();
        sql.append("select vice.app_no as appNo, ");
        sql.append(" v.bill_type as billType, ");
        sql.append(" v.BILL_FLAG as billFlag, ");
        sql.append(" v.orig_no as origNo, ");
        sql.append(" v.bill_money as billMoney, ");
        sql.append(" v.Payer as payer, ");
        sql.append(" v.Payee as payee, ");
        sql.append(" v.acceptor as acceptor, ");
        sql.append(" v.start_date as startDate, ");
        sql.append(" v.end_date as endDate, vice.id as id ");
        sql.append(" from ab_voucherinfo_vice vice");
        sql.append(" left join ab_voucherinfo v on vice.vid = v.id ");
        sql.append(" where vice.bear_model= 1 ");
        return this.listBySQLAliasToBean(sql.toString(), null, GuaContractBillDTO.class);
    }
	
	/**
	 * 付款挂接票据锁定金额操作
	 * @param params aid 流水表ID，addMoney增加金额，subtractMoney减少金额
	 */
	public int updatePayLockMoney(Map<String,Object> params) {
		StringBuilder sql = StrUtil.builder(" update ab_voucherinfo_vice set update_date = :updateDate ");
		if(!params.containsKey("updateDate")){
			params.put("updateDate",new Date());
		}
		if(params.containsKey("validSignNew")){
			sql.append(", valid_sign = :validSignNew ");
		}
		if(params.containsKey("addMoney")){
			sql.append(", pay_lock_money = BT_ROUND((BT_NVL_NUM(pay_lock_money,0) + :addMoney),2) ");
		}
		if(params.containsKey("subtractMoney")){
			sql.append(", valid_sign = (case when BT_ROUND((BT_NVL_NUM(pay_lock_money,0) - :subtractMoney),2) > 0 then '0' when BT_ROUND((BT_NVL_NUM(pay_lock_money,0) - :subtractMoney),2) = 0 then '1' else valid_sign end) ");
			sql.append(", pay_lock_money = BT_ROUND((BT_NVL_NUM(pay_lock_money,0) - :subtractMoney),2) ");
		}
		sql.append(" where 1=1 ");
		if(params.containsKey("aid")){
			sql.append(" and id in ( select a.vvid from ab_accrecords a where a.id=:aid ) ");
		}
		if(params.containsKey("vid")){
			sql.append(" and id=:vid ");
		}
		if(params.containsKey("addMoney")){
			sql.append(" and BT_ROUND((BT_NVL_NUM(pay_lock_money,0) + :addMoney),2) <= BT_ROUND((select max(v.bill_money) from ab_voucherinfo v inner join ab_accrecords a on v.id=a.vid where a.id=:aid),2) ");
		}
		if(params.containsKey("subtractMoney")){
			sql.append(" and BT_ROUND((BT_NVL_NUM(pay_lock_money,0) - :subtractMoney),2) >= 0 ");
		}
		return this.batchSQLExecute(sql.toString(), params);
	}

	public List<AbVoucherinfoVice> listByIdList(List<String> ids){
		if(CollectionUtils.isEmpty(ids)){
			return null;
		}
		String hql="from AbVoucherinfoVice where id in(:ids)  ";
		Map<String,Object> params=new HashMap<>();
		params.put("ids", ids);
		List<AbVoucherinfoVice> list=this.listByHQL(hql, params);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list;
	}

	public int updatePayLockMoneyByIdAndMoney(String id, BigDecimal oldMoney, BigDecimal newMoney, String updateBy){
		Map params = new HashMap();
		if(id == null || newMoney == null){
			return 0;
		}
		StringBuilder sql = StrUtil.builder(" update ab_voucherinfo_vice set update_date = :updateDate,pay_lock_money=:newMoney," +
				"update_by=:updateBy where id=:id  ");
		if(oldMoney == null){
			sql.append("and (pay_lock_money is null)");
		}else {
			sql.append("and (pay_lock_money=:oldMoney)");
			params.put("oldMoney",oldMoney);
		}
		params.put("id",id);
		params.put("newMoney",newMoney);
		params.put("updateBy",updateBy);
		params.put("updateDate",new Date());
		return this.batchSQLExecute(sql.toString(), params);
	}

	/**
	* @Description 根据从表id获取票据号
	* @author zhous
	* @date 2024/8/20 16:07
	*/
    public String getOrigNoByVid(String vId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select av.id as id,av.orig_no as origNo  ");
        sql.append("from ab_voucherinfo_vice avv ");
        sql.append("left join ab_voucherinfo av on av.id=avv.vid ");
        sql.append("where avv.id=:vId and av.orig_no is not null ");
        Map<String, Object> params = new HashMap<>();
        params.put("vId", vId);
        List<AbVoucherinfo> list = this.listBySQLAliasToBean(sql.toString(), params, AbVoucherinfo.class);
        if(CollectionUtil.isEmpty(list)){
            return null;
        }
        String origNo = list.get(0).getOrigNo();
        if(StringUtils.isEmpty(origNo)){
            return null;
        }
        return origNo;
    }
}
