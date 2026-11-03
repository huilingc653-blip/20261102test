package com.bytter.increment.saas.controller;

import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.entity.ChoProductRenewal;
import com.bytter.increment.domain.service.ChoProductRenewalService;
import com.bytter.increment.domain.vo.ChoProductRedeemVo;
import com.bytter.increment.domain.vo.v1.ChoProductRenewalPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 理财续期Controller
 *
 * @author ouyj
 */
@Api(value = "理财续期", tags = "理财续期")
@RestController("v1ChoProductRenewalController")
@RequestMapping("/v1-increment/chowmatistic/choproductRenewal")
public class ChoProductRenewalController extends BaseController {
    @Autowired
    private ChoProductRenewalService choProductRenewalService;

    @ApiOperation(value = "查询理财续期列表", notes = "查询理财续期列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choProductRenewalService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    @ApiOperation(value = "查询理财续期选择列表", notes = "查询理财续期选择列表")
    @PostMapping("/datatableForSelect")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableForSelect(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choProductRenewalService.getDataTablesUnionForSelect(input, this.getCurrentUser()));
    }

    @ApiOperation(value = "保存理财续期", notes = "保存理财续期")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ChoProductRenewal entity) {
        choProductRenewalService.saveChoProductRedeem(entity, getCurrentUser());
        return success();
    }

    @ApiOperation(value = "批量提交理财续期", notes = "批量提交理财续期")
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestBody BisIDsDTO idsDTO) {
        String ids = idsDTO.getIds();
        choProductRenewalService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }

    @ApiOperation(value = "获取理财续期编辑数据", notes = "获取理财续期编辑数据")
    @GetMapping("/update")
    public AjaxJson<ChoProductRenewalPageVO> update(String id) {
        ChoProductRenewalPageVO pageVO = new ChoProductRenewalPageVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductRedeemVo choProductReqVo = choProductRenewalService.getChoProductRedeemVoById(id);
            pageVO.setChoProductRedeemPage(choProductReqVo);
        }
        return success(pageVO);
    }

    @ApiOperation(value = "修改理财续期", notes = "修改理财续期")
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ChoProductRenewal entity) {
        choProductRenewalService.toModify(entity, getCurrentUser());
        return success();
    }

    @ApiOperation(value = "删除理财续期", notes = "删除理财续期")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ChoProductRenewal entity = choProductRenewalService.get(id, true);
        this.choProductRenewalService.deleteEntity(entity);
        return success();
    }

    @ApiOperation(value = "获取理财续期详情", notes = "获取理财续期详情")
    @GetMapping("/view/{id}")
    public AjaxJson<ChoProductRenewalPageVO> view(@PathVariable("id") String id) {
        ChoProductRenewalPageVO pageVO = new ChoProductRenewalPageVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductRedeemVo choProductReqVo = choProductRenewalService.getChoProductRedeemVoById(id);
            pageVO.setChoProductRedeemPage(choProductReqVo);
            pageVO.setBusType(ChoMessageConstant.BUS_TYPE_PRODUCT_RENEWAL);
        }
        return success(pageVO);
    }

    @ApiOperation(value = "加载审核列表数据", notes = "加载审核列表数据")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(this.choProductRenewalService.dataTableApprove(input));
    }
}
