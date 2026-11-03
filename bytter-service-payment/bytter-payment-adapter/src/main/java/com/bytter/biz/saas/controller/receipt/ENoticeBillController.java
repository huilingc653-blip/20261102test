package com.bytter.biz.saas.controller.receipt;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.entity.ENoticeBill;
import com.bytter.biz.receipt.service.ENoticeBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.ENoticeBillPageVO;
import com.bytter.biz.receipt.vo.ENoticeBillVO;
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
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
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
 * @Description: 通知单电子回单表
 * @date 2017-07-04 15:30:08
 */
@Api(value = "/v1-payment/receipt/enoticebill",tags = "通知单电子回单表")
@RestController("v1ENoticeBillController")
@RequestMapping("/v1-payment/receipt/enoticebill")
public class ENoticeBillController extends BaseController {
    @Autowired
    private ENoticeBillService eNoticeBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * 通知单电子回单表 table列表
     *
     * @return
     */
    @ApiOperation("通知单电子回单表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(eNoticeBillService.listForUnion(input, this.getCurrentUser()));
    }
    /**
     * 通知单电子回单表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<ENoticeBill> update(String id) {
        ENoticeBill bill = new ENoticeBill();
        if (StringUtils.isNotEmpty(id)) {
            bill = eNoticeBillService.get(id);
            return success(bill);
        }
        return success();
    }
    /**
     * 通知单电子回单表详情页
     *
     * @return
     */
    @ApiOperation("通知单电子回单表详情页")
    @LogMessage(content = "查询通知单电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public AjaxJson<ENoticeBillPageVO> view(@PathVariable("id") String id, @PathVariable("opType") String opType) {
        ENoticeBillPageVO pageVO = new ENoticeBillPageVO();
        ENoticeBillVO eNoticeBill = eNoticeBillService.getVOByBillNo(null, id);
        CorpDTO corpDTO = corpService.get(eNoticeBill.getCorpId());
        String netId = corpDTO.getNetId();
        String billType = eNoticeBill.getBillType();
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
            EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
            pageVO.setEVisaTypePage(eVisaType);
        }
        // 获取操作日结的用户
        UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(eNoticeBill.getNetId(), eNoticeBill.getTallyDate());
        if (dayOverOperatorUser != null) {
            eNoticeBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
        }
        if(StringUtils.isNotEmpty(eNoticeBill.getTallierCode()) ){
            com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eNoticeBill.getTallierCode());
            eNoticeBill.setTallierName(userDTO.getUsername());
        }
        if(StringUtils.isNotEmpty(eNoticeBill.getCheckerCode()) ){
            com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eNoticeBill.getCheckerCode());
            eNoticeBill.setCheckerName(userDTO.getUsername());
        }
        //操作类型：0-打印预览 1-打印
        pageVO.setOpType(opType);
        pageVO.setENoticeBillPage(eNoticeBill);
        return success(pageVO);
    }
    /**
     * 添加通知单电子回单表
     *
     * @param eNoticeBill
     * @return
     */
    @ApiOperation("添加通知单电子回单表")
    @LogMessage(content = "添加通知单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ENoticeBill eNoticeBill) {
        eNoticeBillService.save(eNoticeBill);
        return success();
    }
    /**
     * 修改通知单电子回单表
     *
     * @param eNoticeBill
     * @return
     */
    @ApiOperation("修改通知单电子回单表")
    @LogMessage(content = "修改通知单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ENoticeBill eNoticeBill) {
        ENoticeBill t = eNoticeBillService.get(eNoticeBill.getId());
        BeanUtils.copyNotNullProperties(eNoticeBill, t);
        eNoticeBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除通知单电子回单表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除通知单电子回单表")
    @LogMessage(content = "删除通知单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ENoticeBill eNoticeBill = eNoticeBillService.get(id);
        eNoticeBillService.deleteEntity(eNoticeBill);
        return success();
    }
    /**
     * 批量删除通知单电子回单表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除通知单电子回单表")
    @LogMessage(content = "批量删除通知单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            ENoticeBill eNoticeBill = eNoticeBillService.get(id);
            eNoticeBillService.deleteEntity(eNoticeBill);
        }
        return success();
    }
}
