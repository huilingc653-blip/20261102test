package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexCorpPoolTranService;
import com.bytter.biz.foreign.vo.FexCorpPoolTranVO;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 资金池交易信息
 * @date 2020-02-21 14:31:56
 */
@Controller
@RequestMapping("/foreign/fexcorppooltran")
public class FexCorpPoolTranController extends BaseController {

    @Autowired
    private FexCorpPoolTranService fexCorpPoolTranService;

    /**
     * 资金池交易列表
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCORPPOOLTRAN_APPLY)
    @LogMessage(content = "查询资金池交易列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String listSend(Model model) {
        return "foreign/listFexCorpPoolTran";
    }

    /**
     * 资金池交易表 table列表
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCORPPOOLTRAN_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexCorpPoolTranService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 资金池交易表新增页面跳转
     * @param model
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCORPPOOLTRAN_APPLY)
    @GetMapping("/add")
    public String add(Model model) {
        return "foreign/addFexCorpPoolTran";
    }

    /**
     * 保存资金池交易表
     * @param fexCorpPoolTranVO
     * @return
     */
    @LogMessage(content = "添加资金池交易表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson savePool(FexCorpPoolTranVO fexCorpPoolTranVO) {
        fexCorpPoolTranService.save(fexCorpPoolTranVO,this.getCurrentUser());
        return success();
    }

    /**
     * 资金池交易表表修改页面跳转
     * @param fexCorpPoolTranVO
     * @param model
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCORPPOOLTRAN_APPLY)
    @GetMapping("/update")
    public String updatePool(FexCorpPoolTranVO fexCorpPoolTranVO, Model model) {
        FexCorpPoolTranVO tranInfo = null;
        if(!EmptyUtils.isEmpty(fexCorpPoolTranVO.getPoolId()) && !EmptyUtils.isEmpty(fexCorpPoolTranVO.getBankaccId()) && !EmptyUtils.isEmpty(fexCorpPoolTranVO.getCurId())){
            tranInfo = fexCorpPoolTranService.getTranInfoByPoolIdAndBankAccId(fexCorpPoolTranVO.getPoolId(), fexCorpPoolTranVO.getBankaccId(),fexCorpPoolTranVO.getCurId());
        }
        model.addAttribute("fexCorpPoolTranPage", tranInfo);
        return "foreign/updateFexCorpPoolTran";
    }

    /**
     * 修改资金池交易表
     * @param fexCorpPoolTranVO
     * @param model
     * @return
     */
    @LogMessage(content = "资金池交易表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modifyPool(FexCorpPoolTranVO fexCorpPoolTranVO, Model model) {
        fexCorpPoolTranService.modify(fexCorpPoolTranVO, this.getCurrentUser());
        return success();
    }

    /**
     * 外汇资金池表详情页
     * @param poolId 外汇资金池id
     * @param bankAccId 外汇资金池账户表id
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCORPPOOLTRAN_APPLY)
    @LogMessage(content = "查询资金池交易表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{poolId}/{bankAccId}")
    public String viewPool(@PathVariable("poolId") String poolId, @PathVariable("bankAccId") String bankAccId, Model model) {
        if (StringUtils.isNotEmpty(poolId)) {
            FexCorpPoolTranVO vo = fexCorpPoolTranService.getFexCorpPoolTranVOByPoolId(poolId,bankAccId);
            model.addAttribute("fexCorpPoolTranPage", vo);
        }
        return "foreign/viewFexCorpPoolTran";
    }

    /**
     * 删除外汇资金池信息
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除资金池交易信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        fexCorpPoolTranService.delete(id);
        return success();
    }

    /**
     * 根据外汇资金池主体id和账号id查找交易信息
     * @param poolId
     * @param bankaccId
     * @param tranSign
     * @param model
     * @return
     */
    @LogMessage(content = "外汇资金池信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/tranInfo")
    @ResponseBody
    public List<FexCorpPoolTranVO> getBankAccInfo(@RequestParam String poolId, @RequestParam String bankaccId, @RequestParam String tranSign, @RequestParam String curId, Model model) {
        FexCorpPoolTranVO tranVO = fexCorpPoolTranService.getTranInfoByPoolIdAndBankAccId(poolId, bankaccId,curId);
        if(ForeignConstant.FEX_TRAN_SIGN_0.equals(tranSign)){
            return tranVO.getTranTypeJDtl();
        }else{
            return tranVO.getTranTypeWDtl();
        }
    }

}
