package com.bytter.report.domain.cms.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.report.api.constant.ResourceConstant;
import com.bytter.report.domain.cms.dao.CmsLoanRepayReportDao;
import com.bytter.report.domain.cms.vo.LoanRepayReportVO;
import com.bytter.system.api.ICorpService;
import com.google.common.collect.Sets;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
 * 内部贷款报表服务
 */
@Service("cmsLoanRepayReportService")
public class CmsLoanRepayReportService extends BaseService {
    @Autowired
    private CmsLoanRepayReportDao cmsLoanRepayDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Override
    public BaseDao getEntityDao() {
        return cmsLoanRepayDao;
    }
    /**
     * 查询 贷款还款统计信息
     * @param user
     * @param input
     * @param startDate
     * @param endDate
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listLoanRepay(ICurrentUser user,DataTablesInput input, Date startDate, Date endDate){
        List<String> corpAuth = corpService.getCorpIdsByUserResource(ResourceConstant.CMS_PROVIDE_CAPITALIZE_QUERY, user.getId());//单位权限
        // 这里将贷款和还款分开查询（不是一个sql）是因为贷款的条件是贷款日，还款的条件是还款日，他们没有关系。
        // 各个单位本期之前的贷款汇总
        HashMap<String, BigDecimal> corpId2BeforeTotalLoan=this.cmsLoanRepayDao.findLoanByLoanDate(null,DateUtils.addDay(startDate,-1),corpAuth);
        // 各个单位本期之前的还款汇总
        HashMap<String, BigDecimal> corpId2BeforeTotalRepay=this.cmsLoanRepayDao.findRepayByReplayDate(null,DateUtils.addDay(startDate,-1),corpAuth);
        // 各个单位本期的贷款汇总
        HashMap<String, BigDecimal> corpId2CurrentTotalLoan=this.cmsLoanRepayDao.findLoanByLoanDate(startDate,endDate,corpAuth);
        // 各个单位本期的还款汇总
        HashMap<String, BigDecimal> corpId2CurrentTotalRepay=this.cmsLoanRepayDao.findRepayByReplayDate(startDate,endDate,corpAuth);
        // 期初余额、本期还款、本期贷款 都为0的 单位不要显示
        List<String> corpIds= this.fliterAllZero(corpAuth,corpId2BeforeTotalLoan,corpId2BeforeTotalRepay,corpId2CurrentTotalLoan,corpId2CurrentTotalRepay);
        // 所有的单位，网点
        DataTablesOutput<LoanRepayReportVO> loanRepayReportVOList = this.cmsLoanRepayDao.findLoanCorp(corpIds,input);
        List<LoanRepayReportVO> loanRepayReports=loanRepayReportVOList.getData();
        if(EmptyUtils.isEmpty(loanRepayReports)){
            return loanRepayReportVOList;
        }
        for(LoanRepayReportVO loanRepayReportVO:loanRepayReports){
            String corpId=loanRepayReportVO.getCorpId();
            BigDecimal beginningLoan=getMapVal(corpId2BeforeTotalLoan,corpId).subtract(getMapVal(corpId2BeforeTotalRepay,corpId));
            loanRepayReportVO.setBeginningLoan(beginningLoan);
            loanRepayReportVO.setCurrentLoan(getMapVal(corpId2CurrentTotalLoan,corpId));
            loanRepayReportVO.setCurrentRepay(getMapVal(corpId2CurrentTotalRepay,corpId));
            BigDecimal current=getMapVal(corpId2CurrentTotalLoan,corpId).subtract(getMapVal(corpId2CurrentTotalRepay,corpId));
            loanRepayReportVO.setEndingLoan(loanRepayReportVO.getBeginningLoan().add(current));
        }
        return loanRepayReportVOList;
    }
    /**
     * 所有有权访问的单位中去掉 期初余额、本期还款、本期贷款 都为0 的单位
     * @param corpAuth
     * @param corpId2BeforeTotalLoan
     * @param corpId2BeforeTotalRepay
     * @param corpId2CurrentTotalLoan
     * @param corpId2CurrentTotalRepay
     * @return
     */
    private List<String> fliterAllZero(List<String> corpAuth,HashMap<String, BigDecimal> corpId2BeforeTotalLoan, HashMap<String, BigDecimal> corpId2BeforeTotalRepay, HashMap<String, BigDecimal> corpId2CurrentTotalLoan, HashMap<String, BigDecimal> corpId2CurrentTotalRepay) {
        //有权限访问的单位 在各个单位本期的贷款汇总，各个单位本期的还款汇总都不存在的
        Set<String> corpIdsAllZero= Sets.newHashSet();
        for(String corpId:corpAuth){
            if(equalZeroOrNull(corpId2CurrentTotalLoan,corpId) && equalZeroOrNull(corpId2CurrentTotalRepay,corpId)){
                if(equalZeroOrNull(corpId2BeforeTotalLoan,corpId) && equalZeroOrNull(corpId2BeforeTotalRepay,corpId)){
                    corpIdsAllZero.add(corpId);
                }else{
                    BigDecimal beginningLoan=getMapVal(corpId2BeforeTotalLoan,corpId).subtract(getMapVal(corpId2BeforeTotalRepay,corpId));
                    if(BigDecimal.ZERO.compareTo(beginningLoan)==0){
                        corpIdsAllZero.add(corpId);
                    }
                }
            }
        }
        corpAuth.removeAll(corpIdsAllZero);
        // ，各个单位本期之前的贷款汇总，各个单位本期之前的还款汇总都不存在
        // 本期之前还款汇总 - 本期之前的贷款汇总 = 0;
        return corpAuth;
    }
    private boolean equalZeroOrNull(Map<String,BigDecimal> map,String key){
        return BigDecimal.ZERO.compareTo(getMapVal(map,key)) == 0;
    }
    private BigDecimal getMapVal(Map<String,BigDecimal> map,String key){
        BigDecimal val = map.get(key);
        return EmptyUtils.isEmpty(val) ? BigDecimal.ZERO : val;
    }
}
