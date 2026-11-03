package com.bytter.finance.saas.controller.equity;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.equity.dto.CmsEquityInfoSubmitDTO;
import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import com.bytter.finance.domain.equity.entity.CmsEquityInfoDetail;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityTypeService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoApproveVO;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoEditVO;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoViewVO;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoVO;
import com.bytter.finance.domain.equity.vo.CmsEquityTypeVo;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 股权融资登记Controller
 *
 * @author zhukr
 */
@Api(value = "/v1-cms/cms/equity/info", tags = "股权融资登记")
@RestController("v1CmsEquityInfoController")
@RequestMapping("/v1-cms/cms/equity/info")
public class CmsEquityInfoController extends BaseController {
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private CmsEquityTypeService cmsEquityTypeService;

    /**
     * 查询股权融资列表 - 数据
     */
    @LogMessage(content = "查询股权融资列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询股权融资列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityInfoService.getDataEquityTable(input, getCurrentUser()));
    }

    /**
     * 查询股权融资查询列表 - 数据
     */
    @ApiOperation("查询股权融资查询列表")
    @PostMapping("/queryDatatable")
    public AjaxJson<DataTablesOutput> queryDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityInfoService.getDataEquityQueryTable(input, getCurrentUser()));
    }

    /**
     * 查询股权融资明细列表 - 数据
     *
     * @param ids 股权融资明细ids
     */
    @ApiOperation("查询股权融资明细列表")
    @GetMapping("/detailDatatable")
    public AjaxJson<List<CmsEquityInfoDetailVO>> detailDatatable(String ids) {
        String[] allId = ids.split(",");
        List<String> arrayList = new ArrayList<>();
        for (String id : allId) {
            arrayList.add(id);
        }
        return success(cmsEquityInfoService.getDataEquityDetailTable(arrayList));
    }

    /**
     * 股权融资明细新增页面数据
     */
    @ApiOperation("股权融资明细新增页面数据")
    @GetMapping("/addDetail")
    public AjaxJson<String> addDetail() {
        return success(cmsEquityInfoService.getEquityCode());
    }

    /**
     * 股权融资明细编辑页面数据
     *
     * @param equityCode 股权融资编号
     */
    @ApiOperation("股权融资明细编辑页面数据")
    @GetMapping("/updateDetail/{equityCode}")
    public AjaxJson<CmsEquityInfoDetailVO> updateDetail(@PathVariable("equityCode") String equityCode) {
        return success(cmsEquityInfoService.getDetail(equityCode));
    }

    /**
     * 删除股权融资明细
     *
     * @param id 股权融资明细id
     */
    @ApiOperation("删除股权融资明细")
    @PostMapping("/deleteDetail/{id}")
    public AjaxJson deleteDetail(@PathVariable("id") String id) {
        cmsEquityInfoService.deleteDetail(id);
        return success();
    }

    /**
     * 提交/暂存股权融资信息
     */
    @LogMessage(content = "股权融资表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("提交/暂存股权融资信息")
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(@RequestBody CmsEquityInfoSubmitDTO dto) {
        cmsEquityInfoService.tempOrSubmit(dto.getVo(), dto.getSaveType(), this.getCurrentUser(), dto.getIds());
        return success();
    }

    /**
     * 股权融资编辑页面数据
     *
     * @param id 股权融资主信息id
     */
    @ApiOperation("股权融资编辑页面数据")
    @GetMapping("/update/{id}")
    public AjaxJson<CmsEquityInfoEditVO> update(@PathVariable("id") String id) {
        CmsEquityInfoEditVO result = new CmsEquityInfoEditVO();
        result.setIds(cmsEquityInfoService.getDetailIds(id));
        CmsEquityInfo cmsEquityInfo = cmsEquityInfoService.get(id);
        result.setCmsEquityInfo(cmsEquityInfo);
        // 第三方费率信息
        result.setCostVOS(cmsEquityInfoService.listIntermediateCostByDfId(cmsEquityInfo.getId()));
        return success(result);
    }

    /**
     * 股权融资查看页面数据
     *
     * @param id 股权融资主信息id
     */
    @ApiOperation("股权融资查看页面数据")
    @GetMapping("/view/{id}")
    public AjaxJson<CmsEquityInfoViewVO> view(@PathVariable("id") String id) {
        CmsEquityInfoViewVO result = new CmsEquityInfoViewVO();
        result.setIds(cmsEquityInfoService.getDetailIds(id));
        CmsEquityInfoVO cmsEquityInfoVO = cmsEquityInfoService.getById(id);
        CmsEquityTypeVo typeVo = cmsEquityTypeService.getTypeByCode(cmsEquityInfoVO.getEquityTypeCode());
        if (typeVo != null) {
            cmsEquityInfoVO.setEquityTypeName(typeVo.getTypeName());
        }
        result.setCmsEquityInfo(cmsEquityInfoVO);
        result.setDetailVOS(cmsEquityInfoService.getDetails(id, null));
        // 第三方费率信息
        result.setCostVOS(cmsEquityInfoService.listIntermediateCostByDfId(id));
        return success(result);
    }

    /**
     * 保存股权融资明细
     *
     * @param detail 股权融资明细
     */
    @LogMessage(content = "股权融资明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("保存股权融资明细")
    @PostMapping("/saveDetail")
    public AjaxJson saveDetail(@RequestBody CmsEquityInfoDetail detail) {
        AjaxJson ajaxJson = cmsEquityInfoService.saveDetail(detail);
        ajaxJson.setMsg("操作成功");
        return ajaxJson;
    }

    /**
     * 删除股权融资信息
     *
     * @param id 股权融资主信息id
     */
    @LogMessage(content = "删除股权融资信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("删除股权融资信息")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsEquityInfoService.deleteLease(id, this.getCurrentUser());
        return success();
    }

    /**
     * 查询付款账号根据单位
     *
     * @param corpId 单位id
     */
    @ApiOperation("查询付款账号列表")
    @GetMapping("/queryPaymentAccListByCoprId")
    public AjaxJson<List<BtBankAccDTO>> queryPaymentAccListByCoprId(String corpId) {
        if (EmptyUtils.isEmpty(corpId)) {
            return success(new ArrayList<BtBankAccDTO>());
        }
        BtBankAccDTO dto = new BtBankAccDTO();
        dto.setCorpId(corpId);
        dto.setValidSign(BtBankAccConstant.VALIDSIGN_1);
        return success(btBankAccService.getBankAccListAll(dto));
    }

    /**
     * 导入保存股权融资明细
     *
     * @param list 股权融资明细列表
     */
    @ApiOperation("导入保存股权融资明细")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<CmsEquityInfoDetailVO> list) {
        return cmsEquityInfoService.saveImport(list);
    }

    /******************** 审批 **********************/

    /**
     * 查询股权登记审批列表 - 数据
     */
    @LogMessage(content = "查询股权登记审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询股权登记审批列表")
    @PostMapping("/datatableApprove")
    public AjaxJson<DataTablesOutput> datatableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityInfoService.getDatatableApprove(input));
    }

    /**
     * 申请审批详情数据
     *
     * @param id     股权融资主信息id
     * @param taskId 审批任务id
     */
    @ApiOperation("股权登记审批详情数据")
    @GetMapping("/infoApprove")
    public AjaxJson<CmsEquityInfoApproveVO> infoApprove(String id, String taskId) {
        CmsEquityInfoApproveVO result = new CmsEquityInfoApproveVO();
        if (!EmptyUtils.isEmpty(id)) {
            result.setEquityInfo(cmsEquityInfoService.get(id));
        }
        result.setTaskId(taskId);// 必须要做此步骤
        return success(result);
    }
}
