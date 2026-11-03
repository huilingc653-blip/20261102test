package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.entity.NisPaySchedulingElements;
import com.bytter.biz.netbank.service.NisPaySchedulingElementsService;
import com.bytter.biz.netbank.vo.NisPaySchedulingElementsVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * 付款排期要素
 */
@Controller
@RequestMapping("/netbank/nispayschedulingelements")
public class NisPaySchedulingElementsController extends BaseController {
    @Autowired
    private NisPaySchedulingElementsService nisPaySchedulingElementsService;
    /**
     * 业务类别表列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPAYSCHEDULINGELEMENTS_APPLY)
    @LogMessage(content = "查询业务类别表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "netbank/listNisPaySchedulingElements";
    }
    /**
     * 查询付款排期要素列表
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPAYSCHEDULINGELEMENTS_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return nisPaySchedulingElementsService.dataTable(dataTablesInput);
    }
    /**
     * 付款排期要素 新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPAYSCHEDULINGELEMENTS_APPLY)
    @GetMapping("/add")
    public String add() {
        return "netbank/addNisPaySchedulingElements";
    }
    /**
     * 添加付款排期要素
     * @param nisPaySchedulingElements
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPAYSCHEDULINGELEMENTS_APPLY)
    @LogMessage(content = "添加业务类别表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisPaySchedulingElements nisPaySchedulingElements){
        nisPaySchedulingElements.setCreateBy(this.getCurrentUser().getLoginName());
        nisPaySchedulingElements.setCreateDate(DateUtils.getDate());
        nisPaySchedulingElements.setStatus(NetbankConstant.STATUE_CHECK);
        nisPaySchedulingElementsService.saveEntity(nisPaySchedulingElements);
        return success();
    }
    /**
     * 付款排期要素编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPAYSCHEDULINGELEMENTS_APPLY)
    @GetMapping("/update")
    public String update(NisPaySchedulingElements nisPaySchedulingElements, Model model) {
        if (StringUtils.isNotEmpty(nisPaySchedulingElements.getId())) {
            nisPaySchedulingElements = nisPaySchedulingElementsService.get(nisPaySchedulingElements.getId(),true);
            model.addAttribute("nisPaySchedulingElements", nisPaySchedulingElements);
        }
        return "netbank/updateNisPaySchedulingElements";
    }
    /**
     * 修改付款排期要素
     * @param nisPaySchedulingElements
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPAYSCHEDULINGELEMENTS_APPLY)
    @LogMessage(content = "修改业务类别表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(NisPaySchedulingElements nisPaySchedulingElements, HttpServletRequest request){
        NisPaySchedulingElements t = nisPaySchedulingElementsService.get(nisPaySchedulingElements.getId(), true);
        nisPaySchedulingElements.setUpdateBy(this.getCurrentUser().getLoginName());
        nisPaySchedulingElements.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(nisPaySchedulingElements, t);
        nisPaySchedulingElementsService.saveOrUpdate(t);
        return  success();
    }
    /**
     * 删除付款排期要素
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPAYSCHEDULINGELEMENTS_APPLY)
    @LogMessage(content = "删除业务类别表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        NisPaySchedulingElements nisPaySchedulingElements = nisPaySchedulingElementsService.get(id,true);
        nisPaySchedulingElements.setUpdateBy(this.getCurrentUser().getLoginName());
        nisPaySchedulingElements.setUpdateDate(DateUtils.getDate());
        nisPaySchedulingElements.setStatus(NetbankConstant.STATUE_DEL);
        nisPaySchedulingElementsService.saveOrUpdate(nisPaySchedulingElements);
        return success();
    }
    /**
     * 获取付款排期要素
     * @return
     */
    @RequestMapping(params = "getByList")
    @ResponseBody
    public List<NisPaySchedulingElementsVO> getByList() {
        return nisPaySchedulingElementsService.list();
    }
}
