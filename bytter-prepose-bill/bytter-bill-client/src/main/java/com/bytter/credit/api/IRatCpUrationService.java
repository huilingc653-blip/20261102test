package com.bytter.credit.api;
import com.bytter.credit.dto.RatUrationDTO;
/**
 * 企业授信对外接口
 * 
 * @author luxy
 * @version 1.0
 */
public interface IRatCpUrationService {
    /**
     * 暂存授信，写入授信占用表，状态为暂存
     * 
     * @param urationDTO
     *            授信占用DTO
     */
    void tempUseCpRation(RatUrationDTO urationDTO);
    /**
     * 添加占用授信，冻结金额
     * 
     * @param urationDTO
     *            授信占用DTO
     */
    void addUseCpRation(RatUrationDTO urationDTO);
    /**
     * 删除操作，授信状态为释放(需传入授信占用ID，或业务ID和业务类型)
     * 
     * @param urationDTO
     *            授信占用DTO
     */
    void deleteUseCpRation(RatUrationDTO urationDTO);
    /**
     * 授信正式占用，授信状态为占用(需传入授信占用ID，或业务ID和业务类型)
     * 
     * @param urationDTO
     *            授信占用DTO
     */
    void passUseCpRation(RatUrationDTO urationDTO);
    /**
     * 授信占用打回，冻结额度还原(需传入授信占用ID，或业务ID和业务类型)
     * 
     * @param urationDTO
     *            授信占用DTO
     */
    void backUseCpRation(RatUrationDTO urationDTO);
    /**
     * 授信占用释放，授信状态为已释放(需传入授信占用ID，或业务ID和业务类型)
     * 
     * @param urationDTO
     *            授信占用DTO
     */
    void releaseRation(RatUrationDTO urationDTO);
    /**
     * 授信占用后打回(审批通过后打回) 如果业务表中有授信占用ID(urationId)，可直接根据授信占用ID释放，如果没有则根据业务ID(billId)和业务系统类型(sysCode)查找来释放
     * 
     * @param urationDTO
     *            授信占用DTO
     */
    void preBackUseRation(RatUrationDTO urationDTO);
    /**
     * 根据业务类型和票据号查询占用信息
     * 
     * @param sysCode
     *            系统代码
     * @param billId
     *            业务id
     * @return 授信占用DTO
     */
    RatUrationDTO getRatUrationDTOByBillId(String sysCode, String billId);
    /**
     * 授信冻结占用(暂存后直接提交) 如果业务表中有授信占用ID(urationId)，可直接根据授信占用ID冻结占用，如果没有则根据业务ID(billId)和业务系统类型(sysCode)查找来冻结占用
     * 
     * @param urationDTO
     *            授信占用DTO
     */
    void submitUseRation(RatUrationDTO urationDTO);
}
