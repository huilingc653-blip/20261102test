package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.dto.AbVoucherinfoViceDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "/v1-preposebill/accebill/billQuery",tags = "票据信息查询")
@RestController("v1AccebillController")
@RequestMapping("/v1-preposebill/accebill")
public class AccebillController extends BaseController {

    @Autowired
    private IAccebillService accebillService;

    @ResponseBody
    @GetMapping("/listAccebillInfoById")
    public AjaxJson<List<AbVoucherinfoViceDTO>> listAccebillInfoById(String billId, String payId){
        List<AbVoucherinfoViceDTO> abVoucherinfoViceDTOList = accebillService.listAccebillInfoByIds(billId, payId);
        return success(abVoucherinfoViceDTOList);
    }


}
