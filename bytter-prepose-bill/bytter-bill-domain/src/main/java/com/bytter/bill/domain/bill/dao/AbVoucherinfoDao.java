package com.bytter.bill.domain.bill.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
* @Title: Dao
* @Description: 票据主信息
* @author onlineGenerator
* @date 2017-06-21 15:50:47
* @version 1.0
*
*/
@Repository
public class AbVoucherinfoDao extends BaseDao<AbVoucherinfo,String>{
	
	/**
	 * 根据ids批量修改主表数据
	 * @param params
	 * @return
	 */
	public int batchUpdate(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update AbVoucherinfo "
				+ " set updateDate = :updateDate ");
		if(!params.containsKey("updateDate")){
			params.put("updateDate",new Date());
		}
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName ");
		}
		if(params.containsKey("billStatus")){
			hql.append(",billStatus = :billStatus ");
		}
		if(params.containsKey("ebillBlCrclIndc")){
			hql.append(",ebillBlCrclIndc = :ebillBlCrclIndc ");
		}
		if(params.containsKey("boxStatusNew")){
			hql.append(",boxStatus = :boxStatusNew ");		
		}
		if(params.containsKey("billMoneyAdd")){
			hql.append(",billMoney = billMoney + :billMoneyAdd ");
		}
		if(params.containsKey("billMoneySubtract")){
			hql.append(",billMoney = billMoney - :billMoneySubtract ");
		}
		if(params.containsKey("billMoneyNew")){
			hql.append(",billMoney = :billMoneyNew ");
		}
		if(params.containsKey("sbbllRngStrtSnNew")){
			hql.append(",sbbllRngStrtSn = :sbbllRngStrtSnNew ");
		}
		if(params.containsKey("sbbllRngEndSnNew")){
			hql.append(",sbbllRngEndSn = :sbbllRngEndSnNew ");
		}
		if(params.containsKey("orgSbbllRngStrtSnNew")){
			hql.append(",orgSbbllRngStrtSn = :orgSbbllRngStrtSnNew ");
		}
		if(params.containsKey("orgSbbllRngEndSnNew")){
			hql.append(",orgSbbllRngEndSn = :orgSbbllRngEndSnNew ");
		}
//		hql.append(" where ).append(SqlUtil.getInSqlByParams("ids","id",params)");
		hql.append(" where ").append(SqlUtil.getInSqlByParams("ids","id",params));
		if(params.containsKey("boxStatusOld")){
			hql.append(" and boxStatus=:boxStatusOld ");		
		}
		if(params.containsKey("boxStatusNew")){
			hql.append(" and (boxStatus!=:boxStatusNew or boxStatus is null) ");
		}
		if(params.containsKey("billStatus")){
			hql.append(" and (billStatus!=:billStatus or billStatus is null) ");
		}

		if(params.containsKey("sbBllRngEndSN")){
			hql.append(" and sbbllRngStrtSn!=:sbBllRngEndSN ");
		}
		if(params.containsKey("sbBllRngEndSN")){
			hql.append(" and sbbllRngEndSn!=:sbBllRngEndSN ");
		}
		if(params.containsKey("sbbllRngStrtSnOld")){
			hql.append(" and sbbllRngStrtSn=:sbbllRngStrtSnOld ");
		}
		if(params.containsKey("sbbllRngEndSnOld")){
			hql.append(" and sbbllRngEndSn=:sbbllRngEndSnOld ");
		}
		if(params.containsKey("orgSbbllRngStrtSnOld")){
			hql.append(" and orgSbbllRngStrtSn=:orgSbbllRngStrtSnOld ");
		}
		if(params.containsKey("orgSbbllRngEndSnOld")){
			hql.append(" and orgSbbllRngEndSn=:orgSbbllRngEndSnOld ");
		}
		if(params.containsKey("origNo")){
			hql.append(" and origNo!=:origNo ");
		}
		if(params.containsKey("billMoneyOld")){
			hql.append(" and billMoney=:billMoneyOld ");
		}
		return this.batchExecute(hql.toString(), params);
	}
	
	/**
	 * 根据票据号获取票据信息
	 * @param origNo
	 * @return
	 */
	public AbVoucherinfo getAbVoucherinfoByOrigNo(String origNo, String sbbllRngStrtSn, String sbbllRngEndSn){
//		String hql="from AbVoucherinfo where origNo=:origNo and billStatus!='000002'";
		String hql="from AbVoucherinfo where origNo=:origNo";
		String hqlEnd = " order by createDate desc ";
		Map<String,Object> params=new HashMap<>();
		if(sbbllRngStrtSn != null && sbbllRngEndSn != null && (sbbllRngStrtSn + sbbllRngEndSn).length() == 24){
			hql = hql + " and sbbllRngStrtSn=:sbbllRngStrtSn and sbbllRngEndSn=:sbbllRngEndSn";
			params.put("sbbllRngStrtSn", sbbllRngStrtSn);
			params.put("sbbllRngEndSn", sbbllRngEndSn);
		}
		hql = hql + hqlEnd;
		params.put("origNo", origNo);
		List<AbVoucherinfo> list=this.listByHQL(hql, params);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 根据票据号获取最大区间号的票据信息
	 * @param origNo
	 * @return
	 */
	public AbVoucherinfo getAbVoucherinfoByOrigNoAndRangSn(String origNo, String sbbllRngStrtSn, String sbbllRngEndSn){
//		String hql="from AbVoucherinfo where origNo=:origNo and billStatus!='000002'";
		String hql="from AbVoucherinfo where origNo=:origNo";
		String hqlEnd = " order by createDate ";
		Map<String,Object> params=new HashMap<>();
		if(sbbllRngStrtSn != null && sbbllRngEndSn != null && (sbbllRngStrtSn + sbbllRngEndSn).length() == 24){
			hql = hql + " and sbbllRngStrtSn<=:sbbllRngStrtSn and sbbllRngEndSn>=:sbbllRngEndSn";
			params.put("sbbllRngStrtSn", sbbllRngStrtSn);
			params.put("sbbllRngEndSn", sbbllRngEndSn);
		}
		hql = hql + hqlEnd;
		params.put("origNo", origNo);
		List<AbVoucherinfo> list=this.listByHQL(hql, params);
		if(list == null || list.size() == 0){
			return null;
		}
		if(list!=null&&!list.isEmpty() && list.size() == 1){
			return list.get(0);
		}
		int maxValIndex = 0;
		String endNoStr = "0";
		for(int i = 0; i < list.size(); i++){
			AbVoucherinfo info = list.get(0);
			if("0".equals(info.getNewStmIssuofpprInd()) || "0".equals(info.getBlgrdbagcrclPrmtInd())){
				continue;
			}
			Long infoEndNoStr = Long.valueOf(info.getSbbllRngEndSn());
			if(infoEndNoStr > Long.valueOf(endNoStr)){
				maxValIndex = i;
				endNoStr = info.getSbbllRngEndSn();
			}
		}
		return list.get(maxValIndex);
	}

	/**
	 * 根据原票据ID获取拆分明细数据
	 * @param oldDraftId 原票据ID
	 * @return
	 */
	public List<AbVoucherinfo> listByOldDraftId(String oldDraftId){
		if(StrUtil.isEmpty(oldDraftId)){
			return Collections.emptyList();
		}
		Map<String,Object> params=new HashMap<>();
		StringBuilder hql= new StringBuilder(100);
		hql.append("select vv.vid from AB_VOUCHERINFO_VICE vv where vv.OLD_DRAFT_ID =:oldDraftId and vv.valid_Sign != 3");
		params.put("oldDraftId",oldDraftId);

		List<String> idList = createSQLQuery(hql.toString(), params).list();
		if(CollectionUtil.isEmpty(idList)){
			return Collections.emptyList();
		}
		params.put("idList",idList);

		hql= new StringBuilder(100);
		hql.append(" from AbVoucherinfo v where v.id in (:idList) ");
		List<AbVoucherinfo> list=this.listByHQL(hql.toString(), params);
		return list;
	}
	
	/**
	 * 票据号重复验证
	 * @param id     票据从表信息id，不为空时用于修改时票据号校验，非修改校验则该参数为空
	 * @return 存在数据返回true否则false
	 */
	public boolean checkOrigNo(Map<String,Object> params, String id){
		StringBuilder sql=new StringBuilder("select a.id as id from AB_VOUCHERINFO_VICE b left join AB_VOUCHERINFO a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del')  ");


		//判断区分应付和应收票据
		if(StringUtils.isNotEmpty(params) && params.containsKey("viceType")){
			// 因为收票登记tab页面中的暂存数据 点击编辑后，在修改页面上的校验不应该通过 and b.type = 'update' 作为条件
			if (!AccebillConstant.BILL_OPERATE_UPDATE.equals(params.get("viceType"))){
				sql.append(" and b.type = :viceType ");
			}
			 //针对应收票据进行判断是否还有持有票据 (慢sql)
			/*if(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(params.get("viceType"))){
				sql.append(" and b.corp_id=:corpId and exists( select 1 from ab_accrecords a1  ");
				sql.append(" left join ab_accrecords b1 on a1.id=b1.lastid and b1.status not in (-2) and b1.opt_status not in ('-3','-4') ");
				sql.append(" left join ab_accrecords c1 on b1.id=c1.lastid and c1.status not in (-2) and c1.brief_id='011' and c1.opt_status not in ('-3','-4') ");
				sql.append(" where a1.vvid=b.id and a1.brief_id in ('6001','9003','9010','9012','9018','9019','6019','6212') and a1.status!=-2 ");
				sql.append(" and ( (a1.status !=-2 and a1.valid_sign='0') or a1.valid_sign='1' or (b1.id is not null and b1.opt_status!='19') or (c1.id is not null and c1.opt_status!='19') ) ");
				sql.append(" ) ");
			}*/
			if(AccebillConstant.BILL_OPERATE_UPDATE.equals(params.get("viceType"))){
				sql.append(" and b.corp_id=:corpId ");
				sql.append(" left join ab_accrecords a1 on a1.vvid=b.id and a1.vvid=b.id and a1.brief_id in ('6001','9003','9010','9012','9018','9019','6019','6212') and a1.status!=-2 ");
				sql.append(" left join ab_accrecords b1 on a1.id=b1.lastid and b1.status not in (-2) and b1.opt_status not in ('-3','-4') ");
				sql.append(" left join ab_accrecords c1 on b1.id=c1.lastid and c1.status not in (-2) and c1.brief_id='011' and c1.opt_status not in ('-3','-4') ");
				//sql.append(" and ( (a1.status !=-2 and a1.valid_sign='0') or a1.valid_sign='1' or (b1.id is not null and b1.opt_status!='19') or (c1.id is not null and c1.opt_status!='19') ) ");
			}
		}

		sql.append(" where b.valid_sign=:valid_sign and a.orig_no=:origNo ");
		if(StringUtils.isNotEmpty(params) && AccebillConstant.BILL_OPERATE_UPDATE.equals(params.get("viceType"))){
			sql.append(" and ( (a1.status !=-2 and a1.valid_sign='0') or a1.valid_sign='1' or (b1.id is not null and b1.opt_status!='19') or (c1.id is not null and c1.opt_status!='19') ) ");
		}
		if(params.get("sbbllRngStrtSn") != null && params.get("sbbllRngEndSn") != null
				&& (params.get("sbbllRngStrtSn").toString() + params.get("sbbllRngEndSn").toString()).length() == 24){
			sql.append(" and a.SBBLL_RNG_STRT_SN=:sbbllRngStrtSn and a.SBBLL_RNG_END_SN=:sbbllRngEndSn ");
		}
		params.put("valid_sign", "1");
		if(StringUtils.isNotEmpty(id)){
			sql.append(" and b.id <> :notId ");
			params.put("notId", id);
		}
	
		
		List<AbVoucherinfo> list=this.listBySQLAliasToBean(sql.toString(), params, AbVoucherinfo.class);
		return list != null && list.size() > 0;
	}
	
	/**
	* 更新
	* @param entity
	*/
	public void update(AbVoucherinfo entity){
		super.update(entity);
		this.flush();
	}
	/**
	 * 检查此票据新是否允许修改 如果已有单位收入库存且审批通过以后吃票据信息不允许修改
	 * @param params {出票登记、收票登记、收票签收、背书签收、质押签收} status=95 vid=票据主信息ID
	 * @return 不允许修改访问false 可以修改返回true
	 */
	public boolean checkIsUpdateBillInfo(Map<String,Object> params){
		StringBuilder sql=new StringBuilder("select a.id as id from AB_VOUCHERINFO a ");
		sql.append(" left join AB_ACCRECORDS b on a.id = b.vid  ");
		sql.append(" where b.BRIEF_ID in (:briefIds) and a.id=:vid and b.status>=:status ");
		List<AbVoucherinfo> list=this.listBySQLAliasToBean(sql.toString(), params, AbVoucherinfo.class);
		return list == null || list.size() <= 0;
	}
	/**
	 * 根据票据号修改信用等级信息
	 *
	 * @param origNo            票据号
	 * @param payerCdtRatgDuedt 评级到期日
	 * @param payerCdtRatgGgcy  评级机构
	 * @param payerCdtRatgs     信用等级
	 */
    public void updatePayerCdtByOrigNo(String origNo, Date payerCdtRatgDuedt, String payerCdtRatgGgcy,
									   String payerCdtRatgs,String sbbllRngStrtSn, String sbbllRngEndSn) {
		HashMap<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder(" update AB_VOUCHERINFO set ORIG_NO = :origNo ");
    	if (payerCdtRatgDuedt != null) {
    		sql.append(" , PAYER_CDT_RATG_DUEDT = :payerCdtRatgDuedt ");
			params.put("payerCdtRatgDuedt", payerCdtRatgDuedt);
		}
		if (StringUtil.isNotEmpty(payerCdtRatgGgcy)) {
			sql.append(" , PAYER_CDT_RATG_AGCY = :payerCdtRatgGgcy ");
			params.put("payerCdtRatgGgcy", payerCdtRatgGgcy);
		}
		if (StringUtil.isNotEmpty(payerCdtRatgGgcy)) {
			sql.append(" , PAYER_CDT_RATGS = :payerCdtRatgs ");
			params.put("payerCdtRatgs", payerCdtRatgs);
		}
		sql.append("  where ORIG_NO = :origNo ");
		params.put("origNo", origNo);
		sql.append("  and sbbll_Rng_Strt_Sn = :sbbllRngStrtSn ");
		params.put("sbbllRngStrtSn", sbbllRngStrtSn);
		sql.append("  and sbbll_Rng_End_Sn = :sbbllRngEndSn ");
		params.put("sbbllRngEndSn", sbbllRngEndSn);

		this.batchSQLExecute(sql.toString(), params);
	}
}
