package com.bytter.system.saas.controller.web;

import com.bytter.basic.entity.FcExcrateAdjust;
import com.bytter.basic.service.FcExcrateAdjustService;
import com.bytter.basic.vo.FcExcrateAdjustVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: FC_EXCRATE_ADJUST
 * @date 2017-09-06 10:01:05
 */
@Api(value = "FcExcrateAdjustController",tags = "FcExcrateAdjustController")
@RestController("v1FcExcrateAdjustController")
@RequestMapping("/v1-system/basic/business/fcexcrateadjust")
public class FcExcrateAdjustController extends BaseController {
    @Autowired
    private FcExcrateAdjustService fcExcrateAdjustService;
    @Autowired
     private IBtCurrencyService btCurrencyServiceI;
    @Autowired
    private ICorpService corpServiceI;
  
    /**
     * FC_EXCRATE_ADJUST table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fcExcrateAdjustService.getDataTablesUnion(input));
    }
    /**
     * FC_EXCRATE_ADJUST新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public AjaxJson<Map<String,Object>> add(@RequestBody  FcExcrateAdjust fcExcrateAdjust) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.hasText(fcExcrateAdjust.getId())) {
            fcExcrateAdjust = fcExcrateAdjustService.get(fcExcrateAdjust.getId());
            model.put("fcExcrateAdjustPage", fcExcrateAdjust);
        }
        BtCurrencyDTO currencyDTO = this.btCurrencyServiceI.getNatualCurrency();
        if (!EmptyUtils.isEmpty(currencyDTO)) {
            model.put("referCurId", currencyDTO.getId());
            model.put("referCur", currencyDTO);
        }
        return AjaxJson.success(model);
    }
    /**
     * FC_EXCRATE_ADJUST编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<Map<String,Object>> update(@RequestBody  FcExcrateAdjust fcExcrateAdjust) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.hasText(fcExcrateAdjust.getId())) {
            fcExcrateAdjust = fcExcrateAdjustService.get(fcExcrateAdjust.getId());
            model.put("fcExcrateAdjustPage", fcExcrateAdjust);
        }
        BtCurrencyDTO currencyDTO = this.btCurrencyServiceI.getNatualCurrency();
        if (!EmptyUtils.isEmpty(currencyDTO)) {
            model.put("referCur", currencyDTO);
        }
        return AjaxJson.success(model);
    }
    /**
     * FC_EXCRATE_ADJUST详情页
     *
     * @return
     */
    @LogMessage(content = "查询FC_EXCRATE_ADJUST详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<FcExcrateAdjustVO> view(@PathVariable("id") String id) {
        return AjaxJson.success(fcExcrateAdjustService.getByUnion(id));
    }
    /**
     * 添加FC_EXCRATE_ADJUST
     *
     * @param fcExcrateAdjust
     * @return
     */
    @LogMessage(content = "添加FC_EXCRATE_ADJUST", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody FcExcrateAdjust fcExcrateAdjust) {
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
    public AjaxJson modify(@RequestBody FcExcrateAdjust fcExcrateAdjust) {
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
    public AjaxJson batchBack(@RequestParam("ids") String ids) {
        fcExcrateAdjustService.batchBack(ids, getUserLoginName());
        return success();
    }


    /**
     * FC_EXCRATE_ADJUST table列表
     *
     * @return
     */
    @PostMapping("/datatableCheck")
    public AjaxJson<DataTablesOutput> datatableCheck(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fcExcrateAdjustService.getDataTablesCheck(input, getUserLoginName()));
    }
    /**
     * 获取币别汇率
     *
     * @return
     */
    @PostMapping("/getExchangeRate")
    public AjaxJson<BigDecimal> getExchangeRate(String curId, String referCurId, Date preTime, String typeId) {
        if (StringUtils.hasText(curId) && StringUtils.hasText(referCurId) && StringUtils.hasText(typeId) && !EmptyUtils.isEmpty(preTime)) {
            try {
                if (curId.equalsIgnoreCase(referCurId)) {
                    return AjaxJson.success( BigDecimal.ONE);
                }
                return AjaxJson.success(fcExcrateAdjustService.getExchangeRateByDirectCurrency(curId, referCurId, preTime, typeId));
            } catch (Exception e) {
                return AjaxJson.success();
            }
        }
        return AjaxJson.success();
    }
    /**
     * 获取汇率信息(资金计划调用)
     *
     * @param curId
     * @return
     */
    @GetMapping("getExcrateAdjustForBudget")
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
