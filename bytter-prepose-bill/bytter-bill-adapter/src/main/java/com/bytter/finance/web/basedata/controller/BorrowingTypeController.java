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
import com.bytter.finance.dto.LoanTypeDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@Controller
@RequestMapping("/cms/credit/borrowingtype")
public class BorrowingTypeController extends BaseController {
    @Autowired
    private LoanTypeService loanTypeService;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    /**
     * 列表页面跳转
     **/
    @LogMessage(content = "查询CMS_LOAN_TYPE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/credit/listBorrowingType";
    }
    /**
     * 列表内容
     **/
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return loanTypeService.queryBorrowType(dataTablesInput);
    }
    /**
     * 新增页面跳转
     **/
    @GetMapping("/add")
    public String add(LoanType borrowingType, Model model) {
        if (StringUtils.isNotEmpty(borrowingType.getId())) {
            borrowingType = loanTypeService.get(borrowingType.getId());
            model.addAttribute("borrowingTypePage", borrowingType);
        }
        return "cms/credit/addBorrowingType";
    }
    /**
     * 编辑页面跳转
     **/
    @GetMapping("/update")
    public String update(LoanType borrowingType, Model model) {
        if (StringUtils.isNotEmpty(borrowingType.getId())) {
            borrowingType = loanTypeService.get(borrowingType.getId());
            model.addAttribute("borrowingTypePage", borrowingType);
        }
        return "cms/credit/updateBorrowingType";
    }
    /**
     * 新增借款类型
     **/
    @LogMessage(content = "添加CMS_LOAN_TYPE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(LoanType borrowingType) {
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
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(LoanType borrowingType) {
        loanTypeService.verify(borrowingType);
        String id = borrowingType.getId();
        String code = borrowingType.getCode();
        String name = borrowingType.getName();
        String type = borrowingType.getType();
        // 用户名
        String username = getUserVo().getUsername();
        // 登陆名
        String loginname = getUserVo().getLoginName();
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

    /**
     * 借款类型维护选择系统借款类型数据查询
     *
     * @return
     */
    @GetMapping("/queryCmsLoanTypeManagerList")
    @ResponseBody
    public List<LoanTypeDTO> queryCmsLoanTypeManagerList() {
        return loanTypeService.queryCmsLoanTypeManagerList(BaseDataConstant.TYPE_BORROW);
    }
}
