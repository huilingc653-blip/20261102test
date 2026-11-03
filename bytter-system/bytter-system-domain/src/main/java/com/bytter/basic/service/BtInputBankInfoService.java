package com.bytter.basic.service;

import com.bytter.basic.dao.BtInputBankInfoDao;
import com.bytter.basic.entity.BankType;
import com.bytter.basic.entity.BtInputBankInfo;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.BankConstant;
import com.bytter.system.api.dto.BtBankDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行行号维护
 * @date 2017-05-23 19:06:25
 */
@Service("btInputBankInfoServiceBase")
@Transactional
public class BtInputBankInfoService extends BaseService<BtInputBankInfo, String> implements IExcelVerifyHandler<BtInputBankInfoVO>, ITaskService {
    @Autowired
    private BtInputBankInfoDao btInputBankInfoDao;
    @Autowired
    //@Qualifier("btBankTypeServiceBase")
    private BtBankTypeService btBankTypeService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BtInputBankInfo entity) {
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
    public void save(BtInputBankInfo entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BtInputBankInfo entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BtInputBankInfo entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BtInputBankInfo, String> getEntityDao() {
        return btInputBankInfoDao;
    }
    /**
     * 查找是否已有银行行号
     *
     * @return
     */
    public BtInputBankInfo getByBankInfo(String bankCode) {
        return btInputBankInfoDao.getByBankInfo(bankCode);
    }
    /**
     * 查找是否已有银行行号
     *
     * @return
     */
    public List<BtInputBankInfo> getBankInfoForNoBTBank(List<String> bankCode) {
        return btInputBankInfoDao.getBankInfoForNoBTBank(bankCode);
    }
    /**
     * 根据联行号查找对应id
     *
     * @param bankCode
     * @return
     */
    public String getSysBankInfoId(String bankCode) {
        return btInputBankInfoDao.getSysBankInfoId(bankCode);
    }
    /**
     * 根据联行号查找出对应的银行行号信息
     *
     * @param bankCode
     * @return
     */
    public List<BtInputBankInfo> getBtInputBankInfo(String bankCode) {
        return btInputBankInfoDao.getBtInputBankInfo(bankCode);
    }
    /**
     * 查询银行行号信息列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput findListBysql(DataTablesInput input) {
        return btInputBankInfoDao.findListBysql(input);
    }
    /**
     * 根据id查找银行行号信息
     *
     * @param id
     * @return
     */
    public BtInputBankInfoVO getByUnion(String id) {
        BtInputBankInfoVO a = btInputBankInfoDao.getByUnion(id);
        return a;
    }
    @Override
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = btInputBankInfoDao.getDataTables(dataTablesInput);
        return dataTablesOutput;
    }
    /**
     * 周亮
     *
     * @param bankCode
     * @param bankName
     * @return
     */
    public BtInputBankInfoVO getByBankCodeOrBankName(String bankCode, String bankName) {
        if (EmptyUtils.isBlank(bankCode) && EmptyUtils.isBlank(bankName)) {
            return null;
        }
        if (EmptyUtils.isBlank(bankCode)) {
            return btInputBankInfoDao.getByBankCodeOrBankName(bankCode, bankName);
        } else {
            if (bankCode.indexOf("313") > -1 || bankCode.indexOf("402") > -1 || bankCode.indexOf("314") > -1) {
                return btInputBankInfoDao.getByBankCodeOrBankNameAndImport(bankCode, bankName);
            } else {
                return btInputBankInfoDao.getByBankCodeOrBankName(bankCode, bankName);
            }
        }
    }
    /**
     * 根据联行号，开户行得到联行号信息
     *
     * @param bankCode
     * @param bankName
     * @return
     */
    public BtInputBankInfoVO getByBankCode(String bankCode, String bankName) {
        if (EmptyUtils.isBlank(bankCode) && EmptyUtils.isBlank(bankName)) {
            return null;
        }
        if (EmptyUtils.isBlank(bankCode)) {
            return btInputBankInfoDao.getByBankCodeOrBankName(bankCode, bankName);
        } else {
            if (bankCode.indexOf("313") > -1 || bankCode.indexOf("402") > -1 || bankCode.indexOf("314") > -1) {
                return btInputBankInfoDao.getByBankCodeOrBankNameAndImport(bankCode, bankName);
            } else {
                return btInputBankInfoDao.getByBankCodeOrBankName(bankCode, bankName);
            }
        }
    }
    public BtInputBankInfo getBtInputBankInfoByParams(Map<String, Object> params) {
        return btInputBankInfoDao.getBtInputBankInfoByParams(params);
    }
    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<BtInputBankInfoVO> list, ICurrentUser user) {
        List<BtInputBankInfo> entityList = new ArrayList<BtInputBankInfo>();
        String loginName = user.getLoginName();
        Date curDate = DateUtils.getDate();
        for (BtInputBankInfoVO vo : list) {
            BtInputBankInfo btInputBankInfo = new BtInputBankInfo();
            BeanUtils.copyNotNullProperties(vo, btInputBankInfo);
            btInputBankInfo.setCreateBy(loginName);
            btInputBankInfo.setCreateDate(curDate);
            btInputBankInfo.setIbpsCode(BankConstant.IBPS_CODE_IMPORT);
            entityList.add(btInputBankInfo);
            chechSave(btInputBankInfo);
        }
    }
    public DataTablesOutput getDataTablesPrefix(DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = btInputBankInfoDao.getDataTablesPrefix(dataTablesInput);
        return dataTablesOutput;
    }
    public BtInputBankInfoVO getBankPrefix(String bankSourceCode) {
        return btInputBankInfoDao.getBankPrefix(bankSourceCode);
    }
    /**
     * 根据联行号是否在系统中存在来新增和更新信息
     *
     * @param btInputBankInfo
     */
    public void chechSave(BtInputBankInfo btInputBankInfo) {
        BtInputBankInfo info = this.getByBankInfo(btInputBankInfo.getBankCode());
        if (null == info) {
            // 初始化时，给个变更值为初始值
            btInputBankInfo.setUpdateBy(btInputBankInfo.getCreateBy());
            btInputBankInfo.setUpdateDate(btInputBankInfo.getCreateDate());
            this.save(btInputBankInfo);
        } else {
            BeanUtils.copyNotNullProperties(btInputBankInfo, info, "id");
            this.update(info);
        }
    }
    /**
     * 根据联行号截取的值判断数据的正确性
     *
     * @param bankCode
     */
    public void prefixCityExist(String bankCode) {
        bankCode = bankCode.trim();
        String bankPrefix = bankCode.substring(0, 3);
        String bankCityCode = bankCode.substring(3, 7);
        List<BankType> bankPrefixFlag = btBankTypeService.listBySignOrPrefix("1", bankPrefix);
        boolean bankCityCodeFlag = btBankTypeService.bankCityCodeIsExist(bankCityCode);
        if (EmptyUtils.isEmptyList(bankPrefixFlag)) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKPREFIX_ISEXIST, bankCode);
        }
        if (bankCityCodeFlag == false) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKCITYCODE_ISEXIST, bankCode);
        }
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(BtInputBankInfoVO vo) {
        return null;
    }
    @Override
    public void verifyHandler(List<BtInputBankInfoVO> list) {
        Map<String, Boolean> prefixMap = new HashMap<>();
        Map<String, Boolean> cityCodeMap = new HashMap<>();
        for (BtInputBankInfoVO vo : list) {
            List<String> msgList = new ArrayList<>();
            String bankCode = vo.getBankCode();
            if (!EmptyUtils.isBlank(bankCode)) {
                bankCode = bankCode.trim();
                if (bankCode.length() < 12) {
                    msgList.add("basic.business.message.sysbankcode.wrong");
                }
                String bankPrefix = bankCode.substring(0, 3);
                vo.setBankPrefix(bankPrefix);
                if (!prefixMap.containsKey(bankPrefix)) {
                    List<BankType> bankTypeList = btBankTypeService.listBySignOrPrefix("1", bankPrefix);
                    prefixMap.put(bankPrefix, !EmptyUtils.isEmptyList(bankTypeList));
                }
                if (!prefixMap.get(bankPrefix)) {
                    msgList.add("basic.business.message.bankprefix.isexist");
                }
                if (bankCode.length() >= 12) {
                    String bankCityCode = bankCode.substring(3, 7);
                    vo.setBankCityCode(bankCityCode);
                    if (!cityCodeMap.containsKey(bankCityCode)) {
                        boolean bankCityCodeFlag = btBankTypeService.bankCityCodeIsExist(bankCityCode);
                        cityCodeMap.put(bankCityCode, bankCityCodeFlag);
                    }
                    if (!cityCodeMap.get(bankCityCode)) {
                        msgList.add("basic.business.message.bankcitycode.isexist");
                    }
                }
            }
            vo.addErrorMsg(msgList);
        }
    }
    /**
     * 根据联行号或开户行名称获取联行号信息
     *
     * @param bankCode
     * @param bankCode
     * @return
     */
    public BtInputBankInfoVO getByBankCodeOrBankNameAndImport(String bankCode, String bankName) {
        if (EmptyUtils.isBlank(bankCode) && EmptyUtils.isBlank(bankName)) {
            return null;
        }
        return this.btInputBankInfoDao.getByBankCodeOrBankNameAndImport(bankCode, bankName);
    }
    /**
     * 联行号表和客商账户表/银行账号表行名不一样同步任务
     *
     * @param arguments 是否修改客商账户户名=true/false,是否修改银行账户户名=true/false 如：externalAcc=true,bankAcc=true
     */
    @Override
    @XxlJob("BtInputBankInfoService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        logger.info("定时任务接收参数：【{}】", arguments);
        this.logger.info("---自动执行行号和行名不一样的客商账户和银行账号信息表的行名，开始执行定时任务---");
        try {
            if (!StringUtils.isEmpty(arguments)) {
                String[] argumentsArray = arguments.split(",");
                for (String argumentsInfo : argumentsArray) {
                    if (StringUtils.isNotEmpty(argumentsInfo)
                            && argumentsInfo.endsWith("=true")) {
                        int updateNum = 0;
                        if (argumentsInfo.startsWith("externalAcc")) {
                            //是否修改客商账户户名为externalAcc=true时进行设置客商账号对应的银行行名异常变更
                            this.logger.info("进行设置客商账号对应的银行行名异常变更，开始执行---");
                            updateNum = btInputBankInfoDao.updateExternalAccBankNameByInputBankInfo();
                            this.logger.info("进行设置客商账号对应的银行行名异常变更，修改成功【" + updateNum + "】笔");
                            this.logger.info("进行设置客商账号对应的银行行名异常变更，结束执行---");
                        } else if (argumentsInfo.startsWith("bankAcc")) {
                            //是否修改银行账户户名为bankAcc=true时进行设置银行账号对应的银行行名异常变更
                            this.logger.info("进行设置银行账号对应的银行行名异常变更，开始执行---");
                            updateNum = btInputBankInfoDao.updateBankAccBankNameByInputBankInfo();
                            this.logger.info("进行设置银行账号对应的银行行名异常变更，修改成功【" + updateNum + "】笔");
                            this.logger.info("进行设置银行账号对应的银行行名异常变更，结束执行---");
                        }
                    }
                }
            } else {
                this.logger.info("参数为空不执行指定任务处理！");
            }
            this.logger.info("---自动执行行号和行名不一样的客商账户和银行账号信息表的行名，结束执行定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    /**
     * 根据银行代码集合查询联行号信息，包括省市信息
     *
     * @param bankCodes
     * @return 联行信息
     */
    public List<InputBankInfoDTO> getBankCodeInfoByBankCodes(List<String> bankCodes) {
        return this.btInputBankInfoDao.getBankCodeInfoByBankCodes(bankCodes);
    }
    
    public List<InputBankInfoDTO> getBankCodeInfoByBankNames(List<String> bankNames) {
        return this.btInputBankInfoDao.getBankCodeInfoByBankNames(bankNames);
    }
    
    public List<InputBankInfoDTO> getBankListCheck(List<String> bankCodes, String bankPrefix) {
        return btInputBankInfoDao.getBankListCheck(bankCodes, bankPrefix);
    }
    /**
     * 融资服务中通过银行名称获取银行信息
     * @param bankName
     * @return
     */
    public BtBankDTO getByBankName(String bankName) {
        return btInputBankInfoDao.getByBankName(bankName);
    }
    public BtBankDTO getBankName(String bankCode) {
        return btInputBankInfoDao.getBankName(bankCode);
    }

    public List<InputBankInfoDTO> getBankCodeInfoByBankTypeIds(List<String> bankTypeIds){
        if(bankTypeIds == null || bankTypeIds.size() <= 0){
            return null;
        }
        return btInputBankInfoDao.getBankCodeInfoByBankTypeIds(bankTypeIds);
    }
    
}
