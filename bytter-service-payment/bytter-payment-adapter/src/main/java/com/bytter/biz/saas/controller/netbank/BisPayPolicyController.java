package com.bytter.biz.saas.controller.netbank;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.entity.BisPayPolicyPayee;
import com.bytter.biz.netbank.entity.BisPayPolicySort;
import com.bytter.biz.netbank.service.BisPayPolicyService;
import com.bytter.biz.netbank.service.BisPayPolicySortService;
import com.bytter.biz.netbank.vo.BisPayPolicyDetailVO;
import com.bytter.biz.netbank.vo.BisPayPolicySortVo;
import com.bytter.biz.netbank.vo.BisPayPolicyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 自动支付策略表
 * @date 2020-04-17 14:38:52
 */
@Api(value = "自动支付策略", tags = "自动支付策略")
@RestController("v1BisPayPolicyController")
@RequestMapping("/v1-payment/netbank/bispaypolicy")
public class BisPayPolicyController extends BaseController {
    @Autowired
    private BisPayPolicyService bisPayPolicyService;
    @Autowired
    private BisPayPolicySortService bisPayPolicySortService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 自动支付策略表 table列表
     *
     * @return 出参
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("自动支付策略表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BisPayPolicyVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Map<String, Object> vo = dataTablesInput.getParams();
        if (!vo.isEmpty()) {
            if (vo.get("curId") != null) {
                input.addFormData(MatchTypeEnum.EQ, "curId", vo.get("curId"), String.class);
            }
            if (vo.get("busTypeName") != null) {
                input.addFormData(MatchTypeEnum.EQ, "busTypeName", vo.get("busTypeName"), String.class);
            }
            if (vo.get("corpId") != null) {
                input.addFormData(MatchTypeEnum.EQ, "corpId", vo.get("corpId"), String.class);
            }
            if (vo.get("noPayeeName") != null) {
                input.addFormData(MatchTypeEnum.EQ, "noPayeeName", vo.get("noPayeeName"), String.class);
            }
            if (vo.get("payeeName") != null) {
                input.addFormData(MatchTypeEnum.EQ, "payeeName", vo.get("payeeName"), String.class);
            }
            if (vo.get("shareBusTypeName") != null) {
                input.addFormData(MatchTypeEnum.EQ, "shareBusTypeName", vo.get("shareBusTypeName"), String.class);
            }
        }
        return AjaxJson.success(bisPayPolicyService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * @return
     */
    @ApiOperation("添加页面信息")
    @GetMapping("/add")
    public AjaxJson<BisPayPolicyDetailVO> add() {
        BisPayPolicyDetailVO result = new BisPayPolicyDetailVO();
        result.setSort(setModelAddSort(null));
        result.setAutoPayerAccNum(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_AUTOPAYERACCNUM).getValue());
        return AjaxJson.success(result);
    }

    /**
     * @param id
     * @return
     */
    @ApiOperation("修改的页面信息")
    @GetMapping("/update/{id}")
    public AjaxJson<BisPayPolicyDetailVO> update(@PathVariable("id") String id) {
        BisPayPolicyDetailVO result = new BisPayPolicyDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            BisPayPolicyVO bisPayPolicy = bisPayPolicyService.getByUnion(id);
            result.setBisPayPolicyPage(bisPayPolicy);
        }
        result.setSort(setModelAddSort(id));
        result.setAutoPayerAccNum(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_AUTOPAYERACCNUM).getValue());
        return AjaxJson.success(result);
    }

    /**
     * 自动支付策略表详情页
     *
     * @param id
     * @return
     */
    @ApiOperation("获取自动支付策略表详情页")
    @LogMessage(content = "查询自动支付策略表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BisPayPolicyDetailVO> view(@ApiParam("id") @PathVariable("id") String id) {
        BisPayPolicyDetailVO result = new BisPayPolicyDetailVO();
        BisPayPolicyVO bisPayPolicy = bisPayPolicyService.getByUnion(id);
        bisPayPolicy.setValidSignName(dictService.get("sf_yn", bisPayPolicy.getValidSign()));
        bisPayPolicy.setIsWorkDayName(dictService.get("sf_yn", bisPayPolicy.getIsWorkDay()));
        List<BisPayPolicySortVo> sort = setModelAddSort(id);
        result.setBisPayPolicyPage(bisPayPolicy);
        result.setSort(sort);
        return AjaxJson.success(result);
    }

    /**
     * 不支付收款人查看页面跳转
     *
     * @param form 表单入参
     * @return
     */
    @ApiOperation("不支付收款人查看页面详情")
    @PostMapping("/viewNoPayee")
    public AjaxJson<List<BisPayPolicyPayee>> viewNoPayee(@RequestBody BisPayPolicyVO form) {
        List<BisPayPolicyPayee> noPayeeList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(form.getId())) {
            // 支付策略已保存入库的，取已保存的不支付收款人集合
            BisPayPolicyVO db = bisPayPolicyService.getByUnion(form.getId());
            noPayeeList.addAll(db.getNoPayeeList());
        }
        // 有更新操作，会覆盖已保存入库的不支付收款人数据
        if (!CollectionUtils.isEmpty(form.getNoPayeeList())) {
            // 前端不支付收款人“查看”按钮
            noPayeeList = Lists.newArrayList();
            noPayeeList.addAll(form.getNoPayeeList());
        }
        if (StringUtils.isNotEmpty(form.getIsEdit()) && CommonConstant.YES.equals(form.getIsEdit())) {
            // 支付收款人是否更新标志，0未更新，1为已更新。
            if (CollectionUtils.isEmpty(form.getNoPayeeList())) {
                // 更新标志为1，且前端传入不支付收款人信息为空，则证明用户更新为空
                noPayeeList = Lists.newArrayList();
            }
        }
        // 过滤账号与户名为空的数据
        noPayeeList = noPayeeList.stream().filter(item -> StringUtils.isNotEmpty(item.getPayeeAcc()) || StringUtils.isNotEmpty(item.getPayeeName())).collect(Collectors.toList());
        return AjaxJson.success(noPayeeList);
    }


    /**
     * 优先支付策略
     *
     * @param id 策略id
     */
    private List<BisPayPolicySortVo> setModelAddSort(String id) {
        List<BisPayPolicySort> sortList = new ArrayList<>();
        if (!StringUtil.isBlank_new(id)) {
            sortList = bisPayPolicySortService.listByPolicyId(id);
        }
        List<BisPayPolicySortVo> sort = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BisPayPolicySortVo entity = new BisPayPolicySortVo();
            entity.setPolicyType((i + 1) + "");
            entity.setPolicyTypeName(dictService.get("bisPayPolicy.policyType", entity.getPolicyType()));
            entity.setSort(0);
            for (BisPayPolicySort bisPayPolicySort : sortList) {
                if (((i + 1) + "").equals(bisPayPolicySort.getPolicyType())) {
                    BeanUtils.copyNotNullProperties(bisPayPolicySort, entity);
                }
            }
            sort.add(entity);
        }
        return sort;
    }


    /**
     * 添加自动支付策略表
     *
     * @param bisPayPolicy 表单数据
     * @return 操作结果
     */
    @ApiOperation("添加自动支付策略表")
    @LogMessage(content = "添加自动支付策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisPayPolicyVO bisPayPolicy) {
        if (this.dataCheck(bisPayPolicy)) {
            List<String> sort = null;
            List<String> policyType = null;
            if (StringUtils.isNotBlank(bisPayPolicy.getSort())) {
                String[] sorts = bisPayPolicy.getSort().split(CommonConstant.COMMA_SPLIT);
                String[] policyTypes = bisPayPolicy.getPolicyType().split(CommonConstant.COMMA_SPLIT);
                sort = new ArrayList<>(Arrays.asList(sorts));
                policyType = new ArrayList<>(Arrays.asList(policyTypes));
            }
            this.bisPayPolicyService.saveBisPayPolicy(bisPayPolicy, sort, policyType, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 修改自动支付策略表
     *
     * @param bisPayPolicy 表单数据
     * @return 操作结果
     */
    @ApiOperation("修改自动支付策略表")
    @LogMessage(content = "修改自动支付策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(BisPayPolicyVO bisPayPolicy) {
        if (this.dataCheck(bisPayPolicy)) {
            List<String> sort = null;
            List<String> policyType = null;
            if (StringUtils.isNotBlank(bisPayPolicy.getSort())) {
                String[] sorts = bisPayPolicy.getSort().split(CommonConstant.COMMA_SPLIT);
                String[] policyTypes = bisPayPolicy.getPolicyType().split(CommonConstant.COMMA_SPLIT);
                sort = new ArrayList<>(Arrays.asList(sorts));
                policyType = new ArrayList<>(Arrays.asList(policyTypes));
            }
            this.bisPayPolicyService.updateBisPayPolicy(bisPayPolicy, sort, policyType, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 数据校验
     *
     * @param bisPayPolicy 策略对象
     * @return 是否
     */
    private boolean dataCheck(BisPayPolicyVO bisPayPolicy) {
        if (StringUtils.isNotEmpty(bisPayPolicy.getId())) {
            // 编辑
            //重复编码校验
            if (null != bisPayPolicyService.validByPropertyName("policyCode", bisPayPolicy.getPolicyCode(), bisPayPolicy.getId())) {
                throw new ParameterException(NetbankMessageEnum.BISPAYPOLICY_MESSAGE_POLICYCODE_UNIQUE);
            }
            //重复名称校验
            if (null != bisPayPolicyService.validByPropertyName("policyName", bisPayPolicy.getPolicyName(), bisPayPolicy.getId())) {
                throw new ParameterException(NetbankMessageEnum.BISPAYPOLICY_MESSAGE_POLICYNAME_UNIQUE);
            }
            //同单位，同币种，同收款方不允许维护两个启用的策略
            // 因为改成单位可以多选了，这里单个单个校验
            BisPayPolicyVO temp = new BisPayPolicyVO();
            BeanUtils.copyNotNullProperties(bisPayPolicy, temp);
            if (bisPayPolicyService.validByPayee(temp)) {
                CorpDTO corpDTO = corpService.get(bisPayPolicy.getCorpId());
                throw new ParameterException(NetbankMessageEnum.BISPAYPOLICY_MESSAGE_PAYEE_SAME, corpDTO.getName());
            }
        } else {
            // 新增
            String[] corpIdArr = bisPayPolicy.getCorpId().split(CommonConstant.COMMA_SPLIT);
            for (int i = 0; i < corpIdArr.length; i++) {
                //重复编码校验
                String policyCode = bisPayPolicy.getPolicyCode() + StrUtil.fillBefore((i + 1) + "", '0', 3);
                if (null != bisPayPolicyService.validByPropertyName("policyCode", policyCode, bisPayPolicy.getId())) {
                    throw new ParameterException(NetbankMessageEnum.BISPAYPOLICY_MESSAGE_POLICYCODE_UNIQUE);
                }
                //重复名称校验
                String policyName = bisPayPolicy.getPolicyName() + StrUtil.fillBefore((i + 1) + "", '0', 3);
                if (null != bisPayPolicyService.validByPropertyName("policyName", policyName, bisPayPolicy.getId())) {
                    throw new ParameterException(NetbankMessageEnum.BISPAYPOLICY_MESSAGE_POLICYNAME_UNIQUE);
                }
                //同单位，同币种，同收款方不允许维护两个启用的策略
                // 因为改成单位可以多选了，这里单个单个校验
                BisPayPolicyVO temp = new BisPayPolicyVO();
                BeanUtils.copyNotNullProperties(bisPayPolicy, temp);
                temp.setCorpId(corpIdArr[i]);
                if (bisPayPolicyService.validByPayee(temp)) {
                    CorpDTO corpDTO = corpService.get(corpIdArr[i]);
                    throw new ParameterException(NetbankMessageEnum.BISPAYPOLICY_MESSAGE_PAYEE_SAME, corpDTO.getName());
                }
            }
        }
        return true;
    }

    /**
     * 删除自动支付策略表
     *
     * @param id 策略主键id
     * @return 操作结果
     */
    @ApiOperation("删除自动支付策略表")
    @LogMessage(content = "删除自动支付策略表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@ApiParam("id") @PathVariable("id") String id) {
        bisPayPolicyService.delBisPayPolicy(id, this.getCurrentUser());
        return success();
    }


}
