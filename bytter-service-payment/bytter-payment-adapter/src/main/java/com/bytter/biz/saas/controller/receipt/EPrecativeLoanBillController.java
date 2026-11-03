package com.bytter.biz.saas.controller.receipt;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.entity.EPrecativeLoanBill;
import com.bytter.biz.receipt.service.EPrecativeLoanBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.EPrecativeLoanBillEntityVO;
import com.bytter.biz.receipt.vo.EPrecativeLoanBillPageVO;
import com.bytter.biz.receipt.vo.EPrecativeLoanBillVO;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托贷款电子回单表
 * @date 2017-07-04 15:30:40
 */
@Api(value = "/v1-payment/receipt/eprecativeloanbill",tags = "委托贷款电子回单表")
@RestController("v1EPrecativeLoanBillController")
@RequestMapping("/v1-payment/receipt/eprecativeloanbill")
public class EPrecativeLoanBillController extends BaseController {
    @Autowired
    private EPrecativeLoanBillService ePrecativeLoanBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;
    @DubboReference(check = false)
    private IUserService userService;

    /**
     * 委托贷款电子回单表 table列表
     *
     * @return
     */
    @ApiOperation(value = "委托贷款电子回单表列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(ePrecativeLoanBillService.listForUnion(input, this.getCurrentUser()));
    }
    /**
     * 委托贷款电子回单表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("委托贷款电子回单表编辑页面跳转前数据获取")
    @GetMapping("/update")
    public AjaxJson<EPrecativeLoanBillEntityVO> update(String id) {
        EPrecativeLoanBillEntityVO entityVO = new EPrecativeLoanBillEntityVO();
        if (StringUtils.isNotEmpty(id)) {
            EPrecativeLoanBill ePrecativeLoanBill = ePrecativeLoanBillService.get(id);
            entityVO.setEPrecativeLoanBillPage(ePrecativeLoanBill);
            return success(entityVO);
        }
        return success();
    }
    /**
     * 委托贷款电子回单表详情页
     *
     * @return
     */
    @ApiOperation("委托贷款电子回单表详情页")
    @LogMessage(content = "查询委托贷款电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public AjaxJson<EPrecativeLoanBillPageVO> view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        EPrecativeLoanBillPageVO pageVO = new EPrecativeLoanBillPageVO();
        EPrecativeLoanBillVO ePrecativeLoanBill = ePrecativeLoanBillService.getVOByBillNo(null, id);
        String netId = ePrecativeLoanBill.getNetId();
        String billType = ePrecativeLoanBill.getBillType();
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
            EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
            pageVO.setEVisaTypePage(eVisaType);
        }
        if(StringUtils.isNotEmpty(ePrecativeLoanBill.getTallyerCode()) ){
           com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(ePrecativeLoanBill.getTallyerCode());
           ePrecativeLoanBill.setTallyerCode(userDTO.getUsername());
        }
        if(StringUtils.isNotEmpty(ePrecativeLoanBill.getCheckerCode()) ){
            com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(ePrecativeLoanBill.getCheckerCode());
            ePrecativeLoanBill.setCheckerCode(userDTO.getUsername());
        }
        // 获取操作日结的用户
        UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(ePrecativeLoanBill.getNetId(), ePrecativeLoanBill.getTallyDate());
        if (dayOverOperatorUser != null) {
            ePrecativeLoanBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
        }
        //操作类型：0-打印预览 1-打印
        pageVO.setOpType(opType);
        pageVO.setEPrecativeLoanBillPage(ePrecativeLoanBill);
        return success(pageVO);
    }
    /**
     * 添加委托贷款电子回单表
     *
     * @param ePrecativeLoanBill
     * @return
     */
    @ApiOperation("添加委托贷款电子回单表")
    @LogMessage(content = "添加委托贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody EPrecativeLoanBill ePrecativeLoanBill) {
        ePrecativeLoanBillService.save(ePrecativeLoanBill);
        return success();
    }
    /**
     * 修改委托贷款电子回单表
     *
     * @param ePrecativeLoanBill
     * @return
     */
    @ApiOperation("修改委托贷款电子回单表")
    @LogMessage(content = "修改委托贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody EPrecativeLoanBill ePrecativeLoanBill) {
        EPrecativeLoanBill t = ePrecativeLoanBillService.get(ePrecativeLoanBill.getId());
        BeanUtils.copyNotNullProperties(ePrecativeLoanBill, t);
        ePrecativeLoanBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除委托贷款电子回单表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除委托贷款电子回单表")
    @LogMessage(content = "删除委托贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        EPrecativeLoanBill ePrecativeLoanBill = ePrecativeLoanBillService.get(id);
        ePrecativeLoanBillService.deleteEntity(ePrecativeLoanBill);
        return success();
    }
    /**
     * 批量删除委托贷款电子回单表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除委托贷款电子回单表")
    @LogMessage(content = "批量删除委托贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            EPrecativeLoanBill ePrecativeLoanBill = ePrecativeLoanBillService.get(id);
            ePrecativeLoanBillService.deleteEntity(ePrecativeLoanBill);
        }
        return success();
    }
}
