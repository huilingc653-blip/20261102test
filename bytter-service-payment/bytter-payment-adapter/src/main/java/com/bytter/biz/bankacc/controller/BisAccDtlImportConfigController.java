package com.bytter.biz.bankacc.controller;
import com.bytter.biz.inneraccount.service.BisAccDtlImportConfigService;
import com.bytter.biz.inneraccount.vo.BisAccDtlImportConfigVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.BankAccDtlMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: 离线流水智能导入配置表Controller
 * @Description: BIS_ACC_DTL_IMPORT_CONFIG
 * @date 2020-07-31 15:02:30
 */
@Controller
@RequestMapping("/payment/bisaccdtlimportconfig")
public class BisAccDtlImportConfigController extends BaseController {
    @Autowired
    private BisAccDtlImportConfigService bisAccDtlImportConfigService;
    /**
     * 离线流水智能导入配置表列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCDTLIMPORTCONFIG_APPLY)
    @LogMessage(content = "查询离线流水智能导入配置表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
            return "bankacc/listBisAccDtlImportConfig";
    }
    /**
     * 离线流水智能导入配置表 table列表
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCDTLIMPORTCONFIG_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisAccDtlImportConfigService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 离线流水智能导入配置表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCDTLIMPORTCONFIG_APPLY)
    @GetMapping("/add")
    public String add(BisAccDtlImportConfigVO bisAccDtlImportConfig, Model model) {
        List<Map<String,String>> headList=bisAccDtlImportConfigService.getTableInfo(bisAccDtlImportConfig);
        List<Map<String, String>> list = new ArrayList<>();
        //避免被覆盖
        if (EmptyUtils.isNotEmpty(headList)
                && "txt".equals(bisAccDtlImportConfig.getFileType())) {
            Map<String, String> fieldMap = null;
            for (Map<String, String> valueMap : headList) {
                fieldMap = new HashMap<String, String>();
                for (Map.Entry<String, String> vo : valueMap.entrySet()) {
                    fieldMap.put(vo.getKey(), vo.getValue());
                }
                list.add(fieldMap);
            }
        }
        model.addAttribute("headList", headList);//表头信息
        if (StringUtils.isNotEmpty(bisAccDtlImportConfig.getId())) {
            bisAccDtlImportConfig = bisAccDtlImportConfigService.getByIdUnion(bisAccDtlImportConfig.getId());
            model.addAttribute("bisAccDtlImportConfigPage", bisAccDtlImportConfig);
        }
        return "bankacc/addBisAccDtlImportConfig";
    }
    /**
     * 离线流水智能导入配置表编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCDTLIMPORTCONFIG_APPLY)
    @GetMapping("/update")
    public String update(BisAccDtlImportConfigVO bisAccDtlImportConfig, Model model) {
        List<Map<String,String>> headList=bisAccDtlImportConfigService.getTableInfo(bisAccDtlImportConfig);
        List<Map<String, String>> list = new ArrayList<>();
        //避免被覆盖
        if (null != headList && headList.size() > 0
                && "txt".equals(bisAccDtlImportConfig.getFileType())) {
            Map<String, String> fieldMap = null;
            for (Map<String, String> valueMap : headList) {
                fieldMap = new HashMap<>();
                for (Map.Entry<String, String> vo : valueMap.entrySet()) {
                    fieldMap.put(vo.getKey(), vo.getValue());
                }
                list.add(fieldMap);
            }
        }
        model.addAttribute("headList",list!=null&&list.size()>0?list:headList);//表头信息
        if (StringUtils.isNotEmpty(bisAccDtlImportConfig.getId())) {
            bisAccDtlImportConfig = bisAccDtlImportConfigService.getByIdUnion(bisAccDtlImportConfig.getId());
            model.addAttribute("bisAccDtlImportConfigPage", bisAccDtlImportConfig);
        }
        return "bankacc/updateBisAccDtlImportConfig";
    }
    /**
     * 添加离线流水智能导入配置表
     *
     * @param bisAccDtlImportConfig
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCDTLIMPORTCONFIG_APPLY)
    @LogMessage(content = "添加离线流水智能导入配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisAccDtlImportConfigVO bisAccDtlImportConfig) {
        if(this.dataCheck(bisAccDtlImportConfig)) {
            this.bisAccDtlImportConfigService.saveOrUpdate(bisAccDtlImportConfig, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 修改离线流水智能导入配置表
     *
     * @param bisAccDtlImportConfig
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCDTLIMPORTCONFIG_APPLY)
    @LogMessage(content = "修改离线流水智能导入配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BisAccDtlImportConfigVO bisAccDtlImportConfig) {
        if(this.dataCheck(bisAccDtlImportConfig)) {
            this.bisAccDtlImportConfigService.saveOrUpdate(bisAccDtlImportConfig, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 删除离线流水智能导入配置表
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCDTLIMPORTCONFIG_APPLY)
    @LogMessage(content = "删除离线流水智能导入配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        bisAccDtlImportConfigService.delelteUnion(id);
        return success();
    }
    /**
     * 数据校验
     *
     * @param bisAccDtlImportConfig
     * @return
     */
    public boolean dataCheck(BisAccDtlImportConfigVO bisAccDtlImportConfig) {
        //同一个银行类型允许配置多个文件类型，但同一个银行类型，不允许配置相同的文件类型，要校验重复
        if (this.bisAccDtlImportConfigService.isExistFileType(bisAccDtlImportConfig.getBankTypeId(), bisAccDtlImportConfig.getFileType(), bisAccDtlImportConfig.getId())) {
            throw new ParameterException(BankAccDtlMessageEnum.BISACCDTL_IMPORTCONFIG_MESSAGE_REPEAT_FILETYPE);
        }
        //配置参数中，如果是excel格式，需要判断相同所在列不能出现在不同的配置参数中；如果是txt格式，需要判断相同分隔符不能出现在不同的配置参数中
        return true;
    }
}
