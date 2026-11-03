package com.bytter.biz.saas.controller.corpCashier;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.corpCashier.service.NisCorpCashierService;
import com.bytter.biz.corpCashier.vo.NisCorpCashierVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: 单位出纳维护
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/7/11
 */
@Api(value = "/v1-payment/netbank/corpCashier", tags = "单位出纳维护")
@RestController("V1NisCorpCashierController")
@RequestMapping("/v1-payment/netbank/corpCashier")
public class NisCorpCashierController extends BaseController {
    @Autowired
    private NisCorpCashierService corpCashierService;


    @ApiOperation("列表查询")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput<NisCorpCashierVO> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return corpCashierService.getDataTablesUnion(input);
    }

    @ApiOperation("编辑页面")
    @GetMapping("/update/{id}")
    public AjaxJson<NisCorpCashierVO> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            NisCorpCashierVO nisCorpCashierVO = corpCashierService.getCorpCashier(id);
            AjaxJson.success(nisCorpCashierVO);
        }
        return AjaxJson.success();
    }



    @ApiOperation("添加")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody NisCorpCashierVO vo){
        corpCashierService.save(vo,getCurrentUser());
        return success();
    }

    @ApiOperation("删除")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        corpCashierService.deleteCorpCashier(id);
        return success();
    }

    @ApiOperation("批量删除")
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        if (ObjectUtil.isNotEmpty(ids)){
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                corpCashierService.deleteCorpCashier(id);
            }
        }
        return success();
    }

    /**
     * 保存导入数据
     * @param list
     * @return
     */
    @ApiOperation("保存导入数据")
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<NisCorpCashierVO> list) {
        corpCashierService.saveImport(list, this.getCurrentUser());
        return success();
    }
}
