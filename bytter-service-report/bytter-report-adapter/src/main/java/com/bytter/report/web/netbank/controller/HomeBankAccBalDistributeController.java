package com.bytter.report.web.netbank.controller;
import com.bytter.biz.payment.api.service.IBtBankAccBalService;
import com.bytter.core.base.BaseController;
import com.bytter.report.domain.netbank.service.HomeBankAccBalDistributeService;
import com.bytter.report.api.vo.HomeBankAccBalDistributeSearchVO;
import org.apache.dubbo.config.annotation.DubboReference;
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
 * 银行账户余额分布DController
 *
 * @author huangx 2022-3-28
 */
@Controller
@RequestMapping("/bi/homeBankAccBalDistribute")
public class HomeBankAccBalDistributeController extends BaseController {
    @Autowired
    private HomeBankAccBalDistributeService bankAccBalDistributeService;
    @DubboReference(check = false)
	private IBtBankAccBalService btBankAccBalService;

    /**
     * 全景透视
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        //初始化余额
        btBankAccBalService.initBankAccBal();
        HomeBankAccBalDistributeSearchVO searchVO = new HomeBankAccBalDistributeSearchVO();
        //报表默认金额单位：万元
        searchVO.setMoneyType(10000);
        bankAccBalDistributeService.queryBankAccBalDistribute(model, searchVO, this.getCurrentUser());
        return "netbank/listBankAccBalDistribute";
    }
    /**
     * 银行账户余额初始化
     * @param searchVO
     * @return
     * @throws IOException
     */
    @PostMapping("/initBankAccBal")
    @ResponseBody
    public Object initBankAccBal(HomeBankAccBalDistributeSearchVO searchVO) throws IOException {
        Map<String, Object> dataMap = bankAccBalDistributeService.initBankAccBal(searchVO, this.getCurrentUser());
        return dataMap;
    }
    /**
     * 银行账户余额趋势图
     * @param searchVO
     * @return
     * @throws IOException
     */
    @PostMapping("/createBankAccBalDistributeBar")
    @ResponseBody
    public Object createBankAccBalDistributeBar(HomeBankAccBalDistributeSearchVO searchVO) throws IOException {
        return this.bankAccBalDistributeService.createBankAccBalDistributeBar(searchVO, this.getCurrentUser());
    }
    /**
     * 银行账户余额分布图
     * @param searchVO
     * @return
     * @throws IOException
     */
    @PostMapping("/createBankAccBalBar")
    @ResponseBody
    public Object createBankAccBalBar(HomeBankAccBalDistributeSearchVO searchVO) throws IOException {
        return this.bankAccBalDistributeService.createBankAccBalBar(searchVO, this.getCurrentUser());
    }
}
