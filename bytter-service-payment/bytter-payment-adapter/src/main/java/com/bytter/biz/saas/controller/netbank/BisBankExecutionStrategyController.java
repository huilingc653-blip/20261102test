package com.bytter.biz.saas.controller.netbank;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.entity.BisBankExecutionStrategy;
import com.bytter.biz.netbank.service.BisBankExecutionStrategyService;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.netbank.vo.BisBankExecutionStrategyUpdateVO;
import com.bytter.biz.netbank.vo.BisBankExecutionStrategyVO;
import com.bytter.biz.netbank.vo.NisBusTypeRuleVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/// ///import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * 三单关联执行策略
 */
@Api(value = "/v1-payment/netbank/strategy", tags = "三单关联执行策略")
@RestController("v1BisBankExecutionStrategyController")
@RequestMapping("/v1-payment/netbank/strategy")
public class BisBankExecutionStrategyController extends BaseController {
    @Autowired
    private BisBankExecutionStrategyService bisBankExecutionStrategyService;
    @Autowired
    private NisBusTypeService nisBusTypeService;

    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 接口代码业务规则表 table列表
     *
     * @return
     */
    @ApiOperation(" 接口代码业务规则表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BisBankExecutionStrategyVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisBankExecutionStrategyService.getDataTablesUnion(input)) ;
    }

    @ApiOperation("修改三单关联执行策略页面跳转")
    @LogMessage(content = "修改三单关联执行策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/update/{id}")
    public AjaxJson<BisBankExecutionStrategyVO> update(@PathVariable("id") String id) {
        return AjaxJson.success(bisBankExecutionStrategyService.getById(id));
    }

    /**
     * 校验优先排序号是否存在
     *
     * @param bisBankExecutionStrategyVO
     * @return
     */
    @ApiOperation("校验优先排序号是否存在")
    @LogMessage(content = "优先执行排序默认值", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/executeSort")
    public AjaxJson<String> checkExecuteSort(@RequestBody BisBankExecutionStrategyVO bisBankExecutionStrategyVO) {
        boolean b;
        //当“是否存在唯一标识”为是时
        if ("1".equals(bisBankExecutionStrategyVO.getIsExistId())) {
            for (int i = 0; i < bisBankExecutionStrategyVO.getBankTypeIds().length; i++) {
                b = bisBankExecutionStrategyService.CheckxecuteSort(bisBankExecutionStrategyVO.getType(), null, bisBankExecutionStrategyVO.getBankTypeIds()[i], null, bisBankExecutionStrategyVO.getIsExistId());
                if (!b) {  //查询总数不为0
                    return AjaxJson.success("1");
                }
            }
        } else {
            for (int i = 0; i < bisBankExecutionStrategyVO.getBankTypeIds().length; i++) {
                b = bisBankExecutionStrategyService.CheckxecuteSort(bisBankExecutionStrategyVO.getType(), bisBankExecutionStrategyVO.getExecuteSort()
                        , bisBankExecutionStrategyVO.getBankTypeIds()[i], bisBankExecutionStrategyVO.getRules(), bisBankExecutionStrategyVO.getIsExistId());
                if (!b) {  //查询总数不为0
                    return AjaxJson.success("1");
                }
                b = bisBankExecutionStrategyService.CheckxecuteSort(bisBankExecutionStrategyVO.getType(), bisBankExecutionStrategyVO.getExecuteSort()
                        , bisBankExecutionStrategyVO.getBankTypeIds()[i], null, bisBankExecutionStrategyVO.getIsExistId());
                if (!b) {  //查询总数不为0
                    return AjaxJson.success("2");
                }
            }
        }
        return AjaxJson.success("0");//查询总数为0
    }

    /**
     * 校验优先排序号是否存在(编辑)
     *
     * @param bisBankExecutionStrategyVO
     * @return
     */
    @ApiOperation("校验优先排序号是否存在(编辑)")
    @LogMessage(content = "优先执行排序默认值", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/executeUpdateSort")
    public AjaxJson<String> checkExecuteUpdateSort(@RequestBody BisBankExecutionStrategyVO bisBankExecutionStrategyVO) {
        boolean b = true;
        //当“是否存在唯一标识”为是时
        if ("1".equals(bisBankExecutionStrategyVO.getIsExistId())) {
            b = bisBankExecutionStrategyService.checkExecuteUpdateSort(bisBankExecutionStrategyVO.getType(), bisBankExecutionStrategyVO.getExecuteSort(),
                    bisBankExecutionStrategyVO.getId(), bisBankExecutionStrategyVO.getBankTypeId(), null, bisBankExecutionStrategyVO.getIsExistId());
            if (!b) {  //查询总数不为0
                return AjaxJson.success("1");
            }
        } else {
            b = bisBankExecutionStrategyService.checkExecuteUpdateSort(bisBankExecutionStrategyVO.getType(), bisBankExecutionStrategyVO.getExecuteSort(),
                    bisBankExecutionStrategyVO.getId(), bisBankExecutionStrategyVO.getBankTypeId(), bisBankExecutionStrategyVO.getRules(), bisBankExecutionStrategyVO.getIsExistId());
            if (!b) {  //查询总数不为0
                return AjaxJson.success("1");
            }
            b = bisBankExecutionStrategyService.checkExecuteUpdateSort(bisBankExecutionStrategyVO.getType(), bisBankExecutionStrategyVO.getExecuteSort(),
                    bisBankExecutionStrategyVO.getId(), bisBankExecutionStrategyVO.getBankTypeId(), null, bisBankExecutionStrategyVO.getIsExistId());
            if (!b) {  //查询总数不为0
                return AjaxJson.success("2");
            }
        }
        return AjaxJson.success("0");//查询总数不为0，系统已存在该排序
    }

    /**
     * 接口代码业务规则表 table列表
     *
     * @return
     */
    @ApiOperation("退款勾对执行策略 table列表")
    @PostMapping("/datatableRefund")
    public AjaxJson<DataTablesOutput<BisBankExecutionStrategyVO>> dataTableRefund(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisBankExecutionStrategyService.getDataTablesUnionRefund(input));
    }


    @ApiOperation("修改三单关联执行策略表")
    @LogMessage(content = "修改三单关联执行策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/updateRefund/{id}")
    public AjaxJson<BisBankExecutionStrategyUpdateVO> updateRefund(@PathVariable("id") String id) {
        BisBankExecutionStrategyUpdateVO result = new BisBankExecutionStrategyUpdateVO();
        result.setBisBankExecutionStrategy(bisBankExecutionStrategyService.getById(id));
        List<NisBusTypeRuleVO> nisBusTypeRuleVOList = nisBusTypeService.getByByBusId(id);
        int length = nisBusTypeRuleVOList.size();
        result.setLength(length);
        result.setNisBusTypeRuleVOList(nisBusTypeRuleVOList);
        return AjaxJson.success(result);
    }


    /**
     * 添加三单关联执行策略表
     *
     * @param bisBankExecutionStrategy
     * @return
     */
    @ApiOperation("添加三单关联执行策略表")
    @LogMessage(content = "添加三单关联执行策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody BisBankExecutionStrategy bisBankExecutionStrategy) {
        bisBankExecutionStrategyService.saveBisBankExecutionStrategy(bisBankExecutionStrategy, getCurrentUser());
        return success();
    }

    /**
     * 启用标志
     *
     * @param id
     * @return
     */
    @ApiOperation("修改启用标志")
    @LogMessage(content = "修改启用标志", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/updateIsEnable/{id}")
    public AjaxJson updateIsEnable(@PathVariable("id") String id) {
        BisBankExecutionStrategy bankExecutionStrategy = bisBankExecutionStrategyService.queryBisBankExecutionStrategy(id);
        if (bankExecutionStrategy.getIsEnable().equals(CommonConstant.VALID_SIGN_NO)) {
            bankExecutionStrategy.setIsEnable(CommonConstant.VALID_SIGN_YES);
        } else if (bankExecutionStrategy.getIsEnable().equals(CommonConstant.VALID_SIGN_YES)) {
            bankExecutionStrategy.setIsEnable(CommonConstant.VALID_SIGN_NO);
        }
        //设置操作类型
        bankExecutionStrategy.setOpType("updateIsEnable");
        bisBankExecutionStrategyService.modify(bankExecutionStrategy);
        return success();
    }


    /**
     * 修改三单关联执行策略表
     *
     * @param bisBankExecutionStrategy
     * @return
     */
    @ApiOperation("修改三单关联执行策略表")
    @LogMessage(content = "添加三单关联执行策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody  BisBankExecutionStrategy bisBankExecutionStrategy) {
        String[] rules = bisBankExecutionStrategy.getRules().split(CommonConstant.COMMA_SPLIT);
        StringBuilder rulesName = new StringBuilder();
        for (int i = 0; i < rules.length; i++) {
            if (i == 0) {
                rulesName.append(dictService.get("btPurpose.receiptRules", rules[i]));
            } else {
                rulesName.append("+" + dictService.get("btPurpose.receiptRules", rules[i]));
            }
        }
        bisBankExecutionStrategy.setRulesName(rulesName.toString());
        bisBankExecutionStrategy.setUpdateBy(this.getCurrentUser().getLoginName());
        bisBankExecutionStrategy.setUpdateDate(DateUtils.getDate());
        if ("2".equals(bisBankExecutionStrategy.getType()) && ObjectUtil.isEmpty(bisBankExecutionStrategy.getIsExistId())) {
            bisBankExecutionStrategy.setIsExistId("0");
        }
        bisBankExecutionStrategyService.modify(bisBankExecutionStrategy);
        return success();
    }


    /**
     * 删除三单关联执行策略表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除三单关联执行策略表")
    @LogMessage(content = "删除自定义付款方式表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        bisBankExecutionStrategyService.delete(id);
        return success();
    }

    /**
     * 批量删除三单关联执行策略表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除自定义付款方式表")
    @LogMessage(content = "批量删除自定义付款方式表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        this.bisBankExecutionStrategyService.batchDelete(ids);
        return success();
    }


}
