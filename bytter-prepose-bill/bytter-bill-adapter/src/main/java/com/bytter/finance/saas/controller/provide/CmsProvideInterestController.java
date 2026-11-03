package com.bytter.finance.saas.controller.provide;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.vo.RepayPlanVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleDtlVO;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import com.bytter.finance.domain.provide.vo.CmsProvideInterestVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lvwl
 * @version 1.0
 * @Title: Controller
 * @Description: 预计利息明细
 */
@Api(tags = "预计利息明细", value = "/v1-preposebill/cms/provide/cmsprovideinterest")
@RestController("v1CmsProvideInterestController")
@RequestMapping("/v1-preposebill/cms/provide/cmsprovideinterest")
public class CmsProvideInterestController extends BaseController {
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;

    /**
     * 预计利息明细
     *
     * @param sendBillCode 发放编号
     * @param interestID   发放编号
     * @param repairSign   补录标识 0修改、补录
     * @param showSign     显示标识 0查看.1带选择
     * @return
     */
    @GetMapping("/list")
    public AjaxJson<Map<String, Object>> list(String sendBillCode, String interestID, String repairSign, String showSign) {
        Map<String, Object> model = new HashMap<>();
        model.put("sendBillCode", sendBillCode);
        model.put("interestID", interestID);
        model.put("repairSign", repairSign);
        model.put("showSign", showSign);

        //将发放单号前两个字符传到前端用来区分业务
        model.put("type", sendBillCode.substring(0, 2));
        List<CmsProvideInterestVO> list = cmsProvideInterestService.queryInterest(sendBillCode);
        model.put("list", cmsProvideInterestService.buildInterestList(list));
        buildAllInterestList(list, interestID, model);
        model.put("today", DateUtils.parse(DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + " 00:00:00", DateUtils.PATTERN_CLASSICAL));
        return AjaxJson.success(model);
    }

    /**
     * 预计利息明细试算
     *
     * @param repayPlanVo 传参对象
     * @return 页面路径
     */
    @GetMapping("/testList")
    public AjaxJson<Map<String, Object>> testList(RepayPlanVO repayPlanVo, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("repairSign", repayPlanVo.getRepairSign());
        model.put("showSign", repayPlanVo.getShowSign());
        model.put("repayPlanVO", repayPlanVo);
        List<CmsProvideInterestVO> list = cmsProvideInterestService.queryInterestTest(repayPlanVo, request, model);
        BigDecimal sumTotal = BigDecimal.ZERO;
        for (CmsProvideInterestVO cmsProvideInterestVO : list) {
            sumTotal = sumTotal.add(cmsProvideInterestVO.getRepayMoney());
        }
        model.put("sumTotal", sumTotal);
        model.put("listSize", list.size());
        model.put("list", cmsProvideInterestService.buildInterestList(list));
        buildAllInterestList(list, repayPlanVo.getInterestId(), model);
        model.put("today", DateUtils.parse(DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + " 00:00:00", DateUtils.PATTERN_CLASSICAL));
        return AjaxJson.success(model);
    }

    /**
     * 预计利息明细试算
     *
     * @param repayPlanVo 传参对象
     * @return 页面路径
     */
    @LogMessage(content = "预计利息明细试算保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveTestList")

    public AjaxJson saveTestList(RepayPlanVO repayPlanVo, HttpServletRequest request) {
        List<RepaymentSchedule> list = (List<RepaymentSchedule>) request.getSession().getAttribute("repaymentScheduleDtlList");
        cmsProvideInterestService.saveTestList(repayPlanVo, list);
        request.getSession().removeAttribute("repaymentScheduleDtlList");
        return success();
    }

    private void buildAllInterestList(List<CmsProvideInterestVO> cmsProvideInterestVOList, String interestID, Map<String, Object> model) {
        //项目	开始日期 interestStartDate	结束日期 blnInstDate	天数 days	利率 interestRate	本金 loanMoney	利息 payInterest	备注 rmk
        List<CmsProvideInterestVO> allInterests = Lists.newArrayList();
        List<CmsProvideInterestVO> selectableInterests = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cmsProvideInterestVOList)) {
            return;
        }
        Date lastSignRepayDate = cmsProvideInterestVOList.get(0).getSignRepayDate();//上一条记录的计息日期
        int innerCount = 0;
        BigDecimal totalPayInterest = BigDecimal.ZERO;//以signRepayDate分组 对payInterest求和
        Date today = DateUtils.parse(DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + " 00:00:00", DateUtils.PATTERN_CLASSICAL);
        Date selectableSignRepayDate = null;//可选的计息日期
        boolean seletableInterest = false;//可选的利息信息
        for (int i = 0; i < cmsProvideInterestVOList.size(); i++) {
            CmsProvideInterestVO cmsProvideInterestVO = cmsProvideInterestVOList.get(i);
            // logger.debug("i="+i+",status="+cmsProvideInterestVO.getStatus()+",cmsProvideInterestVO="+ JsonUtils.toJSONString(cmsProvideInterestVO));
            if (EmptyUtils.isNotEmpty(interestID)) {
                if (interestID.equals(cmsProvideInterestVO.getId())) {
                    selectableSignRepayDate = cmsProvideInterestVO.getSignRepayDate();
                }
            } else if (cmsProvideInterestVO.getStatus() == Integer.parseInt(CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO) && EmptyUtils.isEmpty(selectableSignRepayDate)) {
                selectableSignRepayDate = cmsProvideInterestVO.getSignRepayDate();
            }
            if (!lastSignRepayDate.equals(cmsProvideInterestVO.getSignRepayDate())) {
                // 生成分组汇总信息
                CmsProvideInterestVO totalInterestInfo = new CmsProvideInterestVO();
                totalInterestInfo.setProjectName("结息");
                totalInterestInfo.setBlnInstDate(cmsProvideInterestVOList.get(i - 1).getBlnInstDate());
                totalInterestInfo.setPayInterest(totalPayInterest);
                String rmk = "";
                if (today.equals(cmsProvideInterestVOList.get(i - 1).getSignRepayDate())) {
                    rmk = "结息日结息";
                } else if (today.after(cmsProvideInterestVOList.get(i - 1).getSignRepayDate())) {
                    rmk = "到期结息";
                }
                totalInterestInfo.setRmk(rmk);
                totalInterestInfo.setSignRepayDate(cmsProvideInterestVOList.get(i - 1).getSignRepayDate());
                allInterests.add(totalInterestInfo);
                // 分组统计值 重置
                innerCount = 0;
                totalPayInterest = BigDecimal.ZERO;
                // 处理可选的利息信息
                seletableInterest = totalInterestInfo.getSignRepayDate().equals(selectableSignRepayDate) ? true : false;
                if (seletableInterest) {
                    selectableInterests.add(totalInterestInfo);
                }
                totalInterestInfo.setSelected(seletableInterest);
            }
            innerCount++;
            allInterests.add(cmsProvideInterestVO);
            cmsProvideInterestVO.setProjectName("(" + innerCount + ")");
            cmsProvideInterestVO.setRmk(null);
            totalPayInterest = totalPayInterest.add(cmsProvideInterestVO.getPayInterest());
            lastSignRepayDate = cmsProvideInterestVO.getSignRepayDate();
            // 处理可选的利息信息
            seletableInterest = cmsProvideInterestVO.getSignRepayDate().equals(selectableSignRepayDate) ? true : false;
            if (seletableInterest) {
                selectableInterests.add(cmsProvideInterestVO);
            }
            cmsProvideInterestVO.setSelected(seletableInterest);
        }
        model.put("allInterests", allInterests);
        model.put("selectableInterests", selectableInterests);
    }

    /**
     * 预计利息明细 - 数据
     */
    @PostMapping("/dataTable")
    public AjaxJson<DataTablesOutput> dataTable(String sendBillCode) {
        List<CmsProvideInterestVO> list = cmsProvideInterestService.queryInterestList(sendBillCode, "", "", "", "", "", "");
        DataTablesOutput out = new DataTablesOutput();
        out.setData(list);
        return AjaxJson.success(out);
    }

    @PostMapping("/calInterest")
    public AjaxJson<List<RepaymentScheduleDtlVO>> calInterest(String repayTypeId, Date sendDate, Date beginDate, Date endDate,
                                                              BigDecimal money, BigDecimal loanRate, BigDecimal rateBase) {
        if (null == rateBase) {
            rateBase = new BigDecimal("360");
        }
        return AjaxJson.success(cmsProvideInterestService.calInterest(repayTypeId, sendDate, beginDate, endDate, money, loanRate, rateBase));
    }
}
