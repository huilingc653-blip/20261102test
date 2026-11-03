package com.bytter.basic.service.impl;
import com.bytter.basic.entity.BtCurrency;
import com.bytter.basic.entity.BtCurrencyInit;
import com.bytter.basic.service.BtCurrencyInitService;
import com.bytter.basic.service.BtCurrencyService;
import com.bytter.basic.vo.BtCurrencyVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * 货币维护Service接口类
 *
 * @author zuoy
 * @since 2017/06/26
 */
@Transactional
@DubboService
public class BtCurrencyServiceImpl implements IBtCurrencyService {
    @Autowired
    private BtCurrencyService btCurrencyServiceBase;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private BtCurrencyInitService btCurrencyInitService;
    @Autowired
    private Ii18nService i18nServiceI;
    @Override
    public BtCurrencyDTO getObjectById(String id) {
        if (EmptyUtils.isBlank(id)) {
            return null;
        }
        BtCurrencyVO btCurrency = this.btCurrencyServiceBase.getByUnion(id);
        if (EmptyUtils.isEmpty(btCurrency)) {
            return null;
        }
        BtCurrencyDTO btCurrencyDTO = new BtCurrencyDTO();
        BeanUtils.copyProperties(btCurrency, btCurrencyDTO);
        return btCurrencyDTO;
    }
    @Override
    public BtCurrencyDTO getNatualCurrency() {
        BtCurrency btCurrency = btCurrencyServiceBase.getNatualCurrency();
        BtCurrencyDTO btCurrencyDTO = new BtCurrencyDTO();
        if (EmptyUtils.isEmpty(btCurrency)) {
            return null;
        }
        BeanUtils.copyProperties(btCurrency, btCurrencyDTO);
        String businessBaseContext = i18nServiceI.getBusinessBaseContext(btCurrency.getId());
        if (StringUtils.isNotBlank(businessBaseContext)) {
            btCurrencyDTO.setCurName(businessBaseContext);
        }
        return btCurrencyDTO;
    }
    @Override
    public BtCurrencyDTO getRMBCurrency() {
        BtCurrency btCurrency = btCurrencyServiceBase.getRMBCurrency();
        BtCurrencyDTO btCurrencyDTO = new BtCurrencyDTO();
        if (EmptyUtils.isEmpty(btCurrency)) {
            return null;
        }
        BeanUtils.copyProperties(btCurrency, btCurrencyDTO);
        return btCurrencyDTO;
    }
    @Override
    public List<BtCurrencyDTO> listByNotNatualCurrency() {
        List<BtCurrency> list = this.btCurrencyServiceBase.listByNotNatualCurrency();
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<BtCurrencyDTO> dto = BeanUtils.copyListProperties(list, BtCurrencyDTO.class);
        return dto;
    }
    @Override
    public String getCurrencyByName(String curName) {
        return this.btCurrencyServiceBase.getCurrencyByName(curName);
    }
    @Override
    public BtCurrencyDTO getCurrencyByCode(String curCode) {
        if (EmptyUtils.isBlank(curCode)) {
            return null;
        }
        BtCurrency btCurrency = this.btCurrencyServiceBase.getCurrencyByCode(curCode);
        BtCurrencyDTO btCurrencyDTO = new BtCurrencyDTO();
        BeanUtils.copyNotNullProperties(btCurrency, btCurrencyDTO);
        return btCurrencyDTO;
    }
    @Override
    public List<BtCurrencyDTO> getBtCurrency() {
        List<BtCurrencyVO> list = this.btCurrencyServiceBase.getBtCurrency();
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<BtCurrencyDTO> dtoList = new ArrayList<>();
        for (BtCurrencyVO vo : list) {
            BtCurrencyDTO dto = new BtCurrencyDTO();
            BeanUtils.copyNotNullProperties(vo, dto);
            dto.setCurSign(vo.getCurSign());
            dtoList.add(dto);
        }
        return dtoList;
    }
    @Override
    public String getIdByNameOrEnglishCode(String eqMatchName) {
        return this.btCurrencyServiceBase.getIdByNameOrEnglishCode(eqMatchName);
    }
    @Override
    public String getCurSign(String id) {
        if (EmptyUtils.isBlank(id)) {
            return null;
        }
        BtCurrencyVO btCurrency = this.btCurrencyServiceBase.getByUnion(id);
        if (EmptyUtils.isEmpty(btCurrency)) {
            return null;
        }
        return btCurrency.getCurSign();
    }
    @Override
    public List<BtCurrencyDTO> listAllBtCurrency() {
        List<BtCurrency> currency = this.btCurrencyServiceBase.listAll();
        return BeanUtils.copyListProperties(currency, BtCurrencyDTO.class);
    }
    @Override
    public List<BtCurrencyDTO> listAllBtCurrencyInit() {
        List<BtCurrencyInit> list = this.btCurrencyInitService.listAll();
        return BeanUtils.copyListProperties(list, BtCurrencyDTO.class);
    }
    /**
     * 根据币别名称或英文见吗获取币种
     *
     * @param search 查询条件
     * @return id
     */
    @Override
    public BtCurrencyDTO getByCurNameOrEnglishCode(String search) {
        List<BtCurrencyVO> list = this.btCurrencyServiceBase.getByCurNameOrEnglishCode(search);
        BtCurrencyDTO btCurrencyDTO = new BtCurrencyDTO();
        if (EmptyUtils.isNotEmpty(list)) {
            BtCurrencyVO btCurrencyVO = list.get(0);
            BeanUtils.copyNotNullProperties(btCurrencyVO, btCurrencyDTO);
        }
        return btCurrencyDTO;
    }
    @Override
    public List<BtCurrencyDTO> getBondCurList(String billCurId, String corpId) {
        BtCurrencyDTO natualBtCurrencyDTO = null;
        if (EmptyUtils.isNotEmpty(corpId)) {//获取单位维护的本位币
            CorpDTO corpDTO = corpServiceI.get(corpId);
            String corpNatualCurrency = corpDTO.getCurId();
            if (EmptyUtils.isNotEmpty(corpNatualCurrency)) {
                natualBtCurrencyDTO = getObjectById(corpNatualCurrency);
            }
        }
        List<BtCurrencyDTO> btCurrencyDTOS = new ArrayList<BtCurrencyDTO>();
        BtCurrencyDTO btCurrencyDTO = null;
        if (StringUtils.isNotEmpty(billCurId)) {
            btCurrencyDTO = getObjectById(billCurId);
            btCurrencyDTOS.add(btCurrencyDTO);//业务币种
            if (null != natualBtCurrencyDTO) {
                if (!natualBtCurrencyDTO.getId().equals(btCurrencyDTO.getId())) {//业务币别和单位维护的本位币一致时不需要重复添加
                    btCurrencyDTOS.add(natualBtCurrencyDTO);//单位维护的本位币
                }
            }
        }
        return btCurrencyDTOS;
    }

    @Override
    public Map<String, BtCurrencyDTO> getCurrencyByIds(List<String> curIds) {
        if (EmptyUtils.isEmpty(curIds)) {
            return new HashMap<>();
        }
        List<BtCurrencyVO> curList = this.btCurrencyServiceBase.listByIds(curIds);
        if (EmptyUtils.isEmpty(curList)) {
            return new HashMap<>();
        }
        List<BtCurrencyDTO> curDtoList = BeanUtils.copyListProperties(curList, BtCurrencyDTO.class);
        return curDtoList.stream().collect(Collectors.toMap(BtCurrencyDTO::getId, Function.identity()));
    }

    @Override
    public Map<String, String> getCurrencyNameByIds(List<String> curIds) {
        if (EmptyUtils.isEmpty(curIds)) {
            return new HashMap<>();
        }
        List<BtCurrencyVO> curList = this.btCurrencyServiceBase.listByIds(curIds);
        if (EmptyUtils.isEmpty(curList)) {
            return new HashMap<>();
        }
        List<BtCurrencyDTO> curDtoList = BeanUtils.copyListProperties(curList, BtCurrencyDTO.class);
        return curDtoList.stream().collect(Collectors.toMap(BtCurrencyDTO::getId, BtCurrencyDTO::getCurName, (a,b) -> a));
    }

    @Override
    public List<BtCurrencyDTO> queryValidCur(){
        return btCurrencyServiceBase.queryValidCur();
    }
    @Override
    public Map<String, BtCurrencyDTO> getCurrency() {
        List<BtCurrency> curList = this.btCurrencyServiceBase.listAll();
        if (EmptyUtils.isEmpty(curList)) {
            return new HashMap<>();
        }
        List<BtCurrencyDTO> curDtoList = BeanUtils.copyListProperties(curList, BtCurrencyDTO.class);
        return curDtoList.stream().collect(Collectors.toMap(BtCurrencyDTO::getId, Function.identity()));
    }
    @Override
    public Map<String, String> getCurrencyNameMap() {
        List<BtCurrency> curList = this.btCurrencyServiceBase.listAll();
        if (EmptyUtils.isEmpty(curList)) {
            return new HashMap<>();
        }
        return curList.stream().collect(Collectors.toMap(BtCurrency::getId, BtCurrency::getCurName));
    }

    @Override
    public Map<String, BtCurrencyDTO> listCurrencyNameMap() {
        Map<String, BtCurrencyDTO> curMap = new HashMap<>();
        List<BtCurrencyDTO> list = this.getBtCurrency();
        for (BtCurrencyDTO cur:list) {
            curMap.put(cur.getCurName(), cur);
        }
        return curMap;
    }

    @Override
    public Map<String, String> getCurrencyIdsByNames(List<String> curNames) {
        if (EmptyUtils.isEmpty(curNames)) {
            return null;
        }
        List<BtCurrencyVO> curList = btCurrencyServiceBase.listByNames(curNames);
        if (EmptyUtils.isEmpty(curList)) {
            return null;
        }
        List<BtCurrencyDTO> curDtoList = BeanUtils.copyListProperties(curList, BtCurrencyDTO.class);
        return curDtoList.stream().collect(Collectors.toMap(BtCurrencyDTO::getCurName, BtCurrencyDTO::getId, (a,b) -> a));
    }


    /**
     * 获取有效的币种Map
     * @return
     */
    @Override
    public Map<String, BtCurrencyDTO> getCurrencyMap() {
        Map<String, BtCurrencyDTO> curMap = new HashMap<>();
        List<BtCurrencyDTO> list = this.getBtCurrency();
        for (BtCurrencyDTO cur:list) {
            curMap.put(cur.getId(), cur);
        }
        return curMap;
    }


    /**
     * 获取有效的币种Map
     * @return
     */
    @Override
    public Map<String, BtCurrencyDTO> getCurrencyEnglishCodeMap() {
        Map<String, BtCurrencyDTO> curMap = new HashMap<>();
        List<BtCurrencyDTO> list = this.getBtCurrency();
        for (BtCurrencyDTO cur:list) {
            curMap.put(cur.getEnglishCode(), cur);
        }
        return curMap;
    }
}
