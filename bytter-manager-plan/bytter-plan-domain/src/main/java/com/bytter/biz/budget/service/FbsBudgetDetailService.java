package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsBudgetDetailDao;
import com.bytter.biz.budget.entity.*;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.budget.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_BUDGET_DETAIL
 * @date 2017-07-19 14:59:31
 */
@Service("fbsBudgetDetailService")
@Transactional
public class FbsBudgetDetailService extends BaseService<FbsBudgetDetail, String> implements IUserExcelVerifyHandler<FbsBudgetDetailVO> {
	@Autowired
	private FbsBudgetDetailDao fbsBudgetDetailDao;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@DubboReference(check = false)
	private IFbsItemService iFbsItemService;
	@Autowired
	private FbsBudgetSuperaddService fbsBudgetSuperaddService;
	@Autowired
	private FbsBudgetSuperaddDetailService fbsBudgetSuperaddDetailService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@Autowired
	private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService iFcExcrateAdjustService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FbsBudgetDetail entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(FbsBudgetDetail entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FbsBudgetDetail entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FbsBudgetDetail entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FbsBudgetDetail, String> getEntityDao() {
		return fbsBudgetDetailDao;
	}
	/**
	 * 进入明细录入界面
	 *
	 * @param model
	 */
	public void inputDtl(FbsBudgetDetailVO vo, FbsBudgetDetailViewVO model) {
		String budgetId = "";//计划id
		String flag = "";//标志 判断是计划录入 还是计划追加 input为录入 add为追加
		String detailId = "";//明细id
		detailId = vo.getDetailId();
		if (!StringUtils.isEmpty(vo.getSuperaddId())) {
			flag = "add";
			FbsBudgetSuperadd addEntity = fbsBudgetSuperaddService.get(vo.getSuperaddId());
			budgetId = addEntity.getBudgetId();
		} else {
			flag = "input";
			budgetId = vo.getBudgetId();
		}
		FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
		/**得到表格的列*/
		List<String> dtlColList = new ArrayList<String>();
		List<String> dtlColValueList = new ArrayList<String>();
		String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
		dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
		String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
		for (int i = 0; i < dtlCols.length; i++) {
			dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
			dtlColValueList.add(dtlCols[i]);
		}
		FbsBudgetDtlAbs budgetDtl = null;
		String dtlId = "";
		String itemCode = "";
		if ("add".equals(flag)) {
			FbsBudgetSuperaddDetail addDetail = fbsBudgetSuperaddDetailService.get(detailId);
			FbsBudgetVO budgetVO = fbsBudgetService.getBudget(budget.getId());
			for (int i = 0; i < budgetVO.getDtlList().size(); i++) {
				if (addDetail.getItemCode().equals(budgetVO.getDtlList().get(i).getItemCode())) {
					dtlId = budgetVO.getDtlList().get(i).getId();
					itemCode = budgetVO.getDtlList().get(i).getItemCode();
					model.setBudgetDtl(  budgetVO.getDtlList().get(i));
				}
			}
		} else {
			budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(detailId, entity);
			dtlId = budgetDtl.getId();
			itemCode = budgetDtl.getItemCode();
			model.setBudgetDtl(  budgetDtl);
		}
		//当前上报明细的科目信息
		model.setInputItem(  iFbsItemService.getFbsItemByCode(itemCode));
		List<FbsBudgetDetailVO> dataList = this.getBtTable(dtlId);
		if (!EmptyUtils.isEmptyList(dataList)) {
			for (FbsBudgetDetailVO fbsBudgetDetailVO : dataList) {
				if ("add".equals(flag)) {
					if (EmptyUtils.isEmpty(fbsBudgetDetailVO.getBtempMoney())) {
						fbsBudgetDetailVO.setBtempMoney(BigDecimal.ZERO);
					}
					fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getBtempNatualMoney());
				} else {
					fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getNatualMoney());
				}
			}
		}
		if (EmptyUtils.isEmptyList(dataList)) {
			dataList = new ArrayList<>();
			FbsBudgetDetailVO vo1 = new FbsBudgetDetailVO();
			if (DateUtils.getDate().getTime() > budget.getBeginDate().getTime()
					&& DateUtils.getDate().getTime() < budget.getEndDate().getTime()) {
				vo1.setVestingDate(DateUtils.getDate());
			} else {
				vo1.setVestingDate(budget.getBeginDate());
			}
			dataList.add(vo1);
		}
		String beginDate = DateUtils.formatDate(budget.getBeginDate());
		String endDate = DateUtils.formatDate(budget.getEndDate());
		model.setFlag(  flag);
		model.setDataList( dataList);
		model.setBudget(  budget);
		model.setDtlColList(  dtlColList);
		model.setDtlColValueList(  dtlColValueList);
		model.setBeginDate(  beginDate);
		model.setEndDate( endDate);
	}
    public void inputDtl(FbsBudgetDetailVO vo, Model model) {
        String budgetId = "";//计划id
        String flag = "";//标志 判断是计划录入 还是计划追加 input为录入 add为追加
        String detailId = "";//明细id
        detailId = vo.getDetailId();
        if (!StringUtils.isEmpty(vo.getSuperaddId())) {
            flag = "add";
            FbsBudgetSuperadd addEntity = fbsBudgetSuperaddService.get(vo.getSuperaddId());
            budgetId = addEntity.getBudgetId();
        } else {
            flag = "input";
            budgetId = vo.getBudgetId();
        }
        FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
        String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
        /**得到表格的列*/
        List<String> dtlColList = new ArrayList<String>();
        List<String> dtlColValueList = new ArrayList<String>();
        String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
        dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
        String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < dtlCols.length; i++) {
            dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
            dtlColValueList.add(dtlCols[i]);
        }
        FbsBudgetDtlAbs budgetDtl = null;
        String dtlId = "";
        String itemCode = "";
        if ("add".equals(flag)) {
            FbsBudgetSuperaddDetail addDetail = fbsBudgetSuperaddDetailService.get(detailId);
            FbsBudgetVO budgetVO = fbsBudgetService.getBudget(budget.getId());
            for (int i = 0; i < budgetVO.getDtlList().size(); i++) {
                if (addDetail.getItemCode().equals(budgetVO.getDtlList().get(i).getItemCode())) {
                    dtlId = budgetVO.getDtlList().get(i).getId();
                    itemCode = budgetVO.getDtlList().get(i).getItemCode();
                    model.addAttribute("budgetDtl", budgetVO.getDtlList().get(i));
                }
            }
        } else {
            budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(detailId, entity);
            dtlId = budgetDtl.getId();
            itemCode = budgetDtl.getItemCode();
            model.addAttribute("budgetDtl", budgetDtl);
        }
        //当前上报明细的科目信息
        model.addAttribute("inputItem", iFbsItemService.getFbsItemByCode(itemCode));
        List<FbsBudgetDetailVO> dataList = this.getBtTable(dtlId);
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (FbsBudgetDetailVO fbsBudgetDetailVO : dataList) {
                if ("add".equals(flag)) {
                    if (EmptyUtils.isEmpty(fbsBudgetDetailVO.getBtempMoney())) {
                        fbsBudgetDetailVO.setBtempMoney(BigDecimal.ZERO);
                    }
                    fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getBtempNatualMoney());
                } else {
                    fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getNatualMoney());
                }
            }
        }
        if (EmptyUtils.isEmptyList(dataList)) {
            dataList = new ArrayList<>();
            FbsBudgetDetailVO vo1 = new FbsBudgetDetailVO();
            if (DateUtils.getDate().getTime() > budget.getBeginDate().getTime()
                    && DateUtils.getDate().getTime() < budget.getEndDate().getTime()) {
                vo1.setVestingDate(DateUtils.getDate());
            } else {
                vo1.setVestingDate(budget.getBeginDate());
            }
            dataList.add(vo1);
        }
        String beginDate = DateUtils.formatDate(budget.getBeginDate());
        String endDate = DateUtils.formatDate(budget.getEndDate());
        model.addAttribute("flag", flag);
        model.addAttribute("dataList", dataList);
        model.addAttribute("budget", budget);
        model.addAttribute("dtlColList", dtlColList);
        model.addAttribute("dtlColValueList", dtlColValueList);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
    }
	/**
	 * 根据预算明细id得到科目所有明细
	 *
	 * @return
	 */
	public List<FbsBudgetDetailVO> getBtTable(String detailId) {
		List<FbsBudgetDetailVO> list = new ArrayList<FbsBudgetDetailVO>();
		List<FbsBudgetDetail> dtlList = fbsBudgetDetailDao.getDetailListById(detailId);
		if (null != dtlList) {
			list = BeanUtils.copyListProperties(dtlList, FbsBudgetDetailVO.class);
			// inject(list);
		}
		return list;
	}
	/**
	 * 根据计划明细id得到科目所有明细
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FbsBudgetDetail> getDetailListById(String detailId) {
		return fbsBudgetDetailDao.getDetailListById(detailId);
	}
	/**
	 * 批量保存明细
	 *
	 * @param bean
	 * @throws Exception
	 */
	public Map<String, Object> toSave(FbsBudgetDetailTransmitVO bean) throws Exception {
		//年计划上报模式:0,年固定;1,年分解
		String yimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE);
		//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		String wcrl = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WCRL);
		BigDecimal result = BigDecimal.ZERO;
		Map<String, Object> map = new HashMap<String, Object>();
		FbsBudget budget = fbsBudgetService.get(bean.getBudgetId(), true);//根据id得到预算计划主表
		String itemCode = "";
		String itemName = "";
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
		FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(bean.getDetailId(), entity);
		itemCode = budgetDtl.getItemCode();
		itemName = budgetDtl.getItemName();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<FbsBudgetDetail> list = new ArrayList<>();
		if (!EmptyUtils.isEmpty(bean.getVestingDate())) {
			for (int i = 0; i < bean.getVestingDate().length; i++) {
				FbsBudgetDetail detail = new FbsBudgetDetail();
				BigDecimal tempNatualMoney = BigDecimal.ZERO;
				if (!EmptyUtils.isEmptyArray(bean.getbMoney()) && !EmptyUtils.isBlank(bean.getbMoney()[i])) {
					detail.setBMoney(new BigDecimal(bean.getbMoney()[i]));
				}
				if (!EmptyUtils.isEmptyArray(bean.getBtempMoney()) && !EmptyUtils.isBlank(bean.getBtempMoney()[i])) {
					detail.setBtempMoney(new BigDecimal(bean.getBtempMoney()[i]));
				}
				// zuoy 2018-11-08 折合人民币
//				if (!EmptyUtils.isEmptyArray(bean.getTempNatualMoney()) && !EmptyUtils.isBlank(bean.getTempNatualMoney()[i])) {
//					if ("input".equals(bean.getFlag())) {
//						detail.setNatualMoney(new BigDecimal(bean.getTempNatualMoney()[i]));
//					} else {
//						detail.setBtempNatualMoney(new BigDecimal(bean.getTempNatualMoney()[i]));
//					}
//				}
				if ("input".equals(bean.getFlag())) {
					result = result.add(detail.getBMoney());
					tempNatualMoney = detail.getBMoney();
				}else{
					result = result.add(detail.getBtempMoney());
					tempNatualMoney = detail.getBtempMoney();
				}
				if (null != bean.getId() && bean.getId().length >0 && !StringUtils.isEmpty(bean.getId()[i])) {
					FbsBudgetDetail t = this.get(bean.getId()[i], true);
					if (!EmptyUtils.isEmpty(t)) {
						if (!"input".equals(bean.getFlag())) {
							detail.setNatualMoney(t.getNatualMoney());
						}
						detail.setBsuperMoney(t.getBsuperMoney());
						detail.setBsuperNatualMoney(t.getBsuperNatualMoney());
					}
				}
				detail.setDetailId(bean.getDetailId());
				detail.setItemCode(itemCode);
				detail.setItemName(itemName);
				detail.setTimeType(budget.getTimeType());
				if (!EmptyUtils.isEmptyArray(bean.getCurId())) {
					detail.setCurId(bean.getCurId()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getSettlType())) {
					detail.setSettlType(bean.getSettlType()[i]);
				}
				if (EmptyUtils.isBlank(detail.getSettlType())) {
					detail.setSettlType(BudgetConstant.SETTLTYPE_0);
				}
				if (!EmptyUtils.isEmptyArray(bean.getCorpName())) {
					detail.setCorpName(bean.getCorpName()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getPurpose())) {
					detail.setPurpose(bean.getPurpose()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getWishPayday()) && !EmptyUtils.isEmpty(bean.getWishPayday()[i])) {
					detail.setWishPayday(format.parse(bean.getWishPayday()[i].trim()));
				}
				if (!EmptyUtils.isEmptyArray(bean.getPayerAcc())) {
					detail.setPayerAcc(bean.getPayerAcc()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getPayeeAcc())) {
					detail.setPayeeAcc(bean.getPayeeAcc()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getPayeeAccName())) {
					detail.setPayeeAccName(bean.getPayeeAccName()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getPayeeBank())) {
					detail.setPayeeBank(bean.getPayeeBank()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getContract())) {
					detail.setContract(bean.getContract()[i]);
				}
				detail.setVestingDate(format.parse(bean.getVestingDate()[i].trim()));
				list.add(detail);
				if (!this.fbsCorpBudgetService.getChildCollectFlag(budget.getCorpId())) {//不是汇总单位才会根据参数判断
					if (BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(yimode) && "input".equals(bean.getFlag()) && "1".equals(budget.getTimeType())) {
						Date date = detail.getVestingDate();
						String month = String.valueOf(DateUtils.getMonth(date));
						BigDecimal money = (BigDecimal) map.get(month);
						if (null != money) {
							map.put(month, money.add(tempNatualMoney));
						} else {
							map.put(month, tempNatualMoney);
						}
					} else if ("0".equals(wcrl) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)
							&& "input".equals(bean.getFlag()) && "3".equals(budget.getTimeType())) {
						// 月滚动+双月周明细  录入明细挂到周计划上
						BigDecimal money = (BigDecimal) map.get("1");
						if (null != money) {
							map.put("1", money.add(tempNatualMoney));
						} else {
							map.put("1", tempNatualMoney);
						}
					} else if (!"0".equals(wcrl) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)
							&& "input".equals(bean.getFlag()) && "4".equals(budget.getTimeType())) {
						// 月滚动+双月周明细  周不上报  录入明细挂到当月计划上
						Date date = detail.getVestingDate();
						FbsBudgetPeriodVO vo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(date, BudgetConstant.TIME_TYPE_WEEK);
						List<FbsBudgetPeriodVO> voList = fbsBudgetPeriodService.autoAenerateByMonth(date);
						String key = "";
						for (int k = 0; k < voList.size(); k++) {
							if (vo.getBeginDate().getTime() == voList.get(k).getBeginDate().getTime()) {
								key = (k + 4) + "";
							}
						}
						BigDecimal money = (BigDecimal) map.get(key);
						if (null != money) {
							map.put(key, money.add(tempNatualMoney));
						} else {
							map.put(key, tempNatualMoney);
						}
					} else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)  && "3".equals(budget.getTimeType())) {
						Date date = detail.getVestingDate();
						FbsBudgetPeriodVO vo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(date, BudgetConstant.TIME_TYPE_WEEK);
						List<FbsBudgetPeriodVO> voList = fbsBudgetPeriodService.autoAenerateByMonth(date);
						String key = "";
						for (int k = 0; k < voList.size(); k++) {
							if (vo.getBeginDate().getTime() == voList.get(k).getBeginDate().getTime()) {
								key = (k + 1) + "";
							}
						}
						BigDecimal money = (BigDecimal) map.get(key);
						if (null != money) {
							map.put(key, money.add(tempNatualMoney));
						} else {
							map.put(key, tempNatualMoney);
						}
					}
				}
			}
		}
		// 先删除之前的明细
		this.fbsBudgetDetailDao.delByDetailId(bean.getDetailId());
		this.batchSave(list);
		map.put("totle", result);
		return map;
	}
	/**
	 * 查看上期明细
	 *
	 * @param model
	 */
	public void viewSuperDetail(FbsBudgetDetailVO vo, Model model) {
		String budgetId = "";//计划id
		String flag = vo.getFlag();//标志 判断是计划录入 还是计划追加 input为录入 add为追加
		String detailId = "";//明细id
		detailId = vo.getDetailId();
		budgetId = vo.getBudgetId();
		FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
		/**得到表格的列*/
		List<String> dtlColList = new ArrayList<String>();
		List<String> dtlColValueList = new ArrayList<String>();
		String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
		dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
		String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
		for (int i = 0; i < dtlCols.length; i++) {
			dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
			dtlColValueList.add(dtlCols[i]);
		}
		FbsBudgetDtlAbs budgetDtl = null;
		String dtlId = "";
		String itemCode = "";
		budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(detailId, entity);
		itemCode = budgetDtl.getItemCode();
		model.addAttribute("budgetDtl", budgetDtl);
		/** 拿到上期的预算和预算明细 begin*/
		FbsBudgetPeriodVO vo2 = fbsBudgetPeriodService.getSuperBudgetPeriodByTimeType(budget.getBeginDate(), budget.getTimeType());
		FbsBudget superBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), vo2.getBeginDate(), vo2.getEndDate());
		FbsBudgetDtlAbs superBudgetDtl = null;
		if (null != superBudget) {
			superBudgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlByItemCode(itemCode, superBudget.getId(), entity);
		}
		/** 拿到上期的预算和预算明细 end*/
		if (null != superBudgetDtl) {
			dtlId = superBudgetDtl.getId();
		}
		List<FbsBudgetDetailVO> dataList = this.getBtTable(dtlId);
		String beginDate = DateUtils.formatDate(vo2.getBeginDate());
		String endDate = DateUtils.formatDate(vo2.getEndDate());
		model.addAttribute("flag", flag);
		model.addAttribute("dataList", dataList);
		model.addAttribute("budget", superBudget);
		model.addAttribute("dtlColList", dtlColList);
		model.addAttribute("dtlColValueList", dtlColValueList);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
	}

    public void viewSuperDetail(FbsBudgetDetailVO vo, FbsBudgetDetailViewVO model) {
        String budgetId = "";//计划id
        String flag = vo.getFlag();//标志 判断是计划录入 还是计划追加 input为录入 add为追加
        String detailId = "";//明细id
        detailId = vo.getDetailId();
        budgetId = vo.getBudgetId();
        FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
        String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
        /**得到表格的列*/
        List<String> dtlColList = new ArrayList<String>();
        List<String> dtlColValueList = new ArrayList<String>();
        String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
        dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
        String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < dtlCols.length; i++) {
            dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
            dtlColValueList.add(dtlCols[i]);
        }
        FbsBudgetDtlAbs budgetDtl = null;
        String dtlId = "";
        String itemCode = "";
        budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(detailId, entity);
        itemCode = budgetDtl.getItemCode();
        model.setBudgetDtl( budgetDtl);
        /** 拿到上期的预算和预算明细 begin*/
        FbsBudgetPeriodVO vo2 = fbsBudgetPeriodService.getSuperBudgetPeriodByTimeType(budget.getBeginDate(), budget.getTimeType());
        FbsBudget superBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), vo2.getBeginDate(), vo2.getEndDate());
        FbsBudgetDtlAbs superBudgetDtl = null;
        if (null != superBudget) {
            superBudgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlByItemCode(itemCode, superBudget.getId(), entity);
        }
        /** 拿到上期的预算和预算明细 end*/
        if (null != superBudgetDtl) {
            dtlId = superBudgetDtl.getId();
        }
        List<FbsBudgetDetailVO> dataList = this.getBtTable(dtlId);
        String beginDate = DateUtils.formatDate(vo2.getBeginDate());
        String endDate = DateUtils.formatDate(vo2.getEndDate());
        model.setFlag(  flag);
        model.setDataList(  dataList);
        model.setBudget( superBudget);
        model.setDtlColList( dtlColList);
        model.setDtlColValueList(  dtlColValueList);
        model.setBeginDate( beginDate);
        model.setEndDate( endDate);
    }
	/**
	 * 汇总时汇总明细
	 *
	 * @param budgetId
	 * @param childBudgetId
	 */
	public void colloctDetail(String budgetId, List<String> childBudgetId) {
		FbsBudget fbsBudget = fbsBudgetService.get(budgetId);
		//先删除汇总明细
		//this.delColloctDetail(budgetId,fbsBudget.getTimeType());
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(fbsBudget.getTimeType());
		//把下级单位的明细插入汇总单位
		for (int i = 0; i < childBudgetId.size(); i++) {
			FbsBudgetVO vo = fbsBudgetService.getBudgetNotItem(childBudgetId.get(i));
			if (null == vo || null == vo.getDtlList()) {
				continue;
			}
			List<FbsBudgetDetail> allDetails = fbsBudgetDetailDao.getDetailListByBudgetId(vo.getId(),entity);
			if(EmptyUtils.isEmptyList(allDetails)){
				continue;
			}
			for (int j = 0; j < vo.getDtlList().size(); j++) {
//				List<FbsBudgetDetail> details = fbsBudgetDetailDao.getDetailListById(vo.getDtlList().get(j).getId());
				List<FbsBudgetDetail> details = new ArrayList<>();
				for(FbsBudgetDetail detail : allDetails){
					if(!EmptyUtils.isEmpty(detail)){
						if(detail.getDetailId().equals(vo.getDtlList().get(j).getId())){
							details.add(detail);
						}
					}
				}
//				if (null == details) {
//					continue;
//				}
				for (int k = 0; k < details.size(); k++) {
					FbsBudgetDetail newDetail = new FbsBudgetDetail();
					BeanUtils.copyNotNullProperties(details.get(k), newDetail);
					FbsBudgetDtlAbs budgetDtlAbs = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlByItemCode(newDetail.getItemCode(), fbsBudget.getId(), entity);
					newDetail.setDetailId(budgetDtlAbs.getId());
					newDetail.setChildCorpDtlId(details.get(k).getId());
					newDetail.setId("");
					fbsBudgetDetailDao.evict(budgetDtlAbs);
					this.save(newDetail);
				}
			}
		}
	}
	/**
	 * 汇总时汇总明细
	 *
	 * @param budgetId
	 * @param childBudgetId
	 */
	public void colloctDetail(String budgetId, List<String> childBudgetId,Boolean isClear) {
		FbsBudget fbsBudget = fbsBudgetService.get(budgetId);
		if(isClear) {
			//先删除汇总明细
			this.delColloctDetail(budgetId, fbsBudget.getTimeType());
		}
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(fbsBudget.getTimeType());
		//把下级单位的明细插入汇总单位
		for (int i = 0; i < childBudgetId.size(); i++) {
			FbsBudgetVO vo = fbsBudgetService.getBudgetNotItem(childBudgetId.get(i));
			if (null == vo || null == vo.getDtlList()) {
				continue;
			}
			List<FbsBudgetDetail> allDetails = fbsBudgetDetailDao.getDetailListByBudgetId(vo.getId(),entity);
			if(EmptyUtils.isEmptyList(allDetails)){
				continue;
			}
			for (int j = 0; j < vo.getDtlList().size(); j++) {
//				List<FbsBudgetDetail> details = fbsBudgetDetailDao.getDetailListById(vo.getDtlList().get(j).getId());
				List<FbsBudgetDetail> details = new ArrayList<>();
				for(FbsBudgetDetail detail : allDetails){
					if(!EmptyUtils.isEmpty(detail)){
						if(detail.getDetailId().equals(vo.getDtlList().get(j).getId())){
							details.add(detail);
						}
					}
				}
//				if (null == details) {
//					continue;
//				}
				for (int k = 0; k < details.size(); k++) {
					FbsBudgetDetail newDetail = new FbsBudgetDetail();
					BeanUtils.copyNotNullProperties(details.get(k), newDetail);
					FbsBudgetDtlAbs budgetDtlAbs = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlByItemCode(newDetail.getItemCode(), fbsBudget.getId(), entity);
					newDetail.setDetailId(budgetDtlAbs.getId());
					newDetail.setChildCorpDtlId(details.get(k).getId());
					newDetail.setId("");
					fbsBudgetDetailDao.evict(budgetDtlAbs);
					this.save(newDetail);
				}
			}
		}
	}
	/**
	 * 追加汇总时汇总明细
	 *
	 * @param budgetId
	 * @param childBudgetId
	 */
	public void colloctDetailSuperAdd(String budgetId, List<String> childBudgetId) {
		FbsBudget fbsBudget = fbsBudgetService.get(budgetId);
		//先删除汇总明细
		this.delColloctDetail(budgetId, fbsBudget.getTimeType());
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(fbsBudget.getTimeType());
		//把下级单位的明细插入汇总单位
		for (int i = 0; i < childBudgetId.size(); i++) {
			// 删除下级单位对应的明细
			FbsBudgetVO vo = fbsBudgetService.getBudget(childBudgetId.get(i));
			if (null == vo || null == vo.getDtlList()) {
				continue;
			}
			for (int j = 0; j < vo.getDtlList().size(); j++) {
				List<FbsBudgetDetail> details = fbsBudgetDetailDao.getDetailListById(vo.getDtlList().get(j).getId());
				if (null == details) {
					continue;
				}
				for (int k = 0; k < details.size(); k++) {
					FbsBudgetDetail newDetail = new FbsBudgetDetail();
					BeanUtils.copyNotNullProperties(details.get(k), newDetail);
					FbsBudgetDtlAbs budgetDtlAbs = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlByItemCode(newDetail.getItemCode(), fbsBudget.getId(), entity);
					newDetail.setDetailId(budgetDtlAbs.getId());
					newDetail.setChildCorpDtlId(details.get(k).getId());
					newDetail.setId("");
					fbsBudgetDetailDao.evict(budgetDtlAbs);
					this.save(newDetail);
				}
			}
		}
	}
	/**
	 * 当汇总打回明细时删除汇总上来的明细
	 *
	 * @param childBudget
	 */
	public void repulseDeatil(FbsBudget childBudget) {
		FbsBudgetVO childBudgetVO = fbsBudgetService.getBudget(childBudget.getId());
		if (!EmptyUtils.isEmpty(childBudgetVO) && !EmptyUtils.isEmpty(childBudgetVO.getDtlList())) {
			for (FbsBudgetDtlAbsVO dtlAbsVO : childBudgetVO.getDtlList()) {
				List<FbsBudgetDetail> detailList = fbsBudgetDetailDao.getDetailListById(dtlAbsVO.getId());
				if (!EmptyUtils.isEmpty(detailList)) {
					for (FbsBudgetDetail detail : detailList) {
						fbsBudgetDetailDao.delDetailByChildId(detail.getId());
					}
				}
			}
		}
	}
	/**
	 * 删除汇总明细
	 *
	 * @param budgetId
	 */
	public void delColloctDetail(String budgetId, String timeType) {
		//获取明细表名
		String tableName = getTableName(timeType);
		fbsBudgetDetailDao.delColloctDetail(budgetId, tableName);
	}
	/**
	 * 进入明细录入界面
	 *
	 * @param model
	 */
	public void toView(FbsBudgetDetailVO vo, Model model) {
		String budgetId = "";//计划id
		String flag = "";//标志 判断是计划录入 还是计划追加 input为录入 add为追加
		String detailId = vo.getDetailId();//明细id
		if (!StringUtils.isEmpty(vo.getSuperaddId())) {
			flag = "add";
			FbsBudgetSuperadd addEntity = fbsBudgetSuperaddService.get(vo.getSuperaddId(), true);
			budgetId = addEntity.getBudgetId();
		} else {
			flag = "input";
			budgetId = vo.getBudgetId();
		}
		FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
		/**得到表格的列*/
		List<String> dtlColList = new ArrayList<String>();
		List<String> dtlColValueList = new ArrayList<String>();
		String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
		dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
		String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
		for (int i = 0; i < dtlCols.length; i++) {
			dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
			dtlColValueList.add(dtlCols[i]);
		}
		FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(detailId, entity);
		model.addAttribute("budgetDtl", budgetDtl);
		List<FbsBudgetDetailVO> dataList = this.getDetailList(budgetId, detailId);
		String beginDate = DateUtils.formatDate(budget.getBeginDate());
		String endDate = DateUtils.formatDate(budget.getEndDate());
		model.addAttribute("flag", flag);
		model.addAttribute("dataList", dataList);
		model.addAttribute("budget", budget);
		model.addAttribute("dtlColList", dtlColList);
		model.addAttribute("dtlColValueList", dtlColValueList);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
	}
    public void toView(FbsBudgetDetailVO vo, FbsBudgetDetailViewVO model) {
        String budgetId = "";//计划id
        String flag = "";//标志 判断是计划录入 还是计划追加 input为录入 add为追加
        String detailId = vo.getDetailId();//明细id
        if (!StringUtils.isEmpty(vo.getSuperaddId())) {
            flag = "add";
            FbsBudgetSuperadd addEntity = fbsBudgetSuperaddService.get(vo.getSuperaddId(), true);
            budgetId = addEntity.getBudgetId();
        } else {
            flag = "input";
            budgetId = vo.getBudgetId();
        }
        FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
        String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
        /**得到表格的列*/
        List<String> dtlColList = new ArrayList<String>();
        List<String> dtlColValueList = new ArrayList<String>();
        String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
        dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
        String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < dtlCols.length; i++) {
            dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
            dtlColValueList.add(dtlCols[i]);
        }
        FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(detailId, entity);
        model.setBudgetDtl(  budgetDtl);
        List<FbsBudgetDetailVO> dataList = this.getDetailList(budgetId, detailId);
        String beginDate = DateUtils.formatDate(budget.getBeginDate());
        String endDate = DateUtils.formatDate(budget.getEndDate());
        model.setFlag(  flag);
        model.setDataList(  dataList);
        model.setBudget( budget);
        model.setDtlColList(  dtlColList);
        model.setDtlColValueList(  dtlColValueList);
        model.setBeginDate(  beginDate);
        model.setEndDate(  endDate);
    }
	public List<FbsBudgetDetailVO> getDetailList(String budgetId, String detailId) {
		List<FbsBudgetDetailVO> dataList = this.getBtTable(detailId);
		boolean isRollMonth = false;
		FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
		//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		//季计划上报模式:1,季固定;2,季滚动;
		String qimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODE);
		//周计划上报模式:1,固定;2,周滚动;
		String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
		//月计划模式控制
		if (BudgetConstant.TIME_TYPE_MONTH.equals(budget.getTimeType())) {
			if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)
					|| BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
				isRollMonth = true;
			}
		}
		if (isRollMonth) {
			String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
			FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(detailId, entity);
			//月滚动模式
			int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
			//是否有年计划
			String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
			//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
			boolean isSpanYear = false;
			if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
					|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
				isSpanYear = true;
			}
			int month = DateUtils.getMonth(budget.getBeginDate());
			int year = DateUtils.getYear(budget.getBeginDate());
			List<String> months = new ArrayList<>();
			List<String> monthsHid = new ArrayList<>();
			for (int i = 1; i <= mimodeRoll; i++) {
				if(!isSpanYear) {
					if (month <= 12) {
						months.add(year+"年"+month + "");
						monthsHid.add(month+"");
						month++;
					}
				}else{
					if(month>12) {
						month = month - 12;
						year++;
					}
					months.add(year+"年"+month + "");
					monthsHid.add(month+"");
					month++;
				}
			}
			Date beginDate = budget.getBeginDate();
			for (int i = 1; i < months.size(); i++) {
				FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, budget.getTimeType());
				FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
				FbsBudgetDtlAbs nextFbsBudgetAbs = this.fbsBudgetDtlAbsService.getByBudgetId(nextFbsBudget.getId(), budgetDtl.getItemCode(), nextFbsBudget.getTimeType());
				dataList.addAll(this.getBtTable(nextFbsBudgetAbs.getId()));
				beginDate = DateUtils.addMonth(beginDate,1);
			}
		}
		if (BudgetConstant.TIME_TYPE_QUARTER.equals(budget.getTimeType()) &&
				BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimode)) {
			String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
			FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(detailId, entity);
			//季计划滚动;
			int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
			//季计划滚动，分解月
			int quarter = DateUtils.getQuarterOfYear(budget.getBeginDate());
			int year = DateUtils.getYear(budget.getBeginDate());
			//保存季度
			List<String> quarters = new ArrayList<>();
			List<String> quartersHid = new ArrayList<>();
			for (int i = 1; i <= qimodeRoll; i++) {
				String quarterShow = "";
				quarterShow +=year+"年"+quarter+"季度</br>";
//				quarterShow +=this.getMonthByQuarter(year,quarter);
				quarters.add(quarterShow);
				quartersHid.add(quarter+"");
				quarter++;
				if(quarter>4){
					year++;
					quarter = 1;
				}
			}
			Date beginDate = budget.getBeginDate();
			for (int i = 1; i < quarters.size(); i++) {
				FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, budget.getTimeType());
				FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
				FbsBudgetDtlAbs nextFbsBudgetAbs = this.fbsBudgetDtlAbsService.getByBudgetId(nextFbsBudget.getId(), budgetDtl.getItemCode(), nextFbsBudget.getTimeType());
				dataList.addAll(this.getBtTable(nextFbsBudgetAbs.getId()));
				beginDate = DateUtils.addMonth(beginDate,3);
			}
		}
		if (BudgetConstant.TIME_TYPE_WEEK.equals(budget.getTimeType()) &&
				BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)) {
			String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
			FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(detailId, entity);
			//周计划滚动;
			int wimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
			Date weekBeginDate = budget.getBeginDate();
			List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
			List<String> weeksHid = new ArrayList<>();
			int thismonth = DateUtils.getMonth(weekBeginDate);
			for (int i = 1; i <= wimodeRoll; i++) {
				FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
				int week = DateUtils.getWeekOfMonth(weekBeginDate);
				int month = DateUtils.getMonth(weekBeginDate);
				if(month > thismonth){
					week = week -1;
				}
				//保存周
				String weekShow = "";
				weekShow +=month+"月第"+week+"周";
				fbsBudgetPaternityVO.setPaternal(weekShow);
				weeks.add(fbsBudgetPaternityVO);
				weeksHid.add(week+"");
				weekBeginDate = DateUtils.addWeek(weekBeginDate, 1);
			}
			Date beginDate = budget.getBeginDate();
			for (int i = 1; i < weeks.size(); i++) {
				FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, budget.getTimeType());
				FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
				FbsBudgetDtlAbs nextFbsBudgetAbs = this.fbsBudgetDtlAbsService.getByBudgetId(nextFbsBudget.getId(), budgetDtl.getItemCode(), nextFbsBudget.getTimeType());
				dataList.addAll(this.getBtTable(nextFbsBudgetAbs.getId()));
				beginDate = DateUtils.addWeek(beginDate,1);
			}
		}
		return dataList;
	}

	@Override
	public ExcelVerifyHanlderResult verifyHandler(FbsBudgetDetailVO vo, Object... obj) {
		List<String> msgList = new ArrayList<>();
		String flag = "";
		String itemCode = "";
		String timeType = "";
		String detailId = "";
		Date beginDate = null;
		Date endDate = null;
		String natualCurId = "";
		String budgetId = "";
		if (!EmptyUtils.isEmpty(obj) && !EmptyUtils.isEmpty(obj[1])) {
			Map<String, String[]> params = (Map<String, String[]>) obj[1];
			if(!EmptyUtils.isEmpty(params) && params.size() > 0) {
				flag = params.get("flag")[0];
				itemCode = params.get("itemCode")[0];
				timeType = params.get("timeType")[0];
				detailId = params.get("detailId")[0];
				beginDate = DateUtils.parseDate(params.get("beginDate")[0]);
				endDate = DateUtils.parseDate(params.get("endDate")[0]);
				natualCurId = params.get("natualCurId")[0];
				budgetId = params.get("budgetId")[0];
				vo.setFlag(flag);
				vo.setTimeType(timeType);
				vo.setDetailId(detailId);
				vo.setNatualCurId(natualCurId);
				vo.setBudgetId(budgetId);
			}
		}
		if (!itemCode.equalsIgnoreCase(vo.getItemCode())) {
			msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_ITEMCODE_EQ);
		}
		if (EmptyUtils.isEmpty(vo.getBMoney()) && EmptyUtils.isEmpty(vo.getBtempMoney())) {
			msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_MONEY_NULL);
		} else {
			if (BudgetConstant.BUDGET_TARGET_INPUT.equals(flag)) {
				if (vo.getBMoney() == null) {
					msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_BMONE_MISMATCH);
				}
				if (!EmptyUtils.isEmpty(vo.getBMoney()) && BigDecimal.ZERO.intValue() == vo.getBMoney().intValue()) {
					msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_BMONE_EQ_ZERO);
				}
			} else {
				if (vo.getBtempMoney() == null) {
					msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_BTEMPMONE_MISMATCH);
				}
				if (!EmptyUtils.isEmpty(vo.getBtempMoney()) && BigDecimal.ZERO.intValue() == vo.getBtempMoney().intValue()) {
					msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_BTEMPMONE_EQ_ZERO);
				}
			}
		}
		String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
		if(dtlCol.contains("curId")) {
			if (EmptyUtils.isBlank(vo.getCurName())) {
				msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_CURID_NULL);
			} else {
				String curId = this.iBtCurrencyService.getCurrencyByName(vo.getCurName());
				if (EmptyUtils.isBlank(curId)) {
					msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_CUR_NULL);
				}
				vo.setCurId(curId);
			}
		}
		if (!EmptyUtils.isBlank(vo.getSettlType())) {
			String settlType = this.dictService.getCodeByName("settlType", vo.getSettlType());
			if (EmptyUtils.isBlank(settlType)) {
				msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_SETTLTYPE_FORMAT);
			}
		}
		if (EmptyUtils.isEmpty(vo.getVestingDate())) {
			msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_VESTINGDATE_NULL);
		} else {
			if (vo.getVestingDate().getTime() < beginDate.getTime() ||
					vo.getVestingDate().getTime() > endDate.getTime()) {
				msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_VESTINGDATE_ERROR);
			}
		}
		if (EmptyUtils.isEmptyList(msgList)) {
			return new ExcelVerifyHanlderResult(true, msgList);
		} else {
			return new ExcelVerifyHanlderResult(false, msgList);
		}
	}

	@Override
	public void verifyHandler(List<FbsBudgetDetailVO> list, Object... obj) {
	}
	/**
	 * 导入保存功能
	 *
	 * @param list
	 * @param user
	 */
	public void saveImport(List<FbsBudgetDetailVO> list, ICurrentUser user) {
		List<FbsBudgetDetail> entityList = new ArrayList<>();
		for (FbsBudgetDetailVO vo : list) {
			FbsBudgetDetail dtl = new FbsBudgetDetail();
			BeanUtils.copyNotNullProperties(vo, dtl);
			String settlType = "";
			if ("现款".trim().equals(vo.getSettlType())) {
				settlType = BudgetConstant.SETTLTYPE_0;
			} else if ("票据".trim().equals(vo.getSettlType())) {
				settlType = BudgetConstant.SETTLTYPE_1;
			}
			dtl.setSettlType(settlType);
			dtl.setItemName(iFbsItemService.getFbsItemByCode(dtl.getItemCode()).getItemName());
			// 计算折合人民币
			BigDecimal curExrate = iFcExcrateAdjustService.getExchangeRateBy(dtl.getCurId(), iBtCurrencyService.getRMBCurrency().getId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
			if (EmptyUtils.isEmpty(curExrate)) {
				curExrate = BigDecimal.ZERO;
			}
			if (!EmptyUtils.isEmpty(dtl.getBtempMoney())) {
				dtl.setBtempNatualMoney(dtl.getBtempMoney().multiply(curExrate));
			} else {
				dtl.setNatualMoney(dtl.getBMoney().multiply(curExrate));
			}
			entityList.add(dtl);
			if("input".equals(vo.getFlag())) {
				fbsBudgetDetailDao.delByDetailId(dtl.getDetailId());
			}
		}
		this.batchSaveOrUpdate(entityList);
	}
	/**
	 * 挂接计划执行时，加载科目明细数据
	 *
	 * @param input
	 * @return
	 * @author zuoy 2018-11-16
	 */
	public DataTablesOutput getProjectDetailInfo(DataTablesInput input) {
		List<String> dtlColIds = (List<String>) input.getFormdata().get("dtlColIds");
		Map<String, String> dtlColMap = new HashMap<>();
		for (String dtl : dtlColIds) {
			dtlColMap.put(dtl, dtl);
		}
		Map<String, Object> params = new HashMap<>();
		String itemCode = input.getFormdata().get("itemCode").toString();
		// 科目
		params.put("itemCode", itemCode);
		// 币别
		String curId = EmptyUtils.isEmpty(input.getFormdata().get("curId")) ? "" : input.getFormdata().get("curId").toString();
		if (!EmptyUtils.isBlank(curId) && dtlColMap.containsKey("curId")) {
			// 非人民币才过滤币别条件
			BtCurrencyDTO cur = iBtCurrencyService.getRMBCurrency();
			if (!curId.equalsIgnoreCase(cur.getId())) {
				params.put("curId", curId);
			}
		}
		// 结算类型
		String settlType = EmptyUtils.isEmpty(input.getFormdata().get("settlType")) ? "" : input.getFormdata().get("settlType").toString();
		if (dtlColMap.containsKey("settlType")) {
			params.put("settlType", settlType);
		}
		// 单位/部门
		String corpName = EmptyUtils.isEmpty(input.getFormdata().get("corpName")) ? "" : input.getFormdata().get("corpName").toString();
		if (dtlColMap.containsKey("corpName")) {
			params.put("corpName", corpName);
		}
		// 用途
		String purpose = EmptyUtils.isEmpty(input.getFormdata().get("purpose")) ? "" : input.getFormdata().get("purpose").toString();
		if (dtlColMap.containsKey("purpose")) {
			params.put("purpose", purpose);
		}
		// 期望支付日期
		if (dtlColMap.containsKey("wishPayday")) {
			params.put("wishPayday", input.getFormdata().get("wishPayday"));
		}
		// 付款账户
		String payerAcc = EmptyUtils.isEmpty(input.getFormdata().get("payerAcc")) ? "" : input.getFormdata().get("payerAcc").toString();
		if (dtlColMap.containsKey("payerAcc")) {
			params.put("payerAcc", payerAcc);
		}
		// 收款账户名
		String payeeAccName = EmptyUtils.isEmpty(input.getFormdata().get("payeeAccName")) ? "" : input.getFormdata().get("payeeAccName").toString();
		if (dtlColMap.containsKey("payeeAccName")) {
			params.put("payeeAccName", payeeAccName);
		}
		// 收款账户
		String payeeAcc = EmptyUtils.isEmpty(input.getFormdata().get("payeeAcc")) ? "" : input.getFormdata().get("payeeAcc").toString();
		if (dtlColMap.containsKey("payeeAcc")) {
			params.put("payeeAcc", payeeAcc);
		}
		// 收款开户行
		String payeeBank = EmptyUtils.isEmpty(input.getFormdata().get("payeeBank")) ? "" : input.getFormdata().get("payeeBank").toString();
		if (dtlColMap.containsKey("payeeBank")) {
			params.put("payeeBank", payeeBank);
		}
		// 合同号
		String contract = EmptyUtils.isEmpty(input.getFormdata().get("contract")) ? "" : input.getFormdata().get("contract").toString();
		if (dtlColMap.containsKey("contract")) {
			params.put("contract", contract);
		}
		Date processDate = DateUtils.parseDate(input.getFormdata().get("processDate").toString());
		String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
		String tableName = getTableName(timeType);
		//根据执行日期获取计划周期
		FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(processDate, timeType);
		String corpId = input.getFormdata().get("corpId").toString();
		//获取单位周期计划
		FbsBudget budget = this.fbsBudgetService.getCorpBudget(corpId, timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
		if (budget != null && budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
			List<FbsBudgetDetailVO> list = fbsBudgetDetailDao.getProjectDetailInfo(params, tableName, budget.getId());
			if (EmptyUtils.isEmptyList(list)) {
				// 存在明细，但未匹配上时，显示明细金额
				int recordsTotal = 0;
				String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
				FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlByItemCode(itemCode, budget.getId(), entity);
				if (!EmptyUtils.isEmpty(budgetDtl)) {
					List<FbsBudgetDetailVO> dataList = this.getBtTable(budgetDtl.getId());
					if (!EmptyUtils.isEmptyList(dataList)) {
						recordsTotal = 1;
					}
				}
				return new DataTablesOutput(1, new ArrayList(0), recordsTotal, 0);
			}
			return new DataTablesOutput(1, list, list.size(), list.size());
		} else {
			return new DataTablesOutput(1, new ArrayList(0), 0, 0);
		}
	}
	/**
	 * 设置追加金额
	 *
	 * @param budgetId
	 * @param timeType
	 */
	public void setSuperaddMoney(String budgetId, String timeType) {
		//获取明细表名
		String tableName = getTableName(timeType);
		fbsBudgetDetailDao.setSuperaddMoney(tableName, budgetId);
	}
	/**
	 * 删除汇总明细
	 *
	 * @param budgetId
	 */
	public void delColloctDetailSuper(String budgetId, String timeType) {
		//获取明细表名
		String tableName = getTableName(timeType);
		// 删除新追加的录入明细
		fbsBudgetDetailDao.delColloctDetailSuper(budgetId, tableName);
	}
	public String getTableName(String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		return tableName;
	}
	/**
	 * 获取汇总明细科目的折合人民币金额
	 *
	 * @param budgetId 计划ID
	 * @param timeType 计划类型
	 * @param target   input 上报  super 追加
	 * @return
	 */
	public Map<String, BigDecimal> collectBudgetDetail(String budgetId, String timeType, String target) {
		Map<String, BigDecimal> map = new HashMap<>();
		List<FbsBudgetDetail> list = this.fbsBudgetDetailDao.collectBudgetDetail(budgetId, getTableName(timeType), null);
		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		for (FbsBudgetDetail detail : list) {
			if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
				map.put(detail.getDetailId() + "_" + detail.getItemCode(), detail.getBtempMoney());
			} else {
				map.put(detail.getDetailId() + "_" + detail.getItemCode(), detail.getBMoney());
			}
		}
		return map;
	}
	/**
	 * 获取汇总明细科目的折合人民币金额
	 *
	 * @param budgetId 计划ID
	 * @param timeType 计划类型
	 * @param target   input 上报  super 追加
	 * @return
	 */
	public Map<String, BigDecimal> collectBudgetDetailResolve(String budgetId, String timeType, String target) {
		Map<String, BigDecimal> map = new HashMap<>();
		List<FbsBudgetDetail> list = this.fbsBudgetDetailDao.collectBudgetDetail(budgetId, getTableName(timeType), "1");
		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		for (FbsBudgetDetail detail : list) {
			if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
				map.put(detail.getItemCode() + "_" + DateUtils.getMonth(detail.getVestingDate()), detail.getBtempMoney());
			} else {
				map.put(detail.getItemCode() + "_" + DateUtils.getMonth(detail.getVestingDate()), detail.getBMoney());
			}
		}
		return map;
	}
	/**
	 * 获取汇总明细科目的折合人民币金额
	 *
	 * @param budgetId 计划ID
	 * @param timeType 计划类型
	 * @param target   input 上报  super 追加
	 * @param months   滚动月数
	 * @return
	 */
	public Map<String, BigDecimal> collectBudgetDetailRollMonth(String budgetId, String timeType, String target,List<String> months) {
		Map<String, BigDecimal> map = new HashMap<>();
		//根据id得到预算计划主表
		FbsBudget budget = fbsBudgetService.get(budgetId, true);
		List<FbsBudgetDetail> list = new ArrayList<>();
		list.addAll(fbsBudgetDetailDao.collectBudgetDetail(budgetId, getTableName(timeType), "1"));
		Date beginDate = budget.getBeginDate();
		for(int i = 1;i < months.size();i++){
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, budget.getTimeType());
			FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			if (!EmptyUtils.isEmpty(nextFbsBudget)) {
				list.addAll(fbsBudgetDetailDao.collectBudgetDetail(nextFbsBudget.getId(), getTableName(timeType), "1"));
			}
			DateUtils.addMonth(beginDate,1);
		}
		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		for (FbsBudgetDetail detail : list) {
			if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
				map.put(detail.getDetailId() + "_" + detail.getItemCode() + "_" + DateUtils.getMonth(detail.getVestingDate()), detail.getBtempMoney());
			} else {
				map.put(detail.getDetailId() + "_" + detail.getItemCode() + "_" + DateUtils.getMonth(detail.getVestingDate()), detail.getBMoney());
			}
		}
		return map;
	}
	/**
	 * 获取汇总明细科目的折合人民币金额
	 *
	 * @param budgetId 计划ID
	 * @param timeType 计划类型
	 * @param target   input 上报  super 追加
	 * @param weekList 周期
	 * @return
	 */
	public Map<String, BigDecimal> collectBudgetDetailRollMonthWeek(String budgetId, String timeType, String target, List<FbsBudgetVO> weekList) {
		Map<String, BigDecimal> map = new HashMap<>();
		FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		boolean isSpanYear = false;
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		int month = DateUtils.getMonth(budget.getBeginDate());
		int year = DateUtils.getYear(budget.getBeginDate());
		//保存月份
		List<FbsBudgetPaternityVO> months = new ArrayList<>();
		for (int i = 1; i <= mimodeRoll; i++) {
			FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
			if(!isSpanYear) {
				if (month <= 12) {
					fbsBudgetPaternityVO.setPaternal(year+"年"+month + "");
					months.add(fbsBudgetPaternityVO);
					month++;
				}
			}else{
				if(month>12) {
					month = month - 12;
					year++;
				}
				fbsBudgetPaternityVO.setPaternal(year+"年"+month + "");
				months.add(fbsBudgetPaternityVO);
				month++;
			}
		}
		List<FbsBudgetDetail> list = new ArrayList<>();
		list.addAll(fbsBudgetDetailDao.collectBudgetDetail(budgetId, getTableName(timeType), "2"));
		for (int i = 0; i < weekList.size(); i++) {
			FbsBudgetVO vo = weekList.get(i);
			for (FbsBudgetDetail detail : list) {
				if (!detail.getVestingDate().before(vo.getBeginDate()) && !detail.getVestingDate().after(vo.getEndDate())) {
					BigDecimal money = map.get(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i + months.size()+1));
					if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
						money = EmptyUtils.isEmpty(money) ? detail.getBtempMoney() : money.add(detail.getBtempMoney());
					} else {
						money = EmptyUtils.isEmpty(money) ? detail.getBMoney() : money.add(detail.getBMoney());
					}
					map.put(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i + months.size()+1), money);
				}
			}
		}
		list = new ArrayList<>();
		FbsBudget fbsbudget = null;
		Date beginDate = budget.getBeginDate();
		//月明细录入
		for(int i = 0; i < months.size(); i++){
			FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, budget.getTimeType());
			fbsbudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), fbsBudgetPeriodVO.getBeginDate(), fbsBudgetPeriodVO.getEndDate());
			if (!EmptyUtils.isEmpty(fbsbudget)) {
				list.addAll(fbsBudgetDetailDao.collectBudgetDetail(fbsbudget.getId(), getTableName(timeType), "1"));
			}
			beginDate = DateUtils.addMonth(beginDate, 1);
		}
		for (FbsBudgetDetail detail : list) {
			if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
				map.put(detail.getDetailId() + "_" + detail.getItemCode() + "_" + DateUtils.getMonth(detail.getVestingDate()), detail.getBtempMoney());
			} else {
				map.put(detail.getDetailId() + "_" + detail.getItemCode() + "_" + DateUtils.getMonth(detail.getVestingDate()), detail.getBMoney());
			}
		}
		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		return map;
	}
	/**
	 * 季滚动获取汇总明细科目的折合人民币金额
	 * @param budgetId 计划ID
	 * @param target   input 上报  super 追加
	 * @param monthList 周期
	 * @return
	 */
	public Map<String, BigDecimal> collectBudgetDetailRollQuarter(String budgetId,String target, List<FbsBudgetVO> monthList) {
		Map<String, BigDecimal> map = new HashMap<>();
		FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
		//季计划滚动;
		int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
		//季计划滚动，分解月
		//int qRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
		int quarter = DateUtils.getQuarterOfYear(budget.getBeginDate());
		int year = DateUtils.getYear(budget.getBeginDate());
		String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
		//保存季度
		List<String> quarters = new ArrayList<>();
		for (int i = 1; i <= qimodeRoll; i++) {
			String quarterShow = "";
			quarterShow +=year+"年"+quarter+"季度";
			quarters.add(quarterShow);
			quarter++;
			if(quarter>4){
				year++;
				quarter = 1;
			}
		}
		List<FbsBudgetDetail> list = new ArrayList<>();
		list.addAll(fbsBudgetDetailDao.collectBudgetDetail(budgetId, getTableName(budget.getTimeType()), "2"));
		for (int i = 0; i < monthList.size(); i++) {
			FbsBudgetVO vo = monthList.get(i);
			for (FbsBudgetDetail detail : list) {
				if (!detail.getVestingDate().before(vo.getBeginDate()) && !detail.getVestingDate().after(vo.getEndDate())) {
					BigDecimal money = map.get(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i + quarters.size()+1));
					if(dtlCol.contains("curId")) {
						if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
							money = EmptyUtils.isEmpty(money) ? detail.getBtempNatualMoney() : money.add(detail.getBtempNatualMoney());
						} else {
							money = EmptyUtils.isEmpty(money) ? detail.getNatualMoney() : money.add(detail.getNatualMoney());
						}
					}else {
						if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
							money = EmptyUtils.isEmpty(money) ? detail.getBtempMoney() : money.add(detail.getBtempMoney());
						} else {
							money = EmptyUtils.isEmpty(money) ? detail.getBMoney() : money.add(detail.getBMoney());
						}
					}
					map.put(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i + quarters.size()+1), money);
				}
			}
		}
		FbsBudget fbsBudget = null;
		Date beginDate = budget.getBeginDate();
		for (int i = 0; i < quarters.size(); i++) {
			list = new ArrayList<>();
			logger.error("=====第{}轮,i+1={}, beginDate={}",i, i+1, beginDate);
			FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, budget.getTimeType());
			logger.error("周期VO begin={}, end={}",
					fbsBudgetPeriodVO==null?null:fbsBudgetPeriodVO.getBeginDate(),
					fbsBudgetPeriodVO==null?null:fbsBudgetPeriodVO.getEndDate());

			fbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), fbsBudgetPeriodVO.getBeginDate(), fbsBudgetPeriodVO.getEndDate());
			logger.error("fbsBudget 是否为空:{}", EmptyUtils.isEmpty(fbsBudget));

			if (!EmptyUtils.isEmpty(fbsBudget)) {
				list.addAll(fbsBudgetDetailDao.collectBudgetDetail(fbsBudget.getId(), getTableName(budget.getTimeType()), "2"));
			}
			logger.error("本季度明细list.size={}", list.size());

			int hitCount = 0;
			for (FbsBudgetDetail detail : list) {
				if (!detail.getVestingDate().before(fbsBudget.getBeginDate()) && !detail.getVestingDate().after(fbsBudget.getEndDate())) {
					hitCount++;
					BigDecimal money = map.get(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i +1));
					if(dtlCol.contains("curId")) {
						if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
							money = EmptyUtils.isEmpty(money) ? detail.getBtempNatualMoney() : money.add(detail.getBtempNatualMoney());
						} else {
							money = EmptyUtils.isEmpty(money) ? detail.getNatualMoney() : money.add(detail.getNatualMoney());
						}
					}else {
						if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
							money = EmptyUtils.isEmpty(money) ? detail.getBtempMoney() : money.add(detail.getBtempMoney());
						} else {
							money = EmptyUtils.isEmpty(money) ? detail.getBMoney() : money.add(detail.getBMoney());
						}
					}
					map.put(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i +1), money);
				}
			}
			logger.error("本季度命中时间条件明细数量 hitCount={}", hitCount);
			beginDate = DateUtils.addMonth(beginDate, 3);
		}

		if (EmptyUtils.isEmptyList(list)) {
			return map;
		}
		return map;
	}

    /**
     * 周滚动获取汇总明细科目的折合人民币金额
     * @param budgetId 计划ID
     * @param target   input 上报  super 追加
     * @param dayList 周期
     * @return
     */
    public Map<String, BigDecimal> collectBudgetDetailRollWeek(String budgetId,String target, List<FbsBudgetVO> dayList) {
        Map<String, BigDecimal> map = new HashMap<>();
        FbsBudget budget = fbsBudgetService.get(budgetId, true);//根据id得到预算计划主表
        //周计划滚动;
        int wimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
        //周计划滚动，分解天
        int wRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
        Date weekBeginDate = budget.getBeginDate();
        List<String> weeks = new ArrayList<>();
        List<String> weeksHid = new ArrayList<>();
        for (int i = 1; i <= wimodeRoll; i++) {
            int week = DateUtils.getWeekOfMonth(weekBeginDate);
            int month = DateUtils.getMonth(weekBeginDate);
            //保存周
            String weekShow = "";
            weekShow +=month+"月第"+week+"周";
            weeks.add(weekShow);
            weeksHid.add(week+"");
            weekBeginDate = DateUtils.addDay(weekBeginDate,7);
        }
        List<FbsBudgetDetail> list = new ArrayList<>();
        list.addAll(fbsBudgetDetailDao.collectBudgetDetail(budgetId, getTableName(budget.getTimeType()), "2"));
        for (int i = 0; i < dayList.size(); i++) {
            FbsBudgetVO vo = dayList.get(i);
            for (FbsBudgetDetail detail : list) {
                if (!detail.getVestingDate().before(vo.getBeginDate()) && !detail.getVestingDate().after(vo.getEndDate())) {
                    BigDecimal money = map.get(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i + weeks.size()+1));
                    if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
                        money = EmptyUtils.isEmpty(money) ? detail.getBtempMoney() : money.add(detail.getBtempMoney());
                    } else {
                        money = EmptyUtils.isEmpty(money) ? detail.getBMoney() : money.add(detail.getBMoney());
                    }
                    map.put(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i + weeks.size()+1), money);
                }
            }
        }
        list = new ArrayList<>();
        FbsBudget fbsBudget = null;
        Date beginDate = budget.getBeginDate();
        //周计划明细
		for (int i = 0; i < weeks.size(); i++) {
			list = new ArrayList<>();
			FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, budget.getTimeType());
			fbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), budget.getTimeType(), fbsBudgetPeriodVO.getBeginDate(), fbsBudgetPeriodVO.getEndDate());
			if (!EmptyUtils.isEmpty(fbsBudget)) {
				list.addAll(fbsBudgetDetailDao.collectBudgetDetail(fbsBudget.getId(), getTableName(budget.getTimeType()), "2"));
			}
			for (FbsBudgetDetail detail : list) {
				if (!detail.getVestingDate().before(fbsBudget.getBeginDate()) && !detail.getVestingDate().after(fbsBudget.getEndDate())) {
					BigDecimal money = map.get(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i +1));
					if (BudgetConstant.BUDGET_TARGET_SUPER.equals(target)) {
						money = EmptyUtils.isEmpty(money) ? detail.getBtempMoney() : money.add(detail.getBtempMoney());
					} else {
						money = EmptyUtils.isEmpty(money) ? detail.getBMoney() : money.add(detail.getBMoney());
					}
					map.put(detail.getDetailId() + "_" + detail.getItemCode() + "_" + (i +1), money);
				}
			}
			FbsBudgetPeriodVO nextBudgetPeriodByTimeType = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, budget.getTimeType());
			beginDate = nextBudgetPeriodByTimeType.getBeginDate();
		}
        if (EmptyUtils.isEmptyList(list)) {
            return map;
        }
        return map;
    }
	/**
	 * 进入明细录入界面-月滚动上报
	 *
	 * @param model
	 */
	public void inputDtlRollMonth(FbsBudgetDetailVO vo, Model model) {
		/**得到表格的列*/
		List<String> dtlColList = new ArrayList<String>();
		List<String> dtlColValueList = new ArrayList<String>();
		String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
		dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
		String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
		for (int i = 0; i < dtlCols.length; i++) {
			dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
			dtlColValueList.add(dtlCols[i]);
		}
		FbsBudget budget = fbsBudgetService.get(vo.getBudgetId(), true);//根据id得到预算计划主表
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
		FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(vo.getDetailId(), entity);
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//是否有年计划
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		boolean isSpanYear = false;
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		int month = DateUtils.getMonth(budget.getBeginDate());
		int year = DateUtils.getYear(budget.getBeginDate());
		//保存月份
		List<String> months = new ArrayList<>();
		List<String> monthsHid = new ArrayList<>();
		for (int i = 1; i <= mimodeRoll; i++) {
			if(!isSpanYear) {
				if (month <= 12) {
					months.add(year+"年"+month + "");
					monthsHid.add(month+"");
					month++;
				}
			}else{
				if(month>12) {
					month = month - 12;
					year++;
				}
				months.add(year+"年"+month + "");
				monthsHid.add(month+"");
				month++;
			}
		}
		String beginDate = DateUtils.formatDate(budget.getBeginDate());
		String endDate = DateUtils.formatDate(budget.getEndDate());
		List<FbsBudgetDetailVO> list = this.getDetailList(budget.getId(), vo.getDetailId());
		Date start = budget.getBeginDate();
		for(int i=1;i<months.size();i++){
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(start, budget.getTimeType());
			endDate = DateUtils.formatDate(nextFbsBudgetPeriodVO.getEndDate());
			start = DateUtils.addMonth(start,1);
		}
		if (EmptyUtils.isEmptyList(list)) {
			FbsBudgetDetailVO newVO = new FbsBudgetDetailVO();
			if (DateUtils.getDate().getTime() > budget.getBeginDate().getTime() && DateUtils.getDate().getTime() < budget.getEndDate().getTime()) {
				newVO.setVestingDate(DateUtils.getDate());
			} else {
				newVO.setVestingDate(budget.getBeginDate());
			}
			list.add(newVO);
		} else {
			for (FbsBudgetDetailVO fbsBudgetDetailVO : list) {
				fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getNatualMoney());
			}
		}
		model.addAttribute("flag", "input");
		model.addAttribute("dataList", list);
		model.addAttribute("budget", budget);
		model.addAttribute("dtlColList", dtlColList);
		model.addAttribute("dtlColValueList", dtlColValueList);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("budgetDtl", budgetDtl);
	}
    public void inputDtlRollMonth(FbsBudgetDetailVO vo, FbsBudgetDetailViewVO model) {
        /**得到表格的列*/
        List<String> dtlColList = new ArrayList<String>();
        List<String> dtlColValueList = new ArrayList<String>();
        String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
        dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
        String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < dtlCols.length; i++) {
            dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
            dtlColValueList.add(dtlCols[i]);
        }
        FbsBudget budget = fbsBudgetService.get(vo.getBudgetId(), true);//根据id得到预算计划主表
        String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
        FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(vo.getDetailId(), entity);
        //月滚动模式
        int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
        //是否有年计划
        String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
        //是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
        boolean isSpanYear = false;
        if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
                || BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
            isSpanYear = true;
        }
        int month = DateUtils.getMonth(budget.getBeginDate());
        int year = DateUtils.getYear(budget.getBeginDate());
        //保存月份
        List<String> months = new ArrayList<>();
        List<String> monthsHid = new ArrayList<>();
        for (int i = 1; i <= mimodeRoll; i++) {
            if(!isSpanYear) {
                if (month <= 12) {
                    months.add(year+"年"+month + "");
                    monthsHid.add(month+"");
                    month++;
                }
            }else{
                if(month>12) {
                    month = month - 12;
                    year++;
                }
                months.add(year+"年"+month + "");
                monthsHid.add(month+"");
                month++;
            }
        }
        String beginDate = DateUtils.formatDate(budget.getBeginDate());
        String endDate = DateUtils.formatDate(budget.getEndDate());
        List<FbsBudgetDetailVO> list = this.getDetailList(budget.getId(), vo.getDetailId());
        Date start = budget.getBeginDate();
        for(int i=1;i<months.size();i++){
            FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(start, budget.getTimeType());
            endDate = DateUtils.formatDate(nextFbsBudgetPeriodVO.getEndDate());
            start = DateUtils.addMonth(start,1);
        }
        if (EmptyUtils.isEmptyList(list)) {
            FbsBudgetDetailVO newVO = new FbsBudgetDetailVO();
            if (DateUtils.getDate().getTime() > budget.getBeginDate().getTime() && DateUtils.getDate().getTime() < budget.getEndDate().getTime()) {
                newVO.setVestingDate(DateUtils.getDate());
            } else {
                newVO.setVestingDate(budget.getBeginDate());
            }
            list.add(newVO);
        } else {
            for (FbsBudgetDetailVO fbsBudgetDetailVO : list) {
                fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getNatualMoney());
            }
        }
        model.setFlag(  "input");
        model.setDataList(  list);
        model.setBudget( budget);
        model.setDtlColList( dtlColList);
        model.setDtlColValueList(  dtlColValueList);
        model.setBeginDate(  beginDate);
        model.setEndDate( endDate);
        model.setBudgetDtl(  budgetDtl);
    }
	/**
	 * 进入明细录入界面-季滚动上报
	 *
	 * @param model
	 */
	public void inputDtlRollQuarter(FbsBudgetDetailVO vo, FbsBudgetDetailViewVO model) {
		/**得到表格的列*/
		List<String> dtlColList = new ArrayList<String>();
		List<String> dtlColValueList = new ArrayList<String>();
		String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
		dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
		String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
		for (int i = 0; i < dtlCols.length; i++) {
			dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
			dtlColValueList.add(dtlCols[i]);
		}
		FbsBudget budget = fbsBudgetService.get(vo.getBudgetId(), true);//根据id得到预算计划主表
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
		FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(vo.getDetailId(), entity);
		//季计划滚动;
		int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
		int quarter = DateUtils.getQuarterOfYear(budget.getBeginDate());
		int year = DateUtils.getYear(budget.getBeginDate());
		//保存季度
		List<String> quarters = new ArrayList<>();
		List<String> quartersHid = new ArrayList<>();
		for (int i = 1; i <= qimodeRoll; i++) {
			String quarterShow = "";
			quarterShow +=year+"年"+quarter+"季度</br>";
//				quarterShow +=this.getMonthByQuarter(year,quarter);
			quarters.add(quarterShow);
			quartersHid.add(quarter+"");
			quarter++;
			if(quarter>4){
				year++;
				quarter = 1;
			}
		}
		String beginDate = DateUtils.formatDate(budget.getBeginDate());
		String endDate = DateUtils.formatDate(budget.getEndDate());
		List<FbsBudgetDetailVO> list = this.getDetailList(budget.getId(), vo.getDetailId());
		Date start = budget.getBeginDate();
		for(int i=1;i<quarters.size();i++){
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(start, budget.getTimeType());
			endDate = DateUtils.formatDate(nextFbsBudgetPeriodVO.getEndDate());
			start = DateUtils.addMonth(start,3);
		}
		if (EmptyUtils.isEmptyList(list)) {
			FbsBudgetDetailVO newVO = new FbsBudgetDetailVO();
			if (DateUtils.getDate().getTime() > budget.getBeginDate().getTime() && DateUtils.getDate().getTime() < budget.getEndDate().getTime()) {
				newVO.setVestingDate(DateUtils.getDate());
			} else {
				newVO.setVestingDate(budget.getBeginDate());
			}
			list.add(newVO);
		} else {
			for (FbsBudgetDetailVO fbsBudgetDetailVO : list) {
				fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getNatualMoney());
			}
		}
		model.setFlag(  "input");
		model.setDataList(  list);
		model.setBudget(  budget);
		model.setDtlColList(  dtlColList);
		model.setDtlColValueList( dtlColValueList);
		model.setBeginDate(  beginDate);
		model.setEndDate( endDate);
		model.setBudgetDtl(  budgetDtl);
	}

    public void inputDtlRollQuarter(FbsBudgetDetailVO vo, Model model) {
        /**得到表格的列*/
        List<String> dtlColList = new ArrayList<String>();
        List<String> dtlColValueList = new ArrayList<String>();
        String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
        dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
        String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < dtlCols.length; i++) {
            dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
            dtlColValueList.add(dtlCols[i]);
        }
        FbsBudget budget = fbsBudgetService.get(vo.getBudgetId(), true);//根据id得到预算计划主表
        String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
        FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(vo.getDetailId(), entity);
        //季计划滚动;
        int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
        int quarter = DateUtils.getQuarterOfYear(budget.getBeginDate());
        int year = DateUtils.getYear(budget.getBeginDate());
        //保存季度
        List<String> quarters = new ArrayList<>();
        List<String> quartersHid = new ArrayList<>();
        for (int i = 1; i <= qimodeRoll; i++) {
            String quarterShow = "";
            quarterShow +=year+"年"+quarter+"季度</br>";
//				quarterShow +=this.getMonthByQuarter(year,quarter);
            quarters.add(quarterShow);
            quartersHid.add(quarter+"");
            quarter++;
            if(quarter>4){
                year++;
                quarter = 1;
            }
        }
        String beginDate = DateUtils.formatDate(budget.getBeginDate());
        String endDate = DateUtils.formatDate(budget.getEndDate());
        List<FbsBudgetDetailVO> list = this.getDetailList(budget.getId(), vo.getDetailId());
        Date start = budget.getBeginDate();
        for(int i=1;i<quarters.size();i++){
            FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(start, budget.getTimeType());
            endDate = DateUtils.formatDate(nextFbsBudgetPeriodVO.getEndDate());
            start = DateUtils.addMonth(start,3);
        }
        if (EmptyUtils.isEmptyList(list)) {
            FbsBudgetDetailVO newVO = new FbsBudgetDetailVO();
            if (DateUtils.getDate().getTime() > budget.getBeginDate().getTime() && DateUtils.getDate().getTime() < budget.getEndDate().getTime()) {
                newVO.setVestingDate(DateUtils.getDate());
            } else {
                newVO.setVestingDate(budget.getBeginDate());
            }
            list.add(newVO);
        } else {
            for (FbsBudgetDetailVO fbsBudgetDetailVO : list) {
                fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getNatualMoney());
            }
        }
        model.addAttribute("flag", "input");
        model.addAttribute("dataList", list);
        model.addAttribute("budget", budget);
        model.addAttribute("dtlColList", dtlColList);
        model.addAttribute("dtlColValueList", dtlColValueList);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("budgetDtl", budgetDtl);
    }
	/**
	 * 进入明细录入界面-季滚动上报
	 *
	 * @param model
	 */
	public void inputDtlRollWeek(FbsBudgetDetailVO vo, FbsBudgetDetailViewVO model) {
		/**得到表格的列*/
		List<String> dtlColList = new ArrayList<String>();
		List<String> dtlColValueList = new ArrayList<String>();
		String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
		dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
		String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
		for (int i = 0; i < dtlCols.length; i++) {
			dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
			dtlColValueList.add(dtlCols[i]);
		}
		FbsBudget budget = fbsBudgetService.get(vo.getBudgetId(), true);//根据id得到预算计划主表
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
		FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(vo.getDetailId(), entity);
		//周计划滚动;
		int wimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
		Date weekBeginDate = budget.getBeginDate();
		List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
		List<String> weeksHid = new ArrayList<>();
		int thismonth = DateUtils.getMonth(weekBeginDate);
		for (int i = 1; i <= wimodeRoll; i++) {
			FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
			int week = DateUtils.getWeekOfMonth(weekBeginDate);
			int month = DateUtils.getMonth(weekBeginDate);
			if(month > thismonth){
				week = week -1;
			}
			//保存周
			String weekShow = "";
			weekShow +=month+"月第"+week+"周";
			fbsBudgetPaternityVO.setPaternal(weekShow);
			weeks.add(fbsBudgetPaternityVO);
			weeksHid.add(week+"");
			weekBeginDate = DateUtils.addWeek(weekBeginDate, 1);
		}
		String beginDate = DateUtils.formatDate(budget.getBeginDate());
		String endDate = DateUtils.formatDate(budget.getEndDate());
		List<FbsBudgetDetailVO> list = this.getDetailList(budget.getId(), vo.getDetailId());
		Date start = budget.getBeginDate();
		for(int i=1;i<weeks.size();i++){
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(start, budget.getTimeType());
			endDate = DateUtils.formatDate(nextFbsBudgetPeriodVO.getEndDate());
			start = DateUtils.addWeek(start,1);
		}
		if (EmptyUtils.isEmptyList(list)) {
			FbsBudgetDetailVO newVO = new FbsBudgetDetailVO();
			if (DateUtils.getDate().getTime() > budget.getBeginDate().getTime() && DateUtils.getDate().getTime() < budget.getEndDate().getTime()) {
				newVO.setVestingDate(DateUtils.getDate());
			} else {
				newVO.setVestingDate(budget.getBeginDate());
			}
			list.add(newVO);
		} else {
			for (FbsBudgetDetailVO fbsBudgetDetailVO : list) {
				fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getNatualMoney());
			}
		}
        model.setFlag(  "input");
        model.setDataList(  list);
        model.setBudget(  budget);
        model.setDtlColList(  dtlColList);
        model.setDtlColValueList( dtlColValueList);
        model.setBeginDate(  beginDate);
        model.setEndDate( endDate);
        model.setBudgetDtl(  budgetDtl);
	}
    public void inputDtlRollWeek(FbsBudgetDetailVO vo, Model model) {
        /**得到表格的列*/
        List<String> dtlColList = new ArrayList<String>();
        List<String> dtlColValueList = new ArrayList<String>();
        String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
        dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
        String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < dtlCols.length; i++) {
            dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
            dtlColValueList.add(dtlCols[i]);
        }
        FbsBudget budget = fbsBudgetService.get(vo.getBudgetId(), true);//根据id得到预算计划主表
        String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
        FbsBudgetDtlAbs budgetDtl = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(vo.getDetailId(), entity);
        //周计划滚动;
        int wimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
        Date weekBeginDate = budget.getBeginDate();
        List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
        List<String> weeksHid = new ArrayList<>();
        int thismonth = DateUtils.getMonth(weekBeginDate);
        for (int i = 1; i <= wimodeRoll; i++) {
            FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
            int week = DateUtils.getWeekOfMonth(weekBeginDate);
            int month = DateUtils.getMonth(weekBeginDate);
            if(month > thismonth){
                week = week -1;
            }
            //保存周
            String weekShow = "";
            weekShow +=month+"月第"+week+"周";
            fbsBudgetPaternityVO.setPaternal(weekShow);
            weeks.add(fbsBudgetPaternityVO);
            weeksHid.add(week+"");
            weekBeginDate = DateUtils.addWeek(weekBeginDate, 1);
        }
        String beginDate = DateUtils.formatDate(budget.getBeginDate());
        String endDate = DateUtils.formatDate(budget.getEndDate());
        List<FbsBudgetDetailVO> list = this.getDetailList(budget.getId(), vo.getDetailId());
        Date start = budget.getBeginDate();
        for(int i=1;i<weeks.size();i++){
            FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(start, budget.getTimeType());
            endDate = DateUtils.formatDate(nextFbsBudgetPeriodVO.getEndDate());
            start = DateUtils.addWeek(start,1);
        }
        if (EmptyUtils.isEmptyList(list)) {
            FbsBudgetDetailVO newVO = new FbsBudgetDetailVO();
            if (DateUtils.getDate().getTime() > budget.getBeginDate().getTime() && DateUtils.getDate().getTime() < budget.getEndDate().getTime()) {
                newVO.setVestingDate(DateUtils.getDate());
            } else {
                newVO.setVestingDate(budget.getBeginDate());
            }
            list.add(newVO);
        } else {
            for (FbsBudgetDetailVO fbsBudgetDetailVO : list) {
                fbsBudgetDetailVO.setTempNatualMoney(fbsBudgetDetailVO.getNatualMoney());
            }
        }
        model.addAttribute("flag", "input");
        model.addAttribute("dataList", list);
        model.addAttribute("budget", budget);
        model.addAttribute("dtlColList", dtlColList);
        model.addAttribute("dtlColValueList", dtlColValueList);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("budgetDtl", budgetDtl);
    }
	/**
	 * 批量保存明细-月滚动上报
	 *
	 * @param bean
	 * @throws Exception
	 */
	public Map<String, Object> saveRollMonth(FbsBudgetDetailTransmitVO bean) throws Exception {
		FbsBudget budget = fbsBudgetService.get(bean.getBudgetId(), true);//根据id得到预算计划主表
		String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
		FbsBudgetDtlAbs fbsBudgetAbs = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(bean.getDetailId(), entity);
		String itemCode = fbsBudgetAbs.getItemCode();
		String itemName = fbsBudgetAbs.getItemName();
		String timeType = budget.getTimeType();
		int month = DateUtils.getMonth(budget.getEndDate());
		//月滚动模式
		int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
		//mRollSubperiod-月滚动模式 滚动分解子周期
		int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
		//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
		String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
		//是否可以跨年
		boolean isSpanYear = false;
		String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
		//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
		if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
				|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
			isSpanYear = true;
		}
		//保存月份
		List<String> months = new ArrayList<String>();
		for (int i = 1; i <= mimodeRoll; i++) {
			if(!isSpanYear) {
				if (month <= 12) {
					months.add(month + "");
					month++;
				}
			}else{
				months.add(month + "");
				month++;
			}
		}
		List<FbsBudget> nextFbsBudgetList = new ArrayList<>();
		List<FbsBudgetDtlAbs> nextFbsBudgetAbsList = new ArrayList<>();
		Date beginDate = budget.getBeginDate();
		int length = months.size();
		if (mRollSubperiod>0) {
			List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(budget.getBeginDate());
			length += listPeriod.size();
		}
		for(int i = 1;i< months.size();i++){
			FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, timeType);
			FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), timeType, nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
			FbsBudgetDtlAbs nextFbsBudgetAbs = this.fbsBudgetDtlAbsService.getByBudgetId(nextFbsBudget.getId(), itemCode, timeType);
			beginDate = DateUtils.addMonth(beginDate,1);
			nextFbsBudgetList.add(nextFbsBudget);
			nextFbsBudgetAbsList.add(nextFbsBudgetAbs);
			if (mRollSubperiod>i) {
				List<FbsBudgetPeriodVO> nextlistPeriod = fbsBudgetPeriodService.getWeekByMonth(nextFbsBudget.getBeginDate());
				length += nextlistPeriod.size();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<FbsBudgetDetail> list = new ArrayList<>();
		if (!EmptyUtils.isEmpty(bean.getVestingDate())) {
			for (int i = 0; i < bean.getVestingDate().length; i++) {
				BigDecimal result = BigDecimal.ZERO;
				FbsBudgetDetail detail = new FbsBudgetDetail();
				if (!EmptyUtils.isEmptyArray(bean.getbMoney()) && !EmptyUtils.isBlank(bean.getbMoney()[i])) {
					detail.setBMoney(new BigDecimal(bean.getbMoney()[i]));
					result = result.add(new BigDecimal(bean.getbMoney()[i]));
				}
				if (!EmptyUtils.isEmptyArray(bean.getBtempMoney()) && !EmptyUtils.isBlank(bean.getBtempMoney()[i])) {
					detail.setBtempMoney(new BigDecimal(bean.getBtempMoney()[i]));
				}
				// zuoy 2018-11-08 折合人民币
//				if (!EmptyUtils.isEmptyArray(bean.getTempNatualMoney()) && !EmptyUtils.isBlank(bean.getTempNatualMoney()[i])) {
//					if ("input".equals(bean.getFlag())) {
//						detail.setNatualMoney(new BigDecimal(bean.getTempNatualMoney()[i]));
//					} else {
//						detail.setBtempNatualMoney(new BigDecimal(bean.getTempNatualMoney()[i]));
//
//					}
//					result = result.add(new BigDecimal(bean.getTempNatualMoney()[i]));
//				}
				if (null != bean.getId() && bean.getId().length >0 && !StringUtils.isEmpty(bean.getId()[i])) {
					FbsBudgetDetail t = this.get(bean.getId()[i]);
					if (!EmptyUtils.isEmpty(t)) {
						if (!"input".equals(bean.getFlag())) {
							detail.setNatualMoney(t.getNatualMoney());
						}
						detail.setBsuperMoney(t.getBsuperMoney());
						detail.setBsuperNatualMoney(t.getBsuperNatualMoney());
					}
				}
				detail.setItemCode(itemCode);
				detail.setItemName(itemName);
				detail.setTimeType(budget.getTimeType());
				Date vestingDate = format.parse(bean.getVestingDate()[i].trim());
				detail.setVestingDate(vestingDate);
				Boolean isSetDetailId = false;
				boolean isMRollSubperiod = false;
				for(int j =0 ;j< nextFbsBudgetList.size();j++){
					FbsBudget nextFbsBudget = nextFbsBudgetList.get(j);
					FbsBudgetDtlAbs nextFbsBudgetAbs = nextFbsBudgetAbsList.get(j);
					if(vestingDate.getTime() >= nextFbsBudget.getBeginDate().getTime() && vestingDate.getTime() <= nextFbsBudget.getEndDate().getTime()){
						isSetDetailId = true;
						detail.setDetailId(nextFbsBudgetAbs.getId());
						if(j>mRollSubperiod-2) {
							isMRollSubperiod = true;
							BigDecimal money = (BigDecimal) map.get(j + 2 + "");
							if (null != money) {
								map.put(j + 2 + "", money.add(result));
							} else {
								map.put(j + 2 + "", result);
							}
						}
					}
				}
				List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(budget.getBeginDate());
				if(!isSetDetailId){
					detail.setDetailId(fbsBudgetAbs.getId());
					String key = "";
					if (mRollSubperiod>0) {
						// 双月周滚动
						for (int k = 0; k < listPeriod.size(); k++) {
							FbsBudgetPeriodVO vo = listPeriod.get(k);
							if (vestingDate.getTime() >= vo.getBeginDate().getTime() && vestingDate.getTime() <= vo.getEndDate().getTime()) {
								key = String.valueOf((k + months.size()+1));
								break;
							}
						}
						BigDecimal money = (BigDecimal) map.get(key);
						if (null != money) {
							map.put(key, money.add(result));
						} else {
							map.put(key, result);
						}
					} else {
						// 月滚动
						Date start = budget.getBeginDate();
						for(int j=0;j<months.size();j++) {
							FbsBudgetPeriodVO vo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(start, timeType);
							if (vestingDate.getTime() >= vo.getBeginDate().getTime() && vestingDate.getTime() <= vo.getEndDate().getTime()) {
								key = String.valueOf((j + 1));
								break;
							}
							start = DateUtils.addMonth(start,1);
						}
						BigDecimal money = (BigDecimal) map.get(key);
						if (null != money) {
							map.put(key, money.add(result));
						} else {
							map.put(key, result);
						}
					}
				}else if(!isMRollSubperiod && mRollSubperiod >0){
					//月分解周
					int size = mRollSubperiod -1;
					if(size  > nextFbsBudgetList.size()){
						size = nextFbsBudgetList.size();
					}
					String key = months.size()+1+listPeriod.size()+"";
					for(int j =0 ;j< size;j++){
						List<FbsBudgetPeriodVO> nextListPeriod = fbsBudgetPeriodService.getWeekByMonth(nextFbsBudgetList.get(j).getBeginDate());
						for (int k = 0; k < nextListPeriod.size(); k++) {
							FbsBudgetPeriodVO vo = nextListPeriod.get(k);
							if (vestingDate.getTime() >= vo.getBeginDate().getTime() && vestingDate.getTime() <= vo.getEndDate().getTime()) {
								key = String.valueOf((Integer.parseInt(key)+k));
								break;
							}
						}
					}
					BigDecimal money = (BigDecimal) map.get(key);
					if (null != money) {
						map.put(key, money.add(result));
					} else {
						map.put(key, result);
					}
				}
				if (!EmptyUtils.isEmptyArray(bean.getCurId())) {
					detail.setCurId(bean.getCurId()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getSettlType())) {
					detail.setSettlType(bean.getSettlType()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getCorpName())) {
					detail.setCorpName(bean.getCorpName()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getPurpose())) {
					detail.setPurpose(bean.getPurpose()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getWishPayday()) && !EmptyUtils.isEmpty(bean.getWishPayday()[i])) {
					detail.setWishPayday(format.parse(bean.getWishPayday()[i].trim()));
				}
				if (!EmptyUtils.isEmptyArray(bean.getPayerAcc())) {
					detail.setPayerAcc(bean.getPayerAcc()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getPayeeAcc())) {
					detail.setPayeeAcc(bean.getPayeeAcc()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getPayeeAccName())) {
					detail.setPayeeAccName(bean.getPayeeAccName()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getPayeeBank())) {
					detail.setPayeeBank(bean.getPayeeBank()[i]);
				}
				if (!EmptyUtils.isEmptyArray(bean.getContract())) {
					detail.setContract(bean.getContract()[i]);
				}
				list.add(detail);
			}
		}
		// 先删除之前的明细
		this.fbsBudgetDetailDao.delByDetailId(fbsBudgetAbs.getId());
		for(int j =0 ;j< nextFbsBudgetAbsList.size();j++) {
			FbsBudgetDtlAbs nextFbsBudgetAbs = nextFbsBudgetAbsList.get(j);
			this.fbsBudgetDetailDao.delByDetailId(nextFbsBudgetAbs.getId());
		}
		this.batchSave(list);
		if (EmptyUtils.isEmptyList(list)) {
			for(int j =1 ;j<= length;j++) {
				map.put(j+"", BigDecimal.ZERO);
			}
		}
//		if (!map.containsKey("1")) {
//			map.put("1", BigDecimal.ZERO);
//		}
		for(int j =1 ;j<= length;j++) {
			if (!map.containsKey(j + "")){
				map.put(j + "", BigDecimal.ZERO);
			}
		}
		return map;
	}
    /**
     * 批量保存明细-季滚动上报
     *
     * @param bean
     * @throws Exception
     */
    public Map<String, Object> saveRollQuarter(FbsBudgetDetailTransmitVO bean) throws Exception {
        FbsBudget budget = fbsBudgetService.get(bean.getBudgetId(), true);//根据id得到预算计划主表
        String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
        FbsBudgetDtlAbs fbsBudgetAbs = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(bean.getDetailId(), entity);
        String itemCode = fbsBudgetAbs.getItemCode();
        String itemName = fbsBudgetAbs.getItemName();
        String timeType = budget.getTimeType();
        //季计划滚动;
        int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
        //季计划滚动，分解月
        int qRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
        int quarter = DateUtils.getQuarterOfYear(budget.getBeginDate());
        int year = DateUtils.getYear(budget.getBeginDate());
        //保存季度
        List<String> quarters = new ArrayList<>();
        for (int i = 1; i <= qimodeRoll; i++) {
            String quarterShow = "";
            quarterShow +=year+"年"+quarter+"季度</br>";
            quarters.add(quarterShow);
            quarter++;
            if(quarter>4){
                year++;
                quarter = 1;
            }
        }
        List<FbsBudget> nextFbsBudgetList = new ArrayList<>();
        List<FbsBudgetDtlAbs> nextFbsBudgetAbsList = new ArrayList<>();
        Date beginDate = budget.getBeginDate();
		int length = quarters.size();
		if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimodeRoll)&&qRollSubperiod>0) {
			length += 3;
		}
        for(int i = 1;i< quarters.size();i++){
            FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, timeType);
            FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), timeType, nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
            FbsBudgetDtlAbs nextFbsBudgetAbs = this.fbsBudgetDtlAbsService.getByBudgetId(nextFbsBudget.getId(), itemCode, timeType);
            beginDate = DateUtils.addMonth(beginDate,3);
            nextFbsBudgetList.add(nextFbsBudget);
            nextFbsBudgetAbsList.add(nextFbsBudgetAbs);
			if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimodeRoll)&&qRollSubperiod>i) {
				length += 3;
			}
        }
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<FbsBudgetDetail> list = new ArrayList<>();
        if (!EmptyUtils.isEmpty(bean.getVestingDate())) {
            for (int i = 0; i < bean.getVestingDate().length; i++) {
                BigDecimal result = BigDecimal.ZERO;
                FbsBudgetDetail detail = new FbsBudgetDetail();
                if (!EmptyUtils.isEmptyArray(bean.getbMoney()) && !EmptyUtils.isBlank(bean.getbMoney()[i])) {
                    detail.setBMoney(new BigDecimal(bean.getbMoney()[i]));
					result = result.add(new BigDecimal(bean.getbMoney()[i]));
                }
                if (!EmptyUtils.isEmptyArray(bean.getBtempMoney()) && !EmptyUtils.isBlank(bean.getBtempMoney()[i])) {
                    detail.setBtempMoney(new BigDecimal(bean.getBtempMoney()[i]));
                }
//                // zuoy 2018-11-08 折合人民币
//                if (!EmptyUtils.isEmptyArray(bean.getTempNatualMoney()) && !EmptyUtils.isBlank(bean.getTempNatualMoney()[i])) {
//                    if ("input".equals(bean.getFlag())) {
//                        detail.setNatualMoney(new BigDecimal(bean.getTempNatualMoney()[i]));
//                    } else {
//                        detail.setBtempNatualMoney(new BigDecimal(bean.getTempNatualMoney()[i]));
//
//                    }
//                    result = result.add(new BigDecimal(bean.getTempNatualMoney()[i]));
//                }
                if (null != bean.getId() && !StringUtils.isEmpty(bean.getId()[i])) {
                    FbsBudgetDetail t = this.get(bean.getId()[i]);
                    if (!EmptyUtils.isEmpty(t)) {
                        if (!"input".equals(bean.getFlag())) {
                            detail.setNatualMoney(t.getNatualMoney());
                        }
                        detail.setBsuperMoney(t.getBsuperMoney());
                        detail.setBsuperNatualMoney(t.getBsuperNatualMoney());
                    }
                }
                detail.setItemCode(itemCode);
                detail.setItemName(itemName);
                detail.setTimeType(budget.getTimeType());
                Date vestingDate = format.parse(bean.getVestingDate()[i].trim());
                detail.setVestingDate(vestingDate);
                Boolean isSetDetailId = false;
                boolean isMRollSubperiod = false;
				for(int j =0 ;j< nextFbsBudgetList.size();j++){
					FbsBudget nextFbsBudget = nextFbsBudgetList.get(j);
					FbsBudgetDtlAbs nextFbsBudgetAbs = nextFbsBudgetAbsList.get(j);
					if(vestingDate.getTime() >= nextFbsBudget.getBeginDate().getTime() && vestingDate.getTime() <= nextFbsBudget.getEndDate().getTime()){
						isSetDetailId = true;
						detail.setDetailId(nextFbsBudgetAbs.getId());
						if(j>qRollSubperiod-2) {
							isMRollSubperiod = true;
							BigDecimal money = (BigDecimal) map.get(j + 2 + "");
							if (null != money) {
								map.put(j + 2 + "", money.add(result));
							} else {
								map.put(j + 2 + "", result);
							}
						}
					}
				}
				if(!isSetDetailId){
					detail.setDetailId(fbsBudgetAbs.getId());
					String key = "";
					if (qRollSubperiod>0) {
						Date quarterBegindate = budget.getBeginDate();
						Date quarterendDate = DateUtils.addMonth(quarterBegindate,1);
						for (int k = 0; k < 3; k++) {
							if (vestingDate.getTime() >= quarterBegindate.getTime() && vestingDate.getTime() < quarterendDate.getTime()) {
								key = String.valueOf((k + quarters.size()+1));
								break;
							}
							quarterBegindate = DateUtils.addMonth(quarterBegindate,1);
							quarterendDate = DateUtils.addMonth(quarterBegindate,1);
						}
						BigDecimal money = (BigDecimal) map.get(key);
						if (null != money) {
							map.put(key, money.add(result));
						} else {
							map.put(key, result);
						}
					} else {
						BigDecimal money = (BigDecimal) map.get("1");
						if (null != money) {
							map.put("1", money.add(result));
						} else {
							map.put("1", result);
						}
					}
				}else if(!isMRollSubperiod && qRollSubperiod >0){
					//季分解月
					int size = qRollSubperiod -1;
					if(size  > nextFbsBudgetList.size()){
						size = nextFbsBudgetList.size();
					}
					String key = quarters.size()+4+"";
					for(int j =0 ;j< size;j++){
						Date quarterBegindate = nextFbsBudgetList.get(j).getBeginDate();
						Date quarterendDate = DateUtils.addMonth(quarterBegindate,1);
						for (int k = 0; k < 3; k++) {
							if (vestingDate.getTime() >= quarterBegindate.getTime() && vestingDate.getTime() < quarterendDate.getTime()) {
								key = String.valueOf((Integer.parseInt(key)+k));
								break;
							}
							quarterBegindate = DateUtils.addMonth(quarterBegindate,1);
							quarterendDate = DateUtils.addMonth(quarterBegindate,1);
						}
					}
					BigDecimal money = (BigDecimal) map.get(key);
					if (null != money) {
						map.put(key, money.add(result));
					} else {
						map.put(key, result);
					}
				}
                if (!EmptyUtils.isEmptyArray(bean.getCurId())) {
                    detail.setCurId(bean.getCurId()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getSettlType())) {
                    detail.setSettlType(bean.getSettlType()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getCorpName())) {
                    detail.setCorpName(bean.getCorpName()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getPurpose())) {
                    detail.setPurpose(bean.getPurpose()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getWishPayday()) && !EmptyUtils.isEmpty(bean.getWishPayday()[i])) {
                    detail.setWishPayday(format.parse(bean.getWishPayday()[i].trim()));
                }
                if (!EmptyUtils.isEmptyArray(bean.getPayerAcc())) {
                    detail.setPayerAcc(bean.getPayerAcc()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getPayeeAcc())) {
                    detail.setPayeeAcc(bean.getPayeeAcc()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getPayeeAccName())) {
                    detail.setPayeeAccName(bean.getPayeeAccName()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getPayeeBank())) {
                    detail.setPayeeBank(bean.getPayeeBank()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getContract())) {
                    detail.setContract(bean.getContract()[i]);
                }
                list.add(detail);
            }
        }
        // 先删除之前的明细
        this.fbsBudgetDetailDao.delByDetailId(fbsBudgetAbs.getId());
        for(int j =0 ;j< nextFbsBudgetAbsList.size();j++) {
            FbsBudgetDtlAbs nextFbsBudgetAbs = nextFbsBudgetAbsList.get(j);
            this.fbsBudgetDetailDao.delByDetailId(nextFbsBudgetAbs.getId());
        }
        this.batchSave(list);
		if (EmptyUtils.isEmptyList(list)) {
			for(int j =1 ;j<= length;j++) {
				map.put(j+"", BigDecimal.ZERO);
			}
		}
//        if (!map.containsKey("1")) {
//            map.put("1", BigDecimal.ZERO);
//        }
		for(int j =1 ;j<= length;j++) {
			if (!map.containsKey(j + "")){
				map.put(j + "", BigDecimal.ZERO);
			}
		}
        return map;
    }
    /**
     * 批量保存明细-周滚动上报
     *
     * @param bean
     * @throws Exception
     */
    public Map<String, Object> saveRollWeek(FbsBudgetDetailTransmitVO bean) throws Exception {
        FbsBudget budget = fbsBudgetService.get(bean.getBudgetId(), true);//根据id得到预算计划主表
        String entity = FbsBudgetDtlAbs.getEntityNameByTimeType(budget.getTimeType());//得到明细实体名
        FbsBudgetDtlAbs fbsBudgetAbs = (FbsBudgetDtlAbs) fbsBudgetDetailDao.getDtlById(bean.getDetailId(), entity);
        String itemCode = fbsBudgetAbs.getItemCode();
        String itemName = fbsBudgetAbs.getItemName();
        String timeType = budget.getTimeType();
        //周计划上报模式:1,固定;2,周滚动;
        String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
        //周计划滚动;
        int wimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
        //周计划滚动，分解天
        int wRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
        Date weekBeginDate = budget.getBeginDate();
        List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
        List<String> weeksHid = new ArrayList<>();
        for (int i = 1; i <= wimodeRoll; i++) {
            FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
            int week = DateUtils.getWeekOfMonth(weekBeginDate);
            int month = DateUtils.getMonth(weekBeginDate);
            //保存周
            String weekShow = "";
            weekShow +=month+"月第"+week+"周";
            fbsBudgetPaternityVO.setPaternal(weekShow);
            weeks.add(fbsBudgetPaternityVO);
            weeksHid.add(week+"");
            weekBeginDate = DateUtils.addWeek(weekBeginDate,1);
        }
        List<FbsBudget> nextFbsBudgetList = new ArrayList<>();
        List<FbsBudgetDtlAbs> nextFbsBudgetAbsList = new ArrayList<>();
		int length = weeks.size();
		if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)&&wimodeRoll>0) {
			//一周有多少天
			int count = DateUtils.dayDiffs(budget.getEndDate(),budget.getBeginDate())+1;
			length += count;
		}
        Date beginDate = budget.getBeginDate();
        for(int i = 1;i< weeks.size();i++){
            FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, timeType);
            FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budget.getCorpId(), timeType, nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
            FbsBudgetDtlAbs nextFbsBudgetAbs = this.fbsBudgetDtlAbsService.getByBudgetId(nextFbsBudget.getId(), itemCode, timeType);
            beginDate = DateUtils.addWeek(beginDate,1);
            nextFbsBudgetList.add(nextFbsBudget);
            nextFbsBudgetAbsList.add(nextFbsBudgetAbs);
			if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)&&wRollSubperiod>i) {
				//一周有多少天
				int count = DateUtils.dayDiffs(nextFbsBudget.getEndDate(),nextFbsBudget.getBeginDate())+1;
				length += count;
			}
        }
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<FbsBudgetDetail> list = new ArrayList<>();
        if (!EmptyUtils.isEmpty(bean.getVestingDate())) {
            for (int i = 0; i < bean.getVestingDate().length; i++) {
                BigDecimal result = BigDecimal.ZERO;
                FbsBudgetDetail detail = new FbsBudgetDetail();
                if (!EmptyUtils.isEmptyArray(bean.getbMoney()) && !EmptyUtils.isBlank(bean.getbMoney()[i])) {
                    detail.setBMoney(new BigDecimal(bean.getbMoney()[i]));
					result = result.add(new BigDecimal(bean.getbMoney()[i]));
                }
                if (!EmptyUtils.isEmptyArray(bean.getBtempMoney()) && !EmptyUtils.isBlank(bean.getBtempMoney()[i])) {
                    detail.setBtempMoney(new BigDecimal(bean.getBtempMoney()[i]));
                }
//                // zuoy 2018-11-08 折合人民币
//                if (!EmptyUtils.isEmptyArray(bean.getTempNatualMoney()) && !EmptyUtils.isBlank(bean.getTempNatualMoney()[i])) {
//                    if ("input".equals(bean.getFlag())) {
//                        detail.setNatualMoney(new BigDecimal(bean.getTempNatualMoney()[i]));
//                    } else {
//                        detail.setBtempNatualMoney(new BigDecimal(bean.getTempNatualMoney()[i]));
//
//                    }
//                    result = result.add(new BigDecimal(bean.getTempNatualMoney()[i]));
//                }
                if (null != bean.getId() && !StringUtils.isEmpty(bean.getId()[i])) {
                    FbsBudgetDetail t = this.get(bean.getId()[i]);
                    if (!EmptyUtils.isEmpty(t)) {
                        if (!"input".equals(bean.getFlag())) {
                            detail.setNatualMoney(t.getNatualMoney());
                        }
                        detail.setBsuperMoney(t.getBsuperMoney());
                        detail.setBsuperNatualMoney(t.getBsuperNatualMoney());
                    }
                }
                detail.setItemCode(itemCode);
                detail.setItemName(itemName);
                detail.setTimeType(budget.getTimeType());
                Date vestingDate = format.parse(bean.getVestingDate()[i].trim());
                detail.setVestingDate(vestingDate);
                Boolean isSetDetailId = false;
                boolean isMRollSubperiod = false;
                for(int j =0 ;j< nextFbsBudgetList.size();j++){
                    FbsBudget nextFbsBudget = nextFbsBudgetList.get(j);
                    FbsBudgetDtlAbs nextFbsBudgetAbs = nextFbsBudgetAbsList.get(j);
                    if(vestingDate.getTime() >= nextFbsBudget.getBeginDate().getTime() && vestingDate.getTime() <= nextFbsBudget.getEndDate().getTime()){
                        isSetDetailId = true;
                        detail.setDetailId(nextFbsBudgetAbs.getId());
                        if(j>wRollSubperiod-2) {
                            isMRollSubperiod = true;
                            BigDecimal money = (BigDecimal) map.get(j + 2 + "");
                            if (null != money) {
                                map.put(j + 2 + "", money.add(result));
                            } else {
                                map.put(j + 2 + "", result);
                            }
                        }
                    }
                }
                //一周有多少天
                int count = DateUtils.dayDiffs(budget.getEndDate(),budget.getBeginDate())+1;
                if(!isSetDetailId){
                    detail.setDetailId(fbsBudgetAbs.getId());
                    String key = "";
                    if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode)&&wRollSubperiod>0) {
                        Date dayBegindate = budget.getBeginDate();
                        for (int k = 0; k < count; k++) {
                            if (vestingDate.getTime() >= dayBegindate.getTime() && vestingDate.getTime() <= dayBegindate.getTime()) {
                                key = String.valueOf((k + weeks.size()+1));
                                break;
                            }
                            dayBegindate = DateUtils.addDay(dayBegindate,1);
                        }
                        BigDecimal money = (BigDecimal) map.get(key);
                        if (null != money) {
                            map.put(key, money.add(result));
                        } else {
                            map.put(key, result);
                        }
                    } else {
                        BigDecimal money = (BigDecimal) map.get("1");
                        if (null != money) {
                            map.put("1", money.add(result));
                        } else {
                            map.put("1", result);
                        }
                    }
                }else if(!isMRollSubperiod && wRollSubperiod >0){
                    //周分解天
                    int size = wRollSubperiod -1;
                    if(size  > nextFbsBudgetList.size()){
                        size = nextFbsBudgetList.size();
                    }
                    String key = weeks.size()+1+count+"";
                    for(int j =0 ;j< size;j++){
                        //一周有多少天
                        int nextCount = DateUtils.dayDiffs(nextFbsBudgetList.get(j).getEndDate(),nextFbsBudgetList.get(j).getBeginDate())+1;
                        Date dayBegindate = budget.getBeginDate();
                        for (int k = 0; k < nextCount; k++) {
                            if (vestingDate.getTime() >= dayBegindate.getTime() && vestingDate.getTime() <= dayBegindate.getTime()) {
                                key = String.valueOf((Integer.parseInt(key)+k));
                                break;
                            }
                            dayBegindate = DateUtils.addDay(dayBegindate,1);
                        }
                    }
                    BigDecimal money = (BigDecimal) map.get(key);
                    if (null != money) {
                        map.put(key, money.add(result));
                    } else {
                        map.put(key, result);
                    }
                }
                if (!EmptyUtils.isEmptyArray(bean.getCurId())) {
                    detail.setCurId(bean.getCurId()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getSettlType())) {
                    detail.setSettlType(bean.getSettlType()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getCorpName())) {
                    detail.setCorpName(bean.getCorpName()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getPurpose())) {
                    detail.setPurpose(bean.getPurpose()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getWishPayday()) && !EmptyUtils.isEmpty(bean.getWishPayday()[i])) {
                    detail.setWishPayday(format.parse(bean.getWishPayday()[i].trim()));
                }
                if (!EmptyUtils.isEmptyArray(bean.getPayerAcc())) {
                    detail.setPayerAcc(bean.getPayerAcc()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getPayeeAcc())) {
                    detail.setPayeeAcc(bean.getPayeeAcc()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getPayeeAccName())) {
                    detail.setPayeeAccName(bean.getPayeeAccName()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getPayeeBank())) {
                    detail.setPayeeBank(bean.getPayeeBank()[i]);
                }
                if (!EmptyUtils.isEmptyArray(bean.getContract())) {
                    detail.setContract(bean.getContract()[i]);
                }
                list.add(detail);
            }
        }
        // 先删除之前的明细
        this.fbsBudgetDetailDao.delByDetailId(fbsBudgetAbs.getId());
        for(int j =0 ;j< nextFbsBudgetAbsList.size();j++) {
            FbsBudgetDtlAbs nextFbsBudgetAbs = nextFbsBudgetAbsList.get(j);
            this.fbsBudgetDetailDao.delByDetailId(nextFbsBudgetAbs.getId());
        }
        this.batchSave(list);
		if (EmptyUtils.isEmptyList(list)) {
			for(int j =1 ;j<= length;j++) {
				map.put(j+"", BigDecimal.ZERO);
			}
		}
//        if (!map.containsKey("1")) {
//            map.put("1", BigDecimal.ZERO);
//        }
		for(int j =1 ;j<= length;j++) {
			if (!map.containsKey(j + "")){
				map.put(j + "", BigDecimal.ZERO);
			}
		}
        return map;
    }
	/**
	 * 得到预算的所有明细
	 * @param budgetId
	 * @param entityName
	 * @return
	 */
	public List<FbsBudgetDetail> getDetailListByBudgetId(String budgetId, String entityName) {
		return fbsBudgetDetailDao.getDetailListByBudgetId(budgetId, entityName);
	}
	/**
	 * 普通上报时导入明细
	 * @param list
	 * @param user
	 */
	public void saveInputImport(List<FbsBudgetDetailVO> list, ICurrentUser user) {
		//先删除所有这些科目明细
		for(FbsBudgetDetailVO vo : list) {
			this.fbsBudgetDetailDao.delByDetailId(vo.getDetailId());
		}
		//再重新保存明细
		this.saveImport(list,user);
	}
	/**
	 * 月滚动上报导入明细
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveRollMonthImport(List<FbsBudgetDetailVO> list,ICurrentUser user) throws Exception{
		Map<String, Object> map = new HashMap<>();
		if(!EmptyUtils.isEmpty(list) && list.size() > 0) {
			List<String> ids = new ArrayList<>();
			String id = list.get(0).getBudgetId();
			ids.add(id);
			//再重新计算key和科目金额
			FbsBudgetVO budgetVO = fbsBudgetService.getBudget(id);
			String timeType = budgetVO.getTimeType();
			int month = DateUtils.getMonth(budgetVO.getEndDate());
			//月滚动模式
			int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
			//mRollSubperiod-月滚动模式 滚动分解子周期
			int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
			//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
			String mimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MIMODE);
			//是否可以跨年
			boolean isSpanYear = false;
			String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
			//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
			if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
					|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
				isSpanYear = true;
			}
			//保存月份
			List<String> months = new ArrayList<String>();
			for (int i = 1; i <= mimodeRoll; i++) {
				if (!isSpanYear) {
					if (month <= 12) {
						months.add(month + "");
						month++;
					}
				} else {
					months.add(month + "");
					month++;
				}
			}
			List<FbsBudget> nextFbsBudgetList = new ArrayList<>();
			Date beginDate = budgetVO.getBeginDate();
			int length = months.size();
			if (mRollSubperiod > 0) {
				List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(budgetVO.getBeginDate());
				length += listPeriod.size();
			}
			for (int i = 1; i < months.size(); i++) {
				FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, timeType);
				FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budgetVO.getCorpId(), timeType, nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
				beginDate = DateUtils.addMonth(beginDate, 1);
				nextFbsBudgetList.add(nextFbsBudget);
				ids.add(nextFbsBudget.getId());
				if (mRollSubperiod > i) {
					List<FbsBudgetPeriodVO> nextlistPeriod = fbsBudgetPeriodService.getWeekByMonth(nextFbsBudget.getBeginDate());
					length += nextlistPeriod.size();
				}
			}
			for (int i = 0; i < list.size(); i++) {
				BigDecimal result = BigDecimal.ZERO;
				FbsBudgetDetailVO fbsBudgetDetailVO = list.get(i);
				if (!EmptyUtils.isEmpty(fbsBudgetDetailVO.getbMoney())) {
					result = result.add(fbsBudgetDetailVO.getbMoney());
				}
				Boolean isSetDetailId = false;
				for (int j = 0; j < nextFbsBudgetList.size(); j++) {
					FbsBudget nextFbsBudget = nextFbsBudgetList.get(j);
					List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(nextFbsBudget.getId(), timeType);
					if (fbsBudgetDetailVO.getVestingDate().getTime() >= nextFbsBudget.getBeginDate().getTime() && fbsBudgetDetailVO.getVestingDate().getTime() <= nextFbsBudget.getEndDate().getTime()) {
						isSetDetailId = true;
						if (j > mRollSubperiod - 2) {
							BigDecimal money = (BigDecimal) map.get(j + 2 + "_"+fbsBudgetDetailVO.getItemCode());
							if (null != money) {
								map.put(j + 2 + "_"+fbsBudgetDetailVO.getItemCode(), money.add(result));
							} else {
								map.put(j + 2 + "_"+fbsBudgetDetailVO.getItemCode(), result);
							}
						}
						for(FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : dtlList){
							if(fbsBudgetDtlAbsVO.getItemCode().equals(fbsBudgetDetailVO.getItemCode())){
								fbsBudgetDetailVO.setDetailId(fbsBudgetDtlAbsVO.getId());
								break;
							}
						}
						fbsBudgetDetailVO.setBudgetId(nextFbsBudget.getId());
					}
				}
				List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(budgetVO.getBeginDate());
				if (!isSetDetailId) {
					String key = "";
					if (mRollSubperiod > 0) {
						for (int k = 0; k < listPeriod.size(); k++) {
							FbsBudgetPeriodVO vo = listPeriod.get(k);
							if (fbsBudgetDetailVO.getVestingDate().getTime() >= vo.getBeginDate().getTime() && fbsBudgetDetailVO.getVestingDate().getTime() <= vo.getEndDate().getTime()) {
								key = String.valueOf((k + months.size() + 1))+"_"+fbsBudgetDetailVO.getItemCode();
								break;
							}
						}
						BigDecimal money = (BigDecimal) map.get(key);
						if (null != money) {
							map.put(key, money.add(result));
						} else {
							map.put(key, result);
						}
					} else {
						// 月滚动
						Date start = budgetVO.getBeginDate();
						for (int j = 0; j < months.size(); j++) {
							FbsBudgetPeriodVO vo = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(start, timeType);
							if (fbsBudgetDetailVO.getVestingDate().getTime() >= vo.getBeginDate().getTime() && fbsBudgetDetailVO.getVestingDate().getTime() <= vo.getEndDate().getTime()) {
								key = (j + 1)+"_"+fbsBudgetDetailVO.getItemCode();
								break;
							}
							start = DateUtils.addMonth(start, 1);
						}
						BigDecimal money = (BigDecimal) map.get(key);
						if (null != money) {
							map.put(key, money.add(result));
						} else {
							map.put(key, result);
						}
					}
				}
			}
			List<FbsBudgetDtlAbsVO> fbsBudgetDtlAbsVOList = budgetVO.getDtlList();
			if (EmptyUtils.isEmptyList(list)) {
				for (int j = 1; j <= length; j++) {
					for(FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : fbsBudgetDtlAbsVOList) {
						if(CommonConstant.NO.equals(fbsBudgetDtlAbsVO.getIsWrite()) || CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsBeginPeriod())
								|| CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsEndPeriod())) {
							continue;
						}
						map.put(j + "_"+fbsBudgetDtlAbsVO.getItemCode(), BigDecimal.ZERO);
					}
				}
			}
			for (int j = 1; j <= length; j++) {
				for(FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : fbsBudgetDtlAbsVOList) {
					if(CommonConstant.NO.equals(fbsBudgetDtlAbsVO.getIsWrite()) || CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsBeginPeriod())
							|| CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsEndPeriod())) {
						continue;
					}
					if (!map.containsKey(j + "_"+fbsBudgetDtlAbsVO.getItemCode())) {
						map.put(j + "_"+fbsBudgetDtlAbsVO.getItemCode(), BigDecimal.ZERO);
					}
				}
			}
			String table = this.getTableName(timeType);
			//先删除所有这些科目明细
			for(String budgetid : ids) {
				this.fbsBudgetDetailDao.delColloctDetail(budgetid,table);
			}
			//再重新保存明细
			this.saveImport(list,user);
		}
		return map;
	}
	/**
	 * 季滚动上报 导入明细
	 * @param list
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveRollQuarterImport(List<FbsBudgetDetailVO> list, ICurrentUser user) throws Exception {
		Map<String, Object> map = new HashMap<>();
		//先删除所有这些科目明细
		for (FbsBudgetDetailVO vo : list) {
			this.fbsBudgetDetailDao.delByDetailId(vo.getDetailId());
		}
		//再重新保存明细
		this.saveImport(list, user);

		if (!EmptyUtils.isEmpty(list) && list.size() > 0) {

			String id = list.get(0).getBudgetId();
			//再重新计算key和科目金额
			FbsBudgetVO budgetVO = fbsBudgetService.getBudget(id);
			String timeType = budgetVO.getTimeType();
			//季计划滚动;
			int qimodeRoll = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
			//季计划滚动，分解月
			int qRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
			int quarter = DateUtils.getQuarterOfYear(budgetVO.getBeginDate());
			int year = DateUtils.getYear(budgetVO.getBeginDate());
			//保存季度
			List<String> quarters = new ArrayList<>();
			for (int i = 1; i <= qimodeRoll; i++) {
				String quarterShow = "";
				quarterShow += year + "年" + quarter + "季度</br>";
				quarters.add(quarterShow);
				quarter++;
				if (quarter > 4) {
					year++;
					quarter = 1;
				}
			}
			List<FbsBudget> nextFbsBudgetList = new ArrayList<>();
			Date beginDate = budgetVO.getBeginDate();
			int length = quarters.size();
			if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimodeRoll) && qRollSubperiod > 0) {
				length += 3;
			}
			for (int i = 1; i < quarters.size(); i++) {
				FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, timeType);
				FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budgetVO.getCorpId(), timeType, nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
				beginDate = DateUtils.addMonth(beginDate, 3);
				nextFbsBudgetList.add(nextFbsBudget);
				if (BudgetConstant.FBS_CONFIG_QIMODE_ROLL.equals(qimodeRoll) && qRollSubperiod > i) {
					length += 3;
				}
			}
			for (int i = 0; i < list.size(); i++) {
				FbsBudgetDetailVO fbsBudgetDetailVO = list.get(i);
				BigDecimal result = BigDecimal.ZERO;
				Date vestingDate = fbsBudgetDetailVO.getVestingDate();
				// 计算折合人民币
				BigDecimal curExrate = iFcExcrateAdjustService.getExchangeRateBy(fbsBudgetDetailVO.getCurId(), fbsBudgetDetailVO.getNatualCurId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
				if (EmptyUtils.isEmpty(curExrate)) {
					curExrate = BigDecimal.ONE;
				}
				if (!EmptyUtils.isEmpty(fbsBudgetDetailVO.getBtempMoney())) {
					fbsBudgetDetailVO.setBtempNatualMoney(fbsBudgetDetailVO.getBtempMoney().multiply(curExrate));
				} else {
					fbsBudgetDetailVO.setNatualMoney(fbsBudgetDetailVO.getBMoney().multiply(curExrate));
				}
				fbsBudgetDetailVO.setExchangeRate(curExrate);
				if (!EmptyUtils.isEmpty(fbsBudgetDetailVO.getNatualMoney())) {
					result = result.add(fbsBudgetDetailVO.getNatualMoney());
				}
				Boolean isSetDetailId = false;
				boolean isMRollSubperiod = false;
				for (int j = 0; j < nextFbsBudgetList.size(); j++) {
					FbsBudget nextFbsBudget = nextFbsBudgetList.get(j);
					if (vestingDate.getTime() >= nextFbsBudget.getBeginDate().getTime() && vestingDate.getTime() <= nextFbsBudget.getEndDate().getTime()) {
						isSetDetailId = true;
						if (j > qRollSubperiod - 2) {
							isMRollSubperiod = true;
							BigDecimal money = (BigDecimal) map.get(j + 2 + "_" + fbsBudgetDetailVO.getItemCode());
							if (null != money) {
								map.put(j + 2 + "_" + fbsBudgetDetailVO.getItemCode(), money.add(result));
							} else {
								map.put(j + 2 + "_" + fbsBudgetDetailVO.getItemCode(), result);
							}
						}
					}
				}
				if (!isSetDetailId) {
					String key = "";
					if (qRollSubperiod > 0) {
						Date quarterBegindate = budgetVO.getBeginDate();
						Date quarterendDate = DateUtils.addMonth(quarterBegindate, 1);
						for (int k = 0; k < 3; k++) {
							if (vestingDate.getTime() >= quarterBegindate.getTime() && vestingDate.getTime() < quarterendDate.getTime()) {
								key = (k + quarters.size() + 1) + "_" + fbsBudgetDetailVO.getItemCode();
								break;
							}
							quarterBegindate = DateUtils.addMonth(quarterBegindate, 1);
							quarterendDate = DateUtils.addMonth(quarterBegindate, 1);
						}
						BigDecimal money = (BigDecimal) map.get(key);
						if (null != money) {
							map.put(key, money.add(result));
						} else {
							map.put(key, result);
						}
					} else {
						BigDecimal money = (BigDecimal) map.get("1" + "_" + fbsBudgetDetailVO.getItemCode());
						if (null != money) {
							map.put("1" + "_" + fbsBudgetDetailVO.getItemCode(), money.add(result));
						} else {
							map.put("1" + "_" + fbsBudgetDetailVO.getItemCode(), result);
						}
					}
				} else if (!isMRollSubperiod && qRollSubperiod > 0) {
					//季分解月
					int size = qRollSubperiod - 1;
					if (size > nextFbsBudgetList.size()) {
						size = nextFbsBudgetList.size();
					}
					String key = quarters.size() + 4 + "";
					for (int j = 0; j < size; j++) {
						Date quarterBegindate = nextFbsBudgetList.get(j).getBeginDate();
						Date quarterendDate = DateUtils.addMonth(quarterBegindate, 1);
						for (int k = 0; k < 3; k++) {
							if (vestingDate.getTime() >= quarterBegindate.getTime() && vestingDate.getTime() < quarterendDate.getTime()) {
								key = (Integer.parseInt(key) + k) + "_" + fbsBudgetDetailVO.getItemCode();
								break;
							}
							quarterBegindate = DateUtils.addMonth(quarterBegindate, 1);
							quarterendDate = DateUtils.addMonth(quarterBegindate, 1);
						}
					}
					BigDecimal money = (BigDecimal) map.get(key);
					if (null != money) {
						map.put(key, money.add(result));
					} else {
						map.put(key, result);
					}
				}
			}
			List<FbsBudgetDtlAbsVO> fbsBudgetDtlAbsVOList = budgetVO.getDtlList();
			if (EmptyUtils.isEmptyList(list)) {
				for (int j = 1; j <= length; j++) {
					for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : fbsBudgetDtlAbsVOList) {
						if (CommonConstant.NO.equals(fbsBudgetDtlAbsVO.getIsWrite()) || CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsBeginPeriod())
								|| CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsEndPeriod())) {
							continue;
						}
						map.put(j + "_" + fbsBudgetDtlAbsVO.getItemCode(), BigDecimal.ZERO);
					}
				}
			}
			for (int j = 1; j <= length; j++) {
				for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : fbsBudgetDtlAbsVOList) {
					if (CommonConstant.NO.equals(fbsBudgetDtlAbsVO.getIsWrite()) || CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsBeginPeriod())
							|| CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsEndPeriod())) {
						continue;
					}
					if (!map.containsKey(j + "_" + fbsBudgetDtlAbsVO.getItemCode())) {
						map.put(j + "_" + fbsBudgetDtlAbsVO.getItemCode(), BigDecimal.ZERO);
					}
				}
			}
		}
		return map;
	}

	/**
	 * 周滚动上报 导入明细
	 * @param list
	 * @throws Exception
	 */
	public Map<String, Object> saveRollWeekImport(List<FbsBudgetDetailVO> list, ICurrentUser user) throws Exception {
		Map<String, Object> map = new HashMap<>();
		//先删除所有这些科目明细
		for (FbsBudgetDetailVO vo : list) {
			this.fbsBudgetDetailDao.delByDetailId(vo.getDetailId());
		}
		//再重新保存明细
		this.saveImport(list, user);

		if (!EmptyUtils.isEmpty(list) && list.size() > 0) {

			String id = list.get(0).getBudgetId();
			//再重新计算key和科目金额
			FbsBudgetVO budgetVO = fbsBudgetService.getBudget(id);
			String timeType = budgetVO.getTimeType();
			//周计划上报模式:1,固定;2,周滚动;
			String wimode = fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODE);
			//周计划滚动;
			int wimodeRoll = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
			//周计划滚动，分解天
			int wRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
			Date weekBeginDate = budgetVO.getBeginDate();
			List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
			List<String> weeksHid = new ArrayList<>();
			for (int i = 1; i <= wimodeRoll; i++) {
				FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
				int week = DateUtils.getWeekOfMonth(weekBeginDate);
				int month = DateUtils.getMonth(weekBeginDate);
				//保存周
				String weekShow = "";
				weekShow += month + "月第" + week + "周";
				fbsBudgetPaternityVO.setPaternal(weekShow);
				weeks.add(fbsBudgetPaternityVO);
				weeksHid.add(week + "");
				weekBeginDate = DateUtils.addWeek(weekBeginDate, 1);
			}
			List<FbsBudget> nextFbsBudgetList = new ArrayList<>();
			int length = weeks.size();
			if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode) && wimodeRoll > 0) {
				//一周有多少天
				int count = DateUtils.dayDiffs(budgetVO.getEndDate(), budgetVO.getBeginDate()) + 1;
				length += count;
			}
			Date beginDate = budgetVO.getBeginDate();
			for (int i = 1; i < weeks.size(); i++) {
				FbsBudgetPeriodVO nextFbsBudgetPeriodVO = fbsBudgetPeriodService.getNextBudgetPeriodByTimeType(beginDate, timeType);
				FbsBudget nextFbsBudget = fbsBudgetService.getCorpBudget(budgetVO.getCorpId(), timeType, nextFbsBudgetPeriodVO.getBeginDate(), nextFbsBudgetPeriodVO.getEndDate());
				beginDate = DateUtils.addWeek(beginDate, 1);
				nextFbsBudgetList.add(nextFbsBudget);
				if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode) && wRollSubperiod > i) {
					//一周有多少天
					int count = DateUtils.dayDiffs(nextFbsBudget.getEndDate(), nextFbsBudget.getBeginDate()) + 1;
					length += count;
				}
			}
            for (FbsBudgetDetailVO fbsBudgetDetailVO : list) {
                BigDecimal result = BigDecimal.ZERO;
                Date vestingDate = fbsBudgetDetailVO.getVestingDate();
                // 计算折合人民币
                BigDecimal curExrate = iFcExcrateAdjustService.getExchangeRateBy(fbsBudgetDetailVO.getCurId(), fbsBudgetDetailVO.getNatualCurId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
                if (EmptyUtils.isEmpty(curExrate)) {
                    curExrate = BigDecimal.ONE;
                }
                if (!EmptyUtils.isEmpty(fbsBudgetDetailVO.getBtempMoney())) {
                    fbsBudgetDetailVO.setBtempNatualMoney(fbsBudgetDetailVO.getBtempMoney().multiply(curExrate));
                } else {
                    fbsBudgetDetailVO.setNatualMoney(fbsBudgetDetailVO.getBMoney().multiply(curExrate));
                }
                fbsBudgetDetailVO.setExchangeRate(curExrate);
                if (!EmptyUtils.isEmpty(fbsBudgetDetailVO.getNatualMoney())) {
                    result = result.add(fbsBudgetDetailVO.getNatualMoney());
                }
                Boolean isSetDetailId = false;
                boolean isMRollSubperiod = false;
                for (int j = 0; j < nextFbsBudgetList.size(); j++) {
                    FbsBudget nextFbsBudget = nextFbsBudgetList.get(j);
                    if (vestingDate.getTime() >= nextFbsBudget.getBeginDate().getTime() && vestingDate.getTime() <= nextFbsBudget.getEndDate().getTime()) {
                        isSetDetailId = true;
                        if (j > wRollSubperiod - 2) {
                            isMRollSubperiod = true;
                            BigDecimal money = (BigDecimal) map.get(j + 2 + "_" + fbsBudgetDetailVO.getItemCode());
                            if (null != money) {
                                map.put(j + 2 + "_" + fbsBudgetDetailVO.getItemCode(), money.add(result));
                            } else {
                                map.put(j + 2 + "_" + fbsBudgetDetailVO.getItemCode(), result);
                            }
                        }
                    }
                }
                //一周有多少天
                int count = DateUtils.dayDiffs(budgetVO.getEndDate(), budgetVO.getBeginDate()) + 1;
                if (!isSetDetailId) {
                    String key = "";
                    if (BudgetConstant.FBS_CONFIG_WIMODE_ROLL.equals(wimode) && wRollSubperiod > 0) {
                        Date dayBegindate = budgetVO.getBeginDate();
                        for (int k = 0; k < count; k++) {
                            if (vestingDate.getTime() >= dayBegindate.getTime() && vestingDate.getTime() <= dayBegindate.getTime()) {
                                key = (k + weeks.size() + 1) + "_" + fbsBudgetDetailVO.getItemCode();
                                break;
                            }
                            dayBegindate = DateUtils.addDay(dayBegindate, 1);
                        }
                        BigDecimal money = (BigDecimal) map.get(key);
                        if (null != money) {
                            map.put(key, money.add(result));
                        } else {
                            map.put(key, result);
                        }
                    } else {
                        BigDecimal money = (BigDecimal) map.get("1" + "_" + fbsBudgetDetailVO.getItemCode());
                        if (null != money) {
                            map.put("1" + "_" + fbsBudgetDetailVO.getItemCode(), money.add(result));
                        } else {
                            map.put("1" + "_" + fbsBudgetDetailVO.getItemCode(), result);
                        }
                    }
                } else if (!isMRollSubperiod && wRollSubperiod > 0) {
                    //周分解天
                    int size = wRollSubperiod - 1;
                    if (size > nextFbsBudgetList.size()) {
                        size = nextFbsBudgetList.size();
                    }
                    String key = weeks.size() + 1 + count + "";
                    for (int j = 0; j < size; j++) {
                        //一周有多少天
                        int nextCount = DateUtils.dayDiffs(nextFbsBudgetList.get(j).getEndDate(), nextFbsBudgetList.get(j).getBeginDate()) + 1;
                        Date dayBegindate = budgetVO.getBeginDate();
                        for (int k = 0; k < nextCount; k++) {
                            if (vestingDate.getTime() >= dayBegindate.getTime() && vestingDate.getTime() <= dayBegindate.getTime()) {
                                key = (Integer.parseInt(key) + k) + "_" + fbsBudgetDetailVO.getItemCode();
                                break;
                            }
                            dayBegindate = DateUtils.addDay(dayBegindate, 1);
                        }
                    }
                    BigDecimal money = (BigDecimal) map.get(key);
                    if (null != money) {
                        map.put(key, money.add(result));
                    } else {
                        map.put(key, result);
                    }
                }
            }
			List<FbsBudgetDtlAbsVO> fbsBudgetDtlAbsVOList = budgetVO.getDtlList();
			if (EmptyUtils.isEmptyList(list)) {
				for (int j = 1; j <= length; j++) {
					for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : fbsBudgetDtlAbsVOList) {
						if (CommonConstant.NO.equals(fbsBudgetDtlAbsVO.getIsWrite()) || CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsBeginPeriod())
								|| CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsEndPeriod())) {
							continue;
						}
						map.put(j + "_" + fbsBudgetDtlAbsVO.getItemCode(), BigDecimal.ZERO);
					}
				}
			}
			for (int j = 1; j <= length; j++) {
				for (FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : fbsBudgetDtlAbsVOList) {
					if (CommonConstant.NO.equals(fbsBudgetDtlAbsVO.getIsWrite()) || CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsBeginPeriod())
							|| CommonConstant.YES.equals(fbsBudgetDtlAbsVO.getIsEndPeriod())) {
						continue;
					}
					if (!map.containsKey(j + "_" + fbsBudgetDtlAbsVO.getItemCode())) {
						map.put(j + "_" + fbsBudgetDtlAbsVO.getItemCode(), BigDecimal.ZERO);
					}
				}
			}
		}
		return map;
	}
}
