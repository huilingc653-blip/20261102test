package com.bytter.guarantee.api;

import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.dto.LcEnrolDTO;

import java.util.List;

public interface ILcGuaranteeBillBondService {

    public DataTablesOutput findAbVoucherInfoId(String corpId, String scoringStartDate, String scoringEndDate, String bankType, String bankId);

    public List<LcEnrolDTO> findLcEnrol(String corpId, String scoringStartDate, String scoringEndDate, String bankType, String bankId);
}
