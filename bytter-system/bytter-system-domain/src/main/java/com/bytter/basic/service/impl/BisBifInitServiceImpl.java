package com.bytter.basic.service.impl;
import com.bytter.basic.entity.BisBifInit;
import com.bytter.basic.service.BisBifInitService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.dto.AccAndBifDTO;
import com.bytter.system.api.dto.BisBifInitDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * Created by zhangxb on 2017/5/22 0022.
 */
@Service("bisBifInitServiceI")
@DubboService
@Transactional
public class BisBifInitServiceImpl implements IBisBifInitService {
    @Autowired
    private BisBifInitService bisBifInitService;
    @Override
    public BisBifInitDTO getObjectById(String id) {
        BisBifInit bisBifInit = bisBifInitService.getById(id);
        if (EmptyUtils.isEmpty(bisBifInit) || EmptyUtils.isBlank(bisBifInit.getId())) {
            return null;
        }
        BisBifInitDTO bisBifInitDTO = new BisBifInitDTO();
        BeanUtils.copyNotNullProperties(bisBifInit, bisBifInitDTO);
        return bisBifInitDTO;
    }
    @Override
    public List<BisBifInitDTO> listByValidSign(String validSign) {
        List<BisBifInit> bisBifInitList = bisBifInitService.getListByValidSign(validSign);
        if (EmptyUtils.isEmptyList(bisBifInitList)) {
            return null;
        }
        List<BisBifInitDTO> list = BeanUtils.copyListProperties(bisBifInitList, BisBifInitDTO.class);
        return list;
    }
    @Override
    public List<BisBifInitDTO> listBySignOrBankType(String validSign, String bankType) {
        List<BisBifInit> bisBifInitList = bisBifInitService.listBySignOrBankType(validSign, bankType);
        if (EmptyUtils.isEmptyList(bisBifInitList)) {
            return null;
        }
        List<BisBifInitDTO> list = BeanUtils.copyListProperties(bisBifInitList, BisBifInitDTO.class);
        return list;
    }
    @Override
    public BisBifInitDTO getByBifCode(String bifCode) {
        BisBifInit bisBifInit = this.bisBifInitService.getByBifCode(bifCode);
        if (EmptyUtils.isEmpty(bisBifInit)) {
            return null;
        }
        BisBifInitDTO dto = new BisBifInitDTO();
        BeanUtils.copyProperties(bisBifInit, dto);
        return dto;
    }
    @Override
    public BisBifInitDTO getByName(String name) {
        BisBifInit bisBifInit = this.bisBifInitService.getByName(name);
        if (EmptyUtils.isEmpty(bisBifInit)) {
            return null;
        }
        BisBifInitDTO dto = new BisBifInitDTO();
        BeanUtils.copyProperties(bisBifInit, dto);
        return dto;
    }
	@Override
	public Map<String, AccAndBifDTO> getQuerycmdByBifAndAcc(List<Map<String, String>> mapParam) {
		// TODO Auto-generated method stub
		return null;
	}
    @Override
    public Map<String, BisBifInitDTO> getBisBifInitMap() {
        List<BisBifInit> list = this.bisBifInitService.listAll();
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<BisBifInitDTO> resultList = BeanUtils.copyListProperties(list, BisBifInitDTO.class);
        return resultList.stream().collect(Collectors.toMap(BisBifInitDTO::getId, Function.identity()));
    }

    @Override
    public Map<String, BisBifInitDTO> getBisBifInitBankTypeIdMap() {
        List<BisBifInit> list = this.bisBifInitService.listBisBifInit();
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<BisBifInitDTO> resultList = BeanUtils.copyListProperties(list, BisBifInitDTO.class);
        return resultList.stream().collect(Collectors.toMap(BisBifInitDTO::getBankTypeId, Function.identity()));
    }

    @Override
    public Map<String, BisBifInitDTO> listBifInitByIds(List<String> ids) {
        List<BisBifInit> bisBifList = bisBifInitService.listByIds(ids);
        if (EmptyUtils.isEmpty(bisBifList)){
            return null;
        }
        List<BisBifInitDTO> resultList = BeanUtils.copyListProperties(bisBifList, BisBifInitDTO.class);
        return resultList.stream().collect(Collectors.toMap(BisBifInitDTO::getId, Function.identity(), (first, second) -> first));
    }

    @Override
    public Map<String, BisBifInitDTO> listBifInitByCode(List<String> codeList) {
        List<BisBifInit> bisBifList = bisBifInitService.listByCode(codeList);
        if (EmptyUtils.isEmpty(bisBifList)){
            return null;
        }
        List<BisBifInitDTO> resultList = BeanUtils.copyListProperties(bisBifList, BisBifInitDTO.class);
        return resultList.stream().collect(Collectors.toMap(BisBifInitDTO::getBifCode, Function.identity(), (first, second) -> first));
    }

    @Override
    public List<BisBifInitDTO> getDirectConnectBankType() {
        List<BisBifInit> bisBifList = bisBifInitService.getDirectConnectBankType();
        if (EmptyUtils.isEmptyList(bisBifList)) {
            return null;
        }
        List<BisBifInitDTO> list = BeanUtils.copyListProperties(bisBifList, BisBifInitDTO.class);
        return list;
    }
}
