package com.bytter.biz.finacial.interest.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.finacial.interest.dao.FcInterestSingleNodBookingDao;
import com.bytter.biz.finacial.interest.entity.FcInterestSingleNodBook;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleNodBookVO;
import com.bytter.biz.finacial.interest.vo.NoticeAccountVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.service.IChoInnerFinanceService;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service
@Transactional
public class FcInterestSingleNodBookingService extends BaseService<FcInterestSingleNodBook, String> {
    @Autowired
    private FcInterestSingleNodBookingDao fcInterestSingleNodBookingDao;
    @Autowired
    private FcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private ISystemForeignService iSystemForeignService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;

    @DubboReference(check = false)
    private IChoInnerFinanceService choInnerFinanceService;

    @DubboReference(check = false)
    private ICorpService iCorpService;

    @DubboReference(check = false)
    private IDictService iDictService;

    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;

    @Override
    public BaseDao<FcInterestSingleNodBook, String> getEntityDao() {
        return fcInterestSingleNodBookingDao;
    }

    public DataTablesOutput dataTableBooking(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = iCorpService.getUserCorpIds(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_QUERY, user.getId());
        DataTablesOutput output = fcInterestSingleNodBookingDao.dataTableBooking(dataTablesInput, corpIds);
        List<FcInterestSingleNodBookVO> data = output.getData();
        Map<String, String> corpNameMap = iCorpService.getNameByIds(corpIds);
        data.forEach(a -> {
            a.setCorpName(corpNameMap.get(a.getCorpId()));
        });

        return output;
    }

    public List<NoticeAccountVO> getNoticeAccount(String corpId, String curId) {
        List<FcAccDTO> fcAccDTOS = iFcAccService.getNoticeAccount(corpId, curId);
        List<String> accounts = fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        //accounts02 是需要被排除的
        List<String> accounts02 = choInnerFinanceService.filterBankAcc(accounts);
        //过滤后的
        List<FcAccDTO> list = fcAccDTOS.stream().filter(a -> !accounts02.contains(a.getBlnctrAcc())).collect(Collectors.toList());
        List<String> accounts03 = list.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        List<FcAccDTO> fcAccDTOList = fcInterestSingleNodBookingDao.getNoticeAccount(accounts03);
        Map<String, FcAccDTO> accountMap = fcAccDTOList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        NoticeAccountVO noticeAccount = null;
        List<NoticeAccountVO> newList = new ArrayList<>();
        for (FcAccDTO fcAccDTO : list) {
            //赋值
            FcAccDTO fcAccDTO1 = accountMap.get(fcAccDTO.getBlnctrAcc());
            fcAccDTO.setFcBalance(fcAccDTO1.getFcBalance());
            noticeAccount = new NoticeAccountVO();
            BeanUtils.copyNotNullProperties(fcAccDTO, noticeAccount);
            FcOnlineLedgerDTO fcDTO = this.fcOnlineLedgerService.getByBlnctrAcc(fcAccDTO.getBlnctrAcc());
            noticeAccount.setFcBalance(EmptyUtils.isEmpty(fcDTO) ? BigDecimal.ZERO : fcDTO.getCheckBalance().subtract(fcDTO.getFreezeMoney()));
            if (StringUtils.hasText(fcAccDTO.getRelationAcc())) {
                FcAccDTO relationAccount = fcAccService.getAccDTOByAccount(fcAccDTO.getRelationAcc());
                if (null != relationAccount) {
                    noticeAccount.setRelationAccId(relationAccount.getId());
                    noticeAccount.setRelationAcc(relationAccount.getBlnctrAcc());
                    noticeAccount.setRelationAccName(relationAccount.getAccName());
                }
            }
            newList.add(noticeAccount);
        }
        return newList;
    }
    public void saveInterestSingleNodBook(FcInterestSingleNodBookVO vo, ICurrentUser user) {
        FcInterestSingleNodBook entity = new FcInterestSingleNodBook();
        BeanUtils.copyNotNullProperties(vo, entity);
        entity.setStatus(InterestConstant.NODBOOKING_STATUE_TEMP);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FC_INTEREST_SINGLE_NOD_BOOK));
        entity.setCreateBy(user.getId());
        entity.setCreateDate(DateUtils.getDateTime());
        FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getNoticeAccount());
        entity.setNoticeAccType(fcAccDTO.getAccType());
        saveEntity(entity);
    }
    public void modifyInterestSingleNodBook(FcInterestSingleNodBookVO vo, ICurrentUser user) {
        FcInterestSingleNodBook entity = get(vo.getId());
        BeanUtils.copyNotNullProperties(vo, entity);
        entity.setUpdateBy(user.getId());
        entity.setUpdateDate(DateUtils.getDateTime());
        updateEntity(entity);
    }
    public FcInterestSingleNodBookVO getByUnion(String id) {
        FcInterestSingleNodBookVO vo = fcInterestSingleNodBookingDao.getByUnion(id);
        if (ObjectUtil.isNotEmpty(vo) && com.bytter.core.utils.StringUtils.isNotBlank(vo.getCorpId())){
            CorpDTO corpDTO = iCorpService.get(vo.getCorpId());
            vo.setCorpName(corpDTO.getName());
        }
        vo.setNoticeTypeName(iDictService.get("fcAcc.notifyType", vo.getNoticeType()));
        List<String> fcAccIds = Lists.newArrayList();
        fcAccIds.add(vo.getCapitalAccId());
        fcAccIds.add(vo.getInterestAccId());

        List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByIds(Lists.newArrayList(vo.getNoticeAccountId(), vo.getCapitalAccId(), vo.getInterestAccId()));
        Map<String, String> map = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getId, FcAccDTO::getAccName));
        vo.setNoticeAccountName(map.get(vo.getNoticeAccountId()));
        vo.setCapitalAccName(map.get(vo.getCapitalAccId()));
        vo.setInterestAccName(map.get(vo.getInterestAccId()));
        return vo;
    }
    /**
     * 取消预约
     *
     * @param id
     */
    public void unBooking(String id) {
        FcInterestSingleNodBook entity = get(id);
        entity.setStatus(InterestConstant.NODBOOKING_STATUE_UNBOOKING);
        updateEntity(entity);
    }
    /**
     * 逻辑删除
     *
     * @param id
     */
    public void deleteBooking(String id) {
        FcInterestSingleNodBook entity = get(id);
        entity.setStatus(InterestConstant.NODBOOKING_STATUE_DELETE);
        updateEntity(entity);
    }
    /**
    * <br>
    * <b>功能：</b> 查询账户通知预约信息 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/28 0028 <br>
    */
    public List<FcInterestSingleNodBookVO> queryAccountNodBookList(String account, String id) {
        return fcInterestSingleNodBookingDao.listByAccount(account, id);
    }
    /**
     * 根据通知存款账号获取账号相同的预约
     * @param account
     * @return
     */
    public Boolean checkAccount(String account,String id){
        Boolean result = true;
        List<FcInterestSingleNodBookVO> fcInterestSingleNodBookVOList = queryAccountNodBookList(account, id);
        if(EmptyUtils.isNotEmpty(fcInterestSingleNodBookVOList) && fcInterestSingleNodBookVOList.size()>0){
            result = false;
        }
        return result;
    }
    /**
     * <br>
     * <b>功能：</b> 更新账户预约记录为已使用 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/28 0028 <br>
     */
    public void updateAccountBookNodUsed(String id, String account){
        fcInterestSingleNodBookingDao.updateAccountBookNod(id, account, InterestConstant.NODBOOKING_STATUE_USED);
    }
}
