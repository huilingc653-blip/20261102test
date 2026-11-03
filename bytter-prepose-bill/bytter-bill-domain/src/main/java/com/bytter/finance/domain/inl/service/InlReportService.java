package com.bytter.finance.domain.inl.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.dao.InlReportDao;
import com.bytter.finance.domain.inl.entity.CmsInlHistory;
import com.bytter.finance.domain.inl.vo.CmsInlReportInterestVO;
import com.bytter.finance.domain.inl.vo.InlReportVO;
import com.bytter.finance.domain.provide.dao.CmsProvideInterestDao;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service("inlReportService")
public class InlReportService extends BaseService implements IUserExcelVerifyHandler<InlReportVO> {
    @Autowired
    private InlReportDao inlReportDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsProvideInterestDao cmsProvideInterestDao;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    /**
     * 内部拆借台账报表
     * 
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listReport(DataTablesInput input, ICurrentUser user) {
        Map<String, Object> params = input.getFormdata();
        List repayStatus = new ArrayList();
        if (null != params.get("repayStatus")) {
            if (params.get("repayStatus") instanceof ArrayList) {
                for (Object status : (List) params.get("repayStatus")) {
                    if (status != null && StringUtils.isNotEmpty(status.toString().trim())) {
                        repayStatus.add(status.toString().trim());
                    }
                }
            } else if (StringUtils.isNotEmpty(params.get("repayStatus").toString().trim())) {
                for (String status : params.get("repayStatus").toString().split(",")) {
                    if (StringUtils.isNotEmpty(status.trim())) {
                        repayStatus.add(status.trim());
                    }
                }
            }
        }
        return inlReportDao.listReport(input, corpService.getUserCorpIds("", user.getId()), repayStatus);
    }
    public DataTablesOutput listReportInterest(DataTablesInput input, ICurrentUser user) {
        Map<String, Object> params = input.getFormdata();
        DataTablesOutput output = inlReportDao.listReportInterest(input, corpService.getUserCorpIds("", user.getId()),
                params);
        if ((null == params.get("ged_nextRepayDate") || StringUtils.isEmpty(params.get("ged_nextRepayDate").toString()))
                && (null == params.get("led_nextRepayDate")
                        || StringUtils.isEmpty(params.get("led_nextRepayDate").toString()))) {
            List<CmsInlReportInterestVO> cmsInlReportInterestVOS = output.getData();
            for (CmsInlReportInterestVO cmsInlReportInterestVO : cmsInlReportInterestVOS) {
                if (StringUtils.isNotEmpty(cmsInlReportInterestVO.getBillNo())) {
                    List<CmsProvideInterest> cmsProvideInterests = cmsProvideInterestDao
                            .ListInterestList(cmsInlReportInterestVO.getBillNo());
                    if (ListUtils.isNotNullOrEmpty(cmsProvideInterests)) {
                        CmsProvideInterest cmsProvideInterest = cmsProvideInterests.get(0);
                        cmsInlReportInterestVO.setNextRepayDate(cmsProvideInterest.getRepayDate());
                        cmsInlReportInterestVO.setNextRepayMoney(cmsProvideInterest.getRepayMoney());
                        cmsInlReportInterestVO.setNextRepayInterest(cmsProvideInterest.getPayInterest());
                        cmsInlReportInterestVO.setNextRepayTotal(
                                cmsProvideInterest.getRepayMoney().add(cmsProvideInterest.getPayInterest()));
                    }
                }
            }
            output.setData(cmsInlReportInterestVOS);
        }
        return output;
    }
    /**
     * 保存导入数据
     */
    public void saveImport(List<InlReportVO> list, ICurrentUser user) {
        List<CmsInlHistory> cmsInlHistories = new ArrayList<>();
        for (InlReportVO inlReportVO : list) {
            CmsInlHistory cmsInlHistory = new CmsInlHistory();
            BeanUtils.copyProperties(inlReportVO, cmsInlHistory);
            cmsInlHistory.setBillNo(InlUtil.getInlInfoNo());
            cmsInlHistory.setBorrowInCorp(corpService.getCorpIdByName(inlReportVO.getBorrowInCorpName()).getId());
            cmsInlHistory.setBorrowOutCorp(corpService.getCorpIdByName(inlReportVO.getBorrowOutCorpName()).getId());
            cmsInlHistory.setCurId(btCurrencyService.getIdByNameOrEnglishCode(inlReportVO.getCurName()));
            cmsInlHistory.setBorrowInCorpName(inlReportVO.getBorrowInCorpName());
            cmsInlHistory.setBorrowOutCorpName(inlReportVO.getBorrowOutCorpName());
            cmsInlHistory.setCurName(inlReportVO.getCurName());
            cmsInlHistory.setLeftSendMoney(inlReportVO.getMoney().subtract(inlReportVO.getSendMoney()));
            cmsInlHistory.setLeftRepayMoeny(cmsInlHistory.getMoney().subtract(cmsInlHistory.getRepayMoney()));
            cmsInlHistory.setTerm(DateUtils.dayDiffs(cmsInlHistory.getEndDate(), cmsInlHistory.getStartDate()));
            cmsInlHistory.setUnit("D");// 默认为日
            cmsInlHistory.setCreateBy(user.getId());
            cmsInlHistory.setCreateDate(new Date());
            if (BigDecimal.ZERO.compareTo(cmsInlHistory.getLeftSendMoney()) == 0) {
                cmsInlHistory.setSendStatus(1);// 全部发放
            } else if (BigDecimal.ZERO.compareTo(inlReportVO.getSendMoney()) == 0) {
                cmsInlHistory.setSendStatus(2);// 未发放
            } else {
                cmsInlHistory.setSendStatus(3);// 部分发放
            }
            if (cmsInlHistory.getMoney().compareTo(cmsInlHistory.getRepayMoney()) == 0) {
                cmsInlHistory.setRepayStatus(1);// 已全部还款
            } else if (BigDecimal.ZERO.compareTo(cmsInlHistory.getRepayInterest()) == 0
                    && BigDecimal.ZERO.compareTo(cmsInlHistory.getRepayMoney()) == 0) {
                cmsInlHistory.setRepayStatus(2);// 未还款
            } else {
                cmsInlHistory.setRepayStatus(3);// 部分还款
            }
            cmsInlHistories.add(cmsInlHistory);
        }
        inlReportDao.batchSave(cmsInlHistories);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(InlReportVO inlReportVO, Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<InlReportVO> list, Object... objects) {
        if (ListUtils.isNotNullOrEmpty(list)) {
            for (InlReportVO inlReportVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                CorpDTO corpDTO1 = corpService.getCorpIdByName(inlReportVO.getBorrowInCorpName());
                if (null == corpDTO1) {
                    errorMsgList.add(InlConstant.CMS_INL_REPORT_IMPORT_CORPERROR1);
                }
                CorpDTO corpDTO2 = corpService.getCorpIdByName(inlReportVO.getBorrowInCorpName());
                if (null == corpDTO2) {
                    errorMsgList.add(InlConstant.CMS_INL_REPORT_IMPORT_CORPERROR2);
                }
                String curId = btCurrencyService.getIdByNameOrEnglishCode(inlReportVO.getCurName());
                if (StringUtils.isEmpty(curId)) {
                    errorMsgList.add(InlConstant.CMS_INL_REPORT_IMPORT_CURERROR2);
                }
                if (DateUtils.dayDiffs(inlReportVO.getEndDate(), inlReportVO.getStartDate()) < 0) {
                    errorMsgList.add(InlConstant.CMS_INL_REPORT_IMPORT_DATEERROR);
                }
                if (ListUtils.isNotNullOrEmpty(errorMsgList)) {
                    inlReportVO.addErrorMsg(errorMsgList);
                }
            }
        }
    }
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
}
