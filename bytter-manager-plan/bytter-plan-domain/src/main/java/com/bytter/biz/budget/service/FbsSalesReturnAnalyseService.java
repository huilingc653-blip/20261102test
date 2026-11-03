package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsSalesReturnAnalyseDao;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetProcess;
import com.bytter.biz.budget.entity.FbsSalesReturnAnalyse;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IFcExcrateAdjustService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
/**
 * @author huangx
 * @Description 资金计划分析
 * @date 2019-3-26
 */
@Service("fbsSalesReturnAnalyseService")
@Transactional
public class FbsSalesReturnAnalyseService extends BaseService<FbsSalesReturnAnalyse, String> {
    @Autowired
    private FbsSalesReturnAnalyseDao fbsSalesReturnAnalyseDao;
    @DubboReference(check = false)
	private IConfigService configService;
    @Autowired
    private FbsBudgetService fbsBudgetService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @Autowired
    private FbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FbsSalesReturnAnalyse entity) {
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
    public void save(FbsSalesReturnAnalyse entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FbsSalesReturnAnalyse entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FbsSalesReturnAnalyse entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<FbsSalesReturnAnalyse, String> getEntityDao() {
        return fbsSalesReturnAnalyseDao;
    }
    /**
     * 根据单位ID和周期类型获取往期上报完成的计划
     *
     * @param corpId
     * @param beginDate
     * @param oppAccName
     * @return
     */
    public List<FbsSalesReturnAnalyse> listByCorpId(String corpId, Date beginDate, String oppAccName) {
        return fbsSalesReturnAnalyseDao.listByCorpId(corpId, beginDate, oppAccName);
    }
    /**
     * 汇总
     *
     * @param childIds
     * @param childIds
     */
    public void saveSales(List<String> childIds, List<String> diffReasons) {
        for (int i = 0; i < childIds.size(); i++) {
            FbsSalesReturnAnalyse dtl = this.fbsSalesReturnAnalyseDao.get(childIds.get(i));
            dtl.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_WAIT_CHECK);
            dtl.setDiffReason(diffReasons.get(i));
            this.saveOrUpdate(dtl);
        }
    }
    /**
     * 分析复核
     *
     * @param childIds
     */
    public void saveCheckSales(List<String> childIds, String passFlag) {
        for(int i=0; i<childIds.size(); i++){
            FbsSalesReturnAnalyse dtl = this.fbsSalesReturnAnalyseDao.get(childIds.get(i));
            if("0".equals(passFlag)) {
                dtl.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_CHECK);//审核通过
            }else{
                dtl.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_BACK);//审核打回
            }
            this.saveOrUpdate(dtl);
        }
    }
    /**
     * 获取科目折合人民币的执行金额(不包含资金调拨的计划执行)
     *
     * @param beginDate
     * @param endDate
     * @param itemCodes
     * @param corpId
     * @param oppAccName
     * @param settlType
     * @return
     */
    public BigDecimal collectProcessMoney(Date beginDate, Date endDate, String itemCodes, String corpId, String oppAccName, String settlType) {
        return  this.fbsSalesReturnAnalyseDao.collectProcessMoney(beginDate, endDate, itemCodes, corpId, oppAccName, settlType);
    }
    /**
     * 初始化
     *
     * @param fbsBudgetAnalyse
     * @return
     */
    public List<FbsSalesReturnAnalyse> initSalesReturnAnalyse(FbsBudgetAnalyseVO fbsBudgetAnalyse) {
        String salesReturnItem = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_SALESRETURNITEM).getValue();//销售回款科目设置
        List<FbsSalesReturnAnalyse> dltList = new ArrayList<>();
        if(StringUtils.isNotEmpty(salesReturnItem)) {
            String itemCodes = "";
            String[] salesReturnItems = salesReturnItem.split(";");
            HashMap<String, String> salesReturnItemMap = new HashMap<>();
            for (int i = 0; i < salesReturnItems.length; i++) {
                salesReturnItemMap.put(salesReturnItems[i], salesReturnItems[i]);
                itemCodes = itemCodes + "'" + salesReturnItems[i] + "',";
            }
            itemCodes = itemCodes.substring(0, itemCodes.length()-1);
            Date beginDate = DateUtils.parseDate(fbsBudgetAnalyse.getQueryDate() + "-01");//开始日期
            Date endDate = DateUtils.getLastMonthDate(beginDate);//结束日期
            List<FbsSalesReturnAnalyse> list = this.listByCorpId(fbsBudgetAnalyse.getCorpId(), beginDate, fbsBudgetAnalyse.getOppAccName());
            if (EmptyUtils.isNotEmpty(list)) {
                //更新实际金额
                for (FbsSalesReturnAnalyse sales : list) {
                    if (BudgetConstant.ANALYSE_STATUE_TEMP.equals(sales.getAnalyseStatus())) {
                        sales.setFactBillMoney(this.collectProcessMoney(beginDate, endDate, itemCodes, fbsBudgetAnalyse.getCorpId(), sales.getOppAccName(), BudgetConstant.SETTLTYPE_1));
                        sales.setFactCashMoney(this.collectProcessMoney(beginDate, endDate, itemCodes, fbsBudgetAnalyse.getCorpId(), sales.getOppAccName(), BudgetConstant.SETTLTYPE_0));
                        this.saveOrUpdate(sales);
                    }
                    dltList.add(sales);
                }
            } else {
                FbsBudget budget = fbsBudgetService.getCorpBudget(fbsBudgetAnalyse.getCorpId(), fbsBudgetAnalyse.getTimeType(), beginDate, endDate);
                if (budget != null && budget.getStatus() >= BudgetConstant.STATUE_CHECK) {
                    FbsBudgetVO fbsBudget = fbsBudgetService.getBudget(budget.getId());//获取资金计划
                    String detailIds = "";
                    for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
                        if (salesReturnItemMap.containsKey(dtl.getItemCode())) {
                            detailIds += "'" + dtl.getId() + "',";
                        }
                    }
                    if (StringUtils.isNotEmpty(detailIds)) {
                        detailIds = detailIds.substring(0, detailIds.length() - 1);
                        //获取计划明细
                        List<FbsSalesReturnAnalyseVO> planData = this.fbsSalesReturnAnalyseDao.listByBudgetDetailId(detailIds);
                        HashMap<String, FbsSalesReturnAnalyse> oppAccNameMap = new HashMap<>();
                        for (FbsSalesReturnAnalyseVO vo : planData) {
                            //已存在
                            if (oppAccNameMap.containsKey(vo.getOppAccName())) {
                                FbsSalesReturnAnalyse sales = oppAccNameMap.get(vo.getOppAccName());
                                if (BudgetConstant.SETTLTYPE_1.equals(vo.getSettlType())) {
                                    sales.setPlanBillMoney(sales.getPlanBillMoney().add(vo.getPlanMoney()));
                                    sales.setFactBillMoney(this.collectProcessMoney(beginDate, endDate, itemCodes, fbsBudgetAnalyse.getCorpId(), sales.getOppAccName(), BudgetConstant.SETTLTYPE_1));
                                } else {
                                    sales.setPlanCashMoney(sales.getPlanCashMoney().add(vo.getPlanMoney()));
                                    sales.setFactCashMoney(this.collectProcessMoney(beginDate, endDate, itemCodes, fbsBudgetAnalyse.getCorpId(), sales.getOppAccName(), BudgetConstant.SETTLTYPE_0));
                                }
                                oppAccNameMap.put(vo.getOppAccName(), sales);
                            } else {
                                FbsSalesReturnAnalyse sales = new FbsSalesReturnAnalyse();
                                sales.setCorpId(fbsBudgetAnalyse.getCorpId());
                                sales.setBeginDate(beginDate);
                                sales.setOppAccName(vo.getOppAccName());
                                sales.setPlanCashMoney(BigDecimal.ZERO);
                                sales.setPlanBillMoney(BigDecimal.ZERO);
                                sales.setFactCashMoney(BigDecimal.ZERO);
                                sales.setFactBillMoney(BigDecimal.ZERO);
                                if (BudgetConstant.SETTLTYPE_1.equals(vo.getSettlType())) {
                                    sales.setPlanBillMoney(sales.getPlanBillMoney().add(vo.getPlanMoney()));
                                    sales.setFactBillMoney(this.collectProcessMoney(beginDate, endDate, itemCodes, fbsBudgetAnalyse.getCorpId(), sales.getOppAccName(), BudgetConstant.SETTLTYPE_1));
                                } else {
                                    sales.setPlanCashMoney(sales.getPlanCashMoney().add(vo.getPlanMoney()));
                                    sales.setFactCashMoney(this.collectProcessMoney(beginDate, endDate, itemCodes, fbsBudgetAnalyse.getCorpId(), sales.getOppAccName(), BudgetConstant.SETTLTYPE_0));
                                }
                                sales.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_TEMP);//默认待分析
                                oppAccNameMap.put(vo.getOppAccName(), sales);
                            }
                        }
                        for (Map.Entry<String, FbsSalesReturnAnalyse> entry : oppAccNameMap.entrySet()) {
                            this.saveOrUpdate(entry.getValue());
                            dltList.add(entry.getValue());
                        }
                    }
                }
            }
        }
        return dltList;
    }
    /**
     * 收/支执行排行表
     * @param timeType
     * @param beginDate
     * @param endDate
     * @param itemCode
     * @return
     */
    public List<FbsBudgetAnalyseResportVO> listBudgetByTimeType(String timeType, Date beginDate, Date endDate,String itemCode) {
        List<FbsBudgetAnalyseResportVO> result = this.fbsSalesReturnAnalyseDao.listBudgetByTimeType(timeType,beginDate,endDate,itemCode);
        List<FbsBudgetAnalyseResportVO> list = new ArrayList<>();
        for(FbsBudgetAnalyseResportVO vo : result){
            //过滤掉汇总单位
            boolean collectFlag = fbsCorpBudgetService.getChildCollectFlag(vo.getCorpId());//是否是汇总单位
            if(!collectFlag) {
                vo.setFactMoney(this.setFactMoney(vo.getCorpId(), vo.getBeginDate(), vo.getEndDate(), itemCode));
                vo.setDiffMoney(vo.getPlanMoney().subtract(vo.getFactMoney()));//差异金额 = 计划金额 - 实际金额
                //差异比率=差异金额/计划金额
                if (vo.getDiffMoney().compareTo(BigDecimal.ZERO) > 0 && vo.getPlanMoney().compareTo(BigDecimal.ZERO) > 0) {
                    vo.setDiffRatio(vo.getDiffMoney().divide(vo.getPlanMoney(), 6, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                } else {
                    vo.setDiffRatio(BigDecimal.ZERO);
                }
                //收入/支出执行率=实际金额/计划金额
                if (vo.getFactMoney().compareTo(BigDecimal.ZERO) > 0 && vo.getPlanMoney().compareTo(BigDecimal.ZERO) > 0) {
                    vo.setProcessRatio(vo.getFactMoney().divide(vo.getPlanMoney(), 6, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                } else {
                    vo.setProcessRatio(BigDecimal.ZERO);
                }
                list.add(vo);
            }
        }
        if(!EmptyUtils.isEmptyList(list)) {
            // 按点击数倒序
            Collections.sort(list, new Comparator<FbsBudgetAnalyseResportVO>() {
                @Override
                public int compare(FbsBudgetAnalyseResportVO vo0, FbsBudgetAnalyseResportVO vo1) {
                    BigDecimal processRatio0 = vo0.getProcessRatio();
                    BigDecimal processRatio1 = vo1.getProcessRatio();
                    if (processRatio1.compareTo(processRatio0) > 0) {
                        return 1;
                    } else if (processRatio1.compareTo(processRatio0) == 0) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
        return list;
    }
    /***
     * 设置实际金额
     * @param corpId
     * @param beginDate
     * @param endDate
     * @param itemCode
     * @return
     */
    private BigDecimal setFactMoney(String corpId, Date beginDate, Date endDate, String itemCode){
        List<FbsBudgetProcess> listBudgetProcess = fbsBudgetProcessService.listBudgetAlreadyProcessByCorpId(corpId, beginDate, endDate, itemCode);
        BigDecimal factTotal = BigDecimal.ZERO;
        for (FbsBudgetProcess vo : listBudgetProcess){
            if(EmptyUtils.isEmpty(vo.getProcessCurMoney())){
                vo.setProcessCurMoney(BigDecimal.ZERO);
            }
            factTotal = factTotal.add(vo.getProcessCurMoney());
        }
        return factTotal;
    }
}
