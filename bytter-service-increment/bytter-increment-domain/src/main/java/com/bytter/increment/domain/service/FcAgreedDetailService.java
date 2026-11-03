package com.bytter.increment.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.dto.FcAgreeDepositDetailDTO;
import com.bytter.increment.domain.dao.FcAgreedDetailDao;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: 协定存款登记明细表数据类
 * @date 2019-05-10 16:44:38
 */
@Service("fcAgreedDetailService")
@Transactional
public class FcAgreedDetailService extends BaseService<FcAgreedDetail,String> {
    @Autowired
    private FcAgreedDetailDao fcAgreedDetailDao;
    @Override
    public BaseDao<FcAgreedDetail, String> getEntityDao() {
        return fcAgreedDetailDao;
    }
    /**
     * 根据协定主表id查询明细
     * @param agreedId
     * @return
     */
    public List<FcAgreedDetail> getDetailList(String agreedId){
        return fcAgreedDetailDao.getDetailByAgreedIdLineNo(agreedId,null);
    }
    /**
     * 根据协定主表id和行号查询明细
     * @param agreedId 协定主表id
     * @param lineNo 行号
     * @return
     */
    public FcAgreedDetail getDetailByAgreedIdLineNo(String agreedId,Integer lineNo){
        List<FcAgreedDetail> details = fcAgreedDetailDao.getDetailByAgreedIdLineNo(agreedId,lineNo);
        FcAgreedDetail detail = null;
        if(!EmptyUtils.isEmptyList(details)){
            detail = details.get(0);
        }
        return detail;
    }
    /**
     * <br>
     * <b>功能：</b> 获取账户协定明细 <br>
     * <b>作者：</b>liyh<br>
     * @param account 账号
     * @param balanceDate 余额时间
     * @param isAdjust 是否调整 true 是 false 否
     * <b>日期：</b> 2019/5/14 <br>
     */
    public List<FcAgreeDepositDetailDTO> getFcAgreeDepositDetail(String account, Date balanceDate, boolean isAdjust) {
        return fcAgreedDetailDao.getFcAgreeDepositDetail(account, balanceDate, isAdjust);
    }
}
