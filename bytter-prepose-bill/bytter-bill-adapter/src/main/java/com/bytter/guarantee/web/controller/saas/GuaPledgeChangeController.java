package com.bytter.guarantee.web.controller.saas;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.guarantee.domain.service.GuaPledgeChangeService;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoHisVO;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.guarantee.domain.vo.GuaPledgePageVO;
import com.bytter.system.api.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author luxy
 * @version 1.0
 * @Title:GuaPledgeChangeController
 * @Description:抵质押物变更控制层
 * @date 2018年12月12日 下午1:42:09
 */
@Api(tags = "抵质押物变更",value = "/v1-guarantee/cms/guarantee/pledgeChange")
@RestController("v1GuaPledgeChangeController")
@RequestMapping("/v1-guarantee/cms/guarantee/pledgeChange")
public class GuaPledgeChangeController extends BaseController {
    @Autowired
    private GuaPledgeChangeService pledgeChangeService;
    @DubboReference(check = false)
    private IUserService userService;

    /**
     * 抵质押物变更列表查询
     *
     * @param input 查询参数
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "抵质押物变更列表查询")
    @PostMapping("/datalist")
    @ResponseBody
    public AjaxJson<DataTablesOutput> datalist(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return success(pledgeChangeService.queryDataList(dataTablesInput, this.getCurrentUser()));
    }
    /**
     * 变更页面
     */
    @ApiOperation(value = "抵质押物变更页面")
    @GetMapping("/addChange/{id}")
    public AjaxJson<GuaPledgePageVO> addChange(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgePageVO pageVO = new GuaPledgePageVO();
            GuaPledgeInfoVO guaPledge = pledgeChangeService.queryPledgeInfoById(id);
            pageVO.setGuaPledge(guaPledge);
            if (pledgeChangeService.checkData(id)) {// 数据为抵押且被担保合同占用
                pageVO.setFlag("1");
            }
            return success(pageVO);
        }
        return success();
    }
    /**
     * 保存抵质押物变更信息
     */
    @ApiOperation(value = "保存抵质押物变更信息")
    @PostMapping("/saveChange")
    @ResponseBody
    public AjaxJson saveChange(@RequestBody GuaPledgeInfoHisVO pledgeInfoHisVO) {
        pledgeChangeService.savePledgeChangeHis(pledgeInfoHisVO, this.getCurrentUser());
        return success();
    }
    /**
     * 根据抵质押物id查询抵质押物信息
     */
    @ApiOperation(value = "根据抵质押物id查询抵质押物信息")
    @GetMapping("/viewchanged/{id}")
    public AjaxJson<GuaPledgeInfoVO> viewpledgeChange(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgePageVO guaPledgePageVO = new GuaPledgePageVO();
            GuaPledgeInfoVO guaPledge = pledgeChangeService.queryPledgeInfoById(id);
            guaPledgePageVO.setGuaPledge(guaPledge);
            return success(guaPledgePageVO);
        }
        return success();
    }

    /**
     * 通过担保ID查询相应的历史信息
     *
     * @param input
     * @param guaId
     * @return
     */
    @ApiOperation(value = "通过担保ID查询相应的历史信息")
    @RequestMapping("/listguadata")
    @ResponseBody
    public AjaxJson<DataTablesOutput> listGuaLogInfo(@RequestBody V1DataTablesInput input, String guaId) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return success(pledgeChangeService.queryChangeHisList(dataTablesInput, guaId));
    }
    /**
     * 根据历史抵质押物id查询历史变更抵质押物信息
     */
    @ApiOperation(value = "根据历史抵质押物id查询历史变更抵质押物信息")
    @GetMapping("/viewHischanged/{id}")
    public AjaxJson<GuaPledgePageVO> viewpledgeChangeHis(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgePageVO pageVO = new GuaPledgePageVO();
            GuaPledgeInfoVO guaPledge = pledgeChangeService.queryHisPledgeInfoById(id);
            if (StringUtils.isNotEmpty(guaPledge.getCreateBy())) {
                guaPledge.setCreateBy(userService.getByUserId(guaPledge.getCreateBy()).getUsername());
            }
            pageVO.setGuaPledge(guaPledge);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 判断抵质押物是否已被担保合同占用
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "判断抵质押物是否已被担保合同占用")
    @GetMapping("/checkData")
    @ResponseBody
    public AjaxJson<Boolean> checkData(String id) {
        return success(pledgeChangeService.checkData(id));
    }
}
