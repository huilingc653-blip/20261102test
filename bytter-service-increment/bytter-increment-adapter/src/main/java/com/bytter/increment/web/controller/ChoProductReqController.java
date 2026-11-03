package com.bytter.increment.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.domain.vo.ChoProductReqImportVo;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/chowmatistic/choproductreq")
public class ChoProductReqController extends BaseController {
    @Autowired
    private ChoProductReqService choProductReqService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @GetMapping("/list")
    public String list(Model model){
        return "chowmatistic/listChoProductReq";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return choProductReqService.getDataTablesUnion(dataTablesInput,this.getCurrentUser());
    }
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model){
        model.addAttribute("flowStatus", flowStatus);
        return "chowmatistic/listChoProductReqApprove";
    }
    /**
     * 跳转至审核详情界面
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        ChoProductReqVo vo = new ChoProductReqVo();
        if (StringUtils.isNotEmpty(id)) {
            vo = this.choProductReqService.getChoProductReqVoById(id);
            model.addAttribute("choProductReqPage", vo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_BANK);
        }
        return "chowmatistic/approveChoProductReq";
    }
    /**
     * 导入
     *
     * @param list 理财产品登记数据
     * @return 是否成功
     */
    @LogMessage(content = "导入理财产品登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<ChoProductReqImportVo> list) {
        return choProductReqService.saveImport(list, this.getCurrentUser());
    }
    /**
     * 下载理财产品登记导入模板
     *
     * @param request  request
     * @param response response
     * @param vo       查询条件
     * @param model    model
     */
    @LogMessage(content = "下载理财产品登记导入模板", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/downloadReqModel")
    public void downloadReqModel(HttpServletRequest request, HttpServletResponse response, ChoProductReqVo vo, Model model) throws Exception {
        choProductReqService.downloadReqModel(request, response, vo, model, getCurrentUser());
    }
    /**
     * 加载审核列表数据
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput){
        return this.choProductReqService.dataTableApprove(dataTablesInput);
    }
    @GetMapping("/add")
    public String add(ChoProductReq choProductReq, Model model){
        if (StringUtils.isNotEmpty(choProductReq.getId())) {
            choProductReq = choProductReqService.get(choProductReq.getId());
            model.addAttribute("choProductReqPage", choProductReq);
        }
        return "chowmatistic/addChoProductReq";
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ChoProductReq choProduct){
        choProduct.setCreateBy(this.getCurrentUser().getLoginName());
        choProduct.setCreateDate(DateUtils.getDate());
        choProductReqService.saveChoProductReq(choProduct,getCurrentUser());
        return success();
    }
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids){
        choProductReqService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }
    @GetMapping("/update")
    public String update(ChoProductReq choProductReq, Model model){
        if (StringUtils.isNotEmpty(choProductReq.getId())) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(choProductReq.getId());
            model.addAttribute("choProductReqPage", choProductReqVo);
        }
        return "chowmatistic/updateChoProductReq";
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ChoProductReq choProductReq){
        ChoProductReq t = choProductReqService.get(choProductReq.getId(), true);
        choProductReq.setUpdateBy(this.getCurrentUser().getLoginName());
        choProductReq.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyProperties(choProductReq, t);
        choProductReqService.toModify(t,getCurrentUser());
        return success();
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        ChoProductReq entity = choProductReqService.get(id);
        this.choProductReqService.deleteEntity(entity);
        return success();
    }
    @PostMapping("/back/{id}")
    @ResponseBody
    public AjaxJson back(@PathVariable("id") String id){
        this.choProductReqService.back(id);
        return success();
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id,Model model){
        if (StringUtils.isNotEmpty(id)) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(id);
            model.addAttribute("choProductReqPage", choProductReqVo);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ);
        }
        return "chowmatistic/viewChoProductReq";
    }
    /**
     * 获取账号
     * @param corpId
     * @param bankCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBankAcc(String corpId, String bankCode, String curId, String bankType){
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if(!StringUtils.isEmpty(corpId) && !StringUtils.isEmpty(curId) && !StringUtils.isEmpty(bankType)){
            //获取转账权限的银行在线账号
            List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            //获取转账权限的银行离线账号
            List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if(!EmptyUtils.isEmptyList(listOnlineBankAcc)){
                listAcc.addAll(listOnlineBankAcc);
            }
            if(!EmptyUtils.isEmptyList(listOfflineBankAcc)){
                listAcc.addAll(listOfflineBankAcc);
            }
        }
        //过滤活期账号
        List<BtBankAccDTO> listAccResult = new ArrayList<BtBankAccDTO>();
        if(!EmptyUtils.isEmptyList(listAcc)){
            for(BtBankAccDTO dto: listAcc){
                if(curId.equalsIgnoreCase(dto.getCurId()) && bankType.equalsIgnoreCase(dto.getBankTypeId())
                        && (StringUtils.isBlank(bankCode) || bankCode.equalsIgnoreCase(dto.getBankCode()))){
                    listAccResult.add(dto);
                }
            }
        }
        return listAccResult;
    }
    @GetMapping("/select")
    public String selectProduct(Model model){
        return "chowmatistic/selectChoProduct";
    }
    @PostMapping("/listValidSign")
    @ResponseBody
    public DataTablesOutput dataTableValidSign(@RequestBody DataTablesInput dataTablesInput){
        Date deadlineDate = DateUtils.getDateYMD();
        return choProductReqService.getDataTablesValidSign(dataTablesInput,deadlineDate,this.getCurrentUser());
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
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(businessId);
            model.addAttribute("choProductReqPage", choProductReqVo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ);
        }
        return "chowmatistic/viewApproveChoProductReq";
    }

    @PostMapping("/modifyAttachmentId")
    @ResponseBody
    public AjaxJson modifyAttachmentId(ChoProductReq choProductReq){
        ChoProductReq t = choProductReqService.get(choProductReq.getId(), true);
        t.setAttachmentId(choProductReq.getAttachmentId());
        choProductReqService.modifyAttachmentId(t,getCurrentUser());
        return success();
    }

    @PostMapping("/filterBySource")
    @ResponseBody
    public Object filterBySource(String corpVOList) {
        return choProductReqService.getFilterBySource(corpVOList);
    }

    /**
     * 银行类型维护列表-根据理财产品登记表中银行类型获取
     *
     * @return
     */
    @GetMapping("/bankTypeJson")
    @ResponseBody
    public List<BankTypeDTO> getBankTypeJson() {
        return choProductReqService.getBankTypeJson();
    }

    /**
     * 购买银行列表-根据理财产品登记表中购买银行获取，可根据银行类型联动查询
     *
     * @return
     */
    @GetMapping("/bankInfoJson")
    @ResponseBody
    public List<ChoProductReqVo> getBankInfoJson(String bankTypeIds) {
        return choProductReqService.getBankInfoJson(bankTypeIds);
    }
}
