package com.bytter.system.web.controller;
import com.bytter.basic.entity.FcExcrateAdjust;
import com.bytter.basic.service.FcExcrateAdjustService;
import com.bytter.basic.vo.FcExcrateAdjustVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: FC_EXCRATE_ADJUST
 * @date 2017-09-06 10:01:05
 */
@Controller
@RequestMapping("/basic/business/fcexcrateadjust")
public class FcExcrateAdjustController extends BaseController {
    @Autowired
    private FcExcrateAdjustService fcExcrateAdjustService;
    @Autowired
     private IBtCurrencyService btCurrencyServiceI;
    @Autowired
    private ICorpService corpServiceI;
    /**
     * FC_EXCRATE_ADJUST列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询FC_EXCRATE_ADJUST列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listFcExcrateAdjust";
    }
    /**
     * FC_EXCRATE_ADJUST table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcExcrateAdjustService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * FC_EXCRATE_ADJUST新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(FcExcrateAdjust fcExcrateAdjust, Model model) {
        if (StringUtils.hasText(fcExcrateAdjust.getId())) {
            fcExcrateAdjust = fcExcrateAdjustService.get(fcExcrateAdjust.getId());
            model.addAttribute("fcExcrateAdjustPage", fcExcrateAdjust);
        }
        BtCurrencyDTO currencyDTO = this.btCurrencyServiceI.getNatualCurrency();
        if (!EmptyUtils.isEmpty(currencyDTO)) {
            model.addAttribute("referCurId", currencyDTO.getId());
            model.addAttribute("referCur", currencyDTO);
        }
        return "basic/business/addFcExcrateAdjust";
    }
    /**
     * FC_EXCRATE_ADJUST编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(FcExcrateAdjust fcExcrateAdjust, Model model) {
        if (StringUtils.hasText(fcExcrateAdjust.getId())) {
            fcExcrateAdjust = fcExcrateAdjustService.get(fcExcrateAdjust.getId());
            model.addAttribute("fcExcrateAdjustPage", fcExcrateAdjust);
        }
        BtCurrencyDTO currencyDTO = this.btCurrencyServiceI.getNatualCurrency();
        if (!EmptyUtils.isEmpty(currencyDTO)) {
            model.addAttribute("referCur", currencyDTO);
        }
        return "basic/business/updateFcExcrateAdjust";
    }
    /**
     * FC_EXCRATE_ADJUST详情页
     *
     * @return
     */
    @LogMessage(content = "查询FC_EXCRATE_ADJUST详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FcExcrateAdjustVO vo = fcExcrateAdjustService.getByUnion(id);
        model.addAttribute("fcExcrateAdjustPage", vo);
        return "basic/business/viewFcExcrateAdjust";
    }
    /**
     * 添加FC_EXCRATE_ADJUST
     *
     * @param fcExcrateAdjust
     * @return
     */
    @LogMessage(content = "添加FC_EXCRATE_ADJUST", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcExcrateAdjust fcExcrateAdjust) {
        fcExcrateAdjustService.saveFcExcrateAdjust(fcExcrateAdjust, getUserLoginName());
        return success();
    }
    /**
     * 修改FC_EXCRATE_ADJUST
     *
     * @param fcExcrateAdjust
     * @return
     */
    @LogMessage(content = "修改FC_EXCRATE_ADJUST", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FcExcrateAdjust fcExcrateAdjust) {
        FcExcrateAdjust t = fcExcrateAdjustService.get(fcExcrateAdjust.getId());
        BeanUtils.copyNotNullProperties(fcExcrateAdjust, t);
        fcExcrateAdjustService.updateFcExcrateAdjust(t, getUserLoginName());
        return success();
    }
    /**
     * 删除FC_EXCRATE_ADJUST
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除FC_EXCRATE_ADJUST", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        FcExcrateAdjust fcExcrateAdjust = fcExcrateAdjustService.get(id);
        fcExcrateAdjustService.deleteEntity(fcExcrateAdjust);
        return success();
    }
    /**
     * 批量删除FC_EXCRATE_ADJUST
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除FC_EXCRATE_ADJUST", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FcExcrateAdjust fcExcrateAdjust = fcExcrateAdjustService.get(id);
            fcExcrateAdjustService.deleteEntity(fcExcrateAdjust);
        }
        return success();
    }
    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        fcExcrateAdjustService.batchSubmit(ids, getUserLoginName());
        return success();
    }
    /**
     * 复核
     *
     * @return
     */
    @PostMapping("/check")
    @ResponseBody
    public AjaxJson check(@RequestParam("id") String id) {
        fcExcrateAdjustService.batchCheck(id, getUserLoginName());
        return success();
    }
    /**
     * 批量复核
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchCheck")
    @ResponseBody
    public AjaxJson batchCheck(@RequestParam("ids") String ids) {
        fcExcrateAdjustService.batchCheck(ids, getUserLoginName());
        return success();
    }
    /**
     * 打回
     *
     * @return
     */
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(@RequestParam("id") String id) {
        fcExcrateAdjustService.batchBack(id, getUserLoginName());
        return success();
    }
    /**
     * 批量复核
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchBack")
    @ResponseBody
    public AjaxJson batchBack(@RequestParam("ids") String ids) {
        fcExcrateAdjustService.batchBack(ids, getUserLoginName());
        return success();
    }
    /**
     * 货币维护复核页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询FC_EXCRATE_ADJUST列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listCheck")
    public String listApprove(Model model) {
        return "basic/business/listFcExcrateAdjustCheck";
    }
    /**
     * FC_EXCRATE_ADJUST table列表
     *
     * @return
     */
    @PostMapping("/datatableCheck")
    @ResponseBody
    public DataTablesOutput datatableCheck(@RequestBody DataTablesInput dataTablesInput) {
        return fcExcrateAdjustService.getDataTablesCheck(dataTablesInput, getUserLoginName());
    }
    /**
     * 获取币别汇率
     *
     * @return
     */
    @PostMapping("/getExchangeRate")
    @ResponseBody
    public BigDecimal getExchangeRate(String curId, String referCurId, Date preTime, String typeId) {
        if (StringUtils.hasText(curId) && StringUtils.hasText(referCurId) && StringUtils.hasText(typeId) && !EmptyUtils.isEmpty(preTime)) {
            try {
                if (curId.equalsIgnoreCase(referCurId)) {
                    return BigDecimal.ONE;
                }
                return fcExcrateAdjustService.getExchangeRateByDirectCurrency(curId, referCurId, preTime, typeId);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    /**
     * 获取汇率信息(资金计划调用)
     *
     * @param curId
     * @return
     */
    @RequestMapping(params = "getExcrateAdjustForBudget")
    @ResponseBody
    public AjaxJson getExcrateAdjustForBudget(String corpId, String curId) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        //获取顶级单位/网点
        CorpDTO topCorp = corpServiceI.getTopCorpByCorpId(corpId);
        BigDecimal exChangeRate = BigDecimal.ONE;
        if (StringUtils.hasText(curId) && topCorp != null && !curId.equals(topCorp.getCurId())) {
            exChangeRate = fcExcrateAdjustService.getExchangeRateBy(curId, topCorp.getCurId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
        }
        j.setObj(exChangeRate);
        return j;
    }
}
