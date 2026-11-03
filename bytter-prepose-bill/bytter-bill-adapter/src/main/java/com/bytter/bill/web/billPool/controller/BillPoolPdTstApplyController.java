package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolPdTstApplyImpService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据质押托管转换申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
@Controller
@RequestMapping("/billPool/pdTstApply")
public class BillPoolPdTstApplyController extends BaseController {
    @Autowired
    BillPoolPdTstApplyImpService billPoolPdTstApplyImpService;
    /**
     * <b>功能：</b>获取待发送票据质押托管转换申请数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryPdTstApplyByBifCode")
    @ResponseBody
    public AjaxJson queryPdTstApplyByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstApplyByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取等待结果票据质押托管转换数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryPdTstApplyResultByBifCode")
    @ResponseBody
    public AjaxJson queryPdTstApplyResultByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstApplyResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改票据质押托管转换的指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updatePoolPdTstApply")
    @ResponseBody
    public AjaxJson updatePoolPdTstApply(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolPdTstApplyImpService.updatePoolPdTstApply(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取待发送质押托管转换质押申请数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryPdTstPledgeByBifCode")
    @ResponseBody
    public AjaxJson queryPdTstPledgeByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstPledgeByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取等待结果质押托管转换质押申请数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryPdTstPledgeResultByBifCode")
    @ResponseBody
    public AjaxJson queryPdTstPledgeResultByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstPledgeResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改票据质押托管转换质押申请的指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updatePoolPdTstPledge")
    @ResponseBody
    public AjaxJson updatePoolPdTstPledge(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolPdTstApplyImpService.updatePoolPdTstPledge(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取待发送质押托管转换票据质押解除数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryRemovePledgeByBifCode")
    @ResponseBody
    public AjaxJson queryRemovePledgeByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryRemovePledgeByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取等待结果质押托管转换票据质押解除数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryRemovePledgeResultByBifCode")
    @ResponseBody
    public AjaxJson queryRemovePledgeResultByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryRemovePledgeResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改票据质押托管转换质押解除的指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updatePoolRemovePledge")
    @ResponseBody
    public AjaxJson updatePoolRemovePledge(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolPdTstApplyImpService.updatePoolRemovePledge(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取质押托管转换待确认失败数据<br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2022-11-16 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryPdTstPledgeConfirmFailByBifCode")
    @ResponseBody
    public AjaxJson queryPdTstPledgeConfirmFailByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstPledgeConfirmFailByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取质押托管转换待确认等待结果数据<br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2022-11-16 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryPdTstPledgeConfirmResultByBifCode")
    @ResponseBody
    public AjaxJson queryPdTstPledgeConfirmResultByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolPdTstApplyImpService.queryPdTstPledgeConfirmResultByBifCode(bifCode, reCode);
        return success(voList);
    }
}
