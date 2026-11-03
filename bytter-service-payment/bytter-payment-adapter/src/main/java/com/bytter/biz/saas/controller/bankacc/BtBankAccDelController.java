package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.inneraccount.service.BtBankAccDelService;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccAppRevPageVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccRevPageVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccViewVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccAppService;
import com.bytter.system.api.IBtBankAccCommonService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号注销
 * @date 2017-05-23 17:37:39
 */
@Api(value = "银行账号注销",tags = "银行账号注销")
@RestController("v1BtBankAccDelController")
@RequestMapping("/v1-payment/bankacc/btbankaccdel")
public class BtBankAccDelController extends BaseController {
    @Autowired
    private BtBankAccDelService btBankAccDelService;
    @DubboReference(check = false)
    private IBtBankAccAppService btBankAccAppService;
    @DubboReference(check = false)
    private IBtBankAccCommonService btBankAccCommonService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;

    /**
     * 银行账号注销表 table列表
     *
     * @return
     */
    @ApiOperation("查询银行账号注销表列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccAppDTO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btBankAccDelService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    @ApiOperation("isFrom为App的银行账号注销申请编辑的页面数据获取")
    @GetMapping("/getAccAppUpdateInfo")
    @ResponseBody
    public AjaxJson<BtBankAccAppRevPageVO> getAccAppUpdateInfo(String id) {
        BtBankAccAppRevPageVO btBankAccAppRevPageVo = new BtBankAccAppRevPageVO();
        if (StringUtils.isNotEmpty(id)) {
            BtBankAccAppDTO btBankAccAppVO = btBankAccAppService.getBankAccApp(id);
            String curId = btBankAccCommonService.setCurId(btBankAccAppVO.getId(), "app");
            btBankAccAppRevPageVo.setCurId(curId);
            btBankAccAppRevPageVo.setBtBankAccAppPage(btBankAccAppVO);
            //银行类别对应的境内外标识
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccAppVO.getBankTypeId());
            btBankAccAppRevPageVo.setIsDomesticBankType(btBankAccAppVO.getIsDomesticBank() == null ? "" : btBankAccAppVO.getIsDomesticBank());
        }
        return success(btBankAccAppRevPageVo);
    }

    @ApiOperation("isFrom为Acc的银行账号注销申请编辑的页面数据获取")
    @GetMapping("/getAccUpdateInfo")
    @ResponseBody
    public AjaxJson<BtBankAccRevPageVO> getAccUpdateInfo(String id) {
        BtBankAccRevPageVO btBankAccRevPageVo = new BtBankAccRevPageVO();
        if (StringUtils.isNotEmpty(id)) {
            BtBankAccDTO btBankAccVO = btBankAccCommonService.getBtBankAccVOById(id);
            String curId = btBankAccCommonService.setCurId(btBankAccVO.getId(), "acc");
            btBankAccRevPageVo.setCurId(curId);
            btBankAccRevPageVo.setBtBankAccAppPage(btBankAccVO);
            //银行类别对应的境内外标识
            btBankAccRevPageVo.setIsDomesticBankType(btBankAccVO.getIsDomesticBank() == null ? "" : btBankAccVO.getIsDomesticBank());
        }
        return success(btBankAccRevPageVo);
    }
    /**
     * 银行账号注销表详情页
     *
     * @return
     */
    @ApiOperation("查询银行账号注销申请表详情")
    @LogMessage(content = "查询银行账号注销申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    @ResponseBody
    public AjaxJson<BtBankAccViewVO> view(@PathVariable("id") String id) {
        BtBankAccViewVO btBankAccViewVO = new BtBankAccViewVO();
        BtBankAccAppDTO vo = btBankAccAppService.getBankAccApp(id);
        btBankAccViewVO.setBtBankAccAppPage(vo);
        btBankAccViewVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_DEL);
        return success(btBankAccViewVO);
    }
    /**
     * 修改银行账号注销
     *
     * @param btBankAccApp
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_APPLY)
    @ApiOperation("修改银行账号注销申请请求")
    @LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody BtBankAccAppDTO btBankAccApp) {
        btBankAccDelService.toModify(btBankAccApp, this.getCurrentUser());
        return success();
    }
    /**
     * 删除银行账号注销
     *
     * @param id
     * @return
     */
    @ApiOperation("删除银行账号注销申请请求")
    @LogMessage(content = "删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        this.btBankAccDelService.delete(id, this.getCurrentUser());
        return success();
    }
    /**
     * 批量删除银行账号注销
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除银行账号申请表")
    @LogMessage(content = "批量删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.btBankAccDelService.delete(id, this.getCurrentUser());
        }
        return success();
    }
}
