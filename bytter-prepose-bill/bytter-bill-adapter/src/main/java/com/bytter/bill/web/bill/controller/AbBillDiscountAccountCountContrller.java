package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillDiscountAccountCountService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountCountVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description:贴现到账统计Controller
 * @date 2017-7-21 13:49:49
 */
@Controller
@RequestMapping("/bill/accebill/abbilldiscountaccountCount")
public class AbBillDiscountAccountCountContrller {
    @Autowired
    private AbBillDiscountAccountCountService abBillDiscountAccountCountService;
    /**
     * 贴现到账统计页面跳转
     *
     * @return
     */
    @LogMessage(content = "贴现到账页面统计跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model, AbBillDiscountAccountCountVO vo) {
        if (vo.getIsQuery() != null && "1".equals(vo.getIsQuery())) {
            abBillDiscountAccountCountService.toListInit(model,vo);
        }
        return "bill/accebill/listAbBillDiscountAccountCount";
    }
    /**
     * 贴现到账统计列表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput,String corpId) {
        if(StringUtils.isNotBlank (corpId)){
            dataTablesInput.getFormdata ().put("ins_corpId",corpId);
        }
        return abBillDiscountAccountCountService.listAbBillDiscountAccountCount(dataTablesInput);
    }
}
