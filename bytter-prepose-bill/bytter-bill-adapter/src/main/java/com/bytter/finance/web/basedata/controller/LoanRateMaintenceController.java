package com.bytter.finance.web.basedata.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IRateMaintenceService;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款利率维护
 * @date 2017-07-26 18:38:18
 */
@Controller
@RequestMapping("/cms/loanrate/maintence")
public class LoanRateMaintenceController extends BaseController {
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IRateMaintenceService iRateMaintenceService;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;

    /**
     * 贷款利率维护列表 页面跳转
     */
    @LogMessage(content = "查询本位币贷款利率维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listBase")
    public String listBase(Model model) {
        model.addAttribute("baseSign", BaseDataConstant.IS_BANKBASERATE);
        return "cms/basedata/loanRateMaintence/listLoanRateMaintence";
    }

    /**
     * 贷款利率维护列表 页面跳转 -非本位币
     */
    @LogMessage(content = "查询外币贷款利率维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listNotBase")
    public String listNotBase(Model model) {
        model.addAttribute("baseSign", BaseDataConstant.NOT_BANKBASERATE);
        return "cms/basedata/loanRateMaintence/listLoanRateMaintenceNotBase";
    }

    /**
     * 贷款利率复核 - 本位币页面
     */
    @LogMessage(content = "查询贷款利率本位币复核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCheckBase")
    public String listCheckBase(Model model) {
        model.addAttribute("baseSign", BaseDataConstant.IS_BANKBASERATE);
        model.addAttribute("status", BaseDataConstant.STATUS_SUBMIT);
        model.addAttribute("loginName", getUserVo().getLoginName());
        return "cms/basedata/loanRateMaintence/listLoanRateMaintenceBaseCheck";
    }

    /**
     * 贷款利率复核 - 外币页面
     */
    @LogMessage(content = "查询贷款利率外币复核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCheckNotBase")
    public String listCheckNotBase(Model model) {
        model.addAttribute("baseSign", BaseDataConstant.NOT_BANKBASERATE);
        model.addAttribute("status", BaseDataConstant.STATUS_SUBMIT);
        model.addAttribute("loginName", getUserVo().getLoginName());
        return "cms/basedata/loanRateMaintence/listLoanRateMaintenceNotBaseCheck";
    }

    /**
     * 贷款利率维护新增页面跳转 - 本位币
     */
    @GetMapping("/addBase")
    public String addBase(Model model) {
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.addAttribute("cur", cur);
        model.addAttribute("baseSign", BaseDataConstant.IS_BANKBASERATE);
        return "cms/basedata/loanRateMaintence/addLoanRateMaintence";
    }

    /**
     * 贷款利率维护新增页面跳转 - 外币
     */
    @GetMapping("/addNotBase")
    public String addNotBase(Model model) {
        model.addAttribute("baseSign", BaseDataConstant.NOT_BANKBASERATE);
        return "cms/basedata/loanRateMaintence/addLoanRateMaintence";
    }

    /**
     * 添加贷款利率维护 - 本位币
     */
    // @LogMessage(content = "添加贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/saveBaseRate")
    @ResponseBody
    public AjaxJson saveBaseRate(LoanRateMaintenceVO vo) {
        loanRateMaintenceService.saveLoanRateMainTenceVO(vo, getCurrentUser());
        return success();
    }

    /**
     * 贷款利率维护编辑页面跳转 - 本位币
     */
    @GetMapping("/editBase/{id}")
    public String editBase(@PathVariable("id") String id, Model model) {
        LoanRateMaintenceVO vo = loanRateMaintenceService.getLoanRateById(id);
        model.addAttribute("vo", vo);
        model.addAttribute("baseSign", BaseDataConstant.IS_BANKBASERATE);
        return "cms/basedata/loanRateMaintence/updateLoanRateMaintence";
    }

    /**
     * 贷款利率维护编辑页面跳转 - 外币
     */
    @GetMapping("/editNotBase/{id}")
    public String editNotBase(@PathVariable("id") String id, Model model) {
        LoanRateMaintenceVO vo = loanRateMaintenceService.getLoanRateById(id);
        model.addAttribute("vo", vo);
        model.addAttribute("baseSign", BaseDataConstant.NOT_BANKBASERATE);
        return "cms/basedata/loanRateMaintence/updateLoanRateMaintence";
    }

    /**
     * 修改贷款利率维护
     */
    // @LogMessage(content = "修改贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(TbRateMaintenceDTO tbLoanRateMaintence) {
        loanRateMaintenceService.modifyLoanRate(tbLoanRateMaintence, getCurrentUser());
        return success();
    }

    /**
     * 删除贷款利率维护
     */
    // @LogMessage(content = "删除贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        TbRateMaintenceDTO tbRateMaintenceDTO = iRateMaintenceService.getById(id);
        if (loanRateMaintenceService.isRefered(tbRateMaintenceDTO)) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_REATREFERED);
        }
        iRateMaintenceService.deleteByEntity(tbRateMaintenceDTO);
        return success();
    }

    /**
     * 禁用贷款利率维护
     */
    // @LogMessage(content = "禁用贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/valid/{id}")
    @ResponseBody
    public AjaxJson valid(@PathVariable("id") String id) {
        TbRateMaintenceDTO tbRateMaintenceDTO = iRateMaintenceService.getById(id);
        if (loanRateMaintenceService.isRefered(tbRateMaintenceDTO)) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANRATE_NOTVALIDFALSE);
        }
        tbRateMaintenceDTO.setIsValid(BaseDataConstant.IS_VALID_FALSE);
        iRateMaintenceService.updateByEntity(tbRateMaintenceDTO);
        msService.insertProcessMsg(tbRateMaintenceDTO.getId(), BaseDataConstant.RATE_MSG_TYPE,
                CmsProcessMsgConstant.MSGA_FORBIDDEN, "禁用", getCurrentUser());
        return success();
    }

    /**
     * 复核贷款利率维护
     */
    // @LogMessage(content = "复核贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/check")
    @ResponseBody
    public AjaxJson check(@RequestParam("ids[]") List<String> ids) {
        loanRateMaintenceService.updateStatusById(ids, BaseDataConstant.STATUS_PASS, null, getCurrentUser());
        return success();
    }

    /**
     * 打回贷款利率维护
     */
    // @LogMessage(content = "打回贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(@RequestParam("ids[]") List<String> ids, @RequestParam("backScribe") String backScribe) {
        loanRateMaintenceService.updateStatusById(ids, BaseDataConstant.STATUS_BACK, backScribe, getCurrentUser());
        return success();
    }
    /******** ajax获取数据 *******/
    /**
     * 功能说明：根据参数查询 基准利率
     */
    @RequestMapping("/queryBaseRate")
    @ResponseBody
    public List<TbBaseRateDTO> queryBaseRate() {
        return iBaseRateService.getBaseRateByType(BaseDataConstant.IS_BANKBASERATE);
    }

    /**
     * 功能说明：根据参数查询 可选择的基准利率
     */
    @RequestMapping("/queryOptionalBaseRate")
    @ResponseBody
    public List<TbBaseRateDTO> queryOptionalBaseRate(String rateTypeId, String curId) {
        BtCurrencyDTO btCurrencyDTO = this.btCurrencyServiceImpl.getObjectById(curId);
        if (BtCurrencyConstant.RMB.equals(btCurrencyDTO.getCurName())) {
            return this.iBaseRateService.getBaseRateListById(rateTypeId);
        } else {
            return iBaseRateService.getBaseRateByType(BaseDataConstant.NOT_BANKBASERATE);
        }
    }

    /**
     * 功能说明：根据参数查询 基准利率
     */
    @RequestMapping("/queryNotBaseRate")
    @ResponseBody
    public List<TbBaseRateDTO> queryNotBaseRate() {
        return iBaseRateService.getBaseRateByType(BaseDataConstant.NOT_BANKBASERATE);
    }

    /**
     * 功能说明：查询非本位币 集合
     */
    @RequestMapping("/queryNotBaseCur")
    @ResponseBody
    public List<BtCurrencyDTO> queryNotBaseCur() {
        return btCurrencyServiceImpl.listByNotNatualCurrency();
    }

    /**
     * 功能说明：根据利率类型、借款周期、币别
     */
    @RequestMapping("/querRateMaintence")
    @ResponseBody
    public List<LoanRateMaintenceVO> querRateMaintence(Date time, String curId, String baseId) {
        return loanRateMaintenceService.getRateMaintenceByTimeCurBase(time, curId, baseId);
    }

    /**
     * 功能介绍：根据生效日期币别获取利率类型
     *
     * @param: time 生效日期
     * @param: cur 币别
     */
    @RequestMapping("/queryBaseType")
    @ResponseBody
    public List<LoanRateMaintenceVO> queryBaseType(Date time, String curId) {
        return loanRateMaintenceService.getRateMaintenceByTimeCur(time, curId);
    }
}
