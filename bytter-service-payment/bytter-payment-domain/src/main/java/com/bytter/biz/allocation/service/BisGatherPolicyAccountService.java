package com.bytter.biz.allocation.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.dao.BisGatherPolicyAccountDao;
import com.bytter.biz.allocation.entity.BisGatherPolicyAccount;
import com.bytter.biz.allocation.vo.BisGatherPolicyAccountVO;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 资金中心调拨账户配置
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/11
 */
@Service("bisGatherPolicyAccountService")
@Transactional
public class BisGatherPolicyAccountService extends BaseService<BisGatherPolicyAccount, String> {
    @Autowired
    private BisGatherPolicyAccountDao bisGatherPolicyAccountDao;

    @Override
    public BaseDao<BisGatherPolicyAccount, String> getEntityDao() {
        return bisGatherPolicyAccountDao;
    }

    /***
     * 查询列表数据
     * @param dataTablesInput
     * @return TODO
     */
    public DataTablesOutput<BisGatherPolicyAccountVO> getAccountDatatable(DataTablesInput dataTablesInput) {
        return bisGatherPolicyAccountDao.getAccountDatatable(dataTablesInput);
    }

    /***
     * 校验账号是否存在
     * @param propertyName
     * @param propertyValue
     * @param id
     * @return TODO
     */
    public boolean checkExist(String propertyName,Object propertyValue,String id) {
        Boolean flag = false;
        List<BisGatherPolicyAccount> list = bisGatherPolicyAccountDao.getUniqueByProperty(propertyName, propertyValue,id);
        if (ObjectUtil.isNotEmpty(list)){
            flag = true;
        }
        return flag;
    }

    /***
     * 保存数据
     * @param policyAccountVO
     * @param currentUser
     * @return TODO
     */
    public void save(BisGatherPolicyAccountVO policyAccountVO, ICurrentUser currentUser) {
        BisGatherPolicyAccount policyAccount = new BisGatherPolicyAccount();
        BeanUtils.copyNotNullProperties(policyAccountVO,policyAccount);
        policyAccount.setCreateBy(currentUser.getLoginName());
        policyAccount.setCreateDate(new Date());
        bisGatherPolicyAccountDao.save(policyAccount);
    }

    public void modifyAccount(BisGatherPolicyAccountVO policyAccountVO, ICurrentUser currentUser) {
        BisGatherPolicyAccount policyAccount = bisGatherPolicyAccountDao.get(policyAccountVO.getId());
        if (ObjectUtil.isNotEmpty(policyAccount)){
            policyAccount.setCorpName(policyAccountVO.getCorpName());
            policyAccount.setBankAcc(policyAccountVO.getBankAcc());
            policyAccount.setBankName(policyAccountVO.getBankName());
            policyAccount.setPriority(policyAccountVO.getPriority());
            policyAccount.setIsDown(policyAccountVO.getIsDown());
            policyAccount.setStatus(policyAccountVO.getStatus());
            policyAccount.setUpdateBy(currentUser.getLoginName());
            policyAccount.setUpdateDate(new Date());
            bisGatherPolicyAccountDao.update(policyAccount);
        }
    }

    public BisGatherPolicyAccountVO getByBankAcc(String bankAcc) {
        BisGatherPolicyAccountVO vo = null;;
        List<BisGatherPolicyAccount> list = bisGatherPolicyAccountDao.listBy("bankAcc", bankAcc);
        if (ObjectUtil.isNotEmpty(list)){
            BisGatherPolicyAccount policyAccount = list.get(0);
            vo = new BisGatherPolicyAccountVO();
            BeanUtils.copyNotNullProperties(policyAccount,vo);
        }
        return vo;
    }

    /**
     * 资金中心配置的调拨账户
     */
    public List<BisGatherPolicyAccountVO> getAllIsDown() {
        List<BisGatherPolicyAccountVO> vos = new ArrayList<>();
        List<BisGatherPolicyAccount> list = bisGatherPolicyAccountDao.getAllIsDown();
        if (ObjectUtil.isNotEmpty(list)){
            for (BisGatherPolicyAccount policyAccount : list) {
                BisGatherPolicyAccountVO vo = new BisGatherPolicyAccountVO();
                BeanUtils.copyNotNullProperties(policyAccount,vo);
                vos.add(vo);
            }
        }
        return vos;
    }
}
