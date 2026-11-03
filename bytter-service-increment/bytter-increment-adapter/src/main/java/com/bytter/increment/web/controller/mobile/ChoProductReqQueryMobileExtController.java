package com.bytter.increment.web.controller.mobile;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.domain.service.ChoProductReqQueryService;
import com.bytter.increment.domain.vo.ChoProductReqUncollectedVo;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/mobile/chowmatistic/choproductreqquery")
public class ChoProductReqQueryMobileExtController extends BaseController {

    @Autowired
    private ChoProductReqQueryService choProductReqQueryService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 理财管理App跳转首页
     * @param model
     * @return
     */
    @PostMapping("/appIndex")
    public String appIndex(Model model){
        return "mobile/chowmatistic/appChowmatisticReportIndex";
    }

    /**
     * 理财管理报表显示
     *
     * @return
     */
    @RequestMapping("/mobileReq")
    public String mobileReq(Model model, ChoProductReqUncollectedVo reqVo) {
        List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
        model.addAttribute("curList", curList);
        model.addAttribute("curId",reqVo.getSign());
        return "mobile/chowmatistic/mobileReq";
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
        return "mobile/chowmatistic/mobileViewReq";
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
}
