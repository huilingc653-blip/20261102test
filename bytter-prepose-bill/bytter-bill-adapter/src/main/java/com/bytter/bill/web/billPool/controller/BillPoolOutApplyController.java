package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolOutApplyImpService;
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
 * <b>功能：</b>票据出池申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
@Controller
@RequestMapping("/billPool/outApply")
public class BillPoolOutApplyController extends BaseController {
    @Autowired
    BillPoolOutApplyImpService billPoolOutApplyImpService;
    /**
     * <b>功能：</b>获取待发送票据出池申请数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryOutApplyByBifCode")
    @ResponseBody
    public AjaxJson queryOutApplyByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolOutApplyImpService.queryOutApplyByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取等待结果票据出池数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryOutApplyResultByBifCode")
    @ResponseBody
    public AjaxJson queryOutApplyResultByBifCode(String bifCode, String reCode) {
        List<PoolBillVoucherVO> voList = billPoolOutApplyImpService.queryOutApplyResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改票据出池的指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updatePoolOutApply")
    @ResponseBody
    public AjaxJson updatePoolOutApply(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolOutApplyImpService.updatePoolOutApply(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取待发送票据质押解除出池数据<br>
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
        List<PoolBillVoucherVO> voList = billPoolOutApplyImpService.queryRemovePledgeByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取等待结果票据质押解除出池数据<br>
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
        List<PoolBillVoucherVO> voList = billPoolOutApplyImpService.queryRemovePledgeResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改票据质押解除出池的指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updatePoolRemovePledge")
    @ResponseBody
    public AjaxJson updatePoolRemovePledge(@RequestBody PoolBillVoucherVO paramsVo) {
        billPoolOutApplyImpService.updatePoolRemovePledge(paramsVo);
        return success();
    }
}
