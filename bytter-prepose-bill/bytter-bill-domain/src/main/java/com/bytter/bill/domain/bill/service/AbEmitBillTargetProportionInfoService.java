package com.bytter.bill.domain.bill.service;

import cn.hutool.core.date.DateUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbEmitBillTargetProportionInfoDao;
import com.bytter.bill.domain.bill.entity.AbEmitBillTargetProportionInfo;
import com.bytter.bill.domain.bill.vo.AbEmitBillTargetProportionInfoVO;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* @Description 银承开票占比目标维护服务类
* @author zhous
* @date 2025/4/22 10:33
*/
@Service("abEmitBillTargetProportionInfoService")
@Transactional
public class AbEmitBillTargetProportionInfoService extends BaseService<AbEmitBillTargetProportionInfo, String> implements IExcelVerifyHandler<AbEmitBillTargetProportionInfoVO> {

    @Autowired
    private AbEmitBillTargetProportionInfoDao abEmitBillTargetProportionInfoDao;

    @Autowired
    private AbCorpService abCorpService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @Override
    public BaseDao<AbEmitBillTargetProportionInfo, String> getEntityDao() {
        return abEmitBillTargetProportionInfoDao;
    }

    /**
    * @Description 银承开票占比目标维护列表查询
    * @author zhous
    * @date 2025/4/23 9:03
    */
    public DataTablesOutput dataTable(DataTablesInput input) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(
                AccebillConstant.AB_BILL_RESOURCE_BILLTARGETPROPORTIONINFO_QUERY, input.getCurrentUserId());
        DataTablesOutput<AbEmitBillTargetProportionInfoVO> out = abEmitBillTargetProportionInfoDao.dataTable(input,corpAuthList);
        return out;
    }

    /**
    * @Description 保存或修改数据
    * @author zhous
    * @date 2025/4/22 13:34
    */
    public void saveOrUpdate(AbEmitBillTargetProportionInfoVO vo, ICurrentUser currentUser) {
        // 如果有id 则为修改
        AbEmitBillTargetProportionInfo entity =null;
        if (StringUtils.isNotEmpty(vo.getId())){
            entity = abEmitBillTargetProportionInfoDao.get(vo.getId(), true);
            if(vo.getTargetProportion() != null){
                entity.setTargetProportion(vo.getTargetProportion());
            }
            entity.setUpdateBy(currentUser.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
        }else{
            entity = new AbEmitBillTargetProportionInfo();
            BeanUtil.copyProperties(vo, entity);
            //重复校验
            boolean exist = abEmitBillTargetProportionInfoDao.existBillTargetProportionInfo(vo.getCorpId(), vo.getCurrentYear());
            if(exist){
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLTARGETPROPORTION_REPEAT);
            }
            entity.setCreateBy(currentUser.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
        }
        saveOrUpdate(entity);
    }

    /**
     * 导入数据
     *
     * @param list
     * @return
     */
    public void saveImport(List<AbEmitBillTargetProportionInfoVO> list, ICurrentUser currentUser) {
        if (list != null && !list.isEmpty()) {
            for(AbEmitBillTargetProportionInfoVO info : list){
                //重复校验
                boolean exist = abEmitBillTargetProportionInfoDao.existBillTargetProportionInfo(info.getCorpId(), info.getCurrentYear());
                if(exist){
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLTARGETPROPORTION_REPEAT);
                }
                AbEmitBillTargetProportionInfo entity = new AbEmitBillTargetProportionInfo();
                BeanUtil.copyProperties(info, entity);
                entity.setTargetProportion(entity.getTargetProportion().divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));
                entity.setCreateBy(currentUser.getLoginName());
                entity.setCreateDate(DateUtils.getDate());
                this.saveEntity(entity);
            }
        }
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(AbEmitBillTargetProportionInfoVO obj) {
        return null;
    }

    @Override
    public void verifyHandler(List<AbEmitBillTargetProportionInfoVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //单位集合
            HashMap<String, CorpDTO> corpMap = new HashMap<>();
            for (AbEmitBillTargetProportionInfoVO vo : list) {
                List<String> msgList = new ArrayList<>();
                // 判断单位在系统中是否存在
                CorpDTO corp = corpMap.get(vo.getCorpName());
                if (corp == null) {
                    corp = this.corpService.getCorpIdByName(vo.getCorpName());
                    corpMap.put(vo.getCorpName(), corp);
                }
                if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                    corp = new CorpDTO();
                    msgList.add("nis.netbank.message.corp.null");
                } else {
                    vo.setCorpId(corp.getId());
                    vo.setCorpName(corp.getName());
                    vo.setCorpCode(corp.getCode());
                }
                //校验年份格式
                if(!StringUtils.isEmpty(vo.getCurrentYear())){
                    if (!vo.getCurrentYear().matches("\\d{4}")) {
                        msgList.add("table.emitBill.currentYear.format.error");
                    }
                }

                //目标
                if (vo.getTargetProportion() != null) {
                    if (BigDecimal.ZERO.compareTo(vo.getTargetProportion()) > 0) {
                        msgList.add("table.emitBill.targetProportion.is.negative");
                    }else{
                        vo.setTargetProportion(vo.getTargetProportion().setScale(4, BigDecimal.ROUND_HALF_UP));
                    }
                }
                //重复校验
                boolean exist = abEmitBillTargetProportionInfoDao.existBillTargetProportionInfo(vo.getCorpId(), vo.getCurrentYear());
                if(exist){
                    msgList.add("accebill.message.error.billtargetproportion.repeat");
                }
                vo.addErrorMsg(msgList);
            }
        }
    }

    /**
    * @Description 根据id删除
    * @author zhous
    * @date 2025/4/23 9:03
    */
    public void deleteById(String id, ICurrentUser currentUser) {
        abEmitBillTargetProportionInfoDao.deleteById(id,currentUser);
    }
}
