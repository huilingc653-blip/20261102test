package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolLimitApplyImpService;
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
 * <b>功能：</b>额度申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-17 <br>
 */
@Controller
@RequestMapping("/billPool/limitApply")
public class BillPoolLimitApplyController extends BaseController {
    @Autowired
    BillPoolLimitApplyImpService billPoolLimitApplyImpService;
    /**
     * <b>功能：</b>获取待发送额度申请数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-17 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryLimitApplyByBifCode")
    @ResponseBody
    public AjaxJson queryLimitApplyByBifCode(String bifCode, String reCode) {
        List<PoolLimitVO> voList = billPoolLimitApplyImpService.queryLimitApplyByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取等待结果额度数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-17 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryLimitApplyResultByBifCode")
    @ResponseBody
    public AjaxJson queryLimitApplyResultByBifCode(String bifCode, String reCode) {
        List<PoolLimitVO> voList = billPoolLimitApplyImpService.queryLimitApplyResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改额度的指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-17 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updatePoolLimitApply")
    @ResponseBody
    public AjaxJson updatePoolLimitApply(@RequestBody PoolLimitVO paramsVo) {
        billPoolLimitApplyImpService.updatePoolLimitApply(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取需要查询复核数据的票据池<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-18 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池信息
     */
    @PostMapping("/queryCheckPoolByBifCode")
    @ResponseBody
    public AjaxJson queryCheckPoolByBifCode(String bifCode) {
        List<PoolBaseVO> voList = billPoolLimitApplyImpService.queryCheckPoolByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：对获取的待复核数据进行入库（业务中数据不替换，有失败数据和已完成数据进行替换）</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-17 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/savePoolLimitApplyCheck")
    @ResponseBody
    public AjaxJson savePoolLimitApplyCheck(@RequestBody List<PoolLimitVO> paramsVo) {
        billPoolLimitApplyImpService.savePoolLimitApplyCheck(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取待发送额度申请复核数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-17 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryLimitApplyCheckByBifCode")
    @ResponseBody
    public AjaxJson queryLimitApplyCheckByBifCode(String bifCode, String reCode) {
        List<PoolLimitVO> voList = billPoolLimitApplyImpService.queryLimitApplyCheckByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取等待结果额度申请复核数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-17 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    @PostMapping("/queryLimitApplyCheckResultByBifCode")
    @ResponseBody
    public AjaxJson queryLimitApplyCheckResultByBifCode(String bifCode, String reCode) {
        List<PoolLimitVO> voList = billPoolLimitApplyImpService.queryLimitApplyCheckResultByBifCode(bifCode, reCode);
        return success(voList);
    }
    /**
     * <b>功能：修改额度申请复核的指令状态</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-17 <br>
     *
     * @param paramsVo 修改数据的参数
     * @return
     */
    @PostMapping("/updatePoolLimitApplyCheck")
    @ResponseBody
    public AjaxJson updatePoolLimitApplyCheck(@RequestBody PoolLimitVO paramsVo) {
        billPoolLimitApplyImpService.updatePoolLimitApplyCheck(paramsVo);
        return success();
    }
}
