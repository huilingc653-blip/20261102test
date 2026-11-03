package com.bytter.system.saas.controller.web;

import com.bytter.basic.entity.FcInstrateAdjust;
import com.bytter.basic.service.FcInstrateAdjustService;
import com.bytter.basic.vo.FcInstrateAdjustDetailVO;
import com.bytter.basic.vo.FcInstrateAdjustSimpleVO;
import com.bytter.basic.vo.FcInstrateAdjustVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.PoolContant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.dto.FcInstrateAdjustSimpleSearchDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 内部利率
 * @date 2017-07-11 10:54:47
 */
@Api(value = "/v1-system/basic/business/fcinstrateadjust", tags = "资金池账户利率调整复核")
@RestController("v1FcInstrateAdjustController")
@RequestMapping("/v1-system/basic/business/fcinstrateadjust")
public class FcInstrateAdjustController extends BaseController {
    @Autowired
    private FcInstrateAdjustService fcInstrateAdjustService;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IFcCorpPoolService fcCorpPoolServiceI;


    /**
     * 内部利率 table列表
     *
     * @return
     */
    @ApiOperation("内部利率 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<FcInstrateAdjustVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        if (PoolContant.POOL_SIGN_1.equals(input.getFormdata().get("poolSign"))) {
            return AjaxJson.success(fcInstrateAdjustService.getDataTablesUnionForPool(input, this.getCurrentUser()));
        } else {
            return AjaxJson.success(fcInstrateAdjustService.getDataTablesUnion(input, this.getCurrentUser()));
        }

    }


    /**
     * 查询所有的待复核的调整利率 table列表
     *
     * @return
     */
    @PostMapping("/checkDataTable")
    public AjaxJson<DataTablesOutput<FcInstrateAdjustVO>> checkDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        if (PoolContant.POOL_SIGN_1.equals(input.getFormdata().get("poolSign"))) {
            return AjaxJson.success(fcInstrateAdjustService.getWaitCheckDataTablesPoolUnion(input, this.getCurrentUser()));
        } else {
            return AjaxJson.success(fcInstrateAdjustService.getWaitCheckDataTablesUnion(input, this.getCurrentUser()));
        }
    }

    /**
     * 根据ID查询内部利率调整详情
     *
     * @return
     */
    @GetMapping("/detailById/{id}")
    public AjaxJson<FcInstrateAdjust> detail(@PathVariable("id") String id) {
        return AjaxJson.success(fcInstrateAdjustService.get(id));
    }

    /**
     * 内部利率编辑页面跳转,内部利率编辑页面跳转
     *
     * @return
     */
    @ApiOperation("获取内部利率详情：内部利率编辑页面跳转,内部利率编辑页面跳转")
    @GetMapping("/check/{poolSign}")
    public AjaxJson<FcInstrateAdjustDetailVO> check(@RequestBody FcInstrateAdjust fcInstrateAdjust, @PathVariable("poolSign") String poolSign) {
        FcInstrateAdjustDetailVO vo = new FcInstrateAdjustDetailVO();
        vo.setPoolSign(poolSign);
        commonView(fcInstrateAdjust.getId(), vo, poolSign);
        return AjaxJson.success(vo);
    }

    /**
     * 内部利率详情页
     *
     * @return
     */
    @ApiOperation("内部利率详情页")
    @LogMessage(content = "查询内部利率详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{poolSign}")
    public AjaxJson<FcInstrateAdjustDetailVO> view(@PathVariable("id") String id, @PathVariable("poolSign") String poolSign) {
        FcInstrateAdjustDetailVO vo = new FcInstrateAdjustDetailVO();
        vo.setPoolSign(poolSign);
        vo.setIsView("Y");
        commonView(id, vo, poolSign);
        return AjaxJson.success(vo);
    }

    private void commonView(String id, FcInstrateAdjustDetailVO model, String poolSign) {
        if (StringUtils.isNotEmpty(id)) {
            FcInstrateAdjustSimpleVO adjust = fcInstrateAdjustService.getFcInstrateAdjust(id, poolSign);
            //如果是资金池取资金池代码
            if (PoolContant.POOL_SIGN_1.equals(poolSign)) {
                model.setNetCode((StringUtils.isEmpty(adjust.getNetId()) || fcCorpPoolServiceI.getById(adjust.getNetId()) == null) ? "" : fcCorpPoolServiceI.getById(adjust.getNetId()).getPoolCode());
            } else {
                model.setNetCode((StringUtils.isEmpty(adjust.getNetId()) || corpServiceI.get(adjust.getNetId()) == null) ? "" : corpServiceI.get(adjust.getNetId()).getCode());
            }
            model.setCorpCode((StringUtils.isEmpty(adjust.getCorpId()) || corpServiceI.get(adjust.getCorpId()) == null) ? "" : corpServiceI.get(adjust.getCorpId()).getCode());
            model.setFcInstrateAdjustPage(adjust);
        }
    }


    /**
     * 内部table列表
     *
     * @param vo
     * @return
     */
    @ApiOperation("内部table列表")
    @PostMapping("/dyncTable")
    public AjaxJson<List<FcInstrateAdjustSimpleVO>> dyncTable(@RequestBody FcInstrateAdjustSimpleSearchDTO vo) {
        List<FcInstrateAdjustSimpleVO> fcInitAccs = fcInstrateAdjustService.getFcInitAccsByParams(vo.getNetId(), vo.getCorpId(), vo.getSubjectId(), vo.getCurId(), vo.getInstrtType(), vo.getAccount());
           return AjaxJson.success(fcInitAccs);
    }

    /**
     * 获取资金池
     *
     * @return
     */
    @ApiOperation("获取资金池")
    @GetMapping("getUserCorpPools")
    public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPools() {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_RATE_ADJUST;
        return AjaxJson.success(fcCorpPoolServiceI.getUserCorpPools(this.getCurrentUser(), resource));
    }

    /**
     * 获取资金池
     *
     * @param poolId
     * @return
     */
    @ApiOperation("获取资金池")
    @GetMapping("getUserCorpPoolChilds/{poolId}")
    public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPoolChilds(@PathVariable("poolId") String poolId) {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_RATE_ADJUST;
        return AjaxJson.success(fcCorpPoolServiceI.getUserCorpPoolChilds(this.getCurrentUser(), resource, poolId));
    }

    /**
     * 添加内部利率
     *
     * @param fcInstrateAdjust
     * @return
     */
    //
    @ApiOperation("添加内部利率")
    @LogMessage(content = "添加内部利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{poolSign}")
    public AjaxJson save(@RequestBody List<FcInstrateAdjust> fcInstrateAdjust, @PathVariable("poolSign") String poolSign) {
        fcInstrateAdjustService.save(fcInstrateAdjust, getCurrentUser(), poolSign);
        return success();
    }

    /**
     * 修改内部利率
     *
     * @param fcInstrateAdjust
     * @return
     */
    @ApiOperation("修改内部利率")
    @LogMessage(content = "修改内部利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify/{poolSign}")
    public AjaxJson update(@RequestBody FcInstrateAdjust fcInstrateAdjust, @PathVariable("poolSign") String poolSign) {
        fcInstrateAdjustService.modify(fcInstrateAdjust, getCurrentUser(), poolSign);
        return success();
    }

    /**
     * 复核
     *
     * @param fcInstrateAdjust
     * @return
     */
    @ApiOperation("复核")
    @LogMessage(content = "复核", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/apply")
    public AjaxJson apply(@RequestBody FcInstrateAdjust fcInstrateAdjust) {
        String fcId = fcInstrateAdjust.getId();
        if (fcId != null) {
            fcInstrateAdjustService.checkFcInAdjusts(fcInstrateAdjust, getCurrentUser());
        }
        return success();
    }

    /**
     * 删除内部利率
     *
     * @param id
     * @return
     */
    //
    @ApiOperation("删除内部利率")
    @LogMessage(content = "删除内部利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        FcInstrateAdjust fcInstrateAdjust = fcInstrateAdjustService.get(id);
        fcInstrateAdjustService.deleteEntity(fcInstrateAdjust);
        return success();
    }

    /**
     * 批量删除内部利率
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除内部利率")
    @LogMessage(content = "批量删除内部利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            FcInstrateAdjust fcInstrateAdjust = fcInstrateAdjustService.get(id);
            fcInstrateAdjustService.deleteEntity(fcInstrateAdjust);
        }
        return success();
    }


}
