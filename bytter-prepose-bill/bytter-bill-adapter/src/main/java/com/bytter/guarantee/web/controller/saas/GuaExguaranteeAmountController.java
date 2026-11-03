package com.bytter.guarantee.web.controller.saas;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTableOutPutOrderUtils;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.domain.service.GuaExguaranteeAmountService;
import com.bytter.guarantee.domain.vo.GuaExguaranteeAmountPageVO;
import com.bytter.guarantee.domain.vo.GuaExguaranteeAmountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Api(tags = "外部定价限额表",value = "/v1-guarantee/cms/guarantee/guaExGuaranteeAmount")
@RestController("v1GuaExguaranteeAmountController")
@RequestMapping("/v1-guarantee/cms/guarantee/guaExGuaranteeAmount")
class GuaExguaranteeAmountController extends BaseController {
    @Autowired
    private GuaExguaranteeAmountService guaExguaranteeAmountService;

    /**
     * 用途维护表 table列表
     *
     * @return
     */
    @ApiOperation(value = "外部定价限额列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput dataTablesUnion = guaExguaranteeAmountService.getDataTablesUnion(input);
        DataTableOutPutOrderUtils.orderOutputList(dataTablesInput, dataTablesUnion.getData(),"assetsProportion");
        return success(dataTablesUnion);
    }
    /**
     * 添加
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "添加外部定价限额表")
    @LogMessage(content = "添加外部定价限额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody GuaExguaranteeAmountVO vo) {
        guaExguaranteeAmountService.saveBtPurpose(vo, this.getCurrentUser().getLoginName());
        return success();
    }
    /**
     * 编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<GuaExguaranteeAmountPageVO> update(String id) {
        GuaExguaranteeAmountVO exguaranteeAmountVO = guaExguaranteeAmountService.getByUnion(id);
        GuaExguaranteeAmountPageVO pageVO = new GuaExguaranteeAmountPageVO();
        pageVO.setAmountVO(exguaranteeAmountVO);
        return success(pageVO);
    }
    /**
     * 修改
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "修改外部定价限额表")
    @LogMessage(content = "修改外部定价限额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody GuaExguaranteeAmountVO vo) {
        guaExguaranteeAmountService.modifyBtPurpose(vo, this.getCurrentUser().getLoginName());
        return success();
    }
    /**
     * 查看详情页
     *
     * @return
     */
    @LogMessage(content = "查询详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<GuaExguaranteeAmountPageVO> view(@PathVariable("id") String id, Model model) {
        GuaExguaranteeAmountVO exguaranteeAmountVO = guaExguaranteeAmountService.getByUnion(id);
        GuaExguaranteeAmountPageVO pageVO = new GuaExguaranteeAmountPageVO();
        pageVO.setAmountVO(exguaranteeAmountVO);
        return success(pageVO);
    }
    @GetMapping("/corpId.json/{id}")
    @ResponseBody
    public AjaxJson<TreeGridResponse> getCorpIdByPurposeId(@PathVariable("id") String id) {
        return success(new TreeGridResponse<>(guaExguaranteeAmountService.getCorpIdByAmountId(id)));
    }
    /**
     * 删除用途维护表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除外部定价限额表")
    @LogMessage(content = "删除外部定价限额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        guaExguaranteeAmountService.delete(id);
        return success();
    }
}
