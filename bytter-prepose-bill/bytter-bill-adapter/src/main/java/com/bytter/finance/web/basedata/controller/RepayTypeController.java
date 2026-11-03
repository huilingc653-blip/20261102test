package com.bytter.finance.web.basedata.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: 还款方式维护
 * @Description: CMS_REPAY_TYPE
 * @date 2022-08-10 15:47:15
 */
@Controller
@RequestMapping("/cms/repay/repaytype")
public class RepayTypeController extends BaseController {
    @Autowired
    private RepayTypeService repayTypeService;
    /**
     * 查询还款方式列表
     **/
    @LogMessage(content = "查询CMS_REPAY_TYPE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/repaytype/listRepayType";
    }
    /**
     * 查询还款方式数据
     **/
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return repayTypeService.getRepayType(dataTablesInput);
    }
    /**
     * 新增页面跳转
     **/
    @GetMapping("/add")
    public String add(RepayTypeVO repayTypeVo, Model model) {
        if (StringUtils.isNotEmpty(repayTypeVo.getId())) {
            repayTypeVo = repayTypeService.getRepayTypeById(repayTypeVo.getId());
            model.addAttribute("repayTypePage", repayTypeVo);
        }
        model.addAttribute("repayCode", SequenceSingleton.getSequence(SequenceEnum.CMS_REPAY_CODE));
        return "cms/repaytype/addRepayType";
    }
    /**
     * 编辑、复制页面跳转
     **/
    @GetMapping("/update")
    public String update(RepayTypeVO repayTypeVo, String type, Model model) {
        repayTypeVo = repayTypeService.getisInput(repayTypeVo);
        if (BaseDataConstant.FREQUENCY_TYPE_C.equals(repayTypeVo.getCapitalFrequencyType())
                && BaseDataConstant.FREQUENCY_NEXT_1.equals(repayTypeVo.getCapitalFrequencyNext())) {
            model.addAttribute("dtl", repayTypeVo.getCapitalFrequencyDetail().split(","));
        }
        if ("clone".equals(type)){
            // 复制时候重新生产编码，
            repayTypeVo.setRepayCode(SequenceSingleton.getSequence(SequenceEnum.CMS_REPAY_CODE));
        }
        model.addAttribute("repayTypePage", repayTypeVo);
        model.addAttribute("type", type);
        return "cms/repaytype/updateRepayType";
    }
    /**
     * 还款方式详情页
     *
     * @return
     */
    @LogMessage(content = "查询还款方式详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        RepayTypeVO repayTypeVo = repayTypeService.getRepayTypeById(id);
        model.addAttribute("repayTypePage", repayTypeVo);
        return "cms/repaytype/viewRepayType";
    }
    /**
     * 增加还款方式
     **/
    @LogMessage(content = "添加CMS_REPAY_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RepayTypeVO RepayTypeVo) {
        Integer status = BaseDataConstant.STATUS_REDACT;
        RepayTypeVo.setStatus(status);
        repayTypeService.saveRepayType(RepayTypeVo, getCurrentUser());
        return success();
    }
    /**
     * 编辑还款方式
     **/
    @LogMessage(content = "修改CMS_REPAY_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RepayTypeVO repayTypeVo) {
        repayTypeService.updateRepayType(repayTypeVo, getCurrentUser());
        return success();
    }
    /**
     * 删除还款方式
     **/
    @LogMessage(content = "删除CMS_REPAY_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
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
    @GetMapping("/queryRepayTypeList")
    @ResponseBody
    public List<RepayTypeVO> queryRepayTypeList() {
        return repayTypeService.queryRepayTypeList();
    }

    /**
     * 获取还款方式(除随借随还)
     */
    @GetMapping("/queryNoNRepayTypeList")
    @ResponseBody
    public List<RepayTypeVO> queryNoNRepayTypeList() {
        return repayTypeService.queryNoNRepayTypeList();
    }

    /**
     * 获取还款方式(无本金与利息无关随借随还)
     */
    @GetMapping("/queryNoTRepayTypeList")
    @ResponseBody
    public List<RepayTypeVO> queryNoTRepayTypeList() {
        return repayTypeService.queryNoTRepayTypeList();
    }

    /**
     * 启用/禁用
     *
     * @param id
     *            主键id
     * @param validSign
     *            启用/禁用状态
     * @return
     */
    @PostMapping("/updateValidSign")
    @ResponseBody
    public AjaxJson updateValidSign(String id, String validSign) {
        repayTypeService.setValidSign(id, validSign);
        return success();
    }
}
