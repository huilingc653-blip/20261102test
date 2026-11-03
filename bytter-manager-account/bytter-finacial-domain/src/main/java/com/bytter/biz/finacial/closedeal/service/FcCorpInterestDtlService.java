package com.bytter.biz.finacial.closedeal.service;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.closedeal.dao.FcCorpInterestDtlDao;
import com.bytter.biz.finacial.closedeal.entity.FcCorpInterestDtl;
import com.bytter.biz.finacial.closedeal.vo.FcCorpInterestDtlVO;
import com.bytter.biz.finacial.closedeal.vo.FcDayCloseVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FC_CORP_INTEREST_DTL
 * @date 2017-07-12 14:23:15
 */
@Service("fcCorpInterestDtlService")
@Transactional
public class FcCorpInterestDtlService extends BaseService<FcCorpInterestDtl, String> {
    @Autowired
    private FcCorpInterestDtlDao fcCorpInterestDtlDao;
    @DubboReference(check = false)
    private IBankTypeService iBankTypeService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(FcCorpInterestDtl entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcCorpInterestDtl entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcCorpInterestDtl entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcCorpInterestDtl entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<FcCorpInterestDtl, String> getEntityDao() {
        return fcCorpInterestDtlDao;
    }
    /**
     * 功能介绍 : 查询可以产生单位利息明细的账务数据。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcCorpInterestDtlVO> queryBookInterestDtl(FcDayCloseVO dayClose) {

        //TODO 该网点下账户列表
        List<FcAccDTO> fcAccByNetIdList = fcAccService.getFcAccByNetId(dayClose.getNetCode());
        if (EmptyUtils.isEmpty(fcAccByNetIdList) || fcAccByNetIdList.size() < 1){
            return null;
        }
        List<String> allAccByNetList = fcAccByNetIdList.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        List<String> singleAccByNetList = fcAccByNetIdList.stream().filter(fcAcc -> StringUtils.equals(fcAcc.getAccType(), "03"))
                .map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        List<FcCorpInterestDtlVO> fcCorpInterestDtlVOS = fcCorpInterestDtlDao.queryBookInterestDtl(dayClose, singleAccByNetList, allAccByNetList);

        if (EmptyUtils.isEmpty(fcCorpInterestDtlVOS) || fcCorpInterestDtlVOS.size() < 1){
            return null;
        }
        //TODO 默认情况下，单位id和 bank_code都需要有默认值，都是从基础获得
        List<String> corpIds = fcCorpInterestDtlVOS.stream().map(FcCorpInterestDtlVO::getCorpId).collect(Collectors.toList());
        Map<String, String> corpIdMap = iCorpService.getNameByIds(corpIds);
        List<String> bankTypeIds = fcCorpInterestDtlVOS.stream().map(FcCorpInterestDtlVO::getBankTypeId).collect(Collectors.toList());
        List<BankTypeDTO> bankTypeDTOS = iBankTypeService.getBankTypeNameAndBankTypeByIds(bankTypeIds);
        Map<String, BankTypeDTO> bankTypeMap = bankTypeDTOS.stream().collect(Collectors.toMap(BankTypeDTO::getId, Function.identity()));

        fcCorpInterestDtlVOS.forEach(a -> {
            a.setCorpName(corpIdMap.get(a.getCorpId()));
            a.setBankType(bankTypeMap.get(a.getBankTypeId()).getBankType());
            a.setBankTypeName(bankTypeMap.get(a.getBankTypeId()).getTypeName());
        });
        return fcCorpInterestDtlVOS;
    }
    /**
     * 功能介绍 : 保存单位利息明细数据。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void generateCorpInterest(FcDayCloseVO dayClose, ICurrentUser user) {
        List<FcCorpInterestDtlVO> list = queryBookInterestDtl(dayClose);
        if (null == list) {
            return;
        }
        for (FcCorpInterestDtlVO corpInterestDtl : list) {
            FcCorpInterestDtl interestDtl = new FcCorpInterestDtl();
            BeanUtils.copyProperties(corpInterestDtl, interestDtl);
            interestDtl.setCreateBy(user.getId());
            interestDtl.setCreateTime(DateUtils.getDate());
            this.save(interestDtl);
        }
    }
}
