package com.bytter.basic.service;
import com.bytter.basic.dao.BtBankTypeDao;
import com.bytter.basic.entity.BankType;
import com.bytter.basic.vo.BankTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.service.SysAttachmentService;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行类型维护
 * @date 2017-05-17 18:07:33
 * todo 定时任务扫描类型维护
 */
@Service("btBankTypeServiceBase")
@Transactional
public class BtBankTypeService extends BaseService<BankType, String> implements ITaskService {
    @Autowired
    private BtBankTypeDao btBankTypeDao;
    @Autowired
    private IConfigService configServiceI;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private Ii18nService i18nServiceI;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private IDictService dictServiceI;
    @Override
    public BaseDao<BankType, String> getEntityDao() {
        return btBankTypeDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = btBankTypeDao.getDataTablesUnion(dataTablesInput, "");
        if (dataTablesOutput != null && dataTablesOutput.getData() != null) {
            List<BankTypeVO> bankTypeList = dataTablesOutput.getData();
            for (BankTypeVO bankType : bankTypeList) {
                if (!StringUtils.isEmpty(bankType.getRemitMethod())) {
                    bankType.setRemitMethodName(this.getRemitMethodName(bankType.getRemitMethod()));
                }
                bankType.setValidSignName(!StringUtils.isEmpty(bankType.getValidSign()) && CommonConstant.NO.equals(bankType.getValidSign()) ? CommonConstant.STRING_NO : CommonConstant.STRING_YES);
            }
        }
        return dataTablesOutput;
    }
    public String getRemitMethodName(String remitMethod) {
        String remitMethodName = "";
        if (!StringUtils.isEmpty(remitMethod)) {
            String[] remitMethods = remitMethod.split(CommonConstant.COMMA_SPLIT);
            for (String rm : remitMethods) {
                if (StringUtils.isEmpty(remitMethodName)) {
                    remitMethodName = dictServiceI.get("fexForeignReq.remitMethod", rm);
                } else {
                    remitMethodName = remitMethodName + CommonConstant.COMMA_SPLIT + dictServiceI.get("fexForeignReq.remitMethod", rm);
                }
            }
        }
        return remitMethodName;
    }
    /**
     * 保存银行类型
     *
     * @param bankType
     * @param fileId
     */
    public void saveBankType(BankType bankType, String fileId) {
        // icon图标
        if (StringUtils.hasText(fileId)) {
            byte[] iconBytes = sysAttachmentService.getAttachmentDataByid(fileId);
            bankType.setIcon("data:image/png;base64," + Base64Utils.encodeToString(iconBytes));
            sysAttachmentService.deleteAttachment(fileId);
        }
        saveEntity(bankType);
    }
    public void updateBankType(BankType bankType, String fileId) {
        // icon图标
        if (StringUtils.hasText(fileId)) {
            byte[] iconBytes = sysAttachmentService.getAttachmentDataByid(fileId);
            bankType.setIcon("data:image/png;base64," + Base64Utils.encodeToString(iconBytes));
            sysAttachmentService.deleteAttachment(fileId);
        }
        updateEntity(bankType);
    }
    /**
     * 获取有效的银行列表信息
     *
     * @param
     */
    public List<BankType> getListByValidSign(String validSign) {
        List<BankType> bankTypes;
        if (StringUtils.isEmpty(validSign)) {
            bankTypes = btBankTypeDao.listAll();
        } else {
            bankTypes = btBankTypeDao.listBy("validSign", validSign);
        }
        for (BankType bankType : bankTypes) {
            btBankTypeDao.evict(bankType);
            String businessBaseContext = i18nServiceI.getBusinessBaseContext(bankType.getId());
            if (StringUtils.hasText(businessBaseContext)) {
                bankType.setTypeName(businessBaseContext);
            }
        }
        return bankTypes;
    }
    /**
     * 根据是否境内获取有效的银行类别
     *
     * @param
     */
    public List<BankType> getListByIsDomesticBank(String isDomesticBank) {
        if (isDomesticBank == null) {
            return null;
        }
        List<BankType> listByValidSign = this.getListByValidSign("1");
        List<BankType> listByIsDomestic = new ArrayList<>();
        for (BankType bankType : listByValidSign) {
            if (isDomesticBank.equals(bankType.getIsDomesticBank())) {
                listByIsDomestic.add(bankType);
            }
        }
        return listByIsDomestic;
    }
    /**
     * 根据编码获取银行类型
     *
     * @param bankType
     * @return
     */
    public BankType findByType(String bankType) {
        return btBankTypeDao.findByType(bankType);
    }
    /**
     * 判断是否存在多条
     *
     * @param bankType
     * @param opType
     * @param id
     * @return
     */
    public boolean isExistMulByBankType(String bankType, String opType, String id) {
        boolean flag = false;
        List<BankType> list = btBankTypeDao.listByBankType(bankType);
        if (list != null) {
            if (CommonConstant.SUBMIT_ADD.equals(opType) && list.size() > 0) {
                flag = true;
            } else if (CommonConstant.SUBMIT_MODIFY.equals(opType)) {
                if (list.size() > 0) {
                    if (!id.equals(list.get(0).getId())) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
    /**
     * 判断是否存在多条
     *
     * @param bankType
     * @param opType
     * @return
     */
    public boolean isExistMulByBankPrefix(BankType bankType, String opType) {
        if (!"313".equals(bankType.getBankPrefix()) && !"402".equals(bankType.getBankPrefix()) && !"314".equals(bankType.getBankPrefix())) {
            return false;
        }
        boolean flag = false;
        List<BankType> list = btBankTypeDao.listByBankPrefix(bankType.getBankPrefix(), bankType.getBankNamePrefix());
        if (!CollectionUtils.isEmpty(list)) {
            if (CommonConstant.SUBMIT_ADD.equals(opType) && list.size() > 0) {
                flag = true;
            } else if (CommonConstant.SUBMIT_MODIFY.equals(opType)) {
                if (list.size() > 0) {
                    if (!bankType.getId().equals(list.get(0).getId())) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
    /**
     * 根据编码获取银行类型
     *
     * @param id
     * @return
     */
    public BankType findById(String id) {
        return btBankTypeDao.get(id, true);
    }
    /**
     * 根据有效标志，人行号前缀过滤
     *
     * @param validSign
     * @param bankPrefix
     * @return
     */
    public List<BankType> listBySignOrPrefix(String validSign, String bankPrefix) {
        if (StringUtils.isEmpty(validSign)) {
            return btBankTypeDao.listBy("bankPrefix", bankPrefix);
        } else {
            return btBankTypeDao.listBySignOrPrefix(validSign, bankPrefix);
        }
    }
    /**
     * 判断银行城市id是否存在
     */
    public boolean bankCityCodeIsExist(String bankCityCode) {
        return btBankTypeDao.bankCityCodeIsExist(bankCityCode);
    }
    public BankType getByIdCache(String id) {
        return btBankTypeDao.getByIdCache(id);
    }
    /**
     * 定时任务
     * 网银证书到期提醒
     *
     * @author qinl
     * @date 2019-07-04
     */
    @Override
    @XxlJob("BtBankTypeService")
    public void run(String arguments) {
        Date curDate = DateUtils.getDate();
        this.logger.info(curDate + "---网银证书到期提醒，开始执行定时任务---");
        try {
            //获取参数配置对象
            ConfigDTO configDTO = configServiceI.getConfig(ConfigConstant.SYS, "netbankCfcaDateAlertDays");
            //年检到期提醒时间
            Integer netbankCfcaDateAlertDays = EmptyUtils.isBlank(configDTO.getValue()) ? 0 : Integer.parseInt(configDTO.getValue());
            String searchDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(), netbankCfcaDateAlertDays), DateUtils.PATTERN_CLASSICAL_SIMPLE);
            //到期账户集合
            List<BankType> bankTypeList = btBankTypeDao.listByCfcaDateAlertDays(searchDate);
            //消息发送
            if (CollectionUtils.isEmpty(bankTypeList)) {
                this.logger.info("---网银证书到期的数量为0---");
                notificationService.completeListByTag(NotificationConstant.BASESYS.BANKACC_CFCA_DATE);
            } else {
                Map<String, Integer> noticeMap = new HashMap<>();
                noticeMap.put("count", bankTypeList.size());
                notificationService.completeListByTag(NotificationConstant.BASESYS.BANKACC_CFCA_DATE);
                notificationService.sendToUserByResPermission(SysResourceConstant.BANK_TYPE_QUERY, NotificationConstant.BASESYS.BANKACC_CFCA_DATE, noticeMap);
//                notificationService.send(bankTypeList.get(0).getId(),null,NotificationConstant.BASESYS.BANKACC_CFCA_DATE,noticeMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.logger.info("---网银证书到期提醒，结束执行定时任务---");
    }
    /**
     * 根据银行类别名称查找
     *
     * @param typeName
     * @return
     */
    public List<BankType> getByTypeName(String typeName) {
        List<BankType> bankTypeList = this.btBankTypeDao.listBy("typeName", typeName);
        return bankTypeList;
    }
    /**
     * 根据账号过滤银行类别
     *
     * @return
     */
    public List<BankTypeVO> listBankTypeFilterByBankAcc() {
        return this.btBankTypeDao.listBankTypeFilterByBankAcc();
    }
    public List<BankType> getByBankTypeNames(Set<String> bankTypeNames) {
        return this.btBankTypeDao.listByBankTypeNames(bankTypeNames);
    }
    /**
     * 根据id查找银行类别名称
     * @param ids
     * @return
     */
    public List<String> getBankTypeNameByIds(List<String> ids) {
        return btBankTypeDao.getBankTypeNameByIds(ids);
    }
    public List<BankType> getBankTypeNameAndBankTypeByIds(List<String> ids) {
        return btBankTypeDao.getBankTypeNameAndBankTypeByIds(ids);
    }
    public String getBankTypeId(Map<String, Object> params, Integer interiorApply) {
        StringBuilder sql = new StringBuilder("");
        if (interiorApply == 1) {
            sql.append(" select  bt.id")
                    .append(" from bt_input_bank_info bi")
                    .append(" left join bt_bank_type bt")
                    .append(" on bt.bank_prefix = bi.bank_prefix")
                    .append(" and (bt.bank_name_prefix is null or")
                    .append(" bi.bank_name like bt_concat2(bt.bank_name_prefix, '%'))")
                    .append(" where bt.id is not null")
                    .append(" and bi.sys_bank_code =:bankCode");
        } else {
            sql.append(" select banktype.id from  bt_bank_acc acc")
                    .append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id")
                    .append(" where acc.bank_acc =:pledgeeAcc");
        }
        @SuppressWarnings("unchecked")
        List<String> bankTypeIds = this.getSession().createSQLQuery(sql.toString()).setProperties(params).list();
        return ListUtils.isNullOrEmpty(bankTypeIds) ? null : bankTypeIds.get(0);
    }
}
