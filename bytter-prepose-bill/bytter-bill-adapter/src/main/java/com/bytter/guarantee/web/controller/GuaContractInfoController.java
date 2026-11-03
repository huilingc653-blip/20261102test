package com.bytter.guarantee.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.service.GuaContractChangeService;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.guarantee.dto.GuaWayDTO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @version 1.0
 * @Title:GuaContractInfoController
 * @Description:被担保合同管理控制层
 * @date 2018年12月12日 上午9:45:46
 */
@Controller
@RequestMapping("/cms/guarantee/guaContractInfo")
public class GuaContractInfoController extends BaseController {
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private GuaContractChangeService guaContractChangeService;
    @Autowired
    private IGuaWayServie iGuaWayServie;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 被担保合同列表toListPageInfo
     *
     * @param model 试图
     * @return 页面
     */
    @GetMapping("/list")
    @LogMessage(content = "跳转到担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String list(Model model) {
        return "cms/guarantee/listGuaContractInfo";
    }

    /**
     * 列表内容
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput<GuaContractInfoVO> dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return guaContractInfoService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 跳转到新增被担保合同登记页面
     *
     * @param model 模型
     * @return 新增页面
     */
    @GetMapping("/add")
    @LogMessage(content = "跳转到新增被担保合同登记页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String addGuaPledgeInfo(Model model) {
        // 担保对接合同管理
        String relateCon = guaContractInfoService.relateCon();
        if (GuaranteeConstant.RELATE_CON_YES.equals(relateCon)) {
            String conType = guaContractInfoService.conType();
            model.addAttribute("conType", conType);
        }
        model.addAttribute("relateCon", relateCon);
        return "cms/guarantee/addGuaContractInfo";
    }

    /**
     * 保存担保合同信息
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson savePledgeInfo(GuaContractInfoVO contractVO) {
        this.handelData(contractVO);
        contractVO.setBusinessType(GuaranteeConstant.CON_TYPE_0);// 担保合同类型为被担保合同
        guaContractInfoService.tempOrSubmitInfo(contractVO, this.getCurrentUser());
        return success();
    }

    /**
     * 修改被担保合同登记页面跳转
     */
    @LogMessage(content = "修改被担保合同", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/editContractInfo/{id}")
    public String editContractInfo(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoVO vo = guaContractInfoService.getGuaContractInfoById(id);
            if (vo != null) {
                guaContractInfoService.getGuaPerson(vo);
                model.addAttribute("vo", vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                model.addAttribute("guaWays", guaWays);
            }
            // 对接合同管理
            String relateCon = guaContractInfoService.relateCon();
            if (GuaranteeConstant.RELATE_CON_YES.equals(relateCon)) {// 担保对接合同管理
                String conType = guaContractInfoService.conType();
                model.addAttribute("conType", conType);
                model.addAttribute("conId", vo.getConId());
                model.addAttribute("conTypeId", vo.getConTypeId());
            }
            model.addAttribute("relateCon", relateCon);
        }
        return "cms/guarantee/updateGuaContractInfo";
    }

    /**
     * 查询担保方式
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<GuaWayVO> getListGuaWay() {
        List<GuaWayVO> list = guaContractInfoService.getListGuaWay();
        return list;
    }

    /**
     * 带参数传递
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/toPage")
    public String toOccupyPage(ParamsVO vo, Model model) {
        model.addAttribute("vo", vo);
        List<GuaPledgeInfoVO> list = guaContractInfoService.convertPledgeInfoList(vo.getPledgeMsg());
        model.addAttribute("list", list);
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        model.addAttribute("config", configDTO);
        // if(vo.getPledgeAttr().equals(GuaranteeConstant.GUA_TYPE_DY)){
        // List<GuaPledgeInfoVO> list = guaContractInfoService.convertPledgeInfoList(vo.getpDids());
        // model.addAttribute("list", list);
        // }
        //
        // if(vo.getPledgeAttr().equals(GuaranteeConstant.GUA_TYPE_ZY)){
        // List<GuaPledgeInfoVO> list = guaContractInfoService.convertPledgeInfoList(vo.getpZids());
        // model.addAttribute("list", list);
        // }
        return "cms/guarantee/occupyPledgePage";
    }

    /**
     * 带参数传递
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/toListPageInfo")
    public String toOccupyPageInfo(ParamsVO vo, Model model) {
        model.addAttribute("vo", vo);
        // 获取是否允许多次抵押
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        model.addAttribute("config", configDTO);
        return "cms/guarantee/listSelectGuaPledge";
    }

    /**
     * 相应ajax的get请求
     */
    @GetMapping("getInfo")
    @ResponseBody
    public List<GuaPledgeInfoVO> getListGuPledgeInfo(ParamsVO vo) {
        List<String> pledgeIds = new ArrayList<String>();
        if (vo.getPledgeIds() != null && !(vo.getPledgeIds().equals(""))) {
            String[] ids = vo.getPledgeIds().split(",");
            for (String id : ids) {
                pledgeIds.add(id);
            }
        }
        if (pledgeIds != null && pledgeIds.size() > 0) {
            List<GuaPledgeInfoVO> list = guaContractInfoService.getListGuaPledgeInfo(pledgeIds);
            return list;
        } else {
            return null;
        }
    }

    @LogMessage(content = "删除合同信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delGuaContractInfo(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            guaContractInfoService.delGuaContractInfo(id, this.getCurrentUser());
        }
        return success();
    }

    @LogMessage(content = "查看担保合同信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewInfo/{id}")
    public String viewGuaContractInfo(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoVO vo = new GuaContractInfoVO();
            vo = guaContractInfoService.getGuaContractInfoById(id);
            if (vo != null) {
                model.addAttribute("vo", vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            model.addAttribute("guaWays", guaWays);
            String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
            model.addAttribute("guaWayNames", guaWayNames);
        }
        return "cms/guarantee/viewGuaContractInfo";
    }

    /**
     * 担保合同审批列表页面跳转
     */
    @GetMapping("/listApprove")
    @LogMessage(content = "担保合同审批列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/guarantee/listGuaContractApprove";
    }

    /**
     * 审批列表
     */
    @PostMapping("/approveDataList")
    @ResponseBody
    public DataTablesOutput approveDataList(@RequestBody DataTablesInput dataTablesInput) {
        return guaContractInfoService.getApproveDataTables(dataTablesInput);
    }

    /**
     * 查看审批详情
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoVO vo = new GuaContractInfoVO();
            vo = guaContractInfoService.getGuaContractInfoById(id);
            if (vo != null) {
                model.addAttribute("vo", vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                model.addAttribute("guaWays", guaWays);
                String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
                model.addAttribute("guaWayNames", guaWayNames);
            }
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/viewGuaContractInfoApprove";
    }

    /**
     * 被担保合同查询列表
     *
     * @param model
     * @return
     */
    @GetMapping("/searchlist")
    @LogMessage(content = "跳转到担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String searchlist(Model model) {
        String relateCon = guaContractInfoService.relateCon();
        model.addAttribute("relateCon", relateCon);
        return "cms/guarantee/listSearchGuaContract";
    }

    /**
     * 被担保合同查询列表内容
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/searchdatatable")
    @ResponseBody
    public DataTablesOutput searchGuaContractTables(@RequestBody DataTablesInput dataTablesInput) {
        return guaContractInfoService.searchGuaContractTables(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 占用释放详情
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/queryEmployDtl/{id}")
    public String queryGuaContractHistory(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            List<GuaConrrantChangeTrackVO> trackList = guaContractChangeService.queryGuaContractHistory(id);
            model.addAttribute("trackList", trackList);
        }
        return "cms/guarantee/GuaContractEmployDtl";
    }

    /**
     * 跳转到增加担保合同页面
     *
     * @return
     */
    @LogMessage(content = "跳转到增加担保合同页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addGuaContract")
    public String addGuaContract(ParamsVO vo, Model model) {
        vo.setStartDate(null);
        vo.setEndDate(null);
        // 担保的挂接没有币种要求，可不同币种的挂接
        vo.setCurId(null);
        model.addAttribute("paramsVO", vo);
        if (vo.getContractIds() != null || vo.getContractIds() != "") {
            List<GuaContractInfoVO> list = guaContractInfoService.getContractList(vo);
            model.addAttribute("contractlist", list);
        }
        return "cms/guarantee/addGuaContractPage";
    }

    /**
     * 审批通过的被担保合同列表
     *
     * @param model
     * @return
     */
    @GetMapping("/guaContractlist")
    @LogMessage(content = "跳转到选择担保合同页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String guaContractlist(ParamsVO vo, Model model) {
        model.addAttribute("paramsVO", vo);
        return "cms/guarantee/selectGuaContractPage";
    }

    /**
     * 审批通过的被担保合同列表数据
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @PostMapping("/approvedDatatable")
    @ResponseBody
    public DataTablesOutput<GuaContractInfoVO> approvedDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return guaContractInfoService.getApprovedDataTables(dataTablesInput, null, this.getCurrentUser());
    }

    /**
     * 根据id查询担保合同
     */
    @GetMapping("getContractList")
    public String getContractList(ParamsVO vo, Model model) {
        List<GuaContractInfoVO> list = guaContractInfoService.getContractList(vo);
        model.addAttribute("contractlist", list);
        return "cms/guarantee/viewGuaContractDetail";
    }

    /**
     * 查看手机审批详情
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            GuaContractInfoVO vo = new GuaContractInfoVO();
            vo = guaContractInfoService.getGuaContractInfoById(businessId);
            if (vo != null) {
                model.addAttribute("vo", vo);
            }
            // 设置担保方式和担保金额
            List<GuaContractWayVO> guaContractWayVOS = vo.getGuaDtls();// 合同关联担保方式信息
            if (guaContractWayVOS != null && guaContractWayVOS.size() > 0) {
                String guaWayNames = "";
                for (GuaContractWayVO guaWay : guaContractWayVOS) {
                    String mulLan = ii18nService.getBusinessBaseContext(guaWay.getId());
                    if (StringUtils.isNotEmpty(mulLan)) {
                        guaWayNames += mulLan + ",";
                    } else {
                        guaWayNames += guaWay.getGuaWayName() + ",";
                    }
                }
                model.addAttribute("guaWayNames", guaWayNames);
                model.addAttribute("guaWayVOList", guaContractWayVOS);
            }
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/mobileGuaContractInfo";
    }

    /***
     * 担保方式明细处理
     *
     * @param contractVO 担保合同信息VO
     */
    private void handelData(GuaContractInfoVO contractVO) {
        // 获取担保方式明细
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        if (EmptyUtils.isEmptyList(guaDtls)) {
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUACONTRACT_GUAWAY_NULL);
        }
        // 过滤无效数据（前端录入时，下标可能不连续）
        guaDtls = guaDtls.stream().filter(guaWay -> StringUtils.isNotEmpty(guaWay.getWayCodes())).collect(Collectors.toList());
        contractVO.setGuaDtls(guaDtls);
    }

    @RequestMapping("/appReportlist")
    @LogMessage(content = "跳转到担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String guaContractAppReport(Model model) {
        String relateCon = guaContractInfoService.relateCon();
        model.addAttribute("relateCon", relateCon);
        List<BtCurrencyDTO> curList = currencyService.getBtCurrency();
        model.addAttribute("curList", curList);
        List<GuaWayDTO> guaWayList = iGuaWayServie.getAllGuaWay();
        model.addAttribute("guaWayList", guaWayList);
        return "cms/guarantee/guaContractAppReport";
    }

    @PostMapping("/appReportData")
    @ResponseBody
    public Object appReportData(String corpIds, String guaWayCode, String guaNo, String contractCode, String startDate,
                                String endDate) {
        DataTablesInput input = new DataTablesInput();
        HashMap<String, Object> params = new HashMap<>();
        params.put("ins_corpId", corpIds);
        params.put("ins_guaType", guaWayCode);
        params.put("likes_contractCode", contractCode);
        params.put("likes_guaNo", guaNo);
        if (EmptyUtils.isNotEmpty(startDate)) {
            params.put("ged_startDate", DateUtils.formatDate(startDate));
        }
        if (EmptyUtils.isNotEmpty(endDate)) {
            params.put("led_endDate", DateUtils.formatDate(endDate));
        }
        input.setFormdata(params);
        input.setLength(1000);
        DataTablesOutput output = guaContractInfoService.searchGuaContractTables(input, this.getCurrentUser());
        List<GuaContractInfoVO> contractInfoVOList = output.getData();
        return contractInfoVOList;
    }

    @RequestMapping("/mobileGuaTypeTrack")
    public String mobileGuaTypeTrack(String contractId, Model model) {
        List<GuaConrrantChangeTrackVO> trackList = guaContractChangeService.queryGuaContractHistory(contractId);
        model.addAttribute("trackList", trackList);
        return "cms/guarantee/mobileGuaTypeTrack";
    }

    /**
     * 添加贷款信息表
     *
     * @param
     * @return
     */
    @LogMessage(content = "添加贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<GuaContractExcelVO> guaContractExcelVOS) {
        for (GuaContractExcelVO vo : guaContractExcelVOS) {
            guaContractInfoService.saveImport(vo, this.getCurrentUser());
        }
        return success();
    }
}
