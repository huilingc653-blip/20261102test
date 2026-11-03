package com.bytter.guarantee.web.controller.saas;

import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaContractUseService;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "被担保合同使用登记控制层",value = "/v1-guarantee/cms/guarantee/guaContractUse")
@RestController("v1GuaContractUseController")
@RequestMapping("/v1-guarantee/cms/guarantee/guaContractUse")
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
    @ApiOperation(value = "担保合同使用登记列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return success(guaContractUseService.dataTables(dataTablesInput, getCurrentUser()));
    }
    @ApiOperation(value = "被担保合同使用登记列表")
    @GetMapping("/useList/{contractId}")
    @LogMessage(content = "跳转到担保合同使用登记列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<List<GuaContractUseDtlVO>> useList(@PathVariable("contractId") String contractId) {
        List<GuaContractUseDtlVO> useList = guaContractUseService.getUseList(contractId);
        return success(useList);
    }
    @ApiOperation(value = "跳转到被担保合同使用登记页面")
    @GetMapping("/addUse")
    @LogMessage(content = "跳转到被担保合同使用登记页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<GuaContractUsePageVO> addUse(String contractId) {
        GuaContractUsePageVO pageVO = new GuaContractUsePageVO();
        if (StringUtils.isNotEmpty(contractId)) {
            GuaContractInfoVO vo = new GuaContractInfoVO();
            vo = guaContractInfoService.getGuaContractInfoById(contractId);
            if (vo != null) {
                pageVO.setVo(vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(contractId);
            pageVO.setGuaWays(guaWays);
            String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
            pageVO.setGuaWayNames(guaWayNames);
            BtCurrencyDTO currencyDTO = iBtCurrencyService.getRMBCurrency();
            pageVO.setCurrencyDTO(currencyDTO);
        }
        return success(pageVO);
    }
    @ApiOperation(value = "被担保合同使用登记查看数据")
    @GetMapping("/view/{id}")
    public AjaxJson<GuaContractUsePageViewVO> viewUseMsg(@PathVariable("id") String id, Model model) {
        GuaContractUsePageViewVO pageViewVO = new GuaContractUsePageViewVO();
        if (EmptyUtils.isNotEmpty(id)) {
            GuaContractUseDtlVO contractOccupVO = guaContractUseService.queryUseDtlById(id);
            pageViewVO.setContractOccupVO(contractOccupVO);
            GuaContractInfoVO vo = guaContractInfoService.getGuaContractInfoById(contractOccupVO.getContractId());
            if (vo != null) {
                pageViewVO.setVo(vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(contractOccupVO.getContractId());
            pageViewVO.setGuaWays(guaWays);
            String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
            pageViewVO.setGuaWayNames(guaWayNames);
        }
        return success(pageViewVO);
    }
    @ApiOperation(value = "选择业务数据获取")
    @GetMapping("/selectBusiness")
    @LogMessage(content = "跳转到选择业务数据页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<GuaContractSelectPageVO> selectBusiness(ParamsVO paramsVO) {
        GuaContractSelectPageVO pageVO = new GuaContractSelectPageVO();
        pageVO.setSysCode(paramsVO.getSysCode());
        pageVO.setCurId(paramsVO.getCurId());
        String corpId = paramsVO.getCorpId();
        if (EmptyUtils.isNotEmpty(corpId)) {
            pageVO.setCorpId(corpId);
            CorpDTO corpDTO = corpService.get(corpId);
            pageVO.setCorpName(corpDTO.getName());
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
        pageVO.setBusinessIds(ids);
        pageVO.setStartDate(paramsVO.getStartDate());
        pageVO.setEndDate(paramsVO.getEndDate());
        return success(pageVO);
    }
    @PostMapping("/save")
    @ResponseBody
    @LogMessage(content = "保存担保合同使用登记数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson save(@RequestBody  GuaContractUseVO guaContractUseVO) {
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
    @ApiOperation(value = "提交担保合同使用登记数据")
    @PostMapping("/submit")
    @ResponseBody
    @LogMessage(content = "提交担保合同使用登记数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson submit(@RequestBody BisIDsDTO bisIDsDTO) {
        String ids = bisIDsDTO.getIds();
        guaContractUseService.submit(ids, getCurrentUser());
        return success();
    }
}
