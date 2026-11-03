package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolPdTstApplyImpService;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
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
 * @Description: 票据质押托管转换申请服务接口
 */
@Api(value = "/v1-preposebill/billPool/pdTstApply", tags = "票据质押托管转换申请服务接口")
@RestController("v1BillPoolPdTstApplyController")
@RequestMapping("/v1-preposebill/billPool/pdTstApply")
public class BillPoolPdTstApplyController extends BaseController {

    @Autowired
    private BillPoolPdTstApplyImpService billPoolPdTstApplyImpService;

    /**
     * 获取待发送票据质押托管转换申请数据
     */
    @ApiOperation("获取待发送票据质押托管转换申请数据")
    @GetMapping("/queryPdTstApplyByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryPdTstApplyByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstApplyByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 获取等待结果票据质押托管转换数据
     */
    @ApiOperation("获取等待结果票据质押托管转换数据")
    @GetMapping("/queryPdTstApplyResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryPdTstApplyResultByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstApplyResultByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 修改票据质押托管转换的指令状态
     */
    @ApiOperation("修改票据质押托管转换的指令状态")
    @PostMapping("/updatePoolPdTstApply")
    public AjaxJson updatePoolPdTstApply(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolPdTstApplyImpService.updatePoolPdTstApply(paramsVo);
        return success();
    }

    /**
     * 获取待发送质押托管转换质押申请数据
     */
    @ApiOperation("获取待发送质押托管转换质押申请数据")
    @GetMapping("/queryPdTstPledgeByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryPdTstPledgeByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstPledgeByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 获取等待结果质押托管转换质押申请数据
     */
    @ApiOperation("获取等待结果质押托管转换质押申请数据")
    @GetMapping("/queryPdTstPledgeResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryPdTstPledgeResultByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstPledgeResultByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 修改票据质押托管转换质押申请的指令状态
     */
    @ApiOperation("修改票据质押托管转换质押申请的指令状态")
    @PostMapping("/updatePoolPdTstPledge")
    public AjaxJson updatePoolPdTstPledge(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolPdTstApplyImpService.updatePoolPdTstPledge(paramsVo);
        return success();
    }

    /**
     * 获取待发送质押托管转换票据质押解除数据
     */
    @ApiOperation("获取待发送质押托管转换票据质押解除数据")
    @GetMapping("/queryRemovePledgeByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryRemovePledgeByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryRemovePledgeByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 获取等待结果质押托管转换票据质押解除数据
     */
    @ApiOperation("获取等待结果质押托管转换票据质押解除数据")
    @GetMapping("/queryRemovePledgeResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryRemovePledgeResultByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryRemovePledgeResultByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 修改票据质押托管转换质押解除的指令状态
     */
    @ApiOperation("修改票据质押托管转换质押解除的指令状态")
    @PostMapping("/updatePoolRemovePledge")
    public AjaxJson updatePoolRemovePledge(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolPdTstApplyImpService.updatePoolRemovePledge(paramsVo);
        return success();
    }

    /**
     * 获取质押托管转换待确认失败数据
     */
    @ApiOperation("获取质押托管转换待确认失败数据")
    @GetMapping("/queryPdTstPledgeConfirmFailByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryPdTstPledgeConfirmFailByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstPledgeConfirmFailByBifCode(bifCode, reCode);
        return success(voList);
    }

    /**
     * 获取质押托管转换待确认等待结果数据
     */
    @ApiOperation("获取质押托管转换待确认等待结果数据")
    @GetMapping("/queryPdTstPledgeConfirmResultByBifCode")
    public AjaxJson<List<PoolBillVoucherVO>> queryPdTstPledgeConfirmResultByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstPledgeConfirmResultByBifCode(bifCode, reCode);
        return success(voList);
    }
}
