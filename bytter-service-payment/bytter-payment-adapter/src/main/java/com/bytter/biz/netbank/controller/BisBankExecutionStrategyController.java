package com.bytter.biz.netbank.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.entity.BisBankExecutionStrategy;
import com.bytter.biz.netbank.service.BisBankExecutionStrategyService;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.netbank.vo.BisBankExecutionStrategyVO;
import com.bytter.biz.netbank.vo.NisBusTypeRuleVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * 三单关联执行策略
 */
@Controller
@RequestMapping("/netbank/strategy")
public class BisBankExecutionStrategyController extends BaseController {
    @Autowired
    private BisBankExecutionStrategyService bisBankExecutionStrategyService;
    @Autowired
    private NisBusTypeService nisBusTypeService;

    @DubboReference(check = false)
	private IDictService dictService;
    @LogMessage(content = "查询三单关联执行策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "netbank/listBisBankExecutionStrategy";
    }
    /**
     * 接口代码业务规则表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return bisBankExecutionStrategyService.getDataTablesUnion(dataTablesInput);
    }
    @LogMessage(content = "新增三单关联执行策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public String add() {
        return "netbank/addBisBankExecutionStrategy";
    }
    /**
     * 添加三单关联执行策略表
     * @param bisBankExecutionStrategy
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_BISBANKEXECUTIONSTRATEGY_APPLY)
    @LogMessage(content = "添加三单关联执行策略表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisBankExecutionStrategy bisBankExecutionStrategy){
        bisBankExecutionStrategyService.saveBisBankExecutionStrategy(bisBankExecutionStrategy,getCurrentUser());
        return success();
    }
    /**
     * 启用标志
     * @param id
     * @return
     */
    @LogMessage(content = "修改启用标志",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/updateIsEnable/{id}")
    @ResponseBody
    public AjaxJson updateIsEnable(@PathVariable("id") String id){
        BisBankExecutionStrategy bankExecutionStrategy = bisBankExecutionStrategyService.queryBisBankExecutionStrategy(id);
        if(bankExecutionStrategy.getIsEnable().equals(CommonConstant.VALID_SIGN_NO)){
            bankExecutionStrategy.setIsEnable(CommonConstant.VALID_SIGN_YES);
        }else if(bankExecutionStrategy.getIsEnable().equals(CommonConstant.VALID_SIGN_YES)){
            bankExecutionStrategy.setIsEnable(CommonConstant.VALID_SIGN_NO);
        }
        //设置操作类型
        bankExecutionStrategy.setOpType("updateIsEnable");
        bisBankExecutionStrategyService.modify(bankExecutionStrategy);
        return success();
    }
    @LogMessage(content = "修改三单关联执行策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        BisBankExecutionStrategyVO bisBankExecutionStrategyVO = bisBankExecutionStrategyService.getById(id);
        model.addAttribute("bisBankExecutionStrategy",bisBankExecutionStrategyVO);
        return "netbank/updateBisBankExecutionStrategy";
    }
    /**
     * 修改三单关联执行策略表
     * @param bisBankExecutionStrategy
     * @return
     */
    @LogMessage(content = "添加三单关联执行策略表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(BisBankExecutionStrategy bisBankExecutionStrategy){
        String[] rules=bisBankExecutionStrategy.getRules().split(CommonConstant.COMMA_SPLIT);
        StringBuilder rulesName = new StringBuilder();
        for (int i = 0; i < rules.length; i++) {
            if(StringUtils.isNotEmpty(rulesName)){
                rulesName.append("+");
            }
            rulesName.append(dictService.get("btPurpose.receiptRules" , rules[i]));
        }
        bisBankExecutionStrategy.setRulesName(rulesName.toString());
        bisBankExecutionStrategy.setUpdateBy(this.getCurrentUser().getLoginName());
        bisBankExecutionStrategy.setUpdateDate(DateUtils.getDate());
        if ("2".equals(bisBankExecutionStrategy.getType()) && ObjectUtil.isEmpty(bisBankExecutionStrategy.getIsExistId())){
            bisBankExecutionStrategy.setIsExistId("0");
        }
        bisBankExecutionStrategyService.modify(bisBankExecutionStrategy);
        return success();
    }

    /**
     * 校验优先排序号是否存在
     * @param bisBankExecutionStrategyVO
     * @return
     */
    @LogMessage(content = "优先执行排序默认值",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/executeSort")
    @ResponseBody
    public String checkExecuteSort(@RequestBody BisBankExecutionStrategyVO bisBankExecutionStrategyVO){
        boolean b = true;
        //当“是否存在唯一标识”为是时
        if("1".equals(bisBankExecutionStrategyVO.getIsExistId())){
            for(int i=0;i<bisBankExecutionStrategyVO.getBankTypeIds().length;i++){
                b = bisBankExecutionStrategyService.CheckxecuteSort(bisBankExecutionStrategyVO.getType(),null,bisBankExecutionStrategyVO.getBankTypeIds()[i],null,bisBankExecutionStrategyVO.getIsExistId());
                if(!b){  //查询总数不为0
                    return "1";
                }
            }
        }else{
            for(int i=0;i<bisBankExecutionStrategyVO.getBankTypeIds().length;i++){
                b = bisBankExecutionStrategyService.CheckxecuteSort(bisBankExecutionStrategyVO.getType(),bisBankExecutionStrategyVO.getExecuteSort()
                        ,bisBankExecutionStrategyVO.getBankTypeIds()[i],bisBankExecutionStrategyVO.getRules(),bisBankExecutionStrategyVO.getIsExistId());
                if(!b){  //查询总数不为0
                    return "1";
                }
                b = bisBankExecutionStrategyService.CheckxecuteSort(bisBankExecutionStrategyVO.getType(),bisBankExecutionStrategyVO.getExecuteSort()
                        ,bisBankExecutionStrategyVO.getBankTypeIds()[i],null,bisBankExecutionStrategyVO.getIsExistId());
                if(!b){  //查询总数不为0
                    return "2";
                }
            }
        }
        return "0";//查询总数为0
    }

    /**
     * 校验优先排序号是否存在(编辑)
     * @param bisBankExecutionStrategyVO
     * @return
     */
    @LogMessage(content = "优先执行排序默认值",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/executeUpdateSort")
    @ResponseBody
    public String checkExecuteUpdateSort(@RequestBody BisBankExecutionStrategyVO bisBankExecutionStrategyVO){
        boolean b = true;
        //当“是否存在唯一标识”为是时
        if("1".equals(bisBankExecutionStrategyVO.getIsExistId())){
            b = bisBankExecutionStrategyService.checkExecuteUpdateSort(bisBankExecutionStrategyVO.getType(),bisBankExecutionStrategyVO.getExecuteSort(),
                    bisBankExecutionStrategyVO.getId(),bisBankExecutionStrategyVO.getBankTypeId(),null, bisBankExecutionStrategyVO.getIsExistId());
            if(!b){  //查询总数不为0
                return "1";
            }
        }else{
            b = bisBankExecutionStrategyService.checkExecuteUpdateSort(bisBankExecutionStrategyVO.getType(),bisBankExecutionStrategyVO.getExecuteSort(),
                    bisBankExecutionStrategyVO.getId(),bisBankExecutionStrategyVO.getBankTypeId(),bisBankExecutionStrategyVO.getRules(), bisBankExecutionStrategyVO.getIsExistId());
            if(!b){  //查询总数不为0
                return "1";
            }
            b = bisBankExecutionStrategyService.checkExecuteUpdateSort(bisBankExecutionStrategyVO.getType(),bisBankExecutionStrategyVO.getExecuteSort(),
                    bisBankExecutionStrategyVO.getId(),bisBankExecutionStrategyVO.getBankTypeId(),null, bisBankExecutionStrategyVO.getIsExistId());
            if(!b){  //查询总数不为0
                return "2";
            }
        }
        return "0";//查询总数不为0，系统已存在该排序
    }

    /**
     * 删除三单关联执行策略表
     * @param id
     * @return
     */
    @LogMessage(content = "删除自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        bisBankExecutionStrategyService.delete(id);
        return success();
    }
    /**
     * 批量删除三单关联执行策略表
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        this.bisBankExecutionStrategyService.batchDelete(ids);
        return success();
    }

    @LogMessage(content = "查询退款勾对执行策略", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRefund")
    public String listRefund() {
        return "netbank/listRefundStrategy";
    }

    /**
     * 接口代码业务规则表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableRefund")
    @ResponseBody
    public DataTablesOutput dataTableRefund(@RequestBody DataTablesInput dataTablesInput){
        return bisBankExecutionStrategyService.getDataTablesUnionRefund(dataTablesInput);
    }

    @LogMessage(content = "新增退款勾对执行策略", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addRefund")
    public String addRefund() {
        return "netbank/addRefundStrategy";
    }

    @LogMessage(content = "修改三单关联执行策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/updateRefund/{id}")
    public String updateRefund(@PathVariable("id") String id, Model model) {
        BisBankExecutionStrategyVO bisBankExecutionStrategyVO = bisBankExecutionStrategyService.getById(id);
        List<NisBusTypeRuleVO> nisBusTypeRuleVOList =  nisBusTypeService.getByByBusId(bisBankExecutionStrategyVO.getId());
        int length = nisBusTypeRuleVOList.size();
        model.addAttribute("length", length);
        model.addAttribute("nisBusTypeRuleVOList", nisBusTypeRuleVOList);
        model.addAttribute("bisBankExecutionStrategy",bisBankExecutionStrategyVO);
        return "netbank/updateRefundStrategy";
    }

}
