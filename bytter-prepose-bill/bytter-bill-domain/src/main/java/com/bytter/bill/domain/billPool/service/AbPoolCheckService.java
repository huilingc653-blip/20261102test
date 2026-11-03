package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.billPool.dao.AbPoolCheckDao;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 业务检查Service
 * @Description: 业务检查处理类（检查池接口、票据池、池单位、质权人等是否有在途业务数据）
 * @date 2020-2-24
 */
@Service("abPoolCheckService")
@Transactional
public class AbPoolCheckService extends BaseService<AbPool, String> {
    @Autowired
    private AbPoolCheckDao abPoolCheckDao;
    public BaseDao<AbPool, String> getEntityDao() {
        return null;
    }
    /**
     * 池接口判断是否有在途业务数据
     *
     * @param fid 池接口id
     * @return
     */
    public boolean poolIfCheckQuery(String fid) {
        if (StringUtils.isEmpty(fid)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        boolean resulet = true;
        resulet = this.checkQuery(AbPoolConstant.AB_POOL_CHECK_QUERYTYPE_1, null, fid);
        return resulet;
    }
    /**
     * 票据池判断是否有在途业务数据
     *
     * @param pid 票据池id
     * @return
     */
    public boolean poolCheckQuery(String pid) {
        if (StringUtils.isEmpty(pid)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        boolean resulet = true;
        resulet = this.checkQuery(AbPoolConstant.AB_POOL_CHECK_QUERYTYPE_2, "BP_ID", pid);
        return resulet;
    }
    /**
     * 池单位判断是否有在途业务数据
     *
     * @param corpId 池单位corpId
     * @return
     */
    public boolean poolCorpCheckQuery(String corpId) {
        if (StringUtils.isEmpty(corpId)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        boolean resulet = true;
        resulet = this.checkQuery(AbPoolConstant.AB_POOL_CHECK_QUERYTYPE_3, "CORP_ID", corpId);
        return resulet;
    }
    /**
     * 质权人判断是否有在途业务数据
     *
     * @param pId 质权人Id
     * @return
     */
    public boolean poolPledgeeCheckQuery(String pId) {
        if (StringUtils.isEmpty(pId)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        boolean resulet = true;
        resulet = this.checkQuery(AbPoolConstant.AB_POOL_CHECK_QUERYTYPE_4, null, pId);
        return resulet;
    }
    /**
     * 根据id查询各业务是否存在在途数据
     *
     * @param type       查询类型 1池接口 2票据池 3池单位  4质权人
     * @param queryField 查询字段
     * @param id         查询id
     * @return
     */
    public boolean checkQuery(Integer type, String queryField, String id) {
        boolean resulet = true;
        /*
          业务判断
        */
        //质权人判断
        if (AbPoolConstant.AB_POOL_CHECK_QUERYTYPE_4 == type) {
            //入池申请
            //审批
            if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8018)) {
                throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_INTOAPPLY_APPROVE);
            }
            //指令
            if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8018)) {
                throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_INTOAPPLY_INSTRUCT);
            }
            //电票签收（选签收入池）
            //审批
            if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0,AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_3,null)) {
                throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_SIGN_APPROVE);
            }
            //指令
            if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1,AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_3,null)) {
                throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_SIGN_INSTRUCT);
            }
            return resulet;
        }
        //额度申领
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_LIMIT_APPLY", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_LIMITAPPLY_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_LIMIT_APPLY", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_LIMITAPPLY_INSTRUCT);
        }
        //额度申领核准
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_LIMIT_APPLYCHECK", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_LIMITAPPLYCHECK_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_LIMIT_APPLYCHECK", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_LIMITAPPLYCHECK_INSTRUCT);
        }
        //额度分配
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_LIMIT_ALLOT", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_LIMITALLOT_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_LIMIT_ALLOT", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_LIMITALLOT_INSTRUCT);
        }
        //额度分配核准
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_LIMIT_ALLOTCHECK", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_LIMITALLOTCHECK_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_LIMIT_ALLOTCHECK", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_1,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_LIMITALLOTCHECK_INSTRUCT);
        }
        //保证金存入
        //审批
        if (abPoolCheckDao.checkBailQuery(type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_IN)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_BAILIN_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkBailQuery(type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_IN)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_BAILIN_INSTRUCT);
        }
        //保证金提取
        //审批
        if (abPoolCheckDao.checkBailQuery(type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_OUT)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_BAILOUT_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkBailQuery(type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_OUT)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_BAILOUT_INSTRUCT);
        }
        //入池申请
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8018)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_INTOAPPLY_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8018)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_INTOAPPLY_INSTRUCT);
        }
        //出池申请
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8019)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_OUTAPPLY_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8019)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_OUTAPPLY_INSTRUCT);
        }
        //质押转托管
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8118)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_PDTST1_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8118)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_PDTST1_INSTRUCT);
        }
        //托管转质押
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8218)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_PDTST2_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_8218)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_PDTST2_INSTRUCT);
        }
        //提示付款
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_020)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_COLLECTIONAPPLY_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_2,AbPoolConstant.AB_POOL_BRIEF_020)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_COLLECTIONAPPLY_INSTRUCT);
        }
        //出票申请
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_5,AbPoolConstant.AB_POOL_BRIEF_001)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_EMITAPPLY_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_5,AbPoolConstant.AB_POOL_BRIEF_001)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_EMITAPPLY_INSTRUCT);
        }
        //背书申请
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_4,AbPoolConstant.AB_POOL_BRIEF_010)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_ENDORSE_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_4,AbPoolConstant.AB_POOL_BRIEF_010)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_ENDORSE_INSTRUCT);
        }
        //贴现申请
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_4,AbPoolConstant.AB_POOL_BRIEF_011)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_DISCOUNT_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_4,AbPoolConstant.AB_POOL_BRIEF_011)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_DISCOUNT_INSTRUCT);
        }
        //融资申请管理
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_6,AbPoolConstant.AB_POOL_BRIEF_8001)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_FINAAPPLY_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1, AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_6,AbPoolConstant.AB_POOL_BRIEF_8001)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_FINAAPPLY_INSTRUCT);
        }
        //电票签收（选签收入池）
        //审批
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_0,AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_3,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_SIGN_APPROVE);
        }
        //指令
        if (abPoolCheckDao.checkQuery("AB_POOL_DTL", type, queryField, id, AbPoolConstant.AB_POOL_CHECK_TYPE_1,AbPoolConstant.AB_POOL_CHECK_BUSINESSTYPE_3,null)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_CHECK_MESSAGE_SIGN_INSTRUCT);
        }
        return resulet;
    }
}
