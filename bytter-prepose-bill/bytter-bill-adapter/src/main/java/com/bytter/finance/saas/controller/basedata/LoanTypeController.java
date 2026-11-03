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
import com.bytter.finance.enums.BaseDataMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhengxn
 * @version 1.0
 * @Title: 贷款类型维护
 * @Description: CMS_LOAN_TYPE
 * @date 2017-07-19 14:05:55
 */
@Api(value = "/v1-cms/cms/credit/loantype", tags = "贷款类型维护")
@RestController("v1LoanTypeController")
@RequestMapping("/v1-cms/cms/credit/loantype")
public class LoanTypeController extends BaseController {
    @Autowired
    private LoanTypeService loanTypeService;
    /**
     * 查询贷款类型数据
     **/
    @ApiOperation("查询贷款类型数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(loanTypeService.queryLoanType(tablesInput));
    }

    /**
     * 编辑页面数据
     **/
    @ApiOperation("编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<LoanType> updatePage(String id) {
        LoanType loanType = new LoanType();
        if (StringUtils.isNotEmpty(id)) {
            loanType = loanTypeService.get(loanType.getId());
        }
        return success(loanType);
    }
    /**
     * 增加贷款类型
     **/
    @LogMessage(content = "添加CMS_LOAN_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("增加贷款类型")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody LoanType loanType) {
        String type = BaseDataConstant.TYPE_CREDIT;
        Integer status = BaseDataConstant.STATUS_REDACT;
        loanType.setType(type);
        loanType.setStatus(status);
        loanTypeService.saveLoanType(loanType, getCurrentUser());
        return success();
    }
    /**
     * 编辑贷款类型
     **/
    @LogMessage(content = "修改CMS_LOAN_TYPE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @ApiOperation("编辑贷款类型")
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody LoanType loanType) {
        loanTypeService.verify(loanType);
        String id = loanType.getId();
        String code = loanType.getCode();
        String name = loanType.getName();
        String type = loanType.getType();
        // 用户名
        String username = getCurrentUser().getUsername();
        // 登陆名
        String loginname = getCurrentUser().getLoginName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sdf.format(new Date());
        loanTypeService.updateLoan(id, code, name, loginname, username, date, type,loanType.getCmsLoanTypeManagerId());
        return success();
    }
    /**
     * 删除贷款类型
     **/
    @LogMessage(content = "删除CMS_LOAN_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("删除贷款类型")
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
}
