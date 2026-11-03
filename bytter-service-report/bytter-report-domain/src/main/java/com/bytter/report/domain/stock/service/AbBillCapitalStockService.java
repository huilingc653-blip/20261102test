package com.bytter.report.domain.stock.service;
import com.bytter.report.domain.stock.dao.AbBillCapitalStockDao;
import com.bytter.report.domain.stock.vo.AbBillVO;
import com.bytter.report.domain.stock.vo.CapitalStockWidgetVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/***
 * 资金存量汇总报表Service
 * @author zuoy
 * @date 2017/10/27
 */
@Service("abBillCapitalStockService")
@Transactional
public class AbBillCapitalStockService extends BaseService<AbBillVO, String> {
	@Autowired
	private AbBillCapitalStockDao abBillCapitalStockDao;
	@Override
	public BaseDao<AbBillVO, String> getEntityDao() {
		return abBillCapitalStockDao;
	}
	/**
	 * 根据查询条件生成报表查询语句,存量种类为应收票据,币别为人民币
	 *
	 * @param reportType 报表类型
	 * @return String
	 */
	public String getQueryAbBillSql(String reportType,String corpSql,String netId) {
		if (StringUtils.isEmpty(reportType)) {
			return null;
		}
		return abBillCapitalStockDao.getQueryAbBillSql(reportType,corpSql,netId);
	}
	/**
	 * 数据列表查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String bankTypeId, Date beginDate, Date endDate, String reportType, String corpId) {
		return abBillCapitalStockDao.getAbBilldtl(dataTablesInput, bankTypeId, beginDate, endDate, reportType,corpId);
	}
	/**
	 * 根据网点ID和日期获取票据可用资金余额
	 * 可用资金余额 = 期初资金余额+本期资金增加额-本期资金减少额-本期不可动用资金
	 */
	public BigDecimal getBillAvailBal(String netId, Date beginDate, Date endDate) {
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.isNotEmpty(netId)) {
			params.put("netId", netId);
		}
		params.put("balDate", new java.sql.Date(DateUtils.addDay(beginDate, -1).getTime ()));
		params.put("beginDate", new java.sql.Date(beginDate.getTime ()));
		params.put("endDate", new java.sql.Date(endDate.getTime ()));
		CapitalStockWidgetVO capitalStockWidgetVO = abBillCapitalStockDao.getBillAvailBal(params);
		if (capitalStockWidgetVO != null) {
			return capitalStockWidgetVO.getBillAvailBal();
		}
		return BigDecimal.ZERO;
	}
	/**
	 * 根据网点ID和日期获取票据可用资金余额查询SQL
	 */
	public String getBillAvailBalSql(String netId) {
		return abBillCapitalStockDao.getBillAvailBalSql(netId);
	}
}
