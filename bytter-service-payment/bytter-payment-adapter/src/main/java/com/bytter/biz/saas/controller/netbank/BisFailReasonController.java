package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.entity.BisFailReason;
import com.bytter.biz.netbank.service.BisFailReasonService;
import com.bytter.biz.netbank.vo.BisFailReasonVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
@Api(value = "/v1-payment/netbank/bisfailreason",tags = "交易失败原因维护")
@RestController("v1BisFailReasonController")
@RequestMapping("/v1-payment/netbank/bisfailreason")
public class BisFailReasonController extends BaseController {
    @Autowired
    private BisFailReasonService bisFailReasonService;


    /**
     * 交易失败原因表 table列表
     *
     * @return
     */
    @ApiOperation("交易失败原因表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisFailReasonVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisFailReasonService.getDataTablesUnion(input));
    }
    /**
     * 交易失败原因表新增页面跳转,交易失败原因表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("交易失败原因详情")
    @GetMapping("/update/{id}")
    public AjaxJson<BisFailReason> update(@PathVariable("id") String id) {
        return AjaxJson.success(bisFailReasonService.getById(id));
    }

    /**
     * 交易失败原因表详情页
     *
     * @return
     */
    @ApiOperation("查询交易失败原因表详情")
    @LogMessage(content = "查询交易失败原因表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BisFailReasonVO> view(@PathVariable("id") String id) {
        return AjaxJson.success(bisFailReasonService.getByUnion(id));
    }

    /**
     * 添加交易失败原因表
     * @param bisFailReason
     * @return
     */
    
    @ApiOperation("添加交易失败原因")
    @LogMessage(content = "添加交易失败原因表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody  BisFailReason bisFailReason){
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
    @ApiOperation("修改交易失败原因表")
    @LogMessage(content = "修改交易失败原因表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody  BisFailReason bisFailReason){
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
    @ApiOperation("删除交易失败原因表")
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
    @ApiOperation("批量删除交易失败原因表")
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
