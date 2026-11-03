package com.bytter.biz.capital.controller;
import com.bytter.biz.capital.entity.NisCapitalRecord;
import com.bytter.biz.capital.service.NisCapitalRecordService;
import com.bytter.biz.capital.vo.NisCapitalRecordVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author shenc
 * @date 2019.3.28
 * 资金备案台账
 */
@Controller
@RequestMapping("/capital/record")
public class NisCapitalRecordController extends BaseController {
    @Autowired
    private NisCapitalRecordService nisCapitalRecordService;
//    @RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISCAPITALRECORD_QUERY)
    @GetMapping("/list")
    public String list(){
        return "capitalrecord/listNisCapitalRecord";
    }
//    @RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISCAPITALRECORD_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return nisCapitalRecordService.getDataTablesUnion(dataTablesInput,getCurrentUser());
    }
//    @RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISCAPITALRECORD_QUERY)
    @GetMapping("/add")
    public String add(NisCapitalRecord nisCapitalRecord, Model model){
        if (StringUtils.isNotEmpty(nisCapitalRecord.getId())) {
            nisCapitalRecord = nisCapitalRecordService.get(nisCapitalRecord.getId());
            model.addAttribute("nisCapitalRecordPage", nisCapitalRecord);
        }
        String corpId = getCurrentUser().getCorpId();
        model.addAttribute("corpId",corpId);
        return "capitalrecord/addNisCapitalRecord";
    }
//    @RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISCAPITALRECORD_QUERY)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisCapitalRecord nisCapitalRecord){
        nisCapitalRecord.setCreateBy(this.getCurrentUser().getLoginName());
        nisCapitalRecord.setCreateDate(DateUtils.getDate());
        nisCapitalRecordService.saveNisCapitalRecord(nisCapitalRecord);
        return success();
    }
//    @RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISCAPITALRECORD_QUERY)
    @GetMapping("/update")
    public String update(NisCapitalRecord nisCapitalRecord, Model model){
        if (StringUtils.isNotEmpty(nisCapitalRecord.getId())) {
            NisCapitalRecordVo nisCapitalRecordVo = nisCapitalRecordService.getNisCapitalRecordVoById(nisCapitalRecord.getId());
            model.addAttribute("nisCapitalRecordPage", nisCapitalRecordVo);
        }
        return "capitalrecord/updateNisCapitalRecord";
    }
//    @RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISCAPITALRECORD_QUERY)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(NisCapitalRecord choProductReq){
        NisCapitalRecord t = nisCapitalRecordService.get(choProductReq.getId());
        choProductReq.setUpdateBy(this.getCurrentUser().getLoginName());
        choProductReq.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(choProductReq, t);
        nisCapitalRecordService.toModify(t);
        return success();
    }
//    @RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISCAPITALRECORD_QUERY)
    @GetMapping("/view")
    public String view(NisCapitalRecord nisCapitalRecord, Model model){
        if (StringUtils.isNotEmpty(nisCapitalRecord.getId())) {
            NisCapitalRecordVo nisCapitalRecordVo = nisCapitalRecordService.getNisCapitalRecordVoById(nisCapitalRecord.getId());
            model.addAttribute("nisCapitalRecordPage", nisCapitalRecordVo);
        }
        return "capitalrecord/viewNisCapitalRecord";
    }
//    @RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISCAPITALRECORD_DELETE)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(@RequestParam("ids")String ids) {
        for (String id:ids.split(CommonConstant.COMMA_SPLIT)) {
            NisCapitalRecord record = nisCapitalRecordService.get(id);
            nisCapitalRecordService.deleteEntity(record);
        }
        return success();
    }
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<NisCapitalRecordVo> list) {
        if (EmptyUtils.isEmptyList(list)) {
            AjaxJson ajaxJson = new AjaxJson();
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        this.nisCapitalRecordService.saveImport(list,getCurrentUser());
        return success();
    }
}
