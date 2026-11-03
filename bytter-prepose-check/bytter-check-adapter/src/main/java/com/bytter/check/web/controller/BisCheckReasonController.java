package com.bytter.check.web.controller;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.check.reconcile.service.BisCheckReasonService;
import com.bytter.check.reconcile.vo.BisCheckReasonVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BankTypeDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/bis/checkReason")
public class BisCheckReasonController extends BaseController{

    @Autowired
    private BisCheckReasonService bisCheckReasonService;
    @DubboReference(check = false)
    private IBankTypeService iBankTypeService;
    @DubboReference(check = false)
    private IDictService dictService;
	/**
     *
     * @return
     */
    //////@RequiresPermissions(ReceiptConstant.BIS_CHECK_REASON_QUERY)
    @GetMapping("/list")
    public String list(Model model) {
		return "reconcile/listCheckReason";
    }
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
	//////@RequiresPermissions(ReceiptConstant.BIS_CHECK_REASON_QUERY)
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput output = bisCheckReasonService.getDataTablesUnion(dataTablesInput);
        List<BisCheckReasonVO> voList= output.getData();
        if(voList.size()>0) {
            Map<String, BankTypeDTO> typeMap= iBankTypeService.getBankTypeMap();
//		(select bbt.type_Name from bt_bank_type bbt where bbt.id=br.bank_type_id) as bankTypeName
            for(BisCheckReasonVO vo:voList) {
                vo.setBankTypeName(typeMap.get(vo.getBankTypeId()).getTypeName());
                String [] appScopeS=vo.getAppScope().split(",");
                String appScopeName="";
                for(String appScope:appScopeS){
                    appScopeName+=dictService.get("BIS_CHECK_REASON_SCOPE", appScope)+",";
                }
                vo.setAppScopeName(appScopeName.substring(0,appScopeName.length()-1));
                vo.setValidSignName(dictService.get("valid_sign",vo.getValidSign()));
            }
        }
        return output;
    }
    /**
    *
    * @return
    */
    //////@RequiresPermissions(ReceiptConstant.BIS_CHECK_REASON_QUERY)
   @GetMapping("/add")
   public String add(Model model) {
       return "reconcile/addCheckReason";
   }
    /**
     *
     * @return
     */
    //////@RequiresPermissions(ReceiptConstant.BIS_CHECK_REASON_QUERY)
    @GetMapping("/edit/{id}")
    public String add(@PathVariable("id") String id, Model model) {
    	model.addAttribute("flag", true);
        model.addAttribute("checkReasonVo", bisCheckReasonService.getBisCheckReason(id));
        return "reconcile/addCheckReason";
    }
    /**
    *
    * @return
    */
    //////@RequiresPermissions(ReceiptConstant.BIS_CHECK_REASON_QUERY)
    @GetMapping("/view/{id}")
   public String edit(@PathVariable("id") String id, Model model) {
        BisCheckReasonVO vo = bisCheckReasonService.getBisCheckReason(id);
        vo.setBankTypeName(iBankTypeService.getObjectById(vo.getBankTypeId()).getTypeName());
        String [] appScopeS=vo.getAppScope().split(",");
        String appScopeName="";
        for(String appScope:appScopeS){
            appScopeName+=dictService.get("BIS_CHECK_REASON_SCOPE", appScope)+",";
        }
        vo.setAppScopeName(appScopeName.substring(0,appScopeName.length()-1));
        vo.setValidSignName(dictService.get("valid_sign", vo.getValidSign()));
        model.addAttribute("checkReasonVo", vo);
        return "reconcile/viewCheckReason";
    }
    /**
     * @param
     * @return
     */
    //////@RequiresPermissions(ReceiptConstant.BIS_CHECK_REASON_QUERY)
    @PostMapping("/save/{operator}")
    @ResponseBody
    public AjaxJson saveOrUpdate(@PathVariable("operator")String operator, BisCheckReasonVO bisCheckReasonVO) {
        return bisCheckReasonService.saveOrUpdate(operator, bisCheckReasonVO, getCurrentUser());
    }
	@PostMapping("/btDel")
	@ResponseBody
    //////@RequiresPermissions(ReceiptConstant.BIS_CHECK_REASON_QUERY)
	public AjaxJson batchDel(String ids){
		return  bisCheckReasonService.saveOrUpdate(ids,this.getCurrentUser());
	}
	/**
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<BisCheckReasonVO> getListByValidSign(String bankTypeId) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("bankTypeId",bankTypeId);
    	params.put("validSign", ReceiptConstant.BIS_BANK_VALID_SIGN_1);
        List<BisCheckReasonVO> bisCheckReasonVO = bisCheckReasonService.getListByValidSign(params);
        
        return bisCheckReasonVO;
    }
}
