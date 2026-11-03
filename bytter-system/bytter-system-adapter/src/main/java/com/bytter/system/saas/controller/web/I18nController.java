package com.bytter.system.saas.controller.web;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.service.I18nService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基础数据&业务基础数据国际化控制器
 *
 * @author zhoul
 * @since 2019-11-12
 */
@Api(value = "/v1-system/sys/i18n",tags = "基础数据&业务基础数据国际化控制器")
@RestController("v1I18nController")
@RequestMapping("/v1-system/sys/i18n")
public class I18nController extends BaseController {
    private static class I18nParams {
        private String type;
        private List<I18nDTO> dataList;
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public List<I18nDTO> getDataList() {
            return dataList;
        }
        public void setDataList(List<I18nDTO> dataList) {
            this.dataList = dataList;
        }
    }
    @Autowired
    @Qualifier("i18nService")
    private I18nService i18nService;

    @ApiOperation("添加或者修改")
    @PostMapping("/saveOrUpdate")
    public AjaxJson saveOrUpdate(@RequestBody I18nParams params) {
        if (CollectionUtils.isNotEmpty(params.getDataList())) {
            i18nService.saveOrUpdate(params.getType(), params.getDataList());
        }
        return AjaxJson.success();
    }

    @ApiOperation("列表查询")
    @GetMapping("/list")
    public AjaxJson<List<I18nDTO>> list(String type, String keyId) {
        return AjaxJson.success(i18nService.listI18nByKeyId(type, keyId));
    }

    @ApiOperation("删除")
    @GetMapping("/delete")
    public AjaxJson delete(String type, String langCode, String keyId) {
        i18nService.delete(type, langCode, keyId);
        return AjaxJson.success();
    }
}
