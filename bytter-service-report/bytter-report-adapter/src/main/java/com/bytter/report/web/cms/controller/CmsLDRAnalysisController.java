package com.bytter.report.web.cms.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.domain.cms.service.CmsLDRAnalysisService;
import com.bytter.report.domain.cms.vo.CmsLDRAnalysisVO;
import com.bytter.report.domain.cms.vo.CmsLDRParamVO;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.BankTypeDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @Title: CmsLDRAnalysisController
 * @ProjectName T3(3.2)
 * @Description: 融资存贷比分析汇总表
 * @date 2020/1/16 11:22
 */
@Controller
@RequestMapping("/cms/report/cmsLDRanalysis")
public class CmsLDRAnalysisController extends BaseController {
    @Autowired
    private CmsLDRAnalysisService cmsLDRAnalysisService;
    @DubboReference
    private IBankTypeService iBankTypeService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private ICorpService corpService;

    @GetMapping("/list")
    public String corplist(Model model){
        model.addAttribute("searchEndDate",DateUtils.formatDate(DateUtils.getDate(),"yyyy-MM-dd"));
        return "cms/report/cmsLDRAnalysis/listLDRAnalysis";
    }

    //@RequiresPermissions(ResourceConstant.CMS_REPORT_LDR_ANALYSIS_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input){
        DataTablesOutput output=null;
        String sortType=(String)input.getFormdata().get("typesOfStatistics");//统计维度
         if(sortType.equals(CmsConstant.SORT_TYPE_1)){//统计维度--融资银行
            output=cmsLDRAnalysisService.bankDatatable(input,getCurrentUser());
             List<CmsLDRAnalysisVO> data = output.getData();
             if (CollectionUtil.isNotEmpty(data)) {
                 Map<String, BankTypeDTO> bankTypeMap = iBankTypeService.getBankTypeMap();
                 if(null != bankTypeMap){
                     for (CmsLDRAnalysisVO vo : data) {
                         BankTypeDTO bankTypeDTO = bankTypeMap.get(vo.getBankType());
                         if(null != bankTypeDTO){
                             vo.setBankTypeName(bankTypeDTO.getTypeName());
                         }
                     }
                 }
             }

        }else{//统计维度--融资单位(默认)
            output=cmsLDRAnalysisService.corpDatatable(input,getCurrentUser());
            List<CmsLDRAnalysisVO> data = output.getData();
            if (CollectionUtil.isNotEmpty(data)) {
                 Map<String, String> corpNameMap = corpService.getNameByIds(data.stream().map(CmsLDRAnalysisVO::getCorpId).collect(Collectors.toList()));
                 for (CmsLDRAnalysisVO vo : data) {
                     vo.setCorpName(corpNameMap.get(vo.getCorpId()));
                 }
            }
        }


        return output;
    }

    /**
     * <b>功能介绍:取有效银行类别</b><br>
     */
    @GetMapping("/bankTypeList")
    @ResponseBody
    public List<BankTypeDTO> getBankTypeValid(){
        List<BankTypeDTO> bankTypeDTOS=iBankTypeService.listByValidSign("1");
        BankTypeDTO bankTypeDTO=new BankTypeDTO();//手动增加一个银行类别
        bankTypeDTO.setId("net");
        bankTypeDTO.setBankType("0000");
        bankTypeDTO.setTypeName(multiLangServiceI.getLang("common.button.center")); //结算中心
        bankTypeDTOS.add(bankTypeDTO);
        return bankTypeDTOS;
    }

    /**
     * 查看银行/结算中心明细
     */
    @GetMapping("/viewBankTypeDtl")
    public String viewBankTypeDtl(CmsLDRParamVO paramVO,Model model){
        List<CmsLDRAnalysisVO> voList=null;
        DataTablesInput input=new DataTablesInput();
        Map<String, Object> params = input.getFormdata();
        params.put("curId",paramVO.getCurId());
        params.put("bankType",paramVO.getBankType());
        params.put("endDate",DateUtils.formatDate(paramVO.getEndDate(),"yyyy-MM-dd"));
        params.put("isContain",paramVO.getIsContain());
        params.put("typesOfStatistics",paramVO.getTypesOfStatistics());
        String[] cycleList = null;
        String cycle="";
        if(null!=paramVO.getCycle()||StringUtils.isNotEmpty(paramVO.getCycle())){
            cycleList = paramVO.getCycle().split(",");
            if(paramVO.getCycle().contains("all,")){
                cycle=paramVO.getCycle().replace("all,","");
            }
        }
        params.put("cycle",cycle);
        params.put("isConvertStandCur",paramVO.getIsConvertStandCur());//是否折合本位币
        params.put("moneyUnit",paramVO.getMoneyUnit());//币别单位

        params.put("dtlFlag",CmsConstant.IS_TRUE);
        input.setFormdata(params);
        if(paramVO.getNetId()!=null&& StringUtils.isNotEmpty(paramVO.getNetId())){//银行类型属于结算中心
            params.put("netFlag", CmsConstant.IS_TRUE);
            params.put("netId", paramVO.getNetId());
            voList=cmsLDRAnalysisService.getCorpDtl(input);
            model.addAttribute("netFlag",CmsConstant.IS_TRUE);
        }else{
            voList=cmsLDRAnalysisService.getBankDtl(input);
        }
        model.addAttribute("searchEndDate",DateUtils.formatDate(paramVO.getEndDate(),"yyyy-MM-dd"));
        model.addAttribute("voList",voList);
        model.addAttribute("cycleList",cycleList);
        return "cms/report/cmsLDRAnalysis/listBankAnalysisDtl";
    }

    /**
     *币种转换校验
     *@Param origCurId
     *@Param convertCurId
     *@return
     */
    @PostMapping("/checkConvertCurId")
    @ResponseBody
    public AjaxJson checkConvertCurId(String origCurId, String convertCurId, Date date){
        cmsLDRAnalysisService.checkConvertCurId(origCurId, convertCurId, date);
        return success();
    }

}
