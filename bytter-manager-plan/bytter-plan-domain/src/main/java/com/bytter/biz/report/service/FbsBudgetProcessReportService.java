package com.bytter.biz.report.service;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetDtlAbs;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.service.FbsItemService;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.vo.FbsBudgetProcessVO;
import com.bytter.biz.budget.vo.FbsBudgetSuperaddDetailVO;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.biz.report.dao.FbsBudgetProcessReportDao;
import com.bytter.biz.report.vo.FbsBudgetProcessReportVO;
import com.bytter.biz.report.vo.FbsBudgetProcessViewVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 资金计划上报报表
 *
 * @author zuoy 2018-02-06
 */
@Service("fbsBudgetProcessReportService")
@Transactional
public class FbsBudgetProcessReportService extends BaseService<FbsBudgetProcessReportVO, String> {
	@Autowired
	private FbsBudgetProcessReportDao fbsBudgetProcessReportDao;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@Autowired
	private BudgetUtilService budgetUtilService;
	@DubboReference(check = false)
	private IConfigService iConfigService;
	@Autowired
	private FbsItemService fbsItemService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@Override
	public BaseDao<FbsBudgetProcessReportVO, String> getEntityDao() {
		return fbsBudgetProcessReportDao;
	}
	public DataTablesOutput dataTable(DataTablesInput dataTablesInput) {
		List<FbsBudgetProcessViewVO> list = new ArrayList<>();
		String corpIdStr = String.valueOf(dataTablesInput.getFormdata().get("corpId"));
		String timeType = String.valueOf(dataTablesInput.getFormdata().get("timeType"));
		Date beginDate = DateUtils.parseDate(String.valueOf(dataTablesInput.getFormdata().get("beginDate")));
		Date endDate = DateUtils.parseDate(String.valueOf(dataTablesInput.getFormdata().get("endDate")));
		String[] corpIds = corpIdStr.split(CommonConstant.COMMA_SPLIT);
		// 获取周期
		Map<String, String> periods = budgetUtilService.getPeriodByTimeType(beginDate, endDate, timeType);
		if (!EmptyUtils.isEmpty(periods) && StringUtils.isNotBlank(corpIdStr)) {
			for(String corpId:corpIds) {
				CorpDTO corpDTO = this.iCorpService.get(corpId);
				// 组装数据
				Integer count = 0;
				Integer first = dataTablesInput.getStart();
				Integer maxResult = dataTablesInput.getStart() + dataTablesInput.getLength();
				for (Map.Entry<String, String> period : periods.entrySet()) {
					if (count >= first && count < maxResult) {
						FbsBudgetProcessViewVO vo = new FbsBudgetProcessViewVO();
						vo.setTimeType(timeType);
						vo.setCorpId(corpId);
						vo.setCorpName(corpDTO.getName());
						vo.setPeriod(period.getKey());
						String[] date = period.getValue().split("~");
						vo.setBeginDate(date[0]);
						vo.setEndDate(date[1]);
						list.add(vo);
					}
					count++;
				}
			}
		}
		return new DataTablesOutput(dataTablesInput.getDraw(), list, periods.size(), periods.size());
	}
	/**
	 * 资金计划查询报表
	 *
	 * @param timeType  预算类型
	 * @param corpId    单位ID
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 */
	public List<FbsBudgetProcessReportVO> listFbsBudgetProcessReport(String timeType, String corpId, Date beginDate, Date endDate) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		//年计划上报模式:0,年固定;1,年分解
		String yimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YIMODE).getValue();
		//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
		String mimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
		boolean resolveYear = BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equalsIgnoreCase(yimode);
		boolean resolveMonth = BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equalsIgnoreCase(mimode);
		if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && resolveYear) {
			tableName = "FBS_BUDGET_RESOLVE_YEAR";
		}
		if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && resolveMonth) {
			tableName = "FBS_BUDGET_RESOLVE_MONTH";
		}
		List<FbsBudgetProcessReportVO> reportList = this.fbsBudgetProcessReportDao.listFbsBudget(timeType, corpId, beginDate, endDate, tableName);
		List<String> corpList = new ArrayList<>();
		// 设置执行金额数据
		if (!EmptyUtils.isEmptyList(reportList)) {
			List<FbsBudgetProcessReportVO> processList = new ArrayList<>();
			List<FbsBudgetSuperaddDetailVO> superaddList = new ArrayList<>();
			//判断单位是否是汇总单位
			if (this.fbsCorpBudgetService.getChildCollectFlag(corpId)) {
				//包含本单位本身的计划执行
				corpList.add(corpId);
				//获取下级单位
				List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(corpId);
				list = this.setChildCorp(list);
				//取下级单位执行金额
				for(FbsCorpBudgetDTO dto : list){
					corpList.add(dto.getCorpId());
				}
				processList = this.fbsBudgetProcessReportDao.listByFbsBudgetProcess(corpList, beginDate, endDate, "");
				superaddList = this.fbsBudgetProcessReportDao.listByFbsBudgetSuperadd(timeType, corpList, beginDate, endDate);
			}else {
				processList = this.fbsBudgetProcessReportDao.listByFbsBudgetProcess(corpId, beginDate, endDate, "");
				superaddList = this.fbsBudgetProcessReportDao.listByFbsBudgetSuperadd(timeType, corpId, beginDate, endDate);
			}
			for (FbsBudgetProcessReportVO reportVO : reportList) {
				FbsItemVO item = fbsItemService.getFbsItemVO(null, reportVO.getItemCode());
				reportVO.setItem(item);
				if (!EmptyUtils.isEmptyList(processList)) {
					// 计算计划执行金额
					for (FbsBudgetProcessReportVO processVO : processList) {
						if (reportVO.getItemCode().equalsIgnoreCase(processVO.getItemCode())) {
							reportVO.setInProcessMoney(processVO.getInProcessMoney());
							reportVO.setInTempProcessMoney(processVO.getInTempProcessMoney());
							reportVO.setOutProcessMoney(processVO.getOutProcessMoney());
							reportVO.setOutTempProcessMoney(processVO.getOutTempProcessMoney());
						}
					}
					if (EmptyUtils.isEmpty(reportVO.getInProcessMoney())) {
						if (!EmptyUtils.isEmpty(reportVO.getItem()) && "0".equalsIgnoreCase(reportVO.getItem().getIsWrite())) {
							// 汇总上级科目的执行金额
							FbsBudgetProcessReportVO collectFbsBudgetProcessReportVO = new FbsBudgetProcessReportVO();
							//判断单位是否是汇总单位
							if (this.fbsCorpBudgetService.getChildCollectFlag(corpId)) {
								collectFbsBudgetProcessReportVO = this.fbsBudgetProcessReportDao.collectFbsBudgetProcess(corpList, beginDate, endDate, reportVO.getItemCode());
							}else {
								collectFbsBudgetProcessReportVO = this.fbsBudgetProcessReportDao.collectFbsBudgetProcess(corpId, beginDate, endDate, reportVO.getItemCode(),reportVO.getItem().getSign());
							}
							reportVO.setInProcessMoney(collectFbsBudgetProcessReportVO.getInProcessMoney());
							reportVO.setInTempProcessMoney(collectFbsBudgetProcessReportVO.getInTempProcessMoney());
							reportVO.setOutProcessMoney(collectFbsBudgetProcessReportVO.getOutProcessMoney());
							reportVO.setOutTempProcessMoney(collectFbsBudgetProcessReportVO.getOutTempProcessMoney());
						} else {
							reportVO.setInProcessMoney(BigDecimal.ZERO);
							reportVO.setInTempProcessMoney(BigDecimal.ZERO);
							reportVO.setOutProcessMoney(BigDecimal.ZERO);
							reportVO.setOutTempProcessMoney(BigDecimal.ZERO);
						}
					}
					// 计算可用余额、执行比例、可用余额占比
					reportVO.setCanBMoney(getCanBMoneyByCalculation(reportVO));
					reportVO.setExerciseRatio(getExerciseRatioByCalculation(reportVO));
					reportVO.setCanBMoneyRatio(getCanBMoneyRatioByCalculation(reportVO));
				} else {
					reportVO.setInProcessMoney(BigDecimal.ZERO);
					reportVO.setInTempProcessMoney(BigDecimal.ZERO);
					reportVO.setOutProcessMoney(BigDecimal.ZERO);
					reportVO.setOutTempProcessMoney(BigDecimal.ZERO);
					// 计算可用余额、执行比例、可用余额占比
					reportVO.setCanBMoney(getCanBMoneyByCalculation(reportVO));
					reportVO.setExerciseRatio(getExerciseRatioByCalculation(reportVO));
					reportVO.setCanBMoneyRatio(getCanBMoneyRatioByCalculation(reportVO));
				}
				// 计算误差率
				if (!EmptyUtils.isEmptyList(superaddList)) {
					for (FbsBudgetSuperaddDetailVO superaddVO : superaddList) {
						if (reportVO.getItemCode().equalsIgnoreCase(superaddVO.getItemCode())) {
							reportVO.setErrorRate(getErrorRateByCalculation(superaddVO, reportVO));
							break;
						}
					}
				}
				if(EmptyUtils.isEmpty(reportVO.getErrorRate())){
					reportVO.setErrorRate(BigDecimal.ZERO);
				}
				if(EmptyUtils.isEmpty(reportVO.getExerciseRatio())){
					reportVO.setExerciseRatio(BigDecimal.ZERO);
				}
			}
		}
		return reportList;
	}
	/**
	 * 资金计划查询报表
	 *
	 * @param timeType  预算类型
	 * @param corpId    单位ID
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 */
	public List<FbsBudgetProcessReportVO> listFbsBudgetProcessReport(String timeType, String[] corpId, Date beginDate, Date endDate,String[] itemCode) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		//年计划上报模式:0,年固定;1,年分解
		String yimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YIMODE).getValue();
		//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
		String mimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
		boolean resolveYear = BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equalsIgnoreCase(yimode);
		boolean resolveMonth = BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equalsIgnoreCase(mimode);
		if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && resolveYear) {
			tableName = "FBS_BUDGET_RESOLVE_YEAR";
		}
		if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && resolveMonth) {
			tableName = "FBS_BUDGET_RESOLVE_MONTH";
		}
		List<FbsBudgetProcessReportVO> reportList = this.fbsBudgetProcessReportDao.listFbsBudget(timeType, corpId, beginDate, endDate, tableName,itemCode);
		List<String> corpList = new ArrayList<>();
		// 设置执行金额数据
		if (!EmptyUtils.isEmptyList(reportList)) {
			List<FbsBudgetProcessReportVO> processList = new ArrayList<>();
			List<FbsBudgetSuperaddDetailVO> superaddList = new ArrayList<>();
			for(int i =0; i<corpId.length;i++) {
				//判断单位是否是汇总单位
				if (this.fbsCorpBudgetService.getChildCollectFlag(corpId[i])) {
					//包含本单位本身的计划执行
					corpList.add(corpId[i]);
					//获取下级单位
					List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(corpId[i]);
					list = this.setChildCorp(list);
					//取下级单位执行金额
					for (FbsCorpBudgetDTO dto : list) {
						corpList.add(dto.getCorpId());
					}
				} else {
					//包含本单位本身的计划执行
					corpList.add(corpId[i]);
				}
			}
			processList = this.fbsBudgetProcessReportDao.listByFbsBudgetProcess(corpList, beginDate, endDate, "");
			superaddList = this.fbsBudgetProcessReportDao.listByFbsBudgetSuperadd(timeType, corpList, beginDate, endDate);
			for (FbsBudgetProcessReportVO reportVO : reportList) {
				reportVO.setItem(fbsItemService.getFbsItemVO(null, reportVO.getItemCode()));
				if (!EmptyUtils.isEmptyList(processList)) {
					// 计算计划执行金额
					for (FbsBudgetProcessReportVO processVO : processList) {
						if (reportVO.getItemCode().equalsIgnoreCase(processVO.getItemCode())) {
							reportVO.setInProcessMoney(processVO.getInProcessMoney());
							reportVO.setInTempProcessMoney(processVO.getInTempProcessMoney());
							reportVO.setOutProcessMoney(processVO.getOutProcessMoney());
							reportVO.setOutTempProcessMoney(processVO.getOutTempProcessMoney());
						}
					}
					if (EmptyUtils.isEmpty(reportVO.getInProcessMoney())) {
						if (!EmptyUtils.isEmpty(reportVO.getItem()) && "0".equalsIgnoreCase(reportVO.getItem().getIsWrite())) {
							// 汇总上级科目的执行金额
							FbsBudgetProcessReportVO collectFbsBudgetProcessReportVO = new FbsBudgetProcessReportVO();
							collectFbsBudgetProcessReportVO = this.fbsBudgetProcessReportDao.collectFbsBudgetProcess(corpList, beginDate, endDate, reportVO.getItemCode());
							reportVO.setInProcessMoney(collectFbsBudgetProcessReportVO.getInProcessMoney());
							reportVO.setInTempProcessMoney(collectFbsBudgetProcessReportVO.getInTempProcessMoney());
							reportVO.setOutProcessMoney(collectFbsBudgetProcessReportVO.getOutProcessMoney());
							reportVO.setOutTempProcessMoney(collectFbsBudgetProcessReportVO.getOutTempProcessMoney());
						} else {
							reportVO.setInProcessMoney(BigDecimal.ZERO);
							reportVO.setInTempProcessMoney(BigDecimal.ZERO);
							reportVO.setOutProcessMoney(BigDecimal.ZERO);
							reportVO.setOutTempProcessMoney(BigDecimal.ZERO);
						}
					}
					// 计算可用余额、执行比例、可用余额占比
					reportVO.setCanBMoney(getCanBMoneyByCalculation(reportVO));
					reportVO.setExerciseRatio(getExerciseRatioByCalculation(reportVO));
					reportVO.setCanBMoneyRatio(getCanBMoneyRatioByCalculation(reportVO));
				} else {
					reportVO.setInProcessMoney(BigDecimal.ZERO);
					reportVO.setInTempProcessMoney(BigDecimal.ZERO);
					reportVO.setOutProcessMoney(BigDecimal.ZERO);
					reportVO.setOutTempProcessMoney(BigDecimal.ZERO);
					// 计算可用余额、执行比例、可用余额占比
					reportVO.setCanBMoney(getCanBMoneyByCalculation(reportVO));
					reportVO.setExerciseRatio(getExerciseRatioByCalculation(reportVO));
					reportVO.setCanBMoneyRatio(getCanBMoneyRatioByCalculation(reportVO));
				}
				// 计算误差率
				if (!EmptyUtils.isEmptyList(superaddList)) {
					for (FbsBudgetSuperaddDetailVO superaddVO : superaddList) {
						if (reportVO.getItemCode().equalsIgnoreCase(superaddVO.getItemCode())) {
							reportVO.setErrorRate(getErrorRateByCalculation(superaddVO, reportVO));
							break;
						}
					}
				}
				if(EmptyUtils.isEmpty(reportVO.getErrorRate())){
					reportVO.setErrorRate(BigDecimal.ZERO);
				}
				if(EmptyUtils.isEmpty(reportVO.getExerciseRatio())){
					reportVO.setExerciseRatio(BigDecimal.ZERO);
				}
			}
		}
		return reportList;
	}
	/**
	 * 计算可用金额
	 * 上期留存金额 = 上期上报金额 + 上期追加金额（已追加） - 上期已执行金额（内部） - 上期已执行金额（外部）
	 *
	 * @return
	 */
	/*public BigDecimal getRemainBMoneyByCalculation(FbsBudgetProcessReportVO lastReportVO, FbsBudgetProcessReportVO lastProcessVO) {
		BigDecimal remainBMoney = lastReportVO.getbMoney().add(lastReportVO.getBsuperMoney());
		if (!EmptyUtils.isEmpty(lastProcessVO)) {
			remainBMoney = remainBMoney.subtract(lastProcessVO.getInProcessMoney()).subtract(lastProcessVO.getOutProcessMoney());
		}
		return remainBMoney;
	}*/
	/**
	 * 计算可用金额
	 * 可用金额：=上报金额+已追加金额-执行金额（内部）-执行金额（外部）-在途金额（内部）-在途金额（外部）
	 *
	 * @return
	 */
	public BigDecimal getCanBMoneyByCalculation(FbsBudgetProcessReportVO reportVO) {
		return reportVO.getbMoney().add(reportVO.getBsuperMoney()).subtract(reportVO.getInProcessMoney()).subtract(reportVO.getOutProcessMoney()).subtract(reportVO.getInTempProcessMoney()).subtract(reportVO.getOutTempProcessMoney());
	}
	/**
	 * 计算执行比例
	 * 执行比例 = （执行金额（内部）+执行金额（外部）*100/（上报金额+已追加金额）
	 *
	 * @return
	 */
	public BigDecimal getExerciseRatioByCalculation(FbsBudgetProcessReportVO reportVO) {
		BigDecimal curProcessMoney = reportVO.getInProcessMoney().add(reportVO.getOutProcessMoney());
		BigDecimal curBMoney = reportVO.getbMoney().add(reportVO.getBsuperMoney());
		if (BigDecimal.ZERO.intValue() == curBMoney.intValue()) {
			return BigDecimal.ZERO;
		}
		BigDecimal exerciseRatio = curProcessMoney.multiply(new BigDecimal(100)).divide(curBMoney, 6, RoundingMode.HALF_UP);
		return exerciseRatio.abs();
	}
	/**
	 * 计算误差率
	 * 误差率：= （追加金额+追减金额绝对值）*100/（上报金额+追加金额-追减金额）
	 *
	 * @return
	 */
	public BigDecimal getErrorRateByCalculation(FbsBudgetSuperaddDetailVO superaddVO, FbsBudgetProcessReportVO reportVO) {
		BigDecimal curBMoney = reportVO.getbMoney().add(reportVO.getBsuperMoney());
		if (BigDecimal.ZERO.intValue() == curBMoney.intValue()) {
			return BigDecimal.ZERO;
		}
		return superaddVO.getBsuperMoney().multiply(new BigDecimal(100)).divide(curBMoney, 6, RoundingMode.HALF_UP);
	}
	/**
	 * 计算可用余额占比
	 * 可用余额占比：=可用金额*100/（上报金额+已追加金额）
	 *
	 * @return
	 */
	public BigDecimal getCanBMoneyRatioByCalculation(FbsBudgetProcessReportVO reportVO) {
		BigDecimal curBMoney = reportVO.getbMoney().add(reportVO.getBsuperMoney());
		//上报金额为0 或者 可用金额小于等于0 时，可用余额占比为0
		if (BigDecimal.ZERO.intValue() == curBMoney.intValue()
				|| BigDecimal.ZERO.compareTo(reportVO.getCanBMoney()) >= 0) {
			return BigDecimal.ZERO;
		}
		return reportVO.getCanBMoney().multiply(new BigDecimal(100)).divide(curBMoney, 6, RoundingMode.HALF_UP);
	}
	/**
	 * 显示预算执行明细数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTablesProcessInfo(DataTablesInput dataTablesInput) {
		String corpId = dataTablesInput.getFormdata().get("eqs_corpId").toString();
		String joinsql = "";
		if(corpId.contains(",")) {
			String[] corpIds = corpId.split(",");
			dataTablesInput.getFormdata().remove("eqs_corpId");
			List<String> corpList = new ArrayList<>();
			for(int i=0;i<corpIds.length;i++){
				//包含本单位本身的计划执行
				corpList.add(corpIds[i]);
				if (this.fbsCorpBudgetService.getChildCollectFlag(corpIds[i])) {
					//获取下级单位
					List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(corpIds[i]);
					list = this.setChildCorp(list);
					//取下级单位执行金额
					for (FbsCorpBudgetDTO dto : list) {
						corpList.add(dto.getCorpId());
					}
				}
			}
			joinsql = SqlUtil.getInSql(corpList,"t.corp_id",true);
		}else{
			//判断单位是否是汇总单位
			if (this.fbsCorpBudgetService.getChildCollectFlag(corpId)) {
				List<String> corpList = new ArrayList<>();
				//包含本单位本身的计划执行
				corpList.add(corpId);
				//获取下级单位
				List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(corpId);
				list = this.setChildCorp(list);
				//取下级单位执行金额
				for (FbsCorpBudgetDTO dto : list) {
					corpList.add(dto.getCorpId());
				}
				joinsql = SqlUtil.getInSql(corpList,"t.corp_id",true);
				dataTablesInput.getFormdata().remove("eqs_corpId");
			}
		}
		return this.fbsBudgetProcessReportDao.getDataTablesProcessInfo(dataTablesInput,joinsql);
	}
	/**
	 * 显示资金计划追加明细数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTablesSuperaddInfo(DataTablesInput dataTablesInput) {
		return this.fbsBudgetProcessReportDao.getDataTablesSuperaddInfo(dataTablesInput);
	}
	/**
	 * 手机端显示资金计划执行明细数据(执行金额取折合人民币)
	 * @param itemCode
	 * @param corpId
	 * @param beginDate
	 * @param endDate
	 * @param statusFlag
	 * @return
	 */
	public List<FbsBudgetProcessVO> mobileFbsBudgetProessDetailList(String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag) {
		return this.fbsBudgetProcessReportDao.mobileFbsBudgetProessDetailList(itemCode,corpId,beginDate,endDate,statusFlag);
	}
	/**
	 * 明细计划执行查询
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTablesDetailProcessInfo(DataTablesInput dataTablesInput) {
		//获取计划
		String id = (String) dataTablesInput.getFormdata().get("eqs_budgetId");
		FbsBudget fbsBudget = fbsBudgetService.get(id);
		DataTablesOutput data =new DataTablesOutput ();
		if(id.equals("-1")){
			return data;
		}
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(fbsBudget.getTimeType());
		data =  this.fbsBudgetProcessReportDao.getDataTablesDetailProcessInfo(dataTablesInput, tableName);
		return setBudgetProcessDetailReport(fbsBudget, data);
	}
	/**
	 * 查询计划明细执行
	 * @param fbsBudget
	 * @return
	 */
	public DataTablesOutput setBudgetProcessDetailReport(FbsBudget fbsBudget, DataTablesOutput data) {
		if(EmptyUtils.isEmpty(data)){
			return data;
		}
		for(FbsBudgetProcessReportVO reportVO:(List<FbsBudgetProcessReportVO>)data.getData()){
			reportVO.setbMoney(reportVO.getbMoney()==null?BigDecimal.ZERO:reportVO.getbMoney());
			reportVO.setBsuperMoney(reportVO.getBsuperMoney()==null?BigDecimal.ZERO:reportVO.getBsuperMoney());
			reportVO.setBtempMoney(reportVO.getBtempMoney()==null?BigDecimal.ZERO:reportVO.getBtempMoney());
			reportVO.setbMoneySum(reportVO.getbMoney().add(reportVO.getBsuperMoney()));
			reportVO.setRemainBMoney(BigDecimal.ZERO);
			reportVO.setItem(fbsItemService.getFbsItemVO(null, reportVO.getItemCode()));
			List<String> corpList = new ArrayList<>();
			//判断单位是否是汇总单位
			if (this.fbsCorpBudgetService.getChildCollectFlag(fbsBudget.getCorpId())) {
				//包含本单位本身的计划执行
				corpList.add(fbsBudget.getCorpId());
				//获取下级单位
				List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(fbsBudget.getCorpId());
				//取下级单位执行金额
				for(FbsCorpBudgetDTO dto : list){
					corpList.add(dto.getCorpId());
				}
			}else{
				corpList.add(fbsBudget.getCorpId());
			}
			// 汇总上级科目的执行金额
			FbsBudgetProcessReportVO collectFbsBudgetProcessReportVO = this.fbsBudgetProcessReportDao.getBudgetProcessMoneyByDetail(fbsBudget.getBeginDate(), fbsBudget.getEndDate(), reportVO.getItemCode(), corpList, reportVO);
			reportVO.setInProcessMoney(collectFbsBudgetProcessReportVO.getInProcessMoney());
			reportVO.setInTempProcessMoney(collectFbsBudgetProcessReportVO.getInTempProcessMoney());
			reportVO.setOutProcessMoney(collectFbsBudgetProcessReportVO.getOutProcessMoney());
			reportVO.setOutTempProcessMoney(collectFbsBudgetProcessReportVO.getOutTempProcessMoney());
			// 计算可用余额、执行比例、可用余额占比
			reportVO.setCanBMoney(getCanBMoneyByCalculation(reportVO));
		}
		return data;
	}
	/**
	 * 获取下级单位直到单位不是汇总单位
	 * @param list
	 */
	public List<FbsCorpBudgetDTO>  setChildCorp(List<FbsCorpBudgetDTO> list){
		List<FbsCorpBudgetDTO> temp = new ArrayList<>();
		for(FbsCorpBudgetDTO fbsCorpBudgetDTO : list){
			if(fbsCorpBudgetDTO.getCollectFlag() == 1){
				List<FbsCorpBudgetDTO> tempList = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(fbsCorpBudgetDTO.getCorpId());
				if(EmptyUtils.isNotEmpty(tempList)) {
					temp.addAll(this.setChildCorp(tempList));
				}
			}
		}
		temp.addAll(list);
		return temp;
	}
}
