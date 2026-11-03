package com.bytter.increment.web.controller;

import cn.hutool.json.JSONUtil;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.entity.ChoProductRedeemDetail;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.service.ChoProductRedeemService;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.domain.vo.ChoProductRedeemBeanVO;
import com.bytter.increment.domain.vo.ChoProductRedeemDetailVO;
import com.bytter.increment.domain.vo.ChoProductRedeemVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
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
@RequestMapping("/chowmatistic/choproductredeem")
public class ChoProductRedeemController extends BaseController {
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @DubboReference(check = false)
	private ICorpService iCorpService;
    @DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @GetMapping("/list")
    public String list(){
        return "chowmatistic/listChoProductRedeem";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return choProductRedeemService.getDataTablesUnion(dataTablesInput,this.getCurrentUser());
    }
    @PostMapping("/datatableForSelect")
    @ResponseBody
    public DataTablesOutput dataTableForSelect(@RequestBody DataTablesInput dataTablesInput){
        return choProductRedeemService.getDataTablesUnionForSelect(dataTablesInput,this.getCurrentUser());
    }
    @GetMapping("/add")
    public String add(ChoProductRedeem entity,Model model){
        if (StringUtils.isNotEmpty(entity.getId())) {
            entity = choProductRedeemService.get(entity.getId());
            model.addAttribute("choProductRedeemPage", entity);
        }
        return "chowmatistic/addChoProductRedeemBatch";
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ChoProductRedeemBeanVO choProductRedeemBeanVO){
        ChoProductRedeem entity = new ChoProductRedeem();
        BeanUtils.copyNotNullProperties(choProductRedeemBeanVO,entity);
        if (entity.getRedeemMoney() == null) {
            entity.setRedeemMoney(new BigDecimal(0));
        }
        entity.setCreateBy(this.getCurrentUser().getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        choProductRedeemService.saveChoProductRedeem(entity,choProductRedeemBeanVO,getCurrentUser(),false);
        return success();
    }
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids){
        choProductRedeemService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }
    @GetMapping("/update")
    public String update(ChoProductRedeem choProductRedeem, Model model){
        return choProductRedeemService.toUpdate(choProductRedeem, model);
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ChoProductRedeemBeanVO choProductRedeemBeanVO){
        ChoProductRedeem t = choProductRedeemService.get(choProductRedeemBeanVO.getId(),true);
        choProductRedeemBeanVO.setUpdateBy(this.getCurrentUser().getLoginName());
        choProductRedeemBeanVO.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(choProductRedeemBeanVO, t,"bankName","bankAcc");
        choProductRedeemService.toModify(t,choProductRedeemBeanVO,getCurrentUser());
        return success();
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        ChoProductRedeem entity = choProductRedeemService.get(id,true);
        this.choProductRedeemService.deleteEntity(entity);
        return success();
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id,Model model){
        if (StringUtils.isNotEmpty(id)) {
            ChoProductRedeemVo choProductReqVo = choProductRedeemService.getChoProductRedeemVoById(id);
            List<ChoProductRedeemDetailVO> list = choProductRedeemService.getChoProductRedeemDetailVOById(id);
            //设置上次赎回日期和待赎回金额
            for(ChoProductRedeemDetailVO choProductRedeemDetailVO : list){
                List<ChoProductRedeemDetail> detailList = choProductRedeemService.listProductRedeemApproved(choProductRedeemDetailVO.getReqId());
                if (!CollectionUtils.isEmpty(detailList)) {
                    choProductRedeemDetailVO.setLastRedeemDate(detailList.get(detailList.size() - 1).getRedeemDate());
                }
                BigDecimal leftMoney = choProductRedeemService.getLeftMoneyByReqId(choProductRedeemDetailVO.getReqId());
                choProductRedeemDetailVO.setLeftMoney(leftMoney);
            }
            model.addAttribute("choProductRedeemPage", choProductReqVo);
            model.addAttribute("list", list);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM);
        }
        return "chowmatistic/viewChoProductRedeemBatch";
    }
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model){
        model.addAttribute("flowStatus", flowStatus);
        return "chowmatistic/listChoProductRedeemApprove";
    }
    /**
     * 跳转至审核详情界面
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            ChoProductRedeemVo choProductReqVo = choProductRedeemService.getChoProductRedeemVoById(id);
            List<ChoProductRedeemDetailVO> list = choProductRedeemService.getChoProductRedeemDetailVOById(id);
            //设置上次赎回日期和待赎回金额
            for(ChoProductRedeemDetailVO choProductRedeemDetailVO : list){
                List<ChoProductRedeemDetail> detailList = choProductRedeemService.listProductRedeemApproved(choProductRedeemDetailVO.getReqId());
                if (!CollectionUtils.isEmpty(detailList)) {
                    choProductRedeemDetailVO.setLastRedeemDate(detailList.get(detailList.size() - 1).getRedeemDate());
                }
                BigDecimal leftMoney = choProductRedeemService.getLeftMoneyByReqId(choProductRedeemDetailVO.getReqId());
                choProductRedeemDetailVO.setLeftMoney(leftMoney);
            }
            model.addAttribute("choProductRedeemPage", choProductReqVo);
            model.addAttribute("list", list);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM);
            model.addAttribute("taskId", taskId);
        }
        return "chowmatistic/approveChoProductRedeem";
    }
    /**
     * 加载审核列表数据
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody  DataTablesInput dataTablesInput){
        return this.choProductRedeemService.dataTableApprove(dataTablesInput);
    }
    @GetMapping("/getLeftMoney/{reqiId}")
    @ResponseBody
    public String getLeftMoney(@PathVariable("reqiId") String reqId,Model model){
        AjaxJson json = new AjaxJson(true);
        if (StringUtils.isNotEmpty(reqId)) {
            BigDecimal leftMoney = choProductRedeemService.getLeftMoneyByReqId(reqId);
            Map<String,Object> params = new HashMap<>();
            params.put("leftMoney",leftMoney);
            BigDecimal leftRedeemPortion = choProductRedeemService.getLeftRedeemPortionByReqId(reqId);
            params.put("leftRedeemPortion",leftRedeemPortion);
            List<ChoProductRedeemDetail> detailList = choProductRedeemService.listProductRedeemApproved(reqId);
            if (!CollectionUtils.isEmpty(detailList)) {
                params.put("lastRedeemDate", detailList.get(detailList.size() - 1).getRedeemDate());
            }
            json.setAttributes(params);
        }
        return JSONUtil.toJsonStr(json);
    }
    @GetMapping("/queryRedeemInfo/{reqId}")
    @ResponseBody
    public AjaxJson queryRedeemInfo(@PathVariable("reqId") String reqId,Model model){
        AjaxJson json = new AjaxJson(true);
        if (StringUtils.isNotEmpty(reqId)) {
            ChoProductReq choProductReq = choProductReqService.get(reqId);
            Map<String,Object> params = new HashMap<>(4);
            List<ChoProductRedeemDetail> detailList = choProductRedeemService.listProductRedeemApproved(reqId);
            // 可赎回本金
            BigDecimal maxRedeemMoney = choProductReq.getBuyMoney();
            // 可赎回份额
            BigDecimal maxRedeemPortion = choProductReq.getPortion();
            if (!CollectionUtils.isEmpty(detailList)) {
                for (ChoProductRedeemDetail redeem : detailList) {
                    maxRedeemMoney = maxRedeemMoney.subtract(redeem.getRedeemMoney());
                    maxRedeemPortion = maxRedeemPortion.subtract(redeem.getRedeemPortion());
                }
                params.put("lastRedeemDate", detailList.get(detailList.size() - 1).getRedeemDate());
            }
            params.put("maxRedeemMoney", maxRedeemMoney);
            params.put("maxRedeemPortion", maxRedeemPortion);
            json.setAttributes(params);
        }
        return json;
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            ChoProductRedeemVo choProductRedeemVo = choProductRedeemService.getChoProductRedeemVoById(businessId);
            model.addAttribute("choProductRedeemPage", choProductRedeemVo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM);
        }
        return "chowmatistic/viewApproveChoProductRedeem";
    }
    /**
     * <p>
     * （组件） 理财赎回
     * </p>
     *
     * @return selectBtSwiftInfo
     */
    @GetMapping("/select")
    public String select(String corpId,String row,String id,Model model) {
        if(StringUtils.isNotBlank(corpId)){
            CorpDTO corpDTO = iCorpService.get(corpId);
            model.addAttribute("corp",corpDTO);
        }
        model.addAttribute("row",row);
        if(StringUtils.isNotBlank(id)){
        	BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(id);
            if(StringUtils.isNotEmpty(bisAccDtlDTO)){
                // 获取交易日期
                String date = DateUtils.date2Str(bisAccDtlDTO.getTransTime(), DateUtils.date_sdf());
                model.addAttribute("endDate",date);
                Date dateStart = DateUtils.addDay(bisAccDtlDTO.getTransTime(), -30);
                model.addAttribute("startDate",DateUtils.date2Str(dateStart, DateUtils.date_sdf()));
                // 获取交易金额
                model.addAttribute("dto", bisAccDtlDTO);
            }
        }
        return "chowmatistic/selectChoProductRedeem";
    }

    /**
     * 银行账户流水查询
     *
     * @param model
     * @return
     */
    @GetMapping("/query")
    public String query(Model model, String corpId, String bankAcc, BigDecimal redeemMoney, Date redeemDate) {
        model.addAttribute("corpId", corpId);
        model.addAttribute("bankAcc", bankAcc);
        if(!EmptyUtils.isEmpty(redeemMoney)){
            BigDecimal amtMin = redeemMoney.subtract(redeemMoney.multiply(BigDecimal.valueOf(0.1)));
            BigDecimal amtMax = redeemMoney.add(redeemMoney.multiply(BigDecimal.valueOf(0.1)));
            model.addAttribute("amtMin", amtMin);
            model.addAttribute("amtMax", amtMax);
        }
        model.addAttribute("transTime", redeemDate);
        return "chowmatistic/listBisAccDtlQuerySelect";
    }

    @PostMapping("/updateRedeemDetail")
    @ResponseBody
    public AjaxJson updateRedeemDetail(String redeemDetailId, BigDecimal incomeMoney, BigDecimal incomeRate){
        choProductRedeemService.updateRedeemDetail(redeemDetailId, incomeMoney, incomeRate);
        return success();
    }
}
