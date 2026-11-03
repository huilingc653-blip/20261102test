package com.bytter.biz.saas.controller.receivable;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.receivable.entity.NisReceivable;
import com.bytter.biz.receivable.service.NisReceivableService;
import com.bytter.biz.receivable.vo.NisReceivableVO;
import com.bytter.biz.receivable.vo.v1.NisReceivableAddConfigVO;
import com.bytter.biz.receivable.vo.v1.NisReceivablePageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 收款通知表
 */
@Api(value = "收款管理",tags = "收款管理")
@RestController("v1NisReceivableController")
@RequestMapping("/v1-payment/receivable/nisreceivable")
public class NisReceivableController extends BaseController {
    @Autowired
    private NisReceivableService nisReceivableService;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;

    /**
     * 收款通知表 table列表
     *
     * @return
     */
    @ApiOperation("收款管理 收款办理表单")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<NisReceivableVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(nisReceivableService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 收款通知表新增页面跳转
     *
     * @return
     */
    @ApiOperation("新增前 配置获取")
    @GetMapping("/getConfigBeforeAdd")
    public AjaxJson<NisReceivableAddConfigVO> add() {
        NisReceivableAddConfigVO configVO = new NisReceivableAddConfigVO();
        return success(configVO);
    }
    /**
     * 收款通知表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("跳转前获取数据信息")
    @GetMapping("/getInfoBeforeUpdate")
    @ResponseBody
    public AjaxJson<NisReceivablePageVO> update(String id) {
        NisReceivablePageVO pageVO = new NisReceivablePageVO();
        NisReceivableVO nisReceivable = nisReceivableService.getByIdUnion(id);
        pageVO.setNisReceivablePage(nisReceivable);
        pageVO.setExternalCorp(configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        CorpDTO corp = corpService.get(nisReceivable.getCorpId());
        //所用账号单位代码
        if (EmptyUtils.isBlank(corp.getUserAccountCode())) {
            corp.setUserAccountCode(corp.getId());
        }
        pageVO.setAccCorpId(corp.getUserAccountCode());
        return success(pageVO);
    }
    /**
     * 收款通知表详情页
     *
     * @return
     */
    @ApiOperation("查询收款办理详情")
    @LogMessage(content = "查询收款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<NisReceivablePageVO> view(@PathVariable("id") String id) {
        NisReceivablePageVO pageVO = new NisReceivablePageVO();
        NisReceivableVO nisReceivable = nisReceivableService.getByIdUnion(id);
        pageVO.setNisReceivablePage(nisReceivable);
        pageVO.setExternalCorp(configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        pageVO.setBusType(NisMessageConstant.BUS_TYPE_RECEIVABLE);

        return success(pageVO);
    }
    /**
     * 数据校验
     *
     * @param nisReceivable
     * @return
     */
    private boolean dataCheck(NisReceivableVO nisReceivable) {
        //单位不为空
        if (StringUtils.isEmpty(nisReceivable.getCorpId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CORP_NULL);
        }
        //币别不为空
        if (StringUtils.isEmpty(nisReceivable.getCurId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CUR_NULL);
        }
        //状态校验
        if (StringUtils.isNotEmpty(nisReceivable.getId())) {
            NisReceivable entity = nisReceivableService.get(nisReceivable.getId(), true);
            //暂存和编辑状态才可以提交
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        return true;
    }
    /**
     * 添加收款通知表
     * 收款办理
     * @param nisReceivable
     * @return
     */
    @ApiOperation("添加收款办理数据")
    @LogMessage(content = "添加收款办理数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody NisReceivableVO nisReceivable) {
        if (dataCheck(nisReceivable)) {
            nisReceivable.setBillFrom(NetbankConstant.RECEIVABLE_BILLFROM_1);
            nisReceivableService.saveNisReceivable(nisReceivable, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 修改收款通知表
     *
     * @param NisReceivable
     * @return
     */
    @ApiOperation("修改收款办理")
    @LogMessage(content = "修改收款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody NisReceivableVO NisReceivable) {
        if (dataCheck(NisReceivable)) {
            NisReceivable.setBatch(false);
            nisReceivableService.updateNisReceivable(NisReceivable, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 获取本方账号
     *
     * @param corpId
     * @return
     */
    @ApiOperation("获取本方账号")
    @GetMapping("/getBankAcc")
    @ResponseBody
    public AjaxJson<List<BtBankAccDTO>> getBankAcc(String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        List<BtBankAccDTO> listBankAccTemp = null;
        if (StringUtils.isNotBlank(corpId)) {
            BtBankAccDTO dto = new BtBankAccDTO();
            dto.setCorpId(corpId);
            listBankAccTemp = btBankAccService.getBankAccListAll(dto);
        }
        if (!EmptyUtils.isEmptyList(listBankAccTemp)) {
            listAcc.addAll(listBankAccTemp);
            for(BtBankAccDTO vo:listAcc){
                BigDecimal balance = bisAccBalService.getLastByBalDate(vo.getBankAcc(), vo.getCurId());
                if(!EmptyUtils.isEmpty(balance)){
                    vo.setBalance(balance);
                }else{
                    vo.setBalance(BigDecimal.ZERO);
                }
            }
        }
        return success(listAcc);
    }
    /**
     * 删除收款通知表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除收款办理")
    @LogMessage(content = "删除收款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        NisReceivable entity = nisReceivableService.get(id);
        //暂存和编辑状态才可以删除
        if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        nisReceivableService.deleteNisReceivable(id, this.getCurrentUser(), false);
        return success();
    }
    /**
     * 批量删除收款通知表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除收款通知表")
    @LogMessage(content = "批量删除收款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisReceivable entity = nisReceivableService.get(id);
            //暂存和编辑状态才可以删除
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        nisReceivableService.batchDelReceivable(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 批量提交收款通知表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量提交收款办理")
    @LogMessage(content = "批量提交收款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        boolean flag = false;
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisReceivableVO nisReceivableVO = nisReceivableService.getByIdUnion(id);
            flag = dataCheck(nisReceivableVO);
        }
        if (flag) {
            nisReceivableService.batchSubmitNisReceivable(ids, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 根据审批列表的收款单corpIds组装单位树
     *
     * @return
     */
    @ApiOperation("根据审批列表的收款单corpIds组装单位树")
    @GetMapping("/treeByCorpIds")
    @ResponseBody
    public AjaxJson<List<Map<String, Object>>> treeByCorpIds() {
        return success(this.nisReceivableService.treeByCorpIds(getCurrentUser()));
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput<WorkFlowPersistence>> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(this.nisReceivableService.dataTableApprove(input));
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     */
    @ApiOperation("跳转至审核详情界面")
    @GetMapping("/approve")
    @ResponseBody
    public AjaxJson<NisReceivablePageVO> approve(String id, String taskId) {
        if (StringUtils.isNotEmpty(id)) {
            NisReceivablePageVO pageVO = new NisReceivablePageVO();
            NisReceivableVO nisReceivableVO = this.nisReceivableService.getByIdUnion(id);
            pageVO.setNisReceivablePage(nisReceivableVO);
            pageVO.setTaskId(taskId);
            pageVO.setBusType(NisMessageConstant.BUS_TYPE_RECEIVABLE);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @ApiOperation("保存导入数据")
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<NisReceivableVO> list) {
        this.nisReceivableService.saveImport(list, this.getCurrentUser());
        return success();
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     */
    @ApiOperation("跳转至手机审核详情界面")
    @GetMapping("/mobileApprove")
    public AjaxJson<NisReceivablePageVO> mobileApprove(String businessId, String taskId) {

        if (StringUtils.isNotEmpty(businessId)) {
            NisReceivablePageVO pageVO = new NisReceivablePageVO();
            NisReceivableVO nisReceivableVO = this.nisReceivableService.getByIdUnion(businessId);
            pageVO.setNisReceivablePage(nisReceivableVO);
            pageVO.setTaskId(taskId);
            pageVO.setBusType(NisMessageConstant.BUS_TYPE_RECEIVABLE);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 收款详细信息选择页面
     *
     * @return
     */
    @ApiOperation("收款详细信息选择页面")
    @GetMapping("/receivablePage")
    @ResponseBody
    public AjaxJson<NisReceivablePageVO> receivablePage(NisReceivableVO vo) {
        NisReceivablePageVO pageVO = new NisReceivablePageVO();
        BtBankAccDTO dto = btBankAccService.getBankAccInfo(vo.getBankAcc());
        vo.setAccName(dto.getAccName());
        vo.setOppAccNo(StringUtils.formatNull(vo.getOppAccNo()));
        vo.setOppBankCode(StringUtils.formatNull(vo.getOppBankCode()));
        vo.setOppAccName(StringUtils.formatNull(vo.getOppAccName()));
        pageVO.setNisReceivablePage(vo);
        return success(pageVO);
    }
    /**
     * 选择收款通知表
     *
     * @param ids
     * @return
     */
    @ApiOperation("收款详细信息选择 多个收款单")
    @LogMessage(content = "选择收款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSelect")
    @ResponseBody
    public AjaxJson batchSelect(@RequestParam("ids") String ids, @RequestParam("bankSerialNo") String bankSerialNo) {
        nisReceivableService.batchSelect(ids, bankSerialNo);
        return success();
    }
}
