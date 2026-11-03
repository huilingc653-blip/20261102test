package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.service.I18nService;
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
@Controller
@RequestMapping("/sys/i18n")
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
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson saveOrUpdate(@RequestBody I18nParams params) {
        if (CollectionUtils.isNotEmpty(params.getDataList())) {
            i18nService.saveOrUpdate(params.getType(), params.getDataList());
        }
        return success();
    }
    @GetMapping("/view")
    public String view(Model model, String keyId, String type) {
        model.addAttribute("keyId", keyId);
        model.addAttribute("type", type);
        return "sys/i18n/view";
    }
    @GetMapping("/list")
    @ResponseBody
    public List<I18nDTO> list(String type, String keyId) {
        return i18nService.listI18nByKeyId(type, keyId);
    }
    @GetMapping("/delete")
    @ResponseBody
    public Object delete(String type, String langCode, String keyId) {
        i18nService.delete(type, langCode, keyId);
        return success();
    }
}
