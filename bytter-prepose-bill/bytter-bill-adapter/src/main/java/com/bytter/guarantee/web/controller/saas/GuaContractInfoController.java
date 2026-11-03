package com.bytter.guarantee.web.controller.saas;

import com.bytter.bill.domain.bill.vo.v1.EditContractInfoVO;
import com.bytter.bill.domain.bill.vo.v1.ToOccupyPageVO;
import com.bytter.bill.domain.bill.vo.v1.ViewGuaContractInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @version 1.0
 * @Title:GuaContractInfoController
 * @Description:被担保合同管理控制层
 * @date 2018年12月12日 上午9:45:46
 */
@Api(tags = "被担保合同管理",value = "/v1-guarantee/cms/guarantee/guaContractInfo")
@RestController("v1GuaContractInfoController")
@RequestMapping("/v1-guarantee/cms/guarantee/guaContractInfo")
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
     * 列表内容
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<GuaContractInfoVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(guaContractInfoService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 保存担保合同信息
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson savePledgeInfo(@RequestBody GuaContractInfoVO contractVO) {
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
    public AjaxJson<EditContractInfoVO> editContractInfo(@PathVariable("id") String id) {
        EditContractInfoVO model = new EditContractInfoVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoVO vo = guaContractInfoService.getGuaContractInfoById(id);
            if (vo != null) {
                guaContractInfoService.getGuaPerson(vo);
                model.setVo(vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                model.setGuaWays(guaWays);
            }
            // 对接合同管理
            String relateCon = guaContractInfoService.relateCon();
            if (GuaranteeConstant.RELATE_CON_YES.equals(relateCon)) {// 担保对接合同管理
                String conType = guaContractInfoService.conType();
                model.setConType(conType);
                model.setConId(vo.getConId());
                model.setConTypeId(vo.getConTypeId());
            }
            model.setRelateCon(relateCon);
        }
        return AjaxJson.success(model);
    }

    /**
     * 查询担保方式
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public AjaxJson<List<GuaWayVO>> getListGuaWay() {
        List<GuaWayVO> list = guaContractInfoService.getListGuaWay();
        return AjaxJson.success(list);
    }

    /**
     * 带参数传递
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/toPage")
    public AjaxJson<ToOccupyPageVO> toOccupyPage(ParamsVO vo) {
        ToOccupyPageVO model = new ToOccupyPageVO();
        model.setVo(vo);
        List<GuaPledgeInfoVO> list = guaContractInfoService.convertPledgeInfoList(vo.getPledgeMsg());
        model.setList(list);
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        model.setConfig(configDTO);

        return AjaxJson.success(model);
    }

    /**
     * 带参数传递
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/toListPageInfo")
    public AjaxJson<ToOccupyPageVO> toOccupyPageInfo(ParamsVO vo) {
        ToOccupyPageVO model = new ToOccupyPageVO();
        model.setVo(vo);
        // 获取是否允许多次抵押
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        model.setConfig(configDTO);
        return AjaxJson.success(model);
    }

    /**
     * 相应ajax的get请求
     */
    @GetMapping("getInfo")
    @ResponseBody
    public AjaxJson<List<GuaPledgeInfoVO>> getListGuPledgeInfo(ParamsVO vo) {
        List<String> pledgeIds = new ArrayList<String>();
        if (vo.getPledgeIds() != null && !(vo.getPledgeIds().equals(""))) {
            String[] ids = vo.getPledgeIds().split(",");
            for (String id : ids) {
                pledgeIds.add(id);
            }
        }
        if (pledgeIds != null && pledgeIds.size() > 0) {
            List<GuaPledgeInfoVO> list = guaContractInfoService.getListGuaPledgeInfo(pledgeIds);
            return AjaxJson.success(list);
        } else {
            return AjaxJson.success();
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
    public AjaxJson<ViewGuaContractInfoVO> viewGuaContractInfo(@PathVariable("id") String id) {
        ViewGuaContractInfoVO model = new ViewGuaContractInfoVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoVO vo = new GuaContractInfoVO();
            vo = guaContractInfoService.getGuaContractInfoById(id);
            if (vo != null) {
                model.setVo(vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            model.setGuaWays(guaWays);
            String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
            model.setGuaWayNames(guaWayNames);
        }
        return AjaxJson.success(model);
    }


    /**
     * 审批列表
     */
    @PostMapping("/approveDataList")
    @ResponseBody
    public AjaxJson<DataTablesOutput> approveDataList(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(guaContractInfoService.getApproveDataTables(input));
    }

    /**
     * 查看审批详情
     */
    @GetMapping("/viewApprove")
    public AjaxJson<ViewGuaContractInfoVO> viewApprove(String id, String taskId) {
        ViewGuaContractInfoVO model = new ViewGuaContractInfoVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoVO vo = guaContractInfoService.getGuaContractInfoById(id);
            if (vo != null) {
                model.setVo(vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                model.setGuaWays(guaWays);
                String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
                model.setGuaWayNames(guaWayNames);
            }
        }
        model.setTaskId(taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }

    /**
     * 被担保合同查询列表
     *
     * @return
     */
    @GetMapping("/searchlist")
    @LogMessage(content = "跳转到担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<Map<String, Object>> searchlist() {
        Map<String, Object> model = new HashMap<>();
        String relateCon = guaContractInfoService.relateCon();
        model.put("relateCon", relateCon);
        return AjaxJson.success(model);
    }

    /**
     * 被担保合同查询列表内容
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/searchdatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> searchGuaContractTables(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(guaContractInfoService.searchGuaContractTables(input, this.getCurrentUser()));
    }

    /**
     * 占用释放详情
     *
     * @param id
     * @return
     */
    @GetMapping("/queryEmployDtl/{id}")
    public AjaxJson<List<GuaConrrantChangeTrackVO>> queryGuaContractHistory(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            List<GuaConrrantChangeTrackVO> trackList = guaContractChangeService.queryGuaContractHistory(id);
            return AjaxJson.success(trackList);
        }
        return AjaxJson.success();
    }

    /**
     * 跳转到增加担保合同页面
     *
     * @return
     */
    @LogMessage(content = "跳转到增加担保合同页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addGuaContract")
    public AjaxJson<List<GuaContractInfoVO>> addGuaContract(ParamsVO vo, Model model) {
        vo.setStartDate(null);
        vo.setEndDate(null);
        // 担保的挂接没有币种要求，可不同币种的挂接
        vo.setCurId(null);
        model.addAttribute("paramsVO", vo);
        List<GuaContractInfoVO> list =new ArrayList<>();
        if (vo.getContractIds() != null || vo.getContractIds() != "") {
            list.addAll(guaContractInfoService.getContractList(vo));
        }
        return AjaxJson.success(list);
    }


    /**
     * 审批通过的被担保合同列表数据
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @PostMapping("/approvedDatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<GuaContractInfoVO>> approvedDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(guaContractInfoService.getApprovedDataTables(input, null, this.getCurrentUser()));
    }

    /**
     * 根据id查询担保合同
     */
    @GetMapping("getContractList")
    public AjaxJson< List<GuaContractInfoVO> > getContractList(ParamsVO vo) {
        List<GuaContractInfoVO> list = guaContractInfoService.getContractList(vo);
        return AjaxJson.success(list);
    }

    /**
     * 查看手机审批详情
     */
    @GetMapping("/mobileApprove")
    public AjaxJson<Map<String ,Object>> mobileApprove(String businessId, String taskId) {
        Map<String ,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            GuaContractInfoVO vo = new GuaContractInfoVO();
            vo = guaContractInfoService.getGuaContractInfoById(businessId);
            if (vo != null) {
                model.put("vo", vo);
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
                model.put("guaWayNames", guaWayNames);
                model.put("guaWayVOList", guaContractWayVOS);
            }
        }
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
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
    public AjaxJson< List<GuaContractInfoVO>> appReportData(String corpIds, String guaWayCode, String guaNo, String contractCode, String startDate,
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
        return AjaxJson.success(contractInfoVOList);
    }

    @RequestMapping("/mobileGuaTypeTrack")
    public AjaxJson< List<GuaConrrantChangeTrackVO> > mobileGuaTypeTrack(String contractId) {
        List<GuaConrrantChangeTrackVO> trackList = guaContractChangeService.queryGuaContractHistory(contractId);
        return AjaxJson.success(trackList);
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
