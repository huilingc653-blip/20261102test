package com.bytter.biz.saas.controller.wage;

import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.wage.entity.NisWageDetail;
import com.bytter.biz.wage.service.NisWageDetailService;
import com.bytter.biz.wage.service.NisWageService;
import com.bytter.biz.wage.vo.NisWageDetailVO;
import com.bytter.biz.wage.vo.NisWageVO;
import com.bytter.biz.wage.vo.v1.NisWageUpdateDetailVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBisBifRuleService;
import com.bytter.system.api.dto.BisBifRuleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller  
 * @Description: NIS_WAGE_DETAIL
 * @author onlineGenerator
 * @date 2018-01-08 17:19:59
 * @version 1.0
 *
 */
@Api(value = "/v1-payment/wage/niswagedetail", tags = "代发工资明细")
@RestController("v1NisWageDetailController")
@RequestMapping("/v1-payment/wage/niswagedetail")
public class NisWageDetailController extends BaseController {
	@Autowired
	private NisWageDetailService nisWageDetailService;
	@Autowired
	private NisWageService nisWageService;
	@DubboReference(check = false)
	private IBisBifRuleService bisBifRuleService;

    /**
    * NIS_WAGE_DETAIL table列表
    *
    * @return
    */
    @ApiOperation(value = "NIS_WAGE_DETAIL列表")
	@PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(nisWageDetailService.getDataTablesUnion(input));
    }


    /**
     * NIS_WAGE_DETAIL编辑页面跳转
     *
     * @return
     */
    @ApiOperation(value = "NIS_WAGE_DETAIL编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<NisWageUpdateDetailVO> update(@PathVariable("id") String id) {
        NisWageUpdateDetailVO model=new NisWageUpdateDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            NisWageDetail nisWageDetail = nisWageDetailService.get(id);
            String isMustCode = CommonConstant.YES;
            if(nisWageDetail!=null){
                NisWageVO nisWage= nisWageService.getByIdUnion(nisWageDetail.getWageId());
                model.setNisWagePage( nisWage);
                if(nisWage!=null){
                    BisBifRuleDTO bisBifRuleDTO = bisBifRuleService.findByBifId(nisWage.getBifCode());
                    if (bisBifRuleDTO != null && StringUtils.isNotBlank(bisBifRuleDTO.getIsMustBankCode())) {
                        isMustCode = bisBifRuleDTO.getIsMustBankCode();
                    }
                }
            }
            model.setNisWageDetailPage( nisWageDetail);
            model.setIsMustCode( isMustCode);
        }
        return AjaxJson.success(model);
    }
    /**
     * 修改NIS_WAGE_DETAIL
     * @param nisWageDetail
     * @return
     */
    @ApiOperation(value = "修改NIS_WAGE_DETAIL")
    @LogMessage(content = "修改NIS_WAGE_DETAIL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody  NisWageDetailVO nisWageDetail){
        nisWageDetailService.updateNisWageDetail(nisWageDetail, this.getCurrentUser());
        return  success();
    }
    /**
     * NIS_WAGE详情页
     *
     * @return
     */
    @ApiOperation(value = "NIS_WAGE详情页")
    @LogMessage(content = "查询NIS_WAGE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{wageId}/{opType}")
    public AjaxJson<NisWageUpdateDetailVO> view(@PathVariable("wageId") String wageId, @PathVariable("opType") String opType) {
        NisWageUpdateDetailVO model=new NisWageUpdateDetailVO();
        model.setWageId(  wageId);
        if(CommonConstant.NO.equals(opType)){
            //查询交易成功的明细
            model.setVoucherStat( BdlinkConstant.SUCCESS);
        }else if(CommonConstant.YES.equals(opType)) {
            //查询等待结果的明细
            model.setVoucherStat(  BdlinkConstant.WAIT_RESULT);
        }else{
            //查询交易失败的明细
            model.setVoucherStat(  BdlinkConstant.BANK_FAILED+CommonConstant.COMMA_SPLIT+BdlinkConstant.INTERFACE_FAILED);
        }
        return AjaxJson.success(model);
    }
}
