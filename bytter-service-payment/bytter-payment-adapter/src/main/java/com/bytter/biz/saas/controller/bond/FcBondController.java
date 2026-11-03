package com.bytter.biz.saas.controller.bond;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bond.vo.BondConfigVO;
import com.bytter.biz.payment.api.dto.BisBatchEditInterDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondDetailService;
import com.bytter.guarantee.api.IFcBondLogService;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.guarantee.vo.FcBondLogVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.pricing.service.IPricingDepositRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


/**
 * @author luxy
 * @version 1.0
 * @Title: Controller
 * @Description: 保证金管理
 * @date 2018-09-04
 */
@Api(value = "/v1-payment/bond/fcbond",tags = "存入保证金")
@RestController("v1FcBondController")
@RequestMapping("/v1-payment/bond/fcbond")
public class FcBondController extends BaseController {
    @DubboReference(check = false)
    private IFcBondDetailService fcBondDetailService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IPricingDepositRateService iPricingDepositRateService;
    @DubboReference(check = false)
    IFcBondLogService fcBondLogService;
    @DubboReference(check = false)
    private IBisAccBalService iBisAccBalService;
    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IConfigService configService;




    @GetMapping("/bondConfig")
    @ApiOperation("保证金管理参数获取")
    public AjaxJson<BondConfigVO> bondConfig() {
        BondConfigVO bondConfigVO = new BondConfigVO();
        bondConfigVO.setConfig(configService.listByType(ConfigConstant.BOND));
        return success(bondConfigVO);
    }


    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("加载审核列表数据")
    @LogMessage(content = "查询审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput<WorkFlowPersistence>> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(this.fcBondDetailService.dataTableApprove(input));
    }

    /**
     * fusb,2018-9-12添加 根据保证金id,得到保证金信息,跳转到审批详情页面
     *
     * @param id
     * @return
     */
    @ApiOperation("根据保证金id,得到保证金信息,跳转到审批详情页面")
    @LogMessage(content = "跳转到审批详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/approve")
    public AjaxJson<FcBondDetailVO> approve(@RequestParam("id")  String id) {
        if (StringUtils.isNotEmpty(id)) {
            FcBondDetailVO fcBondDetailVO = this.fcBondDetailService.getFcBondById(id);
            if (StringUtils.isNotEmpty(fcBondDetailVO.getOutBankAcc())) {
                BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcBondDetailVO.getOutBankAcc(), fcBondDetailVO.getCurId());
                if (null != bankInfo) {
                    fcBondDetailVO.setOutBankAccMoney(bankInfo.getAvailBal());
                } else {
                    fcBondDetailVO.setOutBankAccMoney(BigDecimal.ZERO);
                }
            }
            return AjaxJson.success(fcBondDetailVO);
        }
        return AjaxJson.success();
    }

    /**
     * 保证金表 table列表
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("保证金表table列表")
    @LogMessage(content = "查询FC_BOND列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<FcBondDetailVO> > dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fcBondDetailService.getDataTablesUnion(input, this.getCurrentUser()));
    }



    /**
     * 保存新增的保证金
     *
     * @param fcBond
     * @return AjaxJson
     */
    @ApiOperation("保存新增的保证金")
    @LogMessage(content = "添加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody FcBondDetailVO fcBond) {
        fcBondDetailService.saveFcBondDetail(fcBond, this.getCurrentUser());
        return success();
    }

    /**
     * 获取已经开好户的保证金银行账户
     *
     * @param corpId
     * @return
     */
    @ApiOperation("获取已经开好户的保证金银行账户")
    @PostMapping("/getBondAcc")
    public AjaxJson<List<BtBankAccDTO>> getBondAcc(String corpId) {
        List<BtBankAccDTO> listBondAcc = new ArrayList<BtBankAccDTO>();
        BtBankAccDTO bankDTO = new BtBankAccDTO();
        if (StringUtils.isNotEmpty(corpId)) {
            bankDTO.setCorpId(corpId);
        } else {
            List<String> corpIdList = corpService.getCorpIdsByUserResource("netbank:fcBond:depositOut:apply", getCurrentUser().getId());
            String corpIds = String.join(",", corpIdList);
            bankDTO.setCorpId(corpIds);
        }
        bankDTO.setAccType("04");
        listBondAcc = iBtBankAccService.getBankAccListAll(bankDTO);
        return AjaxJson.success(listBondAcc);
    }

    /**
     * @author luxy 判断所选保证金账户是否已存出保证金
     */
    @ApiOperation("判断所选保证金账户是否已存出保证金")
    @PostMapping("/queryBondAcc/{id}")
    public AjaxJson<Boolean> queryBondAcc(@PathVariable("id") String id) {
        boolean success = fcBondDetailService.queryBondAcc(id);
        AjaxJson result =new AjaxJson(success);
        if(!success){
            result.setMsg("fcbond.add.checkBondAcc");
        }
        return result;
    }

    /**
     * 编辑存出保证金保证金页面跳转
     *
     * @param id
     * @return
     */
    @ApiOperation("编辑存出保证金保证金页面跳转")
    @LogMessage(content = "编辑保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editBond/{id}")
    public AjaxJson<FcBondDetailVO> editBond(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            FcBondDetailVO fcBond = fcBondDetailService.getFcBondById(id);
            if (StringUtils.isNotEmpty(fcBond.getOutBankAcc())) {
                BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcBond.getOutBankAcc(), fcBond.getCurId());
                if (null != bankInfo) {
                    fcBond.setOutBankAccMoney(bankInfo.getAvailBal());
                } else {
                    fcBond.setOutBankAccMoney(BigDecimal.ZERO);
                }
            }
            BtBankAccDTO bankAccInfo = iBtBankAccService.getBankAccInfo(fcBond.getOutBankAcc());
            if (null != bankAccInfo){
                fcBond.setIsOnline(bankAccInfo.getIsOnline());
            }
            return AjaxJson.success(fcBond);
        }
        return AjaxJson.success();
    }

    /**
     * 修改存出保证金
     *
     * @param fcBond
     * @return
     */
    @ApiOperation("修改存出保证金")
    @LogMessage(content = "修改保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody  FcBondDetailVO fcBond) {
        fcBondDetailService.updateFcBondDetail(fcBond, this.getCurrentUser());
        return success();
    }

    /**
     * 查看存出保证金详情
     *
     * @param id
     * @return
     */
    @ApiOperation("查看存出保证金详情")
    @LogMessage(content = "查询保证金信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewBond/{id}")
    public AjaxJson<FcBondDetailVO> viewBond(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            FcBondDetailVO fcBond = fcBondDetailService.getFcBondById(id);
            if (StringUtils.isNotEmpty(fcBond.getOutBankAcc())) {
                BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcBond.getOutBankAcc(), fcBond.getCurId());
                if (null != bankInfo) {
                    fcBond.setOutBankAccMoney(bankInfo.getAvailBal());
                } else {
                    fcBond.setOutBankAccMoney(BigDecimal.ZERO);
                }
            }
            if(StringUtils.isNotEmpty(fcBond.getIsRegular())){
                fcBond.setIsRegularName("1".equals(fcBond.getIsRegular()) ? "是" : "否");
            }
            BtBankAccDTO bankAccInfo = iBtBankAccService.getBankAccInfo(fcBond.getOutBankAcc());
            if (null != bankAccInfo){
                fcBond.setIsOnline(bankAccInfo.getIsOnline());
            }
            return AjaxJson.success(fcBond);
        }
        return AjaxJson.success();
    }

    /**
     * 删除存出保证金
     *
     * @param id
     * @return
     */
    @ApiOperation("删除存出保证金")
    @LogMessage(content = "删除某条保证金信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/deleteBond/{id}")
    public AjaxJson deleteBond(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            fcBondDetailService.deleteBond(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 存出保证金手机审批页面
     *
     * @param businessId
     * @return
     */
    @ApiOperation("存出保证金手机审批页面")
    @LogMessage(content = "跳转到手机审批详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/mobileApprove")
    public AjaxJson<Map<String,Object>> mobileApprove(String businessId) {
        Map<String,Object>  model=new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            FcBondDetailVO fcBondDetailVO = this.fcBondDetailService.getFcBondById(businessId);
            model.put("fcBond", fcBondDetailVO);
            List<FcBondLogVO> listOpMessage = fcBondLogService.listOpMessage(businessId);//操作日志
            model.put("listOpMessage", listOpMessage);
        }
        return AjaxJson.success(model);
    }

    /**
     * 根据期限、币别取定期利率
     */
    @ApiOperation("根据期限、币别取定期利率")
    @PostMapping("/getRate")
    public AjaxJson<BigDecimal> getRate(String dayLimit, String curId) {
        BigDecimal rate = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(dayLimit) && StringUtils.isNotEmpty(curId)) {
            rate = iPricingDepositRateService.getDepositRateByPeriod("05", dayLimit, curId);
        }
        return AjaxJson.success(rate);
    }




    /**
     * 保证金账户查询批量编辑利率
     *
     * @param bondIds      多条id记录
     * @param interestRate 利率
     * @return 操作结果
     */
    @ApiOperation("保证金账户查询批量编辑利率")
    @LogMessage(content = "保证金账户查询批量编辑利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchEditInterestRate")
    public AjaxJson batchEditInterestRate(@RequestBody BisBatchEditInterDTO batchEditInterDTO) {
        fcBondDetailService.batchEditInterestRate(batchEditInterDTO.getBondIds(), batchEditInterDTO.getInterestRate(), this.getCurrentUser());
        return success();
    }
}
