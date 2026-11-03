package com.bytter.bill.web.saas.controller.bill;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 手机端统一入口
 * @author caojy
 *
 */
@Api(value = "/v1-preposebill/bill/mobile/port", tags = "手机端统一入口")
@RestController("v1AbBillMobilePortController")
@RequestMapping("/v1-preposebill/bill/mobile/port")
public class AbBillMobilePortController extends BaseController {

    @DubboReference(check = false)
    private IUserService userService;

    /**
     * APP报表-承兑汇票
     *
     * @return
     */
    @RequestMapping("/appAccebillReportIndex")
    @ApiOperation("APP报表-承兑汇票页面初始化")
    public AjaxJson<Map<String, Object>> appAccebillReportIndex() {
        ConcurrentModel model = new ConcurrentModel();
        String userId = getCurrentUser().getId();
        UserDTO userDTO = userService.getByUserId(userId);
        CorpDTO corp = userDTO.getCorp();
        model.addAttribute("corpId", corp.getId());
        model.addAttribute("netId", corp.getNetId());
        return AjaxJson.success(model.asMap());
    }
}