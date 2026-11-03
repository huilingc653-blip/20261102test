package com.bytter.biz.saas.controller.bankacc;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.inneraccount.service.BtBankAccDelHandleService;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccDelHandlePageVO;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.service.FcReceiptTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccCommonService;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号注销办理
 * @date 2017-05-23 17:37:39
 */
@Api(value = "银行账号注销办理",tags = "银行账号注销办理")
@RestController("v1BtBankAccDelHandleController")
@RequestMapping("/v1-payment/bankacc/btbankaccdelhandle")
public class BtBankAccDelHandleController extends BaseController {
    @Autowired
    private BtBankAccDelHandleService btBankAccDelHandleService;
    @DubboReference(check = false)
    private IBtBankAccCommonService btBankAccCommonService;
    @Autowired
    private FcBankAccGroupService fcBankAccGroupService;
    @Autowired
    private FcReceiptTypeService fcReceiptTypeService;

    /**
     * 银行账号申请表 table列表
     *
     * @return
     */
    @ApiOperation("银行账号注销办理")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccAppDTO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btBankAccDelHandleService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 银行账号申请表详情页
     *
     * @return
     */
    @ApiOperation("银行账号注销办理查看")
    @LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    @ResponseBody
    public AjaxJson<BtBankAccDelHandlePageVO> view(@PathVariable("id") String id, Model model) {
        BtBankAccDelHandlePageVO pageVO = new BtBankAccDelHandlePageVO();
        BtBankAccAppDTO btBankAccVO = btBankAccCommonService.getBtBankAccAppVOById(id);
        //查询所有账户组
        List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
        HashMap<String, String> map = new HashMap<>();
        for (FcBankAccGroup fcBankAccGroup : validPayType) {
            map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
        }
        //查询账户组名称
        String fcAccGroupCode = btBankAccVO.getFcAccGroupCode();
        if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
            List<String> codes = Arrays.asList(fcAccGroupCode.split(","));
            StringBuilder accGroupName = new StringBuilder();
            for(int i = 0;i<=codes.size()-1;i++){
                if (i==codes.size()-1){
                    if (map.containsKey(codes.get(i))){
                        accGroupName.append(map.get(codes.get(i)));
                    }
                }else{
                    if (map.containsKey(codes.get(i))){
                        accGroupName.append(map.get(codes.get(i))+",");
                    }
                }
            }
            btBankAccVO.setFcAccGroupName(accGroupName.toString());
        }
        //获取所有的单据类型
        List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
        HashMap<String, String> fcReceiptMap = new HashMap<>();
        for (FcReceiptType fcReceiptType : validFcReceiptType) {
            fcReceiptMap.put(fcReceiptType.getReceiptCode(),fcReceiptType.getReceiptName());
        }
        if (EmptyUtils.isNotEmpty(btBankAccVO.getReceiptCode())){
            List<String> receiptCodeList = Arrays.asList(btBankAccVO.getReceiptCode().split(","));
            StringBuilder fcReceiptType = new StringBuilder();
            for(int i = 0;i<=receiptCodeList.size()-1;i++){
                if (i==receiptCodeList.size()-1){
                    if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
                        fcReceiptType.append(fcReceiptMap.get(receiptCodeList.get(i)));
                    }
                }else{
                    if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
                        fcReceiptType.append(fcReceiptMap.get(receiptCodeList.get(i))+",");
                    }
                }
            }
            if(ObjectUtil.isNotEmpty(fcReceiptType)) btBankAccVO.setReceiptName(fcReceiptType.toString());
        }
        pageVO.setBtBankAccAppPage(btBankAccVO);
        return success(pageVO);
    }
    /**
     * 银行账号申请表详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDELHANDLE_APPLY)
    @ApiOperation("table页面的注销办理操作")
    @LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/getInfoBeforeUpdate")
    @ResponseBody
    public AjaxJson<BtBankAccDelHandlePageVO> getInfoBeforeUpdate(String id) {
        BtBankAccDelHandlePageVO pageVO = new BtBankAccDelHandlePageVO();
        BtBankAccAppDTO vo = btBankAccCommonService.getBtBankAccAppVOById(id);
        //查询所有账户组
        List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
        HashMap<String, String> map = new HashMap<>();
        for (FcBankAccGroup fcBankAccGroup : validPayType) {
            map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
        }
        //查询账户组名称
        String fcAccGroupCode = vo.getFcAccGroupCode();
        if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
            List<String> codes = Arrays.asList(fcAccGroupCode.split(","));
            StringBuilder accGroupName = new StringBuilder();
            for(int i = 0;i<=codes.size()-1;i++){
                if (i==codes.size()-1){
                    if (map.containsKey(codes.get(i))){
                        accGroupName.append(map.get(codes.get(i)));
                    }
                }else{
                    if (map.containsKey(codes.get(i))){
                        accGroupName.append(map.get(codes.get(i))+",");
                    }
                }
            }
            vo.setFcAccGroupName(accGroupName.toString());
        }
        //获取所有的单据类型
        List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
        HashMap<String, String> fcReceiptMap = new HashMap<>();
        for (FcReceiptType fcReceiptType : validFcReceiptType) {
            fcReceiptMap.put(fcReceiptType.getReceiptCode(),fcReceiptType.getReceiptName());
        }
        if (EmptyUtils.isNotEmpty(vo.getReceiptCode())){
            List<String> receiptCodeList = Arrays.asList(vo.getReceiptCode().split(","));
            StringBuilder fcReceiptType = new StringBuilder();
            for(int i = 0;i<=receiptCodeList.size()-1;i++){
                if (i==receiptCodeList.size()-1){
                    if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
                        fcReceiptType.append(fcReceiptMap.get(receiptCodeList.get(i)));
                    }
                }else{
                    if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
                        fcReceiptType.append(fcReceiptMap.get(receiptCodeList.get(i))+",");
                    }
                }
            }
            if(ObjectUtil.isNotEmpty(fcReceiptType)) vo.setReceiptName(fcReceiptType.toString());
        }
        pageVO.setBtBankAccAppPage(vo);
        pageVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_DEL);
        return success(pageVO);
    }
    /**
     * 注销办理
     *
     * @param btBankAccVO
     * @return
     */
    @ApiOperation("修改银行账号申请表")
    @LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody BtBankAccVO btBankAccVO) {
        btBankAccDelHandleService.toModify(btBankAccVO, this.getCurrentUser());
        return success();
    }
    /**
     * 打回
     *
     * @param id
     * @return
     */
    @ApiOperation("打回操作")
    @LogMessage(content = "删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        this.btBankAccDelHandleService.back(id, this.getCurrentUser());
        return success();
    }
}
