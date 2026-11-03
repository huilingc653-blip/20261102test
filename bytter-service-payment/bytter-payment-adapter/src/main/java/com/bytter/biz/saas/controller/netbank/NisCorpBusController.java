package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.netbank.dto.NisCorpBusAddVO;
import com.bytter.biz.netbank.entity.NisCorpBus;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.netbank.service.NisCorpBusService;
import com.bytter.biz.netbank.vo.NisCorpBusVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/// ///import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 单位业务类别对照表
 * @date 2017-05-18 16:36:11
 */
@Api(value = "/v1-payment/netbank/niscorpbus", tags = "单位业务类别对照")
@RestController("v1NisCorpBusController")
@RequestMapping("/v1-payment/netbank/niscorpbus")
@Slf4j
public class NisCorpBusController extends BaseController {
    @Autowired
    private NisCorpBusService nisCorpBusService;
    @Autowired
    private NisBusTypeService nisBusTypeService;
    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * 查询单位业务种类设置列表
     *
     * @param model
     * @return
     */

    /**
     * 单位业务类别显示 table列表
     *
     * @return
     */

    @ApiOperation("单位业务类别显示 table列表")
    @PostMapping("/dataTableList")
    public DataTablesOutput<NisCorpBusVO> dataTableList(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return nisCorpBusService.getDataTablesUnion(input, getCurrentUser().getId());
    }

    /**
     * 单位业务类别对照表新增页面跳转
     *
     * @return
     */
    @ApiOperation("单位业务类别对照表新增页面跳转")
    @GetMapping("/add")
    public AjaxJson<Map<String, Object>> add(NisCorpBus nisCorpBus) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(nisCorpBus.getId())) {
            nisCorpBus = nisCorpBusService.get(nisCorpBus.getId());
            model.put("nisCorpBusPage", nisCorpBus);
        }
        //获取有效的业务种类
        //model.addAttribute("nisBusTypes", nisBusTypeService.getValidBusType());
        //获取分配的单位
        model.put("corps", nisCorpBusService.getCorpIdList());
        //获取业务种类列表
        model.put("nisBusTypeList", nisBusTypeService.getNisBusType("1"));
        return AjaxJson.success(model);
    }

    /**
     * 单位业务类别对照表新增页面跳转
     *
     * @return
     */
    @ApiOperation("单位业务类别对照表新增页面跳转")
    @GetMapping("/copy")
    public AjaxJson<Map<String, Object>> copy(NisCorpBus nisCorpBus) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(nisCorpBus.getId())) {
            nisCorpBus = nisCorpBusService.get(nisCorpBus.getId());
            model.put("nisCorpBusPage", nisCorpBus);
        }
        String busTypes = "";
        if (StringUtils.isNotEmpty(nisCorpBus.getCorpId())) {
            busTypes = nisCorpBusService.findBusTypeName(nisCorpBus.getCorpId());
        }
        log.debug("------------busTypes=" + busTypes);
        model.put("copyedNisBusTypes", busTypes);
        //获取有效的业务种类
        //model.addAttribute("nisBusTypes", nisBusTypeService.getValidBusType());
        //获取分配的单位
        model.put("corps", nisCorpBusService.getCorpIdList());
        //获取业务种类列表
        model.put("nisBusTypeList", nisBusTypeService.getNisBusType("1"));
        return AjaxJson.success(model);
    }

    /**
     * 添加单位业务类别对照表
     *
     * @param nisCorpBus
     * @return
     */
    @ApiOperation("添加单位业务类别对照表")
    @LogMessage(content = "添加单位业务类别对照表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody NisCorpBusAddVO nisCorpBus) {
        if (nisCorpBus.getBusIds() == null || nisCorpBus.getBusIds().isEmpty() || (nisCorpBus.getBusIds().size() == 1 && CommonConstant.NO.equals(nisCorpBus.getBusIds().get(0)))) {
            nisCorpBus.setBusIds(new ArrayList<>());
        }
        nisCorpBusService.assignBusCorp(nisCorpBus, nisCorpBus.getBusIds());
        return success();
    }

    @ApiOperation("getNisCorpBus")
    @GetMapping("getNisCorpBus")
    public AjaxJson<List<NisCorpBusVO>> getNisCorpBus(String corpId, String cdSign) {
        return AjaxJson.success(nisCorpBusService.getByBusType(corpId, cdSign));
    }

    @ApiOperation("根据单位ID,收付款标志获取分配的业务种类")
    @GetMapping("getBusIdsByCorpId")
    public AjaxJson<List<String>> getBusIdsByCorpId(String corpId) {
        return AjaxJson.success(nisCorpBusService.getBusIdsByCorpId(corpId));
    }

    /**
     * 根据收付款标志获取分配的业务种类
     *
     * @param cdSign
     * @return
     */
    @ApiOperation("根据收付款标志获取分配的业务种类")
    @GetMapping("getNisCorpBusByCdSign")
    public AjaxJson<List<NisCorpBusVO>> getNisCorpBusByCdSign(String cdSign) {
        return AjaxJson.success(nisCorpBusService.getNisCorpBusByCdSign(cdSign));
    }

    /**
     * 获取分配的业务种类
     *
     * @param corpId
     * @param cdSign
     * @return
     */
    @ApiOperation("获取分配的业务种类")
    @RequestMapping("/listNisCorpBusVOByCorpIdAndCdSign")
    public AjaxJson<List<NisCorpBusVO>> listNisCorpBusVOByCorpIdAndCdSign(String corpId, String cdSign) {
        return AjaxJson.success(nisCorpBusService.getByBusType(corpId, cdSign));
    }

    /**
     * 获取分配的业务种类
     *
     * @param cdSign
     * @return
     */
    @ApiOperation("获取分配的业务种类")
    @RequestMapping(params = "listNisCorpBusVOByCdSign")
    public AjaxJson<List<NisCorpBusVO>> listNisCorpBusVOByCdSign(String cdSign) {
        return AjaxJson.success(nisCorpBusService.getByBusType(null, cdSign));
    }

    /**
     * 获取分配的业务种类
     *
     * @return
     */
    @ApiOperation("获取分配的业务种类")
    @LogMessage(content = "查询分配的业务种类", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{corpId}")
    public AjaxJson<CorpDTO> view(@PathVariable("corpId") String corpId) {
        return AjaxJson.success(corpService.get(corpId));
    }

    /**
     * 单位业务类别显示 table列表
     *
     * @return
     */
    @ApiOperation("单位业务类别显示 table列表")
    @PostMapping("/dataTableView")
    public DataTablesOutput<NisCorpBusVO> dataTableView(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return nisCorpBusService.getDataTablesViewUnion(input);
    }
}
