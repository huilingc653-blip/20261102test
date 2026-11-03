package com.bytter.bill.web.saas.controller.bill;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillBankAutoDtlService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillPayAndReceVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.bill.domain.bill.vo.GuaContractBillVO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.google.common.base.Joiner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 票据从表信息
 * @date 2017-05-31 15:25:10
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abvoucherinfoVice", tags = "票据从表信息")
@RestController("v1AbVoucherinfoViceController")
@RequestMapping("/v1-preposebill/bill/accebill/abvoucherinfoVice")
public class AbVoucherinfoViceController extends BaseController {
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbBillBankAutoDtlService abBillBankAutoDtlService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;

    /**
     * 票据详细信息查看
     *
     * @return 前端路径
     */
    @ApiOperation("票据详细信息查看")
    @PostMapping("/accebillInfoPage")
    public AjaxJson<Map<String, Object>> accebillInfoPage(@RequestBody AbVoucherinfoViceVO reVo) {
        Map<String, Object> model = new HashMap<>();
        if ("1".equals(reVo.getIsEmitBill())) {
//			reVo.setIsPayed("0");
            reVo.setEndorse(null);
        } else {
            reVo.setPayee(null);
            List<String> bifCodeList = abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISPOOLENDORSE);
            String bifCodes = null;
            if (bifCodeList != null && !bifCodeList.isEmpty()) {
                bifCodes = Joiner.on(",").join(bifCodeList);
            }
            model.put("bifCodes", bifCodes);
        }
        model.put("params", reVo);
        return AjaxJson.success(model);
    }


    /**
     * 单位持有应收票据信息
     *
     * @return 前端路径
     */
    @ApiOperation("单位持有应收票据信息")
    @PostMapping("/accebillInfoReceivePage")
    public AjaxJson<Map<String, Object>> accebillInfoReceivePage(@RequestBody AbVoucherinfoViceVO reVo) {
        Map<String, Object> model = new HashMap<>();
        reVo.setPayee(null);
        List<String> bifCodeList = abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISPOOLENDORSE);
        String bifCodes = null;
        if (bifCodeList != null && !bifCodeList.isEmpty()) {
            bifCodes = Joiner.on(",").join(bifCodeList);
        }
        model.put("bifCodes", bifCodes);
        model.put("params", reVo);
        if (StringUtils.isNotEmpty(reVo.getPayId())) {
            NisBillheadDTO nisBillheadDto = nisBillheadService.getNisBillheadById(reVo.getPayId());
            model.put("nisBillhead", nisBillheadDto);
        }
        return AjaxJson.success(model);
    }

    /**
     * 列表显示票据信息
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("列表显示票据信息")
    @PostMapping("/accebillInfoDatatable")
    public AjaxJson<DataTablesOutput<AbVoucherinfoViceVO>> accebillInfoDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abVoucherinfoViceService.listAccebillInfoDataTable(input));
    }

    /**
     * 票据详细信息查看
     *
     * @return
     */
    @ApiOperation("票据详细信息查看")
    @PostMapping("/accebillViewPage")
    public AjaxJson<Map<String, Object>> accebillViewPage(@RequestBody AbVoucherinfoViceVO reVo) {
        Map<String, Object> model = new HashMap<>();
        List<AbVoucherinfoViceVO> infoList = abVoucherinfoViceService.getAccebillDataViewList(reVo);
        model.put("infoList", infoList);
        model.put("isEmitBill", reVo.getIsEmitBill());
        //showPayMoney控制页面是否展示支付金额
        model.put("showPayMoney", (StringUtils.isBlank(reVo.getPayId()) ? "0" : "1"));
        return AjaxJson.success(model);
    }

    /**
     * 票据详细信息查看
     *
     * @return
     */
    @ApiOperation("票据详细信息查看")
    @PostMapping("/accebillDataView")
    public AjaxJson<DataTablesOutput<AbVoucherinfoViceVO>> accebillDataView(@RequestBody AbVoucherinfoViceVO reVo) {
        DataTablesOutput<AbVoucherinfoViceVO> dataTablesOutput = new DataTablesOutput<>();
        if (StrUtil.isNotEmpty(reVo.getId())) {
            List<AbVoucherinfoViceVO> infoList = abVoucherinfoViceService.getAccebillDataViewList(reVo);
            if (CollectionUtil.isNotEmpty(infoList)) {
                dataTablesOutput.setData(infoList);
                dataTablesOutput.setRecordsTotal(infoList.size());
            } else {
                dataTablesOutput.setData(new ArrayList());
            }
        } else {
            dataTablesOutput.setData(new ArrayList());
        }
        return AjaxJson.success(dataTablesOutput);
    }


    /**
     * 根据单位ID和业务类型判断是否挂预算
     *
     * @param corpId
     * @return
     */
    @ApiOperation("根据单位ID和业务类型判断是否挂预算")
    @PostMapping("/isBudget")
    public AjaxJson isBudget(String corpId, String briefId) {
        boolean isBudget = abVoucherinfoViceService.isBudget(corpId, briefId);
        AjaxJson json = new AjaxJson();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("isBudget", isBudget);
        json.setAttributes(attributes);
        json.setSuccess(true);
        return json;
    }

    /**
     * 是否启用线上清算
     *
     * @return
     */
    @ApiOperation("是否启用线上清算")
    @PostMapping("/isEOnlinePay")
    public AjaxJson isEOnlinePay() {
        AjaxJson json = new AjaxJson();
        boolean isEOnlinePay = abVoucherinfoViceService.isEOnlinePay();
        json.setSuccess(isEOnlinePay);
        return json;
    }

    /**
     * 查询未挂接担保数据
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("查询未挂接担保数据")
    @PostMapping("/guaranteeDatatable")
    public AjaxJson<DataTablesOutput<AbBillPayAndReceVO>> guaranteeDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abVoucherinfoViceService.listGuaranteeDataTable(input));
    }

    /**
     * 获取挂接担保合同的票据数据
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ApiOperation("获取挂接担保合同的票据数据")
    @PostMapping("/getGuaContract")
    public AjaxJson<DataTablesOutput<GuaContractBillVO>> getUseChangelist(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abVoucherinfoViceService.getGuaForBill(input, getCurrentUser()));
    }
}
