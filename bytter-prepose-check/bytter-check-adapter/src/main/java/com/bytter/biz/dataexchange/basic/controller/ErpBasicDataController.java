package com.bytter.biz.dataexchange.basic.controller;
import com.bytter.biz.dataexchange.basic.entity.ErpDataType;
import com.bytter.biz.dataexchange.basic.service.ErpBasicDataService;
import com.bytter.biz.dataexchange.basic.service.ErpDataTypeService;
import com.bytter.biz.dataexchange.basic.vo.ErpBasicDataListVO;
import com.bytter.biz.dataexchange.basic.vo.ErpBasicDataVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 基础数据映射
 * @date 2017-12-14 11:24:27
 */
@Controller
@RequestMapping("/dataexchange2/basic/erpbasicdata")
public class ErpBasicDataController extends BaseController {
    @Autowired
    private ErpBasicDataService erpBasicDataService;

    @Autowired
    private ErpDataTypeService erpDataTypeService;
    /**
     * 基础数据映射列表 页面跳转
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPBASICDATA_VIEW)
    @LogMessage(content = "查询基础数据映射列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "dataexchange/basic/listErpBasicData";
    }
    /**
     * 基础数据映射 table列表
     *
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPBASICDATA_VIEW)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return erpBasicDataService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 保存
     *
     * @param erpBasicDataList
     * @return
     */
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_BASIC_ERPBASICDATA_ADD)
    @LogMessage(content = "保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson save(HttpServletRequest request, ErpBasicDataListVO erpBasicDataList) {
        erpBasicDataService.saveErpBasicData(erpBasicDataList, getCurrentUser());
        return success();
    }
    /**
     * 银行账号与ERP科目对应关系
     **/
//    @RequiresPermissions(DataexchangeResourceConstant.FINACIAL_CHECK_ERPANDBANKACC_APPLY)
    @GetMapping("/erpandbankacc")
    public String erpAndBankAcc(Model model, ErpBasicDataVO vo) {
        return "erp/improtTemplateErpAndBankAcc";
    }
    @PostMapping("/erpdatatable")
    @ResponseBody
    public DataTablesOutput erpDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return erpBasicDataService.getErpList(dataTablesInput);
    }
    /**
     * 功能介绍 : ERP与银行账号对应关系数据保存<br>
     *
     * @author zhengxn
     */
    @LogMessage(content = "ERP与银行账号对应关系数据导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<ErpBasicDataVO> list) {
        this.erpBasicDataService.saveImport(list, this.getCurrentUser());
        return success();
    }


     /**
     * 查询类型列表
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<ErpDataType> listByValidSign() {
        List<ErpDataType> list = erpDataTypeService.listErpDataType("1");
        return list;
    }
}
