package com.bytter.biz.receipt.controller;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.receipt.entity.EAccrualBill;
import com.bytter.biz.receipt.service.EAccrualBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.EAccrualBillVO;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@Controller
@RequestMapping("/receipt/eaccrualbill")
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
     * 利息清单电子回单列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询利息清单电子回单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "receipt/elecbill/listEAccrualBill";
    }
    /**
     * 利息清单电子回单 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return eAccrualBillService.listForUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 利息清单电子回单新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(EAccrualBill eAccrualBill, Model model) {
        if (StringUtils.isNotEmpty(eAccrualBill.getId())) {
            eAccrualBill = eAccrualBillService.get(eAccrualBill.getId());
            model.addAttribute("eAccrualBillPage", eAccrualBill);
        }
        return "receipt/elecbill/addEAccrualBill";
    }
    /**
     * 利息清单电子回单编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(EAccrualBill eAccrualBill, Model model) {
        if (StringUtils.isNotEmpty(eAccrualBill.getId())) {
            eAccrualBill = eAccrualBillService.get(eAccrualBill.getId());
            model.addAttribute("eAccrualBillPage", eAccrualBill);
        }
        return "receipt/elecbill/updateEAccrualBill";
    }
    /**
     * 利息清单电子回单详情页
     *
     * @return
     */
    @LogMessage(content = "查询利息清单电子回单详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public String view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
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
                model.addAttribute("dtlDTOList", dtlDTOList);
            }
            String netId = eAccrualBill.getNetId();
            String billType = eAccrualBill.getBillType();
            if (EmptyUtils.isNotEmpty(netId) && EmptyUtils.isNotEmpty(billType)) {
                EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                model.addAttribute("eVisaTypePage", eVisaType);
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
        model.addAttribute("opType", opType);
        model.addAttribute("eAccrualBillPage", eAccrualBill);
        if (eAccrualBill.getBillType().equals(ReceiptConstant.RECEIPT_BILL_TYPE_01)) {
            return "receipt/elecbill/viewEAccrualBill3";
        }
        if (flag) {
            return "receipt/elecbill/viewEAccrualBill2";
        }
        return "receipt/elecbill/viewEAccrualBill";
    }
    /**
     * 添加利息清单电子回单
     *
     * @param eAccrualBill
     * @return
     */
    @LogMessage(content = "添加利息清单电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(EAccrualBill eAccrualBill) {
        eAccrualBillService.save(eAccrualBill);
        return success();
    }
    /**
     * 修改利息清单电子回单
     *
     * @param eAccrualBill
     * @return
     */
    @LogMessage(content = "修改利息清单电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(EAccrualBill eAccrualBill) {
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
    @LogMessage(content = "删除利息清单电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
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
    @LogMessage(content = "批量删除利息清单电子回单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
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
    @PostMapping("/depositIstlist")
    @ResponseBody
    public DataTablesOutput depositIstlist(@RequestBody DataTablesInput dataTablesInput) {
        return eAccrualBillService.depositIstlist(dataTablesInput);
    }
    /**
     * 存款利息清单电子回单详情页
     *
     * @return
     */
    @LogMessage(content = "查询存款利息清单电子回单详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDepositIst/{id}/{opType}")
    public String viewEDepositIst(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
//		EAccrualBillVO eAccrualBill = eAccrualBillService.getVOByBillNo(null,id);
//		model.addAttribute("eAccrualBillPage", eAccrualBill);
        DataTablesInput input = new DataTablesInput();
        input.addFilter(MatchTypeEnum.EQ, "id", id);
        DataTablesOutput output = eAccrualBillService.depositIstlist(input);
        List<EAccrualBillVO> eAccrualBillVOS = output.getData();
        EAccrualBillVO eAccrualBillVO = eAccrualBillVOS.get(0);
        model.addAttribute("eAccrualBillPage", eAccrualBillVO);
        if (!EmptyUtils.isEmpty(eAccrualBillVO)) {
            String corpId = eAccrualBillVO.getNetId();
            String billType = eAccrualBillVO.getBillType();
            if (EmptyUtils.isNotEmpty(corpId) && EmptyUtils.isNotEmpty(billType)) {
                String netId = corpService.getNetByCorpId(corpId).getId();
                EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
                model.addAttribute("eVisaTypePage", eVisaType);
            }
        }
        //操作类型：0-打印预览 1-打印
        model.addAttribute("opType", opType);
        return "receipt/elecbill/viewEDepositIstBill";
    }
}
