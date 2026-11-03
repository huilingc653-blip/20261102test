package com.bytter.finance.web.equity.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import com.bytter.finance.domain.equity.entity.CmsEquityInfoDetail;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityTypeService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoVO;
import com.bytter.finance.domain.equity.vo.CmsEquityTypeVo;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 股权融资登记
 */
@Controller
@RequestMapping("/cms/equity/info")
public class CmsEquityInfoController extends BaseController {
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private CmsEquityTypeService cmsEquityTypeService;
    /**
     * 查询股权融资列表-页面跳转
     */
    @LogMessage(content = "查询股权融资列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/equity/listEquityInfo";
    }
    /**
     * 查询股权融资列表-页面跳转
     */
    @LogMessage(content = "查询股权融资列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        return "cms/equity/listEquityQuery";
    }
    /**
     * 股权融资列表 - 数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityInfoService.getDataEquityTable(dataTablesInput, getCurrentUser());
    }
    /**
     * 股权融资列表 - 数据
     */
    @PostMapping("/queryDatatable")
    @ResponseBody
    public DataTablesOutput queryDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityInfoService.getDataEquityQueryTable(dataTablesInput, getCurrentUser());
    }
    /**
     * 股权融资明细列表 - 数据
     */
    @PostMapping("/detailDatatable")
    @ResponseBody
    public List<CmsEquityInfoDetailVO> detailDatatable(String ids) {
        String[] allId = ids.split(",");
        List<String> arrayList = new ArrayList<>();
        for (String id : allId) {
            arrayList.add(id);
        }
        List<CmsEquityInfoDetailVO> list = cmsEquityInfoService.getDataEquityDetailTable(arrayList);
        return list;
    }
    /**
     * 股权融资新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "cms/equity/addEquityInfo";
    }
    /**
     * 股权融资明细新增页面跳转
     *
     * @return
     */
    @GetMapping("/addDetail")
    public String addDetail(Model model) {
        String equityCode = cmsEquityInfoService.getEquityCode();
        model.addAttribute("equityCode", equityCode);
        return "cms/equity/addEquityDetailInfo";
    }
    /**
     * 股权融资明细编辑页面跳转
     *
     * @return
     */
    @GetMapping("/updateDetail/{equityCode}")
    public String updateDetail(@PathVariable("equityCode") String equityCode, Model model) {
        CmsEquityInfoDetailVO detail = cmsEquityInfoService.getDetail(equityCode);
        model.addAttribute("detail", detail);
        return "cms/equity/updateEquityDetailInfo";
    }
    /**
     * 删除股权融资明细
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/deleteDetail/{id}")
    public AjaxJson deleteDetail(@PathVariable("id") String id) {
        cmsEquityInfoService.deleteDetail(id);
        return success();
    }
    /**
     * 提交
     */
    @LogMessage(content = "股权融资表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(CmsEquityInfoVO vo, String saveType, String ids) {
        cmsEquityInfoService.tempOrSubmit(vo, saveType, this.getCurrentUser(), ids);
        return success();
    }
    /**
     * 修改-页面跳转
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        String ids = cmsEquityInfoService.getDetailIds(id);
        CmsEquityInfo cmsEquityInfo = cmsEquityInfoService.get(id);
        model.addAttribute("ids", ids);
        model.addAttribute("cmsEquityInfo", cmsEquityInfo);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = cmsEquityInfoService.listIntermediateCostByDfId(cmsEquityInfo.getId());
        model.addAttribute("costVOS", costVOS);
        return "cms/equity/updateEquityInfo";
    }
    /**
     * 查看-页面跳转
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        String ids = cmsEquityInfoService.getDetailIds(id);
        model.addAttribute("ids", ids);
        CmsEquityInfoVO cmsEquityInfoVO = cmsEquityInfoService.getById(id);
        CmsEquityTypeVo typeVo = cmsEquityTypeService.getTypeByCode(cmsEquityInfoVO.getEquityTypeCode());
        if (typeVo != null) {
            cmsEquityInfoVO.setEquityTypeName(typeVo.getTypeName());
        }
        model.addAttribute("cmsEquityInfo", cmsEquityInfoVO);
        List<CmsEquityInfoDetailVO> detailVOS = cmsEquityInfoService.getDetails(id, null);
        model.addAttribute("detailVOS", detailVOS);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = cmsEquityInfoService.listIntermediateCostByDfId(id);
        model.addAttribute("costVOS", costVOS);
        return "cms/equity/viewEquityInfo";
    }
    /**
     * 保存明细
     */
    @LogMessage(content = "股权融资明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveDetail")
    @ResponseBody
    public AjaxJson saveDetail(CmsEquityInfoDetail detail) {
        AjaxJson ajaxJson = cmsEquityInfoService.saveDetail(detail);
        ajaxJson.setMsg("操作成功");
        return ajaxJson;
    }
    @LogMessage(content = "删除股权融资信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsEquityInfoService.deleteLease(id, this.getCurrentUser());
        return success();
    }
    /**
     * 查询帐号根据单位,币种
     */
    @RequestMapping("/queryPaymentAccListByCoprId")
    @ResponseBody
    public List<BtBankAccDTO> queryPaymentAccListByCoprId(String corpId) {
        if (EmptyUtils.isEmpty(corpId)) {
            return new ArrayList<BtBankAccDTO>();
        }
        BtBankAccDTO dto = new BtBankAccDTO();
        dto.setCorpId(corpId);
        dto.setValidSign(BtBankAccConstant.VALIDSIGN_1);
        return btBankAccService.getBankAccListAll(dto);
    }
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CmsEquityInfoDetailVO> list) {
        return cmsEquityInfoService.saveImport(list);
    }
    /******************** 审批 **********************/
    /**
     * 股权登记审批列表-页面跳转
     */
    @LogMessage(content = "查询股权登记审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listInfoApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/equity/approve/listEquityApprove";
    }
    /**
     * 股权登记审批列表 - 数据
     */
    @PostMapping("/datatableApprove")
    @ResponseBody
    public DataTablesOutput datatableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsEquityInfoService.getDatatableApprove(dataTablesInput);
    }
    /**
     * 申请审批 详情页面
     */
    @GetMapping("/infoApprove")
    public String infoApprove(String id, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(id)) {
            CmsEquityInfo info = cmsEquityInfoService.get(id);
            model.addAttribute("equityInfo", info);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/equity/approve/viewEquityApprove";
    }
}
