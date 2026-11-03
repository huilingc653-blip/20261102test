package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.vo.FcVoucherTypeVO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 自定义付款方式表
 * @date 2017-05-12 17:18:41
 */
@Api(value = "/v1-payment/netbank/fcvouchertype", tags = "自定义付款方式")
@RestController("V1FcVoucherTypeController")
@RequestMapping("/v1-payment/netbank/fcvouchertype")
public class FcVoucherTypeController extends BaseController {
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;


    /**
     * 自定义付款方式表 table列表
     *
     * @return
     */
    @ApiOperation("自定义付款方式表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<FcVoucherTypeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fcVoucherTypeService.getDataTablesUnion(input));
    }

    /**
     * 自定义付款方式表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("根据ID查询定义付款方式")
    @GetMapping("/getFcVoucherTypeById/{id}")
    public AjaxJson<FcVoucherType> getFcVoucherTypeById(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            FcVoucherType fcVoucherType = fcVoucherTypeService.getById(id);
            AjaxJson.success(fcVoucherType);
        }
        return AjaxJson.success();
    }

    /**
     * 自定义付款方式表详情页
     *
     * @return
     */
    @ApiOperation("自定义付款方式表详情页")
    @LogMessage(content = "查询自定义付款方式表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<FcVoucherTypeVO> view(@PathVariable("id") String id) {
        FcVoucherTypeVO fcVoucherType = fcVoucherTypeService.getByIdUnion(id);
        return AjaxJson.success(fcVoucherType);
    }


    /**
     * 获取挂资金计划的付款方式
     *
     * @return
     */
    @ApiOperation("获取挂资金计划的付款方式")
    @GetMapping("/getVoucherTypeForBudget")
    public AjaxJson<List<FcVoucherTypeDTO>> getVoucherTypeForBudget() {
        return AjaxJson.success(fcVoucherTypeService.getVoucherTypeForBudget());
    }

    /**
     * 直营付款只过滤转收，拨付，内转，其他
     *
     * @return
     */
    @ApiOperation("直营付款只过滤转收，拨付，内转，其他")
    @GetMapping("/getValidVoucherTypeForPool")
    public AjaxJson<List<FcVoucherTypeDTO>>  getValidVoucherTypeForPool() {
        List<FcVoucherTypeDTO> listSpecialVoucherType = new ArrayList<FcVoucherTypeDTO>();
        List<FcVoucherTypeDTO> listAll = fcVoucherTypeService.getFcVoucherTypeAll();
        for (FcVoucherTypeDTO type : listAll) {
            if (NetbankConstant.PAY_TYPE_01.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_10.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_05.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_11.equals(type.getSysVoucherCode())) {
                listSpecialVoucherType.add(type);
            }
        }
        return AjaxJson.success(listSpecialVoucherType);
    }

    /**
     * 获取挂资金计划的付款方式
     *
     * @return
     */
    @ApiOperation("获取挂资金计划的付款方式")
    @GetMapping("/getVoucherTypeForErp")
    public AjaxJson<List<FcVoucherTypeDTO>> getVoucherTypeForErp() {
        return AjaxJson.success(fcVoucherTypeService.getVoucherTypeForErp());
    }

    /**
     * 外汇管理获取有效的付款方式-过滤系统结算方式为外付网银
     *
     * @param
     * @return
     */
    @ApiOperation("外汇管理获取有效的付款方式-过滤系统结算方式为外付网银")
    @GetMapping("/getFcVoucherTypeForForeign")
    public AjaxJson<List<FcVoucherTypeDTO>> getFcVoucherTypeForForeign() {
        List<FcVoucherTypeDTO> listSpecialVoucherType = new ArrayList<FcVoucherTypeDTO>();
        List<FcVoucherTypeDTO> listAll = fcVoucherTypeService.getFcVoucherTypeAll();
        for (FcVoucherTypeDTO type : listAll) {
            if (NetbankConstant.PAY_TYPE_34.equals(type.getSysVoucherCode())) {
                listSpecialVoucherType.add(type);
            }
        }
        return AjaxJson.success(listSpecialVoucherType);
    }

    /**
     * 查询所有有效供付款方式分配的付款方式（内部账号流水查询）
     *
     * @return
     */
    @ApiOperation("查询所有有效供付款方式分配的付款方式（内部账号流水查询）")
    @GetMapping("/getFcVoucherTypeForReport")
    public AjaxJson<List<FcVoucherTypeDTO>> getFcVoucherTypeForReport() {
        return  AjaxJson.success(fcVoucherTypeService.getFcVoucherTypeForReport());
    }

    /**
     * 查询所有有效且生成指令的的付款方式
     *
     * @return
     */
    @ApiOperation("查询所有有效且生成指令的的付款方式")
    @GetMapping("/getValidVoucherTypeForExc")
    public AjaxJson<List<FcVoucherTypeDTO>> getValidVoucherTypeForExc() {
        return AjaxJson.success(fcVoucherTypeService.getValidVoucherTypeForExc());
    }

    @ApiOperation("查询所有有效供付款方式分配的付款方式")
    @GetMapping("/json")
    public AjaxJson<List<FcVoucherTypeDTO>> json(){
        return AjaxJson.success(fcVoucherTypeService.getFcVoucherTypeForAssign());
    }

    /**
     * 添加自定义付款方式表
     * @param fcVoucherType
     * @return
     */
    @ApiOperation("添加自定义付款方式表")
    @LogMessage(content = "添加自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody  FcVoucherType fcVoucherType){
        if (fcVoucherTypeService.findByCode(fcVoucherType.getVoucherType()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_VOUTYPE_MESSAGE_VOUCHERTYPE_UNIQUE);
        }
        if (fcVoucherTypeService.getByVoucherName(fcVoucherType.getVoucherName()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_VOUTYPE_MESSAGE_VOUCHERTYPENAME_UNIQUE);
        }
        if (StringUtils.isEmpty(fcVoucherType.getSysVoucherCode().trim())) {
            throw new ParameterException(NetbankMessageEnum.NIS_VOUTYPE_MESSAGE_SYSVOUCHERCODE_NULL);
        }
        this.setFcVocherType(fcVoucherType);
        fcVoucherType.setCreateBy(this.getCurrentUser().getLoginName());
        fcVoucherType.setCreateDate(DateUtils.getDate());
        this.fcVoucherTypeService.saveFcVoucherType(fcVoucherType, LocaleContextHolder.getLocale().toString());
        return success();
    }

    /**
     * 修改自定义付款方式表
     * @param fcVoucherType
     * @return
     */
    @ApiOperation("修改自定义付款方式表")
    @LogMessage(content = "修改自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody FcVoucherType fcVoucherType){
        if (StringUtils.isEmpty(fcVoucherType.getSysVoucherCode().trim())) {
            throw new ParameterException(NetbankMessageEnum.NIS_VOUTYPE_MESSAGE_SYSVOUCHERCODE_NULL);
        }
        FcVoucherType t = fcVoucherTypeService.get(fcVoucherType.getId(), true);
        BeanUtils.copyNotNullProperties(fcVoucherType, t);
        this.setFcVocherType(t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        this.fcVoucherTypeService.updateFcVoucherType(t, LocaleContextHolder.getLocale().toString());
        return  success();
    }
    /**
     * 删除自定义付款方式表
     * @param id
     * @return
     */
    @ApiOperation("删除自定义付款方式表")
    @LogMessage(content = "删除自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id){
        FcVoucherType fcVoucherType = fcVoucherTypeService.get(id);
        fcVoucherTypeService.deleteFcVoucherType(fcVoucherType);
        return success();
    }
    /**
     * 批量删除自定义付款方式表
     * @param ids
     * @return
     */
    @ApiOperation("批量删除自定义付款方式表")
    @LogMessage(content = "批量删除自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        this.fcVoucherTypeService.batchDelete(ids);
        return success();
    }


    /**
     * 获取付款方式信息
     * @param id
     * @return
     */
    @ApiOperation("获取付款方式信息")
    @GetMapping( "/getFcVoucherType/{id}")
    public AjaxJson<FcVoucherTypeVO> getFcVoucherType(@PathVariable("id") String id){
        return AjaxJson.success(fcVoucherTypeService.getByIdUnion(id));
    }
    /**
     * 直营付款只过滤代付，转付，拨付，代理支付
     * @return
     */
    @ApiOperation("直营付款只过滤代付，转付，拨付，代理支付")
    @GetMapping("/specialVoucherType")
    public AjaxJson<List<FcVoucherTypeDTO>> specialVoucherType (){
        List<FcVoucherTypeDTO> listSpecialVoucherType = new ArrayList<FcVoucherTypeDTO>();
        List<FcVoucherTypeDTO> listAll = fcVoucherTypeService.getFcVoucherTypeAll();
        for(FcVoucherTypeDTO type : listAll){
            if(NetbankConstant.PAY_TYPE_02.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_10.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_33.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_35.equals(type.getSysVoucherCode())){
                listSpecialVoucherType.add(type);
            }
        }
        return AjaxJson.success(listSpecialVoucherType);
    }
    /**
     * 费用报销查询有效的付款方式-过滤系统结算方式为转付、代付、外付网银、代理支付
     * @param
     * @return
     */
    @ApiOperation("费用报销查询有效的付款方式-过滤系统结算方式为转付、代付、外付网银、代理支付")
    @GetMapping("/getFcVoucherTypeFilter")
    public AjaxJson<List<FcVoucherTypeDTO>> getFcVoucherTypeFilter(){
        List<FcVoucherTypeDTO> listSpecialVoucherType = new ArrayList<FcVoucherTypeDTO>();
        List<FcVoucherTypeDTO> listAll = fcVoucherTypeService.getFcVoucherTypeAll();
        for(FcVoucherTypeDTO type : listAll){
            if(NetbankConstant.PAY_TYPE_02.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_33.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_35.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_34.equals(type.getSysVoucherCode())){
                listSpecialVoucherType.add(type);
            }
        }
        return AjaxJson.success(listSpecialVoucherType);
    }
    /**
     * 付款方式设置
     * @param fcVoucherType
     * @return
     */
    private FcVoucherType setFcVocherType(FcVoucherType fcVoucherType){
        if(NetbankConstant.PAY_TYPE_34.equals(fcVoucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_48.equals(fcVoucherType.getSysVoucherCode())){
            //外付网银，默认走银企直连且不记账，不允许修改
            fcVoucherType.setIsElectronPay(CommonConstant.YES);
            fcVoucherType.setIsAccount(CommonConstant.NO);
        }else if(NetbankConstant.PAY_TYPE_05.equals(fcVoucherType.getSysVoucherCode())){
            //内转，默认不走银企直连且记账，不允许修改
            fcVoucherType.setIsElectronPay(CommonConstant.NO);
            fcVoucherType.setIsAccount(CommonConstant.YES);
        }else if(NetbankConstant.PAY_TYPE_49.equals(fcVoucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_50.equals(fcVoucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_06.equals(fcVoucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_62.equals(fcVoucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_63.equals(fcVoucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_07.equals(fcVoucherType.getSysVoucherCode())){
            //应收/付票据，默认票据，不走银企直连且不记账
            fcVoucherType.setIsElectronPay(CommonConstant.NO);
            fcVoucherType.setIsAccount(CommonConstant.NO);
        }else if(NetbankConstant.PAY_TYPE_04.equals(fcVoucherType.getSysVoucherCode())){
            //现付，默认不走银企直连，不允许修改
            fcVoucherType.setIsElectronPay(CommonConstant.NO);
        }
        //系统初始化的付款方式，有效标志默认是，不允许修改为否
		/*if(CommonConstant.YES.equals(fcVoucherType.getIsSystem())){
			fcVoucherType.setValidSign(CommonConstant.YES);
		}*/
        return fcVoucherType;
    }
    /**
     * 功能介绍 : 获取有效的付款方式。<br>
     * <b>日期：</b>2017年12月19日<br>
     * @version 1.0
     */
    @ApiOperation("获取有效的付款方式")
    @GetMapping("/getValidVoucherType")
    public AjaxJson<List<FcVoucherTypeDTO>> getValidVoucherType(){
        return AjaxJson.success(fcVoucherTypeService.getValidFcVoucherType());
    }





}
