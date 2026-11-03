package com.bytter.biz.finacial.tally.service.impl;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.biz.finacial.tally.dao.*;
import com.bytter.biz.finacial.tally.entity.*;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.RegionDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 记账平台核算接口实现类
 *
 * @author huangyl
 */
@Service("TallyServiceImpl")
@DubboService
@Transactional
public class TallyServiceImpl implements ITallyService {
    @Autowired
    private BtNetChalkDao btNetChalkDao;
    @Autowired
    private FcBankAccDtlDao fcBankAccDtlDao;
    @Autowired
    private FcUnionAccbookDao unionAccBookDao;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IFcVoucherTypeService voucherTypeService;
    @DubboReference
    private IRegionService regionService;
    @DubboReference
    private IBisAccDtlService dtlService;
    @DubboReference(check = false)
    private IFcSubjectService fcSubjectService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @Autowired
    private VoucherImportDao voucherImportDao;
    @Autowired
    private FinacialTallyRemindDao remindDao;
    @Autowired
    private FcManualFinacialColumnDao columnDao;
    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;
    @DubboReference(check = false)
    private IConfigService configService;

    @Override
    public DataTablesOutput<BtNetChalkDTO> queryBtNetChalk(DataTablesInput dataTablesInput, String type, String userId, List<String> corpAuth) {
//        List<BtNetChalkDTO> chalkList = btNetChalkDao.listQueryChalk(dataTablesInput, type, userId, corpAuth);
//        if (!CollectionUtils.isEmpty(chalkList)) {
//            List<String> oppAccNoList = chalkList.stream().map(BtNetChalkDTO::getOppAccNo).collect(Collectors.toList());
//            List<BtBankAccDTO> accList = bankAccService.listByIds(null, oppAccNoList);
//            List<String> corpIds = accList.stream().map(BtBankAccDTO::getCorpId).collect(Collectors.toList());
        DataTablesOutput<BtNetChalkDTO> data = btNetChalkDao.queryBtNetChalk(dataTablesInput, type, userId, corpAuth, null);
        if (!CollectionUtils.isEmpty(data.getData())) {
            List<String> queryCorpIds = data.getData().stream().map(BtNetChalkDTO::getCorpId).collect(Collectors.toList());
            List<String> curIds = data.getData().stream().map(BtNetChalkDTO::getCurId).collect(Collectors.toList());
            Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(queryCorpIds);
            Map<String, BtCurrencyDTO> curMap = currencyService.getCurrencyByIds(curIds);
            data.getData().stream().forEach(item -> {
                if (corpMap.containsKey(item.getCorpId())) {
                    item.setCorpName(corpMap.get(item.getCorpId()).getName());
                }
                if (curMap.containsKey(item.getCurId())) {
                    item.setCurName(curMap.get(item.getCurId()).getCurName());
                }
            });
        }
        return data;
//        }
//        return new DataTablesOutput<>();
    }

    @Override
    public BtNetChalkDTO queryBtNetChalkDTO(String id) {
        return btNetChalkDao.queryBtNetChalkDTO(id);
    }

    @Override
    public int updateEditAutoCheckSign(BtNetChalkDTO entity, List<String> idList) {
        return btNetChalkDao.updateEditAutoCheckSign(entity, idList);
    }

    @Override
    public int updateEditPassSign(List<String> idList, String newEState) {
        return btNetChalkDao.updateEditPassSign(idList, newEState);
    }

    @Override
    public int updateAutoCheckSign(BtNetChalkDTO entity, List<String> idList) {
        return btNetChalkDao.updateAutoCheckSign(entity, idList);
    }

    @Override
    public int updateTallySign(BtNetChalkDTO entity, List<String> idList, String eState) {
        return btNetChalkDao.updateTallySign(entity, idList, eState);
    }

    @Override
    public int updateEditBackSign(String id, String eState) {
        return btNetChalkDao.updateEditBackSign(id, eState);
    }

    @Override
    public int updateCheckBackSign(String id) {
        return btNetChalkDao.updateCheckBackSign(id);
    }

    @Override
    public int updateCheckSign(BtNetChalkDTO entity, List<String> idList) {
        return btNetChalkDao.updateCheckSign(entity, idList);
    }

    @Override
    public List<BtNetChalkDTO> queryTallyData(String booleanFalse) {
        //获取数据
        List<BtNetChalkDTO> btNetChalks = btNetChalkDao.queryTallyData(booleanFalse, null);
        //获取对账数据后的账号id集合
        List<String> bankAccIds = btNetChalks.stream().map(BtNetChalkDTO::getBankAccId).collect(Collectors.toList());
        //根据账号id集合获取账号信息
        List<BtBankAccDTO> bankAccList = bankAccService.listByIds(bankAccIds, null);
        //获取单位id，排除资金调拨数据
        List<String> corpIds = bankAccList.stream().map(BtBankAccDTO::getCorpId).collect(Collectors.toList());
        return btNetChalkDao.queryTallyData(booleanFalse, corpIds);
    }

    @Override
    public List<BtNetChalkDTO> queryTallyData(String booleanFalse,String num) {
        StringBuilder configSql = new StringBuilder();
        String uses = configService.getConfig("fcl", "receiveDtlUses").getValue(); //付款流水用途包含
        String remark = configService.getConfig("fcl", "receiveDtlRemark").getValue(); //付款流水摘要包含
        String postscript = configService.getConfig("fcl", "receiveDtlPostscript").getValue(); //付款流水附言包含
        configSql.append(" (1=1");
        if(StringUtils.isNotBlank(uses)){ //用途
            uses = uses.replace("，",",");
            String[] split = uses.split(",");
            configSql.append(" and (vbn.purpose is null or (1=1 ");
            for (String s : split) {
                configSql.append(" and vbn.purpose not like '%").append(s).append("%'");
            }
            configSql.append(" )) ");
        }
        if(StringUtils.isNotBlank(remark)){ //摘要
            remark = remark.replace("，",",");
            String[] split = remark.split(",");
            configSql.append(" and (vbn.remark is null or (1=1 ");
            for (String s : split) {
                configSql.append(" and vbn.remark not like '%").append(s).append("%'");
            }
            configSql.append(" )) ");
        }
        if(StringUtils.isNotBlank(postscript)){ //附言
            postscript = postscript.replace("，",",");
            String[] split = postscript.split(",");
            configSql.append(" and (vbn.postscript is null or (1=1 ");
            for (String s : split) {
                configSql.append(" and vbn.postscript not like '%").append(s).append("%'");
            }
            configSql.append(" )) ");
        }
        configSql.append(" )");
        return btNetChalkDao.queryTallyData(booleanFalse, configSql.toString(),num);
    }

    @Override
    public BtNetChalkDTO queryBtNetChalk(String id) {
        return btNetChalkDao.queryBtNetChalkDTO(id);
    }

    @Override
    public void updateBtNetChalkEntity(BtNetChalkDTO btNetChalk) {
        BtNetChalk netChalk = new BtNetChalk();
        BeanUtils.copyNotNullProperties(btNetChalk, netChalk);
        btNetChalkDao.update(netChalk);
    }

    @Override
    public BtNetChalkDTO getNeChalkDtoById(String id) {
        BtNetChalk netChalk = btNetChalkDao.get(id);
        BtNetChalkDTO netChalkDto = new BtNetChalkDTO();
        BeanUtils.copyNotNullProperties(netChalk, netChalkDto);
        return netChalkDto;
    }

    @Override
    public void updateAutoTallySign(String id, String tallySign) {
        btNetChalkDao.updateAutoTallySign(id, tallySign);
    }

    @Override
    public Integer verifyCorpDisableByCorpId(String corpId) {
        return btNetChalkDao.verifyCorpDisableByCorpId(corpId);
    }

    @Override
    public List<FcAccbookDtlDTO> getBookDtlVo(String bookId) {
        return btNetChalkDao.getBookDtlVo(bookId);
    }

    @Override
    public void saveUnionAccBook(FcUnionAccbookDTO unionAccBookDto) {
        FcUnionAccbook fcUnion = new FcUnionAccbook();
        BeanUtils.copyNotNullProperties(unionAccBookDto, fcUnion);
        unionAccBookDao.save(fcUnion);
    }

    @Override
    public void updateUnionAccEntity(FcUnionAccbookDTO unionAccBookDto) {
        FcUnionAccbook fcUnion = new FcUnionAccbook();
        BeanUtils.copyNotNullProperties(unionAccBookDto, fcUnion);
        unionAccBookDao.merge(fcUnion);
    }

    @Override
    public List<FcUnionAccbookDTO> queryFcUnionAccBook(String id) {
        return unionAccBookDao.queryFcUnionAccBook(id);
    }

    @Override
    public DataTablesOutput<FcUnionAccbookDTO> queryFcUnionAccBook(DataTablesInput dataTablesInput, String type, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.FCUNIONACCBOOK_FCUNIONACCBOOK_APPLY, userId);
        DataTablesOutput<FcUnionAccbookDTO> unionAccBookList = unionAccBookDao.queryFcUnionAccBook(dataTablesInput, type, userId, corpAuth);
        setFcUnionAccBookData(unionAccBookList.getData());
        return unionAccBookList;
    }

    @Override
    public List<FcUnionAccbookDTO> getUnionAccBookBySendNo(String sendVoucherNo) {
        return unionAccBookDao.getUnionAccBookBySendNo(sendVoucherNo);
    }

    @Override
    public void updateUnionAccBookBillState(String id, String billState, String oldBillState) {
        unionAccBookDao.updateUnionAccBookBillState(id, billState, oldBillState);
    }

    @Override
    public List<FcUnionAccbookDTO> queryTallyFcUnionAccBook() {
        List<FcUnionAccbookDTO> unionAccBookList = unionAccBookDao.queryTallyFcUnionAccBook();
        setFcUnionAccBookData(unionAccBookList);
        return unionAccBookList;
    }

    public DataTablesOutput<BisAccDtlCopyDTO> queryBisAccDtl(DataTablesInput input, String menuType, List<String> corpAuth) {
        DataTablesOutput<BisAccDtlDTO> query = fcBankAccDtlDao.queryBisAccDtl(input, menuType, corpAuth);
        DataTablesOutput<BisAccDtlCopyDTO> output = new DataTablesOutput<>();
        BeanUtils.copyNotNullProperties(output, query);
        output.setData(BeanUtils.copyListProperties(query.getData(), BisAccDtlCopyDTO.class));
        return output;
    }

    public DataTablesOutput<BisAccDtlCopyDTO> queryFcBankAccDtl(DataTablesInput input, String type, String menuType, String id, List<String> corpAuth) {
        //获取银行账号
        List<BtBankAccDTO> accDtoList = bankAccService.listByFcBankAccDtl(corpAuth);
        //获取明细
        if (!CollectionUtils.isEmpty(accDtoList)) {
            List<String> bankAccList = new ArrayList<>();
            List<String> oppAccNoList = new ArrayList<>();
            accDtoList.forEach(item -> {
                bankAccList.add(item.getBankAcc());
                oppAccNoList.add(item.getOppAccNo());
            });
            List<BisAccDtlDTO> fcAccDtl = fcBankAccDtlDao.listByBankAccOrOppAcc(bankAccList, oppAccNoList, type, id);
            if (!CollectionUtils.isEmpty(fcAccDtl)) {
                List<String> dtlIds = fcAccDtl.stream().map(BisAccDtlDTO::getAccDtlId).collect(Collectors.toList());
                DataTablesOutput<BisAccDtlDTO> query = dtlService.queryFcBankAccDtl(input, menuType, dtlIds);
                if (!CollectionUtils.isEmpty(query.getData())) {
                    Map<String, BtBankAccDTO> accMap = accDtoList.stream().collect(Collectors.toMap(BtBankAccDTO::getBankAcc, Function.identity()));
                    Map<String, BisAccDtlDTO> fcBankDtlMap = fcAccDtl.stream().collect(Collectors.toMap(BisAccDtlDTO::getAccDtlId, Function.identity()));
                    query.getData().forEach(item -> {
                        if (accMap.containsKey(item.getBankAcc())) {
                            item.setCorpName(accMap.get(item.getBankAcc()).getCorpName());
                            item.setCurName(accMap.get(item.getBankAcc()).getCurName());
                        }
                        if (fcBankDtlMap.containsKey(item.getAccDtlId())) {
                            item.setId(fcBankDtlMap.get(item.getAccDtlId()).getId());
                            item.setTallySign(fcBankDtlMap.get(item.getAccDtlId()).getTallySign());
                        }
                    });
                }
                DataTablesOutput<BisAccDtlCopyDTO> output = new DataTablesOutput<>();
                BeanUtils.copyNotNullProperties(output, query);
                output.setData(BeanUtils.copyListProperties(query.getData(), BisAccDtlCopyDTO.class));
                return output;
            }
        }
        return new DataTablesOutput<>();
    }

    @Override
    public FcBankAccDtlDTO getFcBankAccDtl(String key) {
        FcBankAccDtl dtl = fcBankAccDtlDao.get(key, true);
        FcBankAccDtlDTO dto = new FcBankAccDtlDTO();
        if (null != dtl) {
            BeanUtils.copyNotNullProperties(dtl, dto);
        }
        return dto;
    }

    @Override
    public FcBankAccDtlDTO findByDtlId(String id) {
        return fcBankAccDtlDao.findByDtlId(id);
    }

    @Override
    public void fcBankAccDtlSaveOrUpdate(FcBankAccDtlDTO entity) {
        FcBankAccDtl dtl = new FcBankAccDtl();
        BeanUtils.copyNotNullProperties(entity, dtl);
        fcBankAccDtlDao.saveOrUpdate(dtl);
    }

    @Override
    public FcBankAccDtlDTO findByOrderNo(String orderNo) {
        return fcBankAccDtlDao.findByOrderNo(orderNo);
    }

    @Override
    public void updateFcBankAccDtlEntity(FcBankAccDtlDTO entity) {
        FcBankAccDtl dtl = new FcBankAccDtl();
        BeanUtils.copyNotNullProperties(entity, dtl);
        fcBankAccDtlDao.update(dtl);
    }

    @Override
    public DataTablesOutput<FcAccbookImportDTO> voucherImpDatatable(DataTablesInput dataTablesInput, String userId) {
        DataTablesOutput<FcAccbookImportDTO> output = voucherImportDao.voucherImpDatatable(dataTablesInput, userId);
        //voucherType = fc_voucher_type 的 id
        List<FcAccbookImportDTO> data = output.getData();
        List<String> voucherIds = data.stream().map(FcAccbookImportDTO::getVoucherType).collect(Collectors.toList());
        List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(voucherIds);
        Map<String, String> voucherMap = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
        if (ObjectUtils.isNotEmpty(voucherMap)) {
            data.forEach(a -> {
                a.setVoucherName(voucherMap.get(a.getVoucherType()));
            });
        }

        return output;
    }

    @Override
    public void deleteImportVoucher(List<String> ids) {
        voucherImportDao.deleteImportVoucher(ids);
    }

    @Override
    public void batchSaveFcAccBookImport(List<FcAccbookImportDTO> importlist) {
        List<FcAccbookImport> list = BeanUtils.copyListProperties(importlist, FcAccbookImport.class);
        voucherImportDao.batchSave(list);
    }

    @Override
    public List<FcAccbookImportDTO> getImprotVoucher(List<String> ids) {
        return voucherImportDao.getImprotVoucher(ids);
    }

    public List<BisAccDtlCopyDTO> queryTallyBisAccDtl(String menuType) {
        List<BisAccDtlDTO> dtlList = fcBankAccDtlDao.queryTallyBisAccDtl(menuType);
        return BeanUtils.copyListProperties(dtlList, BisAccDtlCopyDTO.class);
    }

    @Override
    public void batchSaveRemindDtl(List<FcTallyRemindDtlDTO> remindDtls) {
        List<FcTallyRemindDtl> remindDtlList = BeanUtils.copyListProperties(remindDtls, FcTallyRemindDtl.class);
        remindDao.batchSave(remindDtlList);
    }

    @Override
    public void deleteRemindDtlByBusId(String busId) {
        remindDao.deleteByBusId(busId);
    }

    @Override
    public void createSQLQueryFinacialColumn(String toString, Map<String, Object> params) {
        columnDao.createSQLQuery(toString, params).executeUpdate();
    }

    @Override
    public FcManualFinacialTypeDTO getTypeBysysCodeAndFlow(String sysRaceCode, String flowType) {
        return columnDao.getTypeBysysCodeAndFlow(sysRaceCode, flowType);
    }

    @Override
    public List<FcManualFinacialColumnDTO> listColumnVoBySysRaceCode(String id) {
        return columnDao.listColumnVoBySysRaceCode(id);
    }

    @Override
    public DataTablesOutput<FcManualFinacialTypeDTO> getDataTablesAllType(DataTablesInput dataTablesInput, ICurrentUser user) {
        return columnDao.getDataTablesAllType(dataTablesInput, user);
    }

    @Override
    public FcManualFinacialTypeDTO getType(String id) {
        return columnDao.getType(id);
    }

    @Override
    public void saveOrUpdateManualFinacial(FcManualFinacialTypeDTO type) {
        FcManualFinacialType fcManualFinacialType = new FcManualFinacialType();
        BeanUtils.copyProperties(type, fcManualFinacialType);
        columnDao.saveOrUpdate(fcManualFinacialType);
    }

    @Override
    public void deleteColumnByTypeId(String id) {
        columnDao.deleteColumnByTypeId(id);
    }

    @Override
    public void batchSaveManualFinacialColumn(List<FcManualFinacialColumnDTO> list) {
        columnDao.batchSave(list);
    }

    @Override
    public void deleteTypeById(String id) {
        columnDao.deleteTypeById(id);
    }

    /**
     * 设置其他域的字段数据
     *
     * @param unionAccBookList 网点往来账数据集合
     * @return 网点往来账数据集合
     */
    private void setFcUnionAccBookData(List<FcUnionAccbookDTO> unionAccBookList) {
        if (!CollectionUtils.isEmpty(unionAccBookList)) {
            List<String> regionIds = unionAccBookList.stream().map(FcUnionAccbookDTO::getExternalCity).distinct().collect(Collectors.toList());
            List<String> list = new ArrayList<>();
            unionAccBookList.forEach(item -> {
                list.add(item.getSendNet());
                list.add(item.getReceiveNet());
            });
            List<String> corpIds = list.stream().distinct().collect(Collectors.toList());
            //获取单位数据信息
            Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpIds);
            Map<String, RegionDTO> regionMap = regionService.listByIds(regionIds);
            List<FcVoucherTypeDTO> validFcVoucherType = voucherTypeService.getValidFcVoucherType();
            Map<String, FcVoucherTypeDTO> voucherTypeMap = validFcVoucherType.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, Function.identity()));
            //设置省市名称
            unionAccBookList.forEach(item -> {
                //设置省市名称
                if (regionMap.containsKey(item.getExternalCity())) {
                    item.setProvInputName(regionMap.get(item.getExternalCity()).getProvName());
                    item.setCityName(regionMap.get(item.getExternalCity()).getName());
                }
                //设置付款方式
                if (voucherTypeMap.containsKey(item.getVoucherType())) {
                    item.setVoucherName(voucherTypeMap.get(item.getVoucherType()).getVoucherName());
                }
                //设置发送网点和接收网点信息
                if (corpMap != null && corpMap.containsKey(item.getSendNet())) {
                    item.setSendNetName(corpMap.get(item.getSendNet()).getName());
                }
                if (corpMap != null && corpMap.containsKey(item.getReceiveNet())) {
                    item.setReceiveNetName(corpMap.get(item.getReceiveNet()).getName());
                }
            });
        }
    }

    public List<FcBankAccDtlDTO> listFcBankAccDtlDTOByDtlIds(List<String> dtlIds, String type, String userId) {
        return fcBankAccDtlDao.listFcBankAccDtlDTOByDtlIds(dtlIds, type, userId);
    }

    @Override
    public List<FcBankAccDtlDTO> getFcBankAccDtlsByIds(List<String> ids) {
        return fcBankAccDtlDao.getFcBankAccDtlsByIds(ids);
    }

    @Override
    public List<BtNetChalkDTO> listBtNetChalkVo(List<String> idList) {
        return btNetChalkDao.listBtNetChalkVo(idList);
    }

    @Override
    public List<String> listByBookId(String bookId) {
        return btNetChalkDao.listByBookId(bookId);
    }

    @Override
    public BtNetChalkDTO queryBtNetChalkVo(String id) {
        return btNetChalkDao.queryBtNetChalkDTO(id);
    }

    @Override
    public boolean existCashSubject(List<String> accList) {
        if (EmptyUtils.isEmpty(accList) || accList.size() < 1) {
            return false;
        }
        List<com.bytter.system.api.dto.FcAccDTO> fcAccList = fcAccService.getAccSet(accList);
        if (EmptyUtils.isEmpty(fcAccList) || fcAccList.size() < 1) {
            return false;
        }
        List<String> subIds = fcAccList.stream().filter(fcAcc -> StringUtils.isNotBlank(fcAcc.getSubjectId())).map(com.bytter.system.api.dto.FcAccDTO::getSubjectId).distinct().collect(Collectors.toList());
        return fcSubjectService.checkCashSubject(subIds);
    }

    @Override
    public int mergeUpdateEditBackSign(List<String> idList) {
        return btNetChalkDao.mergeUpdateEditBackSign(idList);
    }

    @Override
    public int mergeUpdateCheckBackSign(List<String> idList) {
        return btNetChalkDao.mergeUpdateCheckBackSign(idList);
    }

    @Override
    public void updateBisAccDtlRecovery(String id) {
        btNetChalkDao.updateBisAccDtlRecovery(id);
    }
}
