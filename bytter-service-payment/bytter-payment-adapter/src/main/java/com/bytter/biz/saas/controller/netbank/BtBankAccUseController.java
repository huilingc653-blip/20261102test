package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.netbank.entity.BtBankAccUse;
import com.bytter.biz.netbank.service.BtBankAccUseService;
import com.bytter.biz.netbank.vo.BtBankAccUseVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: 账号用途维护
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/10/23
 */
@Api(value ="/v1-payment/netbank/bankAccUse",tags = "账号用途维护")
@RestController("v1BtBankAccUseController")
@RequestMapping("/v1-payment/netbank/bankAccUse")
public class BtBankAccUseController extends BaseController {
    @Autowired
    private BtBankAccUseService service;


     @ApiOperation("列表查询")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BtBankAccUseVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(service.listData(input));
    }


    @ApiOperation("新增账户用途")
    @LogMessage(content = "新增账户用途",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody BtBankAccUseVO vo){
        service.save(vo,getCurrentUser());
        return success();
    }

    /**
     * 修改账户用途
     * @return
     */
    @ApiOperation("修改账户用途页面数据")
    @GetMapping("/update/{id}")
    public AjaxJson<BtBankAccUse> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
           return AjaxJson.success(service.get(id, true));
        }
        return AjaxJson.success();
    }

    @ApiOperation("删除账户用途")
    @LogMessage(content = "删除账户用途",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id){
        service.deleteById(id);
        return success();
    }

    /**
     * excel 导入
     * @param
     * @return
     */
    @ApiOperation("导入")
    @LogMessage(content = "添加账号用途", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<BtBankAccUseVO> list) {
        service.saveImport(list, getCurrentUser());
        return success();
    }

    @ApiOperation("获取账号用途所有数据")
    @GetMapping("/json")
    public AjaxJson<List<BtBankAccUse>> json(){
        return AjaxJson.success(service.listAll());
    }
}
