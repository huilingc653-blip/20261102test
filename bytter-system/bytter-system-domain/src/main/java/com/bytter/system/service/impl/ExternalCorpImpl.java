package com.bytter.system.service.impl;
import cn.hutool.core.bean.BeanUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.system.api.dto.FbsBudgetDetailDTO;
import com.bytter.system.api.vo.ExternalCorpBankAccDetailDto;
import com.bytter.system.dao.ExternalCorpAndCorpDao;
import com.bytter.system.dao.ExternalCorpBankACCDao;
import com.bytter.system.dao.ExternalCorpDao;
import com.bytter.system.entity.ExternalCorp;
import com.bytter.system.entity.ExternalCorpAndCorp;
import com.bytter.system.entity.ExternalCorpBankAcc;
import com.bytter.system.service.CorpService;
import com.bytter.system.service.ExternalCorpService;
import com.bytter.system.vo.ExternalCorpBankAccVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/7/6.
 */
@Service("externalCorpI")
@DubboService
public class ExternalCorpImpl implements IExternalCorpService {
    @Autowired
    private ExternalCorpDao externalCorpDao;
    @Autowired
    private ExternalCorpBankACCDao externalCorpBankACCDao;
    @Autowired
    private ExternalCorpAndCorpDao externalCorpAndCorpDao;
    @Autowired
    //@Qualifier("externalCorpServiceBase")
    private ExternalCorpService externalCorpService;
    @Autowired
    private IConfigService configServiceI;
    @Autowired
    //@Qualifier("corpServiceBase")
    private CorpService corpService;
    public List<ExternalCorpDTO> listByCorpId(String corpId) {
        List<String> externalCorpAndCorps = externalCorpAndCorpDao.listExternalCorpIdsByCorpIds(corpId);
        List<ExternalCorp> externalCorpList = externalCorpDao.listExternalCorpByIds(externalCorpAndCorps);
        List<ExternalCorpDTO> externalCorpDTOList = BeanUtils.copyListProperties(externalCorpList, ExternalCorpDTO.class);
        for (ExternalCorpDTO externalCorpDTO : externalCorpDTOList) {
            List<ExternalCorpBankAcc> externalCorpBankAccList = externalCorpBankACCDao.listBankAccByExternalCorpId(externalCorpDTO.getId());
            List<ExternalCorpBankAccDTO> externalCorpBankAccDTOList = BeanUtils.copyListProperties(externalCorpBankAccList, ExternalCorpBankAccDTO.class);
            externalCorpDTO.setExternalCorpBankAccList(externalCorpBankAccDTOList);
        }
        return externalCorpDTOList;
    }
    /**
     * 根据客商ID和客商名称判断是否需要新增客商信息
     *
     * @param externalCorpDTO
     */
    @Transactional
    @Override
    public void saveIfNotExist(ExternalCorpDTO externalCorpDTO) {
        //客商统一管理，不保存/更新客商资料信息
        String externalCorp = configServiceI.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue();
        if (CommonConstant.YES.equals(externalCorp)) {
            return;
        }
        //单位和客商名称为空，则直接返回
        if (StringUtils.isEmpty(externalCorpDTO.getCorpId()) || StringUtils.isEmpty(externalCorpDTO.getName())) {
            return;
        }
        //默认国内
        if (EmptyUtils.isEmpty(externalCorpDTO.getIsNative())) {
            externalCorpDTO.setIsNative(1);
        }
        //根据单位和客户名称判断数据库是否已经存在
        //ExternalCorp externalCorpDB = externalCorpDao.findByCorpIdAndName(externalCorpDTO.getCorpId(), externalCorpDTO.getName());
        List<ExternalCorp> externalCorpDBList = externalCorpDao.findExternalCorpsByCorpIdAndName(externalCorpDTO.getName());
        if (EmptyUtils.isNotEmpty(externalCorpDBList) && externalCorpDBList.size() > 1) {
            return;
        }
        ExternalCorp externalCorpDB = EmptyUtils.isEmpty(externalCorpDBList) ? null : externalCorpDBList.get(0);
        //根据客户银行账号判断数据库是否已经存在
        ExternalCorpBankAccDTO firstBankAcc = EmptyUtils.isEmpty(externalCorpDTO.getExternalCorpBankAccList())
                ? null : externalCorpDTO.getExternalCorpBankAccList().get(0);
        String externalAcc = firstBankAcc == null ? null : firstBankAcc.getExternalAcc();
        // 空账号不具备唯一性，不能用于判断客商是否已经存在。
        ExternalCorpBankAcc externalCorpBankAccDB = StringUtils.isBlank(externalAcc)
                ? null : externalCorpBankACCDao.findByExternalAcc(externalAcc);
        ExternalCorp externalCorpInput = BeanUtils.copyNewInstanceProperties(externalCorpDTO, ExternalCorp.class);
        //客商资料不存在且客商账号不存在则新增客商的同时新增账号信息
        if (externalCorpDB == null && externalCorpBankAccDB == null) {
            //审核状态为暂存
            externalCorpInput.setAuditStatus(CommonConstant.STATUE_CHECK);
            externalCorpDao.save(externalCorpInput);
            if ("0".equals(externalCorpInput.getIsCommon())) {
                ExternalCorpAndCorp externalCorpAndCorp = new ExternalCorpAndCorp();
                externalCorpAndCorp.setExternalCorpId(externalCorpInput.getId());
                externalCorpAndCorp.setCorpId(externalCorpDTO.getCorpId());
                externalCorpAndCorpDao.save(externalCorpAndCorp);
            }
            if (!EmptyUtils.isEmpty(externalCorpDTO.getExternalCorpBankAccList())) {
                for (ExternalCorpBankAccDTO externalCorpBankAccDTO : externalCorpDTO.getExternalCorpBankAccList()) {
                    ExternalCorpBankAcc externalCorpBankAcc = BeanUtils.copyNewInstanceProperties(externalCorpBankAccDTO, ExternalCorpBankAcc.class);
                    externalCorpBankAcc.setExternalCorpId(externalCorpInput.getId());
                    externalCorpBankACCDao.save(externalCorpBankAcc);
                }
            }
        } else if (externalCorpDB == null && externalCorpBankAccDB != null) {
            //根据主键获取客商实体
            externalCorpDB = externalCorpDao.get(ExternalCorp.class, externalCorpBankAccDB.getExternalCorpId());
            if (externalCorpDB != null) {
                // 业务新增客商保存时,检查该客商是否与单位已绑定,若已经绑定,而且当前的单位不是已经绑定的单位,则删除绑定关系后,设置客商为共享类型
                //修改为不是已经绑定的单位，增加绑定关系
                List<String> externalCorpAndCorps = externalCorpAndCorpDao.getCorpIdsByExternalCorpId(externalCorpDB.getId());
                if (!EmptyUtils.isEmptyList(externalCorpAndCorps)) {
                    if (!externalCorpAndCorps.contains(externalCorpDTO.getCorpId())) {
//                        externalCorpAndCorpDao.deleteExternalCorpAndCorp(externalCorpBankAccDB.getExternalCorpId());
                        ExternalCorpAndCorp externalCorpAndCorp = new ExternalCorpAndCorp();
                        externalCorpAndCorp.setExternalCorpId(externalCorpBankAccDB.getExternalCorpId());
                        externalCorpAndCorp.setCorpId(externalCorpDTO.getCorpId());
                        externalCorpAndCorpDao.save(externalCorpAndCorp);
                        // 注意：此处不再用当前单据的名称/资料覆盖共享客商主数据，
                        // 否则同一客商下其它单据的名称、地址等展示字段会跟着变。
                        externalCorpDB.setIsCommon("1");
                        externalCorpDao.update(externalCorpDB);
                    }
                }
            }
            //获取客商原本存在的账号信息
            List<ExternalCorpBankAcc> externalCorpBankAccList = externalCorpBankACCDao.listBankAccByExternalCorpId(externalCorpBankAccDB.getExternalCorpId());
            for (ExternalCorpBankAccDTO externalCorpBankAccDTO : externalCorpDTO.getExternalCorpBankAccList()) {
                //判断客商账号信息不存在，则新增客商信息，存在则更新客商信息
                if (!checkExternalCorpBankAccExist(externalCorpBankAccList, externalCorpBankAccDTO)) {
                    ExternalCorpBankAcc externalCorpBankAcc = BeanUtils.copyNewInstanceProperties(externalCorpBankAccDTO, ExternalCorpBankAcc.class);
                    externalCorpBankAcc.setExternalCorpId(externalCorpBankAccDB.getExternalCorpId());
                    externalCorpBankACCDao.save(externalCorpBankAcc);
                } else {
                    for (ExternalCorpBankAcc externalCorpBankAcc : externalCorpBankAccList) {
                        if (externalCorpBankAccDTO.getExternalAcc().equals(externalCorpBankAcc.getExternalAcc())) {
                            externalCorpBankAcc.setBank(externalCorpBankAccDTO.getBank());
                            externalCorpBankAcc.setBankType(externalCorpBankAccDTO.getBankType());
                            externalCorpBankAcc.setBankSourceCode(externalCorpBankAccDTO.getBankSourceCode());
                            externalCorpBankAcc.setAccProvince(externalCorpBankAccDTO.getAccProvince());
                            externalCorpBankAcc.setAccCity(externalCorpBankAccDTO.getAccCity());
                            externalCorpBankACCDao.update(externalCorpBankAcc);
                        }
                    }
                }
            }
        } else if (externalCorpDB != null && externalCorpBankAccDB == null) {
            if (!EmptyUtils.isEmpty(externalCorpDTO.getExternalCorpBankAccList())) {
                for (ExternalCorpBankAccDTO externalCorpBankAccDTO : externalCorpDTO.getExternalCorpBankAccList()) {
                    ExternalCorpBankAcc externalCorpBankAcc = BeanUtils.copyNewInstanceProperties(externalCorpBankAccDTO, ExternalCorpBankAcc.class);
                    externalCorpBankAcc.setExternalCorpId(externalCorpDB.getId());
                    externalCorpBankACCDao.save(externalCorpBankAcc);
                }
            }
        }
    }
    /**
     * 检测是否存在
     *
     * @param externalCorpBankAccList 账号的集合
     * @param externalCorpBankAccDTO  账号对象
     * @return
     */
    private boolean checkExternalCorpBankAccExist(List<ExternalCorpBankAcc> externalCorpBankAccList, ExternalCorpBankAccDTO externalCorpBankAccDTO) {
        for (ExternalCorpBankAcc externalCorpBankAcc : externalCorpBankAccList) {
            if (externalCorpBankAcc.getExternalAcc() != null && externalCorpBankAcc.getExternalAcc().equals(externalCorpBankAccDTO.getExternalAcc())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public ExternalCorpDTO findByCorpIdAndName(String corpId, String name) {
        ExternalCorp externalCorpDB = externalCorpDao.findByCorpIdAndName(corpId, name);
        if (externalCorpDB != null) {
            ExternalCorpDTO externalCorpDTO = BeanUtils.copyNewInstanceProperties(externalCorpDB, ExternalCorpDTO.class);
            return externalCorpDTO;
        }
        return null;
    }

    @Override
    public ExternalCorpDTO getByCorpIdAndCode(String code) {
        ExternalCorp externalCorpDB = externalCorpDao.getByCorpIdAndCode(code);
        if (externalCorpDB != null) {
            ExternalCorpDTO externalCorpDTO = BeanUtils.copyNewInstanceProperties(externalCorpDB, ExternalCorpDTO.class);
            return externalCorpDTO;
        }
        return null;
    }

    /**
     * 根据客商表Id和客商银行账号查找客商账号信息
     *
     * @param externalCorpId
     * @param bankAcc
     * @return
     */
    @Override
    public ExternalCorpBankAccDTO getExternalCorpBankAccByIdAndAcc(String externalCorpId, String bankAcc) {
        ExternalCorpBankAccVO vo = externalCorpService.getExternalCorpBankAccByIdAndAcc(externalCorpId, bankAcc);
        if (!EmptyUtils.isEmpty(vo)) {
            ExternalCorpBankAccDTO dto = new ExternalCorpBankAccDTO();
            BeanUtils.copyNotNullProperties(vo, dto);
            return dto;
        }
        return null;
    }
    /**
     * 根据客商银行账号集合查询客商账号信息，包括省市信息
     *
     * @param bankAccs 客商银行账号集合
     * @return 客商账号信息
     */
    @Override
    public Map<String, ExternalCorpBankAccDTO> getExternalCorpBankAccByAcc(List<String> bankAccs) {
        if (EmptyUtils.isEmpty(bankAccs)){
            return null;
        }
        List<ExternalCorpBankAccDTO> externalCorpBankAccDTOList = externalCorpBankACCDao.getBankAccByInfoAccs(bankAccs);
        if (EmptyUtils.isEmpty(externalCorpBankAccDTOList)) {
            return null;
        }
        Map<String, ExternalCorpBankAccDTO> map = new HashMap<>();
        for (ExternalCorpBankAccDTO dto : externalCorpBankAccDTOList) {
            map.put(dto.getExternalAcc(), dto);
        }
        return map;
    }
    /**
     * 根据id集合获取客商
     *
     * @param externalCorpIds
     * @return
     */
    public List<ExternalCorpDTO> getExternalCorpVoListByIds(List<String> externalCorpIds) {
        if (StringUtils.isNotEmpty(externalCorpIds)) {
            return null;
        }
        List<ExternalCorp> externalCorpList = externalCorpDao.getExternalCorpByIds(externalCorpIds);
        if (EmptyUtils.isEmpty(externalCorpList)) {
            return null;
        }
        List<ExternalCorpDTO> externalCorpDTOList = BeanUtils.copyListProperties(externalCorpList, ExternalCorpDTO.class);
        return externalCorpDTOList;
    }
    /**
     * 根据id集合获取客商Map
     *
     * @param externalCorpIds
     * @return
     */
    public Map<String,ExternalCorpDTO> getExternalCorpVoMapByIds(List<String> externalCorpIds) {
        if (StringUtils.isNotEmpty(externalCorpIds)) {
            return null;
        }
        List<ExternalCorp> externalCorpList = externalCorpDao.getExternalCorpByIds(externalCorpIds);
        if (EmptyUtils.isEmpty(externalCorpList)) {
            return null;
        }
        List<ExternalCorpDTO> externalCorpDTOList = BeanUtils.copyListProperties(externalCorpList, ExternalCorpDTO.class);
        Map<String,ExternalCorpDTO> map=new LinkedHashMap<>();
        for(ExternalCorpDTO vo:externalCorpDTOList){
            map.put(vo.getId(),vo);
        }
        return map;
    }
    /**
     * 根据id获取客商
     *
     * @param externalCorpId
     * @return
     */
    public ExternalCorpDTO getExternalCorpVoById(String externalCorpId) {
        List<String> idList=new ArrayList<>();
        idList.add(externalCorpId);
        List<ExternalCorp> list = this.externalCorpDao.getExternalCorpByIds(idList);
        if (EmptyUtils.isEmpty(list)) {
            return null;
        }
        ExternalCorp vo = list.get(0);
        if (!EmptyUtils.isEmpty(vo)) {
            ExternalCorpDTO dto = new ExternalCorpDTO();
            BeanUtils.copyNotNullProperties(vo, dto);
            return dto;
        }
        return null;
    }
    public IPageResult<ExternalCorpBankAccDetailDto> listByExternalDetail(DataTablesInput dataTablesInput) {
        Map<String, Object> params = dataTablesInput.getFormdata();
        params.put("fex", "1");
        dataTablesInput.setFormdata(params);
        return externalCorpDao.listByExternalDetail(dataTablesInput,null);
    }


    public ExternalCorpBankAccDetailDto listByExternalDetailForBill(DataTablesInput dataTablesInput) {
        Map<String, Object> params = dataTablesInput.getFormdata();
        params.put("fex", "1");
        dataTablesInput.setFormdata(params);
        return (ExternalCorpBankAccDetailDto)externalCorpDao.listByExternalDetailForBill(dataTablesInput);
    }

    public List<ExternalCorpDTO> findExternalCorpLikeName(String name) {
        List<ExternalCorp> externalCorpList= externalCorpDao.findExternalCorpLikeName(name);
        if (EmptyUtils.isEmpty(externalCorpList)) {
            return null;
        }
        return BeanUtils.copyListProperties(externalCorpList, ExternalCorpDTO.class);
    }

    public boolean queryExternalCorpAndCorpId(String externalCorpId,String corpId) {
        //查询客商共享的单位
        List<Map> maps = this.externalCorpDao.queryExternalCorpAndCorpId(externalCorpId);
        List<String> corpIdList = maps.stream().map(map -> map.get("CORP_ID").toString()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(maps)){
            return true;
        } else {
            if (corpIdList.contains(corpId)){
                return false;
            }else {
                return true;
            }
        }
    }

    @Override
    public List<ExternalCorpDTO> queryAll() {
        List<ExternalCorp> externalCorpList = externalCorpDao.queryAll();
        List<ExternalCorpDTO> externalCorpDTOList = BeanUtils.copyListProperties(externalCorpList, ExternalCorpDTO.class);
        for (ExternalCorpDTO externalCorpDTO : externalCorpDTOList) {
            List<ExternalCorpBankAcc> externalCorpBankAccList = externalCorpBankACCDao.listBankAccByExternalCorpId(externalCorpDTO.getId());
            List<ExternalCorpBankAccDTO> externalCorpBankAccDTOList = BeanUtils.copyListProperties(externalCorpBankAccList, ExternalCorpBankAccDTO.class);
            externalCorpDTO.setExternalCorpBankAccList(externalCorpBankAccDTOList);
        }
        return externalCorpDTOList;
    }


    @Override
    public ExternalCorpBankAccDTO queryExternalCorpAndAccInfoDTO(FbsBudgetDetailDTO budgetDetail) {
        ExternalCorpBankAccDTO externalCorpBankAccDTO = externalCorpBankACCDao.findAccInfoAndBankByExternalAcc(budgetDetail);
        return externalCorpBankAccDTO;
    }

    @Override
    public List<ExternalCorpDTO> queryInId(List<String> ids){
        return externalCorpDao.queryInId(ids);
    }

    @Override
    public Map<String, ExternalCorpDTO> queryFexExternalCorpMapByIds(List<String> ids) {
        List<ExternalCorpDTO> list = externalCorpDao.queryFexExternalCorpByIds(ids);
        if (EmptyUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<String, ExternalCorpDTO> map = new HashMap<>();
        for (ExternalCorpDTO dto : list) {
            map.put(dto.getId(), dto);
        }
        return map;
    }

    /**
     * 根据外汇交易对手数据源ID批量查询展示信息。
     *
     * @param ids 交易对手数据源ID集合
     * @return 以数据源ID为键的交易对手展示信息
     */
    @Override
    public Map<String, ExternalCorpDTO> queryFexCounterpartyMapByIds(List<String> ids) {
        List<ExternalCorpDTO> list = externalCorpDao.queryFexCounterpartyByIds(ids);
        if (EmptyUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<String, ExternalCorpDTO> map = new HashMap<>();
        for (ExternalCorpDTO dto : list) {
            if (dto != null && StringUtils.isNotBlank(dto.getId()) && !map.containsKey(dto.getId())) {
                map.put(dto.getId(), dto);
            }
        }
        return map;
    }

    /**
     * 根据名称模糊查询外汇统一交易对手。
     *
     * @param name 交易对手名称
     * @return 交易对手信息
     */
    @Override
    public List<ExternalCorpDTO> findFexCounterpartyLikeName(String name) {
        return externalCorpDao.findFexCounterpartyLikeName(name);
    }

    @Override
    public Map<String, ExternalCorpBankAccDTO> queryFexExternalCorpBankAccMapByIds(List<String> ids) {
        List<ExternalCorpBankAccDTO> list = externalCorpBankACCDao.queryFexExternalCorpBankAccByIds(ids);
        if (EmptyUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<String, ExternalCorpBankAccDTO> map = new HashMap<>();
        for (ExternalCorpBankAccDTO dto : list) {
            map.put(dto.getId(), dto);
        }
        return map;
    }

    @Override
    public List<ExternalCorpBankAccDTO> queryFexExternalCorpBankAccListByExternalCorpId(String externalCorpId) {
        List<ExternalCorpBankAccDTO> list = externalCorpBankACCDao.queryFexExternalCorpBankAccByExternalCorpId(externalCorpId);
        return EmptyUtils.isEmpty(list) ? Collections.emptyList() : list;
    }

    @Override
    public ExternalCorpDTO getById(String id) {
        return externalCorpService.getById(id);
    }


    @Override
    public List<ExternalCorpDTO> queryInExternalType(List<String> externalType){
        return externalCorpDao.queryInExternalType(externalType);
    }


    /**
     * 根据客商ID和客商名称判断是否需要新增客商信息
     *
     * @param externalCorpDTO
     */
    @Transactional
    @Override
    public void updateExternalCorpInfo(ExternalCorpDTO externalCorpDTO, ExternalCorpBankAccDTO bankAccDTO, ICurrentUser user) {
        //客商统一管理，不保存/更新客商资料信息
        String externalCorp = configServiceI.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue();
        if (CommonConstant.YES.equals(externalCorp)) {
            return;
        }
        if (StringUtils.isBlank(externalCorpDTO.getId())) {
            return;
        }
        ExternalCorp externalCorpDB = externalCorpService.get(externalCorpDTO.getId(), true);
        // 网点/单位选择传入的是单位ID，不对应客商主数据，不能按客商更新。
        if (externalCorpDB == null) {
            return;
        }
        if (StringUtils.isNotBlank(externalCorpDTO.getProvince())) {
            externalCorpDB.setProvince(externalCorpDTO.getProvince());
        }
        if (StringUtils.isNotBlank(externalCorpDTO.getCity())) {
            externalCorpDB.setCity(externalCorpDTO.getCity());
        }
        if (StringUtils.isNotBlank(externalCorpDTO.getAddress())) {
            externalCorpDB.setAddress(externalCorpDTO.getAddress());
        }
        if (externalCorpDTO.getIsNative() != null) {
            externalCorpDB.setIsNative(externalCorpDTO.getIsNative());
        }
        externalCorpService.updateEntity(externalCorpDB);

        //根据客户银行账号判断数据库是否已经存在
        ExternalCorpBankAccVO externalCorpBankAccVO = externalCorpService.getExternalCorpBankAccByIdAndAcc(externalCorpDB.getId(), bankAccDTO.getExternalAcc());
        if (externalCorpBankAccVO != null && StringUtils.isNotBlank(externalCorpBankAccVO.getId())) {
            ExternalCorpBankAcc externalCorpBankAccDB = externalCorpBankACCDao.get(externalCorpBankAccVO.getId(), true);
            externalCorpBankAccDB.setBankCountryId(bankAccDTO.getBankCountryId());
            externalCorpBankAccDB.setSwiftCode(bankAccDTO.getSwiftCode());
            externalCorpBankAccDB.setBankAddr(bankAccDTO.getBankAddr());
            externalCorpBankACCDao.update(externalCorpBankAccDB);
        }
    }

    /**
     *
     * 根据客商账号查询账号信息
     *
     * @param externalAcc
     * @return
     */
    @Override
    public ExternalCorpBankAccDTO queryByBankAcc(String externalAcc) {
        ExternalCorpBankAccVO externalCorpBankAccVO = externalCorpBankACCDao.queryByBankAcc(externalAcc);
        ExternalCorpBankAccDTO externalCorpBankAccDTO = new ExternalCorpBankAccDTO();
        if (null != externalCorpBankAccVO) {
            BeanUtil.copyProperties(externalCorpBankAccVO, externalCorpBankAccDTO);
        }
        return externalCorpBankAccDTO;
    }


    @Override
    public List<String> getCorpIdsByExternalCorpId(String externalId) {
        return externalCorpAndCorpDao.getCorpIdsByExternalCorpId(externalId);
    }
}
