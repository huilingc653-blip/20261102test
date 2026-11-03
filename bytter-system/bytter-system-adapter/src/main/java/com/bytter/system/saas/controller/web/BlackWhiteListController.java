package com.bytter.system.saas.controller.web;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.constant.Globals;
import com.bytter.system.api.IBlackWhiteService;
import com.bytter.system.api.constant.SysBlackWhiteConstant;
import com.bytter.system.api.dto.BlackWhiteDTO;
import com.bytter.system.service.BlackWhiteListService;
import com.bytter.system.vo.BlackWhiteListVO;
import com.bytter.system.vo.v1.BlackWhiteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 黑白名单controll
 */
@Api(value = "/v1-system/blackwhite/configure", tags = "黑白名单controll")
@RestController("v1BlackWhiteListController")
@RequestMapping("/v1-system/blackwhite/configure")
public class BlackWhiteListController extends BaseController {
    @Autowired
    public BlackWhiteListService blackWhiteListService;
    @Autowired
    public IBlackWhiteService blackWhiteServiceImpl;

    /**
     * 黑/白名单配置
     *
     * @return
     */
    @ApiOperation("黑/白名单配置")
    @GetMapping("/{busType}/list")
    public AjaxJson<BlackWhiteVO> blackList(@PathVariable("busType") String busType) {
        BlackWhiteVO map = new BlackWhiteVO();
        map.setIntercept(SysBlackWhiteConstant.getBwType(busType));
        return AjaxJson.success(map);
    }

    /**
     * table/列表
     *
     * @return
     */
    @ApiOperation("table/列表")
    @PostMapping("/datatable")
    @LogMessage(content = "黑/白名单table", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public AjaxJson<DataTablesOutput<BlackWhiteListVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(blackWhiteListService.getDataTablesUnion(input));
    }

    /**
     * 详情
     *
     * @return
     */
    @ApiOperation("详情")
    @GetMapping("/view/{id}")
    public AjaxJson<BlackWhiteVO> view(@PathVariable("id") String id) {
        BlackWhiteVO data = new BlackWhiteVO();
        BlackWhiteListVO whiteListVO = blackWhiteListService.getInfoVoV1(id);
        if (whiteListVO != null) {
            data.setBlackWhite(whiteListVO);
            data.setIntercept(whiteListVO.getBwType());
        }
        return AjaxJson.success(data);
    }

    /**
     * 跳转到新增
     *
     * @return
     */
    @ApiOperation("跳转到新增")
    @GetMapping("/add/{operate}")
    public AjaxJson<BlackWhiteVO> add(@PathVariable("operate") String operate) {
        BlackWhiteVO map = new BlackWhiteVO();
        map.setIntercept(operate);
        map.setOperator(AccebillConstant.BILL_OPERATE_ADD);
        return AjaxJson.success(map);
    }

    /**
     * @return
     */
    @ApiOperation("修改页面数据")
    @GetMapping("/update/{id}")
    public AjaxJson<BlackWhiteVO> update(@PathVariable("id") String id) {
        BlackWhiteVO vo = new BlackWhiteVO();
        vo.setOperator(AccebillConstant.BILL_OPERATE_UPDATE);
        BlackWhiteListVO whiteListVO = blackWhiteListService.getInfoVoV1(id);
        if (whiteListVO != null) {
            vo.setBlackWhite(whiteListVO);
            vo.setIntercept(whiteListVO.getBwType());
        }
        return AjaxJson.success(vo);
    }

    /**
     * 新增/修改
     *
     * @param
     * @return
     */
    @ApiOperation("新增/修改")
    @LogMessage(content = "新增-黑白名单信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{operator}")
    public AjaxJson saveOrUpdate(@PathVariable("operator") String operator, @RequestBody BlackWhiteListVO blackWhiteListVO) {
        return blackWhiteListService.saveOrUpdate(operator, blackWhiteListVO, getCurrentUser());
    }

    @ApiOperation("删除-黑白名单信息")
    @LogMessage(content = "删除-黑白名单信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSUCCE)
    @PostMapping("/batchDel")
    public AjaxJson batchdata(String ids) {
        return blackWhiteListService.batchdata(ids, this.getCurrentUser());
    }

    /**
     * 导入保证申请
     *
     * @param list
     * @return
     */
    @ApiOperation("导入保证申请")
    @LogMessage(content = "导入黑白名单信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<BlackWhiteListVO> list, String intercept) {
        AjaxJson json = success();
        boolean flag = blackWhiteListService.saveImport(list, this.getCurrentUser(), intercept);
        json.setSuccess(flag);
        return success();
    }


    /**
     * 获取黑名单收付限制配置信息
     *
     * @return
     */
    @ApiOperation("获取黑名单收付限制配置信息")
    @GetMapping("/blacklistCheckType")
    public AjaxJson<String> getBlacklistCheckType() {
        return AjaxJson.success(blackWhiteServiceImpl.getBlacklistCheck());
    }

    /**
     * 前端客商和承兑行校验
     *
     * @return
     */
    @ApiOperation("前端客商和承兑行校验")
    @PostMapping("/blackWhiteCheck")
    public AjaxJson<BlackWhiteDTO> blackWhiteCheck(@RequestBody BlackWhiteListVO blackWhiteListVO) {
        return AjaxJson.success(blackWhiteServiceImpl.blackWhiteCheckBatch(blackWhiteListVO.getDtoList(), blackWhiteListVO.getIsControl(), blackWhiteListVO.getListType()));
    }
}
