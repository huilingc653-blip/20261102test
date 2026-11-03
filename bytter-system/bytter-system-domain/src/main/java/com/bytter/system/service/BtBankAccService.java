package com.bytter.system.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.common.rest.RestResponse;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.SysResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.system.api.vo.BtBankAccBalVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.dao.BtBankAccAppDao;
import com.bytter.system.dao.BtBankAccDao;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtBankAccApp;
import com.bytter.system.entity.BtBankAccCur;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
//import com.bytter.system.api.IBisAccBalService;
//import com.bytter.system.dao.BtBankAccAppDao;
//import com.bytter.system.vo.BtBankAccBalVO;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Service("btBankAccService")
@Transactional
public class BtBankAccService extends BaseService<BtBankAcc, String>
		implements  ITaskService, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private BtBankAccDao btBankAccDao;
    @Autowired
    private BtBankAccAppDao btBankAccAppDao;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private BtBankAccMessageService btBankAccMessageService;
    @Autowired
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private ILicenseService licenseService;
    @Autowired
    private IAccountGrantService accountGrantService;
    @Autowired
    private IBtBankAccQueryService btBankAccQueryService;
    @Autowired
    private BtBankAccCommonService btBankAccCommonService;
    @Autowired
    private IConfigService configServiceI;
    @Autowired
    private IBisAccAttributeService bisAccAttributeService;
    @Autowired
    private IBtAccNatureService btAccNatureService;
    @Autowired
    private IDictService dictServiceI;
    @Autowired
    private IBankTypeService bankTypeService;
    @Autowired
    private IMultiLangService multiLangServiceI;

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BtBankAcc entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(BtBankAcc entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BtBankAcc entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BtBankAcc entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BtBankAcc, String> getEntityDao() {
        return btBankAccDao;
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACC_QUERY, user.getId()), "acc.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        return btBankAccDao.getDataTablesUnion(dataTablesInput, sql.toString());
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(BtBankAccApp entity) {
        btBankAccDao.saveOrUpdate(entity);
    }
    /**
     * 根据单位id得到银行账号列表
     *
     * @param corpId
     * @return
     */
    public List<BtBankAcc> getListByCorpId(String corpId) {
        List<String> corpIds = new ArrayList<String>();
        corpIds.add(corpId);
        return this.getListByCorpIds(corpIds);
    }
    /**
     * 根据多个单位id得到银行账号列表
     *
     * @param corpIds
     * @return
     */
    public List<BtBankAcc> getListByCorpIds(List<String> corpIds) {
        return btBankAccDao.getListByCorpIds(corpIds);
    }
    /**
     * 根据VO所传的条件获取有效的银行账号集合
     */
    public List<BtBankAccVO> getListByVO(BtBankAccVO vo, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY, user.getId()), "b.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        return btBankAccDao.getListByVO(vo, sql);
    }
    /**
     * 根据VO所传的条件获取有效的银行账号集合
     */
    public RestResponse<BtBankAccVO> getPageListByVO(IParameterInput parameterInput, BtBankAccVO vo, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY, user.getId()), "b.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        IPageResult<BtBankAccVO> pageResult = btBankAccDao.getPageListByVO(parameterInput, vo, sql);
        return pageResult.convert(RestResponse.class);
    }
    /**
     * 获取存在子帐号的银行信息
     * zuoy 2017-06-22
     *
     * @return
     */
    public List<BtBankAccVO> getPrentBankAccList() {
        return btBankAccCommonService.listPrentBankAcc();
    }
    /**
     * 设置账户币别
     *
     * @param vo
     * @author zuoy 2018-02-28
     */
    public void setBtCurrency(BtBankAccVO vo) {
        if (EmptyUtils.isBlank(vo.getCurId())) {
            List<BtCurrencyDTO> list = btBankAccCommonService.listCurAccById(vo.getId());
            StringBuilder curName = new StringBuilder();
            StringBuilder curIds = new StringBuilder();
            for (BtCurrencyDTO currencyDTO : list) {
                if (EmptyUtils.isBlank(curIds.toString())) {
                    curIds.append(currencyDTO.getId());
                    curName.append(currencyDTO.getCurName());
                } else {
                    curIds.append(CommonConstant.COMMA_SPLIT).append(currencyDTO.getId());
                    curName.append(CommonConstant.COMMA_SPLIT).append(currencyDTO.getCurName());
                }
            }
            vo.setCurName(curName.toString());
            vo.setCurId(curIds.toString());
        } else {
            StringBuilder curName = new StringBuilder();
            StringBuilder curIds = new StringBuilder();
            for (String curId : vo.getCurId().split(",")) {
                BtCurrencyDTO currencyDTO = this.btCurrencyServiceI.getObjectById(curId);
                if (!EmptyUtils.isEmpty(currencyDTO)) {
                    if (EmptyUtils.isBlank(curIds.toString())) {
                        curIds.append(currencyDTO.getId());
                        curName.append(currencyDTO.getCurName());
                    } else {
                        curIds.append(CommonConstant.COMMA_SPLIT).append(currencyDTO.getId());
                        curName.append(CommonConstant.COMMA_SPLIT).append(currencyDTO.getCurName());
                    }
                }
            }
            vo.setCurName(curName.toString());
            vo.setCurId(curIds.toString());
        }
    }
    /**
     * 根据银行账号各个条件查询银行账号列表
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List<BtBankAccDTO> getBankAccList(BtBankAccDTO dto) {
        List<BtBankAcc> accList = btBankAccDao.getBankAccList(dto);
        if(CollectionUtil.isEmpty(accList)){
            return null;
        }
        List<BtBankAccDTO> dtoList = BeanUtils.copyListProperties(accList, BtBankAccDTO.class);
        for (int i = 0; i < accList.size(); i++) {
            List<String> curList = new ArrayList<String>();
            List<String> curNameList = new ArrayList<String>();
            List<Map<String, String>> counterAccList = new ArrayList<Map<String, String>>();
            List<BtBankAccCurDTO> btBankAccCurDTOList = new ArrayList<BtBankAccCurDTO>();
            if(CollectionUtil.isEmpty(accList.get(i).getCurList())){
                continue;
            }
            for (int j = 0; j < accList.get(i).getCurList().size(); j++) {
                if(accList.get(i).getCurList().get(j) == null || StrUtil.isBlank(accList.get(i).getCurList().get(j).getCurId())){
                    continue;
                }
                curList.add(accList.get(i).getCurList().get(j).getCurId());
                curNameList.add(btCurrencyServiceI.getObjectById(accList.get(i).getCurList().get(j).getCurId()).getCurName());
                Map<String, String> counterAccMap = new HashMap<String, String>();
                counterAccMap.put(accList.get(i).getCurList().get(j).getCurId(), accList.get(i).getCurList().get(j).getCounterAcc());
                counterAccList.add(counterAccMap);
                BtBankAccCurDTO curDTO = new BtBankAccCurDTO();
                BeanUtils.copyNotNullProperties(accList.get(i).getCurList().get(j), curDTO);
                btBankAccCurDTOList.add(curDTO);
            }
            dtoList.get(i).setCurList(curList);
            dtoList.get(i).setCurNameList(curNameList);
            dtoList.get(i).setCounterAccList(counterAccList);
            dtoList.get(i).setBtBankAccCurDTOList(btBankAccCurDTOList);
            if (StringUtils.isNotBlank(accList.get(i).getBankTypeId())) {
                BankTypeDTO bankType = bankTypeService.getObjectById(accList.get(i).getBankTypeId());
                if (!EmptyUtils.isEmpty(bankType)) {
                    dtoList.get(i).setBankTypeName(bankType.getTypeName());
                }
            }
            if (StringUtils.isNotBlank(accList.get(i).getCorpId())) {
                CorpDTO corp = corpServiceI.get(accList.get(i).getCorpId());
                if (!EmptyUtils.isEmpty(corp)) {
                    dtoList.get(i).setCorpName(corp.getName());
                }
            }
            // 设置SWIFT CODE
            if (CommonConstant.YES.equals(dtoList.get(i).getIsDomesticBank())) {
                dtoList.get(i).setSwiftCode(btBankAccCommonService.getSwiftCodeByBankCode(dtoList.get(i).getBankCode()));
            } else {
                dtoList.get(i).setSwiftCode(dtoList.get(i).getBankCode());
            }
        }
        Map<String, BtBankAccDTO> map = new HashMap<String, BtBankAccDTO>();
        for (int i = 0; i < dtoList.size(); i++) {
            if (!map.containsKey(dtoList.get(i).getId())) {
                map.put(dtoList.get(i).getId(), dtoList.get(i));
            }
        }
        List<BtBankAccDTO> tempList = new ArrayList<BtBankAccDTO>();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            tempList.add(map.get(it.next()));
        }
        return tempList;
    }
    /**
     * 根据银行账号各个条件和资源权限查询银行账号列表
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List<BtBankAccDTO> getBankAccList(AccountSearchDTO dto) {
        return accountGrantService.listDTONotUnionByMultiVal(dto.getUserId(), null, dto.getResource(), null, dto.getAccountType(), dto.getPermission());
    }
    /**
     * 根据银行账号各个条件查询银行账号列表(因为一个银行账号可以多币别，会有多条记录)
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List<BtBankAccDTO> getBankAccListAllNew(BtBankAccDTO dto) {
        // 根据条件查询结果
        List<BtBankAccDTO> accList = btBankAccDao.getBankAccDTOList(dto);
        //获取单位信息
        Map<String, CorpDTO> corpMap = corpServiceI.getCorpMap();
        //获取币种信息
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyServiceI.getCurrency();
        for (BtBankAccDTO btBankAccDTO : accList) {
            if(!EmptyUtils.isEmpty(corpMap)){
                if(corpMap.containsKey(btBankAccDTO.getCorpId())){
                    CorpDTO corpDTO = corpMap.get(btBankAccDTO.getCorpId());
                    if(!EmptyUtils.isEmpty(corpDTO)){
                        //设置单位名称
                        btBankAccDTO.setCorpName(corpDTO.getName());
                    }
                }
            }
            if(!EmptyUtils.isEmpty(currencyMap)){
                if(currencyMap.containsKey(btBankAccDTO.getCurId())){
                    BtCurrencyDTO btCurrencyDTO = currencyMap.get(btBankAccDTO.getCurId());
                    if(!EmptyUtils.isEmpty(btCurrencyDTO)){
                        //设置币别code
                        btBankAccDTO.setCurEnglishCode(btCurrencyDTO.getEnglishCode());
                    }
                }
            }
        }
        return accList;
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion1(DataTablesInput dataTablesInput) {
        return btBankAccDao.getDataTablesUnion1(dataTablesInput);
    }
    public DataTablesOutput<BtBankAccVO> getDataTablesUnion2(DataTablesInput dataTablesInput, String joinSql) {
        return btBankAccDao.getDataTablesUnion2(dataTablesInput, joinSql);
    }
    /**
     * 根据条件查询分页列表 离线账号，在线账号都可以显示
     *
     * @param dataTablesInput 表格封装信息
     * @return 表格封装信息
     */
    public DataTablesOutput getDataTablesUnionAll(DataTablesInput dataTablesInput) {
        return btBankAccDao.getDataTablesUnionAll(dataTablesInput);
    }
    /**
     * 根据idlist 得到银行账户列表
     *
     * @param idList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<BtBankAccDTO> getBankAccByIdList(List<String> idList) {
        if (EmptyUtils.isEmpty(idList)) {
            return new ArrayList<>();
        }
        /*List<BtBankAccVO> accList = btBankAccDao.getBankAccVOByIdList(idList);
        List<BtBankAccDTO> tempList = new ArrayList<BtBankAccDTO>();
        for (int i = 0; i < accList.size(); i++) {
            BtBankAccDTO tempDto = new BtBankAccDTO();
            accList.get(i).setBalance(bisAccBalService.getLastByBalDate(accList.get(i).getBankAcc(),accList.get(i).getCurId()));
            BeanUtils.copyNotNullProperties(accList.get(i), tempDto);
            tempList.add(tempDto);
        }*/
        //性能优化
        BtBankAccDTO bankAccDTO = new BtBankAccDTO();
        bankAccDTO.setIdList(idList);
        List<BtBankAccDTO> tempList = btBankAccDao.getBankAccDTOList(bankAccDTO);
        return tempList;
    }
    /**
     * 根据idlist 得到银行账户列表(不关联余额)
     *
     * @param idList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<BtBankAccDTO> getBankAccNotUnionByIdList(List<String> idList) {
        if (EmptyUtils.isEmpty(idList)) {
            return new ArrayList<>();
        }
        //性能优化
        BtBankAccDTO bankAccDTO = new BtBankAccDTO();
        bankAccDTO.setIdList(idList);
        List<BtBankAccDTO> tempList = btBankAccDao.getBankAccNotUnionByIdList(bankAccDTO);
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyServiceI.getCurrency();
        if (EmptyUtils.isNotEmpty(tempList)) {
            for (BtBankAccDTO bankAcc : tempList) {
                if (currencyMap.containsKey(bankAcc.getCurId())) {
                    bankAcc.setCurName(currencyMap.get(bankAcc.getCurId()).getCurName());
                }
            }
        }
        return tempList;
    }
    /**
     * 根据内部账号和币别，获取银行账号集合
     *
     * @param countAcc
     * @param curId
     * @return
     */
    public List<BtBankAccDTO> getBankAccByCountAcc(String countAcc, String curId) {
        List<BtBankAcc> accList = btBankAccDao.getBankAccByCountAcc(countAcc, curId);
        List<BtBankAccDTO> tempList = new ArrayList<BtBankAccDTO>();
        if (null != tempList) {
            tempList = BeanUtils.copyListProperties(accList, BtBankAccDTO.class);
        }
        return tempList;
    }
    /**
     * 根据联行号，获取银行账号集合
     *
     * @param bankCode
     * @return
     */
    public List<BtBankAccDTO> getBankAccByBankCode(String bankCode) {
        List<BtBankAcc> accList = btBankAccDao.getBankAccByBankCode(bankCode);
        List<BtBankAccDTO> tempList = new ArrayList<BtBankAccDTO>();
        if (null != tempList) {
            tempList = BeanUtils.copyListProperties(accList, BtBankAccDTO.class);
        }
        return tempList;
    }
    /**
     * 根据银行账号id和币别，获取对应的父账号信息
     *
     * @param bankAccId
     * @param curId
     * @return
     */
    public BtBankAccDTO getParentAccByAccId(String bankAccId, String curId) {
        BtBankAccCur bankAccCur = btBankAccCommonService.getBtBankAccCur(bankAccId, curId);
        BtBankAccDTO dto = null;
        if (!EmptyUtils.isEmpty(bankAccCur)) {
            BtBankAcc prentBankAcc = btBankAccDao.getBankAccByIdAndCurId(bankAccCur.getPrentAccId(), curId);
            if (EmptyUtils.isEmpty(prentBankAcc)) {
                return dto;
            }
            dto = new BtBankAccDTO();
            BeanUtils.copyNotNullProperties(prentBankAcc, dto);
            dto.setCurId(curId);
            dto.setCurName(btCurrencyServiceI.getObjectById(curId).getCurName());
            dto.setCorpName(this.corpServiceI.get(dto.getCorpId()).getName());
        }
        return dto;
    }
    /**
     * 根据条件得到银行账户列表
     * zuoy 2017-07-17
     *
     * @param accountSearchDTO
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<BtBankAccDTO> getBankAccByAccountDTO(AccountSearchDTO accountSearchDTO) {
        // 若为电票专户，则仅查询本位币   zuoy 2017-09-19  zg提出
        if (!EmptyUtils.isBlank(accountSearchDTO.getElectricBill())
                && BtBankAccConstant.ELECTRICBILL_YES.equalsIgnoreCase(accountSearchDTO.getElectricBill())) {
            BtCurrencyDTO btCurrencyDTO = this.btCurrencyServiceI.getNatualCurrency();
            if (!EmptyUtils.isEmpty(btCurrencyDTO)) {
                accountSearchDTO.setCurId(btCurrencyDTO.getId());
            }
        }
        List<BtBankAccDTO> tempList = new ArrayList<BtBankAccDTO>();
        //优化银行账户查询，先查询账号，再查询余额
        List<BtBankAccVO> accList = btBankAccDao.getBankAccByAccountDTO(accountSearchDTO);
        List<String> bankAccs = new ArrayList<>();
        List<String> curIds = new ArrayList<>();
        for (BtBankAccVO bankAccVO : accList) {
            BtCurrencyDTO btCurrencyDTO = btCurrencyServiceI.getObjectById(bankAccVO.getCurId());
            if (!EmptyUtils.isEmpty(btCurrencyDTO)) {
                bankAccVO.setCurName(btCurrencyDTO.getCurName());
                bankAccVO.setCurEnglishCode(btCurrencyDTO.getEnglishCode());
            }
            CorpDTO corpDTO = corpServiceI.get(bankAccVO.getCorpId());
            if (!EmptyUtils.isEmpty(corpDTO)) {
                bankAccVO.setCorpName(corpDTO.getName());
            }
            bankAccs.add(bankAccVO.getBankAcc());
            curIds.add(bankAccVO.getCurId());
        }
        //根据参数设置判断是否需要查询账户余额 //TODO屏蔽
        if (accountSearchDTO.getSearchBal() != null && accountSearchDTO.getSearchBal()) {
            //查询账户余额 //TODO
            List<BtBankAccBalVO> accBalList = btBankAccDao.queryBankAccBalanceById(bankAccs, curIds);
            Map<String, BigDecimal> accBalMap = new HashMap<>();
            for (BtBankAccBalVO accBalVO : accBalList) {
                String bankAcc = accBalVO.getBankAcc();
                String curId = accBalVO.getCurId();
                BigDecimal bal = accBalVO.getBal();
                accBalMap.put(bankAcc + "_" + curId, bal);
            }

            for (BtBankAccVO bankAccVO : accList) {
                String bankAcc = bankAccVO.getBankAcc();
                String curId = bankAccVO.getCurId();
                BigDecimal bal = accBalMap.get(bankAcc + "_" + curId);
                if (null == bal) {
                    bal = BigDecimal.ZERO;
                }
                bankAccVO.setBalance(bal);
            }
        }
        for (int i = 0; i < accList.size(); i++) {
            BtBankAccDTO tempDto = new BtBankAccDTO();
            BeanUtils.copyNotNullProperties(accList.get(i), tempDto);
            tempList.add(tempDto);
        }
        return tempList;
    }
    /**
     * 打回
     *
     * @param id
     */
    public void back(String id, ICurrentUser user) {
        BtBankAccApp btbankaccapp = this.btBankAccDao.getBtbankAccApp(id);
        btbankaccapp.setStatus(BtBankAccConstant.STATUE_BACK);
        this.saveOrUpdate(btbankaccapp);
        //生成日志
        btBankAccMessageService.saveMessage(btbankaccapp.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_BACK, "", BtBankAccMessageConstant.BUS_TYPE_APP, user);
        // 消息提醒
        notificationService.complete(NotificationConstant.NETBANK.BANKACC_REQ_HANDLE, id, user.getId());
        notificationService.sendToUserByLoginName(btbankaccapp.getId(), btbankaccapp.getCreateBy(), NotificationConstant.NETBANK.BANKACC_REQ, btbankaccapp);
    }
    /**
     * 银行账号数量是否超过了license允许的数量
     *
     * @return
     */
    public boolean isBtBankAccCountLicense() {
        boolean result = false;
        // 得到有效银行账号数量
        Integer bankAccCount = 0;
        List list = btBankAccCommonService.listEffectiveBtBankAcc();
        Long countEffectiveBtBankAcc = btBankAccCommonService.countEffectiveBtBankAcc();
        if (!EmptyUtils.isEmptyList(list)) {
            bankAccCount = list.size();
        }
        if (!EmptyUtils.isEmpty(countEffectiveBtBankAcc)) {
            bankAccCount = countEffectiveBtBankAcc.intValue();
        }
        Integer licenseCount = licenseService.getBankAccountNumber();
        if (licenseCount == 0) {
            result = true;
        } else {
            if (licenseCount >= bankAccCount) {
                result = true;
            }
        }
        return result;
    }
    /**
     * 获取实体账号
     *
     * @param btBankAccApp
     * @return
     */
    public List<BtBankAccVO> getEntityAcc(BtBankAccApp btBankAccApp) {
        return btBankAccDao.getEntityAcc(btBankAccApp);
    }
    /**
     * 获取绑定账号
     *
     * @param btBankAccApp
     * @return
     */
    public List<BtBankAccVO> getBindingBankAcc(BtBankAccApp btBankAccApp) {
        return btBankAccDao.getBindingBankAcc(btBankAccApp);
    }
    /**
     * 根据账户或户名查询账户简略信息
     *
     * @param bankAcc
     * @param accName
     * @return
     * @author zuoy 2018-03-19
     */
    public List<BtBankAcc> listBtBankAccBriefInfoBy(String bankAcc, String accName) {
        return this.btBankAccDao.listBtBankAccBriefInfoBy(bankAcc, accName);
    }
    /**
     * 判断两个账号(或者户名)是否所属同一网点下
     *
     * @param accInfo1  账号或者户名
     * @param accInfo2  账号或者户名
     * @param isBankAcc 是否是账号 true-比较账户 false-比较户名
     * @return
     * @author zuoy 2018-03-19
     */
    public boolean isTheSameNetByAccInfo(String accInfo1, String accInfo2, boolean isBankAcc) {
        if (EmptyUtils.isBlank(accInfo1) || EmptyUtils.isBlank(accInfo2)) {
            return false;
        }
        List<BtBankAcc> corp1 = null;
        List<BtBankAcc> corp2 = null;
        if (isBankAcc) {
            corp1 = this.listBtBankAccBriefInfoBy(accInfo1, null);
            corp2 = this.listBtBankAccBriefInfoBy(accInfo2, null);
        } else {
            corp1 = this.listBtBankAccBriefInfoBy(null, accInfo1);
            corp2 = this.listBtBankAccBriefInfoBy(null, accInfo2);
        }
        if (EmptyUtils.isEmptyList(corp1) || EmptyUtils.isEmptyList(corp2)) {
            return false;
        }
        return this.corpServiceI.isTheSameNetByCorpId(corp1.get(0).getCorpId(), corp2.get(0).getCorpId());
    }
    /**
     * 修改银行流水截至时间
     *
     * @param bankAccId
     * @param curId
     * @param endDtlDate
     * @author zuoy 2018-04-04
     */
    public void updateEndDtlDate(String bankAccId, String curId, Date endDtlDate) {
        this.btBankAccDao.updateEndDtlDate(bankAccId, curId, endDtlDate);
    }
    /**
     * 获取最近一次银行流水截至时间
     *
     * @param bankAccId
     * @param curId
     * @author zuoy 2018-04-04
     */
    public Date getLastEndDtlDate(String bankAccId, String curId) {
        return this.btBankAccDao.getLastEndDtlDate(bankAccId, curId);
    }
    public DataTablesOutput<BtBankAccVO> getDataTablesUnionSYS(DataTablesInput dataTablesInput, ICurrentUser user) {
        return this.btBankAccQueryService.getDataTablesUnionSYS(dataTablesInput, user);
    }
    /**
     * 根据条件查找账户信息
     *
     * @param id
     * @param curId 币别ID
     * @return
     * @author zuoy 2018-05-29
     */
    public BtBankAcc getBankAccByIdAndCurId(String id, String curId) {
        return this.btBankAccDao.getBankAccByIdAndCurId(id, curId);
    }
    /**
     * 根据条件查找账户信息
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @return
     * @author zuoy 2018-05-29
     */
    public BtBankAcc getBankAccByBankAccAndCurId(String bankAcc, String curId) {
        return this.btBankAccDao.getBankAccByBankAccAndCurId(bankAcc, curId);
    }
    /**
     * 功能介绍 ： 根据父账号和子账号对应的内部账号取具有支出属性的子账号。<br>
     * <b>日期：</b>2018-05-19<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<BtBankAccDTO> getOutPayByParentAcc(String prentAccId, String curId, String counterAcc) {
        return btBankAccDao.getOutPayByParentAcc(prentAccId, curId, counterAcc);
    }
    /**
     * 获取下属账户单位集合
     *
     * @param prentAccId 父类账户ID
     * @return
     * @author zuoy 2018-06-12
     */
    public List<String> listChildCorpIdsByParent(String prentAccId) {
        return btBankAccDao.listChildCorpIdsByParent(prentAccId);
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableOldBankAcc(DataTablesInput dataTablesInput, ICurrentUser user) {
        DataTablesOutput output = btBankAccDao.getDataTableOldBankAcc(dataTablesInput, "");
        this.btBankAccCommonService.setCurName(output);
        return output;
    }
    /**
     * 定时任务
     * 账户到期日期提醒
     *
     * @param arguments
     * @author zuoy 2018-11-07
     */
    @Override
    @XxlJob("BtBankAccService")
    public void run(String arguments) {
//		银行账号为${bankAcc}在${endDate}过期，请及时办理续期或者注销手续！
        try {
            Date curDate = DateUtils.getDate();
            this.logger.info(curDate + "---启用账户到期提醒，开始执行定时任务---");
            String bankAccEndDateAlertDays = configServiceI.getConfig(ConfigConstant.BANKACC, BtBankAccConstant.BANK_ACC_ENDDATE_ALERT_DAYS).getValue();
            Integer alertDays = EmptyUtils.isBlank(bankAccEndDateAlertDays) ? 0 : Integer.parseInt(bankAccEndDateAlertDays);
            String searchDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(), alertDays), DateUtils.PATTERN_CLASSICAL_SIMPLE);
            List<BtBankAccVO> list = this.btBankAccDao.listBankAccByEndDate(searchDate);
            if (EmptyUtils.isEmptyList(list)) {
                this.logger.info(curDate + "---账户到期的数量为0---");
            } else {
                for (BtBankAccVO vo : list) {
                    // endDate为Date类型，Velocity渲染时会调用toString()显示为时间戳格式，此处格式化为yyyy-MM-dd
                    
                    Map<String, Object> params = BeanUtils.beanToMap(vo);
                    params.put("endDate", DateUtils.date2Str(vo.getEndDate(), DateUtils.date_sdf()));
                    notificationService.sendToUserByLoginName(vo.getId(), vo.getCreateBy(), NotificationConstant.NETBANK.BANKACC_ENDDATE, params);
                }
            }
            this.logger.info("---启用账户到期提醒，结束执行定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * <br>
     * <b>功能：</b>查询单位/网点可用余额（企e金服接口）<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-11-25<br>
     *
     * @param corpflag
     * @param corpCode
     * @return
     */
    public BigDecimal queryBankAccAvailableBal(String corpflag, String corpCode) {
        return this.btBankAccDao.queryBankAccAvailableBal(corpflag, corpCode);
    }
    /**
     * 获取资源下的单位ID
     *
     * @param resource 资源
     * @return
     * @author zuoy 2017-10-10
     */
    private List<String> getUserCorpIds(String resource, String userId) {
        // 获取资源下的单位ID
        List<String> userCorpIds = null;
        if (!EmptyUtils.isBlank(resource)) {
            userCorpIds = this.corpServiceI.getUserCorpIds(resource, userId);
        }
        return userCorpIds;
    }
    /**
     * 同步接口-批量增加
     *
     * @param list
     * @author zuoy 2019-01-24
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void syncBatchSave(List<BtBankAccDTO> list) {
        // 账户属性默认 综合户
        BisAccAttributeDTO bisAccAttribute = bisAccAttributeService.findByCode("02");
        // 账户性质默认 一般户
        BtAccNatureDTO btAccNature = btAccNatureService.findByCode("0004");
        for (BtBankAccDTO dto : list) {
            BtBankAcc btBankAcc = new BtBankAcc();
            BeanUtils.copyNotNullProperties(dto, btBankAcc);
            syncCheck(dto, btBankAcc);
            if (EmptyUtils.isBlank(dto.getAttributeId()) && !EmptyUtils.isEmpty(bisAccAttribute)) {
                btBankAcc.setAttributeId(bisAccAttribute.getId());
            }
            if (EmptyUtils.isBlank(dto.getNatureId()) && !EmptyUtils.isEmpty(btAccNature)) {
                btBankAcc.setNatureId(btAccNature.getId());
            }
            this.save(btBankAcc);
            if (!EmptyUtils.isEmptyList(dto.getBtBankAccCurDTOList())) {
                for (BtBankAccCurDTO curDTO : dto.getBtBankAccCurDTOList()) {
                    BtBankAccCur accCur = new BtBankAccCur();
                    BeanUtils.copyNotNullProperties(curDTO, accCur);
                    accCur.setBankAccId(btBankAcc.getId());
                    this.getEntityDao().save(accCur);
                }
            }
        }
    }
    /**
     * 同步接口-批量修改
     *
     * @param list
     * @author zuoy 2019-01-24
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void syncBatchUpdate(List<BtBankAccDTO> list) {
        for (BtBankAccDTO dto : list) {
            BtBankAcc btBankAcc = this.btBankAccDao.getByBankAcc(dto.getBankAcc());
            BeanUtils.copyNotNullProperties(dto, btBankAcc);
            this.update(btBankAcc);
            if (!EmptyUtils.isEmptyList(dto.getBtBankAccCurDTOList())) {
                for (BtBankAccCurDTO curDTO : dto.getBtBankAccCurDTOList()) {
                    BtBankAccCur accCur = this.btBankAccCommonService.getBtBankAccCur(btBankAcc.getId(), curDTO.getCurId());
                    if (accCur == null) {
                        accCur = new BtBankAccCur();
                        BeanUtils.copyNotNullProperties(curDTO, accCur);
                        accCur.setBankAccId(btBankAcc.getId());
                    } else {
                        BeanUtils.copyNotNullProperties(curDTO, accCur);
                    }
                    this.getEntityDao().saveOrUpdate(accCur);
                }
            }
        }
    }
    /**
     * 同步接口-批量删除
     *
     * @param list
     * @author zuoy 2019-01-24
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void syncBatchDelete(List<BtBankAccDTO> list) {
        for (BtBankAccDTO dto : list) {
            BtBankAcc btBankAcc = this.btBankAccDao.getByBankAcc(dto.getBankAcc());
            List<BtBankAccCur> curList = btBankAccCommonService.listBtBankAccCurById(btBankAcc.getId());
            if (!EmptyUtils.isEmptyList(curList)) {
                for (BtBankAccCur cur : curList) {
                    this.getEntityDao().delete(this.getEntityDao().merge(cur));
                }
            }
            this.delete(btBankAcc.getId());
        }
    }
    /**
     * 同步检查
     *
     * @param dto
     * @param btBankAcc
     */
    public void syncCheck(BtBankAccDTO dto, BtBankAcc btBankAcc) {
        // 校验账户性质
        if (!EmptyUtils.isBlank(dto.getNatureName())) {
            // 判断账户性质在系统中是否存在
            String natureId = btBankAccCommonService.getNatureId(dto.getNatureName());
            if (EmptyUtils.isEmpty(natureId)) {
//                throw new RuntimeException(String.format("账号为[%s],账户性质为[%s],不存在!", dto.getBankAcc(), dto.getNatureName()));
                throw new ParameterException(NetbankMessageEnum.NIS_ACC_NATURE_NULL, dto.getBankAcc(), dto.getNatureName());
            } else {
                btBankAcc.setNatureId(natureId);
            }
        }
        // 校验账户属性
        if (!EmptyUtils.isBlank(dto.getAttributeName())) {
            // 判断账户属性在系统中是否存在
            String attributeId = btBankAccCommonService.getAttributeId(dto.getAttributeName());
            if (EmptyUtils.isEmpty(attributeId)) {
//                throw new RuntimeException(String.format("账号为[%s],账户属性为[%s],不存在!", dto.getBankAcc(), dto.getNatureName()));
                throw new ParameterException(NetbankMessageEnum.NIS_ACC_ACCATTR_NULL, dto.getBankAcc(), dto.getNatureName());
            } else {
                btBankAcc.setAttributeId(attributeId);
            }
        }
        // 校验账户类别
        if (!EmptyUtils.isBlank(dto.getAccTypeName())) {
            // 判断账户类别在系统中是否存在
            String accType = dictServiceI.getCodeByName("btBankAcc.accType", dto.getAccTypeName());
            if (EmptyUtils.isEmpty(accType)) {
//                throw new RuntimeException(String.format("账号为[%s],账户属性为[%s],不存在!", dto.getBankAcc(), dto.getAccTypeName()));
                throw new ParameterException(NetbankMessageEnum.NIS_ACC_ACCATTR_NULL, dto.getBankAcc(), dto.getAccTypeName());
            } else {
                btBankAcc.setAccType(accType);
                // 账户类别为定期时，定期期限不能为空
                if (BtBankAccConstant.ACCTYPE_02.equalsIgnoreCase(accType)) {
                    if (EmptyUtils.isEmpty(btBankAcc.getRegulars())) {
//                        throw new RuntimeException(String.format("账号为[%s],账户类别为定期时，定期期限不能为空!", btBankAcc.getBankAcc()));
                        throw new ParameterException(NetbankMessageEnum.NIS_ACCTYPE_REGULAR_NULL, btBankAcc.getBankAcc());
                    } else {
                        String regulars = dictServiceI.getCodeByName("btBankAcc.Term", btBankAcc.getRegulars());
                        if (EmptyUtils.isBlank(regulars)) {
//                            throw new RuntimeException(String.format("账号为[%s],定期期限为[%s],不存在!", btBankAcc.getRegulars()));
                            throw new ParameterException(NetbankMessageEnum.NIS_ACC_REGULAR_NULL, btBankAcc.getRegulars());
                        }
                    }
                }
                // 账户类别为通知时，通知类型不能为空
                if (BtBankAccConstant.ACCTYPE_06.equalsIgnoreCase(accType)) {
                    if (EmptyUtils.isEmpty(btBankAcc.getNoticeType())) {
//                        throw new RuntimeException(String.format("账号为[%s],账户类别为通知时，通知类型不能为空!", btBankAcc.getBankAcc()));
                        throw new ParameterException(NetbankMessageEnum.NIS_ACCTYPE_NOTICE_NULL, btBankAcc.getBankAcc());
                    } else {
                        String noticeType = dictServiceI.getCodeByName("bankAcc.noticeType", btBankAcc.getNoticeType());
                        if (EmptyUtils.isBlank(noticeType)) {
//                            throw new RuntimeException(String.format("账号为[%s],通知类型为[%s],不存在!", btBankAcc.getRegulars()));
                            throw new ParameterException(NetbankMessageEnum.NIS_ACCTYPE_NOTICETYPE_NULL, btBankAcc.getRegulars(), noticeType);
                        }
                    }
                }
                // 账户类别为协定时，协定金额，协定利率，协定合同，协定开始日期，结束日期不能为空
                if (BtBankAccConstant.ACCTYPE_07.equalsIgnoreCase(accType)) {
                    if (EmptyUtils.isEmpty(btBankAcc.getAgrMoney())) {
//                        throw new RuntimeException(String.format("账号为[%s],账户类别为协定时，协定金额不能为空!", btBankAcc.getBankAcc()));
                        throw new ParameterException(NetbankMessageEnum.NIS_ACCTYPE_AGREEMENT_MONEY_NULL, btBankAcc.getBankAcc());
                    }
                    if (EmptyUtils.isEmpty(btBankAcc.getAgrInterest())) {
//                        throw new RuntimeException(String.format("账号为[%s],账户类别为协定时，协定利率不能为空!", btBankAcc.getBankAcc()));
                        throw new ParameterException(NetbankMessageEnum.NIS_ACCTYPE_AGREEMENT_INTEREST_RATE_NULL, btBankAcc.getBankAcc());
                    }
                    if (EmptyUtils.isEmpty(btBankAcc.getAgrContract())) {
//                        throw new RuntimeException(String.format("账号为[%s],账户类别为协定时，协定合同不能为空!", btBankAcc.getBankAcc()));
                        throw new ParameterException(NetbankMessageEnum.NIS_ACCTYPE_CONTRACT_AGREEMENT_NULL, btBankAcc.getBankAcc());
                    }
                    if (EmptyUtils.isEmpty(btBankAcc.getAgrBeginDate())) {
//                        throw new RuntimeException(String.format("账号为[%s],账户类别为协定时，协定开始日期不能为空!", btBankAcc.getBankAcc()));
                        throw new ParameterException(NetbankMessageEnum.NIS_ACCTYPE_START_DATE_AGREEMENT_NULL, btBankAcc.getBankAcc());
                    }
                    if (EmptyUtils.isEmpty(btBankAcc.getAgrEndDate())) {
//                        throw new RuntimeException(String.format("账号为[%s],账户类别为协定时，结束日期不能为空!", btBankAcc.getBankAcc()));
                        throw new ParameterException(NetbankMessageEnum.NIS_ACCTYPE_END_DATE_AGREEMENT_NULL, btBankAcc.getBankAcc());
                    }
                }
            }
        }
    }
    /**
     * @param @param  accountSearchDTO
     * @param @return 参数
     * @return List<AccountDTO>    返回类型
     * @throws
     * @Title: getRasBankListBy
     * @Description: TODO(增加稽查银行账户信息查询)
     * @author Jinyong.cao
     */
    public List<AccountDTO> getRasBankListBy(AccountSearchDTO accountSearchDTO, String local) {
        accountSearchDTO.setCurId(btCurrencyServiceI.getRMBCurrency().getId());
        List<String[]> accList = btBankAccDao.getBankRasAccByAccountDTO(accountSearchDTO);
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        if (null != accList && accList.size() > 0) {
            for (Object[] bankAccObj : accList) {
                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setBankAccId(bankAccObj[0].toString());
                accountDTO.setBankAcc(bankAccObj[1].toString());
                accountDTO.setAccName(bankAccObj[2].toString());
                accountDTO.setBankName(bankAccObj[3].toString());
                accountDTO.setEntityAccId(bankAccObj[4] == null ? null : bankAccObj[4].toString());
                accountDTO.setEntityAccNo(bankAccObj[5] == null ? null : bankAccObj[5].toString());
                accountDTO.setCurName(!StringUtils.isBlank(local) && local.equals("zh_CN") ? "人民币" : "RMB");
                list.add(accountDTO);
            }
        }
        return list;
    }
    /**
     * 查询正常状态离线账号列表（带分页自动生成余额专用）
     *
     * @param parameterInput
     * @return
     */
    public RestResponse<BtBankAccDTO> bankAccResult(IParameterInput parameterInput) {
        IPageResult<BtBankAccDTO> bankAccNote = btBankAccDao.getBankAccListByIsOnlineResult(parameterInput);
        return bankAccNote.convert(RestResponse.class);
    }
    /**
     * 根据Id获取账号
     *
     * @param btBankAcc 账号VO
     * @return
     */
    @Transactional(readOnly = true)
    public List<BtBankAccVO> selectBankAccView(BtBankAccVO btBankAcc) {
        if (btBankAcc == null || StringUtils.isBlank(btBankAcc.getId())) {
            return null;
        }
        String[] ids = StringUtils.split(btBankAcc.getId(), CommonConstant.COMMA_SPLIT);
        List<BtBankAccVO> list = btBankAccDao.listByIds(ids, btBankAcc.getCurId());
        if(EmptyUtils.isNotEmpty(list)){
            Map<String, CorpDTO> corpMap = corpServiceI.getCorpMap();
            Map<String, BankTypeDTO> bankTypeMap = bankTypeService.getBankTypeMap();
            Map<String, BtCurrencyDTO> currency = btCurrencyServiceI.getCurrency();

            list.forEach(a -> {
                if(null != corpMap){
                    CorpDTO corpDTO = corpMap.get(a.getCorpId());
                    if(null != corpDTO){
                        a.setCorpName(corpDTO.getName());
                    }
                }
                if(null != bankTypeMap){
                    BankTypeDTO bankTypeDTO = bankTypeMap.get(a.getBankTypeId());
                    if(null != bankTypeDTO){
                        a.setBankTypeName(bankTypeDTO.getTypeName());
                    }
                }
                if(null != currency){
                    BtCurrencyDTO btCurrencyDTO = currency.get(a.getCurId());
                    if(null != btCurrencyDTO){
                        a.setCurName(btCurrencyDTO.getCurName());
                    }
                }
            });
        }
        return list;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位已经开设银行账户，不允许删除！
            int num = this.btBankAccDao.getBankAccCountByCorpId(corpId, "del");
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_BANKACC,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DEL_BANKACC_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位存在有效的银行账户，不允许禁用！
            int num = this.btBankAccDao.getBankAccCountByCorpId(corpId, "disable");
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_BANKACC,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BTBANKACC_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    /**
     * 根据用户获取银行账号
     *
     * @param isDomesticBank 是否境内银行 1-境内，0-境外
     * @param userId         用户id
     * @return
     */
    public List<BtBankAccVO> getBankAccVOByuserId(String userId, String isDomesticBank) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpServiceI.getUserCorpIds(SysResourceConstant.SYS_ACCOUNTGRANT_ACCOUNT, userId), "acc.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        return this.btBankAccDao.getBankAccVOByuserId(isDomesticBank, sql.toString());
    }
    /**
     * 根据用户权限获取有效的银行账号
     *
     * @param userId  用户id
     * @param corpId  单位id
     * @param curId   币别
     * @param accType 账户类别 可多选用,隔开
     * @return
     */
    public List<BtBankAccVO> getBankAccVOByuserIdCurId(String userId, String corpId, String curId, String accType) {
        StringBuilder sql = new StringBuilder();
//        String corpAuthSql = SqlUtil.getInSql(
//                corpServiceI.getUserCorpIds(SysResourceConstant.SYS_ACCOUNTGRANT_ACCOUNT, userId), "acc.corp_id",
//                true);
//        sql.append(" and ").append(corpAuthSql);
        return this.btBankAccDao.getBankAccVOByuserIdCurId(corpId, curId, accType, sql.toString());
    }
    /**
     * 获取所有父账号集合
     *
     * @return
     */
    public List<BtBankAccDTO> getAllParentBankAcc() {
        List<BtBankAccVO> btBankAccVOList = this.btBankAccDao.getAllParentBankAcc();
        for (BtBankAccVO btBankAccVO : btBankAccVOList) {
            CorpDTO corpDTO = corpServiceI.get(btBankAccVO.getCorpId());
            if (!EmptyUtils.isEmpty(corpDTO)) {
                btBankAccVO.setCorpName(corpDTO.getName());
            }
        }
        List<BtBankAccDTO> list = new ArrayList<>();
        if (!EmptyUtils.isEmpty(btBankAccVOList)) {
            list = BeanUtils.copyListProperties(btBankAccVOList, BtBankAccDTO.class);
        }
        return list;
    }
    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<BtBankAccDTO> getBankAccByCorpIdList(List<String> corpIdList, String joinSql, Map<String, Object> params) {
        if (EmptyUtils.isEmpty(corpIdList)) {
            return new ArrayList<>();
        }
        List<BtBankAccVO> accList = btBankAccDao.getBankAccVOByCorpIdList(corpIdList, joinSql, params);
        List<BtBankAccDTO> tempList = new ArrayList<BtBankAccDTO>();
        for (int i = 0; i < accList.size(); i++) {
            BtBankAccDTO tempDto = new BtBankAccDTO();
            BeanUtils.copyNotNullProperties(accList.get(i), tempDto);
            tempList.add(tempDto);
        }
        return tempList;
    }
    /**
     * 根据单位集合 得到银行账户列表(不包含账户余额)
     *
     * @param corpIdList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<BtBankAccDTO> getBankAccNotUnionByCorpIdList(List<String> corpIdList, String joinSql,Map<String,Object> params) {
//        if (EmptyUtils.isEmpty(corpIdList)) {
//            return new ArrayList<>();
//        }
        List<BtBankAccVO> accList = btBankAccDao.getBankAccByCorpIdList(corpIdList, joinSql, params);
        List<BtBankAccDTO> tempList = new ArrayList<BtBankAccDTO>();
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyServiceI.getCurrency();
        for (int i = 0; i < accList.size(); i++) {
            BtBankAccDTO tempDto = new BtBankAccDTO();
            BeanUtils.copyNotNullProperties(accList.get(i), tempDto);
            if (currencyMap.containsKey(tempDto.getCurId())) {
                tempDto.setCurName(currencyMap.get(tempDto.getCurId()).getCurName());
            }
            tempList.add(tempDto);
        }
        return tempList;
    }

    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<String> getBankAccIdsByCorpIdList(List<String> corpIdList, String joinSql, Map<String,Object> params) {
        if (EmptyUtils.isEmpty(corpIdList)) {
            return new ArrayList<>();
        }
        List<BtBankAccVO> accList = btBankAccDao.getBankAccByCorpIdList(corpIdList, joinSql, params);
        List<String> tempList = new ArrayList<>();
        for (BtBankAccVO acc : accList) {
            tempList.add(acc.getId());
        }
        return tempList;
    }
    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<String> getBankAccsByCorpIdList(List<String> corpIdList, String joinSql, Map<String,Object> params) {
        if (EmptyUtils.isEmpty(corpIdList)) {
            return new ArrayList<>();
        }
        List<BtBankAccVO> accList = btBankAccDao.getBankAccByCorpIdList(corpIdList, joinSql, params);
        List<String> tempList = new ArrayList<>();
        for (BtBankAccVO acc : accList) {
            tempList.add(acc.getBankAcc());
        }
        return tempList;
    }

    /**
     * 根据账号集合查询账户信息，包括省市信息
     *
     * @param bankAccs 账号集合
     * @return 账户信息
     */
    public List<BtBankAccDTO> getBankAccByInfoAccs(List<String> bankAccs) {
        if (EmptyUtils.isEmpty(bankAccs)) {
            return new ArrayList<>();
        }
        return btBankAccDao.getBankAccByInfoAccs(bankAccs);
    }
    public List<BtBankAccDTO> getBankAccByInfoAccs(List<String> bankAccs,DataTablesInput input) {
        if (EmptyUtils.isEmpty(bankAccs)) {
            return new ArrayList<>();
        }
        return btBankAccDao.getBankAccByInfoAccs(bankAccs,input);
    }
    /**
     * 根据账号id集合查询账户信息，包括省市信息
     *
     * @param ids 账号id集合
     * @return 账户信息
     */
    public List<BtBankAccDTO> getBankAccUnByIds(List<String> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return btBankAccDao.getBankAccUnByIds(ids);
    }
    /**
     * 根据账户ID集合获取账号信息
     *
     * @param idList
     * @return
     */
    public List<String> getBankAccByIds(List<String> idList) {
        return btBankAccDao.getBankAccByIds(idList);
    }
    public BtBankAcc get(String id) {
        return btBankAccDao.get(id);
    }
    public List<BtBankAccDTO> getBankAccDTOById(String id) {
        return btBankAccDao.getBankAccDTOById(id);
    }
    /**
     * 根据账号属性查询账号是否有使用过这个属性
     *
     * @param propertyName
     * @param value
     * @return
     */
    public boolean isUsedBaseData(String propertyName, String value) {
        List bankAccList = btBankAccDao.listBy(propertyName, value);
        List bankAppList = btBankAccAppDao.listBy(propertyName, value);
        return !EmptyUtils.isEmptyList(bankAccList) || !EmptyUtils.isEmptyList(bankAppList);
    }
    public List<BtBankAccCurDTO> getCountertAcc(String bankAcc) {
        return btBankAccDao.getCountertAcc(bankAcc);
    }
    public List<BtBankAccDTO> listByIds(List<String> bankAccIds, List<String> oppAccNoList) {
        return btBankAccDao.listByIds(bankAccIds, oppAccNoList);
    }
    /**
     * 根据银行接口代码得到银行账号，用与银企直联
     * BT_BANK_ACC table列表
     *
     * @return
     */
    public DataTablesOutput<BtBankAccVO> getDataTableByBifCode(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_SENDQUERY, user.getId()), "acc.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput<BtBankAccVO> output = btBankAccDao.getDataTableByBifCode(dataTablesInput, sql.toString());
        List<BtBankAccVO> list = output.getData();
        if(list.size()>0) {
            for (BtBankAccVO vo : list) {
                vo.setAccTypeName(dictServiceI.get("btBankAcc.accType", vo.getAccType()));
            }
        }
        return output;
    }
    /**
     * 账户明细截至日期
     * zuoy 2017-07-10
     *
     * @param corpId
     * @param bankAcc
     * @param bifCode
     * @param curId
     * @return
     */
    public List<BtBankAccVO> listBankAccByEnd(String corpId, String bankAcc, String bifCode, String curId,String joinSql) {
        return this.btBankAccDao.listBankAccByEnd(corpId, bankAcc, bifCode, curId,joinSql);
    }

    /**
     * 根据银行接口代码得到银行账号，用与银企直联
     * BT_BANK_ACC table列表
     *
     * @return
     */
    public DataTablesOutput bindingBankAccDatatable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        StringBuilder leftSql = new StringBuilder();
        String ids = (String) dataTablesInput.getFormdata().get("id");
        String type = (String) dataTablesInput.getFormdata().get("type");
        String chooseId = (String) dataTablesInput.getFormdata().get("chooseId");
        if(EmptyUtils.isNotEmpty(ids)){
            String[] idArr = ids.split(CommonConstant.COMMA_SPLIT);
            if(!EmptyUtils.isEmpty(idArr)) {
                sql.append(" and " + SqlUtil.getInSql(idArr,"acc.id",true));
            }
        }else {
            if("view".equals(type)){
                sql.append(" and acc.id = '#' ");
            }
        }
        String idSql = "";
        if(EmptyUtils.isNotEmpty(chooseId)){
            String[] idArr = chooseId.split(CommonConstant.COMMA_SPLIT);
            idSql = " where " + SqlUtil.getInSql(idArr,"id",true);

        }
        leftSql.append(" left join (select id,'1' as sign from bt_bank_acc "+idSql+") a on acc.id = a.id ");
        DataTablesOutput output = btBankAccDao.bindingBankAccDatatable(dataTablesInput, sql.toString(),leftSql.toString());
        return output;
    }
}
