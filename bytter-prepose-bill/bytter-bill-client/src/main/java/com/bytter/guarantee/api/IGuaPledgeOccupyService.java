package com.bytter.guarantee.api;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import java.util.List;
/**
 * @author luxy
 * @Title: IGuaPledgeOccupyService
 * @ProjectName T3(3.2)
 * @Description: 抵质押物占用接口
 * @date 2020/7/7 11:26
 */
public interface IGuaPledgeOccupyService {
    /**
     * 冻结抵质押物占用信息（暂存或提交操作均是冻结状态）
     */
    void freezePledgeOccupy(List<GuaPledgeOccupyDTO> list);
    void savePledgeOccupy(List<GuaPledgeOccupyDTO> list, Integer status, String sysCode);
    /**
     * 占用抵质押物占用信息（存在补录过程的业务，补录审批通过后正式占用）
     */
    void addPledgeOccupy(List<GuaPledgeOccupyDTO> list);
    /**
     * 打回抵质押物占用信息(额度正式占用后的打回)
     */
    void backPledgeOccupy(String businessId, String itemNo);
    /**
     * 根据业务id和业务编号删除抵质押物占用信息
     * 
     * @param businessId
     */
    void deletePledgeOccupy(String businessId, String itemNo);
    /**
     * 根据业务id和抵质押物编号修改抵质押物占用信息的状态
     * 
     * @param businessId
     */
    void updatePledgeOccupy(String businessId, String pledgeId);
    /**
     * 根据业务id和业务编号释放抵质押物占用或冻结信息
     * 
     * @param businessId
     */
    void releasePledgeOccupy(String businessId, String itemNo);
    /**
     * 根据业务id和业务编号获取抵质押占用信息
     * 
     * @param businessId
     * @param itemNo
     * @return
     */
    List<GuaPledgeOccupyDTO> getPledgeOccupy(String businessId, String itemNo);
    /**
     * 根据抵质押物id获取抵质押物冻结数据
     * 
     * @param pledgeId
     * @return
     */
    public List<GuaPledgeOccupyDTO> getPledgeOccupyListFrozen(String pledgeId);
    /**
     * 根据抵质押物id获取抵质押物占用数据
     * 
     * @param pledgeId
     * @return
     */
    public List<GuaPledgeOccupyDTO> getPledgeOccupyList(String pledgeId);
}
