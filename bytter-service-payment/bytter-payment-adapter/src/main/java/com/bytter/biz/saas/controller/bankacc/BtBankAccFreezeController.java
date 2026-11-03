package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.inneraccount.entity.BtBankAccFreeze;
import com.bytter.biz.inneraccount.service.BtBankAccFreezeService;
import com.bytter.biz.inneraccount.vo.BtBankAccFreezeVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccFreezePageVO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC_FREEZE
 * @date 2017-06-14 14:26:46
 */
@Api(value = "银行账户冻结申请",tags = "银行账户冻结申请")
@RestController("v1BtBankAccFreezeController")
@RequestMapping("/v1-payment/bankacc/btbankaccfreeze")
public class BtBankAccFreezeController extends BaseController {
    @Autowired
    private BtBankAccFreezeService btBankAccFreezeService;

    /**
     * BT_BANK_ACC_FREEZE table列表
     *
     * @return
     */
    @ApiOperation("银行冻结查询列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btBankAccFreezeService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * BT_BANK_ACC_FREEZE新增页面跳转
     *
     * @return
     */
    @ApiOperation("新增冻结申请前的账号信息获取")
    @GetMapping("/getAccInfoBeforeFreeze")
    @ResponseBody
    public AjaxJson<BtBankAccFreezePageVO> getAccInfoBeforeFreeze(String bankAccId,String curId) {
        BtBankAccFreezePageVO pageVo = btBankAccFreezeService.getAccInfoBeforeFreeze(bankAccId, curId);
        return success(pageVo);
    }
    /**
     * BT_BANK_ACC_FREEZE编辑页面跳转
     *
     * @return
     */
    @ApiOperation("修改冻结账号信息获取")
    @GetMapping("/getFreezeById")
    @ResponseBody
    public AjaxJson<BtBankAccFreezePageVO> getFreezeById(String id) {
        BtBankAccFreezePageVO btBankAccFreezePageVO = new BtBankAccFreezePageVO();
        BtBankAccFreezeDTO dto = btBankAccFreezeService.get(id);
        BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
        BeanUtils.copyNotNullProperties(dto, vo);
        btBankAccFreezeService.toUpdateFreeze(vo, btBankAccFreezePageVO);
        return success(btBankAccFreezePageVO);
    }
    /**
     * BT_BANK_ACC_FREEZE详情页
     *
     * @return
     */
    @ApiOperation("查询银行冻结详情")
    @LogMessage(content = "查询BT_BANK_ACC_FREEZE详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    @ResponseBody
    public AjaxJson<BtBankAccFreezePageVO> view(@PathVariable("id") String id) {
        BtBankAccFreezeDTO dto = btBankAccFreezeService.get(id, true);
        BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
        BeanUtils.copyNotNullProperties(dto, vo);
        BtBankAccFreezePageVO pageVo = btBankAccFreezeService.getAccInfoBeforeFreeze(vo.getBankAccId(), vo.getCurId());
        pageVo.setFreezeMoney(dto.getFreezeMoney());
        pageVo.setFreezeRession(dto.getFreezeRession());
        return success(pageVo);
    }
    /**
     * 添加BT_BANK_ACC_FREEZE
     *
     * @param btBankAccFreeze
     * @return
     */
    @ApiOperation("银行账户冻结暂存")
    @LogMessage(content = "添加BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/freezeSaveTemp")
    @ResponseBody
    public AjaxJson freezeSaveTemp(@RequestBody BtBankAccFreeze btBankAccFreeze) {
        btBankAccFreezeService.freezeSaveTemp(btBankAccFreeze, this.getCurrentUser());
        return success();
    }
    /**
     * 冻结提交
     */
    @ApiOperation("银行账户冻结提交")
    @LogMessage(content = "添加BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/freezeSave")
    @ResponseBody
    public AjaxJson freezeSave(String ids) {
        btBankAccFreezeService.toSubmit(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 修改BT_BANK_ACC_FREEZE
     *
     * @param btBankAccFreeze
     * @return
     */
    @ApiOperation("修改冻结申请")
    @LogMessage(content = "修改BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody BtBankAccFreeze btBankAccFreeze) {
        BtBankAccFreezeDTO dto = btBankAccFreezeService.get(btBankAccFreeze.getId());
        dto.setUpdateBy(this.getCurrentUser().getLoginName());
        dto.setUpdateDate(DateUtils.getDate());
        dto.setFreezeMoney(btBankAccFreeze.getFreezeMoney());
        dto.setFreezeDate(btBankAccFreeze.getFreezeDate());
        dto.setFreezeRession(btBankAccFreeze.getFreezeRession());
        btBankAccFreezeService.saveOrUpdate(dto);
        return success();
    }
    /**
     * 删除BT_BANK_ACC_FREEZE
     *
     * @param id
     * @return
     */
    @ApiOperation("删除冻结数据")
    @LogMessage(content = "删除BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtBankAccFreezeDTO dto = btBankAccFreezeService.get(id);
        dto.setStatus(BtBankAccConstant.STATUE_DEL);
        btBankAccFreezeService.saveOrUpdate(dto);
        return success();
    }
    /**
     * 批量删除BT_BANK_ACC_FREEZE
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除冻结")
    @LogMessage(content = "批量删除BT_BANK_ACC_FREEZE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtBankAccFreezeDTO dto = btBankAccFreezeService.get(id);
            btBankAccFreezeService.deleteEntity(dto);
        }
        return success();
    }
    @ApiOperation("获取冻结暂存列表")
    @GetMapping("/getTempFreeze")
    @ResponseBody
    public AjaxJson<List<BtBankAccFreezeVO>> getPrentBankAccList(String bankAccId, String curId) {
        return success(btBankAccFreezeService.getTempFreeze(bankAccId, curId));
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("银行账户冻结审核列表数据")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccFreezeDTO>> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(this.btBankAccFreezeService.dataTableApprove(input));
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @return String
     */
    @ApiOperation("跳转至冻结审核详情界面前获取信息")
    @GetMapping("/approve")
    @ResponseBody
    public AjaxJson<BtBankAccFreezePageVO> approve(String id, String taskId) {
        if (StringUtils.isNotEmpty(id)) {
            BtBankAccFreezeDTO dto = this.btBankAccFreezeService.get(id, true);
            BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
            BeanUtils.copyNotNullProperties(dto, vo);
            BtBankAccFreezePageVO btBankAccFreezePageVO = btBankAccFreezeService.getAccInfoBeforeFreeze(vo.getBankAccId(), vo.getCurId());
            btBankAccFreezePageVO.setTaskId(taskId);
            btBankAccFreezePageVO.setFreezeMoney(dto.getFreezeMoney());
            btBankAccFreezePageVO.setFreezeRession(dto.getFreezeRession());
            return success(btBankAccFreezePageVO);
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
    @ApiOperation("手机审核冻结账户跳转")
    @GetMapping("/mobileApprove")
    @ResponseBody
    public AjaxJson<BtBankAccFreezePageVO> mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            BtBankAccFreezeDTO dto = this.btBankAccFreezeService.get(businessId, true);
            BtBankAccFreezeVO vo = new BtBankAccFreezeVO();
            BeanUtils.copyNotNullProperties(dto, vo);
            btBankAccFreezeService.toAddFreeze(vo, model);
            BtBankAccFreezePageVO btBankAccFreezePageVO = btBankAccFreezeService.getAccInfoBeforeFreeze(vo.getBankAccId(), vo.getCurId());
            btBankAccFreezePageVO.setTaskId(taskId);
            btBankAccFreezePageVO.setFreezeMoney(dto.getFreezeMoney());
            btBankAccFreezePageVO.setFreezeRession(dto.getFreezeRession());
            btBankAccFreezePageVO.setFreeze(dto);
            return success(btBankAccFreezePageVO);
        }
        return success();
    }
    /**
     * BT_BANK_ACC_FREEZE table列表
     *
     * @return
     */
    @ApiOperation("查询->银行账户冻结/解冻查询")
    @PostMapping("/dataTableQuery")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccFreezeDTO>> dataTableQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btBankAccFreezeService.getDataTablesQuery(input, this.getCurrentUser()));
    }
}
