package com.bytter.basic.service;

import com.bytter.basic.dao.BtInputBankSwiftDao;
import com.bytter.basic.entity.BtInputBankSwift;
import com.bytter.basic.vo.BtInputBankSwiftVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @ClassName BtInputBankSwiftService
 * @Author zx
 * @Description 境内银行Swift Code管理业务类
 * @Date 2026/1/21 13:53
 */
@Service("btInputBankSwiftService")
@Transactional(rollbackFor = Exception.class)
public class BtInputBankSwiftService extends BaseService<BtInputBankSwift, String> {

    @Autowired
    private BtInputBankSwiftDao btInputBankSwiftDao;

    @Override
    public BaseDao<BtInputBankSwift, String> getEntityDao() {
        return btInputBankSwiftDao;
    }

    /**
     * 境内银行Swift Code管理数据查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return btInputBankSwiftDao.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 新增境内银行Swift Code管理数据
     *
     * @param vo
     * @param user
     * @return
     */
    public void saveBtInputBankSwift(BtInputBankSwiftVO vo, ICurrentUser user) {
        //数据校验
        checkData(vo, null);

        BtInputBankSwift entity = new BtInputBankSwift();
        entity.setBankCode(vo.getBankCode());
        entity.setBankName(vo.getBankName());
        entity.setSwiftCode(vo.getSwiftCode());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(new Date());
        this.saveEntity(entity);
    }

    /**
     * 修改境内银行Swift Code管理
     *
     * @param vo
     * @param user
     * @return
     */
    public void modify(BtInputBankSwiftVO vo, ICurrentUser user) {
        //数据校验
        checkData(vo, vo.getId());

        BtInputBankSwift entity = btInputBankSwiftDao.get(vo.getId());
        entity.setBankCode(vo.getBankCode());
        entity.setBankName(vo.getBankName());
        entity.setSwiftCode(vo.getSwiftCode());
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(new Date());
        btInputBankSwiftDao.update(entity);
    }

    /**
     * 数据校验
     *
     * @param vo
     * @return
     */
    public void checkData(BtInputBankSwiftVO vo, String existingId) {
        //开户行行号不能为空
        if (EmptyUtils.isEmpty(vo.getBankCode())) {
            throw new ParameterException(BankInfoMessageEnum.BT_INPUT_BANK_SWIFT_BANKCODE_NULL);
        }

        //SWIFT CODE不能为空
        if (EmptyUtils.isEmpty(vo.getSwiftCode())) {
            throw new ParameterException(BankInfoMessageEnum.BT_INPUT_BANK_SWIFT_SWIFTCODE_NULL);
        }

        //开户行行号是否已存在对应的SWIFT CODE映射
        if (btInputBankSwiftDao.getBtInputBankSwiftByBankCode(vo.getBankCode(), existingId) != null) {
            throw new ParameterException(BankInfoMessageEnum.BT_INPUT_BANK_SWIFT_BANKCODE_EXISTS, vo.getBankCode());
        }
    }

}
