package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.GuaWay;
import com.bytter.guarantee.domain.service.GuaWayService;
import com.bytter.guarantee.domain.vo.GuaWayVO;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 担保方式维护
 */
@Api(tags = "担保方式维护", value = "/v1-guarantee/cms/guarantee/guaway")
@RestController("v1GuaWayController")
@RequestMapping("/v1-guarantee/cms/guarantee/guaway")
public class GuaWayController extends BaseController {
    @Autowired
    private GuaWayService guaWayService;

    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 担保方式维护列表查询
     *
     * @param input 查询参数
     * @return 列表数据
     */
    @ApiOperation(value = "担保方式列表查询")
    @PostMapping("/dataTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<GuaWayVO>> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        DataTablesOutput<GuaWayVO> output = guaWayService.dataTable(dataTablesInput);
        Map<String, DictDTO> typeMap = dictService.listMap("guarantee.pledge.TypeAttr");
        output.getData().forEach(item -> {
            item.setIsPledgeName("1".equals(item.getIsPledge()) ? "是" : "否");
            if(StringUtils.isNotEmpty(item.getPledgeProperty())){
                item.setPledgePropertyName(typeMap.get(item.getPledgeProperty()).getName());
            }
        });

        return success(output);
    }

    /**
     * 担保方式维护编辑页面跳转------
     */
    @ResponseBody
    @GetMapping("/update/{id}")
    public AjaxJson<GuaWay> update(@PathVariable String id) {
        if (StringUtils.isNotEmpty(id)) {
            GuaWay guaWay = guaWayService.get(id, true);
            return success(guaWay);
        }
        return success();
    }
    /**
     * 添加担保方式维护
     *
     * @param guaWay 担保方式信息
     * @return 结果
     */
    @LogMessage(content = "添加担保方式维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody GuaWay guaWay) {
        guaWayService.save(guaWay, getUserId());
        return success();
    }
    /**
     * 修改担保方式维护
     */
    @LogMessage(content = "修改担保方式维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody GuaWay guaWay) {
        guaWayService.update(guaWay, getUserId());
        return success();
    }
    /**
     * 删除担保方式维护
     */
    @LogMessage(content = "删除担保方式维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        guaWayService.deleteGuaWay(id);
        return success();
    }
    /**
     * @return
     */
    @ApiOperation(value = "查询所有担保方式")
    @GetMapping("/listGuaWay")
    @ResponseBody
    public AjaxJson<List<GuaWay>> listGuaWay() {
        return success(guaWayService.listAll());
    }
}
