package com.bytter.finance.saas.controller.loan;

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
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: CMS_CONTRACT
 * @date 2017-08-02 10:07:46
 */

@Api(tags = "cmscontract", value = "/v1-preposebill/cms/loan/cmscontract")
@RestController("v1CmsContractController")
@RequestMapping("/v1-preposebill/cms/loan/cmscontract")
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
    public AjaxJson<Map<String,Object>> add(CmsContract cmsContract) {
        Map<String,Object> model=new HashMap<>();
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
        model.put("cmsContractPage", cmsContract);
        return AjaxJson.success(model);
    }

    /**
     * CMS_CONTRACT编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<Map<String, Object>> update(String liCode) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(liCode)) {
            CmsContract   cmsContract = cmsContractService.findCmsContractByliCode(liCode);
            model.put("cmsContractPage", cmsContract);
        }
        return AjaxJson.success(model);
    }

    /**
     * CMS_CONTRACT打印页面跳转
     *
     * @return
     */
    @GetMapping("/print")
    public AjaxJson<Map<String, Object>> print(CmsContract cmsContract) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(cmsContract.getLiCode())) {
            cmsContract = cmsContractService.findCmsContractByliCode(cmsContract.getLiCode());
            model.put("cmsContractPage", cmsContract);
        }
        return AjaxJson.success(model);
    }

    @GetMapping("/contractSet")
    public AjaxJson<Map<String, Object>> contractSet() {
        Map<String,Object> model=new HashMap<>();
        String liCode = CmsConstant.LOAN_CONTRACTNO_INIT;// 初始化模板编号
        CmsContract cmsContract = cmsContractService.findCmsContractByliCode(liCode);
        if (EmptyUtils.isEmpty(cmsContract)) {// 初始化模板信息
            CmsContract cmsContractInit = new CmsContract();
            cmsContractInit.setLiCode(liCode);
            cmsContractInit.setContractCode("0");
            cmsContractInit.setCorpId("-1");
            model.put("cmsContractPage", cmsContractInit);
        } else {
            model.put("cmsContractPage", cmsContract);
        }
        model.put("pageType", "set");
        return AjaxJson.success(model);
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
    public AjaxJson save(@RequestBody CmsContract cmsContract) {
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
    public AjaxJson modify(@RequestBody  CmsContract cmsContract) {
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
