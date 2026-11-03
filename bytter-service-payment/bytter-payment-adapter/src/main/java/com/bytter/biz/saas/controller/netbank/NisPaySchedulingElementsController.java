package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.netbank.entity.NisPaySchedulingElements;
import com.bytter.biz.netbank.service.NisPaySchedulingElementsService;
import com.bytter.biz.netbank.vo.NisPaySchedulingElementsVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 付款排期要素
 */
@Api(value = "/v1-payment/netbank/nispayschedulingelements", tags = "付款排期要素")
@RestController("v1NisPaySchedulingElementsController")
@RequestMapping("/v1-payment/netbank/nispayschedulingelements")
public class NisPaySchedulingElementsController extends BaseController {
    @Autowired
    private NisPaySchedulingElementsService nisPaySchedulingElementsService;

    /**
     * 查询付款排期要素列表
     *
     * @return
     */
    @ApiOperation("查询付款排期要素列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisPaySchedulingElementsVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisPaySchedulingElementsService.dataTable(input));
    }


    /**
     * 添加付款排期要素
     *
     * @param nisPaySchedulingElements
     * @return
     */
    @ApiOperation("添加付款排期要素")
    @LogMessage(content = "添加业务类别表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody NisPaySchedulingElements nisPaySchedulingElements) {
        nisPaySchedulingElements.setCreateBy(this.getCurrentUser().getLoginName());
        nisPaySchedulingElements.setCreateDate(DateUtils.getDate());
        nisPaySchedulingElements.setStatus(NetbankConstant.STATUE_CHECK);
        nisPaySchedulingElementsService.saveEntity(nisPaySchedulingElements);
        return success();
    }

    /**
     * 付款排期要素编辑页面跳转
     *
     * @return
     */
    @ApiOperation("付款排期要素编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<NisPaySchedulingElements> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(nisPaySchedulingElementsService.get(id, true));
        }
        return AjaxJson.success();
    }

    /**
     * 修改付款排期要素
     *
     * @param nisPaySchedulingElements
     * @return
     */
    @ApiOperation("修改付款排期要素")
    @LogMessage(content = "修改业务类别表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody NisPaySchedulingElements nisPaySchedulingElements) {
        NisPaySchedulingElements t = nisPaySchedulingElementsService.get(nisPaySchedulingElements.getId(), true);
        nisPaySchedulingElements.setUpdateBy(this.getCurrentUser().getLoginName());
        nisPaySchedulingElements.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(nisPaySchedulingElements, t);
        nisPaySchedulingElementsService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除付款排期要素
     *
     * @param id
     * @return
     */
    @ApiOperation("删除业务类别表")
    @LogMessage(content = "删除业务类别表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        NisPaySchedulingElements nisPaySchedulingElements = nisPaySchedulingElementsService.get(id, true);
        nisPaySchedulingElements.setUpdateBy(this.getCurrentUser().getLoginName());
        nisPaySchedulingElements.setUpdateDate(DateUtils.getDate());
        nisPaySchedulingElements.setStatus(NetbankConstant.STATUE_DEL);
        nisPaySchedulingElementsService.saveOrUpdate(nisPaySchedulingElements);
        return success();
    }

    /**
     * 获取付款排期要素
     *
     * @return
     */
    @ApiOperation("获取付款排期要素")
    @RequestMapping(path = "/getByList")
    public AjaxJson<List<NisPaySchedulingElementsVO>> getByList() {
        return success(nisPaySchedulingElementsService.list());
    }
}
