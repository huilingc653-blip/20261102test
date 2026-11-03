package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexAddWord;
import com.bytter.biz.foreign.service.FexAddWordService;
import com.bytter.biz.foreign.vo.FexAddWordVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 交易附言表
 * @author huangx
 * @date 2022-10-31 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexAddWord")
public class FexAddWordController extends BaseController {
    @Autowired
    private FexAddWordService fexAddWordService;

    /**
     * 交易附言表列表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXADDWORD_QUERY)
    @LogMessage(content = "查询交易附言表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "foreign/listFexAddWord";
    }

    /**
     * 交易附言表 table列表
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXADDWORD_QUERY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexAddWordService.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 交易附言表新增页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXADDWORD_APPLY)
    @GetMapping("/add")
    public String add(FexAddWord fexAddWord, Model model) {
        if (StringUtils.isNotEmpty(fexAddWord.getId())) {
            fexAddWord = fexAddWordService.get(fexAddWord.getId());
            model.addAttribute("fexAddWordPage", fexAddWord);
        }
        return "foreign/addFexAddWord";
    }

    /**
     * 交易附言表编辑页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXADDWORD_APPLY)
    @GetMapping("/update")
    public String update(FexAddWordVO fexAddWord, Model model) {
        if (StringUtils.isNotEmpty(fexAddWord.getId())) {
            fexAddWord = fexAddWordService.getByIdUnion(fexAddWord.getId());
            model.addAttribute("fexAddWordPage", fexAddWord);
        }
        return "foreign/updateFexAddWord";
    }

    /**
     * 交易附言表详情页
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXADDWORD_QUERY)
    @LogMessage(content = "查询交易附言表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexAddWordVO fexAddWord = fexAddWordService.getByIdUnion(id);
        model.addAttribute("fexAddWordPage", fexAddWord);
        return "foreign/viewFexAddWord";
    }

    /**
     * 添加交易附言表
     * @param fexAddWord
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXADDWORD_APPLY)
    @LogMessage(content = "添加交易附言表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexAddWord fexAddWord){
        FexAddWordVO fexAddWordVO = fexAddWordService.getByTranCodeAndCdSign(fexAddWord.getTranCode(), fexAddWord.getCdSign());
        if (fexAddWordVO != null) {
            throw new ParameterException(ForeignMessageEnum.FEX_ADD_WORD_TRANCODE_MESSAGE_EXISTS);
        }
        fexAddWord.setCreateBy(this.getCurrentUser().getLoginName());
        fexAddWord.setCreateDate(DateUtils.getDate());
        this.fexAddWordService.saveEntity(fexAddWord);
        return success();
    }

    /**
     * 修改交易附言表
     * @param fexAddWord
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXADDWORD_APPLY)
    @LogMessage(content = "修改交易附言表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexAddWord fexAddWord){
        FexAddWordVO fexAddWordVO = fexAddWordService.getByTranCodeAndCdSign(fexAddWord.getTranCode(), fexAddWord.getCdSign());
        if (fexAddWordVO != null && !fexAddWord.getId().equals(fexAddWordVO.getId())) {
            throw new ParameterException(ForeignMessageEnum.FEX_ADD_WORD_TRANCODE_MESSAGE_EXISTS);
        }
        FexAddWord t = fexAddWordService.get(fexAddWord.getId(), true);
        BeanUtils.copyNotNullProperties(fexAddWord, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        this.fexAddWordService.updateEntity(t);
        return  success();
    }

    /**
     * 删除交易附言表
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXADDWORD_APPLY)
    @LogMessage(content = "删除交易附言表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        fexAddWordService.delete(id);
        return success();
    }

    /**
     * 批量删除交易附言表
     * @param ids
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXADDWORD_APPLY)
    @LogMessage(content = "批量删除交易附言表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        if (StringUtils.isNotEmpty(ids)) {
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                fexAddWordService.delete(id);
            }
        }
        return success();
    }

    /**
     * 获取交易附言信息
     *
     * @param tranCode
     * @return
     */
    @RequestMapping("/getFexAddWord")
    @ResponseBody
    public AjaxJson getFexAddWord(String tranCode, String cdSign) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        FexAddWordVO fexAddWordVO = fexAddWordService.getByTranCodeAndCdSign(tranCode, cdSign);
        if (fexAddWordVO != null && CommonConstant.YES.equals(fexAddWordVO.getValidSign())) {
            ajaxJson.setObj(fexAddWordVO.getAddWord());
        } else {
            ajaxJson.setObj("");
        }
        return ajaxJson;
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<FexAddWordVO> list) {
        this.fexAddWordService.saveImport(list, this.getCurrentUser());
        return success();
    }
}
