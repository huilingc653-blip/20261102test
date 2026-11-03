package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.entity.RatBusinessRace;
import com.bytter.credit.domain.service.RatBusinessRaceService;
import com.bytter.credit.domain.service.RatItemCustomService;
import com.bytter.credit.domain.vo.RatBusinessRaceVO;
import com.bytter.credit.domain.vo.RatItemCustomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 授信业务品种维护
 */
@Controller
@RequestMapping("/cms/rat/ratBusinessRace")
public class RatBusinessRaceController extends BaseController {
    @Autowired
    private RatBusinessRaceService ratBusinessRaceService;
    @Autowired
    private RatItemCustomService ratItemCustomService;
    @GetMapping("/list")
    public String list() {
        return "cms/rat/businessrace/listRatBusinessRace";
    }
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return ratBusinessRaceService.dataTable(input);
    }
    /**
     * 业务种类新增页面跳转-----
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("a", "a");
        return "cms/rat/businessrace/addRatBusRace";
    }
    /**
     * 业务种类编辑页面跳转------
     */
    @GetMapping("/update")
    public String update(RatBusinessRace race, Model model) {
        if (StringUtils.isNotEmpty(race.getId())) {
            RatBusinessRace ratRace = ratBusinessRaceService.get(race.getId(), true);
            List<RatItemCustomVO> customVOS = ratItemCustomService.getRatCustomByBank(ratRace.getCustomId());
            model.addAttribute("ratRace", ratRace);
            model.addAttribute("customVO", customVOS.get(0));
        }
        return "cms/rat/businessrace/updateRatBusRace";
    }
    /**
     * 添加业务种类
     */
    @LogMessage(content = "添加授信业务种类", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatBusinessRace fcBusRacePool) {
        ratBusinessRaceService.save(fcBusRacePool, getUserId());
        return success();
    }
    /**
     * 修改业务种类
     */
    @LogMessage(content = "修改授信业务种类", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RatBusinessRace fcBusRace) {
        ratBusinessRaceService.update(fcBusRace, getUserId());
        return success();
    }
    /**
     * 删除业务种类
     */
    @LogMessage(content = "删除授信业务种类", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ratBusinessRaceService.deleteRatBusinessRace(id);
        return success();
    }
    /**
     * 获取有效银行授信品种
     */
    @GetMapping("/getBankCustom")
    @ResponseBody
    public List<RatItemCustomVO> getBankCustom() {
        return ratItemCustomService.getRatCustomByBank(null);
    }
    /**
     * 根据授信品种获取业务名称
     */
    @GetMapping("/getBillByCustomId")
    @ResponseBody
    public List<RatBusinessRaceVO> getBillByCustomId(String customId) {
        List<RatBusinessRaceVO> ratBusinessRaceVOS = new ArrayList<RatBusinessRaceVO>();
        if (customId != null) {
            ratBusinessRaceVOS = ratBusinessRaceService.findAllByCustomId(customId);
        }
        return ratBusinessRaceVOS;
    }
}
