package com.bytter.increment.saas.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.enums.ChoProductMessageEnum;
import com.bytter.increment.domain.entity.ChoProduct;
import com.bytter.increment.domain.service.ChoProductService;
import com.bytter.increment.domain.vo.ChoProductVo;
import com.bytter.increment.domain.vo.v1.ChoProductPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */

@Api(tags = "理财产品管理",value = "/v1-increment/chowmatistic/choproduct")
@RestController("v1ChoProductController")
@RequestMapping("/v1-increment/chowmatistic/choproduct")
public class ChoProductController extends BaseController {
    @Autowired
    private ChoProductService choProductService;

    @ApiOperation(value = "获取理财产品数据", notes = "获取理财产品数据")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(choProductService.getDataTablesUnion(tablesInput));
    }
    @ApiOperation(value = "保存理财产品", notes = "保存理财产品")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ChoProduct choProduct){
        if (choProductService.findProductByCode(choProduct.getProductCode()) != null) {
            throw new ParameterException(ChoProductMessageEnum.CHO_PRODUCT_TYPE_CODE_UNIQUE);
        }
        choProduct.setCreateBy(this.getCurrentUser().getLoginName());
        choProduct.setCreateDate(DateUtils.getDate());
        choProductService.saveChoProduct(choProduct);
        return success();
    }
    @GetMapping("/update")
    public AjaxJson<ChoProductPageVO> update(String id){
        ChoProductPageVO pageVO = new ChoProductPageVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProduct choProduct = choProductService.get(id);
            pageVO.setChoProductPage(choProduct);
        }
        return success(pageVO);
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ChoProduct choProduct){
        ChoProduct t = choProductService.get(choProduct.getId());
        choProduct.setUpdateBy(this.getCurrentUser().getLoginName());
        choProduct.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(choProduct, t);
        choProductService.modifyChoProduct(t);
        return success();
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        ChoProduct nisProject = choProductService.get(id);
        this.choProductService.deleteEntity(nisProject);
        return success();
    }
    @GetMapping("/getProductsByType")
    @ResponseBody
    public AjaxJson<List<ChoProduct>> getProductsByType(String productType,String bankType){
        List<ChoProduct> products = choProductService.getProductsByType(productType,bankType);
        return success(products);
    }
    @RequestMapping("/json")
    @ResponseBody
    public AjaxJson<List<ChoProductVo>> json(String financialType) {
        return success(choProductService.listValidSignAll(financialType));
    }


    @GetMapping("/json2/{financialType}")
    @ResponseBody
    public AjaxJson<List<ChoProductVo>> json2(@PathVariable("financialType") String financialType) {
        return success(choProductService.listValidSignAll(financialType));
    }

    @GetMapping("/json3")
    @ResponseBody
    public AjaxJson<List<ChoProductVo>> json3(String financialType, String productType) {
        return success(choProductService.listValidSignAll(financialType, productType));
    }
}
