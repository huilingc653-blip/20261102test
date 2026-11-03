package com.bytter.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.common.page.PageResult;
import com.bytter.core.common.rest.RestResponse;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.constant.SysResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtBankAccMessageConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.AccountGrantMessageEnum;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import com.bytter.system.api.vo.BillBankBankAccVO;
import com.bytter.system.api.vo.BtBankAccBalVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.api.vo.VAccListVo;
import com.bytter.system.dao.BtBankAccCurDao;
import com.bytter.system.dao.BtBankAccDao;
import com.bytter.system.dao.NisBindingBankaccDao;
import com.bytter.system.dao.SysTenantManagerDao;
import com.bytter.system.entity.*;
import com.bytter.system.service.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author qijh
 * @Description 银行账户实现类
 * @date 2019-3-26
 */
@Service("btBankAccServiceI")
@DubboService
@Transactional
public class BtBankAccServiceImpl extends BaseService<BtBankAcc, String> implements IBtBankAccService {
    @Autowired
    private IMultiLangService multiLangServiceI;
    @Autowired
    private BtBankAccDao btBankAccDao;
    @Autowired
    private BtBankAccCurDao btBankAccCurDao;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    @Autowired
    private BtBankAccService btBankAccService;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IFcExcrateAdjustService fcExcrateAdjustServiceI;
    @Autowired(required = false)
    private IBankTypeService btBankTypeService;
    @Autowired(required = false)
    private BtBankAccCommonService btBankAccCommonService;
    @Autowired
    private ILicenseService licenseService;
    @Autowired(required = false)
    private BtBankAccMessageService btBankAccMessageService;
    @Autowired
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private IAccountGrantService accountGrantService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private NisBindingBankaccDao nisBindingBankaccDao;
    @DubboReference(check = false)
    private IConfigService configService;

    @Autowired
    private SysTenantManagerDao sysTenantManagerDao;

    @Autowired
    private SysTenantManagerService sysTenantManagerService;

    @Autowired(required=false)
    private ISysCountryCustService sysCountryCustService;

    @Autowired(required = false)
    private IBtSwiftInfoService btSwiftInfoService;

    @Override
    public String save(BtBankAccDTO dto) {
        String tenantId = (dto.getTenantId() == null ? UserContextHelper.helper().getTenantId() : dto.getTenantId());
        if(tenantId == null){
            throw new BusinessException("获取租户信息异常");
        }
        sysTenantManagerDao.updateByTenantId(tenantId);
        int l = countAllBankAccByValidSign(tenantId);
        SysTenantManager byTenantCode = sysTenantManagerService.getByTenantCode(tenantId);
        if(byTenantCode == null){
            logger.error(String.format("通过租户id%s查询租户信息错误",dto.getCreateBy()));
            throw new BusinessException("租户不存在，请确认");
        }
        if(byTenantCode.getAccountNumber() < (l+1)){
            throw new BusinessException(BankInfoMessageEnum.SYS_TENANT_ACCOUNT_NUM_OVER_LIMIT,
                    String.valueOf(l), "1" ,byTenantCode.getAccountNumber().toString());
        }
        BtBankAcc entity = new BtBankAcc();
        BeanUtils.copyProperties(dto, entity);
        // 用途为“营收账号”【对应用途编码为：1004】的数量不能超过参数配置中的数量
        if (ObjectUtil.isNotEmpty(entity.getAccUseCode()) && entity.getAccUseCode().indexOf("1004") != -1){
            ConfigDTO config = configService.getConfig("allot", "revenueAccountsNumber");
            Integer size = getBankAccUse1004("", entity.getCorpId());
            size = size + 1;
            if (size > config.toInteger(0)){
                throw new BusinessException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_1004_MAX);
            }
        }
        btBankAccDao.save(entity);
       return entity.getId();
    }

    private long countUserByTenantId(String tenantId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1) FROM BT_BANK_ACC WHERE TENANT_ID = :TENANT_ID AND STATUS <> :STATUS AND (IS_CLOSE is null or IS_CLOSE = :IS_CLOSE)");
        Map<String,Object> params = new HashMap<>();
        params.put("TENANT_ID",tenantId);
        params.put("STATUS",BtBankAccConstant.STATUE_DEL);
        params.put("IS_CLOSE","0");
        Map bySQL = btBankAccDao.getBySQL(sql.toString(), params);
        return (bySQL.get("count(1)") == null ? 0 : Long.valueOf(bySQL.get("count(1)").toString()));
    }

    /**
     * 根据查询有效的银行账户
     * @return int
     */
    public int countAllBankAccByValidSign(String tenantId) {
        String sql = " SELECT COUNT(1) FROM BT_BANK_ACC where STATUS <> '-2' and TENANT_ID = '"+tenantId+"' ";
        Number count = (Number) getEntityDao().createSQLQuery(sql).uniqueResult();
        return count.intValue();
    }


    @Override
    public void update(BtBankAccDTO dto) {
        BtBankAcc entity = new BtBankAcc();
        BeanUtils.copyProperties(dto, entity);
        // 用途为“营收账号”【对应用途编码为：1004】的数量不能超过参数配置中的数量
        if (ObjectUtil.isNotEmpty(entity.getAccUseCode()) && entity.getAccUseCode().indexOf("1004") != -1){
            ConfigDTO config = configService.getConfig("allot", "revenueAccountsNumber");
            Integer size = getBankAccUse1004(dto.getId(), dto.getCorpId());
            size = size + 1;
            if (size > config.toInteger(0)){
                throw new BusinessException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_1004_MAX);
            }
        }
        List<BtBankAccCurDTO> btBankAccCurDTOS = dto.getBtBankAccCurDTOList();
        List<BtBankAccCur> curs = BeanUtils.copyListProperties(btBankAccCurDTOS, BtBankAccCur.class);
        entity.setCurList(curs);
        btBankAccDao.update(entity);
    }
    @Override
    public BaseDao<BtBankAcc, String> getEntityDao() {
        return btBankAccDao;
    }
    @Override
    public BtBankAccDTO getBtBankAcc(String id) {
        BtBankAccDTO acc = new BtBankAccDTO();
        com.bytter.system.entity.BtBankAcc entity = btBankAccService.get(id);
        BeanUtils.copyProperties(entity, acc);
        acc.setCurList(entity.getCurList().stream().map(BtBankAccCur::getCurId).collect(Collectors.toList()));
        return acc;
    }
    /**
     * 根据银行账号各个条件查询银行账号列表
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    public List<BtBankAccDTO> getBankAccList(BtBankAccDTO dto) {
        return btBankAccService.getBankAccList(dto);
    }
    /**
     * 根据银行账号各个条件查询银行账号列表(因为一个银行账号可以多币别，会有多条记录)
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    public List<BtBankAccDTO> getBankAccListAll(BtBankAccDTO dto) {
//        List<BtBankAccDTO> list=btBankAccService.getBankAccListAll(dto);
        List<BtBankAccDTO> list = btBankAccService.getBankAccListAllNew(dto);
        return list;
    }
    /**
     * 根据条件查询分页列表
     *
     * @param dataTablesInput
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return btBankAccService.getDataTablesUnion1(dataTablesInput);
    }
    /**
     * 根据条件查询分页列表 离线账号，在线账号都可以显示
     *
     * @param dataTablesInput 表格封装信息
     * @return 表格封装信息
     */
    @Override
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionAll(DataTablesInput dataTablesInput) {
        return btBankAccService.getDataTablesUnionAll(dataTablesInput);
    }
    @Override
    public BtBankAccDTO getBankAccById(String id) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        List<BtBankAccDTO> list = this.getBankAccByIdList(idList);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        BtBankAccDTO bankAccDTO = list.get(0);
        if (CommonConstant.YES.equals(bankAccDTO.getIsDomesticBank())){
            SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getByNameZhLevelTop("中国");
            if (sysCountryCustDTO != null) {
                bankAccDTO.setCountryId(sysCountryCustDTO.getId());
            }
        } else if (CommonConstant.NO.equals(bankAccDTO.getIsDomesticBank()) && StringUtils.isNotBlank(bankAccDTO.getProv())) { //境外银行获取国家地区名称
            SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(bankAccDTO.getProv());
            if (sysCountryCustDTO != null) {
                bankAccDTO.setCountryId(sysCountryCustDTO.getId());
            }
        } else if (CommonConstant.NO.equals(bankAccDTO.getIsDomesticBank())&& StringUtils.isNotBlank(bankAccDTO.getBankCode())) {
            BtSwiftInfoDTO btSwiftInfoDTO = btSwiftInfoService.getByCode(bankAccDTO.getBankCode());
            if (btSwiftInfoDTO != null) {
                bankAccDTO.setCountryId(btSwiftInfoDTO.getCountryCode());
            }
        }
        return bankAccDTO;
    }
    /**
     * 根据银行账户查找账户信息
     * zuoy 2017-06-28
     *
     * @param bankAcc
     * @return
     */
    @Override
    public BtBankAccDTO getBankAccInfo(String bankAcc) {
        BtBankAccDTO dto = new BtBankAccDTO();
        if (StringUtils.isNotEmpty(bankAcc)) {
            dto.setBankAcc(bankAcc);
            List<BtBankAccDTO> list = btBankAccService.getBankAccList(dto);
            if (EmptyUtils.isEmptyList(list)) {
                return null;
            }
            BtBankAccDTO bankAccDTO = list.get(0);
            if (CommonConstant.YES.equals(bankAccDTO.getIsDomesticBank())){
                SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getByNameZhLevelTop("中国");
                if (sysCountryCustDTO != null) {
                    bankAccDTO.setCountryId(sysCountryCustDTO.getId());
                }
            } else if (CommonConstant.NO.equals(bankAccDTO.getIsDomesticBank()) && StringUtils.isNotBlank(bankAccDTO.getProv())) { //境外银行获取国家地区名称
                SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(bankAccDTO.getProv());
                if (sysCountryCustDTO != null) {
                    bankAccDTO.setCountryId(sysCountryCustDTO.getId());
                }
            }
            if (CommonConstant.NO.equals(bankAccDTO.getIsDomesticBank())&& StringUtils.isNotBlank(bankAccDTO.getBankCode())) {
                BtSwiftInfoDTO btSwiftInfoDTO = btSwiftInfoService.getByCode(bankAccDTO.getBankCode());
                if (btSwiftInfoDTO != null) {
                    bankAccDTO.setCountryId(btSwiftInfoDTO.getCountryCode());
                }
            }
            return bankAccDTO;
        } else {
            return null;
        }
    }
    /**
     * 根据idlist 得到银行账户列表
     *
     * @param idList
     * @return
     */
    @Override
    public List<BtBankAccDTO> getBankAccByIdList(List<String> idList) {
        return btBankAccService.getBankAccByIdList(idList);
    }
    /**
     * 根据idlist 得到银行账户列表(不关联余额)
     *
     * @param idList
     * @return
     */
    @Override
    public List<BtBankAccDTO> getBankAccNotUnionByIdList(List<String> idList) {
        return btBankAccService.getBankAccNotUnionByIdList(idList);
    }
    /**
     * 根据内部账号和币别，获取银行账号集合
     *
     * @param countAcc
     * @param curId
     * @return
     */
    @Override
    public List<BtBankAccDTO> getBankAccByCountAcc(String countAcc, String curId) {
        return btBankAccService.getBankAccByCountAcc(countAcc, curId);
    }
    /**
     * 根据联行号，获取银行账号集合
     *
     * @param bankCode
     * @return
     */
    @Override
    public List<BtBankAccDTO> getBankAccByBankCode(String bankCode) {
        return btBankAccService.getBankAccByBankCode(bankCode);
    }
    /**
     * 根据银行账号id和币别，获取对应的父账号信息
     *
     * @param bankAccId
     * @param curId
     * @return
     */
    @Override
    public BtBankAccDTO getParentAccByAccId(String bankAccId, String curId) {
        return btBankAccService.getParentAccByAccId(bankAccId, curId);
    }
    @Override
    public List<BtBankAccDTO> getBankAccByAccountDTO(AccountSearchDTO accountSearchDTO) {
        return this.btBankAccService.getBankAccByAccountDTO(accountSearchDTO);
    }
    @Override
    public String getBlnctrAcc(String bankAcc, String curId) {
        BtBankAccDTO bankAccDTO = new BtBankAccDTO();
        bankAccDTO.setBankAcc(bankAcc);
        bankAccDTO.setCurId(curId);
        List<BtBankAccDTO> list = this.getBankAccList(bankAccDTO);
        String blnctrAcc = "";
        if (!EmptyUtils.isEmptyList(list)) {
            for (BtBankAccDTO dto : list) {
                List<Map<String, String>> mapList = dto.getCounterAccList();
                if (!EmptyUtils.isEmptyList(mapList)) {
                    for (Map<String, String> map : mapList) {
                        if (map.containsKey(curId)) {
                            blnctrAcc = map.get(curId);
                        }
                    }
                }
            }
        }
        return blnctrAcc;
    }
    /**
     * 根据账号属性查询账号是否有使用过这个属性
     *
     * @param propertyName
     * @param value
     * @return
     */
    @Override
    public boolean isUsedBaseData(String propertyName, String value) {
        return btBankAccService.isUsedBaseData(propertyName, value);
    }
    /**
     * 判断两个账号(或者户名)是否所属同一网点下
     *
     * @param accInfo1  账号或者户名
     * @param accInfo2  账号或者户名
     * @param isBankAcc 是否是账号 true-比较账户 false-比较户名
     * @return
     */
    @Override
    public boolean isTheSameNetByAccInfo(String accInfo1, String accInfo2, boolean isBankAcc) {
        if (EmptyUtils.isBlank(accInfo1) || EmptyUtils.isBlank(accInfo2)) {
            return false;
        }
        return this.btBankAccService.isTheSameNetByAccInfo(accInfo1, accInfo2, isBankAcc);
    }
    @Override
    public void updateEndDtlDate(String bankAccId, String curId, Date endDtlDate) {
        this.btBankAccService.updateEndDtlDate(bankAccId, curId, endDtlDate);
    }
    @Override
    public Date getLastEndDtlDate(String bankAccId, String curId) {
        return this.btBankAccService.getLastEndDtlDate(bankAccId, curId);
    }
    @Override
    public DataTablesOutput<BtBankAccVO> getDataTablesUnionSYS(DataTablesInput dataTablesInput, ICurrentUser user) {
        return this.btBankAccService.getDataTablesUnionSYS(dataTablesInput, user);
    }
    @Override
    public BtBankAccDTO getBankAccByBankAccAndCurId(String bankAcc, String curId) {
        BtBankAcc entity = this.btBankAccService.getBankAccByBankAccAndCurId(bankAcc, curId);
        if (EmptyUtils.isEmpty(entity)) {
            return null;
        }
        BtBankAccDTO dto = new BtBankAccDTO();
        BeanUtils.copyNotNullProperties(entity, dto);
        if (!EmptyUtils.isEmptyList(entity.getCurList())) {
            BankTypeDTO bankTypeDTO = btBankTypeService.getObjectById(entity.getBankTypeId());
            if (!EmptyUtils.isEmpty(bankTypeDTO)) {
                dto.setBankNamePrefix(bankTypeDTO.getBankNamePrefix());
            }
            dto.setCurId(entity.getCurList().get(0).getCurId());
            dto.setCounterAccId(entity.getCurList().get(0).getCounterAcc());
            dto.setSuperCounterAcc(entity.getCurList().get(0).getSuperCounterAcc());
            dto.setPrentAccId(entity.getCurList().get(0).getPrentAccId());
        }
        return dto;
    }
    @Override
    public List<BtBankAccDTO> getOutPayByParentAcc(String prentAccId, String curId, String counterAcc) {
        return btBankAccService.getOutPayByParentAcc(prentAccId, curId, counterAcc);
    }
    @Override
    public List<String> listChildCorpIdsByParent(String prentAccId) {
        return btBankAccService.listChildCorpIdsByParent(prentAccId);
    }
    /**
     * <br>
     * <b>功能：</b>查询单位/（企e金服接口）<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-11-25<br>
     *
     * @param corpflag
     * @param corpCode
     * @return
     */
    @Override
    public BigDecimal queryBankAccAvailableBal(String corpflag, String corpCode) {
        return this.btBankAccService.queryBankAccAvailableBal(corpflag, corpCode);
    }
    @Override
    public void syncBatchSave(List<BtBankAccDTO> list) {
        if(CollectionUtil.isEmpty(list)){
            return;
        }
        String tenantId = (list.get(0).getTenantId() == null ? UserContextHelper.helper().getTenantId() : list.get(0).getTenantId());
        if(tenantId == null){
            throw new BusinessException("获取租户信息异常");
        }
        sysTenantManagerDao.updateByTenantId(tenantId);
        int l = countAllBankAccByValidSign(tenantId);
        SysTenantManager byTenantCode = sysTenantManagerService.getByTenantCode(tenantId);
        if(byTenantCode == null){
            logger.error(String.format("通过租户id%s查询租户信息错误",list.get(0).getCreateBy()));
            throw new BusinessException("租户不存在，请确认");
        }
        if(byTenantCode.getAccountNumber() < (l+1)){
            throw new BusinessException(BankInfoMessageEnum.SYS_TENANT_ACCOUNT_NUM_OVER_LIMIT,
                    String.valueOf(l), "1" ,byTenantCode.getAccountNumber().toString());
        }
        this.btBankAccService.syncBatchSave(list);
    }
    @Override
    public void syncBatchUpdate(List<BtBankAccDTO> list) {
        this.btBankAccService.syncBatchUpdate(list);
    }
    @Override
    public void syncBatchDelete(List<BtBankAccDTO> list) {
        this.btBankAccService.syncBatchDelete(list);
    }
    /**
     * 查询正常状态离线账号列表（带分页自动生成余额专用）
     *
     * @param parameterInput
     * @return
     */
    @Override
    public RestResponse<BtBankAccDTO> bankAccResult(IParameterInput parameterInput) {
        return this.btBankAccService.bankAccResult(parameterInput);
    }

    @Override
    public List<BtBankAccDTO> getBankAccByUserId(String userId, String isDomesticBank) {
        List<BtBankAccVO> btBankAccVOList = this.btBankAccService.getBankAccVOByuserId(userId, isDomesticBank);
        List<BtBankAccDTO> list = new ArrayList<>();
        if (!EmptyUtils.isEmpty(btBankAccVOList)) {
            list = BeanUtils.copyListProperties(btBankAccVOList, BtBankAccDTO.class);
        }
        return list;
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
    @Override
    public List<BtBankAccDTO> getBankAccByUserIdCurId(String userId, String corpId, String curId, String accType) {
        List<BtBankAccVO> btBankAccVOList = this.btBankAccService.getBankAccVOByuserIdCurId(userId, corpId, curId, accType);
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
     * @param joinSql    拼接其他查询条件
     * @return
     */
    @Override
    public List<BtBankAccDTO> getBankAccNotUnionByCorpIdList(List<String> corpIdList, String joinSql, Map<String,Object> params) {
        return btBankAccService.getBankAccNotUnionByCorpIdList(corpIdList, joinSql, params);
    }

    /**
     * 获取所有父账号集合
     *
     * @return
     */
    @Override
    public List<BtBankAccDTO> getAllParentBankAcc() {
        return this.btBankAccService.getAllParentBankAcc();
    }
    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList
     * @param joinSql    拼接其他查询条件
     * @return
     */
    @Override
    public List<String> getBankAccIdsByCorpIdList(List<String> corpIdList, String joinSql, Map<String,Object> params) {
        return btBankAccService.getBankAccIdsByCorpIdList(corpIdList, joinSql, params);
    }
    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList
     * @param joinSql    拼接其他查询条件
     * @return
     */
    @Override
    public List<String> getBankAccsByCorpIdList(List<String> corpIdList, String joinSql, Map<String,Object> params) {
        return btBankAccService.getBankAccsByCorpIdList(corpIdList, joinSql, params);
    }

    /**
     * 根据银行账号查询
     *
     * @param bankAcc 账号
     * @return
     */
    @Override
    public BtBankAccDTO getBankAccInfoByBankAcc(String bankAcc) {
        BtBankAcc btBankAcc = btBankAccCommonService.getBtBankAccByBankAcc(bankAcc);
        if (btBankAcc == null) {
            return null;
        }
        BtBankAccDTO result = new BtBankAccDTO();
        BeanUtils.copyNotNullProperties(btBankAcc, result);
        return result;
    }
    /**
     * 根据账号集合查询账户信息，包括省市信息
     *
     * @param bankAccs 账号集合
     * @return 账户信息
     */
    @Override
    public Map<String, BtBankAccDTO> getBankAccByInfoAccs(List<String> bankAccs) {
        if (EmptyUtils.isEmpty(bankAccs)) {
            return null;
        }
        List<BtBankAccDTO> btBankAccDTOList = this.btBankAccService.getBankAccByInfoAccs(bankAccs);
        if (EmptyUtils.isEmpty(btBankAccDTOList)) {
            return null;
        }
        Map<String, BtBankAccDTO> map = new HashMap<>();
        for (BtBankAccDTO dto : btBankAccDTOList) {
            map.put(dto.getBankAcc(), dto);
        }
        return map;
    }
    /**
     * 根据账号集合查询账户信息，包括省市信息
     *
     * @param bankAccs 账号集合
     * @return 账户信息
     */
    @Override
    public Map<String, BtBankAccDTO> getBankAccMapByAccsAndCurId(List<String> bankAccs,DataTablesInput input) {
        if (EmptyUtils.isEmpty(bankAccs)) {
            return null;
        }
        List<BtBankAccDTO> btBankAccDTOList = this.btBankAccService.getBankAccByInfoAccs(bankAccs,input);
        if (EmptyUtils.isEmpty(btBankAccDTOList)) {
            return null;
        }
        Map<String, BtBankAccDTO> map = new HashMap<>();
        for (BtBankAccDTO dto : btBankAccDTOList) {
            map.put(dto.getBankAcc()+dto.getCurId(), dto);
        }
        return map;
    }
    @Override
    public Map<String, BtBankAccDTO> getBankAccMapByAccsAndCurId(List<String> bankAccs) {
        if (EmptyUtils.isEmpty(bankAccs)) {
            return null;
        }
        List<BtBankAccDTO> btBankAccDTOList = this.btBankAccService.getBankAccByInfoAccs(bankAccs);
        if (EmptyUtils.isEmpty(btBankAccDTOList)) {
            return null;
        }
        Map<String, BtBankAccDTO> map = new HashMap<>();
        for (BtBankAccDTO dto : btBankAccDTOList) {
            map.put(dto.getBankAcc()+dto.getCurId(), dto);
        }
        return map;
    }
    /**
     * 根据账号id集合查询账户信息，包括省市信息
     *
     * @param ids 账号id集合
     * @return 账户信息
     */
    @Override
    public Map<String, BtBankAccDTO> getBankAccUnByIds(List<String> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return MapUtil.empty();
        }
        List<BtBankAccDTO> btBankAccDTOList = this.btBankAccService.getBankAccUnByIds(ids);
        if (EmptyUtils.isEmpty(btBankAccDTOList)) {
            return MapUtil.empty();
        }
        Map<String, BtBankAccDTO> map = new HashMap<>();
        for (BtBankAccDTO dto : btBankAccDTOList) {
            map.put(dto.getBankAcc(), dto);
        }
        return map;
    }
    /**
     * 根据账户ID集合获取账号信息
     *
     * @param idList
     * @return
     */
    @Override
    public List<String> getBankAccByIds(List<String> idList) {
        return btBankAccService.getBankAccByIds(idList);
    }
    @Override
    public void batchSave(List<BtBankAcc> entityList) {
        if(CollectionUtil.isEmpty(entityList)){
            return;
        }
        String tenantId = (entityList.get(0).getTenantId() == null ? UserContextHelper.helper().getTenantId() : entityList.get(0).getTenantId());
        if(tenantId == null){
            throw new BusinessException("获取租户信息异常");
        }
        sysTenantManagerDao.updateByTenantId(tenantId);
        int l = countAllBankAccByValidSign(tenantId);
        SysTenantManager byTenantCode = sysTenantManagerService.getByTenantCode(tenantId);
        if(byTenantCode == null){
            logger.error(String.format("通过租户id%s查询租户信息错误",entityList.get(0).getCreateBy()));
            throw new BusinessException("租户不存在，请确认");
        }
        long num = (l+entityList.size());
        Integer accountNumber = byTenantCode.getAccountNumber();
        if (accountNumber != null && accountNumber < num) {
            throw new BusinessException(BankInfoMessageEnum.SYS_TENANT_ACCOUNT_NUM_OVER_LIMIT,
                    String.valueOf(l),
                    String.valueOf(entityList.size()),
                    String.valueOf(accountNumber));
        }
        btBankAccService.batchSave(entityList);
    }
    /**
     * 银行账号数量是否超过了license允许的数量
     *
     * @return
     */
    @Override
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
        if (licenseCount == null || licenseCount == 0) {
            result = true;
        } else {
            if (licenseCount >= bankAccCount) {
                result = true;
            }
        }
        return result;
    }
    @Override
    public com.bytter.system.api.vo.BtBankAccVO getByUnion(String id, String curId) {
        // TODO Auto-generated method stub
        BtBankAccVO vo = btBankAccDao.getByUnion(id, curId);
        CorpDTO corpDTO = corpServiceI.get(vo.getCorpId());
        if (!EmptyUtils.isEmpty(corpDTO)) {
            vo.setCorpName(corpDTO.getName());
        }
        Region region = regionService.getById(vo.getProv());
        if (!EmptyUtils.isEmpty(region)) {
            vo.setProvName(region.getName());
        }
        region = regionService.getById(vo.getCity());
        if (!EmptyUtils.isEmpty(region)) {
            vo.setCityName(region.getName());
        }
        com.bytter.system.api.vo.BtBankAccVO accVO = new com.bytter.system.api.vo.BtBankAccVO();
        BeanUtils.copyProperties(vo, accVO);
        return accVO;
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<BtBankAccDTO> getBankAccList(AccountSearchDTO dto) {
        return accountGrantService.listDTONotUnionByMultiVal(dto.getUserId(), null, dto.getResource(), null, dto.getAccountType(), dto.getPermission());
    }
    @Override
    public List<BtBankAccDTO> getBankAccList() {
        return btBankAccCommonService.listEffectiveBtBankAccs();
    }

    @Override
    public List<BtBankAccDTO> getDistinctBankAccList() {
        return btBankAccCommonService.listEffectiveBtBankAccs()
                .stream().filter(StreamUtils.distinctKey(BtBankAccDTO::getBankCode))
                .collect(Collectors.toList());
    }

    /**
     * 银行账号开户处理
     *
     * @param btBankAcc
     */
//		@Override
//		public void toModify(com.bytter.system.api.vo.BtBankAcc btBankAcc, ICurrentUser user){//TODO修改
//	        BtBankAccApp btBankAccApp = this.btBankAccCommonService.getBtBankAccApp(btBankAcc.getId());
//	        if (!btBankAccCommonService.isBankAcc(btBankAcc.getBankAcc())) {
//	            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_UNIQUE);
//	        }
//	        if (!this.isBtBankAccCountLicense()) {
//	            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_ACCOUNT_LICENSE, licenseService.getBankAccountNumber().toString());
//	        }
//	        String curIdStr = btBankAcc.getCurId();
//	        if (EmptyUtils.isBlank(curIdStr)) {
//	            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL);
//	        }
//	        String[] curIds = curIdStr.split(",");
//	        if (EmptyUtils.isEmptyArray(curIds)) {
//	            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL);
//	        }
//	        if (!EmptyUtils.isBlank(btBankAcc.getBifCode())) {
//	            BisBifInitDTO bisBifInitDTO = this.bisBifInitService.getObjectById(btBankAcc.getBifCode());
//	            if (!EmptyUtils.isEmpty(bisBifInitDTO) && !btBankAcc.getBankTypeId().equalsIgnoreCase(bisBifInitDTO.getBankTypeId())) {
//	                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BIFINIT_EQ_BANKTYPEID);
//	            }
//	        }
//	        //办理完成状态
//	        btBankAccApp.setStatus(BtBankAccConstant.STATUE_CANCELLATION);
//	        btBankAccApp.setBankAcc(btBankAcc.getBankAcc());
//	        btBankAccApp.setUpdateBy(user.getLoginName());
//	        btBankAccApp.setUpdateDate(DateUtils.getDate());
//
//	        btBankAcc.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
//	        btBankAcc.setCreateBy(user.getLoginName());
//	        btBankAcc.setCreateDate(DateUtils.getDate());
//	        this.saveOrUpdate(btBankAccApp);
//	        btBankAcc.setId("");
//	        this.save(btBankAcc);
//	        //新增账户自动授权
//	        AccountGrantDTO grantDTO = new AccountGrantDTO();
//	        grantDTO.setAccountId(btBankAcc.getId());
//	        //授权给申请人
//	        UserDTO createBy = userService.getByLoginName(btBankAccApp.getCreateBy());
//	        grantDTO.setUserId(createBy.getId());
//	        grantDTO.setCorpId(btBankAcc.getCorpId());
//	        if (!EmptyUtils.isEmpty(btBankAcc.getIsOnline()) && BtBankAccConstant.ISONLINE_0.equals(btBankAcc.getIsOnline())) {
//	            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.OFFLINE_BANK_TYPE));
//	        } else {
//	            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.ONLINE_BANK_TYPE));
//	        }
//	        accountGrantService.saveAccountGrant(grantDTO);
//	        List<String> curList = new ArrayList<String>();
//	        for (int i = 0; i < curIds.length; i++) {
//	            BtBankAccCur accCur = new BtBankAccCur();
//	            if ("all".equals(curIds[i])) {
//	                continue;
//	            }
//	            BtBankAccAppCur cur = this.btBankAccCommonService.getBtBankAccAppCur(btBankAcc.getId(), curIds[i]);
//	            if (EmptyUtils.isEmpty(cur)) {
//	                accCur.setBankAccId(btBankAcc.getId());
//	                accCur.setCurId(curIds[i]);
//	                this.getEntityDao().save(accCur);
//	            }
//	            curList.add(curIds[i]);
//	        }
//
//	        if (BtBankAccConstant.ISONLINE_0.equalsIgnoreCase(btBankAcc.getIsOnline())) {
//	            // 初始化余额信息 zuoy 2017-07-20
//	            BtBankAccDTO dto = new BtBankAccDTO();
//	            BeanUtils.copyNotNullProperties(btBankAcc, dto);
//	            dto.setCurList(curList);
////	            this.bisAccBalService.initBisAccBal(dto, user);
//	        }
//	        //生成日志
//	        btBankAccMessageService.saveMessage(btBankAcc.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_APPHANDLE, user);
//	        // 消息提醒
//	        notificationService.complete(NotificationConstant.NETBANK.BANKACC_REQ_HANDLE, btBankAccApp.getId(), user.getId());
////	        notificationService.sendToUserByLoginName(btBankAccApp.getId(), btBankAccApp.getCreateBy(), NotificationConstant.NETBANK.BANKACC_REQ_HANDLE_SUCCESS, btBankAccApp);
//	        //开启审批
//	         getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_ACC_HANDLE, btBankAcc.getId(), btBankAcc, btBankAcc.getCorpId(), user.getId());
//	    }
//
    /**
     * 根据Id获取账号
     *
     * @param btBankAcc 账号VO
     * @return
     */
    @Override
    public List<com.bytter.system.api.vo.BtBankAccVO> selectBankAccView(com.bytter.system.api.vo.BtBankAccVO btBankAcc) {
        if (btBankAcc == null || StringUtils.isBlank(btBankAcc.getId())) {
            return null;
        }
        String[] ids = StringUtils.split(btBankAcc.getId(), CommonConstant.COMMA_SPLIT);
        List<BtBankAccVO> list = btBankAccDao.listByIds(ids, btBankAcc.getCurId());
        List<com.bytter.system.api.vo.BtBankAccVO> listvo = BeanUtils.copyListProperties(list,BtBankAccVO.class);
        return listvo;
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
    /**
     * 打回
     *
     * @param id
     */
    @Override
    public void back(String id, ICurrentUser user) {
        BtBankAccApp btbankaccapp = new BtBankAccApp();
        com.bytter.system.entity.BtBankAccApp entity = this.btBankAccDao.getBtbankAccApp(id);
        BeanUtils.copyProperties(entity, btbankaccapp);
        btbankaccapp.setStatus(BtBankAccConstant.STATUE_BACK);
        this.saveOrUpdate(btbankaccapp);
        //生成日志
        btBankAccMessageService.saveMessage(btbankaccapp.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_BACK, "", BtBankAccMessageConstant.BUS_TYPE_APP, user);
        // 消息提醒
        notificationService.complete(NotificationConstant.NETBANK.BANKACC_REQ_HANDLE, id, user.getId());
        notificationService.sendToUserByLoginName(btbankaccapp.getId(), btbankaccapp.getCreateBy(), NotificationConstant.NETBANK.BANKACC_REQ, btbankaccapp);
    }
    @Override
    public void deleteEntity(BtBankAccDTO dto) {
        BtBankAcc entity = new BtBankAcc();
        BeanUtils.copyProperties(dto, entity);
        this.delete(entity.getId());
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
    @Override
    public List<BtBankAccDTO> getListByCorpId(String corpId) {
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
    @Override
    public List<BtBankAccDTO> getListByCorpIds(List<String> corpIds) {
        List<BtBankAcc> list = btBankAccDao.getListByCorpIds(corpIds);
        List<BtBankAccDTO> listDTO = BeanUtils.copyListProperties(list, BtBankAccDTO.class);
        return listDTO;
    }

    @Override
    public List<BtBankAccDTO> queryListByBankTypeIds(List<String> bankTypeIds) {
        List<BtBankAcc> list = btBankAccDao.queryListByBankTypeIds(bankTypeIds);
        List<BtBankAccDTO> listDTO = BeanUtils.copyListProperties(list, BtBankAccDTO.class);
        return listDTO;
    }

    /**
     * 获取实体账号
     *
     * @param btBankAccApp
     * @return
     */
    @Override
    public List<BtBankAccDTO> getEntityAcc(BtBankAccAppDTO btBankAccApp) {
        com.bytter.system.entity.BtBankAccApp app = new com.bytter.system.entity.BtBankAccApp();
        BeanUtils.copyProperties(btBankAccApp, app);
        List<BtBankAccVO> list = btBankAccDao.getEntityAcc(app);
        List<BtBankAccDTO> acc = BeanUtils.copyListProperties(list, BtBankAccDTO.class);
        return acc;
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableOldBankAcc(DataTablesInput dataTablesInput, ICurrentUser user) {
        DataTablesOutput output = btBankAccDao.getDataTableOldBankAcc(dataTablesInput, "");
        this.btBankAccCommonService.setCurName(output);
        return output;
    }
    /**
     * 根据单位、账号类型、权限，查询帐号列表
     * zuoy 2017-07-13
     *
     * @param accountSearchDTO
     * @return
     */
    @Override
    public List<AccountDTO> listBy(AccountSearchDTO accountSearchDTO) {
        String accountType = accountSearchDTO.getAccountType();
        //校验参数是否正确
        if (EmptyUtils.isBlank(accountType) || EmptyUtils.isBlank(accountType)) {
            throw new ParameterException(AccountGrantMessageEnum.SYS_ACCOUNT_TYPE_NOT_CORRECT);
        }
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        //单位需要必填且单位为空的情况下，直接不查询账号
        if (CommonConstant.YES.equals(accountSearchDTO.getIsMustCorp()) && StringUtils.isBlank(accountSearchDTO.getCorpId())) {
            return list;
        }
        // 获取资源下的单位ID
        if (!EmptyUtils.isBlank(accountSearchDTO.getResource())) {
            List<String> userCorpIds = this.getUserCorpIds(accountSearchDTO.getResource(), accountSearchDTO.getUserId());
            accountSearchDTO.setCorpGrantList(userCorpIds);
        }
        try {
            List<BtBankAccDTO> listBankAcc = this.getBankAccByAccountDTO(accountSearchDTO);
            if (!EmptyUtils.isEmptyList(listBankAcc)) {
                list = copyBtBankAccDTOList(listBankAcc, accountSearchDTO);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return null;
        }
        return list;
    }
    /**
     * 将BtBankAccDTO集合cope到AccountDTO集合
     * zuoy 2017-07-13
     *
     * @param listBankAcc      银行账户集合
     * @param accountSearchDTO
     * @return
     */
    @Override
    public List<AccountDTO> copyBtBankAccDTOList(List<BtBankAccDTO> listBankAcc, AccountSearchDTO accountSearchDTO) {
        List<AccountDTO> list = new ArrayList<>();
        Map<String, BtBankAccDTO> parentBankAccMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(accountSearchDTO.getContainParent()) && accountSearchDTO.getContainParent()) {
            //获取所有的父账号集合
            List<BtBankAccDTO> parentBankAccList = this.getAllParentBankAcc();
            for (BtBankAccDTO dto : parentBankAccList) {
                parentBankAccMap.put(dto.getId(), dto);
            }
        }
        for (BtBankAccDTO bankAcc : listBankAcc) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setBankAccId(bankAcc.getId());
            accountDTO.setCorpId(bankAcc.getCorpId());
            accountDTO.setCorpName(bankAcc.getCorpName());
            accountDTO.setBankAcc(bankAcc.getBankAcc());
            accountDTO.setAccName(bankAcc.getAccName());
            accountDTO.setBankName(bankAcc.getBankName());
            accountDTO.setBankCode(bankAcc.getBankCode());
            accountDTO.setCurId(bankAcc.getCurId());
            accountDTO.setCurName(bankAcc.getCurName());
            accountDTO.setBifCode(bankAcc.getBifCode());
            accountDTO.setProv(bankAcc.getProv());
            accountDTO.setProvName(bankAcc.getProvName());
            accountDTO.setCity(bankAcc.getCity());
            accountDTO.setCityName(bankAcc.getCityName());
            accountDTO.setBalance(bankAcc.getBalance());
            accountDTO.setFcBalance(bankAcc.getFcBalance());
            accountDTO.setBankTypeId(bankAcc.getBankTypeId());
            accountDTO.setBankTypeName(bankAcc.getBankTypeName());
            accountDTO.setBankNamePrefix(bankAcc.getBankNamePrefix());
            accountDTO.setCounterAccId(bankAcc.getCounterAccId());
            accountDTO.setSuperCounterAcc(bankAcc.getSuperCounterAcc());
            // containParent参数为true时，加载父账户信息
            if (!EmptyUtils.isEmpty(accountSearchDTO.getContainParent()) && accountSearchDTO.getContainParent()) {
//	                BtBankAccDTO parentBankAcc = this.getParentAccByAccId(bankAcc.getId(), bankAcc.getCurId());
                BtBankAccDTO parentBankAcc = parentBankAccMap.get(bankAcc.getPrentAccId());
                if (!EmptyUtils.isEmpty(parentBankAcc)) {
                    AccountDTO parentAccountDTO = new AccountDTO();
                    parentAccountDTO.setBankAccId(parentBankAcc.getId());
                    parentAccountDTO.setCorpId(parentBankAcc.getCorpId());
                    parentAccountDTO.setCorpName(parentBankAcc.getCorpName());
                    parentAccountDTO.setBankAcc(parentBankAcc.getBankAcc());
                    parentAccountDTO.setAccName(parentBankAcc.getAccName());
                    parentAccountDTO.setBankName(parentBankAcc.getBankName());
                    parentAccountDTO.setBankCode(parentBankAcc.getBankCode());
                    parentAccountDTO.setCurId(parentBankAcc.getCurId());
                    parentAccountDTO.setCurName(parentBankAcc.getCurName());
                    parentAccountDTO.setBifCode(parentBankAcc.getBifCode());
                    parentAccountDTO.setProv(parentBankAcc.getProv());
                    parentAccountDTO.setProvName(parentBankAcc.getProvName());
                    parentAccountDTO.setCity(parentBankAcc.getCity());
                    parentAccountDTO.setCityName(parentBankAcc.getCityName());
                    parentAccountDTO.setBankTypeId(parentBankAcc.getBankTypeId());
                    parentAccountDTO.setBankTypeName(parentBankAcc.getBankTypeName());
                    parentAccountDTO.setBankNamePrefix(parentBankAcc.getBankNamePrefix());
                    //避免相同父账号反复查询余额TODO  设置余额，在使用到的场景分两个接口查，一个基本信息，一个余额
//	                    if (parentAccountDTO.getBalance() == null) {
//	                        BigDecimal parentBalance = this.bisaccBalService.getLastByBalDate(parentBankAcc.getBankAcc(), parentBankAcc.getCurId());
//	                        parentBankAccMap.get(bankAcc.getPrentAccId()).setBalance(parentBalance);
//	                        parentAccountDTO.setBalance(EmptyUtils.isEmpty(parentBalance) ? BigDecimal.ZERO : parentBankAcc.getBalance());
//	                    }
                    accountDTO.setParentAccount(parentAccountDTO);
                }
            }
            list.add(accountDTO);
        }
        return list;
    }
    /**
     * 获取资源下的单位ID
     *
     * @param resource 资源
     * @return
     * @author zuoy 2017-10-10
     */
    @Override
    public List<String> getUserCorpIds(String resource, String userId) {
        // 获取资源下的单位ID
        List<String> userCorpIds = null;
        if (!EmptyUtils.isBlank(resource)) {
            userCorpIds = this.corpServiceI.getUserCorpIds(resource, userId);
        }
        return userCorpIds;
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
    @Override
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
     * 根据条件查找账户信息
     *
     * @param id
     * @param curId 币别ID
     * @return
     * @author zuoy 2018-05-29
     */
    @Override
    public BtBankAccDTO getBankAccByIdAndCurId(String id, String curId) {
        BtBankAcc acc = this.btBankAccDao.getBankAccByIdAndCurId(id, curId);
        if (EmptyUtils.isEmpty(acc)) {
            return null;
        }
        BtBankAccDTO bankAcc = new BtBankAccDTO();
        BeanUtils.copyProperties(acc, bankAcc);
        BtBankAccCurDTO btBankAccCur = btBankAccCurDao.getBankAccCurByBankAccId(id,curId);
        //设置结算中心信息
        if (!ObjectUtils.isEmpty(btBankAccCur)) {
            bankAcc.setCurId(btBankAccCur.getCurId());
            bankAcc.setCounterAccId(btBankAccCur.getCounterAcc());
            bankAcc.setSuperCounterAcc(btBankAccCur.getSuperCounterAcc());
            bankAcc.setPrentAccId(btBankAccCur.getPrentAccId());
        }
        CorpDTO corpDTO = corpServiceI.get(acc.getCorpId());
        if (!EmptyUtils.isEmpty(corpDTO)) {
            bankAcc.setCorpName(corpDTO.getName());
        }
        return bankAcc;
    }
    /**
     * 查询正常状态在线和离线账号列表
     *
     * @param isOnline 1-在线账号，0-离线账号
     * @return 账号集合
     */
    @Override
    public List<BtBankAccDTO> getBankAccListByIsOnline(String isOnline) {
        return this.btBankAccDao.getBankAccListByIsOnline(isOnline);
    }
    @Override
    public List<BtBankAccDTO> getBankAccDTOById(String id) {
        return btBankAccDao.getBankAccDTOById(id);
    }
    /**
     * 根据账号集合查询账户信息，包括省市信息
     *
     * @param bankAccs 账号集合
     * @return 账户信息
     */
    @Override
    public List<BtBankAccDTO> getBankAccByInfoAccsList(List<String> bankAccs) {
        if (EmptyUtils.isEmpty(bankAccs)) {
            return new ArrayList<>();
        }
        return btBankAccDao.getBankAccByInfoAccs(bankAccs);
    }
    @Override
    public List<BtBankAccDTO> getBankAccByBankAccIds(List<String> bankAccIds) {
        if (EmptyUtils.isEmpty(bankAccIds)) {
            return new ArrayList<>();
        }
        List<BtBankAccVO> accList = btBankAccDao.getBankAccByBankAccIds(bankAccIds);
        return BeanUtils.copyListProperties(accList, BtBankAccDTO.class);
    }
    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Override
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
    @Override
    public List<com.bytter.system.api.vo.BtBankAccVO> getBankAccVOByCorpIdList(List<String> corpIdList, String joinSql, Map<String, Object> params) {
        if (EmptyUtils.isEmpty(corpIdList)) {
            return new ArrayList<>();
        }
        List<BtBankAccVO> list = btBankAccDao.getBankAccVOByCorpIdList(corpIdList, joinSql, params);
        return list;
    }
    /**
     * 根据用户权限获取有效的银行账号
     *
     * @param corpId  单位id
     * @param curId   币别
     * @param accType 账户类别 可多选用,隔开
     * @return
     */
    @Override
    public List<com.bytter.system.api.vo.BtBankAccVO> getBankAccVOByuserIdCurIdWithSql(String corpId, String curId, String accType, String sql) {
        List<BtBankAccVO> listvo = this.btBankAccDao.getBankAccVOByuserIdCurId(corpId, curId, accType, sql);
        return listvo;
    }
    /**
     * 根据用户获取银行账号
     *
     * @param isDomesticBank 是否境内银行 1-境内，0-境外
     * @param userId         用户id
     * @return
     */
    @Override
    public List<com.bytter.system.api.vo.BtBankAccVO> getBankAccVOByuserId(String userId, String isDomesticBank) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpServiceI.getUserCorpIds(SysResourceConstant.SYS_ACCOUNTGRANT_ACCOUNT, userId), "acc.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        List<BtBankAccVO> listvo = this.btBankAccDao.getBankAccVOByuserId(isDomesticBank, sql.toString());
        return listvo;
    }
    @Override
    public int getBankAccCountByCorpId(String corpId, String busType) {
        return this.btBankAccDao.getBankAccCountByCorpId(corpId, "disable");
    }
    @Override
    public void delete(String id) {
        this.btBankAccDao.delete(id);
    }
    @Override
    public List<String> getBankAccByAccId(List<String> accIds) {
        return btBankAccDao.getBankAccByAccId(accIds);
    }
    @Override
    public void saveOrUpdateObj(BtBankAccDTO dto) {
        BtBankAcc acc = new BtBankAcc();
        BeanUtils.copyProperties(dto, acc);
        // 用途为“营收账号”【对应用途编码为：1004】的数量不能超过参数配置中的数量
        if (ObjectUtil.isNotEmpty(acc.getAccUseCode()) && acc.getAccUseCode().indexOf("1004") != -1){
            ConfigDTO config = configService.getConfig("allot", "revenueAccountsNumber");
            Integer size = getBankAccUse1004(dto.getId(), dto.getCorpId());
            size = size + 1;
            if (size > config.toInteger(0)){
                throw new BusinessException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_1004_MAX);
            }
        }
        this.btBankAccDao.saveOrUpdate(acc);
    }
    /**
     * 分页获取银行账户列表
     *
     * @param vo       vo
     * @return 银行账户列表
     */
    @Override
    public IPageResult<BtBankAccVO> getPageListByVO(IParameterInput parameterInput,BtBankAccVO vo, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY, user.getId()), "b.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        IPageResult<BtBankAccVO> listpage = this.btBankAccDao.getPageListByVO(parameterInput, vo, sql);
        List<BtBankAccVO> listvo = listpage.getList();
        List<com.bytter.system.api.vo.BtBankAccVO> listResult = BeanUtils.copyListProperties(listvo, com.bytter.system.api.vo.BtBankAccVO.class);
        PageResult pgr = new PageResult();
        pgr.setList(listResult);
        pgr.setPageNumber(listpage.getPageNumber());
        pgr.setTotalRow(listpage.getTotalRow());
        pgr.setVersion(listpage.getVersion());
        return pgr;
    }
    @Override
    public List<BtBankAccVO> listByIds(String[] ids, String curId) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public IPageResult<BtBankAccDTO> getBankAccListByIsOnlineResult(IParameterInput parameterInput) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<String[]> getBankRasAccByAccountDTO(AccountSearchDTO accountSearchDTO) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public BtBankAccDTO getByBankAcc(String bankAcc) {
        BtBankAcc byBankAcc = btBankAccDao.getByBankAcc(bankAcc);
        return BeanUtil.copyProperties(byBankAcc,BtBankAccDTO.class);
    }
    @Override
    public List<BtBankAccVO> listBankAccByEndDate(String searchDate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BtBankAccDTO> listBankAccByDate(String endDate) {
        return btBankAccDao.listBankAccByDate(endDate);
    }

    @Override
    public DataTablesOutput getDataTableOldBankAcc(DataTablesInput dataTablesInput, String joinSql) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<BtBankAccDTO> listBtBankAccBriefInfoBy(String bankAcc, String accName) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public BtBankAccAppDTO getBtbankAccApp(String id) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<BtBankAccDTO> getBankAccNotUnionByIdList(BtBankAccDTO dto) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<BtBankAccDTO> getBankAccDTOList(BtBankAccDTO dto) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<BtBankAccVO> getBankAccVOByIdList(List<String> idList) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public DataTablesOutput getDataTablesUnion1(DataTablesInput dataTablesInput) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<com.bytter.system.api.vo.BtBankAccVO> getListByVO(com.bytter.system.api.vo.BtBankAccVO vo,
                                                                  StringBuilder corpSql) {
        return btBankAccDao.getListByVO(vo, corpSql);
    }
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<BtBankAccBalVO> queryBankAccBalanceById(List<String> bankAccs, List<String> curIds) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<Map<String, Object>> getBankModelByBankAcc(List<String> bankAccList) {
        return btBankAccDao.getBankModelByBankAcc(bankAccList);
    }
    @Override
    public Map<String, BtBankAccDTO> getBillBankAccByBankAccList(List<String> bankAccList) {
        return btBankAccDao.getBillBankAccByBankAccList(bankAccList);
    }
    @Override
    public List<Map<String, Object>> queryVAccListVo(String bankAcc) {
        return btBankAccDao.queryVAccListVo(bankAcc);
    }
    /**
     * <b>功能：</b>查询银行账号信息视图<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据信息
     */
    @Override
    public List<VAccListVo> queryVAccListVoNew(String bifCode) {
        return btBankAccDao.queryVAccListVoNew(bifCode);
    }
    /**
     * <b>功能：</b>查询银行账号信息视图<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据信息
     */
    @Override
    public List<VAccListVo> queryVAccListVoNewByCorp(String bifCode, List<String> poolCorpId) {
        return btBankAccDao.queryVAccListVoNewByCorp(bifCode, poolCorpId);
    }
    @Override
    public boolean getReqAndRecCorpIdById(String id) {
        return btBankAccDao.getReqAndRecCorpIdById(id);
    }
    @Override
    public BillBankBankAccVO getBankAccByAcctId(String acctId) {
        return btBankAccDao.getBankAccByAcctId(acctId);
    }
    @Override
    public DataTablesOutput<BtBankAccVO> getDataTablesUnion2(DataTablesInput dataTablesInput, String joinSql) {
        return btBankAccService.getDataTablesUnion2(dataTablesInput, joinSql);
    }
    @Override
    public List<BtBankAccCurDTO> getCountertAcc(String bankAcc) {
        return btBankAccService.getCountertAcc(bankAcc);
    }
    @Override
    public List<BtBankAccDTO> listByIds(List<String> bankAccIds, List<String> oppAccNoList) {
        return btBankAccService.listByIds(bankAccIds, oppAccNoList);
    }
    @Override
    public List<String> getBankClassByExtAcc(List<String> accList) {
        return btBankAccDao.getBankClassByExtAcc(accList);
    }
    @Override
    public List<String> getBankClassByBankAcc(List<String> accList) {
        return btBankAccDao.getBankClassByBankAcc(accList);
    }
    @Override
    public List<BtBankAccDTO> listByFcBankAccDtl(List<String> corpAuth) {
        return btBankAccDao.listByFcBankAccDtl(corpAuth);
    }
    @Override
    public List<BtBankAccDTO> listBankAccByEnd(String corpId, String bankAcc, String bifCode, String curId,String joinSql) {
        List<BtBankAccVO> listBankAcc = btBankAccService.listBankAccByEnd(corpId, bankAcc, bifCode, curId,joinSql);
        if (EmptyUtils.isEmptyList(listBankAcc)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(listBankAcc, BtBankAccDTO.class);
    }
    public List<BtBankAccDTO> listBankAccBySql(String sql, Map<String, Object> params) {
        List<BtBankAccVO> list = btBankAccDao.listBankAccBySql(sql, params);
        if (EmptyUtils.isEmptyList(list)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(list, BtBankAccDTO.class);
    }

    @Override
    public List<Map<String, Object>> getBankAccByOpType(String bankAcc, String netCode, String natureCode) {
        return btBankAccDao.getBankAccByOpType(bankAcc,netCode,natureCode);
    }

    /**
     * 查询ERP在离线账号
     * @param dataTablesInput
     * @param userId
     * @param bankList
     * @return
     */
    @Override
    public DataTablesOutput getBankAccsErpData(DataTablesInput dataTablesInput, String userId, List<String> bankList) {
        return btBankAccDao.getBankAccsErpData(dataTablesInput,userId,bankList);
    }

    @Override
    public List<BtBankAccDTO> listBankAccByNillHead(List<String> downAccIds,List<String> curIds) {
    	StringBuilder sql = new StringBuilder();
    	sql.append("select c.id,c.bank_acc as downAcc,c.acc_name as downAccName, ");
    	sql.append("pc.bank_acc as downAccPrent,pc.acc_name as downAccNamePrent ");
    	sql.append("from bt_bank_acc c ");
    	sql.append("left join bt_bank_acc_cur bc on c.id  = bc.bank_acc_id ");
    	sql.append("left join bt_bank_acc pc on pc.id = bc.prent_acc_id ");
    	sql.append("where 1=1 ");
		sql.append(" and ").append(SqlUtil.getInSql(downAccIds, "c.id", true));
		sql.append(" and ").append(SqlUtil.getInSql(curIds, "bc.cur_id", true));
        List<BtBankAccVO> list = btBankAccDao.listBankAccBySqlNew(sql.toString(), null);
        if (EmptyUtils.isEmptyList(list)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(list, BtBankAccDTO.class);
    }

    @Override
    public List<BtBankAccDTO> batchSaveEntity(List<BtBankAccDTO> btBankAccDTOList) {
        if(EmptyUtils.isEmpty(btBankAccDTOList)){
            return null;
        }
        List<BtBankAcc> btBankAccList = new ArrayList<BtBankAcc>();
        for(BtBankAccDTO btBankAccDTO:btBankAccDTOList){
            if(!EmptyUtils.isEmpty(btBankAccDTO)){
                BtBankAcc btBankAcc = new BtBankAcc();
                BeanUtils.copyNotNullProperties(btBankAccDTO, btBankAcc);
                btBankAccList.add(btBankAcc);
            }
        }
        this.batchSave(btBankAccList);
        // 返回ID 以便变得地方调用
        String id = "";
        for (BtBankAcc acc : btBankAccList){
            id = acc.getId();
            for (BtBankAccDTO accDTO : btBankAccDTOList){
                if (accDTO.getBankAcc().equals(acc.getBankAcc())){
                    accDTO.setId(acc.getId());
                }
            }
        }
        return btBankAccDTOList;
    }

    /**
     * 根据账号id集合查询账户信息，包括省市信息
     *
     * @param ids 账号id集合
     * @return 账户信息
     */
    @Override
    public Map<String, BtBankAccDTO> getBankAccDTOByIds(List<String> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return MapUtil.empty();
        }
        List<BtBankAccDTO> btBankAccDTOList = this.btBankAccService.getBankAccUnByIds(ids);
        if (EmptyUtils.isEmpty(btBankAccDTOList)) {
            return MapUtil.empty();
        }
        Map<String, BtBankAccDTO> map = new HashMap<>();
        for (BtBankAccDTO dto : btBankAccDTOList) {
            map.put(dto.getId(), dto);
        }
        return map;
    }

    @Override
    public Map<String, BtBankAccDTO> getBankAccInfoByBankAccList(List<String> bankAccList) {
        if (EmptyUtils.isEmpty(bankAccList)) {
            return MapUtil.empty();
        }
        List<BtBankAccDTO> listData = btBankAccDao.getBankAccInfoByBankAccList(bankAccList);
        if (EmptyUtils.isEmpty(listData)) {
            return MapUtil.empty();
        }
        return listData.stream().collect(Collectors.toMap(BtBankAccDTO::getBankAcc, bank -> bank));
    }

    @Override
    public String getBifCodeByBankAcc(String bankAcc) {
        return btBankAccDao.getBifCodeByBankAcc(bankAcc);
    }

    @Override
    public List<BtBankAccDTO> getByGroupCode(String accGroupCode){
        return btBankAccDao.getByGroupCode(accGroupCode);
    }

    @Override
    public int updateBankAccByIds(List<String> ids, Date closeDate) {
        return btBankAccDao.updateBankAccByIds(ids, closeDate);
    }

    @Override
    public void setBtCurrency(BtBankAccVO vo) {
        btBankAccService.setBtCurrency(vo);
    }

    @Override
    public int setCheckBankAccSave(String ids, String checkSign) {
        return btBankAccDao.setCheckBankAccSave(ids, checkSign);
    }

    @Override
    public void saveNisBindingBankac(String bankId, String bindingBankId) {
        nisBindingBankaccDao.saveNisBindingBankacc(bankId,bindingBankId);
    }

    @Override
    public void deleteBybankId(String bankId) {
        nisBindingBankaccDao.deleteBybankId(bankId);
    }

    @Override
    public void deleteZhBailAccount(String bankAcc, ICurrentUser user) {
        BtBankAcc btBankAcc = btBankAccDao.getByBankAcc(bankAcc);
        if(btBankAcc!=null){
            btBankAccService.delete(btBankAcc.getId());
        }
    }

    @Override
    public List<BtBankAccDTO> getExpireBailBankAcc(String curDate) {
        return btBankAccDao.getExpireBailBankAcc(curDate);
    }

    @Override
    public Integer getBankAccUse1004(String id, String corpId) {
        // 用途为“营收账号”【对应用途编码为：1004】的占用
        return btBankAccDao.queryBankAccList("1004",id, corpId);
    }

    /**
     * 根据单位获取有效的营收账号
     * @param corpIds
     * @return
     */
    @Override
    public List<BtBankAccDTO> listBankAcc1004(List<String> corpIds) {
        return btBankAccDao.listBankAcc1004(corpIds);
    }

    @Override
    public void updateBankAccClose(String bankAcc, String isClose) {
        BtBankAccDTO bankAccInfo = this.getBankAccInfo(bankAcc);
        if (ObjectUtil.isNotEmpty(bankAccInfo)){
            btBankAccDao.updateBankAccClose(bankAcc,isClose);
        }
    }
}
