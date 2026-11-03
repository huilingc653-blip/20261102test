package com.bytter.credit.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatCreditCooperationBank;
import com.bytter.credit.domain.service.RatCreditCooperationBankService;
import com.bytter.credit.enums.RatMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 授信合作银行维护
 *
 * @author fangcf
 * @date 2024-06-20
 */
@Controller
@RequestMapping("/cms/rat/creditCooperationBank")
public class RatCreditCooperationBankController extends BaseController {

    @Autowired
    private RatCreditCooperationBankService ratCreditCooperationBankService;

    /**
     * 授信合作银行维护 页面跳转
     *
     * @return 页面地址
     */
    @LogMessage(content = "查询授信合作银行维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/rat/creditcooperationbank/listRatCreditCooperationBank";
    }

    /**
     * 授信合作银行维护 table列表
     *
     * @param input 入参对象
     * @return 出参对象
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return ratCreditCooperationBankService.listOutputData(input);
    }

    /**
     * 授信合作银行维护新增页面跳转
     *
     * @param model model对象
     * @return 页面地址
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "cms/rat/creditcooperationbank/addRatCreditCooperationBank";
    }

    /**
     * 授信合作银行维护编辑页面跳转
     *
     * @param bank  授信合作银行对象
     * @param model model对象
     * @return 页面地址
     */
    @GetMapping("/update")
    public String update(RatCreditCooperationBank bank, Model model) {
        if (StringUtils.isNotEmpty(bank.getId())) {
            bank = ratCreditCooperationBankService.get(bank.getId(), true);
            model.addAttribute("bankPage", bank);
        }
        return "cms/rat/creditcooperationbank/updateRatCreditCooperationBank";
    }

    /**
     * 新增授信合作银行
     *
     * @param bank 授信合作银行对象
     * @return 出参对象
     */
    @LogMessage(content = "新增授信合作银行", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatCreditCooperationBank bank) {
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
    @LogMessage(content = "删除授信合作银行", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
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
    @LogMessage(content = "修改授信合作银行", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(RatCreditCooperationBank bank) {
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
    @LogMessage(content = "授信合作银行导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<RatCreditCooperationBank> list) {
        ratCreditCooperationBankService.saveImport(list, this.getCurrentUser());
        return success();
    }
}
