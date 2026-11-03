package com.bytter.guarantee.web.controller.saas;

import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaContractWayService;
import com.bytter.guarantee.domain.service.GuaPledgeInfoService;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fusb
 * @version 1.0
 * @Title GuaMortgageInfoController
 * @Description 抵质押物登记Controller
 * @date 2018年12月11日
 */
@Api(tags = "抵质押物登记Controller",value = "/v1-guarantee/cms/guarantee/pledgeInfo")
@RestController("v1GuaPledgeInfoController")
@RequestMapping("/v1-guarantee/cms/guarantee/pledgeInfo")
public class GuaPledgeInfoController extends BaseController {
    @Autowired
    GuaPledgeInfoService guaPledgeInfoService;
    @Autowired
    GuaContractInfoService guaContractInfoService;
    @Autowired
    GuaContractWayService guaContractWayService;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    @DubboReference(check = false)
    IUserService userService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 获取单位直属网点
     */
    @ApiOperation(value = "获取单位直属网点")
    @GetMapping("/getCenNetId")
    @ResponseBody
    public AjaxJson<Map<String, String>> getCenNetId(String corpId) {
        if (StringUtils.isEmpty(corpId)) {
            return null;
        }
        Map<String, String> corp = guaPledgeInfoService.getCenNetId(corpId);
        return success(corp);
    }
    /**
     * 抵质押物登记页面列表查询
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ApiOperation(value = "抵质押物登记页面列表查询")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<GuaPledgeInfoVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(guaPledgeInfoService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 保存抵质押物信息
     */
    @ApiOperation(value = "保存抵质押物信息")
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson savePledgeInfo(@RequestBody GuaPledgeInfoVO pledgeInfoVO) {
        guaPledgeInfoService.tempOrSubmitInfo(pledgeInfoVO, this.getCurrentUser());
        return success();
    }
    /**
     * 更新抵质押物信息
     */
    @ApiOperation(value = "更新前获取抵质押物信息")
    @LogMessage(content = "更新抵质押物信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editPledgeInfo/{id}")
    public AjaxJson<GuaPledgeInfoVO> editPledgeInfo(@PathVariable("id") String id) {
        GuaPledgeInfoVO vo = new GuaPledgeInfoVO();
        if (StringUtils.isNotEmpty(id)) {
            vo = guaPledgeInfoService.getGuaPledgeInfoVOById(id);
        }
        return success(vo);
    }
    /**
     * 删除抵质押物信息
     */
    @ApiOperation(value = "删除抵质押物信息")
    @LogMessage(content = "删除抵质押物信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson deleteBond(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            guaPledgeInfoService.deleteGuaPledgeInfo(id, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 查看抵质押物信息
     */
    @GetMapping("/view/{id}")
    public AjaxJson<GuaPledgePageVO> viewGuaPledgeInfo(@PathVariable("id") String id) {
        GuaPledgePageVO pageVO = new GuaPledgePageVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgeInfoVO pledgeInfo = guaPledgeInfoService.getGuaPledgeInfoVOById(id);
            String userName = "";
            if (StringUtils.isNotEmpty(pledgeInfo.getCreateBy())) {
                userName = pledgeInfo.getCreateBy();
            } else {
                ICurrentUser user = getCurrentUser();
                userName = user.getUsername();
            }
            pageVO.setGuaPledge(pledgeInfo);
            pageVO.setOperator(userName);
        }
        return success(pageVO);
    }

    /**
     * 审批列表
     */
    @PostMapping("/approveDataList")
    @ResponseBody
    public DataTablesOutput approveDataList(@RequestBody DataTablesInput dataTablesInput) {
        return guaPledgeInfoService.getApproveDataTables(dataTablesInput);
    }
    /**
     * 查看审批详情
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgeInfoVO pledgeInfo = guaPledgeInfoService.getGuaPledgeInfoVOById(id);
            ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
            model.addAttribute("operator", currentUser.getUsername());
            model.addAttribute("guaPledge", pledgeInfo);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/viewGuaPledgeInfoApprove";
    }
    /**
     * 抵质押物查询
     */
    @GetMapping("/searchList")
    @LogMessage(content = "跳转到抵质押物登查询页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<ConfigDTO> searchList(Model model) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        return success(configDTO);
    }
    /**
     * 抵质押物查询列表
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ApiOperation(value = "抵质押物查询列表")
    @PostMapping("/searchtable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<GuaPledgeInfoVO>> searchDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(guaPledgeInfoService.searchDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 抵质押物查询列表
     **/
    @ApiOperation(value = "抵质押物查询列表")
    @PostMapping("/passData")
    @ResponseBody
    public AjaxJson<DataTablesOutput> searchPassData(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        List<String> pDids = new ArrayList<String>();
        String pDYids = tablesInput.getPara("pDids");
        if (pDYids != null && !(pDYids.equals(""))) {
            String[] DYids = pDYids.split(",");
            for (String id : DYids) {
                pDids.add(id);
            }
        }
        return success(guaPledgeInfoService.querypledgeInfoPassTables(tablesInput, pDids));
    }
    /**
     * 通过抵质押id得到抵质押被担保合同占用的信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "通过抵质押id得到抵质押被担保合同占用的信息")
    @GetMapping("/viewOcuppyInfo/{id}")
    public AjaxJson<List<GuaPledgeOccupyInfoVO>> viewGuaPledgeOccupyInfo(@PathVariable("id") String id) {
        List<GuaPledgeOccupyInfoVO> listInfo = guaPledgeInfoService.getGuaPledgeOccupyInfo(id);
        if (listInfo != null && listInfo.size() > 0) {
            for (int i = 0; i < listInfo.size(); i++) {
                StringBuilder guaWayName = new StringBuilder();
                // 根据担保合同id,得到担保方式id,用于初始化复选框
                List<GuaContractWayVO> guaContractWayList = guaContractWayService
                        .getGuaContractWay(listInfo.get(i).getGuaId());
                for (GuaContractWayVO way : guaContractWayList) {
                    GuaWayVO guaWay = guaContractInfoService.getGuaWayById(way.getWayCodes());
                    String mulLan = ii18nService.getBusinessBaseContext(guaWay.getId());
                    if (EmptyUtils.isBlank(guaWayName.toString())) {
                        guaWayName.append(guaWay.getName());
                        // if(StringUtils.isNotEmpty(mulLan)){
                        // guaWayName.append(mulLan);
                        // }else{
                        // guaWayName.append(guaWay.getName());
                        // }
                    } else {
                        guaWayName.append("、").append(guaWay.getName());
                        // if(StringUtils.isNotEmpty(mulLan)){
                        // guaWayName.append("、").append(mulLan);
                        // }else{
                        // guaWayName.append("、").append(guaWay.getName());
                        // }
                    }
                }
                listInfo.get(i).setGuaTypeName(guaWayName.toString());
            }
        }
        return success(listInfo);
    }
    /**
     * 查看手机审批详情
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            GuaPledgeInfoVO pledgeInfo = guaPledgeInfoService.getGuaPledgeInfoVOById(businessId);
            model.addAttribute("guaPledge", pledgeInfo);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/mobileGuaPledgeInfo";
    }
    /**
     * 导入
     *
     * @param
     * @return
     */
    @ApiOperation(value = "导入")
    @LogMessage(content = "导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<GuaPledgeInfoVO> guaPledgeInfoVOS) {
        //TODO 补充名称信息
        fillCorp(guaPledgeInfoVOS);
        fillCurName(guaPledgeInfoVOS);
        fillRegCorpName(guaPledgeInfoVOS);
        for (GuaPledgeInfoVO vo : guaPledgeInfoVOS) {
            guaPledgeInfoService.saveImport(vo, this.getCurrentUser());
        }
        return success();
    }
    private void fillCorp(List<GuaPledgeInfoVO> guaPledgeInfoVOS) {
        if (EmptyUtils.isEmpty(guaPledgeInfoVOS)) {
            return;
        }
        List<String> corpIds = guaPledgeInfoVOS.stream().map(guaPledgeInfo -> guaPledgeInfo.getCorpId()).collect(Collectors.toList());
        Map<String, CorpDTO> corpDTOMap = iCorpService.listCorpDtoByCorpIds(corpIds);
        if (EmptyUtils.isEmpty(corpDTOMap)) {
            return;
        }
        guaPledgeInfoVOS.stream().forEach(guaPledgeInfo -> guaPledgeInfo.setCorpName(corpDTOMap.get(guaPledgeInfo.getCorpId()).getName()));
    }
    private void fillCurName(List<GuaPledgeInfoVO> guaPledgeInfoVOS) {
        if (EmptyUtils.isEmpty(guaPledgeInfoVOS)) {
            return;
        }
        List<String> curIds = guaPledgeInfoVOS.stream().map(guaPledgeInfo -> guaPledgeInfo.getCurId()).collect(Collectors.toList());
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyByIds(curIds);
        if (EmptyUtils.isEmpty(currencyMap)) {
            return;
        }
        guaPledgeInfoVOS.stream().forEach(guaPledgeInfo -> guaPledgeInfo.setCurName(currencyMap.get(guaPledgeInfo.getCurId()).getCurName()));
    }
    private void fillRegCorpName(List<GuaPledgeInfoVO> guaPledgeInfoVOS) {
        if (EmptyUtils.isEmpty(guaPledgeInfoVOS)) {
            return;
        }
        List<String> regCorps = guaPledgeInfoVOS.stream()
                .filter(guaPledgeInfo -> StringUtils.isNotEmpty(guaPledgeInfo.getRegCorp()))
                .map(guaPledge -> guaPledge.getRegCorp()).collect(Collectors.toList());
        if (EmptyUtils.isEmpty(regCorps)) {
            return;
        }
        Map<String, CorpDTO> corpDTOMap = iCorpService.listCorpDtoByCorpIds(regCorps);
        if (EmptyUtils.isEmpty(corpDTOMap)) {
            return;
        }
        guaPledgeInfoVOS.stream().forEach(guaPledgeInfo -> {
            if (EmptyUtils.isEmpty(guaPledgeInfo.getRegCorp())) {
                return;
            }
            guaPledgeInfo.setRegCorpName(corpDTOMap.get(guaPledgeInfo.getRegCorp()).getName());
        });
    }
    /**
     * 批量提交
     *
     * @param ids
     * @return
     */

    @ApiOperation("批量提交抵质押物")
    @LogMessage(content = "批量提交抵质押物", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestBody BisIDsDTO bisIDsDTO) {
        String ids = bisIDsDTO.getIds();
        guaPledgeInfoService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }
}
