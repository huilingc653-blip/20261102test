package com.bytter.system.api;
import com.bytter.system.api.dto.BtBankAccAppCurDTO;
import java.util.List;
public interface IBtBankAccAppCurService {
    List<BtBankAccAppCurDTO> listByAppId(String appId);
    void delByAppId(String appId);
    void saveByAppId(String appId, String[] curIds);
}
