package com.bytter.finance.web.provide.controller;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.service.CmsRepaymentScheduleService;
import com.bytter.finance.domain.basedata.service.UpdateRepaymentScheduleService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.ProvideSyndicatedDtlService;
import com.bytter.finance.domain.provide.service.ProvideSyndicatedSendService;
import com.bytter.finance.domain.provide.service.ProvideSyndicatedService;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedDtlVo;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedSendVo;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedVo;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 借款种类：银团相关
 * @date 2022-07-28 10:45:09
 */
@Controller
@RequestMapping("/cms/provide/providesyndicated")
public class ProvideSyndicatedController extends BaseController {
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private CmsRepaymentScheduleService cmsRepaymentScheduleService;
    @Autowired
    private ProvideSyndicatedService provideSyndicatedService;
    @Autowired
    private ProvideSyndicatedSendService provideSyndicatedSendService;
    @Autowired
    private ProvideSyndicatedDtlService provideSyndicatedDtlService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private UpdateRepaymentScheduleService updateRepaymentScheduleService;
    /**
     * 加载银团借款明细
     *
     * @param directFinanceId
     * @return
     */
    @PostMapping("/listProvideSyndicated")
    @ResponseBody
    public List<ProvideSyndicatedVo> getByDirectFinanceId(String directFinanceId) {
        List<ProvideSyndicatedVo> list = provideSyndicatedService.getByDirectFinanceId(directFinanceId);
        return list;
    }
    /**
     * 加载银团借款发放明细
     *
     * @param provideLoanSendId
     * @return
     */
    @PostMapping("/listProvideSyndicatedSend")
    @ResponseBody
    public List<ProvideSyndicatedSendVo> getSendByProvideLoanSendId(String provideLoanSendId) {
        List<ProvideSyndicatedSendVo> list = provideSyndicatedSendService.getSendByProvideLoanSendId(provideLoanSendId);
        return list;
    }
    /**
     * 加载银团借款还款明细
     *
     * @param repayId
     * @return
     */
    @PostMapping("/listProvideSyndicatedDtl")
    @ResponseBody
    public List<ProvideSyndicatedDtlVo> getDtlByDirectFinanceId(String repayId) {
        List<ProvideSyndicatedDtlVo> list = provideSyndicatedDtlService.getByRepayId(repayId);
        return list;
    }
    /**
     * 跳转到还款计划添加银团页面（可以修改数据:其他）
     *
     * @param provideSyndicatedDtlVo
     * @param model
     * @return
     */
    @GetMapping("/toProvideSyndicatedDtl")
    public String toProvideSyndicatedDtl(ProvideSyndicatedDtlVo provideSyndicatedDtlVo, Model model) {
        model.addAttribute("provideSyndicatedDtlVo", provideSyndicatedDtlVo);
        return "cms/provide/listProvideSyndicatedDtl";
    }
}
