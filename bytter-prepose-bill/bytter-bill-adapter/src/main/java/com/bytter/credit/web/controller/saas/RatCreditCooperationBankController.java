package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatCreditCooperationBank;
import com.bytter.credit.domain.service.RatCreditCooperationBankService;
import com.bytter.credit.enums.RatMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 授信合作银行维护
 *
 * @author fangcf
 * @date 2024-06-20
 */
@Api(tags = "授信合作银行维护", value = "/v1-cms/cms/rat/creditCooperationBank")
@RestController("v1RatCreditCooperationBankController")
@RequestMapping("/v1-cms/cms/rat/creditCooperationBank")
public class RatCreditCooperationBankController extends BaseController {

    @Autowired
    private RatCreditCooperationBankService ratCreditCooperationBankService;

    /**
     * 授信合作银行维护 table列表
     *
     * @param dataTablesInput 入参对象
     * @return 出参对象
     */
    @ApiOperation("查询授信合作银行维护表格数据")
    @LogMessage(content = "查询授信合作银行维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(ratCreditCooperationBankService.listOutputData(input));
    }

    /**
     * 授信合作银行维护编辑页面数据
     *
     * @param bank 授信合作银行对象
     * @return 出参对象
     */
    @ApiOperation("授信合作银行维护编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatCreditCooperationBank> updatePage(RatCreditCooperationBank bank) {
        if (StringUtils.isNotEmpty(bank.getId())) {
            bank = ratCreditCooperationBankService.get(bank.getId(), true);
        }
        return success(bank);
    }

    /**
     * 新增授信合作银行
     *
     * @param bank 授信合作银行对象
     * @return 出参对象
     */
    @ApiOperation("新增授信合作银行")
    @LogMessage(content = "新增授信合作银行", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatCreditCooperationBank bank) {
        if (StringUtils.isEmpty(bank.getBankCode())) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_CREDITCOOPERATIONBANK_NULL);
        }
        if (StringUtils.isEmpty(bank.getCooperationType())) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_COOPERATIONTYPE_NULL);
        }
        ratCreditCooperationBankService.saveRatCreditCooperationBank(bank, getCurrentUser());
        return success();
    }

    /**
     * 删除授信合作银行
     *
     * @param id 主键id
     * @return 出参对象
     */
    @ApiOperation("删除授信合作银行")
    @LogMessage(content = "删除授信合作银行", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ratCreditCooperationBankService.delete(id);
        return success();
    }

    /**
     * 修改授信合作银行
     *
     * @param bank 授信合作银行对象
     * @return 出参对象
     */
    @ApiOperation("修改授信合作银行")
    @LogMessage(content = "修改授信合作银行", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatCreditCooperationBank bank) {
        if (StringUtils.isEmpty(bank.getCooperationType())) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_COOPERATIONTYPE_NULL);
        }
        ratCreditCooperationBankService.updateRatCreditCooperationBank(bank, getCurrentUser());
        return success();
    }

    /**
     * 授信合作银行导入
     *
     * @param list 导入数据集合
     * @return 出参对象
     */
    @ApiOperation("授信合作银行导入")
    @LogMessage(content = "授信合作银行导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<RatCreditCooperationBank> list) {
        ratCreditCooperationBankService.saveImport(list, this.getCurrentUser());
        return success();
    }
}
