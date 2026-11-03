package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.entity.RatUration;
import com.bytter.credit.domain.service.RatBkrationDisService;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.service.RatUrationService;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.*;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.dto.BtBankDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信占用结转台账表
 * @date 2017-08-19 11:36:37
 */
@RestController("v1RatUrationController")
@RequestMapping("/v1-cms/cms/rat/raturation")
public class RatUrationController extends BaseController {
    @Autowired
    private RatUrationService ratUrationService;
    @Autowired
    private RatBkrationService bkrationService;
    @Autowired
    private RatBkrationDisService disService;
    @DubboReference(check = false)
    private IBtCurrencyService curService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IInputBankInfoService bankInfoService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 获取所有授信银行信息
     *
     * @return
     */
    @GetMapping("/getRationBank")
    @ResponseBody
    public AjaxJson<List<BtBankDTO>> json() {
        List<BtBankDTO> bankDTOs = bkrationService.queryRationBank();
        return AjaxJson.success(bankDTOs);
    }


    /**
     * 授信占用台账表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<RatUrationBeanDTO>> dataTable(@RequestBody DataTablesInput input) {
        Object beginDate = input.getFormdata().get("beginDate");
        if (!EmptyUtils.isEmpty(beginDate)) {
            input.addFilter(MatchTypeEnum.LE, "beginDate", beginDate.toString(), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "endDate", beginDate.toString(), Date.class);// 小于等于授信结束日期
        }
        return AjaxJson.success(ratUrationService.queryRatURationList(input, RationConstant.CMS_RATION_CHANGE_APPLY, getUserId()));
    }

    /**
     * 功能介绍 : 跳转授信额度单选择。<br>
     * <b>日期：</b>2017年8月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @LogMessage(content = "跳转授信额度单选择页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/chooseration")
    public AjaxJson<Map<String, Object>> chooseration(String urationIds, String changeType) {
        Map<String, Object> model = new HashMap<>();
        List<RatUrationBean> urationBeans = ratUrationService.queryChangeRation(urationIds, changeType);
        model.put("urationBeans", urationBeans);
        model.put("changeType", changeType);
        boolean isShowBtn = false; // 默认不显示【确定】【取消】按钮
        if (!CollectionUtils.isEmpty(urationBeans)) {
            List<RatUrationBean> filterList = urationBeans.stream().filter(item -> !CollectionUtils.isEmpty(item.getRationDtls())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(filterList)) {
                isShowBtn = true;
            }
            model.put("isShowBtn", isShowBtn);
        }
        return AjaxJson.success(model);
    }


    /**
     * 授信占用台账表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update/{id}")
    public AjaxJson<RatUration> update(@PathVariable String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(ratUrationService.get(id));
        }
        return AjaxJson.success();
    }

    /**
     * 授信占用台账表详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信占用台账表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatUration> view(@PathVariable("id") String id) {
        return AjaxJson.success(ratUrationService.get(id));
    }

    /**
     * 授信跨年结转
     *
     * @return
     */
    @LogMessage(content = "授信跨年结转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/rationChange")
    @ResponseBody
    public AjaxJson rationChange(String urationIds, String changeType) {
        ratUrationService.changeRationCode(urationIds, changeType, getUserId());
        return success();
    }

    /**
     * 添加授信占用台账表
     *
     * @param ratUration
     * @return
     */
    //
    @LogMessage(content = "添加授信占用台账表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody  RatUration ratUration) {
        ratUrationService.save(ratUration);
        return success();
    }

    /**
     * 修改授信占用台账表
     *
     * @param ratUration
     * @return
     */
    //
    @LogMessage(content = "修改授信占用台账表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody RatUration ratUration) {
        RatUration t = ratUrationService.get(ratUration.getId());
        BeanUtils.copyNotNullProperties(ratUration, t);
        ratUrationService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除授信占用台账表
     *
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除授信占用台账表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatUration ratUration = ratUrationService.get(id);
        ratUrationService.deleteEntity(ratUration);
        return success();
    }

    /**
     * 授信选择页面
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/toRationDtl")
    public AjaxJson<Map<String,Object>> toRationDtl(UrationParamDTO paramDTO, RatUrationDTO urationDTO) {
        Map<String,Object> model=new HashMap<>();
        paramDTO.setRatCode(null);
        paramDTO.setItemCode(urationDTO.getRatItemCode());
        paramDTO.setStatus(RationConstant.STATUS_CHECK.toString());// 选择授信时只能选择审批通过状态的数据
        String bankCtlType = configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_BANK_CONTROLTYPE).getValue();// 授信银行控制
        paramDTO.setBankCtlType(bankCtlType);
        if (EmptyUtils.isNotEmpty(bankCtlType) && bankCtlType.equals(RationConstant.RAT_BANKTYPE_SAME)) {
            if (EmptyUtils.isNotEmpty(paramDTO.getBankCode())) {
                InputBankInfoDTO bankInfoDTO = bankInfoService.getByBankCode(paramDTO.getBankCode());// 根据联行号查询银行信息
                if (null != bankInfoDTO) {
                    // String bankPrefix=bankInfoDTO.getBankPrefix();
                    paramDTO.setBankPrefix(bankInfoDTO.getBankTypeId());
                }
            }
        }
        List<RatUrationDisDTO> rations = ratUrationService.queryCorpDisRation(paramDTO, urationDTO);
        if (!CollectionUtils.isEmpty(rations)) {
            for (RatUrationDisDTO dto : rations) {
                InputBankInfoDTO bankInfoDTO1 = bankInfoService.getByBankCode(dto.getBankCode());// 根据联行号查询银行信息
                if (bankInfoDTO1 != null) {
                    dto.setBankTypeName(bankInfoDTO1.getBankTypeName());
                }
            }
        }
        model.put("rations", rations);
        if (EmptyUtils.isEmptyList(rations)) {
            model.put("rationDtls", "0");// 无符合条件的授信合同
        } else {
            model.put("rationDtls", "1");
        }
        return AjaxJson.success(model);
    }

    /**
     * 查看已选授信单
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/viewRationDtl")
    public AjaxJson<Map<String,Object>> viewRationDtl(@RequestBody  RatUrationDTO urationDTO) {
        Map<String,Object> map=new HashMap<>();
        if (CommonConstant.YES.equals(urationDTO.getItemFlag())) {
            List<RatUrationDtlDTO> applyDtls = new ArrayList<>();
            RatUrationDtlDTO ratUrationDtlDTO = new RatUrationDtlDTO();
            ratUrationDtlDTO.setItemCode(urationDTO.getItemCode());
            ratUrationDtlDTO.setUseMaxMoney(urationDTO.getUseMaxMoney());
            applyDtls.add(ratUrationDtlDTO);
            urationDTO.setApplyDtls(applyDtls);
        }
        List<RatUrationDisDTO> rations = ratUrationService.viewCorpDisRation(urationDTO, urationDTO.getIsRmbRat());
        map.put("rations", rations);
        return AjaxJson.success(map);
    }

    /**
     * 银行授信额度明细查询
     *
     * @return
     */
    @LogMessage(content = "银行授信额度明细查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/rationQuery")
    public AjaxJson<Map<String,Object>> rationQuery(UrationParamDTO paramDTO) {
        Map<String,Object> model=new HashMap<>();
        // 显示已分配的单位、银行
        String corpSql = "";
        List<CorpDTO> corpDTOs = disService.queryRationCorp(corpSql);
        List<BtBankDTO> bankDTOs = bkrationService.queryRationBank();
        if (EmptyUtils.isEmpty(paramDTO.getMoneyType())) {
            paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);// 元
        }
        BtCurrencyDTO currencyDTO = curService.getNatualCurrency();// 本位币
        if (EmptyUtils.isEmpty(paramDTO.getCurCode())) {
            paramDTO.setCurCode(currencyDTO.getId());
        }
        List<RatUrationUseVO> rations = null;
        paramDTO.setStatus(RationConstant.STATUS_CHECK + "," + RationConstant.STATUS_INVALID);// 授信额度明细表可查已作废的数据
        // 显示单位
        List<String> corpAuth = corpService.getUserCorpIds(RationConstant.CMS_RATION_RATIONQUERY_QUERY,
                this.getCurrentUser().getId());
        String corpId = paramDTO.getCorpId();// 保存每次查询的单位
        if (StringUtils.isEmpty(paramDTO.getCorpId())) {// 未选择查询的单位则默认查询当前用户下所有单位的
            String tempStr = StringUtils.listTtoString(corpAuth);
            paramDTO.setCorpId(tempStr);
        }
        rations = ratUrationService.queryRationUseDtl(paramDTO);
        String bankSign = RationConstant.RAT_COMMON_N;
        if (!EmptyUtils.isEmptyList(bankDTOs)) {
            bankSign = RationConstant.RAT_COMMON_Y;
        }
        model.put("corpId", corpId);
        model.put("currencyDTO", rations);
        model.put("rations", rations);
        model.put("corpDTOs", corpDTOs);
        model.put("bankDTOs", bankDTOs);
        model.put("bankSign", bankSign);
        model.put("paramDTO", paramDTO);
        model.put("naturalCur", currencyDTO.getId());
        model.put("relateCon", bkrationService.getRelateCon());
        return AjaxJson.success(model);
    }

    /**
     * 查看授信占用情况
     *
     * @return
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewRatioUse")
    public AjaxJson<Map<String,Object>> viewRatioUse(RatUrationQueryVO queryVO ) {
        Map<String,Object> model=new HashMap<>();
        List<RatUrationUseBeanVO> totalrations = ratUrationService.queryCorpRationUseDtl(queryVO);
        List<RatUrationUseBeanVO> rations = new ArrayList<RatUrationUseBeanVO>();
        // 在占用明细页面,隐藏掉占用金额为0的数据
        for (RatUrationUseBeanVO rat : totalrations) {
            if ((!EmptyUtils.isEmpty(rat.getConvertPreMoney())
                    && rat.getConvertPreMoney().compareTo(BigDecimal.ZERO) > 0)
                    || (!EmptyUtils.isEmpty(rat.getBillMoney()) && rat.getBillMoney().compareTo(BigDecimal.ZERO) > 0)) {
                rations.add(rat);
            }
        }
        fillCurrencyNames(rations);
        model.put("rations", rations);
        model.put("queryVO", queryVO);
        return AjaxJson.success(model);
    }

    /**
     * 银行授信额度明细查询页上传合同按钮页面跳转
     *
     * @param ratCode 授信录入主键id
     * @return 跳转页面
     */
    @LogMessage(content = "授信额度录入上传合同", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/uploadAttachFile/{ratCode}")
    public AjaxJson<Map<String,Object>> uploadAttachFile(@PathVariable("ratCode") String ratCode) {
        Map<String,Object> model=new HashMap<>();
        RatBkrationPageVO ratBkration = bkrationService.getBkRationBeanById(ratCode);
        if (StringUtils.isNotEmpty(ratBkration.getOldRatCode())) {
            RatBkration oldBkration = bkrationService.get(ratBkration.getOldRatCode(), true);
            ratBkration.setOldRatCode(oldBkration.getRatCode());
        }
        model.put("ratBkrationPage", ratBkration);
        ConfigDTO bailConfig = bkrationService.getIsHangBail();// 参数设置是否挂接保证金
        model.put("isHangBail", bailConfig.getValue());
        ConfigDTO guaConfig = bkrationService.getMountGuarantee();// 参数设置是否挂接担保
        model.put("isGuarantee", guaConfig.getValue());
        model.put("relateCon", bkrationService.getRelateCon());
        model.put("conType", bkrationService.getRatConType());
        model.put("conId", ratBkration.getConId());
        return AjaxJson.success(model);
    }

    /**
     * 保存上传合同附件
     *
     * @param rationtDTO 入参对象
     * @return 出参对象
     */
    @LogMessage(content = "保存上传合同附件", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveAttachFile")
    @ResponseBody
    public AjaxJson saveAttachFile(@RequestBody  RatBkRationVm rationtDTO) {
        RatBkration ratBkration = bkrationService.get(rationtDTO.getId(), true);
        if (!ObjectUtils.isEmpty(ratBkration)) {
            // 保存上传合同附件
            ratBkration.setContractAttachfilepath(rationtDTO.getContractAttachfilepath());
            ratBkration.setUpdateBy(getUserId());
            ratBkration.setUpdateDate(DateUtils.getDate());
            bkrationService.saveOrUpdate(ratBkration);
        }
        return success();
    }



    /**
     * 银行授信业务品种明细数据明细查询
     *
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "银行授信业务品种明细数据明细查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/getRaturationUsedItemDetail")
    @ResponseBody
    public AjaxJson<DataTablesOutput<RatItemDisVO>> getRaturationUsedItemDetail(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<RatItemDisVO> output = ratUrationService.queryRaturationUsedItemDetail(input, getCurrentUser());
        fillItemDetailCurrencyNames(output == null ? null : output.getData());
        return AjaxJson.success(output);
    }

    /**
     * @return
     */
    @GetMapping("/getBanks")
    @ResponseBody
    public AjaxJson<List<BtBankDTO>> getBanks() {
        List<BtBankDTO> bankDTOs = bkrationService.queryRationBank();
        return AjaxJson.success(bankDTOs);
    }

    @PostMapping("/exportExcel")
    public void doExportInterestBill(@RequestBody UrationParamDTO paramDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "银行授信额度明细.xls";
        try {
            if (ServletUtils.isIE(request)) {
                fileName = URLEncoder.encode(fileName, "UTF8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
        // 显示已分配的单位、银行
        if (EmptyUtils.isEmpty(paramDTO.getMoneyType())) {
            paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);// 元
        }
        Date beginDate = paramDTO.getBeginDate();
        if (null == beginDate) {
            beginDate = DateUtils.getDate();
            paramDTO.setBeginDate(beginDate);
        }
        Date endDate = paramDTO.getEndDate();
        if (null == endDate) {
            endDate = DateUtils.getDate();
            paramDTO.setEndDate(endDate);
        }
        BtCurrencyDTO currencyDTO = curService.getNatualCurrency();// 本位币
        if (EmptyUtils.isEmpty(paramDTO.getCurCode())) {
            paramDTO.setCurCode(currencyDTO.getId());
        }
        paramDTO.setStatus(RationConstant.STATUS_CHECK + "," + RationConstant.STATUS_INVALID);// 授信额度明细表可查已作废的数据
        HSSFWorkbook workbook = ratUrationService.exportExcel(paramDTO, getCurrentUser());
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 银行授信额度占用明细导出
     *
     * @param queryVO  查询条件
     * @param request  request
     * @param response response
     */
    @PostMapping("/exportOccupyDtl")
    public void exportOccupyDtl(@RequestBody RatUrationQueryVO queryVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 导出的文件名（不要带后缀，后面统一拼接）
        String fileName = "银行授信额度占用明细";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());

        // ========= 删除旧的IE/非IE分支，删掉response.setCharacterEncoding =========
        // 二进制xls不要setCharacterEncoding！
        response.setContentType("application/vnd.ms-excel");
        // 同时携带 filename 和 filename*，适配你前端的正则解析
        response.setHeader("content-disposition",
                "attachment;filename=" + encodedFileName + ".xls;filename*=UTF-8''" + encodedFileName + ".xls");

        //导出数据
        List<RatUrationUseBeanVO> totalrations = ratUrationService.queryCorpRationUseDtl(queryVO);
        List<RatUrationUseBeanVO> rations = new ArrayList<RatUrationUseBeanVO>();
        // 隐藏掉占用金额为0的数据
        for (RatUrationUseBeanVO rat : totalrations) {
            if ((!EmptyUtils.isEmpty(rat.getConvertPreMoney())
                    && rat.getConvertPreMoney().compareTo(BigDecimal.ZERO) > 0)
                    || (!EmptyUtils.isEmpty(rat.getBillMoney()) && rat.getBillMoney().compareTo(BigDecimal.ZERO) > 0)) {
                rations.add(rat);
            }
        }
        fillCurrencyNames(rations);
        HSSFWorkbook workbook = ratUrationService.exportOccupyDtl(rations);

        //输出流，用try-with-resources自动关闭
        try (OutputStream ouputStream = response.getOutputStream()) {
            workbook.write(ouputStream);
            ouputStream.flush();
        } finally {
            workbook.close();
        }
    }


    /**
     * 品种明细补全业务币种名称：SQL 取的是 RAT_BKRATION.CUR_NAME，@Currency 注入已失效。
     */
    private void fillItemDetailCurrencyNames(List<RatItemDisVO> items) {
        if (EmptyUtils.isEmpty(items)) {
            return;
        }
        List<String> curIds = new ArrayList<>();
        for (RatItemDisVO item : items) {
            if (EmptyUtils.isNotEmpty(item.getInitCurId())) {
                curIds.add(item.getInitCurId());
            } else if (EmptyUtils.isNotEmpty(item.getInitCurCode())) {
                curIds.add(item.getInitCurCode());
            }
        }
        if (EmptyUtils.isEmpty(curIds)) {
            return;
        }
        Map<String, BtCurrencyDTO> currencyMap = curService.getCurrencyByIds(curIds);
        if (EmptyUtils.isEmpty(currencyMap)) {
            return;
        }
        for (RatItemDisVO item : items) {
            String curId = EmptyUtils.isNotEmpty(item.getInitCurId()) ? item.getInitCurId() : item.getInitCurCode();
            BtCurrencyDTO cur = currencyMap.get(curId);
            if (cur != null && EmptyUtils.isNotEmpty(cur.getCurName())) {
                item.setInitCurName(cur.getCurName());
            }
        }
    }

    /**
     * SaaS 占用明细补全币别名称：SQL 依赖 RAT_BKRATION.CUR_NAME，且原币种误用授信币种名；
     * @Currency 注入已失效，按币种 id 从币别主数据回填。
     */
    private void fillCurrencyNames(List<RatUrationUseBeanVO> rations) {
        if (EmptyUtils.isEmpty(rations)) {
            return;
        }
        List<String> curIds = new ArrayList<>();
        for (RatUrationUseBeanVO rat : rations) {
            if (EmptyUtils.isNotEmpty(rat.getCurCode())) {
                curIds.add(rat.getCurCode());
            }
            if (EmptyUtils.isNotEmpty(rat.getInitCurCode())) {
                curIds.add(rat.getInitCurCode());
            }
        }
        if (EmptyUtils.isEmpty(curIds)) {
            return;
        }
        Map<String, BtCurrencyDTO> currencyMap = curService.getCurrencyByIds(curIds);
        if (EmptyUtils.isEmpty(currencyMap)) {
            return;
        }
        for (RatUrationUseBeanVO rat : rations) {
            BtCurrencyDTO cur = currencyMap.get(rat.getCurCode());
            if (cur != null && EmptyUtils.isNotEmpty(cur.getCurName())) {
                rat.setCurName(cur.getCurName());
            }
            BtCurrencyDTO initCur = currencyMap.get(rat.getInitCurCode());
            if (initCur != null && EmptyUtils.isNotEmpty(initCur.getCurName())) {
                rat.setInitCurName(initCur.getCurName());
            }
        }
    }

    /**
     * 判断是否是IE
     *
     * @param request
     * @return
     */
    private boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0
                || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
}
