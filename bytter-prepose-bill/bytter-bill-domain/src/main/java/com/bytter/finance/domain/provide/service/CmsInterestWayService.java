package com.bytter.finance.domain.provide.service;

import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.provide.dao.CmsInterestWayDao;
import com.bytter.finance.domain.provide.entity.CmsInterestWay;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.CmsInterestWayVO;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author songkk
 * @version 1.0
 * @Title: Service
 * @Description: CMS_INTEREST_WAY
 * @date 2017-08-24 11:09:43
 */
@Service("cmsInterestWayService")
@Transactional
public class CmsInterestWayService extends BaseService<CmsInterestWay, String> {
    @Autowired
    private CmsInterestWayDao cmsInterestWayDao;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @Autowired
    private LoanInfoService loanInfoService;
    /**
     * 查询CmsInterestWay DataTable数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listCmsInterestWay(DataTablesInput dataTablesInput, String sendCode) {
        return cmsInterestWayDao.listCmsInterestWay(dataTablesInput, sendCode);
    }
    /**
     * 根据单号查询最后一笔数据
     *
     * @param sendCode
     * @return
     */
    @Transactional(readOnly = true)
    public CmsInterestWayVO getCmsInterestWay(String id, String sendCode) {
        List<CmsInterestWayVO> cmsList = cmsInterestWayDao.getCmsInterestWay(id);
        CmsInterestWayVO vo;
        String date = null;
        if (StringUtils.isNotEmpty(sendCode)) {
            date = DateUtils.date2Str(cmsProvideLoanSendService.getLoanSendVOBySendBillCode(sendCode).getSendDate(),
                    DateUtils.date_sdf());
        } else {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if (cmsList != null && cmsList.size() > 0) {
            vo = cmsList.get(0);
            vo.setToDayDate(DateUtils.date2Str(vo.getActiveDate(), DateUtils.date_sdf()));
        } else {
            vo = new CmsInterestWayVO();
            vo.setToDayDate(date);
        }
        vo.setSendCode(sendCode);
        vo.setId(id);
        return vo;
    }
    /**
     * 保存计息方式表
     *
     * @param cmsInterestWayVO
     */
    public void save(CmsInterestWayVO cmsInterestWayVO, ICurrentUser user) {
        String interesWayStr = cmsInterestWayVO.getInterestWay();
        CmsInterestWay entity = new CmsInterestWay();
        entity.setSendCode(cmsInterestWayVO.getId());// 发放id
        entity.setInterestWay(interesWayStr);// 计息方式
        entity.setExpiryDay(cmsInterestWayVO.getExpiryDay());// 计息日
        entity.setWithholdingDay(cmsInterestWayVO.getWithholdingDay());// 预提日
        entity.setActiveDate(cmsInterestWayVO.getActiveDate());// 执行日期
        entity.setLastUpdateTime(cmsInterestWayVO.getLastUpdateTime());//
        entity.setCreateName(user.getUsername());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(new Date());
        entity.setInterestFrequencyType(cmsInterestWayVO.getInterestFrequencyType());
        entity.setInterestFrequencyNext(cmsInterestWayVO.getInterestFrequencyNext());
        entity.setInterestFrequency(cmsInterestWayVO.getInterestFrequency());
        if (!BaseDataConstant.OTHER.equals(cmsInterestWayVO.getInterestFrequency())) {
            entity.setInterestFrequencyNum(cmsInterestWayVO.getInterestFrequency());
        }
        super.saveEntity(entity);
        // 修该银行借款帐号的预提标志
        ProvideLoanSend send = cmsProvideLoanSendService.get(cmsInterestWayVO.getId());
        if (EmptyUtils.isNotEmpty(send.getLoanAcc())) {
            FcAccDTO fcAccDto = innerAccountService.getByBlnctrAcc(send.getLoanAcc());
            if (!EmptyUtils.isEmpty(fcAccDto) && !EmptyUtils.isEmpty(fcAccDto.getId())) {
                if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.get().equals(interesWayStr)
                        || InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_NO_ANTICIPATE.get()
                                .equals(interesWayStr)) {
                    fcAccDto.setPredrawSign(interesWayStr);
                } else {
                    fcAccDto.setPredrawSign(InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.get());
                }
                innerAccountService.updateFcAcc(fcAccDto);
            }
        }
        // 调整个别借款利息测算数据
        cmsProvideInterestService.adjustProvideInterest(send, user);
    }
    public BaseDao<CmsInterestWay, String> getEntityDao() {
        return cmsInterestWayDao;
    }
    /**
     * 获取1-31的数据，为天数赋值
     *
     * @return
     */
    public List<Map<String, Object>> getJSONArray() {
        List<Map<String, Object>> jsonarray = new ArrayList<Map<String, Object>>();
        for (Integer i = 1; i <= 31; i++) {
            jsonarray.add(getJsonObj(i.toString(), i.toString()));
        }
        return jsonarray;
    }
    public Map<String, Object> getJsonObj(String name, String value) {
        Map<String, Object> jsonobj = new HashMap<String, Object>();
        jsonobj.put("name", name);
        jsonobj.put("value", value);
        return jsonobj;
    }
}
