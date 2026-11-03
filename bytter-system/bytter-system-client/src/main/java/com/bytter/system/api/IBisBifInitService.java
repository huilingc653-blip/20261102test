package com.bytter.system.api;

import com.bytter.system.api.dto.AccAndBifDTO;
import com.bytter.system.api.dto.BisBifInitDTO;

import java.util.List;
import java.util.Map;
/**
 * Created by zhangxb on 2017/5/22 0022.
 */
public interface IBisBifInitService {
    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    BisBifInitDTO getObjectById(String id);
    /**
     * 根据有效标志过滤
     *
     * @param validSign 可空
     * @return
     */
    List<BisBifInitDTO> listByValidSign(String validSign);
    /**
     * 根据有效标志，银行类别过滤
     *
     * @param validSign  可空
     * @param bankTypeId
     * @return
     */
    List<BisBifInitDTO> listBySignOrBankType(String validSign, String bankTypeId);
    /**
     * 根据接口代码查找
     *
     * @param bifCode 接口代码
     * @return
     */
    BisBifInitDTO getByBifCode(String bifCode);
    /**
     * 根据接口代码名称查找
     *
     * @param name 接口代码名称
     * @return
     */
    BisBifInitDTO getByName(String name);
    
    
    /**
     * 根据账户ID和接口code查询指令对应的信息 //TODO 
select b.id as id,b.voucher_No as voucherNo,b.trans_Id as transId,b.send_Time as sendTime,b.bank_Acc_Id as bankAccId,b.bank_Acc as bankAcc,
b.cur_Id as curId,b.start_Time as startTime,b.end_Time as endTime,b.bif_Code as bifCode,b.cmd_Type as cmdType,b.cmd_Stat as cmdStat,b.trans_Time as transTime,
b.return_Time as returnTime,b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,
c.corp_Id as corpId,c.acc_Name as accName,c.bank_Name as bankName,
t.name as bifName,
from bis_query_cmd b left join bt_bank_acc c on  b.bank_Acc_Id = c.id
left join bis_bif_init t on b.bif_Code = t.id
where b.id=:id
     */
    Map<String,AccAndBifDTO> getQuerycmdByBifAndAcc(List<Map<String,String>> mapParam);
    Map<String, BisBifInitDTO> getBisBifInitMap();
    Map<String, BisBifInitDTO> getBisBifInitBankTypeIdMap();

    /**
     * 根据ID获取接口信息。
     * @param ids ID集合。
     * @return 接口表信息。
     */
    Map<String, BisBifInitDTO> listBifInitByIds(List<String> ids);

    /**
     * 根据接口code获取接口信息。
     *
     * @param codeList code集合。
     * @return 接口信息。
     */
    Map<String, BisBifInitDTO> listBifInitByCode(List<String> codeList);

    /**
    * @Description 获取银行类型的直连银行为'是'的银行接口信息
    * @author zhous
    * @date 2024/8/22 17:07
    */
    List<BisBifInitDTO> getDirectConnectBankType();

}
