package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.vo.EAccrualBillDetailVO;
import com.bytter.biz.receipt.entity.EAccrualBill;
import com.bytter.biz.receipt.service.EAccrualBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.EAccrualBillVO;
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
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 利息清单电子回单
 * @date 2017-07-04 15:27:13
 */
@Api(value = "/v1-payment/receipt/eaccrualbill",tags = "电子回单查询")
@RestController("v1EAccrualBillController")
@RequestMapping("/v1-payment/receipt/eaccrualbill")
public class EAccrualBillController extends BaseController {
    @Autowired
    private EAccrualBillService eAccrualBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    /**
     * 利息清单电子回单 table列表
     *
     * @return
     */
    @ApiOperation("利息清单电子回单 table列表")
    @PostMapping("/datatable")

    public AjaxJson<DataTablesOutput<EAccrualBillVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success( eAccrualBillService.listForUnion(input, this.getCurrentUser()));
    }


    /**
     * 利息清单电子回单编辑页面跳转
     *
     * @return
     */
    @ApiOperation("利息清单电子回单编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<EAccrualBill> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            AjaxJson.success(eAccrualBillService.get(id));
        }
        return AjaxJson.success();
    }
    /**
     * 利息清单电子回单详情页
     *
     * @return
     */
    @ApiOperation("利息清单电子回单详情页")
    @LogMessage(content = "查询利息清单电子回单详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public AjaxJson<EAccrualBillDetailVO> view(@PathVariable("id") String id, @PathVariable("opType") String opType) {
        EAccrualBillDetailVO model=new EAccrualBillDetailVO();
        EAccrualBillVO eAccrualBill = eAccrualBillService.getVOByBillNo(null, id);
        Map<String, BtCurrencyDTO> mapCur = currencyService.getCurrency();
        boolean flag = false;
        if (!EmptyUtils.isEmpty(eAccrualBill)) {
            if (EmptyUtils.isNotEmpty(eAccrualBill.getBillNo())) {
                List<FcInterestDtlDTO> dtlDTOList = eAccrualBillService.getFcInterestDtlDTOByOrderNo(eAccrualBill.getBillNo());
                if (EmptyUtils.isEmpty(dtlDTOList)) {
                    flag = true;
                }
                //本金
                BigDecimal money = new BigDecimal(0);
                //利息
                BigDecimal nummoney = new BigDecimal(0);
                for (FcInterestDtlDTO fcInterestDtlDTO : dtlDTOList) {
                    nummoney = nummoney.add(fcInterestDtlDTO.getInterest());
                }
                //贷款利息单重新计算本金
                if (eAccrualBill.getBillType().equals(ReceiptConstant.RECEIPT_BILL_TYPE_02)) {
                    money = this.eAccrualBillService.getMoney(eAccrualBill.getAccNo(), eAccrualBill.getStartDate());
                    eAccrualBill.setMoney(money);
                }
                eAccrualBill.setNummoney(nummoney);
                model.setDtlDTOList( dtlDTOList);
            }
            String netId = eAccrualBill.getNetId();
            String billType = eAccrualBill.getBillType();
            if (EmptyUtils.isNotEmpty(netId) && EmptyUtils.isNotEmpty(billType)) {
                EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                model.setEVisaTypePage( eVisaType);
            }
            if(StringUtils.isNotEmpty(eAccrualBill.getTallierCode()) ){
                com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eAccrualBill.getTallierCode());
                eAccrualBill.setTallierName(userDTO.getUsername());
            }
            if(StringUtils.isNotEmpty(eAccrualBill.getCheckerCode()) ){
                com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eAccrualBill.getCheckerCode());
                eAccrualBill.setCheckerName(userDTO.getUsername());
            }
            // 获取操作日结的用户
            UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(eAccrualBill.getNetId(), eAccrualBill.getTallyDate());
            if (dayOverOperatorUser != null) {
                eAccrualBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
            }
        }
        if(!StringUtils.isEmpty(eAccrualBill.getCurId())) {
        	if(!ObjectUtils.isEmpty(mapCur.get(eAccrualBill.getCurId()))) {
        		eAccrualBill.setCurName(mapCur.get(eAccrualBill.getCurId()).getCurName());
        	}
        }
        //操作类型：0-打印预览 1-打印
        model.setOpType(  opType);
        model.setEAccrualBillPage(  eAccrualBill);
        return AjaxJson.success(model);
    }
    /**
     * 添加利息清单电子回单
     *
     * @param eAccrualBill
     * @return
     */
    @ApiOperation("添加利息清单电子回单")
    @LogMessage(content = "添加利息清单电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody EAccrualBill eAccrualBill) {
        eAccrualBillService.save(eAccrualBill);
        return success();
    }
    /**
     * 修改利息清单电子回单
     *
     * @param eAccrualBill
     * @return
     */
    @ApiOperation("修改利息清单电子回单")
    @LogMessage(content = "修改利息清单电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody EAccrualBill eAccrualBill) {
        EAccrualBill t = eAccrualBillService.get(eAccrualBill.getId());
        BeanUtils.copyNotNullProperties(eAccrualBill, t);
        eAccrualBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除利息清单电子回单
     *
     * @param id
     * @return
     */
    @ApiOperation("删除利息清单电子回单")
    @LogMessage(content = "删除利息清单电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        EAccrualBill eAccrualBill = eAccrualBillService.get(id);
        eAccrualBillService.deleteEntity(eAccrualBill);
        return success();
    }
    /**
     * 批量删除利息清单电子回单
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除利息清单电子回单")
    @LogMessage(content = "批量删除利息清单电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            EAccrualBill eAccrualBill = eAccrualBillService.get(id);
            eAccrualBillService.deleteEntity(eAccrualBill);
        }
        return success();
    }
    /**
     * 存款利息清单电子回单 table列表
     *
     * @return
     */
    @ApiOperation(" 存款利息清单电子回单 table列表")
    @PostMapping("/depositIstlist")
    public AjaxJson<DataTablesOutput<EAccrualBillVO>> depositIstlist(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(eAccrualBillService.depositIstlist(input));
    }
    /**
     * 存款利息清单电子回单详情页
     *
     * @return
     */
    @LogMessage(content = "查询存款利息清单电子回单详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDepositIst/{id}/{opType}")
    public AjaxJson<EAccrualBillDetailVO> viewEDepositIst(@PathVariable("id") String id, @PathVariable("opType") String opType) {
        EAccrualBillDetailVO model=new EAccrualBillDetailVO();
        DataTablesInput input = new DataTablesInput();
        input.addFilter(MatchTypeEnum.EQ, "id", id);
        DataTablesOutput<EAccrualBillVO> output = eAccrualBillService.depositIstlist(input);
        List<EAccrualBillVO> eAccrualBillVOS = output.getData();
        EAccrualBillVO eAccrualBillVO = eAccrualBillVOS.get(0);
        model.setEAccrualBillPage(  eAccrualBillVO);
        if (!EmptyUtils.isEmpty(eAccrualBillVO)) {
            String corpId = eAccrualBillVO.getNetId();
            String billType = eAccrualBillVO.getBillType();
            if (EmptyUtils.isNotEmpty(corpId) && EmptyUtils.isNotEmpty(billType)) {
                String netId = corpService.getNetByCorpId(corpId).getId();
                EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                model.setEVisaTypePage( eVisaType);
            }
        }
        //操作类型：0-打印预览 1-打印
        model.setOpType( opType);
        return AjaxJson.success(model);
    }
}
