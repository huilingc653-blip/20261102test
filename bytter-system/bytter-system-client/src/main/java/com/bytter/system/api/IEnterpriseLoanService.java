package com.bytter.system.api;

import com.bytter.system.api.dto.FcAccPlusDTO;

import java.util.List;

/**
 * @author luoyj
 * @date 2024/1/24.
 */
public interface IEnterpriseLoanService {

    List<FcAccPlusDTO> getTallyAccTypeAccs(String netId, String corpId, String curId, List<String> accs);


    List<FcAccPlusDTO> getTallyAccByCorp(String netId, String corpId, String curId);


}
