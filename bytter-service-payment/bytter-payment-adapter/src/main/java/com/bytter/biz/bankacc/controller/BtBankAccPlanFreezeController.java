package com.bytter.biz.bankacc.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.inneraccount.entity.BtBankAccPlanFreeze;
import com.bytter.biz.inneraccount.service.BtBankAccPlanFreezeService;
import com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 银行账户计划冻结申请
 *
 * @author fangcf
 * @date 2025-02-11
 */
@Controller
@RequestMapping("/bankacc/btbankaccplanfreeze")
public class BtBankAccPlanFreezeController extends BaseController {

    @Autowired
    private BtBankAccPlanFreezeService btBankAccPlanFreezeService;

    /**
     * 银行账户计划冻结列表 页面跳转
     *
     * @return 银行账户计划冻结列表
     */
    @LogMessage(content = "查询BT_BANK_ACC_PLAN_FREEZE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bankacc/listBtBankAccPlanFreeze";
    }

    /**
     * 银行账户计划冻结列表
     *
     * @return 银行账户计划冻结数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btBankAccPlanFreezeService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    @GetMapping("/add")
    public String add(BtBankAccPlanFreezeVO vo, Model model) {
        btBankAccPlanFreezeService.addPlanFreeze(vo, model);
        return "bankacc/addBtBankAccPlanFreeze";
    }

    @GetMapping("/update")
    public String update(BtBankAccPlanFreezeVO vo, Model model) {
        BtBankAccPlanFreeze entity = btBankAccPlanFreezeService.get(vo.getId(), true);
        BeanUtils.copyNotNullProperties(entity, vo);
        btBankAccPlanFreezeService.updatePlanFreeze(vo, model);
        return "bankacc/updateBtBankAccPlanFreeze";
    }

    /**
     * 计划冻结申请暂存
     *
     * @param vo 计划申请表vo
     * @return 响应对象
     */
    @LogMessage(content = "添加BT_BANK_ACC_PLAN_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/planFreezeTemp")
    @ResponseBody
    public AjaxJson planFreezeTemp(BtBankAccPlanFreezeVO vo) {
        btBankAccPlanFreezeService.planFreezeTemp(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 计划冻结申请修改
     *
     * @param vo 计划申请表vo
     * @return 响应对象
     */
    @LogMessage(content = "修改BT_BANK_ACC_PLAN_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/planFreezeModify")
    @ResponseBody
    public AjaxJson planFreezeModify(BtBankAccPlanFreezeVO vo) {
        btBankAccPlanFreezeService.planFreezeModify(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 计划冻结申请提交
     *
     * @param ids id集合
     * @return 响应对象
     */
    @LogMessage(content = "添加BT_BANK_ACC_PLAN_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
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
    @LogMessage(content = "删除BT_BANK_ACC_PLAN_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
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
    @PostMapping("/listTempPlanFreeze")
    @ResponseBody
    public List<BtBankAccPlanFreezeVO> listTempPlanFreeze(String bankAccId, String curId) {
        return btBankAccPlanFreezeService.listTempPlanFreeze(bankAccId, curId);
    }

    /**
     * 银行账户计划冻结申请导入
     *
     * @param list 导入数据集合
     * @return 出参对象
     */
    @LogMessage(content = "银行账户计划冻结申请导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BtBankAccPlanFreezeVO> list) {
        btBankAccPlanFreezeService.saveImport(list, getCurrentUser());
        return success();
    }

    /**
     * 银行账户计划冻结申请查询列表 页面跳转
     *
     * @return 银行账户计划冻结列表
     */
    @LogMessage(content = "查询BT_BANK_ACC_PLAN_FREEZE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryList")
    public String queryList(Model model) {
        return "bankacc/listBtBankAccPlanFreezeQuery";
    }

    /**
     * 银行账户计划冻结申请查询列表
     *
     * @return 银行账户计划冻结数据
     */
    @PostMapping("/queryDataTable")
    @ResponseBody
    public DataTablesOutput queryDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btBankAccPlanFreezeService.getQueryDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 银行账户计划冻结申请批量解冻
     *
     * @param ids 多条id记录
     * @return 操作结果
     */
    @LogMessage(content = "银行账户计划冻结申请批量解冻", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchManualPlanUnFreeze")
    @ResponseBody
    public AjaxJson batchManualPlanUnFreeze(@RequestParam("ids") String ids) {
        btBankAccPlanFreezeService.batchManualPlanUnFreeze(ids, this.getCurrentUser());
        return success();
    }


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
