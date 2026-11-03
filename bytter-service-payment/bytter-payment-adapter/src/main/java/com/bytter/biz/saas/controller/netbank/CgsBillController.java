package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.entity.CgsBill;
import com.bytter.biz.netbank.entity.NisMessage;
import com.bytter.biz.netbank.service.CgsBillService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.netbank.vo.CgsBillVO;
import com.bytter.biz.payment.api.constant.NisWorkFlowConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.workflow.api.IWorkFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托收款
 * @date 2017-05-26 11:14:59
 *
 */
@Api(value = "/v1-payment/netbank/cgsbill", tags = "委托收款")
@RestController("v1CgsBillController")
@RequestMapping("/v1-payment/netbank/cgsbill")
public class CgsBillController extends BaseController {
    @Resource
    private CgsBillService cgsBillService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private NisMessageService nisMessageService;
    @Autowired
    private IWorkFlowService workFlowServiceImpl;

    /**
     * 委托收款 table列表
     *
     * @return
     */
    @ApiOperation("委托收款 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<CgsBillVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cgsBillService.findListBysql(input, this.getCurrentUser()));
    }

    /**
     * 委托收款新增页面跳转
     *
     * @return
     */
    @ApiOperation("委托收款新增页面跳转")
    @GetMapping("/add")
    public AjaxJson<String> add() {
        return AjaxJson.success(SequenceSingleton.getSequence(SequenceEnum.CGS_BILL_CONSIGN_CODE));
    }

    /**
     * 委托收款编辑页面跳转
     *
     * @return
     */
    @ApiOperation("委托收款编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<CgsBill> update(@PathVariable("id") String id) {
        return AjaxJson.success(cgsBillService.get(id));
    }

    /**
     * 委托收款详情页
     *
     * @return
     */
    @ApiOperation("查询委托收款详情")
    @LogMessage(content = "查询委托收款详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<CgsBillVO> view(@PathVariable("id") String id) {
        return AjaxJson.success(cgsBillService.getByUnion(id));
    }

    /**
     * 数据校验
     *
     * @param cgsBill
     * @return
     */
    private boolean dataCheck(CgsBill cgsBill) {
        //收款单位和付款单位不能相同
        if (cgsBill.getPayeeCorpId().equals(cgsBill.getPayerCorpId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_CGSBILL_MESSAGE_CGSCODE_UNIQUE);
        }
        //委托收款金额不能为空且大于0
        if (EmptyUtils.isEmpty(cgsBill.getMoney())) {
            throw new ParameterException(NetbankMessageEnum.NIS_CGSBILL_MESSAGE_MONEY_NULL);
        }
        return true;
    }

    /**
     * 添加委托收款
     *
     * @param cgsBill
     * @return
     */
    @ApiOperation("添加委托收款")
    @LogMessage(content = "添加委托收款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(CgsBill cgsBill) {
        //暂存时不校验数据正确性
        //if(cgsBill.getStatus() != 0){
        dataCheck(cgsBill);
        //	}
        cgsBillService.saveCgsBill(cgsBill, false, this.getCurrentUser());
        return success();
    }

    /**
     * 修改委托收款
     *
     * @param cgsBill
     * @return
     */
    @ApiOperation("修改委托收款")
    @LogMessage(content = "修改委托收款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(CgsBill cgsBill) {
        //暂存时不校验数据正确性
        //if(cgsBill.getStatus() != 0){
        dataCheck(cgsBill);
        //}
        cgsBillService.updateCgsBill(cgsBill, this.getCurrentUser());
        return success();
    }

    /**
     * 计算手续费
     *
     * @return
     */
    public BigDecimal chargeMoney(BigDecimal money) {
        return cgsBillService.chargeMoney(money);
    }

    /**
     * 查询支付管理参数中的手续费率
     *
     * @return
     */
    @ApiOperation("查询支付管理参数中的手续费率")
    @GetMapping("getChargeRateTal")
    public AjaxJson getChargeRateTal() {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        String chargeRate = configService.getConfig("nis", "chargeRate").getValue();
        j.setObj(chargeRate);
        return j;
    }

    /**
     * 根据支付结算参数的手续费率算出手续费
     *
     * @param m
     * @return
     */
    @ApiOperation("根据支付结算参数的手续费率算出手续费")
    @GetMapping("getChargeRate")
    public AjaxJson getChargeRate(String m) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        BigDecimal chargeMoney = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(m)) {
            chargeMoney = chargeMoney(new BigDecimal(m));
        }
        j.setObj(chargeMoney);
        return j;
    }

    /**
     * 删除委托收款
     *
     * @param id
     * @return
     */
    @ApiOperation("删除委托收款")
    @LogMessage(content = "删除委托收款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        cgsBillService.deleteCgsBill(id, this.getCurrentUser());
        return success();
    }

    /**
     * 批量提交委托收款
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量提交委托收款")
    @LogMessage(content = "批量提交委托收款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchSubmit")
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            CgsBill cgsBill = cgsBillService.get(id);
            dataCheck(cgsBill);
            cgsBillService.saveCgsBill(cgsBill, true, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 批量删除委托收款
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除委托收款")
    @LogMessage(content = "批量删除委托收款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            cgsBillService.deleteCgsBill(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @ApiOperation("保存导入数据")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<CgsBillVO> list) {
        this.cgsBillService.saveImport(list, this.getCurrentUser());
        return success();
    }


    /**
     * 账号列表页面
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("账号列表页面")
    @PostMapping("/selectPayeeAcc.json")
    public DataTablesOutput<FcAccDTO> listByPayeeAccDetail(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return cgsBillService.listByPayeeAccDetail(input);
    }

    /**
     * 账号列表页面
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("账号列表页面")
    @PostMapping("/listMulAccInfoDetail")
    public DataTablesOutput<FcAccListVO> listMulAccInfoDetail(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return cgsBillService.listMulAccInfoDetail(input);
    }


    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("审批列表查询")
    @PostMapping("/dataTableApprove")
    public DataTablesOutput<WorkFlowPersistence> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        String sign = "";
        if (!EmptyUtils.isEmpty(input.getFormdata().get("sign"))) {
            String type = String.valueOf(input.getFormdata().get("sign"));
            if ("payee".equals(type)) {
                sign = NisWorkFlowConstant.NIS_CGSBILL_PAYEE;
            } else if ("payer".equals(type)) {
                sign = NisWorkFlowConstant.NIS_CGSBILL_PAYER;
            }
        }
        WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, this.getUserId());
        DataTablesOutput<CgsBillVO> cb = cgsBillService.dataTableApprove(input, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(cb);
    }

    /**
     * 审批详情页面
     *
     * @param id
     * @param taskId
     * @return
     */
    @ApiOperation("审批详情页面")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String id, String taskId) {
        Map<String, Object> model = new HashMap<>();
        CgsBillVO cgsBillVO = new CgsBillVO();
        if (StringUtils.isNotEmpty(id)) {
            cgsBillVO = cgsBillService.getByUnion(id);
            model.put("cgsBillPage", cgsBillVO);
            model.put("taskId", taskId);
            List<NisMessage> nisMessage = nisMessageService.listMessage(id, "cgs");
            model.put("nisMessageList", nisMessage);
        }
        return AjaxJson.success(model);
    }
}
