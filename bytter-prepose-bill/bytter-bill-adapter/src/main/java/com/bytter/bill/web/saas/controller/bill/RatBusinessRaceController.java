package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.vo.v1.RatBusinessRaceUpdateVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.entity.RatBusinessRace;
import com.bytter.credit.domain.service.RatBusinessRaceService;
import com.bytter.credit.domain.service.RatItemCustomService;
import com.bytter.credit.domain.vo.RatBusinessRaceVO;
import com.bytter.credit.domain.vo.RatItemCustomVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授信业务品种维护
 */
@Api(value = "/v1-preposebill/cms/rat/ratBusinessRace", tags = "授信业务品种维护")
@RestController("v1RatBusinessRaceController")
@RequestMapping("/v1-preposebill/cms/rat/ratBusinessRace")
public class RatBusinessRaceController extends BaseController {
    @Autowired
    private RatBusinessRaceService ratBusinessRaceService;
    @Autowired
    private RatItemCustomService ratItemCustomService;

    @PostMapping("/dataTable")
    public AjaxJson<DataTablesOutput<RatBusinessRaceVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(ratBusinessRaceService.dataTable(input));
    }

    /**
     * 业务种类新增页面跳转-----
     */
    @GetMapping("/add")
    public AjaxJson<Map<String, String>> add() {
        Map<String, String> model = new HashMap<>();
        model.put("a", "a");
        return AjaxJson.success(model);
    }

    /**
     * 业务种类编辑页面跳转------
     */
    @GetMapping("/update/{id}")
    public AjaxJson<RatBusinessRaceUpdateVO> update(@PathVariable String id) {
        RatBusinessRaceUpdateVO model = new RatBusinessRaceUpdateVO();
        if (StringUtils.isNotEmpty(id)) {
            RatBusinessRace ratRace = ratBusinessRaceService.get(id, true);
            List<RatItemCustomVO> customVOS = ratItemCustomService.getRatCustomByBank(ratRace.getCustomId());
            model.setRatRace(ratRace);
            model.setCustomVOS(customVOS.get(0));
        }
        return AjaxJson.success(model);
    }

    /**
     * 添加业务种类
     */
    @LogMessage(content = "添加授信业务种类", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatBusinessRace fcBusRacePool) {
        ratBusinessRaceService.save(fcBusRacePool, getUserId());
        return success();
    }

    /**
     * 修改业务种类
     */
    @LogMessage(content = "修改授信业务种类", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatBusinessRace fcBusRace) {
        ratBusinessRaceService.update(fcBusRace, getUserId());
        return success();
    }

    /**
     * 删除业务种类
     */
    @LogMessage(content = "删除授信业务种类", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ratBusinessRaceService.deleteRatBusinessRace(id);
        return success();
    }

    /**
     * 获取有效银行授信品种
     */
    @GetMapping("/getBankCustom")
    public AjaxJson<List<RatItemCustomVO>> getBankCustom() {
        return AjaxJson.success(ratItemCustomService.getRatCustomByBank(null));
    }

    /**
     * 根据授信品种获取业务名称
     */
    @GetMapping("/getBillByCustomId")
    public AjaxJson<List<RatBusinessRaceVO>> getBillByCustomId(String customId) {
        List<RatBusinessRaceVO> ratBusinessRaceVOS = new ArrayList<RatBusinessRaceVO>();
        if (customId != null) {
            ratBusinessRaceVOS = ratBusinessRaceService.findAllByCustomId(customId);
        }
        return AjaxJson.success(ratBusinessRaceVOS);
    }
}
