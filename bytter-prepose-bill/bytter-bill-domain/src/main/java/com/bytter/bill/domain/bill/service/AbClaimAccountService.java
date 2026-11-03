package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbClaimAccountDao;
import com.bytter.bill.domain.bill.entity.AbClaimAccount;
import com.bytter.bill.domain.bill.vo.AbClaimAccountVo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
/**
 * 票据认领账号规则配置服务类
 *
 * @author mof
 */
@Service("abClaimAccountService")
@Transactional
public class AbClaimAccountService extends BaseService<AbClaimAccount, String> {
    @Autowired
    private AbClaimAccountDao abClaimAccountDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(AbClaimAccount entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(AbClaimAccount entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(AbClaimAccount entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(AbClaimAccount entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<AbClaimAccount, String> getEntityDao() {
        return abClaimAccountDao;
    }
    /**
     * 列表查询显示
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbClaimAccountInfo(DataTablesInput dataTablesInput) {
        return abClaimAccountDao.listAbClaimAccountInfo(dataTablesInput);
    }
    /**
     * 根据ID获取账号规则信息
     *
     * @param id
     * @return
     */
    public AbClaimAccountVo getByIdUnion(String id) {
        return abClaimAccountDao.getByIdUnion(id);
    }
    /**
     * @param id
     */
    public void deleteAbClaimAccount(String id) {
        //有效标志为无效的账号范围规则允许删除，有效标志为有效的账号范围规则不允许删除
        AbClaimAccountVo abClaimAccount = this.getByIdUnion(id);
        if (AccebillConstant.AB_BILL_VALID_SIGN_1.equals(abClaimAccount.getValidSign())) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIMACCOUNT_VALIDSIGN);
        }
        abClaimAccountDao.deleteAbClaimAccount(id);
    }
    /**
     * 检验单位+账号+业务类型不允许重复，当重复时系统弹出提示对话框，提示文字为“单位+账号+业务类型不允许重复”
     *
     * @param abClaimAccountVo
     */
    public boolean checkAddRepeat(AbClaimAccountVo abClaimAccountVo) {
        List<AbClaimAccountVo> list = abClaimAccountDao.getByRepeat(abClaimAccountVo);
        if (list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 根据单位和业务类型查询有效账号规则
     *
     * @param params 参数map
     * @return 有效账号规则集合
     */
    public List<AbClaimAccountVo> listAbClaimAccountInfo(Map<String, Object> params) {
        return abClaimAccountDao.listAbClaimAccountInfo(params);
    }
}
