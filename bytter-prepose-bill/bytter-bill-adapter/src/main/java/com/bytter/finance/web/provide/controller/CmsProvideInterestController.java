package com.bytter.finance.web.provide.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.vo.RepayPlanVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleDtlVO;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import com.bytter.finance.domain.provide.vo.CmsProvideInterestVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author lvwl
 * @version 1.0
 * @Title: Controller
 * @Description: 预计利息明细
 */
@Controller
@RequestMapping("/cms/provide/cmsprovideinterest")
public class CmsProvideInterestController extends BaseController {
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    /**
     * 预计利息明细
     *
     * @param sendBillCode
     *            发放编号
     * @param interestID
     *            发放编号
     * @param repairSign
     *            补录标识 0修改、补录
     * @param showSign
     *            显示标识 0查看.1带选择
     * @return
     */
    @GetMapping("/list")
    public String list(Model model, String sendBillCode, String interestID, String repairSign, String showSign) {
        model.addAttribute("sendBillCode", sendBillCode);
        model.addAttribute("interestID", interestID);
        model.addAttribute("repairSign", repairSign);
        model.addAttribute("showSign", showSign);

        //将发放单号前两个字符传到前端用来区分业务
        model.addAttribute("type",sendBillCode.substring(0,2));
        List<CmsProvideInterestVO> list = cmsProvideInterestService.queryInterest(sendBillCode);
        model.addAttribute("list", cmsProvideInterestService.buildInterestList(list));
        buildAllInterestList(list, interestID, model);
        model.addAttribute("today",DateUtils.parse(DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + " 00:00:00",DateUtils.PATTERN_CLASSICAL));
        return "cms/provide/repay/cmsInterestWay";
    }
    /**
     * 预计利息明细试算
     *
     * @param repayPlanVo 传参对象
     * @return 页面路径
     */
    @GetMapping("/testList")
    public String testList(Model model, RepayPlanVO repayPlanVo, HttpServletRequest request) {
        model.addAttribute("repairSign", repayPlanVo.getRepairSign());
        model.addAttribute("showSign", repayPlanVo.getShowSign());
        model.addAttribute("repayPlanVO", repayPlanVo);
        List<CmsProvideInterestVO> list = cmsProvideInterestService.queryInterestTest(repayPlanVo, request, model);
        BigDecimal sumTotal = BigDecimal.ZERO;
        for (CmsProvideInterestVO cmsProvideInterestVO : list) {
            sumTotal = sumTotal.add(cmsProvideInterestVO.getRepayMoney());
        }
        model.addAttribute("sumTotal",sumTotal);
        model.addAttribute("listSize",list.size());
        model.addAttribute("list", cmsProvideInterestService.buildInterestList(list));
        buildAllInterestList(list, repayPlanVo.getInterestId(), model);
        model.addAttribute("today", DateUtils.parse(DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + " 00:00:00", DateUtils.PATTERN_CLASSICAL));
        return "cms/provide/repay/cmsInterestWayTest";
    }

    /**
     * 预计利息明细试算
     *
     * @param repayPlanVo 传参对象
     * @return 页面路径
     */
    @LogMessage(content = "预计利息明细试算保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveTestList")
    @ResponseBody
    public AjaxJson saveTestList(RepayPlanVO repayPlanVo, BigDecimal sumTotal, HttpServletRequest request) {
        List<RepaymentSchedule> list = (List<RepaymentSchedule>) request.getSession().getAttribute("repaymentScheduleDtlList");
        cmsProvideInterestService.saveTestList(repayPlanVo, list);
        request.getSession().removeAttribute("repaymentScheduleDtlList");
        return success();
    }
    private void buildAllInterestList(List<CmsProvideInterestVO> cmsProvideInterestVOList, String interestID, Model model) {
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
        model.addAttribute("allInterests", allInterests);
        model.addAttribute("selectableInterests", selectableInterests);
    }
    /**
     * 预计利息明细 - 数据
     */
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput dataTable(String sendBillCode) {
        List<CmsProvideInterestVO> list = cmsProvideInterestService.queryInterestList(sendBillCode, "", "", "", "", "", "");
        DataTablesOutput out = new DataTablesOutput();
        out.setData(list);
        return out;
    }
    @PostMapping("/calInterest")
    @ResponseBody
    public List<RepaymentScheduleDtlVO> calInterest(String repayTypeId, Date sendDate, Date beginDate, Date endDate,
            BigDecimal money, BigDecimal loanRate, BigDecimal rateBase) {
        if (null == rateBase) {
            rateBase = new BigDecimal("360");
        }
        return cmsProvideInterestService.calInterest(repayTypeId, sendDate, beginDate, endDate, money, loanRate, rateBase);
    }
}
