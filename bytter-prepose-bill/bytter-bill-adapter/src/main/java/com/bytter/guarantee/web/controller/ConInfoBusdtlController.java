package com.bytter.guarantee.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.service.ConInfoBusdtlService;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * @version V1.0
 * @Title: 合同字段明细控制类
 * @Description: 合同字段明细控制类
 * @author: zhenghj
 * @date: 2019-11-10
 */
@Controller
@RequestMapping("/contract/conInfoBusdtl")
public class ConInfoBusdtlController extends BaseController {
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    /**
     * 获取维护的有效合同类型
     *
     * @return
     */
    @PostMapping("/getConInfoBusdtlByMid")
    @ResponseBody
    public List<ConInfoBusdtlVO> getConInfoBusdtlByMid(String mid) {
        List<ConInfoBusdtlVO> busdtlList = null;
        List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService.getConInfoBusDtl(new ConInfoBusdtlVO(mid));
        if (ListUtils.isNotNullOrEmpty(conBusdtlList)) {
            busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
        }
        return busdtlList;
    }
}
