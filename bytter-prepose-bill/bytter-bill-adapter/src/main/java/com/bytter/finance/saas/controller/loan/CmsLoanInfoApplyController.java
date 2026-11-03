package com.bytter.finance.saas.controller.loan;

import com.bytter.bill.domain.bill.vo.v1.CmsLoanInfoApplyDetailVO;
import com.bytter.bill.domain.bill.vo.v1.CmsLoanInfoApplyUpdateVO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.service.IFcInterestService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.service.CmsLoanInfoApplyService;
import com.bytter.finance.domain.loan.service.CmsLoanInfoService;
import com.bytter.finance.domain.loan.service.CmsLoanRepayService;
import com.bytter.finance.domain.loan.service.CmsLoanSendService;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IBtPurposeService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.ConfigDTO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "/v1-preposebill/cms/loan/cmsloaninfoApply", tags = "贷款申请")
@RestController("v1CmsLoanInfoApplyController")
@RequestMapping("/v1-preposebill/cms/loan/cmsloaninfoApply")
public class CmsLoanInfoApplyController extends BaseController {
    @Autowired
    private CmsLoanInfoApplyService cmsLoanInfoApplyService;
    @Autowired
    private CmsLoanInfoService cmsLoanInfoService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @DubboReference(check = false)
    private IBtPurposeService btPurposeService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IFcDayCloseService iFcDayCloseService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private IRatCpUrationService ucprationService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie contractOccupServie;
    @Autowired
    private IGuaWayServie guaWayServie;
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsLoanRepayService cmsLoanRepayService;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IFcInterestService fcInterestService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private LoanTypeService loanTypeService;

    /**
     * 内部贷款申请列表页面跳转
     */
    @GetMapping("/list")
    public AjaxJson<Map<String, Object>> list() {
        Map<String, Object> model = new HashMap<>();
        model.put("pageType", "edit");
        return AjaxJson.success(model);
    }

    /**
     * <b>功能介绍：</b>列表数据<br>
     * <b>日期：</b>2017年7月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @PostMapping("/listdatatable")
    public AjaxJson<DataTablesOutput> listdatatable(@RequestBody V1DataTablesInput dataTablesInput, String pageType) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsLoanInfoService.getEditDataTables(input, pageType, getUserId(),
                ResourceConstant.CMS_LOAN_INFO_APPLY_QUERY));
    }

    /**
     * 修改贷款信息表
     *
     * @param param
     * @return
     */
    @LogMessage(content = "修改贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody CmsLoanInfoApplyUpdateVO param) {
        CmsLoanInfoVO cmsLoanInfoVO = new CmsLoanInfoVO();
        RatUrationDTO urationDTO = new RatUrationDTO();
        BeanUtils.copyProperties(param, cmsLoanInfoVO);
        BeanUtils.copyProperties(param, urationDTO);
        cmsLoanInfoService.updateLoanInfo(cmsLoanInfoVO, getCurrentUser(), urationDTO);
        return success();
    }

    /**
     * 贷款信息表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<CmsLoanInfoApplyDetailVO> update(CmsLoanInfo cmsLoanInfo) {
        CmsLoanInfoApplyDetailVO model = new CmsLoanInfoApplyDetailVO();
        boolean isCharging = false;
        if (StringUtils.isNotEmpty(cmsLoanInfo.getId())) {
            cmsLoanInfo = cmsLoanInfoService.get(cmsLoanInfo.getId());
            model.setCmsLoanInfoPage(cmsLoanInfo);
            // 贷款种类Id loanGenreId
            String loanGenreId = loanTypeService.queryLoanTypeByCode(cmsLoanInfo.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getId();
            model.setLoanGenreId(loanGenreId);
            model.setCreateDate(DateUtils.date2Str(cmsLoanInfo.getCreateDate(), DateUtils.date_sdf()));
            model.setNetName(cmsLoanInfoService.getCenNetId(cmsLoanInfo.getCorpId()).get("corpName"));
        }
        ConfigDTO config = cmsLoanInfoService.getRelateRat();// 贷款参数设置是否授信挂接
        if (CmsConstant.IS_TRUE.equals(config.getValue())) {
            if (CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
                model.setCpURationDTO(
                        ucprationService.getRatUrationDTOByBillId(ConfigConstant.CMS, cmsLoanInfo.getId()));// 授信占用明细
            }
        }
        model.setRelateRat(config.getValue());
        model.setIsCharging(isCharging);
        // 查询发放的 还款计划
        CmsRepaymentSchedule crs = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(cmsLoanInfo.getId());
        if (crs != null) {
            model.setPlanId(crs.getId());
        }
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.setIsGuarantee(isGuarantee.getValue());// 业务模块是否挂担保参数设置
        if (isGuarantee.getValue().equals(CmsConstant.IS_TRUE)) {
            model.setIsHangGuarantee(cmsLoanInfo.getIsHangGuarantee());// 业务是否挂担保
            if (cmsLoanInfo.getIsHangGuarantee() != null
                    && cmsLoanInfo.getIsHangGuarantee().equals(CmsConstant.IS_TRUE)) {
                setGuaContractMsg(cmsLoanInfo, model);
            }
        }
        return AjaxJson.success(model);
    }


    public void setGuaContractMsg(CmsLoanInfo cmsLoanInfo, CmsLoanInfoApplyDetailVO model) {
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        guaContractOccupDTO.setBusinessId(cmsLoanInfo.getId());
        guaContractOccupDTO.setStatus(GuaranteeConstant.GUA_CONTRACT_TEMP);// 暂存状态
        GuaContractOccupDTO guaContractOccup = contractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
        List<String> idList = guaContractOccup.getContractIds();
        String contractIds = "";
        for (String id : idList) {
            contractIds += id + ",";
        }
        model.setIdList(idList);// 担保合同id List
        model.setContractIds(contractIds);// 担保合同id+担保合同号，存入业务表的字段
        model.setGuaWayNames(guaContractOccup.getGuaWayName());// 担保方式名称组合
        model.setGuaWay(guaContractOccup.getGuaWay());// 担保方式code组合
        model.setGuaranteeRmk(guaContractOccup.getGuaRmk());
        model.setContractCode(guaContractOccup.getContractCode());// 担保合同编号组合，逗号隔开
    }

    /**
     * 贷款信息表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public AjaxJson<CmsLoanInfoApplyDetailVO> add(CmsLoanInfo cmsLoanInfo) {
        CmsLoanInfoApplyDetailVO model = new CmsLoanInfoApplyDetailVO();
        boolean isCharging = false;// 是否启用台账 FIXME 默认不启用
        if (StringUtils.isNotEmpty(cmsLoanInfo.getId())) {
            cmsLoanInfo = cmsLoanInfoService.get(cmsLoanInfo.getId());
            model.setCmsLoanInfoPage(cmsLoanInfo);
        }
        ConfigDTO config = cmsLoanInfoService.getRelateRat();
        model.setRelateRat(config.getValue());// 挂接授信
        model.setIsCharging(isCharging);
        ConfigDTO isGuarantee = cmsLoanInfoService.getIsGuarantee();// 获取参数设置是否挂接担保
        model.setIsGuarantee(isGuarantee.getValue());
        if (isGuarantee.getValue().equals(CmsConstant.IS_TRUE)) {
            model.setIsHangGuarantee(cmsLoanInfo.getIsHangGuarantee());
        }
        model.setIsCharging(isCharging);
        // model.addAttribute("isAssure", isAssure);
        model.setCreateDate(DateUtils.date2Str(DateUtils.date_sdf()));
        return AjaxJson.success(model);
    }

    /**
     * 添加贷款信息表
     *
     * @param param
     * @return
     */
    @LogMessage(content = "添加贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsLoanInfoApplyUpdateVO param) {
        CmsLoanInfoVO cmsLoanInfoVO = new CmsLoanInfoVO();
        RatUrationDTO urationDTO = new RatUrationDTO();
        BeanUtils.copyProperties(param, cmsLoanInfoVO);
        BeanUtils.copyProperties(param, urationDTO);
        if (StringUtils.isEmpty(cmsLoanInfoVO.getId())) {
            // 生成编号
            String billCode = cmsLoanInfoService.getLoanInfoBillCode(CmsConstant.LOAN_INFO_BILL_CODE_PREFIX);
            cmsLoanInfoVO.setBillCode(billCode);
        }
        cmsLoanInfoService.saveLoanInfo(cmsLoanInfoVO, getCurrentUser(), urationDTO);
        return success();
    }

    /**
     * 添加贷款信息表
     *
     * @param cmsLoanInfoVOs
     * @return
     */
    @LogMessage(content = "添加贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<CmsLoanInfoVO> cmsLoanInfoVOs) {
        for (int i = 0; i < cmsLoanInfoVOs.size(); i++) {
            if (StringUtils.isEmpty(cmsLoanInfoVOs.get(i).getId())) {
                // 生成编号
                String billCode = cmsLoanInfoService.getLoanInfoBillCode(CmsConstant.LOAN_INFO_BILL_CODE_PREFIX);
                cmsLoanInfoVOs.get(i).setBillCode(billCode);
            }
        }
        cmsLoanInfoApplyService.saveImport(cmsLoanInfoVOs, getCurrentUser());
        return success();
    }

    /**
     * 删除贷款信息表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        CmsLoanInfo cmsLoanInfo = cmsLoanInfoService.get(id);
        cmsLoanInfo.setStatus(CmsConstant.DELETED);// 更新状态为-2
        cmsLoanInfoService.updateEntity(cmsLoanInfo);
        // 授信挂接
        if (!EmptyUtils.isEmpty(cmsLoanInfo.getIsHangCredit())
                && CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, id);
            ucprationService.deleteUseCpRation(urationDTO);
        }
        notificationService.complete(NotificationConstant.CMS.LOAN_ADD, cmsLoanInfo.getId(), getCurrentUser().getId());
        return success();
    }
}
