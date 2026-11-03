package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.entity.ErpDataTypeField;
import com.bytter.biz.transfer.swap.service.ErpInterConfigFillService;
import com.bytter.biz.transfer.swap.vo.ErpInterConfigFillFormatVO;
import com.bytter.check.dto.ErpDataTypeDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * Created by zhouj on 2018/5/28.
 */
@Controller
@RequestMapping("/erp/swap/interconfigfill")
public class ErpInterConfigFillController extends BaseController {
    @Autowired
    private ErpInterConfigFillService erpInterConfigFillService;
    /**
     * 查询所有有效凭证类型
     * @return
     */
    @GetMapping("/queryErpDataTypeFieldValid")
    @ResponseBody
    public List<ErpDataTypeField> queryErpDataTypeFieldValid(String type) {
        return erpInterConfigFillService.queryErpDataTypeFieldValid(type);
    }
    /**
     * 基础数据类型
     * @return
     */
    @GetMapping("/queryListErpDataTypeValid")
    @ResponseBody
    public List<ErpDataTypeDTO> queryListErpDataTypeValid() {
        return erpInterConfigFillService.listErpDataTypeByValidSign();
    }
    /**
     * 添加补录配置
     * @param erpInterConfigFillFormatVO
     * @return
     */
    @LogMessage(content = "添加补录配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ErpInterConfigFillFormatVO erpInterConfigFillFormatVO){
        erpInterConfigFillService.saveInterConfigFill(erpInterConfigFillFormatVO,this.getCurrentUser());
        return success();
    }
}
