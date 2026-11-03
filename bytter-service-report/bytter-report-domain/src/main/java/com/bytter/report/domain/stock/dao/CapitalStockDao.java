package com.bytter.report.domain.stock.dao;
import com.bytter.report.api.constant.CapitalStockConstant;
import com.bytter.report.domain.stock.vo.BisAccDtlVO;
import com.bytter.report.domain.stock.vo.BtBankAccFreezeVO;
import com.bytter.report.domain.stock.vo.CapitalStockVO;
import com.bytter.report.domain.stock.vo.CapitalStockWidgetVO;
// import com.bytter.biz.netbank.spi.dto.BisAccBalDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BisAccBalDTO;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 资金存量报表Dao
 *
 * @author zuoy 2017-10-25
 */
@Repository
public class CapitalStockDao extends BaseDao<CapitalStockVO, String> {
	/**
	 * 获取网点查询列表
	 *
	 * @param dataTablesInput
	 * @param billSql         票据查询SQL
	 * @param beginDate       查询开始日期
	 * @param endDate         查询结束日期
	 * @param curId           币别ID
	 * @return
	 */
	public DataTablesOutput getNetDataTables(DataTablesInput dataTablesInput, String billSql,String netId, Date beginDate, Date endDate, String curId, String stockType, Date balDate, String joinSql) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		if (!CapitalStockConstant.STOCK_TYPE_BILL.equalsIgnoreCase(stockType)) {
			sql.append(" select BT_NVL_CHAR(t.net_id, t1.net_id) netId, '1' stockType,BT_NVL_NUM(beginBal, 0) beginBal, BT_NVL_NUM(inAmt, 0) inAmt, BT_NVL_NUM(outAmt, 0) outAmt, ");
			sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0), 0) endBal, BT_NVL_NUM(freezeMoney, 0) freezeMoney, ");
			sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0) - BT_NVL_NUM(freezeMoney, 0), 0) availBal  from ( ");
			sql.append(" (select c.net_id,sum(b.avail_bal) as beginBal from bis_acc_bal b left join sys_corp c on b.corp_id = c.id ");
			sql.append(" where b.bal_date >= :beginDate and b.bal_date <= :endDate");
			if(EmptyUtils.isNotEmpty(netId)){
				sql.append(" and c.net_id = :netId ");
				params.put("netId", netId);
			}
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and b.cur_id = :curId");
				params.put("curId", curId);
			}
			if (StringUtils.isNotEmpty(joinSql)) {
				sql.append(" and "+joinSql);
			}
			sql.append(" group by c.net_id) t ");
			sql.append(" left join ");
			sql.append(" (select c.net_id, sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '02' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '11' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '0' then BT_NVL_NUM(d.amt,0) else 0 end) as inAmt, ");
			sql.append(" sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '01' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '12' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '1' then BT_NVL_NUM(d.amt,0) else 0 end) as outAmt ");
			sql.append(" from bis_acc_dtl d left join bt_bank_acc acc on d.bank_acc = acc.bank_acc left join sys_corp c on acc.corp_id = c.id");
			sql.append(" where d.trans_time >= :nisBeginDate and d.trans_time <= :nisEndDate ");
			if(EmptyUtils.isNotEmpty(netId)){
				sql.append(" and c.net_id = :netId ");
				params.put("netId", netId);
			}
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and d.cur_id = :curId");
			}
			if (StringUtils.isNotEmpty(joinSql)) {
				sql.append(" and "+joinSql);
			}
			sql.append(" group by c.net_id ) t1 ");
			sql.append(" on t.net_id = t1.net_id ");
			sql.append(" left join (select c.net_id,sum(b.frz_bal) as freezeMoney from bis_acc_bal b left join sys_corp c on b.corp_id = c.id ");
			sql.append(" where b.bal_date >= :nisBeginDate and b.bal_date <= :nisEndDate ");
			if(EmptyUtils.isNotEmpty(netId)){
				sql.append(" and c.net_id = :netId ");
				params.put("netId", netId);
			}
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and b.cur_id = :curId");
				params.put("curId", curId);
			}
			if (StringUtils.isNotEmpty(joinSql)) {
				sql.append(" and "+joinSql);
			}
			sql.append(" group by c.net_id) t2  on t.net_id = t2.net_id ) ");
			// 银行流水开始结束时间
			params.put("nisBeginDate", beginDate);
			params.put("nisEndDate", endDate);
		}
		params.put("netId", netId);
		params.put("curId", curId);
		params.put("balDate", balDate);
		// 票据开始结束时间
		params.put("beginDate", DateUtils.parseDate(beginDate));
		params.put("endDate",  DateUtils.parseDate(endDate));
		params.put("endDateAddOne", DateUtils.addDay(DateUtils.getDateYMD(endDate),1));
		params.put("curdate", new java.sql.Date(System.currentTimeMillis()));
		if (!EmptyUtils.isBlank(billSql)) {
			if (EmptyUtils.isBlank(stockType)) {
				sql.append(" union all  ");
			}
			sql.append(billSql);
		}
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, CapitalStockVO.class);
	}
	/**
	 * 获取单位查询列表
	 *
	 * @param dataTablesInput
	 * @param billSql         票据查询SQL
	 * @param netId           网点ID
	 * @param beginDate       查询开始日期
	 * @param endDate         查询结束日期
	 * @param curId           币别ID
	 * @return
	 */
	public DataTablesOutput getCorpDataTables(DataTablesInput dataTablesInput, String billSql, String netId, Date beginDate, Date endDate, String curId, String stockType, Date balDate, String joinSql) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		if (!CapitalStockConstant.STOCK_TYPE_BILL.equalsIgnoreCase(stockType)) {
			sql.append(" select BT_NVL_CHAR(t.corp_id, t1.corp_id) corpId,'1' stockType, BT_NVL_NUM(beginBal, 0) beginBal, BT_NVL_NUM(inAmt, 0) inAmt, BT_NVL_NUM(outAmt, 0) as outAmt, ");
			sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0), 0) endBal, BT_NVL_NUM(freezeMoney, 0) as freezeMoney, ");
			sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0) - BT_NVL_NUM(freezeMoney, 0), 0) as availBal  from ( ");
			sql.append(" (select b.corp_id, sum(b.avail_bal) as beginBal from bis_acc_bal b left join sys_corp c on b.corp_id = c.id ");
			sql.append(" where b.bal_date >= :beginDate and b.bal_date <= :endDate and c.net_id = :netId ");
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and b.cur_id = :curId");
				params.put("curId", curId);
			}
			if (StringUtils.isNotEmpty(joinSql)) {
				sql.append(" and "+joinSql);
			}
			sql.append(" group by b.corp_id) t ");
			sql.append(" left join ");
			sql.append(" (select acc.corp_id, sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '02' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '11' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '0' then BT_NVL_NUM(d.amt,0) else 0 end) as inAmt, ");
			sql.append(" sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '01' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '12' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '1' then BT_NVL_NUM(d.amt,0) else 0 end) as outAmt ");
			sql.append(" from bis_acc_dtl d left join bt_bank_acc acc on d.bank_acc = acc.bank_acc left join sys_corp c on acc.corp_id = c.id ");
			sql.append(" where d.trans_time >= :nisBeginDate and d.trans_time <= :nisEndDate ");
			sql.append(" and c.net_id = :netId ");
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and d.cur_id = :curId");
			}
			if (StringUtils.isNotEmpty(joinSql)) {
				sql.append(" and "+joinSql);
			}
			sql.append(" group by acc.corp_id ) t1 ");
			sql.append(" on t.corp_id = t1.corp_id ");
			sql.append(" left join (select b.corp_id, sum(b.frz_bal) as freezeMoney from bis_acc_bal b left join sys_corp c on b.corp_id = c.id ");
			sql.append(" where b.bal_date >= :nisBeginDate and b.bal_date <= :nisEndDate and c.net_id = :netId ");
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and b.cur_id = :curId");
				params.put("curId", curId);
			}
			if (StringUtils.isNotEmpty(joinSql)) {
				sql.append(" and "+joinSql);
			}
			sql.append(" group by b.corp_id) t2  on t.corp_id = t2.corp_id  ) ");
			// 银行流水开始结束时间
			params.put("nisBeginDate", beginDate);
			params.put("nisEndDate", endDate);
		}
		params.put("netId", netId);
		params.put("balDate", balDate);
		// 票据开始结束时间
		params.put("beginDate", DateUtils.parseDate(beginDate));
		params.put("endDate",  DateUtils.parseDate(endDate));
		params.put("endDateAddOne", DateUtils.addDay(DateUtils.getDateYMD(endDate),1));
		params.put("curdate", new java.sql.Date(System.currentTimeMillis()));
		if (!EmptyUtils.isBlank(billSql)) {
			if (EmptyUtils.isBlank(stockType)) {
				sql.append(" union all  ");
			}
			sql.append(billSql);
		}
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, CapitalStockVO.class);
	}
	/**
	 * 获取银行查询列表
	 *
	 * @param dataTablesInput
	 * @param billSql         票据查询SQL
	 * @param corpId          网点ID
	 * @param beginDate       查询开始日期
	 * @param endDate         查询结束日期
	 * @param curId           币别ID
	 * @return
	 */
	public DataTablesOutput getBankTypeDataTables(DataTablesInput dataTablesInput, String billSql, String corpId, Date beginDate, Date endDate, String curId, String stockType, Date balDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		if (!CapitalStockConstant.STOCK_TYPE_BILL.equalsIgnoreCase(stockType)) {
			sql.append(" select BT_NVL_CHAR(t.bank_type_id, t1.bank_type_id) bankTypeId, BT_NVL_CHAR(t.Type_Name, t1.Type_Name) bankTypeName, '1' stockType, ");
			sql.append(" BT_NVL_NUM(beginBal, 0) beginBal,BT_NVL_NUM(inAmt, 0) inAmt,BT_NVL_NUM(outAmt, 0) outAmt, ");
			sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0), 0) endBal, BT_NVL_NUM(freezeMoney, 0) freezeMoney, ");
			sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0) - BT_NVL_NUM(freezeMoney, 0), 0) availBal  from ( ");
			sql.append(" (select acc.bank_type_id, bankType.Type_Name, sum(b.avail_bal) beginBal from bis_acc_bal b ");
			sql.append(" left join bt_bank_acc acc on b.bank_acc = acc.bank_acc left join bt_bank_type bankType on bankType.id = acc.bank_type_id ");
			sql.append(" where b.bal_date >= :beginDate and b.bal_date <= :endDate and  b.corp_id = :corpId ");
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and b.cur_id = :curId");
				params.put("curId", curId);
			}
			sql.append(" group by acc.bank_type_id, bankType.Type_Name) t ");
			sql.append(" left join ");
			sql.append(" (select acc.bank_type_id, bankType.Type_Name, sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '02' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '11' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '0' then BT_NVL_NUM(d.amt,0) else 0 end) as inAmt, ");
			sql.append(" sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '01' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '12' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '1' then BT_NVL_NUM(d.amt,0) else 0 end) as outAmt ");
			sql.append(" from bis_acc_dtl d left join bt_bank_acc acc on d.bank_acc = acc.bank_acc left join bt_bank_type bankType on bankType.id = acc.bank_type_id ");
			sql.append(" where d.trans_time >= :nisBeginDate and d.trans_time <= :nisEndDate ");
			sql.append(" and acc.corp_id = :corpId ");
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and d.cur_id = :curId");
			}
			sql.append(" group by acc.bank_type_id, bankType.Type_Name) t1 ");
			sql.append(" on t.bank_type_id = t1.bank_type_id  ");
			sql.append(" left join (select acc.bank_type_id, bankType.Type_Name, sum(b.frz_bal) freezeMoney from bis_acc_bal b ");
			sql.append(" left join bt_bank_acc acc on b.bank_acc = acc.bank_acc left join bt_bank_type bankType on bankType.id = acc.bank_type_id ");
			sql.append(" where b.bal_date >= :nisBeginDate and b.bal_date <= :nisEndDate and  b.corp_id = :corpId ");
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and b.cur_id = :curId");
				params.put("curId", curId);
			}
			sql.append(" group by acc.bank_type_id, bankType.Type_Name) t2  on t.bank_type_id = t2.bank_type_id  ) ");
			// 银行流水开始结束时间
			params.put("nisBeginDate", beginDate);
			params.put("nisEndDate", endDate);
		}
		params.put("corpId", corpId);
		params.put("balDate", balDate);
		// 票据开始结束时间
		params.put("beginDate", DateUtils.parseDate(beginDate));
		params.put("endDate",  DateUtils.parseDate(endDate));
		params.put("endDateAddOne", DateUtils.addDay(DateUtils.getDateYMD(endDate),1));
		params.put("curdate", new java.sql.Date(System.currentTimeMillis()));
		if (!EmptyUtils.isBlank(billSql)) {
			if (EmptyUtils.isBlank(stockType)) {
				sql.append(" union all  ");
			}
			sql.append(billSql);
		}
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, CapitalStockVO.class);
	}
	/**
	 * 获取账户流水查询列表
	 *
	 * @param dataTablesInput
	 * @param bankTypeId      银行类型ID
	 * @param beginDate       查询开始日期
	 * @param endDate         查询结束日期
	 * @param curId           币别ID
	 * @return
	 */
	public DataTablesOutput getDtlDataTables(DataTablesInput dataTablesInput, String bankTypeId, Date beginDate, Date endDate, String curId, String corpId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select dtl.trans_time, acc.corp_id, acc.bank_type_id, bankType.type_name bankTypeName, dtl.cur_id, dtl.bank_acc, dtl.acc_name, dtl.bank_name, dtl.amt, ");
		sql.append(" dtl.bal, dtl.cd_sign, dtl.rb_sign, dtl.opp_acc_no, dtl.opp_acc_name, dtl.opp_acc_bank, acc.is_online from bis_acc_dtl dtl ");
		sql.append(" left join bt_bank_acc acc on dtl.bank_acc = acc.bank_acc  left join bt_bank_type bankType on bankType.id = acc.bank_type_id ");
		sql.append(" where bankType.id = :bankTypeId ");
		params.put("bankTypeId", bankTypeId);
		sql.append(" and dtl.trans_time >= :beginDate and dtl.trans_time <= :endDate ");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and dtl.cur_id = :curId ");
			params.put("curId", curId);
		}
		if (!EmptyUtils.isBlank(corpId)) {
			sql.append(" and acc.corp_id = :corpId ");
			params.put("corpId", corpId);
		}
		sql.append(" order by dtl.trans_time desc ");
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, BisAccDtlVO.class);
	}
	/**
	 * 获取账户冻结查询列表
	 *
	 * @param dataTablesInput
	 * @param bankTypeId      银行类型
	 * @param beginDate       查询开始日期
	 * @param endDate         查询结束日期
	 * @param curId           币别ID
	 * @return
	 */
	public DataTablesOutput getFreezeDataTables(DataTablesInput dataTablesInput, String bankTypeId, Date beginDate, Date endDate, String curId, String corpId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select f.corp_id, f.cur_id, acc.bank_type_id, bankType.type_name as bankTypeName, f.bank_acc, acc.acc_name, acc.bank_name, ");
		sql.append(" (f.freeze_money - BT_NVL_NUM((select sum(f1.freeze_money) from bt_bank_acc_freeze f1 where f1.f_uuid = f.id and f1.uf_flag = 1), 0)) as freeze_money, ");
		sql.append(" f.freeze_date, f.freeze_ression from bt_bank_acc_freeze f ");
		sql.append(" left join bt_bank_acc acc on f.bank_acc_id = acc.id left join bt_bank_type bankType on bankType.id = acc.bank_type_id ");
		sql.append(" where f.uf_flag = 0 and f.status = 95 ");
		sql.append(" and bankType.id = :bankTypeId ");
		params.put("bankTypeId", bankTypeId);
		sql.append(" and f.freeze_date >= :beginDate and f.freeze_date <= :endDate ");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and f.cur_id = :curId ");
			params.put("curId", curId);
		}
		if (!EmptyUtils.isBlank(corpId)) {
			sql.append(" and f.corp_Id = :corpId ");
			params.put("corpId", corpId);
		}
		sql.append(" order by f.freeze_date desc ");
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, BtBankAccFreezeVO.class);
	}
	/**
	 * 根据条件查询可用资金报表
	 *
	 * @param billSql
	 * @param beginDate
	 * @param endDate
	 * @param curId
	 * @param stockType
	 * @param netId
	 * @return
	 */
	public List<CapitalStockVO> listByNet(String billSql, Date beginDate, Date endDate, String curId, String stockType, String netId, Date balDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		if (!CapitalStockConstant.STOCK_TYPE_BILL.equalsIgnoreCase(stockType)) {
			sql.append(" select BT_NVL_CHAR(t.net_id, t1.net_id) netId, '1' stockType,BT_NVL_NUM(beginBal, 0) beginBal, BT_NVL_NUM(inAmt, 0) inAmt, BT_NVL_NUM(outAmt, 0) outAmt, ");
			sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0), 0) endBal, BT_NVL_NUM(freezeMoney, 0) freezeMoney, ");
			sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0) - BT_NVL_NUM(freezeMoney, 0), 0) availBal from ( ");
			sql.append(" (select c.net_id,sum(b.avail_bal) beginBal,sum(b.frz_bal) freezeMoney from bis_acc_bal b left join sys_corp c on b.corp_id = c.id ");
			sql.append(" where b.bal_date >= :beginDate and b.bal_date <= :endDate");
			if (!EmptyUtils.isBlank(netId)) {
				sql.append(" and c.net_id = :netId ");
				params.put("netId", netId);
			}
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and b.cur_id = :curId");
				params.put("curId", curId);
			}
			sql.append(" group by c.net_id) t ");
			sql.append(" left join ");
			sql.append(" (select c.net_id, sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '02' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '11' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '0' then BT_NVL_NUM(d.amt,0) else 0 end) as inAmt, ");
			sql.append(" sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '01' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '12' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '1' then BT_NVL_NUM(d.amt,0) else 0 end) as outAmt ");
			sql.append(" from bis_acc_dtl d left join bt_bank_acc acc on d.bank_acc = acc.bank_acc left join sys_corp c on acc.corp_id = c.id ");
			sql.append(" where d.trans_time >= :nisBeginDate and d.trans_time <= :nisEndDate ");
			if (!EmptyUtils.isBlank(curId)) {
				sql.append(" and d.cur_id = :curId ");
			}
			sql.append(" group by c.net_id ) t1 ");
			sql.append(" on t.net_id = t1.net_id ) ");
			// 银行流水开始结束时间
			params.put("nisBeginDate", beginDate);
			params.put("nisEndDate", endDate);
		}
		params.put("balDate", balDate);
		// 票据开始结束时间
		params.put("beginDate", DateUtils.parseDate(beginDate));
		params.put("endDate",  DateUtils.parseDate(endDate));
		params.put("endDateAddOne", DateUtils.addDay(DateUtils.getDateYMD(endDate),1));
		params.put("curdate", new java.sql.Date(System.currentTimeMillis()));
		if (!EmptyUtils.isBlank(billSql)) {
			if (EmptyUtils.isBlank(stockType)) {
				sql.append(" union all ");
			}
			sql.append(billSql);
			if (!EmptyUtils.isBlank(netId)) {
				sql.append(" where netId =: netId ");
			}
		}
		return this.listBySQLAliasToBean(sql.toString(), params, CapitalStockVO.class);
	}
	public DataTablesOutput getDataTablesWidget(DataTablesInput dataTablesInput, String billSql, Date beginDate, Date endDate, String curId, Date balDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select BT_NVL_CHAR(t.net_id, t1.net_id) netId, '1' stockType,t.cur_id curId, ");
		sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0) - BT_NVL_NUM(freezeMoney, 0), 0) accAvailBal,0 as billAvailBal from ( ");
		sql.append(" (select c.net_id, b.cur_id, sum(b.avail_bal) beginBal,sum(b.frz_bal) freezeMoney from bis_acc_bal b left join sys_corp c on b.corp_id = c.id ");
		sql.append(" where b.bal_date >= :beginDate and b.bal_date <= :endDate");
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and b.cur_id = :curId ");
			params.put("curId", curId);
		}
		sql.append(" group by c.net_id, b.cur_id) t ");
		sql.append(" left join ");
		sql.append(" (select c.net_id, sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '02' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '11' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '0' then BT_NVL_NUM(d.amt,0) else 0 end) as inAmt, ");
		sql.append(" sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '01' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '12' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '1' then BT_NVL_NUM(d.amt,0) else 0 end) as outAmt ");
		sql.append(" from bis_acc_dtl d left join bt_bank_acc acc on d.bank_acc = acc.bank_acc left join sys_corp c on acc.corp_id = c.id");
		sql.append(" where d.trans_time >= :nisBeginDate and d.trans_time <= :nisEndDate ");
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and d.cur_id = :curId ");
		}
		sql.append(" group by c.net_id, d.cur_id ) t1 ");
		sql.append(" on t.net_id = t1.net_id ) ");
		params.put("balDate", balDate);
		// 银行流水开始结束时间
		params.put("nisBeginDate", beginDate);
		params.put("nisEndDate", endDate);
		// 票据开始结束时间
		params.put("beginDate", DateUtils.parseDate(beginDate));
		params.put("endDate",  DateUtils.parseDate(endDate));
		params.put("endDateAddOne", DateUtils.addDay(DateUtils.getDateYMD(endDate),1));
		params.put("curdate", new java.sql.Date(System.currentTimeMillis()));
		if (!EmptyUtils.isBlank(billSql)) {
			sql.append(" union ").append(billSql);
		}
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, CapitalStockWidgetVO.class);
	}
	public List<CapitalStockWidgetVO> listAccAvailBalByWidget(String netId, Date beginDate, Date endDate, String curId, Date balDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select t.cur_id curId, BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0) - BT_NVL_NUM(freezeMoney, 0), 0) as accAvailBal from ( ");
		sql.append(" (select c.net_id, b.cur_id, sum(b.avail_bal) beginBal,sum(b.frz_bal) as freezeMoney from bis_acc_bal b left join sys_corp c on b.corp_id = c.id ");
		sql.append(" where b.bal_date >= :beginDate and b.bal_date <= :endDate ");
		if (!EmptyUtils.isBlank(netId)) {
			sql.append(" and c.net_id = :netId ");
			params.put("netId", netId);
		}
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and b.cur_id = :curId ");
			params.put("curId", curId);
		}
		sql.append(" group by c.net_id, b.cur_id) t ");
		sql.append(" left join ");
		sql.append(" (select c.net_id, d.cur_id, sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '02' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '11' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '0' then BT_NVL_NUM(d.amt,0) else 0 end) as inAmt, ");
		sql.append(" sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '01' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '12' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '1' then BT_NVL_NUM(d.amt,0) else 0 end) as outAmt ");
		sql.append(" from bis_acc_dtl d left join bt_bank_acc acc on d.bank_acc = acc.bank_acc left join sys_corp c on acc.corp_id = c.id ");
		sql.append(" where d.trans_time >= :nisBeginDate and d.trans_time <= :nisEndDate ");
		if (!EmptyUtils.isBlank(netId)) {
			sql.append(" and c.net_id = :netId ");
		}
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and d.cur_id = :curId ");
		}
		sql.append(" group by c.net_id, d.cur_id ) t1 ");
		sql.append(" on t.net_id = t1.net_id ) order by accAvailBal desc");
		params.put("balDate", balDate);
		// 银行流水开始结束时间
		params.put("nisBeginDate", beginDate);
		params.put("nisEndDate", endDate);
		return this.listBySQLAliasToBean(sql.toString(), params, CapitalStockWidgetVO.class);
	}
	public List<CapitalStockWidgetVO> listDataTablesWidget(String billSql, Date beginDate, Date endDate, String curId, Date balDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		if (!EmptyUtils.isBlank(billSql)) {
			sql.append(" select BT_NVL_CHAR(t.net_id, BT_NVL_CHAR(t1.net_id,bill.netId)) netId, '1' stockType, '" + curId + "' as curId, BT_NVL_NUM(bill.billAvailBal,0) as billAvailBal, ");
		} else {
			sql.append(" select BT_NVL_CHAR(t.net_id, t1.net_id) netId, '1' stockType, '" + curId + "' as curId, 0 as billAvailBal, ");
		}
		sql.append(" BT_NVL_NUM(BT_NVL_NUM(beginBal, 0) + BT_NVL_NUM(inAmt, 0) - BT_NVL_NUM(outAmt, 0) - BT_NVL_NUM(freezeMoney, 0), 0) as accAvailBal from ( ");
		sql.append(" (select c.net_id, b.cur_id, sum(b.avail_bal) as beginBal,sum(b.frz_bal) as freezeMoney from bis_acc_bal b left join sys_corp c on b.corp_id = c.id ");
		sql.append(" where b.bal_date >= :beginDate and b.bal_date <= :endDate ");
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and b.cur_id = :curId ");
			params.put("curId", curId);
		}
		sql.append(" group by c.net_id, b.cur_id) t ");
		sql.append(" left join ");
		sql.append(" (select c.net_id, d.cur_id,  sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '02' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '11' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '0' then BT_NVL_NUM(d.amt,0) else 0 end) as inAmt, ");
		sql.append(" sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '01' then BT_NVL_NUM(d.amt,0) else 0 end) + sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '12' then BT_NVL_NUM(d.amt,0) else 0 end)+sum(case when BT_CONCAT(d.cd_sign,d.rb_sign) = '1' then BT_NVL_NUM(d.amt,0) else 0 end) as outAmt ");
		sql.append(" from bis_acc_dtl d left join bt_bank_acc acc on d.bank_acc = acc.bank_acc left join sys_corp c on acc.corp_id = c.id ");
		sql.append(" where d.trans_time >= :nisBeginDate and d.trans_time <= :nisEndDate ");
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and d.cur_id = :curId ");
		}
		sql.append(" group by c.net_id, d.cur_id ) t1 ");
		sql.append(" on t.net_id = t1.net_id ) ");
		params.put("balDate", DateUtils.parseDate(balDate));
		// 银行流水开始结束时间
		params.put("nisBeginDate", beginDate);
		params.put("nisEndDate", endDate);
		// 票据开始结束时间
		params.put("beginDate", DateUtils.parseDate(beginDate));
		params.put("endDate",  DateUtils.parseDate(endDate));
		params.put("endDateAddOne", DateUtils.addDay(DateUtils.getDateYMD(endDate),1));
		params.put("curdate", new java.sql.Date(System.currentTimeMillis()));
		if (!EmptyUtils.isBlank(billSql)) {
			sql.append(" left join ( ").append(billSql).append(" ) bill on t.net_id = bill.netId ");
		}
		return this.listBySQLAliasToBean(sql.toString(), params, CapitalStockWidgetVO.class);
	}
	/**
	 * 查询最近一天有余额的余额日期
	 *
	 * @param balDate 余额日期
	 * @param curId   币别ID
	 * @param corpId  单位ID
	 * @return
	 */
	public Date getLastBalByBisAccBal(Date balDate, String curId, String corpId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select * from bis_acc_bal b where b.bal_date <= :balDate ");
		params.put("balDate", balDate);
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and b.cur_id = :curId ");
			params.put("curId", curId);
		}
		if (!EmptyUtils.isBlank(corpId)) {
			sql.append(" and b.corp_id = :curId ");
			params.put("corpId", corpId);
		}
		sql.append(" order by bal_date desc ");
		List<BisAccBalDTO> list = this.listBySQLAliasToBean(sql.toString(), params, BisAccBalDTO.class);
		if (EmptyUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0).getBalDate();
	}
}
