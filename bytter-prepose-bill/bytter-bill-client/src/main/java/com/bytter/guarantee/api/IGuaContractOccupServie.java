package com.bytter.guarantee.api;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaContractQueryDTO;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import java.util.List;
/**
 * 担保合同接口
 *
 * @author liusy
 */
public interface IGuaContractOccupServie {
    /**
     * 业务暂存时调用（暂存占用）
     *
     * @param guaContractOccupDTO
     */
    void tempUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO);
    /**
     * 业务提交审批调用（在审占用）
     *
     * @param guaContractOccupDTO
     */
    void submitUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO);
    /**
     * 业务提交审批调用（正式占用）
     *
     * @param guaContractOccupDTO
     */
    void saveUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO);
    /**
     * 业务删除或业务结束调用（释放）
     *
     * @param guaContractOccupDTO
     */
    void releaseUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO);
    /**
     * 业务打回调用
     *
     * @param guaContractOccupDTO
     */
    void backUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO);
    /**
     * 发放打回调用
     *
     * @param guaContractOccupDTO
     */
    void sendBackUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO);
    /**
     * 查询担保占用信息（不包含已释放数据）
     *
     * @param guaContractOccupDTO
     * @return 担保合同占用DTO
     */
    GuaContractOccupDTO getUseGuaContractOccup(GuaContractOccupDTO guaContractOccupDTO);
    /**
     * 根据业务id和业务类型删除挂接担保合同信息
     *
     * @param businessId
     */
    void deleteGuaContractOccup(String businessId, String sysCode);
    /**
     * 查询担保占用情况
     *
     * @param businessId 业务id
     * @param itemNo     业务编号
     * @return 担保合同使用DTO
     */
    GuaContractUseDTO getGuaContractOccupyDtl(String businessId, String itemNo);
    /**
     * 根据主业务id和业务编码查询担保方式
     *
     * @param businessIds
     * @param sysCode
     * @return
     */
    List<GuaContractQueryDTO> listGuaWayName(List<String> businessIds, String sysCode);

    /**
     * 获取挂接担保的业务id
     *
     * @param sysCode   查询业务种类
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param status    担保状态
     * @return 业务id集合，
     */
    List<GuaContractOccupDTO> getGua(String sysCode, String startDate, String endDate, String status,String contractId);

    /**
     * 获取担保合同暂用情况
     * @param sysCode   查询业务种类
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param status    担保状态
     * @return
     */
    List<GuaContractOccupDTO> listUseGua(String sysCode, String startDate, String endDate, String status);
}
