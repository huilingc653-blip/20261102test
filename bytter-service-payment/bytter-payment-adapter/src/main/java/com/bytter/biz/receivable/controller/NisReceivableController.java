package com.bytter.biz.receivable.controller;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.receivable.entity.NisReceivable;
import com.bytter.biz.receivable.service.NisReceivableService;
import com.bytter.biz.receivable.vo.NisReceivableVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
////import org.apache.shiro.authz.annotation.Logical;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 收款通知表
 * @date 2019-07-02 17:30:33
 */
@Controller
@RequestMapping("/receivable/nisreceivable")
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
     * 收款通知表列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISRECEIVABLE_QUERY)
    @LogMessage(content = "查询收款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "receivable/listNisReceivable";
    }
    /**
     * 收款通知表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_NETBANK_NISRECEIVABLE_QUERY,
//            ResourceConstant.NETBANK_NETBANKQUERY_NISRECEIVABLE_QUERY
//    }, logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return nisReceivableService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 收款通知表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISRECEIVABLE_APPLY)
    @GetMapping("/add")
    public String add(NisReceivable nisReceivable, Model model) {
        if (StringUtils.isNotEmpty(nisReceivable.getId())) {
            nisReceivable = nisReceivableService.get(nisReceivable.getId());
            model.addAttribute("nisReceivablePage", nisReceivable);
        }
        model.addAttribute("externalCorp", configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        return "receivable/addNisReceivable";
    }
    /**
     * 收款通知表编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISRECEIVABLE_APPLY)
    @GetMapping("/update")
    public String update(NisReceivableVO nisReceivable, Model model) {
        if (StringUtils.isNotEmpty(nisReceivable.getId())) {
            nisReceivable = nisReceivableService.getByIdUnion(nisReceivable.getId());
            model.addAttribute("nisReceivablePage", nisReceivable);
        }
        model.addAttribute("externalCorp", configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        CorpDTO corp = corpService.get(nisReceivable.getCorpId());
        //所用账号单位代码
        if (EmptyUtils.isBlank(corp.getUserAccountCode())) {
            corp.setUserAccountCode(corp.getId());
        }
        model.addAttribute("accCorpId", corp.getUserAccountCode());
        return "receivable/updateNisReceivable";
    }
    /**
     * 收款通知表详情页
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISRECEIVABLE_QUERY)
    @LogMessage(content = "查询收款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        NisReceivableVO nisReceivable = nisReceivableService.getByIdUnion(id);
        model.addAttribute("nisReceivablePage", nisReceivable);
        model.addAttribute("externalCorp", configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_RECEIVABLE);
        return "receivable/viewNisReceivable";
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISRECEIVABLE_APPLY)
    @LogMessage(content = "添加收款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisReceivableVO nisReceivable) {
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISRECEIVABLE_APPLY)
    @LogMessage(content = "修改收款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(NisReceivableVO NisReceivable) {
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
    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBankAcc(String corpId) {
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
        return listAcc;
    }
    /**
     * 删除收款通知表
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_DELETE)
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_DELETE)
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_UPDATE)
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
     * 收款查询 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANKQUERY_NISRECEIVABLE_QUERY)
    @LogMessage(content = "查询收款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        return "receivable/listNisReceivableQuery";
    }
    /**
     * 根据审批列表的收款单corpIds组装单位树
     *
     * @return
     */
    @GetMapping("/treeByCorpIds")
    @ResponseBody
    public Object treeByCorpIds() {
        return this.nisReceivableService.treeByCorpIds(getCurrentUser());
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "receivable/listNisReceivableApprove";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.nisReceivableService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            NisReceivableVO nisReceivableVO = this.nisReceivableService.getByIdUnion(id);
            model.addAttribute("nisReceivablePage", nisReceivableVO);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", NisMessageConstant.BUS_TYPE_RECEIVABLE);
        }
        return "receivable/approveNisReceivable";
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
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
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            NisReceivableVO nisReceivableVO = this.nisReceivableService.getByIdUnion(businessId);
            model.addAttribute("nisReceivablePage", nisReceivableVO);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", NisMessageConstant.BUS_TYPE_RECEIVABLE);
        }
        return "receivable/viewApproveNisReceivable";
    }
    /**
     * 收款详细信息选择页面
     *
     * @return
     */
    @GetMapping("/receivablePage")
    public String receivablePage(NisReceivableVO vo, Model model) {
        BtBankAccDTO dto = btBankAccService.getBankAccInfo(vo.getBankAcc());
        vo.setAccName(dto.getAccName());
        vo.setOppAccNo(StringUtils.formatNull(vo.getOppAccNo()));
        vo.setOppBankCode(StringUtils.formatNull(vo.getOppBankCode()));
        vo.setOppAccName(StringUtils.formatNull(vo.getOppAccName()));
        model.addAttribute("nisReceivablePage", vo);
        return "receivable/nisReceivablePage";
    }
    /**
     * 选择收款通知表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "选择收款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSelect")
    @ResponseBody
    public AjaxJson batchSelect(@RequestParam("ids") String ids, @RequestParam("bankSerialNo") String bankSerialNo) {
        nisReceivableService.batchSelect(ids, bankSerialNo);
        return success();
    }
}
