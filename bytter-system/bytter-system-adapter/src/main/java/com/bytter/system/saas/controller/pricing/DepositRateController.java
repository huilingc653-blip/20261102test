package com.bytter.system.saas.controller.pricing;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.pricing.service.DepositRateService;
import com.bytter.pricing.vo.DepositRateVO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.pricing.constant.PricingConstant;
import com.bytter.system.pricing.service.IPricingDepositRateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author luxy
 * @Title: DepositRateController
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/5/23 8:49
 */
@Api(value = "/v1-system/pricing/depositRate/interestRatePreserve", tags = "存款利率维护")
@RestController("v1DepositRateController")
@RequestMapping("/v1-system/pricing/depositRate/interestRatePreserve")
public class DepositRateController extends BaseController {
    @Autowired
    private DepositRateService depositRateService;
    @Autowired
    private IConfigService configServiceI;

    @Autowired
    private IPricingDepositRateService iPricingDepositRateService;


    /**
     * 查询存款利率维护列表
     *
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    @LogMessage(content = "查询存款利率维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(depositRateService.datatable(input, getCurrentUser()));
    }

    @PostMapping("/save")
    @ResponseBody
    @LogMessage(content = "保存存款利率维护数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson save(@RequestBody DepositRateVO depositRateVO) {
        if (depositRateVO.getSaveType().equals(PricingConstant.SAVE_TEMP)) {//暂存
            depositRateVO.setStatus(PricingConstant.STATUS_TEMP);
        } else if (depositRateVO.getSaveType().equals(PricingConstant.SAVE_SUBMIT)) {
            String isAutoCheck = configServiceI.getConfig("pricing", "autoCheck").getValue();
            if (isAutoCheck.equals(PricingConstant.PRICING_COMMON_Y)) {//自动复核
                depositRateVO.setStatus(PricingConstant.STATUS_PASS);
            } else if (isAutoCheck.equals(PricingConstant.PRICING_COMMON_N)) {
                depositRateVO.setStatus(PricingConstant.STATUS_RECHECK);
            }
        }
        depositRateService.save(depositRateVO, getCurrentUser());
        return success();
    }

    @GetMapping("/update/{id}")
    @LogMessage(content = "跳转到编辑存款利率页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DepositRateVO> update(@PathVariable("id") String id) {
        return AjaxJson.success(depositRateService.getDepositRateById(id));
    }

    @GetMapping("/view/{id}")
    @LogMessage(content = "跳转到查看存款利率页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DepositRateVO> view(@PathVariable("id") String id) {
        DepositRateVO depositRateVO = depositRateService.getDepositRateById(id);
        return AjaxJson.success(depositRateVO);
    }

    /**
     * 根据id删除存款利率类型
     */
    @LogMessage(content = "删除存款利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        depositRateService.delete(id);
        return success();
    }
}
