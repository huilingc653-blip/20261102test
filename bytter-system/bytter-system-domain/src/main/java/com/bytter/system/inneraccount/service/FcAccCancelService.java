package com.bytter.system.inneraccount.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.vo.FcAccVO;
import com.bytter.system.constants.InnerAccountConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.enums.InnerAccountMessageEnum;
import com.bytter.system.inneraccount.dao.FcAccDao;
import com.bytter.system.inneraccount.entity.FcAcc;
import com.bytter.system.inneraccount.entity.FcAccLog;
import com.bytter.system.service.CorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户信息表
 * @date 2017-05-25 19:36:12
 */
@Service("fcAccCancelService")
@Transactional
public class FcAccCancelService extends BaseService<FcAcc, String> {
    @Autowired
    private FcAccDao fcAccDao;
    @Autowired
    private CorpService corpService;
    @Autowired
    private FcAccLogService fcAccLogService;
    @Autowired
    private FcAccService fcAccService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FcAcc entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcAcc entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcAcc entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FcAcc entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<FcAcc, String> getEntityDao() {
        return fcAccDao;
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELQUERY, userVO.getId()), "t.corp_Id", params);
        params.put("loginName", userVO.getLoginName());
        //是否注销复核
        String isCheck = (String) dataTablesInput.getFormdata().get("isCheck");
        if ("1".equals(isCheck)) {
            sql.append(" where t.acc_state=:accState and t.Operator_Code <> :loginName");
            params.put("accState", InnerAccountConstant.ACC_STATE_REQUISITION);
        } else {//查找可以注销的账户，正常状态账户和注销暂存，注销打回
            sql.append(" where (t.acc_state=:accStateN or (t.acc_state=:accStateZ and t.Operator_Code =:loginName )");
            sql.append(" or (t.acc_state=:accStateU and t.Operator_Code =:loginName))");
            params.put("accStateN", InnerAccountConstant.ACC_STATE_NORMAL);
            params.put("accStateZ", InnerAccountConstant.ACC_STATE_Z);
            params.put("accStateU", InnerAccountConstant.ACC_STATE_U);
        }
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = fcAccDao.getDataTablesUnion(dataTablesInput, sql.toString(), params);
        return output;
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String sqlWhere, Map<String, Object> params) {
        DataTablesOutput output = fcAccDao.getDataTablesUnionForPool(dataTablesInput, sqlWhere, params);
        return output;
    }
    /**
     * 校验账户类型为内部账号的账户销户是否有关联关系
     *
     * @param fcAcc
     */
    public void isExsitsByExpPay(FcAcc fcAcc) {
        if (!EmptyUtils.isEmpty(fcAcc) && !EmptyUtils.isEmpty(fcAcc.getAccType())) {
            if (InnerAccountConstant.FCACC_INNER_ACCOUNT.equals(fcAcc.getAccType())) {
                /** 账号{0}有关联的计息账号存在，不能销户！*/
                if (fcAccService.isExsitsByExpPay(fcAcc.getBlnctrAcc())) {
                    throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_EXIST_EXPPAY, fcAcc.getBlnctrAcc());
                }
            }
        }
    }
    /**
     * 是否活期
     *
     * @param accTypeCode
     * @return
     */
    public boolean isCurrType(String accTypeCode) {
        return accTypeCode.equals(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
    }
    /**
     * 获取FcAccVO
     *
     * @param account
     * @return
     */
    public FcAccVO getFcAccVOByBlnctrAcc(String account) {
        return fcAccService.getFcAccVOByBlnctrAcc(account);
    }
    /**
     * 注销转出账号,单位企业活期账号，不包含自己的活期账号
     *
     * @param blnctrAcc
     * @param netId
     * @return
     */
    public List<FcAccVO> transferOutAcc(String blnctrAcc, String netId) {
        return fcAccService.transferOutAcc(blnctrAcc, netId);
    }
    /**
     * <b>功能介绍：</b>获取借方记账账号<br>
     * <b>日期：</b>2020年6月16日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public String getDebitAccout(FcAccVO fcAccDTO, boolean interestSign) {
        if (interestSign) {//计息
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())) {//企业活期
                return fcAccDTO.getInterestInExp();//借利息支出
            } else if (InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType())) {//放款
                return fcAccDTO.getAccForDeduct();//借企业活期
            }
        } else {
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_BORROW.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(fcAccDTO.getAccType())) {//企业活期、银行借款、企业定期、企业通知
                return fcAccDTO.getInterestInExp();//借利息支出
            } else if (InnerAccountConstant.FCACC_BANK_DEMAND.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_BANK_BEGULAR.equals(fcAccDTO.getAccType())) {//银行活期、企业放款、银行定期
                return fcAccDTO.getInterestRecPay();//借应收利息户
            }
        }
        return null;
    }
    /**
     * <b>功能介绍：</b>获取贷方记账账号<br>
     * <b>日期：</b>2020年6月16日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public String getCreditAccout(FcAccVO fcAccDTO, boolean interestSign) {
        if (interestSign) {//计息
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())) {//企业活期
                return fcAccDTO.getBlnctrAcc();//贷企业活期
            } else if (InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType())) {//放款
                return fcAccDTO.getInterestInExp();//贷利息收入
            }
        } else {
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_BORROW.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(fcAccDTO.getAccType())) {//企业活期、银行借款、企业定期、企业通知
                return fcAccDTO.getInterestRecPay();//贷应付利息
            } else if (InnerAccountConstant.FCACC_BANK_DEMAND.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_BANK_BEGULAR.equals(fcAccDTO.getAccType())) {//银行活期、企业放款、银行定期
                return fcAccDTO.getInterestInExp();//贷利息收入
            }
        }
        return null;
    }
    /**
     * 保存销户日志
     *
     * @param acc
     * @param opType 　操作类型
     * @param bizMsg 　业务信息
     */
    private void saveLog(FcAcc acc, String opType, String bizMsg, String loginName) {
        FcAccLog fcAccLog = new FcAccLog();
        fcAccLog.setBizId(acc.getId());
        fcAccLog.setOpType(opType);
        fcAccLog.setAccType(acc.getAccType());
        fcAccLog.setBizMsg(bizMsg);
        fcAccLog.setAccount(acc.getBlnctrAcc());
        fcAccLog.setBizType(InnerAccountConstant.BIZ_TYPE_CANCEL);
        fcAccLogService.saveLog(fcAccLog, loginName);
    }
}
