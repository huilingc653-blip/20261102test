package com.bytter.increment.saas.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.domain.entity.ChoProductNetVal;
import com.bytter.increment.domain.entity.ChoProductRenewal;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.service.*;
import com.bytter.increment.domain.vo.ChoProductRedeemDetailVO;
import com.bytter.increment.domain.vo.ChoProductReqUncollectedVo;
import com.bytter.increment.domain.vo.v1.ChoProductReqMoblieReportVO;
import com.bytter.increment.domain.vo.v1.ChoProductReqQueryViewVO;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Api(value = "理财产品查询接口",tags = "理财产品查询接口")
@RestController("v1ChoProductReqQueryController")
@RequestMapping("/v1-increment/chowmatistic/choproductreqquery")
public class ChoProductReqQueryController extends BaseController {
    @Autowired
    private ChoProductReqQueryService choProductReqQueryService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @DubboReference(check = false)
	private IDictService dictService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @Autowired
    private ChoProductRenewalService choProductRenewalService;
    @Autowired
    private ChoProductNetValService choProductNetValService;

    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        setRedeemStatus(input);
        return success(choProductReqQueryService.getDataTablesUnion(input,this.getCurrentUser()));
    }

    @ApiOperation(value = "查询理财产品选择列表", notes = "查询理财产品选择列表")
    @PostMapping("/datatableForSelect")
    @ResponseBody
    public AjaxJson<DataTablesOutput> datatableForSelect(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        setRedeemStatus(input);
        return success(choProductReqQueryService.getDataTablesUnionForSelect(input));
    }

    @ApiOperation(value = "查询理财产品详情", notes = "查询理财产品详情")
    @GetMapping("/view/{id}")
    public AjaxJson<ChoProductReqQueryViewVO> view(@PathVariable("id") String id){
        ChoProductReqQueryViewVO viewVO = new ChoProductReqQueryViewVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductReqVo vo = choProductReqService.getChoProductReqVoById(id);
            viewVO.setChoProductReqPage(vo);
            List<ChoProductRedeemDetailVO> list = choProductRedeemService.listProductRedeemDetailVOApproved(vo.getId());
            if (!CollectionUtils.isEmpty(list)) {
                viewVO.setList(list);
            }
            List<ChoProductRenewal> choProductRenewalList = choProductRenewalService.listChoProductRenewalByReqId(vo.getId());
            if (!CollectionUtils.isEmpty(choProductRenewalList)) {
                viewVO.setRenewalList(choProductRenewalList);
            }
            List<ChoProductNetVal> choProductNetValList = choProductNetValService.listByReqId(id);
            if (!CollectionUtils.isEmpty(choProductNetValList)) {
                viewVO.setNetValList(choProductNetValList);
            }
        }
        viewVO.setBusType(ChoMessageConstant.BUS_TYPE_PRODUCT_REQ);
        return success(viewVO);
    }

    @ApiOperation(value = "查询应收未收利息列表", notes = "查询应收未收利息列表")
    @PostMapping("/uncollectedDatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> uncollectedDataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choProductReqQueryService.uncollectedDataTable(input,this.getCurrentUser()));
    }

    @ApiOperation(value = "查询理财产品报表数据", notes = "查询理财产品报表数据")
    @PostMapping("/getReqMoblieList")
    @ResponseBody
    public AjaxJson<ChoProductReqMoblieReportVO> getReqMoblieList(@RequestBody ChoProductReqUncollectedVo reqVo) {
        ChoProductReqMoblieReportVO reportVO = new ChoProductReqMoblieReportVO();
        List<ChoProductReqUncollectedVo> voList = choProductReqQueryService.listReportInfo(reqVo,this.getCurrentUser());
        BigDecimal buyTotal = BigDecimal.ZERO;
        BigDecimal referIncomeTotal = BigDecimal.ZERO;
        for(ChoProductReqUncollectedVo vo : voList){
            vo.setProductType(dictService.get("cho.productType", vo.getProductType()));
            buyTotal = buyTotal.add(EmptyUtils.isEmpty(vo.getBuyMoney())?BigDecimal.ZERO:vo.getBuyMoney());
            referIncomeTotal = referIncomeTotal.add(EmptyUtils.isEmpty(vo.getReferIncomeMoney())?BigDecimal.ZERO:vo.getReferIncomeMoney());
        }
        reportVO.setDataList(voList);
        reportVO.setBuyTotal(buyTotal);
        reportVO.setReferIncomeTotal(referIncomeTotal);
        return success(reportVO);
    }

    @ApiOperation(value = "查询理财产品收益明细", notes = "查询理财产品收益明细")
    @GetMapping("/incomeMoneyView/{id}")
    public AjaxJson<ChoProductReqQueryViewVO> incomeMoneyView(@PathVariable("id") String id){
        ChoProductReqQueryViewVO viewVO = new ChoProductReqQueryViewVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductReqVo vo = choProductReqService.getChoProductReqVoById(id);
            viewVO.setChoProductReqPage(vo);
            List<ChoProductRedeemDetailVO> list = choProductRedeemService.listProductRedeemDetailVOApproved(vo.getId());
            if (!CollectionUtils.isEmpty(list)) {
                viewVO.setList(list);
            }
        }
        viewVO.setBusType(ChoMessageConstant.BUS_TYPE_PRODUCT_REQ);
        return success(viewVO);
    }

    @ApiOperation(value = "上传附件", notes = "上传附件")
    @PostMapping("/saveUpload")
    @ResponseBody
    public AjaxJson saveUpload(@RequestBody ChoProductReq choProduct){
        ChoProductReq choProductReq = choProductReqService.get(choProduct.getId());
        if(!EmptyUtils.isEmpty(choProductReq)){
            String attachmentId = choProduct.getAttachmentId();
            if(!EmptyUtils.isEmpty(attachmentId)){
                choProductReq.setAttachmentId(attachmentId);
                choProductReq.setUpdateBy(this.getCurrentUser().getLoginName());
                choProductReq.setUpdateDate(DateUtils.getDate());
                choProductReqService.saveOrUpdate(choProductReq);
            }
        }
        return success();
    }

    private void setRedeemStatus(DataTablesInput dataTablesInput) {
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String status = (String) formdata.get("eqi_status");
        String redeemStatus = "";
        if (ChoProductReqConstant.QUERY_STATUS_REDEEMTEMP.equalsIgnoreCase(status)){
            //赎回暂存
            status = ChoProductReqConstant.QUERY_STATUS_CHECK;
            redeemStatus = ChoProductReqConstant.QUERY_STATUS_TEMP;
        }else if (ChoProductReqConstant.QUERY_STATUS_REDEEMWAITCHECK.equalsIgnoreCase(status)){
            //赎回审批中
            status = ChoProductReqConstant.QUERY_STATUS_CHECK;
            redeemStatus = ChoProductReqConstant.QUERY_STATUS_WAIT_CHECK;
        }else if (ChoProductReqConstant.QUERY_STATUS_REDEEMCHECK.equalsIgnoreCase(status)){
            //赎回审批通过
            status = ChoProductReqConstant.QUERY_STATUS_CHECK;
            redeemStatus = ChoProductReqConstant.QUERY_STATUS_CHECK;
        }else if (ChoProductReqConstant.QUERY_STATUS_REDEEMBACK.equalsIgnoreCase(status)){
            //赎回审批打回
            status = ChoProductReqConstant.QUERY_STATUS_CHECK;
            redeemStatus = ChoProductReqConstant.QUERY_STATUS_BACK;
        }
        formdata.put("eqi_status",status);
        formdata.put("eqi_redeemStatus",redeemStatus);
        dataTablesInput.setFormdata(formdata);
    }

}
