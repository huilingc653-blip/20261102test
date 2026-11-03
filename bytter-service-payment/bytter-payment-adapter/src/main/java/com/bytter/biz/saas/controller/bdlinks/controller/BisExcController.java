package com.bytter.biz.saas.controller.bdlinks.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlinks.entity.BisExc;
import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.bdlinks.vo.v1.BisExcConfigVO;
import com.bytter.biz.bdlinks.vo.v1.BisExcPageVO;
import com.bytter.biz.bdlinks.vo.v1.BisExcViewPayResultPageVO;
import com.bytter.biz.finacial.api.dto.FcAccbookCloseDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.dto.NisExpenseDetailDTO;
import com.bytter.biz.payment.api.dto.NisWageDetailDTO;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.biz.payment.api.service.INisExpenseDetailService;
import com.bytter.biz.payment.api.service.INisWageService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.enums.UserMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 付款指令表 TODO
 * @date 2017-06-28 16:31:56
 */
@Api(value = "付款指令表",tags = "付款指令表")
@RestController("v1BisExcController")
@RequestMapping("/v1-payment/bdlink/bisexc")
public class BisExcController extends BaseController {
    @Autowired
    private BisExcService bisExcService;
    @DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
    @DubboReference(check = false)
	private INisExpenseDetailService nisExpenseDetailService;
    @DubboReference(check = false)
	private IConfigService configService;
//    @Resource
//    private PasswordHash passwordHash;
    @DubboReference(check = false)
	private IUserService userService;
    @DubboReference(check = false)
	private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
	private INisWageService nisWageService;
    @DubboReference(check = false)
	private INisAllocationReqService nisAllocationReqService;

    @ApiOperation("获取列表配置信息listConfig 主要获取isMathcFailReason信息")
    @LogMessage(content = "获取列表配置信息listConfig", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/getListConfig")
    public AjaxJson<BisExcConfigVO> getListConfig() {
        BisExcConfigVO configVO = new BisExcConfigVO();
        //是否匹配交易失败原因
        configVO.setIsMathcFailReason(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISMATHCFAILREASON).getValue());
        configVO.setStartDate(DateUtils.formatDate(DateUtils.getDateYMD(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return success(configVO);
    }
    /**
     * 付款指令表 table列表
     * 【指令追踪】--查询
     * @return
     */
    @ApiOperation("付款指令表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisExcVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisExcService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 付款指令表详情页
     *
     * @return
     */
    @ApiOperation(value = "查询付款指令表详情"
                ,notes = "原先的/view/{id} 和 /viewExcProc/{id} /viewDefeat/{id}同用这一个接口")
    @LogMessage(content = "查询付款指令表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BisExcPageVO> view(@PathVariable("id") String id) {
        BisExcPageVO pageVO = new BisExcPageVO();
        BisExcVO bisExc = bisExcService.getByIdUnion(id);
        pageVO.setBisExcPage(bisExc);
        pageVO.setBusType(NisMessageConstant.BUS_TYPE_BDLINKPAY);
        //转付查询下拨指令信息
        String showBisExcProc = CommonConstant.NO;
        if(NetbankConstant.PAY_TYPE_02.equals(bisExc.getSysVoucherCode())){
            String voucherVoProc = bisExc.getVoucherNo().substring(0,2)+"3"+bisExc.getVoucherNo().substring(3);
            BisExcVO bisExcProc = bisExcService.getSpecFailForTranPay(voucherVoProc, bisExc.getCbsBillId());
            if(bisExcProc != null){
                showBisExcProc = CommonConstant.YES;
                //中间付款信息
                pageVO.setBisExcProcPage(bisExcProc);
            }
        }
        String rmk = "";
        if (StringUtils.isNotBlank(bisExc.getCbsBillId())) {
            if (BdlinkConstant.FROM_NIS_PAY.equals(bisExc.getVoucherFrom())) {
                NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(bisExc.getCbsBillId());
                if (nisBillheadDTO != null) {
                    rmk = nisBillheadDTO.getRmk();
                }
            } else if (BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN.equals(bisExc.getVoucherFrom())
                    || BdlinkConstant.FROM_ALLOCATION_TYPE_ASK.equals(bisExc.getVoucherFrom())
                    || BdlinkConstant.FROM_ALLOCATION_TYPE_POS.equals(bisExc.getVoucherFrom())) {
                NisAllocationReqDTO nisAllocationReqDTO = nisAllocationReqService.getNisAllocationReqByBillNo(bisExc.getCbsBillNo());
                if (nisAllocationReqDTO != null) {
                    rmk = nisAllocationReqDTO.getRmk();
                }
            }
        }
        pageVO.setRmk(rmk);
        pageVO.setShowBisExcProc(showBisExcProc);
        return success(pageVO);
    }

    /**
     * 付款指令表详情页
     *
     * @return
     */
    @ApiOperation("根据ID获取付款指令信息，在进入编辑页面前调用")
    @LogMessage(content = "付款指令编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/getById")
    public AjaxJson<BisExcPageVO> getById(String id) {
        BisExcPageVO pageVO = new BisExcPageVO();
        BisExcVO bisExc = bisExcService.getByIdUnion(id);
        pageVO.setBisExcPage(bisExc);
        pageVO.setBusType(NisMessageConstant.BUS_TYPE_BDLINKPAY);
       return success(pageVO);
    }
    /**
     * 付款指令表详情页
     *
     * @return
     */
    @ApiOperation("根据VoucherNo查询付款指令详情")
    @LogMessage(content = "查询付款指令表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByVoucherNo/{voucherNo}")
    public AjaxJson<BisExcPageVO> viewByVoucherNo(@PathVariable("voucherNo") String voucherNo) {
        BisExcPageVO pageVO = new BisExcPageVO();
        BisExcVO bisExc = bisExcService.getByVoucherNo(voucherNo);
        pageVO.setBisExcPage(bisExc);
        pageVO.setBusType(NisMessageConstant.BUS_TYPE_BDLINKPAY);
        String rmk = "";
        if (StringUtils.isNotBlank(bisExc.getCbsBillId())) {
            if (BdlinkConstant.FROM_NIS_PAY.equals(bisExc.getVoucherFrom())) {
                NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadById(bisExc.getCbsBillId());
                if (nisBillheadDTO != null) {
                    rmk = nisBillheadDTO.getRmk();
                }
            } else if (BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN.equals(bisExc.getVoucherFrom())
                    || BdlinkConstant.FROM_ALLOCATION_TYPE_ASK.equals(bisExc.getVoucherFrom())
                    || BdlinkConstant.FROM_ALLOCATION_TYPE_POS.equals(bisExc.getVoucherFrom())) {
                NisAllocationReqDTO nisAllocationReqDTO = nisAllocationReqService.getNisAllocationReqByBillNo(bisExc.getCbsBillNo());
                if (nisAllocationReqDTO != null) {
                    rmk = nisAllocationReqDTO.getRmk();
                }
            }
        }
        pageVO.setRmk(rmk);
        return success(pageVO);
    }

    /**
     * 保存失败明细到付款指令表
     *
     * @param bisExc
     * @return
     */
    @ApiOperation("保存失败明细到付款指令表")
    @LogMessage(content = "保存失败明细到付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveFailReason")
    @ResponseBody
    public AjaxJson saveFailReason(BisExc bisExc) {
        BisExc entity = bisExcService.get(bisExc.getId(), true);
        //失败状态才可以提交
        if (entity != null && !BdlinkConstant.BANK_FAILED.equals(entity.getVoucherStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(entity.getVoucherStat())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        //更新失败原因
        entity.setFailReason(bisExc.getFailReason());
        bisExcService.saveOrUpdate(entity);
        bisExcService.defeatBisExc(entity, this.getCurrentUser());
        return success();
    }
    /**
     * 发送付款指令表
     * 【发送指令】
     * 【指令发送】
     * @param id
     * @return
     */
    @ApiOperation("发送付款指令")
    @LogMessage(content = "发送付款指令", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/send/{id}")
    @ResponseBody
    public AjaxJson send(@PathVariable("id") String id) {
        BisExc bisExc = bisExcService.get(id, true);
        //审批通过和待发送状态才可以提交
        if (bisExc != null && !BdlinkConstant.WAIT_FILL.equals(bisExc.getVoucherStat()) && !BdlinkConstant.PASS.equals(bisExc.getVoucherStat())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        bisExcService.sendBisExc(bisExc, this.getCurrentUser());
        return success();
    }
    /**
     * 发送付款指令表
     *
     * @param id
     * @return
     */
    @ApiOperation("sendAndUpdate发送付款指令")
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/sendAndUpdate/{id}/{isQuick}/{difBank}/{areaSign}/{purpose}/{addWord}")
    @ResponseBody
    public AjaxJson sendAndUpdate(@PathVariable("id") String id, @PathVariable("isQuick") String isQuick
    ,@PathVariable("difBank") String difBank,@PathVariable("areaSign") String areaSign
            ,@PathVariable("purpose") String purpose,@PathVariable("addWord") String addWord) {
        BisExc bisExc = bisExcService.get(id, true);
        bisExc.setIsQuick(isQuick);
        if(EmptyUtils.isNotEmpty(difBank)){
            bisExc.setDifBank(difBank);
        }
        if(EmptyUtils.isNotEmpty(areaSign)){
            bisExc.setAreaSign(areaSign);
        }
        if(EmptyUtils.isNotEmpty(purpose)){
            bisExc.setPurpose(purpose);
        }
        if(EmptyUtils.isNotEmpty(addWord)){
            bisExc.setAddWord(addWord);
        }
        //审批通过和待发送状态才可以提交
        if (bisExc != null && !BdlinkConstant.WAIT_FILL.equals(bisExc.getVoucherStat()) && !BdlinkConstant.PASS.equals(bisExc.getVoucherStat())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        bisExcService.sendBisExc(bisExc, this.getCurrentUser());
        return success();
    }
    /**
     * 批量发送付款指令表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量发送付款指令表")
    @LogMessage(content = "批量发送付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSend")
    @ResponseBody
    public AjaxJson batchSend(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisExc bisExc = bisExcService.get(id, true);
            //审批通过和待发送状态才可以提交
            if (bisExc != null && !BdlinkConstant.WAIT_FILL.equals(bisExc.getVoucherStat()) && !BdlinkConstant.PASS.equals(bisExc.getVoucherStat())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
            }
            bisExcService.sendBisExc(bisExc, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 退回付款指令表
     *
     * @param id
     * @return
     */
    @ApiOperation("退回付款指令表")
    @LogMessage(content = "退回付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(@RequestParam("id") String id, @RequestParam("backMsg") String backMsg) {
        BisExc bisExc = bisExcService.get(id, true);
        //准备发送和交易成功、等待结果、指令已消费的指令不可以退回
        if (BdlinkConstant.PRE_SEND.equals(bisExc.getVoucherStat()) || BdlinkConstant.SUCCESS.equals(bisExc.getVoucherStat())
                || BdlinkConstant.WAIT_RESULT.equals(bisExc.getVoucherStat()) || BdlinkConstant.CONSUMPTION.equals(bisExc.getVoucherStat())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        bisExcService.backBisExc(bisExc, backMsg, this.getCurrentUser());
        return success();
    }
    /**
     * 批量退回付款指令表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量退回付款指令表")
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchBack")
    @ResponseBody
    public AjaxJson batchBack(@RequestParam("ids") String ids, @RequestParam("backMsg") String backMsg) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisExc bisExc = bisExcService.get(id, true);
            //准备发送和交易成功、等待结果、指令已消费的指令不可以退回
            if (BdlinkConstant.PRE_SEND.equals(bisExc.getVoucherStat()) || BdlinkConstant.SUCCESS.equals(bisExc.getVoucherStat())
                    || BdlinkConstant.WAIT_RESULT.equals(bisExc.getVoucherStat()) || BdlinkConstant.CONSUMPTION.equals(bisExc.getVoucherStat())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
            }
            bisExcService.backBisExc(bisExc, backMsg, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 重发付款指令表
     * 【指令重发】
     * @param id
     * @return
     */
    @ApiOperation("【指令重发】 重发付款指令表 ")
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/resend/{id}")
    @ResponseBody
    public AjaxJson resend(@PathVariable("id") String id) {
        BisExc bisExc = bisExcService.get(id, true);
        //失败状态才可以提交
        if (bisExc != null && !BdlinkConstant.BANK_FAILED.equals(bisExc.getVoucherStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisExc.getVoucherStat())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        bisExcService.resendBisExc(bisExc, this.getCurrentUser());
        return success();
    }
    /**
     * 批量重发付款指令表
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISEXC_TRACERESEND)
    @ApiOperation("批量重发付款指令表")
    @LogMessage(content = "批量重发付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchResend")
    @ResponseBody
    public AjaxJson batchResend(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisExc bisExc = bisExcService.get(id, true);
            //失败状态才可以提交
            if (bisExc != null && !BdlinkConstant.BANK_FAILED.equals(bisExc.getVoucherStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisExc.getVoucherStat())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
            }
            bisExcService.resendBisExc(bisExc, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 作废付款指令表
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISEXC_TRACEDEFEAT)
    @ApiOperation("作废付款指令")
    @LogMessage(content = "作废付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/defeat/{id}")
    @ResponseBody
    public AjaxJson defeat(@PathVariable("id") String id) {
        BisExc bisExc = bisExcService.get(id, true);
        //失败状态才可以提交
        if (bisExc != null && !BdlinkConstant.BANK_FAILED.equals(bisExc.getVoucherStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisExc.getVoucherStat())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        bisExcService.defeatBisExc(bisExc, this.getCurrentUser());
        return success();
    }
    /**
     * 批量作废付款指令表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量作废付款指令")
    @LogMessage(content = "批量作废付款指令", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchDefeat")
    @ResponseBody
    public AjaxJson batchDefeat(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisExc bisExc = bisExcService.get(id, true);
            //失败状态才可以提交
            if (bisExc != null && !BdlinkConstant.BANK_FAILED.equals(bisExc.getVoucherStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisExc.getVoucherStat())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
            }
            bisExcService.defeatBisExc(bisExc, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 添加付款指令表
     *
     * @param bisExc
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISEXC_SALEPAYMENTAPPLY)
    @ApiOperation("添加付款指令")
    @LogMessage(content = "添加付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisExc bisExc) {
        //收付款账号不能相同
        if (bisExc.getPayerAcc().equals(bisExc.getPayeeAcc())) {
            throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
        }
        bisExcService.addBisExc(bisExc, this.getCurrentUser());
        return success();
    }
    /**
     * 修改付款指令表
     *
     * @param bisExc
     * @return
     */
    @ApiOperation("修改付款指令表")
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BisExcVO bisExc) {
        BisExc t = bisExcService.get(bisExc.getId());
        //并发校验
        if (!BdlinkConstant.STATUE_TEMP.equals(t.getStatus()) && !BdlinkConstant.STATUE_BACK.equals(t.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, t.getVoucherNo());
        }
        //收付款账号不能相同
        if (bisExc.getPayerAcc().equals(bisExc.getPayeeAcc())) {
            throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
        }
        bisExc.setVoucherFrom(BdlinkConstant.FROM_BIS_EXC_PAY);
        bisExcService.updateBisExc(bisExc, this.getCurrentUser());
        return success();
    }
    /**
     * 删除付款指令表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除付款指令表")
    @LogMessage(content = "删除付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        //并发校验
        BisExc bisExc = bisExcService.get(id);
        if (!BdlinkConstant.STATUE_TEMP.equals(bisExc.getStatus()) && !BdlinkConstant.STATUE_BACK.equals(bisExc.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        bisExcService.deleteBisExc(id, this.getCurrentUser());
        return success();
    }
    /**
     * 批量删除付款指令表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除付款指令表")
    @LogMessage(content = "批量删除付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisExc bisExc = bisExcService.get(id, true);
            //并发校验
            if (!BdlinkConstant.STATUE_TEMP.equals(bisExc.getStatus()) && !BdlinkConstant.STATUE_BACK.equals(bisExc.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
            }
        }
        bisExcService.batchDelBisExc(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 批量提交付款通知表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量提交付款指令表")
    @LogMessage(content = "批量提交付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        List<BisExc> list = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisExc bisExc = bisExcService.get(id, true);
            //并发校验
            if (!BdlinkConstant.STATUE_TEMP.equals(bisExc.getStatus()) && !BdlinkConstant.STATUE_BACK.equals(bisExc.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
            }
            list.add(bisExc);
        }
        bisExcService.batchSubmitBisExc(list, this.getCurrentUser());
        return success();
    }
    /**
     * 获取付款账号
     *
     * @param corpId
     * @param flag
     * @return
     */
    @ApiOperation("获取付款账号")
    @PostMapping("/findBankAcc")
    @ResponseBody
    public AjaxJson<List<BtBankAccDTO>> findBankAcc(String corpId, String curId, String bifCode, String voucherTypeId, String payerAcc, String flag) {
        return success(bisExcService.findBankAcc(corpId, curId, bifCode, voucherTypeId, payerAcc, flag));
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */

    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput<WorkFlowPersistence>> dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return success(this.bisExcService.dataTableApprove(dataTablesInput));
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return String
     */
    @ApiOperation("根据ID获取审核详情信息")
    @GetMapping("/approve")
    @ResponseBody
    public AjaxJson<BisExcPageVO> approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            BisExcPageVO pageVO = new BisExcPageVO();
            BisExcVO bisExcVO = this.bisExcService.getByIdUnion(id);
            pageVO.setBisExcPage(bisExcVO);
            pageVO.setTaskId(taskId);
            pageVO.setBusType(NisMessageConstant.BUS_TYPE_BDLINKPAY);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @ApiOperation("获取手机审核详情信息")
    @GetMapping("/mobileApprove")
    @ResponseBody
    public AjaxJson<BisExcPageVO> mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            BisExcPageVO pageVO = new BisExcPageVO();
            BisExcVO bisExcVO = this.bisExcService.getByIdUnion(businessId);
            pageVO.setBisExcPage(bisExcVO);
            pageVO.setTaskId(taskId);
            pageVO.setBusType(NisMessageConstant.BUS_TYPE_BDLINKPAY);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 判断指令是否记账
     *
     * @param cbsBillNo
     * @param busOriType
     * @return
     */
    @ApiOperation(value = "判断指令是否记账"
            ,notes = "返回结果：\n" +
            "0-可退回/作废\n" +
            "1-业务单{0}的凭证需作废，请先处理账务\n" +
            "2-业务单{0}的凭证记账日期已进行日结需冲账，请先处理账务\n" +
            "3-业务单{0}存在等待结果的对私明细，请待银行确定交易状态之后处理\n" +
            "4-业务单{0}退回可选择退回到制单处或者记账处\n" +
            "其他-业务单{0}有{1}笔明细失败，失败金额合计{2}，需做手工账冲销，请确认是否已进行账务处理")
    @PostMapping(path = "/getFcAccBook")
    @ResponseBody
    public AjaxJson getFcAccBook(String cbsBillNo, String cbsBillId, String voucherFrom, Integer busOriType) {
        BisExcVO bisExcVO = this.bisExcService.getByCbsBillNo(cbsBillNo, voucherFrom);
        //返回结果：
        //0-可退回/作废
        //1-业务单{0}的凭证需作废，请先处理账务
        //2-业务单{0}的凭证记账日期已进行日结需冲账，请先处理账务
        //3-业务单{0}存在等待结果的对私明细，请待银行确定交易状态之后处理
        //4-业务单{0}退回可选择退回到制单处或者记账处
        //其他-业务单{0}有{1}笔明细失败，失败金额合计{2}，需做手工账冲销，请确认是否已进行账务处理
        String result = "0";
        if (BdlinkConstant.TRANS_MODE_PRIVATE.equals(bisExcVO.getTransMode())) {
            //获取对私付款明细
            List<NisExpenseDetailDTO> listDtl = nisExpenseDetailService.listByBillId(bisExcVO.getCbsBillId());
            if (!EmptyUtils.isEmptyList(listDtl)) {
                for (NisExpenseDetailDTO dtl : listDtl) {
                    if(BdlinkConstant.WAIT_RESULT.equals(dtl.getVoucherStat())){
                        result = "3";
                        break;
                    }
                }
            }
        }
        if("0".equals(result)) {
            FcAccbookCloseDTO fcAccbookCloseDTO = fcAccbookService.getAccbookCloseDTO(cbsBillId, busOriType);
            //判断是否记过账，且还未处理账务
            if (fcAccbookCloseDTO != null && !StringUtils.isBlank(fcAccbookCloseDTO.getBookId())) {
                //是否已作废或已冲账 0 : 已冲账 已作废， 1：凭证正在作废流程中 2：凭证正在冲账流程中 3：可冲账或作废处理
                if("0".equals(fcAccbookCloseDTO.getDealFlag())) {
                    result = "4";
                } else {
                    //判断是否对私付款
                    boolean isPrivate = false;
                    if (BdlinkConstant.FROM_NIS_PAY.equals(voucherFrom)) {
                        if (BdlinkConstant.TRANS_MODE_PRIVATE.equals(bisExcVO.getTransMode())) {
                            isPrivate = true;
                            //获取失败的对私付款明细
                            List<NisExpenseDetailDTO> listDtl = nisExpenseDetailService.listByBillIdFailure(bisExcVO.getCbsBillId());
                            if (!EmptyUtils.isEmptyList(listDtl)) {
                                BigDecimal payMoney = BigDecimal.ZERO;
                                for (NisExpenseDetailDTO dtl : listDtl) {
                                    payMoney = payMoney.add(dtl.getExpenseMoney());
                                }
                                if (payMoney.compareTo(bisExcVO.getPayMoney()) == 0) {
                                    //整笔失败
                                    //不存在失败明细单打回，及还未经过银行处理，提示作废账务
                                    //是否已日结 true :已日结 false ： 未日结
                                    if (fcAccbookCloseDTO.isCloseFlag() && fcAccbookCloseDTO.getCloseDate().compareTo(fcAccbookCloseDTO.getTallyDate()) >= 0) {
                                        result = "2";
                                    } else {
                                        result = "1";
                                    }
                                } else {
                                    result = listDtl.size() + "," + payMoney;
                                }
                            } else {
                                //不存在失败明细单打回，及还未经过银行处理，提示作废账务
                                //是否已日结 true :已日结 false ： 未日结
                                if (fcAccbookCloseDTO.isCloseFlag() && fcAccbookCloseDTO.getCloseDate().compareTo(fcAccbookCloseDTO.getTallyDate()) >= 0) {
                                    result = "2";
                                } else {
                                    result = "1";
                                }
                            }
                        }
                    }
                    if (!isPrivate) {
                        //是否已日结 true :已日结 false ： 未日结
                        if (fcAccbookCloseDTO.isCloseFlag() && fcAccbookCloseDTO.getCloseDate().compareTo(fcAccbookCloseDTO.getTallyDate()) >= 0) {
                            result = "2";
                        } else {
                            result = "1";
                        }
                    }
                }
            }
        }
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(result);
        return j;
    }
    /**
     * 批量提交时重复付款校验
     *
     * @param id
     * @return
     */
    @ApiOperation("提交时重复付款校验")
    @PostMapping(path = "/rePay")
    @ResponseBody
    public AjaxJson rePay(String id, String type) {
        AjaxJson json = new AjaxJson();
        // 校验发送后的指令是否出现重复数据
        BisExc entity = this.bisExcService.get(id);
        boolean flag = bisExcService.isExistRepay(entity.getCorpId(), entity.getPayMoney(), entity.getPayeeAcc(), entity.getCurId(), id, entity.getWishPayday(), type);
        json.setSuccess(flag);
        if(flag){
            json.setMessageEnum(NetbankMessageEnum.BIS_EXC_MESSAGE_SEND_REPAY);
        }
        json.setObj(entity);
        return json;
    }
    /**
     * 批量提交时重复付款校验
     *
     * @param ids 单位
     * @return
     */
    @ApiOperation("批量提交时重复付款校验")
    @PostMapping(path = "/batchRePay")
    @ResponseBody
    public AjaxJson batchRePay(String ids, String type) {
        AjaxJson json = new AjaxJson();
        boolean flag = false;
        BisExc errorExc = new BisExc();
        // 校验发送后的指令是否出现重复数据
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisExc entity = this.bisExcService.get(id);
            flag = bisExcService.isExistBatchRepay(entity.getCorpId(), entity.getPayMoney(), entity.getPayeeAcc(), entity.getCurId(), id, entity.getWishPayday(), "", "", type);
            if (flag) {
                BeanUtils.copyNotNullProperties(entity, errorExc);
                json.setMessageEnum(NetbankMessageEnum.BIS_EXC_MESSAGE_SEND_BATCHREPAY);
                break;
            }
        }
        // 校验待发送指令是否出现重复数据
        if (!flag) {
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                BisExc entity = this.bisExcService.get(id);
                String voucherStat = "trace".equalsIgnoreCase(type) ? "'" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "'" : "'" + BdlinkConstant.WAIT_FILL + "'";
                flag = bisExcService.isExistBatchRepay(entity.getCorpId(), entity.getPayMoney(), entity.getPayeeAcc(), entity.getCurId(), id, entity.getWishPayday(), voucherStat, "", type);
                if (flag) {
                    BeanUtils.copyNotNullProperties(entity, errorExc);
                    json.setMessageEnum(NetbankMessageEnum.BIS_EXC_MESSAGE_SEND_BATCHREPAY_WAIT_FILL);
                    break;
                }
            }
        }
        if(!flag){
            json.setObj(null);
            json.setMessageEnum(null);
            json.setMsg(null);
        }else{
            json.setObj(errorExc);
        }
        json.setSuccess(!flag);
        return json;
    }
    /**
     * 导出全部
     *
     * @return
     */
    @ApiOperation("导出全部")
    @PostMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput dataTablesUnion = bisExcService.getDataTablesUnion(dataTablesInput, user);
                List<BisExcVO> list = dataTablesUnion.getData();
                BisExcVO bisExc = new BisExcVO();
                bisExc.setPayMoney(BigDecimal.ZERO);
                for(BisExcVO vo:list){
                    bisExc.setPayMoney(bisExc.getPayMoney().add(vo.getPayMoney()));
                    bisExc.setVoucherNo("汇总");
                }
                list.add(bisExc);
                return dataTablesUnion.getData();
            }
        }, totalParam, fileNameParam, "付款指令查询", 5000, BisExcVO.class);
    }
    /**
     * 对私付款单明细金额与付款金额校验
     *
     * @param ids
     * @return
     */
    @ApiOperation("对私付款单明细金额与付款金额校验")
    @PostMapping(path = "/validPayMoney")
    @ResponseBody
    public AjaxJson validPayMoney(String ids) {
        AjaxJson json = AjaxJson.success();
        String cbsBillNo = "";
        //对私付款校验明细总金额与指令发送金额是否一致
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisExc bisExc = this.bisExcService.get(id, true);
            if (BdlinkConstant.FROM_NIS_PAY.equals(bisExc.getVoucherFrom())) {
                if (BdlinkConstant.TRANS_MODE_PRIVATE.equals(bisExc.getTransMode())) {
                    BigDecimal sumExpenseMoney = nisExpenseDetailService.getSumExpenseMoneyByBillId(bisExc.getCbsBillId());
                    if (sumExpenseMoney.compareTo(BigDecimal.ZERO)>0 && sumExpenseMoney.compareTo(bisExc.getPayMoney()) != 0) {
                        cbsBillNo = cbsBillNo + bisExc.getCbsBillNo() + CommonConstant.COMMA_SPLIT;
                    }
                }
            }
        }
        if (!StringUtils.isBlank(cbsBillNo)) {
            json.setObj(cbsBillNo.substring(0, cbsBillNo.length() - 1));
        } else {
            json.setObj(false);
        }
        return json;
    }
    /**
     * 失败指令结果查询
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISEXC_TRACEQUERY)
    @ApiOperation("失败指令结果查询")
    @LogMessage(content = "失败指令结果查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/query/{id}")
    @ResponseBody
    public AjaxJson query(@PathVariable("id") String id) {
        BisExc bisExc = bisExcService.get(id, true);
        //失败状态才可以提交
        if (bisExc != null && !BdlinkConstant.BANK_FAILED.equals(bisExc.getVoucherStat())
                && !BdlinkConstant.INTERFACE_FAILED.equals(bisExc.getVoucherStat())
                && !BdlinkConstant.CONSUMPTION.equals(bisExc.getVoucherStat())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
        }
        bisExcService.queryResultBisExc(bisExc, this.getCurrentUser());
        return success();
    }
    /**
     * 批量结果查询
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量失败指令结果查询")
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchQuery")
    @ResponseBody
    public AjaxJson batchQuery(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisExc bisExc = bisExcService.get(id, true);
            //失败状态才可以提交
            if (bisExc != null && !BdlinkConstant.BANK_FAILED.equals(bisExc.getVoucherStat())
                    && !BdlinkConstant.INTERFACE_FAILED.equals(bisExc.getVoucherStat())
                    && !BdlinkConstant.CONSUMPTION.equals(bisExc.getVoucherStat())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
            }
            bisExcService.queryResultBisExc(bisExc, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 设为支付成功/失败
     *
     * @return
     */
    @ApiOperation("设为支付成功/失败 页面")
    @LogMessage(content = "设为支付成功/失败 页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/setPayResult/{id}/{voucherStat}/{excFrom}")
    @ResponseBody
    public AjaxJson<BisExcViewPayResultPageVO> setPayResult(@PathVariable("id") String id, @PathVariable("voucherStat") String voucherStat,
                                                            @PathVariable("excFrom") String excFrom, Model model) {
        BisExcViewPayResultPageVO pageVO = new BisExcViewPayResultPageVO();
        BisExcVO bisExc = null;
        String hasExpenseDetail = "0";
        if(CommonConstant.YES.equals(excFrom)) {
            bisExc = bisExcService.getSpecFailById(id);
        } else {
            bisExc = bisExcService.getByIdUnion(id);
            if (BdlinkConstant.FROM_NIS_PAY.equals(bisExc.getVoucherFrom())) {
                //对私付款，展示对私付款明细
                if (BdlinkConstant.TRANS_MODE_PRIVATE.equals(bisExc.getTransMode())) {
                    NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadByBillNo(bisExc.getCbsBillNo());
                    //费用报销
                    if (nisBillheadDTO != null && NetbankConstant.BILLFROM_2.equals(nisBillheadDTO.getBillFrom())) {
                        List<NisExpenseDetailDTO> listNisExpenseDetail = nisExpenseDetailService.listByBillIdWaitResult(bisExc.getCbsBillId());
                        pageVO.setListNisExpenseDetail(listNisExpenseDetail);
                        pageVO.setNisBillheadPage(nisBillheadDTO);
                        if (!EmptyUtils.isEmpty(listNisExpenseDetail)) {
                            hasExpenseDetail = "1";
                        }
                    }
                }
            } else if (BdlinkConstant.FROM_NIS_WAGE.equals(bisExc.getVoucherFrom())) {
                //获取代发工资等待结果的明细
                List<NisWageDetailDTO> listNisWageDetail = nisWageService.listByVoucherNoWaitResult(bisExc.getVoucherNo(), bisExc.getCbsBillId());
                pageVO.setListNisWageDetail(listNisWageDetail);
                if (!EmptyUtils.isEmpty(listNisWageDetail)) {
                    hasExpenseDetail = "1";
                }
            }
        }
        pageVO.setHasExpenseDetail(hasExpenseDetail);
        pageVO.setBisExcPage(bisExc);
        pageVO.setVoucherStat(voucherStat);
        pageVO.setExcFrom(excFrom);
        return success(pageVO);
    }
    /**
     * 保存设置结果
     *
     * @param bisExcVo
     * @return
     */
    @ApiOperation("保存设置结果")
    @LogMessage(content = "修改付款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/savePayResult")
    @ResponseBody
    public AjaxJson savePayResult(BisExcVO bisExcVo) {
        ICurrentUser user = this.getCurrentUser();
        UserDTO userDTO = userService.getByLoginName(user.getLoginName());
        String salt = userDTO.getSalt();
        String inputPwd = DigestUtil.md5Hex(bisExcVo.getValidPassword() + salt);
        if (!inputPwd.equals(userDTO.getPassword())) {
            throw new ParameterException(UserMessageEnum.SYS_USER_PASSWORD_NOT_CORRECT);
        }
        if(CommonConstant.YES.equals(bisExcVo.getExcFrom())) {
            bisExcService.setPayResultBisExcProc(bisExcVo, user);
        } else {
            bisExcService.setPayResultBisExc(bisExcVo, user);
        }
        return success();
    }
    /**
     * 指令退回（指定环节）
     *
     * @param backIds
     * @return
     */
    @ApiOperation("指令退回（指定环节）")
    @LogMessage(content = "指令退回（指定环节）", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveBackNode")
    @ResponseBody
    public AjaxJson saveBackNode(@RequestParam("backIds") String backIds, @RequestParam("backNodeIds") String backNodeIds,
                                 @RequestParam("backNode") String backNode, @RequestParam("backMsg") String backMsg) {
        HashMap<String, String> map = new HashMap<>();
        for (String id : backNodeIds.split(CommonConstant.COMMA_SPLIT)) {
            map.put(id, backNode);
        }
        for (String id : backIds.split(CommonConstant.COMMA_SPLIT)) {
            BisExc bisExc = bisExcService.get(id, true);
            //准备发送和交易成功的指令不可以退回
            if (BdlinkConstant.PRE_SEND.equals(bisExc.getVoucherStat()) || BdlinkConstant.SUCCESS.equals(bisExc.getVoucherStat())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisExc.getVoucherNo());
            }
            if (map.containsKey(id)) {
                bisExcService.backBisExc(bisExc, backMsg, backNode, this.getCurrentUser());
            } else {
                bisExcService.backBisExc(bisExc, backMsg, this.getCurrentUser());
            }
        }
        return success();
    }
}
