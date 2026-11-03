package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexTranConfigOperationLog;
import com.bytter.biz.foreign.service.FexTranConfigOperationLogService;
import com.bytter.biz.foreign.vo.FexTranConfigOperationLogVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 交易模板配置操作日志
 */
@Controller
@RequestMapping("/foreign/fexTranConfigOperationLog")
public class FexTranConfigOperationLogController extends BaseController {

    @Autowired
    private FexTranConfigOperationLogService fexTranConfigOperationLogService;
    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 交易模板配置操作日志列表查询页面
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANGROUPRULE_APPLY)
    @GetMapping("/index")
    public String listOperationLog() {
        return "foreign/configOperationLog/fexTranConfigOperationLogList";
    }

    /**
     * 查询日志列表数据
     *
     * @param dataTablesInput
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANCONFIGOPERATIOBOG_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fexTranConfigOperationLogService.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 交易模板配置操作日志列表查询页面
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANCONFIGOPERATIOBOG_QUERY)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexTranConfigOperationLog log = fexTranConfigOperationLogService.get(id);
        if (log != null) {
            FexTranConfigOperationLogVo vo = new FexTranConfigOperationLogVo();
            BeanUtils.copyProperties(log, vo);
            Map<String, DictDTO>  map=dictService.listMap("fextranTemplateConfig.operationPage");
            if(!map.isEmpty()&& StringUtils.isNotEmpty(vo.getOperationPage())){
                DictDTO dto=  map.get(vo.getOperationPage());
                if(dto!=null){
                    vo.setOperationPageName(dto.getName());
                }
            }
            model.addAttribute("log", vo);
        }
        return "foreign/configOperationLog/fexTranConfigOperationLogView";
    }
}
