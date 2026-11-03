package com.bytter.report.web.netbank.controller;
import com.bytter.report.domain.netbank.service.HomeBankAccDtlDistributeService;
import com.bytter.report.domain.netbank.vo.HomeBankAccDtlDistributeSearchVO;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.util.Map;
/**
 * 结算收支分析Controller
 * @author huangx 2022-3-28
 */
@Controller
@RequestMapping("/bi/homeBankAccDtlDistribute")
public class HomeBankAccDtlDistributeController extends BaseController {
    @Autowired
    private HomeBankAccDtlDistributeService bankAccDtlDistributeService;
    @GetMapping("/list")
    public String list(Model model) {
        HomeBankAccDtlDistributeSearchVO searchVO = new HomeBankAccDtlDistributeSearchVO();
        //报表默认金额单位：万元
        searchVO.setMoneyType(10000);
        bankAccDtlDistributeService.queryBankAccDtlDistribute(model, searchVO, this.getCurrentUser());
        return "netbank/listBankAccDtlDistribute";
    }
    /**
     * 收支明细初始化
     * @param searchVO
     * @return
     * @throws IOException
     */
    @PostMapping("/initBankAccDtl")
    @ResponseBody
    public Object initBankAccDtl(HomeBankAccDtlDistributeSearchVO searchVO) throws IOException {
        Map<String, Object> dataMap = bankAccDtlDistributeService.initBankAccDtl(searchVO, this.getCurrentUser());
        return dataMap;
    }
    /**
     * 银行账户余额趋势图
     * @param searchVO
     * @return
     * @throws IOException
     */
    @PostMapping("/createBankAccDtlDistributeBar")
    @ResponseBody
    public Object createBankAccDtlDistributeBar(HomeBankAccDtlDistributeSearchVO searchVO) throws IOException {
        return this.bankAccDtlDistributeService.createBankAccDtlDistributeBar(searchVO, this.getCurrentUser());
    }
}
