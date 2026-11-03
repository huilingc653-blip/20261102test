package com.bytter.increment.web.controller;
import com.bytter.increment.domain.entity.ChoProduct;
import com.bytter.increment.domain.service.ChoProductService;
import com.bytter.increment.api.enums.ChoProductMessageEnum;
import com.bytter.increment.domain.vo.ChoProductVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
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
@Controller
@RequestMapping("/chowmatistic/choproduct")
public class ChoProductController extends BaseController {
    @Autowired
    private ChoProductService choProductService;
    @GetMapping("/list")
    public String list(Model model){
        return "chowmatistic/listChoProduct";
    }

    @GetMapping("/list2")
    public String list2(Model model, String financialType, String productType,String bankTypeId){
        model.addAttribute("financialType", financialType);
        model.addAttribute("productType", productType);
        model.addAttribute("bankTypeId", bankTypeId);
        return "chowmatistic/listChoProduct2";
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return choProductService.getDataTablesUnion(dataTablesInput);
    }
    @GetMapping("/add")
    public String add(ChoProduct choProduct,Model model){
        if (StringUtils.isNotEmpty(choProduct.getId())) {
            choProduct = choProductService.get(choProduct.getId());
            model.addAttribute("choProductPage", choProduct);
        }
        return "chowmatistic/addChoProduct";
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ChoProduct choProduct){
        if (choProductService.findProductByCode(choProduct.getProductCode()) != null) {
            throw new ParameterException(ChoProductMessageEnum.CHO_PRODUCT_TYPE_CODE_UNIQUE);
        }
        choProduct.setCreateBy(this.getCurrentUser().getLoginName());
        choProduct.setCreateDate(DateUtils.getDate());
        choProductService.saveChoProduct(choProduct);
        return success();
    }
    @GetMapping("/update")
    public String update(ChoProduct choProduct,Model model){
        if (StringUtils.isNotEmpty(choProduct.getId())) {
            choProduct = choProductService.get(choProduct.getId());
            model.addAttribute("choProductPage", choProduct);
        }
        return "chowmatistic/updateChoProduct";
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ChoProduct choProduct){
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
    public Object getProductsByType(String productType,String bankType){
        List<ChoProduct> products = choProductService.getProductsByType(productType,bankType);
        return products;
    }
    @RequestMapping("/json")
    @ResponseBody
    public List<ChoProductVo> json(String financialType) {
        return choProductService.listValidSignAll(financialType);
    }


    @GetMapping("/json2/{financialType}")
    @ResponseBody
    public List<ChoProductVo> json2(@PathVariable("financialType") String financialType) {
        return choProductService.listValidSignAll(financialType);
    }

    @GetMapping("/json3")
    @ResponseBody
    public List<ChoProductVo> json3(String financialType, String productType) {
        return choProductService.listValidSignAll(financialType, productType);
    }
}
