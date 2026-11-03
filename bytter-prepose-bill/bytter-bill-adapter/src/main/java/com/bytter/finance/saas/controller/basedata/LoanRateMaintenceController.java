package com.bytter.finance.saas.controller.basedata;

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
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 贷款利率维护
 * @date 2017-07-26 18:38:18
 */
@Api(value = "/v1-cms/cms/loanrate/maintence", tags = "贷款利率维护")
@RestController("v1LoanRateMaintenceController")
@RequestMapping("/v1-cms/cms/loanrate/maintence")
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
    public AjaxJson<Map<String,Object>> listBase() {
        Map<String,Object> model=new HashMap<>();
        model.put("baseSign", BaseDataConstant.IS_BANKBASERATE);
        return AjaxJson.success(model);
    }

    /**
     * 贷款利率维护列表 页面跳转 -非本位币
     */
    @LogMessage(content = "查询外币贷款利率维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listNotBase")
    public  AjaxJson<Map<String,Object>>  listNotBase() {
        Map<String,Object> model=new HashMap<>();
        model.put("baseSign", BaseDataConstant.NOT_BANKBASERATE);
        return AjaxJson.success(model);
    }

    /**
     * 贷款利率复核 - 本位币页面
     */
    @LogMessage(content = "查询贷款利率本位币复核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCheckBase")
    public  AjaxJson<Map<String,Object>>  listCheckBase() {
        Map<String,Object> model=new HashMap<>();
        model.put("baseSign", BaseDataConstant.IS_BANKBASERATE);
        model.put("status", BaseDataConstant.STATUS_SUBMIT);
        model.put("loginName", getUserVo().getLoginName());
        return AjaxJson.success(model);
    }

    /**
     * 贷款利率复核 - 外币页面
     */
    @LogMessage(content = "查询贷款利率外币复核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCheckNotBase")
    public  AjaxJson<Map<String,Object>>  listCheckNotBase() {
        Map<String,Object> model=new HashMap<>();
        model.put("baseSign", BaseDataConstant.NOT_BANKBASERATE);
        model.put("status", BaseDataConstant.STATUS_SUBMIT);
        model.put("loginName", getUserVo().getLoginName());
        return AjaxJson.success(model);
    }

    /**
     * 贷款利率维护新增页面跳转 - 本位币
     */
    @GetMapping("/addBase")
    public AjaxJson<Map<String,Object>> addBase() {
        Map<String,Object> model=new HashMap<>();
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.put("cur", cur);
        model.put("baseSign", BaseDataConstant.IS_BANKBASERATE);
        return AjaxJson.success(model);
    }

    /**
     * 贷款利率维护新增页面跳转 - 外币
     */
    @GetMapping("/addNotBase")
    public AjaxJson<Map<String,Object>>  addNotBase() {
        Map<String,Object> model=new HashMap<>();
        model.put("baseSign", BaseDataConstant.NOT_BANKBASERATE);
        return AjaxJson.success(model);
    }

    /**
     * 添加贷款利率维护 - 本位币
     */
    // @LogMessage(content = "添加贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/saveBaseRate")
    public AjaxJson saveBaseRate(@RequestBody LoanRateMaintenceVO vo) {
        loanRateMaintenceService.saveLoanRateMainTenceVO(vo, getCurrentUser());
        return success();
    }

    /**
     * 贷款利率维护编辑页面跳转 - 本位币
     */
    @GetMapping("/editBase/{id}")
    public AjaxJson<Map<String,Object>>  editBase(@PathVariable("id") String id ) {
        Map<String,Object> model=new HashMap<>();
        LoanRateMaintenceVO vo = loanRateMaintenceService.getLoanRateById(id);
        model.put("vo", vo);
        model.put("baseSign", BaseDataConstant.IS_BANKBASERATE);
        return AjaxJson.success(model);
    }

    /**
     * 贷款利率维护编辑页面跳转 - 外币
     */
    @GetMapping("/editNotBase/{id}")
    public AjaxJson<Map<String,Object>>  editNotBase(@PathVariable("id") String id) {
        Map<String,Object> model=new HashMap<>();
        LoanRateMaintenceVO vo = loanRateMaintenceService.getLoanRateById(id);
        model.put("vo", vo);
        model.put("baseSign", BaseDataConstant.NOT_BANKBASERATE);
        return AjaxJson.success(model);
    }

    /**
     * 修改贷款利率维护
     */
    // @LogMessage(content = "修改贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody  TbRateMaintenceDTO tbLoanRateMaintence) {
        loanRateMaintenceService.modifyLoanRate(tbLoanRateMaintence, getCurrentUser());
        return success();
    }

    /**
     * 删除贷款利率维护
     */
    // @LogMessage(content = "删除贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
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
    public AjaxJson check(@RequestParam("ids[]") List<String> ids) {
        loanRateMaintenceService.updateStatusById(ids, BaseDataConstant.STATUS_PASS, null, getCurrentUser());
        return success();
    }

    /**
     * 打回贷款利率维护
     */
    // @LogMessage(content = "打回贷款利率维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/back")
    public AjaxJson back(@RequestParam("ids[]") List<String> ids, @RequestParam("backScribe") String backScribe) {
        loanRateMaintenceService.updateStatusById(ids, BaseDataConstant.STATUS_BACK, backScribe, getCurrentUser());
        return success();
    }
    /******** ajax获取数据 *******/
    /**
     * 功能说明：根据参数查询 基准利率
     */
    @RequestMapping("/queryBaseRate")
    public AjaxJson<List<TbBaseRateDTO>> queryBaseRate() {
        return AjaxJson.success(iBaseRateService.getBaseRateByType(BaseDataConstant.IS_BANKBASERATE));
    }

    /**
     * 功能说明：根据参数查询 可选择的基准利率
     */
    @RequestMapping("/queryOptionalBaseRate")
    public AjaxJson<List<TbBaseRateDTO>> queryOptionalBaseRate(String rateTypeId, String curId) {
        BtCurrencyDTO btCurrencyDTO = this.btCurrencyServiceImpl.getObjectById(curId);
        if (BtCurrencyConstant.RMB.equals(btCurrencyDTO.getCurName())) {
            return AjaxJson.success(this.iBaseRateService.getBaseRateListById(rateTypeId));
        } else {
            return AjaxJson.success(iBaseRateService.getBaseRateByType(BaseDataConstant.NOT_BANKBASERATE));
        }
    }

    /**
     * 功能说明：根据参数查询 基准利率
     */
    @RequestMapping("/queryNotBaseRate")
    public AjaxJson<List<TbBaseRateDTO>> queryNotBaseRate() {
        return AjaxJson.success(iBaseRateService.getBaseRateByType(BaseDataConstant.NOT_BANKBASERATE));
    }

    /**
     * 功能说明：查询非本位币 集合
     */
    @RequestMapping("/queryNotBaseCur")
    public AjaxJson<List<BtCurrencyDTO>> queryNotBaseCur() {
        return AjaxJson.success(btCurrencyServiceImpl.listByNotNatualCurrency());
    }

    /**
     * 功能说明：根据利率类型、借款周期、币别
     */
    @RequestMapping("/querRateMaintence")
    public AjaxJson<List<LoanRateMaintenceVO>> querRateMaintence(Date time, String curId, String baseId) {
        return AjaxJson.success(loanRateMaintenceService.getRateMaintenceByTimeCurBase(time, curId, baseId));
    }

    /**
     * 功能介绍：根据生效日期币别获取利率类型
     *
     * @param: time 生效日期
     * @param: cur 币别
     */
    @RequestMapping("/queryBaseType")
    public AjaxJson<List<LoanRateMaintenceVO> > queryBaseType(Date time, String curId) {
        return AjaxJson.success(loanRateMaintenceService.getRateMaintenceByTimeCur(time, curId));
    }
}
