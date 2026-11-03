package com.bytter.check.saas.controller;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.check.reconcile.service.BisCheckReasonService;
import com.bytter.check.reconcile.vo.BisCheckReasonVO;
import com.bytter.check.vo.BisCheckReasonPageVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.*;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BankTypeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "对账原因维护",tags = "对账原因维护")
@RestController("v1BisCheckReasonController")
@RequestMapping("/v1-check/bis/checkReason")
public class BisCheckReasonController extends BaseController{

    @Autowired
    private BisCheckReasonService bisCheckReasonService;
    @DubboReference(check = false)
    private IBankTypeService iBankTypeService;
    @DubboReference(check = false)
    private IDictService dictService;

    @ApiOperation("对账原因维护table表格")
    @PostMapping("/datatable")
    @ResponseBody
	public AjaxJson<DataTablesOutput<BisCheckReasonVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = bisCheckReasonService.getDataTablesUnion(input);
        List<BisCheckReasonVO> voList= output.getData();
        if(voList.size()>0) {
            Map<String, BankTypeDTO> typeMap= iBankTypeService.getBankTypeMap();
//		(select bbt.type_Name from bt_bank_type bbt where bbt.id=br.bank_type_id) as bankTypeName
            for(BisCheckReasonVO vo:voList) {
                vo.setBankTypeName(typeMap.get(vo.getBankTypeId()).getTypeName());
                String [] appScopeS=vo.getAppScope().split(",");
                String appScopeName="";
                for(String appScope:appScopeS){
                    appScopeName+=dictService.get("BIS_CHECK_REASON_SCOPE", appScope)+",";
                }
                vo.setAppScopeName(appScopeName.substring(0,appScopeName.length()-1));
                vo.setValidSignName(dictService.get("valid_sign",vo.getValidSign()));
            }
        }
        //前后端分离的情况做排序
        if(input.getColumns()!=null && input.getColumns().size() == 1){
            DataTableOutPutOrderUtils.orderOutputList(input,output.getData());
        }
        return success(output);
    }

    @ApiOperation(value = "获取对账原因信息"
                ,notes = "原先的flag是用来分辨 新增和编辑的  现在不再传入flag")
    @GetMapping("/getInfoById")
    @ResponseBody
    public AjaxJson<BisCheckReasonPageVO> getInfoById(String id) {
        BisCheckReasonPageVO pageVO = new BisCheckReasonPageVO();
        BisCheckReasonVO vo = bisCheckReasonService.getBisCheckReason(id);
        pageVO.setCheckReasonVo(vo);
        return success(pageVO);
    }
    /**
    *
    * @return
    */
    @ApiOperation("查看对账原因明细")
    @GetMapping("/view/{id}")
    @ResponseBody
    public AjaxJson<BisCheckReasonPageVO> view(@PathVariable("id") String id) {
        BisCheckReasonPageVO pageVO = new BisCheckReasonPageVO();
        BisCheckReasonVO vo = bisCheckReasonService.getBisCheckReason(id);
        vo.setBankTypeName(iBankTypeService.getObjectById(vo.getBankTypeId()).getTypeName());
        String [] appScopeS=vo.getAppScope().split(",");
        String appScopeName="";
        for(String appScope:appScopeS){
            appScopeName+=dictService.get("BIS_CHECK_REASON_SCOPE", appScope)+",";
        }
        vo.setAppScopeName(appScopeName.substring(0,appScopeName.length()-1));
        vo.setValidSignName(dictService.get("valid_sign", vo.getValidSign()));
        pageVO.setCheckReasonVo(vo);
        return success(pageVO);
    }
    /**
     * @param
     * @return
     */
    @ApiOperation("保存/更新信息operator为add或update")
    @PostMapping("/save/{operator}")
    @ResponseBody
    public AjaxJson saveOrUpdate(@PathVariable("operator")String operator, BisCheckReasonVO bisCheckReasonVO) {
        return bisCheckReasonService.saveOrUpdate(operator, bisCheckReasonVO, getCurrentUser());
    }
	@ApiOperation("批量删除")
    @PostMapping("/btDel")
	@ResponseBody
	public AjaxJson batchDel(String ids){
		return  bisCheckReasonService.saveOrUpdate(ids,this.getCurrentUser());
	}
	/**
     * @return
     */
    @ApiOperation("余额原因类型组件选择")
    @GetMapping("/json")
    @ResponseBody
    public AjaxJson<List<BisCheckReasonVO>> getListByValidSign(String bankTypeId) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("bankTypeId",bankTypeId);
    	params.put("validSign", ReceiptConstant.BIS_BANK_VALID_SIGN_1);
        List<BisCheckReasonVO> bisCheckReasonVO = bisCheckReasonService.getListByValidSign(params);
        return success(bisCheckReasonVO);
    }
}
