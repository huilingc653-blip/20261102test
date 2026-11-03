package com.bytter.guarantee.web.controller.saas;

import com.bytter.bill.domain.bill.vo.v1.PledgeOccupyAddPageVO;
import com.bytter.bill.domain.bill.vo.v1.PledgeOccupyPageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author luxy
 * @Title: GuaPledgeOccupyController
 * @ProjectName T3(3.2)
 * @Description: 抵质押物占用控制层
 * @date 2020/7/7 9:44
 */
@Api(tags = "抵质押物占用接口",value = "/v1-guarantee/cms/guarantee/pledgeOccupy")
@RestController("v1GuaPledgeOccupyController")
@RequestMapping("/v1-guarantee/cms/guarantee/pledgeOccupy")
public class GuaPledgeOccupyController extends BaseController {
    @Autowired
    private GuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @DubboReference(check = false)
    private IConfigService configService;
    @ApiOperation(value = "跳转到选择抵质押物页面")
    @GetMapping("/addGuaPledgeOccupy")
    @LogMessage(content = "跳转到选择抵质押物页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<PledgeOccupyAddPageVO> addGuaPledge(ParamsVO vo) {
        PledgeOccupyAddPageVO pageVO = new PledgeOccupyAddPageVO();
        List<GuaPledgeInfoVO> list = guaContractInfoService.convertPledgeInfoList(vo.getPledgeMsg());
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        pageVO.setVo(vo);
        pageVO.setRelateFlag(true);
        pageVO.setConfig(configDTO);
        pageVO.setList( list);
        return success(pageVO);
    }
    @ApiOperation(value = "跳转到查看选择的抵质押物数据查看")
    @GetMapping("/viewPledgeOccupy")
    @LogMessage(content = "跳转到查看选择的抵质押物页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<PledgeOccupyPageVO> viewPledgeList(ParamsVO vo) {
        PledgeOccupyPageVO pledgeOccupyPageVO = new PledgeOccupyPageVO();
        List<GuaPledgeOccupyDTO> list = pledgeOccupyService.convertPledgeInfoList(vo.getPledgeMsg());
        pledgeOccupyPageVO.setPledgeList( list);
        pledgeOccupyPageVO.setViewFlag("0");
        return success(pledgeOccupyPageVO);
    }
    /**
     * 通过抵质押id得到抵质押被担保合同占用的信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "通过抵质押id得到抵质押被担保合同占用的信息")
    @GetMapping("/viewOcuppyList/{id}")
    public AjaxJson<List<GuaPledgeOccupyVO>> viewGuaPledgeOccupyInfo(@PathVariable("id") String id) {
        if (!EmptyUtils.isBlank(id)) {
            List<GuaPledgeOccupyVO> occupyList = pledgeOccupyService.getPledgeOccupyList(id);
            return success(occupyList);
        }
        return success();
    }
}
