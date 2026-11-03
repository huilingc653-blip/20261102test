package com.bytter.check.service;
import com.bytter.check.constant.FinacialConstant;
import com.bytter.check.dao.FcCompareAccDao;
import com.bytter.check.dto.FcCompareAccDTO;
import com.bytter.check.entity.FcBankCheckDtl;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FC_COMPARE_ACC
 * @date 2017-06-02 17:21:47
 */
@Service("fcCompareAccService")
@Transactional
public class FcCompareAccService extends BaseService<FcCompareAcc, String> {
    @Autowired
    private FcCompareAccDao fcCompareAccDao;
    @Autowired
    private FcBankCheckDtlService fcBankCheckDtlService;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(FcCompareAcc entity) {
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
    public void save(FcCompareAcc entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcCompareAcc entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcCompareAcc entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<FcCompareAcc, String> getEntityDao() {
        return fcCompareAccDao;
    }
    /**
     * <br>
     * <b>功能：</b> 删除对账单数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/6/3 0003 <br>
     */
    public void deleteCompareDtl(String id) {
        //获取对账单明细
        FcCompareAcc entity = get(id, true);
        String compareSign = entity.getCompareSign();
        //已对账或未对账的数据才能删除
        if (FinacialConstant.COMPARE_SIGN_YES.equals(compareSign) || FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)) {
            FcBankCheckDtl bankCheckDtl = new FcBankCheckDtl();
            bankCheckDtl.setErpId(id);
            bankCheckDtl.setBankAcc(entity.getCompAcc());
            bankCheckDtl.setCurCode(entity.getCurId());
            bankCheckDtl.setStatus(CommonConstant.STATUE_DELETE);
            fcBankCheckDtlService.save(bankCheckDtl);
            fcCompareAccDao.deleteCompareAcc(entity);
        }
    }

    public void deleteFcCompareAccByVoucherNoAndCloseDate(List<String> voucherNos, Date closeDate) {
        fcCompareAccDao.deleteFcCompareAccByVoucherNoAndCloseDate(voucherNos,closeDate);
    }

    public List<FcCompareAccDTO> getBalanceByCondition(List<FcCompareAccDTO> fcCompareAccDTOList) {
        return fcCompareAccDao.getBalanceByCondition(fcCompareAccDTOList);
    }

    public List<FcCompareAccDTO> getFcCompareAccDTOBySql(String sql, Map<String, Object> params){
        return fcCompareAccDao.getFcCompareAccDTOBySql( sql, params);
    }
    public void updateCompareAccBalance(Map<String, Object> params) {
        fcCompareAccDao.updateCompareAccBalance( params);
    }
}
