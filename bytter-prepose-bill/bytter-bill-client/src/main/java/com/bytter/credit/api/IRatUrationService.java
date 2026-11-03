package com.bytter.credit.api;

import com.bytter.credit.dto.*;
import com.bytter.credit.vo.RatUrationUseVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 银行授信对外接口
 *
 * @author liyh
 */
public interface IRatUrationService {
    /**
     * 暂存授信冻结占用(业务数据暂存不提交)
     *
     * @param urationDTO 授信占用DTO
     */
    void tempUseRation(RatUrationDTO urationDTO);
    /**
     * 授信冻结占用(业务数据提交待审批)
     *
     * @param urationDTO 授信占用DTO
     */
    void addUseRation(RatUrationDTO urationDTO);
    /**
     * 授信冻结占用(暂存后直接提交) 如果业务表中有授信占用ID(urationId)，可直接根据授信占用ID冻结占用，如果没有则根据业务ID(billId)和业务系统类型(sysCode)查找来冻结占用
     *
     * @param urationDTO 授信占用DTO
     */
    void submitUseRation(RatUrationDTO urationDTO);
    /**
     * 授信冻结占用(业务暂存后删除) 如果业务表中有授信占用ID(urationId)，可直接根据授信占用ID删除占用，如果没有则根据业务ID(billId)和业务系统类型(sysCode)查找来删除占用
     *
     * @param urationDTO 授信占用DTO
     */
    void deleteUseRation(RatUrationDTO urationDTO);
    /**
     * 授信正式占用(审批通过调用) 如果业务表中有授信占用ID(urationId)，可直接根据授信占用ID占用，如果没有则根据业务ID(billId)和业务系统类型(sysCode)查找来占用
     *
     * @param urationDTO 授信占用DTO
     */
    void passUseRation(RatUrationDTO urationDTO);
    /**
     * 直接正式占用(正式占用后修改授信合同,原授信合同释放，占用新的授信合同)
     * 如果业务表中有授信占用ID(urationId)，可直接根据授信占用ID释放，如果没有则根据业务ID(billId)和业务系统类型(sysCode)查找来占用
     *
     * @param urationDTO 授信占用DTO
     */
    void directUseRation(RatUrationDTO urationDTO);
    /**
     * 授信打回(审批打回) 如果业务表中有授信占用ID(urationId)，可直接根据授信占用ID释放，如果没有则根据业务ID(billId)和业务系统类型(sysCode)查找来释放
     *
     * @param urationDTO 授信占用DTO
     */
    void backUseRation(RatUrationDTO urationDTO);
    /**
     * 授信占用后打回(审批通过后打回) 如果业务表中有授信占用ID(urationId)，可直接根据授信占用ID释放，如果没有则根据业务ID(billId)和业务系统类型(sysCode)查找来释放
     *
     * @param urationDTO 授信占用DTO
     */
    void preBackUseRation(RatUrationDTO urationDTO);
    /**
     * 功能介绍 : 授信释放(已正式占用的释放) 如果业务表中有授信占用ID(urationId)，可直接根据授信占用ID释放，如果没有则根据业务ID(billId)和业务系统类型(sysCode)查找来释放
     *
     * @param urationDTO 授信占用DTO
     */
    void releaseRation(RatUrationDTO urationDTO);
    /**
     * 根据各系统业务类型和业务单号（ID）获取授信占用信息
     *
     * @param sysCode 系统代码
     * @param billId  业务id
     * @return 授信占用DTO
     */
    RatUrationDTO getRatUrationDTOByBillId(String sysCode, String billId);
    /**
     * 根据各系（ID）获取授信占用信息
     *
     * @param id 业务id
     * @return 授信占用DTO
     */
    RatUrationDTO getRatUrationDTOById(String id);
    /**
     * 根据系统代码和业务id获取授信详情
     *
     * @param sysCode 系统代码
     * @param billId  业务id
     * @return 授信公共页面详情
     * todo
     */
    String getRatCommonDTOByBillId(String sysCode, String billId);
    /**
     * 根据各系统业务类型和业务单号（ID）获取授信占用信息
     *
     * @param urationDTO 授信占用DTO
     * @return 授信占用集合
     */
    List<RatUrationDTO> getRatUrationList(RatUrationDTO urationDTO);
    /**
     * @return
     */
    String getRatBankCtlConfig();
    /**
     * todo 原来实现为内部服务 需暴露为外部接口实现 功能介绍 : 授信挂接数据查询。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    List<RatUrationDisDTO> viewCorpDisRation(RatUrationDTO urationDTO, String isRmbRat);
    /**
     * todo 需要暴露给外部服务使用 功能介绍 : 获取银行授信品种。<br>
     */
    List<RatItemCustomDTO> getRatCustomByBank(String customId);
    /**
     * todo 获取授信品种
     *
     * @param code
     * @return
     */
    RatItemCustomDTO getRatCustomByCode(String code);
    /**
     * 获取已授权联行号
     *
     * @return 联行号信息集合
     */
    List<String> queryRationBank();

    List<String> queryRationBank(String corpId);
    /**
     * 获取已授权的票据编号。
     *
     * @return 票据编号。
     */
    public List<String> queryRationBill();
    /**
     * 通过业务id及编码查询授信挂接的担保方式
     * @param businessIds
     * @param sysCode
     * @return
     */
    List<GuaContractOccupDTO> getRatGuaWayNameList(List<String> businessIds, String sysCode);
    
    public List<RatUrationUseVO> queryRationUseDtl(UrationParamDTO paramDTO);


    /**
     * 根据授信编号和授信品种类型获取授信信息
     * @param corpId 授信单位ID
     * @param contractNo 授信合同编号
     * @param itemTypeCode 授信品种类型代码
     * @return 授信信息
     */
    RatUrationDisDTO getRatUrationDTOByParams(String corpId, String contractNo,String itemTypeCode);

    /**
    * @Description 票据指令失败打回
    * @author zhous
    * @date 2025/5/21 17:52
    */
    void billReleaseRation(RatUrationDTO urationDTO);
}
