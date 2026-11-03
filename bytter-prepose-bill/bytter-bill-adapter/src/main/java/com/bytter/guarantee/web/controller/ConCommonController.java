package com.bytter.guarantee.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.service.ConCorpService;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @version V1.0
 * @Title: 合同公共功能控制类
 * @Package com.bytter.biz.contract.controller
 * @author: zhengg
 * @date: 2019-11-6 下午2:19:43
 */
@Controller
@RequestMapping("/contract/conCommon")
public class ConCommonController extends BaseController {
    @Autowired
    private ConCommonService conCommonService;
    @Autowired
    private ConCorpService conCorpService;
    /**
     * 选择合同控制器入口
     *
     * @param params
     *            传递查询参数
     * @param model
     * @return
     */
    @GetMapping("/conSelect")
    public String conSelect(ConInfoVO params, Model model) {
        // 合同内部业务选票不用传递单位id和必须传递资源值过来进行 选票
        // 外部系统业务选票必须传递单位id和不需要传递资源值过来进行 选票
        if (StringUtils.isNotEmpty(params.getCorpId()) && StringUtils.isEmpty(params.getOpResource())) {
            CorpDTO corpInfo = conCorpService.get(params.getCorpId());
            model.addAttribute("corpInfo", corpInfo);
            // 单位id不为空和资源值为空值标识为外部系统业务选票标识
            model.addAttribute("isOutSystem", ConConstant.CON_BUSHOOK_VALID_SIGN_1);
        }
        model.addAttribute("params", params);
        return "contract/listConSelect";
    }
    /**
     * 选择合同 获取合同 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/conSelectData")
    @ResponseBody
    public DataTablesOutput conSelectData(@RequestBody DataTablesInput input) {
        return conCommonService.getConSelectData(input);
    }
    /**
     * 查看合同详细控制器入口
     *
     * @param params
     *            传递查询参数
     * @param model
     * @return
     */
    @GetMapping("/conView")
    public String conView(ConInfoVO params, Model model) {
        ConInfoDTO conInfo = conCommonService.getConInfo(params);
        model.addAttribute("conInfo", conInfo);
        return "contract/viewConBase";
    }
}
