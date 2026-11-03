package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.dto.AbPoolUpdateQueryExcDTO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolQueryImpService;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolBailVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaApplyVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitInfoVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolQueryExcVO;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>票据池查询服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
@Api(value = "/v1-preposebill/billPool/query", tags = "票据池查询")
@RestController("v1BillPoolQueryController")
@RequestMapping("/v1-preposebill/billPool/query")
public class BillPoolQueryController extends BaseController {

    @Autowired
    private BillPoolQueryImpService billPoolQueryImpService;

    /**
     * <b>功能：</b>获取对于银行类型需要的票据池<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池信息
     */
    @ApiOperation("获取对于银行类型需要的票据池")
    @GetMapping("/queryPoolByBifCode")
    public AjaxJson<List<PoolBaseVO>> queryPoolByBifCode(String bifCode) {
        List<PoolBaseVO> voList = billPoolQueryImpService.queryPoolByBifCode(bifCode);
        return success(voList);
    }

    /**
     * <b>功能：</b>获取指定银行类型的出入池登记薄查询待返回票据池和账号<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池信息及发起人账号信息
     */
    @ApiOperation("获取指定银行类型的出入池登记薄查询待返回票据池和账号")
    @GetMapping("/queryPoolAndReqAccByBifCode")
    public AjaxJson<List<PoolBaseVO>> queryPoolAndReqAccByBifCode(String bifCode) {
        List<PoolBaseVO> voList = billPoolQueryImpService.queryPoolAndReqAccByBifCode(bifCode);
        return success(voList);
    }

    /**
     * <b>功能：</b>获取对于银行类型需要的票据账户<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池信息
     */
    @ApiOperation("获取对于银行类型需要的票据账户")
    @GetMapping("/queryBillAccByBifCode")
    public AjaxJson<List<VAccListVo>> queryBillAccByBifCode(String bifCode) {
        List<VAccListVo> voList = billPoolQueryImpService.queryBillAccByBifCode(bifCode);
        return success(voList);
    }

    /**
     * <b>功能：</b>获取对应银行类型需要的票据池额度查询指令<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @ApiOperation("获取对应银行类型需要的票据池额度查询指令")
    @GetMapping("/queryPoolLimitExcByBifCode")
    public AjaxJson<List<PoolQueryExcVO>> queryPoolLimitExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolLimitExcByBifCode(bifCode);
        return success(voList);
    }

    /**
     * <b>功能：对获取的票据池和额度进行入库（无则新增有则修改）</b><br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("对获取的票据池和额度进行入库")
    @PostMapping("/savePoolLimitInfo")
    public AjaxJson savePoolLimitInfo(@RequestBody List<PoolLimitInfoVO> paramsVo) {
        billPoolQueryImpService.savePoolLimitInfo(paramsVo);
        return success();
    }

    /**
     * <b>功能：对获取的票据池额度明细进行入库）</b><br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("对获取的票据池额度明细进行入库")
    @PostMapping("/savePoolLimitDtlInfo")
    public AjaxJson savePoolLimitDtlInfo(@RequestBody List<PoolLimitVO> paramsVo) {
        billPoolQueryImpService.savePoolLimitDtlInfo(paramsVo);
        return success();
    }

    /**
     * <b>功能：</b>获取对应银行类型需要的票据池额度交易明细查询指令<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @ApiOperation("获取对应银行类型需要的票据池额度交易明细查询指令")
    @GetMapping("/queryPoolLimitDtlExcByBifCode")
    public AjaxJson<List<PoolQueryExcVO>> queryPoolLimitDtlExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolLimitDtlExcByBifCode(bifCode);
        return success(voList);
    }

    /**
     * <b>功能：</b>获取对应银行类型需要的票据池出入池查询指令<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @ApiOperation("获取对应银行类型需要的票据池出入池查询指令")
    @GetMapping("/queryPoolInOutExcByBifCode")
    public AjaxJson<List<PoolQueryExcVO>> queryPoolInOutExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolInOutExcByBifCode(bifCode);
        return success(voList);
    }

    /**
     * <b>功能：对获取的出入池账簿进行入库（先根据交易日期和出入池方式进行删除然后再新增）</b><br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("对获取的出入池账簿进行入库")
    @PostMapping("/savePoolInOutInfo")
    public AjaxJson savePoolInOutInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolInOutInfo(paramsVo);
        return success();
    }

    /**
     * <b>功能：对获取的质押托管转换进行入库（先根据交易日期进行删除然后再新增）</b><br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("对获取的质押托管转换进行入库")
    @PostMapping("/savePoolPdtstInfo")
    public AjaxJson savePoolPdtstInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolPdtstInfo(paramsVo);
        return success();
    }

    /**
     * <b>功能：对获取的票据池出票明细进行入库（先根据交易日期进行删除然后再新增）</b><br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("对获取的票据池出票明细进行入库")
    @PostMapping("/savePoolEmitInfo")
    public AjaxJson savePoolEmitInfo(@RequestBody List<PoolEmitFinaApplyVO> paramsVo) {
        billPoolQueryImpService.savePoolEmitInfo(paramsVo);
        return success();
    }

    /**
     * <b>功能：</b>获取对应银行类型和查询类型需要的票据池查询指令<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @ApiOperation("获取对应银行类型和查询类型需要的票据池查询指令")
    @GetMapping("/queryPoolExcByBifCodeAll")
    public AjaxJson<List<PoolQueryExcVO>> queryPoolExcByBifCodeAll(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolExcByBifCode(bifCode);
        return success(voList);
    }

    /**
     * <b>功能：</b>获取对应银行类型和查询类型需要的票据池查询指令<br>
     *
     * @param bifCode   银行接口代码
     * @param queryType 查询类型代码
     * @return 票据池查询指令信息
     */
    @ApiOperation("获取对应银行类型和查询类型需要的票据池查询指令")
    @GetMapping("/queryPoolExcByBifCode")
    public AjaxJson<List<PoolQueryExcVO>> queryPoolExcByBifCode(String bifCode, String queryType) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolExcByBifCode(bifCode, queryType);
        return success(voList);
    }

    /**
     * <b>功能：</b>获取对应银行类型需要的票据池保证金明细查询指令<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @ApiOperation("获取对应银行类型需要的票据池保证金明细查询指令")
    @GetMapping("/queryPoolBailExcByBifCode")
    public AjaxJson<List<PoolQueryExcVO>> queryPoolBailExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolBailExcByBifCode(bifCode);
        return success(voList);
    }

    /**
     * <b>功能：对获取的保证金交易明细进行入库（先根据申请日期进行删除然后再新增）</b><br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("对获取的保证金交易明细进行入库")
    @PostMapping("/savePoolBailInfo")
    public AjaxJson savePoolBailInfo(@RequestBody List<PoolBailVO> paramsVo) {
        billPoolQueryImpService.savePoolBailInfo(paramsVo);
        return success();
    }

    /**
     * <b>功能：</b>获取对应银行类型需要的票据池在池查询指令<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据池查询指令信息
     */
    @ApiOperation("获取对应银行类型需要的票据池在池查询指令")
    @GetMapping("/queryPoolHoldExcByBifCode")
    public AjaxJson<List<PoolQueryExcVO>> queryPoolHoldExcByBifCode(String bifCode) {
        List<PoolQueryExcVO> voList = billPoolQueryImpService.queryPoolHoldExcByBifCode(bifCode);
        return success(voList);
    }

    /**
     * <b>功能：对获取的在池交易明细进行入库（先根据申请日期进行删除然后再新增）</b><br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("对获取的在池交易明细进行入库")
    @PostMapping("/savePoolHoldInfo")
    public AjaxJson savePoolHoldInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolHoldInfo(paramsVo);
        return success();
    }

    /**
     * <b>功能：对获取的提示付款账簿进行入库（先根据交易日期和电纸票标识进行删除然后再新增）</b><br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("对获取的提示付款账簿进行入库")
    @PostMapping("/savePoolIsPayInfo")
    public AjaxJson savePoolIsPayInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolIsPayInfo(paramsVo);
        return success();
    }

    /**
     * <b>功能：对获取的出票融资申请账簿进行入库（先根据交易日期和电纸票标识进行删除然后再新增）</b><br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("对获取的出票融资申请账簿进行入库")
    @PostMapping("/savePoolFinaApplyInfo")
    public AjaxJson savePoolFinaApplyInfo(@RequestBody List<PoolBillVoucherVO> paramsVo) {
        billPoolQueryImpService.savePoolFinaApplyInfo(paramsVo);
        return success();
    }

    /**
     * <b>功能：</b>根据查询业务ID修改票据池查询指令状态<br>
     *
     * @param dto 指令修改参数
     * @return 修改成功还是失败 true成功false失败
     */
    @ApiOperation("根据查询业务ID修改票据池查询指令状态")
    @PostMapping("/updatePoolQueryExc")
    public AjaxJson<Boolean> updatePoolQueryExc(@RequestBody AbPoolUpdateQueryExcDTO dto) {
        boolean flag = billPoolQueryImpService.updatePoolQueryExc(dto.getBusId(), dto.getReCode(), dto.getReMsg());
        return success(flag);
    }

    /**
     * <b>功能：</b>保存保证金账户信息<br>
     *
     * @param paramsVo 数据的信息
     */
    @ApiOperation("保存保证金账户信息")
    @PostMapping("/saveAbPoolBailInfo")
    public AjaxJson saveAbPoolBailInfo(@RequestBody List<PoolBailVO> paramsVo) {
        billPoolQueryImpService.saveAbPoolBailInfo(paramsVo);
        return success();
    }
}
