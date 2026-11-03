package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.dao.FexProfitLossCalculateVo;
import com.bytter.biz.foreign.entity.FexProfitLossManage;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.service.FexProfitLossManageDtlService;
import com.bytter.biz.foreign.service.FexProfitLossManageService;
import com.bytter.biz.foreign.service.FexTranReqExtService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.vo.FexProfitLossManageDtlVO;
import com.bytter.biz.foreign.vo.FexProfitLossManageVO;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqExtVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IBtRateTypeService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2025/9/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Controller
@RequestMapping("/foreign/fexProfit/lossManage")
public class FexProfitLossManageController extends BaseController {
    @Autowired
    private FexProfitLossManageService fexProfitLossManageService;

    @Autowired
    private FexTranReqService fexTranReqService;

    @Autowired
    private FexTranReqExtService fexTranReqExtService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    @DubboReference(check = false)
    private IConfigService configService;

    @DubboReference(check = false)
    private IBtRateTypeService btRateTypeService;

    @Autowired
    private FexProfitLossManageDtlService fexProfitLossManageDtlService;

    /**
     * 损益信息查询 页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXPROFITLOSSMANAGE_OP)
    @LogMessage(content = "查询损益信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        addRatePrecision(model);
        return "foreign/listFexProfitLossManage";
    }

    /**
     * table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        DataTablesOutput DataTablesOutput = fexProfitLossManageService.getDataTablesUnion(dataTablesInput,getCurrentUser());
        return DataTablesOutput;
    }

    /**
     *
     * 计算交易损益
     *
     * @param profitLossCalculateVo
     * @return
     */
    @PostMapping("/calculateFexProfitLoss")
    @ResponseBody
    public AjaxJson calculateFexProfitLoss(@RequestBody FexProfitLossCalculateVo profitLossCalculateVo){
        if (StringUtils.isEmpty(profitLossCalculateVo.getRateTypeId())) {
            ConfigDTO config = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
            profitLossCalculateVo.setRateTypeId(config.getValue());
        }

        Map<String, BigDecimal> calculateResult = fexProfitLossManageService.calculateFexProfitLoss(profitLossCalculateVo);
        return success(calculateResult);
    }


    /**
     *
     * 计算生命周期损益
     *
     * @param profitLossCalculateVo
     * @return
     */
    @PostMapping("/calculateFexLifeCycleProfitLoss")
    @ResponseBody
    public AjaxJson calculateFexLifeCycleProfitLoss(@RequestBody FexProfitLossCalculateVo profitLossCalculateVo){
        Map<String, BigDecimal> calculateResult = fexProfitLossManageService.calculateFexLifeCycleProfitLoss(profitLossCalculateVo);
        return success(calculateResult);
    }

    /**
     *
     * 计算折本位币损益
     *
     * @param profitLossCalculateVo
     * @return
     */
    @PostMapping("/calculateFexLifeCycleConvertLossMoney")
    @ResponseBody
    public AjaxJson calculateFexLifeCycleConvertLossMoney(@RequestBody FexProfitLossCalculateVo profitLossCalculateVo){
        BigDecimal profitLossMoney = fexProfitLossManageService.calculateFexLifeCycleConvertLossMoney(profitLossCalculateVo);
        return success(profitLossMoney);
    }

    /**
     *
     * 计算交割损益
     *
     * @param profitLossCalculateVo
     * @return
     */
    @PostMapping("/calculateFexLifeCycleDeliveryLossMoney")
    @ResponseBody
    public AjaxJson calculateFexLifeCycleDeliveryLossMoney(@RequestBody FexProfitLossCalculateVo profitLossCalculateVo){
        BigDecimal deliveryLossMoney = fexProfitLossManageService.calculateFexLifeCycleDeliveryLossMoney(profitLossCalculateVo);
        return success(deliveryLossMoney);
    }

    /**
     * 删除损益信息
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除损益", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        FexProfitLossManage fexProfitLossManage = fexProfitLossManageService.get(id);
        fexProfitLossManageService.deleteEntity(fexProfitLossManage);
        return success();
    }

    /**
     * 损益信息编辑界面跳转
     *
     * @return
     */
    @LogMessage(content = "损益信息编辑界面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/update/{reqId}")
    public String update(@PathVariable("reqId") String reqId, Model model) {
        //获取交易管理信息
        FexTranReq fexTranReq = fexTranReqService.get(reqId);
        FexTranReqVO fexTranReqVO = new FexTranReqVO();
        BeanUtils.copyProperties(fexTranReq, fexTranReqVO);
        CorpDTO corpDTO = corpService.get(fexTranReq.getCorpId());
        if(null != corpDTO){
            fexTranReqVO.setCorpName(corpDTO.getName());
        }
        Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrencyMap();
        FexTranReqExtVo fexTranReqExtVo = null;
        if(null != fexTranReq){
            fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranReq.getId());
            if(currencyMap.containsKey(fexTranReqExtVo.getTranCurPairBaseId())){
                BtCurrencyDTO btCurrencyDTO = currencyMap.get(fexTranReqExtVo.getTranCurPairBaseId());
                if(null != btCurrencyDTO){
                    fexTranReqExtVo.setTranCurPairBaseEnglishCode(btCurrencyDTO.getEnglishCode());
                }
            }
            //交易货币对报价币种
            if(currencyMap.containsKey(fexTranReqExtVo.getTranCurPairReferId())){
                BtCurrencyDTO btCurrencyDTO = currencyMap.get(fexTranReqExtVo.getTranCurPairReferId());
                if(null != btCurrencyDTO){
                    fexTranReqExtVo.setTranCurPairReferEnglishCode(btCurrencyDTO.getEnglishCode());
                }
            }
            model.addAttribute("fexTranReqExt", fexTranReqExtVo);
        }
        String tranType = "";
        if(ForeignConstant.TRAN_TYPE_1.equals(fexTranReqVO.getTranType())){
            if(StringUtils.isNotBlank(fexTranReqVO.getIsSwapTran()) && "1".equals(fexTranReqVO.getIsSwapTran())){
                fexTranReqVO.setTranType(ForeignConstant.TRAN_TYPE_9);
                tranType = "swap";
            }else{
                if(ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReqVO.getFexTranType())){
                    fexTranReqVO.setTranType(ForeignConstant.TRAN_TYPE_7);
                    tranType = "spot";
                }else if(ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranReqVO.getFexTranType())){
                    fexTranReqVO.setTranType(ForeignConstant.TRAN_TYPE_8);
                    tranType = "forward";
                }
            }
        }else if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())){
            tranType = "2";
        }
        model.addAttribute("corpDTO",corpDTO);
        model.addAttribute("fexTranReq",fexTranReqVO);
        model.addAttribute("tranType",tranType);
        //损益集合
        List<FexProfitLossManageVO> fexProfitLossManageList = fexProfitLossManageService.listFexProfitLossManage(reqId);
        model.addAttribute("fexProfitLossManageList",fexProfitLossManageList);
        FexProfitLossManageVO fexProfitLossManageVO = fexProfitLossManageList.get(0);
        fexProfitLossManageService.fillMissingQuotationDate(fexProfitLossManageVO, getCurrentUser().getLoginName());
        model.addAttribute("fexProfitLossManage",fexProfitLossManageVO);
        //损益明细集合
        List<FexProfitLossManageDtlVO> fexProfitLossManageDtlList = fexProfitLossManageDtlService.listFexProfitLossManageDtl(reqId);
        model.addAttribute("fexProfitLossManageDtlList",fexProfitLossManageDtlList);

        List<FexTranLifeCycleVo> fexTranLifeCycleList = fexProfitLossManageService.listFexTranLifeCycle(reqId);
        model.addAttribute("fexTranLifeCycleList",fexTranLifeCycleList);
        String page = "";
        if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())){
            //计算期权费金额
            fexProfitLossManageService.calculatePremiumMoney(fexProfitLossManageVO, fexTranReqVO);
            Map<String, BigDecimal> result = fexProfitLossManageService.calculatePremiumMoney(fexProfitLossManageVO, fexTranReqVO);
            model.addAttribute("premiumMoney", result.get("premiumMoney"));
            model.addAttribute("convertMoney", result.get("convertMoney"));
            page = "foreign/updateFexProfitLossManageOption";
        }else{
            page = "foreign/updateFexProfitLossManage";
        }
        addRatePrecision(model);
        return page;
    }

    /**
     * 修改数据
     * @param vo
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXPROFITLOSSMANAGE_OP)
    @LogMessage(content = "修改损益数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(FexProfitLossManageVO vo){
        fexProfitLossManageService.modify(vo, getCurrentUser().getLoginName());
        return success();
    }

    /**
     * 损益信息查看
     * @param reqId
     * @param model
     * @return
     */
    @LogMessage(content = "损益信息查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/view/{reqId}")
    public String view(@PathVariable("reqId") String reqId, Model model) {
        //获取交易管理信息
        FexTranReq fexTranReq = fexTranReqService.get(reqId);
        FexTranReqVO fexTranReqVO = new FexTranReqVO();
        BeanUtils.copyProperties(fexTranReq, fexTranReqVO);
        CorpDTO corpDTO = corpService.get(fexTranReq.getCorpId());
        if(null != corpDTO){
            fexTranReqVO.setCorpName(corpDTO.getName());
        }
        Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrencyMap();
        if(null != fexTranReq){
            FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranReq.getId());
            if(currencyMap.containsKey(fexTranReqExtVo.getTranCurPairBaseId())){
                BtCurrencyDTO btCurrencyDTO = currencyMap.get(fexTranReqExtVo.getTranCurPairBaseId());
                if(null != btCurrencyDTO){
                    fexTranReqExtVo.setTranCurPairBaseEnglishCode(btCurrencyDTO.getEnglishCode());
                }
            }
            //交易货币对报价币种
            if(currencyMap.containsKey(fexTranReqExtVo.getTranCurPairReferId())){
                BtCurrencyDTO btCurrencyDTO = currencyMap.get(fexTranReqExtVo.getTranCurPairReferId());
                if(null != btCurrencyDTO){
                    fexTranReqExtVo.setTranCurPairReferEnglishCode(btCurrencyDTO.getEnglishCode());
                }
            }
            model.addAttribute("fexTranReqExt", fexTranReqExtVo);
        }
        String tranType = "";
        if(ForeignConstant.TRAN_TYPE_1.equals(fexTranReqVO.getTranType())){
            if(StringUtils.isNotBlank(fexTranReqVO.getIsSwapTran()) && "1".equals(fexTranReqVO.getIsSwapTran())){
                fexTranReqVO.setTranType(ForeignConstant.TRAN_TYPE_9);
                tranType = "swap";
            }else{
                if(ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReqVO.getFexTranType())){
                    fexTranReqVO.setTranType(ForeignConstant.TRAN_TYPE_7);
                    tranType = "spot";
                }else if(ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranReqVO.getFexTranType())){
                    fexTranReqVO.setTranType(ForeignConstant.TRAN_TYPE_8);
                    tranType = "forward";
                }
            }
        }else if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReqVO.getTranType())){
            tranType = "2";
        }

        model.addAttribute("corpDTO",corpDTO);
        model.addAttribute("fexTranReq",fexTranReqVO);
        model.addAttribute("tranType",tranType);
        //损益集合
        List<FexProfitLossManageVO> fexProfitLossManageList = fexProfitLossManageService.listFexProfitLossManage(reqId);
        model.addAttribute("fexProfitLossManageList",fexProfitLossManageList);
        FexProfitLossManageVO fexProfitLossManageVO = fexProfitLossManageList.get(0);
        fexProfitLossManageService.fillMissingQuotationDate(fexProfitLossManageVO, getCurrentUser().getLoginName());
        //历史数据已实现净损益为空时补算并落库，避免外汇期权交易查看页「已实现损益-净损益」为空
        fexProfitLossManageService.fillMissingOptionNetRealMoneyTotal(fexProfitLossManageVO, getCurrentUser().getLoginName());
        if(StringUtils.isNotBlank(fexProfitLossManageVO.getCurId()) && currencyMap.containsKey(fexProfitLossManageVO.getCurId())){
            fexProfitLossManageVO.setCurEnName(currencyMap.get(fexProfitLossManageVO.getCurId()).getEnglishCode());
        }
        model.addAttribute("fexProfitLossManage",fexProfitLossManageVO);
        //损益明细集合
        List<FexProfitLossManageDtlVO> fexProfitLossManageDtlList = fexProfitLossManageDtlService.listFexProfitLossManageDtl(reqId);
        model.addAttribute("fexProfitLossManageDtlList",fexProfitLossManageDtlList);
        //生命周期集合
        /*List<FexTranLifeCycleVo> fexTranLifeCycleList = fexProfitLossManageService.listFexTranLifeCycle(reqId);
        model.addAttribute("fexTranLifeCycleList",fexTranLifeCycleList);*/
        String page = "";
        if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())){
            page = "foreign/viewFexProfitLossManageOption";
        }else{
            page = "foreign/viewFexProfitLossManage";
        }
        addRatePrecision(model);
        return page;
    }

    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXPROFITLOSSMANAGE_OP)
    @LogMessage(content = "损益批量更新", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchUpdate")
    @ResponseBody
    public AjaxJson batchUpdate(@RequestParam("ids") String ids) {
        fexProfitLossManageService.batchUpdate(ids, getCurrentUser());
        return success();
    }

    /**
     * 计算浮动损益、折本位币浮动损益
     * @param vo
     * @return
     */
    @LogMessage(content = "计算浮动损益、折本位币浮动损益", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/calculatePositionProfit")
    @ResponseBody
    public AjaxJson calculatePositionProfit(FexProfitLossManageVO vo) {
        return success(fexProfitLossManageService.calculatePositionProfit(vo));
    }

    /**
     * 计算期权费金额
     * @param vo
     * @return
     */
    @LogMessage(content = "计算期权费金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/calculatePremiumMoney")
    @ResponseBody
    public AjaxJson calculatePremiumMoney(FexProfitLossManageVO vo) {
        FexProfitLossManage fexProfitLossManage = fexProfitLossManageService.get(vo.getId(), true);
        FexProfitLossManageVO fexProfitLossManageVO = new FexProfitLossManageVO();
        BeanUtils.copyProperties(fexProfitLossManage, fexProfitLossManageVO);
        FexTranReqVO fexTranReqVO = fexTranReqService.getByIdUnion(fexProfitLossManageVO.getReqId());
        fexProfitLossManageVO.setCurId(vo.getCurId());
        return success(fexProfitLossManageService.calculatePremiumMoney(fexProfitLossManageVO, fexTranReqVO));
    }

    /**
     * 根据默认估值牌价类型向页面提供汇率精度与格式。
     *
     * @param model 页面模型
     */
    private void addRatePrecision(Model model) {
        int decimalPlaces = 6;
        ConfigDTO config = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE,
                ForeignConstant.FEX_CONFIG_DEFTRANRATETYPE);
        if (config != null && StringUtils.isNotBlank(config.getValue())) {
            Integer configuredPlaces = btRateTypeService.getDecimalPlacesById(config.getValue());
            if (configuredPlaces != null && configuredPlaces >= 0) {
                decimalPlaces = configuredPlaces;
            }
        }
        StringBuilder rateFormat = new StringBuilder("#,##0");
        if (decimalPlaces > 0) {
            rateFormat.append('.');
            for (int i = 0; i < decimalPlaces; i++) {
                rateFormat.append('0');
            }
        }
        model.addAttribute("rateDecimalPlaces", decimalPlaces);
        model.addAttribute("rateFormat", rateFormat.toString());
    }
}
