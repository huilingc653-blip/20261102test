package com.bytter.increment.saas.controller;

import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.AgreedConstant;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.enums.ChoInnerFinanceMessageEnum;
import com.bytter.increment.domain.entity.FcAgreedDeposit;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import com.bytter.increment.domain.service.FcAgreedDepositService;
import com.bytter.increment.domain.service.FcAgreedDetailService;
import com.bytter.increment.domain.vo.FcAccVO;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import com.bytter.increment.domain.vo.v1.FcAgreedDepositPageVO;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 协定存款登记表
 * @date 2019-05-10 16:44:38
 */
@Api(value = "协定存款登记",tags = "协定存款登记")
@RestController("v1FcAgreedDepositController")
@RequestMapping("/v1-increment/agreeddeposit/fcagreeddeposit")
public class FcAgreedDepositController extends BaseController {
    @Autowired
    private FcAgreedDepositService fcAgreedDepositService;
    @Autowired
    private FcAgreedDetailService fcAgreedDetailService;
    @DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
    @DubboReference(check = false)
	private IFcDayCloseService fcDayCloseService;
    /**
     * 协定存款登记表 table列表
     *
     * @return
     */
    @ApiOperation("查询协定存款登记表列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAgreedDepositVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcAgreedDepositService.findListBysql(input, this.getCurrentUser()));
    }
    /**
     * 协定存款登记表复核 table列表
     *
     * @return
     */
    @ApiOperation("查询协定存款登记表复核列表")
    @PostMapping("/datatableCheck")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAgreedDepositVO>> datatableCheck(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcAgreedDepositService.checkListBysql(input,this.getCurrentUser()));
    }

    /**
     * 协定存款登记编辑页面跳转
     *
     * @return
     */
    @ApiOperation(value = "在协定存款登记页面跳转前获取对应数据信息",
                    notes = "原先的/update /updatePos /updateTerm都改用此接口获取数据信息")
    @GetMapping("/getInfoBeforeUpdate")
    @ResponseBody
    public AjaxJson<FcAgreedDepositPageVO> getInfoBeforeUpdate(FcAgreedDepositVO fcAgreedDeposit) {
        FcAgreedDepositPageVO pageVO = new FcAgreedDepositPageVO();
        FcAgreedDepositVO fcAgreedDepositVO = fcAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        if(fcAgreedDepositVO != null){
            pageVO.setFcAgreedDepositPage(fcAgreedDepositVO);
            Date tallyDate = fcDayCloseService.getPoolDayCloseDate(fcAgreedDepositVO.getNetId());
            tallyDate = DateUtils.addDay(tallyDate, 1);
            String dateStr = DateUtils.formatDate(tallyDate);
            pageVO.setDateStr(dateStr);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 协定存款登记表详情页
     *
     * @return
     */
    @ApiOperation(value = "查询协定存款登记表详情"
            ,notes = "会根据opType去跳转不同页面\n" +
                     "1. 如果opType=0跳转agreeddeposit/viewFcAgreedDeposit\n" +
                     "2. 如果opType=1跳转agreeddeposit/viewPosFcAgreedDeposit\n" +
                     "3. 其他情况跳转agreeddeposit/viewTermFcAgreedDeposit")
    @LogMessage(content = "查询协定存款登记表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{opType}/{id}")
    public AjaxJson<FcAgreedDepositPageVO> view(@PathVariable("opType") String opType, @PathVariable("id") String id) {
        FcAgreedDepositPageVO pageVO = new FcAgreedDepositPageVO();
        FcAgreedDepositVO fcAgreedDepositVO = fcAgreedDepositService.getByUnion(id);
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
        fcAgreedDepositVO.setDetailList(details);
        pageVO.setFcAgreedDepositPage(fcAgreedDepositVO);
        pageVO.setOpType(opType);
        return success(pageVO);
    }
    /**
     * 协定存款登记表详情页
     *
     * @return
     */
    @ApiOperation("查询复核协定存款登记表详情")
    @LogMessage(content = "查询复核协定存款登记表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/check/{busType}/{id}")
    public AjaxJson<FcAgreedDepositPageVO> check(@PathVariable("busType") String busType, @PathVariable("id") String id, Model model) {
        FcAgreedDepositPageVO pageVO = new FcAgreedDepositPageVO();
        pageVO.setId(id);
        pageVO.setBusType(busType);
        FcAgreedDepositVO fcAgreedDepositVO = fcAgreedDepositService.getByUnion(id);
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
        fcAgreedDepositVO.setDetailList(details);
        pageVO.setFcAgreedDepositPage(fcAgreedDepositVO);
        return success(pageVO);
    }
    /**
     * 添加协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @ApiOperation("添加协定存款登记表")
    @LogMessage(content = "添加协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.saveFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 添加协定存款延期
     * @param fcAgreedDeposit
     * @return
     */
    @ApiOperation("添加协定存款延期登记表")
    @LogMessage(content = "添加协定存款延期登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/savePos")
    @ResponseBody
    public AjaxJson savePos(@RequestBody FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.saveFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 添加协定存款终止
     * @param fcAgreedDeposit
     * @return
     */
    @ApiOperation("添加协定存款终止登记表")
    @LogMessage(content = "添加协定存款终止登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/saveTerm")
    @ResponseBody
    public AjaxJson saveTerm(@RequestBody FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.saveFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 添加协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @ApiOperation("协定存款复核")
    @LogMessage(content = "协定存款复核",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/opCheck")
    @ResponseBody
    public AjaxJson opCheck(@RequestBody FcAgreedDepositVO fcAgreedDeposit){
        fcAgreedDepositService.opCheck(fcAgreedDeposit,this.getCurrentUser());
        return success();
    }
    /**
     * 修改协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @ApiOperation("修改协定存款登记表")
    @LogMessage(content = "修改协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.updateFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 修改协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @ApiOperation("修改协定存款登记表")
    @LogMessage(content = "修改协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyPos")
    @ResponseBody
    public AjaxJson modifyPos(@RequestBody FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.updateFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 修改协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @ApiOperation("修改协定存款登记表")
    @LogMessage(content = "修改协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyTerm")
    @ResponseBody
    public AjaxJson modifyTerm(@RequestBody FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.updateFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 删除协定存款信息
     * @param id
     * @return
     */

    @ApiOperation("删除协定存款登记表")
    @LogMessage(content = "删除协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        fcAgreedDepositService.delFcAgreedDeposit(id);
        return success();
    }
    /**
     * 获取资金池
     * @param opType 0登记 1延期 2终止
     * @param checkSign 0 登记标记 1 复核标记
     * @return
     */
    @ApiOperation("获取资金池")
    @PostMapping(path = "/getUserCorpPools")
    @ResponseBody
    public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPools(String opType,String checkSign) {
        String resource = "";
        if(AgreedConstant.POOL_SIGN_0.equals(checkSign)){
            if(AgreedConstant.BUS_TYPE_1.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_QUERY;
            }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_QUERY;
            }else{
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_QUERY;
            }
        }else{
            if(AgreedConstant.BUS_TYPE_1.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POSCHECK_QUERY;
            }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERMCHECK_QUERY;
            }else{
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REGCHECK_QUERY;
            }
        }
        return success(fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource));
    }
    /**
     * 获取资金池
     * @param poolId
     * @param opType 0登记 1延期 2终止
     * @param checkSign 0 登记标记 1 复核标记
     * @return
     */
    @ApiOperation("获取资金池子单位")
    @PostMapping(path = "/listUserCorpPoolChilds")
    @ResponseBody
    public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPoolChilds(String poolId, String opType, String checkSign) {
        String resource = "";
        if(AgreedConstant.POOL_SIGN_0.equals(checkSign)){
            if(AgreedConstant.BUS_TYPE_1.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_QUERY;
            }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_QUERY;
            }else{
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_QUERY;
            }
        }else{
            if(AgreedConstant.BUS_TYPE_1.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POSCHECK_QUERY;
            }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERMCHECK_QUERY;
            }else{
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REGCHECK_QUERY;
            }
        }
        return success(fcCorpPoolService.getUserCorpPoolChilds(this.getCurrentUser(), resource, poolId));
    }
    /**
     * 根据资金池或开户单位查找内部账号
     * @param netId
     * @param corpId
     * @return
     */
    @ApiOperation("根据资金池或开户单位查找内部账号")
    @PostMapping(path = "/getBlnctrAcc")
    @ResponseBody
    public AjaxJson<List<FcAccVO>> getBlnctrAcc(String netId, String corpId, String busType){
        List<FcAccVO> list = fcAgreedDepositService.getBlnctrAcc(this.getCurrentUser(),netId,corpId,busType);
        return success(list);
    }
    /**
     * <b>功能介绍:获取费用明细</b><br>
     * <b>日期：</b>2017年6月15日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    @ApiOperation("获取费用明细")
    @PostMapping("/listFcAgreedDetail")
    @ResponseBody
    public AjaxJson<List<FcAgreedDetail>> listFcChargeTypeDtls(String agreedId){
        return  success(fcAgreedDetailService.getDetailList(agreedId));
    }
    /**
     * 根据账户id查找协定信息
     * @param accId
     * @return
     */
    @ApiOperation("根据账户id查找协定信息")
    @PostMapping(path = "/getInfo")
    @ResponseBody
    public AjaxJson getInfo(String accId,String busType){
        AjaxJson j=new AjaxJson();
        FcAgreedDepositVO vo = fcAgreedDepositService.getAgreeInfo(accId,busType);
        j.setSuccess(true);
        j.setObj(vo);
        return j;
    }
    /**
     * 数据校验
     *
     * @param vo
     * @return
     */
    private boolean dataCheck(FcAgreedDepositVO vo) {
        boolean flag = true;
        if(InnerFinanceConstant.BUS_TYPE_0.equals(vo.getOpType())){
            List<FcAgreedDepositVO> contractList = fcAgreedDepositService.getInfoByContractNo(vo.getContractNo());
            if (!EmptyUtils.isEmptyList(contractList)) {
                if (StringUtils.isBlank(vo.getId())) {
                    throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_CONTRACTNO_UNIQUE, vo.getContractNo());
                }else if (!vo.getId().equals(contractList.get(0).getId())) {
                    throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_CONTRACTNO_UNIQUE, vo.getContractNo());
                }
            }
        }
        List<Integer> status = new ArrayList<Integer>();
        status.add(AgreedConstant.STATUE_TEMP);
        status.add(AgreedConstant.STATUE_BACK);
        List<FcAgreedDepositVO> agreedList = fcAgreedDepositService.getAgreedInfo(vo.getAccId(), vo.getOpType(), status);
        if (!EmptyUtils.isEmptyList(agreedList)) {
            if (StringUtils.isBlank(vo.getId())) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_BLNCTRACC_USE, vo.getBlnctrAcc());
            } else if (!vo.getId().equals(agreedList.get(0).getId())) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_BLNCTRACC_USE, vo.getBlnctrAcc());
            }
        }
        return flag;
    }
    /**
     * 根据资金池获取日期
     * @param netId
     * @return
     */
    @ApiOperation("根据资金池获取日期")
    @PostMapping(path = "/getBeginDate")
    @ResponseBody
    public AjaxJson getBeginDate(String netId){
        AjaxJson j=new AjaxJson();
        Date tallyDate = fcDayCloseService.getPoolDayCloseDate(netId);
        tallyDate = DateUtils.addDay(tallyDate, 1);
        String dateStr = DateUtils.formatDate(tallyDate);
        j.setSuccess(true);
        j.setObj(dateStr);
        return j;
    }
}
