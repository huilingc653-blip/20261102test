package com.bytter.report.domain.stock.service;
import com.bytter.biz.budget.spi.service.IFbsBudgetPeriodService;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.api.constant.ResourceConstant;
import com.bytter.report.domain.stock.dao.FbsBudgetProcessRateDao;
import com.bytter.report.domain.stock.vo.FbsBudgetProcessRateVO;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
@Service("fbsBudgetProcessRateService")
@Transactional
public class FbsBudgetProcessRateService extends BaseService<FbsBudgetProcessRateVO, String> {
    @Autowired
    private FbsBudgetProcessRateDao fbsBudgetProcessRateDao;
    @DubboReference(check = false)
	private IFbsBudgetService fbsBudgetService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFbsBudgetPeriodService fbsBudgetPeriodService;
    @Override
    public BaseDao<FbsBudgetProcessRateVO, String> getEntityDao() {
        return fbsBudgetProcessRateDao;
    }
    /**
     * 计划执行进度
     * @param corpId
     * @param timeType
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<FbsBudgetProcessRateVO> queryBudgetProcessRate(String corpId, String timeType, Date beginDate, Date endDate, ICurrentUser user){
        String tableName = fbsBudgetService.getTableNameByTimeType(timeType);
        beginDate = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate,timeType).getBeginDate();
        endDate = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(endDate,timeType).getEndDate();
        StringBuilder sql = new StringBuilder();
        String resource = ResourceConstant.BUDGET_REPORT_FBSBUDGETPROCESS_QUERY;
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "t.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        return fbsBudgetProcessRateDao.queryBudgetProcessRate(corpId, timeType, beginDate, endDate, tableName, sql.toString());
    }
    /**
     * 根据计划Id获取明细列表
     *
     * @param budgetId
     * @param beginDate
     * @return
     */
    public List<FbsBudgetProcessRateVO> queryBudgetProcessRateDtl(String budgetId, String timeType, Date beginDate, Date endDate) {
        String tableName = fbsBudgetService.getTableNameByTimeType(timeType);
        return fbsBudgetProcessRateDao.queryBudgetProcessRateDtl(budgetId, beginDate, endDate, tableName, false);
    }
    /**
     * 创建柱状图报表
     * @param vo
     * @return
     */
    public Object createFbsBudgetProcessRateBar(FbsBudgetProcessRateVO vo, ICurrentUser user) {
        String tableName = fbsBudgetService.getTableNameByTimeType(vo.getTimeType());
        List<FbsBudgetProcessRateVO>  dataList =  fbsBudgetProcessRateDao.queryBudgetProcessRateDtl(vo.getId(), vo.getBeginDate(), vo.getEndDate(), tableName, true);
        return this.getBarData(dataList);
    }
    /**
     * 组装报表数据
     * @param list
     * @return
     */
    private Object getBarData(List<FbsBudgetProcessRateVO> list) {
        Map<String, Object> map = new HashMap<>();
        //设置上报金额
        List<BigDecimal> bMoneySet = new ArrayList<>();
        //设置执行金额
        List<BigDecimal> processMoneySet = new ArrayList();
        //资金增加
        boolean flagMoney = false;
        for (FbsBudgetProcessRateVO vo : list) {
            if(vo.getItemCode().equals(BudgetConstant.ITEM_CODE_200)) {
                bMoneySet.add(vo.getbMoney());
                processMoneySet.add(vo.getProcessMoney());
                flagMoney = true;
                break;
            }
        }
        if(!flagMoney){
            bMoneySet.add(BigDecimal.ZERO);
            processMoneySet.add(BigDecimal.ZERO);
        }
        //资金减少
        boolean flagProcessMoney = false;
        for (FbsBudgetProcessRateVO vo : list) {
            if(vo.getItemCode().equals(BudgetConstant.ITEM_CODE_400)) {
                bMoneySet.add(vo.getbMoney());
                processMoneySet.add(vo.getProcessMoney());
                flagProcessMoney = true;
                break;
            }
        }
        if(!flagProcessMoney){
            bMoneySet.add(BigDecimal.ZERO);
            processMoneySet.add(BigDecimal.ZERO);
        }
        map.put("bMoneyData", bMoneySet);
        map.put("processMoneyData", processMoneySet);
        return map;
    }
}
