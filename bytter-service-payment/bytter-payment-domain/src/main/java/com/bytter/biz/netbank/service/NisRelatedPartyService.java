package com.bytter.biz.netbank.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.dao.NisRelatedPartyDao;
import com.bytter.biz.netbank.entity.NisRelatedParty;
import com.bytter.biz.netbank.vo.NisRelatedPartyReportVO;
import com.bytter.biz.netbank.vo.NisRelatedPartyVO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Description: 关联方service
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/10/17
 */
@Service("nisRelatedPartyService")
public class NisRelatedPartyService extends BaseService<NisRelatedParty, String> implements IExcelVerifyHandler<NisRelatedPartyVO> {
    @Autowired
    private NisRelatedPartyDao nisRelatedPartyDao;
    @DubboReference(check = false)
    private ICorpService corpService;

    @Override
    public BaseDao<NisRelatedParty, String> getEntityDao() {
        return nisRelatedPartyDao;
    }

    @Transactional
    public void save(NisRelatedPartyVO vo, ICurrentUser currentUser) {
        // 校验code是否重复
        if (nisRelatedPartyDao.checkCode(vo.getRelatedCode(),null)){
            throw new ParameterException(NetbankMessageEnum.NETBANK_MESSAGE_RELATEDPARTY_UNIQUE);
        }

        NisRelatedParty entity = new NisRelatedParty();
        entity.setRelatedCode(vo.getRelatedCode());
        entity.setRelatedName(vo.getRelatedName());
        entity.setStatus(CommonConstant.STATUE_CHECK);
        entity.setCreateBy(currentUser.getLoginName());
        entity.setCreateDate(new Date());
        nisRelatedPartyDao.save( entity);

    }

    @Transactional
    public void update(NisRelatedPartyVO vo, ICurrentUser currentUser) {
        NisRelatedParty entity = nisRelatedPartyDao.get(vo.getId(), true);
        entity.setRelatedName(vo.getRelatedName());
        entity.setUpdateBy(currentUser.getLoginName());
        entity.setUpdateDate(new Date());
        nisRelatedPartyDao.update(entity);
    }

    @Transactional
    public void saveImport(List<NisRelatedPartyVO> list, ICurrentUser currentUser) {
        List<NisRelatedParty> entityList = new ArrayList<>();
        for (NisRelatedPartyVO vo : list){
            NisRelatedParty entity = new NisRelatedParty();
            entity.setRelatedCode(vo.getRelatedCode());
            entity.setRelatedName(vo.getRelatedName());
            entity.setStatus(CommonConstant.STATUE_CHECK);
            entity.setCreateBy(currentUser.getLoginName());
            entity.setCreateDate(new Date());
            entityList.add( entity);
        }
        nisRelatedPartyDao.batchSave(entityList);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisRelatedPartyVO obj) {
        return null;
    }

    @Override
    public void verifyHandler(List<NisRelatedPartyVO> list) {
        Map<String, String> map = new HashMap<>();
        for (NisRelatedPartyVO vo : list){
            List<String> errorList = new ArrayList<>();

            if (ObjectUtil.isEmpty(vo.getRelatedCode())){
                errorList.add("关联方编码不能为空");
            }else{
                if (map.containsKey(vo.getRelatedCode())){
                    errorList.add("关联方编码重复");
                }else{
                    if (nisRelatedPartyDao.checkCode(vo.getRelatedCode(), null)){
                        errorList.add("关联方编码重复");
                    }
                }
                map.put(vo.getRelatedCode(),vo.getRelatedCode());
            }

            if (ObjectUtil.isEmpty(vo.getRelatedName())){
                errorList.add("关联方名称不能为空");
            }

            vo.addErrorMsg(errorList);
        }
    }

    public DataTablesOutput<NisRelatedPartyReportVO> reportDatatable(DataTablesInput dataTablesInput, ICurrentUser currentUser) {
        String corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
        List<String> corpIds = new ArrayList<>();
        if (ObjectUtil.isEmpty(corpId)){
            corpIds = corpService.getCorpIdsByUserResource("report:netbank:relatedParty:query", currentUser.getId());
        }else{
            corpIds = Arrays.asList(corpId.split(","));
        }
        DataTablesOutput<NisRelatedPartyReportVO> dataTablesOutput = nisRelatedPartyDao.reportDatatable(dataTablesInput, corpIds);
        List<NisRelatedPartyReportVO> data = dataTablesOutput.getData();
        Integer indexNo = 1;
        for (NisRelatedPartyReportVO vo : data){
            vo.setIndexNo(indexNo);
            if(CommonConstant.NO.equals(vo.getCdSign())){
                vo.setCdSign("收入");
            }else{
                vo.setCdSign("支出");
            }
            indexNo++;
        }
        return dataTablesOutput;
    }
}
