package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.guarantee.domain.service.ConTypeFieldService;
import com.bytter.guarantee.domain.vo.ConTypeFieldVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: 合同类型字段明细控制类
 * @Description: 合同类型字段明细控制类
 * @author: zhenghj
 * @date: 2019-11-6
 * @version V1.0
 */
@Api(value = "/v1-guarantee/contract/conTypeField", tags = "合同类型字段明细")
@RestController("v1ConTypeFieldController")
@RequestMapping("/v1-guarantee/contract/conTypeField")
public class ConTypeFieldController extends BaseController {
    @Autowired
    private ConTypeFieldService conTypeFieldService;

    /**
     * 根据id获取合同类型字段列表
     */
    @ApiOperation("根据id获取合同类型字段列表")
    @PostMapping("/getConTypeFieldById")
    public List<ConTypeFieldVO> getConTypeFieldById(String id) {
        return conTypeFieldService.getConTypeFieldById(id);
    }
}