package com.bytter.finance.web.bond.controller;
import com.bytter.finance.domain.bond.service.CmsBondInterestService;
import com.bytter.finance.domain.bond.vo.CmsBondInterestVo;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 债券利息预计明细表
 * @date 2022-06-08 10:07:46
 */
@Controller
@RequestMapping("/cms/bond/cmsbondinterest")
public class CmsBondInterestController extends BaseController {
    @Autowired
    private CmsBondInterestService cmsBondInterestService;
    /**
     * 债券还款计划模块加载利息明细
     *
     * @param registerId
     *            债劵注册id
     * @return
     */
    @PostMapping("/listInterestByRegisterId")
    @ResponseBody
    public List<CmsBondInterestVo> listInterestByRegisterId(String registerId) {
        List<CmsBondInterestVo> list = cmsBondInterestService.getByRegisterId(registerId);
        return list;
    }
}
