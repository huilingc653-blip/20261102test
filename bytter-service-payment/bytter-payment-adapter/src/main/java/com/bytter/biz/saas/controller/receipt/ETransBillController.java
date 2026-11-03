package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.entity.ETransBill;
import com.bytter.biz.receipt.service.ETransBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.ETransBillEntityVO;
import com.bytter.biz.receipt.vo.ETransBillPageVO;
import com.bytter.biz.receipt.vo.ETransBillVO;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 账户转换电子回单表
 * @date 2017-07-04 15:30:19
 */
@Api(value = "/v1-payment/receipt/etransbill",tags = "账户转换电子回单表")
@RestController("v1ETransBillController")
@RequestMapping("/v1-payment/receipt/etransbill")
public class ETransBillController extends BaseController {
    @Autowired
    private ETransBillService eTransBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;
    @DubboReference(check = false)
    private IUserService userService;

    /**
     * 账户转换电子回单表 table列表
     *
     * @return
     */
    @ApiOperation(value = "账户转换电子回单表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(eTransBillService.datatables(input, this.getCurrentUser()));
    }

    /**
     * 账户转换电子回单表编辑页面跳转
     *
     * @return
     */
    @ApiOperation(value = "账户转换电子回单表编辑页面跳转前数据获取")
    @GetMapping("/update")
    public AjaxJson<ETransBillEntityVO> update(String id) {
        ETransBillEntityVO pageVO = new ETransBillEntityVO();
        if (StringUtils.isNotEmpty(id)) {
            ETransBill eTransBill = eTransBillService.get(id);
            pageVO.setEPoundageBillPage(eTransBill);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 账户转换电子回单表详情页
     *
     * @return
     */
    @LogMessage(content = "查询账户转换电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public AjaxJson<ETransBillPageVO> view(@PathVariable("id") String id, @PathVariable("opType") String opType) {
        ETransBillPageVO pageVO = new ETransBillPageVO();
        DataTablesInput input=new DataTablesInput();
        input.addFilter(MatchTypeEnum.EQ,"id",id);
        DataTablesOutput output=eTransBillService.datatables(input, this.getCurrentUser());
        List<ETransBillVO>  eTransBillVOS=output.getData();
        ETransBillVO eTransBillVO=eTransBillVOS.get(0);
        pageVO.setETransBillPage(eTransBillVO);
        if(!EmptyUtils.isEmpty(eTransBillVO)) {
            if(StringUtils.isNotEmpty(eTransBillVO.getTallierCode()) ){
                com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eTransBillVO.getTallierCode());
                eTransBillVO.setTallierName(userDTO.getUsername());
            }
            if(StringUtils.isNotEmpty(eTransBillVO.getCheckerCode()) ){
                com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eTransBillVO.getCheckerCode());
                eTransBillVO.setCheckerName(userDTO.getUsername());
            }
            String corpId = eTransBillVO.getCorpId();
            String billType = eTransBillVO.getBillType();
            if(EmptyUtils.isNotEmpty(corpId) && EmptyUtils.isNotEmpty(billType)){
                String netId=corpService.getNetByCorpId(corpId).getId();
                EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType,netId);
                pageVO.setEVisaTypePage(eVisaType);
            }
            // 获取操作日结的用户
            UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(eTransBillVO.getNetId(), eTransBillVO.getTallyDate());
            if (dayOverOperatorUser != null) {
                eTransBillVO.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
            }
        }
        //操作类型：0-打印预览 1-打印
        pageVO.setOpType(opType);
        return success(pageVO);
    }
    /**
     * 添加账户转换电子回单表
     *
     * @param eTransBill
     * @return
     */
    @ApiOperation(value = "添加账户转换电子回单表")
    @LogMessage(content = "添加账户转换电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ETransBill eTransBill) {
        eTransBillService.save(eTransBill);
        return success();
    }
    /**
     * 修改账户转换电子回单表
     *
     * @param eTransBill
     * @return
     */
    @ApiOperation(value = "修改账户转换电子回单表")
    @LogMessage(content = "修改账户转换电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ETransBill eTransBill) {
        ETransBill t = eTransBillService.get(eTransBill.getId());
        BeanUtils.copyNotNullProperties(eTransBill, t);
        eTransBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除账户转换电子回单表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除账户转换电子回单表")
    @LogMessage(content = "删除账户转换电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ETransBill eTransBill = eTransBillService.get(id);
        eTransBillService.deleteEntity(eTransBill);
        return success();
    }
    /**
     * 批量删除账户转换电子回单表
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除账户转换电子回单表")
    @LogMessage(content = "批量删除账户转换电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            ETransBill eTransBill = eTransBillService.get(id);
            eTransBillService.deleteEntity(eTransBill);
        }
        return success();
    }
    /**
     * 定期取款和通知取款电子回单表打印页
     *
     * @return
     */
    @ApiOperation(value = "定期取款和通知取款电子回单表打印页")
    @LogMessage(content = "查询账户转换电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewEDrawMoneyBill/{id}")
    public AjaxJson<ETransBillPageVO> viewEDrawMoneyBill(@PathVariable("id") String id, Model model) {
        ETransBillPageVO pageVO = new ETransBillPageVO();
        DataTablesInput input=new DataTablesInput();
        input.addFilter(MatchTypeEnum.EQ,"id",id);
        DataTablesOutput output=eTransBillService.datatables(input, this.getCurrentUser());
        List<ETransBillVO>  eTransBillVOS=output.getData();
        ETransBillVO eTransBillVO=eTransBillVOS.get(0);
        pageVO.setETransBillPage(eTransBillVO);
        if(!EmptyUtils.isEmpty(eTransBillVO)) {
            String netId = eTransBillVO.getCorpId();
            String billType = eTransBillVO.getBillType();
            if(EmptyUtils.isNotEmpty(netId) && EmptyUtils.isNotEmpty(billType)){
                EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType,netId);
                pageVO.setEVisaTypePage(eVisaType);
            }
        }
        return success(pageVO);
    }
    /**
     * 定期存单和通知存单电子回单表打印页
     *
     * @return
     */
    @ApiOperation(value = "定期存单和通知存单电子回单表打印页")
    @LogMessage(content = "查询账户转换电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewEDepositBill/{id}")
    public AjaxJson<ETransBillPageVO> viewDepositMoneyBill(@PathVariable("id") String id) {
        ETransBillPageVO pageVO = new ETransBillPageVO();
        DataTablesInput input=new DataTablesInput();
        input.addFilter(MatchTypeEnum.EQ,"id",id);
        DataTablesOutput output=eTransBillService.datatables(input, this.getCurrentUser());
        List<ETransBillVO>  eTransBillVOS=output.getData();
        ETransBillVO eTransBillVO=eTransBillVOS.get(0);
        pageVO.setETransBillPage(eTransBillVO);
        if(!EmptyUtils.isEmpty(eTransBillVO)) {
            String netId = eTransBillVO.getCorpId();
            String billType = eTransBillVO.getBillType();
            if(EmptyUtils.isNotEmpty(netId) && EmptyUtils.isNotEmpty(billType)){
                EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType,netId);
                pageVO.setEVisaTypePage(eVisaType);
            }
        }
        return success(pageVO);
    }
}
