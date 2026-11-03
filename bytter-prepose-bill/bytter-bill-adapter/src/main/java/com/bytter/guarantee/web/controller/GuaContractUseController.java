package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaContractUseService;
import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import com.bytter.guarantee.domain.vo.GuaContractUseDtlVO;
import com.bytter.guarantee.domain.vo.GuaContractUseVO;
import com.bytter.guarantee.domain.vo.ParamsVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaContractUseController
 * @ProjectName T3(3.2)
 * @Description: 被担保合同使用登记控制层
 * @date 2020/4/29 9:58
 */
@Controller
@RequestMapping("/cms/guarantee/guaContractUse")
public class GuaContractUseController extends BaseController {
    @Autowired
    private GuaContractUseService guaContractUseService;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private IGuaWayServie iGuaWayServie;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @GetMapping("/list")
    @LogMessage(content = "跳转到被担保合同使用登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String list(Model model) {
        return "cms/guarantee/listGuaContractUse";
    }
    @PostMapping("/datatables")
    @ResponseBody
    public DataTablesOutput datatables(@RequestBody DataTablesInput input) {
        return guaContractUseService.dataTables(input, getCurrentUser());
    }
    @GetMapping("/useList/{contractId}")
    @LogMessage(content = "跳转到担保合同使用登记列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String useList(@PathVariable("contractId") String contractId, Model model) {
        List<GuaContractUseDtlVO> useList = guaContractUseService.getUseList(contractId);
        model.addAttribute("useList", useList);
        return "cms/guarantee/listGuaContractUseDetail";
    }
    @GetMapping("/addUse")
    @LogMessage(content = "跳转到被担保合同使用登记页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String addUse(String contractId, Model model) {
        if (StringUtils.isNotEmpty(contractId)) {
            GuaContractInfoVO vo = new GuaContractInfoVO();
            vo = guaContractInfoService.getGuaContractInfoById(contractId);
            if (vo != null) {
                model.addAttribute("vo", vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(contractId);
            model.addAttribute("guaWays", guaWays);
            String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
            model.addAttribute("guaWayNames", guaWayNames);
            BtCurrencyDTO currencyDTO = iBtCurrencyService.getRMBCurrency();
            model.addAttribute("currencyDTO", currencyDTO);
        }
        return "cms/guarantee/addGuaContractUse";
    }
    @GetMapping("/view/{id}")
    public String viewUseMsg(@PathVariable("id") String id, Model model) {
        if (EmptyUtils.isNotEmpty(id)) {
            GuaContractUseDtlVO contractOccupVO = guaContractUseService.queryUseDtlById(id);
            model.addAttribute("contractOccupVO", contractOccupVO);
            GuaContractInfoVO vo = guaContractInfoService.getGuaContractInfoById(contractOccupVO.getContractId());
            if (vo != null) {
                model.addAttribute("vo", vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(contractOccupVO.getContractId());
            model.addAttribute("guaWays", guaWays);
            String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
            model.addAttribute("guaWayNames", guaWayNames);
        }
        return "cms/guarantee/viewGuaContractUse";
    }
    @GetMapping("/selectBusiness")
    @LogMessage(content = "跳转到选择业务数据页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String selectBusiness(ParamsVO paramsVO, Model model) {
        model.addAttribute("sysCode", paramsVO.getSysCode());
        model.addAttribute("curId", paramsVO.getCurId());
        String corpId = paramsVO.getCorpId();
        if (EmptyUtils.isNotEmpty(corpId)) {
            model.addAttribute("corpId", corpId);
            CorpDTO corpDTO = corpService.get(corpId);
            model.addAttribute("corpName", corpDTO.getName());
        }
        // 查询合同已经被业务使用的数据
        List<GuaContractUseDtlVO> useDatas = guaContractUseService.queryUseDtlByConId(paramsVO.getContractIds(),
                paramsVO.getSysCode());
        String ids = "";
        for (GuaContractUseDtlVO useDtlVO : useDatas) {
            ids += useDtlVO.getBusinessId() + ",";
        }
        String businessIds = paramsVO.getBusinessIds();
        if (EmptyUtils.isNotEmpty(businessIds)) {
            ids += businessIds.trim();
        }
        model.addAttribute("businessIds", ids);
        model.addAttribute("startDate", paramsVO.getStartDate());
        model.addAttribute("endDate", paramsVO.getEndDate());
        return "cms/guarantee/selectBusinessPage";
    }
    @PostMapping("/save")
    @ResponseBody
    @LogMessage(content = "保存担保合同使用登记数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson save(GuaContractUseVO guaContractUseVO) {
        guaContractUseService.save(guaContractUseVO, getCurrentUser());
        return success();
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    @LogMessage(content = "删除担保合同使用登记数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson delete(@PathVariable("id") String id) {
        guaContractUseService.delete(id);
        return success();
    }
    @PostMapping("/submit/{ids}")
    @ResponseBody
    @LogMessage(content = "提交担保合同使用登记数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson submit(@PathVariable("ids") String ids) {
        guaContractUseService.submit(ids, getCurrentUser());
        return success();
    }
}
