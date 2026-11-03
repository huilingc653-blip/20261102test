package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.dto.AbPoolPledgeeOptDTO;
import com.bytter.bill.domain.billPool.service.AbPoolCheckService;
import com.bytter.bill.domain.billPool.service.AbPoolPledgeeService;
import com.bytter.bill.domain.billPool.vo.AbPoolPledgeeDtlVO;
import com.bytter.bill.domain.billPool.vo.AbPoolPledgeeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: 质押人设置Controller
 * @Description: 质押人设置
 */
@Api(value = "/v1-preposebill/billPool/pledgee", tags = "质押人设置")
@RestController("v1AbPoolPledgeeController")
@RequestMapping("/v1-preposebill/billPool/pledgee")
public class AbPoolPledgeeController extends BaseController {

    @Autowired
    private AbPoolPledgeeService abPoolPledgeeService;

    @Autowired
    private AbPoolCheckService abPoolCheckService;

    /**
     * 质押人 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询质押人信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询质押人列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolPledgeeService.getDataTablesUnion(input));
    }

    /**
     * 质押人详情(禁止编辑)页面数据
     */
    @ApiOperation("质押人详情(禁止编辑)页面数据")
    @GetMapping("/permitDtl/{id}")
    public AjaxJson<AbPoolPledgeeDtlVO> retreatPage(@PathVariable("id") String id) {
        AbPoolPledgeeVO abPoolPledgeeVO = abPoolPledgeeService.getAbPoolPledgeeVOByBpId(id);
        if (null != abPoolPledgeeVO
                && StringUtils.isEmpty(abPoolPledgeeVO.getBankAcc())
                && !StringUtils.isEmpty(abPoolPledgeeVO.getBpType())
                && !abPoolPledgeeVO.getBpType().equals(AbPoolConstant.AB_BILL_POOL_BP_TYPE_1)) {
            abPoolPledgeeVO.setBankAcc(AbPoolConstant.AB_POLL_VALID_SIGN_0);
        }
        AbPoolPledgeeDtlVO result = new AbPoolPledgeeDtlVO();
        result.setAbPoolPledgeeVO(abPoolPledgeeVO);
        result.setFlag(false);
        return success(result);
    }

    /**
     * 质押人详情(可编辑)页面数据
     */
    @ApiOperation("质押人详情(可编辑)页面数据")
    @GetMapping("/view/{id}")
    public AjaxJson<AbPoolPledgeeDtlVO> view(@PathVariable("id") String id) {
        AbPoolPledgeeVO abPoolPledgeeVO = abPoolPledgeeService.getAbPoolPledgeeVOByBpId(id);
        if (null != abPoolPledgeeVO
                && StringUtils.isEmpty(abPoolPledgeeVO.getBankAcc())
                && !StringUtils.isEmpty(abPoolPledgeeVO.getBpType())
                && !abPoolPledgeeVO.getBpType().equals(AbPoolConstant.AB_BILL_POOL_BP_TYPE_1)) {
            abPoolPledgeeVO.setBankAcc(AbPoolConstant.AB_POLL_VALID_SIGN_0);
        }
        AbPoolPledgeeDtlVO result = new AbPoolPledgeeDtlVO();
        result.setAbPoolPledgeeVO(abPoolPledgeeVO);
        result.setFlag(true);
        return success(result);
    }

    /**
     * 某一行操作启用/禁用
     */
    @ApiOperation("质押人启用/禁用")
    @PostMapping("/opt")
    public AjaxJson siginOpt(@RequestBody AbPoolPledgeeOptDTO dto) {
        //禁用判断是否有在途
        if (String.valueOf(dto.getOpt()).equals(AbPoolConstant.AB_POLL_VALID_SIGN_0)) {
            abPoolCheckService.poolPledgeeCheckQuery(dto.getId());
        }
        abPoolPledgeeService.updateDispose(dto.getId(), dto.getOpt(), getCurrentUser());
        return success();
    }

    /**
     * 修改质押人
     */
    @ApiOperation("修改质押人")
    @PostMapping("/updateAbPoolPledgee")
    public AjaxJson updateAbPoolPledgee(@RequestBody AbPoolPledgeeVO abPoolPledgeeVO) {
        return abPoolPledgeeService.settingPoolPledgeeVO(abPoolPledgeeVO, getCurrentUser());
    }
}
