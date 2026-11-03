package com.bytter.credit.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatCreditCooperationBankDao;
import com.bytter.credit.domain.entity.RatCreditCooperationBank;
import com.bytter.credit.domain.vo.RatCreditCooperationBankVO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtBankService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 授信合作银行维护
 *
 * @author fangcf
 * @date 2024-06-20
 */
@Service("ratCreditCooperationBankService")
@Transactional
public class RatCreditCooperationBankService extends BaseService<RatCreditCooperationBank, String> implements IUserExcelVerifyHandler<RatCreditCooperationBankVO> {

    @Autowired
    private RatCreditCooperationBankDao ratCreditCooperationBankDao;

    @DubboReference
    private IBtBankService btBankService;

    @DubboReference
    private IDictService dictService;

    @Override
    public BaseDao<RatCreditCooperationBank, String> getEntityDao() {
        return ratCreditCooperationBankDao;
    }

    /**
     * 查询授信合作银行维护数据
     *
     * @param input 入参对象
     * @return 出参对象
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listOutputData(DataTablesInput input) {
        return ratCreditCooperationBankDao.listOutputData(input);
    }

    /**
     * 新增授信合作银行
     *
     * @param bank 授信合作银行对象
     * @param user 用户对象
     */
    public void saveRatCreditCooperationBank(RatCreditCooperationBank bank, ICurrentUser user) {
        // 同一个授信银行只能有一条记录，首先要根据联行号查询是否存在
        List<RatCreditCooperationBank> resultList = ratCreditCooperationBankDao.listCooperationBankByBankCode(bank.getBankCode());
        if (!CollectionUtils.isEmpty(resultList)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_CREDITCOOPERATIONBANK_EXIST);
        }
        bank.setCreateBy(user.getId());
        bank.setCreateDate(DateUtils.getDate());
        this.saveEntity(bank);
    }

    /**
     * 修改授信合作银行
     *
     * @param bank 授信合作银行对象
     * @param user 用户对象
     */
    public void updateRatCreditCooperationBank(RatCreditCooperationBank bank, ICurrentUser user) {
        RatCreditCooperationBank entity = this.get(bank.getId(), true);
        entity.setCooperationType(bank.getCooperationType());
        entity.setUpdateBy(user.getId());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
    }

    /**
     * 授信合作银行导入
     *
     * @param list 导入数据集合
     * @param user 用户对象
     */
    public void saveImport(List<RatCreditCooperationBank> list, ICurrentUser user) {
        list.forEach(bank -> this.saveRatCreditCooperationBank(bank, user));
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(RatCreditCooperationBankVO obj, Object... user) {
        return null;
    }

    @Override
    public void verifyHandler(List<RatCreditCooperationBankVO> list, Object... user) {
        if (!CollectionUtils.isEmpty(list)) {
            for (RatCreditCooperationBankVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 授信银行名称
                if (StringUtils.isEmpty(vo.getBankName())) {
                    errorMsgList.add(RationConstant.CMS_RAT_CREDITCOOPERATIONBANK_NULL);
                } else {
                    BtBankDTO btBankDTO = btBankService.getByBankName(vo.getBankName());
                    if (EmptyUtils.isEmpty(btBankDTO)) {
                        //授信合作银行不存在
                        errorMsgList.add(RationConstant.CMS_RATBKRATION_BANK_ERROR);
                    } else {
                        //授信合作银行存在时，需判断该联行号合作情况记录是否存在数据中
                        List<RatCreditCooperationBank> resultList = ratCreditCooperationBankDao.listCooperationBankByBankCode(btBankDTO.getBankCode());
                        if (!CollectionUtils.isEmpty(resultList)) {
                            errorMsgList.add(RationConstant.CMS_RAT_CREDITCOOPERATIONBANK_EXIST);
                        } else {
                            vo.setBankCode(btBankDTO.getBankCode());
                        }
                    }
                }
                // 合作情况
                if (StringUtils.isEmpty(vo.getCooperationTypeName())) {
                    errorMsgList.add(RationConstant.CMS_RAT_COOPERATIONTYPE_NULL);
                } else {
                    String cooperationType = dictService.getCodeByName("rat.cooperation.type", vo.getCooperationTypeName());
                    if (StringUtils.isEmpty(cooperationType)) {
                        errorMsgList.add(RationConstant.CMS_RAT_COOPERATIONTYPE_ERROR);
                    } else {
                        vo.setCooperationType(cooperationType);
                    }
                }
                vo.addErrorMsg(errorMsgList);
            }
        }
    }
}
