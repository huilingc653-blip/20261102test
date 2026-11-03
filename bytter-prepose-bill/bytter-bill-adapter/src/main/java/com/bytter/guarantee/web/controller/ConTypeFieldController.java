package com.bytter.guarantee.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.guarantee.domain.service.ConTypeFieldService;
import com.bytter.guarantee.domain.vo.ConTypeFieldVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * 
 * @Title: 合同类型字段明细控制类
 * @Description: 合同类型字段明细控制类
 * @author: zhenghj
 * @date: 2019-11-6
 * @version V1.0
 */
@Controller
@RequestMapping("/contract/contractTypeField")
public class ConTypeFieldController extends BaseController {
    @Autowired
    private ConTypeFieldService conTypeFieldService;
    /**
     * 获取维护的有效合同类型
     *
     * @return
     */
    @PostMapping("/getConTypeFieldById")
    @ResponseBody
    public List<ConTypeFieldVO> getConTypeFieldById(String id) {
        return conTypeFieldService.getConTypeFieldById(id);
    }
}
