package com.bytter.biz.saas.controller.bond;

import com.bytter.core.base.BaseController;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.guarantee.api.IFcBondPreserveService;
import com.bytter.guarantee.vo.FcBondAccVO;
import com.bytter.guarantee.vo.FcBondPreserveVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:FcBondPreserveController
 * @Description:保证金维护控制层
 * @date 2018年9月18日 上午10:03:29
 */
@Api(value = "/v1-payment/bond/fcbondpreserve", tags = "保证金维护")
@RestController("v1FcBondPreserveController")
@RequestMapping("/v1-payment/bond/fcbondpreserve")
public class FcBondPreserveController extends BaseController {
    @DubboReference(check = false)
    private IFcBondPreserveService fcBondPreserveService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 获取维护列表的数据
     */
    @ApiOperation("获取维护列表的数据")
    @PostMapping("/getDataTable")
    public AjaxJson<DataTablesOutput<FcBondPreserveVO>> getDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<FcBondPreserveVO> output = fcBondPreserveService.getPreserveTable(input, this.getCurrentUser());
        List<FcBondPreserveVO> voList = output.getData();
        for (FcBondPreserveVO vo : voList) {
            //查询账号
            BtBankAccDTO accDTO = iBtBankAccService.getBankAccById(vo.getBankAccId());
            if (null != accDTO) {
                vo.setBankAcc(accDTO.getBankAcc());
                vo.setAccName(accDTO.getAccName());
                vo.setBankName(accDTO.getBankName());
                vo.setCorpId(accDTO.getCorpId());
                vo.setCorpName(corpService.getCorpName(accDTO.getCorpId()));
                vo.setCurId(accDTO.getCurId());
                vo.setCurName(accDTO.getCurName());
            }
        }
        return AjaxJson.success(output);
    }

    /**
     * 保存数据
     */
    @ApiOperation("保存数据")
    @PostMapping("/saveFcBondItem")
    public AjaxJson tempOrSubmit(FcBondPreserveVO fcBondPreserveVO, String saveType) {
        fcBondPreserveService.tempOrsubmit(fcBondPreserveVO, saveType, this.getCurrentUser());
        return success();
    }

    /**
     * 编辑数据
     */
    @ApiOperation("编辑数据")
    @GetMapping("/editBondItem/{id}")
    public AjaxJson<FcBondPreserveVO> editBondItem(@PathVariable("id") String id) {
        return AjaxJson.success(fcBondPreserveService.getFcBondItemById(id));
    }

    /**
     * 查看保证金项目信息
     */
    @ApiOperation("查看保证金项目信息")
    @GetMapping("/viewBondItem/{id}")
    public AjaxJson<FcBondPreserveVO> viewBondItem(@PathVariable("id") String id) {
        FcBondPreserveVO vo = fcBondPreserveService.getFcBondItemById(id);
        //查询账号
        BtBankAccDTO accDTO = iBtBankAccService.getBankAccById(vo.getBankAccId());
        if (null != accDTO) {
            vo.setBankAcc(accDTO.getBankAcc());
            vo.setAccName(accDTO.getAccName());
            vo.setBankName(accDTO.getBankName());
            vo.setCorpId(accDTO.getCorpId());
            vo.setCorpName(corpService.getCorpName(accDTO.getCorpId()));
            vo.setCurId(accDTO.getCurId());
            vo.setCurName(accDTO.getCurName());
        }
        return AjaxJson.success(vo);
    }

    /**
     * 删除保证金项目
     */
    @ApiOperation("删除保证金项目")
    @PostMapping("/deleteBondItem/{id}")
    public AjaxJson deleteBondItem(@PathVariable("id") String id) {
        fcBondPreserveService.deleteFcBondItem(id, this.getCurrentUser());
        return success();
    }

    /**
     * 释放保证金项目占用金额
     */
    @ApiOperation("释放保证金项目占用金额")
    @PostMapping("/releaseItemAmount/{id}")
    public AjaxJson releaseItemAmount(@PathVariable("id") String id) {
        fcBondPreserveService.releaseItemAmount(id, this.getCurrentUser());
        return success();
    }


    /**
     * 根据网点和银行获取存出保证金账户列表
     */
    @ApiOperation("根据网点和银行获取存出保证金账户列表")
    @PostMapping("/getSelectBondAccByTerms")
    public AjaxJson<DataTablesOutput<FcBondAccVO>> getSelectBondAccByTerms(@RequestBody V1DataTablesInput dataTablesInput, String corpId, String bankCode, String curId) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fcBondPreserveService.queryBondAccDataTables(input, corpId, bankCode, curId, this.getCurrentUser()));
    }


    /**
     * 保证金项目审批表 table列表
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("保证金项目审批表 table列表")
    @PostMapping("/bondItemApproveTable")
    public AjaxJson<DataTablesOutput<WorkFlowPersistence>> bondItemApproveTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fcBondPreserveService.getApproveDataTables(input));
    }

    /**
     * 审批详情页面
     */
    @ApiOperation("审批详情页面")
    @GetMapping("/infoApprove")
    public AjaxJson<FcBondPreserveVO> infoApprove(String id) {
        return AjaxJson.success(fcBondPreserveService.getFcBondItemById(id));
    }

    /**
     * 手机审批详情页面
     */
    @ApiOperation("手机审批详情页面")
    @GetMapping("/mobileApprove")
    public AjaxJson<FcBondPreserveVO> mobileApprove(String businessId) {
        return AjaxJson.success(fcBondPreserveService.getFcBondItemById(businessId));
    }
}
