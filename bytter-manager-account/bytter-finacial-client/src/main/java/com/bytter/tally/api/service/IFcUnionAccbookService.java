package com.bytter.tally.api.service;



import com.bytter.tally.api.dto.FcUnionAccbookDTO;

import java.util.List;

/**
 * 联行来账对外接口
 * @author liyh
 * @since 2019-2-19
 */
public interface IFcUnionAccbookService {
    /**
     * 保存联行来账信息
     * @param accbookDTO 账务实体
     */
    public void saveFcUnionAccbook(FcUnionAccbookDTO accbookDTO);
    /**
     * 根据发送凭证号获取往来信息
     * @param sendVoucherNo  发送凭证号
     * @return 网点往来账务集合
     */
    public List<FcUnionAccbookDTO> getUnionAccbookBySendNo(String sendVoucherNo);
    /**
     * 更新往来信息状态
     * @param id 往来信息单据Id
     * @param billState 要改成单据状态
     * @param oldBillState 之前的单据状态
     */
    public void updateUnionAccbookBillState(String id, String billState, String oldBillState);
}
