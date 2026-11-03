package com.bytter.increment.web.controller;
import com.bytter.increment.domain.service.ChoProductDetailedService;
import com.bytter.increment.domain.vo.ChoProductDetailedShowVo;
import com.bytter.increment.domain.vo.ChoProductDetailedVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @author shenc
 * @version 1.0
 * @Title: Controller
 * @Description: 理财产品
 * @date 2019-03-26 14:03:30
 */
@Controller
@RequestMapping("/report/choProductDetailed")
public class ChoProductDetailedController extends BaseController {
    @Autowired
    private ChoProductDetailedService choProductDetailedService;
    @GetMapping("/list")
    public String list(Model model, ChoProductDetailedVo vo) {
        if (EmptyUtils.isEmpty(vo.getCurrentStartDate())) {
            vo.setCurrentStartDate(DateUtils.getDate());
        }
        if (EmptyUtils.isEmpty(vo.getCurrentEndDate())) {
            vo.setCurrentEndDate(DateUtils.getDate());
        }
        if (vo.getCurrentStartDate() != null) {
            List<ChoProductDetailedShowVo> choRedeemDetailedVos = choProductDetailedService.listChoRedeemDetail(getCurrentUser(), vo);
            model.addAttribute("choProducts", choRedeemDetailedVos);
            model.addAttribute("search", vo);
        }
        return "chowmatistic/listChoProductDetailed";
    }
    @PostMapping("/getChoProductDetailed")
    @ResponseBody
    public Object getChoProductDetailed(ChoProductDetailedVo vo) {
        if (EmptyUtils.isEmpty(vo.getCurrentStartDate())) {
            vo.setCurrentStartDate(DateUtils.getDate());
        }
        if (EmptyUtils.isEmpty(vo.getCurrentEndDate())) {
            vo.setCurrentEndDate(DateUtils.getDate());
        }
        List<ChoProductDetailedShowVo> choRedeemDetailedVos = choProductDetailedService.listChoRedeemDetail(getCurrentUser(), vo);
        return choRedeemDetailedVos;
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput output = new DataTablesOutput();
        ChoProductDetailedVo vo = new ChoProductDetailedVo();
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String corpId = String.valueOf(formdata.get("corpId"));
        Object bankObj = formdata.get("bankTypeId");
        String bankTypeId = bankObj == null ? null : String.valueOf(bankObj);
        String productType = String.valueOf(formdata.get("productType"));
        Object curObj = formdata.get("curId");
        String curId = curObj == null ? null : String.valueOf(curObj);
        Object deadObj = formdata.get("isDeadline");
        String isDeadline = deadObj == null ? null : String.valueOf(deadObj);
        Date currentStartDate = DateUtils.parse(String.valueOf(formdata.get("currentStartDate")), "yyyy-MM-dd");
        Date currentEndDate = DateUtils.parse(String.valueOf(formdata.get("currentEndDate")), "yyyy-MM-dd");
        if (EmptyUtils.isNotEmpty(corpId)) {
            vo.setCorpId(corpId);
        }
        if (EmptyUtils.isNotEmpty(bankTypeId)) {
            vo.setBankTypeId(bankTypeId);
        }
        if (EmptyUtils.isNotEmpty(productType)) {
            vo.setProductType(productType);
        }
        if (EmptyUtils.isNotEmpty(curId)) {
            vo.setCurId(curId);
        }
        if (EmptyUtils.isNotEmpty(isDeadline)) {
            vo.setIsDeadline(isDeadline);
        }
        if (EmptyUtils.isNotEmpty(String.valueOf(formdata.get("currentStartDate")))) {
            vo.setCurrentStartDate(currentStartDate);
        }
        if (EmptyUtils.isNotEmpty(String.valueOf(formdata.get("currentEndDate")))) {
            vo.setCurrentEndDate(currentEndDate);
        }
        List<ChoProductDetailedShowVo> list = choProductDetailedService.listChoRedeemDetail(getCurrentUser(), vo);
        List<ChoProductDetailedShowVo> newList = new ArrayList<>();
        if (list.size() <= 10) {
            output.setData(list);
        } else {
            int start = dataTablesInput.getStart();
            for (int i = start; i < start + dataTablesInput.getLength(); i++) {
                if (i < list.size()) {
                    newList.add(list.get(i));
                }
            }
            output.setData(newList);
        }
        output.setRecordsTotal(list.size());
        output.setRecordsFiltered(list.size());
        output.setDraw(dataTablesInput.getDraw()+1);
        return output;
    }
}
