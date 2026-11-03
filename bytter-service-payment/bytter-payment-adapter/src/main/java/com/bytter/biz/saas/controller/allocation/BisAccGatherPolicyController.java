package com.bytter.biz.saas.controller.allocation;

import com.bytter.biz.allocation.vo.BisGatherPolicyQueryVO;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.inneraccount.service.BisAccGatherPolicyService;
import com.bytter.biz.inneraccount.service.BisGatherPolicyService;
import com.bytter.biz.inneraccount.vo.BisAccGatherPolicyTreeVO;
import com.bytter.biz.inneraccount.vo.v1.BisGatherPolicyListVO;
import com.bytter.biz.inneraccount.vo.v1.ListBankAccVO;
import com.bytter.biz.payment.api.dto.BisAccGatherPolicyRelationDeleteDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.vo.BtBankAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号关系策略维护表
 * @date 2017-06-20 16:44:38
 */
@Api(value = "/v1-payment/allocation/bisaccgatherpolicy", tags = "银行账号关系策略维护表")
@RestController("v1BisAccGatherPolicyController")
@RequestMapping("/v1-payment/allocation/bisaccgatherpolicy")
public class BisAccGatherPolicyController extends BaseController {
    @Autowired
    private BisAccGatherPolicyService bisAccGatherPolicyService;
    @Autowired
    private BisGatherPolicyService bisGatherPolicyService;

    /**
     * 银行账号关系策略维护表列表 页面跳转
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("查询银行账号关系策略维护表列表")
    @LogMessage(content = "查询银行账号关系策略维护表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<List<BisAccGatherPolicyTreeVO>> list(BisGatherPolicy bisGatherPolicy) throws Exception {
        return AjaxJson.success(bisAccGatherPolicyService.listTreeData(bisGatherPolicy));
    }

    /**
     * 银行账号关系策略维护表列表 查询
     *
     * @return
     * @throws Exception
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_QUERY)
    @ApiOperation("银行账号关系策略维护表列表查询")
    @LogMessage(content = "查询银行账号关系策略维护表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryList")
    public AjaxJson<BisGatherPolicyListVO> queryList(BisGatherPolicyQueryVO queryVO) throws Exception {
        BisGatherPolicy bisGatherPolicy = new BisGatherPolicy();
        bisGatherPolicy.setGatherType(queryVO.getGatherType());
        bisGatherPolicy.setGatherPolicyCode(queryVO.getGatherPolicyCode());
        BisGatherPolicyListVO vo = BisGatherPolicyListVO.builder().treeDate(bisAccGatherPolicyService.listTreeData(bisGatherPolicy,queryVO.getParentAccName()))
                .bisGatherPolicyPage(bisGatherPolicy).parentAccName(queryVO.getParentAccName()).build();

        return AjaxJson.success(vo);
    }

    /**
     * 父账号查询
     *
     * @return
     */

    @ApiOperation("查询银行账号")
    @LogMessage(content = "查询银行账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listParentBankAcc/{gatherPolicyId}/{curId}")
    public AjaxJson<ListBankAccVO> listParentBankAcc(@PathVariable("gatherPolicyId") String gatherPolicyId, @PathVariable("curId") String curId) {
        return AjaxJson.success(ListBankAccVO.builder().gatherPolicyId(gatherPolicyId).prentAccId("").parentSign("1").curId(curId).gatherType(bisGatherPolicyService.get(gatherPolicyId, true).getGatherType()).build());

    }

    /**
     * 子账号查询
     *
     * @return
     */

    @ApiOperation("查询银行账号")
    @LogMessage(content = "查询银行账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listBankAcc/{gatherPolicyId}/{prentAccId}/{curId}")
    public AjaxJson<ListBankAccVO> listBankAcc(@PathVariable("gatherPolicyId") String gatherPolicyId, @PathVariable("prentAccId") String prentAccId, @PathVariable("curId") String curId) {
        return AjaxJson.success(ListBankAccVO.builder().gatherPolicyId(gatherPolicyId).prentAccId(prentAccId).parentSign("").curId(curId).gatherType(bisGatherPolicyService.get(gatherPolicyId, true).getGatherType()).build());
    }

    /**
     * 银行账号查询列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("银行账号查询列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BtBankAccVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisAccGatherPolicyService.getDataTablesUnion(input));
    }

    /**
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("childDatatable")
    @PostMapping("/childDatatable")
    public AjaxJson<DataTablesOutput<BtBankAccVO>> childDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisAccGatherPolicyService.getDataTablesUnionChild(input));
    }

    /**
     * 添加银行账号关系策略维护表
     *
     * @param ids
     * @param gatherPolicyId
     * @param prentAccId
     * @param curId
     * @return
     * @throws Exception
     */

    @ApiOperation("添加银行账号关系策略维护表")
    @LogMessage(content = "添加银行账号关系策略维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestParam("ids") String ids, @RequestParam("gatherPolicyId") String gatherPolicyId,
                         @RequestParam("prentAccId") String prentAccId, @RequestParam("curId") String curId) throws Exception {
        if (!StringUtils.isEmpty(prentAccId)) {
            //增加子账号
            bisAccGatherPolicyService.saveChildAcc(ids, gatherPolicyId, prentAccId, curId, this.getCurrentUser());
        } else {
            //增加父账号
            bisAccGatherPolicyService.savePrentAcc(ids, gatherPolicyId, curId, this.getCurrentUser());
        }
        return success();
    }


    @ApiOperation("添加银行账号关系策略维护表")
    @LogMessage(content = "添加银行账号关系策略维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveChild")
    public AjaxJson saveChild(@RequestParam("ids") String ids, @RequestParam("gatherPolicyId") String gatherPolicyId,
                              @RequestParam("curId") String curId, @RequestParam("childIds") String childIds) throws Exception {
        //增加父账号
        bisAccGatherPolicyService.savePrentAcc(ids, gatherPolicyId, curId, childIds, this.getCurrentUser());
        return success();
    }

    @LogMessage(content = "删除银行账号关系策略维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(@RequestBody BisAccGatherPolicyRelationDeleteDTO deleteDTO) {
        bisAccGatherPolicyService.deleteBisGatherPolicy(deleteDTO.getBankAccId(), deleteDTO.getPrentAccId(), deleteDTO.getGatherPolicyId());
        return success();
    }

}
