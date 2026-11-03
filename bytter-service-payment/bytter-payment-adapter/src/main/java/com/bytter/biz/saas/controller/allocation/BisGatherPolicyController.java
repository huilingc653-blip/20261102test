package com.bytter.biz.saas.controller.allocation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.entity.BisGatherPolicyAccount;
import com.bytter.biz.allocation.entity.BisGatherPolicyAgreement;
import com.bytter.biz.allocation.service.BisGatherPolicyAccountService;
import com.bytter.biz.allocation.service.BisGatherPolicyAgreementService;
import com.bytter.biz.allocation.vo.BisGatherPolicyAccountVO;
import com.bytter.biz.allocation.vo.BisGatherPolicyAgreementVO;
import com.bytter.biz.allocation.vo.BisGatherPolicyVO;
import com.bytter.biz.bdlink.api.enums.AllocationMessageEnum;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.inneraccount.service.BisGatherPolicyService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
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
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/// /import org.apache.shiro.authz.annotation.Logical;
/// ///import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 调拨策略表
 * @date 2017-06-16 14:38:52
 */
@Api(value = "/v1-payment/allocation/bisgatherpolicy", tags = "资金中心调拨账户配置")
@RestController("v1BisGatherPolicyController")
@RequestMapping("/v1-payment/allocation/bisgatherpolicy")
public class BisGatherPolicyController extends BaseController {
    @Autowired
    private BisGatherPolicyService bisGatherPolicyService;
    @Autowired
    private BisGatherPolicyAccountService bisGatherPolicyAccountService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    @Autowired
    private BisGatherPolicyAgreementService bisGatherPolicyAgreementService;


    /**
     * 调拨策略表 table列表
     *
     * @return
     */
    @ApiOperation("调拨策略表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BisGatherPolicy>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisGatherPolicyService.getDataTables(input));
    }
    /**
     * 调拨策略表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("调拨策略表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<BisGatherPolicyVO> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            BisGatherPolicyVO gatherPolicyVO = new BisGatherPolicyVO();
            BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(id, true);
            if (ObjectUtil.isNotEmpty(bisGatherPolicy) && "6".equals(bisGatherPolicy.getGatherMode())) {
                // 去除小数点后面的0，直接去掉。不进行四舍五入
                bisGatherPolicy.setGatherAmt(bisGatherPolicy.getGatherAmt().setScale(0, BigDecimal.ROUND_DOWN));
            }
            if (ObjectUtil.isNotEmpty(bisGatherPolicy) && "5".equals(bisGatherPolicy.getGatherMode())) {
                // 去除小数点后面的0，直接去掉。不进行四舍五入
                bisGatherPolicy.setMinAmt(bisGatherPolicy.getMinAmt().setScale(0, BigDecimal.ROUND_DOWN));
            }
            BeanUtils.copyProperties(bisGatherPolicy, gatherPolicyVO);
            setDictName(gatherPolicyVO);

            return AjaxJson.success(gatherPolicyVO);
        }
        return AjaxJson.success();
    }

    private void setDictName(BisGatherPolicyVO gatherPolicyVO) {
        BtCurrencyDTO currencyDTO = this.currencyService.getObjectById(gatherPolicyVO.getCurId());
        if (!EmptyUtils.isEmpty(currencyDTO)) {
            gatherPolicyVO.setCurName(currencyDTO.getCurName());
        }
        if(StringUtils.isNotEmpty(gatherPolicyVO.getGatherType())){
            gatherPolicyVO.setGatherTypeName(dictService.get("bisGatherPolicy.gatherType", gatherPolicyVO.getGatherType()));
        }
        if(StringUtils.isNotEmpty(gatherPolicyVO.getFrqMode())){
            gatherPolicyVO.setFrqModeName(dictService.get("bisGatherPolicy.frqMode", gatherPolicyVO.getFrqMode()));
        }
    }

    /**
     * 调拨策略表详情页
     *
     * @return
     */
    @ApiOperation("查询调拨策略表详情")
    @LogMessage(content = "查询调拨策略表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BisGatherPolicyVO> view(@PathVariable("id") String id) {
        BisGatherPolicyVO bisGatherPolicyVO = new BisGatherPolicyVO();
        BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(id, true);
        if (ObjectUtil.isNotEmpty(bisGatherPolicy) && "6".equals(bisGatherPolicy.getGatherMode())) {
            // 去除小数点后面的0，直接去掉。不进行四舍五入
            bisGatherPolicy.setGatherAmt(bisGatherPolicy.getGatherAmt().setScale(0, BigDecimal.ROUND_DOWN));
        }
        if (ObjectUtil.isNotEmpty(bisGatherPolicy) && "5".equals(bisGatherPolicy.getGatherMode())) {
            // 去除小数点后面的0，直接去掉。不进行四舍五入
            bisGatherPolicy.setMinAmt(bisGatherPolicy.getMinAmt().setScale(0, BigDecimal.ROUND_DOWN));
        }
        BeanUtils.copyProperties(bisGatherPolicy, bisGatherPolicyVO);
        setDictName(bisGatherPolicyVO);
        return AjaxJson.success(bisGatherPolicyVO);
    }

    /**
     * 添加调拨策略表
     *
     * @param bisGatherPolicy
     * @return
     */
    @ApiOperation("添加调拨策略表")
    @LogMessage(content = "添加调拨策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody BisGatherPolicy bisGatherPolicy) {
        //判断策略编码是否唯一
        if (bisGatherPolicyService.findByCode(bisGatherPolicy.getGatherPolicyCode()) != null) {
            throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_GATHERPOLICYCODE_UNIQUE);
        }
        if (bisGatherPolicy != null) {
            //4固定余额 3固定额度 0根据前一日归集金额下拨
            if ("0".equals(bisGatherPolicy.getGatherMode())) {
                //将frq_mode和serial_day置空
                bisGatherPolicy.setFrqMode(null);
                bisGatherPolicy.setSerialDay(null);
            }
            if ("4".equals(bisGatherPolicy.getGatherMode())) {
                //将downFixedAmt置空
                bisGatherPolicy.setDownFixedAmt(null);
            }
            if ("3".equals(bisGatherPolicy.getGatherMode())) {
                //将gatherAmt置空
                bisGatherPolicy.setGatherAmt(null);
            }
        }
        String launchTime = DateUtils.formatDate(bisGatherPolicy.getDefDate(), "HH:mm:ss").replace(":", "");
        bisGatherPolicy.setLaunchTime(launchTime);
        String endTime = DateUtils.formatDate(bisGatherPolicy.getEndDate(), "HH:mm:ss").replace(":", "");
        bisGatherPolicy.setEndTime(endTime);
        bisGatherPolicyService.save(bisGatherPolicy, this.getCurrentUser());
        return success();
    }

    /**
     * 修改调拨策略表
     *
     * @param bisGatherPolicy
     * @return
     */
    @ApiOperation("修改调拨策略表")
    @LogMessage(content = "修改调拨策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody BisGatherPolicy bisGatherPolicy) {
        bisGatherPolicyService.updateBisGatherPolicy(bisGatherPolicy);
        return success();
    }

    /**
     * 删除调拨策略表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除调拨策略表")
    @LogMessage(content = "删除调拨策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(id);
        bisGatherPolicyService.deleteEntity(bisGatherPolicy);
        return success();
    }

    /**
     * 批量删除调拨策略表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除调拨策略表")
    @LogMessage(content = "批量删除调拨策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@RequestBody BisIDsDTO idsDTO) {
        String ids = idsDTO.getIds();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(id);
            bisGatherPolicyService.deleteEntity(bisGatherPolicy);
        }
        return success();
    }

    /**
     * 批量启用调拨策略
     *
     * @return
     */
    @ApiOperation("批量启用调拨策略")
    @LogMessage(content = "批量启用调拨策略", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchEnable")
    public AjaxJson batchEnable(@RequestBody BisIDsDTO idsDTO) {
        String ids = idsDTO.getIds();
        this.bisGatherPolicyService.batchUpdateValidSign(ids, CommonConstant.YES);
        return success();
    }

    /**
     * 批量停用调拨策略
     *
     * @return
     */
    @ApiOperation("批量停用调拨策略")
    @LogMessage(content = "批量停用调拨策略", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchStop")
    public AjaxJson batchStop(@RequestBody BisIDsDTO idsDTO) {
        String ids = idsDTO.getIds();
        this.bisGatherPolicyService.batchUpdateValidSign(ids, CommonConstant.NO);
        return success();
    }


    @ApiOperation("查询列表数据")
    @PostMapping("/accountDatatable")
    public AjaxJson<DataTablesOutput<BisGatherPolicyAccountVO>> accountDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns( );
        return AjaxJson.success(bisGatherPolicyAccountService.getAccountDatatable(input));
    }

    @ApiOperation("查询支付账户")
    @GetMapping("/getPayerAccQuery")
    public AjaxJson<List<BtBankAccDTO>> getPayerAccQuery(@RequestParam String resource, @RequestParam String corpCode) {
        String corpId = "";
        if (ObjectUtil.isNotEmpty(corpCode)) {
            CorpDTO corp = iCorpService.getCorpByCode(corpCode);
            if (ObjectUtil.isNotEmpty(corp)) {
                corpId = corp.getId();
            }
        }
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        //获取转账权限的银行账号
        List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), corpId, resource, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
        for (BtBankAccDTO dto : listBankAccTemp) {
            dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
            listAcc.add(dto);
        }
        List<FcAccDTO> listFcAcc = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), corpId, resource, null, "1", AccountGrantConstant.TRANSFER_ACCOUNT);
        if (!EmptyUtils.isEmptyList(listFcAcc)) {
            for (FcAccDTO fcAcc : listFcAcc) {
                //过滤企业活期账号
                if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAcc.getAccType())) {
                    BtBankAccDTO bankAcc = new BtBankAccDTO();
                    bankAcc.setId(fcAcc.getId());
                    bankAcc.setBankAcc(fcAcc.getBlnctrAcc());
                    bankAcc.setAccName(fcAcc.getAccName());
                    bankAcc.setCurId(fcAcc.getCurId());
                    bankAcc.setCurName(fcAcc.getCurName());
                    bankAcc.setBankName(fcAcc.getNetName());
                    bankAcc.setBalance(fcAcc.getFcBalance());
                    listAcc.add(bankAcc);
                }
            }
        }
        return AjaxJson.success(listAcc);
    }

    @ApiOperation("添加资金中心调拨账户配置")
    @LogMessage(content = "添加资金中心调拨账户配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveAccount")
    public AjaxJson saveAccount(@RequestBody BisGatherPolicyAccountVO policyAccountVO) {
        //判断账号
        if (bisGatherPolicyAccountService.checkExist("bankAcc", policyAccountVO.getBankAcc(), null)) {
            throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_ACCOUNT_EXIST, policyAccountVO.getBankAcc());
        }
        // 判断优先级是否唯一
        if (bisGatherPolicyAccountService.checkExist("priority", policyAccountVO.getPriority(), null)) {
            throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_PRIORITY_EXIST, policyAccountVO.getPriority().toString());
        }
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(policyAccountVO.getBankAcc());
        if (ObjectUtil.isNotEmpty(bankAccInfo)) {
            policyAccountVO.setBankName(bankAccInfo.getBankName());
            policyAccountVO.setCorpName(bankAccInfo.getCorpName());
        }
        bisGatherPolicyAccountService.save(policyAccountVO, this.getCurrentUser());
        return success();
    }


    @ApiOperation("删除资金中心调拨账户配置")
    @LogMessage(content = "删除资金中心调拨账户配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/deleteAccount/{id}")
    public AjaxJson deleteAccount(@PathVariable("id") String id) {
        BisGatherPolicyAccount policyAccount = bisGatherPolicyAccountService.get(id);
        bisGatherPolicyAccountService.deleteEntity(policyAccount);
        return success();
    }

    @ApiOperation("查询资金中心调拨账户配置")
    @LogMessage(content = "查询资金中心调拨账户配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewAccount/{id}")
    public AjaxJson<BisGatherPolicyAccount> viewAccount(@PathVariable("id") String id) {
        BisGatherPolicyAccount policyAccount = bisGatherPolicyAccountService.get(id, true);
        return AjaxJson.success(policyAccount);
    }

    @ApiOperation("更新资金中心调拨账户配置页面数据")
    @GetMapping("/updateAccount/{id}")
    public AjaxJson<BisGatherPolicyAccount> updateAccount(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(bisGatherPolicyAccountService.get(id, true));
        }
        return AjaxJson.success();
    }

    @ApiOperation("修改资金中心调拨账户配置")
    @LogMessage(content = "修改资金中心调拨账户配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyAccount")
    public AjaxJson modifyAccount(@RequestBody BisGatherPolicyAccountVO policyAccountVO) {
        // 判断账号是否唯一
        if (bisGatherPolicyAccountService.checkExist("bankAcc", policyAccountVO.getBankAcc(), policyAccountVO.getId())) {
            throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_ACCOUNT_EXIST, policyAccountVO.getBankAcc());
        }
        // 判断优先级是否唯一
        if (bisGatherPolicyAccountService.checkExist("priority", policyAccountVO.getPriority(), policyAccountVO.getId())) {
            throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_PRIORITY_EXIST, policyAccountVO.getPriority().toString());
        }
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(policyAccountVO.getBankAcc());
        if (ObjectUtil.isNotEmpty(bankAccInfo)) {
            policyAccountVO.setBankName(bankAccInfo.getBankName());
            policyAccountVO.setCorpName(bankAccInfo.getCorpName());
        }
        bisGatherPolicyAccountService.modifyAccount(policyAccountVO, this.getCurrentUser());
        return success();
    }

    @ApiOperation("内部资金归集管理协议签订列表")
    @PostMapping("/agreementDatatable")
    public AjaxJson<DataTablesOutput<BisGatherPolicyAgreementVO>> agreementDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<BisGatherPolicyAgreementVO> result=new DataTablesOutput<>();
                DataTablesOutput<BisGatherPolicyAgreement> out=  bisGatherPolicyAgreementService.getDataTables(input);
                BeanUtil.copyProperties(out, result);
                if(EmptyUtils.isNotEmpty(out.getData())){
                    result.setData(BeanUtil.copyToList(out.getData(), BisGatherPolicyAgreementVO.class));
                }
        return AjaxJson.success(result);
    }

    @ApiOperation("添加内部资金归集管理协议签订")
    @LogMessage(content = "添加内部资金归集管理协议签订", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveAgreement")
    public AjaxJson saveAgreement(@RequestBody BisGatherPolicyAgreementVO policyAgreementVO) {
        //判断单位
        String[] corpIds = policyAgreementVO.getCorpId().split(CommonConstant.COMMA_SPLIT);
        for (String corpId : corpIds) {
            if (bisGatherPolicyAgreementService.checkCorpExist(corpId, null)) {
                CorpDTO corpDTO = iCorpService.get(corpId);
                throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_AGREEMENT_EXIST, corpDTO.getName());
            }
        }
        bisGatherPolicyAgreementService.save(policyAgreementVO, this.getCurrentUser());
        return success();
    }

    @ApiOperation("删除内部资金归集管理协议签订")
    @LogMessage(content = "删除内部资金归集管理协议签订", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/deleteAgreement/{id}")
    public AjaxJson deleteAgreement(@PathVariable("id") String id) {
        BisGatherPolicyAgreement policyAgreement = bisGatherPolicyAgreementService.get(id);
        bisGatherPolicyAgreementService.deleteEntity(policyAgreement);
        return success();
    }

    @ApiOperation("修改内部资金归集管理协议签订页面")
    @GetMapping("/updateAgreement/{id}")
    public AjaxJson<BisGatherPolicyAgreement> updateAgreement(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(bisGatherPolicyAgreementService.get(id, true));
        }
        return AjaxJson.success();
    }

    @ApiOperation("修改内部资金归集管理协议签订")
    @LogMessage(content = "修改内部资金归集管理协议签订", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyAgreement")
    public AjaxJson modifyAgreement(@RequestBody BisGatherPolicyAgreementVO policyAgreementVO) {
        //判断单位
        String[] corpIds = policyAgreementVO.getCorpId().split(CommonConstant.COMMA_SPLIT);
        for (String corpId : corpIds) {
            if (bisGatherPolicyAgreementService.checkCorpExist(corpId, policyAgreementVO.getId())) {
                CorpDTO corpDTO = iCorpService.get(corpId);
                throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_AGREEMENT_EXIST, corpDTO.getName());
            }
        }
        bisGatherPolicyAgreementService.modify(policyAgreementVO, this.getCurrentUser());
        return success();
    }

    @ApiOperation("导入")
    @PostMapping("/saveAgreementImport")
    public AjaxJson saveAgreementImport(@RequestBody List<BisGatherPolicyAgreementVO> list) {
        bisGatherPolicyAgreementService.saveImport(list, this.getCurrentUser());
        return success();
    }

}
