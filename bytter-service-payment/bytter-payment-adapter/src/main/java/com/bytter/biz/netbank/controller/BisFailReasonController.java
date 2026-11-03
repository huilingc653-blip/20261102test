package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.entity.BisFailReason;
import com.bytter.biz.netbank.service.BisFailReasonService;
import com.bytter.biz.netbank.vo.BisFailReasonVO;
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
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.redisson.misc.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @Title: Controller
 * @Description: 交易失败原因表
 * @author huangx
 * @date 2019-05-17 14:26:13
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/netbank/bisfailreason")
public class BisFailReasonController extends BaseController {
    @Autowired
    private BisFailReasonService bisFailReasonService;
    /**
     * 交易失败原因表列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISFAILREASON_APPLY)
    @LogMessage(content = "查询交易失败原因表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "netbank/listBisFailReason";
    }
    /**
     * 交易失败原因表 table列表
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISFAILREASON_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return bisFailReasonService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 交易失败原因表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISFAILREASON_APPLY)
    @GetMapping("/add")
    public String add(BisFailReason bisFailReason, Model model) {
        if (StringUtils.isNotEmpty(bisFailReason.getId())) {
            bisFailReason = bisFailReasonService.getById(bisFailReason.getId());
            model.addAttribute("bisFailReasonPage", bisFailReason);
        }
        return "netbank/addBisFailReason";
    }
    /**
     * 交易失败原因表编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISFAILREASON_APPLY)
    @GetMapping("/update")
    public String update(BisFailReason BisFailReason, Model model) {
        if (StringUtils.isNotEmpty(BisFailReason.getId())) {
            BisFailReason = bisFailReasonService.getById(BisFailReason.getId());
            model.addAttribute("bisFailReasonPage", BisFailReason);
        }
        return "netbank/updateBisFailReason";
    }
    /**
     * 交易失败原因表详情页
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISFAILREASON_APPLY)
    @LogMessage(content = "查询交易失败原因表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BisFailReasonVO bisFailReason = bisFailReasonService.getByUnion(id);
        model.addAttribute("bisFailReasonPage", bisFailReason);
        return "netbank/viewBisFailReason";
    }
    /**
     * 添加交易失败原因表
     * @param bisFailReason
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISFAILREASON_APPLY)
    @LogMessage(content = "添加交易失败原因表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisFailReason bisFailReason){
        if(this.dataCheck(bisFailReason)) {
            bisFailReason.setCreateBy(this.getCurrentUser().getLoginName());
            bisFailReason.setCreateDate(DateUtils.getDate());
            this.bisFailReasonService.save(bisFailReason, LocaleContextHolder.getLocale().toString());
        }
        return success();
    }
    /**
     * 修改交易失败原因表
     * @param bisFailReason
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISFAILREASON_APPLY)
    @LogMessage(content = "修改交易失败原因表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BisFailReason bisFailReason){
        if(this.dataCheck(bisFailReason)) {
            BisFailReason t = bisFailReasonService.getById(bisFailReason.getId());
            BeanUtils.copyNotNullProperties(bisFailReason, t);
            t.setUpdateBy(this.getCurrentUser().getLoginName());
            t.setUpdateDate(DateUtils.getDate());
            this.bisFailReasonService.saveOrUpdate(t,LocaleContextHolder.getLocale().toString());
        }
        return  success();
    }
    /**
     * 数据校验
     *
     * @param bisFailReason
     * @return
     */
    private boolean dataCheck(BisFailReason bisFailReason) {
        //重复编码校验
        if (bisFailReasonService.validByPropertyName("failCode", bisFailReason.getFailCode(), bisFailReason.getId()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_FAILREASON_MESSAGE_FAILCODE_UNIQUE);
        }
        //重复名称校验
        if (bisFailReasonService.validByPropertyName("failName", bisFailReason.getFailName(), bisFailReason.getId()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_FAILREASON_MESSAGE_FAILNAME_UNIQUE);
        }
        //重复关键字校验 不同的失败原因中不能有相同的关键字词汇，并且关键字词汇之间不能有包含关系
        String[] keyWords = bisFailReason.getKeyWords().split(CommonConstant.SEMI_SPLIT);
        for(String keyWord : keyWords) {
            //同一失败原因不允许有相同关键字词汇
            int count = 0 ;
            Pattern p = Pattern.compile("("+keyWord+")");
            Matcher m = p.matcher(bisFailReason.getKeyWords());
            while (m.find()) {
                count++;
            }
            if(count > 1){
                throw new ParameterException(NetbankMessageEnum.NIS_FAILREASON_MESSAGE_KEYWORDS_UNIQUE, keyWord);
            }
            //不同一失败原因不允许有相同关键字词汇
            if (bisFailReasonService.isExsitKeyWord(keyWord, bisFailReason.getId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_FAILREASON_MESSAGE_KEYWORDS_UNIQUE, keyWord);
            }
        }
        return true;
    }
    /**
     * 删除交易失败原因表
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISFAILREASON_APPLY)
    @LogMessage(content = "删除交易失败原因表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        BisFailReason bisFailReason = bisFailReasonService.get(id);
        bisFailReasonService.deleteEntity(bisFailReason);
        return success();
    }
    /**
     * 批量删除交易失败原因表
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISFAILREASON_APPLY)
    @LogMessage(content = "批量删除交易失败原因表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids){
        for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
            BisFailReason bisFailReason = bisFailReasonService.get(id);
            bisFailReasonService.deleteEntity(bisFailReason);
        }
        return success();
    }
}
