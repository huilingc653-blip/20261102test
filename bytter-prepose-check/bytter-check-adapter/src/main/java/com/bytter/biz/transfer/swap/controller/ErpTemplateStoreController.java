package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.entity.ErpHisDtlTemplate;
import com.bytter.biz.transfer.swap.entity.ErpTemplate;
import com.bytter.biz.transfer.swap.entity.ErpTemplateStore;
import com.bytter.biz.transfer.swap.enums.ErpInterConfigEnum;
import com.bytter.biz.transfer.swap.service.ErpInterConfigService;
import com.bytter.biz.transfer.swap.service.ErpTemplateService;
import com.bytter.biz.transfer.swap.service.ErpTemplateStoreService;
import com.bytter.biz.transfer.swap.vo.ErpTemplateBillVO;
import com.bytter.biz.transfer.swap.vo.ErpTemplateStoreVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.util.ExportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/29.
 */
@Controller
@RequestMapping("/erp/swap/templatestore")
public class ErpTemplateStoreController extends BaseController {
    @Autowired
    private ErpTemplateStoreService templateStoreService;
    @Autowired
    private ErpTemplateService erpTemplateService;
    @Autowired
    private ErpInterConfigService erpInterConfigService;
    @LogMessage(content = "查询银行账号明细表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{type}")
    public String list(@PathVariable("type")String  type, Model model) {
        model.addAttribute("configType", ErpInterConfigEnum.valueOf(type.toUpperCase()).getCode());
        if(ErpInterConfigEnum.ACCEBILL.getCode().equalsIgnoreCase(type)){
            return "transfer/swap/templatestorebill_list";
        }else{
            model.addAttribute("maxDate", DateUtils.addDay(DateUtils.getDate(), 1));
            int newSetValue= isNewSet(type);
            model.addAttribute("newSet", newSetValue);
            return "transfer/swap/templatestore_list";
        }
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return templateStoreService.getDataTable(dataTablesInput);
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
                DataTablesOutput dataTablesOutput = templateStoreService.getDataTable(dataTablesInput);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "收款数据处理", 5000, ErpTemplateStoreVO.class);
    }
    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAllQuery")
    public void exportAllQuery(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput dataTablesOutput = templateStoreService.getDataTable(dataTablesInput);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "收款数据查询", 5000, ErpTemplateStoreVO.class);
    }
    @PostMapping("/bill/datatable")
    @ResponseBody
    public DataTablesOutput billdataTable(@RequestBody DataTablesInput dataTablesInput) {
        return templateStoreService.getDataTablebill(dataTablesInput);
    }
    @GetMapping("/query/{type}")
    public String query(@PathVariable("type")String  type, Model model) {
        model.addAttribute("configType", ErpInterConfigEnum.valueOf(type.toUpperCase()).getCode());
        if(ErpInterConfigEnum.ACCEBILL.getCode().equalsIgnoreCase(type)){
            return "transfer/swap/templatestorebill_query";
        }else{
            model.addAttribute("maxDate", DateUtils.addDay(DateUtils.getDate(), 1));
            int newSetValue= isNewSet(type);
            model.addAttribute("newSet", newSetValue);
            return "transfer/swap/templatestore_query";
        }
    }
    @PostMapping("/datatable/query")
    @ResponseBody
    public DataTablesOutput dataTableQuery(@RequestBody DataTablesInput dataTablesInput) {
        return templateStoreService.getDataTableQuery(dataTablesInput);
    }
    @PostMapping("/bill/datatable/query")
    @ResponseBody
    public DataTablesOutput billdataTableQuery(@RequestBody DataTablesInput dataTablesInput) {
        return templateStoreService.getDataTablebillQuery(dataTablesInput);
    }
    /**
     * 补录
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/entering/{id}")
    public String entering(@PathVariable("id")String id,@RequestParam(required = true) String type, Model model) {
        model.addAttribute("configType", ErpInterConfigEnum.valueOf(type.toUpperCase()).getCode());
        if(ErpInterConfigEnum.ACCEBILL.getCode().equalsIgnoreCase(type)){
            ErpTemplateBillVO vo = templateStoreService.selectBillById(id);
            if(!EmptyUtils.isEmpty(vo)){
                ErpTemplate templateType = erpTemplateService.selectDtlMapById(vo.getTemplateId());
                vo.setTypeId(EmptyUtils.isEmpty(templateType)?null:templateType.getTemplateTypeId());
                //模板是否可编辑
                model.addAttribute("enteringIsEdit", templateType.getEnteringIsEdit());
            }else{
                //模板是否可编辑（默认为0）
                model.addAttribute("enteringIsEdit", "0");
            }
            model.addAttribute("templateStore", vo);
            return "transfer/swap/templatestorebill_info";
        }else{
            ErpTemplateStoreVO vo = templateStoreService.selectBankccById(id);
            if(!EmptyUtils.isEmpty(vo)){
                ErpTemplate templateType = erpTemplateService.selectDtlMapById(vo.getTemplateId());
                vo.setTypeId(EmptyUtils.isEmpty(templateType)?null:templateType.getTemplateTypeId());
                //模板是否可编辑
                model.addAttribute("enteringIsEdit", templateType.getEnteringIsEdit());
            }else{
                //模板是否可编辑（默认为0）
                model.addAttribute("enteringIsEdit", "0");
            }
            model.addAttribute("templateStore", vo);
            return "transfer/swap/templatestore_info";
        }
    }
    /**
     * 根据模板补录和单据补录取 数据主信息列表头信息
     * @return
     */
    @RequestMapping("/dynaDataHeader")
    @ResponseBody
    public List<Map<String,String>> warnDataHeader(String type){
       return templateStoreService.erpDataHeader(isNewSet(type),type);
    }
    /**
     * 获取补录配置参数
     * @param
     * @return
     */
    private int isNewSet(String type){
        return erpInterConfigService.getValueByTypeAndCode(type,"newSet").intValue();
    }
    /**
     * 批量补录
     * @param type
     * @param model
     * @return
     */
    @GetMapping("/enterings/{type}")
    public String enterings(@PathVariable("type")String  type,@RequestParam(value = "ids",required = true) String ids, Model model) {
        model.addAttribute("ids", ids);
        model.addAttribute("configType", ErpInterConfigEnum.valueOf(type.toUpperCase()).getCode());
        return "transfer/swap/templatestore_infos";
    }
    
    @GetMapping("/json/{id}")
    @ResponseBody
    public ErpTemplateStore json(@PathVariable("id") String id) {
        return templateStoreService.selectDtlMapById(id);
    }
    /**
     * 进行补录操作时,校验补录的单据的状态
     * @param ids
     * @return
     */
    @GetMapping("/checkStatus")
    @ResponseBody
    public AjaxJson checkErpTemplateStoreStatus(@RequestParam(value = "ids",required = true) String ids,String type) {
        List<String> idList = Arrays.asList(ids.split(","));
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        for (String id : idList){
            ErpHisDtlTemplate vo = templateStoreService.getErpHisDtlTemplate(id);
            if ((!EmptyUtils.isEmpty(vo) && !EmptyUtils.isEmpty(vo.getStatus()))
                    && (vo.getStatus() != 0 && vo.getStatus() != -1)){
                json.setSuccess(false);
                return json;
            }
        }
        return json;
    }
    /**
     * 进行补录操作时,校验补录的单据的状态
     * @param ids
     * @return
     */
    @GetMapping("/checkComfire")
    @ResponseBody
    public AjaxJson checkItemOnWaitComfire(@RequestParam(value = "ids",required = true) String ids,String type) {
        List<String> idList = Arrays.asList(ids.split(","));
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        for (String id : idList){
            ErpHisDtlTemplate vo = templateStoreService.getErpHisDtlTemplate(id);
            if ((!EmptyUtils.isEmpty(vo) && !EmptyUtils.isEmpty(vo.getStatus()))
                    && (vo.getStatus() == -1 || vo.getStatus() == 2)){
                json.setSuccess(false);
                return json;
            }
        }
        return json;
    }
}
