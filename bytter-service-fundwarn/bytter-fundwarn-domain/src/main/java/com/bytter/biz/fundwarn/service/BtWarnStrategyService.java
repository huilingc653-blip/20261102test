package com.bytter.biz.fundwarn.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.fundwarn.dao.BtWarnConditionDao;
import com.bytter.biz.fundwarn.dao.BtWarnStrategyDao;
import com.bytter.biz.fundwarn.dao.BtWarningLogDao;
import com.bytter.biz.fundwarn.entity.BtWarnCondition;
import com.bytter.biz.fundwarn.entity.BtWarnStrategy;
import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.biz.fundwarn.vo.BtWarnConditionVO;
import com.bytter.biz.fundwarn.vo.BtWarnStrategyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: Service
 * @Description: 预警策略业务处理类
 * @date 2018-09-27
 */
@Service("btWarnStrategyService")
@Transactional
public class BtWarnStrategyService extends BaseService<BtWarnStrategy, String> implements ICorpDeleteVerifyService, DictInjectProvider {
    @Autowired
    private BtWarnStrategyDao btWarnStrategyDao;
    @Autowired
    private BtWarningLogDao btWarningLogDao;
    @Autowired
    private BtWarnConditionDao btWarnConditionDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private FundWarnTaskService fundWarnTaskService;

    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<BtWarnStrategy, String> getEntityDao() {
        return btWarnStrategyDao;
    }

    /**
     * 加载预警策略管理数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listWarnStrategy(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuthList = corpService.getUserCorpIds(FundWarnConstant.RESOURCE_WARNSTRATEGY_QUERY, dataTablesInput.getCurrentUserId());
        DataTablesOutput<BtWarnStrategyVO> dataTablesOutput = btWarnStrategyDao.listWarnStrategy(dataTablesInput, corpAuthList, user.getTenantId());
        if (dataTablesOutput.getData().size() > 0){
            List<BtWarnStrategyVO> list = dataTablesOutput.getData();
            for (BtWarnStrategyVO item : list){
                if (StringUtils.isNotBlank(item.getNetId())){
                    Map<String, CorpDTO> map = corpService.getCorpMap();
                    String[] netIds = item.getNetId().split(CommonConstant.COMMA_SPLIT);
                    List<String> netNameList = new ArrayList<>();
                    for (String netId : netIds) {
                        CorpDTO corpDTO = map.get(netId);
                        if (corpDTO != null && StringUtils.isNotBlank(corpDTO.getName())) {
                            netNameList.add(corpDTO.getName());
                        }
                    }
                    // 拼接成逗号分隔的网点名称字符串
                    String netNames = String.join(CommonConstant.COMMA_SPLIT, netNameList);
                    item.setNetName(netNames);
                }
            }
        }
        return dataTablesOutput;
    }

    /**
     * 根据单位ID集合查询数据
     *
     * @param corpIds
     * @return
     * @author zhenghj 2018-10-12
     */
    public DataTablesOutput<CorpDTO> getDataTablesCorpByCorpIds(DataTablesInput dataTablesInput, List<String> corpIds) {
        DataTablesOutput<CorpDTO> output = btWarnStrategyDao.getDataTablesCorpByCorpIds(dataTablesInput, corpIds);
        return output;
    }

    /**
     * 根据条件查询没有删除且是有效的银行账户
     *
     * @param dataTablesInput
     * @param bankacc
     * @return
     */
    public DataTablesOutput getNotDeleteAndValidAndBankAcc(DataTablesInput dataTablesInput, List<String> bankacc) {
        return btWarnStrategyDao.getNotDeleteAndValidAndBankAcc(dataTablesInput, bankacc);
    }

    /**
     * 根据内部账号和币别查询状态正常的内部账号数据
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcAccDTO> getblnctrAccByAccAndcurId(DataTablesInput dataTablesInput, List<String> acc) {
        DataTablesOutput<FcAccDTO> output = btWarnStrategyDao.getblnctrAccByAccAndcurId(dataTablesInput, acc);
        if (output.getData() != null) {
            List<FcAccDTO> oldList = output.getData();
            List<String> netIds = oldList.stream()
                    .map(FcAccDTO::getNetId)
                    .distinct()
                    .collect(Collectors.toList());

            Map<String, String> netIdMap = corpService.getNameByNetIds(netIds);
            // Map<String, String> corpIdMap =  corpService.getNameByIds(oldCorpIds);
            for (FcAccDTO item : oldList) {
                item.setNetName(netIdMap.get(item.getNetId()));
                // item.setCorpName(corpIdMap.get(item.getCorpId()));
            }
        }

        return output;
    }

    /**
     * 判断策略代码是否重复
     *
     * @param warnCode 策略代码
     * @return 存在数据返回true否则false
     */
    @Transactional(readOnly = true)
    public boolean checkWarnCode(String warnCode, String tenantId) {
        boolean flag = false;
        if (StringUtils.isNotEmpty(warnCode)) {
            flag = btWarnStrategyDao.checkWarnCode(warnCode, tenantId);
        }

        if (flag) {
            return true;
        }
        return false;
    }


    /**
     * 新增预警策略
     *
     * @param strategyVO
     * @param conditionVO
     * @param userVo
     */
    public void save(BtWarnStrategyVO strategyVO, BtWarnConditionVO conditionVO, ICurrentUser userVo) {
        BtWarnStrategy strategy = new BtWarnStrategy();
        BeanUtils.copyProperties(strategyVO, strategy, "tenantId");
        strategy.setCreateBy(userVo.getLoginName());
        strategy.setCreateName(userVo.getUsername());
        strategy.setCreateDate(new Date());
        strategy.setUpdateBy(userVo.getUsername());
        strategy.setUpdateDate(new Date());
        strategy.setUpdateName(userVo.getUsername());
        //如果已经保存客户号，那么截取掉重新保存
        String name = strategyVO.getName();
        if (name.contains("(客户号:")) {
            name = name.substring(0, name.indexOf("(客户号:"));
        }
        //String tenantCode = btWarnStrategyDao.getTenantCodeById(strategy.getTenantId());
        //strategy.setName(name+"(客户号:"+tenantCode+")");
        btWarnStrategyDao.save(strategy);//保存预警策略主信息

        //循环添加预警策略条件,批量保存
        List<BtWarnCondition> warnConditionList = new ArrayList<>();
        //单位Id、银行账号、内部账户列表选择
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), strategy.getDimensionCode(), conditionVO.getCorpIdAndAccStr());
        //业务属性选择：银行账号属性
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCATTRIBUTE, conditionVO.getBankAttStr());
        //业务属性选择：银行账号类别
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCTYPE, conditionVO.getBankAccTypeStr());
        //业务属性选择：银行账号性质
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCNATURE, conditionVO.getBankAccOnlineStr());
        //业务属性选择：内部账户性质
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_FCACCNATURE, conditionVO.getBankAccNatureStr());
        //业务属性选择：会计科目
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_ACCSUBJECT, conditionVO.getSubjectNameStr());
        //逾期账款比例大于
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_WARNCONDITIONRATE, conditionVO.getWarnConditionRate());
        //客商信用等级
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_RATINGSELECTION, conditionVO.getRatingSelection());
        //已逾期天数达到X天在当天预警
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_DATENUM, conditionVO.getDateNum());
        //业务属性选择：计划周期
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_TIME_TYPE, conditionVO.getTimeType());

        int size = 0;
        if (!warnConditionList.isEmpty()) {
            size = btWarnConditionDao.batchSave(warnConditionList);
            // btWarnConditionDao.flush();
        }
    }


    /**
     * 修改预警策略
     *
     * @param strategyVO
     * @param conditionVO
     * @param userVo
     */
    public boolean modifyWarnStrategyCondition(BtWarnStrategyVO strategyVO, BtWarnConditionVO conditionVO, ICurrentUser userVo) {
        BtWarnStrategy strategy = btWarnStrategyDao.get(strategyVO.getId(), true);
        strategyVO.setTaskCron(strategy.getTaskCron());
        strategyVO.setTaskId(strategy.getTaskId());
        String typeCode = strategyVO.getTypeCode();
        if(!StringUtils.contains(new String[]{FundWarnConstant.TYPECODE_CASHPAY, FundWarnConstant.TYPECODE_DAYSUMPAY,
                FundWarnConstant.TYPECODE_EQCORPPAY, FundWarnConstant.TYPECODE_BANKACCPAY}, typeCode)){
            strategyVO.setStatus(strategy.getStatus());
        }
        strategyVO.setCreateBy(strategy.getCreateBy());
        strategyVO.setCreateName(strategy.getCreateName());
        strategyVO.setCreateDate(strategy.getCreateDate());
        BeanUtils.copyProperties(strategyVO, strategy);
        strategy.setUpdateBy(userVo.getLoginName());
        strategy.setUpdateName(userVo.getUsername());
        strategy.setUpdateDate(new Date());
        //如果已经保存客户号，那么截取掉重新保存
        String name = strategyVO.getName();
        if (name.contains("(客户号:")) {
            name = name.substring(0, name.indexOf("(客户号:"));
        }
        //String tenantCode = btWarnStrategyDao.getTenantCodeById(strategy.getTenantId());
        //strategy.setName(name+"(客户号:"+tenantCode+")");
        btWarnStrategyDao.update(strategy);//更新预警策略主信息

        //删除原来的预警策略条件
        btWarnConditionDao.deleteCondtionByWarnId(strategyVO.getId());
        //循环添加预警策略条件,批量保存
        List<BtWarnCondition> warnConditionList = new ArrayList<>();
        //单位Id、银行账号、内部账户列表选择
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), strategy.getDimensionCode(), conditionVO.getCorpIdAndAccStr());
        //业务属性选择：银行账号属性
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCATTRIBUTE, conditionVO.getBankAttStr());
        //业务属性选择：银行账号类别
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCTYPE, conditionVO.getBankAccTypeStr());
        //业务属性选择：银行账号性质
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCNATURE, conditionVO.getBankAccOnlineStr());
        //业务属性选择：内部账户性质
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_FCACCNATURE, conditionVO.getBankAccNatureStr());
        //业务属性选择：会计科目
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_ACCSUBJECT, conditionVO.getSubjectNameStr());
        //逾期账款比例大于
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_WARNCONDITIONRATE, conditionVO.getWarnConditionRate());
        //客商信用等级
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_RATINGSELECTION, conditionVO.getRatingSelection());
        //已逾期天数达到X天在当天预警
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_DATENUM, conditionVO.getDateNum());
        //业务属性选择：计划周期
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_TIME_TYPE, conditionVO.getTimeType());
        int size = 0;
        if (!warnConditionList.isEmpty()) {
            size = btWarnConditionDao.batchSave(warnConditionList);
            //btWarnConditionDao.flush();
        }
        return true;
    }

    /**
     * 复制预警策略
     *
     * @param strategyVO
     * @param conditionVO
     * @param userVo
     */
    public void copy(BtWarnStrategyVO strategyVO, BtWarnConditionVO conditionVO, ICurrentUser userVo) {
        BtWarnStrategy strategy = new BtWarnStrategy();
        BeanUtils.copyProperties(strategyVO, strategy, "tenantId");
        strategy.setCreateBy(userVo.getLoginName());
        strategy.setCreateName(userVo.getUsername());
        //strategy.setTenantId(userVo.getTenantId());
        strategy.setCreateDate(new Date());
        //如果已经保存客户号，那么截取掉重新保存
        String name = strategyVO.getName();
        if (name.contains("(客户号:")) {
            name = name.substring(0, name.indexOf("(客户号:"));
        }
        //String tenantCode = btWarnStrategyDao.getTenantCodeById(strategy.getTenantId());
        //strategy.setName(name+"(客户号:"+tenantCode+")");
        btWarnStrategyDao.save(strategy);//保存预警策略主信息

        //循环添加预警策略条件,批量保存
        List<BtWarnCondition> warnConditionList = new ArrayList<>();
        //单位Id、银行账号、内部账户列表选择
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), strategy.getDimensionCode(), conditionVO.getCorpIdAndAccStr());
        //业务属性选择：银行账号属性
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCATTRIBUTE, conditionVO.getBankAttStr());
        //业务属性选择：银行账号类别
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCTYPE, conditionVO.getBankAccTypeStr());
        //业务属性选择：银行账号性质
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCNATURE, conditionVO.getBankAccOnlineStr());
        //业务属性选择：内部账户性质
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_FCACCNATURE, conditionVO.getBankAccNatureStr());
        //业务属性选择：会计科目
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_ACCSUBJECT, conditionVO.getSubjectNameStr());
        //逾期账款比例大于
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_WARNCONDITIONRATE, conditionVO.getWarnConditionRate());
        //客商信用等级
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_RATINGSELECTION, conditionVO.getRatingSelection());
        //已逾期天数达到X天在当天预警
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_DATENUM, conditionVO.getDateNum());

        //业务属性选择：计划周期
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_TIME_TYPE, conditionVO.getTimeType());
        int size = 0;
        if (!warnConditionList.isEmpty()) {
            size = btWarnConditionDao.batchSave(warnConditionList);
        }
        //添加扫描参数 不添加 复制的让他自己去添加扫描参数
        // fundWarnTaskService.saveOrUpdateWarnTaskInfo(strategy,userVo);
        // btWarnStrategyDao.update(strategy);//更新预警策略主信息

    }

    /**
     * 显示编辑的预警策略数据
     *
     * @param warnId
     * @return
     */
    public BtWarnStrategyVO getBtWarnStrategyInfo(String warnId) {
        if (StringUtils.isEmpty(warnId)) {
            return null;
        }
        BtWarnStrategyVO warnStrategyVO = new BtWarnStrategyVO();
        BtWarnStrategy warnStrategy = btWarnStrategyDao.get(warnId, true);
        BeanUtils.copyProperties(warnStrategy, warnStrategyVO);
        return warnStrategyVO;
    }

    /**
     * 预警策略扫描参数维护
     *
     * @param strategyVO
     * @param userVo
     */
    public boolean saveScanParameter(BtWarnStrategyVO strategyVO, ICurrentUser userVo) {
        BtWarnStrategy strategy = btWarnStrategyDao.get(strategyVO.getId());
        strategy.setStatus(strategyVO.getStatus());
//        strategy.setTaskId(strategyVO.getTaskId());
        strategy.setTaskCron(strategyVO.getTaskCron());
        strategy.setForwardTime(strategyVO.getForwardTime());
        fundWarnTaskService.saveOrUpdateWarnTaskInfo(strategy, userVo);
        btWarnStrategyDao.update(strategy);//更新预警策略主信息
        return true;
    }

    /**
     * 预警策略扫描参数手动执行
     *
     * @param strategyVO
     */
    public boolean executeScan(BtWarnStrategyVO strategyVO) {
        BtWarnStrategy strategyInfo = btWarnStrategyDao.get(strategyVO.getId());
        BtWarnStrategy strategy = new BtWarnStrategy();
        BeanUtils.copyProperties(strategyInfo, strategy);
        strategy.setCurrentUser(strategyVO.getCurrentUser());
        strategy.setRunDate(strategyVO.getRunDate());
        strategy.setForwardTime(strategyVO.getForwardTime());
        try {
            fundWarnTaskService.executeTaskStrategy(strategy);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据预警策略ID获取预警策略条件表数据
     *
     * @param warnId
     * @return
     */
    public List<BtWarnCondition> getWarnConditionByWarnId(String warnId) {
        List<BtWarnCondition> warnConditions = btWarnConditionDao.getWarnConditionByWarnId(warnId);
        return warnConditions;
    }

    /**
     * 获取前端传过来的预警策略条件信息处理为list
     *
     * @param warnConditionList 预警策略条件List
     * @param warnId            预警id
     * @param dataType          数据类型（corp单位、bankAcc银行账户、fcAcc内部账户、bankAccAttribute银行账号属性、bankAccType银行账号类别、bankAccNature银行账号性质、fcAccNature内部账户性质、accSubject会计科目）
     * @param dataValue         数据值，选择为全部时值为null
     * @return
     */
    public List<BtWarnCondition> getConditionListByVo(List<BtWarnCondition> warnConditionList, String warnId, String dataType, String dataValue) {

        if ("all".equals(dataValue)) {
            BtWarnCondition condition = new BtWarnCondition();
            condition.setWarnId(warnId);
            condition.setDataType(dataType);
            condition.setDataValue(null);
            warnConditionList.add(condition);
        } else if (StringUtils.isNotEmpty(dataValue)) {
            String[] values = dataValue.split(",");
            for (String value : values) {
                BtWarnCondition condition = new BtWarnCondition();
                condition.setWarnId(warnId);
                condition.setDataType(dataType);
                condition.setDataValue(value);
                warnConditionList.add(condition);
            }
        }
        return warnConditionList;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        //禁用划分细则
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有资金预警数据，不允许删除！
            int num = btWarnStrategyDao.getValidFundWranNumByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FUNDWARN,
                        SysConfigConstant.BusType.FUND_WARN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.FUNDWARN_VALID_WARN_ERROR)));
            }
            if (ListUtils.isNotNullOrEmpty(verifyErrorDTOS)) {
                return verifyErrorDTOS;
            }
        }
        return null;
    }

    /**
     * 获取有效租户的资金预警数据(定时任务类)
     *
     * @return 有效资金预警数据
     */
    public List<BtWarnStrategy> getValidBtWarnStrategy() {
        return btWarnStrategyDao.getValidBtWarnStrategy();
    }

    /**
     * 逾期账款超标一键初始化
     */
    @Transactional
    public void initExReceivaOver(ICurrentUser user) {
        String netId = btWarnStrategyDao.getFirstNetId(user.getTenantId());
        if (EmptyUtils.isEmpty(netId)) {
            return;
        }
        String allCorpId = btWarnStrategyDao.getAllCorpId(user.getTenantId());
        for (int i = 1; i <= 11; i++) {
            BtWarnStrategyVO btWarnStrategyVO = new BtWarnStrategyVO();
            BtWarnConditionVO btWarnConditionVO = new BtWarnConditionVO();

            //公共部分
            btWarnStrategyVO.setNetId(netId); //第一个结算中心
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setCurId("de6c461acecd11ed898bfa163ee5daf6"); //人民币
            btWarnStrategyVO.setWarnLevel("1"); //预警级别：中
            btWarnStrategyVO.setStatus(1); //状态：启动

            if (i == 1) {
                //预警策略一
                btWarnStrategyVO.setCode("YJ001");
                btWarnStrategyVO.setName("逾期账款超标YJ001-按单位");
                btWarnStrategyVO.setDimensionCode("corp"); //预警维度：单位

                btWarnConditionVO.setCorpIdAndAccStr(allCorpId); //单位选择 全部单位
                btWarnConditionVO.setWarnConditionRate("0.100000"); //逾期账款比例大于10%
            } else {
                //预警策略二 ～ 预警策略十一
                btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

                if (i == 2) {
                    btWarnStrategyVO.setCode("YJ002");
                    btWarnStrategyVO.setName("逾期账款超标YJ002-按客商信用等级AAA级");

                    btWarnConditionVO.setRatingSelection("AAA"); //客商信用等级：AAA
                    btWarnConditionVO.setWarnConditionRate("0.010000"); //逾期账款比例大于1%
                } else if (i == 3) {
                    btWarnStrategyVO.setCode("YJ003");
                    btWarnStrategyVO.setName("逾期账款超标YJ003-按客商信用等级AA级");

                    btWarnConditionVO.setRatingSelection("AA"); //客商信用等级：AA
                    btWarnConditionVO.setWarnConditionRate("0.020000"); //逾期账款比例大于2%
                } else if (i == 4) {
                    btWarnStrategyVO.setCode("YJ004");
                    btWarnStrategyVO.setName("逾期账款超标YJ004-按客商信用等级A级");

                    btWarnConditionVO.setRatingSelection("A"); //客商信用等级：A
                    btWarnConditionVO.setWarnConditionRate("0.030000"); //逾期账款比例大于3%
                } else if (i == 5) {
                    btWarnStrategyVO.setCode("YJ005");
                    btWarnStrategyVO.setName("逾期账款超标YJ005-按客商信用等级BBB级");

                    btWarnConditionVO.setRatingSelection("BBB"); //客商信用等级：BBB
                    btWarnConditionVO.setWarnConditionRate("0.050000"); //逾期账款比例大于5%
                } else if (i == 6) {
                    btWarnStrategyVO.setCode("YJ006");
                    btWarnStrategyVO.setName("逾期账款超标YJ006-按客商信用等级BB级");

                    btWarnConditionVO.setRatingSelection("BB"); //客商信用等级：BB
                    btWarnConditionVO.setWarnConditionRate("0.070000"); //逾期账款比例大于7%
                } else if (i == 7) {
                    btWarnStrategyVO.setCode("YJ007");
                    btWarnStrategyVO.setName("逾期账款超标YJ007-按客商信用等级B级");

                    btWarnConditionVO.setRatingSelection("B"); //客商信用等级：B
                    btWarnConditionVO.setWarnConditionRate("0.100000"); //逾期账款比例大于10%
                } else if (i == 8) {
                    btWarnStrategyVO.setCode("YJ008");
                    btWarnStrategyVO.setName("逾期账款超标YJ008-按客商信用等级CCC级");

                    btWarnConditionVO.setRatingSelection("CCC"); //客商信用等级：CCC
                    btWarnConditionVO.setWarnConditionRate("0.150000"); //逾期账款比例大于15%
                } else if (i == 9) {
                    btWarnStrategyVO.setCode("YJ009");
                    btWarnStrategyVO.setName("逾期账款超标YJ009-按客商信用等级CC级");

                    btWarnConditionVO.setRatingSelection("CC"); //客商信用等级：CC
                    btWarnConditionVO.setWarnConditionRate("0.200000"); //逾期账款比例大于20%
                } else if (i == 10) {
                    btWarnStrategyVO.setCode("YJ010");
                    btWarnStrategyVO.setName("逾期账款超标YJ010-按客商信用等级C级");

                    btWarnConditionVO.setRatingSelection("C"); //客商信用等级：C
                    btWarnConditionVO.setWarnConditionRate("0.250000"); //逾期账款比例大于25%
                } else if (i == 11) {
                    btWarnStrategyVO.setCode("YJ011");
                    btWarnStrategyVO.setName("逾期账款超标YJ011-按客商信用等级D级");

                    btWarnConditionVO.setRatingSelection("D"); //客商信用等级：D
                    btWarnConditionVO.setWarnConditionRate("0.300000"); //逾期账款比例大于30%
                }
            }
            //新增预警策略
            saveOrUpdateWS(btWarnStrategyVO, btWarnConditionVO, user);
        }
    }

    /**
     * 逾期时间预警一键初始化
     */
    @Transactional
    public void initTimeWarnOver(ICurrentUser user) {
        String netId = btWarnStrategyDao.getFirstNetId(user.getTenantId());
        if (EmptyUtils.isEmpty(netId)) {
            return;
        }
        for (int i = 12; i <= 21; i++) {
            BtWarnStrategyVO btWarnStrategyVO = new BtWarnStrategyVO();
            BtWarnConditionVO btWarnConditionVO = new BtWarnConditionVO();

            //公共部分
            btWarnStrategyVO.setNetId(netId); //第一个结算中心
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setCurId("de6c461acecd11ed898bfa163ee5daf6"); //人民币
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级
            btWarnStrategyVO.setWarnLevel("1"); //预警级别：中
            btWarnStrategyVO.setStatus(1); //状态：启动

            if (i == 12) {
                btWarnStrategyVO.setCode("YJ012");
                btWarnStrategyVO.setName("逾期时间预警YJ012-客商信用等级AAA级");

                btWarnConditionVO.setRatingSelection("AAA"); //客商信用等级：AAA
                btWarnConditionVO.setDateNum("1,4,8,"); //已逾期天数达到：1,4,8,
            } else if (i == 13) {
                btWarnStrategyVO.setCode("YJ013");
                btWarnStrategyVO.setName("逾期时间预警YJ013-客商信用等级AA级");

                btWarnConditionVO.setRatingSelection("AA"); //客商信用等级：AA
                btWarnConditionVO.setDateNum("3,6,11,"); //已逾期天数达到：3,6,11,
            } else if (i == 14) {
                btWarnStrategyVO.setCode("YJ014");
                btWarnStrategyVO.setName("逾期时间预警YJ014-客商信用等级A级");

                btWarnConditionVO.setRatingSelection("A"); //客商信用等级：A
                btWarnConditionVO.setDateNum("4,11,16,"); //已逾期天数达到：4,11,16,
            } else if (i == 15) {
                btWarnStrategyVO.setCode("YJ015");
                btWarnStrategyVO.setName("逾期时间预警YJ015-客商信用等级BBB级");

                btWarnConditionVO.setRatingSelection("BBB"); //客商信用等级：BBB
                btWarnConditionVO.setDateNum("6,16,21,"); //已逾期天数达到：6,16,21,
            } else if (i == 16) {
                btWarnStrategyVO.setCode("YJ016");
                btWarnStrategyVO.setName("逾期时间预警YJ016-客商信用等级BB级");

                btWarnConditionVO.setRatingSelection("BB"); //客商信用等级：BB
                btWarnConditionVO.setDateNum("8,22,31,"); //已逾期天数达到：8,22,31,
            } else if (i == 17) {
                btWarnStrategyVO.setCode("YJ017");
                btWarnStrategyVO.setName("逾期时间预警YJ017-客商信用等级B级");

                btWarnConditionVO.setRatingSelection("B"); //客商信用等级：B
                btWarnConditionVO.setDateNum("11,31,46,"); //已逾期天数达到：11,31,46,
            } else if (i == 18) {
                btWarnStrategyVO.setCode("YJ018");
                btWarnStrategyVO.setName("逾期时间预警YJ018-客商信用等级CCC级");

                btWarnConditionVO.setRatingSelection("CCC"); //客商信用等级：CCC
                btWarnConditionVO.setDateNum("16,36,61,"); //已逾期天数达到：16,36,61,"
            } else if (i == 19) {
                btWarnStrategyVO.setCode("YJ019");
                btWarnStrategyVO.setName("逾期时间预警YJ019-客商信用等级CC级");

                btWarnConditionVO.setRatingSelection("CC"); //客商信用等级：CC
                btWarnConditionVO.setDateNum("21,46,91,"); //已逾期天数达到：21,46,91,
            } else if (i == 20) {
                btWarnStrategyVO.setCode("YJ020");
                btWarnStrategyVO.setName("逾期时间预警YJ020-客商信用等级C级");

                btWarnConditionVO.setRatingSelection("C"); //客商信用等级：C
                btWarnConditionVO.setDateNum("31,61,"); //已逾期天数达到：31,61,
            } else if (i == 21) {
                btWarnStrategyVO.setCode("YJ021");
                btWarnStrategyVO.setName("逾期时间预警YJ021-客商信用等级D级");

                btWarnConditionVO.setRatingSelection("D"); //客商信用等级：D
                btWarnConditionVO.setDateNum("0,"); //已逾期天数达到：0,
            }
            //新增预警策略
            saveOrUpdateWS(btWarnStrategyVO, btWarnConditionVO, user);
        }
    }

    /**
     * 单条预警策略（YJ001~YJ021）恢复初始化操作
     */
    @Transactional
    public void goInit(String code, ICurrentUser user) {
        BtWarnStrategyVO btWarnStrategyVO = new BtWarnStrategyVO();
        BtWarnConditionVO btWarnConditionVO = new BtWarnConditionVO();
        String netId = btWarnStrategyDao.getFirstNetId(user.getTenantId());
        String allCorpId = btWarnStrategyDao.getAllCorpId(user.getTenantId());

        //公共部分
        btWarnStrategyVO.setNetId(netId); //第一个结算中心
        btWarnStrategyVO.setCurId("de6c461acecd11ed898bfa163ee5daf6"); //人民币
        btWarnStrategyVO.setWarnLevel("1"); //预警级别：中
        btWarnStrategyVO.setStatus(1); //状态：启动

        if ("YJ001".equals(code)) {
            //预警策略一
            btWarnStrategyVO.setCode("YJ001");
            btWarnStrategyVO.setName("逾期账款超标YJ001-按单位");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("corp"); //预警维度：单位

            btWarnConditionVO.setCorpIdAndAccStr(allCorpId); //单位选择 全部单位
            btWarnConditionVO.setWarnConditionRate("0.100000"); //逾期账款比例大于10%
        } else if ("YJ002".equals(code)) {
            btWarnStrategyVO.setCode("YJ002");
            btWarnStrategyVO.setName("逾期账款超标YJ002-按客商信用等级AAA级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("AAA"); //客商信用等级：AAA
            btWarnConditionVO.setWarnConditionRate("0.010000"); //逾期账款比例大于1%
        } else if ("YJ003".equals(code)) {
            btWarnStrategyVO.setCode("YJ003");
            btWarnStrategyVO.setName("逾期账款超标YJ003-按客商信用等级AA级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("AA"); //客商信用等级：AA
            btWarnConditionVO.setWarnConditionRate("0.020000"); //逾期账款比例大于2%
        } else if ("YJ004".equals(code)) {
            btWarnStrategyVO.setCode("YJ004");
            btWarnStrategyVO.setName("逾期账款超标YJ004-按客商信用等级A级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("A"); //客商信用等级：A
            btWarnConditionVO.setWarnConditionRate("0.030000"); //逾期账款比例大于3%
        } else if ("YJ005".equals(code)) {
            btWarnStrategyVO.setCode("YJ005");
            btWarnStrategyVO.setName("逾期账款超标YJ005-按客商信用等级BBB级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("BBB"); //客商信用等级：BBB
            btWarnConditionVO.setWarnConditionRate("0.050000"); //逾期账款比例大于5%
        } else if ("YJ006".equals(code)) {
            btWarnStrategyVO.setCode("YJ006");
            btWarnStrategyVO.setName("逾期账款超标YJ006-按客商信用等级BB级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("BB"); //客商信用等级：BB
            btWarnConditionVO.setWarnConditionRate("0.070000"); //逾期账款比例大于7%
        } else if ("YJ007".equals(code)) {
            btWarnStrategyVO.setCode("YJ007");
            btWarnStrategyVO.setName("逾期账款超标YJ007-按客商信用等级B级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("B"); //客商信用等级：B
            btWarnConditionVO.setWarnConditionRate("0.100000"); //逾期账款比例大于10%
        } else if ("YJ008".equals(code)) {
            btWarnStrategyVO.setCode("YJ008");
            btWarnStrategyVO.setName("逾期账款超标YJ008-按客商信用等级CCC级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("CCC"); //客商信用等级：CCC
            btWarnConditionVO.setWarnConditionRate("0.150000"); //逾期账款比例大于15%
        } else if ("YJ009".equals(code)) {
            btWarnStrategyVO.setCode("YJ009");
            btWarnStrategyVO.setName("逾期账款超标YJ009-按客商信用等级CC级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("CC"); //客商信用等级：CC
            btWarnConditionVO.setWarnConditionRate("0.200000"); //逾期账款比例大于20%
        } else if ("YJ010".equals(code)) {
            btWarnStrategyVO.setCode("YJ010");
            btWarnStrategyVO.setName("逾期账款超标YJ010-按客商信用等级C级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("C"); //客商信用等级：C
            btWarnConditionVO.setWarnConditionRate("0.250000"); //逾期账款比例大于25%
        } else if ("YJ011".equals(code)) {
            btWarnStrategyVO.setCode("YJ011");
            btWarnStrategyVO.setName("逾期账款超标YJ011-按客商信用等级D级");
            btWarnStrategyVO.setTypeCode("exReceivaOver"); //预警类别：逾期账款超标
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("D"); //客商信用等级：D
            btWarnConditionVO.setWarnConditionRate("0.300000"); //逾期账款比例大于30%
        } else if ("YJ012".equals(code)) {
            btWarnStrategyVO.setCode("YJ012");
            btWarnStrategyVO.setName("逾期时间预警YJ012-客商信用等级AAA级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("AAA"); //客商信用等级：AAA
            btWarnConditionVO.setDateNum("1,4,8,"); //已逾期天数达到：1,4,8,
        } else if ("YJ013".equals(code)) {
            btWarnStrategyVO.setCode("YJ013");
            btWarnStrategyVO.setName("逾期时间预警YJ013-客商信用等级AA级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("AA"); //客商信用等级：AA
            btWarnConditionVO.setDateNum("3,6,11,"); //已逾期天数达到：3,6,11,
        } else if ("YJ014".equals(code)) {
            btWarnStrategyVO.setCode("YJ014");
            btWarnStrategyVO.setName("逾期时间预警YJ014-客商信用等级A级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("A"); //客商信用等级：A
            btWarnConditionVO.setDateNum("4,11,16,"); //已逾期天数达到：4,11,16,
        } else if ("YJ015".equals(code)) {
            btWarnStrategyVO.setCode("YJ015");
            btWarnStrategyVO.setName("逾期时间预警YJ015-客商信用等级BBB级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("BBB"); //客商信用等级：BBB
            btWarnConditionVO.setDateNum("6,16,21,"); //已逾期天数达到：6,16,21,
        } else if ("YJ016".equals(code)) {
            btWarnStrategyVO.setCode("YJ016");
            btWarnStrategyVO.setName("逾期时间预警YJ016-客商信用等级BB级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("BB"); //客商信用等级：BB
            btWarnConditionVO.setDateNum("8,22,31,"); //已逾期天数达到：8,22,31,
        } else if ("YJ017".equals(code)) {
            btWarnStrategyVO.setCode("YJ017");
            btWarnStrategyVO.setName("逾期时间预警YJ017-客商信用等级B级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("B"); //客商信用等级：B
            btWarnConditionVO.setDateNum("11,31,46,"); //已逾期天数达到：11,31,46,
        } else if ("YJ018".equals(code)) {
            btWarnStrategyVO.setCode("YJ018");
            btWarnStrategyVO.setName("逾期时间预警YJ018-客商信用等级CCC级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("CCC"); //客商信用等级：CCC
            btWarnConditionVO.setDateNum("16,36,61,"); //已逾期天数达到：16,36,61,"
        } else if ("YJ019".equals(code)) {
            btWarnStrategyVO.setCode("YJ019");
            btWarnStrategyVO.setName("逾期时间预警YJ019-客商信用等级CC级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("CC"); //客商信用等级：CC
            btWarnConditionVO.setDateNum("21,46,91,"); //已逾期天数达到：21,46,91,
        } else if ("YJ020".equals(code)) {
            btWarnStrategyVO.setCode("YJ020");
            btWarnStrategyVO.setName("逾期时间预警YJ020-客商信用等级C级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("C"); //客商信用等级：C
            btWarnConditionVO.setDateNum("31,61,"); //已逾期天数达到：31,61,
        } else if ("YJ021".equals(code)) {
            btWarnStrategyVO.setCode("YJ021");
            btWarnStrategyVO.setName("逾期时间预警YJ021-客商信用等级D级");
            btWarnStrategyVO.setTypeCode("timeWarnOver"); //预警类别：逾期时间预警
            btWarnStrategyVO.setDimensionCode("bsCusCreditRating"); //预警维度：客商信用等级

            btWarnConditionVO.setRatingSelection("D"); //客商信用等级：D
            btWarnConditionVO.setDateNum("0,"); //已逾期天数达到：0,
        }
        //新增预警策略
        saveOrUpdateWS(btWarnStrategyVO, btWarnConditionVO, user);
    }

    /**
     * 新增预警策略
     *
     * @param strategyVO
     * @param conditionVO
     * @param userVo
     */
    public void saveOrUpdateWS(BtWarnStrategyVO strategyVO, BtWarnConditionVO conditionVO, ICurrentUser userVo) {
        BtWarnStrategy btWarnStrategy = btWarnStrategyDao.getWSByCode(strategyVO.getCode(), userVo.getTenantId());
        if (!EmptyUtils.isEmpty(btWarnStrategy)) {
            //删除原来的预警策略
            btWarnStrategyDao.delete(btWarnStrategy.getId());
            //删除原来的预警策略条件
            btWarnConditionDao.deleteCondtionByWarnId(btWarnStrategy.getId());
        }

        BtWarnStrategy strategy = new BtWarnStrategy();
        BeanUtils.copyProperties(strategyVO, strategy, "tenantId");
        //strategy.setTenantId(userVo.getTenantId());
        strategy.setCreateBy(userVo.getLoginName());
        strategy.setCreateName(userVo.getUsername());

        //如果已经保存客户号，那么截取掉重新保存
        //如果已经保存客户号，那么截取掉重新保存
        String name = strategyVO.getName();
        if (name.contains("(客户号:")) {
            name = name.substring(0, name.indexOf("(客户号:"));
        }
        //String tenantCode = btWarnStrategyDao.getTenantCodeById(strategy.getTenantId());
        //strategy.setName(name+"(客户号:"+tenantCode+")");
        btWarnStrategyDao.save(strategy);//保存预警策略主信息

        //循环添加预警策略条件,批量保存
        List<BtWarnCondition> warnConditionList = new ArrayList<>();
        //单位Id、银行账号、内部账户列表选择
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), strategy.getDimensionCode(), conditionVO.getCorpIdAndAccStr());
        //业务属性选择：银行账号属性
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCATTRIBUTE, conditionVO.getBankAttStr());
        //业务属性选择：银行账号类别
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCTYPE, conditionVO.getBankAccTypeStr());
        //业务属性选择：银行账号性质
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_BANKACCNATURE, conditionVO.getBankAccOnlineStr());
        //业务属性选择：内部账户性质
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_FCACCNATURE, conditionVO.getBankAccNatureStr());
        //业务属性选择：会计科目
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_ACCSUBJECT, conditionVO.getSubjectNameStr());
        //逾期账款比例大于
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_WARNCONDITIONRATE, conditionVO.getWarnConditionRate());
        //客商信用等级
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_RATINGSELECTION, conditionVO.getRatingSelection());
        //已逾期天数达到X天在当天预警
        warnConditionList = getConditionListByVo(warnConditionList, strategy.getId(), FundWarnConstant.CONDITION_TYPE_DATENUM, conditionVO.getDateNum());
        int size = 0;
        if (!warnConditionList.isEmpty()) {
            size = btWarnConditionDao.batchSave(warnConditionList);
            // btWarnConditionDao.flush();
        }
    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
