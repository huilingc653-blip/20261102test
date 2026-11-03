package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolLimitApplyImpService;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 票据池额度申请服务接口
 */
@Api(value = "/v1-preposebill/billPool/limitApply", tags = "票据池额度申请服务接口")
@RestController("v1BillPoolLimitApplyController")
@RequestMapping("/v1-preposebill/billPool/limitApply")
public class BillPoolLimitApplyController extends BaseController {

    @Autowired
    private BillPoolLimitApplyImpService billPoolLimitApplyImpService;

    /**
     * 获取待发送额度申请数据
     */
    @ApiOperation("获取待发送额度申请数据")
    @GetMapping("/queryLimitApplyByBifCode")
    public AjaxJson<List<PoolLimitVO>> queryLimitApplyByBifCode(String bifCode, String reCode) {
        List<PoolLimitVO> voList = billPoolLimitApplyImpService.queryLimitApplyByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 获取等待结果额度数据
     */
    @ApiOperation("获取等待结果额度数据")
    @GetMapping("/queryLimitApplyResultByBifCode")
    public AjaxJson<List<PoolLimitVO>> queryLimitApplyResultByBifCode(String bifCode, String reCode) {
        List<PoolLimitVO> voList = billPoolLimitApplyImpService.queryLimitApplyResultByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 修改额度的指令状态
     */
    @ApiOperation("修改额度的指令状态")
    @PostMapping("/updatePoolLimitApply")
    public AjaxJson updatePoolLimitApply(@RequestBody PoolLimitVO paramsVo) {
        billPoolLimitApplyImpService.updatePoolLimitApply(paramsVo);
        return success();
    }

    /**
     * 获取需要查询复核数据的票据池
     */
    @ApiOperation("获取需要查询复核数据的票据池")
    @GetMapping("/queryCheckPoolByBifCode")
    public AjaxJson<List<PoolBaseVO>> queryCheckPoolByBifCode(String bifCode) {
        List<PoolBaseVO> voList = billPoolLimitApplyImpService.queryCheckPoolByBifCode(bifCode);
        return success(voList);
    }

    /**
     * 对获取的待复核数据进行入库
     */
    @ApiOperation("对获取的待复核数据进行入库")
    @PostMapping("/savePoolLimitApplyCheck")
    public AjaxJson savePoolLimitApplyCheck(@RequestBody List<PoolLimitVO> paramsVo) {
        billPoolLimitApplyImpService.savePoolLimitApplyCheck(paramsVo);
        return success();
    }

    /**
     * 获取待发送额度申请复核数据
     */
    @ApiOperation("获取待发送额度申请复核数据")
    @GetMapping("/queryLimitApplyCheckByBifCode")
    public AjaxJson<List<PoolLimitVO>> queryLimitApplyCheckByBifCode(String bifCode, String reCode) {
        List<PoolLimitVO> voList = billPoolLimitApplyImpService.queryLimitApplyCheckByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 获取等待结果额度申请复核数据
     */
    @ApiOperation("获取等待结果额度申请复核数据")
    @GetMapping("/queryLimitApplyCheckResultByBifCode")
    public AjaxJson<List<PoolLimitVO>> queryLimitApplyCheckResultByBifCode(String bifCode, String reCode) {
        List<PoolLimitVO> voList = billPoolLimitApplyImpService.queryLimitApplyCheckResultByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 修改额度申请复核的指令状态
     */
    @ApiOperation("修改额度申请复核的指令状态")
    @PostMapping("/updatePoolLimitApplyCheck")
    public AjaxJson updatePoolLimitApplyCheck(@RequestBody PoolLimitVO paramsVo) {
        billPoolLimitApplyImpService.updatePoolLimitApplyCheck(paramsVo);
        return success();
    }
}
