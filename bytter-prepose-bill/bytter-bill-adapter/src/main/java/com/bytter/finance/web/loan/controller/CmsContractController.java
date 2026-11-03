package com.bytter.finance.web.loan.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.loan.dao.CmsLoanInfoDao;
import com.bytter.finance.domain.loan.entity.CmsContract;
import com.bytter.finance.domain.loan.service.CmsContractService;
import com.bytter.finance.domain.loan.service.CmsLoanInfoService;
import com.bytter.finance.domain.loan.service.CmsLoanSendService;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.loan.vo.CmsLoanSendVO;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Date;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: CMS_CONTRACT
 * @date 2017-08-02 10:07:46
 */
@Controller
@RequestMapping("/cms/loan/cmscontract")
public class CmsContractController extends BaseController {
    @Autowired
    private CmsContractService cmsContractService;
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @Autowired
    private CmsLoanInfoDao cmsLoanInfoDao;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IMultiLangService multiLangServiceI;

    /**
     * CMS_CONTRACT新增页面跳转
     *
     * @return
     * @throws ParseException
     */
    @GetMapping("/add")
    public String add(CmsContract cmsContract, Model model) {
        CmsContract cmsContractInfo = cmsContractService.findCmsContractByliCode(CmsConstant.LOAN_CONTRACTNO_INIT);
        if (cmsContractInfo != null && EmptyUtils.isNotEmpty(cmsContract.getLiCode())) {
            CmsLoanSendVO loanSend = cmsLoanSendService
                    .getCmsLoanSendById(cmsLoanSendService.getLoanSendByBillCode(cmsContract.getLiCode()).getId());
            CmsLoanInfoVO loanInfo = cmsLoanInfoService
                    .getCmsLoanInfoById(cmsLoanInfoDao.findByBillCode(loanSend.getLoanCode()).getId());
            // 合同文本中替代字符
            // 合同编号 #htbh#
            // 贷款人 #jkr#
            // 借款人 #dkr#
            // 借款种类 #dkzl#
            // 币 别 #bb#
            // 金额大写 #je#
            // 贷款用途 #yt#
            // 贷款期限 #qx#
            // 开始日期 #ksrq#
            // 到期日期 #jsrq#
            // 月利率为 #lv#
            String replaceStr[] = {"#htbh#", "#jkr#", "#dkr#", "#dkzl#", "#bb#", "#je#", "#yt#", "#qx#", "#ksrq#",
                    "#jsrq#", "#lv#"};
            String htbh = loanSend.getContractCode();
            String jkr = loanInfo.getCorpName();
            String dkr = loanInfo.getNetName();
            String dkzl = loanInfo.getLoanClassName();
            String bb = loanInfo.getCurName();
            String je = NumberUtil.formatNumeric(loanSend.getLoanMoney(), "#,##0.00");
            String yt = loanInfo.getPurpose() == null ? "" : loanInfo.getPurpose();
            String ksrq = DateUtils.date2Str(loanSend.getLoanDate(), DateUtils.date_sdf_wz());
            String jsrq = DateUtils.date2Str(loanSend.getRepayDate(), DateUtils.date_sdf_wz());
            // String qx = loanSend.getLoanTerm()+""+("2".equals(loanInfo.getTernUnit())?"天":"个月");
            String qx = String.valueOf(loanInfo.getLoanTerm()) + ("2".equals(loanInfo.getTernUnit()) ? "日" : "月");
            String lv = cmsContractService.showRate(loanSend.getLoanRate().doubleValue());
            String replaceStrNew[] = {htbh, jkr, dkr, dkzl, bb, je, yt, qx, ksrq, jsrq, lv};
            String contract = cmsContractInfo.getContractInfo();
            for (int i = 0; i < replaceStr.length; i++) {
                if (replaceStrNew[i] == null) {
                    replaceStrNew[i] = "";
                }
                contract = contract.replaceAll(replaceStr[i], replaceStrNew[i]);
            }
            cmsContract.setContractInfo(contract);
            notificationService.complete(NotificationConstant.CMS.LOAN_CONTRACT, loanSend.getId(),
                    getCurrentUser().getId());
        }
        model.addAttribute("cmsContractPage", cmsContract);
        return "cms/loan/contract/addCmsContract";
    }

    /**
     * CMS_CONTRACT编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(CmsContract cmsContract, Model model) {
        if (StringUtils.isNotEmpty(cmsContract.getLiCode())) {
            cmsContract = cmsContractService.findCmsContractByliCode(cmsContract.getLiCode());
            model.addAttribute("cmsContractPage", cmsContract);
        }
        return "cms/loan/contract/updateCmsContract";
    }

    /**
     * CMS_CONTRACT打印页面跳转
     *
     * @return
     */
    @GetMapping("/print")
    public String print(CmsContract cmsContract, Model model) {
        if (StringUtils.isNotEmpty(cmsContract.getLiCode())) {
            cmsContract = cmsContractService.findCmsContractByliCode(cmsContract.getLiCode());
            model.addAttribute("cmsContractPage", cmsContract);
        }
        return "cms/loan/contract/printCmsContract";
    }

    @GetMapping("/contractSet")
    public String contractSet(Model model) {
        String liCode = CmsConstant.LOAN_CONTRACTNO_INIT;// 初始化模板编号
        CmsContract cmsContract = cmsContractService.findCmsContractByliCode(liCode);
        if (EmptyUtils.isEmpty(cmsContract)) {// 初始化模板信息
            CmsContract cmsContractInit = new CmsContract();
            cmsContractInit.setLiCode(liCode);
            cmsContractInit.setContractCode("0");
            cmsContractInit.setCorpId("-1");
            model.addAttribute("cmsContractPage", cmsContractInit);
        } else {
            model.addAttribute("cmsContractPage", cmsContract);
        }
        model.addAttribute("pageType", "set");
        return "cms/loan/contract/updateCmsContract";
    }

    /**
     * 添加CMS_CONTRACT
     *
     * @param cmsContract
     * @return
     */
    @LogMessage(content = "添加CMS_CONTRACT", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsContract cmsContract) {
        cmsContract.setCreateBy(getUserId());
        cmsContract.setCreateName(getUserLoginName());
        cmsContract.setCreateDate(new Date());
        cmsContractService.saveContract(cmsContract, getCurrentUser());
        return success();
    }

    @GetMapping("/downDoc")
    @ResponseBody
    public void downDoc(CmsContract cmsContract) {
    }

    /**
     * 修改CMS_CONTRACT
     *
     * @param cmsContract
     * @return
     */
    @LogMessage(content = "修改CMS_CONTRACT", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsContract cmsContract) {
        ShiroUser user = getUserVo();
        cmsContract.setUpdateBy(user.getLoginName());
        cmsContract.setUpdateName(user.getUsername());
        cmsContract.setUpdateDate(new Date());
        if (EmptyUtils.isNotEmpty(cmsContract.getId())) {// 判断ID是否存在
            CmsContract t = cmsContractService.get(cmsContract.getId());
            BeanUtils.copyNotNullProperties(cmsContract, t);
            cmsContractService.saveOrUpdate(t);
        } else {
            cmsContractService.save(cmsContract);
        }
        return success();
    }
}
