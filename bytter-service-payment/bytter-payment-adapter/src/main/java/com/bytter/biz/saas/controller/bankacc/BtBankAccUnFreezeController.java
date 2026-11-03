package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.inneraccount.entity.BtBankAccFreeze;
import com.bytter.biz.inneraccount.service.BtBankAccUnFreezeService;
import com.bytter.biz.inneraccount.vo.BtBankAccFreezeVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccUnFreezePageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC_UNFREEZE
 * @date 2017-06-14 14:26:46
 */
@Api(value = "银行账户解冻", tags = "银行账户解冻")
@RestController("v1BtBankAccUnFreezeController")
@RequestMapping("/v1-payment/bankacc/btbankaccunfreeze")
public class BtBankAccUnFreezeController extends BaseController {
    @Autowired
    private BtBankAccUnFreezeService btBankAccUnFreezeService;
    /**
     * BT_BANK_ACC_UNFREEZE table列表
     *
     * @return
     */
    @ApiOperation("银行解冻查询列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btBankAccUnFreezeService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * BT_BANK_ACC_UNFREEZE新增页面跳转
     *
     * @return
     */
    @ApiOperation(value = "新增冻结申请前的账号信息获取",notes = "fuuid非必填 他是冻结信息Id")
    @GetMapping("/getAccInfoBeforeUnFreeze")
    @ResponseBody
    public AjaxJson<BtBankAccUnFreezePageVO> getAccInfoBeforeUnFreeze(String bankAccId,String curId
                                            ,@RequestParam(required = false)  String fuuid) {
        BtBankAccUnFreezePageVO pageVo = btBankAccUnFreezeService.getAccInfoBeforeUnFreeze(bankAccId, curId,fuuid);
        return success(pageVo);
    }

    @ApiOperation(value = "修改解冻账号信息获取",notes = "取代原先update跳转页面 用于跳转前获取数据")
    @GetMapping("/getUnFreezeById")
    @ResponseBody
    public AjaxJson<BtBankAccUnFreezePageVO> getUnFreezeById(String id) {
        BtBankAccUnFreezePageVO btBankAccUnFreezePageVO = new BtBankAccUnFreezePageVO();
        BtBankAccFreezeDTO dto = btBankAccUnFreezeService.get(id);
        BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
        BeanUtils.copyNotNullProperties(dto, vo);
        btBankAccUnFreezeService.toUpdateFreeze(vo, btBankAccUnFreezePageVO);
        return success(btBankAccUnFreezePageVO);
    }

    @ApiOperation("查询银行解冻详情")
    @LogMessage(content = "查询BT_BANK_ACC_UNFREEZE详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BtBankAccUnFreezePageVO> view(@PathVariable("id") String id, Model model) {
        BtBankAccFreezeDTO freeze = this.btBankAccUnFreezeService.get(id);
        BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
        BeanUtils.copyNotNullProperties(freeze, vo);
        BtBankAccUnFreezePageVO pageVO = btBankAccUnFreezeService.getAccInfoBeforeUnFreeze(vo.getBankAccId(), vo.getCurId(), vo.getFUuid());
        pageVO.setFreezeMoney(freeze.getFreezeMoney());
        pageVO.setFreezeRession(freeze.getFreezeRession());
        return success(pageVO);
    }
    /**
     * 添加BT_BANK_ACC_UNFREEZE
     *
     * @param btBankAccFreeze
     * @return
     */
    @ApiOperation(value = "添加暂存解冻信息",notes = "原先是freezeSaveTemp 现在改名了unFreezeSaveTemp")
    @LogMessage(content = "添加BT_BANK_ACC_UNFREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/unFreezeSaveTemp")
    @ResponseBody
    public AjaxJson freezeSaveTemp(@RequestBody BtBankAccFreezeDTO btBankAccFreeze) {
        btBankAccUnFreezeService.freezeSaveTemp(btBankAccFreeze, this.getCurrentUser());
        return success();
    }
    /**
     * 解冻提交
     */
    @ApiOperation(value = "添加提交解冻信息",notes = "原先是freezeSave 现在改名了unFreezeSave")
    @LogMessage(content = "添加BT_BANK_ACC_UNFREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/unFreezeSave")
    @ResponseBody
    public AjaxJson freezeSave(String ids) {
        btBankAccUnFreezeService.toSubmit(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 修改BT_BANK_ACC_UNFREEZE
     *
     * @param btBankAccFreeze
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCUNFREEZE_APPLY)
    @ApiOperation("修改冻结信息")
    @LogMessage(content = "修改BT_BANK_ACC_UNFREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody BtBankAccFreeze btBankAccFreeze) {
        BtBankAccFreezeDTO t = btBankAccUnFreezeService.get(btBankAccFreeze.getId());
        BeanUtils.copyNotNullProperties(btBankAccFreeze, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        btBankAccUnFreezeService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除BT_BANK_ACC_UNFREEZE
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCUNFREEZE_APPLY)
    @ApiOperation("删除冻结消息")
    @LogMessage(content = "删除BT_BANK_ACC_UNFREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtBankAccFreezeDTO btBankAccFreeze = btBankAccUnFreezeService.get(id);
        btBankAccFreeze.setStatus(BtBankAccConstant.STATUE_DEL);
        btBankAccFreeze.setUpdateBy(this.getCurrentUser().getLoginName());
        btBankAccFreeze.setUpdateDate(DateUtils.getDate());
        btBankAccUnFreezeService.saveOrUpdate(btBankAccFreeze);
        return success();
    }
    /**
     * 批量删除BT_BANK_ACC_UNFREEZE
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCUNFREEZE_APPLY)
    @ApiOperation("批量删除冻结信息")
    @LogMessage(content = "批量删除BT_BANK_ACC_UNFREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtBankAccFreezeDTO btBankAccFreeze = btBankAccUnFreezeService.get(id);
            btBankAccUnFreezeService.deleteEntity(btBankAccFreeze);
        }
        return success();
    }
    @ApiOperation("获取冻结信息列表")
    @GetMapping("/getTempFreeze")
    @ResponseBody
    public AjaxJson<List<BtBankAccFreezeDTO>> getTempFreeze(String bankAccId, String curId) {
        return success(btBankAccUnFreezeService.getTempFreeze(bankAccId, curId));
    }
    @ApiOperation("获取解冻信息列表")
    @GetMapping("/getUnFreeze")
    @ResponseBody
    public AjaxJson<List<BtBankAccFreezeDTO>> getUnFreeze(String bankAccId, String curId) {
        return success(btBankAccUnFreezeService.getUnFreeze(bankAccId, curId));
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("银行账户解冻审核列表数据")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccFreezeDTO>> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(this.btBankAccUnFreezeService.dataTableApprove(input));
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @return String
     */
    @ApiOperation("跳转至解冻审核详情界面前获取信息")
    @GetMapping("/approve")
    @ResponseBody
    public AjaxJson<BtBankAccUnFreezePageVO> approve(String id, String taskId) {
        if (StringUtils.isNotEmpty(id)) {
            BtBankAccFreezeDTO freeze = this.btBankAccUnFreezeService.get(id);
            BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
            BeanUtils.copyNotNullProperties(freeze, vo);
            BtBankAccUnFreezePageVO pageVO = btBankAccUnFreezeService.getAccInfoBeforeUnFreeze(vo.getBankAccId(), vo.getCurId(), vo.getFUuid());
            pageVO.setTaskId(taskId);
            pageVO.setFreezeMoney(freeze.getFreezeMoney());
            pageVO.setFreezeRession(freeze.getFreezeRession());
            return success(pageVO);
        }
        return success();
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @ApiOperation("手机审核解冻账户跳转")
    @GetMapping("/mobileApprove")
    @ResponseBody
    public AjaxJson<BtBankAccUnFreezePageVO> mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            BtBankAccFreezeDTO freeze = this.btBankAccUnFreezeService.get(businessId);
            BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
            BeanUtils.copyNotNullProperties(freeze, vo);
            BtBankAccUnFreezePageVO pageVO = btBankAccUnFreezeService.getAccInfoBeforeUnFreeze(vo.getBankAccId(), vo.getCurId(), vo.getFUuid());
            pageVO.setTaskId(taskId);
            pageVO.setFreezeMoney(freeze.getFreezeMoney());
            pageVO.setFreezeRession(freeze.getFreezeRession());
            pageVO.setFreeze(freeze);
            return success(pageVO);
        }
        return success();
    }
}
