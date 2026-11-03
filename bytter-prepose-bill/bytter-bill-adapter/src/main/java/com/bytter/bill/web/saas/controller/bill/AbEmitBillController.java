package com.bytter.bill.web.saas.controller.bill;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.bill.domain.bill.vo.v1.AbEmitBillAddVo;
import com.bytter.bill.domain.bill.vo.v1.AbEmitBillPageVO;
import com.bytter.bill.domain.bill.vo.v1.AbEmitBillSaveVO;
import com.bytter.bill.domain.bill.vo.v1.MobileApproveVO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.vm.RatBkrationCommonDTO;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: 开票登记Controller
 * @Description: AB_VOUCHERINFO
 * @date 2017-06-21 15:30:56
 */
@Api(value = "/v1-preposebill/bill/accebill/abemitbill", tags = "开票Controller")
@RestController("v1AbEmitBillController")
@RequestMapping("/v1-preposebill/bill/accebill/abemitbill")
public class AbEmitBillController extends BaseController {
    @Autowired
    private AbEmitBillService abEmitBillService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbRatUrationService abRatUrationService;
    @Autowired
    private AbFbsBudgetProcessService abFbsBudgetProcessService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbFcBondHookService abFcBondHookService;
    @Autowired
    private AbGuaranteeService abGuaranteeService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbRatCpUrationService abRatCpUrationService;
    @Autowired
    private IRatUrationService iRatUrationService;
    @Autowired
    private AbFieldIsmdfService abFieldIsmdfService;
    @Autowired
    private AbBillQueryService abBillQueryService;
    @Autowired
    private AbBillBankAutoDtlService abBillBankAutoDtlService;
    @Autowired
    private IBisBifInitService iBisBifInitService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * AB_VOUCHERINFO table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "查询开票登记 table列表")
    @LogMessage(content = "查询开票登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return AjaxJson.success(abEmitBillService.getDataTablesUnion(dataTablesInput));
    }


    /**
     * 开票登记详情页
     *
     * @param aid
     * @param model
     * @return
     */
    @ApiOperation(value = "开票登记详情页")
    @LogMessage(content = "查询开票登记详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping(value = {"/view/{aid}/{payId}", "/view/{aid}"})
    public AjaxJson<AbEmitBillPageVO> view(@PathVariable("aid") String aid, @PathVariable(value = "payId", required = false) String payId, Model model) {
        AbEmitBillPageVO pageVO = new AbEmitBillPageVO();
        AbEmitBillVo vo = abEmitBillService.getAbEmitBillVoByAid(aid, false);
        if (ObjectUtil.isNotEmpty(vo.getBusCorpId())) {
            String busCorpName = corpService.getCorpName(vo.getBusCorpId());
            vo.setBusCorpName(busCorpName);
        }
        List<AbLog> abLogList = abLogService.listAbLog(vo.getVvid(), null);
        if (StringUtils.isNotEmpty(payId)) {
            vo.setVoucherId(payId);
        }
        pageVO.setAbEmitBillVo(vo);
        pageVO.setFcBondRate(vo.getBailrate());
        pageVO.setAbLogList(abLogList);
        pageVO.setBillId(vo.getVvid());

        // 挂预算系统参数
        if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
            pageVO.setPayItem(abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
            pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_EMIT);
            pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);

            List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
                    BudgetConstant.BUSINESS_NAME_EMIT, BudgetConstant.PROCESS_SYSNAME_AB, vo.getVvid());
            if (list != null && !list.isEmpty()) {
                // 显示预算
                pageVO.setBudgetDisplay(true);
            }
        }
        //挂授信 显示
        if (!StringUtils.isBlank(vo.getRatmodel()) && vo.getRatmodel().equals("1")) {
            pageVO.setIsEnabled(true);
        } else {
            pageVO.setIsEnabled(false);
        }
        //判断是否有已挂保证金数据
        FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(), vo.getAppNo());
        String bailmodel = FcBondConstant.IS_HOOK_N;
        if (fcBondApplyDTO != null) {
            bailmodel = FcBondConstant.IS_HOOK_Y;
        }
        pageVO.setBailmodel(bailmodel);
        abBillAttachmentService.viewAttachment(aid, model);

        setAbBillAttachmentVO(model,pageVO);

        abEmitBillService.viewBusInfo(vo, model);

        if(model.getAttribute("busInfo") != null){
            pageVO.setBusInfo((AbVoucherinfoVO) model.getAttribute("busInfo"));
        }
        //是否启用担保参数
        pageVO.setIsHangGuarantee(vo.getIsHangGuarantee() == null ? FcBondConstant.IS_HOOK_N : vo.getIsHangGuarantee());
        pageVO.setAppNo(vo.getAppNo());
        //挂内部授信 显示
        if (!StringUtils.isBlank(vo.getInsideRatModel()) && vo.getInsideRatModel().equals(FcBondConstant.IS_HOOK_Y)) {
            pageVO.setCpIsEnabled(true);
            pageVO.setCpURationDTO(abRatCpUrationService.getRatUrationDTOByBillId(vo.getVvid()));
        } else {
            pageVO.setCpIsEnabled(false);
        }
        String voBillId = pageVO.getBillId();
        String voAppNo = pageVO.getAppNo();
        // 银行授信信息
        if (StringUtils.isNotEmpty(voBillId)) {
            RatBkrationCommonDTO ratCommonDTO = ratBkrationService.getRatCommonDTOByBillId(ConfigConstant.ACCEBILL, voBillId);
            pageVO.setRatBkrationPage(ratCommonDTO);
        }
        //保证金信息
        if (StringUtils.isNotEmpty(voBillId) && StringUtils.isNotEmpty(voAppNo)) {
            pageVO.setFcBondApplyDTO(abFcBondHookService.getFcBondApplyByItemIds(voBillId, voAppNo));
        }
        //设置挂接担保
        if (StringUtils.isNotEmpty(voBillId) && StringUtils.isNotEmpty(voAppNo)) {
            pageVO.setGuaContractUseDTO(abGuaranteeService.getGuaContractOccupyDtl(voBillId, voAppNo));
        }
        pageVO.setCurId(abVoucherinfoViceService.getCurIdByCNY());
        return success(pageVO);
    }

    private void setAbBillAttachmentVO(Model model, AbEmitBillPageVO pageVO) {
        if(model.getAttribute("attachmentInfo") != null){
            pageVO.setAttachmentInfo((AbBillAttachmentVO) model.getAttribute("attachmentInfo"));
        }
        if(model.getAttribute("attachmentInfoDiscount") != null){
            pageVO.setAttachmentInfoDiscount((AbBillAttachmentVO) model.getAttribute("attachmentInfoDiscount"));
        }
        if(model.getAttribute("isAttachmentShow") != null){
            pageVO.setIsAttachmentShow((Boolean) model.getAttribute("isAttachmentShow"));
        }
        if(model.getAttribute("bankTypeName") != null){
            pageVO.setBankTypeName((String) model.getAttribute("bankTypeName"));
        }
    }


    /**
     * 招行协议文本信息查询
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "招行协议文本信息查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/selectCmbCrlinfoByCVMNo")
    public AjaxJson<CmbCrlinfoVo> selectCmbCrlinfoByCVMNo(@RequestBody CmbCrlinfoVo vo) {
        return AjaxJson.success(abEmitBillService.selectCmbCrlinfoByCVMNo(vo.getCreditlineid()));
    }

    /**
     * 招行合作银行号查询
     *
     * @return
     */
    @LogMessage(content = "招行合作银行号查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/dataTableCooperativeBankNumber")
    public AjaxJson<DataTablesOutput<CmbBnkbilnegVo>> dataTableCooperativeBankNumber(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abEmitBillService.dataTableCooperativeBankNumber(input));
    }


    /**
     * 招行风险合同信息表
     *
     * @return
     */
    @LogMessage(content = "招行风险合同信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/dataTableCMBCRLCONInfo")
    public AjaxJson<DataTablesOutput<CmbCrlconVo>> dataTableCMBCRLCONInfo(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abEmitBillService.dataTableCMBCRLCONInfo(input));
    }


    /**
     * 开票登记新增页面跳转
     *
     * @return
     */
    @GetMapping("/add/{isBatchBill}")
    public AjaxJson<AbEmitBillAddVo> add(@PathVariable String isBatchBill) {
        AbEmitBillAddVo model = new AbEmitBillAddVo();
        String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO);
        model.setAppNo(appNo);
        if (AccebillConstant.BILL_ISBATCHBILL.equals(isBatchBill)) {
            String batchNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_BATCHNO);
            model.setBatchNo(batchNo);
            model.setIsBatchBill(AccebillConstant.BILL_ISBATCHBILL);
        }
        // 挂预算系统参数
        if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
            model.setPayItem(abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
        }
        // 挂授信
        boolean isRelateRat = abRatUrationService.isRelateRat();
        // 挂内部授信
        boolean isOpenCpRat = abRatCpUrationService.isRelateCpRat();
        model.setIsRelateRat(isRelateRat);
//		if (isRelateRat || isOpenCpRat) {
        model.setCurId(abVoucherinfoViceService.getCurIdByCNY());
//		}
        model.setExternalCorpNum(abVoucherinfoViceService.getExternalCorpNum());
        model.setIsEmitMake(abVoucherinfoViceService.isEmitMake());
        //是否挂保证金
        model.setIsBail(abFcBondHookService.isBail());
        //挂保证金银行类型
        model.setBailBankControlType(abFcBondHookService.bailBankControlType());
        //是否启用授信敞口
        model.setMouthRat(abRatUrationService.isNotOpenRat());
        //是否启用担保参数
        model.setIsGuarantee(abGuaranteeService.isHangGuarantee());
        model.setIsHangGuarantee(AccebillConstant.AB_BILL_VALID_SIGN_1);
        //获取纸票票据金额最大额度
        model.setMaxBillMoney(abVoucherinfoViceService.getMaxBillMoney());
        //增加是否挂内部授信
        model.setIsOpenCpRat(isOpenCpRat);
        model.setExternalCorp(abConfigService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        return AjaxJson.success(model);
    }

    /**
     * 开票登记编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update/{aid}")
    public AjaxJson<Map<String, Object>> update(@PathVariable("aid") String aid, HttpServletRequest request) {
        Map<String, Object> map = abEmitBillService.dealBillInfo(aid, request.getParameter("isBatchBill"), request.getParameter("batchNo"));
        return AjaxJson.success(map);
    }


    /**
     * 查看开票票据详情页面
     *
     * @return
     */
    @LogMessage(content = "查询开票登记详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public AjaxJson<AbEmitBillAddVo> view(String viceId, String accId, boolean isShowPool) {
        AbEmitBillAddVo model = new AbEmitBillAddVo();
        AbEmitBillVo vo = new AbEmitBillVo();
        //担保业务提供外部参数特殊处理:查询开票详情
        if (!StringUtils.isBlank(viceId) && StringUtils.isBlank(accId)) {
            vo = abEmitBillService.getAbEmitBillVoByAid(
                    abAccrecordsService.getBillByVid(viceId, AccebillConstant.BRIEF_STATUS_001).getId(), false);
        } else {
            vo = abEmitBillService.getAbEmitBillVoByAid(accId, false);
        }
        if (isShowPool) {
            AbVoucherinfoVO abVoucherinfoVO = new AbVoucherinfoVO();
            abVoucherinfoVO.setOrigNo(vo.getOrigNo());
            abBillQueryService.getBillPoolInfo(abVoucherinfoVO);
            model.setInfo(abVoucherinfoVO);
        }
        List<AbLog> abLogList = abLogService.listAbLog(viceId, null);
        model.setAbEmitBillVo(vo);
        model.setFcBondRate(vo.getBailrate());
        model.setAbLogList(abLogList);
        model.setBillId(vo.getVvid());
        // 挂预算系统参数
        if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
            model.setPayItem(abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
            model.setBusinessName(BudgetConstant.BUSINESS_NAME_EMIT);
            model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);
        }
        // 挂授信
        boolean isRelateRat = abRatUrationService.isRelateRat();
        model.setIsRelateRat(isRelateRat);
        if (isRelateRat) {
            model.setRatUrationDTO(abRatUrationService.getRatUrationDTOByBillId(vo.getVvid()));
        }
        //挂授信 显示
        if (!StringUtils.isBlank(vo.getRatmodel()) && vo.getRatmodel().equals("1")) {
            model.setIsEnabled(true);
        } else {
            model.setIsEnabled(false);
        }
        //判断是否有已挂保证金数据
        FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(), vo.getAppNo());
        String bailmodel = FcBondConstant.IS_HOOK_N;
        if (fcBondApplyDTO != null) {
            bailmodel = FcBondConstant.IS_HOOK_Y;
        }
        model.setBailmodel(bailmodel);
        //是否启用担保参数
        model.setIsHangGuarantee(vo.getIsHangGuarantee() == null ? FcBondConstant.IS_HOOK_N : vo.getIsHangGuarantee());
        model.setAppNo(vo.getAppNo());
        model.setIsShowPool(isShowPool);
        abBillAttachmentService.viewAttachmentV1(accId, model);
        return AjaxJson.success(model);
    }

    /**
     * 新增-保存开票登记
     *
     * @param abEmitBillVo 登记信息vo
     * @return
     */
    @LogMessage(content = "新增-保存开票登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbEmitBillSaveVO abEmitBillVo) {
       // 授信VO
        RatUrationDTO urationDTO=new RatUrationDTO();
        BeanUtils.copyProperties(abEmitBillVo,urationDTO);
        //判断挂银行授信
        if (AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getRatmodel())) {
            urationDTO.setCorpId(abEmitBillVo.getRatBankCorpId());//银行授信单位CorpId
        }
        abEmitBillVo.setCreditCode(urationDTO.getRatItemCode());
        String busCorpName = corpService.getCorpName(abEmitBillVo.getBusCorpId());
        abEmitBillVo.setBusCorpName(busCorpName);
        // todo:数据转换
        // com.bytter.bill.api.bill.dto.RatUrationDTO ratUrationDTO = BeanUtils.copyNewInstanceProperties(urationDTO, com.bytter.bill.api.bill.dto.RatUrationDTO.class);
        abEmitBillVo.setUrationDTO(urationDTO);
        abEmitBillService.saveOrUpdate(abEmitBillVo, AccebillConstant.BILL_OPERATE_ADD, this.getCurrentUser());
        AjaxJson rejson = success();
        if (AccebillConstant.BILL_ISBATCHBILL.equals(abEmitBillVo.getIsBatchBill())) {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("appNo", SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO));
            attributes.put("aids", abEmitBillVo.getId());
            attributes.put("vids", abEmitBillVo.getVid());
            attributes.put("vvids", abEmitBillVo.getVvid());
            rejson.setAttributes(attributes);
        }
        return rejson;
    }

    /**
     * 提交开票登记
     *
     * @param abEmitBillVo
     * @return
     */
    @LogMessage(content = "提交开票登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit/{operate}")
    public AjaxJson submit(@RequestBody AbEmitBillSaveVO abEmitBillVo, @PathVariable("operate") String operate) {
        // 授信VO
        RatUrationDTO urationDTO=new RatUrationDTO();
        BeanUtils.copyProperties(abEmitBillVo,urationDTO);
        //判断挂银行授信
        if (AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getRatmodel())) {
            urationDTO.setCorpId(abEmitBillVo.getRatBankCorpId());//银行授信单位CorpId
        }
        abEmitBillVo.setCreditCode(urationDTO.getRatItemCode());
        // todo:数据转换
        // com.bytter.bill.api.bill.dto.RatUrationDTO ratUrationDTO = BeanUtils.copyNewInstanceProperties(urationDTO, com.bytter.bill.api.bill.dto.RatUrationDTO.class);
        abEmitBillVo.setUrationDTO(urationDTO);
        abEmitBillService.submit(abEmitBillVo, operate, this.getCurrentUser());
        return success();
    }

    /**
     * 修改保存开票登记信息
     *
     * @param abEmitBillVo 登记信息vo
     * @return
     */
    @LogMessage(content = "修改保存开票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody AbEmitBillSaveVO abEmitBillVo  ) {
        // 授信VO
        RatUrationDTO urationDTO=new RatUrationDTO();
        BeanUtils.copyProperties(abEmitBillVo,urationDTO);
        //判断挂银行授信
        if (AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getRatmodel())) {
            urationDTO.setCorpId(abEmitBillVo.getRatBankCorpId());//银行授信单位CorpId
        }
        // todo:数据转换
        // com.bytter.bill.api.bill.dto.RatUrationDTO ratUrationDTO = BeanUtils.copyNewInstanceProperties(urationDTO, com.bytter.bill.api.bill.dto.RatUrationDTO.class);
        abEmitBillVo.setUrationDTO(urationDTO);
        abEmitBillVo.setCreditCode(urationDTO.getRatItemCode());
        // abEmitBillService.update(abEmitBillVo);
        abEmitBillService.saveOrUpdate(abEmitBillVo, AccebillConstant.BILL_OPERATE_UPDATE, this.getCurrentUser());
        return success();
    }

    /**
     * 付款开票打回
     *
     * @param abEmitBillVo 登记信息vo
     * @return
     */
    @LogMessage(content = "付款开票打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_REPULSE)
    @PostMapping("/repulse")
    public AjaxJson repulse(@RequestBody AbEmitBillVo abEmitBillVo) {
        abEmitBillService.repulse(abEmitBillVo, this.getCurrentUser());
        return success();
    }

    /**
     * 删除开票登记
     *
     * @param aid    id信息
     * @param reason 删除原因
     * @return
     */
    @LogMessage(content = "删除开票登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    public AjaxJson delete(String aid, String reason) {
        abEmitBillService.delete(aid, reason, this.getCurrentUser());
        return success();
    }

    /**
     * 批量删除开票登记信息
     *
     * @param aids   id信息
     * @param reason 删除原因
     * @return
     */
    @LogMessage(content = "批量删除开票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(String aids, String reason) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abEmitBillService.batchDelete(aids, reason, this.getCurrentUser()));
        return ajaxJson;
    }

    /**
     * 批量提交开票登记信息
     *
     * @param vids  主信息id
     * @param vvids 从表ID
     * @param aids  流水ID
     * @return
     */
    @LogMessage(content = "批量提交开票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    public AjaxJson batchSubmit(String vids, String vvids, String aids) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abEmitBillService.batchSubmit(vids, vvids, aids, this.getCurrentUser(), false));
        return ajaxJson;
    }

    /**
     * 批量提交开票登记信息
     *
     * @param vids  主信息id
     * @param vvids 从表ID
     * @param aids  流水ID
     * @return
     */
    @LogMessage(content = "批次提交开票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchNoSubmit")
    public AjaxJson batchNoSubmit(String vids, String vvids, String aids) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(abEmitBillService.batchSubmit(vids, vvids, aids, this.getCurrentUser(), true));
        return ajaxJson;
    }

    /**
     * 根据收票人账号获取银行信息 用于付款生成的开票登记，编辑时选择对内时校验收票人账号是否存在，不存在则不允许选择对内开票
     *
     * @param bankAcc
     * @return
     */
    @PostMapping("/getBankDataByPayeeacc")
    public AjaxJson getBankDataByPayeeacc(String bankAcc) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(abEmitBillService.getBankDataByBankAcc(bankAcc));
        return ajaxJson;
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input= dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abEmitBillService.dataTableApprove(input));
    }

    /**
     * 跳转至审核详情界面
     *
     * @param businessId 审批流水ID
     * @param taskId     流程ID
     * @return String
     */
    @GetMapping("/approve")
    public AjaxJson<AbEmitBillAddVo> approve(String businessId, String taskId) {
        AbEmitBillAddVo model=new AbEmitBillAddVo();
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillVo vo = abEmitBillService.getAbEmitBillVoByAid(businessId, false);
            model.setAbEmitBillVo(  vo);
            model.setIsApprove(  true);
            model.setBillId(  vo.getVvid());
            // 挂预算系统参数
            if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
                model.setPayItem(
                        abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
                model.setBusinessName(  BudgetConstant.BUSINESS_NAME_EMIT);
                model.setProcessSysname(  BudgetConstant.PROCESS_SYSNAME_AB);
            }
            List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
                    BudgetConstant.BUSINESS_NAME_EMIT, BudgetConstant.PROCESS_SYSNAME_AB, vo.getVvid());
            if (list != null && !list.isEmpty()) {
                // 显示预算
                model.setBudgetDisplay(  "true");
            }
            abBillAttachmentService.viewAttachmentV1(businessId, model);
            //判断是否有已挂保证金数据
            FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(), vo.getAppNo());
            String bailmodel = FcBondConstant.IS_HOOK_N;
            if (fcBondApplyDTO != null) {
                bailmodel = FcBondConstant.IS_HOOK_Y;
            }
            model.setBailmodel(  bailmodel);
            //挂授信 显示
            if (!StringUtils.isBlank(vo.getRatmodel()) && vo.getRatmodel().equals("1")) {
                model.setIsEnabled(  true);
            } else {
                model.setIsEnabled(  false);
            }
            //是否启用担保参数
            model.setIsHangGuarantee(  vo.getIsHangGuarantee() == null ? FcBondConstant.IS_HOOK_N : vo.getIsHangGuarantee());
            model.setAppNo(  vo.getAppNo());
            //挂内部授信 显示
            if (!StringUtils.isBlank(vo.getInsideRatModel()) && vo.getInsideRatModel().equals(FcBondConstant.IS_HOOK_Y)) {
                model.setCpIsEnabled(true);
                model.setRatUrationDTO( abRatCpUrationService.getRatUrationDTOByBillId(vo.getVvid()));
            } else {
                model.setCpIsEnabled(  false);
            }
            model.setCurId( abVoucherinfoViceService.getCurIdByCNY());
            model.setFcBondRate(  vo.getBailrate());
        }
        model.setTaskId( taskId);
        boolean isRelateRat = abRatUrationService.isRelateRat();
        model.setIsRelateRat(  isRelateRat);
        return AjaxJson.success(model);
    }

    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @return String
     */
    @GetMapping("/mobileApprove")
    public AjaxJson<MobileApproveVO> mobileApprove(String businessId, String taskId) {
        MobileApproveVO model = new MobileApproveVO();
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillVo vo = abEmitBillService.getAbEmitBillVoByAid(businessId, false);
            model.setAbEmitBillVo(vo);
            model.setIsApprove(true);
            // 挂预算系统参数
            if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
                model.setPayItem(
                        abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
                model.setBusinessName(BudgetConstant.BUSINESS_NAME_EMIT);
                model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);
            }
            List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
                    BudgetConstant.BUSINESS_NAME_EMIT, BudgetConstant.PROCESS_SYSNAME_AB, vo.getVvid());
            if (list != null && !list.isEmpty()) {
                // 显示预算
                model.setBudgetDisplay("true");
            }
        }
        model.setTaskId(taskId);
        return AjaxJson.success(model);
    }

    /**
     * 开票登记导入
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<AbEmitBillVo> list) {
        abEmitBillService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * @Description 打开招行保证金选择界面
     * @author zhous
     * @date 2024/9/6 15:31
     */
    @GetMapping("/selectZhBondAccPage")
    public AjaxJson<Map<String, Object>> selectExternalCorpPage(String curacc, String bankType, String accName) {
        Map<String, Object> map = new HashMap<>();
        if (StrUtil.isNotBlank(curacc)) {
            map.put("curacc", curacc);
        }
        //默认查询招行页面
        String toPage = AccebillConstant.ZH_BANK;
        if (StrUtil.isNotBlank(bankType)) {
            toPage = bankType;
        }
        map.put("toPage", toPage);
        // 资源权限
        if (ObjectUtil.isNotEmpty(accName)) {
            map.put("accName", accName);
        }
        String url = "bill/accebill/select" + toPage + "BondAccPage";
        map.put("url", url);
        return AjaxJson.success(map);
    }

    /**
     * 招行保证金账户信息表
     *
     * @return
     */

    @LogMessage(content = "招行保证金账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getZhBondAcc")
    public AjaxJson<DataTablesOutput<CmbBailAccountInfoVo>> getZhBondAcc(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abEmitBillService.getZhBondAccDataTables(input));
    }

    /**
     * 民生保证金账户信息表
     *
     * @return
     */
    @LogMessage(content = "民生保证金账户信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getMsBondAcc")
    public AjaxJson<DataTablesOutput<CmbcBailAccountInfoVo>> getMsBondAcc(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abEmitBillService.getMsBondAccDataTables(input));
    }


    @PostMapping("/exportSelData")
    public String exportPayExcelByMap(String params, String ids, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 转换前台传来的数据为JSON
        params = URLDecoder.decode(params, "utf-8").replaceAll("&quot;", "\"");
        ids = URLDecoder.decode(ids, "utf-8").replaceAll("&quot;", "\"");
        // 用于排除当出现多余的列
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DataTablesInput dataTablesInput = mapper.readValue(params, DataTablesInput.class);
        dataTablesInput.getFormdata().put("ins_aid", ids);
        dataTablesInput.setTitle("开票登记导出");
        dataTablesInput.setFilename("开票登记导出");
        // 配置导出要素
        List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
        DataTablesOutput dataTablesUnion = abEmitBillService.getDataTablesUnion(dataTablesInput);
        List<AbEmitBillVo> data = dataTablesUnion.getData();
        if (CollectionUtil.isEmpty(data)) {
            return null;
        }
        modelMap.put("dataTablesInput", dataTablesInput);
        abBillQueryService.listSelectData(dataTablesInput, modelMap, entityList, request, response, data);
        return null;
    }
}
