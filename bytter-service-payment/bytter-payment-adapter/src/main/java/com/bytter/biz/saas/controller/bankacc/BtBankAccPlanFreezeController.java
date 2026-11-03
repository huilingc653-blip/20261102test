package com.bytter.biz.saas.controller.bankacc;

import cn.hutool.core.text.CharSequenceUtil;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.inneraccount.entity.BtBankAccPlanFreeze;
import com.bytter.biz.inneraccount.service.BtBankAccPlanFreezeService;
import com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccPlanFreezePageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 银行账户计划冻结申请
 *
 * @author fangcf
 * @date 2025-02-11
 */
@Api(value = "银行账户计划冻结申请",tags = "银行账户计划冻结申请")
@RestController("v1BtBankAccPlanFreezeController")
@RequestMapping("/v1-payment/bankacc/btbankaccplanfreeze")
public class BtBankAccPlanFreezeController extends BaseController {

    @Autowired
    private BtBankAccPlanFreezeService btBankAccPlanFreezeService;

    /**
     * 银行账户计划冻结列表
     *
     * @return 银行账户计划冻结数据
     */
    @ApiOperation("银行账户计划冻结列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btBankAccPlanFreezeService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    @ApiOperation(value = "新增计划冻结申请前的账号信息获取",notes = "替换原先的add跳转获取数据")
    @GetMapping("/getAccInfoBeforePlanFreeze")
    @ResponseBody
    public AjaxJson<BtBankAccPlanFreezePageVO> getAccInfoBeforeFreeze(String bankAccId, String curId) {
        BtBankAccPlanFreezeVO vo = new BtBankAccPlanFreezeVO();
        vo.setBankAccId(bankAccId);
        vo.setCurId(curId);
        BtBankAccPlanFreezePageVO pageVo = btBankAccPlanFreezeService.getAccInfoBeforePlanFreeze(vo);
        return success(pageVo);
    }

    @ApiOperation(value = "修改冻结账号信息获取",notes = "用来替代原先update跳转获取数据")
    @GetMapping("/getPlanFreezeById")
    @ResponseBody
    public AjaxJson<BtBankAccPlanFreezePageVO> getPlanFreezeById(String id) {
        BtBankAccPlanFreeze entity = btBankAccPlanFreezeService.get(id, true);
        BtBankAccPlanFreezeVO vo = new BtBankAccPlanFreezeVO();
        BeanUtils.copyNotNullProperties(entity, vo);
        BtBankAccPlanFreezePageVO pageVO = btBankAccPlanFreezeService.getAccInfoBeforePlanFreeze(vo);
        return success(pageVO);
    }

    /**
     * 计划冻结申请暂存
     *
     * @param vo 计划申请表vo
     * @return 响应对象
     */
    @ApiOperation("计划冻结申请暂存")
    @LogMessage(content = "添加BT_BANK_ACC_PLAN_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/planFreezeTemp")
    @ResponseBody
    public AjaxJson planFreezeTemp(@RequestBody BtBankAccPlanFreezeVO vo) {
        btBankAccPlanFreezeService.planFreezeTemp(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 计划冻结申请修改
     *
     * @param vo 计划申请表vo
     * @return 响应对象
     */
    @ApiOperation("计划冻结申请修改")
    @LogMessage(content = "修改BT_BANK_ACC_PLAN_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/planFreezeModify")
    @ResponseBody
    public AjaxJson planFreezeModify(@RequestBody BtBankAccPlanFreezeVO vo) {
        btBankAccPlanFreezeService.planFreezeModify(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 计划冻结申请提交
     *
     * @param ids id集合
     * @return 响应对象
     */
    @ApiOperation("计划冻结申请提交")
    @LogMessage(content = "计划冻结申请提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/planFreezeSubmit")
    @ResponseBody
    public AjaxJson planFreezeSubmit(String ids) {
        btBankAccPlanFreezeService.planFreezeSubmit(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 计划冻结申请删除
     *
     * @param id 计划冻结申请记录主键id
     * @return 响应对象
     */
    @ApiOperation("计划冻结申请删除")
    @LogMessage(content = "计划冻结申请删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtBankAccPlanFreeze entity = btBankAccPlanFreezeService.get(id, true);
        entity.setStatus(BtBankAccConstant.STATUE_DEL);
        btBankAccPlanFreezeService.saveOrUpdate(entity);
        return success();
    }

    /**
     * 查询暂存的计划冻结申请记录
     *
     * @param bankAccId 银行账号id
     * @param curId     币别id
     * @return 暂存的计划冻结申请记录集合
     */
    @ApiOperation("查询暂存的计划冻结申请记录")
    @GetMapping("/listTempPlanFreeze")
    @ResponseBody
    public AjaxJson<List<BtBankAccPlanFreezeVO>> listTempPlanFreeze(String bankAccId, String curId) {
        return success(btBankAccPlanFreezeService.listTempPlanFreeze(bankAccId, curId));
    }

    /**
     * 银行账户计划冻结申请导入
     *
     * @param list 导入数据集合
     * @return 出参对象
     */
    @ApiOperation("银行账户计划冻结申请导入")
    @LogMessage(content = "银行账户计划冻结申请导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BtBankAccPlanFreezeVO> list) {
        btBankAccPlanFreezeService.saveImport(list, getCurrentUser());
        return success();
    }


    /**
     * 银行账户计划冻结申请查询列表
     *
     * @return 银行账户计划冻结数据
     */
    @ApiOperation("查询菜单下的 银行账户计划冻结申请查询列表")
    @PostMapping("/queryDataTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccPlanFreezeVO>> queryDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btBankAccPlanFreezeService.getQueryDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 银行账户计划冻结申请批量解冻
     *
     * @param ids 多条id记录
     * @return 操作结果
     */
    @ApiOperation("银行账户计划冻结申请批量解冻")
    @LogMessage(content = "银行账户计划冻结申请批量解冻", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchManualPlanUnFreeze")
    @ResponseBody
    public AjaxJson batchManualPlanUnFreeze(@RequestParam("ids") String ids) {
        btBankAccPlanFreezeService.batchManualPlanUnFreeze(ids, this.getCurrentUser());
        return success();
    }


    @ApiOperation("银行账户计划冻结申请批量删除")
    @LogMessage(content = "银行账户计划冻结申请批量删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchManualPlanDelete")
    @ResponseBody
    public AjaxJson batchManualPlanDelete(@RequestParam("ids") String ids) {
        if (CharSequenceUtil.isNotEmpty(ids)) {
            btBankAccPlanFreezeService.batchManualPlanDelete(ids, this.getCurrentUser());
        }
        return success();
    }
}
