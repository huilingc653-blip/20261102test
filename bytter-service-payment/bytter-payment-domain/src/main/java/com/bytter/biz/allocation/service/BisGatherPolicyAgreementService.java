package com.bytter.biz.allocation.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.dao.BisGatherPolicyAgreementDao;
import com.bytter.biz.allocation.entity.BisGatherPolicyAccount;
import com.bytter.biz.allocation.entity.BisGatherPolicyAgreement;
import com.bytter.biz.allocation.vo.BisGatherPolicyAgreementVO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 内部资金归集管理协议签订
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/12
 */
@Service("bisGatherPolicyAgreementService")
@Transactional
public class BisGatherPolicyAgreementService extends BaseService<BisGatherPolicyAgreement, String> implements IUserExcelVerifyHandler<BisGatherPolicyAgreementVO> {
    @Autowired
    private BisGatherPolicyAgreementDao policyAgreementDao;
    @DubboReference(check = false)
    private ICorpService corpService;

    @Override
    public BaseDao<BisGatherPolicyAgreement, String> getEntityDao() {
        return policyAgreementDao;
    }

    public boolean checkCorpExist(String corpId, String id) {
        boolean flag = false;
        List<BisGatherPolicyAgreement> list = policyAgreementDao.getUniqueByProperty(corpId,id);
        if (ObjectUtil.isNotEmpty(list)){
            flag = true;
        }
        return flag;
    }

    public void save(BisGatherPolicyAgreementVO policyAgreementVO, ICurrentUser currentUser) {
        String[] corpIds = policyAgreementVO.getCorpId().split(CommonConstant.COMMA_SPLIT);
        for (String corpId : corpIds){
            CorpDTO corpDTO = corpService.get(corpId);
            BisGatherPolicyAgreement policyAgreement = new BisGatherPolicyAgreement();
            BeanUtils.copyNotNullProperties(policyAgreementVO,policyAgreement);
            policyAgreement.setCorpId(corpId);
            policyAgreement.setCorpName(corpDTO.getName());
            policyAgreement.setCreateBy(currentUser.getLoginName());
            policyAgreement.setCreateDate(new Date());
            policyAgreementDao.save(policyAgreement);
        }
    }

    public void modify(BisGatherPolicyAgreementVO policyAgreementVO, ICurrentUser currentUser) {
        String[] corpIds = policyAgreementVO.getCorpId().split(CommonConstant.COMMA_SPLIT);
        for (String corpId : corpIds){
            CorpDTO corpDTO = corpService.get(corpId);
            BisGatherPolicyAgreement policyAgreement = policyAgreementDao.get(policyAgreementVO.getId());
            BeanUtils.copyNotNullProperties(policyAgreementVO,policyAgreement);
            policyAgreement.setCorpId(corpId);
            policyAgreement.setCorpName(corpDTO.getName());
            policyAgreement.setUpdateBy(currentUser.getLoginName());
            policyAgreement.setUpdateDate(new Date());
            policyAgreementDao.update(policyAgreement);
        }
    }

    public void saveImport(List<BisGatherPolicyAgreementVO> list, ICurrentUser currentUser) {
        for (BisGatherPolicyAgreementVO policyAgreementVO : list){
            BisGatherPolicyAgreement policyAgreement = new BisGatherPolicyAgreement();
            BeanUtils.copyNotNullProperties(policyAgreementVO,policyAgreement);
            policyAgreement.setCreateBy(currentUser.getLoginName());
            policyAgreement.setCreateDate(new Date());
            policyAgreementDao.save(policyAgreement);
        }
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(BisGatherPolicyAgreementVO agreementVO, Object... user) {
        return null;
    }

    @Override
    public void verifyHandler(List<BisGatherPolicyAgreementVO> list, Object... user) {
        if (ObjectUtil.isNotEmpty(list)){
            for (BisGatherPolicyAgreementVO agreementVO : list){
                List<String> errMessage = new ArrayList<String>();
                if (EmptyUtils.isEmpty(agreementVO.getCorpName())){
                    errMessage.add("单位名称：不能为空！");
                }else{
                    CorpDTO corpDTO = corpService.getCorpIdByName(agreementVO.getCorpName());
                    if (ObjectUtil.isEmpty(corpDTO)) {
                        errMessage.add("单位名称："+agreementVO.getCorpName()+"，输入值不存在！");
                    }else{
                        if (checkCorpExist(corpDTO.getId(),"")){
                            errMessage.add("单位名称："+agreementVO.getCorpName()+"，已存在！不允许重复添加。");
                        }
                        agreementVO.setCorpId(corpDTO.getId());
                    }
                }
                if (ObjectUtil.isEmpty(agreementVO.getIsAgreementStr())){
                    errMessage.add("协议是否签订：不能为空！");
                }else{
                    if (!"是".equals(agreementVO.getIsAgreementStr()) && !"否".equals(agreementVO.getIsAgreementStr())){
                        errMessage.add("协议是否签订："+agreementVO.getIsAgreementStr()+"，输入值无效！");
                    }
                    if ("是".equals(agreementVO.getIsAgreementStr())){
                        agreementVO.setIsAgreement(CommonConstant.YES);
                    }
                    if ("否".equals(agreementVO.getIsAgreementStr())){
                        agreementVO.setIsAgreement(CommonConstant.NO);
                    }
                }
                agreementVO.addErrorMsg(errMessage);
            }
        }
    }

    public BisGatherPolicyAgreementVO getByCorpId(String corpId) {
        List<BisGatherPolicyAgreement> list = policyAgreementDao.getUniqueByProperty(corpId,"");
        if (ObjectUtil.isNotEmpty(list)){
            BisGatherPolicyAgreementVO vo = new BisGatherPolicyAgreementVO();
            BeanUtils.copyNotNullProperties(list.get(0),vo);
            return vo;
        }else{
            return null;
        }
    }
}
