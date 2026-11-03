package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.biz.inneraccount.dto.v1.FcFreezeSearchDTO;
import com.bytter.biz.inneraccount.service.FcFreezeService;
import com.bytter.biz.inneraccount.vo.FcFreezeListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeSearchListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeVO;
import com.bytter.biz.inneraccount.vo.v1.FcAccFreezePageVO;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 内部账号冻结
 * @date 2017-06-16 17:31:26
 */
@Api(value = "内部账号冻结" ,tags ="内部账号冻结")
@RestController("v1FcFreezeController")
@RequestMapping("/v1-payment/inneraccount/fcfreeze")
public class FcFreezeController extends BaseController {
    @Autowired
    private FcFreezeService fcFreezeService;
    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * 内部账号冻结 table列表
     *
     * @return
     */
    @ApiOperation("内部账号冻结 table列表,申请和复核共用接口 通过opType区别 申请为S 复核为C")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcFreezeListVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcFreezeService.getDataTablesUnion(input, getCurrentUser()));
    }

    @ApiOperation("根据id查询内部账号冻结信息")
    @ResponseBody
    @GetMapping("/getById")
    public AjaxJson<FcFreezeVO> getById(String id){
        FcFreezeVO fcFreezeVO = fcFreezeService.getById(id);
        return success(fcFreezeVO);
    }

    /**
     * 内部账号冻结编辑页面跳转
     *
     * @return
     */
    @ApiOperation("内部账号冻结编辑页面跳转前 数据获取")
    @GetMapping("/updateOrView")
    @ResponseBody
    public AjaxJson<FcAccFreezePageVO> updateOrView(FcFreezeSearchDTO fcFreezeSearchDTO) {
        if (StringUtils.isNotEmpty(fcFreezeSearchDTO.getId())) {
            FcAccFreezePageVO pageVO = new FcAccFreezePageVO();
            FcFreezeVO fcFreeze = new FcFreezeVO();
            fcFreeze.setId(fcFreezeSearchDTO.getId());
            FcFreezeListVO fcFreezeVO = fcFreezeService.getEntityById(fcFreeze);
            if (fcFreezeVO == null) {
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS, fcFreeze.getBlnctrAcc());
            }
            pageVO.setFcFreezePage(fcFreezeVO);
            pageVO.setOpType(fcFreezeSearchDTO.getOpType());
            pageVO.setNetCode(corpService.get(fcFreezeVO.getNetId()).getCode());
            pageVO.setCorpCode(corpService.get(fcFreezeVO.getCorpId()).getCode());
            return success(pageVO);
        }
        return success();
    }
    /**
     * 添加内部账号冻结
     *
     * @param fcFreeze
     * @return
     */
    @ApiOperation(value = "添加/编辑内部账号冻结",
            notes = "新增与编辑都用该接口  \n" +
            "通过 opType 来区别  新增则为A   修改为M\n" +
            "通过 传输对象的status来区别 暂存 和 提交   0为暂存  11为提交")
    @LogMessage(content = "添加/编辑内部账号冻结", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody FcFreezeVO fcFreeze) {
        fcFreezeService.saveEntity(fcFreeze, getCurrentUser());
        return success();
    }

    @ApiOperation("内部账号冻结复核")
    @LogMessage(content = "内部账号冻结复核", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/check")
    @ResponseBody
    public AjaxJson check(@RequestBody FcFreezeVO fcFreeze) {
        fcFreezeService.checkEntity(fcFreeze, getCurrentUser());
        return success();
    }
    /**
     * 删除内部账号冻结
     *
     * @param id
     * @return
     */
    @ApiOperation("删除内部账号冻结")
    @LogMessage(content = "删除内部账号冻结", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        fcFreezeService.delete(id, getCurrentUser());
        return success();
    }
    /**
     * 功能介绍 : 获取有效的内部账户。
     * 以后扩展为有权限的内部账户<br>
     * <b>日期：</b>2017年6月12日<br>
     */
    @ApiOperation("获取有效的内部账户")
    @GetMapping("/getValidAccountByCorpId/{corpId}")
    @ResponseBody
    public AjaxJson<List<FcFreezeListVO>> getValidAccountByCorpId(@PathVariable("corpId") String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            return success(fcFreezeService.getValidAccountByCorpId(corpId));
        }
        return success();
    }
    /**
     * 功能介绍 : 获取历史账号。<br>
     * <b>日期：</b>2017年6月12日<br>
     */
    @ApiOperation("获取历史账号")
    @GetMapping("/getHisListByAccount/{account}")
    @ResponseBody
    public AjaxJson<List<FcFreezeVO>> getHisListByAccount(@PathVariable("account") String account) {
        if (StringUtils.isNotEmpty(account)) {
            return success(fcFreezeService.getHisListByAccount(account));
        }
        return success();
    }

    /**
     * 内部账号冻结解冻查询
     *
     * @return
     */
    @ApiOperation("内部账号管理->查询->获取存款冻结解冻查询")
    @PostMapping("/dataTableAll")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcFreezeSearchListVO>> dataTableAll(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(fcFreezeService.getDataTableAll(input, getCurrentUser()));
    }
    /**
     * 可冻冻记录列表
     *
     * @return
     */
    @ApiOperation("可冻结记录列表 传入corpId")
    @PostMapping("/getTableData")
    @ResponseBody
    public AjaxJson<List<FcFreezeListVO>> getTableData(@RequestBody FcFreezeSearchDTO fcFreezeSearchDTO) {
        String corpId = fcFreezeSearchDTO.getCorpId();
        if (StringUtils.isNotEmpty(corpId)) {
            return success(fcFreezeService.getValidAccountByCorpId(corpId));
        }
        return success(new ArrayList<>());
    }
}
