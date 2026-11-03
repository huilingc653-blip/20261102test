package com.bytter.report.domain.cms.dao;

import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.report.domain.cms.vo.RatUrationCurVO;
import com.bytter.report.domain.cms.vo.RatBkStatisticSearchVO;
import com.bytter.report.domain.cms.vo.RatBkStatisticVO;
import com.bytter.report.domain.cms.vo.RatBkUseVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.RationConstant;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行授信使用统计dao
 * @author yangl
 *
 */
@Repository
public class RatBkUseStatisticsDao extends BaseDao<RatBkStatisticVO,String>{
	
	/**
	 * 查询表头
	 * @param ratVO
	 * @return
	 */
	public List<RatBkStatisticVO> queryRatBkUseStatisticsHead(RatBkStatisticSearchVO ratVO){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.rit_code as ritCode,t.rit_name as ritName,t.id as ritCount,t.name as ricName  from ( ");
		sql.append(getRatBkUseHeadByCorp(ratVO));
		sql.append(" union ");
		sql.append(getRatBkUseHeadByGroup(ratVO));
		sql.append(" ) t ");
		sql.append("order by t.rit_code ");
		if (!EmptyUtils.isEmpty(ratVO.getRatDate())) {
			params.put("ratDate", ratVO.getRatDate());
		}
		return listBySQLAliasToBean(sql.toString(), params,RatBkStatisticVO.class);
	}
	
	/**
	 * 获取单位品种类型
	 * @param ratVO
	 * @return
	 */
	public String getRatBkUseHeadByCorp(RatBkStatisticSearchVO ratVO){
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct rit.rit_code, rit.rit_name, ric.id,ric.name ");
		sql.append("from RAT_BKRATION rbt ");
		sql.append("inner join RAT_BKRATION_DIS rbd on rbt.id = rbd.rat_code ");
		sql.append("inner join RAT_BKRATION_DIS_DETAIL rbdd on rbdd.dis_code = rbd.id ");
		sql.append("inner join RAT_ITEM_CUSTOM ric on ric.id = rbdd.item_code ");
		sql.append("left join RAT_ITEM_TYPE rit on ric.rit_code = rit.id ");
		sql.append("where 1 = 1 and (rbdd.money + rbdd.super_by_relief_money) > 0 ");
		if (!EmptyUtils.isEmpty(ratVO.getRatDate())) {
			sql.append(" and rbt.begindate <=:ratDate and rbt.enddate >=:ratDate ");
		}
		if (!EmptyUtils.isEmpty(ratVO.getCorpId())) {
			sql.append(" and " + SqlUtil.getInSql(ratVO.getCorpId().split(","), "rbd.corp_id", true));
		}
		sql.append(" and ric.type = 0 and ric.status = 0 ");
		return sql.toString();
	}
	
	/**
	 * 获取集团品种类型
	 * @param ratVO
	 * @return
	 */
	public String getRatBkUseHeadByGroup(RatBkStatisticSearchVO ratVO){
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct rit.rit_code,rit.rit_name,ric.id,ric.name ");
		sql.append("from RAT_BKRATION rbt ");
		sql.append("inner join RAT_BKRATION_DETAIL rbdt on rbt.id=rbdt.rat_code ");
		sql.append("inner join RAT_ITEM_CUSTOM ric on rbdt.item_code=ric.id ");
		sql.append("left join RAT_ITEM_TYPE rit on ric.rit_code = rit.id ");
		sql.append("where 1 = 1 and (rbdt.money+rbdt.super_add_money)>0 ");
		if (!EmptyUtils.isEmpty(ratVO.getRatDate())) {
			sql.append(" and rbt.begindate <=:ratDate and rbt.enddate >=:ratDate ");
		}
		if (!EmptyUtils.isEmpty(ratVO.getCorpId())) {
			sql.append(" and " + SqlUtil.getInSql(ratVO.getCorpId().split(","), "rbd.corp_id", true));
		}
		sql.append(" and ric.type = 0 and ric.status = 0 ");
		return sql.toString();
	}
	
	
	/**
	 * 功能介绍 : 查询单位授信额度及占用情况。<br>
	 * <b>日期：</b>2018年03月22日<br>
	 * @version 1.0
	 * @author yangl 
	 */
	public List<RatBkUseVO> queryCorpRatDisration(RatBkStatisticSearchVO searchVO,List<String> corpAuth) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select rat.corpId as corpId,sc.name as corpName,sc.code as corpNo, rat.ratCode as ratCode,rat.disCode as disCode, ");
		sql.append(" rb.beginDate as beginDate,rb.endDate as endDate,rat.compSign as compSign, ");
		sql.append(" rat.itemFlag as itemFlag,rat.itemCode as itemCode,rat.itemOrder as itemOrder,bb.bank_name as bankName,rb.BANK_CODE as bankCode, ");
		sql.append(" rc.name as itemName,rat.money as money,rat.SUPERPROCESSMONEY as useMoney,rat.freezeMoney as freezeMoney, ");
		sql.append(" rat.addMoney as addMoney,rat.VIRTUALURATION as VIRTUALURATION,rat.VIRTUALRELIEF as VIRTUALRELIEF, ");
		sql.append(" rat.RELIEFANDURATIONMONEY as RELIEFANDURATIONMONEY,rat.maxMONEY as maxMONEY,rat.virtualMaxMoney as virtualMaxMoney, ");
		sql.append(" rat.PROCESSMaxMONEY as useMaxMONEY, rat.freezeMaxMoney as freezeMaxMoney, rat.addMaxMoney as addMaxMoney,");
		sql.append(" rat.distributeway as disWay, rb.rat_mode as ratMode, rb.cur_code as curCode,cur.cur_name as curName, rb.remark  as remark, ");
		sql.append(" bt_nvl(rat.creditType,'1') as creditType,");
		sql.append(" BT_NVL_NUM(rat.bondApply, 0) bondApply,  rat.bondCurIdApply, ");
		sql.append(" BT_NVL_NUM(rat.bondAway, 0) bondAway,rat.bondCurIdAway, ");
		sql.append(" rat.exchangeRateApply ,rat.exchangeRateAway  ");
		sql.append(" from ( ");
		sql.append(" select ratSum.*, ");
		sql.append(" ratBondApply.bondMoney     as bondApply, ");
		sql.append(" ratBondApply.bond_curid as bondCurIdApply,");
		sql.append(" ratBondApply.exchange_rate as exchangeRateApply,");
		sql.append(" ratBondAway.bondMoney      as bondAway, ");
		sql.append(" ratBondAway.bond_curid as bondCurIdAway, ");
		sql.append(" ratBondAway.exchange_rate as exchangeRateAway ");
		sql.append(" from ( ");
		sql.append(" SELECT E.corp_id as corpId, ");
		sql.append("        E.RAT_CODE as ratCode, ");
		sql.append("        E.DIS_CODE as disCode, ");
		sql.append("        SUM(E.MONEY) as money, ");
		sql.append("        SUM(E.SUPERPROCESSMONEY) as SUPERPROCESSMONEY, ");
		sql.append("        SUM(E.freezeMoney) as freezeMoney, ");
		sql.append("        SUM(E.addMoney) as addMoney, ");
		sql.append("        SUM(E.VIRTUALURATION) as VIRTUALURATION , ");
		sql.append("        SUM(E.VIRTUALRELIEF) as VIRTUALRELIEF, ");
		sql.append("        SUM(E.RELIEFANDURATIONMONEY) as RELIEFANDURATIONMONEY, ");
		sql.append("        E.DISTRIBUTE_WAY as DISTRIBUTEWAY, ");
		sql.append("        SUM(E.maxMONEY) as maxmoney, ");
		sql.append("        SUM(E.PROCESSMaxMONEY) as PROCESSMaxMONEY, ");
		sql.append("        SUM(E.freezeMaxMoney) as freezeMaxMoney, ");
		sql.append("        SUM(E.addMaxMoney) as addMaxMoney, ");
		sql.append("        SUM(E.virtualMaxMoney) as virtualMaxMoney, ");
		sql.append("        E.ITEM_CODE as itemCode, ");
		sql.append("        e.item_flag as itemFlag, ");
		sql.append("        e.comp_Sign as compSign, ");
		sql.append("        e.item_order as itemOrder, ");
		sql.append("		E.CREDIT_TYPE as creditType ");
		sql.append("   FROM (SELECT M.corp_id, ");
		sql.append("                M.RAT_CODE, ");
		sql.append("                M.DIS_CODE, ");
		sql.append("                M.MONEY, ");
		sql.append("                N.SUPERPROCESSMONEY, ");
		sql.append("                N.freezeMoney, ");
		sql.append("                N.addMoney, ");
		sql.append("                M.maxMONEY, ");
		sql.append("                N.PROCESSMaxMONEY, ");
		sql.append("                N.freezeMaxMoney, ");
		sql.append("                N.addMaxMoney, ");
		sql.append("                N.virtualMaxMoney, ");
		sql.append("                N.VIRTUALURATION, ");
		sql.append("                N.VIRTUALRELIEF, ");
		sql.append("                N.RELIEFANDURATIONMONEY, ");
		sql.append("                N.ITEM_CODE, ");
		sql.append("                N.item_flag, ");
		sql.append("                N.item_order, ");
		sql.append("                M.comp_Sign, ");
		sql.append("                N.DISTRIBUTE_WAY, ");
		sql.append("				N.CREDIT_TYPE ");
		sql.append("  FROM (SELECT RBD.corp_id,RBD.RAT_CODE,A.dis_code,A.MONEY,A.maxMONEY,A.ITEM_CODE,A.ITEM_FLAG,A.item_order,A.comp_Sign ");
		sql.append("                   FROM RAT_BKRATION_DIS RBD, ");
		sql.append("                        (SELECT RBDD.DIS_CODE, ");
		sql.append("                                RBDD.ITEM_CODE, ");
		sql.append("                                RBDD.ITEM_FLAG, ");
		sql.append("                                RBDD.item_order, ");
		sql.append("                                RBDD.comp_Sign, ");
		sql.append("                                SUM(RBDD.MONEY + RBDD.SUPER_BY_RELIEF_MONEY - ");
		sql.append("                                    RBDD.SUPER_RELIEF_MONEY) MONEY, ");
		sql.append("                                SUM(RBDD.max_MONEY + RBDD.BY_RELIEF_max_MONEY - ");
		sql.append("                                	RBDD.RELIEF_max_MONEY) as  maxMONEY ");
		sql.append("                           FROM RAT_BKRATION_DIS        RBD, ");
		sql.append("                                RAT_BKRATION_DIS_DETAIL RBDD ");
		sql.append("                          WHERE RBDD.DIS_CODE = RBD.id ");
		sql.append("                          GROUP BY RBDD.DIS_CODE, RBDD.ITEM_CODE, RBDD.ITEM_FLAG,RBDD.item_order,RBDD.comp_Sign) A ");
		sql.append("                  WHERE RBD.id = A.DIS_CODE ");
		if (!EmptyUtils.isEmpty(searchVO.getCorpId())) {//单位不为空
			sql.append(" and " + SqlUtil.getInSql(searchVO.getCorpId().split(","), "RBD.corp_id", true));
		}else{
			sql.append(" AND ").append(getSQLIn(corpAuth,900,"RBD.corp_id"));
		}
		sql.append("                    AND RBD.STATUS = 95 ");
		sql.append("                    ) M, ");
		sql.append("                (SELECT RBD.corp_id, ");
		sql.append("                        RBD.RAT_CODE, ");
		sql.append("                        B.SUPERPROCESSMONEY, ");
		sql.append("                        B.freezeMoney, ");
		sql.append("                        case when B.addMoney is null then 0 else B.addMoney end addMoney, ");
		sql.append("                        B.PROCESSMaxMONEY, ");
		sql.append("                        B.freezeMaxMoney, ");
		sql.append("                        case when B.addMaxMoney is null then 0 else B.addMaxMoney end addMaxMoney, ");
		sql.append("                        B.virtualMaxMoney, ");
		sql.append("                        B.VIRTUALURATION, ");
		sql.append("                        B.VIRTUALRELIEF, ");
		sql.append("                        B.RELIEFANDURATIONMONEY, ");
		sql.append("                        RBD.DISTRIBUTE_WAY, ");
		sql.append("                        B.ITEM_CODE, ");
		sql.append("                        b.item_flag, ");
		sql.append("                        b.item_order, ");
		sql.append("						B.CREDIT_TYPE ");
		sql.append("                   FROM RAT_BKRATION_DIS RBD, ");
		sql.append("                        (SELECT RBDD.DIS_CODE, ");
		sql.append("                                RBDD.ITEM_CODE, ");
		sql.append("                                RBDD.ITEM_FLAG, ");
		sql.append("                                RBDD.item_order, ");
		sql.append("								RBDD.CREDIT_TYPE,");
		sql.append("                                SUM(RBDD.VIRTUAL_URATION) AS VIRTUALURATION, ");
		sql.append("                                SUM(RBDD.VIRTUAL_RELIEF_MONEY) AS VIRTUALRELIEF, ");
		sql.append("                                SUM(RBDD.RELIEF_AND_URATION_MONEY) AS RELIEFANDURATIONMONEY, ");
		sql.append("                                SUM(RBDD.SUPER_PROCESS_MONEY) SUPERPROCESSMONEY, ");
		sql.append("                                SUM(FREEZE_MONEY) as freezeMoney, ");
		sql.append("            					SUM(RBDD.PROCESS_max_MONEY) as PROCESSMaxMONEY, ");
		sql.append("            					sum(RBDD.FREEZE_MAX_MONEY) as freezeMaxMoney, ");
		sql.append("            					sum(RBDD.virtual_Max_Money) as virtualMaxMoney, ");
		sql.append("            					SUM(radd.MONEY) as addMoney, ");
		sql.append("            					SUM(radd.max_MONEY) as addMaxMoney ");
		sql.append("                           FROM RAT_BKRATION_DIS RBD ");
		sql.append("                           JOIN RAT_BKRATION_DIS_DETAIL RBDD ");
		sql.append("                             ON RBDD.DIS_CODE = RBD.id ");
		sql.append("                          LEFT JOIN RAT_BKRATION RBK ");
		sql.append("                             ON RBD.RAT_CODE = RBK.id ");
		sql.append("  LEFT JOIN (select RBAA.rat_code,RBAA.corp_id,RBAD.ITEM_CODE,SUM((-1) * RBAD.MONEY) as MONEY, ");
		sql.append("    SUM((-1) * RBAD.max_MONEY) as max_MONEY ");
		sql.append("     from RAT_BKRATION_ADD RBAA ");
		sql.append("     LEFT JOIN RAT_BKRATION_ADD_DETAIL RBAD ");
		sql.append("      ON RBAD.ADD_CODE = RBAA.id ");
		sql.append("     where (RBAD.money < 0 or RBAD.max_money < 0) ");
		sql.append("     AND RBAA.FLAG = :flag ");
		sql.append("     AND 11 <= RBAA.STATUS ");
		sql.append("     AND RBAA.STATUS < 95 ");
		sql.append("     group by RBAA.rat_code,RBAA.corp_id,RBAD.ITEM_CODE) radd ");
		sql.append("     on radd.rat_code = RBK.id and radd.CORP_ID = RBD.CORP_ID ");
		sql.append("     AND radd.ITEM_CODE = RBDD.ITEM_CODE ");
		sql.append("     where RBD.status = 95 ");
		sql.append("                          GROUP BY RBDD.DIS_CODE, ");
		sql.append("                                   RBDD.ITEM_CODE, ");
		sql.append("                                   RBDD.ITEM_Flag, ");
		sql.append("                                   RBDD.ITEM_order, ");
		sql.append("								   RBDD.CREDIT_TYPE ");
		sql.append("                         ) B ");
		sql.append("                  WHERE RBD.id = B.DIS_CODE ");
		if (!EmptyUtils.isEmpty(searchVO.getCorpId())) {
			sql.append(" and " + SqlUtil.getInSql(searchVO.getCorpId().split(","), "RBD.corp_id", true));
		}else{
			sql.append(" AND ").append(getSQLIn(corpAuth,900,"RBD.corp_id"));
		}
		sql.append("                    AND RBD.STATUS = 95) N ");
		sql.append("          WHERE M.RAT_CODE = N.RAT_CODE ");
		sql.append("            AND M.corp_id = N.corp_id ");
		sql.append("            AND M.ITEM_CODE = N.ITEM_CODE) E ");
		sql.append("          GROUP BY E.corp_id, ");
		sql.append("           E.RAT_CODE, ");
		sql.append("           E.DIS_CODE, ");
		sql.append("           E.ITEM_CODE, ");
		sql.append("           E.ITEM_FLAG, ");
		sql.append("           e.item_order, ");
		sql.append("           e.comp_sign, ");
		sql.append("		   E.CREDIT_TYPE,");
		sql.append("           E.DISTRIBUTE_WAY ");
		sql.append(" ) ratSum ");
		//保证金（已执行）授信占用记录的状态如果是占用，则该记录的保证金记录到已执行
		sql.append(" left join ( ");
		sql.append(" select ratBond.rat_code, ");
		sql.append(" ratBond.item_code, ");
		sql.append(" ratBond.dis_code, ");
		sql.append("  ratBond.bond_curid, ");
		sql.append("  ratBond.exchange_rate, ");
		sql.append(" sum(ratBond.bond_money) bondMoney ");
		sql.append(" from (select uration.rat_code, ");//查询授信启用敞口时关联保证金的数据
		sql.append(" uration.item_code, ");
		sql.append(" uration.dis_code, ");
		sql.append(" uration.bond_curid, ");
		sql.append(" uration.exchange_rate, ");
		sql.append(" uration.bond_money ");
		sql.append(" from rat_uration uration ");
		sql.append(" where uration.bill_id in ");
		sql.append(" (select fcbondApply.item_id ");
		sql.append(" from fc_bond_apply fcbondApply ");
		sql.append(" where (fcbondApply.Status > 2 or fcbondApply.Status < 2) ");//保证金状态不为释放状态
		sql.append(" and fcbondApply.Apply_Type = 0) ");//保证金占用类型为外部挂接
		sql.append(" and uration.status = 2 ");//授信为占用状态
		sql.append(" and uration.is_open_rat = 1 ");//授信启用敞口授信
		sql.append(" union all ");
		sql.append(" select uration.rat_code, ");//查询授信启用敞口时手工填写保证金的数据
		sql.append(" uration.item_code, ");
		sql.append(" uration.dis_code, ");
		sql.append(" uration.bond_curid, ");
		sql.append(" uration.exchange_rate, ");
		sql.append(" uration.bond_money ");
		sql.append(" from rat_uration uration ");
		sql.append(" where uration.bill_id not in ");
		sql.append(" (select fcbondApply.item_id ");
		sql.append(" from fc_bond_apply fcbondApply ");
		sql.append(" where (fcbondApply.Status > 2 or fcbondApply.Status < 2) ");//保证金状态不为释放状态
		sql.append(" and fcbondApply.Apply_Type = 0) ");//保证金占用类型为外部挂接
		sql.append(" and uration.status = 2 ");//授信为占用状态
		sql.append(" and uration.is_open_rat = 1 ");//授信启用敞口授信
		sql.append(" ) ratBond ");
		sql.append(" group by ratBond.rat_code, ");
		sql.append(" ratBond.item_code, ");
		sql.append(" ratBond.dis_code, ");
		sql.append(" ratBond.bond_curid, ");
		sql.append(" ratBond.exchange_rate ");
		sql.append(" ) ratBondApply ");
		sql.append(" on ratSum.ratCode = ratBondApply.rat_code ");
		sql.append(" and ratSum.itemCode = ratBondApply.item_code ");
		sql.append(" and ratSum.disCode = ratBondApply.dis_code ");
		//保证金（在途）授信占用记录的状态如果是在审占用，则该记录的保证金记录到在途
		sql.append(" left join ( ");
		sql.append(" select ratBond.rat_code, ");
		sql.append(" ratBond.item_code, ");
		sql.append(" ratBond.dis_code, ");
		sql.append(" ratBond.bond_curid, ");
		sql.append(" ratBond.exchange_rate, ");
		sql.append(" sum(ratBond.bond_money) bondMoney ");
		sql.append(" from (select uration.rat_code, ");//查询授信启用敞口时关联保证金的数据
		sql.append(" uration.item_code, ");
		sql.append(" uration.dis_code, ");
		sql.append(" uration.bond_curid, ");
		sql.append(" uration.exchange_rate, ");
		sql.append(" uration.bond_money ");
		sql.append(" from rat_uration uration ");
		sql.append(" where uration.bill_id in ");
		sql.append(" (select fcbondApply.item_id ");
		sql.append(" from fc_bond_apply fcbondApply ");
		sql.append(" where (fcbondApply.Status > 2 or fcbondApply.Status < 2) ");//保证金状态不为释放状态
		sql.append(" and fcbondApply.Apply_Type = 0) ");//保证金占用类型为外部挂接
		sql.append(" and uration.status = 1 ");//授信为在审占用状态
		sql.append(" and uration.is_open_rat = 1 ");//授信启用敞口授信
		sql.append(" union all ");
		sql.append(" select uration.rat_code, ");//查询授信启用敞口时手工填写保证金的数据
		sql.append(" uration.item_code, ");
		sql.append(" uration.dis_code, ");
		sql.append(" uration.bond_curid, ");
		sql.append(" uration.exchange_rate, ");
		sql.append(" uration.bond_money ");
		sql.append(" from rat_uration uration ");
		sql.append(" where uration.bill_id not in ");
		sql.append(" (select fcbondApply.item_id ");
		sql.append(" from fc_bond_apply fcbondApply ");
		sql.append(" where (fcbondApply.Status > 2 or fcbondApply.Status < 2) ");//保证金状态不为释放状态
		sql.append(" and fcbondApply.Apply_Type = 0) ");//保证金占用类型为外部挂接
		sql.append(" and uration.status = 1 ");//授信为在审占用状态
		sql.append(" and uration.is_open_rat = 1 ");//授信启用敞口授信
		sql.append(" ) ratBond ");
		sql.append(" group by ratBond.rat_code, ");
		sql.append(" ratBond.item_code, ");
		sql.append(" ratBond.dis_code, ");
		sql.append(" ratBond.bond_curid, ");
		sql.append(" ratBond.exchange_rate ");
		sql.append(" ) ratBondAway ");
		sql.append(" on ratSum.ratCode = ratBondAway.rat_code ");
		sql.append(" and ratSum.itemCode = ratBondAway.item_code ");
		sql.append(" and ratSum.disCode = ratBondAway.dis_code ");
		sql.append(" )rat, ");
		sql.append("           sys_corp sc, ");
		sql.append("        bt_bank bb, ");
		sql.append("        rat_bkration rb, ");
		sql.append("        RAT_ITEM_CUSTOM rc, ");
		sql.append("        BT_CURRENCY cur ");
		sql.append("       where  rat.corpid = sc.id ");
		sql.append("       and rat.ratCode = rb.id ");
		sql.append("       and rb.BANK_CODE = bb.sys_bank_code ");
		sql.append("       and rat.itemCode = rc.id ");
		sql.append("       and rb.cur_code = cur.id ");
		if (!EmptyUtils.isEmpty(searchVO.getCorpId())) {
			sql.append(" and " + SqlUtil.getInSql(searchVO.getCorpId().split(","), "rat.corpId", true));
		}else{
			sql.append(" AND ").append(getSQLIn(corpAuth,900,"rat.corpId"));
		}
		if (!EmptyUtils.isEmpty(searchVO.getCurCode()) && !EmptyUtils.isEmpty(searchVO.getCurCode().trim())) {
			sql.append("       and rb.cur_CODE = :curCode ");
			params.put("curCode", searchVO.getCurCode());
		}

		/**
		 * 时间过滤
		 */
		if (!EmptyUtils.isEmpty(searchVO.getBeginDate())) {
			sql.append(" and rb.begindate >=:beginDate ");
			params.put("beginDate",searchVO.getBeginDate());
		}

		if (!EmptyUtils.isEmpty(searchVO.getEndDate())) {
			sql.append(" and rb.begindate <=:endDate ");
			params.put("endDate",searchVO.getEndDate());
		}

		if (!EmptyUtils.isEmpty(searchVO.getBeginDate2())) {
			sql.append(" and rb.enddate >=:beginDate2 ");
			params.put("beginDate2",searchVO.getBeginDate2());
		}

		if (!EmptyUtils.isEmpty(searchVO.getEndDate2())) {
			sql.append(" and rb.enddate <=:endDate2 ");
			params.put("endDate2",searchVO.getEndDate2());
		}

/*		if (!EmptyUtils.isEmpty(searchVO.getRatDate())) {
			sql.append("  and rb.beginDate <= :ratDate and rb.endDate >= :ratDate ");
			params.put("ratDate", searchVO.getRatDate());
		}*/

		if (!EmptyUtils.isEmpty(searchVO.getIsExpire()) && !EmptyUtils.isEmpty(searchVO.getIsExpire().trim())) {
			params.put("sysDate",new Date(System.currentTimeMillis()));
			if (CmsConstant.IS_TRUE.equals(searchVO.getIsExpire())) {
				sql.append(" and rb.endDate >=:sysDate ");
			} else {
				sql.append(" and rb.endDate<:sysDate ");
			}
		}
		if (!EmptyUtils.isEmpty(searchVO.getBankCode())) {
			sql.append(" and rb.BANK_CODE = :bankCode ");
			params.put("bankCode", searchVO.getBankCode());
		}
//		if (!EmptyUtils.isEmpty(searchVO.getRatCode()) && !EmptyUtils.isEmpty(searchVO.getRatCode().trim())) {
//			sql.append(" and rat.ratCode = :ratCode ");
//			params.put("ratCode", searchVO.getRatCode());
//		}
		params.put("flag", RationConstant.RAT_ADD_FLAG_GROUP);//集团追加
		
		sql.append("  order by rat.corpid,rat.ratCode,rat.itemOrder ");

		return listBySQLAliasToBean(sql.toString(), params, RatBkUseVO.class);
	}

	/**
	 * 根据授信编号，分配编号，授信品种标识查询授信占用额度
	 */
	public RatUrationUseDtlVO queryItemApplyMoney(RatBkUseVO bkUseVO){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql=new StringBuilder();
		sql.append(" select ru.ITEM_CODE as itemCode, rc.NAME as itemName,")
				.append(" case when sum(case when  ru.status in(2,4) then ru.MONEY end) is null ")
				.append("  then 0 else ")
				.append(" sum(case when  ru.status in(2,4) then ru.MONEY end) end money , ")
				.append(" case when sum(case when  ru.status in(1) then ru.MONEY end) is null ")
				.append("  then 0 else ")
				.append("sum(case when  ru.status in(1) then ru.MONEY end) end approveMoney,")
				.append(" ru.dis_code as disCode, ru.rat_code as ratCode,")
				.append(" ru.rat_type as ratType ")
				.append(" from RAT_URATION ru ")
				.append(" left join RAT_ITEM_CUSTOM rc on ru.ITEM_CODE = rc.ID where ru.RAT_TYPE = '0'");

		if (EmptyUtils.isNotEmpty(bkUseVO.getItemFlag())) {
			sql.append(" and ru.item_Flag = :itemFlag ");
			params.put("itemFlag",bkUseVO.getItemFlag());
		}
		if (EmptyUtils.isNotEmpty(bkUseVO.getRatCode())) {
			sql.append(" and ru.rat_code = :ratCode ");
			params.put("ratCode", bkUseVO.getRatCode());
		}
		if (EmptyUtils.isNotEmpty(bkUseVO.getDisCode())) {
			sql.append(" and ru.dis_code = :disCode ");
			params.put("disCode", bkUseVO.getDisCode());
		}
		if (EmptyUtils.isNotEmpty(bkUseVO.getItemCode())) {
			sql.append(" and ru.item_code = :itemCode ");
			params.put("itemCode", bkUseVO.getItemCode());
		}
		sql.append("group by ru.item_code,rc.name,ru.dis_code,ru.rat_code,ru.rat_type");
		return getBySQLAliasToBean(sql.toString(), params, RatUrationUseDtlVO.class);
	}

    /**
     * <br>
     * <b>功能：</b> 查询所有占用及在审占用的授信数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/17 0017 <br>
     */
    public List<RatUrationCurVO> queryItemApplyMoney(List<String> ratCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.dis_code as disCode, ru.rat_code as ratCode,ru.item_code as itemCode,ru.status, ");
        sql.append(" ru.cur_code as curCode,ru.init_cur_code as initCurCode,ru.pre_money as billMoney, ");
        sql.append(" ru.bill_id as billId,ru.business_name as sysCode,ru.money as money, ");
        sql.append(" ru.is_open_rat as isOpenRat,ru.bond_curid as bondCurid,ru.bond_money as bondMoney, ");
        sql.append(" ru.item_flag as itemFlag,ru.exchange_rate as exchangeRate ");
        sql.append(" from rat_uration ru ");
        sql.append(" where ru.rat_type = '0' and ru.status in ('1','2','3','4') ");
        sql.append(" and ").append(getSQLIn(ratCodes, 900, "ru.rat_code"));
        sql.append(" and not exists (select 1 from rat_uration_apply ra where ru.bill_id = ra.id) ");
        sql.append(" union all ");
        sql.append(" select ru.dis_code as disCode, ru.rat_code as ratCode,ru.item_code as itemCode,ru.status, ");
        sql.append(" ru.cur_code as curCode,ra.init_cur_code as initCurCode,ru.bill_money as billMoney, ");
        sql.append(" ru.bill_id as billId,ru.business_name as sysCode,ru.money as money, ");
        sql.append(" ru.is_open_rat as isOpenRat,ru.bond_curid as bondCurid,ru.bond_money as bondMoney, ");
        sql.append(" ru.item_flag as itemFlag,ru.exchange_rate as exchangeRate ");
        sql.append(" from rat_uration ru ,rat_uration_apply ra ");
        sql.append(" where ru.bill_id = ra.id and ru.rat_type = '0' and ru.status in ('1','2','3','4') ");
        sql.append(" and ").append(getSQLIn(ratCodes, 900, "ru.rat_code"));
        //sql.append(" and ").append(getSQLIn(disCodes, 900, "ru.dis_code"));
        return listBySQLAliasToBean(sql.toString(), params, RatUrationCurVO.class);
    }
}
