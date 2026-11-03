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
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.entity.GuaContractOccup;
import com.bytter.guarantee.domain.service.GuaContractChangeService;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaContractOccupService;
import com.bytter.guarantee.domain.vo.GuaConrrantChangeTrackVO;
import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import com.bytter.guarantee.domain.vo.ParamsVO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 被担保合同变更
 *
 * @author yangl
 */
@Controller
@RequestMapping("/cms/guarantee/guaContractChange")
public class GuaContractChangeController extends BaseController {
    @Autowired
    private GuaContractChangeService guaContractChangeService;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private GuaContractOccupService guaContractOccupService;
    /**
     * 跳转担保合同变更列表页面
     *
     * @return
     */
    @GetMapping("/list")
    public String list() {
        return "cms/guarantee/listGuaContractChange";
    }
    /**
     * 被担保合同使用列表
     *
     * @return
     */
    @GetMapping("/useChangelist")
    public String useChangelist() {
        return "cms/guarantee/guaContractOccupUseTab";
    }
    /**
     * 担保合同变更列表
     *
     * @param input 查询参数
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        Map<String, Object> map = input.getFormdata();
        map.put("eqs_bussinessType", GuaranteeConstant.CON_TYPE_0);// 担保合同类型为被担保合同
        return guaContractChangeService.queryContractChangeList(input, GuaranteeConstant.GUA_CONTRACT_INFO_QUERY,
                this.getCurrentUser());
    }
    /**
     * 变更页面跳转
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editContractInfo/{id}")
    public String editContractInfo(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoVO info = guaContractChangeService.queryContractInfoById(id);
            guaContractInfoService.getGuaPerson(info);
            model.addAttribute("info", info);
            String guaWays = guaContractChangeService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                model.addAttribute("guaWays", guaWays);
            }
        }
        return "cms/guarantee/updateGuaContractChangeInfo";
    }
    /**
     * 保存担保合同信息
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(GuaContractInfoVO contractVO) {
        this.handelData(contractVO);
        guaContractChangeService.tempOrSubmit(contractVO, this.getCurrentUser());
        return success();
    }
    /**
     * 历史详情变更页面跳转
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/queryGuaContractHistory/{id}")
    public String queryGuaContractHistory(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            List<GuaConrrantChangeTrackVO> trackList = guaContractChangeService.queryGuaContractHistory(id);
            model.addAttribute("trackList", trackList);
        }
        return "cms/guarantee/GuaContractHistoryInfo";
    }
    private void handelData(GuaContractInfoVO contractVO) {
        // 去掉无效数据
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        if (EmptyUtils.isEmptyList(guaDtls)) {
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUACONTRACT_GUAWAY_NULL);
        }
        List<GuaContractWayVO> wayList = new ArrayList<>();
        for (GuaContractWayVO guaWay : guaDtls) {
            if (guaWay.getMoney() == null) {
                continue;
            }
            if (null != guaWay.getWayCodes()) {
                wayList.add(guaWay);
            }
        }
        contractVO.setGuaDtls(wayList);
    }
    /**
     * 提交担保调整
     *
     * @param businessId:业务id contractComp:担保合同信息组合
     * @return
     */
    @LogMessage(content = "调整担保提交操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(String businessId, String contractIds, String billCode, String sysCode,
                           HttpServletRequest request) {
        if (contractIds != null) {
            if (contractIds.contains(",")) {
                for (String contractId : contractIds.split(",")) {
                    GuaContractOccup guaContractOccup = new GuaContractOccup();
                    guaContractOccup.setBusinessId(businessId);
                    guaContractOccup.setContractId(contractId);
                    guaContractOccup.setItemNo(billCode);
                    guaContractOccup.setSysCode(sysCode);
                    guaContractOccup.setUseDate(DateUtils.getDate());
                    guaContractOccup.setStatus(GuaranteeConstant.GUA_CONTRACT_USED);
                    guaContractOccupService.saveEntity(guaContractOccup);// 将挂接担保信息写入担保占用表GUA_CONTRACT_OCCUP
                }
            } else {
                GuaContractOccup guaContractOccup = new GuaContractOccup();
                guaContractOccup.setBusinessId(businessId);
                guaContractOccup.setContractId(contractIds);
                guaContractOccup.setItemNo(billCode);
                guaContractOccup.setSysCode(sysCode);
                guaContractOccup.setUseDate(DateUtils.getDate());
                guaContractOccup.setStatus(GuaranteeConstant.GUA_CONTRACT_USED);
                guaContractOccupService.saveEntity(guaContractOccup);// 将挂接担保信息写入担保占用表GUA_CONTRACT_OCCUP
            }
        }
        return success();
    }
    /**
     * 跳转到调整担保页面
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "跳转到调整担保页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/changeGuaContract")
    public String changeGuaContract(String id, String sysCode, String corpId, String curId, String billCode,
                                    Model model) {
        List<GuaContractOccup> guaContractList = guaContractOccupService.queryByBusinessId(id,
                GuaranteeConstant.GUA_CONTRACT_USED);
        String contractIds = "";
        for (GuaContractOccup guaContractOccup : guaContractList) {
            contractIds += guaContractOccup.getContractId() + ",";
        }
        ParamsVO vo = new ParamsVO();
        vo.setContractIds(contractIds);
        vo.setCorpId(corpId);
        vo.setCurId(curId);
        // vo.setStartDate();
        // vo.setEndDate();
        if (contractIds != null || contractIds != "") {
            List<GuaContractInfoVO> list = guaContractInfoService.getContractList(vo);
            model.addAttribute("contractlist", list);
        }
        model.addAttribute("paramsVO", vo);
        model.addAttribute("businessId", id);
        model.addAttribute("sysCode", sysCode);
        model.addAttribute("billCode", billCode);
        model.addAttribute("corpId", corpId);
        return "cms/guarantee/changeGuaContractPage";
    }
    /**
     * 释放担保合同操作
     *
     * @param contractId:担保合同id businessId:业务id
     */
    @LogMessage(content = "释放担保合同操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/release")
    @ResponseBody
    public AjaxJson release(String contractId, String businessId) {
        // 改变担保合同占用表的数据状态
        GuaContractOccup guaContractOccup = guaContractOccupService.queryByParams(businessId, contractId,
                GuaranteeConstant.GUA_CONTRACT_USED);
        Integer status = GuaranteeConstant.GUA_CONTRACT_RELEASE;// 释放状态
        guaContractOccupService.changeStatusById(guaContractOccup.getId(), status);
        return success();
    }
}
