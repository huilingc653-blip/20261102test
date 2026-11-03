package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaApplyVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaConVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolEmitFinaApplyImpService;
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
 * <b>功能：</b>出票融资申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-12 <br>
 */
@Controller
@RequestMapping("/billPool/bail")
public class BillPoolEmitFinaApplyController extends BaseController {
    @Autowired
    BillPoolEmitFinaApplyImpService billPoolBankExcImpService;
    /**
     * <b>功能：</b>获取待发送融资申请数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryEmitFinaByBifCode")
    @ResponseBody
    public AjaxJson queryEmitFinaByBifCode(String bifCode, String reCode) {
        List<PoolEmitFinaApplyVO> voList = billPoolBankExcImpService.queryEmitFinaByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取等待结果融资申请数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryEmitFinaResultByBifCode")
    @ResponseBody
    public AjaxJson queryEmitFinaResultByBifCode(String bifCode, String reCode) {
        List<PoolEmitFinaApplyVO> voList = billPoolBankExcImpService.queryEmitFinaResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改融资申请的指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updatePoolEmitFinaApply")
    @ResponseBody
    public AjaxJson updatePoolEmitFinaApply(@RequestBody PoolEmitFinaApplyVO paramsVo) {
        billPoolBankExcImpService.updatePoolEmitFinaApply(paramsVo);
        return success();
    }
    /**
     * <b>功能：保存出票融资的合同</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param conVo 合同要素
     * @return
     */
    @PostMapping("/savePoolEmitFinaCon")
    @ResponseBody
    public AjaxJson savePoolEmitFinaCon(@RequestBody PoolEmitFinaConVO conVo) {
        billPoolBankExcImpService.savePoolEmitFinaCon(conVo);
        return success();
    }
    /**
     * <b>功能：</b>获取待发送重新生成出票融资合同数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryResetPoolEmitFinaConByBifCode")
    @ResponseBody
    public AjaxJson queryResetPoolEmitFinaConByBifCode(String bifCode, String reCode) {
        List<PoolEmitFinaConVO> voList = billPoolBankExcImpService.queryResetPoolEmitFinaConByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取重新生成出票融资合同等待结果数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryResetPoolEmitFinaConResultByBifCode")
    @ResponseBody
    public AjaxJson queryResetPoolEmitFinaConResultByBifCode(String bifCode, String reCode) {
        List<PoolEmitFinaConVO> voList = billPoolBankExcImpService.queryResetPoolEmitFinaConResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改重新生成出票融资合同数据指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updateResetPoolEmitFinaCon")
    @ResponseBody
    public AjaxJson updateResetPoolEmitFinaCon(@RequestBody PoolEmitFinaConVO paramsVo) {
        billPoolBankExcImpService.updateResetPoolEmitFinaCon(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取待发送签收确认出票融资合同数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/querySignOkPoolEmitFinaConByBifCode")
    @ResponseBody
    public AjaxJson querySignOkPoolEmitFinaConByBifCode(String bifCode, String reCode) {
        List<PoolEmitFinaConVO> voList = billPoolBankExcImpService.querySignOkPoolEmitFinaConByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取签收确认出票融资合同等待结果数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/querySignOkPoolEmitFinaConResultByBifCode")
    @ResponseBody
    public AjaxJson querySignOkPoolEmitFinaConResultByBifCode(String bifCode, String reCode) {
        List<PoolEmitFinaConVO> voList = billPoolBankExcImpService.querySignOkPoolEmitFinaConResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改签收确认出票融资合同数据指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-12 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updateSignOkPoolEmitFinaCon")
    @ResponseBody
    public AjaxJson updateSignOkPoolEmitFinaCon(@RequestBody PoolEmitFinaConVO paramsVo) {
        billPoolBankExcImpService.updateSignOkPoolEmitFinaCon(paramsVo);
        return success();
    }
}
