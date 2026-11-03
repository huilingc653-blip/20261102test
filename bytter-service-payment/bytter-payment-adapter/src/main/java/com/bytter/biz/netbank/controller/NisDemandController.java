package com.bytter.biz.netbank.controller;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.netbank.entity.NisDemand;
import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
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
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *资金需求单
 */
@Controller
@RequestMapping("/netbank/nisdemand")
public class NisDemandController extends BaseController {
    @Autowired
    private NisDemandService nisDemandService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @LogMessage(content = "查询资金需求单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "netbank/listNisDemand";
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = nisDemandService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
        return out;
    }

    /**
     * 资金需求清单表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "netbank/addNisDemand";
    }


    /**
     * 获取需求账号
     * @param resource
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAccQuery")
    @ResponseBody
    public List<BtBankAccDTO> getBankAccQuery(String resource,String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        //获取转账权限的银行账号
        List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), corpId, resource, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
        for (BtBankAccDTO dto : listBankAccTemp) {
            dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
            listAcc.add(dto);
        }
        return listAcc;
    }

    /**
     * 获取需求账号
     * @param corpId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBankAcc(String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        if (!StringUtils.isEmpty(corpId.trim())) {
            //获取单位信息
            CorpDTO corp = corpService.get(corpId);
            String parentIds = StringUtils.isBlank(corp.getParentIds()) ? corp.getId() : corp.getParentIds();
            //获取转账权限的银行账号
            List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), parentIds, null, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
            List<BtBankAccDTO> listBankAcc = new ArrayList<>();
            if (!EmptyUtils.isEmptyList(listBankAccTemp)) {
                for (BtBankAccDTO dto : listBankAccTemp) {
                    //账号属性对外付款不为收款的账号才能对外付款
                    if ("2".equals(dto.getForeignDes()) || "3".equals(dto.getForeignDes())) {
                        dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
                        listBankAcc.add(dto);
                    }
                }
            }
            if (!EmptyUtils.isEmptyList(listBankAcc)) {
                listAcc.addAll(listBankAcc);
            }
        }
        return listAcc;
    }

    /**
     * 获取账号余额
     * @param bankAcc
     * @param curId
     * @return
     */
    @RequestMapping(params = "getBal")
    @ResponseBody
    public BisAccBalDTO getBal(String bankAcc, String curId){
        BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(bankAcc,curId);
        if(EmptyUtils.isEmpty(bisAccBalDTO)){
            bisAccBalDTO = new BisAccBalDTO();
        }
        return bisAccBalDTO;
    }

    /**
     * 获取父账号
     * @param corpId
     * @return
     */
    @RequestMapping(params = "getSuperCorp")
    @ResponseBody
    public CorpDTO getSuperCorp(String corpId){
        CorpDTO corpDTO = corpService.getParentById(corpId);
        if(EmptyUtils.isEmpty(corpDTO)){
            corpDTO = new CorpDTO();
        }
        return corpDTO;
    }

    /**
     * 获取父账号
     * @param bankAccId
     * @param curId
     * @return
     */
    @RequestMapping(params = "getSuperBankAcc")
    @ResponseBody
    public BtBankAccDTO getSuperBankAcc(String bankAccId, String curId){
        return btBankAccService.getParentAccByAccId(bankAccId,curId);
    }

    @LogMessage(content = "添加资金需求清单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisDemandVO nisDemandVO) {
        String billNo = nisDemandService.saveNisDemand(nisDemandVO, this.getCurrentUser());
        return success(billNo);
    }

    /**
     * 批量作废资金需求清单
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量作废资金需求清单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDefeat")
    @ResponseBody
    public AjaxJson batchDefeat(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisDemand entity = nisDemandService.get(id, true);
            //暂存和已确认状态才可以提交
            if (entity != null && !NisDemandConstant.STATUE_TEMP.equals(entity.getStatus()) && !NisDemandConstant.STATUE_CHECK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        nisDemandService.batchDefeatNisDemand(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 作废资金需求清单
     *
     * @param id
     * @return
     */
    @LogMessage(content = "作废资金需求清单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/defeat/{id}")
    @ResponseBody
    public AjaxJson defeat(@PathVariable("id") String id) {
        //状态校验
        NisDemand entity = nisDemandService.get(id, true);
        //暂存和已确认状态才可以提交
        if (entity != null && !NisDemandConstant.STATUE_TEMP.equals(entity.getStatus()) && !NisDemandConstant.STATUE_CHECK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        nisDemandService.defeatNisDemand(id, this.getCurrentUser());
        return success();
    }


    /**
     * 修改资金需求清单
     * @param nisDemandVO
     * @return
     */
    @LogMessage(content = "修改资金需求清单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(NisDemandVO nisDemandVO) {
        nisDemandService.updateNisDemand(nisDemandVO, this.getCurrentUser());
        return success();
    }

    /**
     * 资金需求清单编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(NisDemandVO nisDemandVO, Model model) {
        if (StringUtils.isNotEmpty(nisDemandVO.getId())) {
            nisDemandVO = nisDemandService.getByIdUnion(nisDemandVO.getId());
            //暂存和编辑状态才可以提交
            if (!EmptyUtils.isEmpty(nisDemandVO) && !NisDemandConstant.STATUE_TEMP.equals(nisDemandVO.getStatus()) && !NisDemandConstant.STATUE_CHECK.equals(nisDemandVO.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisDemandVO.getBillNo());
            }
            model.addAttribute("nisDemand", nisDemandVO);
        }
        return "netbank/updateNisDemand";
    }

    /**
     * 付款通知表详情页
     *
     * @return
     */
    @LogMessage(content = "查询付款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        NisDemandVO nisDemandVO = nisDemandService.getByIdUnion(id);
        model.addAttribute("nisDemand", nisDemandVO);
        return "netbank/viewNisDemand";
    }


    /**
     * 付款通知表详情页
     *
     * @return
     */
    @LogMessage(content = "查询付款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillNo/{BillNo}")
    public String viewByBillNo(@PathVariable("BillNo") String BillNo, Model model) {
        NisDemandVO nisDemandVO = nisDemandService.getByBillNoUnion(BillNo);
        model.addAttribute("nisDemand", nisDemandVO);
        return "netbank/viewNisDemand";
    }
    /**
     * 生成上级子账户需求
     * @param ids
     * @return
     */
    @LogMessage(content = "生成上级子账户需求单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/generateDemand")
    @ResponseBody
    public AjaxJson generateDemand(@RequestParam("ids") String ids) {
        List<String> idList = Arrays.asList(ids.split(CommonConstant.COMMA_SPLIT));
        this.nisDemandService.generateDemand(idList,this.getCurrentUser());
        return success();
    }

    /**
     * 生成下拨付款指令
     * @param ids
     * @return
     */
    @LogMessage(content = "生成上级子账户需求单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/generateAllocation")
    @ResponseBody
    public AjaxJson generateAllocation(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            nisDemandService.generateAllocation(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 付款单是否存在需求单
     * @param id       付款id
     * @return
     */
    @GetMapping("isHasDemand")
    @ResponseBody
    public AjaxJson isHasDemand(String id,String payAcc,String curId) {
        AjaxJson j = new AjaxJson();
        NisDemandVO nisDemandVO = nisDemandService.listByPayBillIdNoStatus(id);
        BtBankAccDTO  btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(payAcc,curId);
        if(EmptyUtils.isEmpty(nisDemandVO) && !EmptyUtils.isEmpty(btBankAccDTO) && "1".equals(btBankAccDTO.getIsDemand())){
            j.setSuccess(false);
        }else{
            j.setSuccess(true);
        }
        return j;
    }

    @LogMessage(content = "查询资金需求单列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listQuery")
    public String listQuery() {
        return "netbank/listQueryNisDemand";
    }

    @PostMapping("/datatableQuery")
    @ResponseBody
    public DataTablesOutput datatableQuery(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = nisDemandService.getDataTablesUnionQuery(dataTablesInput, this.getCurrentUser());
        return out;
    }

    /**
     * 批量选择付款账号
     *
     * @return
     */
    @GetMapping("/toSetSuperAcc/{corpId}")
    public String toSetPayerAcc(@PathVariable("corpId") String corpId, Model model) {
        model.addAttribute("corpId", corpId);
        return "netbank/batchSetSuperAcc";
    }

    /**
     * 批量选择付款账号
     *
     * @return
     */
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSetSuperAcc")
    @ResponseBody
    public AjaxJson batchSetSuperAcc(NisDemandVO nisDemandVO) {
        nisDemandService.batchSetSuperAcc(nisDemandVO, this.getCurrentUser());
        return success();
    }


}
