package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaPledgeOccupyService;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.guarantee.domain.vo.GuaPledgeOccupyVO;
import com.bytter.guarantee.domain.vo.ParamsVO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaPledgeOccupyController
 * @ProjectName T3(3.2)
 * @Description: 抵质押物占用控制层
 * @date 2020/7/7 9:44
 */
@Controller
@RequestMapping("/cms/guarantee/pledgeOccupy")
public class GuaPledgeOccupyController extends BaseController {
    @Autowired
    private GuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @DubboReference(check = false)
    private IConfigService configService;
    @GetMapping("/addGuaPledgeOccupy")
    @LogMessage(content = "跳转到选择抵质押物页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String addGuaPledge(ParamsVO vo, Model model) {
        model.addAttribute("vo", vo);
        List<GuaPledgeInfoVO> list = guaContractInfoService.convertPledgeInfoList(vo.getPledgeMsg());
        model.addAttribute("list", list);
        model.addAttribute("relateFlag", true);
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        model.addAttribute("config", configDTO);
        return "cms/guarantee/occupyPledgePage";
    }
    @GetMapping("/viewPledgeOccupy")
    @LogMessage(content = "跳转到查看选择的抵质押物页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String viewPledgeList(ParamsVO vo, Model model) {
        List<GuaPledgeOccupyDTO> list = pledgeOccupyService.convertPledgeInfoList(vo.getPledgeMsg());
        model.addAttribute("pledgeList", list);
        model.addAttribute("viewFlag", "0");
        return "cms/guarantee/viewPledgeList";
    }
    /**
     * 通过抵质押id得到抵质押被担保合同占用的信息
     * 
     * @param id
     * @return
     */
    @GetMapping("/viewOcuppyList/{id}")
    public String viewGuaPledgeOccupyInfo(@PathVariable("id") String id, Model model) {
        if (!EmptyUtils.isBlank(id)) {
            List<GuaPledgeOccupyVO> occupyList = pledgeOccupyService.getPledgeOccupyList(id);
            model.addAttribute("occupyList", occupyList);
        }
        return "cms/guarantee/viewGuaPledgeOccupyList";
    }
}
