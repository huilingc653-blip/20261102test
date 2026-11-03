package com.bytter.increment.saas.controller;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.InnerAccountConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.domain.vo.v1.FcAgreedDepositUpdateRegVO;
import com.bytter.increment.domain.vo.v1.FcAgreedDepositAddAgreedAccVO;
import com.bytter.increment.domain.vo.v1.FcAgreedDepositApproveVO;
import com.bytter.increment.api.constant.AgreedConstant;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.enums.ChoInnerFinanceMessageEnum;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import com.bytter.increment.domain.service.ChoInnerAgreedDepositService;
import com.bytter.increment.domain.service.FcAgreedDetailService;
import com.bytter.increment.domain.vo.FcAccVO;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import com.bytter.increment.domain.vo.v1.FcAgreedDepositPageVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 内部存款-协定存款登记表
 * @date 2019-05-10 16:44:38
 */
@Api(value = "内部存款-协定存款登记", tags = "内部存款-协定存款登记")
@RestController("v1ChoInnerAgreedDepositController")
@RequestMapping("/v1-increment/agreeddeposit/choinneragreeddeposit")
public class ChoInnerAgreedDepositController extends BaseController {
    @Autowired
    private ChoInnerAgreedDepositService choInnerAgreedDepositService;
    @Autowired
    private FcAgreedDetailService fcAgreedDetailService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IFcDayCloseService fcDayCloseService;

    @DubboReference(check = false)
    private IInnerAccountService iInnerAccountService;

    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;

    @ApiOperation("查询内部存款-协定存款登记表列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choInnerAgreedDepositService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    @ApiOperation("添加协定存款登记表")
    @LogMessage(content = "添加协定存款登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDeposit.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FC_AGREED_DEPOSIT));
            choInnerAgreedDepositService.saveChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser());
        }
        return success();
    }

    @ApiOperation("添加协定存款延期登记表")
    @LogMessage(content = "添加协定存款延期登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/savePos")
    @ResponseBody
    public AjaxJson savePos(@RequestBody FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDeposit.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FC_AGREED_DEPOSIT));
            choInnerAgreedDepositService.saveChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser());
        }
        return success();
    }

    @ApiOperation("添加协定存款终止登记表")
    @LogMessage(content = "添加协定存款终止登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveTerm")
    @ResponseBody
    public AjaxJson saveTerm(@RequestBody FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDeposit.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FC_AGREED_DEPOSIT));
            choInnerAgreedDepositService.saveChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser());
        }
        return success();
    }

    @ApiOperation("修改协定存款登记登记表")
    @LogMessage(content = "修改协定存款登记登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyReg")
    @ResponseBody
    public AjaxJson modifyReg(@RequestBody FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            choInnerAgreedDepositService.updateChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser(), false);
        }
        return success();
    }

    @ApiOperation("修改协定存款延期表")
    @LogMessage(content = "修改协定存款延期表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyPos")
    @ResponseBody
    public AjaxJson modifyPos(@RequestBody FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            choInnerAgreedDepositService.updateChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser(), false);
        }
        return success();
    }

    @ApiOperation("修改协定存款终止表")
    @LogMessage(content = "修改协定存款终止表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyTerm")
    @ResponseBody
    public AjaxJson modifyTerm(@RequestBody FcAgreedDepositVO fcAgreedDeposit) {
        if (dataCheck(fcAgreedDeposit)) {
            choInnerAgreedDepositService.updateChoAgreedDeposit(fcAgreedDeposit, this.getCurrentUser(), false);
        }
        return success();
    }

    @ApiOperation("在协定存款登记页面跳转前获取对应数据信息")
    @GetMapping("/getInfoBeforeUpdate")
    @ResponseBody
    public AjaxJson<FcAgreedDepositPageVO> getInfoBeforeUpdate(FcAgreedDepositVO fcAgreedDeposit) {
        FcAgreedDepositPageVO pageVO = new FcAgreedDepositPageVO();
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        if (fcAgreedDepositVO != null) {
            pageVO.setFcAgreedDepositPage(fcAgreedDepositVO);
            Date tallyDate = fcDayCloseService.getDayCloseFinalDate(fcAgreedDepositVO.getNetId());
            tallyDate = DateUtils.addDay(tallyDate, 1);
            String dateStr = DateUtils.formatDate(tallyDate);
            pageVO.setDateStr(dateStr);
            return success(pageVO);
        }
        return success();
    }

    @ApiOperation("查询协定存款表详情")
    @LogMessage(content = "查询协定存款表详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<FcAgreedDepositPageVO> view(@PathVariable("id") String id) {
        FcAgreedDepositPageVO pageVO = new FcAgreedDepositPageVO();
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getByUnion(id);
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
        fcAgreedDepositVO.setDetailList(details);
        pageVO.setFcAgreedDepositPage(fcAgreedDepositVO);
        pageVO.setBusType(ChoMessageConstant.BUS_TYPE_AGREE);
        if (fcAgreedDepositVO != null) {
            pageVO.setOpType(fcAgreedDepositVO.getBusType());
        }
        return success(pageVO);
    }



    @ApiOperation("在协定存款修改登记页面获取对应数据信息")
    @GetMapping("/updateChoReg")
    public AjaxJson<FcAgreedDepositUpdateRegVO> updateChoReg(FcAgreedDepositVO fcAgreedDeposit) {
        FcAgreedDepositUpdateRegVO vo = new FcAgreedDepositUpdateRegVO();
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        if (fcAgreedDepositVO != null) {
            vo.setFcAgreedDepositPage(fcAgreedDepositVO);
            Date closeDate = fcDayCloseService.getDayCloseFinalDate(fcAgreedDepositVO.getNetId());
            vo.setCloseDate(closeDate);
            FcAccDTO fcAccDTO = iInnerAccountService.getByBlnctrAcc(fcAgreedDepositVO.getAgrAcc());
            vo.setFcAccPage(fcAccDTO);
        }
        return success(vo);
    }


    @ApiOperation("查询复核协定存款登记表详情")
    @LogMessage(content = "查询复核协定存款登记表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/check/{busType}/{id}")
    public AjaxJson<FcAgreedDepositPageVO> check(@PathVariable("busType") String busType, @PathVariable("id") String id) {
        FcAgreedDepositPageVO pageVO = new FcAgreedDepositPageVO();
        pageVO.setId(id);
        pageVO.setBusType(busType);
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getByUnion(id);
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
        fcAgreedDepositVO.setDetailList(details);
        pageVO.setFcAgreedDepositPage(fcAgreedDepositVO);
        return success(pageVO);
    }

    @ApiOperation("删除协定存款登记表")
    @LogMessage(content = "删除协定存款登记表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        choInnerAgreedDepositService.delChoAgreedDeposit(id, this.getCurrentUser());
        return success();
    }

    @ApiOperation("批量删除协定存款登记表")
    @LogMessage(content = "批量删除费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestBody BisIDsDTO idsDTO) {
        for (String id : idsDTO.getIds().split(CommonConstant.COMMA_SPLIT)) {
            choInnerAgreedDepositService.delChoAgreedDeposit(id, this.getCurrentUser());
        }
        return success();
    }

    @ApiOperation("批量提交协定存款信息")
    @LogMessage(content = "批量提交协定存款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestBody BisIDsDTO idsDTO) {
        for (String id : idsDTO.getIds().split(CommonConstant.COMMA_SPLIT)) {
            FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getByUnion(id);
            fcAgreedDepositVO.setStatus(InnerFinanceConstant.STATUE_WAIT_CHECK);
            choInnerAgreedDepositService.updateChoAgreedDeposit(fcAgreedDepositVO, this.getCurrentUser(), true);
        }
        return success();
    }

    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(this.choInnerAgreedDepositService.dataTableApprove(input));
    }

    @ApiOperation("查询内部存款-协定存款查询列表")
    @PostMapping("/datatablequery")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choInnerAgreedDepositService.getDataTableQuery(input, this.getCurrentUser()));
    }

    @ApiOperation("查询内部存款-协定存款明细查询列表")
    @PostMapping("/datatablequerydtl")
    @ResponseBody
    public AjaxJson<DataTablesOutput> getDataTableQueryDtl(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choInnerAgreedDepositService.getDataTableQueryDtl(input));
    }

    @ApiOperation("根据开户单位查找内部账号")
    @GetMapping("/getBlnctrAccByCorpId")
    @ResponseBody
    public AjaxJson<List<FcAccVO>> getBlnctrAccByCorpId(String corpId, String busType) {
        List<FcAccVO> list = choInnerAgreedDepositService.getBlnctrAccByCorpId(this.getCurrentUser(), corpId, busType);
        return success(list);
    }

    @ApiOperation("根据账户id查找协定信息")
    @GetMapping("/getChoInfo")
    @ResponseBody
    public AjaxJson<FcAgreedDepositVO> getChoInfo(String accId, String busType) {
        FcAgreedDepositVO vo = choInnerAgreedDepositService.getAgreeInfo(accId, busType);
        return success(vo);
    }

    @ApiOperation("获取费用明细")
    @GetMapping("/listFcAgreedDetail")
    @ResponseBody
    public AjaxJson<List<FcAgreedDetail>> listFcAgreedDetail(String agreedId) {
        return success(fcAgreedDetailService.getDetailList(agreedId));
    }

    @ApiOperation("根据单位id获取日结日期")
    @GetMapping("/getBeginDate")
    @ResponseBody
    public AjaxJson<String> getBeginDate(String corpId) {
        /*获取单位直属网点*/
        CorpDTO netCorp = iCorpService.getNetByCorpId(corpId);
        String netId = "";
        if (netCorp == null) {
            /*如果没有直属网点就是本网点*/
            netId = corpId;
        } else {
            netId = netCorp.getId();
        }
        Date tallyDate = fcDayCloseService.getDayCloseFinalDate(netId);
        tallyDate = DateUtils.addDay(tallyDate, 1);
        String dateStr = DateUtils.formatDate(tallyDate);
        return success(dateStr);
    }

    @ApiOperation("跳转新增协定账号")
    @GetMapping("/addAgreedAcc")
    public AjaxJson<FcAgreedDepositAddAgreedAccVO> addAgreedAcc(FcAgreedDepositVO fcAgreedDepositVO) {
        FcAgreedDepositAddAgreedAccVO vo = new FcAgreedDepositAddAgreedAccVO();
        vo.setBeginDate(DateUtils.formatDate(fcAgreedDepositVO.getBeginDate()));
        // 获取单位直属网点(如果没有直属网点就是本网点)
        CorpDTO netCorp = iCorpService.getNetByCorpId(fcAgreedDepositVO.getCorpId());
        if (netCorp == null) {
            netCorp = new CorpDTO();
            netCorp.setId(fcAgreedDepositVO.getCorpId());
            netCorp.setCode(iCorpService.get(fcAgreedDepositVO.getCorpId()).getCode());
        }
        vo.setNetInfo(netCorp);
        //币别信息
        vo.setCurInfo(iBtCurrencyService.getObjectById(fcAgreedDepositVO.getCurId()));
        CorpDTO corpDTO = iCorpService.get(fcAgreedDepositVO.getCorpId());
        vo.setCorpInfo(corpDTO);
        //账号信息
        List<String> accIds = new ArrayList<>();
        accIds.add(fcAgreedDepositVO.getAccId());
        List<String> accState = new ArrayList<>();
        accState.add(InnerAccountConstant.ACC_STATE_NORMAL);
        List<FcAccDTO> fcAcc = iInnerAccountService.listFcAccInfo(accIds, accState);
        if (!EmptyUtils.isEmptyList(fcAcc)) {
            vo.setAccInfo(fcAcc.get(0));
        }
        FcAccDTO agrAcc;
        //协定账号信息
        if (StringUtils.isNotBlank(fcAgreedDepositVO.getAgrAcc())) {
            agrAcc = iInnerAccountService.getByBlnctrAcc(fcAgreedDepositVO.getAgrAcc());
            int accAccNoRule = configService.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
            if (!EmptyUtils.isEmpty(agrAcc)) {
                agrAcc.setAccAccNoRule(accAccNoRule);
            }
        } else {
            agrAcc = new FcAccDTO();
            agrAcc.setInterestSign("1");
        }
        vo.setAgrAccInfo(agrAcc);
        vo.setFcAgreedDepositPage(fcAgreedDepositVO);
        //顺序号生成模式
        int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
        vo.setSeqNumPage(seqGeneratingMode);
        //根据资金池标志生成内部账号生成法则
        String reVal = fcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
        int relen = reVal.length();
        vo.setRelen(relen);
        vo.setPoolSign("0");
        return success(vo);
    }

    @ApiOperation("在协定存款延期修改页面获取对应数据信息")
    @GetMapping("/updateChoPos")
    public AjaxJson<FcAgreedDepositVO> updateChoPos(FcAgreedDepositVO fcAgreedDeposit) {
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        return success(fcAgreedDepositVO);
    }

    @ApiOperation("在协定存款终止修改页面获取对应数据信息")
    @GetMapping("/updateChoTerm")
    public AjaxJson<FcAgreedDepositVO> updateChoTerm(FcAgreedDepositVO fcAgreedDeposit) {
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        return success(fcAgreedDepositVO);
    }

    @ApiOperation("查询协定存款详情(根据业务类型)")
    @GetMapping("/viewCho/{id}")
    public AjaxJson<FcAgreedDepositPageVO> viewCho(@PathVariable("id") String id) {
        FcAgreedDepositPageVO pageVO = new FcAgreedDepositPageVO();
        FcAgreedDepositVO fcAgreedDepositVO = choInnerAgreedDepositService.getByUnion(id);
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
        fcAgreedDepositVO.setDetailList(details);
        pageVO.setFcAgreedDepositPage(fcAgreedDepositVO);
        pageVO.setBusType(ChoMessageConstant.BUS_TYPE_AGREE);
        if (fcAgreedDepositVO != null) {
            pageVO.setOpType(fcAgreedDepositVO.getBusType());
        }
        return success(pageVO);
    }

    @ApiOperation("跳转至审核详情界面")
    @GetMapping("/approve")
    public AjaxJson<FcAgreedDepositApproveVO> approve(String id, String taskId) {
        FcAgreedDepositApproveVO vo = new FcAgreedDepositApproveVO();
        if (StringUtils.isNotEmpty(id)) {
            FcAgreedDepositVO fcAgreedDepositVO = this.choInnerAgreedDepositService.getByUnion(id);
            List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
            fcAgreedDepositVO.setDetailList(details);
            vo.setFcAgreedDepositPage(fcAgreedDepositVO);
            vo.setTaskId(taskId);
            vo.setBusType(ChoMessageConstant.BUS_TYPE_AGREE);
        }
        return success(vo);
    }

    @ApiOperation("跳转至手机审核详情界面")
    @GetMapping("/mobileApprove")
    public AjaxJson<FcAgreedDepositApproveVO> mobileApprove(String businessId, String taskId) {
        FcAgreedDepositApproveVO vo = new FcAgreedDepositApproveVO();
        if (StringUtils.isNotEmpty(businessId)) {
            FcAgreedDepositVO fcAgreedDepositVO = this.choInnerAgreedDepositService.getByUnion(businessId);
            List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(businessId);
            if (!EmptyUtils.isEmpty(fcAgreedDepositVO)) {
                fcAgreedDepositVO.setDetailList(details);
                vo.setFcAgreedDepositPage(fcAgreedDepositVO);
                vo.setTaskId(taskId);
                vo.setBusType(ChoMessageConstant.BUS_TYPE_AGREE);
            }
        }
        return success(vo);
    }

    /**
     * 数据校验
     *
     * @param vo
     * @return
     */
    private boolean dataCheck(FcAgreedDepositVO vo) {
        boolean flag = true;
        if (InnerFinanceConstant.BUS_TYPE_0.equals(vo.getOpType())) {
            List<FcAgreedDepositVO> contractList = choInnerAgreedDepositService.getInfoByContractNo(vo.getContractNo());
            if (!EmptyUtils.isEmptyList(contractList)) {
                if (StringUtils.isBlank(vo.getId())) {
                    throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_CONTRACTNO_UNIQUE, vo.getContractNo());
                } else if (!vo.getId().equals(contractList.get(0).getId())) {
                    throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_CONTRACTNO_UNIQUE, vo.getContractNo());
                }
            }
        }
        List<Integer> status = new ArrayList<Integer>();
        status.add(AgreedConstant.STATUE_TEMP);
        status.add(AgreedConstant.STATUE_BACK);
        List<FcAgreedDepositVO> agreedList = choInnerAgreedDepositService.getAgreedInfo(vo.getAccId(), vo.getOpType(), status);
        if (!EmptyUtils.isEmptyList(agreedList)) {
            if (StringUtils.isBlank(vo.getId())) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_BLNCTRACC_USE, vo.getBlnctrAcc());
            } else if (!vo.getId().equals(agreedList.get(0).getId())) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_BLNCTRACC_USE, vo.getBlnctrAcc());
            }
        }
        return flag;
    }
}
