package com.bytter.finance.saas.controller.basedata;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.entity.LoanType;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.dto.LoanTypeDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhengxn
 * @version 1.0
 * @Title: 借款类型维护
 * @Description: CMS_LOAN_TYPE
 * @date 2017-07-19 15:47:15
 */
@Api(value = "/v1-cms/cms/credit/borrowingtype", tags = "借款类型维护")
@RestController("v1BorrowingTypeController")
@RequestMapping("/v1-cms/cms/credit/borrowingtype")
public class BorrowingTypeController extends BaseController {
    @Autowired
    private LoanTypeService loanTypeService;
    @DubboReference(check = false)
    private Ii18nService ii18nService;

    /**
     * 列表内容
     **/
    @ApiOperation("查询借款类型列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(loanTypeService.queryBorrowType(input));
    }

    /**
     * 新增页面数据
     **/
    @ApiOperation("新增页面数据")
    @GetMapping("/add")
    public AjaxJson<LoanType> add(LoanType borrowingType) {
        if (StringUtils.isNotEmpty(borrowingType.getId())) {
            borrowingType = loanTypeService.get(borrowingType.getId());
        }
        return success(borrowingType);
    }

    /**
     * 编辑页面数据
     **/
    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<LoanType> updatePage(LoanType borrowingType) {
        if (StringUtils.isNotEmpty(borrowingType.getId())) {
            borrowingType = loanTypeService.get(borrowingType.getId());
        }
        return success(borrowingType);
    }

    /**
     * 新增借款类型
     **/
    @LogMessage(content = "添加CMS_LOAN_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("新增借款类型")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody LoanType borrowingType) {
        String type = BaseDataConstant.TYPE_BORROW;
        Integer status = BaseDataConstant.STATUS_REDACT;
        borrowingType.setStatus(status);
        borrowingType.setType(type);
        loanTypeService.saveLoanType(borrowingType, getCurrentUser());
        return success();
    }

    /**
     * 修改借款类型
     **/
    @LogMessage(content = "修改CMS_LOAN_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("修改借款类型")
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody LoanType borrowingType) {
        loanTypeService.verify(borrowingType);
        String id = borrowingType.getId();
        String code = borrowingType.getCode();
        String name = borrowingType.getName();
        String type = borrowingType.getType();
        // 用户名
        String username = getCurrentUser().getUsername();
        // 登陆名
        String loginname = getCurrentUser().getLoginName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sdf.format(new Date());
        List<I18nDTO> i18nDTOS = ii18nService.listBusinessBaseByKeyId(id);
        for (I18nDTO i18nDTO : i18nDTOS) {
            if (i18nDTO.getLocale().equals(LocaleContextHolder.getLocale().toString())) {
                i18nDTO.setContext(name);
                i18nDTO.setKeyId(id);
                ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
            }
        }
        loanTypeService.updateLoan(id, code, name, loginname, username, date, type,borrowingType.getCmsLoanTypeManagerId());
        return success();
    }

    /**
     * 删除
     **/
    @LogMessage(content = "删除CMS_LOAN_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("删除借款类型")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        LoanType loanType = loanTypeService.get(id);
        String id1 = id;
        String code = loanType.getCode();
        String type = loanType.getType();
        // 是否已被引用，否则不可删除
        boolean flag = loanTypeService.judgeLoanTypeByCode(code, type);
        if (!flag) {
            throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REFERENCED_EXIST);
        }
        loanTypeService.deleteLoan(id1);
        return success();
    }

    /**
     * 借款类型维护选择系统借款类型数据查询
     *
     * @return
     */
    @ApiOperation("查询系统借款类型列表")
    @GetMapping("/queryCmsLoanTypeManagerList")
    public AjaxJson<List<LoanTypeDTO>> queryCmsLoanTypeManagerList() {
        return success(loanTypeService.queryCmsLoanTypeManagerList(BaseDataConstant.TYPE_BORROW));
    }
}
