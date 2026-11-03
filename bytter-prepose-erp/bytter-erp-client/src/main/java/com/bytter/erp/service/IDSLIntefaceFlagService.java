package com.bytter.erp.service;

import com.bytter.erp.dto.DSLIntefaceFlagDTO;

public interface IDSLIntefaceFlagService {

	DSLIntefaceFlagDTO getFlagBy(String sourceId,String tableSource);

}
