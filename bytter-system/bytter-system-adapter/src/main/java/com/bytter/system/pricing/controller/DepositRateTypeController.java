package com.bytter.system.pricing.controller;
import com.bytter.pricing.entity.DepositRateType;
import com.bytter.pricing.service.DepositRateTypeService;
import com.bytter.pricing.vo.DepositRateTypeVO;
import com.bytter.system.pricing.constant.PricingConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
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
@Controller
@RequestMapping("/pricing/depositRateType/interestRateType")
public class DepositRateTypeController extends BaseController {
    @Autowired
    private DepositRateTypeService depositIRateTypeService;
    /**
     *跳转到存款利率类型列表
     */
    @LogMessage(content = "查询存款利率类型列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(){
        return "pricing/depositRate/listDepositRateType";
    }
    /**
     * 存款利率类型 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return depositIRateTypeService.getDataTables(dataTablesInput);
    }
    /**
     * 新增存款利率类型
     */
    @GetMapping("/add")
    @LogMessage(content = "跳转到新增存款利率类型页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    public String add() {
        return "pricing/depositRate/addDepositRateType";
    }
    /**
     * 检查编号是否重复
     * @param code
     * @return
     */
    @PostMapping("/checkCode")
    @ResponseBody
    public String checkContractNo(String code) {
        DepositRateType depositRateType=depositIRateTypeService.findPeriodByCode(code);
        if (depositRateType==null) {
            return PricingConstant.PRICING_COMMON_Y;//不存在
        }
        return PricingConstant.PRICING_COMMON_N;
    }
    /**
     * 保存存款利率类型
     * @param depositRateTypeVO
     * @return
     */
    @LogMessage(content = "添加存款利率类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(DepositRateTypeVO depositRateTypeVO){
        depositIRateTypeService.save(depositRateTypeVO);
        return success();
    }
    /**
     * 编辑存款利率类型
     */
    @GetMapping("/update")
    public String update(DepositRateType depositRateType, Model model) {
        if (StringUtils.isNotEmpty(depositRateType.getId())) {
            depositRateType = depositIRateTypeService.get(depositRateType.getId());
            model.addAttribute("depositRateType", depositRateType);
        }
        return "pricing/depositRate/updateDepositRateType";
    }
    /**
     * 查看存款利率类型详情
     */
    @LogMessage(content = "查询存款利率类型详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        DepositRateType depositRateType = depositIRateTypeService.get(id);
        model.addAttribute("depositRateType", depositRateType);
        return "pricing/depositRate/viewDepositRateType";
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
    public List<DepositRateType> getDepositRateType(){
        List<DepositRateType> typeList=depositIRateTypeService.getTypeList();
        return typeList;
    }
    /**
     * 根据code获取存款利率类型
     */
    @GetMapping("/getRateTypeByCode")
    @ResponseBody
    public DepositRateType getDepositRateTypeByCode(String code){
        DepositRateType depositRateType=depositIRateTypeService.findPeriodByCode(code);
        return depositRateType;
    }
    /**
     * 获取所有有效货币信息
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<DepositRateType> json(String depositClass, String depositAccSign) {
        return depositIRateTypeService.getRateTypeByParamsNotJoinCur(depositClass, depositAccSign);
    }
}
