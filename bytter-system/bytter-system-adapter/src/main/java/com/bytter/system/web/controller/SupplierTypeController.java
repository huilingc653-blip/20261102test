package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.ExternalCorpMessageEnum;
import com.bytter.system.entity.SupplierType;
import com.bytter.system.service.SupplierTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
@RequestMapping("/sys/suppliertype")
public class SupplierTypeController extends BaseController {
    @Autowired
    private SupplierTypeService supplierTypeService;
    @GetMapping("/list")
    public String list() {
        return "sys/suppliertype/list";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return supplierTypeService.dataTables(dataTablesInput);
    }
    @GetMapping("/addOrUpdate")
    public String addOrUpdate(String id, Model model) {
        if (!StringUtils.isBlank(id)) {
            SupplierType supplierType = supplierTypeService.get(id);
            model.addAttribute("supplierType", supplierType);
        }
        return "sys/suppliertype/addOrUpdate";
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(SupplierType supplierType) {
        supplierType.setCreateBy(getUserId());
        supplierType.setCreateDate(DateUtils.getDate());
        supplierType.setStatus("1");
        checkSupplierTypeCode(supplierType);
        supplierTypeService.save(supplierType);
        return success();
    }
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(SupplierType supplierType) {
        SupplierType supplier = supplierTypeService.get(supplierType.getId());
        BeanUtils.copyNotNullProperties(supplierType, supplier);
        supplier.setUpdateBy(getUserId());
        supplier.setUpdateDate(DateUtils.getDate());
        checkSupplierTypeCode(supplierType);
        supplierTypeService.update(supplier);
        return success();
    }
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(@RequestParam("externalIds[]") List<String> externalIds) {
        supplierTypeService.deleteByIds(externalIds);
        return success();
    }
    @GetMapping("/view")
    public String view(String id, Model model) {
        if (StringUtils.isNotBlank(id)) {
            SupplierType supplierType = supplierTypeService.get(id, true);
            model.addAttribute("supplierType", supplierType);
        }
        return "sys/suppliertype/view";
    }
    @GetMapping("/json")
    @ResponseBody
    public List<SupplierType> json() {
        return supplierTypeService.getValidSign();
    }
    /**
     * 检查供应商编号是否存在
     *
     * @param supplierType
     */
    private void checkSupplierTypeCode(SupplierType supplierType) {
        SupplierType existEntityByCode = supplierTypeService.getEntityByCode(supplierType.getCode());
        if (!EmptyUtils.isEmpty(existEntityByCode) && !existEntityByCode.getId().equalsIgnoreCase(supplierType.getId())) {
            throw new ParameterException(ExternalCorpMessageEnum.SYS_SUPPLIER_TYPE_MESSAGE_CODE_EXIST);
        }
        SupplierType existEntityByName = supplierTypeService.getEntityByName(supplierType.getName());
        if (!EmptyUtils.isEmpty(existEntityByName) && !existEntityByName.getId().equalsIgnoreCase(supplierType.getId())) {
            throw new ParameterException(ExternalCorpMessageEnum.SYS_SUPPLIER_TYPE_MESSAGE_NAME_EXIST);
        }
    }
}
