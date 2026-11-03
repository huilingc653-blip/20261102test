package com.bytter.system.api;


import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IBtBankAccUnFreezeService {

    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql);

    List<BtBankAccFreezeDTO> getBtBankAccFreeze(String accId, String curId);

    List<BtBankAccFreezeDTO> getBtBankAccFreezeTemp(String accId, String curId);

    List<BtBankAccFreezeDTO> getBtBankAccUnFreeze(String accId, String curId);

    BigDecimal getFreezeMoney(String accId, String curId);

    BigDecimal getUfFreezeMoney(String accId, String curId);

    BigDecimal getUfFreezeMoney(String accId, String curId, String uufid);

    BigDecimal getUfWaitFreezeMoney(String accId, String curId, String uufid);

    BigDecimal getFreezeMoney(String id);

    BigDecimal getUfFreezeMoneyForTemp(String accId, String curId, String uufid);

    DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql);

    BtBankAccFreezeDTO getObject(String id);

    void saveOrUpdate(BtBankAccFreezeDTO btBankAccFreezeDTO);

    void save(BtBankAccFreezeDTO btBankAccFreezeDTO);

    void deleteEntity(BtBankAccFreezeDTO btBankAccFreezeDTO);

}
