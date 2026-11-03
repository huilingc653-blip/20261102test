package com.bytter.finance.saas.controller.lease;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.finance.domain.lease.service.LeaseInterestService;
import com.bytter.finance.domain.lease.vo.LeaseInterestListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 租赁租金支付表
 * @date 2022-04-15 10:07:46
 */
@Api(value = "/v1-preposebill/cms/lease/leaseinterest", tags = "租赁租金支付表")
@RestController("v1LeaseInterestController")
@RequestMapping("/v1-preposebill/cms/lease/leaseinterest")
public class LeaseInterestController extends BaseController {
    @Autowired
    private LeaseInterestService leaseInterestService;

    /**
     * 预计利息明细
     *
     * @param leaseId
     * @param showSign
     *            显示标识 0查看.1带选择
     * @return
     */
    @ApiOperation("预计利息明细")
    @GetMapping("/list")
    public AjaxJson<LeaseInterestListVO> list(String leaseId, String showSign) {
        LeaseInterestListVO result = new LeaseInterestListVO();
        result.setShowSign(showSign);
        result.setList(leaseInterestService.queryInterestList(leaseId));
        return AjaxJson.success(result);
    }
}
