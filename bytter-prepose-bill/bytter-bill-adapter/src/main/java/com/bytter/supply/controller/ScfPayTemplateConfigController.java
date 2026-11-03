package com.bytter.supply.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.dto.ScfPayTemplateBaseInfoDTO;
import com.bytter.supply.domain.payable.service.ScfPayTemplateConfigService;
import com.bytter.supply.domain.payable.vo.ScfPayTemplateViewConfigVO;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;
import com.bytter.supply.domain.platform.vo.ScfProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/enterpriseBusiness/scfPayTemplateConfig")
public class ScfPayTemplateConfigController extends BaseController {

    @Autowired
    private ScfPayTemplateConfigService scfPayTemplateConfigService;
}
