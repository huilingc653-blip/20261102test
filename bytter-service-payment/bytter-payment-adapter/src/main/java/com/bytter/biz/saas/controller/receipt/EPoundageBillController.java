package com.bytter.biz.saas.controller.receipt;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.entity.EPoundageBill;
import com.bytter.biz.receipt.service.EPoundageBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.EPoundageBillEntityVO;
import com.bytter.biz.receipt.vo.EPoundageBillPageVO;
import com.bytter.biz.receipt.vo.EPoundageBillVO;
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
 * @Description: 手续费电子回单表
 * @date 2017-07-04 15:30:13
 */
@Api(value = "/v1-payment/receipt/epoundagebill",tags = "手续费电子回单表")
@RestController("v1EPoundageBillController")
@RequestMapping("/v1-payment/receipt/epoundagebill")
public class EPoundageBillController extends BaseController {
    @Autowired
    private EPoundageBillService ePoundageBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;

    /**
     * 手续费电子回单表 table列表
     *
     * @return
     */
    @ApiOperation(value = "手续费电子回单表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(ePoundageBillService.listForUnion(input, this.getCurrentUser()));
    }

    /**
     * 手续费电子回单表编辑页面跳转
     *
     * @return
     */
    @ApiOperation(value = "手续费电子回单表编辑页面跳转前数据获取")
    @GetMapping("/update")
    public AjaxJson<EPoundageBillEntityVO> update(String id) {
        EPoundageBillEntityVO pageVO = new EPoundageBillEntityVO();
        if (StringUtils.isNotEmpty(id)) {
            EPoundageBill ePoundageBill = ePoundageBillService.get(id);
            pageVO.setEPoundageBillPage(ePoundageBill);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 手续费电子回单表详情页
     *
     * @return
     */
    @ApiOperation(value = "手续费电子回单表详情页")
    @LogMessage(content = "查询手续费电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public AjaxJson<EPoundageBillPageVO> view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        EPoundageBillPageVO pageVO = new EPoundageBillPageVO();
        EPoundageBillVO ePoundageBill = ePoundageBillService.getVOByBillNo(null, id);
        String netId = ePoundageBill.getNetId();
        String billType = ePoundageBill.getBillType();
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
            EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
            pageVO.setEVisaTypePage(eVisaType);
        }
        // 获取操作日结的用户
        UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(ePoundageBill.getNetId(), ePoundageBill.getTallyDate());
        if (dayOverOperatorUser != null) {
            ePoundageBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
        }
        //操作类型：0-打印预览 1-打印
        pageVO.setOpType(opType);
        pageVO.setEPoundageBillPage(ePoundageBill);
        return success(pageVO);
    }
    /**
     * 添加手续费电子回单表
     *
     * @param ePoundageBill
     * @return
     */
    @ApiOperation(value = "添加手续费电子回单表")
    @LogMessage(content = "添加手续费电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody EPoundageBill ePoundageBill) {
        ePoundageBillService.save(ePoundageBill);
        return success();
    }
    /**
     * 修改手续费电子回单表
     *
     * @param ePoundageBill
     * @return
     */
    @ApiOperation(value = "修改手续费电子回单表")
    @LogMessage(content = "修改手续费电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody EPoundageBill ePoundageBill) {
        EPoundageBill t = ePoundageBillService.get(ePoundageBill.getId());
        BeanUtils.copyNotNullProperties(ePoundageBill, t);
        ePoundageBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除手续费电子回单表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除手续费电子回单表")
    @LogMessage(content = "删除手续费电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        EPoundageBill ePoundageBill = ePoundageBillService.get(id);
        ePoundageBillService.deleteEntity(ePoundageBill);
        return success();
    }
    /**
     * 批量删除手续费电子回单表
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除手续费电子回单表")
    @LogMessage(content = "批量删除手续费电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            EPoundageBill ePoundageBill = ePoundageBillService.get(id);
            ePoundageBillService.deleteEntity(ePoundageBill);
        }
        return success();
    }
}
