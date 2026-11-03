package com.bytter.biz.report.service;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.report.dao.FundsBalanceStatisticsDao;
import com.bytter.biz.report.vo.FundsBalanceStatisticsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
 * 资金余额统计表（报表）
 *
 * @author wangzx
 * @date 2018-4-13
 */
@Service("fundsBalanceStatisticsService")
@Transactional
public class FundsBalanceStatisticsService extends BaseService<FundsBalanceStatisticsVO, String> {
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private FundsBalanceStatisticsDao fundsBalanceStatisticsDao;
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;

	@Override
	public BaseDao<FundsBalanceStatisticsVO, String> getEntityDao() {
		return fundsBalanceStatisticsDao;
	}
	/**
	 * 资金余额统计报表
	 *
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getFundsBalanceStatisticsDtl(DataTablesInput input, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_FUNDSBALANCESTATISTICS_QUERY, user.getId()), "f.corpId", true);
		sql.append(corpAuthSql);
		DataTablesOutput out = fundsBalanceStatisticsDao.getFundsBalanceStatisticsDtl(input, sql.toString());
		List<FundsBalanceStatisticsVO> list = out.getData();
		FundsBalanceStatisticsVO vo = null;
		if (!EmptyUtils.isEmptyList(list)) {
			Map<String, Map<String, Object>> map = new LinkedHashMap<>();
			for (FundsBalanceStatisticsVO fo : list) {
				String key = fo.getCorpName() + ";" + fo.getCurName();
				Map<String, Object> m = map.get(key);
				if (m == null) {
					m = new HashMap<String, Object>();
					map.put(key, m);
				}
				m.put(fo.getAccType(), fo.getBalance());
			}
			List<FundsBalanceStatisticsVO> outList = new ArrayList<FundsBalanceStatisticsVO>();
			for (FundsBalanceStatisticsVO fo : list) {
				FundsBalanceStatisticsVO entity = new FundsBalanceStatisticsVO();
				String key = fo.getCorpName() + ";" + fo.getCurName();
				String[] keyTemp = key.split(";");
				entity.setCorpName(keyTemp[0]);
				entity.setCurName(keyTemp[1]);
				Map value = map.get(key);
				entity.setAccType01(value.get("01") == null ? BigDecimal.ZERO : (BigDecimal) value.get("01"));
				entity.setAccType02(value.get("02") == null ? BigDecimal.ZERO : (BigDecimal) value.get("02"));
				entity.setAccType03(value.get("03") == null ? BigDecimal.ZERO : (BigDecimal) value.get("03"));
				entity.setAccType04(value.get("04") == null ? BigDecimal.ZERO : (BigDecimal) value.get("04"));
				entity.setAccType05(value.get("05") == null ? BigDecimal.ZERO : (BigDecimal) value.get("05"));
				entity.setAccType06(value.get("06") == null ? BigDecimal.ZERO : (BigDecimal) value.get("06"));
				entity.setAccType07(value.get("07") == null ? BigDecimal.ZERO : (BigDecimal) value.get("07"));
				entity.setAccType09(value.get("09") == null ? BigDecimal.ZERO : (BigDecimal) value.get("09"));
				entity.setAccType10(value.get("10") == null ? BigDecimal.ZERO : (BigDecimal) value.get("10"));
				entity.setAccType11(value.get("1010") == null ? BigDecimal.ZERO : (BigDecimal) value.get("1010"));
				entity.setAccType12(value.get("2120") == null ? BigDecimal.ZERO : (BigDecimal) value.get("2120"));
				entity.setAccType13(value.get("2130") == null ? BigDecimal.ZERO : (BigDecimal) value.get("2130"));
				entity.setAccType14(value.get("2140") == null ? BigDecimal.ZERO : (BigDecimal) value.get("2140"));
				BigDecimal fcTotal = BigDecimal.ZERO;
				entity.setFcTotal(fcTotal.add(entity.getAccType11()).add(entity.getAccType12()).add(entity.getAccType13()).add(entity.getAccType14()));
				BigDecimal bankTotal = BigDecimal.ZERO;
				entity.setBankTotal(bankTotal.add(entity.getAccType01()).add(entity.getAccType02()).add(entity.getAccType03()).add(entity.getAccType04())
						.add(entity.getAccType05()).add(entity.getAccType06()).add(entity.getAccType07()).add(entity.getAccType09()));
				outList.add(entity);
			}
			out.setData(outList);
		}
		return out;
	}
	/**
	 * 获取查询条件年份
	 * @return
	 */
	public int[] getYears(){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int startYear = year-5;
		int endYear   = year+5;
		int cn = 0;
		int[] yearItems = new int[10];
		while(startYear<endYear){
			yearItems[cn++] = startYear++;
		}
		return yearItems;
	}
	
	
	/**获取当前用户对应资源的单位或网点id**/
	public List<CorpDTO> listUserCorp(String resource, String userId){
		return corpService.listUserCorp(resource, userId);
	}
	
	/**获取币别**/
	public List<BtCurrencyDTO> getBtCurrency() {
		return btCurrencyService.getBtCurrency();
	}
	
	public List<FundsBalanceStatisticsVO> getData(String curId, String corpId,String startDate,String endDate,String userId){
		List<FundsBalanceStatisticsVO> data = fundsBalanceStatisticsDao.getData(curId, corpId,startDate,endDate,1,userId,null);
		List<FundsBalanceStatisticsVO> outList = new ArrayList<FundsBalanceStatisticsVO>();
		if(!EmptyUtils.isEmptyList(data)){
			Map<String,Map<String,Object>> map = new LinkedHashMap<>();
			for(FundsBalanceStatisticsVO fo : data){
				//把 SQL 里面的 f.balance / 1  改到此处处理 dividend.divide(divisor, 0, BigDecimal.ROUND_DOWN);
				if (fo.getBalance() != null){
					fo.setBalance(fo.getBalance().divide(new BigDecimal(1), 0, BigDecimal.ROUND_DOWN));
				}
				String key = fo.getCorpName()+";"+fo.getCurName();
				Map<String,Object> m = map.get(key);
				if(m == null){
					m = new HashMap<String,Object>();
					map.put(key,m);
				}
				m.put(fo.getAccType(),fo.getBalance());
			}
			
			for (Map.Entry<String, Map<String, Object>> item : map.entrySet()) {
				FundsBalanceStatisticsVO entity = new FundsBalanceStatisticsVO();
				String key = item.getKey();
				String[] keyTemp = key.split(";");
				entity.setCorpName(keyTemp[0]);
				entity.setCurName(keyTemp[1]);
				Map value = item.getValue();
				entity.setAccType01((BigDecimal)value.get("01") == null ? BigDecimal.ZERO : (BigDecimal)value.get("01"));
				entity.setAccType02((BigDecimal)value.get("02") == null ? BigDecimal.ZERO : (BigDecimal)value.get("02"));
				entity.setAccType03((BigDecimal)value.get("03") == null ? BigDecimal.ZERO : (BigDecimal)value.get("03"));
				entity.setAccType04((BigDecimal)value.get("04") == null ? BigDecimal.ZERO : (BigDecimal)value.get("04"));
				entity.setAccType05((BigDecimal)value.get("05") == null ? BigDecimal.ZERO : (BigDecimal)value.get("05"));
				entity.setAccType06((BigDecimal)value.get("06") == null ? BigDecimal.ZERO : (BigDecimal)value.get("06"));
				entity.setAccType07((BigDecimal)value.get("07") == null ? BigDecimal.ZERO : (BigDecimal)value.get("07"));
				entity.setAccType09((BigDecimal)value.get("09") == null ? BigDecimal.ZERO : (BigDecimal)value.get("09"));
				entity.setAccType10((BigDecimal)value.get("10") == null ? BigDecimal.ZERO : (BigDecimal)value.get("10"));
				entity.setAccType11((BigDecimal)value.get("1010") == null ? BigDecimal.ZERO : (BigDecimal)value.get("1010"));
				entity.setAccType12((BigDecimal)value.get("2120") == null ? BigDecimal.ZERO : (BigDecimal)value.get("2120"));
				entity.setAccType13((BigDecimal)value.get("2130") == null ? BigDecimal.ZERO : (BigDecimal)value.get("2130"));
				entity.setAccType14((BigDecimal)value.get("2140") == null ? BigDecimal.ZERO : (BigDecimal)value.get("2140"));
				BigDecimal fcTotal = BigDecimal.ZERO;
				entity.setFcTotal(fcTotal.add(entity.getAccType11()).add(entity.getAccType12()).add(entity.getAccType13()).add(entity.getAccType14()));
				BigDecimal bankTotal = BigDecimal.ZERO;
				entity.setBankTotal(bankTotal.add(entity.getAccType01()).add(entity.getAccType02()).add(entity.getAccType03()).add(entity.getAccType04())
					.add(entity.getAccType05()).add(entity.getAccType06()).add(entity.getAccType07()).add(entity.getAccType09()));
				outList.add(entity);
		}	
	}
		return outList;
	}	
	public Object getPieData(List<FundsBalanceStatisticsVO> list,String corpName,String curName) {
		Map<String, Object> map = new HashMap<>();
		Set<String> legend = new HashSet();
		Set<String> curSet = new HashSet();
		//类型
		legend.add("现金");
		legend.add("活期");
		legend.add("定期");
		legend.add("通知");
		
		map.put("title", corpName+"   "+curName);
		for (FundsBalanceStatisticsVO vo : list) {
			curSet.add(vo.getCurName());
		}
		map.put("legend", legend);
		List<String> yAxisList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			FundsBalanceStatisticsVO netVo = list.get(i);
			yAxisList.add(netVo.getCurName());
		}
		map.put("yAxisData1", yAxisList);
		List<Series> series = new ArrayList<>();
		Map<String, String> openParams = new HashMap<>();
		List<Object> datas = new ArrayList<>();
		for (String moneyTypeName : legend) {
			for (int j=0;j<yAxisList.size();j++) {
				boolean flag = true;
				String curId = "";
				String corpId = "";
				for (FundsBalanceStatisticsVO vo : list) {
					BigDecimal sumMoney = BigDecimal.ZERO;
					if (yAxisList.get(j).equalsIgnoreCase(vo.getCurName())) {
						flag = false;
						corpId = vo.getCorpId();
						if("现金".equals(moneyTypeName)){
							sumMoney = vo.getAccType11();
						}
						if("活期".equals(moneyTypeName)){
							sumMoney = vo.getAccType12();
						}
						if("定期".equals(moneyTypeName)){
							sumMoney = vo.getAccType13();
						}
						if("通知".equals(moneyTypeName)) {
							sumMoney = vo.getAccType14();
						}
					}
					Map<String,Object> seriesMap = new HashMap<String,Object>();
					seriesMap.put("name",moneyTypeName);
					seriesMap.put("value", sumMoney);
					datas.add(seriesMap);
				}
			}
		}
		// 设置数据
		Pie pie = new Pie();
		String[] radius = {"32%", "60%"};//圆心坐标位置，距离X轴和Y轴的百分比
		pie.name("内部存款");
		pie.radius("70%");//半径大小
		pie.center(radius);
		pie.setType(SeriesType.pie);
		pie.data(datas.toArray(new Object[datas.size()]));
		Normal normal = new Normal();
		normal.setShow(false);
		normal.setPosition(Position.center);
		pie.avoidLabelOverlap(false);
		ItemStyle item = new ItemStyle();
		item.normal(normal);
		pie.label(item);
		
		series.add(pie);
		map.put("openParams", openParams);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			map.put("series", mapper.writeValueAsString(series));
			return map;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "[]";
		}
	}
	
	
	public Object getPieDataBank(List<FundsBalanceStatisticsVO> list,String corpName) {
		Map<String, Object> map = new HashMap<>();
		Set<String> legend = new HashSet();
		Set<String> curSet = new HashSet();
		//类型
		legend.add("活期");
		legend.add("协定");
		legend.add("通知");
		legend.add("专用户");
		legend.add("保证金");
		legend.add("贷款户");
		legend.add("虚拟户");
		legend.add("定期");
		
		map.put("title", corpName+"   银行存款");
		for (FundsBalanceStatisticsVO vo : list) {
			curSet.add(vo.getCurName());
		}
		map.put("legend", legend);
		List<String> yAxisList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			FundsBalanceStatisticsVO netVo = list.get(i);
			yAxisList.add(netVo.getCurName());
		}
		map.put("yAxisData1", yAxisList);
		List<Series> series = new ArrayList<>();
		Map<String, String> openParams = new HashMap<>();
		List<Object> datas = new ArrayList<>();
		for (String moneyTypeName : legend) {
			for (int j=0;j<yAxisList.size();j++) {
				boolean flag = true;
				String curId = "";
				String corpId = "";
				for (FundsBalanceStatisticsVO vo : list) {
					BigDecimal sumMoney = BigDecimal.ZERO;
					if (yAxisList.get(j).equalsIgnoreCase(vo.getCurName())) {
						flag = false;
						corpId = vo.getCorpId();
						if("活期".equals(moneyTypeName)){
							sumMoney = vo.getAccType01();
						}
						if("协定".equals(moneyTypeName)){
							sumMoney = vo.getAccType07();
						}
						if("通知".equals(moneyTypeName)){
							sumMoney = vo.getAccType06();
						}
						if("专用户".equals(moneyTypeName)) {
							sumMoney = vo.getAccType05();
						}
						if("保证金".equals(moneyTypeName)) {
							sumMoney = vo.getAccType04();
						}
						if("贷款户".equals(moneyTypeName)) {
							sumMoney = vo.getAccType03();
						}
						if("虚拟户".equals(moneyTypeName)) {
							sumMoney = vo.getAccType09();
						}
						if("定期".equals(moneyTypeName)) {
							sumMoney = vo.getAccType02();
						}
					}
					Map<String,Object> seriesMap = new HashMap<String,Object>();
					seriesMap.put("name",moneyTypeName);
					seriesMap.put("value", sumMoney);
					datas.add(seriesMap);
				}
			}
		}
		// 设置数据
		Pie pie = new Pie();
		pie.name("银行存款");
		String[] radius = {"32%", "60%"};//圆心坐标位置，距离X轴和Y轴的百分比
		pie.radius("70%");//半径大小
		pie.center(radius);
		pie.setType(SeriesType.pie);
		pie.data(datas.toArray(new Object[datas.size()]));
		Normal normal = new Normal();
		normal.setShow(false);
		normal.setPosition(Position.center);
		
		ItemStyle item = new ItemStyle();
		item.normal(normal);
		pie.label(item);
		
		series.add(pie);
		map.put("openParams", openParams);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			map.put("series", mapper.writeValueAsString(series));
			return map;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "[]";
		}
	}
	
	
	
	public Object getBarData(List<FundsBalanceStatisticsVO> list,String corpName,String curName) {
		Map<String, Object> map = new HashMap<>();
		Set<String> legend = new HashSet();
		Set<String> corpSet = new HashSet();
		//类型
		legend.add(corpName+ "\n"+curName);
		
		map.put("title", corpName+"   票据");
		
		for (FundsBalanceStatisticsVO vo : list) {
			corpSet.add(vo.getCurName());
		}
		map.put("legend", legend);
		List<String> yAxisList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			FundsBalanceStatisticsVO netVo = list.get(i);
			yAxisList.add(netVo.getCurName());
		}
		map.put("yAxisData1", yAxisList);
		List<Series> series = new ArrayList<>();
		Map<String, String> openParams = new HashMap<>();
		for (String moneyTypeName : legend) {
			List<Object> datas = new ArrayList<>();
			for (int j=0;j<yAxisList.size();j++) {
				boolean flag = true;
				String curId = "";
				String corpId = "";
				for (FundsBalanceStatisticsVO vo : list) {
					if (yAxisList.get(j).equalsIgnoreCase(vo.getCurName())) {
						flag = false;
						corpId = vo.getCorpId();
						if(moneyTypeName.equals(corpName+ "\n"+curName)) {
							datas.add(vo.getAccType10());
						}
						
						break;
					}
				}
				if (flag) {
					datas.add(BigDecimal.ZERO);
				}
			}
			// 设置数据
			Bar bar = new Bar();
			bar.setName(moneyTypeName);
			bar.setType(SeriesType.bar);
			bar.setData(datas);
			bar.setSymbolSize(12);
			series.add(bar);
		}
		map.put("openParams", openParams);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			map.put("series", mapper.writeValueAsString(series));
			return map;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "[]";
		}
	}
	/**
	 * 资金余额统计报表
	 *
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
		StringBuilder joinSql = new StringBuilder();
		String corpAuthSql;
		String corpIds = (String) input.getFormdata().get("corpId");
		if (StringUtils.isNotEmpty(corpIds)){
			corpAuthSql = SqlUtil.getInSql(corpIds.split(","),"f.corpId", true);
		}else {
			corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_FUNDSBALANCESTATISTICS_QUERY, user.getId()), "f.corpId", true);
		}
		joinSql.append(corpAuthSql);
		StringBuilder sql = new StringBuilder();
		//获取银行存款类型
		List<DictDTO> dictList = dictService.list("btBankAcc.accType");
		sql.append("select f.corpId as corpId, f.curId as curId,c.name as corpName,cur.cur_name as curName,");
		//组装合计
		StringBuilder sqlSum = new StringBuilder();
		sqlSum.append("sum(");
		//组装内部存款列
		StringBuilder sqlInnerFlied = new StringBuilder();
		//组装银行存款列
		StringBuilder sqlBankFlied = new StringBuilder();
		for(int i=0;i<dictList.size();i++){
			DictDTO dict = dictList.get(i);
			sql.append(" sum(f.bankAccType" + dict.getCode() + ") as bankAccType" + dict.getCode() + ",");
			if(i==0){
				sqlSum.append("BT_NVL_NUM(f.bankAccType" + dict.getCode() + ",0)");
			}else {
				sqlSum.append("+BT_NVL_NUM(f.bankAccType" + dict.getCode() + ",0)");
			}
			sqlInnerFlied.append(" 0 bankAccType" + dict.getCode() + ",");
			sqlBankFlied.append(" max(case when t1.accType='" + dict.getCode() + "' then t1.bal end) bankAccType" + dict.getCode() + ", ");
		}
		sqlSum.append(") bankTotal");
		sql.append(" sum(f.accType11) as accType11,sum(f.accType12) as accType12,sum(f.accType13) as accType13,sum(f.accType14) as accType14, ");
		sql.append(" sum(BT_NVL_NUM(f.accType11,0) + BT_NVL_NUM(f.accType12,0) + BT_NVL_NUM(f.accType13,0) + BT_NVL_NUM(f.accType14,0)) fcTotal,");
		sql.append(sqlSum);
		sql.append(" from ( ");
		sql.append("select t1.corpId, t1.curId,  ");
		sql.append(sqlInnerFlied);
		sql.append(" max(case when t1.accType ='1010' then t1.balance end) AccType11, max(case when t1.accType ='2120' then t1.balance end) AccType12, ");
		sql.append(" max(case when t1.accType ='2130' then t1.balance end) AccType13, max(case when t1.accType ='2140' then t1.balance end) AccType14 ");
		sql.append(" from ( ");
		sql.append(" select t.corpId, t.curId, t.accType, sum(BT_NVL_NUM(ba.balance, 0)) AS balance from ( ");
		sql.append(" select a.corp_id AS corpId, a.blnctr_acc AS bankAcc, a.cur_id AS curId, b.sys_subject_code AS accType, max(c.balance_date) AS balDate ");
		sql.append(" FROM fc_acc a, fc_subject b, fc_acc_balance c WHERE a.subject_id = b.id AND a.blnctr_acc = c.account ");
		sql.append(" and b.sys_subject_code in ('1010','2120','2130','2140') ");
		sql.append(" and a.acc_state not in ('T', 'W', 'B', 'D') ");//过滤掉开户暂存，开户待复核，开户复核打回，注销
		sql.append(" and c.balance_date <= bt_to_date(:endDate, 'yyyy-MM-dd') ");
		sql.append(" group by a.corp_id, a.blnctr_acc, a.cur_id, b.sys_subject_code) t ");
		sql.append(" left join fc_acc_balance ba ON t.bankAcc = ba.account and t.curId = ba.cur_id and t.balDate = ba.balance_date ");
		sql.append(" where t.curId is not null group by t.corpId, t.curId, t.accType) t1 ");
		sql.append(" group by t1.corpId, t1.curId ");
		sql.append(" union all ");
		sql.append(" select t1.corpId,t1.curId,");
		sql.append(sqlBankFlied);
		sql.append(" 0 AccType11, 0 AccType12, 0 AccType13, 0 AccType14");
		sql.append(" from ( ");
		sql.append(" select t.corpId, t.curId, t.accType, sum(ba.bal) bal from ( ");
		sql.append(" select a.corp_id as corpId, a.bank_acc as bankAcc, b.cur_id as curId, a.acc_type as accType, max(b.bal_date) as balDate ");
		sql.append(" from bt_bank_acc a, bis_acc_bal b where a.bank_acc = b.bank_acc and a.valid_sign = '1' and a.status <> -2 ");//过滤状态不为删除的和必须是有效的
		sql.append(" and b.bal_date <= bt_to_date(:endDate, 'yyyy-MM-dd') ");
		sql.append(" group by a.corp_id, a.bank_acc, b.cur_id, a.acc_type) t ");
		sql.append(" left join bis_acc_bal ba on t.bankAcc = ba.bank_acc and t.curId = ba.cur_id and t.balDate = ba.bal_date ");
		sql.append(" where t.curId is not null group by t.corpId, t.curId,t.accType) t1 ");
		sql.append(" group by t1.corpId,t1.curId ");
		sql.append(" ) f left join sys_corp c on f.corpId = c.id left join bt_currency cur on f.curId=cur.id where 1 = 1 ");
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(" and ");
			sql.append(joinSql);
		}
		DataTablesOutput datas = fundsBalanceStatisticsDao.getDynamicDataTablesUnion(input, sql);
		String changeCur=(String)input.getFormdata().get("changeCur");
		String reducedCurId=(String)input.getFormdata().get("reducedCurId");
		String rateType=(String)input.getFormdata().get("rateType");
		Date rateDate= DateUtils.getIntegralEndTime(DateUtils.parse((String)input.getFormdata().get("rateDate"), "yyyy-MM-dd"));
		//获取汇率
		if (datas != null && datas.getData() != null) {
			List<Map> list = datas.getData();
			HashMap<String, BigDecimal> curExrateMap = new HashMap<>();
			for(Map map:list){
				BigDecimal curExrate = BigDecimal.ONE;
				String curId = map.get("curid").toString();
				if(CommonConstant.YES.equals(changeCur)) {
					if (curExrateMap.containsKey(curId)) {
						curExrate = curExrateMap.get(curId);
					} else {
						curExrate = fcExcrateAdjustService.getExchangeRateBy(curId, reducedCurId, DateUtils.setSpecifiedDateOfTime(rateDate), rateType);
						curExrateMap.put(curId, curExrate);
					}
				}
				map.put("curexrate", curExrate);
			}
		}
		return datas;
	}
	/**
	 * app报表列表数据
	 * @param curId
	 * @param corpId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<FundsBalanceStatisticsVO> getAppData(String curId, String corpId,String startDate,String endDate,ICurrentUser user,Integer unit){
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_FUNDSBALANCESTATISTICS_QUERY, user.getId()), "f.corpId", true);
		sql.append(corpAuthSql);
		List<FundsBalanceStatisticsVO> data = fundsBalanceStatisticsDao.getData(curId, corpId,startDate,endDate,unit,user.getId(),sql.toString());
		List<FundsBalanceStatisticsVO> outList = new ArrayList<FundsBalanceStatisticsVO>();
		if(!EmptyUtils.isEmptyList(data)){
			Map<String,Map<String,Object>> map = new LinkedHashMap<>();
			for(FundsBalanceStatisticsVO fo : data){

				//把 SQL 里面的 f.balance / 1  改到此处处理 dividend.divide(divisor, 0, BigDecimal.ROUND_DOWN);
				fo.setBalance(fo.getBalance().divide(new BigDecimal(1), 0, BigDecimal.ROUND_DOWN));

				String key = fo.getCorpName()+";"+fo.getCurName();
				Map<String,Object> m = map.get(key);
				if(m == null){
					m = new HashMap<String,Object>();
					map.put(key,m);
				}
				m.put(fo.getAccType(),fo.getBalance());
			}
			for (Map.Entry<String, Map<String, Object>> item : map.entrySet()) {
				FundsBalanceStatisticsVO entity = new FundsBalanceStatisticsVO();
				String key = item.getKey();
				String[] keyTemp = key.split(";");
				entity.setCorpName(keyTemp[0]);
				entity.setCurName(keyTemp[1]);
				Map value = item.getValue();
				entity.setAccType01((BigDecimal)value.get("01") == null ? BigDecimal.ZERO : (BigDecimal)value.get("01"));
				entity.setAccType02((BigDecimal)value.get("02") == null ? BigDecimal.ZERO : (BigDecimal)value.get("02"));
				entity.setAccType03((BigDecimal)value.get("03") == null ? BigDecimal.ZERO : (BigDecimal)value.get("03"));
				entity.setAccType04((BigDecimal)value.get("04") == null ? BigDecimal.ZERO : (BigDecimal)value.get("04"));
				entity.setAccType05((BigDecimal)value.get("05") == null ? BigDecimal.ZERO : (BigDecimal)value.get("05"));
				entity.setAccType06((BigDecimal)value.get("06") == null ? BigDecimal.ZERO : (BigDecimal)value.get("06"));
				entity.setAccType07((BigDecimal)value.get("07") == null ? BigDecimal.ZERO : (BigDecimal)value.get("07"));
				entity.setAccType09((BigDecimal)value.get("09") == null ? BigDecimal.ZERO : (BigDecimal)value.get("09"));
				entity.setAccType10((BigDecimal)value.get("10") == null ? BigDecimal.ZERO : (BigDecimal)value.get("10"));
				entity.setAccType11((BigDecimal)value.get("1010") == null ? BigDecimal.ZERO : (BigDecimal)value.get("1010"));
				entity.setAccType12((BigDecimal)value.get("2120") == null ? BigDecimal.ZERO : (BigDecimal)value.get("2120"));
				entity.setAccType13((BigDecimal)value.get("2130") == null ? BigDecimal.ZERO : (BigDecimal)value.get("2130"));
				entity.setAccType14((BigDecimal)value.get("2140") == null ? BigDecimal.ZERO : (BigDecimal)value.get("2140"));
				BigDecimal fcTotal = BigDecimal.ZERO;
				entity.setFcTotal(fcTotal.add(entity.getAccType11()).add(entity.getAccType12()).add(entity.getAccType13()).add(entity.getAccType14()));
				BigDecimal bankTotal = BigDecimal.ZERO;
				entity.setBankTotal(bankTotal.add(entity.getAccType01()).add(entity.getAccType02()).add(entity.getAccType03()).add(entity.getAccType04())
						.add(entity.getAccType05()).add(entity.getAccType06()).add(entity.getAccType07()).add(entity.getAccType09()));
				outList.add(entity);
			}
		}
		return outList;
	}
	/**
	 * 根据条件得到小计总和
	 * @param curId
	 * @param corpId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Map<String,BigDecimal> subMap(String curId, String corpId,String startDate,String endDate,String userId){
		List<FundsBalanceStatisticsVO> data = this.getData(curId, corpId, startDate, endDate,userId);
		Map<String,BigDecimal> curMap = new HashMap<>();
		for(FundsBalanceStatisticsVO fo : data){
			String key = "inner;"+fo.getCurName();
			String key1 = "bank;"+fo.getCurName();
			String key2 = "bill;"+fo.getCurName();
			//为空说明没有本币别
			BigDecimal fcTotal = curMap.get(key);
			if(EmptyUtils.isEmpty(fcTotal)){
				curMap.put(key,fo.getFcTotal());
			}else{
				curMap.put(key,fcTotal.add(fo.getFcTotal()));
			}
			BigDecimal bankTotal = curMap.get(key1);
			if(EmptyUtils.isEmpty(bankTotal)){
				curMap.put(key1,fo.getBankTotal());
			}else{
				curMap.put(key1,bankTotal.add(fo.getBankTotal()));
			}
			BigDecimal billTotal = curMap.get(key2);
			if(EmptyUtils.isEmpty(billTotal)){
				curMap.put(key2,fo.getAccType10());
			}else{
				curMap.put(key2,billTotal.add(fo.getAccType10()));
			}
		}
		return curMap;
	}
	/**
	 * 合计金额
	 * @param voList
	 * @return
	 */
	public Map<String,BigDecimal> getTotal(List<FundsBalanceStatisticsVO> voList){
		BigDecimal ixj = BigDecimal.ZERO;//内部-现金
		BigDecimal ihq = BigDecimal.ZERO;//内部-活期
		BigDecimal idq = BigDecimal.ZERO;//内部-定期
		BigDecimal itz = BigDecimal.ZERO;//内部-通知
		BigDecimal bhq = BigDecimal.ZERO;//银行-活期
		BigDecimal bdq = BigDecimal.ZERO;//银行-定期
		BigDecimal bdk = BigDecimal.ZERO;//银行-贷款户
		BigDecimal bbz = BigDecimal.ZERO;//银行-保证金
		BigDecimal bzy = BigDecimal.ZERO;//银行-专用户
		BigDecimal btz = BigDecimal.ZERO;//银行-通知
		BigDecimal bxd = BigDecimal.ZERO;//银行-协定
		BigDecimal bxn = BigDecimal.ZERO;//银行-虚拟户
		BigDecimal bpj = BigDecimal.ZERO;//票据余额
		if(!EmptyUtils.isEmptyList(voList)){
			for(FundsBalanceStatisticsVO vo : voList){
				//内部
				ixj = ixj.add(vo.getAccType11());
				ihq = ihq.add(vo.getAccType12());
				idq = idq.add(vo.getAccType13());
				itz = itz.add(vo.getAccType14());
				//银行
				bhq = bhq.add(vo.getAccType01());
				bdq = bdq.add(vo.getAccType02());
				bdk = bdk.add(vo.getAccType03());
				bbz = bbz.add(vo.getAccType04());
				bzy = bzy.add(vo.getAccType05());
				btz = btz.add(vo.getAccType06());
				bxd = bxd.add(vo.getAccType07());
				bxn = bxn.add(vo.getAccType09());
				//票据
				bpj = bpj.add(vo.getAccType10());
			}
		}
		//内部合计
		BigDecimal innerTotal = ixj.add(ihq).add(idq).add(itz);
		//银行合计
		BigDecimal bankTotal = bhq.add(bdq).add(bdk).add(bbz).add(bzy).add(btz).add(btz).add(bxd).add(bxn);
		Map<String,BigDecimal> map = new HashMap<>();
		map.put("ixj",ixj);
		map.put("ihq",ihq);
		map.put("idq",idq);
		map.put("itz",itz);
		map.put("bhq",bhq);
		map.put("bdq",bdq);
		map.put("bdk",bdk);
		map.put("bbz",bbz);
		map.put("bzy",bzy);
		map.put("btz",btz);
		map.put("bxd",bxd);
		map.put("bxn",bxn);
		map.put("innerTotal",innerTotal);
		map.put("bankTotal",bankTotal);
		return map;
	}
}
