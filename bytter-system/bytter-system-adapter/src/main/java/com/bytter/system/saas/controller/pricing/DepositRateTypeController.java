package com.bytter.system.saas.controller.pricing;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.pricing.entity.DepositRateType;
import com.bytter.pricing.service.DepositRateTypeService;
import com.bytter.pricing.vo.DepositRateTypeVO;
import com.bytter.system.pricing.constant.PricingConstant;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author luxy
 * @Title: DepositIRateTypeController
 * @ProjectName T2_IDEA
 * @Description: 存款利率类型控制层
 * @date 2019/5/22 11:24
 */
@Api(value = "/v1-system//pricing/depositRateType/interestRateType", tags = "存款利率类型控制层")
@RestController("v1DepositRateTypeController")
@RequestMapping("/v1-system//pricing/depositRateType/interestRateType")
public class DepositRateTypeController extends BaseController {
    @Autowired
    private DepositRateTypeService depositIRateTypeService;

    /**
     * 存款利率类型 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(depositIRateTypeService.getDataTables(input));
    }

    /**
     * 检查编号是否重复
     * @param code
     * @return
     */
    @PostMapping("/checkCode")
    @ResponseBody
    public AjaxJson<String> checkContractNo(String code) {
        DepositRateType depositRateType=depositIRateTypeService.findPeriodByCode(code);
        if (depositRateType==null) {
            return AjaxJson.success(PricingConstant.PRICING_COMMON_Y);//不存在
        }
        return AjaxJson.success(PricingConstant.PRICING_COMMON_N);
    }
    /**
     * 保存存款利率类型
     * @param depositRateTypeVO
     * @return
     */
    @LogMessage(content = "添加存款利率类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody  DepositRateTypeVO depositRateTypeVO){
        depositIRateTypeService.save(depositRateTypeVO);
        return success();
    }
    /**
     * 编辑存款利率类型
     */
    @GetMapping("/update")
    public AjaxJson<DepositRateType> update(String id ) {
        if (StringUtils.isNotEmpty(id)) {
           return AjaxJson.success(depositIRateTypeService.get(id));
        }
        return AjaxJson.success();
    }
    /**
     * 查看存款利率类型详情
     */
    @LogMessage(content = "查询存款利率类型详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<DepositRateType> view(@PathVariable("id") String id) {
        return AjaxJson.success(depositIRateTypeService.get(id));
    }
    /**
     * 根据id删除存款利率类型
     */
    @LogMessage(content = "删除存款利率类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        DepositRateType depositRateType= depositIRateTypeService.get(id);
        depositIRateTypeService.isAllowOp(depositRateType);//是否允许操作验证
        depositIRateTypeService.deleteEntity(depositRateType);
        return success();
    }
    /**
     * 获取所有存款利率类型
     */
    @GetMapping("/getDepositRateType")
    @ResponseBody
    public AjaxJson<List<DepositRateType>> getDepositRateType(){
        List<DepositRateType> typeList=depositIRateTypeService.getTypeList();
        return AjaxJson.success(typeList);
    }
    /**
     * 根据code获取存款利率类型
     */
    @GetMapping("/getRateTypeByCode")
    @ResponseBody
    public AjaxJson<DepositRateType> getDepositRateTypeByCode(String code){
        DepositRateType depositRateType=depositIRateTypeService.findPeriodByCode(code);
        return AjaxJson.success(depositRateType);
    }
    /**
     * 获取所有有效货币信息
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public AjaxJson<List<DepositRateType>> json(String depositClass, String depositAccSign) {
        return AjaxJson.success(depositIRateTypeService.getTypeListByParams(depositClass,depositAccSign));
    }
}
