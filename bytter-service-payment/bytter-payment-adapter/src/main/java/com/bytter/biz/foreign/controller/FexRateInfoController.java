package com.bytter.biz.foreign.controller;


import com.bytter.biz.foreign.service.FexRateInfoService;
import com.bytter.biz.foreign.vo.FexRateInfoVO;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 外汇牌价表
 * @author huangx
 * @date 2020-07-16
 *
 */
@Controller
@RequestMapping("/foreign/fexrateinfo")
public class FexRateInfoController extends BaseController {

    @Autowired
    private FexRateInfoService fexRateInfoService;

    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 外汇牌价查询列表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXRATEINFO_QUERY)
    @LogMessage(content = "查询外汇牌价列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        return "foreign/queryFexRateInfo";
    }

    /**
     * 外汇牌价表列表 页面跳转
     *
     * @return
     */

    @LogMessage(content = "查询外汇牌价列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{rateInterfaceType}")
    public String list(@PathVariable("rateInterfaceType") String rateInterfaceType, Model model) {
        model.addAttribute("rateInterfaceType", rateInterfaceType);
        return "foreign/listFexRateInfo";
    }

    /**
     * 外汇牌价表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexRateInfoService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 外汇牌价查询
     * @param fexRateInfoVO
     * @return
     */
    @PostMapping("/query")
    @ResponseBody
    public AjaxJson query(FexRateInfoVO fexRateInfoVO) {
        fexRateInfoService.query(fexRateInfoVO, this.getCurrentUser());
        return success();
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<FexRateInfoVO> list){
        this.fexRateInfoService.saveImport(list, this.getCurrentUser());
        return success();
    }


    @PostMapping("/test")
    @ResponseBody
    public AjaxJson test(String rateTypeId,String curId,String referCurId,String type){
        //String spStr=fexRateInfoService.getQuoMethodForSP(rateTypeId,curId,referCurId);
        BigDecimal spRate= fexRateInfoService.getRateForSP(rateTypeId,curId,referCurId,type);
        //String endStr= fexRateInfoService.getQuoMethodForEnd(rateTypeId,curId,referCurId);
        Date date= DateUtils.parse("2022-02-20","yyyy-MM-dd");
        //BigDecimal endRate=fexRateInfoService.getRateForEnd(rateTypeId,curId,referCurId,date,type);
        return success();
    }

    /**
     *
     * 查询折本位币报价方式
     *
     * @param curId
     * @param referCurId
     * @return
     */
    @PostMapping("/getBaseCurFexRate")
    @ResponseBody
    public AjaxJson getBaseCurFexRate(String curId,String referCurId){
        ConfigDTO profitAndLossConfig = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
        String rateTypeId = "";
        if(profitAndLossConfig!=null && !StringUtils.isBlank(profitAndLossConfig.getValue())){
            rateTypeId = profitAndLossConfig.getValue();
        }
        FexRateInfoVO quoMethodForSP = fexRateInfoService.getQuoMethodForSP(rateTypeId, curId, referCurId);
        return success(quoMethodForSP);
    }


    /**
     *
     * 查询即期汇率
     *
     * @param curId
     * @param referCurId
     * @return
     */
    @PostMapping("/getSpotRate")
    @ResponseBody
    public AjaxJson getSpotRate(String curId,String referCurId){
        if (StringUtils.isBlank(curId) || StringUtils.isBlank(referCurId)) {
            return success();
        }
        ConfigDTO profitAndLossConfig = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
        String rateTypeId = "";
        if(profitAndLossConfig!=null && !StringUtils.isBlank(profitAndLossConfig.getValue())){
            rateTypeId = profitAndLossConfig.getValue();
        }
        // 取默认外汇牌价类型下、最新发布时间的即期牌价；无牌价时不抛错，由页面保持空值
        FexRateInfoVO lastFexRateForSP = fexRateInfoService.getLastFexRateForSP(rateTypeId, curId, referCurId);
        return success(lastFexRateForSP);
    }

    /**
     *
     * 获取报价方式
     *
     * @param curId1
     * @param curId2
     * @param rateTypeId
     * @param exchangeDate
     * @param term
     * @return
     */
    @PostMapping("/getByCurId")
    @ResponseBody
    public AjaxJson getByCurId(String curId1,String curId2,String rateTypeId,String exchangeDate,String term) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setObj(fexRateInfoService.getRateInfoByRateType(curId1,curId2,rateTypeId,exchangeDate,term));
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    /**
     *
     * 获取远期汇率
     *
     * @param curId
     * @param referCurId
     * @param rateDate
     * @param buyCurId
     * @param quotationCurId
     * @return
     */
    @PostMapping("/getRateForEnd")
    @ResponseBody
    public AjaxJson getRateForEnd(String curId, String referCurId, String rateDate, String buyCurId, String quotationCurId){
        ConfigDTO profitAndLossConfig = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
        String rateTypeId = "";
        if(profitAndLossConfig!=null && !StringUtils.isBlank(profitAndLossConfig.getValue())){
            rateTypeId = profitAndLossConfig.getValue();
        }
        String type = null;
        if (buyCurId.equals(quotationCurId)) {
            type = "1";
        } else {
            type = "2";
        }
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setObj(fexRateInfoService.getRateForEnd(rateTypeId, curId, referCurId, DateUtils.parse(rateDate), type));
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
}
