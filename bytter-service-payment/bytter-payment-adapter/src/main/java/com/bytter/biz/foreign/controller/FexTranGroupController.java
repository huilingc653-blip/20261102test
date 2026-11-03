package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexProduct;
import com.bytter.biz.foreign.entity.FexTranGroup;
import com.bytter.biz.foreign.service.FexProductService;
import com.bytter.biz.foreign.service.FexTranGroupService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.vo.FexTranGroupVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.constant.ForeignMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * @Title: Controller
 * @Description: 交易组表Controller
 * @author huangx
 * @date 2020-02-27 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fextrangroup")
public class FexTranGroupController extends BaseController {

    @Autowired
    private FexTranGroupService fexTranGroupService;

    @Autowired
    private FexTranReqService fexTranReqService;

    @Autowired
    private FexProductService fexProductService;
    /**
     * 交易组表列表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANGROUP_APPLY)
    @LogMessage(content = "查询交易组列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "foreign/listFexTranGroup";
    }

    /**
     * 交易组表 table列表
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANGROUP_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexTranGroupService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 交易组表新增页面跳转（产品）
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANGROUP_APPLY)
    @GetMapping("/add")
    public String add(FexTranGroup fexTranGroup, Model model) {
        if (StringUtils.isNotEmpty(fexTranGroup.getId())) {
            fexTranGroup = fexTranGroupService.get(fexTranGroup.getId());
            model.addAttribute("fexTranGroupPage", fexTranGroup);
        }
        return "foreign/addFexTranGroup";
    }

    /**
     * 交易组表编辑页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANGROUP_APPLY)
    @GetMapping("/update")
    public String update(FexTranGroupVO fexTranGroup, Model model) {
        if (StringUtils.isNotEmpty(fexTranGroup.getId())) {
            fexTranGroup = fexTranGroupService.getByIdUnion(fexTranGroup.getId());
            model.addAttribute("fexTranGroupPage", fexTranGroup);
            //获取对应的交易信息
            List<FexTranReqVO> listTran = fexTranReqService.listVOByTranGroup(fexTranGroup.getId());
            model.addAttribute("listTran", listTran);
        }
        return "foreign/updateFexTranGroup";
    }

    /**
     * 交易组表详情页
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANGROUP_APPLY)
    @LogMessage(content = "查询交易组详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexTranGroupVO fexTranGroup = fexTranGroupService.getByIdUnion(id);
        model.addAttribute("fexTranGroupPage", fexTranGroup);
        //获取对应的交易信息
        List<FexTranReqVO> listTran = fexTranReqService.listVOByTranGroup(fexTranGroup.getId());
        model.addAttribute("listTran", listTran);
        return "foreign/viewFexTranGroup";
    }
    /**
     * 添加交易组信息
     * @param fexTranGroup
     * @return
     */
    @LogMessage(content = "添加交易组表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexTranGroupVO fexTranGroup){
        if(this.dataCheck(fexTranGroup)) {
            fexTranGroupService.saveFexTranGroup(fexTranGroup, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 修改交易组表
     * @param fexTranGroup
     * @return
     */
    @LogMessage(content = "修改交易组表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexTranGroupVO fexTranGroup){
        if(this.dataCheck(fexTranGroup)) {
            fexTranGroupService.updateFexTranGroup(fexTranGroup, this.getCurrentUser());
        }
        return  success();
    }

    /**
     * 数据校验
     *
     * @param fexTranGroup
     * @return
     */
    private boolean dataCheck(FexTranGroupVO fexTranGroup) {
        if (fexTranGroupService.isExistGroupName(fexTranGroup.getGroupName(), fexTranGroup.getId())) {
            throw new ParameterException(ForeignMessageEnum.FEX_TRAN_GROUP_GROUPNAME_UNIQUE);
        }
        FexProduct fexProduct = fexProductService.get(fexTranGroup.getProId());
        if(fexProduct.getIsAllowedChange().equals(CommonConstant.NO)){
            if(!fexTranGroup.getRuleId().equals(fexProduct.getTrangroupRuleId())){
                throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TRANGROUPRULE_NO_EDIT);
            }
        }
        return true;
    }

    /**
     * 删除交易组表
     * @param id
     * @return
     */
    @LogMessage(content = "删除交易组表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        List<FexTranReqVO> list = fexTranReqService.listVOByTranGroup(id);
        if(!EmptyUtils.isEmptyList(list)){
            throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TRANGROUP_DEL);
        }
        fexTranGroupService.delFexTranGroup(id, this.getCurrentUser());
        return success();
    }

    /**
     * 获取有效的外汇产品组
     *
     * @return
     */
    @RequestMapping("/listTranGroup")
    @ResponseBody
    public List<FexTranGroupVO> listTranGroup(String tranType, String proId) {
        List<FexTranGroupVO> list = fexTranGroupService.listTranGroup(tranType, proId);
        return list;
    }
}
