package com.bytter.increment.api.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import java.util.List;
/**
 * 内部理财接口类
 * @author wangzx
 * @since 2019-11-5
 */
public interface IChoInnerFinanceService {
    /**
     * 根据业务类型和状态获取内部理财信息集合
     * @param busType 业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约
     * @param status 状态
     * @return
     */
    DataTablesOutput listChoInnerFinanceByBusType(DataTablesInput dataTablesInput, Integer busType, String status, String opType, String userId, List<String> corpAuth);
    /**
     * 根据ID获取理财信息
     * @param id
     * @return
     */
    ChoInnerFinanceDTO getInnerDepositById(String id);
    /**
     * 根据billNo获取理财信息
     * @param billNo
     * @return
     */
    ChoInnerFinanceDTO getInnerDepositByBillNo(String billNo);
    /**
     * 修改理财信息
     * @param dto 银理财信息DTO
     * @param user 当前用户
     */
    void updateInnerFinance(ChoInnerFinanceDTO dto, ICurrentUser user);
    /**
     * 生成日志
     */
    void tallyBackMessage(String id,ICurrentUser user);
    /**
     * 查询待记账数据
     */
    List<ChoInnerFinanceDTO> queryTallyByBusType(Integer busType);
    /**
    * <br>
    * <b>功能：</b> 查询通知取款最近的通知预约记录，如有则更新为已取款 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/12/16 0016 <br>
    */
    public void updateLastN2BData(ChoInnerFinanceDTO entity);
    List<ChoInnerFinanceDTO> getChoInnerFinance(Integer[] status, Integer billType);

    /**
     * 帐号过滤？
     *
     * @return
     */
    List<String> filterBankAcc(List<String> blnctrAccs);
}
