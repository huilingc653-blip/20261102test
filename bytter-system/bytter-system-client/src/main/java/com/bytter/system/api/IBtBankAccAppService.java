package com.bytter.system.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.dto.BtBankAccAppDTO;
public interface IBtBankAccAppService {
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user);
    DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql);
    int getBankAccAppCountByCorpId(String corpId);
    BtBankAccAppDTO getBankAccApp(String id);
    BtBankAccAppDTO getById(String id);
    void saveOrUpdate(BtBankAccAppDTO btBankAccAppDTO);
    BtBankAccAppDTO save(BtBankAccAppDTO btBankAccAppDTO);
    void deleteEntity(BtBankAccAppDTO btBankAccAppDTO);
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, String busType);
    DataTablesOutput setJoinField(DataTablesOutput output);
    String getSequence(SequenceEnum sequenceEnum);
}
