package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.inneraccount.service.BisAccDtlImportConfigService;
import com.bytter.biz.inneraccount.vo.BisAccDtlImportConfigVO;
import com.bytter.biz.inneraccount.vo.v1.BisAccDtlImportConfigPageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.BankAccDtlMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: 离线流水智能导入配置表Controller
 * @Description: BIS_ACC_DTL_IMPORT_CONFIG
 * @date 2020-07-31 15:02:30
 */
@Api(value = "v1-payment/payment/bisaccdtlimportconfig",tags = "离线流水智能导入配置")
@RestController("v1BisAccDtlImportConfigController")
@RequestMapping("v1-payment/payment/bisaccdtlimportconfig")
public class BisAccDtlImportConfigController extends BaseController {

    @Autowired
    private BisAccDtlImportConfigService bisAccDtlImportConfigService;
    /**
     * 离线流水智能导入配置表 table列表
     *
     * @return
     */
    @ApiOperation("离线流水智能导入配置表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisAccDtlImportConfigVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisAccDtlImportConfigService.getDataTablesUnion(input));
    }

    /**
     * 离线流水智能导入配置表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("编辑页面跳转前数据获取")
    @GetMapping("/getInfoBeforeUpdate")
    @ResponseBody
    public AjaxJson<BisAccDtlImportConfigPageVO> getInfoBeforeUpdate(String id, String fileType) {
        BisAccDtlImportConfigPageVO pageVO = new BisAccDtlImportConfigPageVO();
        BisAccDtlImportConfigVO configVO = new BisAccDtlImportConfigVO();
        configVO.setId(id);
        configVO.setFileType(fileType);
        List<Map<String,String>> headList=bisAccDtlImportConfigService.getTableInfo(configVO);
        List<Map<String, String>> list = new ArrayList<>();
        //避免被覆盖
        if (null != headList && headList.size() > 0
                && "txt".equals(configVO.getFileType())) {
            Map<String, String> fieldMap = null;
            for (Map<String, String> valueMap : headList) {
                fieldMap = new HashMap<>();
                for (Map.Entry<String, String> vo : valueMap.entrySet()) {
                    fieldMap.put(vo.getKey(), vo.getValue());
                }
                list.add(fieldMap);
            }
        }
        pageVO.setHeadList(list!=null&&list.size()>0?list:headList);
        if (StringUtils.isNotEmpty(configVO.getId())) {
            configVO = bisAccDtlImportConfigService.getByIdUnion(configVO.getId());
            pageVO.setBisAccDtlImportConfigPage(configVO);
        }
        return success(pageVO);
    }

    /**
     * 添加离线流水智能导入配置表
     *
     * @param bisAccDtlImportConfig
     * @return
     */
    @ApiOperation("添加离线流水智能导入配置表")
    @LogMessage(content = "添加离线流水智能导入配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody BisAccDtlImportConfigVO bisAccDtlImportConfig) {
        if(this.dataCheck(bisAccDtlImportConfig)) {
            this.bisAccDtlImportConfigService.saveOrUpdate(bisAccDtlImportConfig, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 修改离线流水智能导入配置表
     *
     * @param bisAccDtlImportConfig
     * @return
     */
    @ApiOperation("修改离线流水智能导入配置表")
    @LogMessage(content = "修改离线流水智能导入配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody BisAccDtlImportConfigVO bisAccDtlImportConfig) {
        if(this.dataCheck(bisAccDtlImportConfig)) {
            this.bisAccDtlImportConfigService.saveOrUpdate(bisAccDtlImportConfig, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 删除离线流水智能导入配置表
     *
     * @param id
     * @return
     */
    
    @LogMessage(content = "删除离线流水智能导入配置表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        bisAccDtlImportConfigService.delelteUnion(id);
        return success();
    }

    /**
     * 数据校验
     *
     * @param bisAccDtlImportConfig
     * @return
     */
    public boolean dataCheck(BisAccDtlImportConfigVO bisAccDtlImportConfig) {
        //同一个银行类型允许配置多个文件类型，但同一个银行类型，不允许配置相同的文件类型，要校验重复
        if (this.bisAccDtlImportConfigService.isExistFileType(bisAccDtlImportConfig.getBankTypeId(), bisAccDtlImportConfig.getFileType(), bisAccDtlImportConfig.getId())) {
            throw new ParameterException(BankAccDtlMessageEnum.BISACCDTL_IMPORTCONFIG_MESSAGE_REPEAT_FILETYPE);
        }
        //配置参数中，如果是excel格式，需要判断相同所在列不能出现在不同的配置参数中；如果是txt格式，需要判断相同分隔符不能出现在不同的配置参数中
        return true;
    }
}
