package com.bytter.biz.saas.controller.allocation;

import com.bytter.biz.allocation.entity.ExcRulesManage;
import com.bytter.biz.allocation.service.ExcRulesManageService;
import com.bytter.biz.allocation.vo.ExcRulesManageAdCorpVO;
import com.bytter.biz.allocation.vo.ExcRulesManageVO;
import com.bytter.biz.bdlink.api.enums.AllocationMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhoup
 * @Description 例外规则表
 * @date 2018/2/6
 */
@Api(value = "/v1-payment/allocation/excrulesman", tags = "例外规则管理")
@RestController("v1ExcRulesManageController")
@RequestMapping("/v1-payment/allocation/excrulesman")
public class ExcRulesManageController extends BaseController {
    @Autowired
    private ExcRulesManageService excRulesManageService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;

    /**
     * 展示列表数据
     *
     * @param datatablesInput
     * @return
     */
    @ApiOperation("展示列表数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<ExcRulesManageAdCorpVO>> datatable(@RequestBody V1DataTablesInput datatablesInput) {
        DataTablesInput input = datatablesInput.convertAndInitColumns();
        return AjaxJson.success(excRulesManageService.getDataTablesUnion(input, this.getCurrentUser()));
    }


    /**
     * 展示页面
     *
     * @param id
     * @return
     */
    @ApiOperation("展示页面")
    @LogMessage(content = "例外规则管理表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<ExcRulesManageAdCorpVO> view(@PathVariable("id") String id) {
        ExcRulesManageAdCorpVO excRulesManage = excRulesManageService.getByUnion(id);
        return AjaxJson.success(excRulesManage);
    }

    /**
     * 更新页面
     *
     * @param id
     * @return
     */
    @ApiOperation("更新页面")
    @GetMapping("/update/{id}")
    public AjaxJson<ExcRulesManage> update(@PathVariable("id") String id) {
        return AjaxJson.success(excRulesManageService.findById(id));
    }

    /**
     * 新增保存数据
     *
     * @param excRluesManageVO
     * @return
     */
    @ApiOperation("新增保存数据")
    @LogMessage(content = "例外规则管理表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody ExcRulesManageVO excRluesManageVO) {
        excRulesManageService.save(excRluesManageVO, getCurrentUser());
        return success();
    }

    /**
     * 修改后保存数据
     *
     * @param excRluesManage
     * @return
     */
    @ApiOperation("修改后保存数据")
    @LogMessage(content = "例外规则管理表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody ExcRulesManage excRluesManage) {
        //判断收付款账号是否相同
        if (excRluesManage.getPayerAccNo().equals(excRluesManage.getPayeeAccNo())) {
            throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOEQUALPAYEENO, excRluesManage.getPayerAccNo());
        }
        List<ExcRulesManage> listExc = excRulesManageService.getByPayeeNoAndPayerNo(excRluesManage.getPayeeAccNo(), excRluesManage.getPayerAccNo());
        int i = listExc.size();
        for (ExcRulesManage iteratorExc : listExc) {
            if (iteratorExc.getId().equals(excRluesManage.getId())) {
                i--;
            }
        }
        if (i > 0) {
            //判断此条数据是否已存在
            throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOEXIST, excRluesManage.getPayerAccNo(), excRluesManage.getPayeeAccNo());
        }
        ExcRulesManage exRule = excRulesManageService.get(excRluesManage.getId(), true);
        BeanUtils.copyNotNullProperties(excRluesManage, exRule);
        exRule.setUpdateBy(getCurrentUser().getLoginName());
        exRule.setUpdateDate(DateUtils.getDate());
        excRulesManageService.saveOrUpdate(exRule);
        return success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation("删除")
    @LogMessage(content = "例外规则管理表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ExcRulesManage excRulesManage = excRulesManageService.get(id);
        excRulesManageService.deleteEntity(excRulesManage);
        return success();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除")
    @LogMessage(content = "例外规则管理表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        this.excRulesManageService.batchDelete(ids);
        return success();
    }

    /**
     * 根据收款账号Id获取例外规则管理表
     *
     * @param payerAccId
     * @return
     */
    @PostMapping("/getExcRulesManage")
    public AjaxJson<List<ExcRulesManage>> getFcAccBookDtls(String payerAccId) {
        List<ExcRulesManage> excRluesManage = new ArrayList<>();
        if (StringUtils.isNotEmpty(payerAccId)) {
            excRluesManage = excRulesManageService.findBypayerAccId(payerAccId);
        }
        return AjaxJson.success(excRluesManage);
    }

    /**
     * 获取付款账号
     *
     * @param corpId
     * @return
     */
    @PostMapping("/getBankAcc")
    public AjaxJson<List<BtBankAccDTO>> getBankAcc(String corpId) {
        List<BtBankAccDTO> listBankAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(corpId)) {
            List<BtBankAccDTO> getBankAcc = accountGrantService.listBy(corpId, AccountGrantConstant.ONLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(getBankAcc)) {
                listBankAcc.addAll(getBankAcc);
            }
        }
        return AjaxJson.success(listBankAcc);
    }


}
