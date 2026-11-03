package com.bytter.system.saas.controller.bankacc;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtBankAccApp;
import com.bytter.system.service.BtBankAccCommonService;
import com.bytter.system.service.BtBankAccService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Api(value = "/v1-system/bankacc/btbankacc", tags = "银行账户相关")
@RestController("v1BtBankAccController")
@RequestMapping("/v1-system/bankacc/btbankacc")
public class BtBankAccController extends BaseController {
    @Autowired
    private BtBankAccService btBankAccService;
    @Autowired
    private IBtBankAccService btBankAccServiceI;
    @Autowired
    private BtBankAccCommonService btBankAccCommonService;

    /**
     * 查询已选择账号
     *
     * @return
     */
    @ApiOperation("支付账号查询已选择账号")
    @PostMapping("/selectBankAccView")
    public AjaxJson<List<BtBankAccVO>> selectBankAccView(@RequestBody BtBankAccVO bankAcc) {
        List<BtBankAccVO> list = btBankAccService.selectBankAccView(bankAcc);
        return AjaxJson.success(list);
    }


    /**
     * 添加BT_BANK_ACC
     *
     * @param btBankAcc
     * @return
     */

    @LogMessage(content = "添加BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody BtBankAcc btBankAcc) {
        btBankAccService.save(btBankAcc);
        return success();
    }

    /**
     * 删除BT_BANK_ACC
     *
     * @param id
     * @return
     */
    @LogMessage(content = "打回BT_BANK_ACC_APP", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        this.btBankAccService.back(id, this.getCurrentUser());
        return success();
    }

    /**
     * 批量删除BT_BANK_ACC
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtBankAcc btBankAcc = btBankAccService.get(id);
            btBankAccService.deleteEntity(btBankAcc);
        }
        return success();
    }
    /******************
     * 对外url获取数据
     *******************/
    /**
     * 获取银行类型维护列表
     *
     * @return
     */
    @GetMapping("/json")

    public AjaxJson<List<BtBankAcc>> getBankaccList() {
        return AjaxJson.success(btBankAccCommonService.listEffectiveBtBankAcc());
    }

    /**
     * 根据账号id得到账号及父类账户信息
     *
     * @return
     */
    @RequestMapping(params = "getBankAccInfoById")
    public AjaxJson getBankAccAndParentInfoById(String id, String curId) {
        AjaxJson json = new AjaxJson();
        com.bytter.system.api.vo.BtBankAccVO vo = this.btBankAccServiceI.getByUnion(id, curId);
        json.setSuccess(EmptyUtils.isEmpty(vo) ? false : true);
        if (!EmptyUtils.isEmpty(vo) && !EmptyUtils.isBlank(vo.getPrentAccId())) {
            com.bytter.system.api.vo.BtBankAccVO parentBankAcc = btBankAccServiceI.getByUnion(vo.getPrentAccId(), vo.getCurId());
            vo.setParentBankAcc(parentBankAcc);
        }
        json.setObj(vo);
        return json;
    }

    /**
     * 根据单位id得到银行账号列表
     *
     * @return
     */
    @GetMapping("/jsonListByCorpId/{corpId}")
    public AjaxJson<List<BtBankAcc>> getbankAccListbycorpId(@PathVariable("corpId") String corpId) {
        return AjaxJson.success(btBankAccService.getListByCorpId(corpId));
    }

    @PostMapping("/getbankAccDataTable")
    public AjaxJson<List<BtBankAcc>> getbankAccDataTable(String corpId) {
        return AjaxJson.success(btBankAccService.getListByCorpId(corpId));
    }

    /**
     * 根据账号id得到账号
     * zuoy 2017-07-06
     *
     * @return
     */
    @ApiOperation("根据账号id得到账号")
    @GetMapping(path = "/getBankAccById")
    public AjaxJson getBankAccById(String id, String curId) {
        AjaxJson json = new AjaxJson();
        com.bytter.system.api.vo.BtBankAccVO vo = this.btBankAccServiceI.getByUnion(id, curId);
        json.setSuccess(EmptyUtils.isEmpty(vo) ? false : true);
        json.setObj(vo);
        return json;
    }

    /**
     * 根据单位ID查询离线银行账户信息
     * zuoy 2017-08-24
     *
     * @param corpId   单位ID
     * @param isOnline 是否上线
     * @return
     */
    @RequestMapping(params = "getListByCorpIdAndIsOnline")
    public AjaxJson<List<BtBankAccDTO>> getListByCorpIdAndIsOnline(String corpId, String isOnline, String resource) {
        try {
            AccountSearchDTO dto = new AccountSearchDTO(true);
            dto.setUserId(getUserId());
            if (!EmptyUtils.isBlank(corpId)) {
                dto.setCorpId(corpId);
            }
            if (!EmptyUtils.isBlank(isOnline)) {
                dto.setIsOnline(isOnline);
                if (BtBankAccConstant.ISONLINE_0.equals(isOnline)) {
                    dto.setAccountType(AccountGrantConstant.OFFLINE_BANK_TYPE);
                } else {
                    dto.setAccountType(AccountGrantConstant.ONLINE_BANK_TYPE);
                }
            } else {
                dto.setAccountType(AccountGrantConstant.OFFLINE_BANK_TYPE + "," + AccountGrantConstant.ONLINE_BANK_TYPE);
            }
            dto.setPermission(AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isBlank(resource)) {
                if (ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY.contains(resource)) {
                    dto.setResource(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY);
                } else if (ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND.contains(resource)) {
                    dto.setResource(ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND);
                }
            }
            return AjaxJson.success(this.btBankAccService.getBankAccList(dto));
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return AjaxJson.success();
        }
    }

    /**
     * 根据单位ID查询离线银行账户信息
     * zuoy 2017-08-24
     *
     * @param isOnline 是否上线
     * @param resource
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getListByCorpIdAndNotOnline")
    public AjaxJson<List<BtBankAccDTO>> getListByCorpIdAndNotOnline(String isOnline, String resource) {
        try {
            AccountSearchDTO dto = new AccountSearchDTO(true);
            dto.setUserId(getUserId());
            if (!EmptyUtils.isBlank(isOnline)) {
                dto.setIsOnline(isOnline);
                if (BtBankAccConstant.ISONLINE_0.equals(isOnline)) {
                    dto.setAccountType(AccountGrantConstant.OFFLINE_BANK_TYPE);
                } else {
                    dto.setAccountType(AccountGrantConstant.ONLINE_BANK_TYPE);
                }
            } else {
                dto.setAccountType(AccountGrantConstant.OFFLINE_BANK_TYPE + "," + AccountGrantConstant.ONLINE_BANK_TYPE);
            }
            //查询明细的权限
            dto.setPermission(AccountGrantConstant.QUERY_DETAIL);
            if (!EmptyUtils.isBlank(resource)) {
                if (ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY.contains(resource)) {
                    dto.setResource(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY);
                } else if (ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND.contains(resource)) {
                    dto.setResource(ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND);
                }
            }
            return AjaxJson.success(this.btBankAccService.getBankAccList(dto));
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return AjaxJson.success();
        }
    }

    /**
     * 获取实体账号
     *
     * @param btBankAccApp
     * @return
     */
    @GetMapping("/getEntityAcc")
    public AjaxJson<List<BtBankAccVO>> getEntityAcc(@RequestBody BtBankAccApp btBankAccApp) {
        return AjaxJson.success(this.btBankAccService.getEntityAcc(btBankAccApp));
    }

    /**
     * 获取绑定账号
     */
    @PostMapping("/getBindingBankAcc")
    public AjaxJson<List<BtBankAccVO>> getBindingBankAcc(@RequestBody BtBankAccApp btBankAccApp) {
        return AjaxJson.success(this.btBankAccService.getBindingBankAcc(btBankAccApp));
    }


    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableOldBankAcc")
    public DataTablesOutput dataTableOldBankAcc(@RequestBody DataTablesInput dataTablesInput) {
        return this.btBankAccService.dataTableOldBankAcc(dataTablesInput, getCurrentUser());
    }


    @PostMapping("/getListBy")
    public AjaxJson<List<AccountDTO>> getListBy(@RequestBody AccountSearchDTO accountSearchDTO) {
        accountSearchDTO.setUserId(getUserId());
        return AjaxJson.success(this.btBankAccServiceI.listBy(accountSearchDTO));
    }

    /**
     * @param @param  accountSearchDTO
     * @param @return 参数
     * @return List<AccountDTO>    返回类型
     * @throws
     * @Title: getListBy
     * @Description: TODO(增加稽查银行账户信息查询)
     * @author Jinyong.cao
     */
    @PostMapping("/getRasBankListBy")
    public AjaxJson<List<AccountDTO>> getRasBankListBy(@RequestBody AccountSearchDTO accountSearchDTO, HttpServletRequest request) {
        return AjaxJson.success(this.btBankAccService.getRasBankListBy(accountSearchDTO, String.valueOf(RequestContextUtils.getLocale(request))));
    }


    /**
     * 根据银行接口代码得到银行账号，用与银企直联
     * BT_BANK_ACC table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "根据银行接口代码得到银行账号，用与银企直联")
    @PostMapping("/getDataTableByBifCode")
    public AjaxJson<DataTablesOutput<BtBankAccVO>> getDataTableByBifCode(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(btBankAccService.getDataTableByBifCode(input, this.getCurrentUser()));
    }


}
