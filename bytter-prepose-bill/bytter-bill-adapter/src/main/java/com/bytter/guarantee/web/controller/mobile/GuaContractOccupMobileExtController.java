package com.bytter.guarantee.web.controller.mobile;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.guarantee.domain.service.GuaContractOccupService;
import com.bytter.guarantee.domain.vo.GuaContractOccupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @author luxy
 * @Title: GuaContractOccupController
 * @ProjectName T2_IDEA
 * @Description: 担保合同占用控制层
 * @date 2019/4/30 19:47
 */
@Controller
@RequestMapping("/mobile/cms/guarantee/guaContractOccup")
public class GuaContractOccupMobileExtController extends BaseController {
    @Autowired
    private GuaContractOccupService guaContractOccupService;



    @RequestMapping("/mobileContractUseView")
    @LogMessage(content = "跳转到担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String mobileContractUseView(Model model,String contractId){
        model.addAttribute("contractId",contractId);
        return "/mobile/cms/guarantee/mobileGuaContractUseTab";
    }

    @PostMapping("/contractUseList")
    @ResponseBody
    public Object contractUseList(String contractId,String sysCode,String startDate,String endDate,String status){
        DataTablesInput input=new DataTablesInput();
        HashMap<String,Object> params=new HashMap<>();
        params.put("eqs_contractId",contractId);
        params.put("eqs_sysCode",sysCode);
        if(EmptyUtils.isNotEmpty(status)){
            params.put("status",status);
        }
        if(EmptyUtils.isNotEmpty(startDate)){
            startDate=startDate.replace(".","-");
            params.put("ged_useDate",DateUtils.formatDate(startDate));//大于开始日期

        }
        if(EmptyUtils.isNotEmpty(endDate)){
            endDate=endDate.replace(".","-");
            params.put("led_useDate",DateUtils.formatDate(endDate));//小于结束日期
        }

        input.setFormdata(params);
        input.setLength(1000);
        DataTablesOutput output=guaContractOccupService.getUseContractList(input, getCurrentUser());
        List<GuaContractOccupVO> occupVOS=output.getData();
        return occupVOS;
    }

}
