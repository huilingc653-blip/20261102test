package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.vo.FcBankAccGroupVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IOperationAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 银行账户组维护Controller
 */
@Api(value = "/v1-payment/netbank/bankAccGroup", tags = "银行账号维护")
@RestController("v1FcBankAccGroupController")
@RequestMapping("/v1-payment/netbank/bankAccGroup")
public class FcBankAccGroupController extends BaseController {
    @Autowired
    private FcBankAccGroupService fcBankAccGroupService;

    @DubboReference(check = false)
    private IOperationAreaService operationAreaService;

    @Autowired
    private BtBankAccHandleService btBankAccHandleService;


    /**
     * 银行账户组列表 table列表
     *
     * @return
     */
    @ApiOperation(" 银行账户组列表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<FcBankAccGroupVO> >dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fcBankAccGroupService.getDataTablesUnion(input));
    }

    /**
     * 系统结算方式表新增页面跳转
     *
     * @return
     */
    @ApiOperation("系统结算方式表新增页面跳转")
    @GetMapping("/add/{id}")
    public AjaxJson<FcBankAccGroup> add(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(fcBankAccGroupService.get(id));
        }
        return AjaxJson.success();
    }

    /**
     * 系统结算方式表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("系统结算方式表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<FcBankAccGroup> update(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(fcBankAccGroupService.get(id));
        }
        return AjaxJson.success();
    }

    @LogMessage(content = "修改银行账号组", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody FcBankAccGroupVO fcBankAccGroupVO) {
        fcBankAccGroupService.update(fcBankAccGroupVO);
        return success();
    }

    /**
     * 系统结算方式表详情页
     *
     * @return
     */
	@ApiOperation("系统结算方式表详情页")
    @LogMessage(content = "单据类型详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<FcBankAccGroup>  view(@PathVariable("id") String id) {
		if (StringUtils.isNotEmpty(id)) {
			return AjaxJson.success(fcBankAccGroupService.get(id));
		}
		return AjaxJson.success();
    }

    /**
     * 添加系统结算方式表
     *
     * @return
     */
	@ApiOperation("添加系统结算方式表")
    @LogMessage(content = "添加系统结算方式表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody FcBankAccGroupVO fcBankAccGroupVO) {
        fcBankAccGroupService.save(fcBankAccGroupVO);
        return success();
    }



    /**
     * 删除系统结算方式表
     *
     * @param id
     * @return
     */
	@ApiOperation("删除系统结算方式表")
    @LogMessage(content = "删除系统结算方式表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        fcBankAccGroupService.deleteById(id);
        return success();
    }

    /**
     * excel 导入
     *
     * @param
     * @return
     */
	@ApiOperation("导入")
    @LogMessage(content = "添加贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<FcBankAccGroupVO> fcBankAccGroupVOList) {
        fcBankAccGroupService.saveImport(fcBankAccGroupVOList, getCurrentUser());
        return success();
    }
}
