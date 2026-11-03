package com.bytter.guarantee.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaContractWayService;
import com.bytter.guarantee.domain.service.GuaPledgeInfoService;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.guarantee.domain.vo.GuaPledgeOccupyInfoVO;
import com.bytter.guarantee.domain.vo.GuaWayVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
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
@Controller
@RequestMapping("/cms/guarantee/pledgeInfo")
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
     * 抵质押物列表
     */
    @GetMapping("/list")
    @LogMessage(content = "跳转到抵质押物登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String list(Model model) {
        return "cms/guarantee/listGuaPledgeInfo";
    }
    /**
     * 获取单位直属网点
     */
    @GetMapping("/getCenNetId")
    @ResponseBody
    public Map<String, String> getCenNetId(String corpId) {
        if (StringUtils.isEmpty(corpId)) {
            return null;
        }
        Map<String, String> corp = guaPledgeInfoService.getCenNetId(corpId);
        return corp;
    }
    /**
     * 地址押物登记页面列表查询
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput<GuaPledgeInfoVO> dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return guaPledgeInfoService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 跳转到新增抵质押物登记页面
     */
    @GetMapping("/add")
    @LogMessage(content = "跳转到新增抵质押物登记页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String addGuaPledgeInfo(Model model) {
        return "cms/guarantee/addGuaPledgeInfo";
    }
    /**
     * 保存抵质押物信息
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson savePledgeInfo(GuaPledgeInfoVO pledgeInfoVO) {
        guaPledgeInfoService.tempOrSubmitInfo(pledgeInfoVO, this.getCurrentUser());
        return success();
    }
    /**
     * 更新抵质押物信息
     */
    @LogMessage(content = "更新抵质押物信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/editPledgeInfo/{id}")
    public String editPledgeInfo(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgeInfoVO vo = new GuaPledgeInfoVO();
            vo = guaPledgeInfoService.getGuaPledgeInfoVOById(id);
            model.addAttribute("vo", vo);
        }
        return "cms/guarantee/updateGuaPledgeInfo";
    }
    /**
     * 删除抵质押物信息
     */
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
    public String viewGuaPledgeInfo(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgeInfoVO pledgeInfo = guaPledgeInfoService.getGuaPledgeInfoVOById(id);
            String userName = "";
            if (StringUtils.isNotEmpty(pledgeInfo.getCreateBy())) {
                userName = pledgeInfo.getCreateBy();
            } else {
                ICurrentUser user = getCurrentUser();
                userName = user.getUsername();
            }
            model.addAttribute("guaPledge", pledgeInfo);
            model.addAttribute("operator", userName);
        }
        return "cms/guarantee/viewGuaPledgeInfo";
    }
    /**
     * 抵质押物登记审批列表
     */
    @GetMapping("/listApprove")
    @LogMessage(content = "跳转到抵质押物登记审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/guarantee/listGuaPledgeApprove";
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
    public String searchList(Model model) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        model.addAttribute("config", configDTO);
        return "cms/guarantee/listSearchGuaPledgeInfo";
    }
    /**
     * 抵质押物查询列表
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @PostMapping("/searchtable")
    @ResponseBody
    public DataTablesOutput<GuaPledgeInfoVO> searchDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return guaPledgeInfoService.searchDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 抵质押物查询列表
     **/
    @PostMapping("/passData")
    @ResponseBody
    public DataTablesOutput searchPassData(@RequestBody DataTablesInput dataTablesInput) {
        List<String> pDids = new ArrayList<String>();
        String pDYids = dataTablesInput.getPara("pDids");
        if (pDYids != null && !(pDYids.equals(""))) {
            String[] DYids = pDYids.split(",");
            for (String id : DYids) {
                pDids.add(id);
            }
        }
        return guaPledgeInfoService.querypledgeInfoPassTables(dataTablesInput, pDids);
    }
    /**
     * 通过抵质押id得到抵质押被担保合同占用的信息
     *
     * @param id
     * @return
     */
    @GetMapping("/viewOcuppyInfo/{id}")
    public String viewGuaPledgeOccupyInfo(@PathVariable("id") String id, Model model) {
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
            model.addAttribute("listInfo", listInfo);
        }
        return "cms/guarantee/viewGuaContractPledgeInfo";
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
        guaPledgeInfoVOS.stream().forEach(guaPledgeInfo -> guaPledgeInfo.setRegCorpName(corpDTOMap.get(guaPledgeInfo.getRegCorp()).getName()));
    }
    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量提交抵质押物", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        guaPledgeInfoService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }
}
