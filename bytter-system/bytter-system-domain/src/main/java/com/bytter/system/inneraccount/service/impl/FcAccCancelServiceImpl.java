package com.bytter.system.inneraccount.service.impl;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IFcAccCancelService;
import com.bytter.system.api.vo.FcAccVO;
import com.bytter.system.constants.InnerAccountConstant;
import com.bytter.system.enums.InnerAccountMessageEnum;
import com.bytter.system.inneraccount.service.FcAccCancelService;
import com.bytter.system.inneraccount.service.FcAccLogService;
import com.bytter.system.inneraccount.service.FcAccService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户信息表
 * @date 2017-05-25 19:36:12
 */
@Service("fcAccCancelServiceI")
@DubboService
public class FcAccCancelServiceImpl implements IFcAccCancelService {
    @Autowired
    private FcAccCancelService fcAccCancelService;
    @Autowired
    private FcAccService fcAccService;
    @Autowired
    private FcAccLogService fcAccLogService;
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        fcAccCancelService.delete(id);
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO) {
        DataTablesOutput output = fcAccCancelService.getDataTablesUnion(dataTablesInput, userVO);
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
        DataTablesOutput output = fcAccCancelService.getDataTablesUnionForPool(dataTablesInput, sqlWhere, params);
        return output;
    }
    /**
     * 校验账户类型为内部账号的账户销户是否有关联关系
     *
     * @param fcAcc
     */
    public void isExsitsByExpPay(FcAccVO fcAcc) {
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
}
