package com.bytter.system.web.controller;
import com.bytter.basic.entity.BtInputBankInfo;
import com.bytter.basic.service.BtInputBankInfoService;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.service.ExcelCacheService;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行行号维护
 * @date 2017-05-23 19:06:25
 */
@Controller
@RequestMapping("/basic/business/btinputbankinfo")
public class BtInputBankInfoController extends BaseController {
    @Autowired
    private BtInputBankInfoService btInputBankInfoService;
    @Autowired
    private ExcelCacheService excelCacheService;
    /**
     * 银行行号维护列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询银行行号维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBtInputBankInfo";
    }
    /**
     * 银行行号维护 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btInputBankInfoService.getDataTables(dataTablesInput);
    }
    /**
     * 银行行号维护 table列表(需要有前缀的前提)
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/datatablePrefix")
    @ResponseBody
    public DataTablesOutput datatablePrefix(@RequestBody DataTablesInput dataTablesInput) {
        return btInputBankInfoService.getDataTablesPrefix(dataTablesInput);
    }
    /**
     * 选择联行号
     *
     * @return
     */
    @GetMapping("/select")
    public String select(String id, Model model, String joinBankType, String bankTypeId) {
        model.addAttribute("id", id);
        model.addAttribute("joinBankType", joinBankType);
        model.addAttribute("bankTypeId", bankTypeId);
        return "basic/business/selectBtInputBankInfo";
    }
    /**
     * 银行行号维护新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(BtInputBankInfo btInputBankInfo, Model model) {
        if (StringUtils.isNotEmpty(btInputBankInfo.getId())) {
            btInputBankInfo = btInputBankInfoService.get(btInputBankInfo.getId());
            model.addAttribute("btInputBankInfoPage", btInputBankInfo);
        }
        return "basic/business/addBtInputBankInfo";
    }
    /**
     * 银行行号维护编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(BtInputBankInfo btInputBankInfo, Model model) {
        if (StringUtils.isNotEmpty(btInputBankInfo.getId())) {
            BtInputBankInfoVO btInputBankInfoVo = btInputBankInfoService.getByUnion(btInputBankInfo.getId());
            model.addAttribute("btInputBankInfoPage", btInputBankInfoVo);
        }
        return "basic/business/updateBtInputBankInfo";
    }
    /**
     * 银行行号维护详情页
     *
     * @return
     */
    @LogMessage(content = "查询银行行号维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtInputBankInfoVO btInputBankInfo = btInputBankInfoService.getByUnion(id);
        model.addAttribute("btInputBankInfoPage", btInputBankInfo);
        return "basic/business/viewBtInputBankInfo";
    }
    /**
     * 添加银行行号维护
     *
     * @param btInputBankInfo
     * @return
     */
    @LogMessage(content = "添加银行行号维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BtInputBankInfo btInputBankInfo) {
        if (!StringUtils.isNumeric(btInputBankInfo.getBankCode()) || btInputBankInfo.getBankCode().length() < 12) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKCODE_WRONG,btInputBankInfo.getBankCode(),"");
        }
        btInputBankInfoService.prefixCityExist(btInputBankInfo.getBankCode());
        String bankPrefix = StringUtils.substring(StringUtils.trim(btInputBankInfo.getBankCode()), 0, 3);
        String areaCode = StringUtils.substring(StringUtils.trim(btInputBankInfo.getBankCode()), 3, 7);
        btInputBankInfo.setBankPrefix(bankPrefix);
        btInputBankInfo.setAreaCode(areaCode);
        btInputBankInfo.setCreateBy(this.getCurrentUser().getLoginName());
        btInputBankInfo.setCreateDate(DateUtils.getDate());
        btInputBankInfoService.chechSave(btInputBankInfo);
        return success();
    }
    /**
     * 修改银行行号维护
     *
     * @param btInputBankInfo
     * @return
     */
    @LogMessage(content = "修改银行行号维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtInputBankInfo btInputBankInfo) {
        if (btInputBankInfoService.getByBankInfo(btInputBankInfo.getBankCode()) != null &&
                !(btInputBankInfoService.getSysBankInfoId(btInputBankInfo.getBankCode()).equals(btInputBankInfo.getId()))) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKCODE_UNIQUE);
        }
        if (!StringUtils.isNumeric(btInputBankInfo.getBankCode()) || btInputBankInfo.getBankCode().length() < 12) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKCODE_WRONG);
        }
        BtInputBankInfo t = btInputBankInfoService.get(btInputBankInfo.getId());
        String bankPrefix = StringUtils.substring(StringUtils.trim(btInputBankInfo.getBankCode()), 0, 3);
        String areaCode = StringUtils.substring(StringUtils.trim(btInputBankInfo.getBankCode()), 3, 7);
        btInputBankInfo.setBankPrefix(bankPrefix);
        btInputBankInfo.setAreaCode(areaCode);
        btInputBankInfo.setUpdateBy(this.getCurrentUser().getLoginName());
        btInputBankInfo.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(btInputBankInfo, t);
        btInputBankInfoService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除银行行号维护
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除银行行号维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtInputBankInfo btInputBankInfo = btInputBankInfoService.get(id);
        btInputBankInfoService.deleteEntity(btInputBankInfo);
        return success();
    }
    /**
     * 批量删除银行行号维护
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除银行行号维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtInputBankInfo btInputBankInfo = btInputBankInfoService.get(id);
            btInputBankInfoService.deleteEntity(btInputBankInfo);
        }
        return success();
    }
    /**
     * 前台调用获取银行行号信息
     *
     * @param bankCode
     * @return
     */
    @RequestMapping(params = "getBankInfoJson")
    @ResponseBody
    public AjaxJson getBankInfoJson(String bankCode) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        if (StringUtils.isEmpty(bankCode)) {
            j.setObj(btInputBankInfoService.listAll());
        } else {
            j.setObj(btInputBankInfoService.getBtInputBankInfo(bankCode));
        }
        return j;
    }
    @GetMapping("/json")
    @ResponseBody
    public List<BtInputBankInfo> json() {
        return btInputBankInfoService.listAll();
    }
    @GetMapping("/bankInfoJson")
    @ResponseBody
    public List<BtInputBankInfo> getBtInputBankInfo(String bankCode) {
        return btInputBankInfoService.getBtInputBankInfo(bankCode);
    }
    /**
     * 保存导入数据
     *
     * @param
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestParam("cacheCode") String cacheCode) {
        List<BtInputBankInfoVO> list = excelCacheService.getListExcelDataCache(cacheCode, BtInputBankInfoVO.class);
        this.btInputBankInfoService.saveImport(list, this.getCurrentUser());
        return success();
    }
    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput<?> dataTablesOutput = btInputBankInfoService.getDataTablesPrefix(dataTablesInput);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "银行行号信息维护",5000, BtInputBankInfoVO.class);
    }
    /**
     * 选择联行号
     *
     * @return
     */
    @GetMapping("/selectInputBankAndSwiftBankInfo")
    public String selectInputBankAndSwiftBankInfo(String id, Model model, String joinBankType, String bankTypeId) {
        model.addAttribute("id", id);
        model.addAttribute("joinBankType", joinBankType);
        model.addAttribute("bankTypeId", bankTypeId);
        return "basic/business/selectInputBankAndSwiftBankInfo";
    }
    /**
     * 选择联行号
     *
     * @return
     */
    @GetMapping("/selectDomesticBankInfo")
    public String selectDomesticBankInfo(String id, Model model, String joinBankType, String bankTypeId) {
        model.addAttribute("id", id);
        model.addAttribute("joinBankType", joinBankType);
        model.addAttribute("bankTypeId", bankTypeId);
        return "basic/business/selectDomesticBankInfo";
    }
    /**
     * 银行行号维护 table列表(需要有前缀的前提)(选择组件，无需权限控制)
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/datatablePrefixSelect")
    @ResponseBody
    public DataTablesOutput datatablePrefixSelect(@RequestBody DataTablesInput dataTablesInput) {
        return btInputBankInfoService.getDataTablesPrefix(dataTablesInput);
    }


    /**
     * 选择联行号
     *
     * @return
     */
    @GetMapping("/selectAllBankInfo")
    public String selectAllBankInfo(String type, String id, Model model, String joinBankType, String bankTypeId) {
        model.addAttribute("id", id);
//        model.addAttribute("joinBankType", joinBankType);
        model.addAttribute("joinBankType", "true");
        model.addAttribute("bankTypeId", bankTypeId);
        if (StringUtils.isNotBlank(type) && StringUtils.equals(type, "outerBank")) {
            model.addAttribute("onlyShowOutBank", "1");
        } else {
            model.addAttribute("onlyShowOutBank", "0");
        }
        return "basic/business/selectAllBankInfo";
    }

    /**
     * 选择联行号
     *
     * @return
     */
    @GetMapping("/selectInnerBankInfo")
    public String selectInnerBankInfo(String id, Model model, String joinBankType, String bankTypeId) {
        model.addAttribute("id", id);
        model.addAttribute("joinBankType", joinBankType);
        model.addAttribute("bankTypeId", bankTypeId);
        return "basic/business/selectInnerBankInfo";
    }

}
