package com.bytter.biz.ttpay.service;
import com.bytter.biz.ttpay.dao.TpBussinessDao;
import com.bytter.biz.ttpay.dao.TpBussinessTextDao;
import com.bytter.biz.ttpay.entity.TpBussiness;
import com.bytter.biz.ttpay.entity.TpBussinessCorp;
import com.bytter.biz.ttpay.entity.TpBussinessText;
import com.bytter.biz.payment.api.enums.TtPayMessageEnum;
import com.bytter.biz.ttpay.vo.TpBussinessCorpVO;
import com.bytter.biz.ttpay.vo.TpBussinessTextVO;
import com.bytter.biz.ttpay.vo.TpBussinessVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service("tpBussinessService")
@Transactional
public class TpBussinessService extends BaseService<TpBussiness, String> {
    @Autowired
    private TpBussinessDao tpBussinessDao;
    @Autowired
    private TpBussinessTextDao tpBussinessTextDao;
    @Autowired
    private TpBussinessCorpService tpBussinessCorpService;
    @DubboReference(check = false)
    private IDictService dictService;
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput input) {
        DataTablesOutput data = tpBussinessDao.getQueryDataTablesUnion(input);
        return data;
    }
    public void checkUnion(TpBussinessVO tpBussinessVO) {
        if (!tpBussinessDao.checkUnion(tpBussinessVO)) {
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_BUSSINESS_CODE);
        }
    }
    public TpBussinessVO getTpBussiness(String id) {
        TpBussinessVO tpBussinessVO = tpBussinessDao.getTpBussiness(id);
        List<TpBussinessTextVO> tpBussinessTexts = tpBussinessDao.getTpBussinessTexts(tpBussinessVO.getId());
        List<TpBussinessCorpVO> tpBussinessCorps = tpBussinessCorpService.getByBussnissId(tpBussinessVO.getId());
        StringBuilder corps=new StringBuilder();
        StringBuilder corpNames=new StringBuilder();
        if(!EmptyUtils.isEmpty(tpBussinessCorps)){
            for (int i = 0; i < tpBussinessCorps.size(); i++) {
                corps.append(tpBussinessCorps.get(i).getCorpId()).append(CommonConstant.COMMA_SPLIT);
                corpNames.append(tpBussinessCorps.get(i).getCorpName()).append(CommonConstant.COMMA_SPLIT);
            }
            tpBussinessVO.setCorpId(corps.toString().substring(0,corps.length()-1));
            tpBussinessVO.setCorpName(corpNames.toString().substring(0,corpNames.length()-1));
        }
        tpBussinessVO.setTpBussinessTextVOS(tpBussinessTexts);
        return tpBussinessVO;
    }
    public List<TpBussinessTextVO> getTpBussinessTextVOById(String id) {
        return tpBussinessDao.getTpBussinessTexts(id);
    }
    //修改商户信息
    public void updateTpBussiness(TpBussinessVO tpBussinessVO, ICurrentUser user) {
        if(EmptyUtils.isEmpty(tpBussinessVO.getTpBussinessTextVOS())){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPENO_LEASTONE);
        }
        TpBussiness tpBussiness = this.get(tpBussinessVO.getId(), true);
        tpBussinessVO.setUpdateBy(user.getLoginName());
        tpBussinessVO.setUpdateDate(DateUtils.getDate());
        tpBussinessVO.setStatus(95);
        BeanUtils.copyNotNullProperties(tpBussinessVO, tpBussiness);
        this.saveOrUpdate(tpBussiness);
        List<TpBussinessText> tpBussinessTexts = new ArrayList<TpBussinessText>();
        for (TpBussinessTextVO vo : tpBussinessDao.getTpBussinessTexts(tpBussinessVO.getId())) {
            //先删除所有的关联信息
            TpBussinessText tpBussinessText = new TpBussinessText();
            vo.setStatus(-2);
            vo.setTpBusinessId(tpBussiness.getId());
            BeanUtils.copyNotNullProperties(vo, tpBussinessText);
            tpBussinessTexts.add(tpBussinessText);
        }
        tpBussinessTextDao.batchUpdate(tpBussinessTexts);
        //再重新保存关联信息
        tpBussinessText(tpBussinessVO, user, tpBussiness);
        this.saveTpBussinessCorp(tpBussinessVO.getId(), tpBussinessVO.getCorpId());
    }
    //删除
    public void deleteTpBussiness(String id, ICurrentUser user) {
        TpBussinessVO tpBussiness = getTpBussiness(id);
        tpBussiness.setStatus(-2);
        tpBussiness.setUpdateBy(user.getLoginName());
        tpBussiness.setUpdateDate(DateUtils.getDate());
        TpBussiness entity = new TpBussiness();
        BeanUtils.copyNotNullProperties(tpBussiness, entity);
        this.updateEntity(entity);
        List<TpBussinessText> tpBussinessTexts = new ArrayList<TpBussinessText>();
        TpBussinessText text = new TpBussinessText();
        for (TpBussinessTextVO vo : tpBussiness.getTpBussinessTextVOS()) {
            vo.setStatus(-2);
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            text = tpBussinessTextDao.get(vo.getId());
            BeanUtils.copyNotNullProperties(vo, text);
            tpBussinessTexts.add(text);
        }
        this.delTpBussinessCorp(id);
        tpBussinessTextDao.batchUpdate(tpBussinessTexts);
    }
    //保存
    public void saveTpBussiness(TpBussinessVO tpBussinessVO, ICurrentUser user) {
        if(EmptyUtils.isEmpty(tpBussinessVO.getTpBussinessTextVOS())){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPENO_LEASTONE);
        }
        TpBussiness tpBussiness = new TpBussiness();
        tpBussinessVO.setCreateBy(user.getLoginName());
        tpBussinessVO.setCreateDate(DateUtils.getDate());
        tpBussinessVO.setStatus(95);
        BeanUtils.copyNotNullProperties(tpBussinessVO, tpBussiness);
        this.saveEntity(tpBussiness);
        tpBussinessText(tpBussinessVO, user, tpBussiness);
        this.saveTpBussinessCorp(tpBussiness.getId(), tpBussinessVO.getCorpId());
    }
    /**
     * 保存商户单位
     *
     * @param bussnissId
     * @param corpIds
     */
    public void saveTpBussinessCorp(String bussnissId, String corpIds) {
        List<TpBussinessCorpVO> oldCorpList = tpBussinessCorpService.getByBussnissId(bussnissId);
        if (!EmptyUtils.isEmptyList(oldCorpList)) {
            delTpBussinessCorp(bussnissId);
        }
        for (String corpId : corpIds.split(CommonConstant.COMMA_SPLIT)) {
            TpBussinessCorp tpBussinessCorp = new TpBussinessCorp();
            tpBussinessCorp.setBusinessId(bussnissId);
            tpBussinessCorp.setCorpId(corpId);
            this.tpBussinessCorpService.saveEntity(tpBussinessCorp);
        }
    }
    public void delTpBussinessCorp(String bussnissId) {
        this.tpBussinessCorpService.deleteTpBussnissId(bussnissId);
    }
    //保存关联表信息
    public void tpBussinessText(TpBussinessVO tpBussinessVO, ICurrentUser user, TpBussiness tpBussiness) {
        List<TpBussinessTextVO> tpBussinessTextVOs = tpBussinessVO.getTpBussinessTextVOS();
        if (!EmptyUtils.isEmpty(tpBussinessTextVOs)) {
            List<TpBussinessText> tpBussinessTexts = new ArrayList<TpBussinessText>();
            for (TpBussinessTextVO vo : tpBussinessTextVOs) {
                TpBussinessText tpBussinessText = new TpBussinessText();
                vo.setCreateBy(user.getLoginName());
                vo.setCreateDate(DateUtils.getDate());
                vo.setStatus(95);
                vo.setTpBusinessId(tpBussiness.getId());
                if (!EmptyUtils.isEmpty(vo.getPayeeMode()) && vo.getPayeeMode().equals("0")) {//登录号不能为空，与字典ttpay.payType对应
                    if (EmptyUtils.isEmpty(vo.getLoginNo())) {
                        throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_PAYTYPE_NULL);
                    }
                } else if (!EmptyUtils.isEmpty(vo.getPayeeMode()) && vo.getPayeeMode().equals("1")) {//手机号不能为空
                    if (EmptyUtils.isEmpty(vo.getPhone())) {
                        throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_PAYTYPE_NULL);
                    }
                } else if (!EmptyUtils.isEmpty(vo.getPayeeMode()) && vo.getPayeeMode().equals("2")) {//邮箱不能为空
                    if (EmptyUtils.isEmpty(vo.getEmail())) {
                        throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_PAYTYPE_NULL);
                    }
                }
                BeanUtils.copyNotNullProperties(vo, tpBussinessText);
                tpBussinessTexts.add(tpBussinessText);
            }
            tpBussinessTextDao.batchSave(tpBussinessTexts);
        }
    }
    /**
     * 第三方支付：查询收款方列表
     *
     * @param corpId  单位id
     * @param payType 支付方式（微信、支付宝）
     * @return 收款商户号列表
     */
    public List<TpBussinessTextVO> getBussinessinfo(String corpId, String payType) {
        List<TpBussinessTextVO> list=tpBussinessDao.getBussinessinfo(corpId, payType);
        for(Object datum : list){
            TpBussinessTextVO vo=(TpBussinessTextVO)datum;
            String payeeModeName=dictService.getByName("ttpay.payeeMode",vo.getPayeeMode());
            vo.setPayeeModeName(payeeModeName);
        }
        return list;
    }
    @Override
    public BaseDao<TpBussiness, String> getEntityDao() {
        return tpBussinessDao;
    }
}
