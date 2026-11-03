package com.bytter.system.saas.controller.web;

import com.bytter.basic.service.BtBankTypeService;
import com.bytter.basic.vo.BankTypeVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

/**
 * @Description: 银行类型维护（SaaS）
 */
@Api(value = "/v1-system/basic/business/btbanktype", tags = "银行类型维护")
@RestController("v1BtBankTypeController")
@RequestMapping("/v1-system/basic/business/btbanktype")
public class BtBankTypeController extends BaseController {

    @Autowired
    private BtBankTypeService btBankTypeService;

    /**
     * 获取有效银行类型列表
     */
    @ApiOperation("获取有效银行类型列表")
    @GetMapping("/json")
    public AjaxJson<List<BankTypeVO>> json() {
        List<BankTypeVO> list = BeanUtils.copyListProperties(btBankTypeService.getListByValidSign("1"), BankTypeVO.class);
        return AjaxJson.success(sortByBankType(list));
    }

    /**
     * 根据是否境内获取银行类型列表
     */
    @ApiOperation("根据是否境内获取银行类型列表")
    @GetMapping("/json/{isDomesticBank}")
    public AjaxJson<List<BankTypeVO>> jsonByIsDomestic(@PathVariable("isDomesticBank") String isDomesticBank) {
        List<BankTypeVO> list = BeanUtils.copyListProperties(btBankTypeService.getListByIsDomesticBank(isDomesticBank), BankTypeVO.class);
        return AjaxJson.success(sortByBankType(list));
    }

    private List<BankTypeVO> sortByBankType(List<BankTypeVO> list) {
        if (EmptyUtils.isNotEmpty(list)) {
            list.sort(Comparator.comparing(BankTypeVO::getBankType, Comparator.nullsLast(Comparator.naturalOrder())));
        }
        return list;
    }
}
