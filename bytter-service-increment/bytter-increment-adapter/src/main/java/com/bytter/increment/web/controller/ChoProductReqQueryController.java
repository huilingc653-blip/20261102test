package com.bytter.increment.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
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
@Controller
@RequestMapping("/chowmatistic/choproductreqquery")
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
    @GetMapping("/list")
    public String list(Model model,String from){
        if (!StringUtils.isBlank(from) && "notification".equalsIgnoreCase(from)) {
            String endDay = choProductReqQueryService.getEndDayConfig();
            String startDay = DateUtils.formatDate(DateUtils.getDate());
            String endDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(), Integer.valueOf(endDay)));
            model.addAttribute("startDay", startDay);
            model.addAttribute("endDay", endDate);
        }
        return "chowmatistic/listChoProductReqQueryDir";
    }
    /**
     * 理财类业务查询保本查询列表页面跳转
     *
     * @param model model
     * @return 前端路径
     */
    @GetMapping("/listChoProductReqQuery")
    public String listChoProductReqQuery(Model model) {
        model.addAttribute("pageContent", "listChoProductReqQuery");
        return "chowmatistic/listChoProductReqQuery";
    }
    /**
     * 理财类业务查询非保本查询列表页面跳转
     *
     * @param model model
     * @return 前端路径
     */
    @GetMapping("/listChoProductReqNonQuery")
    public String listChoProductReqNonQuery(Model model) {
        model.addAttribute("pageContent", "listChoProductReqNonQuery");
        return "chowmatistic/listChoProductReqNonQuery";
    }
    /**
     * 应收未收利息明细保本查询列表页面跳转
     *
     * @param model model
     * @return 前端路径
     */
    @GetMapping("/listChoProductReqUncollected")
    public String listChoProductReqUncollected(Model model) {
        model.addAttribute("pageContent", "listChoProductReqUncollected");
        return "chowmatistic/listChoProductReqUncollected";
    }
    /**
     * 应收未收利息明细非保本查询列表页面跳转
     *
     * @param model model
     * @return 前端路径
     */
    @GetMapping("/listChoProductReqUncollectedNon")
    public String listChoProductReqUncollectedNon(Model model) {
        model.addAttribute("pageContent", "listChoProductReqUncollectedNon");
        return "chowmatistic/listChoProductReqUncollectedNon";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        setRedeemStatus(dataTablesInput);
        return choProductReqQueryService.getDataTablesUnion(dataTablesInput,this.getCurrentUser());
    }
    @PostMapping("/datatableForSelect")
    @ResponseBody
    public DataTablesOutput datatableForSelect(@RequestBody DataTablesInput dataTablesInput){
        setRedeemStatus(dataTablesInput);
        return choProductReqQueryService.getDataTablesUnionForSelect(dataTablesInput);
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id,Model model){
        if (StringUtils.isNotEmpty(id)) {
            ChoProductReqVo vo = choProductReqService.getChoProductReqVoById(id);
            model.addAttribute("choProductReqPage", vo);
            List list = choProductRedeemService.listProductRedeemDetailVOApproved(vo.getId());
            if (!CollectionUtils.isEmpty(list)) {
                model.addAttribute("list", list);
            }
            List<ChoProductRenewal> choProductRenewalList = choProductRenewalService.listChoProductRenewalByReqId(vo.getId());
            if (!CollectionUtils.isEmpty(choProductRenewalList)) {
                model.addAttribute("renewalList", choProductRenewalList);
            }
            List<ChoProductNetVal> choProductNetValList = choProductNetValService.listByReqId(id);
            if (!CollectionUtils.isEmpty(choProductNetValList)) {
                model.addAttribute("netValList", choProductNetValList);
            }
        }
        model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ);
        return "chowmatistic/viewChoProductReqQuery";
    }
    @GetMapping("/uncollectedList")
    public String uncollectedList(Model model){
        return "chowmatistic/listChoProductReqUncollectedDir";
    }
    @PostMapping("/uncollectedDatatable")
    @ResponseBody
    public DataTablesOutput uncollectedDataTable(@RequestBody DataTablesInput input){
        return choProductReqQueryService.uncollectedDataTable(input,this.getCurrentUser());
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
    /**
     * 理财管理App跳转首页
     * @param model
     * @return
     */
    @PostMapping("/appIndex")
    public String appIndex(Model model){
        return "chowmatistic/appChowmatisticReportIndex";
    }
    /**
     * 理财管理报表显示
     *
     * @return
     */
    @RequestMapping("/mobileReq")
    public String mobileReq(Model model,ChoProductReqUncollectedVo reqVo) {
        List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
        model.addAttribute("curList", curList);
        model.addAttribute("curId",reqVo.getSign());
        return "chowmatistic/mobileReq";
    }
    /**
     * 理财管理报表详情显示
     * @param model
     * @param reqVo
     * @return
     */
    @RequestMapping("/mobileViewReq")
    public String mobileViewReq(Model model,ChoProductReqUncollectedVo reqVo) {
        DataTablesInput input = new DataTablesInput();
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isNotBlank(reqVo.getId())){
            map.put("eqs_id",reqVo.getId());
        }
        String nowDate = DateUtils.formatDate(DateUtils.getDate());
        if(!EmptyUtils.isEmpty(reqVo.getCalEndDateStr())){
            nowDate = reqVo.getCalEndDateStr();
        }
        map.put("calEndDate",nowDate);
        input.setFormdata(map);
        DataTablesOutput dataTablesOutput = choProductReqQueryService.uncollectedDataTable(input,this.getCurrentUser());
        List<ChoProductReqUncollectedVo> voList = dataTablesOutput.getData();
        ChoProductReqUncollectedVo vo = new ChoProductReqUncollectedVo();
        if(!EmptyUtils.isEmptyList(voList)){
            vo = voList.get(0);
            vo.setReferIncomeRate(vo.getReferIncomeRate().multiply(new BigDecimal(100)));
            vo.setBuyMoney(EmptyUtils.isEmpty(vo.getBuyMoney())?BigDecimal.ZERO:vo.getBuyMoney());
            vo.setReferIncomeMoney(EmptyUtils.isEmpty(vo.getReferIncomeMoney())?BigDecimal.ZERO:vo.getReferIncomeMoney());
            vo.setReceivableMoney(EmptyUtils.isEmpty(vo.getReceivableMoney())?BigDecimal.ZERO:vo.getReceivableMoney());
            vo.setActualMoney(EmptyUtils.isEmpty(vo.getActualMoney())?BigDecimal.ZERO:vo.getActualMoney());
            vo.setUncollectedMoney(EmptyUtils.isEmpty(vo.getUncollectedMoney())?BigDecimal.ZERO:vo.getUncollectedMoney());
            vo.setSubFeeMoney(EmptyUtils.isEmpty(vo.getSubFeeMoney())?BigDecimal.ZERO:vo.getSubFeeMoney());
            vo.setRedeemChargeMoney(EmptyUtils.isEmpty(vo.getRedeemChargeMoney())?BigDecimal.ZERO:vo.getRedeemChargeMoney());
            vo.setChargeMoney(EmptyUtils.isEmpty(vo.getChargeMoney())?BigDecimal.ZERO:vo.getChargeMoney());
        }
        model.addAttribute("vo", vo);
        return "chowmatistic/mobileViewReq";
    }
    /**
     * 理财数据列表
     */
    @PostMapping("/getReqMoblieList")
    @ResponseBody
    public Object getReqMoblieList(Model model,ChoProductReqUncollectedVo reqVo) {
        Map<String,Object> mapData = new HashMap<>();
        //根据条件查询数据
        List<ChoProductReqUncollectedVo> voList = choProductReqQueryService.listReportInfo(reqVo,this.getCurrentUser());
        BigDecimal buyTotal = BigDecimal.ZERO;
        BigDecimal referIncomeTotal = BigDecimal.ZERO;
        for(ChoProductReqUncollectedVo vo : voList){
            vo.setProductType(dictService.get("cho.productType", vo.getProductType()));
            buyTotal = buyTotal.add(EmptyUtils.isEmpty(vo.getBuyMoney())?BigDecimal.ZERO:vo.getBuyMoney());
            referIncomeTotal = referIncomeTotal.add(EmptyUtils.isEmpty(vo.getReferIncomeMoney())?BigDecimal.ZERO:vo.getReferIncomeMoney());
        }
        mapData.put("dataList",voList);
        mapData.put("buyTotal",buyTotal);
        mapData.put("referIncomeTotal",referIncomeTotal);
        return mapData;
    }

    @GetMapping("/incomeMoneyView/{id}")
    public String incomeMoneyView(@PathVariable("id") String id,Model model){
        if (StringUtils.isNotEmpty(id)) {
            ChoProductReqVo vo = choProductReqService.getChoProductReqVoById(id);
            model.addAttribute("choProductReqPage", vo);
            List<ChoProductRedeemDetailVO> list  = choProductRedeemService.listProductRedeemDetailVOApproved(vo.getId());
            if (!CollectionUtils.isEmpty(list)) {
                model.addAttribute("list", list);
            }
        }
        model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ);
        return "chowmatistic/viewChoProductRedeemQuery";
    }

    @PostMapping("/saveUpload")
    @ResponseBody
    public AjaxJson save(ChoProductReq choProduct){
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
}
