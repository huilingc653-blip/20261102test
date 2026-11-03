package com.bytter.finance.web.basedata.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.entity.LoanType;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.enums.BaseDataMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author zhengxn
 * @version 1.0
 * @Title: 贷款类型维护
 * @Description: CMS_LOAN_TYPE
 * @date 2017-07-19 14:05:55
 */
@Controller
@RequestMapping("/cms/credit/loantype")
public class LoanTypeController extends BaseController {
    @Autowired
    private LoanTypeService loanTypeService;
    /**
     * 查询贷款类型列表
     **/
    @LogMessage(content = "查询CMS_LOAN_TYPE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/credit/listLoanType";
    }
    /**
     * 查询贷款类型数据
     **/
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return loanTypeService.queryLoanType(dataTablesInput);
    }
    /**
     * 新增页面跳转
     **/
    @GetMapping("/add")
    public String add(LoanType loanType, Model model) {
        if (StringUtils.isNotEmpty(loanType.getId())) {
            loanType = loanTypeService.get(loanType.getId());
            model.addAttribute("loanTypePage", loanType);
        }
        return "cms/credit/addLoanType";
    }
    /**
     * 编辑页面跳转
     **/
    @GetMapping("/update")
    public String update(LoanType loanType, Model model) {
        if (StringUtils.isNotEmpty(loanType.getId())) {
            loanType = loanTypeService.get(loanType.getId());
            model.addAttribute("loanTypePage", loanType);
        }
        return "cms/credit/updateLoanType";
    }
    /**
     * 增加贷款类型
     **/
    @LogMessage(content = "添加CMS_LOAN_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(LoanType loanType) {
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
    // @LogMessage(content = "修改CMS_LOAN_TYPE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(LoanType loanType) {
        loanTypeService.verify(loanType);
        String id = loanType.getId();
        String code = loanType.getCode();
        String name = loanType.getName();
        String type = loanType.getType();
        // 用户名
        String username = getUserVo().getUsername();
        // 登陆名
        String loginname = getUserVo().getLoginName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sdf.format(new Date());
        loanTypeService.updateLoan(id, code, name, loginname, username, date, type,loanType.getCmsLoanTypeManagerId());
        return success();
    }
    /**
     * 删除贷款类型
     **/
    @LogMessage(content = "删除CMS_LOAN_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
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
