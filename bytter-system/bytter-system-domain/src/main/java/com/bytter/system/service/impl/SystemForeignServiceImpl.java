package com.bytter.system.service.impl;
import com.bytter.system.api.ISystemForeignService;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.ErpBasicDataVOVO;
import com.bytter.system.inneraccount.service.FcAccService;
import com.bytter.system.service.CorpService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service("systemForeignServiceI")
@DubboService
public class SystemForeignServiceImpl implements ISystemForeignService {
    @Autowired
    private FcAccService fcAccService;
    @Autowired
    private CorpService corpService;
    @Override
    public List<BalanceQueryDTO> queryAccBalanceAndBank(BalanceQueryDTO dto, String userCode, List<String> corpAuth, String... aTrue) {
        return fcAccService.queryAccBalanceAndBank(dto, userCode, corpAuth, aTrue);
    }
    @Override
    public List<FcAccCorpCurrencyDTO> getAccCorpCurDataByAccounts(List<String> accounts) {
        return fcAccService.getAccCorpCurDataByAccounts(accounts);
    }
    public List listBySQL(String sql, final Map<String, ?> params){
        return corpService.listBySQL(sql.toString(), params);
    }
    public <B> List<B> listBySQLAliasToBean(String sql, final Map<String, ?> params, final Class<B> clazz) {
        return corpService.listBySQLAliasToBean(sql.toString(), null, clazz);
    }
    public List<ErpBasicDataVOVO> listErpBasicDataVOVO(String sql) {
        return corpService.listErpBasicDataVOVO(sql.toString());
    }
}
