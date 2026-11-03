package com.bytter.guarantee.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.guarantee.domain.service.GuaPledgeChangeService;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoHisVO;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.system.api.IUserService;
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
@Controller
@RequestMapping("/cms/guarantee/pledgeChange")
public class GuaPledgeChangeController extends BaseController {
    @Autowired
    private GuaPledgeChangeService pledgeChangeService;
    @DubboReference(check = false)
    private IUserService userService;
    @GetMapping("/list")
    public String pledgeChangelist() {
        return "cms/guarantee/listGuaPledgeChange";
    }
    /**
     * 抵质押物变更列表查询
     *
     * @param input 查询参数
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datalist")
    @ResponseBody
    public DataTablesOutput datalist(@RequestBody DataTablesInput input) {
        return pledgeChangeService.queryDataList(input, this.getCurrentUser());
    }
    /**
     * 变更页面
     */
    @GetMapping("/addChange/{id}")
    public String addChange(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgeInfoVO guaPledge = pledgeChangeService.queryPledgeInfoById(id);
            model.addAttribute("guaPledge", guaPledge);
            if (pledgeChangeService.checkData(id)) {// 数据为抵押且被担保合同占用
                model.addAttribute("flag", "1");
            }
        }
        return "cms/guarantee/addGuaPledgeChange";
    }
    /**
     * 保存抵质押物变更信息
     */
    @PostMapping("/saveChange")
    @ResponseBody
    public AjaxJson saveChange(GuaPledgeInfoHisVO pledgeInfoHisVO) {
        pledgeChangeService.savePledgeChangeHis(pledgeInfoHisVO, this.getCurrentUser());
        return success();
    }
    /**
     * 根据抵质押物id查询抵质押物信息
     */
    @GetMapping("/viewchanged/{id}")
    public String viewpledgeChange(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgeInfoVO guaPledge = pledgeChangeService.queryPledgeInfoById(id);
            model.addAttribute("guaPledge", guaPledge);
        }
        return "cms/guarantee/viewGuaPledgeChange";
    }
    /**
     * 根据抵质押物id查询抵质押物信息
     */
    @GetMapping("/changedlist/{id}")
    public String pledgeChangeHisList(@PathVariable("id") String id, Model model) {
        model.addAttribute("guaId", id);
        return "cms/guarantee/listGuaPledgeChangeHis";
    }
    /**
     * 通过担保ID查询相应的历史信息
     *
     * @param input
     * @param guaId
     * @return
     */
    @RequestMapping("/listguadata")
    @ResponseBody
    public DataTablesOutput listGuaLogInfo(@RequestBody DataTablesInput input, String guaId) {
        return pledgeChangeService.queryChangeHisList(input, guaId);
    }
    /**
     * 根据历史抵质押物id查询历史变更抵质押物信息
     */
    @GetMapping("/viewHischanged/{id}")
    public String viewpledgeChangeHis(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaPledgeInfoVO guaPledge = pledgeChangeService.queryHisPledgeInfoById(id);
            if (StringUtils.isNotEmpty(guaPledge.getCreateBy())) {
                guaPledge.setCreateBy(userService.getByUserId(guaPledge.getCreateBy()).getUsername());
            }
            model.addAttribute("guaPledge", guaPledge);
        }
        return "cms/guarantee/viewGuaPledgeChange";
    }
    /**
     * 判断抵质押物是否已被担保合同占用
     *
     * @param id
     * @return
     */
    @PostMapping("/checkData")
    @ResponseBody
    public String checkData(String id) {
        if (pledgeChangeService.checkData(id)) {
            return "true";
        } else {
            return "false";
        }
    }
}
