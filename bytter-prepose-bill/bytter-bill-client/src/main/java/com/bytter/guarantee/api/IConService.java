package com.bytter.guarantee.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
/**
 * 合同管理公用接口
 * 
 * @author zhengg
 */
public interface IConService {
    /**
     * 获取合同信息 合同ID和业务ID参数传递二选一即可
     * 
     * @param conId
     *            合同ID
     * @param busId
     *            业务ID
     */
    ConInfoDTO getConInfo(String conId, String busId);
    /**
     * 业务生成合同登记操作 corpId 合同发起人单位ID 必填为空提示异常 busId 挂接业务ID 必填为空提示异常 conName 合同名称 必填为空提示异常 conNO 合同编号 可以为空 typeId 合同类型ID
     * 必填为空提示异常 beginDate 合同生效日期 必填为空提示异常 endDate 合同失效日期 可以为空 outBusData 外部业务挂接数据 必填为空或者匹配规则必填项空提示异常
     * 
     * @param conInfoDTO
     *            合同信息
     * @param conPdType
     *            产品业务类型： 内部贷款 CON_PD_INNERLOAN 委托贷款CON_PD_ENTRUSTLOAN 银行借款 CON_PD_BANKBORROW 授信管理 CON_PD_RAT 担保管理
     *            CON_PD_GUA return conInfoDTO 业务完成以后会回写合同ID到DTO中
     */
    void registerConOp(ConInfoDTO conInfoDTO, String conPdType, ICurrentUser user);
    /**
     * 业务生成合同登记操作 内部贷款 CON_PD_INNERLOAN corpId 合同发起人单位ID 必填为空提示异常 busId 挂接业务ID 必填为空提示异常 conName 合同名称 必填为空提示异常 conNO 合同编号
     * 可以为空 typeId 合同类型ID 必填为空提示异常 beginDate 合同生效日期 必填为空提示异常 endDate 合同失效日期 可以为空 outBusData 外部业务挂接数据 必填为空或者匹配规则必填项空提示异常
     * 
     * @param conInfoDTO
     *            合同信息 return conInfoDTO 业务完成以后会回写合同ID到DTO中
     */
    String registerConOpInnerLoan(ConInfoDTO conInfoDTO, ICurrentUser user);
    /**
     * 业务生成合同登记操作 委托贷款CON_PD_ENTRUSTLOAN corpId 合同发起人单位ID 必填为空提示异常 busId 挂接业务ID 必填为空提示异常 conName 合同名称 必填为空提示异常 conNO
     * 合同编号 可以为空 typeId 合同类型ID 必填为空提示异常 beginDate 合同生效日期 必填为空提示异常 endDate 合同失效日期 可以为空 outBusData 外部业务挂接数据
     * 必填为空或者匹配规则必填项空提示异常
     * 
     * @param conInfoDTO
     *            合同信息 return conInfoDTO 业务完成以后会回写合同ID到DTO中
     */
    String registerConOpEntrustLoan(ConInfoDTO conInfoDTO, ICurrentUser user);
    /**
     * 业务生成合同登记操作 银行借款 CON_PD_BANKBORROW corpId 合同发起人单位ID 必填为空提示异常 busId 挂接业务ID 必填为空提示异常 conName 合同名称 必填为空提示异常 conNO
     * 合同编号 可以为空 typeId 合同类型ID 必填为空提示异常 beginDate 合同生效日期 必填为空提示异常 endDate 合同失效日期 可以为空 outBusData 外部业务挂接数据
     * 必填为空或者匹配规则必填项空提示异常
     * 
     * @param conInfoDTO
     *            合同信息 return conInfoDTO 业务完成以后会回写合同ID到DTO中
     */
    String registerConOpBankBorrow(ConInfoDTO conInfoDTO, ICurrentUser user);
    /**
     * 业务生成合同登记操作 授信管理获取合同类型 CON_PD_RAT corpId 合同发起人单位ID 必填为空提示异常 busId 挂接业务ID 必填为空提示异常 conName 合同名称 必填为空提示异常 conNO 合同编号
     * 可以为空 typeId 合同类型ID 必填为空提示异常 beginDate 合同生效日期 必填为空提示异常 endDate 合同失效日期 可以为空 outBusData 外部业务挂接数据 必填为空或者匹配规则必填项空提示异常
     * 
     * @param conInfoDTO
     *            合同信息 return conInfoDTO 业务完成以后会回写合同ID到DTO中
     */
    void registerConOpRat(ConInfoDTO conInfoDTO, ICurrentUser user);
    /**
     * 业务生成合同登记操作 担保管理理获取合同类型 CON_PD_GUA corpId 合同发起人单位ID 必填为空提示异常 busId 挂接业务ID 必填为空提示异常 conName 合同名称 必填为空提示异常 conNO
     * 合同编号 可以为空 typeId 合同类型ID 必填为空提示异常 beginDate 合同生效日期 必填为空提示异常 endDate 合同失效日期 可以为空 outBusData 外部业务挂接数据
     * 必填为空或者匹配规则必填项空提示异常
     * 
     * @param conInfoDTO
     *            合同信息 return conInfoDTO 业务完成以后会回写合同ID到DTO中
     */
    void registerConOpGua(ConInfoDTO conInfoDTO, ICurrentUser user);
    /**
     * 业务生成合同登记操作 对外担保管理理获取合同类型 CON_PD_EXTGUA corpId 合同发起人单位ID 必填为空提示异常 busId 挂接业务ID 必填为空提示异常 conName 合同名称 必填为空提示异常
     * conNO 合同编号 可以为空 typeId 合同类型ID 必填为空提示异常 beginDate 合同生效日期 必填为空提示异常 endDate 合同失效日期 可以为空 outBusData 外部业务挂接数据
     * 必填为空或者匹配规则必填项空提示异常 return conInfoDTO 业务完成以后会回写合同ID到DTO中
     */
    void registerConOpExtGua(ConInfoDTO conInfoDTO, ICurrentUser user);
    /**
     * 业务进行合同挂接操作 一个合同只能被一个业务挂接，否则直接抛异常提示！
     * 
     * @param conId
     *            合同ID
     * @param busId
     *            业务ID
     */
    void hookConOp(String conId, String busId, ICurrentUser user);
    /**
     * 业务取消合同挂接操作 不存在挂接不做任何操作，进行取消合同挂接功能！ 合同ID和业务ID参数传递二选一即可
     * 
     * @param conId
     *            合同ID
     * @param busId
     *            业务ID
     */
    void unHookConOp(String conId, String busId, ICurrentUser user);
    /**
     * 进行合同履约操作 conId 合同ID 必填为空提示异常 outerId 履约业务挂接ID 必填为空提示异常 opMsg 履约原因 必填为空提示异常 opDate 履约时间 必填为空提示异常 opNo 履约申请编号 选填
     * opMoney 履约金额 选填
     * 
     * @param recordDTO
     *            挂接信息参数 return recordDTO对象中会把履约流水ID回写
     */
    void performanceConOp(ConInfoRecordDTO recordDTO, ICurrentUser user);
    /**
     * 进行合同终止操作 conId 合同ID 必填为空提示异常 outerId 终止业务挂接ID 必填为空提示异常 opMsg 终止原因 必填为空提示异常 opDate 终止时间 必填为空提示异常 opNo 终止申请编号 选填
     * 
     * @param recordDTO
     *            挂接信息参数 return recordDTO对象中会把终止流水ID回写
     */
    void terminateConOp(ConInfoRecordDTO recordDTO, ICurrentUser user);
}
