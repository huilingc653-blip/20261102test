package com.bytter.system.saas.controller.web;

import com.bytter.basic.entity.BtPurpose;
import com.bytter.basic.service.BtPurposeService;
import com.bytter.basic.vo.BtPurposeCorpVO;
import com.bytter.basic.vo.BtPurposeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 接口代码维护
 * @date 2017-05-19 17:07:12
 */
@Api(value = "/v1-system/basic/business/btpurpose", tags = "接口代码维护")
@RestController("v1BtPurposeController")
@RequestMapping("/v1-system/basic/business/btpurpose")
public class BtPurposeController extends BaseController {
    @Autowired
    private BtPurposeService btPurposeService;

    /**
     * 用途维护表 table列表
     *
     * @return
     */
    @ApiOperation("用途维护表table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BtPurposeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(btPurposeService.getDataTablesUnion(input));
    }


    /**
     * 编辑页面跳转
     *
     * @return
     */
    @ApiOperation("编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<BtPurposeVO> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(this.btPurposeService.getByUnion(id));
        }
        return AjaxJson.success();
    }

    /**
     * 查看详情页
     *
     * @return
     */
    @ApiOperation("查看详情页")
    @LogMessage(content = "查询接口代码维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BtPurposeVO> view(@PathVariable("id") String id) {
        BtPurposeVO btPurpose = this.btPurposeService.getByUnion(id);
        return AjaxJson.success(btPurpose);
    }

    /**
     * 添加用途维护表
     *
     * @param vo
     * @return
     */
    @ApiOperation("添加用途维护表")
    @LogMessage(content = "添加用途维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody BtPurposeVO vo) {
        btPurposeService.saveBtPurpose(vo, getUserLoginName());
        return success();
    }

    /**
     * 修改用途维护表
     *
     * @param vo
     * @return
     */
    @ApiOperation("修改用途维护表")
    @LogMessage(content = "修改用途维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody BtPurposeVO vo) {
        btPurposeService.updateBtPurpose(vo, getUserLoginName());
        return success();
    }

    /**
     * 删除用途维护表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除用途维护表")
    @LogMessage(content = "删除用途维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        BtPurpose btPurpose = btPurposeService.get(id);
        btPurposeService.deleteEntity(btPurpose);
        return success();
    }

    /**
     * 批量删除用途维护表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除用途维护表")
    @LogMessage(content = "批量删除用途维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtPurpose btPurpose = btPurposeService.get(id);
            btPurposeService.deleteEntity(btPurpose);
        }
        return success();
    }

    /**
     * 获取单位用途
     *
     * @param corpId
     * @param sysBusType
     * @return
     */
    @ApiOperation("获取单位用途")
    @GetMapping(path = "/getPurpose")
    public AjaxJson getPurpose(String corpId, String sysBusType) {
        return AjaxJson.success(btPurposeService.listBy(corpId, sysBusType));
    }

    /**
     * 获取单位用途
     *
     * @param corpId
     * @param sysBusType
     * @return
     */
    @ApiOperation("获取单位用途")
    @GetMapping(value ={ "getByList","getByListSort"})
    public AjaxJson<List<BtPurposeVO>> getByList(String corpId, String sysBusType) {
        return AjaxJson.success(btPurposeService.listBy(corpId, sysBusType));
    }


    @ApiOperation("根据用途ID加载所属单位的信息,并进行排序")
    @GetMapping("/corpId.json/{id}")
    public AjaxJson<List<BtPurposeCorpVO>> getCorpIdByPurposeId(@PathVariable("id") String id) {
        return btPurposeService.getCorpIdByPurposeId(id);
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<BtPurposeVO> list) {
        this.btPurposeService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 获取单位用途(外汇)
     *
     * @param corpId
     * @param sysBusType
     * @return
     */
    @RequestMapping(params = "getByListForFex")
    public AjaxJson<List<BtPurposeVO>> getByListForFex(String corpId, String sysBusType, String curId, String countryId, String bankTypeId) {
        return AjaxJson.success(btPurposeService.listBy(corpId, sysBusType, curId, countryId, bankTypeId));
    }
}
