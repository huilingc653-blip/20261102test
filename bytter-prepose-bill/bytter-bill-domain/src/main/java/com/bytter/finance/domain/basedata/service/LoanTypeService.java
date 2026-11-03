package com.bytter.finance.domain.basedata.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.basedata.dao.LoanTypeDao;
import com.bytter.finance.domain.basedata.entity.LoanType;
import com.bytter.finance.dto.LoanTypeDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.BorrowingMessageEnum;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * @Title: 贷款类型维护
 * @Description: CMS_LOAN_TYPE
 * @author zhengxn
 * @date 2017-07-19 14:05:55
 * @version 1.0
 *
 */
@Service("loanTypeService")
@Transactional
public class LoanTypeService extends BaseService<LoanType, String> {
    @Autowired
    private LoanTypeDao loanTypeDao;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    /** 查询贷款类型 ***/
    @Transactional(readOnly = true)
    public DataTablesOutput queryLoanType(DataTablesInput input) {
        return loanTypeDao.queryLoanType(input);
    }
    /** 修改贷款类型 ***/
    public int updateLoan(String id, String code, String name, String loginname, String username, String date,
            String type,String cmsLoanTypeManagerId) {
        int res = 0;
        boolean isOk = judgeTypeByCode(code, name, type, id);
        if (isOk) {
            LoanType loanType = loanTypeDao.get(id);
            loanType.setCode(code);
            loanType.setName(name);
            loanType.setUpdateName(username);
            loanType.setUpdateBy(loginname);
            loanType.setUpdateDate(new Date());
            loanType.setCmsLoanTypeManagerId(cmsLoanTypeManagerId);
            loanTypeDao.update(loanType);
            // res = loanTypeDao.updateLoan(id, code, name, loginname, username, date,type);
        } else {
            throw new ParameterException(BorrowingMessageEnum.FIN_BORROWING_TYPE_EXIST);
        }
        return res;
    }
    /** 删除贷款类型 **/
    public int deleteLoan(String id) {
        // 删除多语言
        ii18nService.deleteBusinessBaseContext(id);
        return loanTypeDao.deleteLoan(id);
    }
    /** 查询删除类型编码是否存在 **/
    @Transactional(readOnly = true)
    public boolean judgeLoanTypeByCode(String code, String type) {
        return loanTypeDao.judgeLoanTypeByCode(code, type);
    }
    /** 查询类型编码是否存在 **/
    @Transactional(readOnly = true)
    public boolean judgeTypeByCode(String code, String name, String type, String id) {
        return loanTypeDao.judgeTypeByCode(code, name, type, id);
    }
    public void save(LoanType entity) {
        super.saveEntity(entity);
    }
    /** 新增贷款类型 **/
    public void saveLoanType(LoanType borrowingType, ICurrentUser user) {
        verify(borrowingType);
        String code = borrowingType.getCode();
        String name = borrowingType.getName();
        boolean flag = judgeTypeByCode(code, name, borrowingType.getType(), null);
        if (flag) {
            LoanType lt = new LoanType();
            lt.setCode(code);
            lt.setName(name);
            lt.setType(borrowingType.getType());
            lt.setStatus(borrowingType.getStatus());
            lt.setCreateName(user.getUsername());
            lt.setCreateBy(user.getLoginName());
            lt.setCreateDate(new Date());
            lt.setCmsLoanTypeManagerId(borrowingType.getCmsLoanTypeManagerId());
            save(lt);
            this.getEntityDao().flush();
            // 新增当前语种
            I18nDTO i18nDTO = new I18nDTO();
            i18nDTO.setKeyId(lt.getId());
            i18nDTO.setLocale(LocaleContextHolder.getLocale().toString());
            i18nDTO.setContext(lt.getName());
            ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
        } else {
            throw new ParameterException(BorrowingMessageEnum.FIN_BORROWING_TYPE_EXIST);
        }
    }
    /** 查询借款类型 ***/
    @Transactional(readOnly = true)
    public DataTablesOutput queryBorrowType(DataTablesInput input) {
        return loanTypeDao.queryBorrowType(input);
    }
    /**
     * 功能说明：根据类型code查询借款/贷款类型
     * 
     * @param code
     * @param type
     *            借款：BaseDataConstant.TYPE_BORROW 贷款：BaseDataConstant.TYPE_CREDIT
     */
    @Transactional(readOnly = true)
    public LoanTypeDTO queryLoanTypeByCode(String code, String type) {
        return loanTypeDao.queryLoanTypeByCode(code, type);
    }
    /**
     * 功能说明：根据id、类型查询借款/贷款类型
     * 
     * @param id
     * @param type
     *            借款：BaseDataConstant.TYPE_BORROW 贷款：BaseDataConstant.TYPE_CREDIT
     */
    @Transactional(readOnly = true)
    public LoanTypeDTO queryLoanTypeById(String id, String type) {
        return loanTypeDao.queryLoanTypeById(id, type);
    }
    /**
     * 功能说明：根据类型查询借款/贷款类型
     * 
     * @param type
     *            借款：BaseDataConstant.TYPE_BORROW 贷款：BaseDataConstant.TYPE_CREDIT
     */
    @Transactional(readOnly = true)
    public List<LoanTypeDTO> queryLoanTypeList(String type) {
        return loanTypeDao.queryLoanTypeList(type);
    }
    /**
     * 功能介绍 类型维护输入验证。<br>
     * <b>日期：</b>2017年8月11日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public void verify(LoanType entity) {
        if (EmptyUtils.isEmpty(entity.getCode())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANCODEEMPTY);
        }
        if (entity.getCode().length() > 4) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANCODELENGTH);
        }
        if (EmptyUtils.isEmpty(entity.getName())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANNAMEEMPTY);
        }
        if (entity.getName().length() > 10) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANNAMELENGTH);
        }
    }
    public BaseDao<LoanType, String> getEntityDao() {
        return loanTypeDao;
    }

    /**
     * 借款类型维护选择系统借款类型数据查询
     *
     * @param type
     * @return
     */
    @Transactional(readOnly = true)
    public List<LoanTypeDTO> queryCmsLoanTypeManagerList(String type) {
        return loanTypeDao.queryCmsLoanTypeManagerList(type);
    }

}
