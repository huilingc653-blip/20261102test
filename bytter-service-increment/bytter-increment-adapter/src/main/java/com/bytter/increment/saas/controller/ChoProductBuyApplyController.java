package com.bytter.increment.saas.controller;

import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.service.ChoProductBuyApplyService;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.increment.domain.vo.v1.ChoProductReqPageVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 理财产量购买申请Controller
 *
 * @author ouyj
 */
@ApiOperation(value = "理财产量购买申请")
@RestController("v1ChoProductBuyApplyController")
@RequestMapping("/v1-increment/chowmatistic/choproductBuyApply")
public class ChoProductBuyApplyController extends BaseController {
    @Autowired
    private ChoProductBuyApplyService choProductBuyApplyService;
    @Autowired
    private ChoProductReqService choProductReqService;

    @ApiOperation(value = "获取理财产量购买申请列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(choProductBuyApplyService.getDataTablesUnion(tablesInput, this.getCurrentUser()));
    }
    @GetMapping("/update")
    @ApiOperation(value = "获取理财产量购买申请详情")
    public AjaxJson<ChoProductReqPageVO> update(String id) {
        ChoProductReqPageVO pageVO = new ChoProductReqPageVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(id);
            pageVO.setChoProductReqPage(choProductReqVo);
        }
        return success(pageVO);
    }
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestBody BisIDsDTO idsDTO){
        String ids = idsDTO.getIds();
        choProductBuyApplyService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }
    @ApiOperation(value = "保存理财产量购买申请")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ChoProductReq choProduct) {
        choProduct.setCreateBy(this.getCurrentUser().getLoginName());
        choProduct.setCreateDate(DateUtils.getDate());
        choProductBuyApplyService.saveChoProductReq(choProduct, getCurrentUser());
        return success();
    }
    @ApiOperation(value = "修改理财产量购买申请", notes = "修改理财产量购买申请")
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ChoProductReq choProductReq) {
        ChoProductReq t = choProductReqService.get(choProductReq.getId(), true);
        BeanUtils.copyProperties(choProductReq, t);
        choProductBuyApplyService.modifyChoProductReq(t, getCurrentUser());
        return success();
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        this.choProductBuyApplyService.delete(id);
        return success();
    }

    /**
     * 自动生成合同号
     *
     * @return 合同号
     */
    @LogMessage(content = "自动生成合同号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/createContractNo")
    @ResponseBody
    public String createContractNo() {
        AjaxJson json = new AjaxJson();
        String sequenceNo = choProductBuyApplyService.createContractNo();
        json.setObj(sequenceNo);
        json.setSuccess(true);
        return JsonUtils.toJSONString(json);
    }
}
