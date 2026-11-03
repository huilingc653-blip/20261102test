package com.bytter.finance.saas.controller.basedata;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mof
 * @version 1.0
 * @Title: 还款方式维护
 * @Description: CMS_REPAY_TYPE
 * @date 2022-08-10 15:47:15
 */



 @Api(value = "/v1-cms/cms/repay/repaytype", tags = "还款方式维护")
@RestController("v1RepayTypeController")
@RequestMapping("/v1-cms/cms/repay/repaytype")
public class RepayTypeController extends BaseController {
    @Autowired
    private RepayTypeService repayTypeService;

    /**
     * 查询还款方式数据
     **/
    @ApiOperation("查询还款方式列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(repayTypeService.getRepayType(input));
    }

    /**
     * 新增页面数据
     **/
    @ApiOperation("新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RepayTypeVO> add(RepayTypeVO repayTypeVo) {
        if (StringUtils.isNotEmpty(repayTypeVo.getId())) {
            repayTypeVo = repayTypeService.getRepayTypeById(repayTypeVo.getId());
        }
        repayTypeVo.setRepayCode(SequenceSingleton.getSequence(SequenceEnum.CMS_REPAY_CODE));
        return success(repayTypeVo);
    }

    /**
     * 编辑、复制页面数据
     **/
    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RepayTypeVO> update(RepayTypeVO repayTypeVo, String type) {
        repayTypeVo = repayTypeService.getisInput(repayTypeVo);
        if ("clone".equals(type)){
            // 复制时候重新生产编码
            repayTypeVo.setRepayCode(SequenceSingleton.getSequence(SequenceEnum.CMS_REPAY_CODE));
        }
        return success(repayTypeVo);
    }

    /**
     * 还款方式详情页
     *
     * @return
     */
    @LogMessage(content = "查询还款方式详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询还款方式详情")
    @GetMapping("/view/{id}")
    public AjaxJson<RepayTypeVO> view(@PathVariable("id") String id) {
        return success(repayTypeService.getRepayTypeById(id));
    }

    /**
     * 增加还款方式
     **/
    @LogMessage(content = "添加CMS_REPAY_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("添加还款方式")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RepayTypeVO RepayTypeVo) {
        Integer status = BaseDataConstant.STATUS_REDACT;
        RepayTypeVo.setStatus(status);
        repayTypeService.saveRepayType(RepayTypeVo, getCurrentUser());
        return success();
    }

    /**
     * 编辑还款方式
     **/
    @LogMessage(content = "修改CMS_REPAY_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("修改还款方式")
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody RepayTypeVO repayTypeVo) {
        repayTypeService.updateRepayType(repayTypeVo, getCurrentUser());
        return success();
    }

    /**
     * 删除还款方式
     **/
    @LogMessage(content = "删除CMS_REPAY_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("删除还款方式")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        // 是否已被引用，否则不可删除
        boolean flag = repayTypeService.judgeRepayTypeByCode(id);
        if (!flag) {
            throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REF_OTHER);
        }
        repayTypeService.deleteRepayType(id);
        return success();
    }

    /**
     * 获取还款方式
     */
    @ApiOperation("获取还款方式列表")
    @GetMapping("/queryRepayTypeList")
    public AjaxJson<List<RepayTypeVO>> queryRepayTypeList() {
        return success(repayTypeService.queryRepayTypeList());
    }

    /**
     * 获取还款方式(除随借随还)
     */
    @ApiOperation("获取还款方式列表(除随借随还)")
    @GetMapping("/queryNoNRepayTypeList")
    public AjaxJson<List<RepayTypeVO>> queryNoNRepayTypeList() {
        return success(repayTypeService.queryNoNRepayTypeList());
    }

    /**
     * 获取还款方式(无本金与利息无关随借随还)
     */
    @ApiOperation("获取还款方式列表(无本金与利息无关随借随还)")
    @GetMapping("/queryNoTRepayTypeList")
    public AjaxJson<List<RepayTypeVO>> queryNoTRepayTypeList() {
        return success(repayTypeService.queryNoTRepayTypeList());
    }

    /**
     * 启用/禁用
     *
     * @param id        主键id
     * @param validSign 启用/禁用状态
     * @return
     */
    @ApiOperation("启用/禁用")
    @PostMapping("/updateValidSign")
    public AjaxJson updateValidSign(String id, String validSign) {
        repayTypeService.setValidSign(id, validSign);
        return success();
    }
}
