package com.bytter.increment.saas.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.service.ChoProductMaintainService;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.increment.domain.vo.v1.ChoProductReqPageVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 理财维护Controller
 *
 * @author ouyj
 */
@Api(tags = "理财维护")
@RestController("v1ChoProductMaintainController")
@RequestMapping("/v1-increment/chowmatistic/choproductMaintain")
public class ChoProductMaintainController extends BaseController {
    @Autowired
    private ChoProductMaintainService choProductMaintainService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(choProductMaintainService.getDataTablesUnion(tablesInput, this.getCurrentUser()));
    }
    @GetMapping("/update")
    public AjaxJson<ChoProductReqPageVO> update(String id) {
        ChoProductReqPageVO pageVO = new ChoProductReqPageVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(id);
            pageVO.setChoProductReqPage(choProductReqVo);
        }
        return success(pageVO);
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ChoProductReq choProductReq) {
        ChoProductReq t = choProductMaintainService.get(choProductReq.getId(), true);
        BeanUtils.copyProperties(choProductReq, t);
        choProductMaintainService.toModify(t, getCurrentUser());
        return success();
    }
}
