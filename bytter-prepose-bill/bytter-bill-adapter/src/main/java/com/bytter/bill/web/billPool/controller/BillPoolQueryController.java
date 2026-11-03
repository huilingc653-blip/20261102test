package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.bill.domain.billbank.pool.vo.*;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolQueryImpService;
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
 * <b>功能：</b>票据池查询服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
@Controller
@RequestMapping("/billPool/query")
public class BillPoolQueryController extends BaseController {
    @Autowired
    BillPoolQueryImpService billPoolQueryImpService;
    /**
     * <b>功能：</b>获取对于银行类型需要的票据池<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-19 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池信息
     */
    @PostMapping("/queryPoolByBifCode")
    @ResponseBody
    public AjaxJson queryPoolByBifCode(String bifCode) {
        List<PoolBaseVO> voList = billPoolQueryImpService.queryPoolByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取指定银行类型的出入池登记薄查询待返回票据池和账号<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2022-03-16 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池信息及发起人账号信息
     */
    @PostMapping("/queryPoolAndReqAccByBifCode")
    @ResponseBody
    public AjaxJson queryPoolAndReqAccByBifCode(String bifCode) {
        List<PoolBaseVO> voList = billPoolQueryImpService.queryPoolAndReqAccByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取对于银行类型需要的票据账户<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-19 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池信息
     */
    @PostMapping("/queryBillAccByBifCode")
    @ResponseBody
    public AjaxJson queryBillAccByBifCode(String bifCode) {
        List<VAccListVo> voList = billPoolQueryImpService.queryBillAccByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取对应银行类型需要的票据池额度查询指令<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-11-12 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @PostMapping("/queryPoolLimitExcByBifCode")
    @ResponseBody
    public AjaxJson queryPoolLimitExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolLimitExcByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：对获取的票据池和额度进行入库（无则新增有则修改）</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-19 <br>
     *
     * @param paramsVo 数据的信息
     * @return
     */
    @PostMapping("/savePoolLimitInfo")
    @ResponseBody
    public AjaxJson savePoolLimitInfo(@RequestBody List<PoolLimitInfoVO> paramsVo) {
        billPoolQueryImpService.savePoolLimitInfo(paramsVo);
        return success();
    }
    /**
     * <b>功能：对获取的票据池额度明细进行入库）</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2021-03-04 <br>
     *
     * @param paramsVo 数据的信息
     * @return
     */
    @PostMapping("/savePoolLimitDtlInfo")
    @ResponseBody
    public AjaxJson savePoolLimitDtlInfo(@RequestBody List<PoolLimitVO> paramsVo) {
        billPoolQueryImpService.savePoolLimitDtlInfo(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取对应银行类型需要的票据池额度交易明细查询指令<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-11-12 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @PostMapping("/queryPoolLimitDtlExcByBifCode")
    @ResponseBody
    public AjaxJson queryPoolLimitDtlExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolLimitDtlExcByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取对应银行类型需要的票据池出入池查询指令<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-11-12 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @PostMapping("/queryPoolInOutExcByBifCode")
    @ResponseBody
    public AjaxJson queryPoolInOutExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolInOutExcByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：对获取的出入池账簿进行入库（先根据交易日期和出入池方式进行删除然后再新增）</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-19 <br>
     *
     * @param paramsVo 数据的信息
     * @return
     */
    @PostMapping("/savePoolInOutInfo")
    @ResponseBody
    public AjaxJson savePoolInOutInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolInOutInfo(paramsVo);
        return success();
    }
    /**
     * <b>功能：对获取的质押托管转换进行入库（先根据交易日期进行删除然后再新增）</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-19 <br>
     *
     * @param paramsVo 数据的信息
     * @return
     */
    @PostMapping("/savePoolPdtstInfo")
    @ResponseBody
    public AjaxJson savePoolPdtstInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolPdtstInfo(paramsVo);
        return success();
    }
    /**
     * <b>功能：对获取的票据池出票明细进行入库（先根据交易日期进行删除然后再新增）</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-19 <br>
     *
     * @param paramsVo 数据的信息
     * @return
     */
    @PostMapping("/savePoolEmitInfo")
    @ResponseBody
    public AjaxJson savePoolEmitInfo(@RequestBody List<PoolEmitFinaApplyVO> paramsVo) {
        billPoolQueryImpService.savePoolEmitInfo(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取对应银行类型和查询类型需要的票据池查询指令<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-11-12 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @PostMapping("/queryPoolExcByBifCodeAll")
    @ResponseBody
    public AjaxJson queryPoolExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolExcByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取对应银行类型和查询类型需要的票据池查询指令<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-11-12 <br>
     *
     * @param bifCode   银行接口代码
     * @param queryType 查询类型代码
     * @return 票据池查询指令信息
     */
    @PostMapping("/queryPoolExcByBifCode")
    @ResponseBody
    public AjaxJson queryPoolExcByBifCode(String bifCode, String queryType) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolExcByBifCode(bifCode, queryType);
        return success(voList);
    }
    /**
     * <b>功能：</b>获取对应银行类型需要的票据池保证金明细查询指令<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-11-12 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @PostMapping("/queryPoolBailExcByBifCode")
    @ResponseBody
    public AjaxJson queryPoolBailExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolBailExcByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：对获取的保证金交易明细进行入库（先根据申请日期进行删除然后再新增）</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-19 <br>
     *
     * @param paramsVo 数据的信息
     * @return
     */
    @PostMapping("/savePoolBailInfo")
    @ResponseBody
    public AjaxJson savePoolBailInfo(@RequestBody List<PoolBailVO> paramsVo) {
        billPoolQueryImpService.savePoolBailInfo(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>获取对应银行类型需要的票据池在池查询指令<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-11-12 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @PostMapping("/queryPoolHoldExcByBifCode")
    @ResponseBody
    public AjaxJson queryPoolHoldExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolHoldExcByBifCode(bifCode);
        return success(voList);
    }
    /**
     * <b>功能：对获取的在池交易明细进行入库（先根据申请日期进行删除然后再新增）</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-19 <br>
     *
     * @param paramsVo 数据的信息
     * @return
     */
    @PostMapping("/savePoolHoldInfo")
    @ResponseBody
    public AjaxJson savePoolHoldInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolHoldInfo(paramsVo);
        return success();
    }
    /**
     * <b>功能：对获取的提示付款账簿进行入库（先根据交易日期和电纸票标识进行删除然后再新增）</b><br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-02-19 <br>
     *
     * @param paramsVo 数据的信息
     * @return
     */
    @PostMapping("/savePoolIsPayInfo")
    @ResponseBody
    public AjaxJson savePoolIsPayInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolIsPayInfo(paramsVo);
        return success();
    }
    /**
     * <b>功能：对获取的出票融资申请账簿进行入库（先根据交易日期和电纸票标识进行删除然后再新增）</b><br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2020-04-14 <br>
     *
     * @param paramsVo 数据的信息
     * @return
     */
    @PostMapping("/savePoolFinaApplyInfo")
    @ResponseBody
    public AjaxJson savePoolFinaApplyInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolFinaApplyInfo(paramsVo);
        return success();
    }
    /**
     * <b>功能：</b>根据查询业务ID修改票据池查询指令状态<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2020-11-12 <br>
     *
     * @param busId  指令id
     * @param reCode 银行返回代码
     * @param reMsg  银行返回信息
     * @return 修改成功还是失败 true成功false失败
     */
    @PostMapping("/updatePoolQueryExc")
    @ResponseBody
    public AjaxJson updatePoolQueryExc(String busId, String reCode, String reMsg) {
        boolean flag = billPoolQueryImpService.updatePoolQueryExc(busId, reCode, reMsg);
        return success(flag);
    }
    /**
     * <b>功能：</b>保存保证金账户信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2022-8-4 <br>
     *
     * @return 修改成功还是失败 true成功false失败
     */
    @PostMapping("/saveAbPoolBailInfo")
    @ResponseBody
    public AjaxJson saveAbPoolBailInfo(@RequestBody List<PoolBailVO> paramsVo) {
        billPoolQueryImpService.saveAbPoolBailInfo(paramsVo);
        return success();
    }
}
