package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbEmitBailInterestInfoDao;
import com.bytter.bill.domain.bill.entity.AbEmitBailInterestInfo;
import com.bytter.bill.domain.bill.vo.AbEmitBailInterestInfoVO;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* @Description 票据保证金利息维护服务类
* @author zhous
* @date 2025/3/5 15:56
*/
@Service("abEmitBailInterestInfoService")
@Transactional
public class AbEmitBailInterestInfoService extends BaseService<AbEmitBailInterestInfo, String>  {

    @Autowired
    private AbEmitBailInterestInfoDao abEmitBailInterestInfoDao;

    @Autowired
    private AbCorpService abCorpService;

    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<AbEmitBailInterestInfo, String> getEntityDao() {
        return abEmitBailInterestInfoDao;
    }

    /**
    * @Description 表格获取数据
    * @author zhous
    * @date 2025/3/6 17:31
    */
    public DataTablesOutput dataTable(DataTablesInput input) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(
                AccebillConstant.AB_BILL_RESOURCE_BAILINTERESTINFO_QUERY, input.getCurrentUserId());
        DataTablesOutput<AbEmitBailInterestInfoVO> out = abEmitBailInterestInfoDao.dataTable(input,corpAuthList);
        Optional.ofNullable(out.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_EMIT_BILL_BAIL_INTEREST_STATUS","AB_EMIT_BILL_BAIL_INTEREST_PUSH_STATUS");
            for (AbEmitBailInterestInfoVO vo : listData){
                injectValues(vo, listDictMap);
            }
        });
        return out;
    }

    /**
     * @Description 注入字典信息
     * @author zhous
     * @date 2025/2/25 8:45
     */
    private void injectValues(AbEmitBailInterestInfoVO bill, Map<String, Map<String, DictDTO>> listDictMap){
        // 状态
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_EMIT_BILL_BAIL_INTEREST_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status)))
                    .ifPresent(dict -> bill.setStatusName(dict.getName()));
        });
        // 推送状态
        Optional.ofNullable(bill.getPushStatus()).ifPresent(pushStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_EMIT_BILL_BAIL_INTEREST_PUSH_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(pushStatus)))
                    .ifPresent(dict -> bill.setPushStatusName(dict.getName()));
        });
    }

    /**
    * @Description 保存或修改数据
    * @author zhous
    * @date 2025/3/5 17:05
    */
    public void saveOrUpdate(AbEmitBailInterestInfoVO vo, ICurrentUser currentUser) {
        // 如果有id 则为修改
        AbEmitBailInterestInfo entity =null;
        if (StringUtils.isNotEmpty(vo.getId())){
            entity = abEmitBailInterestInfoDao.get(vo.getId(), true);
            if(AccebillConstant.MANUAL_INCREMENT.equals(entity.getRecordSource())){
                if(vo.getInterestMargin() != null){
                    entity.setInterestMargin(vo.getInterestMargin());
                }
            }else{
                if(vo.getBailMoney() != null){
                    entity.setBailMoney(vo.getBailMoney());
                }
                if(vo.getActualSettlementAmount() != null){
                    entity.setActualSettlementAmount(vo.getActualSettlementAmount());
                }
                entity.setDueSettlementAmount(entity.getBillMoney().subtract(entity.getBailMoney()));
                entity.setInterestMargin(entity.getDueSettlementAmount().subtract(entity.getActualSettlementAmount()));
            }
            entity.setUpdateBy(currentUser.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
        }else{
            entity = new AbEmitBailInterestInfo();
            BeanUtil.copyProperties(vo, entity);
            entity.setRecordSource(AccebillConstant.MANUAL_INCREMENT);
            entity.setStatus(AccebillConstant.BAIL_INTEREST_INFO_STATUS_0);
            entity.setPushStatus(AccebillConstant.BAIL_INTEREST_INFO_PUSH_STATUS_0);
            entity.setCreateBy(currentUser.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
        }
        saveOrUpdate(entity);
    }

    /**
    * @Description 自动计算生成保证金利息记录
    * @author zhous
    * @date 2025/3/6 9:59
    */
    public void autoCalculatedInterest(AbEmitBailInterestInfoVO vo, ICurrentUser currentUser) {
        if(StringUtils.isBlank(vo.getCurrentMonth())){
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_BAIL_INTEREST_INFO_CURRENT_MONTH_IS_NULL);
        }
        //获取该月份账号开票数据
        List<AbEmitBailInterestInfo> bailInterestInfoList = abEmitBailInterestInfoDao.getOpenBillInfo(vo.getCurrentMonth());
        if(CollectionUtil.isNotEmpty(bailInterestInfoList)){
            for(AbEmitBailInterestInfo info : bailInterestInfoList){
                info.setDueSettlementAmount(info.getBillMoney().subtract(info.getBailMoney()));
                info.setInterestMargin(info.getDueSettlementAmount().subtract(info.getActualSettlementAmount()));
                info.setStatus(AccebillConstant.BAIL_INTEREST_INFO_STATUS_0);
                info.setPushStatus(AccebillConstant.BAIL_INTEREST_INFO_PUSH_STATUS_0);
                info.setCreateBy(currentUser.getLoginName());
                info.setCreateDate(DateUtils.getDate());
                info.setCurrentMonth(vo.getCurrentMonth());
                info.setRecordSource(AccebillConstant.AUTO_GENERATION);
                //删除重复的暂存数据
                abEmitBailInterestInfoDao.deleteOldBailInterestInfo(info.getBankAcc(),vo.getCurrentMonth());
            }
            batchSave(bailInterestInfoList);
        }
    }

    /**
    * @Description 批量确认或反确认保证金利息记录
    * @author zhous
    * @date 2025/3/6 17:31
    */
    public void batchSureOrNotSure(boolean isSure,String ids, ICurrentUser currentUser) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (isSure) {
            params.put("status", AccebillConstant.BAIL_INTEREST_INFO_STATUS_1);
        }else{
            params.put("status", AccebillConstant.BAIL_INTEREST_INFO_STATUS_0);
        }
        params.put("updateBy", currentUser.getLoginName());
        params.put("updateDate", DateUtils.getDate());
        params.put("ids", ids.split(","));
        abEmitBailInterestInfoDao.updateByIds(params);
    }

    /**
    * @Description 推送共享保证金利息记录
    * @author zhous
    * @date 2025/3/6 17:31
    */
    public void pushSharing(String ids, ICurrentUser currentUser) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pushStatus", AccebillConstant.BAIL_INTEREST_INFO_PUSH_STATUS_1);
        params.put("updateBy", currentUser.getLoginName());
        params.put("updateDate", DateUtils.getDate());
        params.put("ids", ids.split(","));
        abEmitBailInterestInfoDao.updateByIds(params);
    }
}
